package com.bisontecfacturacion.security.config;

public class CustomerErrorType {
	private String errorMessage;
	public CustomerErrorType(String error) {
		this.errorMessage=error;
	}
	public String getErrorMessage(){
		return errorMessage;
	}
}
