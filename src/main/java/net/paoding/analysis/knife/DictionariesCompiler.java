/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer DictionariesCompiler.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.knife;


import java.util.Properties;

/**
 * The Interface DictionariesCompiler.
 *
 * @author l.xue.nong
 */
public interface DictionariesCompiler {

	/**
	 * Should compile.
	 *
	 * @param p the p
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean shouldCompile(Properties p) throws Exception;
	
	/**
	 * Compile.
	 *
	 * @param dictionaries the dictionaries
	 * @param knife the knife
	 * @param p the p
	 * @throws Exception the exception
	 */
	public void compile(Dictionaries dictionaries, Knife knife, Properties p) throws Exception;
	
	/**
	 * Read complied dictionaries.
	 *
	 * @param p the p
	 * @return the dictionaries
	 * @throws Exception the exception
	 */
	public Dictionaries readCompliedDictionaries(Properties p) throws Exception;
}
