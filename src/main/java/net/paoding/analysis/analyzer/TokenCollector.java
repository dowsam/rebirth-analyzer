/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer TokenCollector.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.analyzer;

import java.util.Iterator;

import org.apache.lucene.analysis.Token;

import net.paoding.analysis.knife.Collector;

/**
 * The Interface TokenCollector.
 *
 * @author l.xue.nong
 */
public interface TokenCollector extends Collector {

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 */
	public Iterator<Token> iterator();
}
