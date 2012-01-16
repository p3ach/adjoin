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

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.unit4.exception.Exception;
import com.unit4.tabular.U4Common;

public class U4Triple extends U4Vocabulary {
	private static Logger logger = LoggerFactory.getLogger(U4Triple.class);
	
	private String value;
	private String subjectValue;
	private String propertyValue;
	private String objectValue;
	private String objectType;
	private String objectDatatype;
	
	public U4Triple() {
	}
	
	public U4Triple(Resource subject) {
		super(subject);
	}
	
	public U4Triple(U4Vocabulary vocabulary) {
		super(vocabulary);
	}

	public U4Triple value(String value) {
		this.value = value;
		return this;
	}
	
	public String value() {
		return this.value;
	}
	
	public U4Triple subjectValue(String subjectValue) {
		this.subjectValue = subjectValue;
		return this;
	}
	
	public String subjectValue() {
		return this.subjectValue;
	}
	
	public U4Triple propertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
		return this;
	}
	
	public String propertyValue() {
		return this.propertyValue;
	}
	
	public U4Triple objectValue(String objectValue) {
		this.objectValue = objectValue;
		return this;
	}
	
	public String objectValue() {
		return this.objectValue;
	}
	
	public U4Triple objectType(String objectType) {
		this.objectType = objectType;
		return this;
	}
	
	public String objectType() {
		return this.objectType;
	}
	
	public U4Triple objectDatatype(String objectDatatype) {
		this.objectDatatype = objectDatatype;
		return this;
	}
	
	public String objectDatatype() {
		logger.trace("objectDatatype={}", this.objectDatatype);
		return this.objectDatatype;
	}

	/**
	 * Read the values associated with this mode() and resource().
	 * Throws an Exception if model or resource are null.
	 * @return
	 * This U4Triple.
	 */
	public U4Triple read() {
		value(getString(U4Convert.value));
		subjectValue(getString(U4Convert.subjectValue));
		propertyValue(getString(U4Convert.propertyValue));
		objectValue(getString(U4Convert.objectValue));
		objectType(getString(U4Convert.objectType));
		objectDatatype(getString(U4Convert.objectDatatype));
		return this;
	}
	
	public Boolean hasBefore() {
		logger.debug("hasBefore()");
		return hasProperty(U4Convert.before);
	}
	
	public Resource getBefore() {
		logger.debug("getBefore()");
		return getResource(U4Convert.before);
	}
	
	public Boolean hasAfter() {
		logger.debug("hasAfter()");
		return hasProperty(U4Convert.after);
	}
	
	public Resource getAfter() {
		logger.debug("getAfter()");
		return (Resource) getProperty(U4Convert.after);
	}
	
	/**
	 * Create a statement from the ?Values.
	 * If the Triple has a U4Convert.value property it is evaluated first then the statement is created. 
	 * Each ?Value is evaluated against the context and the output is used to create a statement.
	 * @param context : A VelocityContext used to provide token parsing functionality.
	 * @return statement : A Statement.
	 */
	public Statement statement(VelocityContext context) {
		if (context == null) {
			throw new Exception("context is null.");
		}

		Model model = getModel();
		if (model == null) {
			throw new Exception("model is null.");
		}

		List<Statement> statements = new ArrayList<Statement>();
		
		if (hasBefore()) {
			statements.addAll(new U4Triples(getBefore()).statements(context));
		}
	
		if (value() != null) {
			evaluate(context, value);
		}
		
		if (subjectValue() == null) {
			logger.trace("subjectValue=null");
			return null;
		}
		if (propertyValue() == null) {
			logger.trace("propertyValue=null");
			return null;
		}
		if (objectType() == null) {
			logger.trace("objectType=null");
			return null;
		}
		if (objectValue() == null) {
			logger.trace("objectValue=null");
		}
		
		String evaluatedSubjectValue = evaluate(context, subjectValue());
		Resource subject = ResourceFactory.createResource(U4Common.createURI(evaluatedSubjectValue));

		String evaluatedPropertyValue = evaluate(context, propertyValue());
		Property property = ResourceFactory.createProperty(evaluatedPropertyValue);
		
		String evaluatedObjectType = evaluate(context, objectType());
		Resource objectType = ResourceFactory.createResource(evaluatedObjectType);

		RDFDatatype objectRDFDatatype;
		RDFNode object;
		if (objectType.equals(RDFS.Resource)) {
			String evaluatedObjectValue = evaluate(context, objectValue());

			object = ResourceFactory.createResource(U4Common.createURI(evaluatedObjectValue));
		} else if (objectType.equals(RDFS.Literal)) {
			if (objectDatatype() == null) {
				return null;
			}
			objectRDFDatatype = TypeMapper.getInstance().getTypeByName(evaluate(context, objectDatatype()));
			logger.trace("objectRDFDatatype={}", objectRDFDatatype);
			object = ResourceFactory.createTypedLiteral(evaluate(context, objectValue()), objectRDFDatatype);
		} else {
			logger.warn("unknown objectType.");
			return null;
		}
		
		Statement statement = ResourceFactory.createStatement(subject, property, object); 
		logger.trace("statement={}", statement);
		return statement;
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
		return String.format("[%s, %s, %s, %s, %s]", subjectValue(), propertyValue(), objectValue(), objectType(), objectDatatype());
	}
}
;