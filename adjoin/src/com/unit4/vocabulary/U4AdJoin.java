package com.unit4.vocabulary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.vocabulary.XSD;
import com.unit4.exception.U4Exception;

/**
 * Provides the heavy lifting for converting an input into RDF output based on an AdJoin template.
 * @author dick
 *
 */
public class U4AdJoin extends U4Vocabulary {
	
//	Class.
	
	private static Logger logger = LoggerFactory.getLogger(U4AdJoin.class);
	
	public static final String PREFIX = "adjoin";
	public static final String NS = "http://id.unit4.com/2011/11/01/adjoin-ns#";
	
	public static final String DEFAULT_REGEX_URI = "http://www.unit4.com/id/U4Convert";
	public static final Resource DEFAULT_REGEX = createResource(DEFAULT_REGEX_URI);
	
	public static final String DEFAULT_COLUMN_URN = "Column";
	
	public static final Resource AdJoin = createResource(getURI("AdJoin"));
	
	public static final Resource Null = createResource(getURI("Null"));
	
	public static final Resource Unknown = createResource(getURI("Unknown"));
	
	public static final Property priority = createProperty(getURI("priority"));
	
	public static final Property pattern = createProperty(getURI("pattern"));
	
	public static final Property test = createProperty(getURI("test"));
	
	public static final Property subjectURI = createProperty(getURI("subjectURI"));
	public static final Property propertyURI = createProperty(getURI("propertyURI"));
	public static final Property objectType = createProperty(getURI("objectType"));
	public static final Property objectValue = createProperty(getURI("objectValue"));
	public static final Property objectDatatype = createProperty(getURI("objectDatatype"));
	public static final Property objectLanguage = createProperty(getURI("objectLanguage"));

	public static final Property triples = createProperty(getURI("triples"));
	public static final Property triple = createProperty(getURI("triple"));
	
	public static final Property values = createProperty(getURI("values"));
	public static final Property value = createProperty(getURI("value"));
	
//	public static final Property if = createProperty(getURI("if"));
	
	public static final Property before = createProperty(getURI("before"));
	public static final Property after = createProperty(getURI("after"));

	public static final Property statements = createProperty(getURI("statements"));
	public static final Property statement = createProperty(getURI("statement"));

	public static final String DEFAULT_OBJECT_TYPE = U4RDFS.Resource.getURI();

	public static final String HEADER_URN = "Header";
	public static final String FOOTER_URN = "Footer";
	public static final String BEFORE_ROW_URN = "BeforeRow";
	public static final String AFTER_ROW_URN = "AfterRow";
	public static final String ROW_URN = "Row";
	public static final String COLUMNS_URN = "Columns";
	public static final String COLUMN_URN = "Column";
	
	public static U4AdJoin match(List<U4AdJoin> templates, String input) {
		logger.debug("match({},{})", templates, input);
		if (templates != null) {
			for (U4AdJoin item : templates) {
				if (item.match(input)) {
					logger.trace("Matched {}", item);
					return item;
				}
			}
		}
		logger.trace("No match.");
		return null;
	}
	
	public static RDFNode getObject(String object, Resource type, RDFNode datatype) {
		if (type.equals(U4RDFS.Resource)) {
			return ResourceFactory.createResource(object.replaceAll("[^a-zA-Z0-9:/_-]", "_")); 
		} else if (type.equals(U4RDFS.Literal)) {
			if (datatype.equals(U4AdJoin.Null)) {
				return ResourceFactory.createPlainLiteral(object);
			} else if (datatype.equals(XSD.xstring)) {
				return ResourceFactory.createTypedLiteral(object, XSDDatatype.XSDstring);
			} else if (datatype.equals(XSD.xlong)) {
				return ResourceFactory.createTypedLiteral(object, XSDDatatype.XSDlong);
			} else if (datatype.equals(XSD.decimal)) {
				return ResourceFactory.createTypedLiteral(object, XSDDatatype.XSDdecimal);
			} else {
				throw new U4Exception(String.format("Datatype %s is unknown.", datatype.toString()));
			}
		} else {
			throw new U4Exception("Type is unknown.");
		}
	}
	
