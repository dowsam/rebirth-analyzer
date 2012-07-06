/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer DictionaryDelegate.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.dictionary;

/**
 * The Class DictionaryDelegate.
 *
 * @author l.xue.nong
 */
public class DictionaryDelegate implements Dictionary {
	
	/** The target. */
	private Dictionary target;

	/**
	 * Instantiates a new dictionary delegate.
	 */
	public DictionaryDelegate() {
	}

	/**
	 * Instantiates a new dictionary delegate.
	 *
	 * @param target the target
	 */
	public DictionaryDelegate(Dictionary target) {
		this.target = target;
	}

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public Dictionary getTarget() {
		return target;
	}

	/**
	 * Sets the target.
	 *
	 * @param target the new target
	 */
	public void setTarget(Dictionary target) {
		this.target = target;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.Dictionary#get(int)
	 */
	public Word get(int index) {
		return target.get(index);
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.Dictionary#search(java.lang.CharSequence, int, int)
	 */
	public Hit search(CharSequence input, int offset, int count) {
		return target.search(input, offset, count);
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.Dictionary#size()
	 */
	public int size() {
		return target.size();
	}

}
