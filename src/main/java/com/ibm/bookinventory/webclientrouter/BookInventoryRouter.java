package com.ibm.bookinventory.webclientrouter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ibm.bookinventory.webclientHandler.BookInventoryHandler;
import static com.ibm.bookinventory.constants.BookInventoryConstants.BOOK_FUNCTIONAL_END_POINT_BOOKS;
import static com.ibm.bookinventory.constants.BookInventoryConstants.BOOK_FUNCTIONAL_END_POINT_BOOK;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class BookInventoryRouter {
	@Bean
	public RouterFunction<ServerResponse> BooksRoute(BookInventoryHandler bookInventoryHandler){
		System.out.println("enter in getBooks router" + bookInventoryHandler);
		return 
				RouterFunctions
				.route(GET(BOOK_FUNCTIONAL_END_POINT_BOOKS)
						.and(accept(MediaType.APPLICATION_JSON)),
						bookInventoryHandler::getBooks)
				.andRoute(GET(BOOK_FUNCTIONAL_END_POINT_BOOK + "/{isbn}")
				        .and(accept(MediaType.APPLICATION_JSON)),
				        bookInventoryHandler::getBook)
		        .andRoute(POST(BOOK_FUNCTIONAL_END_POINT_BOOK)
				        .and(accept(MediaType.APPLICATION_JSON)),
		                 bookInventoryHandler::saveBook)
		        .andRoute(PUT(BOOK_FUNCTIONAL_END_POINT_BOOK)
						.and(accept(MediaType.APPLICATION_JSON)),
				        bookInventoryHandler::updateBook)
		        .andRoute(DELETE(BOOK_FUNCTIONAL_END_POINT_BOOK+"/{isbn}")
						.and(accept(MediaType.APPLICATION_JSON)),
				        bookInventoryHandler::deleteBook);
	
	}
}
