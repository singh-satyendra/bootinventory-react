package com.ibm.bookinventory.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.ibm.bookinventory.model.BookData;

public interface BookInventoryService {
	Collection<BookData> getBooks();
	BookData getBook(String isbn);
	
	//Collection<BookData> getBooksByTitle(String isbn);
	BookData createBook(BookData book);
	void deleteBook(String isbn);
	BookData updateOneBookData(BookData bookData);
	
	BookData findBookByIsbn(String isbn);
	BookData findAuthorByIsbn(String isbn);
	
	/** Paging  and Sorting **PagingAndSortingRepository **/
	//List<BookData> findAllBooks(Integer pageNo, Integer pageSize);
	//List<BookData> findByBookType(String bookType, Integer pageNo, Integer pageSize,);
	Map<String, Object> findByTitleContainingIgnoreCase(String title, int pageNo, int pageSize);
	
	//Methods to be used for generic search with pagination 
	List<BookData> findBooksByProperties(String bookType, String isbn, String title, String author, int pageNo, int pageSize);

	
	//List<BookData> getAllBooksSorted(Integer pageNo, Integer pageSize, String sortBy);

	
}

