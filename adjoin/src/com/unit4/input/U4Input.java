package com.unit4.input;

import com.unit4.tabular.U4Common;

public interface U4Input {
	public void setCommon(U4Common common);
	public U4Common getCommon();
	public void setCallback(U4InputCallback callback);
	public U4InputCallback getCallback();
	public void parse(String uri);
}
