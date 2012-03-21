package com.unit4.vocabulary;

import java.util.LinkedList;

import com.hp.hpl.jena.rdf.model.ModelFactory;

public class U4AdJoinTemplate extends U4AdJoin {
	
//	Class
	
	public static final String DEFAULT_TEMPLATE_URI = "http://id.unit4.com/template/AdJoin";
	public static final String DEFAULT_TEMPLATE_GROUP = "Default";

//	Instance
	
	private final LinkedList<String> groups = new LinkedList<String>();
	
	public U4AdJoinTemplate() {
		super(ModelFactory.createDefaultModel().createResource(DEFAULT_TEMPLATE_URI));
	}
	
    public U4AdJoinTemplate addTemplate(String uri) {
    	readModel(uri);
    	return this;
    }

    public LinkedList<String> getGroups() {
    	return this.groups;
    }

    /**
     * Add a group as the first element of the Group list i.e. LIFO.
     * @param name
     */
    public U4AdJoinTemplate addGroup(String name) {
    	getGroups().addFirst(name);
    	return this;
    }

}
