/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer rebirthAnalyzersFactory.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package cn.com.rebirth.analyzer.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.analyzer.InitializationAnalyzer;
import cn.com.rebirth.analyzer.RebirthAnalyzers;
import cn.com.rebirth.commons.utils.ClassResolverUtils;

/**
 * A factory for creating rebirthAnalyzers objects.
 */
public abstract class RebirthAnalyzersFactory {

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(RebirthAnalyzersFactory.class);

	/** The halod context. */
	private static Map<String, RebirthAnalyzers> halodContext = new HashMap<String, RebirthAnalyzers>();
	/** The sum mall analyzers. */
	static {
		List<RebirthAnalyzers> analyzers = ClassResolverUtils.findImpl(RebirthAnalyzers.class);
		if (analyzers != null) {
			for (RebirthAnalyzers rebirthAnalyzers : analyzers) {
				if (rebirthAnalyzers instanceof InitializationAnalyzer) {
					((InitializationAnalyzer) rebirthAnalyzers).beforeInit();
				}
				LOGGER.info("Find Impl :" + rebirthAnalyzers.getClass().getName());
				printlen(rebirthAnalyzers);
				halodContext.put(
						rebirthAnalyzers.getAnalyzerImplName() + "(" + rebirthAnalyzers.getAnalyzerImplVersion() + ")",
						rebirthAnalyzers);
				if (rebirthAnalyzers instanceof InitializationAnalyzer) {
					((InitializationAnalyzer) rebirthAnalyzers).afterInit();
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
	public static RebirthAnalyzers getAnalyzers(String key) {
		return halodContext.get(key);
	}

	/**
	 * Printlen.
	 *
	 * @param rebirthAnalyzers the sum mall analyzers
	 */
	private static void printlen(RebirthAnalyzers rebirthAnalyzers) {
		LOGGER.info("Interface Name:" + RebirthAnalyzers.class.getName() + ",find Achieve Class Name:"
				+ rebirthAnalyzers.getClass().getName() + ",Name:" + rebirthAnalyzers.getAnalyzerImplName()
				+ ",Version:" + rebirthAnalyzers.getAnalyzerImplVersion() + ",Lucene Version:"
				+ rebirthAnalyzers.getLuceneVersion());
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		RebirthAnalyzersFactory.getAnalyzers("rebirthAnalyzer-Impl-Ik(3.2.3)");
		System.out.println("aaaa");
	}

}
