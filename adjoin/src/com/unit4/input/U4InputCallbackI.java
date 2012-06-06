package com.unit4.input;

/**
 * Extend this interface to provide the callback for Objects implementing U4Input.
 *  
 * @author dick
 *
 */
public interface U4InputCallbackI {
	public U4InputCallbackI header();
	public U4InputCallbackI beforeRow();
	public U4InputCallbackI row();
	public U4InputCallbackI afterRow();
	public U4InputCallbackI footer();
}
