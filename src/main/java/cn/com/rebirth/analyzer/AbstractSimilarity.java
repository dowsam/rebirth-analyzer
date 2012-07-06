/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer AbstractSimilarity.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package cn.com.rebirth.analyzer;

import org.apache.lucene.search.DefaultSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractSimilarity.
 *
 * @author l.xue.nong
 */
public abstract class AbstractSimilarity extends DefaultSimilarity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8132114638271078407L;
	
	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/* (non-Javadoc)
	 * @see org.apache.lucene.search.DefaultSimilarity#coord(int, int)
	 */
	@Override
	public float coord(int overlap, int maxOverlap) {
		float coord = similarity(overlap, maxOverlap);
		logger.info("Lucene Similarity Value:" + coord);
		return coord;
	}

	/**
	 * Similarity.
	 *
	 * @param overlap the overlap
	 * @param maxOverlap the max overlap
	 * @return the float
	 */
	public abstract float similarity(int overlap, int maxOverlap);
}
