package com.ibm.bookinventory.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.ResponseEntity;

import com.ibm.bookinventory.model.BookData;

import reactor.core.publisher.Mono;
public interface BookInventoryRepoWeb extends ReactiveMongoRepository<BookData, String>{
	
	Mono<BookData> findByIsbn(String isbn);
	
	Mono<Void> deleteByIsbn(String isbn);
}
