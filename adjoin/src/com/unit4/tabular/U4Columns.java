package com.unit4.tabular;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit4.vocabulary.U4AdJoin;

public class U4Columns {
	private static Logger logger = LoggerFactory.getLogger(U4Columns.class);
	
	private U4Common common;
	
	private Map<Integer, String> indexes;
	private Map<String, Integer> names;
	private Map<Integer, U4AdJoin> matches = new HashMap<Integer, U4AdJoin>();
	
	private Integer index;
	
	public U4Columns() {
	}
	
	public U4Columns(String[] names) {
		setColumns(names);
	}
	
	public U4Columns(List<String> names) {
		setColumns(names);
	}
	
	public void setCommon(U4Common common) {
		this.common = common;
	}
	
	public U4Common getCommon() {
		return this.common;
	}
	
	public void setColumns(String[] names) {
		setColumns(Arrays.asList(names));
	}
	
	public void setColumns(List<String> names) {
		setIndexes(new HashMap<Integer, String>());
		setNames(new HashMap<String, Integer>());
		for (Integer index = 0; index < names.size(); index++) {
			this.indexes.put(index, names.get(index));
			this.names.put(names.get(index), index);
		}
	}
	
	private void setIndexes(Map<Integer, String> indexes) {
		this.indexes = indexes;
	}
	
	public Map<Integer, String> getIndexes() {
		return this.indexes;
	}
	
	private void setNames(Map<String, Integer> names) {
		this.names = names;
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
		logger.trace("getName()");
		return getName(getIndex());
	}
	
	public String getName(Integer index) {
		String name = getIndexes().get(index); 
		logger.trace("getName({})={}", index, name);
		return name;
	}

	public Map<Integer, U4AdJoin> getMatches() {
		return this.matches;
	}
	
	public void setMatch(U4AdJoin match) {
		setMatch(getIndex(), match);
	}
	
	public void setMatch(Integer index, U4AdJoin match) {
		getMatches().put(index, match);
	}
	
	public Boolean hasMatch() {
		return hasMatch(getIndex());
	}
	
	public Boolean hasMatch(Integer index) {
		return (getMatch(index) != null);
	}

	public U4AdJoin getMatch() {
		return getMatch(getIndex());
	}
	
	public U4AdJoin getMatch(Integer index) {
		final U4AdJoin getMatch = getMatches().get(index); 
		logger.trace("getMatch({})={}", index, getMatch);
		return getMatch;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public Integer getIndex() {
		logger.debug("getIndex()");
		return this.index;
	}
	
	public String toString() {
		return getIndexes().toString();
	}
}
