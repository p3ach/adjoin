package com.unit4.tabular;

import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.util.FileManager;
import com.unit4.adjoin.iati.XML.Element;

public class U4InputXML implements U4Input {
	private static Logger logger = LoggerFactory.getLogger(U4Input.class);

	private Stack<U4Element> stack = new Stack<U4Element>();
	
	public U4InputXML() {
	}
	
	public void parse(String uri, U4Common common) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(FileManager.get().open("189"), this);
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		U4Element element = new U4Element(qName, attributes);
		stack.push(element);
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		stack.peek().characters(new String(ch,start,length));
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		for (U4Element e : stack) {
			System.out.println(e.toString());
		}
		stack.pop();
	}
}
