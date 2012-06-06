package com.unit4.input;


public class U4InputF {
	
	// Inner.
	
	private static class Holder { 
        public static final U4InputF instance = new U4InputF();
	}

	// Class.
	
	public static U4InputF getInstance() {
	        return Holder.instance;
	}
	
	// Instance.
	
	/**
	 * Cannot be instantiated.
	 */
	private U4InputF() {
	}

	public U4InputI createInputByType(String type) {
		if (type == "csv") {
			return new U4InputCSV();
		} else if (type == "xml") {
			return new U4InputXML();
		} else {
			return null;
		}
	}
	
	public U4InputI createInputByURI(String uri) {
		if (uri.endsWith(".csv")) {
			return new U4InputCSV();
		} else if (uri.endsWith(".xml")) {
			return new U4InputXML();
		} else {
			return null;
		}
	}
	
}
