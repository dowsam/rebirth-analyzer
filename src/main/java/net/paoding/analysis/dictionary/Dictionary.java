/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Dictionary.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.dictionary;

/**
 * The Interface Dictionary.
 *
 * @author l.xue.nong
 */
public interface Dictionary {

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size();

	/**
	 * Gets the.
	 *
	 * @param index the index
	 * @return the word
	 */
	public Word get(int index);

	/**
	 * Search.
	 *
	 * @param input the input
	 * @param offset the offset
	 * @param count the count
	 * @return the hit
	 */
	public Hit search(CharSequence input, int offset, int count);
}
