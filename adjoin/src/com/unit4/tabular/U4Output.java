package com.unit4.tabular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.unit4.vocabulary.U4RDF;
import com.unit4.vocabulary.U4RDFS;

public class U4Output {
	private static Logger logger = LoggerFactory.getLogger(U4Output.class);
	
	private Model output;
	
	public U4Output() {
		setModel(ModelFactory.createDefaultModel());
		common();
	}
	
	public void common() {
		Model model = getModel();
		U4RDF.setNsPrefix(model);
		U4RDFS.setNsPrefix(model);
	}

	public void setModel(Model output) {
		this.output = output;
	}
	
	public Model getModel() {
		return this.output;
	}
}
