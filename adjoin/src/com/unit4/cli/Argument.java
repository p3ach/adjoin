package com.unit4.cli;

public class Argument {

//	Class.
	
//	Instance.

    protected String text = null;
	protected String name = null;
	protected String value = null;

	public Argument(String text) {
		text(text);

		if (text.startsWith("--")) { // Long form argument i.e. -h
	        text = text.substring(2) ;
		} else if (text.startsWith("-")) { // Short form argument i.e. --help
	        text = text.substring(1);
	    } else { // Value argument i.e. fred
	    	value(text);
	    	return;
	    }
        
		// Split if the text has a "=" or ":" i.e. it is long form --arg:value --arg=value.
        int equal = text.indexOf('=') ;
        int colon = text.indexOf(':') ;
        int j = Integer.MAX_VALUE ;

        // Determine whether "=" or ":" comes first.
        if ( equal > 0 && equal < j ) // check for equal first
            j = equal ;
        if ( colon > 0 && colon < j ) // check for 
            j = colon ;

        if (j == Integer.MAX_VALUE) { // i.e. no "=" or ":"
        	name(text);
        } else { // i.e. one or both of "=" or ":"
	        name(text.substring(0,j));
	        value(text.substring(j+1));
        }
	}
	
	protected void text(String text) {
		this.text = text;
	}
	
	public String text() {
		return this.text;
	}
	
	public Argument(String name, String value) {
		name(name);
		value(value);
	}
	
	protected void name(String name) {
		this.name = name;
	}
	
	public String name() {
		return this.name;
	}
	
	protected void value(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}

	public Boolean shortForm() {
		return (value().startsWith("-") && !value.startsWith("--"));
	}

	public Boolean longForm() {
		return value().startsWith("--");
	}

	public Boolean valueForm() {
		String value = value();
		return ((value == null ? false : !value().startsWith("-")));
	}
	
	public Boolean hasValue() {
		String value = value();
		return (value.indexOf("=") != 0 || value.indexOf(":") != 0);
	}
	
	public String toString() {
		return String.format("%s", text());
	}
}
