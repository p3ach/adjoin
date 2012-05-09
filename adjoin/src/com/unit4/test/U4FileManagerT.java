package com.unit4.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.unit4.file.U4FileManagerC;
import com.unit4.file.U4FileManagerI;

public class U4FileManagerT {

	private static String HTTP_FILE = "http://data.companieshouse.gov.uk/doc/company/06987288.rdf";

	private static String FTP_FILE = "ftp://anonymous:password@sunsite.unc.edu/pub/docs/rfc/rfc999.txt";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final U4FileManagerI fileManager = U4FileManagerC.getInstance().open(FTP_FILE);
		System.out.println(fileManager.toString());
		final InputStream inputStream = fileManager.open();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "US-ASCII"));
	    String line;
	    while ((line = br.readLine()) != null) {
	    	System.out.println(line);
	    }
	    inputStream.close();
		fileManager.close();
	}

}
