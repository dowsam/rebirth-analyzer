/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Knife.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.knife;

/**
 * The Interface Knife.
 *
 * @author l.xue.nong
 */
public interface Knife {

	/** The assigned. */
	int ASSIGNED = 1;

	/** The point. */
	int POINT = 0;

	/** The limit. */
	int LIMIT = -1;

	/**
	 * Assignable.
	 *
	 * @param beef the beef
	 * @param offset the offset
	 * @param index the index
	 * @return the int
	 */
	public int assignable(Beef beef, int offset, int index);

	/**
	 * Dissect.
	 *
	 * @param collector the collector
	 * @param beef the beef
	 * @param offset the offset
	 * @return the int
	 */
	public int dissect(Collector collector, Beef beef, int offset);
}
