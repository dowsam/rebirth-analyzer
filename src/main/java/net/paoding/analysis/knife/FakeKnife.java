/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer FakeKnife.java 2012-7-6 10:23:23 l.xue.nong$$
 */
package net.paoding.analysis.knife;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class FakeKnife.
 *
 * @author l.xue.nong
 */

public class FakeKnife implements Knife, DictionariesWare {

	/** The log. */
	private Log log = LogFactory.getLog(this.getClass());

	/** The name. */
	private String name;

	/** The int param. */
	private int intParam;

	/** The inner. */
	private Inner inner = new Inner();

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
		log.info("set property: name=" + name);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the int param.
	 *
	 * @return the int param
	 */
	public int getIntParam() {
		return intParam;
	}

	/**
	 * Sets the int param.
	 *
	 * @param intParam the new int param
	 */
	public void setIntParam(int intParam) {
		this.intParam = intParam;
		log.info("set property: intParam=" + intParam);
	}

	/**
	 * Sets the inner.
	 *
	 * @param inner the new inner
	 */
	public void setInner(Inner inner) {
		this.inner = inner;
	}

	/**
	 * Gets the inner.
	 *
	 * @return the inner
	 */
	public Inner getInner() {
		return inner;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Knife#assignable(net.paoding.analysis.knife.Beef, int, int)
	 */
	public int assignable(Beef beef, int offset, int index) {
		return LIMIT;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Knife#dissect(net.paoding.analysis.knife.Collector, net.paoding.analysis.knife.Beef, int)
	 */
	public int dissect(Collector collector, Beef beef, int offset) {
		throw new Error("this knife doesn't accept any beef");
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.DictionariesWare#setDictionaries(net.paoding.analysis.knife.Dictionaries)
	 */
	public void setDictionaries(Dictionaries dictionaries) {
	}

	/**
	 * The Class Inner.
	 *
	 * @author l.xue.nong
	 */
	class Inner {
		
		/** The bool. */
		private boolean bool;

		/**
		 * Sets the bool.
		 *
		 * @param bool the new bool
		 */
		public void setBool(boolean bool) {
			this.bool = bool;
			log.info("set property: bool=" + bool);
		}

		/**
		 * Checks if is bool.
		 *
		 * @return true, if is bool
		 */
		public boolean isBool() {
			return bool;
		}
	}

}
