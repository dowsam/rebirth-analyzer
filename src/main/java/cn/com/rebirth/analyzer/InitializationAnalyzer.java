/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer InitializationAnalyzer.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package cn.com.rebirth.analyzer;

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
