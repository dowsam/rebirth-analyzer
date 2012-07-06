/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer FileWordsReader.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.filewords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import net.paoding.analysis.knife.CharSet;

/**
 * The Class FileWordsReader.
 *
 * @author l.xue.nong
 */
public class FileWordsReader {

	/**
	 * Read words.
	 *
	 * @param fileOrDirectory the file or directory
	 * @param charsetName the charset name
	 * @param maxWordLen the max word len
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map/*<String, Set<Word>>*/ readWords(
			String fileOrDirectory, String charsetName, int maxWordLen) throws IOException {
		SimpleReadListener l = new SimpleReadListener();
		readWords(fileOrDirectory, l, charsetName, maxWordLen);
		return l.getResult();
	}
	
	/**
	 * Read words.
	 *
	 * @param fileOrDirectory the file or directory
	 * @param charsetName the charset name
	 * @param maxWordLen the max word len
	 * @param collectionClass the collection class
	 * @param ext the ext
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map/*<String, Collection<Word>>*/ readWords(
			String fileOrDirectory, String charsetName, int maxWordLen, Class collectionClass, String ext) throws IOException {
		SimpleReadListener2 l = new SimpleReadListener2(collectionClass, ext);
		readWords(fileOrDirectory, l, charsetName, maxWordLen);
		return l.getResult();
	}

	/**
	 * Read words.
	 *
	 * @param fileOrDirectory the file or directory
	 * @param l the l
	 * @param charsetName the charset name
	 * @param maxWordLen the max word len
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void readWords(String fileOrDirectory, ReadListener l, String charsetName, int maxWordLen)
			throws IOException {
		File file;
		if (fileOrDirectory.startsWith("classpath:")) {
			String name = fileOrDirectory.substring("classpath:".length());
			URL url = FileWordsReader.class.getClassLoader().getResource(name);
			if (url == null) {
				throw new FileNotFoundException("file \"" + name + "\" not found in classpath!");
			}
			file = new File(getUrlPath(url));
		}
		else {
			file = new File(fileOrDirectory);
			if (!file.exists()) {
				throw new FileNotFoundException("file \"" + fileOrDirectory + "\" not found!");
			}
		}
		ArrayList/*<File>*/ dirs = new ArrayList/*<File>*/();
		LinkedList/*<File>*/ dics = new LinkedList/*<File>*/();
		String dir;
		if (file.isDirectory()) {
			dirs.add(file);
			dir = file.getAbsolutePath();
		} else {
			dics.add(file);
			dir = file.getParentFile().getAbsolutePath();
		}
		int index = 0;
		while (index < dirs.size()) {
			File cur = (File) dirs.get(index++);
			File[] files = cur.listFiles();
			for (int i = 0; i < files.length; i ++) {
				File f = files[i];
				if (f.isDirectory()) {
					dirs.add(f);
				} else {
					dics.add(f);
				}
			}
		}
		for (Iterator iter = dics.iterator(); iter.hasNext();) {
			File f = (File) iter.next();
			String name = f.getAbsolutePath().substring(
						dir.length() + 1);
			name = name.replace('\\', '/');
			if (!l.onFileBegin(name)) {
				continue;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), charsetName));
			String word;
			boolean firstInDic = true;
			while ((word = in.readLine()) != null) {
				if (firstInDic) {
					firstInDic = false;
					// ref:http://www.w3.org/International/questions/qa-utf8-bom
					// ZERO WIDTH NO-BREAK SPACE
					// notepad将文件保存为unitcode或utf-8时会在文件开头保存bom字符串
					// notepad根据是否有bom来识别该文件是否是utf-8编码存储的。
					// 庖丁字典需要将这个字符从词典中去掉
					if (word.length() > 0 && CharSet.isBom(word.charAt(0))) {
						word = word.substring(1);
					}
				}
				
				// maximum word length limitation
				if (maxWordLen <= 0 || word.length() <= maxWordLen) l.onWord(word);
			}
			l.onFileEnd(name);
			in.close();
		}
	}
	
	/**
	 * Gets the url path.
	 *
	 * @param url the url
	 * @return the url path
	 */
	private static String getUrlPath(URL url){
		if (url == null) return null;
		String urlPath = null;
		try {
			urlPath = url.toURI().getPath();
		} catch (URISyntaxException e) {
		}			
		if (urlPath == null){
			urlPath = url.getFile();
		}
		return urlPath;
	}
	
}
