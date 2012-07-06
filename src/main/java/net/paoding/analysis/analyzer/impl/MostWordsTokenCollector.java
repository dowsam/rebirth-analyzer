/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer MostWordsTokenCollector.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.analyzer.impl;

import java.util.Iterator;

import net.paoding.analysis.analyzer.TokenCollector;

import org.apache.lucene.analysis.Token;

/**
 * The Class MostWordsTokenCollector.
 *
 * @author l.xue.nong
 */
public class MostWordsTokenCollector implements TokenCollector, Iterator {

	/** The first token. */
	private LinkedToken firstToken;
	
	/** The last token. */
	private LinkedToken lastToken;

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Collector#collect(java.lang.String, int, int)
	 */
	public void collect(String word, int begin, int end) {
		LinkedToken tokenToAdd = new LinkedToken(word, begin, end);
		if (firstToken == null) {
			firstToken = tokenToAdd;
			lastToken = tokenToAdd;
			return;
		}
		if (tokenToAdd.compareTo(lastToken) > 0) {
			tokenToAdd.pre = lastToken;
			lastToken.next = tokenToAdd;
			lastToken = tokenToAdd;
			//
		} else {
			LinkedToken curTokenToTry = lastToken.pre;
			while (curTokenToTry != null
					&& tokenToAdd.compareTo(curTokenToTry) < 0) {
				curTokenToTry = curTokenToTry.pre;
			}
			if (curTokenToTry == null) {
				firstToken.pre = tokenToAdd;
				tokenToAdd.next = firstToken;
				firstToken = tokenToAdd;
			} else {
				tokenToAdd.next = curTokenToTry.next;
				curTokenToTry.next.pre = tokenToAdd;
				tokenToAdd.pre = curTokenToTry;
				curTokenToTry.next = tokenToAdd;
				
			}
		}
	}

	/** The next linked token. */
	private LinkedToken nextLinkedToken;

	/* (non-Javadoc)
	 * @see net.paoding.analysis.analyzer.TokenCollector#iterator()
	 */
	public Iterator/* <Token> */iterator() {
		nextLinkedToken = firstToken;
		firstToken = null;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return nextLinkedToken != null;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		LinkedToken ret = nextLinkedToken;
		nextLinkedToken = nextLinkedToken.next;
		return ret;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
	}

	/**
	 * The Class LinkedToken.
	 *
	 * @author l.xue.nong
	 */
	private static class LinkedToken extends Token implements Comparable {
		
		/** The pre. */
		public LinkedToken pre;
		
		/** The next. */
		public LinkedToken next;

		/**
		 * Instantiates a new linked token.
		 *
		 * @param word the word
		 * @param begin the begin
		 * @param end the end
		 */
		public LinkedToken(String word, int begin, int end) {
			super(word, begin, end);
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object obj) {
			LinkedToken that = (LinkedToken) obj;
			// 简单/单单/简简单单/
			if (this.endOffset() > that.endOffset())
				return 1;
			if (this.endOffset() == that.endOffset()) {
				return that.startOffset() - this.startOffset();
			}
			return -1;
		}
	}

}
