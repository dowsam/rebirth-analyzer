/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Node.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.detection;

/**
 * The Class Node.
 *
 * @author l.xue.nong
 */
public class Node implements Comparable<Node>{

	/** The path. */
	String path;

	/** The is file. */
	boolean isFile;

	/**
	 * Instantiates a new node.
	 */
	public Node() {
	}

	/**
	 * Instantiates a new node.
	 *
	 * @param path the path
	 * @param isFile the is file
	 */
	public Node(String path, boolean isFile) {
		this.path = path;
		this.isFile = isFile;
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Checks if is file.
	 *
	 * @return true, if is file
	 */
	public boolean isFile() {
		return isFile;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return path;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Node other = (Node) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Node o) {
		//path
		if (this.path != null && o.path != null){
			int cmp = this.path.compareTo(o.path);
			if (cmp != 0) return cmp;
		} else {
			if (this.path != null && o.path == null) return 1;
			if (this.path == null && o.path != null) return -1;
		}
		
		//isfile
		if (this.isFile && !o.isFile) return 1;
		if (!this.isFile && o.isFile) return -1;
		return 0;
	}

}
