/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-analyzer VersionImpl.java 2012-2-3 13:59:46 l.xue.nong$$
 */
package cn.com.rebirth.analyzer;

import cn.com.rebirth.commons.Version;

/**
 * The Class MiniProjectVersion.
 *
 * @author l.xue.nong
 */
public final class RestartAnalyzerVersion implements Version {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8043854155386241988L;

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.Version#getModuleVersion()
	 */
	@Override
	public String getModuleVersion() {
		return "0.0.1.beta";
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.Version#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "restart-analyzer";
	}

}
