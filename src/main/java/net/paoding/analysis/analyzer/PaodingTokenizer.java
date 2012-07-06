/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer PaodingTokenizer.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import net.paoding.analysis.analyzer.impl.MostWordsTokenCollector;
import net.paoding.analysis.knife.Beef;
import net.paoding.analysis.knife.Collector;
import net.paoding.analysis.knife.Knife;
import net.paoding.analysis.knife.Paoding;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * The Class PaodingTokenizer.
 *
 * @author l.xue.nong
 */
public final class PaodingTokenizer extends Tokenizer implements Collector {

	// -------------------------------------------------

	/** The input length. */
	private int inputLength;

	/** The Constant bufferLength. */
	private static final int bufferLength = 128;

	/** The buffer. */
	private final char[] buffer = new char[bufferLength];

	/** The offset. */
	private int offset;

	/** The beef. */
	private final Beef beef = new Beef(buffer, 0, 0);

	/** The dissected. */
	private int dissected;

	/** The knife. */
	private Knife knife;

	/** The token collector. */
	private TokenCollector tokenCollector;

	/** The token iteractor. */
	private Iterator<Token> tokenIteractor;

	/** The term att. */
	private TermAttribute termAtt;
	
	/** The offset att. */
	private OffsetAttribute offsetAtt;
	
	/** The type att. */
	private TypeAttribute typeAtt;

	// -------------------------------------------------

	/**
	 * Instantiates a new paoding tokenizer.
	 *
	 * @param input the input
	 * @param knife the knife
	 * @param tokenCollector the token collector
	 */
	public PaodingTokenizer(Reader input, Knife knife,
			TokenCollector tokenCollector) {
		this.input = input;
		this.knife = knife;
		this.tokenCollector = tokenCollector;
		init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		termAtt = addAttribute(TermAttribute.class);
		offsetAtt = addAttribute(OffsetAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
	}

	// -------------------------------------------------

	/**
	 * Gets the token collector.
	 *
	 * @return the token collector
	 */
	public TokenCollector getTokenCollector() {
		return tokenCollector;
	}

	/**
	 * Sets the token collector.
	 *
	 * @param tokenCollector the new token collector
	 */
	public void setTokenCollector(TokenCollector tokenCollector) {
		this.tokenCollector = tokenCollector;
	}

	// -------------------------------------------------

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Collector#collect(java.lang.String, int, int)
	 */
	public void collect(String word, int offset, int end) {
		tokenCollector.collect(word, this.offset + offset, this.offset + end);
	}

	// -------------------------------------------------
	/**
	 * Gets the input length.
	 *
	 * @return the input length
	 */
	public int getInputLength() {
		return inputLength;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public boolean incrementToken() throws IOException {
		// 已经穷尽tokensIteractor的Token对象，则继续请求reader流入数据
		while (tokenIteractor == null || !tokenIteractor.hasNext()) {
			// System.out.println(dissected);
			int read = 0;
			int remainning = -1;// 重新从reader读入字符前，buffer中还剩下的字符数，负数表示当前暂不需要从reader中读入字符
			if (dissected >= beef.length()) {
				remainning = 0;
			} else if (dissected < 0) {
				remainning = bufferLength + dissected;
			}
			if (remainning >= 0) {
				if (remainning > 0) {
					System.arraycopy(buffer, -dissected, buffer, 0, remainning);
				}
				read = input
						.read(buffer, remainning, bufferLength - remainning);
				inputLength += read;
				int charCount = remainning + read;
				if (charCount < 0) {
					// reader已尽，按接口next()要求返回null.
					return false;
				}
				if (charCount < bufferLength) {
					buffer[charCount++] = 0;
				}
				// 构造“牛”，并使用knife“解”之
				beef.set(0, charCount);
				offset += Math.abs(dissected);
				// offset -= remainning;
				dissected = 0;
			}
			dissected = knife.dissect((Collector) this, beef, dissected);
			// offset += read;// !!!
			tokenIteractor = tokenCollector.iterator();
		}
		// 返回tokensIteractor下一个Token对象
		Token token = tokenIteractor.next();
		termAtt.setTermBuffer(token.term());
		offsetAtt.setOffset(correctOffset(token.startOffset()),
				correctOffset(token.endOffset()));
		typeAtt.setType("paoding");
		return true;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#reset()
	 */
	@Override
	public void reset() throws IOException {
		super.reset();
		offset = 0;
		inputLength = 0;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.Tokenizer#reset(java.io.Reader)
	 */
	@Override
	public void reset(Reader input) throws IOException {
		super.reset(input);
		reset();
	}
}
