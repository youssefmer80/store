package com.store.exception;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientErrorInformation {
	
	private String message;
	private String url;
	
	public ClientErrorInformation() {
		super();

	}
	
	public ClientErrorInformation(String message, String url) {
		super();
		this.message = message;
		this.url = url;
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
