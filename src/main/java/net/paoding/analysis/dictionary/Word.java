/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Word.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.dictionary;

/**
 * The Class Word.
 *
 * @author l.xue.nong
 */
public class Word implements Comparable, CharSequence {

	/** The Constant DEFAUL. */
	public static final int DEFAUL = 0;
	
	/** The text. */
	private String text;
	
	/** The modifiers. */
	private int modifiers = DEFAUL;

	/**
	 * Instantiates a new word.
	 */
	public Word() {
	}

	/**
	 * Instantiates a new word.
	 *
	 * @param text the text
	 */
	public Word(String text) {
		this.text = text;
	}

	/**
	 * Instantiates a new word.
	 *
	 * @param text the text
	 * @param modifiers the modifiers
	 */
	public Word(String text, int modifiers) {
		this.text = text;
		this.modifiers = modifiers;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the modifiers.
	 *
	 * @return the modifiers
	 */
	public int getModifiers() {
		return modifiers;
	}

	/**
	 * Sets the modifiers.
	 *
	 * @param modifiers the new modifiers
	 */
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj) {
		return this.text.compareTo(((Word) obj).text);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return text;
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#length()
	 */
	public int length() {
		return text.length();
	}

	/**
	 * Starts with.
	 *
	 * @param word the word
	 * @return true, if successful
	 */
	public boolean startsWith(Word word) {
		return text.startsWith(word.text);
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#charAt(int)
	 */
	public char charAt(int j) {
		return text.charAt(j);
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#subSequence(int, int)
	 */
	public CharSequence subSequence(int start, int end) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return text.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return text.equals(((Word) obj).text);
	}

	/**
	 * Sets the noise charactor.
	 */
	public void setNoiseCharactor() {
		modifiers |= 1;
	}

	/**
	 * Sets the noise word.
	 */
	public void setNoiseWord() {
		modifiers |= (1 << 1);
	}

	/**
	 * Checks if is noise charactor.
	 *
	 * @return true, if is noise charactor
	 */
	public boolean isNoiseCharactor() {
		return (modifiers & 1) == 1;
	}

	/**
	 * Checks if is noise.
	 *
	 * @return true, if is noise
	 */
	public boolean isNoise() {
		return isNoiseCharactor() || isNoiseWord();
	}

	/**
	 * Checks if is noise word.
	 *
	 * @return true, if is noise word
	 */
	public boolean isNoiseWord() {
		return (modifiers >> 1 & 1) == 1;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Word w = new Word("");
		System.out.println(w.isNoiseCharactor());
		w.setNoiseCharactor();
		System.out.println(w.isNoiseCharactor());
		System.out.println(w.isNoiseWord());
		w.setNoiseWord();
		System.out.println(w.isNoiseWord());
	}

}
