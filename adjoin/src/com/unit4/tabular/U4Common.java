package com.unit4.tabular;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class U4Common {
	private static Logger logger = LoggerFactory.getLogger(U4Common.class);
	
	public static String createURI(String uri) {
		logger.debug("createURI({})", uri);
		String returnURI = uri.replaceAll("[^a-zA-Z0-9:/.~-]", "_");
		logger.trace("returnURI={}", returnURI);
		return returnURI;
	}
	
	private HashMap<String,Object> values = new HashMap<String, Object>();
	
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
		if (has(key)) {
			Object value = getValues().get(key);
			logger.trace("getValue(key={}) value={}", key, value);
			return value;
		} else {
			logger.trace("getValue(key={}) Key does not exist.", key);
			return null;
		}
	}
	
	public Object getValueIndirect(String key) {
		logger.trace("getValueIndirect(key={})", key);
		return getValue((String) getValue(key));
	}
	
	public Boolean has(String key) {
		return getValues().containsKey(key);
	}
	
	public Long countValue(String key) {
		Long count;
		if (has(key)) {
			count = (Long) getValue(key);
		} else {
			count = Long.valueOf(0);
		}
		setValue(key, count++);
		return count;
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
