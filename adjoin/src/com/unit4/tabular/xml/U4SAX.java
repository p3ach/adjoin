package com.unit4.tabular.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.velocity.runtime.parser.node.GetExecutor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hp.hpl.jena.util.FileManager;
import com.unit4.tabular.U4Common;
import com.unit4.tabular.U4Input;

/**
 * U4SAX extends the SAX 2.0 DefaultHandler and implements the Unit4 tabular U4Input. 
 * @author dick
 *
 */
public class U4SAX extends DefaultHandler implements U4Input {

	// Instance.
	
	private U4Common common;
	
	private Stack<U4Element> elements = new Stack<U4Element>();

	public U4SAX() {
		
	}
	
	public void setCommon(U4Common common) {
		this.common = common;
	}
	
	public U4Common getCommon() {
		return this.common;
	}
	
	public Stack<U4Element> getElements() {
		return this.elements;
	}
	
	public void parse(String uri, U4Common common) {
		setCommon(common);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(FileManager.get().open(uri), this);
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}		
	}
	
	public void row(U4Common common) {
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
			names.addAll(e.getValues());
		}

		getElements().pop();
		getCommon().getColumns().setColumns(names);
		getCommon().getRow().setValues(values);
		row(getCommon());
	}
}