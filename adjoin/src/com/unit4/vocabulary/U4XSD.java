package com.unit4.vocabulary;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.XSD;

public class U4XSD extends U4Vocabulary {
	
//	Class.
	public static final String PREFIX = "xsd";
	public static final String NS = "http://www.w3.org/2001/XMLSchema#";
	
    public static final Resource string = XSD.xstring;
    public static final Resource date = XSD.date;
    public static final Resource integer = XSD.integer;
		
	public static String getPrefix() {
		return PREFIX;
	}
	
	public static String getNS() {
		return NS;
	}

	public static void setNsPrefix(Model model) {
		model.setNsPrefix(getPrefix(), getNS());
	}
	
	public static String getURI(String fragment) {
		return getNS() + fragment;
	}
	
	//	Instance.
	
	public U4XSD() {
		super();
	}
	
	public U4XSD(OntResource subject) {
		super(subject);
	}

	public U4XSD(U4Vocabulary vocabulary) {
		super(vocabulary);
	}
	
	public String toString() {
		return String.format("U4XSD [%s]", getSubject());
	}
}
