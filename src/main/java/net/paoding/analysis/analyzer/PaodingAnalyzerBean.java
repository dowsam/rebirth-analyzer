/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer PaodingAnalyzerBean.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.analyzer;

import java.io.Reader;

import net.paoding.analysis.analyzer.impl.MaxWordLengthTokenCollector;
import net.paoding.analysis.analyzer.impl.MostWordsTokenCollector;
import net.paoding.analysis.knife.Knife;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

/**
 * The Class PaodingAnalyzerBean.
 *
 * @author l.xue.nong
 */
public class PaodingAnalyzerBean extends Analyzer {

	// -------------------------------------------------

	/** The Constant MOST_WORDS_MODE. */
	public static final int MOST_WORDS_MODE = 1;

	/** The Constant MAX_WORD_LENGTH_MODE. */
	public static final int MAX_WORD_LENGTH_MODE = 2;

	// -------------------------------------------------
	/** The knife. */
	private Knife knife;

	/** The mode. */
	private int mode = MOST_WORDS_MODE;

	/** The mode class. */
	private Class modeClass;

	// -------------------------------------------------

	/**
	 * Instantiates a new paoding analyzer bean.
	 */
	public PaodingAnalyzerBean() {
	}

	/**
	 * Instantiates a new paoding analyzer bean.
	 *
	 * @param knife the knife
	 */
	public PaodingAnalyzerBean(Knife knife) {
		this.knife = knife;
	}

	/**
	 * Instantiates a new paoding analyzer bean.
	 *
	 * @param knife the knife
	 * @param mode the mode
	 */
	public PaodingAnalyzerBean(Knife knife, int mode) {
		this.knife = knife;
		this.mode = mode;
	}

	/**
	 * Instantiates a new paoding analyzer bean.
	 *
	 * @param knife the knife
	 * @param mode the mode
	 */
	public PaodingAnalyzerBean(Knife knife, String mode) {
		this.knife = knife;
		this.setMode(mode);
	}

	// -------------------------------------------------

	/**
	 * Gets the knife.
	 *
	 * @return the knife
	 */
	public Knife getKnife() {
		return knife;
	}

	/**
	 * Sets the knife.
	 *
	 * @param knife the new knife
	 */
	public void setKnife(Knife knife) {
		this.knife = knife;
	}

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode the new mode
	 */
	public void setMode(int mode) {
		if (mode != MOST_WORDS_MODE && mode != MAX_WORD_LENGTH_MODE) {
			throw new IllegalArgumentException("wrong mode:" + mode);
		}
		this.mode = mode;
		this.modeClass = null;
	}

	/**
	 * Sets the mode class.
	 *
	 * @param modeClass the new mode class
	 */
	public void setModeClass(Class modeClass) {
		this.modeClass = modeClass;
	}

	/**
	 * Sets the mode class.
	 *
	 * @param modeClass the new mode class
	 */
	public void setModeClass(String modeClass) {
		try {
			this.modeClass = Class.forName(modeClass);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("not found mode class:" + e.getMessage());
		}
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode the new mode
	 */
	public void setMode(String mode) {
		if (mode.startsWith("class:")) {
			setModeClass(mode.substring("class:".length()));
		} else {
			if ("most-words".equalsIgnoreCase(mode)
					|| "default".equalsIgnoreCase(mode)
					|| ("" + MOST_WORDS_MODE).equals(mode)) {
				setMode(MOST_WORDS_MODE);
			} else if ("max-word-length".equalsIgnoreCase(mode)
					|| ("" + MAX_WORD_LENGTH_MODE).equals(mode)) {
				setMode(MAX_WORD_LENGTH_MODE);
			}
			else {
				throw new IllegalArgumentException("不合法的分析器Mode参数设置:" + mode);
			}
		}
	}

	// -------------------------------------------------

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String, java.io.Reader)
	 */
	public TokenStream tokenStream(String fieldName, Reader reader) {
		if (knife == null) {
			throw new NullPointerException("knife should be set before token");
		}
		// PaodingTokenizer是TokenStream实现，使用knife解析reader流入的文本
		return new PaodingTokenizer(reader, knife, createTokenCollector());
	}

	/**
	 * Creates the token collector.
	 *
	 * @return the token collector
	 */
	protected TokenCollector createTokenCollector() {
		if (modeClass != null) {
			try {
				return (TokenCollector) modeClass.newInstance();
			} catch (InstantiationException e) {
				throw new IllegalArgumentException("wrong mode class:" + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException("wrong mode class:" + e.getMessage());
			}
		}
		switch (mode) {
		case MOST_WORDS_MODE:
			return new MostWordsTokenCollector();
		case MAX_WORD_LENGTH_MODE:
			return new MaxWordLengthTokenCollector();
		default:
			throw new Error("never happened");
		}
	}
}
