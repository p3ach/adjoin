package com.unit4.cli;

import java.util.Map;

public interface Options {
	public void setOptions(Map<String, Object> options);
	public Map<String, Object> getOptions();
	public void setOption(String key, Object value);
	public Object getOption(String key);
	public Object getOption(String key, Object defaultValue);
	public Boolean hasOption(String key);
	public void removeOptions();
	public void removeOption(String key);
}
