/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-analyzer PaodingSumMallAnalyzer.java 2012-2-13 14:26:54 l.xue.nong$$
 */
package cn.com.restart.analyzer.support;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import cn.com.restart.analyzer.AbstractRestartAnalyzer;
import cn.com.restart.analyzer.RestartAnalyzers;

/**
 * The Class PaodingSumMallAnalyzer.
 *
 * @author l.xue.nong
 */
public final class PaodingRestartAnalyzer extends AbstractRestartAnalyzer implements RestartAnalyzers {

	/** The Constant PAODING_NAME. */
	public static final String PAODING_NAME = "RestartAnalyzer-Impl-Paoding";

	/** The Constant PAODING_VERSION. */
	public static final String PAODING_VERSION = "2.0.4.beta";
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5520201032862588002L;

	/**
	 * Instantiates a new paoding sum mall analyzer.
	 *
	 */
	public PaodingRestartAnalyzer() {
		super(new PaodingAnalyzer());
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.analyzer.SumMallAnalyzer#getAnalyzerImplName()
	 */
	@Override
	public String getAnalyzerImplName() {
		return PaodingRestartAnalyzer.PAODING_NAME;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.analyzer.SumMallAnalyzer#getAnalyzerImplVersion()
	 */
	@Override
	public String getAnalyzerImplVersion() {
		return PaodingRestartAnalyzer.PAODING_VERSION;
	}

}
