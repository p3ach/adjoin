package com.unit4.adjoin.iati;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.unit4.vocabulary.U4IATI;
import com.unit4.vocabulary.U4RDFS;

public class IATI {
	private static Logger logger = LoggerFactory.getLogger(IATI.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		m(new U4IATI(U4IATI.FileHeader.getURI()));
		
		m(new U4IATI(ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF).createResource(U4IATI.FileHeader.getURI())));
		
		m(new U4IATI(ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF).createResource(U4IATI.FileHeader.getURI())));

		p(OntModelSpec.OWL_DL_MEM, "file:iati/iati.owl");
		p(OntModelSpec.OWL_DL_MEM_TRANS_INF, "file:iati/iati.owl");
		p(OntModelSpec.OWL_DL_MEM_RULE_INF, "file:iati/iati.owl");
	}

	static void m(U4IATI iati) {
		iati.readModel("file:iati.ttl");
		iati.readModel("file:language.ttl");
		iati.readModel("file:currency.ttl");
		
		logger.info("\nWithout ontology size {} {}", iati.toString(), iati.getModel().size());
		for (Statement s : iati.getStatements(U4RDFS.subClassOf, (RDFNode) null)) {
			logger.info("Object {}", s.toString());
		}

		StmtIterator iterator;
		
		logger.info("\nStatements for {} {}", RDFS.subClassOf, U4IATI.Activity);
		iterator = iati.getModel().listStatements((Resource) null, RDFS.subClassOf, U4IATI.Activity);
		while (iterator.hasNext()) {
			logger.info("{}", iterator.nextStatement());
		}
		
		iati = null;
	}
	
	static void p(OntModelSpec spec, String uri) {
		OntModel o = ModelFactory.createOntologyModel(spec);
		FileManager.get().readModel(o, uri);

		logger.info("\nWith {} ontology iati.owl size {}", spec.toString(), o.size());
		
		StmtIterator iterator = o.listStatements(o.createResource(o.getNsPrefixURI("") + "DfID"), RDFS.subClassOf, (RDFNode) null);
		while (iterator.hasNext()) {
			logger.info("{}", iterator.nextStatement());
		}
	}
	
}
