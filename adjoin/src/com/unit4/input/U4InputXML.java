package com.unit4.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hp.hpl.jena.util.FileManager;
import com.unit4.tabular.U4Common;
import com.unit4.xml.U4Element;

/**
 * U4SAX extends the SAX 2.0 DefaultHandler and implements the Unit4 tabular U4Input. 
 * @author dick
 *
 */
public class U4InputXML extends DefaultHandler implements U4Input {

	// Instance.
	
	private U4Common common;
	private String uri;
	private U4InputCallback callback;
	
	private Stack<U4Element> elements = new Stack<U4Element>();

	public U4InputXML() {
		
	}
	
	public U4Input setCommon(U4Common common) {
		this.common = common;
		return this;
	}
	
	public U4Common getCommon() {
		return this.common;
	}

	@Override
	public U4Input setURI(String uri) {
		this.uri = uri;
		return this;
	}
	
	@Override
	public String getURI() {
		return this.uri;
	}
	
	public U4Input setCallback(U4InputCallback callback) {
		this.callback = callback;
		return this;
	}
	
	public U4InputCallback getCallback() {
		return this.callback;
	}
	
	public Stack<U4Element> getElements() {
		return this.elements;
	}

	@Override
	public void parse() {
		parse(getURI());
	}

	public void parse(String uri) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			getCommon().getRow().setIndex((long) 0);
			getCallback().header();
			sp.parse(FileManager.get().open(uri), this);
			getCallback().footer();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}		
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		getElements().push(new U4Element(qName, attributes));
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		getElements().peek().setCharacters(new String(ch,start,length));
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		List<String> names = new ArrayList<String>();
		for (U4Element e : getElements()) {
			names.addAll(e.getColumns());
		}
		List<String> values = new ArrayList<String>();
		for (U4Element e : getElements()) {
			values.addAll(e.getValues());
		}
		getElements().pop();

		getCommon().getColumns().setColumns(names);
		
		getCommon().getRow().setIndex(getCommon().getRow().getIndex()+1);
		getCommon().getRow().setValues(values);
		
		getCallback().beforeRow();
		getCallback().row();
		getCallback().afterRow();
	}
}
