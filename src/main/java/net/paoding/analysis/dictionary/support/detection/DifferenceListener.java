/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer DifferenceListener.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.detection;

/**
 * The listener interface for receiving difference events.
 * The class that is interested in processing a difference
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDifferenceListener<code> method. When
 * the difference event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DifferenceEvent
 */
public interface DifferenceListener {

	/**
	 * On.
	 *
	 * @param diff the diff
	 * @throws Exception the exception
	 */
	public void on(Difference diff) throws Exception ;
}
