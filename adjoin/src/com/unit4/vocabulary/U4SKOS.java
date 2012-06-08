package com.unit4.vocabulary;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class U4SKOS extends U4Vocabulary {
	
//	Class.
	
//	public static final Resource Dataset = createResource(getURI("Dataset"));

	public static final Property broader = createProperty(getURI("broader"));
	public static final Property narrower = createProperty(getURI("narrower"));
	public static final Property member = createProperty(getURI("member"));
		
	public static String getPrefix() {
		return "skos";
	}
	
	public static String getNS() {
		return "http://www.w3.org/2004/02/skos/core#";
	}
	
	public static void setNsPrefix(Model model) {
		model.setNsPrefix(getPrefix(), getNS());
	}

	public static String getURI(String fragment) {
		return getNS() + fragment;
	}
	
	protected static U4SKOS from(Object object) {
		if (object instanceof RDFNode) {
			return new U4SKOS((OntResource) object);
		} else {
			return null;
		}
	}
	
	protected static Set<U4SKOS> from(Set<?> objects) {
		Set<U4SKOS> skos = new HashSet<U4SKOS>(objects.size());
		for (Object object : objects) {
			skos.add(from(object));
		}
		return skos;
	}
	
	//	Instance.
	
	public U4SKOS() {
		super();
		common();
	}

	public U4SKOS(U4Vocabulary vocabulary) {
		super(vocabulary);
		common();
	}
	
	public U4SKOS(OntResource subject) {
		setSubject(subject);
		common();
	}
	
	protected void common() {
		getModel().setNsPrefix(getPrefix(), getNS());
	}
	
//	Broader.
	
	public Boolean hasBroader() {
		return hasProperty(U4SKOS.broader);
	}
	
	public U4SKOS addBroader(String urn) {
		addProperty(U4SKOS.broader, createChild(urn));
		return this;
	}
	
//	Narrower.
	
	public Boolean hasNarrower() {
		return hasProperty(U4SKOS.narrower);
	}
	
	public U4SKOS addNarrower(String urn) {
		addProperty(U4SKOS.narrower, createChild(urn));
		return this;
	}
	
	public String toString() {
		return String.format("U4SKOS [%s]", getSubject());
	}
}
