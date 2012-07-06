/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer rebirthAnalyzer.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package cn.com.rebirth.analyzer.support;

import java.io.Reader;
import java.io.Serializable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import cn.com.rebirth.analyzer.RebirthAnalyzers;

/**
 * The Class rebirthAnalyzer.
 *
 * @author l.xue.nong
 */
public class RebirthAnalyzer extends Analyzer implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2483626063644302654L;
	
	/** The mall analyzers. */
	private final RebirthAnalyzers mallAnalyzers;

	/**
	 * Instantiates a new rebirth analyzer.
	 *
	 * @param mallAnalyzers the mall analyzers
	 */
	public RebirthAnalyzer(RebirthAnalyzers mallAnalyzers) {
		super();
		this.mallAnalyzers = mallAnalyzers;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return mallAnalyzers.tokenStream(fieldName, reader);
	}

	/**
	 * Gets the mall analyzers.
	 *
	 * @return the mall analyzers
	 */
	public RebirthAnalyzers getMallAnalyzers() {
		return mallAnalyzers;
	}

}
