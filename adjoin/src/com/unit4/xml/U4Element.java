package com.unit4.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

public class U4Element {
	private String qName;
	private String characters;
	private List<U4Attribute> attributes = new ArrayList<U4Attribute>();
	
	public U4Element(String qName, Attributes attributes) {
		setQName(qName);
		setAttributes(attributes);
	}
	
	public void setQName(String qName) {
		this.qName = qName;
	}
	
	public String getQName() {
		return this.qName;
	}
	
	public void setCharacters(String characters) {
		this.characters = characters;
	}
	
	public String getCharacters() {
		return this.characters;
	}
	
	public void setAttributes(Attributes attributes) {
		for (int index = 0; index < attributes.getLength(); index++) {
			getAttributes().add(new U4Attribute(attributes.getQName(index), attributes.getValue(index)));
		}
	}
	
	public List<U4Attribute> getAttributes() {
		return this.attributes;
	}
	
	public List<String> getColumns() {
		List<String> columns = new ArrayList<String>(getAttributes().size() + 1);
		for (U4Attribute attribute : getAttributes()) {
			columns.add("[" + getQName() + "]" + attribute.getQName());
		}
		if (getCharacters() != null) {
			columns.add("[" + getQName() + "]");
		}
		return columns;
	}
	
	public List<String> getValues() {
		List<String> values = new ArrayList<String>(getAttributes().size() + 1);
		for (U4Attribute attribute : getAttributes()) {
			values.add(attribute.getValue());
		}
		if (getCharacters() != null) {
			values.add(getCharacters());
		}
		return values;
	}
	
	public String toString() {
		return getQName();
	}
}