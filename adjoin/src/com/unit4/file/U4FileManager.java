package com.unit4.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit4.input.U4InputFactory;

public class U4FileManager {
	
	// Inner.
	
	private static class Holder { 
        public static final U4FileManager instance = new U4FileManager();
	}

	// Instance.
	
	private static Logger logger = LoggerFactory.getLogger(U4FileManager.class);
	
	// Class.
	
	public static U4FileManager getInstance() {
	        return Holder.instance;
	}
	
	// Instance.
	
	/**
	 * Cannot be instantiated.
	 */
	private U4FileManager() {
	}

	public InputStream open(String uri) {
		try {
			final URL url = new URL(uri);
			try {
				final URLConnection urlConnection = url.openConnection();
				urlConnection.connect();
				return urlConnection.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
