/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer rebirthAnalyzers.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package cn.com.rebirth.analyzer;

import java.io.Reader;
import java.io.Serializable;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

/**
 * The Interface rebirthAnalyzers.
 *
 * @author l.xue.nong
 */
public interface RebirthAnalyzers extends Serializable {

	/**
	 * Gets the analyzer impl name.
	 *
	 * @return the analyzer impl name
	 */
	public String getAnalyzerImplName();

	/**
	 * Gets the analyzer impl version.
	 *
	 * @return the analyzer impl version
	 */
	public String getAnalyzerImplVersion();

	/**
	 * Token stream.
	 *
	 * @param fieldName the field name
	 * @param reader the reader
	 * @return the token stream
	 */
	public TokenStream tokenStream(String fieldName, Reader reader);

	/**
	 * Gets the lucene version.
	 *
	 * @return the lucene version
	 */
	public Version getLuceneVersion();
}
