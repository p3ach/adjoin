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
		setValue(value);
		setHandler(handler);
		setNames(names);
	}
	
	protected void setValue(Boolean value) {
		this.value = value;
	}
	
	public Boolean getValue() {
		return this.value;
	}
	
	protected void setHandler(ArgumentHandler handler) {
		this.handler = handler;
	}
	
	public ArgumentHandler getHandler() {
		return this.handler;
	}
	
	protected void setNames(String[] names) {
		this.names = new HashSet<String>(Arrays.asList(names));
	}
	
	public Set<String> getNames() {
		return this.names;
	}
	
	public Boolean hasName(String name) {
		return getNames().contains(name);
	}
	
	public String toString() {
		return String.format("ArgumentDeclaration [%s] [%s] [%s].", getValue(), getHandler().toString(), getNames().toString());
	}
}
