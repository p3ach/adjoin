package com.unit4.vocabulary;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;
import com.unit4.exception.Exception;

public class U4Convert extends U4Vocabulary {
	
//	Class.
	
	private static Logger logger = LoggerFactory.getLogger(U4Convert.class);
	
	public static final String PREFIX = "u4convert";
	public static final String NS = "http://www.unit4.com/2011/11/01/u4convert-ns#";
	
	public static final String DEFAULT_REGEX_URI = "http://www.unit4.com/id/U4Convert";
	public static final Resource DEFAULT_REGEX = createResource(DEFAULT_REGEX_URI);
	
	public static final String DEFAULT_COLUMN_URN = "Column";
	
	public static final Resource Convert = createResource(getURI("Convert"));
	
	public static final Resource RegEx = createResource(getURI("RegEx"));
	
	public static final Resource Column = createResource(getURI("Column"));
	
	public static final Resource FormatValue = createResource(getURI("FormatValue"));
	public static final Resource DefineValue = createResource(getURI("DefineValue"));
		
	public static final Resource Null = createResource(getURI("Null"));
	
	public static final Property priority = createProperty(getURI("priority"));
	
	public static final Property pattern = createProperty(getURI("pattern"));
	
	public static final Property subjectURI = createProperty(getURI("subjectURI"));
	public static final Property propertyURI = createProperty(getURI("propertyURI"));
	public static final Property objectURI = createProperty(getURI("objectURI"));
	public static final Property objectType = createProperty(getURI("objectType"));
	public static final Property objectDatatype = createProperty(getURI("objectDatatype"));

	public static final Property value = createProperty(getURI("value"));
	
	public static final Property before = createProperty(getURI("before"));
	public static final Property after = createProperty(getURI("after"));
	
	public static final Property triples = createProperty(getURI("triples"));
	public static final Property triple = createProperty(getURI("triple"));

	public static final String DEFAULT_TEMPLATE_URI = "http://id.unit4.com/template";

	public static final String HEADER_URN = "Header";
	public static final String FOOTER_URN = "Footer";
	public static final String BEFORE_ROW_URN = "BeforeRow";
	public static final String AFTER_ROW_URN = "AfterRow";
	public static final String ROW_URN = "Row";
	public static final String COLUMN_URN = "Column";
	
