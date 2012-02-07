package com.unit4.cli;

public class Argument {

//	Class.
	
//	Instance.

    protected String text = null;
	protected String name = null;
	protected String value = null;

	public Argument(String text) {
		setText(text);

		if (text.startsWith("--")) { // Long form argument i.e. --help
	        text = text.substring(2) ;
		} else if (text.startsWith("-")) { // Short form argument i.e. -h
	        text = text.substring(1);
	    } else { // Value argument i.e. fred
	    	setValue(text);
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
        	setName(text);
        } else { // i.e. one or both of "=" or ":"
	        setName(text.substring(0,j));
	        setValue(text.substring(j+1));
        }
	}
	
	public Argument(String name, String value) {
		setName(name);
		setValue(value);
	}
	
	protected void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

	public Boolean isShortForm() {
		return (getValue().startsWith("-") && !value.startsWith("--"));
	}

	public Boolean isLongForm() {
		return getValue().startsWith("--");
	}

	public Boolean isValueForm() {
		String value = getValue();
		return ((value == null ? false : !getValue().startsWith("-")));
	}
	
	public Boolean hasValue() {
		String value = getValue();
		return (value.indexOf("=") != 0 || value.indexOf(":") != 0);
	}
	
	public String toString() {
		return String.format("Argument [%s]", getText());
	}
}
