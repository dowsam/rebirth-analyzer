/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer rebirthAnalyzerVersion.java 2012-7-6 10:33:17 l.xue.nong$$
 */
package cn.com.rebirth.analyzer;

import cn.com.rebirth.commons.AbstractVersion;
import cn.com.rebirth.commons.Version;

/**
 * The Class rebirthAnalyzerVersion.
 *
 * @author l.xue.nong
 */
public final class RebirthAnalyzerVersion extends AbstractVersion implements Version {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8043854155386241988L;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Version#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "rebirth-analyzer";
	}

}