	public static U4Convert match(List<U4Convert> set, String input) {
		logger.debug("match({},{})", set, input);
		if (set != null) {
			for (U4Convert item : set) {
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
		if (type.equals(RDFS.Resource)) {
			return ResourceFactory.createResource(object.replaceAll("[^a-zA-Z0-9:/_-]", "_")); 
		} else if (type.equals(RDFS.Literal)) {
			if (datatype.equals(U4Convert.Null)) {
				return ResourceFactory.createPlainLiteral(object);
			} else if (datatype.equals(XSD.xstring)) {
				return ResourceFactory.createTypedLiteral(object, XSDDatatype.XSDstring);
			} else if (datatype.equals(XSD.xlong)) {
				return ResourceFactory.createTypedLiteral(object, XSDDatatype.XSDlong);
			} else if (datatype.equals(XSD.decimal)) {
				return ResourceFactory.createTypedLiteral(object, XSDDatatype.XSDdecimal);
			} else {
				throw new Exception(String.format("Datatype %s is unknown.", datatype.toString()));
			}
		} else {
			throw new Exception("Type is unknown.");
		}
	}
	
	public static List<U4Convert> getChildren(List<U4Convert> listOfU4Converts) {
		logger.debug("getChildren()");
		List<U4Convert> children = new ArrayList<U4Convert>();
		for (U4Convert convert : listOfU4Converts) {
			logger.trace("convert {}", convert.toString());
			children.addAll(convert.getChildren());
		}
		return children;
	}
	
//	Instance.
	
//	U4ConvertRegEx
	public static final Property regexPattern = createProperty(getURI("regexPattern"));
	
	public static String getPrefix() {
		return U4Convert.PREFIX;
	}
	
	public static String getNS() {
		return U4Convert.NS;
	}
	
	public static String getURI(String fragment) {
		return getNS() + fragment;
	}

//	public static U4Convert create() {
//		return new U4Convert(ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF).createOntResource("http://id.unit4.com/" + UUID.randomUUID().toString()));
//	}
	
	public static List<U4Convert> create(Set<RDFNode> nodes, Boolean order) {
		logger.debug("create(nodes={}, order={})", nodes, order);
		List<U4Convert> list = new ArrayList<U4Convert>();
		for (RDFNode node : nodes) {
			list.add(new U4Convert(node.asResource()));
		}
		if (order) {
			Collections.sort(list, new U4ConvertComparator());
		}
		return list;
	}
	
//	Instance.
	
//	protected U4SKOS u4SKOS;
	
	public U4Convert() {
	}
	
	public U4Convert(Resource subject) {
		super(subject);
		common();
	}
	
	protected void common() {
		getModel().setNsPrefix(getPrefix(), getNS());
	}
	
	public U4Convert getChild(String urn) {
		return new U4Convert(createChild(urn));
	}	

	public Boolean hasValue() {
		return hasProperty(U4Convert.value);
	}
	
	public U4Convert getValue() {
		return new U4Convert(getResource(U4Convert.value));
	}
	
	/**
	 * Answer whether this AdJoin has a triple property.
	 * @return
	 */
	public Boolean hasTriple() {
		return hasProperty(U4Convert.triple);
	}
	
	/**
	 * Return the resource for the triple property.
	 * @return
	 */
	public U4Convert getTriple() {
		return new U4Convert(getResource(U4Convert.triple));
	}

	public void processValues(VelocityContext context) {
		if (getSubject().isAnon()) { // i.e. (S, :value [)
			if (hasSeq()) { // i.e. ([, rdf:type, rdf:Seq)
				Seq seq = getSeq();
				NodeIterator iterator = seq.iterator();
				while (iterator.hasNext()) {
					new U4Convert(iterator.next().asResource()).processValues(context);
				}
			} else { // i.e. ([, :value, O) 
				evaluate(context, getString(U4Convert.value));
			}
		} else { // i.e (subject, P, O)
//			if (hasBefore()) {
//				statements.addAll(getStatements(getBefore(), context));
//			}
			if (hasValue()) {
				new U4Convert(getValue().getSubject()).processValues(context);
			}
//			if (hasAfter()) {
//				statements.addAll(getStatements(getAfter(), context));
//			}
		}
	}
	
	/** Get all the statements for this AdJoin.
	 * 
	 * <p>This will handle the following AdJoin statements</p>
	 * 
	 * <p>(getSubject(), P, O)</p>
	 * 
	 * <p>(S, adjoin:triple, [)</p>
	 * 
	 * @param context
	 * @return
	 */
	public List<Statement> getStatements(VelocityContext context) {
		List<Statement> statements = new ArrayList<Statement>();
		if (getSubject().isAnon()) { // i.e. (S, :triple [)
			if (hasSeq()) { // i.e. ([, rdf:type, rdf:Seq)
				Seq seq = getSeq();
				NodeIterator iterator = seq.iterator();
				while (iterator.hasNext()) {
					statements.addAll(new U4Convert(iterator.next().asResource()).getStatements(context));
				}
			} else { // Assume it's a triple i.e. ([, :subjectURI, O), ([, :propertyURI, O), ([, ?, O)
				statements.add(getStatement(context));
			}
		} else { // i.e (subject, P, O)
//			if (hasBefore()) {
//				statements.addAll(getStatements(getBefore(), context));
//			}
			if (hasTriple()) {
				statements.addAll(new U4Convert(getTriple().getSubject()).getStatements(context));
			}
//			if (hasAfter()) {
//				statements.addAll(getStatements(getAfter(), context));
//			}
		}
		return statements;
	}
	
	public Statement getStatement(VelocityContext context) {
		Resource subject = ResourceFactory.createResource(evaluate(context, getSubjectURI()));
		
		Property property = ResourceFactory.createProperty(evaluate(context, getPropertyURI()));

		Resource objectType = ResourceFactory.createResource(evaluate(context, getObjectType()));

		if (objectType.equals(RDFS.Resource)) {
			return ResourceFactory.createStatement(subject, property, ResourceFactory.createResource(evaluate(context, getObjectURI())));
		}

		if (objectType.equals(RDFS.Literal)) {
			return ResourceFactory.createStatement(subject, property, ResourceFactory.createTypedLiteral(evaluate(context, getObjectURI()), TypeMapper.getInstance().getTypeByName(evaluate(context, getObjectDatatype()))));
		}
		
		throw new Exception(String.format("Unknown objectType %s", objectType));
	}
	
	public String evaluate(VelocityContext context, String input) {
		if (context == null) {
			throw new Exception("Context is null.");
		}
		
		if (input == null) {
			throw new Exception("Input is null.");
		}
		
    	StringWriter output = new StringWriter();
		try {
			if (Velocity.evaluate(context, output, "U4Convert.evaluate()", input)) {
				return output.toString();
			} else {
				throw new Exception("Velocity returned false.");
			}
		} catch (ParseErrorException e) {
			throw new Exception("Velocity threw a ParseError.", e);
		} catch (MethodInvocationException e) {
			throw new Exception("Velocity threw a MethodInvocation.", e);
		} catch (ResourceNotFoundException e) {
			throw new Exception("Velocity threw a ResourceNotFound", e);
		}
	}

	public Boolean hasChildren() {
		return hasProperty(U4SKOS.narrower);
	}
	
	public List<U4Convert> getChildren() {
		List<U4Convert> children = new ArrayList<U4Convert>();
		for (U4Convert child : U4Convert.create(getProperties(U4SKOS.narrower), true)) {
			children.add(child);
			children.addAll(child.getChildren());
		}
		return children;
	}

	/**
	 * Returns all the U4Convert(s) below this U4Convert (i.e. SKOS.narrower) ordered by priority.
	 * @return
	 */
	public List<U4Convert> getAllSteps() {
		List<U4Convert> steps = new ArrayList<U4Convert>();
		U4Convert step;
		Boolean added;
		for (RDFNode node : getProperties(U4SKOS.narrower)) {
			step = new U4Convert((OntResource) node);
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
	
	public U4Convert setPriority(Integer priority) {
		removeProperty(U4Convert.priority);
		addLiteral(U4Convert.priority, priority);
		return this;
	}
	
	public Integer getPriority() {
		return getInteger(U4Convert.priority);
	}
	
	public String getSubjectURI() {
		return getString(U4Convert.subjectURI);
	}
	
	public String getPropertyURI() {
		return getString(U4Convert.propertyURI);
	}
	
	public String getObjectURI() {
		return getString(U4Convert.objectURI);
	}
		
	public String getObjectType() {
		return getString(U4Convert.objectType);
	}
	
	public String getObjectDatatype() {
		return getString(U4Convert.objectDatatype);
	}

	public Boolean hasPattern() {
		return hasProperty(U4Convert.pattern);
	}
	
	public String getPattern() {
		return getString(U4Convert.pattern);
	}

	public Boolean hasTriples() {
		return hasProperty(U4Convert.triples);
	}
	
	public U4Triples getTriples() {
		return new U4Triples(getResource(U4Convert.triples));
	}
	
	public Boolean hasBefore() {
		return hasProperty(U4Convert.before);
	}
	
	public List<U4Convert> getBefore() {
		return getSeqOrResource(getResource(U4Convert.before));
	}
	
	public Boolean hasAfter() {
		return hasProperty(U4Convert.after); 
	}
	
	public List<U4Convert> getAfter() {
		return getSeqOrResource(getResource(U4Convert.after));
	}

	public List<U4Convert> getSeqOrResource(Resource resource) {
		List<U4Convert> list = new ArrayList<U4Convert>();
		
		if (resource.isAnon()) {
			Seq seq = getModel().getSeq(resource);
			NodeIterator iterator = seq.iterator();
			while (iterator.hasNext()) {
				list.add(new U4Convert(iterator.next().asResource()));
			}
		} else {
			list.add(new U4Convert(resource));
		}
		
		return list;
	}
	
//	public List<Statement> getStatements(List<U4Convert> list, VelocityContext context) {
//		List<Statement> statements = new ArrayList<Statement>();
//		for (U4Convert convert : list) {
//			statements.addAll(convert.getStatements(context));
//		}
//		return statements;
//	}
//	
//	public List<Statement> getStatements(VelocityContext context) {
//		List<Statement> statements = new ArrayList<Statement>();
//		if (hasBefore()) {
//			statements.addAll(getStatements(getBefore(), context));
//		}
//		if (hasTriples()) {
//			statements.addAll(getTriples().getStatements(context));
//		}
//		if (hasAfter()) {
//			statements.addAll(getStatements(getAfter(), context));
//		}
//		return statements;
//	}
	
//	Match.
	public Boolean match(String input) {
		if (hasPattern()) {
			String pattern = getPattern();
			if (pattern == null) {
				return false;
			} else {
				Boolean result = Pattern.matches(pattern, input);
				return result;
			}
		} else {
			return false;
		}
	}

	// Template.
	
	public Boolean hasColumn(String urn) {
		return hasTemplate(urn + "/" + COLUMN_URN);
	}

	public List<U4Convert> getColumn(String urn) {
		logger.debug("getColumn(urn={})", urn);
		return getTemplate(urn + "/" + COLUMN_URN);
	}

	public Boolean hasHeader(String urn) {
		return hasTemplate(urn + "/" + HEADER_URN);
	}
	
	public List<U4Convert> getHeader(String urn) {
		logger.debug("getHeader(urn={})", urn);
		return getTemplate(urn + "/" + HEADER_URN);
	}

	public Boolean hasBeforeRow(String urn) {
		return hasTemplate(urn + "/" + BEFORE_ROW_URN);
	}

	public List<U4Convert> getBeforeRow(String urn) {
		logger.debug("getBeforeRow(urn={})", urn);
		return getTemplate(urn + "/" + BEFORE_ROW_URN);
	}

	public Boolean hasRow(String urn) {
		return hasTemplate(urn + "/" + ROW_URN);
	}
	
	public List<U4Convert> getRow(String urn) {
		logger.debug("getRow(urn={})", urn);
		return getTemplate(urn + "/" + ROW_URN);
	}

	public Boolean hasAfterRow(String urn) {
		return hasTemplate(urn + "/" + AFTER_ROW_URN);
	}
	
	public List<U4Convert> getAfterRow(String urn) {
		logger.debug("getAfterRow({})", urn);
		return getTemplate(urn + "/" + AFTER_ROW_URN);
	}

	public Boolean hasFooter(String urn) {
		return hasTemplate(urn + "/" + FOOTER_URN);
	}
	
	public List<U4Convert> getFooter(String urn) {
		logger.debug("getFooter({})", urn);
		return getTemplate(urn + "/" + FOOTER_URN);
	}

	public Boolean hasTemplate(String urn) {
		logger.debug("hasTemplate({})", urn);
		return hasChild(urn);
	}
	
	public List<U4Convert> getTemplate(String urn) {
		logger.debug("getTemplate(urn={})", urn);
		List<U4Convert> template = new ArrayList<U4Convert>();
		if (hasChild(urn)) {
			template.add(getChild(urn)); //Add the urn i.e. the template being requested.
			template.addAll(getChild(urn).getChildren());
		}
		logger.trace("template.size()={}", template.size());
		logger.trace("{} templates={}", urn, template);
		return template;
	}
	
	public List<U4Convert> getChildren(String urn) {
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
