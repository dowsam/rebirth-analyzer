/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Hit.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package org.wltea.analyzer.dic;

/**
 * The Class Hit.
 *
 * @author l.xue.nong
 */
public class Hit {
	//Hit不匹配
	/** The Constant UNMATCH. */
	private static final int UNMATCH = 0x00000000;
	//Hit完全匹配
	/** The Constant MATCH. */
	private static final int MATCH = 0x00000001;
	//Hit前缀匹配
	/** The Constant PREFIX. */
	private static final int PREFIX = 0x00000010;
	
	
	//该HIT当前状态，默认未匹配
	/** The hit state. */
	private int hitState = UNMATCH;
	
	//记录词典匹配过程中，当前匹配到的词典分支节点
	/** The matched dict segment. */
	private DictSegment matchedDictSegment; 
	/*
	 * 词段开始位置
	 */
	/** The begin. */
	private int begin;
	/*
	 * 词段的结束位置
	 */
	/** The end. */
	private int end;
	
	
	/**
	 * Checks if is match.
	 *
	 * @return true, if is match
	 */
	public boolean isMatch() {
		return (this.hitState & MATCH) > 0;
	}
	
	/**
	 * Sets the match.
	 */
	public void setMatch() {
		this.hitState = this.hitState | MATCH;
	}

	/**
	 * Checks if is prefix.
	 *
	 * @return true, if is prefix
	 */
	public boolean isPrefix() {
		return (this.hitState & PREFIX) > 0;
	}
	
	/**
	 * Sets the prefix.
	 */
	public void setPrefix() {
		this.hitState = this.hitState | PREFIX;
	}
	
	/**
	 * Checks if is unmatch.
	 *
	 * @return true, if is unmatch
	 */
	public boolean isUnmatch() {
		return this.hitState == UNMATCH ;
	}
	
	/**
	 * Sets the unmatch.
	 */
	public void setUnmatch() {
		this.hitState = UNMATCH;
	}
	
	/**
	 * Gets the matched dict segment.
	 *
	 * @return the matched dict segment
	 */
	public DictSegment getMatchedDictSegment() {
		return matchedDictSegment;
	}
	
	/**
	 * Sets the matched dict segment.
	 *
	 * @param matchedDictSegment the new matched dict segment
	 */
	public void setMatchedDictSegment(DictSegment matchedDictSegment) {
		this.matchedDictSegment = matchedDictSegment;
	}
	
	/**
	 * Gets the begin.
	 *
	 * @return the begin
	 */
	public int getBegin() {
		return begin;
	}
	
	/**
	 * Sets the begin.
	 *
	 * @param begin the new begin
	 */
	public void setBegin(int begin) {
		this.begin = begin;
	}
	
	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * Sets the end.
	 *
	 * @param end the new end
	 */
	public void setEnd(int end) {
		this.end = end;
	}	
	
}
