package com.unit4.tabular;

import com.unit4.tabular.csv.U4CSV;
import com.unit4.tabular.xml.U4SAX;

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

	public U4Input createInput(String uri) {
		if (uri.endsWith(".csv")) {
			return new U4CSV();
		} else if (uri.endsWith(".xml")) {
			return new U4SAX();
		} else {
			return null;
		}
	}
	
}
