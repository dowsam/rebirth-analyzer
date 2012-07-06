/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Dictionary.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package org.wltea.analyzer.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import org.wltea.analyzer.cfg.Configuration;

/**
 * The Class Dictionary.
 *
 * @author l.xue.nong
 */
public class Dictionary {
	/*
	 * 分词器默认字典路径 
	 */
	/** The Constant PATH_DIC_MAIN. */
	public static final String PATH_DIC_MAIN = "/org/wltea/analyzer/dic/main.dic";
	
	/** The Constant PATH_DIC_SURNAME. */
	public static final String PATH_DIC_SURNAME = "/org/wltea/analyzer/dic/surname.dic";
	
	/** The Constant PATH_DIC_QUANTIFIER. */
	public static final String PATH_DIC_QUANTIFIER = "/org/wltea/analyzer/dic/quantifier.dic";
	
	/** The Constant PATH_DIC_SUFFIX. */
	public static final String PATH_DIC_SUFFIX = "/org/wltea/analyzer/dic/suffix.dic";
	
	/** The Constant PATH_DIC_PREP. */
	public static final String PATH_DIC_PREP = "/org/wltea/analyzer/dic/preposition.dic";
	
	/** The Constant PATH_DIC_STOP. */
	public static final String PATH_DIC_STOP = "/org/wltea/analyzer/dic/stopword.dic";
	
	
	/*
	 * 词典单子实例
	 */
	/** The Constant singleton. */
	private static final Dictionary singleton;
	
	/*
	 * 词典初始化
	 */
	static{
		singleton = new Dictionary();
	}
	
	/*
	 * 主词典对象
	 */
	/** The _ main dict. */
	private DictSegment _MainDict;
	/*
	 * 姓氏词典
	 */
	/** The _ surname dict. */
	private DictSegment _SurnameDict;
	/*
	 * 量词词典
	 */
	/** The _ quantifier dict. */
	private DictSegment _QuantifierDict;
	/*
	 * 后缀词典
	 */
	/** The _ suffix dict. */
	private DictSegment _SuffixDict;
	/*
	 * 副词，介词词典
	 */
	/** The _ prep dict. */
	private DictSegment _PrepDict;
	/*
	 * 停止词集合
	 */
	/** The _ stop words. */
	private DictSegment _StopWords;
	
	/**
	 * Instantiates a new dictionary.
	 */
	private Dictionary(){
		//初始化系统词典
		loadMainDict();
		loadSurnameDict();
		loadQuantifierDict();
		loadSuffixDict();
		loadPrepDict();
		loadStopWordDict();
	}

