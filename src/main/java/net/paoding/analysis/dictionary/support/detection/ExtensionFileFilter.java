/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer ExtensionFileFilter.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.detection;

import java.io.File;
import java.io.FileFilter;

/**
 * The Class ExtensionFileFilter.
 *
 * @author l.xue.nong
 */
public class ExtensionFileFilter implements FileFilter {
	
	/** The end. */
	private String end;

	/**
	 * Instantiates a new extension file filter.
	 */
	public ExtensionFileFilter() {
	}

	/**
	 * Instantiates a new extension file filter.
	 *
	 * @param end the end
	 */
	public ExtensionFileFilter(String end) {
		this.end = end;
	}

	/**
	 * Sets the end.
	 *
	 * @param end the new end
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File pathname) {
		return pathname.isDirectory() || pathname.getName().endsWith(end);
	}

}
