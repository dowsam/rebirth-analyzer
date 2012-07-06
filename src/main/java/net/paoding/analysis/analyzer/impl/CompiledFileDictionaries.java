/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer CompiledFileDictionaries.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.analyzer.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.paoding.analysis.dictionary.BinaryDictionary;
import net.paoding.analysis.dictionary.Dictionary;
import net.paoding.analysis.dictionary.HashBinaryDictionary;
import net.paoding.analysis.dictionary.Word;
import net.paoding.analysis.dictionary.support.detection.Detector;
import net.paoding.analysis.dictionary.support.detection.DifferenceListener;
import net.paoding.analysis.dictionary.support.filewords.FileWordsReader;
import net.paoding.analysis.exception.PaodingAnalysisException;
import net.paoding.analysis.knife.CJKKnife;
import net.paoding.analysis.knife.Dictionaries;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class CompiledFileDictionaries.
 *
 * @author l.xue.nong
 */
public class CompiledFileDictionaries implements Dictionaries {

	// -------------------------------------------------

	/** The log. */
	protected Log log = LogFactory.getLog(this.getClass());

	// -------------------------------------------------

	/** The vocabulary dictionary. */
	protected Dictionary vocabularyDictionary;

	/** The combinatorics dictionary. */
	protected Dictionary combinatoricsDictionary;

	/** The confucian family names dictionary. */
	protected Dictionary confucianFamilyNamesDictionary;

	/** The noise charactors dictionary. */
	protected Dictionary noiseCharactorsDictionary;

	/** The noise words dictionary. */
	protected Dictionary noiseWordsDictionary;

	/** The units dictionary. */
	protected Dictionary unitsDictionary;

	// -------------------------------------------------

	/** The dic home. */
	protected String dicHome;
	
	/** The noise charactor. */
	protected String noiseCharactor;
	
	/** The noise word. */
	protected String noiseWord;
	
	/** The unit. */
	protected String unit;
	
	/** The confucian family name. */
	protected String confucianFamilyName;
	
	/** The combinatorics. */
	protected String combinatorics;
	
	/** The charset name. */
	protected String charsetName;
	
	/** The max word len. */
	protected int maxWordLen;

	// ----------------------

	/**
	 * Instantiates a new compiled file dictionaries.
	 */
	public CompiledFileDictionaries() {
	}

	/**
	 * Instantiates a new compiled file dictionaries.
	 *
	 * @param dicHome the dic home
	 * @param noiseCharactor the noise charactor
	 * @param noiseWord the noise word
	 * @param unit the unit
	 * @param confucianFamilyName the confucian family name
	 * @param combinatorics the combinatorics
	 * @param charsetName the charset name
	 * @param maxWordLen the max word len
	 */
	public CompiledFileDictionaries(String dicHome, String noiseCharactor,
			String noiseWord, String unit, String confucianFamilyName,
			String combinatorics, String charsetName, int maxWordLen) {
		this.dicHome = dicHome;
		this.noiseCharactor = noiseCharactor;
		this.noiseWord = noiseWord;
		this.unit = unit;
		this.confucianFamilyName = confucianFamilyName;
		this.combinatorics = combinatorics;
		this.charsetName = charsetName;
		this.maxWordLen = maxWordLen;
	}

	/**
	 * Gets the dic home.
	 *
	 * @return the dic home
	 */
	public String getDicHome() {
		return dicHome;
	}

	/**
	 * Sets the dic home.
	 *
	 * @param dicHome the new dic home
	 */
	public void setDicHome(String dicHome) {
		this.dicHome = dicHome;
	}

	/**
	 * Gets the noise charactor.
	 *
	 * @return the noise charactor
	 */
	public String getNoiseCharactor() {
		return noiseCharactor;
	}

	/**
	 * Sets the noise charactor.
	 *
	 * @param noiseCharactor the new noise charactor
	 */
	public void setNoiseCharactor(String noiseCharactor) {
		this.noiseCharactor = noiseCharactor;
	}

	/**
	 * Gets the noise word.
	 *
	 * @return the noise word
	 */
	public String getNoiseWord() {
		return noiseWord;
	}

	/**
	 * Sets the noise word.
	 *
	 * @param noiseWord the new noise word
	 */
	public void setNoiseWord(String noiseWord) {
		this.noiseWord = noiseWord;
	}

	/**
	 * Gets the unit.
	 *
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the unit.
	 *
	 * @param unit the new unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Gets the confucian family name.
	 *
	 * @return the confucian family name
	 */
	public String getConfucianFamilyName() {
		return confucianFamilyName;
	}

	/**
	 * Sets the confucian family name.
	 *
	 * @param confucianFamilyName the new confucian family name
	 */
	public void setConfucianFamilyName(String confucianFamilyName) {
		this.confucianFamilyName = confucianFamilyName;
	}

	/**
	 * Gets the charset name.
	 *
	 * @return the charset name
	 */
	public String getCharsetName() {
		return charsetName;
	}

	/**
	 * Sets the charset name.
	 *
	 * @param charsetName the new charset name
	 */
	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	/**
	 * Gets the max word len.
	 *
	 * @return the max word len
	 */
	public int getMaxWordLen() {
		return maxWordLen;
	}

	/**
	 * Sets the max word len.
	 *
	 * @param maxWordLen the new max word len
	 */
	public void setMaxWordLen(int maxWordLen) {
		this.maxWordLen = maxWordLen;
	}

