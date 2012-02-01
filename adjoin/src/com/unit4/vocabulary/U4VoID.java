package com.unit4.vocabulary;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class U4VoID extends U4Vocabulary {
	
//	Class.
	
	public static final String PREFIX = "void";
	public static final String NS = "http://rdfs.org/ns/void#";
	
	public static final Resource Dataset = createResource(getURI("Dataset"));
	public static final Resource Linkset = createResource(getURI("Linkset"));

	public static final Property subset = createProperty(getURI("subset"));
	public static final Property rootResource = createProperty(getURI("rootResource"));
		
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
		return NS + fragment;
	}
	
	public static Set<U4VoID> getDatasets(OntModel model) {
		Set<U4VoID> datasets = new HashSet<U4VoID>();
		ResIterator iterator = model.listResourcesWithProperty(RDF.type, U4VoID.Dataset);
		while (iterator.hasNext()) {
			datasets.add(new U4VoID((OntResource) iterator.nextResource()));
		}
		return datasets;
	}
	
	public static Set<U4VoID> getSubsets(U4VoID dataset) {
		U4VoID subset;
		Set<U4VoID> subsets = new HashSet<U4VoID>();
		for (RDFNode node : dataset.getProperties(U4VoID.subset)) {
			subset = new U4VoID((OntResource) node.asResource());
			subsets.add(subset);
			subsets.addAll(U4VoID.getSubsets(subset));
		}
		return subsets;
	}
	
	public static Set<U4VoID> getRootResources(U4VoID dataset) {
		Set<U4VoID> resources = new HashSet<U4VoID>();
		for (U4VoID subset : U4VoID.getSubsets(dataset)) {
			resources.addAll(subset.getRootResources());
		}
		return resources;
	}
//	Instance.
	
	public U4VoID() {
	}
	
	public U4VoID(Resource subject) {
		setSubject(subject);
		common();
	}
	
	protected void common() {
		getModel().setNsPrefix(getPrefix(), getNS());
	}
	
//	Subset.
	
	public Boolean hasSubset() {
		return hasProperty(U4VoID.subset);
	}
	
	public Set<U4VoID> getSubsets() {
		U4VoID subset;
		Set<U4VoID> subsets = new HashSet<U4VoID>();
		for (RDFNode node : getProperties(U4VoID.subset)) {
			subset = new U4VoID((OntResource) node.asResource());
			subsets.add(subset);
		}
		return subsets;
	}
	
	public Boolean hasRootResource() {
		return hasProperty(U4VoID.rootResource);
	}
	
	public Set<U4VoID> getRootResources() {
		U4VoID resource;
		Set<U4VoID> resources = new HashSet<U4VoID>();
		for (RDFNode node : getProperties(U4VoID.rootResource)) {
			resource = new U4VoID((OntResource) node.asResource());
			resources.add(resource);
		}
		return resources;
	}
}