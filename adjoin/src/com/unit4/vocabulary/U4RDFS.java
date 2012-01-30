package com.unit4.vocabulary;

import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class U4RDFS extends U4Vocabulary {
	
//	Class.
	public static final String PREFIX = "rdfs";
	public static final String NS = "http://www.w3.org/2000/01/rdf-schema#";

	public static final Resource Class = createResource(getURI("Class"));
    public static final Resource Datatype = createResource(getURI("Datatype"));
    
    public static final Resource Container  = createResource(getURI("Container"));
    
    public static final Resource ContainerMembershipProperty = createResource(getURI("ContainerMembershipProperty"));  
    
    public static final Resource Literal = createResource(getURI("Literal"));
    public static final Resource Resource = createResource(getURI("Resource"));

    public static final Property comment = createProperty(getURI("comment"));
    public static final Property domain = createProperty(getURI("domain"));
    public static final Property label = createProperty(getURI("label"));
    public static final Property isDefinedBy = createProperty(getURI("isDefinedBy"));
    public static final Property range = createProperty(getURI("range"));
    public static final Property seeAlso = createProperty(getURI("seeAlso"));
    public static final Property subClassOf  = createProperty(getURI("subClassOf"));
    public static final Property subPropertyOf  = createProperty(getURI("subPropertyOf"));
    public static final Property member  = createProperty(getURI("member"));
    
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
	
	public U4RDFS() {
		super();
	}
	
	public U4RDFS(OntResource subject) {
		super(subject);
	}

	public U4RDFS(U4Vocabulary vocabulary) {
		super(vocabulary);
	}
	
	public String toString() {
		return String.format("U4RDFS [%s]", getSubject());
	}
}
