/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer Detector.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package net.paoding.analysis.dictionary.support.detection;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class Detector.
 *
 * @author l.xue.nong
 */
public class Detector implements Runnable {

	/** The log. */
	private Log log = LogFactory.getLog(this.getClass());

	/** The listener. */
	private DifferenceListener listener;

	/** The home. */
	private File home;

	/** The filter. */
	private FileFilter filter;

	/** The interval. */
	private long interval;

	/** The last snapshot. */
	private Snapshot lastSnapshot;
	
	/** The thread. */
	private Thread thread;

	/** The alive. */
	private boolean alive = true;

	/**
	 * Sets the listener.
	 *
	 * @param listener the new listener
	 */
	public void setListener(DifferenceListener listener) {
		this.listener = listener;
	}

	/**
	 * Instantiates a new detector.
	 */
	public Detector() {
	}

	/**
	 * Sets the interval.
	 *
	 * @param interval the new interval
	 */
	public void setInterval(int interval) {
		this.interval = interval * 1000;
	}

	/**
	 * Sets the home.
	 *
	 * @param home the new home
	 */
	public void setHome(File home) {
		this.home = home;
	}

	/**
	 * Sets the home.
	 *
	 * @param home the new home
	 */
	public void setHome(String home) {
		this.home = new File(home);
	}

	/**
	 * Sets the filter.
	 *
	 * @param filter the new filter
	 */
	public void setFilter(FileFilter filter) {
		this.filter = filter;
	}
	
	/**
	 * Flash.
	 *
	 * @return the snapshot
	 */
	public Snapshot flash(){
		return Snapshot.flash(home, filter);
	}

	/**
	 * Start.
	 *
	 * @param daemon the daemon
	 */
	public void start(boolean daemon) {
		if (lastSnapshot == null) {
			lastSnapshot = flash();
		}
		thread = new Thread(this);
		thread.setDaemon(daemon);
		thread.start();
	}
	
	
	/**
	 * Gets the last snapshot.
	 *
	 * @return the last snapshot
	 */
	public Snapshot getLastSnapshot() {
		return lastSnapshot;
	}
	
	/**
	 * Sets the last snapshot.
	 *
	 * @param last the new last snapshot
	 */
	public void setLastSnapshot(Snapshot last) {
		this.lastSnapshot = last;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (interval <= 0)
			throw new IllegalArgumentException(
					"should set a interval(>0) for the detection.");
		while (alive) {
			sleep();
			forceDetecting();
		}
	}

	/**
	 * Force detecting.
	 */
	public void forceDetecting() {
		Snapshot current = flash();
		Difference diff = current.diff(lastSnapshot);
		if (!diff.isEmpty()) {
			try {
				listener.on(diff);
				log.info("found differen for " + home);
				log.info(diff);
				lastSnapshot = current;
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	/**
	 * Sets the stop.
	 */
	public void setStop() {
		alive = false;
		thread = null;
	}

	/**
	 * Sleep.
	 */
	private void sleep() {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Detector d = new Detector();
		d.setInterval(1);
		d.setHome(new File("dic"));
		d.setFilter(new ExtensionFileFilter(".dic"));
		d.setListener(new DifferenceListener() {
			public void on(Difference diff) {
				System.out.println(diff);
			}

		});
		d.start(false);
	}

}
