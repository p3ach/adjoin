package com.unit4.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Declaration {
	
//	Class.
	
	public static final Boolean REQUIRE_VALUE = true;
	public static final Boolean NO_REQUIRE_VALUE = false;
	
	public static final List<String> EMPTY_VALID_VALUES = new ArrayList<String>();
	
	public static final Handler NULL_HANDLER = null;
	
	public static final List<String> EMPTY_NAMES = new ArrayList<String>();
	
	public final static Boolean LOOK_FOR = true;
	public final static Boolean NO_LOOK_FOR = false;
	
//	Instance.
	
	private Boolean requireValue;
	private List<String> validValues;
	private Handler handler;
	private List<String> names;
	private Boolean lookFor;

//	Constructors.
	
	public Declaration() {
		this(NO_REQUIRE_VALUE, EMPTY_VALID_VALUES, NULL_HANDLER, EMPTY_NAMES, NO_LOOK_FOR);
	}

	public Declaration(Handler handler, List<String> names) {
		this(NO_REQUIRE_VALUE, EMPTY_VALID_VALUES, handler, names, NO_LOOK_FOR);
	}
	
	public Declaration(Boolean requireValue, Handler handler, List<String> names) {
		this(requireValue, EMPTY_VALID_VALUES, handler, names, NO_LOOK_FOR);
	}
	
	public Declaration(Handler handler, List<String> names, Boolean lookFor) {
		this(NO_REQUIRE_VALUE, EMPTY_VALID_VALUES, handler, names, lookFor);
	}
	
	public Declaration(Boolean requireValue, List<String> validValues, Handler handler, List<String> names, Boolean lookFor) {
		setrequireValue(requireValue);
		setValidValues(validValues);
		setHandler(handler);
		setNames(names);
		setLookFor(lookFor);
	}
	
//	Set/Get (Has).
	
	public Declaration setrequireValue(Boolean value) {
		this.requireValue = value;
		return this;
	}
	
	public Boolean getrequireValue() {
		return this.requireValue;
	}
	
	public Declaration setValidValues(List<String> validValues) {
		this.validValues = validValues;
		return this;
	}
	
	public Boolean hasValidValues() {
		return (this.validValues != null);
	}
	
	public List<String> getValidValues() {
		return this.validValues;
	}
	
	public Declaration setHandler(Handler handler) {
		this.handler = handler;
		return this;
	}
	
	public Handler getHandler() {
		return this.handler;
	}
	
	public Boolean hasHandler() {
		return (getHandler() != null);
	}
	
	public Declaration setNames(List<String> names) {
		this.names = names;
		return this;
	}
	
	public List<String> getNames() {
		return this.names;
	}
	
	public Boolean hasName(String name) {
		return getNames().contains(name);
	}
	
	public Declaration setLookFor(Boolean lookFor) {
		this.lookFor = lookFor;
		return this;
	}
	
	public Boolean getLookFor() {
		return this.lookFor;
	}
	
	public Boolean equals(Argument argument) {
		return getNames().contains(argument.getName());
	}
	
	public Boolean equals(List<Argument> arguments) {
		List<String> names = getNames();
		for (Argument argument : arguments) {
			if (names.contains(argument.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return String.format("Declaration [%s] [%s] [%s]", getrequireValue(), getHandler(), getNames());
	}
}
