package com.unit4.vocabulary;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class U4FOAF extends U4Vocabulary {
	
//	Class.
	public static final String PREFIX = "foaf";
	public static final String NS = "http://xmlns.com/foaf/0.1/";
	
//    public static final Resource Alt = createResource(getURI("Alt"));

    public static final Property homepage = createProperty(getURI("homepage"));
		
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
	
	public U4FOAF() {
		super();
	}
	
	public U4FOAF(OntResource subject) {
		super(subject);
	}

	public U4FOAF(U4Vocabulary vocabulary) {
		super(vocabulary);
	}
	
	public String toString() {
		return String.format("U4FOAF [%s]", getSubject());
	}
}
