/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Context.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package org.wltea.analyzer;

import java.util.HashSet;
import java.util.Set;

import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.seg.ISegmenter;

/**
 * The Class Context.
 *
 * @author l.xue.nong
 */
public class Context{
	
	//是否使用最大词长切分（粗粒度）
	/** The is max word length. */
	private boolean isMaxWordLength = false;	
    //记录Reader内已分析的字串总长度
    //在分多段分析词元时，该变量累计当前的segmentBuff相对于reader的位移
	/** The buff offset. */
    private int buffOffset;	
	//最近一次读入的,可处理的字串长度
	/** The available. */
	private int available;
    //最近一次分析的字串长度
    /** The last analyzed. */
    private int lastAnalyzed;	
    //当前缓冲区位置指针
    /** The cursor. */
    private int cursor; 
    //字符窜读取缓冲
    /** The segment buff. */
    private char[] segmentBuff;
    /*
     * 记录正在使用buffer的分词器对象
     * 如果set中存在有分词器对象，则buffer不能进行位移操作（处于locked状态）
     */
    /** The buff locker. */
    private Set<ISegmenter> buffLocker;
    /*
     * 词元结果集，为每次游标的移动，存储切分出来的词元
     */
	/** The lexeme set. */
    private IKSortedLinkSet lexemeSet;

    
    /**
     * Instantiates a new context.
     *
     * @param segmentBuff the segment buff
     * @param isMaxWordLength the is max word length
     */
    Context(char[] segmentBuff , boolean isMaxWordLength){
    	this.isMaxWordLength = isMaxWordLength;
    	this.segmentBuff = segmentBuff;
    	this.buffLocker = new HashSet<ISegmenter>(4);
    	this.lexemeSet = new IKSortedLinkSet();
	}
    
    /**
     * Reset context.
     */
    public void resetContext(){
    	buffLocker.clear();
    	lexemeSet = new IKSortedLinkSet();
    	buffOffset = 0;
    	available = 0;
    	lastAnalyzed = 0;
    	cursor = 0;
    }

	/**
	 * Checks if is max word length.
	 *
	 * @return true, if is max word length
	 */
	public boolean isMaxWordLength() {
		return isMaxWordLength;
	}

	/**
	 * Sets the max word length.
	 *
	 * @param isMaxWordLength the new max word length
	 */
	public void setMaxWordLength(boolean isMaxWordLength) {
		this.isMaxWordLength = isMaxWordLength;
	}
    
	/**
	 * Gets the buff offset.
	 *
	 * @return the buff offset
	 */
	public int getBuffOffset() {
		return buffOffset;
	}


	/**
	 * Sets the buff offset.
	 *
	 * @param buffOffset the new buff offset
	 */
	public void setBuffOffset(int buffOffset) {
		this.buffOffset = buffOffset;
	}

	/**
	 * Gets the last analyzed.
	 *
	 * @return the last analyzed
	 */
	public int getLastAnalyzed() {
		return lastAnalyzed;
	}


	/**
	 * Sets the last analyzed.
	 *
	 * @param lastAnalyzed the new last analyzed
	 */
	public void setLastAnalyzed(int lastAnalyzed) {
		this.lastAnalyzed = lastAnalyzed;
	}


	/**
	 * Gets the cursor.
	 *
	 * @return the cursor
	 */
	public int getCursor() {
		return cursor;
	}


	/**
	 * Sets the cursor.
	 *
	 * @param cursor the new cursor
	 */
	public void setCursor(int cursor) {
		this.cursor = cursor;
	}
	
	/**
	 * Lock buffer.
	 *
	 * @param segmenter the segmenter
	 */
	public void lockBuffer(ISegmenter segmenter){
		this.buffLocker.add(segmenter);
	}
	
	/**
	 * Unlock buffer.
	 *
	 * @param segmenter the segmenter
	 */
	public void unlockBuffer(ISegmenter segmenter){
		this.buffLocker.remove(segmenter);
	}
	
	/**
	 * Checks if is buffer locked.
	 *
	 * @return true, if is buffer locked
	 */
	public boolean isBufferLocked(){
		return this.buffLocker.size() > 0;
	}

	/**
	 * Gets the available.
	 *
	 * @return the available
	 */
	public int getAvailable() {
		return available;
	}

	/**
	 * Sets the available.
	 *
	 * @param available the new available
	 */
	public void setAvailable(int available) {
		this.available = available;
	}
	
	

	/**
	 * First lexeme.
	 *
	 * @return the lexeme
	 */
	public Lexeme firstLexeme() {
		return this.lexemeSet.pollFirst();
	}
	
