package com.unit4.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class U4FileManagerC {
	
	// Inner.
	
	private static class Holder { 
        public static final U4FileManagerC instance = new U4FileManagerC();
	}

	// Instance.
	
	private static Logger logger = LoggerFactory.getLogger(U4FileManagerC.class);
	
	public static String FILE_PREFIX = "file:";
	public static String HTTP_PREFIX = "http:";
	public static String FTP_PREFIX = "ftp:";
	
	// Class.
	
	public static U4FileManagerC getInstance() {
	        return Holder.instance;
	}
	
	// Instance.
	
	/**
	 * Cannot be instantiated.
	 */
	private U4FileManagerC() {
	}

	public U4FileManagerI open(String uri) {
		if (uri.startsWith(FILE_PREFIX)) {
			return null;
		} else if (uri.startsWith(HTTP_PREFIX)) {
			return null;
		} else if (uri.startsWith(FTP_PREFIX)) {
			return new FTP(uri);
		} else {
			return null;
		}
	}
	
	private InputStream urlOpen(String uri) {
		try {
			final URL url = new URL(uri);
			try {
				final URLConnection urlConnection = url.openConnection();
				urlConnection.connect();
				return urlConnection.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
