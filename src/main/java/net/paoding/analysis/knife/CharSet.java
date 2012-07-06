/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer CharSet.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.knife;

/**
 * The Class CharSet.
 *
 * @author l.xue.nong
 */
public class CharSet {
	
	/**
	 * Checks if is arabian number.
	 *
	 * @param ch the ch
	 * @return true, if is arabian number
	 */
	public static boolean isArabianNumber(char ch) {
		return ch >= '0' && ch <= '9';
	}

	/**
	 * Checks if is lanting letter.
	 *
	 * @param ch the ch
	 * @return true, if is lanting letter
	 */
	public static boolean isLantingLetter(char ch) {
		return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
	}

	/**
	 * Checks if is cjk unified ideographs.
	 *
	 * @param ch the ch
	 * @return true, if is cjk unified ideographs
	 */
	public static boolean isCjkUnifiedIdeographs(char ch) {
		return ch >= 0x4E00 && ch < 0xA000;
	}
	
	/**
	 * Checks if is bom.
	 *
	 * @param ch the ch
	 * @return true, if is bom
	 */
	public static boolean isBom(char ch) {
		// ref:http://www.w3.org/International/questions/qa-utf8-bom
		return ch == 0xFEFF || ch == 0xFFFE;
	}
	
	/**
	 * To number.
	 *
	 * @param ch the ch
	 * @return the int
	 */
	public static int toNumber(char ch) {
		switch (ch) {
		case '0':
		case '零':
		case '〇':
			return 0;
		case '1':
		case '一':
		case '壹':
			return 1;
		case '2':
		case '二':
		case '两':
		case '俩':
		case '貳':
			return 2;
		case '3':
		case '三':
		case '叁':
			return 3;
		case '4':
		case '四':
		case '肆':
			return 4;
		case '5':
		case '五':
		case '伍':
			return 5;
		case '6':
		case '六':
		case '陆':
			return 6;
		case '7':
		case '柒':
		case '七':
			return 7;
		case '8':
		case '捌':
		case '八':
			return 8;
		case '9':
		case '九':
		case '玖':
			return 9;
		case '十':
		case '什':
			return 10;
		case '百':
		case '佰':
			return 100;
		case '千':
		case '仟':
			return 1000;
		/*
		 * Fix issue 12: 溢出bug
		 */
		/*
		case '万':
		case '萬':
			return 10000;
		case '亿':
		case '億':
			return 100000000;
		*/
		default:
			return -1;
		}
	}

}
