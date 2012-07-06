/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer PaodingAnalyzer.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.analyzer;

import java.util.Properties;

import net.paoding.analysis.Constants;
import net.paoding.analysis.analyzer.estimate.TryPaodingAnalyzer;
import net.paoding.analysis.knife.Knife;
import net.paoding.analysis.knife.Paoding;
import net.paoding.analysis.knife.PaodingMaker;

/**
 * The Class PaodingAnalyzer.
 *
 * @author l.xue.nong
 */
public class PaodingAnalyzer extends PaodingAnalyzerBean {

	/**
	 * Instantiates a new paoding analyzer.
	 */
	public PaodingAnalyzer() {
		this(PaodingMaker.DEFAULT_PROPERTIES_PATH);
	}

	/**
	 * Instantiates a new paoding analyzer.
	 *
	 * @param propertiesPath the properties path
	 */
	public PaodingAnalyzer(String propertiesPath) {
		init(propertiesPath);
	}

	/**
	 * Inits the.
	 *
	 * @param propertiesPath the properties path
	 */
	protected void init(String propertiesPath) {
		// 根据PaodingMaker说明，
		// 1、多次调用getProperties()，返回的都是同一个properties实例(只要属性文件没发生过修改)
		// 2、相同的properties实例，PaodingMaker也将返回同一个Paoding实例
		// 根据以上1、2点说明，在此能够保证多次创建PaodingAnalyzer并不会多次装载属性文件和词典
		if (propertiesPath == null) {
			propertiesPath = PaodingMaker.DEFAULT_PROPERTIES_PATH;
		}
		Properties properties = PaodingMaker.getProperties(propertiesPath);
		String mode = Constants
				.getProperty(properties, Constants.ANALYZER_MODE);
		Paoding paoding = PaodingMaker.make(properties);
		setKnife(paoding);
		setMode(mode);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		if (System.getProperty("paoding.try.app") == null) {
			System.setProperty("paoding.try.app", "PaodingAnalyzer");
			System.setProperty("paoding.try.cmd", "java PaodingAnalyzer");
		}
		TryPaodingAnalyzer.main(args);
	}

	// --------------------------------------------------

	/**
	 * Instantiates a new paoding analyzer.
	 *
	 * @param knife the knife
	 * @param mode the mode
	 */
	public PaodingAnalyzer(Knife knife, int mode) {
		super(knife, mode);
	}

	/**
	 * Query mode.
	 *
	 * @param knife the knife
	 * @return the paoding analyzer
	 */
	public static PaodingAnalyzer queryMode(Knife knife) {
		return maxMode(knife);
	}

	/**
	 * Default mode.
	 *
	 * @param knife the knife
	 * @return the paoding analyzer
	 */
	public static PaodingAnalyzer defaultMode(Knife knife) {
		return new PaodingAnalyzer(knife, MOST_WORDS_MODE);
	}

	/**
	 * Max mode.
	 *
	 * @param knife the knife
	 * @return the paoding analyzer
	 */
	public static PaodingAnalyzer maxMode(Knife knife) {
		return new PaodingAnalyzer(knife, MAX_WORD_LENGTH_MODE);
	}

	/**
	 * Writer mode.
	 *
	 * @param knife the knife
	 * @return the paoding analyzer
	 */
	public static PaodingAnalyzer writerMode(Knife knife) {
		return defaultMode(knife);
	}
}
