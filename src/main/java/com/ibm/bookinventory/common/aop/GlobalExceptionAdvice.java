package com.ibm.bookinventory.common.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.mongodb.MongoTimeoutException;
import org.springframework.dao.DuplicateKeyException;

/**
 * This class could be used as a global exception handler, if any exception is thrown by the system then it can handle it 
 * with the help of below handlers.
 *  
 * @author 
 *
 */
@ControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionAdvice.class);
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<?> handleGeneralExceptions(Exception e, WebRequest request) {
		LOGGER.info("handleGeneralExceptions, inside."+e.getClass());
		ErrorResponseMessage oErrorDetails = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(oErrorDetails, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler({DuplicateKeyException.class})
	public final ResponseEntity<?> handleDBAccessException(DuplicateKeyException e, WebRequest request) {
		LOGGER.info("handleDuplicateKeyException, inside."+e.getClass());
		int httpStatusCode = HttpStatus.BAD_REQUEST.value();
		String httpErrorMessage = "Duplicate key";
		String errorMessage = "Data already exists in MongoDB.";
		ErrorResponseMessage oErrorDetails = new ErrorResponseMessage(httpStatusCode, httpErrorMessage, errorMessage, request.getDescription(false));
		return new ResponseEntity<>(oErrorDetails, new HttpHeaders(), HttpStatus.valueOf(httpStatusCode));
	}

	@ExceptionHandler({MongoTimeoutException.class})
	public final ResponseEntity<?> handleDBAccessException(MongoTimeoutException e, WebRequest request) {
		LOGGER.info("handleMongoTimeoutException, inside."+e.getClass());
		int httpStatusCode = HttpStatus.REQUEST_TIMEOUT.value();
		String httpErrorMessage = "Timed out";
		String errorMessage = "Server is unreachable.";
		ErrorResponseMessage oErrorDetails = new ErrorResponseMessage(httpStatusCode, httpErrorMessage, errorMessage, request.getDescription(false));
		return new ResponseEntity<>(oErrorDetails, new HttpHeaders(), HttpStatus.valueOf(httpStatusCode));
	}
}
