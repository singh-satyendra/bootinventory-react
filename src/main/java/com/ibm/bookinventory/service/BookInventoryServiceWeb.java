package com.ibm.bookinventory.service;


import com.ibm.bookinventory.model.BookData;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookInventoryServiceWeb {

	Mono<BookData> getBookByIsbn(String isbn);
	
	Mono<BookData> createBookWeb(BookData book);
	
	Mono<BookData> updateBookWeb(BookData book);
	
	Mono<Void> deleteBookWeb(String isbn);
	
	Flux<BookData> getAllBooksWeb();
 }
