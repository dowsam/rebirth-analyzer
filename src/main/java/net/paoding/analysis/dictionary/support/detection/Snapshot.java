/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Snapshot.java 2012-7-6 10:23:21 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.detection;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * The Class Snapshot.
 *
 * @author l.xue.nong
 */
public class Snapshot {

	// 此次快照版本，使用时间表示
	/** The version. */
	private long version;

	// 根地址，绝对地址，使用/作为目录分隔符
	/** The root. */
	private String root;

	// String为相对根的地址，使用/作为目录分隔符
	/** The nodes map. */
	private Map/*<String, InnerNode>*/ nodesMap = new HashMap/*<String, InnerNode>*/();

	//
	/** The nodes. */
	private InnerNode[] nodes;
	
	//checksum of this snapshot
	/** The checksum. */
	private String checksum;

	/**
	 * Instantiates a new snapshot.
	 */
	private Snapshot() {
	}

	/**
	 * Flash.
	 *
	 * @param root the root
	 * @param filter the filter
	 * @return the snapshot
	 */
	public static Snapshot flash(String root, FileFilter filter) {
		return flash(new File(root), filter);
	}
	
	/**
	 * Flash.
	 *
	 * @param rootFile the root file
	 * @param filter the filter
	 * @return the snapshot
	 */
	public static Snapshot flash(File rootFile, FileFilter filter) {
		Snapshot snapshot = new Snapshot();
		snapshot.implFlash(rootFile, filter);
		return snapshot;
	}
	
	/**
	 * Impl flash.
	 *
	 * @param rootFile the root file
	 * @param filter the filter
	 */
	private void implFlash(File rootFile, FileFilter filter) {
		version = System.currentTimeMillis();
		root = rootFile.getAbsolutePath().replace('\\', '/');
		if (!rootFile.exists()) {
			// do nothing, maybe the file has been deleted
			nodes = new InnerNode[0];
		} else {
			InnerNode rootNode = new InnerNode();
			rootNode.path = root;
			rootNode.isFile = rootFile.isFile();
			rootNode.lastModified = rootFile.lastModified();
			nodesMap.put(root, rootNode);
			if (rootFile.isDirectory()) {
				LinkedList/*<File>*/ files = getPosterity(rootFile, filter);
				nodes = new InnerNode[files.size()];
				Iterator/*<File>*/ iter = files.iterator();
				for (int i = 0; i < nodes.length; i++) {
					File f = (File) iter.next();
					String path = f.getAbsolutePath().substring(
							this.root.length() + 1);
					path = path.replace('\\', '/');
					InnerNode node = new InnerNode();
					node.path = path;
					node.isFile = f.isFile();
					node.lastModified = f.lastModified();
					int index = path.lastIndexOf('/');
					node.parent = index == -1 ? root : path.substring(0, index);
					nodes[i] = node;
					nodesMap.put(path, node);
				}
			}
		}
		
		//sort node for checksum
		Arrays.sort(nodes);
		checksum = null;
	}
	
