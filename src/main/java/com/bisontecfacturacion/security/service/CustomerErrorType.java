package com.bisontecfacturacion.security.service;

public class CustomerErrorType {
	private String errorMessage;
	
	public CustomerErrorType(String error ) {
		this.errorMessage=error;
		
	}
	
	
	public String getErrorMessage(){
		return errorMessage;
	}
}
