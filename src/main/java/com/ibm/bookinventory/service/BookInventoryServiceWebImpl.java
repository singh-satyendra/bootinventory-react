package com.ibm.bookinventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.bookinventory.model.BookData;
import com.ibm.bookinventory.repo.BookInventoryRepoWeb;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookInventoryServiceWebImpl implements BookInventoryServiceWeb {

	@Autowired
	private BookInventoryRepoWeb bookInventoryReopWeb;

	@Override
	public Mono<BookData> getBookByIsbn(String isbn) {
		return bookInventoryReopWeb.findByIsbn(isbn);
	}

	@Override
	public Mono<BookData> createBookWeb(BookData book) {
		return bookInventoryReopWeb.save(book);
	}

	@Override
	public Mono<BookData> updateBookWeb(BookData book, String isbn) {
		return bookInventoryReopWeb.findByIsbn(isbn).flatMap(bookData -> {
			bookData.setBookType(book.getBookType());
			bookData.setAuthor(book.getAuthor());
			bookData.setTitle(book.getTitle());
			return bookInventoryReopWeb.save(bookData);
		});
	}
	
	@Override
	public Mono<Void> deleteBookWeb(String isbn){
		return bookInventoryReopWeb.deleteByIsbn(isbn);
	}
	
	@Override
	public Flux<BookData> getAllBooksWeb(){
		return bookInventoryReopWeb.findAll();
	}
	
}
