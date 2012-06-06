package com.unit4.tabular;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Container;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.unit4.cli.U4Options;
import com.unit4.input.U4InputI;
import com.unit4.output.U4Output;
import com.unit4.vocabulary.U4AdJoinTemplate;

public class U4Common {
	
	// Class.
	
	private static Logger logger = LoggerFactory.getLogger(U4Common.class);
	
	public static String createURI(String uri) {
		logger.debug("createURI({})", uri);
		String returnURI = uri.replaceAll("[^a-zA-Z0-9:/.~-]", "_");
		logger.trace("returnURI={}", returnURI);
		return returnURI;
	}

	// Instance.
	
	private U4InputI input;
	private U4Output output;
	private U4Options options;
	private U4AdJoinTemplate template;
	private U4Columns columns;
	private U4Row row;

	private HashMap<String,Object> values = new HashMap<String, Object>();
	
	public U4Common() {
		common();
	}
	
	public void setInput(U4InputI input) {
		this.input = input;
		input.setCommon(this);
	}
	
	public U4InputI getInput() {
		return this.input;
	}
	
	public void setOutput(U4Output output) {
		this.output = output;
		output.setCommon(this);
	}
	
	public U4Output getOutput() {
		return this.output;
	}

	public void setOptions(U4Options options) {
		this.options = options;
	}
	
	public U4Options getOptions() {
		return this.options;
	}
	
	public void setTemplate(U4AdJoinTemplate template) {
		this.template = template;
	}
	
	public U4AdJoinTemplate getTemplate() {
		return this.template;
	}
	
	public void setColumns(U4Columns columns) {
		this.columns = columns;
		columns.setCommon(this);
	}
	
	public U4Columns getColumns() {
		return this.columns;
	}
	
	public void setRow(U4Row row) {
		this.row = row;
		row.setCommon(this);
	}
	
	public U4Row getRow() {
		return this.row;
	}
	
	public void common() {
		setValue("commonUUID", UUID.randomUUID().toString());
	    
	    Date timestamp = new Date();
	    setValue("commonDate", new SimpleDateFormat("dd/MM/yyyy").format(timestamp));
	    setValue("commonTime", new SimpleDateFormat("HH:mm:ss").format(timestamp));
        setValue("commonTimestamp", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timestamp));
	}
	
	public HashMap<String,Object> getValues() {
		return this.values;
	}
	
	public Object setValue(String key, Object value) {
		getValues().put(key, value);
		return value;
	}
	
	/**
	 * Gets the value associated with a key.
	 * @param key
	 * @return
	 * Object : The value associated with the key or Null if the key does not exist. 
	 */
	public Object getValue(String key) {
		if (hasKey(key)) {
			Object value = getValues().get(key);
			logger.trace("getValue(key={}) value={}", key, value);
			return value;
		} else {
			logger.warn("getValue(key={}) Key does not exist.", key);
			return null;
		}
	}
	
	public Object getValueIndirect(String key) {
		logger.trace("getValueIndirect(key={})", key);
		return getValue((String) getValue(key));
	}
	
	public void removeValue(String key) {
		if (hasKey(key)) {
			Object value = getValues().remove(key);
			logger.trace("removeValue(key={}) value={}", key, value);
		} else {
			logger.warn("removeValue(key={}) Key does not exist.", key);
		}
	}
	
	public Boolean hasKey(String key) {
		return getValues().containsKey(key);
	}
	
	public Long countValue(String key) {
		Long count;
		if (hasKey(key)) {
			count = (Long) getValue(key);
		} else {
			count = Long.valueOf(0);
		}
		count++;
		setValue(key, count);
		return count;
	}
	
	public Long incrementValue(Long value) {
		return (value == null ? null : value++);
	}
	
	public Long countValueIndirect(String key) {
		logger.trace("countValueIndirect(key={})", key);
		return countValue((String) getValue(key));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (String key : getValues().keySet()) {
			sb.append(String.format("[%s, %s]\n", key, getValues().get(key)));
		}
		return sb.toString();
	}
}
