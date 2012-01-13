package com.unit4.cli;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArgumentDeclaration {
	
//	Class.
	
//	Instance.
	
	protected Boolean value;
	protected ArgumentHandler handler;
	protected Set<String> names;
	
	public ArgumentDeclaration(Boolean value, ArgumentHandler handler, String[] names) {
		value(value);
		handler(handler);
		names(names);
	}
	
	protected void value(Boolean value) {
		this.value = value;
	}
	
	public Boolean value() {
		return this.value;
	}
	
	protected void handler(ArgumentHandler handler) {
		this.handler = handler;
	}
	
	public ArgumentHandler handler() {
		return this.handler;
	}
	
	protected void names(String[] names) {
		this.names = new HashSet<String>(Arrays.asList(names));
	}
	
	public Set<String> names() {
		return this.names;
	}
	
	public Boolean has(String name) {
		return names().contains(name);
	}
	
	public String toString() {
		return String.format("%s (%s)", handler().toString(), names().toString());
	}
}