	public static List<U4AdJoin> getChildren(List<U4AdJoin> listOfU4Converts) {
		logger.debug("getChildren()");
		List<U4AdJoin> children = new ArrayList<U4AdJoin>();
		for (U4AdJoin convert : listOfU4Converts) {
			logger.trace("convert {}", convert.toString());
			children.addAll(convert.getChildren());
		}
		return children;
	}
	
//	Instance.
	
//	U4ConvertRegEx
	public static final Property regexPattern = createProperty(getURI("regexPattern"));
	
	public static String getPrefix() {
		return U4AdJoin.PREFIX;
	}
	
	public static String getNS() {
		return U4AdJoin.NS;
	}
	
	public static String getURI(String fragment) {
		return getNS() + fragment;
	}

	public static void setNsPrefix(Model model) {
		model.setNsPrefix(getPrefix(), getNS());
	}
	
//	public static U4Convert create() {
//		return new U4Convert(ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF).createOntResource("http://id.unit4.com/" + UUID.randomUUID().toString()));
//	}
	
	public static List<U4AdJoin> create(Set<RDFNode> nodes, Boolean order) {
		logger.debug("create(nodes={}, order={})", nodes, order);
		List<U4AdJoin> list = new ArrayList<U4AdJoin>();
		for (RDFNode node : nodes) {
			list.add(new U4AdJoin(node.asResource()));
		}
		if (order) {
			Collections.sort(list, new U4ConvertComparator());
		}
		return list;
	}
	
//	Instance.
	
//	protected U4SKOS u4SKOS;
	
	public U4AdJoin() {
	}
	
	public U4AdJoin(Resource subject) {
		super(subject);
		common();
	}
	
	protected void common() {
		getModel().setNsPrefix(getPrefix(), getNS());
	}
	
	public U4AdJoin getChild(String urn) {
		return new U4AdJoin(createChild(urn));
	}	

	public Boolean hasValues() {
		return hasProperty(U4AdJoin.values);
	}
	
	public U4AdJoin getValues() {
		return new U4AdJoin(getResource(U4AdJoin.values));
	}

	public Boolean hasValue() {
		return hasProperty(U4AdJoin.value);
	}
	
	public U4AdJoin getValue() {
		return new U4AdJoin(getResource(U4AdJoin.value));
	}

//	public Boolean hasIf() {
//		return hasProperty(U4AdJoin.if);
//	}
//	
//	public U4AdJoin getIf() {
//		return new U4AdJoin(getResource(U4AdJoin.if));
//	}
	
	/**
	 * Answer whether this AdJoin has an adjoin:statement property.
	 * @return
	 */
	public Boolean hasStatement() {
		return hasProperty(U4AdJoin.statement);
	}
	
	/**
	 * Return the adjoin:statement object as a U4AdJoin.
	 * @return
	 */
	public U4AdJoin getStatement() {
		return new U4AdJoin(getResource(U4AdJoin.statement));
	}

	/**
	 * Answer all the statements for this AdJoin.
	 * For each statement callback with <code>this</code>.
	 * This will walk the statement hierachy.
	 * @param callback A U4AdJoinCallback instance.
	 */
	public void getStatements(U4AdJoinCallback callback) {
		if (hasBefore()) {
			for (U4AdJoin before : getBefore()) {
				before.getStatements(callback);
			}
		}
		
		if (isAnonymous()) { // i.e. [...];
			if (hasContainer()) { // i.e. rdf:Alt|Bag|Seq.
				NodeIterator iterator = getContainer().iterator();
				while (iterator.hasNext()) {
					new U4AdJoin(iterator.next().asResource()).getStatements(callback);
				}
			} else {
				if (hasStatement()) {
					getStatement().getStatements(callback);
				} else {
					callback.callback(this);
				}
			}
		} else {
			if (hasStatement()) {
				new U4AdJoin(getStatement().getSubject()).getStatements(callback);
			}
		}

		if (hasAfter()) {
			for (U4AdJoin after : getAfter()) {
				after.getStatements(callback);
			}
		}
	}

