/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer CombinatoricsKnife.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.knife;

import java.util.HashSet;

import net.paoding.analysis.dictionary.Dictionary;
import net.paoding.analysis.dictionary.Hit;

/**
 * The Class CombinatoricsKnife.
 *
 * @author l.xue.nong
 */
public abstract class CombinatoricsKnife implements Knife, DictionariesWare {

	/** The combinatorics dictionary. */
	protected Dictionary combinatoricsDictionary;

	/** The noise table. */
	protected HashSet/* <String> */noiseTable;

	/**
	 * Instantiates a new combinatorics knife.
	 */
	public CombinatoricsKnife() {
	}

	/**
	 * Instantiates a new combinatorics knife.
	 *
	 * @param noiseWords the noise words
	 */
	public CombinatoricsKnife(String[] noiseWords) {
		setNoiseWords(noiseWords);
	}

	/**
	 * Sets the noise words.
	 *
	 * @param noiseWords the new noise words
	 */
	public void setNoiseWords(String[] noiseWords) {
		noiseTable = new HashSet/* <String> */((int) (noiseWords.length * 1.5));
		for (int i = 0; i < noiseWords.length; i++) {
			noiseTable.add(noiseWords[i]);
		}
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.DictionariesWare#setDictionaries(net.paoding.analysis.knife.Dictionaries)
	 */
	public void setDictionaries(Dictionaries dictionaries) {
		combinatoricsDictionary = dictionaries.getCombinatoricsDictionary();
	}

	/* (non-Javadoc)
	 * @see net.paoding.analysis.knife.Knife#dissect(net.paoding.analysis.knife.Collector, net.paoding.analysis.knife.Beef, int)
	 */
	public int dissect(Collector collector, Beef beef, int offset) {
		// 当point == -1时表示本次分解没有遇到POINT性质的字符；
		// 如果point != -1，该值表示POINT性质字符的开始位置，
		// 这个位置将被返回，下一个Knife将从point位置开始分词
		int point = -1;

		// 记录同质字符分词结束极限位置(不包括limit位置的字符)-也就是assignable方法遇到LIMIT性质的字符的位置
		// 如果point==-1，limit将被返回，下一个Knife将从limit位置开始尝试分词
		int limit = offset + 1;

		// 构建point和limit变量的值:
		// 往前直到遇到LIMIT字符；
		// 其中如果遇到第一次POINT字符，则会将它记录为point
		GO_UNTIL_LIMIT: while (true) {
			switch (assignable(beef, offset, limit)) {
			case LIMIT:
				break GO_UNTIL_LIMIT;
			case POINT:
				if (point == -1) {
					point = limit;
				}
			}
			limit++;
		}
		// 如果最后一个字符也是ASSIGNED以及POINT，
		// 且beef之前已经被分解了一部分(从而能够腾出空间以读入新的字符)，则需要重新读入字符后再分词
		if (limit == beef.length() && offset > 0) {
			return -offset;
		}

		// 检索是否有以该词语位前缀的词典词语
		// 若有，则将它解出
		int dicWordVote = -1;
		if (combinatoricsDictionary != null && beef.charAt(limit) > 0xFF) {
			dicWordVote = tryDicWord(collector, beef, offset, limit);
		}

		// 收集从offset分别到point以及limit的词
		// 注意这里不收集从point到limit的词
		// ->当然可能从point到limit的字符也可能是一个词，不过这不是本次分解的责任
		// ->如果认为它应该是个词，那么只要配置对应的其它Knife实例，该Knife会有机会把它切出来的
		// ->因为我们会返回point作为下一个Knife分词的开始。

		int pointVote = collectPoint(collector, beef, offset, point, limit,
				dicWordVote);
		int limitVote = collectLimit(collector, beef, offset, point, limit,
				dicWordVote);

		return nextOffset(beef, offset, point, limit, pointVote, limitVote,
				dicWordVote);
	}

	/**
	 * Collect point.
	 *
	 * @param collector the collector
	 * @param beef the beef
	 * @param offset the offset
	 * @param point the point
	 * @param limit the limit
	 * @param dicWordVote the dic word vote
	 * @return the int
	 */
	protected int collectPoint(Collector collector, Beef beef, int offset,
			int point, int limit, int dicWordVote) {
		if (point != -1 && dicWordVote == -1) {
			collectIfNotNoise(collector, beef, offset, point);
		}
		return -1;
	}

	/**
	 * Collect limit.
	 *
	 * @param collector the collector
	 * @param beef the beef
	 * @param offset the offset
	 * @param point the point
	 * @param limit the limit
	 * @param dicWordVote the dic word vote
	 * @return the int
	 */
	protected int collectLimit(Collector collector, Beef beef, int offset,
			int point, int limit, int dicWordVote) {
		if (dicWordVote == -1) {
			collectIfNotNoise(collector, beef, offset, limit);
		}
		return -1;
	}

	/**
	 * Try dic word.
	 *
	 * @param collector the collector
	 * @param beef the beef
	 * @param offset the offset
	 * @param limit the limit
	 * @return the int
	 */
	protected int tryDicWord(Collector collector, Beef beef, int offset,
			int limit) {
		int ret = limit;
		for (int end = limit + 1, count = limit - offset + 1; end <= beef
				.length(); end++, count++) {
			Hit hit = combinatoricsDictionary.search(beef, offset, count);
			if (hit.isUndefined()) {
				break;
			} else if (hit.isHit()) {
				collectIfNotNoise(collector, beef, offset, end);
				// 收到词语，将ret设置为该词语的end
				ret = end;
			}
			// gotoNextChar为true表示在词典中存在以当前词为开头的词，
			boolean gotoNextChar = hit.isUnclosed() && end < beef.length()
					&& beef.charAt(end) >= hit.getNext().charAt(count);
			if (!gotoNextChar) {
				break;
			}
		}
		return ret <= limit ? -1 : ret;
		// TODO:
		// 存在的局限:
		// 刚好词语分隔在两次beef之中，比如"U"刚好是此次beef的最后字符，而"盘"是下一次beef的第一个字符
		// 这种情况现在CombinatoricsKnife还没机制办法识别将之处理为一个词语
	}

	/**
	 * Collect if not noise.
	 *
	 * @param collector the collector
	 * @param beef the beef
	 * @param offset the offset
	 * @param end the end
	 */
	protected void collectIfNotNoise(Collector collector, Beef beef,
			int offset, int end) {
		// 将offset和end之间的词(不包含end位置)创建出来给word
		// 如果该词语为噪音词，则重新丢弃之(设置为null)，
		String word = beef.subSequence(offset, end).toString();
		if (noiseTable != null && noiseTable.contains(word)) {
			word = null;
		}

		// 否则发送消息给collect方法，表示Knife新鲜出炉了一个内容为word的候选词语
		// 即：最终决定是否要把这个词语通知给collector的是collect方法
		if (word != null) {
			doCollect(collector, word, beef, offset, end);
		}
	}

	/**
	 * Collect.
	 *
	 * @param collector the collector
	 * @param beef the beef
	 * @param offset the offset
	 * @param end the end
	 */
	protected void collect(Collector collector, Beef beef, int offset, int end) {
		String word = beef.subSequence(offset, end).toString();
		doCollect(collector, word, beef, offset, end);
	}

	/**
	 * Do collect.
	 *
	 * @param collector the collector
	 * @param word the word
	 * @param beef the beef
	 * @param offset the offset
	 * @param end the end
	 */
	protected void doCollect(Collector collector, String word, Beef beef,
			int offset, int end) {
		collector.collect(word, offset, end);
	}

	/**
	 * Next offset.
	 *
	 * @param beef the beef
	 * @param offset the offset
	 * @param point the point
	 * @param limit the limit
	 * @param pointVote the point vote
	 * @param limitVote the limit vote
	 * @param dicWordVote the dic word vote
	 * @return the int
	 */
	protected int nextOffset(Beef beef, int offset, int point, int limit,
			int pointVote, int limitVote, int dicWordVote) {
		int max = pointVote > limitVote ? pointVote : limitVote;
		max = max > dicWordVote ? max : dicWordVote;
		if (max == -1) {
			return point != -1 ? point : limit;
		} else if (max > limit) {
			return max;
		} else {
			return limit;
		}
	}
}
