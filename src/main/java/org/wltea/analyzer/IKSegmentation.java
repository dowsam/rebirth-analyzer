/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer IKSegmentation.java 2012-7-6 10:23:23 l.xue.nong$$
 */
package org.wltea.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.help.CharacterHelper;
import org.wltea.analyzer.seg.ISegmenter;

/**
 * The Class IKSegmentation.
 *
 * @author l.xue.nong
 */
public final class IKSegmentation{

	
	/** The input. */
	private Reader input;	
	//默认缓冲区大小
	/** The Constant BUFF_SIZE. */
	private static final int BUFF_SIZE = 3072;
	//缓冲区耗尽的临界值
	/** The Constant BUFF_EXHAUST_CRITICAL. */
	private static final int BUFF_EXHAUST_CRITICAL = 48;	
    //字符窜读取缓冲
    /** The segment buff. */
    private char[] segmentBuff;
	//分词器上下文
	/** The context. */
	private Context context;
	//分词处理器列表
	/** The segmenters. */
	private List<ISegmenter> segmenters;
    
	/**
	 * Instantiates a new iK segmentation.
	 *
	 * @param input the input
	 */
	public IKSegmentation(Reader input){
		this(input , false);
	}
    
	/**
	 * Instantiates a new iK segmentation.
	 *
	 * @param input the input
	 * @param isMaxWordLength the is max word length
	 */
	public IKSegmentation(Reader input , boolean isMaxWordLength){
		this.input = input ;
		segmentBuff = new char[BUFF_SIZE];
		context = new Context(segmentBuff , isMaxWordLength);
		segmenters = Configuration.loadSegmenter();
	}
	
	/**
	 * Next.
	 *
	 * @return the lexeme
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public synchronized Lexeme next() throws IOException {
		if(context.getResultSize() == 0){
			/*
			 * 从reader中读取数据，填充buffer
			 * 如果reader是分次读入buffer的，那么buffer要进行移位处理
			 * 移位处理上次读入的但未处理的数据
			 */
			int available = fillBuffer(input);
			
            if(available <= 0){
            	context.resetContext();
                return null;
            }else{
            	//分词处理
            	int analyzedLength = 0;
        		for(int buffIndex = 0 ; buffIndex < available ;  buffIndex++){
        			//移动缓冲区指针
        			context.setCursor(buffIndex);
        			//进行字符规格化（全角转半角，大写转小写处理）
        			segmentBuff[buffIndex] = CharacterHelper.regularize(segmentBuff[buffIndex]);
        			//遍历子分词器
        			for(ISegmenter segmenter : segmenters){
        				segmenter.nextLexeme(segmentBuff , context);
        			}
        			analyzedLength++;
        			/*
        			 * 满足一下条件时，
        			 * 1.available == BUFF_SIZE 表示buffer满载
        			 * 2.buffIndex < available - 1 && buffIndex > available - BUFF_EXHAUST_CRITICAL表示当前指针处于临界区内
        			 * 3.!context.isBufferLocked()表示没有segmenter在占用buffer
        			 * 要中断当前循环（buffer要进行移位，并再读取数据的操作）
        			 */        			
        			if(available == BUFF_SIZE
        					&& buffIndex < available - 1   
        					&& buffIndex > available - BUFF_EXHAUST_CRITICAL
        					&& !context.isBufferLocked()){

        				break;
        			}
        		}
				
				for(ISegmenter segmenter : segmenters){
					segmenter.reset();
				}
        		//System.out.println(available + " : " +  buffIndex);
            	//记录最近一次分析的字符长度
        		context.setLastAnalyzed(analyzedLength);
            	//同时累计已分析的字符长度
        		context.setBuffOffset(context.getBuffOffset() + analyzedLength);
        		//如果使用最大切分，则过滤交叠的短词元
        		if(context.isMaxWordLength()){
        			context.excludeOverlap();
        		}
            	//读取词元池中的词元
            	return buildLexeme(context.firstLexeme());
            }
		}else{
			//读取词元池中的已有词元
			return buildLexeme(context.firstLexeme());
		}	
	}
	
    /**
     * Fill buffer.
     *
     * @param reader the reader
     * @return the int
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private int fillBuffer(Reader reader) throws IOException{
    	int readCount = 0;
    	if(context.getBuffOffset() == 0){
    		//首次读取reader
    		readCount = reader.read(segmentBuff);
    	}else{
    		int offset = context.getAvailable() - context.getLastAnalyzed();
    		if(offset > 0){
    			//最近一次读取的>最近一次处理的，将未处理的字串拷贝到segmentBuff头部
    			System.arraycopy(segmentBuff , context.getLastAnalyzed() , this.segmentBuff , 0 , offset);
    			readCount = offset;
    		}
    		//继续读取reader ，以onceReadIn - onceAnalyzed为起始位置，继续填充segmentBuff剩余的部分
    		readCount += reader.read(segmentBuff , offset , BUFF_SIZE - offset);
    	}            	
    	//记录最后一次从Reader中读入的可用字符长度
    	context.setAvailable(readCount);
    	return readCount;
    }	
	
    /**
     * Builds the lexeme.
     *
     * @param lexeme the lexeme
     * @return the lexeme
     */
    private Lexeme buildLexeme(Lexeme lexeme){
    	if(lexeme != null){
			//生成lexeme的词元文本
			lexeme.setLexemeText(String.valueOf(segmentBuff , lexeme.getBegin() , lexeme.getLength()));
			return lexeme;
			
		}else{
			return null;
		}
    }

    /**
     * Reset.
     *
     * @param input the input
     */
	public synchronized void reset(Reader input) {
		this.input = input;
		context.resetContext();
		for(ISegmenter segmenter : segmenters){
			segmenter.reset();
		}
	}

}
