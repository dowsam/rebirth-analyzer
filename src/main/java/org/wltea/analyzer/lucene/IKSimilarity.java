/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer IKSimilarity.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package org.wltea.analyzer.lucene;

import org.apache.lucene.search.DefaultSimilarity;

/**
 * The Class IKSimilarity.
 *
 * @author l.xue.nong
 */
public class IKSimilarity extends DefaultSimilarity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7558565500061194774L;

	/* (non-Javadoc)
	 * @see org.apache.lucene.search.Similarity#coord(int, int)
	 */
	public float coord(int overlap, int maxOverlap) {
		float overlap2 = (float)Math.pow(2, overlap);
		float maxOverlap2 = (float)Math.pow(2, maxOverlap);
		return (overlap2 / maxOverlap2);
	}	
}
