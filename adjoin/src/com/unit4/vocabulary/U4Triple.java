package com.unit4.vocabulary;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.unit4.exception.Exception;

public class U4Triple extends U4Vocabulary {
	private static Logger logger = LoggerFactory.getLogger(U4Triple.class);
	
	private String objectType;

	public U4Triple() {
	}
	
	public U4Triple(Resource subject) {
		super(subject);
	}
	
	public U4Triple(U4Vocabulary vocabulary) {
		super(vocabulary);
	}

	public Boolean hasValue() {
		return hasProperty(U4AdJoin.value);
	}
	
	public String getValue() {
		return getString(U4AdJoin.value);
	}
	
	public U4Triple subjectValue(String subjectValue) {
		return this;
	}
	
	public String getSubjectValue() {
		return getString(U4AdJoin.subjectValue);
	}
	
	public U4Triple propertyValue(String propertyValue) {
		return this;
	}
	
	public String getPropertyValue() {
		return getString(U4AdJoin.propertyValue);
	}
	
	public String getObjectValue() {
		return getString(U4AdJoin.objectValue);
	}
	
	public String getObjectType() {
		return getString(U4AdJoin.objectType);
	}
	
	public String getObjectDatatype() {
		return getString(U4AdJoin.objectDatatype);
	}

	/**
	 * Read the values associated with this mode() and resource().
	 * Throws an Exception if model or resource are null.
	 * @return
	 * This U4Triple.
	 */
	public U4Triple read() {
//		value(getString(U4Convert.value));
//		subjectValue(hasProperty(U4Convert.subjectValue) ? getString(U4Convert.subjectValue) : null);
//		propertyValue(getString(U4Convert.propertyValue));
//		objectValue(getString(U4Convert.objectValue));
//		objectType(getString(U4Convert.objectType));
//		objectDatatype(getString(U4Convert.objectDatatype));
		return this;
	}
	
	public Boolean hasBefore() {
		logger.debug("hasBefore()");
		return hasProperty(U4AdJoin.before);
	}
	
	public Resource getBefore() {
		logger.debug("getBefore()");
		return getResource(U4AdJoin.before);
	}
	
	public Boolean hasAfter() {
		logger.debug("hasAfter()");
		return hasProperty(U4AdJoin.after);
	}
	
	public Resource getAfter() {
		logger.debug("getAfter()");
		return (Resource) getProperty(U4AdJoin.after);
	}
	
	/**
	 * Create a statement from the ?Values.
	 * If the Triple has a U4Convert.value property it is evaluated first then the statement is created. 
	 * Each ?Value is evaluated against the context and the output is used to create a statement.
	 * @param context : A VelocityContext used to provide token parsing functionality.
	 * @return statement : A Statement.
	 */
	public List<Statement> getStatements(VelocityContext context) {
		if (context == null) {
			throw new Exception("Context is null.");
		}

		List<Statement> statements = new ArrayList<Statement>();
		
		if (hasBefore()) {
			statements.addAll(new U4Triples(getBefore()).getStatements(context));
		}
	
		if (hasValue()) {
			evaluate(context, getValue());
		}

		if (valid()) {
			statements.add(getStatement(context));
		}
		
		if (hasAfter()) {
			statements.addAll(new U4Triples(getAfter()).getStatements(context));
		}
		return statements;
	}

	public Boolean valid() {
		return (getSubjectValue() != null) && (getPropertyValue() != null) && (objectType != null) && (getObjectValue() != null);
	}
	
	public Statement getStatement(VelocityContext context) {
//		String evaluatedSubjectValue = evaluate(context, subjectValue());
		Resource subject = ResourceFactory.createResource(evaluate(context, getSubjectValue()));

//		String evaluatedPropertyValue = evaluate(context, propertyValue());
		Property property = ResourceFactory.createProperty(evaluate(context, getPropertyValue()));
		
//		String evaluatedObjectType = evaluate(context, objectType());
		Resource objectType = ResourceFactory.createResource(evaluate(context, getObjectType()));

//		RDFDatatype objectRDFDatatype;
//		RDFNode object;
		if (objectType.equals(RDFS.Resource)) {
//			String evaluatedObjectValue = evaluate(context, objectValue());
//			object = ResourceFactory.createResource(U4Common.createURI(evaluate(context, objectValue())));
			return ResourceFactory.createStatement(subject, property, ResourceFactory.createResource(evaluate(context, getObjectValue())));
		}

		if (objectType.equals(RDFS.Literal)) {
//			if (objectDatatype() != null) {
//				objectRDFDatatype = TypeMapper.getInstance().getTypeByName(evaluate(context, objectDatatype()));
//				logger.trace("objectRDFDatatype={}", objectRDFDatatype);
//				object = ResourceFactory.createTypedLiteral(evaluate(context, objectValue()), TypeMapper.getInstance().getTypeByName(evaluate(context, objectDatatype())));
				return ResourceFactory.createStatement(subject, property, ResourceFactory.createTypedLiteral(evaluate(context, getObjectValue()), TypeMapper.getInstance().getTypeByName(evaluate(context, getObjectDatatype()))));
//			}
		}
		
		throw new Exception(String.format("Unknown objectType %s", objectType));
	}
	
	protected String evaluate(VelocityContext context, String input) {
		logger.debug("evaluate({}, {})", new Object[] {context, input});

		if (context == null) {
			throw new Exception("context is null.");
		}
		
		if (input == null) {
			throw new Exception("input is null.");
		}
		
    	StringWriter output = new StringWriter();
		try {
			if (Velocity.evaluate(context, output, "U4Triple.evaluate()", input)) {
				String evaluated = output.toString();
				logger.trace("evaluated={}", evaluated);
				return evaluated;
			} else {
				throw new Exception("Velocity returned false.");
			}
		} catch (ParseErrorException e) {
			throw new Exception("Velocity threw a ParseError.", e);
		} catch (MethodInvocationException e) {
			throw new Exception("Velocity threw a MethodInvocation.", e);
		} catch (ResourceNotFoundException e) {
			throw new Exception("velocity threw a ResourceNotFound", e);
		}

	}
	
	public String toString() {
		return String.format("U4Triple [%s, %s, %s, %s, %s]", getSubjectValue(), getPropertyValue(), getObjectValue(), getObjectType(), getObjectDatatype());
	}
}
;