package com.niffy.IsometricWorld;

public class GenericClassCastException extends Exception {

	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final long serialVersionUID = -6368568431393095125L;
	// ===========================================================
	// Fields
	// ===========================================================
	/**
	 * The message to display
	 */
	public String Message;
	/**
	 * What is the original error?
	 */
	public Throwable Error;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * 
	 * @param pMessage
	 *            The message to display
	 * @param pError
	 *            What is the original error
	 */
	public GenericClassCastException(String pMessage, Throwable pError) {
		this.Message = pMessage;
		this.Error = pError;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public String toString() {
		String Overall = "Message: " + this.Message + " Error: " + this.Error.toString();
		return Overall;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
