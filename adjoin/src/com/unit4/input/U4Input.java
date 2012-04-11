package com.unit4.input;

import com.unit4.cli.Options;
import com.unit4.tabular.U4Common;

public interface U4Input extends Options {
	public U4Input setCommon(U4Common common);
	public U4Common getCommon();

	public U4Input setURI(String uri);
	public String getURI();
	
	public U4Input setCallback(U4InputCallback callback);
	public U4InputCallback getCallback();
	
	public void parse();
	public void parse(String uri);
}
