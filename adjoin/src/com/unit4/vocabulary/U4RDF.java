package com.unit4.vocabulary;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class U4RDF extends U4Vocabulary {
	
//	Class.
	public static final String PREFIX = "rdf";
	public static final String NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
    public static final Resource Alt = createResource(getURI("Alt"));
    public static final Resource Bag = createResource(getURI("Bag") );
    public static final Resource Property = createResource(getURI("Property"));
    public static final Resource Seq = createResource(getURI("Seq"));
    public static final Resource Statement = createResource(getURI("Statement"));
    public static final Resource List = createResource(getURI("List"));
    public static final Resource nil = createResource(getURI("nil"));

    public static final Property first = createProperty(getURI("first"));
    public static final Property rest = createProperty(getURI("rest"));
    public static final Property subject = createProperty(getURI("subject"));
    public static final Property predicate = createProperty(getURI("predicate"));
    public static final Property object = createProperty(getURI("object"));
    public static final Property type = createProperty(getURI("type"));
    public static final Property value = createProperty(getURI("value"));
		
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
	
	public U4RDF() {
		super();
	}
	
	public U4RDF(OntResource subject) {
		super(subject);
	}

	public U4RDF(U4Vocabulary vocabulary) {
		super(vocabulary);
	}
	
	public String toString() {
		return String.format("U4RDF [%s]", getSubject());
	}
}
