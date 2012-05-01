package com.unit4.output;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.FieldMethodizer;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Container;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.rdf.model.impl.LiteralImpl;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.unit4.cli.Options;
import com.unit4.exception.Exception;
import com.unit4.input.U4Input;
import com.unit4.input.U4InputCallback;
import com.unit4.tabular.U4Columns;
import com.unit4.tabular.U4Common;
import com.unit4.tabular.U4Row;
import com.unit4.vocabulary.U4AdJoin;
import com.unit4.vocabulary.U4AdJoinCallback;
import com.unit4.vocabulary.U4AdJoinTemplate;
import com.unit4.vocabulary.U4DCTerms;
import com.unit4.vocabulary.U4FOAF;
import com.unit4.vocabulary.U4Org;
import com.unit4.vocabulary.U4RDF;
import com.unit4.vocabulary.U4RDFS;
import com.unit4.vocabulary.U4VoID;
import com.unit4.vocabulary.U4XSD;

/**
 * Output an Input as RDF.
 * 
 * @author dick
 *
 */
public class U4OutputRDF implements U4Output {

//	Class.
	
	private static Logger logger = LoggerFactory.getLogger(U4OutputRDF.class);

	private static final OutputStream DEFAULT_OUTPUT_STREAM = System.out;
	private static final String DEFAULT_LANGUAGE = "RDF/XML";

//	Instance.
	
	private U4Common common;
	
	private final Model model = ModelFactory.createDefaultModel(); 

	private final VelocityContext context = new VelocityContext();
	
	private Options options;
	
	public U4OutputRDF() {
   		Velocity.init();
		final Model model = getModel(); 
		U4DCTerms.setNsPrefix(model);
		U4FOAF.setNsPrefix(model);
		U4Org.setNsPrefix(model);
		U4RDF.setNsPrefix(model);
		U4RDFS.setNsPrefix(model);
		U4VoID.setNsPrefix(model);
		U4XSD.setNsPrefix(model);
		U4AdJoin.setNsPrefix(model);
	}
	
	public Model getModel() {
		return this.model;
	}

	protected VelocityContext getContext() {
		return this.context;
	}
	
	protected String getLanguage() {
		return (String) getOptions().getOption("outputLanguage", DEFAULT_LANGUAGE);
	}
	
	public String getOutputURI() {
		return (String) getOptions().getOption("outputURI");
	}
	
	protected OutputStream getOutputStream() {
		final Options options = getOptions();
		if (options.hasOption("outputURI")) {
			try {
				return new FileOutputStream(getOutputURI());
			} catch (FileNotFoundException e) {
				throw new Exception("Failed to create output file.", e);
			}
		} else {
			return DEFAULT_OUTPUT_STREAM;
		}
	}

	@Override
	public void setCommon(U4Common common) {
		this.common = common;
	}
	
	@Override
	public U4Common getCommon() {
		return this.common;
	}
	
	@Override
	public void setOptions(Options options) {
		this.options = options;
	}

	@Override
	public Options getOptions() {
		return this.options;
	}
	
	/**
	 * Convenience method same as <code>getCommon().getInput()</code>.
	 * @return
	 */
	protected U4Input getInput() {
		return getCommon().getInput();
	}
	
	/**
	 * Convenience method same as <code>getCommon().getColumns()</code>.
	 * @return
	 */
	protected U4Columns getColumns() {
		return getCommon().getColumns();
	}
	
	/**
	 * Convenience method same as <code>getCommon().getRow()</code>.
	 * @return
	 */
	protected U4Row getRow() {
		return getCommon().getRow();
	}

	protected Container getContainer(String key) {
		return (Container) common.getValue(key);
	}

