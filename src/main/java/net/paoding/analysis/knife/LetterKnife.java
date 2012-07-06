/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer LetterKnife.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.knife;


/**
 * The Class LetterKnife.
 *
 * @author l.xue.nong
 */
public class LetterKnife extends CombinatoricsKnife {

	/** The Constant DEFAULT_NOISE. */
	public static final String[] DEFAULT_NOISE = { "a", "an", "and", "are", "as", "at",
			"be", "but", "by", "for", "if", "in", "into", "is", "it", "no",
			"not", "of", "on", "or", "such", "that", "the", "their", "then",
			"there", "these", "they", "this", "to", "was", "will", "with",
			"www" };

	
	/**
	 * Instantiates a new letter knife.
	 */
	public LetterKnife() {
		super(DEFAULT_NOISE);
	}

	/**
	 * Instantiates a new letter knife.
	 *
	 * @param noiseWords the noise words
	 */
	public LetterKnife(String[] noiseWords) {
		super(noiseWords);
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Knife#assignable(net.paoding.analysis.knife.Beef, int, int)
	 */
	public int assignable(Beef beef, int offset, int index) {
		char ch = beef.charAt(index);
		if (CharSet.isLantingLetter(ch)) {
			return ASSIGNED;
		}
		if (index > offset) {
			if ((ch >= '0' && ch <= '9') || ch == '-' || ch == '_') {
				return POINT;
			}
		}
		return LIMIT;
	}
	

}
