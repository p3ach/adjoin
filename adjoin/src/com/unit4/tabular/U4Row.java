package com.unit4.tabular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * U4Row
 * 
 * Implements Iterable<String>, Iterator<String>.
 * 
 * @author dick
 *
 */
public class U4Row implements Iterable<String>, Iterator<String> {
	private static Logger logger = LoggerFactory.getLogger(U4Row.class);
	
	private U4Common common;

	private Long index;

	private final List<String> values = new ArrayList<String>();
	private Iterator<String> iterator;
	
//	Constructor
	
	public U4Row() {
	}

//	Iterable
	
	@Override
	public Iterator<String> iterator() {
		getColumns().setIndex(-1);
		this.iterator = getValues().iterator(); 
		return this;
	}

//	Iterator

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public String next() {
		getColumns().setIndex(getColumns().getIndex()+1);
		return iterator.next();
	}

	@Override
	public void remove() {
	}

//	Set/Get
	
	public void setCommon(U4Common common) {
		this.common = common;
	}
	
	public U4Common getCommon() {
		return this.common;
	}

	public U4Columns getColumns() {
		return getCommon().getColumns();
	}
	
	public void setIndex(Long index) {
		this.index = index;
	}
	
	public Long getIndex() {
		return this.index;
	}

	public U4Row setValues(String[] values) {
		setValues(new ArrayList<String>(Arrays.asList(values)));
		return this;
	}
	
	public U4Row setValues(List<String> values) {
		this.values.clear();
		this.values.addAll(values);
		return this;
	}
	
	public List<String> getValues() {
		return this.values;
	}
	
	public Boolean hasValue() {
		final Boolean hasValue = hasValue(getColumns().getIndex());
		logger.trace("hasValue()={}", hasValue); 
		return hasValue;
	}

	public Boolean hasValue(Integer index) {
		final Boolean hasValue = (getValues().get(index) != null);
		logger.trace("hasValue({})={}", index, hasValue);
		return hasValue;
	}
	
	/**
	 * Get the value for the current Columns index.
	 * @return
	 */
	public String getValue() {
		final String getValue = getValue(getColumns().getIndex());
		logger.trace("getValue()={}", getValue); 
		return getValue;
	}
	
	/**
	 * Returns the value with the specified index.
	 * @param index
	 * @return
	 */
	public String getValue(Integer index) {
		final String getValue = getValues().get(index);
		logger.trace("getValue({})={}", index, getValue);
		return getValue;
	}

	/**
	 * Returns the value with the specified column name.
	 * The name is used to fetch the index from the Columns.
	 * @param name
	 * @return
	 */
	public String getValue(String name) {
		return getValue(getColumns().getIndex(name));
	}
	
	/**
	 * Returns the value with the specified key.
	 * The key is used to get the name from the Common.
	 * The name is then used to get the value.
	 * @param key
	 * @return
	 */
	public String getValueIndirect(String key) {
		return getValue((String) getCommon().getValue(key));
	}
	
	public String toString() {
		return getValues().toString(); 
	}
}
