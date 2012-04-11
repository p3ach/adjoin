package com.unit4.output;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.FieldMethodizer;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.unit4.input.U4Input;
import com.unit4.input.U4InputCallback;
import com.unit4.tabular.U4Columns;
import com.unit4.tabular.U4Common;
import com.unit4.tabular.U4Row;
import com.unit4.vocabulary.U4AdJoin;
import com.unit4.vocabulary.U4AdJoinTemplate;
import com.unit4.vocabulary.U4DCTerms;
import com.unit4.vocabulary.U4FOAF;
import com.unit4.vocabulary.U4Org;
import com.unit4.vocabulary.U4RDF;
import com.unit4.vocabulary.U4RDFS;
import com.unit4.vocabulary.U4VoID;
import com.unit4.vocabulary.U4XSD;

public class U4OutputRDF implements U4Output {
	private static Logger logger = LoggerFactory.getLogger(U4OutputRDF.class);

	private U4Common common;
	
	private Model output;
	private String language = "Turtle";
	private OutputStream stream = System.out;
	
	public U4OutputRDF() {
		setModel(ModelFactory.createDefaultModel());
		common();
	}
	
	public void common() {
		Model model = getModel();
		U4DCTerms.setNsPrefix(model);
		U4FOAF.setNsPrefix(model);
		U4Org.setNsPrefix(model);
		U4RDF.setNsPrefix(model);
		U4RDFS.setNsPrefix(model);
		U4VoID.setNsPrefix(model);
		U4XSD.setNsPrefix(model);
	}

	public void setModel(Model output) {
		this.output = output;
	}
	
	public Model getModel() {
		return this.output;
	}
	
	public U4OutputRDF setLanguage(String language) {
		this.language = language;
		return this;
	}
	
	public String getLanguage() {
		return this.language;
	}
	
	public void setStream(OutputStream stream) {
		this.stream = stream;
	}
	
	public OutputStream getStream() {
		return this.stream;
	}

	public void setCommon(U4Common common) {
		this.common = common;
	}
	
	public U4Common getCommon() {
		return this.common;
	}
	
	/**
	 * Convenience method same as <code>getCommon().getInput()</code>
	 * @return
	 */
	public U4Input getInput() {
		return getCommon().getInput();
	}
	
	/**
	 * Convenience method same as <code>getCommon().getColumns()</code>
	 * @return
	 */
	public U4Columns getColumns() {
		return getCommon().getColumns();
	}
	
	/**
	 * Convenience method same as <code>getCommon().getRow()</code>
	 * @return
	 */
	public U4Row getRow() {
		return getCommon().getRow();
	}
	
	public Boolean processInput() {
    	final List<U4AdJoin> columnsTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> columnTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> headerTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> beforeRowTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> rowTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> afterRowTemplates = new ArrayList<U4AdJoin>();
    	final List<U4AdJoin> footerTemplates = new ArrayList<U4AdJoin>();

   		Velocity.init();
   		final VelocityContext context = new VelocityContext();
        context.put("Input", getCommon().getInput());
		context.put("Output", this);
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

	   	U4InputCallback inputCallback = new U4InputCallback() {
   			@Override
			public U4InputCallback header() {
   				logger.trace("header()");
		        for (U4AdJoin headerTemplate : headerTemplates) {
	        		getModel().add(headerTemplate.getStatements(context));
		        }
		        return this;
			}
			
			@Override
			public U4InputCallback beforeRow() {
				logger.trace("beforeRow()");
			    for (U4AdJoin beforeRowtemplate : beforeRowTemplates) {
		    		getModel().add(beforeRowtemplate.getStatements(context));
			    }
			    return this;
			}
			
			@Override
			public U4InputCallback row() {
				logger.trace("row()");
				final U4Columns columns = getColumns();
//				final List<String> values = common.getRow().getValues();
//				for (String value : values) {
//					logger.trace("value={}",value);
//				}
				for (String value : common.getRow()) {
					logger.trace("value={}",value);
					if (value == null | value.equals("")) {
					} else {
						columns.setMatch(U4AdJoin.match(columnTemplates, columns.getName()));
						if (columns.hasMatch()) {
							getModel().add(columns.getMatch().getStatements(context));
						} else {
							columns.setMatch(U4AdJoin.match(rowTemplates, value));
							if (columns.hasMatch()) {
								getModel().add(columns.getMatch().getStatements(context));
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
		    		getModel().add(afterRowtemplate.getStatements(context));
			    }
			    return this;
			}
			
			@Override
			public U4InputCallback footer() {
				logger.trace("footer()");
		        for (U4AdJoin footerTemplate : footerTemplates) {
	        		getModel().add(footerTemplate.getStatements(context));
		        }
		        return this;
			}
		};

		getCommon().getInput().setCallback(inputCallback).parse();

		return true;
	}
	
	public void render() {
		getModel().write(getStream(), getLanguage());
	}
}
