package com.ibm.bookinventory.repo;

import java.util.List;
import com.ibm.bookinventory.model.BookData;

//define a data layer interface with all possible methods for the application
public interface BookInventoryDAL {
	void deleteByIsbn(String isbn);
	BookData saveBookData(BookData book);
	List<BookData> getAllBookData();
	List<BookData> getAllBooksPaginated(int pageNumber, int pageSize);
	BookData findOneByAuthor(String author);
	List<BookData> findByAuthor(String author);
	void updateMultipleBookDataTitle();
	BookData updateOneBookData(BookData bookData);
	void deleteBookData(BookData bookData);
	
	List<BookData> findBooksByProperties(String bookType, String isbn, String title, String author, int pageNo, int pageSize);
}