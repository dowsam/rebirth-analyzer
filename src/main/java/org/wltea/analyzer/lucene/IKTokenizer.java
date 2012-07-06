/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer IKTokenizer.java 2012-7-6 10:23:23 l.xue.nong$$
 */
package org.wltea.analyzer.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;


/**
 * The Class IKTokenizer.
 *
 * @author l.xue.nong
 */
public final class IKTokenizer extends Tokenizer {
	
	//IK分词器实现
	/** The _ ik implement. */
	private IKSegmentation _IKImplement;
	//词元文本属性
	/** The term att. */
	private TermAttribute termAtt;
	//词元位移属性
	/** The offset att. */
	private OffsetAttribute offsetAtt;
	//记录最后一个词元的结束位置
	/** The final offset. */
	private int finalOffset;

	/**
	 * Instantiates a new iK tokenizer.
	 *
	 * @param in the in
	 * @param isMaxWordLength the is max word length
	 */
	public IKTokenizer(Reader in , boolean isMaxWordLength) {
	    super(in);
	    offsetAtt = addAttribute(OffsetAttribute.class);
	    termAtt = addAttribute(TermAttribute.class);
		_IKImplement = new IKSegmentation(in , isMaxWordLength);
	}	
	
	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public final boolean incrementToken() throws IOException {
		//清除所有的词元属性
		clearAttributes();
		Lexeme nextLexeme = _IKImplement.next();
		if(nextLexeme != null){
			//将Lexeme转成Attributes
			//设置词元文本
			termAtt.setTermBuffer(nextLexeme.getLexemeText());
			//设置词元长度
			termAtt.setTermLength(nextLexeme.getLength());
			//设置词元位移
			offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
			//记录分词的最后位置
			finalOffset = nextLexeme.getEndPosition();
			//返会true告知还有下个词元
			return true;
		}
		//返会false告知词元输出完毕
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.analysis.Tokenizer#reset(java.io.Reader)
	 */
	public void reset(Reader input) throws IOException {
		super.reset(input);
		_IKImplement.reset(input);
	}	
	
	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#end()
	 */
	@Override
	public final void end() {
	    // set final offset 
		offsetAtt.setOffset(finalOffset, finalOffset);
	}
	
}
