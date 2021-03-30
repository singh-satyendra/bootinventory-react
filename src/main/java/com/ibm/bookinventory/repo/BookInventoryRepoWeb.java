package com.ibm.bookinventory.repo;

import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.ResponseEntity;

import com.ibm.bookinventory.model.BookData;

import reactor.core.publisher.Mono;
public interface BookInventoryRepoWeb extends ReactiveMongoRepository<BookData, String>{
	
	Mono<BookData> findByIsbn(String isbn);
	
	Mono<Void> deleteByIsbn(String isbn);

	Mono<Void> save(Mono<BookData> book);
}
