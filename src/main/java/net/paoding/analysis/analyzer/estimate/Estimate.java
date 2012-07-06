/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Estimate.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.analyzer.estimate;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;

import net.paoding.analysis.analyzer.PaodingTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

/**
 * The Class Estimate.
 *
 * @author l.xue.nong
 */
public class Estimate {
	
	/** The analyzer. */
	private Analyzer analyzer;
	
	/** The print. */
	private String print;
	
	/** The print gate. */
	private PrintGate printGate;

	/**
	 * Instantiates a new estimate.
	 */
	public Estimate() {
		this.setPrint("50");// 默认只打印前50行分词效果
	}

	/**
	 * Instantiates a new estimate.
	 *
	 * @param analyzer the analyzer
	 */
	public Estimate(Analyzer analyzer) {
		setAnalyzer(analyzer);
		this.setPrint("50");// 默认只打印前50行分词效果
	}

	/**
	 * Sets the analyzer.
	 *
	 * @param analyzer the new analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * Gets the analyzer.
	 *
	 * @return the analyzer
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Sets the prints the.
	 *
	 * @param print the new prints the
	 */
	public void setPrint(String print) {
		if (print == null || print.length() == 0
				|| print.equalsIgnoreCase("null")
				|| print.equalsIgnoreCase("no")) {
			printGate = null;
			this.print = null;
		} else {
			printGate = new LinePrintGate();
			printGate.setPrint(print, 10);
			this.print = print;
		}
	}

	/**
	 * Gets the prints the.
	 *
	 * @return the prints the
	 */
	public String getPrint() {
		return print;
	}

	/**
	 * Test.
	 *
	 * @param input the input
	 */
	public void test(String input) {
		this.test(System.out, input);
	}

	/**
	 * Test.
	 *
	 * @param out the out
	 * @param input the input
	 */
	public void test(PrintStream out, String input) {
		Reader reader = new StringReaderEx(input);
		this.test(out, reader);
	}

	/**
	 * Test.
	 *
	 * @param out the out
	 * @param reader the reader
	 */
	public void test(PrintStream out, Reader reader) {
		try {
			long begin = System.currentTimeMillis();
			
			LinkedList<CToken> list = new LinkedList<CToken>();
			int wordsCount = 0;
			
			//collect token
			TokenStream ts = analyzer.tokenStream("", reader);
			ts.reset();
			TermAttribute termAtt = (TermAttribute) ts
					.addAttribute(TermAttribute.class);
			while (ts.incrementToken()) {
				if (printGate != null && printGate.filter(wordsCount)) {
					list.add(new CToken(termAtt.term(), wordsCount));
				}
				wordsCount++;
			}
			
			long end = System.currentTimeMillis();
			int c = 0;
			if (list.size() > 0) {
				for (CToken ctoken : list) {
					c = ctoken.i;
					if (c % 10 == 0) {
						if (c != 0) {
							out.println();
						}
						out.print((c / 10 + 1) + ":\t");
					}
					out.print(ctoken.t + "/");
				}
			}
			if (wordsCount == 0) {
				System.out.println("\tAll are noise characters or words");
			} else {
				if (c % 10 != 1) {
					System.out.println();
				}
				String inputLength = "<未知>";
				if (reader instanceof StringReaderEx) {
					inputLength = "" + ((StringReaderEx) reader).inputLength;
				} else if (ts instanceof PaodingTokenizer) {
					inputLength = "" + ((PaodingTokenizer) ts).getInputLength();
				}
				System.out.println();
				System.out.println("\t分词器" + analyzer.getClass().getName());
				System.out.println("\t内容长度 " + inputLength + "字符， 分 "
						+ wordsCount + "个词");
				System.out.println("\t分词耗时 " + (end - begin) + "ms ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
	}

	// -------------------------------------------

	/**
	 * The Class CToken.
	 *
	 * @author l.xue.nong
	 */
	static class CToken {
		
		/** The t. */
		String t;
		
		/** The i. */
		int i;

		/**
		 * Instantiates a new c token.
		 *
		 * @param t the t
		 * @param i the i
		 */
		CToken(String t, int i) {
			this.t = t;
			this.i = i;
		}
	}

	/**
	 * The Interface PrintGate.
	 *
	 * @author l.xue.nong
	 */
	static interface PrintGate {
		
		/**
		 * Sets the print.
		 *
		 * @param print the print
		 * @param unitSize the unit size
		 */
		public void setPrint(String print, int unitSize);

		/**
		 * Filter.
		 *
		 * @param count the count
		 * @return true, if successful
		 */
		boolean filter(int count);
	}

	/**
	 * The Class PrintGateToken.
	 *
	 * @author l.xue.nong
	 */
	static class PrintGateToken implements PrintGate {
		
		/** The begin. */
		private int begin;
		
		/** The end. */
		private int end;

		/**
		 * Sets the begin.
		 *
		 * @param begin the new begin
		 */
		public void setBegin(int begin) {
			this.begin = begin;
		}

		/**
		 * Sets the end.
		 *
		 * @param end the new end
		 */
		public void setEnd(int end) {
			this.end = end;
		}

		/* (non-Javadoc)
		 * @see net.paoding.analysis.analyzer.estimate.Estimate.PrintGate#setPrint(java.lang.String, int)
		 */
		public void setPrint(String print, int unitSize) {
			int i = print.indexOf('-');
			if (i > 0) {
				int bv = Integer.parseInt(print.substring(0, i));
				int ev = Integer.parseInt(print.substring(i + 1));
				setBegin(unitSize * (Math.abs(bv) - 1));// 第5行，是从第40开始的
				setEnd(unitSize * Math.abs(ev));// 到第10行，是截止于100(不包含该边界)
			} else {
				setBegin(0);
				int v = Integer.parseInt(print);
				setEnd(unitSize * (Math.abs(v)));
			}
		}

		/* (non-Javadoc)
		 * @see net.paoding.analysis.analyzer.estimate.Estimate.PrintGate#filter(int)
		 */
		public boolean filter(int count) {
			return count >= begin && count < end;
		}
	}

	/**
	 * The Class LinePrintGate.
	 *
	 * @author l.xue.nong
	 */
	static class LinePrintGate implements PrintGate {

		/** The list. */
		private PrintGate[] list;

		/* (non-Javadoc)
		 * @see net.paoding.analysis.analyzer.estimate.Estimate.PrintGate#setPrint(java.lang.String, int)
		 */
		public void setPrint(String print, int unitSize) {
			String[] prints = print.split(",");
			list = new PrintGate[prints.length];
			for (int i = 0; i < prints.length; i++) {
				PrintGateToken pg = new PrintGateToken();
				pg.setPrint(prints[i], unitSize);
				list[i] = pg;
			}
		}

		/* (non-Javadoc)
		 * @see net.paoding.analysis.analyzer.estimate.Estimate.PrintGate#filter(int)
		 */
		public boolean filter(int count) {
			for (int i = 0; i < list.length; i++) {
				if (list[i].filter(count)) {
					return true;
				}
			}
			return false;
		}

	}

	/**
	 * The Class StringReaderEx.
	 *
	 * @author l.xue.nong
	 */
	static class StringReaderEx extends StringReader {
		
		/** The input length. */
		private int inputLength;

		/**
		 * Instantiates a new string reader ex.
		 *
		 * @param s the s
		 */
		public StringReaderEx(String s) {
			super(s);
			inputLength = s.length();
		}
	}

}
