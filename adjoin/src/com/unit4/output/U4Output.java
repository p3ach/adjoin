package com.unit4.output;

import com.unit4.cli.Options;
import com.unit4.tabular.U4Common;


public interface U4Output extends Options {
	public void setCommon(U4Common common);
	public U4Common getCommon();
	
	public Boolean processInput();
	public Boolean write();
}
