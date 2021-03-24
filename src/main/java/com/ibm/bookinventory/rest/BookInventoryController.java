package com.ibm.bookinventory.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ibm.bookinventory.service.BookInventoryService;
import com.ibm.bookinventory.service.BookInventoryServiceWeb;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.ibm.bookinventory.model.BookData;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/bookinventory")
public class BookInventoryController {
	private final Logger logger = LoggerFactory.getLogger(BookInventoryController.class);

	@Autowired
	private BookInventoryService bookInventoryService;

	@Autowired
	private BookInventoryServiceWeb bookInventoryServiceWeb;

	public BookInventoryController() {
	}

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public ResponseEntity<?> getBooks() {
		Collection<BookData> books = this.bookInventoryService.getBooks();
		ResponseEntity<Collection<BookData>> responseEntity = new ResponseEntity<Collection<BookData>>(books,
				HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "/book/{isbn}", method = RequestMethod.GET)
	public ResponseEntity<?> getBook(@PathVariable(value = "isbn") String isbn) {
		// logger.info("Entered BookInventoryController.getBook(). isbn=" + isbn);
		BookData book = this.bookInventoryService.getBook(isbn);
		ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.OK);
		// logger.info("Leaving BookInventoryController.getBook(). isbn=" + isbn);
		return responseEntity;
	}

	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public ResponseEntity<?> createBook(@RequestBody BookData book) {
		logger.info("Entered BookInventoryController ************** POST");
		book = bookInventoryService.createBook(book);
		ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.CREATED);
		return responseEntity;
	}

	@RequestMapping(value = "/book", method = RequestMethod.PUT)
	public ResponseEntity<?> updateBook(@RequestBody BookData book) {
		logger.info("Entered BookInventoryController ************** PUT");
		book = bookInventoryService.updateOneBookData(book);
		ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.CREATED);
		return responseEntity;
	}

	@RequestMapping(value = "/book/{isbn}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBook(@PathVariable(value = "isbn") String isbn) {
		logger.info("Entered BookInventoryController ************** DELETE {} ", isbn);
		this.bookInventoryService.deleteBook(isbn);
		ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(HttpStatus.NO_CONTENT);
		return responseEntity;
	}

	// In this request url paging parameters are optional hence if client do not
	// provides these params then will use the
	// default values to make paging work.
	// http://localhost:9002/bookinventory/books/page?title=SpringBoot&page=0&size=2
	@RequestMapping(value = "/books/page", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllBooksPage(@RequestParam(required = false) String title,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
		Map<String, Object> response = this.bookInventoryService.findByTitleContainingIgnoreCase(title, page, size);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// http://localhost:9002/bookinventory/books/generic?title=SpringBoot&bookType=FICTION&isbn=7&author=Author_1&page=0&size=10
	// Generic Search with pagination
	@RequestMapping(value = "/books/generic", method = RequestMethod.GET)
	public ResponseEntity<?> getBooksByProperties(@RequestParam(required = false) String bookType,
			@RequestParam(required = false) String isbn, @RequestParam(required = false) String title,
			@RequestParam(required = false) String author, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		List<BookData> books = this.bookInventoryService.findBooksByProperties(bookType, isbn, title, author, page,
				size);

		logger.info("Entered BookInventoryController no of records = {}", books.size());

		ResponseEntity<Collection<BookData>> responseEntity = new ResponseEntity<Collection<BookData>>(books,
				HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "/book/isbn/{isbn}")
	public ResponseEntity<?> getBookByIsbn(@PathVariable(value = "isbn") String isbn) {
		BookData book = this.bookInventoryService.findBookByIsbn(isbn);
		ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "/book/author/{isbn}")
	public ResponseEntity<?> getAuthorByIsbn(@PathVariable(value = "isbn") String isbn) {
		BookData book = this.bookInventoryService.findAuthorByIsbn(isbn);
		ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.OK);
		return responseEntity;
	}

	// This is Dummy method to test the Library Service only
	@RequestMapping(value = "/hello")
	public ResponseEntity<?> sayBookInventoryHello() {
		// logger.info("Entered BookInventoryController.sayBookInventoryHello()");
		BookData book = new BookData("FICTION", "12346", "Dummy Author","Some Author, returns from Book Inventory Service.");
		ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.OK);
		// logger.info("Leaving BookInventoryController.sayBookInventoryHello()");
		return responseEntity;
	}

	// ReactiveMongoRepository
	//get all books
	
	@RequestMapping(value = "/wcbooks", method = RequestMethod.GET)
	public ResponseEntity<Flux<BookData>> getAll() {
    	Flux<BookData> books = this.bookInventoryServiceWeb.getAllBooksWeb();
    	HttpStatus status = books != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Flux<BookData>>(books, status);
	}
	
	//get book by Isbn
	@RequestMapping(value = "/wcbook/{isbn}")
	public Mono<ResponseEntity<BookData>> getOneBook(@PathVariable(value = "isbn") String isbn) {
		
		  return this.bookInventoryServiceWeb.getBookByIsbn(isbn)
				  .map((book) -> new ResponseEntity<>(book, HttpStatus.OK)) 
				  .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		  
	}
	
	//create book 
	@RequestMapping(value = "/wcbook" , method = RequestMethod.POST)
	public Mono<ResponseEntity<BookData>> createBookWeb(@Valid @RequestBody BookData bookWeb){
		return this.bookInventoryServiceWeb.createBookWeb(bookWeb)
				.map((book) -> new ResponseEntity<>(book,HttpStatus.CREATED));
				
                
	}
	
	//Update book
	@RequestMapping(value ="/wcbook/{isbn}" , method = RequestMethod.PUT)
	public Mono<ResponseEntity<BookData>> updateBookWeb(@RequestBody BookData book , @PathVariable (value = "isbn") String isbn){
		return this.bookInventoryServiceWeb.updateBookWeb(book, isbn)
				.map( (bookData) -> new ResponseEntity<>(book,HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
			
	}
	
	
    //Delete book
	@RequestMapping(value ="/wcbook/{isbn}" , method = RequestMethod.DELETE)
	public Mono<ResponseEntity<Void>> deleteBookWeb(@PathVariable (value="isbn") String isbn){
		return this.bookInventoryServiceWeb.deleteBookWeb(isbn)
				.map((book) -> new ResponseEntity<>(book, HttpStatus.OK));
	}
	
}
