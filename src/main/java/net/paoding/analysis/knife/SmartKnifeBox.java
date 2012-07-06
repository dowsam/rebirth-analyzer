/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer SmartKnifeBox.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.knife;

/**
 * The Class SmartKnifeBox.
 *
 * @author l.xue.nong
 */
public class SmartKnifeBox extends KnifeBox implements Knife {

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.KnifeBox#dissect(net.paoding.analysis.knife.Collector, net.paoding.analysis.knife.Beef, int)
	 */
	public int dissect(Collector collector, Beef beef, int offset) {
		final int beefLength = beef.length();
		while (offset >= 0 && offset < beefLength) {
			offset = super.dissect(collector, beef, offset);
		}
		return offset;
	}
}
