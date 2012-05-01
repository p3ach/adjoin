package com.unit4.cli;

import java.util.HashMap;
import java.util.Map;

public class Options {
	private Map<String, Object> options;
	
	public Options() {
		setOptions(new HashMap<String, Object>());
	}
	
	public Options(Map<String, Object> options) {
		setOptions(options);
	}
	
	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public Map<String, Object> getOptions() {
		return this.options;
	}
	
	public void setOption(String key, Object value) {
		getOptions().put(key, value);
	}
	
	public Object getOption(String key) {
		return getOptions().get(key);
	}

	public Object getOption(String key, Object defaultValue) {
		if (hasOption(key)) {
			return options.get(key);
		} else {
			return defaultValue;
		}
	}
	
	public Boolean hasOption(String key) {
		return getOptions().containsKey(key);
	}

	public void removeOptions() {
		getOptions().clear();
	}

	public void removeOption(String key) {
		getOptions().remove(key);
	}
	
	@Override
	public String toString() {
		return String.format("U4Options [%s]", getOptions().toString());
	}
}
