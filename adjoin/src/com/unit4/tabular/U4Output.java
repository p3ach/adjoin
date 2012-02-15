package com.unit4.tabular;

import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.unit4.vocabulary.U4DCTerms;
import com.unit4.vocabulary.U4FOAF;
import com.unit4.vocabulary.U4Org;
import com.unit4.vocabulary.U4RDF;
import com.unit4.vocabulary.U4RDFS;
import com.unit4.vocabulary.U4VoID;
import com.unit4.vocabulary.U4XSD;

public class U4Output {
	private static Logger logger = LoggerFactory.getLogger(U4Output.class);
	
	private Model output;
	private String language = null;
	private OutputStream stream = System.out;
	
	public U4Output() {
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
	
	public void setLanguage(String language) {
		this.language = language;
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
	
	public void render() {
		getModel().write(getStream(), getLanguage());
	}
}
