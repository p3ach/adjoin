package com.unit4.input;

import com.unit4.tabular.U4Common;

public interface U4InputCallback {
	public void header(U4Common common);
	public void beforeRow(U4Common common);
	public void row(U4Common common);
	public void afterRow(U4Common common);
	public void footer(U4Common common);
}
