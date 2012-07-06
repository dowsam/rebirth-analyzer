/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer MaxWordLengthTokenCollector.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.analyzer.impl;

import java.util.Iterator;
import java.util.LinkedList;

import net.paoding.analysis.analyzer.TokenCollector;

import org.apache.lucene.analysis.Token;

/**
 * The Class MaxWordLengthTokenCollector.
 *
 * @author l.xue.nong
 */
public class MaxWordLengthTokenCollector implements TokenCollector {

	/** The tokens. */
	@SuppressWarnings("rawtypes")
	private LinkedList/* <Token> */tokens = new LinkedList/* <Token> */();

	/** The candidate. */
	private Token candidate;

	/** The last. */
	private Token last;

	/* (non-Javadoc)
	 * @see net.paoding.analysis.analyzer.TokenCollector#iterator()
	 */
	@SuppressWarnings("unchecked")
	public Iterator/* <Token> */iterator() {
		if (candidate != null) {
			this.tokens.add(candidate);
			candidate = null;
		}
		Iterator/* <Token> */iter = this.tokens.iterator();
		this.tokens = new LinkedList/* <Token> */();
		return iter;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Collector#collect(java.lang.String, int, int)
	 */
	public void collect(String word, int offset, int end) {
		Token c = candidate != null ? candidate : last;
		if (c == null) {
			candidate = new Token(word, offset, end);
		} else if (offset == c.startOffset()) {
			if (end > c.endOffset()) {
				candidate = new Token(word, offset, end);
			}
		} else if (offset > c.startOffset()) {
			if (candidate != null) {
				select(candidate);
			}
			if (end > c.endOffset()) {
				candidate = new Token(word, offset, end);
			} else {
				candidate = null;
			}
		} else if (end >= c.endOffset()) {
			if (last != null && last.startOffset() >= offset && last.endOffset() <= end) {
				for (Iterator/* <Token> */iter = tokens.iterator(); iter.hasNext();) {
					last = (Token) iter.next();
					if (last.startOffset() >= offset && last.endOffset() <= end) {
						iter.remove();
					}
				}
			}
			last = null;
			candidate = new Token(word, offset, end);
		}
	}

	/**
	 * Select.
	 *
	 * @param t the t
	 */
	protected void select(Token t) {
		this.tokens.add(t);
		this.last = t;
	}

}
