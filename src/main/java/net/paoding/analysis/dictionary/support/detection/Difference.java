/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Difference.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.detection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class Difference.
 *
 * @author l.xue.nong
 */
public class Difference {

	/** The modified. */
	private List/* <Node> */modified = new LinkedList/* <Node> */();

	/** The deleted. */
	private List/* <Node> */deleted = new LinkedList/* <Node> */();

	/** The newcome. */
	private List/* <Node> */newcome = new LinkedList/* <Node> */();

	/** The older. */
	private Snapshot older;
	
	/** The younger. */
	private Snapshot younger;

	/**
	 * Gets the modified.
	 *
	 * @return the modified
	 */
	public List/* <Node> */getModified() {
		return modified;
	}

	/**
	 * Sets the modified.
	 *
	 * @param modified the new modified
	 */
	public void setModified(List/* <Node> */modified) {
		this.modified = modified;
	}

	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public List/* <Node> */getDeleted() {
		return deleted;
	}

	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(List/* <Node> */deleted) {
		this.deleted = deleted;
	}

	/**
	 * Gets the newcome.
	 *
	 * @return the newcome
	 */
	public List/* <Node> */getNewcome() {
		return newcome;
	}

	/**
	 * Sets the newcome.
	 *
	 * @param newcome the new newcome
	 */
	public void setNewcome(List/* <Node> */newcome) {
		this.newcome = newcome;
	}

	/**
	 * Gets the older.
	 *
	 * @return the older
	 */
	public Snapshot getOlder() {
		return older;
	}

	/**
	 * Sets the older.
	 *
	 * @param older the new older
	 */
	public void setOlder(Snapshot older) {
		this.older = older;
	}

	/**
	 * Gets the younger.
	 *
	 * @return the younger
	 */
	public Snapshot getYounger() {
		return younger;
	}

	/**
	 * Sets the younger.
	 *
	 * @param younger the new younger
	 */
	public void setYounger(Snapshot younger) {
		this.younger = younger;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return deleted.isEmpty() && modified.isEmpty() && newcome.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String smodified = ArraysToString(modified.toArray(new Node[] {}));
		String snewcome = ArraysToString(newcome.toArray(new Node[] {}));
		String sdeleted = ArraysToString(deleted.toArray(new Node[] {}));
		return "modified=" + smodified + ";newcome=" + snewcome + ";deleted="
				+ sdeleted;
	}

	// 低于JDK1.5无Arrays.toString()方法，故有以下方法
	/**
	 * Arrays to string.
	 *
	 * @param a the a
	 * @return the string
	 */
	private static String ArraysToString(Object[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuffer b = new StringBuffer();
		b.append('[');
		for (int i = 0;; i++) {
			b.append(String.valueOf(a[i]));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}
}
