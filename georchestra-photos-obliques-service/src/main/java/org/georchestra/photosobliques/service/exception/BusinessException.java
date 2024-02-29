package org.georchestra.photosobliques.service.exception;

public class BusinessException extends AppServiceException {

	private static final long serialVersionUID = 1944465018746663664L;


	public BusinessException(String message) {
		super(message, AppServiceExceptionsStatus.BUSINESS_ERROR);
	}

}
