/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer PaodingrebirthAnalyzer.java 2012-7-6 10:32:00 l.xue.nong$$
 */
package cn.com.rebirth.analyzer.support;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import cn.com.rebirth.analyzer.AbstractRebirthAnalyzer;
import cn.com.rebirth.analyzer.RebirthAnalyzers;

/**
 * The Class PaodingrebirthAnalyzer.
 *
 * @author l.xue.nong
 */
public final class PaodingRebirthAnalyzer extends AbstractRebirthAnalyzer implements RebirthAnalyzers {

	/** The Constant PAODING_NAME. */
	public static final String PAODING_NAME = "rebirthAnalyzer-Impl-Paoding";

	/** The Constant PAODING_VERSION. */
	public static final String PAODING_VERSION = "2.0.4.beta";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5520201032862588002L;

	/**
	 * Instantiates a new paoding rebirth analyzer.
	 */
	public PaodingRebirthAnalyzer() {
		super(new PaodingAnalyzer());
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.analyzer.rebirthAnalyzers#getAnalyzerImplName()
	 */
	@Override
	public String getAnalyzerImplName() {
		return PaodingRebirthAnalyzer.PAODING_NAME;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.analyzer.rebirthAnalyzers#getAnalyzerImplVersion()
	 */
	@Override
	public String getAnalyzerImplVersion() {
		return PaodingRebirthAnalyzer.PAODING_VERSION;
	}

}
