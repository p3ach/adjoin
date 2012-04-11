package com.unit4.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit4.tabular.U4Row;

public class U4Attribute {
	private static Logger logger = LoggerFactory.getLogger(U4Attribute.class);
	
	private String qName;
	private String value;
	
	public U4Attribute() {
		
	}
	
	public U4Attribute(String qName, String value) {
		setQName(qName);
		setValue(value);
	}
	
	public void setQName(String qName) {
		logger.trace("setQName({})", qName);
		this.qName = qName;
	}
	
	public String getQName() {
		return this.qName;
	}
	
	public void setValue(String value) {
		logger.trace("setValue({})", value);
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
