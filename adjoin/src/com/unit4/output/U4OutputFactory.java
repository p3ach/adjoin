package com.unit4.output;

public class U4OutputFactory {
	
	// Inner.
	
	private static class Holder { 
        public static final U4OutputFactory instance = new U4OutputFactory();
	}

	// Class.
	
	public static U4OutputFactory getInstance() {
	        return Holder.instance;
	}
	
	// Instance.
	
	/**
	 * Cannot be instantiated.
	 */
	private U4OutputFactory() {
	}

	public U4Output createOutputByType(String type) {
		if (type == "RDF") {
			return new U4OutputRDF();
		} else {
			return null;
		}
	}
}