	/**
	 * Load main dict.
	 */
	private void loadMainDict(){
		//建立一个主词典实例
		_MainDict = new DictSegment((char)0);
		//读取主词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_MAIN);
        if(is == null){
        	throw new RuntimeException("Main Dictionary not found!!!");
        }
        
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_MainDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Main Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//加载扩展词典配置
		List<String> extDictFiles  = Configuration.getExtDictionarys();
		if(extDictFiles != null){
			for(String extDictName : extDictFiles){
				//读取扩展词典文件
				is = Dictionary.class.getResourceAsStream(extDictName);
				//如果找不到扩展的字典，则忽略
				if(is == null){
					continue;
				}
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
					String theWord = null;
					do {
						theWord = br.readLine();
						if (theWord != null && !"".equals(theWord.trim())) {
							//加载扩展词典数据到主内存词典中
							//System.out.println(theWord);
							_MainDict.fillSegment(theWord.trim().toCharArray());
						}
					} while (theWord != null);
					
				} catch (IOException ioe) {
					System.err.println("Extension Dictionary loading exception.");
					ioe.printStackTrace();
					
				}finally{
					try {
						if(is != null){
		                    is.close();
		                    is = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}	
	
	/**
	 * Load surname dict.
	 */
	private void loadSurnameDict(){
		//建立一个姓氏词典实例
		_SurnameDict = new DictSegment((char)0);
		//读取姓氏词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_SURNAME);
        if(is == null){
        	throw new RuntimeException("Surname Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_SurnameDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Surname Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Load quantifier dict.
	 */
	private void loadQuantifierDict(){
		//建立一个量词典实例
		_QuantifierDict = new DictSegment((char)0);
		//读取量词词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_QUANTIFIER);
        if(is == null){
        	throw new RuntimeException("Quantifier Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_QuantifierDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Quantifier Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Load suffix dict.
	 */
	private void loadSuffixDict(){
		//建立一个后缀词典实例
		_SuffixDict = new DictSegment((char)0);
		//读取量词词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_SUFFIX);
        if(is == null){
        	throw new RuntimeException("Suffix Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_SuffixDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Suffix Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}			

	/**
	 * Load prep dict.
	 */
	private void loadPrepDict(){
		//建立一个介词\副词词典实例
		_PrepDict = new DictSegment((char)0);
		//读取量词词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_PREP);
        if(is == null){
        	throw new RuntimeException("Preposition Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					//System.out.println(theWord);
					_PrepDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Preposition Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Load stop word dict.
	 */
	private void loadStopWordDict(){
		//建立一个停止词典实例
		_StopWords = new DictSegment((char)0);
		//读取量词词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_STOP);
        if(is == null){
        	throw new RuntimeException("Stopword Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_StopWords.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Stopword Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//加载扩展停止词典
		List<String> extStopWordDictFiles  = Configuration.getExtStopWordDictionarys();
		if(extStopWordDictFiles != null){
			for(String extStopWordDictName : extStopWordDictFiles){
				//读取扩展词典文件
				is = Dictionary.class.getResourceAsStream(extStopWordDictName);
				//如果找不到扩展的字典，则忽略
				if(is == null){
					continue;
				}
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
					String theWord = null;
					do {
						theWord = br.readLine();
						if (theWord != null && !"".equals(theWord.trim())) {
							//System.out.println(theWord);
							//加载扩展停止词典数据到内存中
							_StopWords.fillSegment(theWord.trim().toCharArray());
						}
					} while (theWord != null);
					
				} catch (IOException ioe) {
					System.err.println("Extension Stop word Dictionary loading exception.");
					ioe.printStackTrace();
					
				}finally{
					try {
						if(is != null){
		                    is.close();
		                    is = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}		
		
	}			
	
	/**
	 * Gets the single instance of Dictionary.
	 *
	 * @return single instance of Dictionary
	 */
	public static Dictionary getInstance(){
		return Dictionary.singleton;
	}
	
	/**
	 * Load extend words.
	 *
	 * @param extWords the ext words
	 */
	public static void loadExtendWords(Collection<String> extWords){
		if(extWords != null){
			for(String extWord : extWords){
				if (extWord != null) {
					//加载扩展词条到主内存词典中
					singleton._MainDict.fillSegment(extWord.trim().toCharArray());
				}
			}
		}
	}
	
	/**
	 * Load extend stop words.
	 *
	 * @param extStopWords the ext stop words
	 */
	public static void loadExtendStopWords(Collection<String> extStopWords){
		if(extStopWords != null){
			for(String extStopWord : extStopWords){
				if (extStopWord != null) {
					//加载扩展的停止词条
					singleton._StopWords.fillSegment(extStopWord.trim().toCharArray());
				}
			}
		}
	}
	
	/**
	 * Match in main dict.
	 *
	 * @param charArray the char array
	 * @return the hit
	 */
	public static Hit matchInMainDict(char[] charArray){
		return singleton._MainDict.match(charArray);
	}
	
	/**
	 * Match in main dict.
	 *
	 * @param charArray the char array
	 * @param begin the begin
	 * @param length the length
	 * @return the hit
	 */
	public static Hit matchInMainDict(char[] charArray , int begin, int length){
		return singleton._MainDict.match(charArray, begin, length);
	}
	
	/**
	 * Match with hit.
	 *
	 * @param charArray the char array
	 * @param currentIndex the current index
	 * @param matchedHit the matched hit
	 * @return the hit
	 */
	public static Hit matchWithHit(char[] charArray , int currentIndex , Hit matchedHit){
		DictSegment ds = matchedHit.getMatchedDictSegment();
		return ds.match(charArray, currentIndex, 1 , matchedHit);
	}

	/**
	 * Match in surname dict.
	 *
	 * @param charArray the char array
	 * @param begin the begin
	 * @param length the length
	 * @return the hit
	 */
	public static Hit matchInSurnameDict(char[] charArray , int begin, int length){
		return singleton._SurnameDict.match(charArray, begin, length);
	}		
	
//	/**
//	 * 
//	 * 在姓氏词典中匹配指定位置的char数组
//	 * （对传入的字串进行后缀匹配）
//	 * @param charArray
//	 * @param begin
//	 * @param end
//	 * @return
//	 */
//	public static boolean endsWithSurnameDict(char[] charArray , int begin, int length){
//		Hit hit = null;
//		for(int i = 1 ; i <= length ; i++){
//			hit = singleton._SurnameDict.match(charArray, begin + (length - i) , i);
//			if(hit.isMatch()){
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
 * Match in quantifier dict.
 *
 * @param charArray the char array
 * @param begin the begin
 * @param length the length
 * @return the hit
 */
	public static Hit matchInQuantifierDict(char[] charArray , int begin, int length){
		return singleton._QuantifierDict.match(charArray, begin, length);
	}
	
	/**
	 * Match in suffix dict.
	 *
	 * @param charArray the char array
	 * @param begin the begin
	 * @param length the length
	 * @return the hit
	 */
	public static Hit matchInSuffixDict(char[] charArray , int begin, int length){
		return singleton._SuffixDict.match(charArray, begin, length);
	}
	
//	/**
//	 * 在后缀词典中匹配指定位置的char数组
//	 * （对传入的字串进行前缀匹配）
//	 * @param charArray
//	 * @param begin
//	 * @param end
//	 * @return
//	 */
//	public static boolean startsWithSuffixDict(char[] charArray , int begin, int length){
//		Hit hit = null;
//		for(int i = 1 ; i <= length ; i++){
//			hit = singleton._SuffixDict.match(charArray, begin , i);
//			if(hit.isMatch()){
//				return true;
//			}else if(hit.isUnmatch()){
//				return false;
//			}
//		}
//		return false;
//	}
	
	/**
 * Match in prep dict.
 *
 * @param charArray the char array
 * @param begin the begin
 * @param length the length
 * @return the hit
 */
	public static Hit matchInPrepDict(char[] charArray , int begin, int length){
		return singleton._PrepDict.match(charArray, begin, length);
	}
	
	/**
	 * Checks if is stop word.
	 *
	 * @param charArray the char array
	 * @param begin the begin
	 * @param length the length
	 * @return true, if is stop word
	 */
	public static boolean isStopWord(char[] charArray , int begin, int length){			
		return singleton._StopWords.match(charArray, begin, length).isMatch();
	}	
}
