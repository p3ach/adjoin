package com.unit4.cli;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class U4Options {
	
	private class U4Option {
		private final String key;
		private final Object value;
		private final Set<String> groups = new HashSet<String>();
		public U4Option(String key, Object value) {
			this.key = key;
			this.value = value;
			this.groups.add(null);
		}
		public String getKey() {
			return this.key;
		}
		public Object getValue() {
			return this.value;
		}
		public Object getValue(String group) {
			return (inGroup(group) ? this.value : null);
		}
		public Set<String> getGroups() {
			return this.groups;
		}
		public Boolean inGroup(String group) {
			return this.groups.contains(group);
		}
		public String toString() {
			return String.format("U4Option [%s, %s, %s]", getKey(), getValue().toString(), getGroups());
		}
	}
	
	private final Map<String, U4Option> options = new HashMap<String, U4Option>();
	
	public U4Options() {
	}
	
	public U4Options setOption(String key, Object value) {
		getOptions().put(key, new U4Option(key, value));
		return this;
	}

	private Map<String, U4Option> getOptions() {
		return this.options;
	}
	
	public Object getOption(String key) {
		return getOptions().get(key).getValue();
	}

	public Object getOption(String key, Object defaultValue) {
		if (hasOption(key)) {
			return getOption(key);
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
