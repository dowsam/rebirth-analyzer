/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer SimpleReadListener.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.filewords;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import net.paoding.analysis.dictionary.Word;
import net.paoding.analysis.knife.CharSet;

/**
 * The listener interface for receiving simpleRead events.
 * The class that is interested in processing a simpleRead
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSimpleReadListener<code> method. When
 * the simpleRead event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SimpleReadEvent
 */
public class SimpleReadListener implements ReadListener {
	
	/** The dics. */
	private Map/* <String, Set<Word>> */dics = new Hashtable/* <String, Set<Word>> */();
	
	/** The words. */
	private HashSet/* <Word> */words = new HashSet/* <Word> */();
	
	/** The ext. */
	private String ext = ".dic";

	/**
	 * Instantiates a new simple read listener.
	 *
	 * @param ext the ext
	 */
	public SimpleReadListener(String ext) {
		this.ext = ext;
	}

	/**
	 * Instantiates a new simple read listener.
	 */
	public SimpleReadListener() {
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.support.filewords.ReadListener#onFileBegin(java.lang.String)
	 */
	public boolean onFileBegin(String file) {
		if (!file.endsWith(ext)) {
			return false;
		}
		words = new HashSet/* <String> */();
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
		// 去除汉字数字词
		for (int i = 0; i < wordText.length(); i++) {
			char ch = wordText.charAt(i);
			int num = CharSet.toNumber(ch);
			if (num >= 0) {
				if (i == 0) {
					if (num > 10) {// "十二" vs "千万"
						break;
					}
				}
				if (num == 2) {
					if (wordText.equals("两") || wordText.equals("两两")) {
						break;
					}
				}
				if (i + 1 == wordText.length()) {
					return;
				}
			} else {
				break;
			}
		}
		int index = wordText.indexOf('[');
		if (index == -1) {
			words.add(new Word(wordText));
		}
		else {
			Word w = new Word(wordText.substring(0, index));
			int mindex = wordText.indexOf("m=", index);
			if (mindex != -1) {
				int mEndIndex = wordText.indexOf("]", mindex);
				String m = wordText.substring(mindex + "m=".length(), mEndIndex);
				w.setModifiers(Integer.parseInt(m));
				words.add(w);
			}
		}
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public Map/* <String, Set<String>> */getResult() {
		return dics;
	}

}