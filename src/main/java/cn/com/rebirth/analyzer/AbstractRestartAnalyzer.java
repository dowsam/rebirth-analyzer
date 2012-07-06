/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-analyzer AbstractSumMallAnalyzer.java 2012-2-13 14:22:53 l.xue.nong$$
 */
package cn.com.rebirth.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractSumMallAnalyzer.
 *
 * @author l.xue.nong
 */
public abstract class AbstractRestartAnalyzer implements RestartAnalyzers {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2125950219476546621L;

	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** The analyzer. */
	private final Analyzer analyzer;

	/**
	 * Instantiates a new abstract sum mall analyzer.
	 *
	 * @param analyzer the analyzer
	 */
	public AbstractRestartAnalyzer(Analyzer analyzer) {
		super();
		this.analyzer = analyzer;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.analyzer.SumMallAnalyzer#getLuceneVersion()
	 */
	@Override
	public Version getLuceneVersion() {
		return Version.LUCENE_35;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.analyzer.SumMallAnalyzers#tokenStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return analyzer.tokenStream(fieldName, reader);
	}

}
