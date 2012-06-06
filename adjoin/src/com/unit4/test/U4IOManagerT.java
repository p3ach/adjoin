package com.unit4.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.unit4.io.U4IOManagerC;

public class U4IOManagerT {

	private static String HTTP_FILE = "http://data.companieshouse.gov.uk/doc/company/06987288.rdf";

	private static String FTP_FILE = "ftp://anonymous:password@sunsite.unc.edu/pub/docs/rfc/rfc999.txt";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
//		read(U4FileManagerC.getInstance().open(HTTP_FILE));
		read(U4IOManagerC.getInstance().open(FTP_FILE));
	}

	private static void read(InputStream inputStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "US-ASCII"));
	    String line;
	    while ((line = br.readLine()) != null) {
	    	System.out.println(line);
	    }
	    inputStream.close();
	}
	
}
