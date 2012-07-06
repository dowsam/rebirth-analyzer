/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-analyzer SumMallSimilarity.java 2012-2-17 13:45:44 l.xue.nong$$
 */
package cn.com.rebirth.analyzer;

/**
 * The Class SumMallSimilarity.
 *
 * @author l.xue.nong
 */
public class RestartSimilarity extends AbstractSimilarity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2398392974218448263L;

	/* (non-Javadoc)
	 * @see cn.com.summall.analyzer.AbstractSimilarity#similarity(int, int)
	 */
	@Override
	public float similarity(int overlap, int maxOverlap) {
		float overlap2 = (float) Math.pow(2, overlap);
		float maxOverlap2 = (float) Math.pow(2, maxOverlap);
		return (overlap2 / maxOverlap2);
	}

}
