package com.unit4.tabular;

public interface U4Input {
	/**
	 * Used to parse an input.
	 * @param uri
	 * @param common
	 */
	public void parse(String uri, U4Common common);
	
	/**
	 * Used as a callback from parse for each row read from the input.
	 * @param common
	 */
	public void row(U4Common common);
}
