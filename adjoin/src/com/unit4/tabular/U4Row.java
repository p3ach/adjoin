package com.unit4.tabular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class U4Row {
	private static Logger logger = LoggerFactory.getLogger(U4Row.class);
	
	private U4Common common = null;
	private U4Columns columns = null;
	private Long index = null;
	private String[] values = null;
	
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
		this.values = values;
	}
	
	public String[] getValues() {
		return this.values;
	}
	
	public String getValue(Integer index) {
		logger.debug("getValue({})", index);
		return getValues()[index];
	}
	
	public String getValue(String name) {
		logger.debug("getValue({})", name);
		return getValues()[getColumns().getIndex(name)];
	}
	
	public String getValueIndirect(String key) {
		logger.trace("getValueIndirect(key={})", key);
		return getValue((String) getCommon().getValue(key));
	}
	
	public String getValue() {
		logger.debug("getValue()");
		return getValue(getColumns().getIndex());
	}
}