	/**
	 * @return 
	 * 
	 */
	public Boolean processInput() {
    	final List<U4AdJoin> columnsTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> columnTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> headerTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> beforeRowTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> rowTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> afterRowTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> footerTemplates = new ArrayList<U4AdJoin>();

   		final VelocityContext context = getContext();
        context.put("Input", getCommon().getInput());
		context.put("Output", this);
		context.put("Options", getCommon().getOptions());
		context.put("Common", getCommon());
        context.put("Columns", getCommon().getColumns());
        context.put("Row", getCommon().getRow());
        
        context.put("Math", Math.class);
        context.put("String", String.class);
        context.put("UUID", UUID.class);
        
        context.put("DCTerms", new FieldMethodizer("com.unit4.vocabulary.U4DCTerms"));
        context.put("FOAF", new FieldMethodizer("com.unit4.vocabulary.U4FOAF"));
        context.put("Org", new FieldMethodizer("com.unit4.vocabulary.U4Org"));
        context.put("RDF", new FieldMethodizer("com.unit4.vocabulary.U4RDF"));
        context.put("RDFS", new FieldMethodizer("com.unit4.vocabulary.U4RDFS"));
        context.put("SKOS", new FieldMethodizer("com.unit4.vocabulary.U4SKOS"));
        context.put("XSD", new FieldMethodizer("com.unit4.vocabulary.U4XSD"));
        context.put("VoID", new FieldMethodizer("com.unit4.vocabulary.U4VoID"));
        context.put("AdJoin", new FieldMethodizer("com.unit4.vocabulary.U4AdJoin"));

        final U4AdJoinTemplate template = getCommon().getTemplate();
		for (String group : template.getGroups()) {
			if ((columnsTemplates.size() == 0) && template.hasColumns(group)) {
				columnsTemplates.addAll(template.getColumns(group));
			}
			if ((columnTemplates.size() == 0) && template.hasColumn(group)) {
				columnTemplates.addAll(template.getColumn(group));
			}
			if ((headerTemplates.size() == 0) && template.hasHeader(group)) {
				headerTemplates.addAll(template.getHeader(group));
			}
			if ((beforeRowTemplates.size() == 0) && template.hasBeforeRow(group)) {
				beforeRowTemplates.addAll(template.getBeforeRow(group));
			}
			if ((rowTemplates.size() == 0) && template.hasRow(group)) {
				rowTemplates.addAll(template.getRow(group));
			}
			if ((afterRowTemplates.size() == 0) && template.hasAfterRow(group)) {
				afterRowTemplates.addAll(template.getAfterRow(group));
			}
			if ((footerTemplates.size() == 0) && template.hasFooter(group)) {
				footerTemplates.addAll(template.getFooter(group));
			}
		}

	   	final U4InputCallback inputCallback = new U4InputCallback() {
	   		final U4AdJoinCallback adjoinCallback = new U4AdJoinCallback() {
	   			class U4Resource {
	   				private String uri;
	   				private Resource resource;
	   				private Seq seq;
	   				public U4Resource(final String uri) {
	   					setURI(uri);
	   					if (uri.startsWith("[Blank]")) {
	   						if (getCommon().has(uri)) {
	   							setResource((Resource) getCommon().getValue(uri));
	   						} else {
	   							setResource((Resource) getCommon().setValue(uri, getModel().createResource()));
	   						}
	   					} else if (uri.startsWith("[Seq]")) {
	   						if (getCommon().has(uri)) {
	   							setSeq((Seq) getCommon().getValue(uri));
	   						} else {
	   							setSeq((Seq) getCommon().setValue(uri, getModel().createSeq()));
	   						}
	   					} else {
	   						setResource(getModel().createResource(uri));
	   					}
	   				}
	   				private void setURI(String uri) {
	   					this.uri = uri;
	   				}
	   				private String getURI() {
	   					return this.uri;
	   				}
	   				private void setResource(Resource resource) {
	   					this.resource = resource;
	   				}
	   				public Resource getResource() {
   						return this.resource;
	   				}
	   				private void setSeq(Seq seq) {
	   					this.seq = seq;
	   					setResource(seq);
	   				}
	   				public Seq getSeq() {
	   					return this.seq;
	   				}
	   				public Boolean isSeq() {
	   					return (getSeq() != null);
	   				}
	   				@Override
	   				public String toString() {
	   					return getURI();
	   				}
	   			}
	   			
				@Override
				public void callback(final U4AdJoin item) {
					if (item.hasValue()) {
						evaluate(item.getString(U4AdJoin.value));
					}

					if (!item.hasSubjectURI()) {
						return;
					}
					
					final U4Resource subject = new U4Resource(evaluate(item.getSubjectURI())); 
					
					final Resource objectType = getModel().createResource(evaluate(item.getObjectType()));

					if (objectType.equals(RDFS.Resource)) {
						final U4Resource object = new U4Resource(evaluate(item.getObjectValue()));
						if (subject.isSeq()) { // Add object to Seq.
							subject.getSeq().add(object.getResource());
						} else { // Add statement to Model.
							final Property property = getModel().createProperty(evaluate(item.getPropertyURI()));
							getModel().add(subject.getResource(), property, object.getResource());
						}
						return;
					} else if (objectType.equals(RDFS.Literal)) {
						final Property property = getModel().createProperty(evaluate(item.getPropertyURI()));
						final String objectValue = evaluate(item.getObjectValue());
						if (item.hasObjectDatatype()) { // Create a typed literal.
							final RDFDatatype rdfDatatype = TypeMapper.getInstance().getTypeByName(evaluate(item.getObjectDatatype()));
							getModel().add(subject.getResource(), property, getModel().createTypedLiteral(objectValue, rdfDatatype));
						} else {
							if (item.hasObjectLanguage()) { // Create a literal with language tag.
								final String objectLanguage = evaluate(item.getObjectLanguage());
								getModel().add(subject.getResource(), property, getModel().createLiteral(objectValue, objectLanguage));
							} else { // Create a plain literal.
								getModel().add(subject.getResource(), property, getModel().createLiteral(objectValue));
							}
						}
						return;
					} else {
						throw new Exception(String.format("Unknown objectType [%s].", objectType));
					}
				}

				private String evaluate(String input) {
					logger.trace("evaluate({})", input);
					
					if (input == null) {
//						throw new Exception("Input is null.");
						return null;
					}
					
			    	final StringWriter output = new StringWriter();
					try {
						if (Velocity.evaluate(context, output, "U4OutputRDF", input)) {
							final String evaluate = output.toString();
							logger.trace("output={}", evaluate);
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
			};
			
   			@Override
			public U4InputCallback header() {
   				logger.trace("header()");
		        for (U4AdJoin headerTemplate : headerTemplates) {
	        		headerTemplate.getStatements(adjoinCallback);
		        }
		        return this;
			}
			
			@Override
			public U4InputCallback beforeRow() {
				logger.trace("beforeRow()");
			    for (U4AdJoin beforeRowtemplate : beforeRowTemplates) {
		    		beforeRowtemplate.getStatements(adjoinCallback);
			    }
			    return this;
			}
			
			@Override
			public U4InputCallback row() {
				logger.trace("row()");
				final U4Columns columns = getColumns();
				for (String value : common.getRow()) {
					logger.trace("value={}",value);
					if (value == null | value.equals("")) {
					} else {
						columns.setMatch(U4AdJoin.match(columnTemplates, columns.getName()));
						if (columns.hasMatch()) {
							columns.getMatch().getStatements(adjoinCallback);
//							getModel().add(columns.getMatch().getStatements(context));
						} else {
							columns.setMatch(U4AdJoin.match(rowTemplates, value));
							if (columns.hasMatch()) {
								columns.getMatch().getStatements(adjoinCallback);
//								getModel().add(columns.getMatch().getStatements(context));
							}
						}
					}
				}
				return this;
			}
			
			@Override
			public U4InputCallback afterRow() {
				logger.trace("afterRow()");
			    for (U4AdJoin afterRowtemplate : afterRowTemplates) {
		    		afterRowtemplate.getStatements(adjoinCallback);
			    }
			    return this;
			}
			
			@Override
			public U4InputCallback footer() {
				logger.trace("footer()");
		        for (U4AdJoin footerTemplate : footerTemplates) {
	        		footerTemplate.getStatements(adjoinCallback);
		        }
		        return this;
			}
		};

		getCommon().getInput().setCallback(inputCallback).parse();
		
		logger.info("Statements {}", getModel().size());

		return true;
	}
	
	@Override
	public Boolean render() {
		logger.info("Render to {} as {}.", getOutputStream().toString(), getLanguage());
		getModel().write(getOutputStream(), getLanguage());
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("U4OutputRDF outputURI[%s]", getOutputURI());
	}
}
