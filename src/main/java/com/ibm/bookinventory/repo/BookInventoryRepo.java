package com.ibm.bookinventory.repo;

import com.ibm.bookinventory.model.BookData;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookInventoryRepo extends MongoRepository<BookData, Long> {
	
	BookData findByIsbn(String isbn);
    void deleteByIsbn(String isbn);
    
    @Query("{'isbn' : ?0}")
	BookData findBookByIsbn(String isbn);
 
	@Query(value = "{'isbn' : ?0}", fields = "{'author' : 1}")
	BookData findAuthorByIsbn(String isbn);
	
	//Not Used
	List<BookData> findByAuthorLike(String author);

 	
	/** Paging  and Sorting **PagingAndSortingRepository **/
	// Find all Books which title containing input string (case insensitive)
	Page<BookData> findByTitleContainingIgnoreCase(String title, Pageable pageable);
	
	//Not Used
	/** interface Page extends Slice. Use it if you don’t need the total number of items and total pages. */
	Slice<BookData> findByAuthor(String author, Pageable pageable);
	 
	  
}
