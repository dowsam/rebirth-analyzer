/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer IKAnalyzer.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package org.wltea.analyzer.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

/**
 * The Class IKAnalyzer.
 *
 * @author l.xue.nong
 */
public final class IKAnalyzer extends Analyzer {
	
	/** The is max word length. */
	private boolean isMaxWordLength = false;
	
	/**
	 * Instantiates a new iK analyzer.
	 */
	public IKAnalyzer(){
		this(false);
	}
	
	/**
	 * Instantiates a new iK analyzer.
	 *
	 * @param isMaxWordLength the is max word length
	 */
	public IKAnalyzer(boolean isMaxWordLength){
		super();
		this.setMaxWordLength(isMaxWordLength);
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new IKTokenizer(reader , isMaxWordLength());
	}

	/**
	 * Sets the max word length.
	 *
	 * @param isMaxWordLength the new max word length
	 */
	public void setMaxWordLength(boolean isMaxWordLength) {
		this.isMaxWordLength = isMaxWordLength;
	}

	/**
	 * Checks if is max word length.
	 *
	 * @return true, if is max word length
	 */
	public boolean isMaxWordLength() {
		return isMaxWordLength;
	}

}
