package com.unit4.tabular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class U4Input {
	private static Logger logger = LoggerFactory.getLogger(U4Input.class);
	
	String uri = null;
	
	public U4Input() {
		
	}
	
	public void setURI(String uri) {
		this.uri = uri;
	}
	
	public String getURI() {
		return this.uri;
	}
}
