/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Dictionaries.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.knife;

import net.paoding.analysis.dictionary.Dictionary;
import net.paoding.analysis.dictionary.support.detection.DifferenceListener;

/**
 * The Interface Dictionaries.
 *
 * @author l.xue.nong
 */
public interface Dictionaries {
	
	/**
	 * Gets the vocabulary dictionary.
	 *
	 * @return the vocabulary dictionary
	 */
	public Dictionary getVocabularyDictionary();

	/**
	 * Gets the confucian family names dictionary.
	 *
	 * @return the confucian family names dictionary
	 */
	public Dictionary getConfucianFamilyNamesDictionary();

	/**
	 * Gets the noise charactors dictionary.
	 *
	 * @return the noise charactors dictionary
	 */
	public Dictionary getNoiseCharactorsDictionary();

	/**
	 * Gets the noise words dictionary.
	 *
	 * @return the noise words dictionary
	 */
	public Dictionary getNoiseWordsDictionary();

	/**
	 * Gets the units dictionary.
	 *
	 * @return the units dictionary
	 */
	public Dictionary getUnitsDictionary();
	
	/**
	 * Gets the combinatorics dictionary.
	 *
	 * @return the combinatorics dictionary
	 */
	public Dictionary getCombinatoricsDictionary();
	
	/**
	 * Start detecting.
	 *
	 * @param interval the interval
	 * @param l the l
	 */
	public void startDetecting(int interval, DifferenceListener l);
	
	/**
	 * Stop detecting.
	 */
	public void stopDetecting();
}