	/**
	 * Last lexeme.
	 *
	 * @return the lexeme
	 */
	public Lexeme lastLexeme() {
		return this.lexemeSet.pollLast();
	}
	
	/**
	 * Adds the lexeme.
	 *
	 * @param lexeme the lexeme
	 */
	public void addLexeme(Lexeme lexeme){
		if(!Dictionary.isStopWord(segmentBuff , lexeme.getBegin() , lexeme.getLength())){
			this.lexemeSet.addLexeme(lexeme);
		}
	}
	
	/**
	 * Gets the result size.
	 *
	 * @return the result size
	 */
	public int getResultSize(){
		return this.lexemeSet.size();
	}
	
	/**
	 * Exclude overlap.
	 */
	public void excludeOverlap(){
		 this.lexemeSet.excludeOverlap();
	}
	
	/**
	 * The Class IKSortedLinkSet.
	 *
	 * @author l.xue.nong
	 */
	private class IKSortedLinkSet{
		//链表头
		/** The head. */
		private Lexeme head;
		//链表尾
		/** The tail. */
		private Lexeme tail;
		//链表的实际大小
		/** The size. */
		private int size;
		
		/**
		 * Instantiates a new iK sorted link set.
		 */
		private IKSortedLinkSet(){
			this.size = 0;
		}
		
		/**
		 * Adds the lexeme.
		 *
		 * @param lexeme the lexeme
		 */
		private void addLexeme(Lexeme lexeme){
			if(this.size == 0){
				this.head = lexeme;
				this.tail = lexeme;
				this.size++;
				return;
				
			}else{
				if(this.tail.compareTo(lexeme) == 0){//词元与尾部词元相同，不放入集合
					return;
					
				}else if(this.tail.compareTo(lexeme) < 0){//词元接入链表尾部
					this.tail.setNext(lexeme);
					lexeme.setPrev(this.tail);
					this.tail = lexeme;
					this.size++;
					return;
					
				}else if(this.head.compareTo(lexeme) > 0){//词元接入链表头部
					this.head.setPrev(lexeme);
					lexeme.setNext(this.head);
					this.head = lexeme;
					this.size++;
					return;
					
				}else{					
					//从尾部上逆
					Lexeme l = this.tail;
					while(l != null && l.compareTo(lexeme) > 0){
						l = l.getPrev();
					}
					if(l.compareTo(lexeme) == 0){//词元与集合中的词元重复，不放入集合
						return;
						
					}else if(l.compareTo(lexeme) < 0){//词元插入链表中的某个位置
						lexeme.setPrev(l);
						lexeme.setNext(l.getNext());
						l.getNext().setPrev(lexeme);
						l.setNext(lexeme);
						this.size++;
						return;
						
					}
				}
			}
			
		}
		
		/**
		 * Poll first.
		 *
		 * @return the lexeme
		 */
		private Lexeme pollFirst(){
			if(this.size == 1){
				Lexeme first = this.head;
				this.head = null;
				this.tail = null;
				this.size--;
				return first;
			}else if(this.size > 1){
				Lexeme first = this.head;
				this.head = first.getNext();
				first.setNext(null);
				this.size --;
				return first;
			}else{
				return null;
			}
		}
		
		/**
		 * Poll last.
		 *
		 * @return the lexeme
		 */
		private Lexeme pollLast(){
			if(this.size == 1){
				Lexeme last = this.head;
				this.head = null;
				this.tail = null;
				this.size--;
				return last;
				
			}else if(this.size > 1){
				Lexeme last = this.tail;
				this.tail = last.getPrev();
				last.setPrev(null);
				this.size--;
				return last;
				
			}else{
				return null;
			}
		}
		
		/**
		 * Exclude overlap.
		 */
		private void excludeOverlap(){
			if(this.size > 1){
				Lexeme one = this.head;
				Lexeme another = one.getNext();
				do{
					if( one.isOverlap(another)
//							&& Lexeme.TYPE_LETTER != one.getLexemeType()
//							&& Lexeme.TYPE_LETTER != another.getLexemeType()
							){
						
						//邻近的两个词元完全交叠
						another = another.getNext();
						//从链表中断开交叠的词元
						one.setNext(another);
						if(another != null){
							another.setPrev(one);
						}
						this.size--;
						
					}else{//词元不完全交叠
						one = another;
						another = another.getNext();
					}
				}while(another != null);
			}
		}
		
		/**
		 * Size.
		 *
		 * @return the int
		 */
		private int size(){
			return this.size;
		}
		
		
	}

}