	public Boolean hasChildren() {
		return hasProperty(U4SKOS.narrower);
	}
	
	public List<U4AdJoin> getChildren() {
		List<U4AdJoin> children = new ArrayList<U4AdJoin>();
		for (U4AdJoin child : U4AdJoin.create(getProperties(U4SKOS.narrower), true)) {
			children.add(child);
			children.addAll(child.getChildren());
		}
		return children;
	}

	/**
	 * Returns all the U4Convert(s) below this U4Convert (i.e. SKOS.narrower) ordered by priority.
	 * @return
	 */
	public List<U4AdJoin> getAllSteps() {
		List<U4AdJoin> steps = new ArrayList<U4AdJoin>();
		U4AdJoin step;
		Boolean added;
		for (RDFNode node : getProperties(U4SKOS.narrower)) {
			step = new U4AdJoin((OntResource) node);
			added = steps.add(step);
			logger.trace("{}", added);
			steps.addAll(step.getAllSteps());
		}
		return steps;
	}

//	public U4Convert addStep(String urn) {
//		U4Convert step = new U4Convert(addChild(urn));
//		step.createProperty(U4SKOS.broader, getSubject());
//		createProperty(U4SKOS.narrower, step.getSubject());
//		return step;
//	}
	
//	Values.
	
	public U4AdJoin setPriority(Integer priority) {
		removeProperty(U4AdJoin.priority);
		addLiteral(U4AdJoin.priority, priority);
		return this;
	}
	
	public Integer getPriority() {
		return getInteger(U4AdJoin.priority);
	}

	
	public Boolean hasTest() {
		return hasProperty(U4AdJoin.test);
	}
	
	public String getTest() {
		return getString(U4AdJoin.test);
	}
	
	public Boolean hasSubjectURI() {
		return hasProperty(U4AdJoin.subjectURI);
	}
	
	public String getSubjectURI() {
		return getString(U4AdJoin.subjectURI);
	}
	
	public String getPropertyURI() {
		return getString(U4AdJoin.propertyURI);
	}
	
	/**
	 * Answer the objectType for this AdJoin defaulting to RDFS.Resource.
	 * @return The objectType.
	 */
	public String getObjectType() {
		return getString(U4AdJoin.objectType, DEFAULT_OBJECT_TYPE);
	}

	public Boolean hasObjectValue() {
		return hasProperty(U4AdJoin.objectValue);
	}
	
	public String getObjectValue() {
		return getString(U4AdJoin.objectValue);
	}
	
	public Boolean hasObjectDatatype() {
		return hasProperty(U4AdJoin.objectDatatype);
	}
	
	public String getObjectLanguage() {
		return getString(U4AdJoin.objectLanguage);
	}
	
	public Boolean hasObjectLanguage() {
		return hasProperty(U4AdJoin.objectLanguage);
	}
	
	public String getObjectDatatype() {
		return getString(U4AdJoin.objectDatatype);
	}
	
	public Boolean hasPattern() {
		return hasProperty(U4AdJoin.pattern);
	}
	
	public String getPattern() {
		return getString(U4AdJoin.pattern);
	}
	
	public Boolean hasBefore() {
		return hasProperty(U4AdJoin.before);
	}
	
	public List<U4AdJoin> getBefore() {
		return getSeqOrResource(getResource(U4AdJoin.before));
	}
	
	public Boolean hasAfter() {
		return hasProperty(U4AdJoin.after); 
	}
	
	public List<U4AdJoin> getAfter() {
		return getSeqOrResource(getResource(U4AdJoin.after));
	}

