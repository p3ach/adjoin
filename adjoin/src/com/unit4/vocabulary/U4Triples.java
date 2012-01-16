
package com.unit4.vocabulary;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.rdf.model.Statement;
import com.unit4.exception.Exception;
import com.unit4.vocabulary.U4Convert;
import com.unit4.vocabulary.U4Vocabulary;

public class U4Triples extends U4Vocabulary {
	private static Logger logger = LoggerFactory.getLogger(U4Triples.class);
	
	public U4Triples() {
	}

	public U4Triples(Resource subject) {
		super(subject);
	}
	
	public U4Triples(U4Vocabulary vocabulary) {
		super(vocabulary);
	}
	
	public Boolean hasBeforeTriples() {
		return hasProperty(U4Convert.beforeTriples);
	}
	
	public U4Convert getBeforeTriples() {
		return new U4Convert(getResource(U4Convert.beforeTriples));
	}
	
	public Boolean hasAfterTriples() {
		return hasProperty(U4Convert.afterTriples);
	}
	
	public U4Convert getAfterTriples() {
		return new U4Convert(getResource(U4Convert.afterTriples));
	}
	
	public List<U4Triple> read() {
		logger.debug("read()");
		Model model = getModel();
		if (model == null) {
			throw new Exception("model is null.");
		}
		Resource resource = getSubject();
		if (resource == null) {
			throw new Exception("resource is null.");
		}
		if (!resource.isAnon()) {
			throw new Exception("resource is not anonymous.");
		}
		Seq seq = model.getSeq(resource);
		logger.trace("seq.size()={}", seq.size());
		List<U4Triple> triples = new ArrayList<U4Triple>(seq.size());
		NodeIterator iterator = seq.iterator();
		while (iterator.hasNext()) {
			triples.add(new U4Triple(iterator.next().asResource()));
		}
		return triples;
	}
	
	public List<Statement> statements(VelocityContext context) {
		logger.debug("statement(context={})", context);
		if (context == null) {
			throw new Exception("context is null.");
		}
		List<Statement> statements = new ArrayList<Statement>();
		if (hasBeforeTriples()) {
			statements.addAll(getBeforeTriples().getTriples().statements(context));
		}
		Statement statement;
		for (U4Triple triple : read()) {
			if (triple.hasBefore()) {

			}
			statement = triple.read().statement(context);
			if (statement != null) {
				statements.add(triple.read().statement(context));
			}
			if (triple.hasAfter()) {
				
			}
		}
		if (hasAfterTriples()) {
			statements.addAll(getAfterTriples().getTriples().statements(context));
		}
		return statements;
	}
}
