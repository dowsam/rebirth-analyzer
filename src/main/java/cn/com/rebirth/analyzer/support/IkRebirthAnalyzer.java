/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer IkrebirthAnalyzer.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package cn.com.rebirth.analyzer.support;

import org.apache.lucene.analysis.Analyzer;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.com.rebirth.analyzer.AbstractRebirthAnalyzer;
import cn.com.rebirth.analyzer.InitializationAnalyzer;
import cn.com.rebirth.analyzer.RebirthAnalyzers;
import cn.com.rebirth.commons.StopWatch;

/**
 * The Class IkrebirthAnalyzer.
 *
 * @author l.xue.nong
 */
public final class IkRebirthAnalyzer extends AbstractRebirthAnalyzer implements RebirthAnalyzers,
		InitializationAnalyzer {
	
	/** The Constant IMPL_ANALYZER_NAME. */
	public static final String IMPL_ANALYZER_NAME = "rebirthAnalyzer-Impl-Ik";

	/** The Constant IMPL_ANALYZER_VERSION. */
	public static final String IMPL_ANALYZER_VERSION = "3.2.3";

	/**
	 * Instantiates a new ik rebirth analyzer.
	 *
	 * @param analyzer the analyzer
	 */
	public IkRebirthAnalyzer(Analyzer analyzer) {
		super(analyzer);
	}

	/**
	 * Instantiates a new ik rebirth analyzer.
	 */
	public IkRebirthAnalyzer() {
		this(new IKAnalyzer());
	}

	/**
	 * Instantiates a new ik rebirth analyzer.
	 *
	 * @param isMaxWordLength the is max word length
	 */
	public IkRebirthAnalyzer(boolean isMaxWordLength) {
		this(new IKAnalyzer(isMaxWordLength));
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3574504142224792506L;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.analyzer.rebirthAnalyzers#getAnalyzerImplName()
	 */
	@Override
	public String getAnalyzerImplName() {
		return IMPL_ANALYZER_NAME;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.analyzer.rebirthAnalyzers#getAnalyzerImplVersion()
	 */
	@Override
	public String getAnalyzerImplVersion() {
		return IMPL_ANALYZER_VERSION;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.analyzer.InitializationAnalyzer#beforeInit()
	 */
	@Override
	public void beforeInit() {

	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.analyzer.InitializationAnalyzer#afterInit()
	 */
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
