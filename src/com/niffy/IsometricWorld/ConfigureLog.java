package com.niffy.IsometricWorld;

import java.io.File;

import org.apache.log4j.Level;

import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * <item>10000</item> -- <item>OFF</item> <br>
 * <item>50000</item> -- <item>FATAL</item><br>
 * <item>40000</item> -- <item>ERROR</item> <br>
 * <item>30000</item> -- <item>WARN</item><br>
 * <item>20000</item> -- <item>INFO</item> <br>
 * <item>10000</item> -- <item>DEBUG</item><br>
 * <item>5000</item> -- <item>TRACE</item><br>
 * 
 * @author Paul Robinson
 * @since 16 Mar 2013 20:40:23
 */
public class ConfigureLog {
	// ===========================================================
	// Constants
	// ===========================================================
	private final static LogConfigurator mLogConfigurator = new LogConfigurator();

	// ===========================================================
	// Fields
	// ===========================================================
	// ===========================================================
	// Constructors
	// ===========================================================
	public ConfigureLog() {

	}

	/**
	 * 
	 * @param pLogName
	 * @param pMaxBackupSize
	 * @param pLevel
	 */
	public static void configure(final String pLogName, final int pMaxBackupSize, final int pLevel, final int pFileSize) {
		Level useLevel = Level.toLevel(pLevel, Level.DEBUG);
		String strFileLocation;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			strFileLocation = Environment.getExternalStorageDirectory() + File.separator + pLogName;
		} else {
			strFileLocation = File.separator + pLogName;
		}
		mLogConfigurator.setMaxBackupSize(pMaxBackupSize);
		mLogConfigurator.setRootLevel(useLevel);
		mLogConfigurator.setMaxFileSize(pFileSize);
		mLogConfigurator.setFileName(strFileLocation);
		mLogConfigurator.setUseLogCatAppender(true);
		// mLogConfigurator.setFilePattern("%d - [%p::%c::%C] - %m%n");
		//mLogConfigurator.setFilePattern("%d - [%p::%c] - %m%n");
		mLogConfigurator.setFilePattern("%d - %-5p - [%t] - [%c] - (%F:%L)- %m%n");
		mLogConfigurator.setLogCatPattern("%m%n");
		mLogConfigurator.configure();

	}

	public static LogConfigurator getLogConfigurator() {
		return mLogConfigurator;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
