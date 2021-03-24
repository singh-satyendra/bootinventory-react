package com.ibm.bookinventory.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.ibm.bookinventory.model.BookData;

@Repository
public class BookInventoryDALImpl implements BookInventoryDAL {

	private final MongoTemplate mongoTemplate;

	@Autowired
	public BookInventoryDALImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public void deleteByIsbn(String isbn) {

		Query query = new Query(Criteria.where("isbn").is(isbn));
		mongoTemplate.remove(query, BookData.class);
	}
	
	
	@Override
	public BookData saveBookData(BookData book) {
		mongoTemplate.save(book);
		return book;
	}
	
	@Override
	public List<BookData> getAllBookData() {
		return mongoTemplate.findAll(BookData.class);
	}

	@Override
	public List<BookData> getAllBooksPaginated(int pageNumber, int pageSize) {
		Query query = new Query();
		query.skip(pageNumber * pageSize);
		query.limit(pageSize);

		return mongoTemplate.find(query, BookData.class);
	}
	
	
	@Override
	public BookData findOneByAuthor(String author) {
	   Query query = new Query();
	   query.addCriteria(Criteria.where("author").is(author));

	   return mongoTemplate.findOne(query, BookData.class);
	}

	@Override
	public List<BookData> findByAuthor(String author) {
	   Query query = new Query();
	   query.addCriteria(Criteria.where("author").is(author));

	   return mongoTemplate.find(query, BookData.class);
	}

	
	@Override
	public void updateMultipleBookDataTitle() {
	   Query query = new Query();
	   Update update = new Update().rename(null, null);
	   mongoTemplate.findAndModify(query, update, BookData.class);
	}

	@Override
	public BookData updateOneBookData(BookData bookData) {
	   Query query = new Query();
	   query.addCriteria(Criteria.where("isbn").is(bookData.getIsbn()));
	   
	   BookData bookDataFromDB = mongoTemplate.findOne(query, BookData.class);
	   
	   if (null != bookDataFromDB) {
		   bookDataFromDB.setBookType(bookData.getBookType());
		   bookDataFromDB.setTitle(bookData.getTitle());
		   bookDataFromDB.setAuthor(bookData.getAuthor());
		   mongoTemplate.save(bookDataFromDB);
	   }
	   else {
		   throw new RuntimeException("Resource not found");
	   }
	   return bookDataFromDB;
	}
	
	
	@Override 
	public void deleteBookData(BookData book) {
		mongoTemplate.remove(book); 
	}
	
	//Methods to be used for generic search with pagination
	@Override 
	public List<BookData> findBooksByProperties(String bookType, String isbn, String title, String author, int pageNo, int pageSize) {
		Pageable page = PageRequest.of(pageNo, pageSize);
		final Query query = new Query().with(page);
		final List<Criteria> criteria = new ArrayList<>();
		if (bookType != null && !bookType.isEmpty()) {
			criteria.add(Criteria.where("bookType").is(bookType));
		}
	     
	    if (isbn != null && !isbn.isEmpty()) {
	    	criteria.add(Criteria.where("isbn").is(isbn));
	    }
	    
	    if (title != null && !title.isEmpty()) {
	    	criteria.add(Criteria.where("title").is(title));
	    }
	    
	    if (author != null && !author.isEmpty()) {
	    	criteria.add(Criteria.where("author").is(author));
	    }
	     
	    if (!criteria.isEmpty()) {
	    	query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
	    }
	    return mongoTemplate.find(query, BookData.class);
	}
	
	
}
