/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Constants.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The Class Constants.
 *
 * @author l.xue.nong
 */
@SuppressWarnings("unchecked")
public class Constants {

	/** The Constant DIC_HOME_CONFIG_FIRST. */
	public static final String DIC_HOME_CONFIG_FIRST = "paoding.dic.home.config-first";
	
	/** The Constant DIC_HOME_CONFIG_FIRST_DEFAULT. */
	public static final String DIC_HOME_CONFIG_FIRST_DEFAULT = "system-env";

	/** The Constant ENV_PAODING_DIC_HOME. */
	public static final String ENV_PAODING_DIC_HOME = "PAODING_DIC_HOME";

	// -------------------------------------------------------------
	/** The Constant DIC_HOME. */
	public static final String DIC_HOME = "paoding.dic.home";
	
	/** The Constant DIC_HOME_DEFAULT. */
	public static final String DIC_HOME_DEFAULT = null;

	// -------------------------------------------------------------
	//
	/** The Constant DIC_CHARSET. */
	public static final String DIC_CHARSET = "paoding.dic.charset";
	
	/** The Constant DIC_CHARSET_DEFAULT. */
	public static final String DIC_CHARSET_DEFAULT = "UTF-8";

	// dictionary word length limit
	/** The Constant DIC_MAXWORDLEN. */
	public static final String DIC_MAXWORDLEN = "paoding.dic.maxWordLen";
	
	/** The Constant DIC_MAXWORDLEN_DEFAULT. */
	public static final String DIC_MAXWORDLEN_DEFAULT = "0";
	
	// -------------------------------------------------------------
	// dictionaries which are skip
	/** The Constant DIC_SKIP_PREFIX. */
	public static final String DIC_SKIP_PREFIX = "paoding.dic.skip.prefix";
	
	/** The Constant DIC_SKIP_PREFIX_DEFAULT. */
	public static final String DIC_SKIP_PREFIX_DEFAULT = "x-";

	// -------------------------------------------------------------
	// chinese/cjk charactors that will not token
	/** The Constant DIC_NOISE_CHARACTOR. */
	public static final String DIC_NOISE_CHARACTOR = "paoding.dic.noise-charactor";
	
	/** The Constant DIC_NOISE_CHARACTOR_DEFAULT. */
	public static final String DIC_NOISE_CHARACTOR_DEFAULT = "x-noise-charactor";

	// -------------------------------------------------------------
	// chinese/cjk words that will not token
	/** The Constant DIC_NOISE_WORD. */
	public static final String DIC_NOISE_WORD = "paoding.dic.noise-word";
	
	/** The Constant DIC_NOISE_WORD_DEFAULT. */
	public static final String DIC_NOISE_WORD_DEFAULT = "x-noise-word";

	// -------------------------------------------------------------
	// unit words, like "ge", "zhi", ...
	/** The Constant DIC_UNIT. */
	public static final String DIC_UNIT = "paoding.dic.unit";
	
	/** The Constant DIC_UNIT_DEFAULT. */
	public static final String DIC_UNIT_DEFAULT = "x-unit";

	// -------------------------------------------------------------
	// like "Wang", "Zhang", ...
	/** The Constant DIC_CONFUCIAN_FAMILY_NAME. */
	public static final String DIC_CONFUCIAN_FAMILY_NAME = "paoding.dic.confucian-family-name";
	
	/** The Constant DIC_CONFUCIAN_FAMILY_NAME_DEFAULT. */
	public static final String DIC_CONFUCIAN_FAMILY_NAME_DEFAULT = "x-confucian-family-name";
	
	// -------------------------------------------------------------
	// like 
	/** The Constant DIC_FOR_COMBINATORICS. */
	public static final String DIC_FOR_COMBINATORICS = "paoding.dic.for-combinatorics";
	
	/** The Constant DIC_FOR_COMBINATORICS_DEFAULT. */
	public static final String DIC_FOR_COMBINATORICS_DEFAULT = "x-for-combinatorics";

	// -------------------------------------------------------------
	// like 
	/** The Constant DIC_DETECTOR_INTERVAL. */
	public static final String DIC_DETECTOR_INTERVAL = "paoding.dic.detector.interval";
	
	/** The Constant DIC_DETECTOR_INTERVAL_DEFAULT. */
	public static final String DIC_DETECTOR_INTERVAL_DEFAULT = "60";

	// -------------------------------------------------------------
	// like "default", "max", ...
	/** The Constant ANALYZER_MODE. */
	public static final String ANALYZER_MODE = "paoding.analyzer.mode";
	
	/** The Constant ANALYZER_MOE_DEFAULT. */
	public static final String ANALYZER_MOE_DEFAULT = "most-words";

	// -------------------------------------------------------------
	// 
	/** The Constant ANALYZER_DICTIONARIES_COMPILER. */
	public static final String ANALYZER_DICTIONARIES_COMPILER = "paoding.analyzer.dictionaries.compiler";
	
	/** The Constant ANALYZER_DICTIONARIES_COMPILER_DEFAULT. */
	public static final String ANALYZER_DICTIONARIES_COMPILER_DEFAULT = null;

	// -------------------------------------------------------------
	/** The Constant map. */
	@SuppressWarnings("rawtypes")
	private static final Map/* <String, String> */map = new HashMap();

	static {
		map.put(DIC_HOME_CONFIG_FIRST, DIC_HOME_CONFIG_FIRST_DEFAULT);
		map.put(DIC_HOME, DIC_HOME_DEFAULT);
		map.put(DIC_CHARSET, DIC_CHARSET_DEFAULT);
		map.put(DIC_MAXWORDLEN, DIC_MAXWORDLEN_DEFAULT);
		map.put(DIC_SKIP_PREFIX, DIC_SKIP_PREFIX_DEFAULT);
		map.put(DIC_NOISE_CHARACTOR, DIC_NOISE_CHARACTOR_DEFAULT);
		map.put(DIC_NOISE_WORD, DIC_NOISE_WORD_DEFAULT);
		map.put(DIC_UNIT, DIC_UNIT_DEFAULT);
		map.put(DIC_CONFUCIAN_FAMILY_NAME, DIC_CONFUCIAN_FAMILY_NAME_DEFAULT);
		map.put(DIC_FOR_COMBINATORICS, DIC_FOR_COMBINATORICS_DEFAULT);
		map.put(DIC_DETECTOR_INTERVAL, DIC_DETECTOR_INTERVAL_DEFAULT);
		map.put(ANALYZER_MODE, ANALYZER_MOE_DEFAULT);
		map.put(ANALYZER_DICTIONARIES_COMPILER, ANALYZER_DICTIONARIES_COMPILER_DEFAULT);
	}

	//
	/** The Constant KNIFE_CLASS. */
	public static final String KNIFE_CLASS = "paoding.knife.class.";

	/**
	 * Gets the property.
	 *
	 * @param p the p
	 * @param name the name
	 * @return the property
	 */
	public static String getProperty(Properties p, String name) {
		return p.getProperty(name, (String) map.get(name));
	}
}
