/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-analyzer IkRestartAnalyzer.java 2012-7-4 13:56:03 l.xue.nong$$
 */
package cn.com.rebirth.analyzer.support;

import org.apache.lucene.analysis.Analyzer;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.com.rebirth.analyzer.AbstractRestartAnalyzer;
import cn.com.rebirth.analyzer.InitializationAnalyzer;
import cn.com.rebirth.analyzer.RestartAnalyzers;
import cn.com.rebirth.commons.StopWatch;

/**
 * The Class IkRestartAnalyzer.
 *
 * @author l.xue.nong
 */
public final class IkRestartAnalyzer extends AbstractRestartAnalyzer implements RestartAnalyzers,
		InitializationAnalyzer {
	/** The Constant PAODING_NAME. */
	public static final String IMPL_ANALYZER_NAME = "RestartAnalyzer-Impl-Ik";

	/** The Constant PAODING_VERSION. */
	public static final String IMPL_ANALYZER_VERSION = "3.2.3";

	/**
	 * Instantiates a new ik restart analyzer.
	 *
	 * @param analyzer the analyzer
	 */
	public IkRestartAnalyzer(Analyzer analyzer) {
		super(analyzer);
	}

	/**
	 * Instantiates a new ik restart analyzer.
	 */
	public IkRestartAnalyzer() {
		this(new IKAnalyzer());
	}

	/**
	 * Instantiates a new ik restart analyzer.
	 *
	 * @param isMaxWordLength the is max word length
	 */
	public IkRestartAnalyzer(boolean isMaxWordLength) {
		this(new IKAnalyzer(isMaxWordLength));
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3574504142224792506L;

	/* (non-Javadoc)
	 * @see cn.com.restart.analyzer.RestartAnalyzers#getAnalyzerImplName()
	 */
	@Override
	public String getAnalyzerImplName() {
		return IMPL_ANALYZER_NAME;
	}

	/* (non-Javadoc)
	 * @see cn.com.restart.analyzer.RestartAnalyzers#getAnalyzerImplVersion()
	 */
	@Override
	public String getAnalyzerImplVersion() {
		return IMPL_ANALYZER_VERSION;
	}

	@Override
	public void beforeInit() {

	}

	@Override
	public void afterInit() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		//初始化词库
		Dictionary.getInstance();
		stopWatch.stop();
		logger.info("Init IK Dic Time-consuming[{}]", stopWatch.totalTime());
	}

}
