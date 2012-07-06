/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer rebirthSimilarity.java 2012-7-6 10:33:32 l.xue.nong$$
 */
package cn.com.rebirth.analyzer;

/**
 * The Class rebirthSimilarity.
 *
 * @author l.xue.nong
 */
public class RebirthSimilarity extends AbstractSimilarity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2398392974218448263L;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.analyzer.AbstractSimilarity#similarity(int, int)
	 */
	@Override
	public float similarity(int overlap, int maxOverlap) {
		float overlap2 = (float) Math.pow(2, overlap);
		float maxOverlap2 = (float) Math.pow(2, maxOverlap);
		return (overlap2 / maxOverlap2);
	}

}
