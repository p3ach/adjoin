package com.unit4.tabular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class U4Row {
	private static Logger logger = LoggerFactory.getLogger(U4Row.class);
	
	private U4Common common;
	private U4Columns columns;
	private Long index;
	private List<String> values;
	
	public U4Row() {
	}

	public void setCommon(U4Common common) {
		this.common = common;
	}
	
	public U4Common getCommon() {
		return this.common;
	}
	
	public void setColumns(U4Columns columns) {
		this.columns = columns;
	}

	public U4Columns getColumns() {
		return this.columns;
	}
	
	public void setIndex(Long index) {
		this.index = index;
	}
	public Long getIndex() {
		return this.index;
	}
	
	public void setValues(String[] values) {
		setValues(new ArrayList<String>(Arrays.asList(values)));
	}
	
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public List<String> getValues() {
		return this.values;
	}
	
	/**
	 * Returns the value with the specified index.
	 * @param index
	 * @return
	 */
	public String getValue(Integer index) {
		return getValues().get(index);
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
	
	/**
	 * Get the value for the current Columns index.
	 * @return
	 */
	public String getValue() {
		return getValue(getColumns().getIndex());
	}
	
	public String toString() {
		return getValues().toString(); 
	}
}
