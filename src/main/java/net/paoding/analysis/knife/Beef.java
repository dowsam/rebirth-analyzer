/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Beef.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.knife;

/**
 * The Class Beef.
 *
 * @author l.xue.nong
 */
public class Beef implements CharSequence {

	// -------------------------------------------------

	/** The value. */
	private final char[] value;

	/** The offset. */
	private int offset;

	/** The count. */
	private int count;

	/** The hash. */
	private int hash; // Default to 0

	// -------------------------------------------------

	/**
	 * Instantiates a new beef.
	 *
	 * @param value the value
	 * @param offset the offset
	 * @param count the count
	 */
	public Beef(char[] value, int offset, int count) {
		this.value = value;
		set(offset, count);
	}

	// -------------------------------------------------

	/**
	 * Sets the.
	 *
	 * @param offset the offset
	 * @param count the count
	 */
	public void set(int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }
        if (offset > value.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }
		this.offset = offset;
		this.count = count;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public char[] getValue() {
		return value;
	}


	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Gets the offset.
	 *
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	// -------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.CharSequence#charAt(int)
	 */
	public char charAt(int index) {
		if (index >= 0 && index < count) {
			char src = value[offset + index];
			if (src > 65280 && src < 65375) {
				src = (char) (src - 65248);
				value[offset + index] = src;
			}
			if (src >= 'A' && src <= 'Z') {
				src += 32;
				value[offset + index] = src;
			} else if (src == 12288) {
				src = 32;
				value[offset + index] = 32;
			}
			return src;
		}
		return (char) -1;
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#length()
	 */
	public int length() {
		return count;
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#subSequence(int, int)
	 */
	public CharSequence subSequence(int start, int end) {
		return new String(value, offset + start, end - start);
	}

	// -------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new String(value, offset, count);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int h = hash;
		if (h == 0) {
			int off = offset;
			char val[] = value;
			int len = count;

			for (int i = 0; i < len; i++) {
				h = 31 * h + val[off++];
			}
			hash = h;
		}
		return h;
	}

}
