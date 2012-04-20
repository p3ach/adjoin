package com.unit4.output;

import com.unit4.cli.Options;
import com.unit4.tabular.U4Common;


public interface U4Output {
	public void setCommon(U4Common common);
	public U4Common getCommon();
	
	public void setOptions(Options options);
	public Options getOptions();
	
	public Boolean processInput();
	public Boolean render();
}
