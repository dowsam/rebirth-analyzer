/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer BinaryDictionary.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.dictionary;

/**
 * The Class BinaryDictionary.
 *
 * @author l.xue.nong
 */
public class BinaryDictionary implements Dictionary {

	// -------------------------------------------------

	/** The asc words. */
	private Word[] ascWords;

	/** The start. */
	private final int start;
	
	/** The end. */
	private final int end;
	
	/** The count. */
	private final int count;

	// -------------------------------------------------

	/**
	 * Instantiates a new binary dictionary.
	 *
	 * @param ascWords the asc words
	 */
	public BinaryDictionary(Word[] ascWords) {
		this(ascWords, 0, ascWords.length);
	}

	/**
	 * Instantiates a new binary dictionary.
	 *
	 * @param ascWords the asc words
	 * @param start the start
	 * @param end the end
	 */
	public BinaryDictionary(Word[] ascWords, int start, int end) {
		this.ascWords = ascWords;
		this.start = start;
		this.end = end;
		this.count = end - start;
	}

	// -------------------------------------------------

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.Dictionary#get(int)
	 */
	public Word get(int index) {
		return ascWords[start + index];
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.Dictionary#size()
	 */
	public int size() {
		return count;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.Dictionary#search(java.lang.CharSequence, int, int)
	 */
	public Hit search(CharSequence input, int begin, int count) {
		int left = this.start;
		int right = this.end - 1;
		int pointer = 0;
		Word word = null;
		int relation;
		//
		while (left <= right) {
			pointer = (left + right) >> 1;
			word = ascWords[pointer];
			relation = compare(input, begin, count, word);
			if (relation == 0) {
				// System.out.println(new String(input,begin, count)+"***" +
				// word);
				int nextWordIndex = pointer + 1;
				if (nextWordIndex >= ascWords.length) {
					return new Hit(pointer, word, null);
				} else {
					return new Hit(pointer, word, ascWords[nextWordIndex]);
				}
			}
			if (relation < 0)
				right = pointer - 1;
			else
				left = pointer + 1;
		}
		//
		if (left >= ascWords.length) {
			return Hit.UNDEFINED;
		}
		//
		boolean asPrex = true;
		Word nextWord = ascWords[left];
		if (nextWord.length() < count) {
			asPrex = false;
		}
		for (int i = begin, j = 0; asPrex && j < count; i++, j++) {
			if (input.charAt(i) != nextWord.charAt(j)) {
				asPrex = false;
			}
		}
		return asPrex ? new Hit(Hit.UNCLOSED_INDEX, null, nextWord)
				: Hit.UNDEFINED;
	}

	/**
	 * Compare.
	 *
	 * @param one the one
	 * @param begin the begin
	 * @param count the count
	 * @param theOther the the other
	 * @return the int
	 */
	public static int compare(CharSequence one, int begin, int count,
			CharSequence theOther) {
		for (int i = begin, j = 0; i < one.length()
				&& j < Math.min(theOther.length(), count); i++, j++) {
			if (one.charAt(i) > theOther.charAt(j)) {
				return 1;
			} else if (one.charAt(i) < theOther.charAt(j)) {
				return -1;
			}
		}
		return count - theOther.length();
	}
}
