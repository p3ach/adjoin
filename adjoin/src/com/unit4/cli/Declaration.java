package com.unit4.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.unit4.ebnf.EBNF;

public class Declaration {
	
//	Class.
	
	public static final Boolean REQUIRE_VALUE = true;
	public static final Boolean NO_REQUIRE_VALUE = false;
	
	public static final List<String> EMPTY_VALID_VALUES = new ArrayList<String>();
	
	public static final Handler NULL_HANDLER = null;
	
	public static final List<String> EMPTY_NAMES = new ArrayList<String>();
	
	public final static Boolean LOOK_FOR = true;
	public final static Boolean NO_LOOK_FOR = false;
	
	public final static Boolean END_CLI = true;
	public final static Boolean NO_END_CLI = false;
	
//	Instance.
	
	private Boolean requireValue;
	private List<String> validValues;
	private Handler handler;
	private List<String> names;
	private Boolean lookFor;
	private Boolean endCLI;

//	Constructors.
	
	public Declaration() {
		this(NO_REQUIRE_VALUE, null, NULL_HANDLER, EMPTY_NAMES, NO_LOOK_FOR, NO_END_CLI);
	}

	/**
	 * Declare a value handler. 
	 * @param handler
	 */
	public Declaration(Handler handler) {
		this(NO_REQUIRE_VALUE, null, handler, null, NO_LOOK_FOR, NO_END_CLI);
	}
	
	public Declaration(Handler handler, List<String> names) {
		this(NO_REQUIRE_VALUE, null, handler, names, NO_LOOK_FOR, NO_END_CLI);
	}
	
	public Declaration(Boolean requireValue, Handler handler, List<String> names) {
		this(requireValue, null, handler, names, NO_LOOK_FOR, NO_END_CLI);
	}
	
	public Declaration(Handler handler, List<String> names, Boolean lookFor) {
		this(NO_REQUIRE_VALUE, null, handler, names, lookFor, NO_END_CLI);
	}
	
	public Declaration(Boolean requireValue, List<String> validValues, Handler handler, List<String> names, Boolean lookFor, Boolean endCLI) {
		setRequireValue(requireValue);
		setValidValues(validValues);
		setHandler(handler);
		setNames(names);
		setLookFor(lookFor);
		setEndCLI(endCLI);
	}
	
//	Set/Get (Has).
	
	public Declaration setRequireValue(Boolean value) {
		this.requireValue = value;
		return this;
	}
	
	public Boolean getRequireValue() {
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
		if (name == null) {
			return (getNames() == null);
		} else {
			 if (getNames() == null) {
				 return false;
			 } else {
				 return getNames().contains(name);
			 }
		}
	}
	
	public Declaration setLookFor(Boolean lookFor) {
		this.lookFor = lookFor;
		return this;
	}
	
	public Boolean getLookFor() {
		return this.lookFor;
	}
	
	public Declaration setEndCLI(Boolean endCLI) {
		this.endCLI = endCLI;
		return this;
	}
	
	public Boolean getEndCLI() {
		return this.endCLI;
	}
	
	public Boolean equals(Argument argument) {
		return hasName(argument.getName());
	}
	
	public Boolean equals(List<Argument> arguments) {
		for (Argument argument : arguments) {
			if (hasName(argument.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return String.format("Declaration [%s] requiresValue[%s] validValues[%s] lookFor[%s] endCLI[%s]", getNames(), getRequireValue(), getValidValues(), getLookFor(), getEndCLI());
	}
}
