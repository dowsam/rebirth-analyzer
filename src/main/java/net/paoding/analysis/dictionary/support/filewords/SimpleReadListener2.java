/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer SimpleReadListener2.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.filewords;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import net.paoding.analysis.dictionary.Word;

/**
 * The Class SimpleReadListener2.
 *
 * @author l.xue.nong
 */
public class SimpleReadListener2 implements ReadListener {
	
	/** The dics. */
	private Map/* <String, Collection<Word>> */dics = new Hashtable/* <String, Collection<String>> */();
	
	/** The collection class. */
	private Class collectionClass = HashSet.class;
	
	/** The words. */
	private Collection/* <Word> */words;
	
	/** The ext. */
	private String ext = ".dic";

	/**
	 * Instantiates a new simple read listener2.
	 *
	 * @param collectionClass the collection class
	 * @param ext the ext
	 */
	public SimpleReadListener2(Class collectionClass, String ext) {
		this.ext = ext;
		this.collectionClass = collectionClass;
	}

	/**
	 * Instantiates a new simple read listener2.
	 */
	public SimpleReadListener2() {
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.support.filewords.ReadListener#onFileBegin(java.lang.String)
	 */
	public boolean onFileBegin(String file) {
		if (!file.endsWith(ext)) {
			return false;
		}
		try {
			words = (Collection) collectionClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.support.filewords.ReadListener#onFileEnd(java.lang.String)
	 */
	public void onFileEnd(String file) {
		String name = file.substring(0, file.length() - 4);
		dics.put(name, words);
		words = null;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.support.filewords.ReadListener#onWord(java.lang.String)
	 */
	public void onWord(String wordText) {
		wordText = wordText.trim().toLowerCase();
		if (wordText.length() == 0 || wordText.charAt(0) == '#'
				|| wordText.charAt(0) == '-') {
			return;
		}
		
		if (!wordText.endsWith("]")) {
			words.add(new Word(wordText));
		}
		else {
			int index = wordText.indexOf('[');
			Word w = new Word(wordText.substring(0, index));
			int mindex = wordText.indexOf("m=", index);
			int mEndIndex = wordText.indexOf("]", mindex);
			String m = wordText.substring(mindex + "m=".length(), mEndIndex);
			w.setModifiers(Integer.parseInt(m));
			words.add(w);
		}
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public Map/* <String, Collection<Word>> */getResult() {
		return dics;
	}

}