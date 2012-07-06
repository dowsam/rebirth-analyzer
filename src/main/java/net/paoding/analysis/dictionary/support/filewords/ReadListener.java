/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer ReadListener.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.filewords;


/**
 * The listener interface for receiving read events.
 * The class that is interested in processing a read
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addReadListener<code> method. When
 * the read event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ReadEvent
 */
public interface ReadListener {
	
	/**
	 * On file begin.
	 *
	 * @param file the file
	 * @return true, if successful
	 */
	public boolean onFileBegin(String file);
	
	/**
	 * On file end.
	 *
	 * @param file the file
	 */
	public void onFileEnd(String file);
	
	/**
	 * On word.
	 *
	 * @param word the word
	 */
	public void onWord(String word);
}
