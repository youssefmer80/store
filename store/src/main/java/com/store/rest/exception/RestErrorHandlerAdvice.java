package com.store.rest.exception;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.store.exception.CategoryNotFoundException;
import com.store.exception.ClientErrorInformation;
import com.store.exception.ProductNotFoundException;

@ControllerAdvice
public class RestErrorHandlerAdvice {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler({ ProductNotFoundException.class,
			CategoryNotFoundException.class })
	public ResponseEntity<ClientErrorInformation> rulesForProductOrCategoryNotFoundException(
			Exception e, HttpServletRequest req) {

		String url = req.getRequestURI();
		String errorMessage = e.toString().split(":")[1].trim();

		ClientErrorInformation clientErrorInformation = new ClientErrorInformation(
				errorMessage, url);
		return new ResponseEntity<ClientErrorInformation>(
				clientErrorInformation, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ClientErrorInformation> processValidationError(
			MethodArgumentNotValidException ex, HttpServletRequest req) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();

		return processFieldErrors(fieldErrors, req);
	}

	private ResponseEntity<ClientErrorInformation> processFieldErrors(
			List<FieldError> fieldErrors, HttpServletRequest req) {
		ClientErrorInformation clientErrorInformation = new ClientErrorInformation();
		StringBuffer message = new StringBuffer();

		for (FieldError fieldError : fieldErrors) {
			String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
			message.append(localizedErrorMessage);
			message.append(System.getProperty("line.separator"));
			// dto.addFieldError(fieldError.getField(), localizedErrorMessage);
		}

		clientErrorInformation.setMessage(message.toString());
		clientErrorInformation.setUrl(req.getRequestURI());

		return new ResponseEntity<ClientErrorInformation>(
				clientErrorInformation, HttpStatus.BAD_REQUEST);
	}

	private String resolveLocalizedErrorMessage(FieldError fieldError) {

		Locale currentLocale = LocaleContextHolder.getLocale();
		String localizedErrorMessage = messageSource.getMessage(fieldError,
				currentLocale);

		// If the message was not found, return the most accurate field error
		// code instead.
		// You can remove this check if you prefer to get the default error
		// message.
		if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
			String[] fieldErrorCodes = fieldError.getCodes();
			localizedErrorMessage = fieldErrorCodes[0] + " " + localizedErrorMessage;
		}

		return localizedErrorMessage;
	}

}
