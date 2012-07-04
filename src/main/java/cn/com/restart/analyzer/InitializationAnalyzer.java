/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-analyzer InitializationAnalyzer.java 2012-7-4 14:12:54 l.xue.nong$$
 */
package cn.com.restart.analyzer;

/**
 * The Interface InitializationAnalyzer.
 *
 * @author l.xue.nong
 */
public interface InitializationAnalyzer {

	/**
	 * Before init.
	 */
	public void beforeInit();

	/**
	 * After init.
	 */
	public void afterInit();
}
