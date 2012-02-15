package com.unit4.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class U4IATI extends U4Vocabulary {
	
//	Class.
	public static final String PREFIX = "iati";
	public static final String NS = "http://iatistandard.org/def/iati#";
	
	public static final Resource Activity = createResource(getURI("Activity"));
	public static final Resource ContainerElements = createResource(getURI("ContainerElements"));
	public static final Resource FileHeader = createResource(getURI("FileHeader"));

    public static final Property version = createProperty(getURI("version"));
		
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
	
	public U4IATI() {
		super();
	}
	
	public U4IATI(String uri) {
		super(uri);
	}
	
	public U4IATI(Resource subject) {
		super(subject);
	}

	public U4IATI(U4Vocabulary vocabulary) {
		super(vocabulary);
	}
	
	public String toString() {
		return String.format("U4IATI [%s]", getSubject());
	}
}
