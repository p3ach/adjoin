package com.unit4.tabular.xml;

public class U4Attribute {
	private String qName;
	private String value;
	
	public U4Attribute() {
		
	}
	
	public U4Attribute(String qName, String value) {
		setQName(qName);
		setValue(value);
	}
	
	public void setQName(String qName) {
		this.qName = qName;
	}
	
	public String getQName() {
		return this.qName;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
