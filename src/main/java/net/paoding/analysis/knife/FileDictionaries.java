/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer FileDictionaries.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.knife;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.paoding.analysis.dictionary.BinaryDictionary;
import net.paoding.analysis.dictionary.Dictionary;
import net.paoding.analysis.dictionary.HashBinaryDictionary;
import net.paoding.analysis.dictionary.Hit;
import net.paoding.analysis.dictionary.Word;
import net.paoding.analysis.dictionary.support.detection.Detector;
import net.paoding.analysis.dictionary.support.detection.DifferenceListener;
import net.paoding.analysis.dictionary.support.detection.ExtensionFileFilter;
import net.paoding.analysis.dictionary.support.filewords.FileWordsReader;
import net.paoding.analysis.exception.PaodingAnalysisException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class FileDictionaries.
 *
 * @author l.xue.nong
 */
public class FileDictionaries implements Dictionaries {

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

	/** The all words. */
	protected Map/* <String, Set<String>> */allWords;

	/** The dic home. */
	protected String dicHome;
	
	/** The skip prefix. */
	protected String skipPrefix;
	
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
	 * Instantiates a new file dictionaries.
	 */
	public FileDictionaries() {
	}

	/**
	 * Instantiates a new file dictionaries.
	 *
	 * @param dicHome the dic home
	 * @param skipPrefix the skip prefix
	 * @param noiseCharactor the noise charactor
	 * @param noiseWord the noise word
	 * @param unit the unit
	 * @param confucianFamilyName the confucian family name
	 * @param combinatorics the combinatorics
	 * @param charsetName the charset name
	 * @param maxWordLen the max word len
	 */
	public FileDictionaries(String dicHome, String skipPrefix,
			String noiseCharactor, String noiseWord, String unit,
			String confucianFamilyName, String combinatorics, String charsetName, int maxWordLen) {
		this.dicHome = dicHome;
		this.skipPrefix = skipPrefix;
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
	 * Gets the skip prefix.
	 *
	 * @return the skip prefix
	 */
	public String getSkipPrefix() {
		return skipPrefix;
	}

	/**
	 * Sets the skip prefix.
	 *
	 * @param skipPrefix the new skip prefix
	 */
	public void setSkipPrefix(String skipPrefix) {
		this.skipPrefix = skipPrefix;
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
			Dictionary noiseWordsDic = getNoiseWordsDictionary();
			for (int i = 0; i < noiseWordsDic.size(); i++) {
				Hit hit = vocabularyDictionary.search(noiseWordsDic.get(i), 0, noiseWordsDic.get(i).length());
				if (hit.isHit()) {
					hit.getWord().setNoiseWord();
				}
			}
			Dictionary noiseCharactorsDic = getNoiseCharactorsDictionary();
			for (int i = 0; i < noiseCharactorsDic.size(); i++) {
				Hit hit = vocabularyDictionary.search(noiseCharactorsDic.get(i), 0, noiseCharactorsDic.get(i).length());
				if (hit.isHit()) {
					hit.getWord().setNoiseCharactor();
				}
			}
			
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
		detector.setFilter(new ExtensionFileFilter(".dic"));
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
	
	/**
	 * Refresh dic words.
	 *
	 * @param dicPath the dic path
	 */
	protected synchronized void refreshDicWords(String dicPath) {
		int index = dicPath.lastIndexOf(".dic");
		String dicName = dicPath.substring(0, index);
		if (allWords != null) {
			try {
				Map/* <String, Set<String>> */temp = FileWordsReader
						.readWords(dicHome + dicPath, charsetName, maxWordLen);
				allWords.put(dicName, temp.values().iterator().next());
			} catch (FileNotFoundException e) {
				// 如果源文件已经被删除了，则表示该字典不要了
				allWords.remove(dicName);
			} catch (IOException e) {
				throw toRuntimeException(e);
			}
			if (!isSkipForVacabulary(dicName)) {
				this.vocabularyDictionary = null;
			}
			// 如果来的是noiseWord
			if (isNoiseWordDicFile(dicName)) {
				this.noiseWordsDictionary = null;
				// noiseWord和vocabulary有关，所以需要更新vocabulary
				this.vocabularyDictionary = null;
			}
			// 如果来的是noiseCharactors
			else if (isNoiseCharactorDicFile(dicName)) {
				this.noiseCharactorsDictionary = null;
				// noiseCharactorsDictionary和vocabulary有关，所以需要更新vocabulary
				this.vocabularyDictionary = null;
			}
			// 如果来的是单元
			else if (isUnitDicFile(dicName)) {
				this.unitsDictionary = null;
			}
			// 如果来的是亚洲人人姓氏
			else if (isConfucianFamilyNameDicFile(dicName)) {
				this.confucianFamilyNamesDictionary = null;
			}
			// 如果来的是以字母,数字等组合类语言为开头的词汇
			else if (isLantinFollowedByCjkDicFile(dicName)) {
				this.combinatoricsDictionary = null;
			}
		}
	}

	// ---------------------------------------------------------------
	// 以下为辅助性的方式-类私有或package私有

	/**
	 * Gets the vocabulary words.
	 *
	 * @return the vocabulary words
	 */
	protected Word[] getVocabularyWords() {
		Map/* <String, Set<Word>> */dics = loadAllWordsIfNecessary();
		Set/* <Word> */set = null;
		Iterator/* <Word> */iter = dics.keySet().iterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			if (isSkipForVacabulary(name)) {
				continue;
			}
			Set/* <Word> */dic = (Set/* <Word> */) dics.get(name);
			if (set == null) {
				set = new HashSet/* <Word> */(dic);
			} else {
				set.addAll(dic);
			}
		}
		Word[] words = (Word[]) set.toArray(new Word[set.size()]);
		Arrays.sort(words);
		return words;
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

	/**
	 * Gets the dictionary words.
	 *
	 * @param dicNameRelativeDicHome the dic name relative dic home
	 * @return the dictionary words
	 */
	protected Word[] getDictionaryWords(String dicNameRelativeDicHome) {
		Map dics;
		try {
			dics = FileWordsReader.readWords(dicHome + "/"
					+ dicNameRelativeDicHome + ".dic", charsetName, maxWordLen);
		} catch (IOException e) {
			throw toRuntimeException(e);
		}
		Set/* <Word> */set = (Set/* <Word> */) dics.get(dicNameRelativeDicHome);
		Word[] words = (Word[]) set.toArray(new Word[set.size()]);
		Arrays.sort(words);
		return words;
	}

	// -------------------------------------

	/**
	 * Load all words if necessary.
	 *
	 * @return the map
	 */
	protected synchronized Map/* <String, Set<String>> */loadAllWordsIfNecessary() {
		if (allWords == null) {
			try {
				log.info("loading dictionaries from " + dicHome);
				allWords = FileWordsReader.readWords(dicHome, charsetName, maxWordLen);
				if (allWords.size() == 0) {
					String message = "Not found any dictionary files, have you set the 'paoding.dic.home' right? ("
							+ this.dicHome + ")";
					log.error(message);
					throw new PaodingAnalysisException(message);
				}
				log.info("loaded success!");
			} catch (IOException e) {
				throw toRuntimeException(e);
			}
		}
		return allWords;
	}

	// ---------------------------------------

	/**
	 * Checks if is skip for vacabulary.
	 *
	 * @param dicNameRelativeDicHome the dic name relative dic home
	 * @return true, if is skip for vacabulary
	 */
	protected final boolean isSkipForVacabulary(String dicNameRelativeDicHome) {
		return dicNameRelativeDicHome.startsWith(skipPrefix)
				|| dicNameRelativeDicHome.indexOf("/" + skipPrefix) != -1;
	}

	/**
	 * Checks if is unit dic file.
	 *
	 * @param dicName the dic name
	 * @return true, if is unit dic file
	 */
	protected boolean isUnitDicFile(String dicName) {
		return dicName.equals(this.unit);
	}

	/**
	 * Checks if is noise charactor dic file.
	 *
	 * @param dicName the dic name
	 * @return true, if is noise charactor dic file
	 */
	protected boolean isNoiseCharactorDicFile(String dicName) {
		return dicName.equals(this.noiseCharactor);
	}

	/**
	 * Checks if is noise word dic file.
	 *
	 * @param dicName the dic name
	 * @return true, if is noise word dic file
	 */
	protected boolean isNoiseWordDicFile(String dicName) {
		return dicName.equals(this.noiseWord);
	}

	/**
	 * Checks if is confucian family name dic file.
	 *
	 * @param dicName the dic name
	 * @return true, if is confucian family name dic file
	 */
	protected boolean isConfucianFamilyNameDicFile(String dicName) {
		return dicName.equals(this.confucianFamilyName);
	}

	/**
	 * Checks if is lantin followed by cjk dic file.
	 *
	 * @param dicName the dic name
	 * @return true, if is lantin followed by cjk dic file
	 */
	protected boolean isLantinFollowedByCjkDicFile(String dicName) {
		return dicName.equals(this.combinatorics);
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
