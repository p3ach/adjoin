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
	
	public static final Property priority = createProperty(getURI("priority"));
	
	public static final Property pattern = createProperty(getURI("pattern"));
	
	public static final Property subjectURI = createProperty(getURI("subjectURI"));
	public static final Property propertyURI = createProperty(getURI("propertyURI"));
	public static final Property objectType = createProperty(getURI("objectType"));
	public static final Property objectURI = createProperty(getURI("objectURI"));
	public static final Property objectValue = createProperty(getURI("objectValue"));
	public static final Property objectDatatype = createProperty(getURI("objectDatatype"));

	public static final Property triples = createProperty(getURI("triples"));
	public static final Property triple = createProperty(getURI("triple"));
	
	public static final Property values = createProperty(getURI("values"));
	public static final Property value = createProperty(getURI("value"));
	
	public static final Property before = createProperty(getURI("before"));
	public static final Property after = createProperty(getURI("after"));

	public static final Property statements = createProperty(getURI("statements"));
	public static final Property statement = createProperty(getURI("statement"));

	public static final String DEFAULT_TEMPLATE_URI = "http://id.unit4.com/template/AdJoin";
	public static final String DEFAULT_TEMPLATE_GROUP = "Default";

	public static final String HEADER_URN = "Header";
	public static final String FOOTER_URN = "Footer";
	public static final String BEFORE_ROW_URN = "BeforeRow";
	public static final String AFTER_ROW_URN = "AfterRow";
	public static final String ROW_URN = "Row";
	public static final String COLUMN_URN = "Column";
	
	public static U4AdJoin match(List<U4AdJoin> set, String input) {
		logger.debug("match({},{})", set, input);
		if (set != null) {
			for (U4AdJoin item : set) {
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
				throw new Exception(String.format("Datatype %s is unknown.", datatype.toString()));
			}
		} else {
			throw new Exception("Type is unknown.");
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

	public Boolean hasValue() {
		return hasProperty(U4AdJoin.value);
	}
	
	public U4AdJoin getValue() {
		return new U4AdJoin(getResource(U4AdJoin.value));
	}
	
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

	public void processValues(VelocityContext context) {
		logger.debug("processValues(context={})", context);
		if (getSubject().isAnon()) { // i.e. (S, :value [)
			if (hasSeq()) { // i.e. ([, rdf:type, rdf:Seq)
				Seq seq = getSeq();
				NodeIterator iterator = seq.iterator();
				while (iterator.hasNext()) {
					new U4AdJoin(iterator.next().asResource()).processValues(context);
				}
			} else { // i.e. ([, :value, O)
				if (hasValue()) {
					evaluate(context, getString(U4AdJoin.value));
				}
			}
		} else { // i.e (subject, P, O)
			if (hasValue()) {
				getValue().processValues(context);
			}
		}
	}

	/** Get all the statements for this U4AdJoin.
	 * 
	 * <p></p>
	 * 
	 * <p></p>
	 * 
	 * <p></p>
	 * 
	 * @param context
	 * @return
	 */
	public List<Statement> getStatements(VelocityContext context) {
		logger.debug("Start getStatements(context={}) for subject {}", context, getSubject());
		
		List<Statement> statements = new ArrayList<Statement>();

		if (isAnonymous()) { // i.e. (S, :triple, [) where [ = getSubject().
			logger.trace("Subject is anonymous.");
			if (hasSeq()) { // i.e. ([, rdf:type, rdf:Seq)
				Seq seq = getSeq();
				logger.trace("Has sequence with {} entry(s).", seq.size());
				NodeIterator iterator = seq.iterator();
				while (iterator.hasNext()) {
					statements.addAll(new U4AdJoin(iterator.next().asResource()).getStatements(context));
				}
			} else { // Assume it's a triple i.e. ([, :subjectURI, O), ([, :propertyURI, O), ([, ?, O)
				logger.trace("Subject is not a Sequence.");
				if (hasStatement()) {
					logger.trace("Has adjoin:statement.");
					statements.addAll(getStatement().getStatements(context));
				} else {
					logger.trace("Does not have adjoin:statement.");
					statements.add(getStatement(context));
				}
			}
		} else { // i.e (S, P, O) where S = getSubject().
			logger.trace("Subject is not anonymous.");
			if (hasValue()) {
				logger.trace("Subject has :value.");
				processValues(context);
			}
			if (hasStatement()) {
				logger.trace("Subject has a adjoin:statement");
				statements.addAll(new U4AdJoin(getStatement().getSubject()).getStatements(context));
			}
			if (hasAfter()) {
				for (U4AdJoin after : getAfter()) {
					statements.addAll(after.getStatements(context));
				}
			}
		}

		logger.debug("Finish getStatements(context={}) for subject {}", context, getSubject());

		return statements;
	}
	
	public Statement getStatement(VelocityContext context) {
		logger.debug("getStatement(context={})", context);
		
		Resource subject = ResourceFactory.createResource(evaluate(context, getSubjectURI()));
		
		Property property = ResourceFactory.createProperty(evaluate(context, getPropertyURI()));

		Resource objectType = ResourceFactory.createResource(evaluate(context, getObjectType()));

		if (objectType.equals(RDFS.Resource)) {
			return ResourceFactory.createStatement(subject, property, ResourceFactory.createResource(evaluate(context, getObjectURI())));
		}

		if (objectType.equals(RDFS.Literal)) {
			return ResourceFactory.createStatement(subject, property, ResourceFactory.createTypedLiteral(evaluate(context, getObjectValue()), TypeMapper.getInstance().getTypeByName(evaluate(context, getObjectDatatype()))));
		}
		
		throw new Exception(String.format("Unknown objectType %s", objectType));
	}
	
	public String evaluate(VelocityContext context, String input) {
		logger.trace("evaluate(context={}, input={}", context, input);
		
		if (context == null) {
			throw new Exception("Context is null.");
		}
		
		if (input == null) {
			throw new Exception("Input is null.");
		}
		
    	StringWriter output = new StringWriter();
		try {
			if (Velocity.evaluate(context, output, "U4AdJoin.evaluate()", input)) {
				String evaluate = output.toString();
				logger.trace("output={}",evaluate);
				return evaluate;
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
	
	public String getSubjectURI() {
		return getString(U4AdJoin.subjectURI);
	}
	
	public String getPropertyURI() {
		return getString(U4AdJoin.propertyURI);
	}
	
	public String getObjectType() {
		return getString(U4AdJoin.objectType);
	}
	
	public String getObjectURI() {
		return getString(U4AdJoin.objectURI);
	}
	
	public String getObjectValue() {
		return getString(U4AdJoin.objectValue);
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
		List<U4AdJoin> list = new ArrayList<U4AdJoin>();
		
		if (resource.isAnon()) {
			Seq seq = getModel().getSeq(resource);
			NodeIterator iterator = seq.iterator();
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

	public List<U4AdJoin> getColumn(String urn) {
		logger.debug("getColumn(urn={})", urn);
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
		logger.debug("hasTemplate(urn={})", urn);
		Boolean hasTemplate = hasChild(urn); 
		return hasTemplate;
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
