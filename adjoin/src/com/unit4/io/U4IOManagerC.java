package com.unit4.io;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit4.exception.U4Exception;

public class U4IOManagerC {
	
	// Inner.
	
	private static class Holder { 
        public static final U4IOManagerC instance = new U4IOManagerC();
	}

	// Instance.
	
	private static Logger logger = LoggerFactory.getLogger(U4IOManagerC.class);
	
	public static String FILE_PREFIX = "file:";
	public static String HTTP_PREFIX = "http:";
	public static String FTP_PREFIX = "ftp:";
	
	// Class.
	
	public static U4IOManagerC getInstance() {
	        return Holder.instance;
	}
	
	// Instance.
	
	/**
	 * Cannot be instantiated.
	 */
	private U4IOManagerC() {
	}

	public InputStream open(String uri) {
		if (uri.startsWith(FILE_PREFIX)) {
			return null;
		} else if (uri.startsWith(HTTP_PREFIX)) {
			return new U4HTTPInputStreamC(uri);
		} else if (uri.startsWith(FTP_PREFIX)) {
			return new U4FTPInputStreamC(uri);
		} else {
			throw new U4Exception(String.format("Unknown protocol [%s].", uri));
		}
	}
}
