package com.unit4.adjoin.iati;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.unit4.vocabulary.U4IATI;
import com.unit4.vocabulary.U4RDFS;

public class IATI {
	private static Logger logger = LoggerFactory.getLogger(IATI.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		U4IATI iati;
		
		iati = new U4IATI(U4IATI.FileHeader.getURI());
		
		iati.readModel("file:iati.ttl");
		iati.readModel("file:language.ttl");
		iati.readModel("file:currency.ttl");
		
		logger.info("Size {} {}", iati.toString(), iati.getModel().size());
		for (Statement s : iati.getStatements(U4RDFS.subClassOf, (RDFNode) null)) {
			logger.info("Object {}", s.toString());
		}
		
		iati = null;

		iati = new U4IATI(ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF).createResource(U4IATI.FileHeader.getURI()));
		iati.readModel("file:iati.ttl");
		iati.readModel("file:language.ttl");
		iati.readModel("file:currency.ttl");
		
		logger.info("Size {} {}", iati.toString(), iati.getModel().size());
		for (Statement s : iati.getStatements(U4RDFS.subClassOf, (RDFNode) null)) {
			logger.info("Object {}", s.toString());
		}

		logger.info("{}", RDFS.subClassOf);
		StmtIterator iterator = iati.getModel().listStatements((Resource) null, RDFS.subClassOf, U4IATI.Activity);
		while (iterator.hasNext()) {
			logger.info("{}", iterator.nextStatement());
		}
		
		iati = null;
//		model.write(System.out, "RDF/XML");
	}

}
