package br.com.luciano.common;

public class ChatException extends Exception {

	private static final long serialVersionUID = -4152325091479746014L;

	public ChatException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ChatException(String message) {
		super(message);
		
	}
	
	

}
