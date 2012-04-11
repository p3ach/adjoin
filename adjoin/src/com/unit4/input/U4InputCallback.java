package com.unit4.input;

/**
 * Extend this interface to provide the callback for Objects implementing U4Input.
 *  
 * @author dick
 *
 */
public interface U4InputCallback {
	public U4InputCallback header();
	public U4InputCallback beforeRow();
	public U4InputCallback row();
	public U4InputCallback afterRow();
	public U4InputCallback footer();
}
