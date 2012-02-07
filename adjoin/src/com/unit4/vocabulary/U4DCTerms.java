package com.unit4.vocabulary;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class U4DCTerms extends U4Vocabulary {
	
//	Class.
	public static final String PREFIX = "dct";
	public static final String NS = "http://purl.org/dc/terms/";
	
    public static final Property created = createProperty(getURI("created"));
    public static final Property creator = createProperty(getURI("creator"));
    public static final Property description = createProperty(getURI("description"));
    public static final Property licence = createProperty(getURI("licence"));
    public static final Property title = createProperty(getURI("title"));
    public static final Property triples = createProperty(getURI("triples"));
    
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
	
	public U4DCTerms() {
		super();
	}
	
	public U4DCTerms(OntResource subject) {
		super(subject);
	}

	public U4DCTerms(U4Vocabulary vocabulary) {
		super(vocabulary);
	}
	
	public String toString() {
		return String.format("U4DCTerms [%s]", getSubject());
	}
}
