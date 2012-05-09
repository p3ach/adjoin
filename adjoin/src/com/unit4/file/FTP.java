package com.unit4.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit4.exception.Exception;

public class FTP implements U4FileManagerI {
	
	// Class.
	
	private static Logger logger = LoggerFactory.getLogger(FTP.class);

	private final FTPClient ftpClient = new FTPClient();
	private final URI uri;
	private final String host;
	private final String username;
	private final String password;
	private final String file;
	
	public FTP(String uri) {
		this.ftpClient.addProtocolCommandListener(new ProtocolCommandListener() {
			@Override
			public void protocolReplyReceived(ProtocolCommandEvent event) {
				logger.trace("Reply [{}].", event.getMessage());
			}
			
			@Override
			public void protocolCommandSent(ProtocolCommandEvent event) {
				logger.trace("Sent [{}].", event.getMessage());
			}
		});
		
		try {
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new Exception(e);
		}
		
		this.host = this.uri.getHost();

		final String userInfo = this.uri.getUserInfo();
		if (userInfo == null) {
			this.username = null;
			this.password = null;
		} else {
			this.username = userInfo.substring(0, userInfo.indexOf(":"));
			this.password = userInfo.substring(userInfo.indexOf(":") + 1);
		}
		
		this.file = this.uri.getPath();
	}
	
	// Instance.
	
	@Override
	public InputStream open() {
		try {
			this.ftpClient.connect(this.host);
		} catch (SocketException e) {
			throw new Exception("Failed to connect.", e);
		} catch (IOException e) {
			throw new Exception("Failed to connect.", e);
		}

		if(!FTPReply.isPositiveCompletion(this.ftpClient.getReplyCode())) {
	    	  try {
				this.ftpClient.disconnect();
			} catch (IOException e) {
				throw new Exception("Failed to disconnect.", e);
			}
	    	return null;
		}

		if (this.username != null) {
			try {
				if (!this.ftpClient.login(this.username, this.password)) {
					throw new Exception("Failed to login.");
				}
			} catch (IOException e) {
				throw new Exception("Failed to login.", e);
			}
		}

		final InputStream inputStream;
		try {
			inputStream = this.ftpClient.retrieveFileStream(this.file);
		} catch (IOException e) {
			throw new Exception("Failed to retrieve file.", e);
		}
		if (inputStream == null) {
			throw new Exception("Failed to retrieve file.");
		}

		return inputStream;
	}

	@Override
	public void close() {
		try {
			this.ftpClient.completePendingCommand();
		} catch (IOException e) {
			throw new Exception("Failed to complete pending command.", e);
		}
		
		if (this.username != null) {
			try {
				this.ftpClient.logout();
			} catch (IOException e) {
				throw new Exception("Failed to logout.", e);
			}
		}

		try {
			this.ftpClient.disconnect();
		} catch (IOException e) {
			throw new Exception("Failed to disconnect.", e);
		}
	}
	
	@Override
	public String toString() {
		return String.format("FTP uri[%s]", this.uri);
	}
}
