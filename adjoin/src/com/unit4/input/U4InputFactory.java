package com.unit4.input;


public class U4InputFactory {
	
	// Inner.
	
	private static class Holder { 
        public static final U4InputFactory instance = new U4InputFactory();
	}

	// Class.
	
	public static U4InputFactory getInstance() {
	        return Holder.instance;
	}
	
	// Instance.
	
	/**
	 * Cannot be instantiated.
	 */
	private U4InputFactory() {
	}

	public U4Input createInputByType(String type) {
		if (type == "csv") {
			return new U4InputCSV();
		} else if (type == "xml") {
			return new U4InputXML();
		} else {
			return null;
		}
	}
	
	public U4Input createInputByURI(String uri) {
		if (uri.endsWith(".csv")) {
			return new U4InputCSV().setURI(uri);
		} else if (uri.endsWith(".xml")) {
			return new U4InputXML().setURI(uri);
		} else {
			return null;
		}
	}
	
}