	/**
	 * Builds the check sum.
	 */
	private void buildCheckSum() {
		short checksum = -631;
		short multiplier = 1;
		String ENCODING = "UTF-8";
		
		StringBuilder value = new StringBuilder();
		for(int i = 0; i < nodes.length; i++){
			value.append(nodes[i].path);
			value.append(nodes[i].isFile);
			value.append(nodes[i].parent);
			value.append(nodes[i].lastModified);
		}

		try {
			byte[] data = value.toString().getBytes(ENCODING);
			for (int b = 0; b < data.length; ++b)
				checksum += data[b] * multiplier++;
		} catch (java.io.UnsupportedEncodingException ex) {

		}

		this.checksum = String.valueOf(checksum);
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * Gets the root.
	 *
	 * @return the root
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * Sets the root.
	 *
	 * @param root the new root
	 */
	public void setRoot(String root) {
		this.root = root;
	}

	//get checksum in lazy mode
	/**
	 * Gets the check sum.
	 *
	 * @return the check sum
	 */
	public String getCheckSum() {
		if (checksum == null) buildCheckSum();
		return checksum;
	}

	/**
	 * Diff.
	 *
	 * @param that the that
	 * @return the difference
	 */
	public Difference diff(Snapshot that) {
		Snapshot older = that;
		Snapshot younger = this;
		if (that.version > this.version) {
			older = this;
			younger = that;
		}
		Difference diff = new Difference();
		if (!younger.root.equals(older.root)) {
			throw new IllegalArgumentException("the snaps should be same root");
		}
		for (int i = 0; i < older.nodes.length; i ++) {
			InnerNode olderNode = older.nodes[i];
			InnerNode yongerNode = (InnerNode) younger.nodesMap.get((String) olderNode.path);
			if (yongerNode == null) {
				diff.getDeleted().add(olderNode);
			} else if (yongerNode.lastModified != olderNode.lastModified) {
				diff.getModified().add(olderNode);
			}
		}

		for (int i = 0; i < younger.nodes.length; i ++) {
			InnerNode yongerNode = younger.nodes[i];
			InnerNode olderNode = (InnerNode) older.nodesMap.get((String) yongerNode.path);
			if (olderNode == null) {
				diff.getNewcome().add(yongerNode);
			}
		}
		diff.setOlder(older);
		diff.setYounger(younger);
		return diff;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws InterruptedException {
		File f = new File("dic");
		Snapshot snapshot1 = Snapshot.flash(f, null);
		System.out.println("----");
		Thread.sleep(3000);
		System.out.println("----");
		Thread.sleep(3000);
		System.out.println("----");
		Snapshot snapshot2 = Snapshot.flash(f, null);
		Difference diff = snapshot2.diff(snapshot1);
		String deleted = ArraysToString(diff.getDeleted().toArray(
				new Node[] {}));
		System.out.println("deleted: " + deleted);
		String modified = ArraysToString(diff.getModified().toArray(
				new Node[] {}));
		System.out.println("modified: " + modified);
		String newcome = ArraysToString(diff.getNewcome().toArray(
				new Node[] {}));
		System.out.println("newcome: " + newcome);
	}
	

	// 低于JDK1.5无Arrays.toString()方法，故有以下方法
	/**
	 * Arrays to string.
	 *
	 * @param a the a
	 * @return the string
	 */
	private static String ArraysToString(Object[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuffer b = new StringBuffer();
		b.append('[');
		for (int i = 0;; i++) {
			b.append(String.valueOf(a[i]));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}

	// --------------------------------------------

	/**
	 * Gets the posterity.
	 *
	 * @param root the root
	 * @param filter the filter
	 * @return the posterity
	 */
	private LinkedList/*<File>*/ getPosterity(File root, FileFilter filter) {
		ArrayList/*<File>*/ dirs = new ArrayList/*<File>*/();
		LinkedList/*<File>*/ files = new LinkedList/*<File>*/();
		dirs.add(root);
		int index = 0;
		while (index < dirs.size()) {
			File cur = (File) dirs.get(index++);
			File[] children = cur.listFiles();
			for (int i = 0; i < children.length; i ++) {
				File f = children[i];
				if (filter == null || filter.accept(f)) {
					if (f.isDirectory()) {
						dirs.add(f);
					} else {
						files.add(f);
					}
				}
			}
		}
		return files;
	}

	/**
	 * The Class InnerNode.
	 *
	 * @author l.xue.nong
	 */
	class InnerNode extends Node {
		
		/** The parent. */
		String parent;
		
		/** The last modified. */
		long lastModified;
		
		/* (non-Javadoc)
		 * @see net.paoding.analysis.dictionary.support.detection.Node#compareTo(net.paoding.analysis.dictionary.support.detection.Node)
		 */
		@Override
		public int compareTo(Node o) {
			// super compare
			int result = super.compareTo(o);
			if (result != 0)
				return result;

			if (o instanceof InnerNode) {
				InnerNode node = (InnerNode) o;
	
				// parent
				if (this.parent != null && node.parent != null) {
					int cmp = this.parent.compareTo(node.parent);
					if (cmp != 0)
						return cmp;
				} else {
					if (this.parent != null && node.parent == null)
						return 1;
					if (this.parent == null && node.parent != null)
						return -1;
				}
	
				// lastModified
				if (this.lastModified > node.lastModified)
					return 1;
				if (this.lastModified < node.lastModified)
					return -1;
			}
			return 0;
		}
	}
}
