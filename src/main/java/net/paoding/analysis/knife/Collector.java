/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Collector.java 2012-7-6 10:23:23 l.xue.nong$$
 */
package net.paoding.analysis.knife;

/**
 * The Interface Collector.
 *
 * @author l.xue.nong
 */
public interface Collector {

	/**
	 * Collect.
	 *
	 * @param word the word
	 * @param offset the offset
	 * @param end the end
	 */
	public void collect(String word, int offset, int end);
}
