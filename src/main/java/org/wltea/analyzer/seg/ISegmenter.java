/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer ISegmenter.java 2012-7-6 10:23:23 l.xue.nong$$
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Context;

/**
 * The Interface ISegmenter.
 *
 * @author l.xue.nong
 */
public interface ISegmenter {
	
	/**
	 * Next lexeme.
	 *
	 * @param segmentBuff the segment buff
	 * @param context the context
	 */
	void nextLexeme(char[] segmentBuff , Context context);
	
	/**
	 * Reset.
	 */
	void reset();
}
