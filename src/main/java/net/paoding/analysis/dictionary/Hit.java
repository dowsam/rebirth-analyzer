/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Hit.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.dictionary;

/**
 * The Class Hit.
 *
 * @author l.xue.nong
 */
public class Hit {

	// -------------------------------------------------

	/** The Constant UNCLOSED_INDEX. */
	public final static int UNCLOSED_INDEX = -1;

	/** The Constant UNDEFINED_INDEX. */
	public final static int UNDEFINED_INDEX = -2;

	/** The Constant UNDEFINED. */
	public final static Hit UNDEFINED = new Hit(UNDEFINED_INDEX, null, null);

	// -------------------------------------------------

	/** The index. */
	private int index;

	/** The word. */
	private Word word;

	/** The next. */
	private Word next;

	// -------------------------------------------------

	/**
	 * Instantiates a new hit.
	 *
	 * @param index the index
	 * @param word the word
	 * @param next the next
	 */
	public Hit(int index, Word word, Word next) {
		this.index = index;
		this.word = word;
		this.next = next;
	}

	// -------------------------------------------------

	/**
	 * Gets the word.
	 *
	 * @return the word
	 */
	public Word getWord() {
		return word;
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	public Word getNext() {
		return next;
	}
	
	/**
	 * Checks if is hit.
	 *
	 * @return true, if is hit
	 */
	public boolean isHit() {
		return this.index >= 0;
	}

	/**
	 * Checks if is unclosed.
	 *
	 * @return true, if is unclosed
	 */
	public boolean isUnclosed() {
		return UNCLOSED_INDEX == this.index
				|| (this.next != null
						&& this.next.length() >= this.word.length() && this.next
						.startsWith(word));
	}

	/**
	 * Checks if is undefined.
	 *
	 * @return true, if is undefined
	 */
	public boolean isUndefined() {
		return UNDEFINED.index == this.index;
	}

	// -------------------------------------------------

	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Sets the word.
	 *
	 * @param key the new word
	 */
	void setWord(Word key) {
		this.word = key;
	}

	/**
	 * Sets the next.
	 *
	 * @param next the new next
	 */
	void setNext(Word next) {
		this.next = next;
	}

	// -------------------------------------------------

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((word == null) ? 0 : word.hashCode());
		result = PRIME * result + index;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Hit other = (Hit) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		if (index != other.index)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (isUnclosed()) {
			return "[UNCLOSED]";
		} else if (isUndefined()) {
			return "[UNDEFINED]";
		}
		return "[" + index + ']' + word;
	}

}
