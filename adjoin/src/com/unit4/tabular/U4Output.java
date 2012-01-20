package com.unit4.tabular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class U4Output {
	private static Logger logger = LoggerFactory.getLogger(U4Output.class);
	
	private Model output = ModelFactory.createDefaultModel();
	
	public U4Output() {
		
	}

	public void setModel(Model output) {
		this.output = output;
	}
	
	public Model getModel() {
		return this.output;
	}
}
