package com.niffy.TMXIsometricExample;

import android.R.bool;
import android.R.integer;

public interface IErrorLog {

	public void setTag(String pTag);
	/**
	 * Enable or disable logging on all levels
	 * @param pEnable {@link bool} <code>TRUE</code> or <code>FALSE</code> 
	 */
	public void enable(boolean pEnable);
	/**
	 * Enable or disable logging for a certain level
	 * @param pLevel {@link integer} Level to enable or disable
	 * @param pEnable {@link bool} <code>TRUE</code> or <code>FALSE</code> 
	 */
	public void enable(final int pLevel, boolean pEnable);
	
	public void verbose(int pLevel, String pTag, String pMsg, Throwable tr);
	public void verbose(int pLevel, String pMsg, Throwable tr);
	public void verbose(int pLevel, String pTag, String pMsg);
	public void verbose(int pLevel, String pMsg);
	public void v(int pLevel, String pTag, String pMsg, Throwable tr);
	public void v(int pLevel, String pMsg, Throwable tr);
	public void v(int pLevel, String pTag, String pMsg);
	public void v(int pLevel, String pMsg);
	
	public void debug(int pLevel, String pTag, String pMsg, Throwable tr);
	public void debug(int pLevel, String pMsg, Throwable tr);
	public void debug(int pLevel, String pTag, String pMsg);
	public void debug(int pLevel, String pMsg);
	public void d(int pLevel, String pTag, String pMsg, Throwable tr);
	public void d(int pLevel, String pMsg, Throwable tr);
	public void d(int pLevel, String pTag, String pMsg);
	public void d(int pLevel, String pMsg);
	
	public void info(int pLevel, String pTag, String pMsg, Throwable tr);
	public void info(int pLevel, String pMsg, Throwable tr);
	public void info(int pLevel, String pTag, String pMsg);
	public void info(int pLevel, String pMsg);
	public void i(int pLevel, String pTag, String pMsg, Throwable tr);
	public void i(int pLevel, String pMsg, Throwable tr);
	public void i(int pLevel, String pTag, String pMsg);
	public void i(int pLevel, String pMsg);
	
	public void warn(int pLevel, String pTag, String pMsg, Throwable tr);
	public void warn(int pLevel, String pMsg, Throwable tr);
	public void warn(int pLevel, String pTag, String pMsg);
	public void warn(int pLevel, String pMsg);
	public void w(int pLevel, String pTag, String pMsg, Throwable tr);
	public void w(int pLevel, String pMsg, Throwable tr);
	public void w(int pLevel, String pTag, String pMsg);
	public void w(int pLevel, String pMsg);
	
	public void error(int pLevel, String pTag, String pMsg, Throwable tr);
	public void error(int pLevel, String pMsg, Throwable tr);
	public void error(int pLevel, String pTag, String pMsg);
	public void error(int pLevel, String pMsg);
	public void e(int pLevel, String pTag, String pMsg, Throwable tr);
	public void e(int pLevel, String pMsg, Throwable tr);
	public void e(int pLevel, String pTag, String pMsg);
	public void e(int pLevel, String pMsg);

}
