package com.unit4.cli;

public class Argument {

//	Class.
	
//	Instance.

    private String text = null;
	private String name = null;
	private String value = null;

	public Argument() {
		
	}
	
	public Argument(String text) {
		setText(text);
	}
	
	public Argument setText(String text) {
		this.text = text;

		if (text.startsWith("--")) { // Long form argument i.e. --help
	        text = text.substring(2) ;
		} else if (text.startsWith("-")) { // Short form argument i.e. -h
	        text = text.substring(1);
	    } else { // Value argument i.e. fred
	    	setValue(text);
	    	return this;
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
        
        return this;
	}
	
	public String getText() {
		return this.text;
	}
	
	public Boolean hasText() {
		return (getText() != null);
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public Boolean hasName() {
		return (this.name != null);
	}
	
	protected void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public Boolean hasValue() {
		return (this.value != null);
	}

	public Boolean isShortForm() {
		String text = getText();
		return (text.startsWith("-") && !text.startsWith("--"));
	}

	public Boolean isLongForm() {
		return getText().startsWith("--");
	}

	public Boolean isValueForm() {
		return (getValue() != null);
	}
	
	public String toString() {
		return String.format("Argument text[%s] name[%s] value[%s] isShort[%s] isLong[%s] isValue[%s]", getText(), getName(), getValue(), isShortForm(), isLongForm(), isValueForm());
	}
}
