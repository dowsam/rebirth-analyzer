/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-analyzer SumMallAnalyzer.java 2012-2-13 14:32:47 l.xue.nong$$
 */
package cn.com.rebirth.analyzer.support;

import java.io.Reader;
import java.io.Serializable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import cn.com.rebirth.analyzer.RestartAnalyzers;

/**
 * The Class SumMallAnalyzer.
 *
 * @author l.xue.nong
 */
public class RestartAnalyzer extends Analyzer implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2483626063644302654L;
	/** The mall analyzers. */
	private final RestartAnalyzers mallAnalyzers;

	/**
	 * Instantiates a new sum mall analyzer.
	 *
	 * @param mallAnalyzers the mall analyzers
	 */
	public RestartAnalyzer(RestartAnalyzers mallAnalyzers) {
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
	public RestartAnalyzers getMallAnalyzers() {
		return mallAnalyzers;
	}

}
