/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer HashBinaryDictionary.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class HashBinaryDictionary.
 *
 * @author l.xue.nong
 */
public class HashBinaryDictionary implements Dictionary {

	// -------------------------------------------------

	/** The asc words. */
	private Word[] ascWords;

	/** The subs. */
	private Map/* <Object, SubDictionaryWrap> */subs;

	/** The hash index. */
	private final int hashIndex;

	/** The start. */
	private final int start;
	
	/** The end. */
	private final int end;
	
	/** The count. */
	private final int count;

	// -------------------------------------------------

	/**
	 * Instantiates a new hash binary dictionary.
	 *
	 * @param ascWords the asc words
	 * @param initialCapacity the initial capacity
	 * @param loadFactor the load factor
	 */
	public HashBinaryDictionary(Word[] ascWords, int initialCapacity,
			float loadFactor) {
		this(ascWords, 0, 0, ascWords.length, initialCapacity, loadFactor);
	}

	/**
	 * Instantiates a new hash binary dictionary.
	 *
	 * @param ascWords the asc words
	 * @param hashIndex the hash index
	 * @param start the start
	 * @param end the end
	 * @param initialCapacity the initial capacity
	 * @param loadFactor the load factor
	 */
	public HashBinaryDictionary(Word[] ascWords, int hashIndex, int start,
			int end, int initialCapacity, float loadFactor) {
		this.ascWords = ascWords;
		this.start = start;
		this.end = end;
		this.count = end - start;
		this.hashIndex = hashIndex;
		subs = new HashMap/* <Object, SubDictionaryWrap> */(initialCapacity,
				loadFactor);
		createSubDictionaries();
	}

	// -------------------------------------------------

	/**
	 * Creates the sub dictionaries.
	 */
	protected void createSubDictionaries() {
		if (this.start >= ascWords.length) {
			return;
		}
		
		// 定位相同头字符词语的开头和结束位置以确认分字典
		int beginIndex = this.start;
		int endIndex = this.start + 1;
		
		char beginHashChar = getChar(ascWords[start], hashIndex);
		char endHashChar;
		for (; endIndex < this.end; endIndex++) {
			endHashChar = getChar(ascWords[endIndex], hashIndex);
			if (endHashChar != beginHashChar) {
				addSubDictionary(beginHashChar, beginIndex, endIndex);
				beginIndex = endIndex;
				beginHashChar = endHashChar;
			}
		}
		addSubDictionary(beginHashChar, beginIndex, this.end);
	}

	/**
	 * Gets the char.
	 *
	 * @param s the s
	 * @param index the index
	 * @return the char
	 */
	protected char getChar(CharSequence s, int index) {
		if (index >= s.length()) {
			return (char) 0;
		}
		return s.charAt(index);
	}

	/**
	 * Adds the sub dictionary.
	 *
	 * @param hashChar the hash char
	 * @param beginIndex the begin index
	 * @param endIndex the end index
	 */
	protected void addSubDictionary(char hashChar, int beginIndex, int endIndex) {
		Dictionary subDic = createSubDictionary(ascWords, beginIndex, endIndex);
		SubDictionaryWrap subDicWrap = new SubDictionaryWrap(hashChar,
				subDic, beginIndex);
		subs.put(keyOf(hashChar), subDicWrap);
	}

	/**
	 * Creates the sub dictionary.
	 *
	 * @param ascWords the asc words
	 * @param beginIndex the begin index
	 * @param endIndex the end index
	 * @return the dictionary
	 */
	protected Dictionary createSubDictionary(Word[] ascWords, int beginIndex,
			int endIndex) {
		int count = endIndex - beginIndex;
		if (count < 16) {
			return new BinaryDictionary(ascWords, beginIndex, endIndex);
		} else {
			return new HashBinaryDictionary(ascWords, hashIndex + 1,
					beginIndex, endIndex, getCapacity(count), 0.75f);
		}
	}

	/** The Constant capacityCandiate. */
	protected static final int[] capacityCandiate = { 16, 32, 64, 128, 256,
			512, 1024, 2048, 4096, 10192 };

	/**
	 * Gets the capacity.
	 *
	 * @param count the count
	 * @return the capacity
	 */
	protected int getCapacity(int count) {
		int capacity = -1;
		count <<= 2;
		count /= 3;
		count += 1;
		for (int i = 0; i < capacityCandiate.length; i++) {
			if (count <= capacityCandiate[i]) {
				capacity = capacityCandiate[i];
				break;
			}
		}
		if (capacity < 0) {
			capacity = capacityCandiate[capacityCandiate.length - 1];
		}
		return capacity;
	}

	// -------------------------------------------------

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.Dictionary#get(int)
	 */
	public Word get(int index) {
		return ascWords[start + index];
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.Dictionary#search(java.lang.CharSequence, int, int)
	 */
	public Hit search(CharSequence input, int begin, int count) {
		SubDictionaryWrap subDic = (SubDictionaryWrap) subs.get(keyOf(input
				.charAt(hashIndex + begin)));
		if (subDic == null) {
			return Hit.UNDEFINED;
		}
		Dictionary dic = subDic.dic;
		// 对count==hashIndex + 1的处理
		if (count == hashIndex + 1) {
			Word header = dic.get(0);
			if (header.length() == hashIndex + 1) {
				if (subDic.wordIndexOffset + 1 < this.ascWords.length) {
					return new Hit(subDic.wordIndexOffset, header,
							this.ascWords[subDic.wordIndexOffset + 1]);
				} else {
					return new Hit(subDic.wordIndexOffset, header, null);
				}
			} else {
				return new Hit(Hit.UNCLOSED_INDEX, null, header);
			}
		}
		// count > hashIndex + 1
		Hit word = dic.search(input, begin, count);
		if (word.isHit()) {
			int index = subDic.wordIndexOffset + word.getIndex();
			word.setIndex(index);
			if (word.getNext() == null && index < size()) {
				word.setNext(get(index + 1));
			}
		}
		return word;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.Dictionary#size()
	 */
	public int size() {
		return count;
	}

	// -------------------------------------------------

	/**
	 * Key of.
	 *
	 * @param theChar the the char
	 * @return the object
	 */
	protected Object keyOf(char theChar) {
		// return theChar - 0x4E00;// '一'==0x4E00
		return new Integer(theChar);
	}

	/**
	 * The Class SubDictionaryWrap.
	 *
	 * @author l.xue.nong
	 */
	static class SubDictionaryWrap {
		
		/** The hash char. */
		char hashChar;

		/** The dic. */
		Dictionary dic;

		/** The word index offset. */
		int wordIndexOffset;

		/**
		 * Instantiates a new sub dictionary wrap.
		 *
		 * @param hashChar the hash char
		 * @param dic the dic
		 * @param wordIndexOffset the word index offset
		 */
		public SubDictionaryWrap(char hashChar, Dictionary dic,
				int wordIndexOffset) {
			this.hashChar = hashChar;
			this.dic = dic;
			this.wordIndexOffset = wordIndexOffset;
		}
	}

}
