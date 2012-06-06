package com.unit4.input;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;
import com.hp.hpl.jena.util.FileManager;
import com.unit4.cli.U4Options;
import com.unit4.exception.U4Exception;
import com.unit4.tabular.U4Common;

public class U4InputCSV implements U4InputI {
	private static Logger logger = LoggerFactory.getLogger(U4InputCSV.class);
	
	private U4Common common;
	private U4Options options;
	private String uri;
	private U4InputCallbackI callback;

	public U4InputCSV() {
	}
	
	@Override
	public U4InputI setCommon(U4Common common) {
		this.common = common;
		return this;
	}
	
	@Override
	public U4Common getCommon() {
		return this.common;
	}
	
	@Override
	public void setOptions(U4Options options) {
		this.options = options;
	}
	
	public U4Options getOptions() {
		return this.options;
	}
	
	@Override
	public U4InputI setCallback(U4InputCallbackI callback) {
		this.callback = callback;
		return this;
	}
	
	@Override
	public U4InputCallbackI getCallback() {
		return this.callback;
	}

	@Override
	public void parse() {
    	InputStream inputStream = FileManager.get().open(uri);
    	if (inputStream == null) {
    		throw new U4Exception(String.format("Unable to read [%s].", uri));
    	}
    	CsvReader csvReader = new CsvReader(inputStream, Charset.forName("UTF-8"));
    	
    	try {
			csvReader.readHeaders();
			getCommon().getColumns().setColumns(csvReader.getHeaders());
		} catch (IOException e) {
			logger.error("Unable to read input headers due to [{}].", e);
		}
    	
    	getCallback().header();
    	
		try {
			while (csvReader.readRecord()) {
				getCommon().getRow().setIndex(csvReader.getCurrentRecord());
				getCommon().getRow().setValues(csvReader.getValues());
				getCallback().beforeRow();
				getCallback().row();
				getCallback().afterRow();
			}
		} catch (IOException e) {
			logger.error("Unable to read input row due to [{}].", e);
		}

    	getCallback().footer();
		
    	csvReader.close();
	}
	
	protected String getURI() {
		return (String) getOptions().getOption("inputURI");
	}
}
