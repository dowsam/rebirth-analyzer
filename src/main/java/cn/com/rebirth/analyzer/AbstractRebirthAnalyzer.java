/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer AbstractrebirthAnalyzer.java 2012-7-6 10:31:17 l.xue.nong$$
 */
package cn.com.rebirth.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractrebirthAnalyzer.
 *
 * @author l.xue.nong
 */
public abstract class AbstractRebirthAnalyzer implements RebirthAnalyzers {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2125950219476546621L;

	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/** The analyzer. */
	private final Analyzer analyzer;

	/**
	 * Instantiates a new abstract rebirth analyzer.
	 *
	 * @param analyzer the analyzer
	 */
	public AbstractRebirthAnalyzer(Analyzer analyzer) {
		super();
		this.analyzer = analyzer;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.analyzer.rebirthAnalyzers#getLuceneVersion()
	 */
	@Override
	public Version getLuceneVersion() {
		return Version.LUCENE_35;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.analyzer.rebirthAnalyzers#tokenStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return analyzer.tokenStream(fieldName, reader);
	}

}
