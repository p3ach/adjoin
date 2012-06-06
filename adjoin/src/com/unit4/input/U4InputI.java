package com.unit4.input;

import com.unit4.cli.U4Options;
import com.unit4.tabular.U4Common;

public interface U4InputI {
	public U4InputI setCommon(U4Common common);
	public U4Common getCommon();
	
	public void setOptions(U4Options options);
	public U4Options getOptions();
	
	public U4InputI setCallback(U4InputCallbackI callback);
	public U4InputCallbackI getCallback();
	
	public void parse();
}
