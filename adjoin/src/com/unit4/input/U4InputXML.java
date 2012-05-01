package com.unit4.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hp.hpl.jena.graph.impl.Fragments.GetSlot;
import com.hp.hpl.jena.util.FileManager;
import com.unit4.cli.Options;
import com.unit4.tabular.U4Common;
import com.unit4.tabular.U4Row;
import com.unit4.xml.U4Attribute;
import com.unit4.xml.U4Element;

/**
 * U4SAX extends the SAX 2.0 DefaultHandler and implements the Unit4 tabular U4Input. 
 * @author dick
 *
 */
public class U4InputXML extends DefaultHandler implements U4Input {
//	Class.
	
	private static Logger logger = LoggerFactory.getLogger(U4InputXML.class);

	// Instance.
	
	private U4Common common;
	private Options options;
	private U4InputCallback callback;
	
	private Stack<U4Element> elements = new Stack<U4Element>();
	
	private Long index;

	public U4InputXML() {
		
	}
	
	@Override
	public U4Input setCommon(U4Common common) {
		this.common = common;
		return this;
	}
	
	@Override
	public U4Common getCommon() {
		return this.common;
	}

	@Override
	public void setOptions(Options options) {
		this.options = options;
	}

	@Override
	public Options getOptions() {
		return this.options;
	}

	@Override
	public U4Input setCallback(U4InputCallback callback) {
		this.callback = callback;
		return this;
	}
	
	@Override
	public U4InputCallback getCallback() {
		return this.callback;
	}
	
	public String getInputURI() {
		return (String) getOptions().getOption("inputURI");
	}
	
	public Stack<U4Element> getElements() {
		return this.elements;
	}
	
	public U4InputXML setIndex(Long index) {
		logger.trace("setIndex({})", index);
		this.index = index;
		return this;
	}
	
	public Long getIndex() {
		logger.trace("getIndex()={}", this.index);
		return this.index;
	}
	
	public Long incrementIndex() {
		setIndex(getIndex() + 1);
		return getIndex();
	}
	
	@Override
	public void parse() {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			setIndex((long) 0);
			getCommon().getRow().setIndex((long) 0);
			getCallback().header();
			sp.parse(FileManager.get().open(getInputURI()), this);
			getCallback().footer();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}		
	}
	
	/**
	 * SAX DefaultHandler startElement(...).
	 * Push the new Element onto the stack. 
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		logger.trace("startElement({})", qName);
		final U4Element element = new U4Element(qName, null, attributes);
		element.setIndex(incrementIndex());
		if (getIndex() > 1) { // i.e. it's not the first Element
			element.setParent(getElements().peek());
		}
		getElements().push(element);
	}
	
	/**
	 * SAX DefaultHandler characters(...).
	 * Peek the top Element from the stack and append the characters to the current characters.
	 * !!! Need to append as SAX may call characters multiple times per Element.   
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		logger.trace("characters(...)");
		final U4Element element = getElements().peek();
		final String characters = element.getCharacters();
		if (characters == null) {
			element.setCharacters(new String(ch,start,length).trim());
		} else {
			element.setCharacters(characters.concat(new String(ch,start,length).trim()));
		}
	}

	/**
	 * SAX DefaultHandler.endElement(...).
	 * Pop the top Element.
	 * Create the columns and values.
	 * Call U4InputCallback.beforeRow(),.row(),.afterRow().
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		logger.trace("endElement({})", qName);

		final U4Element element = getElements().pop();

		if (element.getIndex() > getMaxRows()) {
			return;
		}
		
		final List<U4Attribute> attributes = element.getAttributes();
		
		final List<String> columns = new ArrayList<String>(attributes.size() + 2); // Attributes size + 1 for characters + 1 for parent.
		for (U4Attribute attribute : attributes) {
			columns.add("[" + qName + "]" + attribute.getQName());
		}

		final List<String> values = new ArrayList<String>(attributes.size() + 2); // Attributes size + 1 for characters + 1 for parent.
		for (U4Attribute attribute : attributes) {
			values.add(attribute.getValue());
		}

		if (element.hasCharacters()) {
			columns.add("[" + qName + "]");
			values.add(element.getCharacters());
		}

		final U4Element parent = element.getParent();
		if (parent != null) {
			columns.add("[]Parent");
			values.add(parent.getIndex().toString());
		}

		final U4Common common = getCommon();
		common.getColumns().setColumns(columns);
		common.getRow().setIndex(element.getIndex()); // Use the Element index to set the row index.
		common.getRow().setValues(values);

		final U4InputCallback callback = getCallback();
		callback.beforeRow();
		callback.row();
		callback.afterRow();
	}
	
	protected Long getMaxRows() {
		final Options options = getOptions();
		if (options.hasOption("inputMaxRows")) {
			return Long.valueOf((String) options.getOption("inputMaxRows"));
		} else {
			return Long.MAX_VALUE - 1;
		}
	}
	
	@Override
	public String toString() {
		return String.format("U4InputXML inputURI[%s]", getInputURI()); 
	}
}
