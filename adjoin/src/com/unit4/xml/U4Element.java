package com.unit4.xml;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import com.unit4.tabular.U4Row;

public class U4Element {
	private static Logger logger = LoggerFactory.getLogger(U4Element.class);
	
	private String qName;
	private String characters;
	private List<U4Attribute> attributes = new ArrayList<U4Attribute>();

	private Long index;
	private U4Element parent;
	
	public U4Element() {
		
	}
	
	public U4Element(String qName, String characters, Attributes attributes) {
		setQName(qName);
		setCharacters(characters);
		setAttributes(attributes);
	}
	
	public void setQName(String qName) {
		logger.trace("setQName({})", qName);
		this.qName = qName;
	}
	
	public String getQName() {
		return this.qName;
	}
	
	public void setCharacters(String characters) {
		logger.trace("setCharacters({})", characters);
		this.characters = characters;
	}
	
	public Boolean hasCharacters() {
		return (this.characters != null);
	}
	
	public String getCharacters() {
		logger.trace("getCharacters({})", this.characters);
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
	
	public U4Element setIndex(Long index) {
		logger.trace("setIndex({})", index);
		this.index = index;
		return this;
	}
	
	public Long getIndex() {
		logger.trace("getIndex()={}", this.index);
		return this.index;
	}
	
	public U4Element setParent(U4Element parent) {
		this.parent = parent;
		return this;
	}
	
	public U4Element getParent() {
		return this.parent;
	}
	
	public String toString() {
		return getQName();
	}
}