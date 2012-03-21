package com.unit4.input;

/**
 * Extend this interface to provide the callback for Objects implementing U4Input.
 *  
 * @author dick
 *
 */
public interface U4InputCallback {
	public void header();
	public void beforeRow();
	public void row();
	public void afterRow();
	public void footer();
}
