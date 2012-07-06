/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer KnifeBox.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.knife;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Class KnifeBox.
 *
 * @author l.xue.nong
 */
public class KnifeBox implements Knife {

	/** The knives. */
	private Knife[] knives;

	/** The size. */
	private int size;

	/**
	 * Instantiates a new knife box.
	 */
	public KnifeBox() {
	}

	/**
	 * Instantiates a new knife box.
	 *
	 * @param knives the knives
	 */
	public KnifeBox(List/* <Knife> */knives) {
		this.setKnives(knives);
	}

	/**
	 * Instantiates a new knife box.
	 *
	 * @param knives the knives
	 */
	public KnifeBox(Knife[] knives) {
		this.setKnives(knives);
	}

	/**
	 * Gets the knives.
	 *
	 * @return the knives
	 */
	public Knife[] getKnives() {
		return knives;
	}

	/**
	 * Sets the knives.
	 *
	 * @param knifeList the new knives
	 */
	public void setKnives(List/* <Knife> */knifeList) {
		if (knifeList == null) {
			knifeList = new ArrayList(0);
		}
		size = knifeList.size();
		this.knives = new Knife[size];
		Iterator iter = knifeList.iterator();
		for (int i = 0; i < size; i++) {
			this.knives[i] = (Knife) iter.next();
		}
	}
	
	/**
	 * Sets the knives.
	 *
	 * @param knives the new knives
	 */
	public void setKnives(Knife[] knives) {
		if (knives == null) {
			knives = new Knife[0];
		}
		size = knives.length;
		this.knives = new Knife[size];
		System.arraycopy(knives, 0, this.knives, 0, size);
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Knife#assignable(net.paoding.analysis.knife.Beef, int, int)
	 */
	public int assignable(Beef beef, int offset, int index) {
		return ASSIGNED;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Knife#dissect(net.paoding.analysis.knife.Collector, net.paoding.analysis.knife.Beef, int)
	 */
	public int dissect(Collector collector, Beef beef, int offset) {
		Knife knife;
		for (int i = 0; i < size; i++) {
			knife = knives[i];
			if (ASSIGNED == knife.assignable(beef, offset, offset)) {
				int lastLimit = knife.dissect(collector, beef, offset);
				// 如果返回的下一个分词点发生了变化(可进可退)，则直接返回之，
				// 否则继续让下一个Knife有机会分词
				if (lastLimit != offset) {
					return lastLimit;
				}
			}
		}
		return ++offset;
	}

}
