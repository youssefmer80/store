package com.store.rest.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.store.exception.CategoryNotFoundException;
import com.store.exception.ClientErrorInformation;
import com.store.exception.ProductNotFoundException;


@ControllerAdvice
public class RestErrorHandlerAdvice {
	
	@ExceptionHandler({ProductNotFoundException.class,CategoryNotFoundException.class})
	public ResponseEntity<ClientErrorInformation> rulesForProductOrCategoryNotFoundException(
			Exception e, HttpServletRequest req) {
		
		String url = req.getRequestURI();
		String errorMessage = e.toString().split(":")[1].trim();
		
		ClientErrorInformation clientErrorInformation = new ClientErrorInformation(errorMessage,url);
		return new ResponseEntity<ClientErrorInformation>(clientErrorInformation,HttpStatus.NOT_FOUND);
	}


}
