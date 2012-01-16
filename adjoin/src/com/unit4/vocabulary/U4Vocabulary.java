package com.unit4.vocabulary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.FileManager;
import com.unit4.exception.Exception;

/**
 * Provides the heavy lifting between a vocabulary and the underlying model.
 * @author Dick
 *
 */
public class U4Vocabulary {

//	Class.

	private static Logger logger = LoggerFactory.getLogger(U4Vocabulary.class);
	
	public static final String DEFAULT_ID_UNIT4_URI = "http://id.unit4.com";
	
	public static final String ID_UNIT4 = "http://id.unit4.com";
	
	public static final String DEFAULT_PREFIX = "example";
	public static final String DEFAULT_NS = "http://id.example.org/ns/vocabulary#";
	
	public static String getPrefix() {
		return DEFAULT_PREFIX;
	}
	
	public static String getNS() {
		return DEFAULT_NS;
	}

	public static String getURI(String fragment) {
		return getNS() + fragment;
	}
	
	protected static Resource createResource(String uri) {
		return ResourceFactory.createResource(uri);
	}
	
	protected static Property createProperty(String uri) {
		return ResourceFactory.createProperty(uri);
	}
	
	//	Instance.	
	
	private Resource subject = null;
		
	public U4Vocabulary() {
		logger.debug("U4Vocabulary()");
	}

	public U4Vocabulary(Resource subject) {
		setSubject(subject);
	}
	
	public U4Vocabulary(U4Vocabulary vocabulary) {
		setSubject(vocabulary.getSubject());
	}
	
	public U4Vocabulary readModel(String uri) {
		try {
			FileManager.get().readModel(getModel(), uri);
		} catch (JenaException e) {
			logger.error("Unable to read {} due to [{}]", uri, e);
		}
		return this;
	}
	
	public Model getModel() {
		return getSubject().getModel();
	}
	
	public void setSubject(Resource subject) {
		this.subject = subject;
	}
	
	public Resource getSubject() {
		return this.subject;
	}
	
//	Child methods.
	
	public String getChildURI(String urn) {
		return getSubject().getURI() + "/" + urn;
	}

	public Boolean hasChild(String urn) {
		return getModel().contains(createChild(urn), (Property) null, (RDFNode) null);
	}
	
	public Resource createChild(String urn) {
		return getModel().createResource(getChildURI(urn));
	}
	
//	Create(add)Read(get)Update(?)Delete(remove) CRUD methods.
	
	public U4Vocabulary addProperty(Property property, RDFNode object) {
		getSubject().addProperty(property, object);
		return this;
	}
	
	public U4Vocabulary addProperty(String urn, Property property, RDFNode object) {
		createChild(urn).addProperty(property, object);
		return this;
	}
	
	public U4Vocabulary addString(Property property, String object) {
		getSubject().addLiteral(property, object);
		return this;
	}
	
	public RDFNode getProperty(Property property) {
		Statement statement = getSubject().getProperty(property);
		return (statement == null ? null : statement.getObject()); 
	}

	public Resource getResource(Property property) {
		logger.debug("getResource(property={}", property);
		RDFNode node = getProperty(property);
		if (node == null || !node.isResource()) {
			throw new Exception("Node is not a Resource.");
		} else {
			return node.asResource();
		}
	}
	
	public List<Resource> getResources(Property property) {
		List<Resource> nodes = new ArrayList<Resource>();
		StmtIterator iterator = getSubject().listProperties(property);
		while (iterator.hasNext()) {
			nodes.add(iterator.next().getObject().asResource());
		}
		return nodes;
	}

	public Integer getInteger(Property property) {
		RDFNode node = getProperty(property);
		if (node == null || !node.isLiteral()) {
			return null;
		} else {
			return node.asLiteral().getInt();
		}
	}
	
	public String getString(Property property) {
		RDFNode node = getProperty(property);
		if (node == null || !node.isLiteral()) {
			return null;
		} else {
			return node.asLiteral().getString();
		}
	}
	
	public Set<RDFNode> getProperties(Property property) {
		Set<RDFNode> nodes = new HashSet<RDFNode>();
		StmtIterator iterator = getSubject().listProperties(property);
		while (iterator.hasNext()) {
			nodes.add(iterator.next().getObject());
		}
		return nodes;
	}
	
	public U4Vocabulary addLiteral(Property property, Object object) {
		getSubject().addLiteral(property, object);
		return this;
	}

	public U4Vocabulary removeProperty(Property property) {
		getSubject().removeAll(property);
		return this;
	}
	
//	Has methods.

	public Boolean hasProperty(Property property) {
		return getSubject().hasProperty(property);
	}
	
	public Boolean hasProperty(Property property, RDFNode object) {
		return getSubject().hasProperty(property, object);
	}
	
	public Boolean hasProperty(String urn, Property property) {
		return (createChild(urn).hasProperty(property));
	}
	
	public Boolean hasProperty(String urn, Property property, RDFNode object) {
		return (createChild(urn).hasProperty(property, object));
	}
	
//	Override methods.
	
	public String toString() {
		return String.format("U4Vocabulary [%s]", getSubject());
	}
}
