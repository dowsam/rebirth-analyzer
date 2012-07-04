/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-analyzer SumMallAnalyzersFactory.java 2012-2-13 15:48:00 l.xue.nong$$
 */
package cn.com.restart.analyzer.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.restart.analyzer.InitializationAnalyzer;
import cn.com.restart.analyzer.RestartAnalyzers;
import cn.com.restart.commons.utils.ClassResolverUtils;

/**
 * A factory for creating SumMallAnalyzers objects.
 */
public abstract class RestartAnalyzersFactory {

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(RestartAnalyzersFactory.class);

	/** The halod context. */
	private static Map<String, RestartAnalyzers> halodContext = new HashMap<String, RestartAnalyzers>();
	/** The sum mall analyzers. */
	static {
		List<RestartAnalyzers> analyzers = ClassResolverUtils.findImpl(RestartAnalyzers.class);
		if (analyzers != null) {
			for (RestartAnalyzers sumMallAnalyzers : analyzers) {
				if (sumMallAnalyzers instanceof InitializationAnalyzer) {
					((InitializationAnalyzer) sumMallAnalyzers).beforeInit();
				}
				LOGGER.info("Find Impl :" + sumMallAnalyzers.getClass().getName());
				printlen(sumMallAnalyzers);
				halodContext.put(
						sumMallAnalyzers.getAnalyzerImplName() + "(" + sumMallAnalyzers.getAnalyzerImplVersion() + ")",
						sumMallAnalyzers);
				if (sumMallAnalyzers instanceof InitializationAnalyzer) {
					((InitializationAnalyzer) sumMallAnalyzers).afterInit();
				}
			}
		}
	}

	/**
	 * Gets the analyzers.
	 *
	 * @param key the key
	 * @return the analyzers
	 */
	public static RestartAnalyzers getAnalyzers(String key) {
		return halodContext.get(key);
	}

	/**
	 * Printlen.
	 *
	 * @param sumMallAnalyzers the sum mall analyzers
	 */
	private static void printlen(RestartAnalyzers sumMallAnalyzers) {
		LOGGER.info("Interface Name:" + RestartAnalyzers.class.getName() + ",find Achieve Class Name:"
				+ sumMallAnalyzers.getClass().getName() + ",Name:" + sumMallAnalyzers.getAnalyzerImplName()
				+ ",Version:" + sumMallAnalyzers.getAnalyzerImplVersion() + ",Lucene Version:"
				+ sumMallAnalyzers.getLuceneVersion());
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		RestartAnalyzersFactory.getAnalyzers("RestartAnalyzer-Impl-Ik(3.2.3)");
		System.out.println("aaaa");
	}

}