    /**
     * Sets the lantin fllowed by cjk.
     *
     * @param lantinFllowedByCjk the new lantin fllowed by cjk
     */
    public void setLantinFllowedByCjk(String lantinFllowedByCjk) {
		this.combinatorics = lantinFllowedByCjk;
	}

	/**
	 * Gets the lantin fllowed by cjk.
	 *
	 * @return the lantin fllowed by cjk
	 */
	public String getLantinFllowedByCjk() {
		return combinatorics;
	}

	// -------------------------------------------------

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Dictionaries#getVocabularyDictionary()
	 */
	public synchronized Dictionary getVocabularyDictionary() {
		if (vocabularyDictionary == null) {
			// 大概有5639个字有词语，故取0x2fff=x^13>8000>8000*0.75=6000>5639
			vocabularyDictionary = new HashBinaryDictionary(
					getVocabularyWords(), 0x2fff, 0.75f);
		}
		return vocabularyDictionary;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Dictionaries#getConfucianFamilyNamesDictionary()
	 */
	public synchronized Dictionary getConfucianFamilyNamesDictionary() {
		if (confucianFamilyNamesDictionary == null) {
			confucianFamilyNamesDictionary = new BinaryDictionary(
					getConfucianFamilyNames());
		}
		return confucianFamilyNamesDictionary;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Dictionaries#getNoiseCharactorsDictionary()
	 */
	public synchronized Dictionary getNoiseCharactorsDictionary() {
		if (noiseCharactorsDictionary == null) {
			noiseCharactorsDictionary = new HashBinaryDictionary(
					getNoiseCharactors(), 256, 0.75f);
		}
		return noiseCharactorsDictionary;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Dictionaries#getNoiseWordsDictionary()
	 */
	public synchronized Dictionary getNoiseWordsDictionary() {
		if (noiseWordsDictionary == null) {
			noiseWordsDictionary = new BinaryDictionary(getNoiseWords());
		}
		return noiseWordsDictionary;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Dictionaries#getUnitsDictionary()
	 */
	public synchronized Dictionary getUnitsDictionary() {
		if (unitsDictionary == null) {
			unitsDictionary = new HashBinaryDictionary(getUnits(), 1024, 0.75f);
		}
		return unitsDictionary;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Dictionaries#getCombinatoricsDictionary()
	 */
	public synchronized Dictionary getCombinatoricsDictionary() {
		if (combinatoricsDictionary == null) {
			combinatoricsDictionary = new BinaryDictionary(
					getCombinatoricsWords());
		}
		return combinatoricsDictionary;
	}

	/** The detector. */
	private Detector detector;

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Dictionaries#startDetecting(int, net.paoding.analysis.dictionary.support.detection.DifferenceListener)
	 */
	public synchronized void startDetecting(int interval, DifferenceListener l) {
		if (detector != null || interval < 0) {
			return;
		}
		Detector detector = new Detector();
		detector.setHome(dicHome);
		detector.setFilter(null);
		detector.setFilter(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getPath().endsWith(".dic.compiled")
						|| pathname.getPath().endsWith(".metadata");
			}
		});
		detector.setLastSnapshot(detector.flash());
		detector.setListener(l);
		detector.setInterval(interval);
		detector.start(true);
		this.detector = detector;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Dictionaries#stopDetecting()
	 */
	public synchronized void stopDetecting() {
		if (detector == null) {
			return;
		}
		detector.setStop();
		detector = null;
	}

	// ---------------------------------------------------------------
	// 以下为辅助性的方式-类私有或package私有

	/**
	 * Gets the dictionary words.
	 *
	 * @param dicNameRelativeDicHome the dic name relative dic home
	 * @return the dictionary words
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Word[] getDictionaryWords(String dicNameRelativeDicHome) {
		File f = new File(this.dicHome, "/" + dicNameRelativeDicHome
				+ ".dic.compiled");
		if (!f.exists()) {
			return new Word[0];
		}
		try {
			Map map = FileWordsReader.readWords(f.getAbsolutePath(),
					charsetName, maxWordLen, LinkedList.class, ".dic.compiled");
			List wordsList = (List) map.values().iterator().next();
			return (Word[]) wordsList.toArray(new Word[wordsList.size()]);
		} catch (IOException e) {
			throw toRuntimeException(e);
		}
	}
	
	
	/**
	 * Gets the vocabulary words.
	 *
	 * @return the vocabulary words
	 */
	protected Word[] getVocabularyWords() {
		return getDictionaryWords("vocabulary");
	}

	/**
	 * Gets the confucian family names.
	 *
	 * @return the confucian family names
	 */
	protected Word[] getConfucianFamilyNames() {
		return getDictionaryWords(confucianFamilyName);
	}

	/**
	 * Gets the noise words.
	 *
	 * @return the noise words
	 */
	protected Word[] getNoiseWords() {
		return getDictionaryWords(noiseWord);
	}

	/**
	 * Gets the noise charactors.
	 *
	 * @return the noise charactors
	 */
	protected Word[] getNoiseCharactors() {
		return getDictionaryWords(noiseCharactor);
	}

	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	protected Word[] getUnits() {
		return getDictionaryWords(unit);
	}

	/**
	 * Gets the combinatorics words.
	 *
	 * @return the combinatorics words
	 */
	protected Word[] getCombinatoricsWords() {
		return getDictionaryWords(combinatorics);
	}

	// --------------------------------------

	/**
	 * To runtime exception.
	 *
	 * @param e the e
	 * @return the runtime exception
	 */
	protected RuntimeException toRuntimeException(IOException e) {
		return new PaodingAnalysisException(e);
	}
}
