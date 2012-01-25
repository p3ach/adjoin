package com.unit4.tabular;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit4.vocabulary.U4AdJoin;

public class U4Columns {
	private static Logger logger = LoggerFactory.getLogger(U4Columns.class);
	
	protected Map<Integer, String> indexes = new HashMap<Integer, String>();
	protected Map<String, Integer> names = new HashMap<String, Integer>();
	protected Map<Integer, U4AdJoin> matches = new HashMap<Integer, U4AdJoin>();
	
	protected Integer index;
	
	public U4Columns() {
	}
	
	public U4Columns(String[] names) {
		String name;
		for (Integer index = 0; index < names.length; index += 1) {
			name = names[index];
			this.indexes.put(index, name);
			this.names.put(name, index);
		}
	}
	
	public Map<Integer, String> getIndexes() {
		return this.indexes;
	}
	
	public Map<String, Integer> getNames() {
		return this.names;
	}
	
	public Integer getIndex(String name) {
		logger.debug("getIndex({})", name);
		if (getNames().containsKey(name)) {
			return getNames().get(name);
		} else {
			logger.warn("No index for {}", name);
			return null;
		}
	}
	
	public String getName() {
		logger.debug("getName()");
		return getName(getIndex());
	}
	
	public String getName(Integer index) {
		logger.debug("getName({})", index);
		return getIndexes().get(index);
	}

	public Map<Integer, U4AdJoin> getMatches() {
		return this.matches;
	}
	
	public void setMatch(Integer index, U4AdJoin match) {
		getMatches().put(index, match);
	}
	
	public Boolean hasMatch(Integer index) {
		return (getMatch(index) != null);
	}
	
	public U4AdJoin getMatch(Integer index) {
		logger.debug("getMatch({})", index);
		return getMatches().get(index);
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public Integer getIndex() {
		logger.debug("getIndex()");
		return this.index;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(getIndexes().toString() + "\n");
		sb.append(getNames().toString() + "\n");
		sb.append(getMatches().toString());
		return sb.toString();
	}
}
