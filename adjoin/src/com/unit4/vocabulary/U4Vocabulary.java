package com.unit4.vocabulary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Alt;
import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Container;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.unit4.exception.Exception;

/**
 * Provides the heavy lifting between a vocabulary and the underlying model.
 * @author Dick
 *
 */
public class U4Vocabulary {

//	Class.

	private static Logger logger = LoggerFactory.getLogger(U4Vocabulary.class);
	
	public static final String DEFAULT_UNIT4_URI = "http://id.unit4.com";
	public static final String DEFAULT_PREFIX = "example";
	public static final String DEFAULT_NS = "http://id.example.org/ns/vocabulary#";
	
	public static String getPrefix() {
		return DEFAULT_PREFIX;
	}
	
	public static String getNS() {
		return DEFAULT_NS;
	}
	
	public static void setNsPrefix(Model model) {
	}

	public static String getURI(String fragment) {
		return getNS() + fragment;
	}
	
	public static Resource createResource(String uri) {
		return ResourceFactory.createResource(uri);
	}
	
	public static Property createProperty(String uri) {
		return ResourceFactory.createProperty(uri);
	}
	
	//	Instance.	
	
	private Model model;
	private Resource subject;
		
	public U4Vocabulary() {
	}
	
	public U4Vocabulary(String uri) {
		this(ModelFactory.createDefaultModel().createResource(uri));
	}

	public U4Vocabulary(Resource subject) {
		setSubject(subject);
	}
	
	public U4Vocabulary(U4Vocabulary vocabulary) {
		setSubject(vocabulary.getSubject());
	}
	
	public U4Vocabulary readModel(String uri) {
		try {
			if (FileManager.get().readModel(getModel(), uri) == null) {
				throw new Exception(String.format("Unable to read %s.", uri));
			}
		} catch (JenaException e) {
			throw new Exception(String.format("Unable to read %s.", uri), e);
		}
		return this;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public void setSubject(Resource subject) {
		this.subject = subject;
		setModel(subject.getModel());
	}
	
	public Resource getSubject() {
		return this.subject;
	}
	
//	Child methods.
	
	public String getChildURI(String urn) {
		final String getChildURI = getSubject().getURI() + "/" + urn;
		logger.trace("getChildURI({})={}", urn,getChildURI);
		return getChildURI;
	}

	public Boolean hasChild(String urn) {
		final Boolean hasChild = getModel().contains(createChild(urn), (Property) null, (RDFNode) null); 
		logger.trace("hasChild({})={}", urn,hasChild);
		return hasChild; 
	}
	
	public Resource createChild(String urn) {
		logger.debug("createChild(urn={})", urn);
		Resource createChild = getModel().createResource(getChildURI(urn)); 
		return createChild;
	}
	
//	Anonymous
	
	public Boolean isAnonymous() {
		Boolean isAnonymous = getSubject().isAnon();
		logger.trace("isAnonymous() = {}", isAnonymous);
		return isAnonymous;
	}
	
//	Container/Alt/Bag/Seq

	/**
	 * Test if this U4Vocabulary has a (S, rdf:type, rdf:Alt|rdf:Bag|rdf:Seq).
	 * @return
	 */
	public Boolean hasContainer() {
		Boolean hasContainer = (hasAlt() || hasBag() || hasSeq());
		logger.trace("hasContainer() = {}", hasContainer);
		return hasContainer;
	}
	
	public Container getContainer() {
		if (hasAlt()) {
			return getAlt();
		} else if (hasBag()) {
			return getBag();
		} else if (hasSeq()) {
			return getSeq();
		} else {
			throw new Exception(String.format("No container found for %s", getSubject().toString()));
		}
	}
	
	public Boolean hasAlt() {
		return hasProperty(RDF.type, RDF.Alt);
	}
	
	public Alt getAlt() {
		return getModel().getAlt(getSubject());
	}

	public Boolean hasBag() {
		return hasProperty(RDF.type, RDF.Bag);
	}
	
	public Bag getBag() {
		return getModel().getBag(getSubject());
	}
	
	public Boolean hasSeq() {
		return hasProperty(RDF.type, RDF.Seq);
	}
	
	public Seq getSeq() {
		return getModel().getSeq(getSubject());
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
		RDFNode node = getProperty(property);
		if (node == null) {
			throw new Exception("Node is null.");
		} else if (node.isResource()) {
			return node.asResource();
		} else {
			throw new Exception("Node is not a Resource.");
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
		if (node == null) {
			throw new Exception("Node is null.");
		} else if (node.isLiteral()) {
			return node.asLiteral().getInt();
		} else {
			throw new Exception("Node is not a Literal.");
		}
	}
	
	public String getString(Property property) {
		logger.debug("getProperty({})", property);
		final RDFNode node = getProperty(property);
		if (node == null) {
			throw new Exception(String.format("Node is null for %s %s.", getSubject(), property));
		} else if (node.isLiteral()) {
			final String getString = node.asLiteral().getString();
			logger.trace("={}", getString);
			return getString;
		} else {
			throw new Exception("Node is not a Literal.");
		}
	}
	
	public String getString(Property property, String value) {
		logger.debug("getProperty({},{})", property, value);
		final RDFNode node = getProperty(property);
		if (node == null) {
			return value;
		} else if (node.isLiteral()) {
			final String getString = node.asLiteral().getString(); 
			logger.trace("={}", getString);
			return getString;
		} else {
			throw new Exception("Node is not a Literal.");
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
//		return getModel().contains(getSubject(), property);
		Boolean hasProperty = getSubject().hasProperty(property);
		logger.trace("hasProperty({}) = {}", property.toString(), hasProperty.toString());
		return hasProperty;
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
	
//	Other get...(s).

	public List<Statement> getStatements(Property property, RDFNode node) {
		List<Statement> statements = new ArrayList<Statement>();
		StmtIterator iterator = getModel().listStatements(getSubject(), property, node);
		while (iterator.hasNext()) {
			statements.add(iterator.nextStatement());
		}
		return statements;
	}
	
//	Override methods.
	
	public String toString() {
		return String.format("U4Vocabulary [%s]", getSubject());
	}
}
