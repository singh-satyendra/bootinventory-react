package com.ibm.bookinventory.webclientHandler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ibm.bookinventory.model.BookData;
import com.ibm.bookinventory.repo.BookInventoryRepoWeb;

import reactor.core.publisher.Mono;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class BookInventoryHandler {

	private final BookInventoryRepoWeb bookInventoryRepoweb;
	private final Logger logger = LoggerFactory.getLogger(BookInventoryHandler.class);

	static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

	public BookInventoryHandler(BookInventoryRepoWeb bookInventoryRepoWeb) {
		this.bookInventoryRepoweb = bookInventoryRepoWeb;
	}

	public Mono<ServerResponse> getBooks(ServerRequest serverRequest) {
		 return ServerResponse.ok()
	                          .contentType(MediaType.APPLICATION_JSON)
	                          .body(bookInventoryRepoweb.findAll(), BookData.class);

	}

	public Mono<ServerResponse> getBook(ServerRequest serverRequest) {
	String isbn = serverRequest.pathVariable("isbn");
        Mono<BookData> bookMono = bookInventoryRepoweb.findByIsbn(isbn);

        return bookMono.flatMap(book ->
        ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(book), BookData.class)))
        .switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> deleteBook(ServerRequest serverRequest) {
		
		String isbn = serverRequest.pathVariable("isbn");
	        Mono<Void> deleteBook = bookInventoryRepoweb.deleteByIsbn(isbn);

	        return ServerResponse.ok()
	                             .contentType(MediaType.APPLICATION_JSON)
	                             .body(deleteBook, Void.class);
	}

	public Mono<ServerResponse> saveBook(ServerRequest serverRequest) {
		  Mono<BookData> createBook = serverRequest.bodyToMono(BookData.class);
		  
		  return createBook.flatMap(item -> ServerResponse.ok()
		                                                  .contentType(MediaType.APPLICATION_JSON)
		                                                  .body(bookInventoryRepoweb.save(item), BookData.class));
		  
		 
	}

	
	
	
	public Mono<ServerResponse> updateBook(ServerRequest serverRequest) {

       // String isbn = serverRequest.pathVariable("isbn");

      //  int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        Mono<BookData> updatedbook = serverRequest.bodyToMono(BookData.class)
                .flatMap((books) -> {

                    Mono<BookData> bookMono = bookInventoryRepoweb.findByIsbn(books.getIsbn())
                            .flatMap(bookData -> {
                            	bookData.setBookType(books.getBookType());
                				bookData.setAuthor(books.getAuthor());
                				bookData.setTitle(books.getTitle());
                                return bookInventoryRepoweb.save(bookData);

                            });
                    return bookMono;
                });

        return updatedbook.flatMap(book ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(Mono.just(book), BookData.class)))
                .switchIfEmpty(notFound);


    }

}
