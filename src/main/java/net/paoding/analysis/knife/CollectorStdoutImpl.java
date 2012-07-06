/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer CollectorStdoutImpl.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.knife;

/**
 * The Class CollectorStdoutImpl.
 *
 * @author l.xue.nong
 */
public class CollectorStdoutImpl implements Collector {

	/** The tl. */
	private static ThreadLocal/* <Integer> */tl = new ThreadLocal/* <Integer> */() {
		protected Object/* Integer */initialValue() {
			return new Integer(0);
		}
	};

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Collector#collect(java.lang.String, int, int)
	 */
	public void collect(String word, int begin, int end) {
		int last = ((Integer) tl.get()).intValue();
		Integer c = new Integer(last + 1);
		tl.set(c);
		System.out.println(c + ":\t[" + begin + ", " + end + ")=" + word);
	}

}
