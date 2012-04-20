package com.unit4.input;

import com.unit4.cli.Options;
import com.unit4.tabular.U4Common;

public interface U4Input {
	public U4Input setCommon(U4Common common);
	public U4Common getCommon();
	
	public void setOptions(Options options);
	public Options getOptions();
	
	public U4Input setCallback(U4InputCallback callback);
	public U4InputCallback getCallback();
	
	public void parse();
	public void parse(String uri);
}
