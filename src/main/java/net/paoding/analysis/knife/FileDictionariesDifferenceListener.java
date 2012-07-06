/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer FileDictionariesDifferenceListener.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.knife;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.paoding.analysis.dictionary.support.detection.Difference;
import net.paoding.analysis.dictionary.support.detection.DifferenceListener;
import net.paoding.analysis.dictionary.support.detection.Node;

/**
 * The listener interface for receiving fileDictionariesDifference events.
 * The class that is interested in processing a fileDictionariesDifference
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addFileDictionariesDifferenceListener<code> method. When
 * the fileDictionariesDifference event occurs, that object's appropriate
 * method is invoked.
 *
 * @see FileDictionariesDifferenceEvent
 */
public class FileDictionariesDifferenceListener implements DifferenceListener {

	/** The dictionaries. */
	private FileDictionaries dictionaries;

	/** The knife box. */
	private KnifeBox knifeBox;

	/**
	 * Instantiates a new file dictionaries difference listener.
	 */
	public FileDictionariesDifferenceListener() {
	}

	/**
	 * Instantiates a new file dictionaries difference listener.
	 *
	 * @param dictionaries the dictionaries
	 * @param knifeBox the knife box
	 */
	public FileDictionariesDifferenceListener(Dictionaries dictionaries,
			KnifeBox knifeBox) {
		this.dictionaries = (FileDictionaries) dictionaries;
		this.knifeBox = knifeBox;
	}

	/**
	 * Gets the dictionaries.
	 *
	 * @return the dictionaries
	 */
	public Dictionaries getDictionaries() {
		return dictionaries;
	}

	/**
	 * Sets the dictionaries.
	 *
	 * @param dictionaries the new dictionaries
	 */
	public void setDictionaries(Dictionaries dictionaries) {
		this.dictionaries = (FileDictionaries) dictionaries;
	}

	/**
	 * Gets the knife box.
	 *
	 * @return the knife box
	 */
	public KnifeBox getKnifeBox() {
		return knifeBox;
	}

	/**
	 * Sets the knife box.
	 *
	 * @param knifeBox the new knife box
	 */
	public void setKnifeBox(KnifeBox knifeBox) {
		this.knifeBox = knifeBox;
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.dictionary.support.detection.DifferenceListener#on(net.paoding.analysis.dictionary.support.detection.Difference)
	 */
	public synchronized void on(Difference diff) {
		List/* <Node> */all = new LinkedList/* <Node> */();
		all.addAll((List/* <Node> */) diff.getDeleted());
		all.addAll((List/* <Node> */) diff.getModified());
		all.addAll((List/* <Node> */) diff.getNewcome());
		for (Iterator iter = all.iterator(); iter.hasNext();) {
			Node node = (Node) iter.next();
			if (node.isFile()) {
				dictionaries.refreshDicWords(node.getPath());
			}
		}
		Knife[] knives = knifeBox.getKnives();
		for (int i = 0; i < knives.length; i ++) {
			Knife knife = knives[i];
			if (knife instanceof DictionariesWare) {
				((DictionariesWare) knife).setDictionaries(dictionaries);
			}
		}
	}

}