	public List<U4AdJoin> getSeqOrResource(Resource resource) {
		final List<U4AdJoin> list = new ArrayList<U4AdJoin>();
		
		if (resource.isAnon()) {
			final Seq seq = getModel().getSeq(resource);
			final NodeIterator iterator = seq.iterator();
			while (iterator.hasNext()) {
				list.add(new U4AdJoin(iterator.next().asResource()));
			}
		} else {
			list.add(new U4AdJoin(resource));
		}
		
		return list;
	}
	
//	Match.
	public Boolean match(String input) {
		logger.debug("match({})", input);
		final Boolean match;
		if (hasPattern()) {
			final String pattern = getPattern();
			if (pattern == null) {
				match = false;
			} else {
				if (input == null) {
					match = false;
				} else {
					match =  Pattern.matches(pattern, input);
				}
			}
		} else {
			match = false;
		}
		logger.trace("={}", match);
		return match;
	}

	// Template.
	
	public Boolean hasColumns(String urn) {
		return hasTemplate(urn + "/" + COLUMNS_URN);
	}

	public List<U4AdJoin> getColumns(String urn) {
		return getTemplate(urn + "/" + COLUMNS_URN);
	}
	
	public Boolean hasColumn(String urn) {
		return hasTemplate(urn + "/" + COLUMN_URN);
	}

	public List<U4AdJoin> getColumn(String urn) {
		return getTemplate(urn + "/" + COLUMN_URN);
	}

	public Boolean hasHeader(String urn) {
		logger.trace("hasHeader(urn={})", urn);
		Boolean hasHeader = hasTemplate(urn + "/" + HEADER_URN); 
		return hasHeader;
	}
	
	public List<U4AdJoin> getHeader(String urn) {
		logger.debug("getHeader(urn={})", urn);
		List<U4AdJoin> getHeader = getTemplate(urn + "/" + HEADER_URN); 
		return getHeader;
	}

	public Boolean hasBeforeRow(String urn) {
		return hasTemplate(urn + "/" + BEFORE_ROW_URN);
	}

	public List<U4AdJoin> getBeforeRow(String urn) {
		logger.debug("getBeforeRow(urn={})", urn);
		return getTemplate(urn + "/" + BEFORE_ROW_URN);
	}

	public Boolean hasRow(String urn) {
		return hasTemplate(urn + "/" + ROW_URN);
	}
	
	public List<U4AdJoin> getRow(String urn) {
		logger.debug("getRow(urn={})", urn);
		return getTemplate(urn + "/" + ROW_URN);
	}

	public Boolean hasAfterRow(String urn) {
		return hasTemplate(urn + "/" + AFTER_ROW_URN);
	}
	
	public List<U4AdJoin> getAfterRow(String urn) {
		logger.debug("getAfterRow(urn={})", urn);
		return getTemplate(urn + "/" + AFTER_ROW_URN);
	}

	public Boolean hasFooter(String urn) {
		return hasTemplate(urn + "/" + FOOTER_URN);
	}
	
	public List<U4AdJoin> getFooter(String urn) {
		logger.debug("getFooter(urn={})", urn);
		return getTemplate(urn + "/" + FOOTER_URN);
	}

	public Boolean hasTemplate(String urn) {
		return hasChild(urn);
	}
	
	public List<U4AdJoin> getTemplate(String urn) {
		logger.debug("getTemplate(urn={})", urn);
		List<U4AdJoin> template = new ArrayList<U4AdJoin>();
		if (hasChild(urn)) {
			template.add(getChild(urn)); //Add the urn i.e. the template being requested.
			template.addAll(getChild(urn).getChildren());
		}
		logger.trace("template.size()={}", template.size());
		logger.trace("{} templates={}", urn, template);
		return template;
	}
	
	public List<U4AdJoin> getChildren(String urn) {
		logger.debug("getChildren(urn={})", urn);
		if (hasChild(urn)) {
			return getChild(urn).getChildren();
		} else {
			return null;
		}
	}

	// Overridden.
	
	public String toString() {
		return getSubject().toString();
	}
}
