/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Lexeme.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package org.wltea.analyzer;

/**
 * The Class Lexeme.
 *
 * @author l.xue.nong
 */
public final class Lexeme implements Comparable<Lexeme>{
	//lexemeType常量
	//普通词元
	/** The Constant TYPE_CJK_NORMAL. */
	public static final int TYPE_CJK_NORMAL = 0;
	//姓氏
	/** The Constant TYPE_CJK_SN. */
	public static final int TYPE_CJK_SN = 1;
	//尾缀
	/** The Constant TYPE_CJK_SF. */
	public static final int TYPE_CJK_SF = 2;
	//未知的
	/** The Constant TYPE_CJK_UNKNOWN. */
	public static final int TYPE_CJK_UNKNOWN = 3;
	//数词
	/** The Constant TYPE_NUM. */
	public static final int TYPE_NUM = 10;
	//量词
	/** The Constant TYPE_NUMCOUNT. */
	public static final int TYPE_NUMCOUNT = 11;
	//英文
	/** The Constant TYPE_LETTER. */
	public static final int TYPE_LETTER = 20;
	
	//词元的起始位移
	/** The offset. */
	private int offset;
    //词元的相对起始位置
    /** The begin. */
    private int begin;
    //词元的长度
    /** The length. */
    private int length;
    //词元文本
    /** The lexeme text. */
    private String lexemeText;
    //词元类型
    /** The lexeme type. */
    private int lexemeType;
    
    //当前词元的前一个词元
    /** The prev. */
    private Lexeme prev;
    //当前词元的后一个词元
    /** The next. */
    private Lexeme next;
    
	/**
	 * Instantiates a new lexeme.
	 *
	 * @param offset the offset
	 * @param begin the begin
	 * @param length the length
	 * @param lexemeType the lexeme type
	 */
	public Lexeme(int offset , int begin , int length , int lexemeType){
		this.offset = offset;
		this.begin = begin;
		if(length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
		this.lexemeType = lexemeType;
	}
	
    /*
     * 判断词元相等算法
     * 起始位置偏移、起始位置、终止位置相同
     * @see java.lang.Object#equals(Object o)
     */
	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o){
		if(o == null){
			return false;
		}
		
		if(this == o){
			return true;
		}
		
		if(o instanceof Lexeme){
			Lexeme other = (Lexeme)o;
			if(this.offset == other.getOffset()
					&& this.begin == other.getBegin()
					&& this.length == other.getLength()){
				return true;			
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}
	
    /*
     * 词元哈希编码算法
     * @see java.lang.Object#hashCode()
     */
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode(){
    	int absBegin = getBeginPosition();
    	int absEnd = getEndPosition();
    	return  (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
    }
    
    /*
     * 词元在排序集合中的比较算法
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
	/* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Lexeme other) {
		//起始位置优先
        if(this.begin < other.getBegin()){
            return -1;
        }else if(this.begin == other.getBegin()){
        	//词元长度优先
        	if(this.length > other.getLength()){
        		return -1;
        	}else if(this.length == other.getLength()){
        		return 0;
        	}else {//this.length < other.getLength()
        		return 1;
        	}
        	
        }else{//this.begin > other.getBegin()
        	return 1;
        }
	}
	
	/**
	 * Checks if is overlap.
	 *
	 * @param other the other
	 * @return true, if is overlap
	 */
	public boolean isOverlap(Lexeme other){
		if(other != null){
			if(this.getBeginPosition() <= other.getBeginPosition() 
					&& this.getEndPosition() >= other.getEndPosition()){
				return true;
				
			}else if(this.getBeginPosition() >= other.getBeginPosition() 
					&& this.getEndPosition() <= other.getEndPosition()){
				return true;
				
			}else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Gets the offset.
	 *
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Sets the offset.
	 *
	 * @param offset the new offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
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
	 * Gets the begin position.
	 *
	 * @return the begin position
	 */
	public int getBeginPosition(){
		return offset + begin;
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
	 * Gets the end position.
	 *
	 * @return the end position
	 */
	public int getEndPosition(){
		return offset + begin + length;
	}
	
	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength(){
		return this.length;
	}	
	
	/**
	 * Sets the length.
	 *
	 * @param length the new length
	 */
	public void setLength(int length) {
		if(this.length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
	}
	
	/**
	 * Gets the lexeme text.
	 *
	 * @return the lexeme text
	 */
	public String getLexemeText() {
		if(lexemeText == null){
			return "";
		}
		return lexemeText;
	}

	/**
	 * Sets the lexeme text.
	 *
	 * @param lexemeText the new lexeme text
	 */
	public void setLexemeText(String lexemeText) {
		if(lexemeText == null){
			this.lexemeText = "";
			this.length = 0;
		}else{
			this.lexemeText = lexemeText;
			this.length = lexemeText.length();
		}
	}

	/**
	 * Gets the lexeme type.
	 *
	 * @return the lexeme type
	 */
	public int getLexemeType() {
		return lexemeType;
	}

	/**
	 * Sets the lexeme type.
	 *
	 * @param lexemeType the new lexeme type
	 */
	public void setLexemeType(int lexemeType) {
		this.lexemeType = lexemeType;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(this.getBeginPosition()).append("-").append(this.getEndPosition());
		strbuf.append(" : ").append(this.lexemeText).append(" : \t");
		switch(lexemeType) {
			case TYPE_CJK_NORMAL : 
				strbuf.append("CJK_NORMAL");
				break;
			case TYPE_CJK_SF :
				strbuf.append("CJK_SUFFIX");
				break;
			case TYPE_CJK_SN :
				strbuf.append("CJK_NAME");
				break;
			case TYPE_CJK_UNKNOWN :
				strbuf.append("UNKNOWN");
				break;
			case TYPE_NUM : 
				strbuf.append("NUMEBER");
				break;
			case TYPE_NUMCOUNT :
				strbuf.append("COUNT");
				break;
			case TYPE_LETTER :
				strbuf.append("LETTER");
				break;

		}
		return strbuf.toString();
	}

	/**
	 * Gets the prev.
	 *
	 * @return the prev
	 */
	Lexeme getPrev() {
		return prev;
	}

	/**
	 * Sets the prev.
	 *
	 * @param prev the new prev
	 */
	void setPrev(Lexeme prev) {
		this.prev = prev;
	}

	/**
	 * Gets the next.
	 *
	 * @return the next
	 */
	Lexeme getNext() {
		return next;
	}

	/**
	 * Sets the next.
	 *
	 * @param next the new next
	 */
	void setNext(Lexeme next) {
		this.next = next;
	}

	
}