/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer PaodingAnalysisException.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.exception;

/**
 * The Class PaodingAnalysisException.
 *
 * @author l.xue.nong
 */
public class PaodingAnalysisException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5319477662251490296L;

	/**
	 * Instantiates a new paoding analysis exception.
	 */
	public PaodingAnalysisException() {
		super();
	}

	/**
	 * Instantiates a new paoding analysis exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public PaodingAnalysisException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new paoding analysis exception.
	 *
	 * @param message the message
	 */
	public PaodingAnalysisException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new paoding analysis exception.
	 *
	 * @param cause the cause
	 */
	public PaodingAnalysisException(Throwable cause) {
		super(cause);
	}

}
