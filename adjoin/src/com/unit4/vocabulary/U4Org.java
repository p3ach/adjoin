package com.unit4.vocabulary;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class U4Org extends U4Vocabulary {
	
//	Class.
	public static final String PREFIX = "org";
	public static final String NS = "http://www.w3.org/ns/org#";

	public static final Resource Organisation = createResource(getURI("Organisation"));
	public static final Resource FormalOrganisation = createResource(getURI("FormalOrganisation"));

	public static final Property organisation = createProperty(getURI("organisation"));
		
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
	
	public U4Org() {
		super();
	}
	
	public U4Org(OntResource subject) {
		super(subject);
	}

	public U4Org(U4Vocabulary vocabulary) {
		super(vocabulary);
	}
	
	public String toString() {
		return String.format("U4Org [%s]", getSubject());
	}
}
