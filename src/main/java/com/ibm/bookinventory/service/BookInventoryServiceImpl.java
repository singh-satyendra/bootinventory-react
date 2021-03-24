package com.ibm.bookinventory.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.bookinventory.repo.BookInventoryDAL;
import com.ibm.bookinventory.repo.BookInventoryRepo;
import com.ibm.bookinventory.model.BookData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class BookInventoryServiceImpl implements BookInventoryService {
	
	private final Logger logger = LoggerFactory.getLogger(BookInventoryServiceImpl.class);
	@Autowired
	private BookInventoryRepo bookInventoryRepo;
	
	@Autowired
	BookInventoryDAL bookInventoryDAL;
	

	public BookInventoryServiceImpl() {
	}
	
	@Override
	public BookData getBook(String isbn) {
		//logger.info("Entered BookInventoryServiceImpl.getBook(). isbn=" + isbn);
		BookData book = this.bookInventoryRepo.findByIsbn(isbn);
		//logger.info("Exiting BookInventoryServiceImpl.getBook(). isbn=" + isbn);
		return book;
	}

	@Override
	public Collection<BookData> getBooks() {
		//logger.info("Entered BookInventoryServiceImpl.getBooks()");
		Collection<BookData> books = this.bookInventoryRepo.findAll();
		//logger.info("Exiting BookInventoryServiceImpl.getBooks()");
		return books;
	}
	

	@Override
	public BookData createBook(BookData book) {
		//logger.info("Entered BookInventoryServiceImpl.createBook(). isbn : {}",book.getIsbn());
		book = this.bookInventoryRepo.save(book);
		//logger.info("Exiting BookInventoryServiceImpl.createBook(). isbn : {} ",book.getIsbn());
		return book;
	}
	
	
	@Override
	public BookData updateOneBookData(BookData book) {
		book = this.bookInventoryDAL.updateOneBookData(book);
	    return book;
	}

	@Override
	public void deleteBook(String isbn) {
		this.bookInventoryRepo.deleteByIsbn(isbn);
	}
	
	@Override
	public Map<String, Object> findByTitleContainingIgnoreCase(String title, int pageNo, int pageSize) {
		List<BookData> bookDataList = new ArrayList<BookData>();
		Pageable paging = null;
		
	
		Page<BookData> pageBookData;
		if (title == null) {
			//logger.info("Entered findByTitleContainingIgnoreCase title == null ");
			paging = PageRequest.of(pageNo, pageSize);
			pageBookData = bookInventoryRepo.findAll(paging);
		}
		else {
			//Sort authorSort = Sort.by(sortByAuthor); 
			//Sort titleSort = Sort.by(sortByTitle); 
			//Sort groupBySort = authorSort.and(titleSort); 
			
			Sort titleSort = Sort.by(Sort.Direction.ASC, title);
			paging = PageRequest.of(pageNo, pageSize, titleSort);
			pageBookData = bookInventoryRepo.findByTitleContainingIgnoreCase(title, paging);
		}
		
		bookDataList = pageBookData.getContent();
		
		Map<String, Object> response = new HashMap<>();
		response.put("books", bookDataList);
		response.put("currentPage", pageBookData.getNumber());
		response.put("totalItems", pageBookData.getTotalElements());
		response.put("totalPages", pageBookData.getTotalPages());
	
		return response;
	}
	
	@Override
	public List<BookData> findBooksByProperties(String bookType, String isbn, String title, String author, int pageNo, int pageSize) {
		return bookInventoryDAL.findBooksByProperties(bookType, isbn, title, author, pageNo, pageSize);
	}

	
	@Override
	public BookData findBookByIsbn(String isbn) {
		return this.bookInventoryRepo.findBookByIsbn(isbn);
	}
	
	@Override
	public BookData findAuthorByIsbn(String isbn) {
		return this.bookInventoryRepo.findAuthorByIsbn(isbn);
	}
	
	
	private BookData createandupdateBook(BookData book) {
		
		BookData bookDataFromDB = this.bookInventoryRepo.findByIsbn(book.getIsbn());
		if(null != bookDataFromDB) {
			bookDataFromDB.setBookType(book.getBookType());
			bookDataFromDB.setTitle(book.getTitle());
			bookDataFromDB.setAuthor(book.getAuthor());
			book = this.bookInventoryRepo.save(bookDataFromDB);
		}
		else {
			book = this.bookInventoryRepo.save(book);
		}
		return book;
	}

	
}