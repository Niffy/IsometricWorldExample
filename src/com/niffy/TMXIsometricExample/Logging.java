package com.niffy.TMXIsometricExample;

import java.util.HashMap;

import android.util.Log;

public class Logging implements IErrorLog {

	protected String TAG = "defaultTag";
	protected boolean enabled = true;
	protected HashMap<Integer, Boolean> levels = new HashMap<Integer, Boolean>();

	@Override
	public void setTag(String pTag) {
		this.TAG = pTag;
	}

	@Override
	public void enable(boolean pEnable) {
		this.enabled = pEnable;
	}
	
	public void enable(final int pLevel, boolean pEnable){
		this.levels.put(pLevel, pEnable);
	}

	@Override
	public void debug(int pLevel, String pTag, String pMsg, Throwable tr) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.d(pTag, msg, tr);
				}
			}else{
				this.enable(pLevel, true);
				this.debug(pLevel, this.TAG, pMsg, tr);
			}
		}
	}

	@Override
	public void debug(int pLevel, String pMsg, Throwable tr) {
		this.debug(pLevel, this.TAG, pMsg, tr);
	}

	@Override
	public void debug(int pLevel, String pTag, String pMsg) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.d(pTag, msg);
				}
			}else{
				this.enable(pLevel, true);
				this.debug(pLevel, this.TAG, pMsg);
			}
		}
	}

	@Override
	public void debug(int pLevel, String pMsg) {
		this.debug(pLevel, this.TAG,pMsg);
	}

	@Override
	public void d(int pLevel, String pTag, String pMsg, Throwable tr) {
		this.debug(pLevel, pTag, pMsg, tr);
	}

	@Override
	public void d(int pLevel, String pMsg, Throwable tr) {
		this.debug(pLevel, this.TAG, pMsg, tr);
	}

	@Override
	public void d(int pLevel, String pTag, String pMsg) {
		this.debug(pLevel, pTag, pMsg);
	}

	@Override
	public void d(int pLevel, String pMsg) {
		this.debug(pLevel, this.TAG, pMsg);
	}

	@Override
	public void verbose(int pLevel, String pTag, String pMsg, Throwable tr) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.v(pTag, msg, tr);
				}
			}else{
				this.enable(pLevel, true);
				this.verbose(pLevel, this.TAG, pMsg, tr);
			}
		}
	}

	@Override
	public void verbose(int pLevel, String pMsg, Throwable tr) {
		this.verbose(pLevel, this.TAG, pMsg, tr);
	}

	@Override
	public void verbose(int pLevel, String pTag, String pMsg) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.v(pTag, msg);
				}
			}else{
				this.enable(pLevel, true);
				this.verbose(pLevel, this.TAG, pMsg);
			}
		}
	}

	@Override
	public void verbose(int pLevel, String pMsg) {
		this.verbose(pLevel, this.TAG, pMsg);
	}

	@Override
	public void v(int pLevel, String pTag, String pMsg, Throwable tr) {
		this.verbose(pLevel, pTag, pMsg, tr);
	}

	@Override
	public void v(int pLevel, String pMsg, Throwable tr) {
		this.verbose(pLevel, this.TAG, pMsg, tr);
	}

	@Override
	public void v(int pLevel, String pTag, String pMsg) {
		this.verbose(pLevel, this.TAG, pMsg);
	}

	@Override
	public void v(int pLevel, String pMsg) {
		this.verbose(pLevel, this.TAG,pMsg);
	}

	@Override
	public void info(int pLevel, String pTag, String pMsg, Throwable tr) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.i(pTag, msg, tr);
				}
			}else{
				this.enable(pLevel, true);
				this.info(pLevel, this.TAG, pMsg, tr);
			}
		}
	}

	@Override
	public void info(int pLevel, String pMsg, Throwable tr) {
		this.info(pLevel, this.TAG, pMsg, tr);
	}

	@Override
	public void info(int pLevel, String pTag, String pMsg) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.i(pTag, msg);
				}
			}else{
				this.enable(pLevel, true);
				this.info(pLevel, this.TAG, pMsg);
			}
		}
	}

	@Override
	public void info(int pLevel, String pMsg) {
		this.info(pLevel, this.TAG, pMsg);
	}

	@Override
	public void i(int pLevel, String pTag, String pMsg, Throwable tr) {
		this.info(pLevel, pTag, pMsg, tr);
	}

	@Override
	public void i(int pLevel, String pMsg, Throwable tr) {
		this.info(pLevel, this.TAG, pMsg, tr);		
	}

	@Override
	public void i(int pLevel, String pTag, String pMsg) {
		this.info(pLevel, pTag, pMsg);
	}

	@Override
	public void i(int pLevel, String pMsg) {
		this.info(pLevel, this.TAG, pMsg);
	}

	@Override
	public void warn(int pLevel, String pTag, String pMsg, Throwable tr) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.w(pTag, msg, tr);
				}
			}else{
				this.enable(pLevel, true);
				this.warn(pLevel, this.TAG, pMsg, tr);
			}
		}
	}

	@Override
	public void warn(int pLevel, String pMsg, Throwable tr) {
		this.warn(pLevel, this.TAG, pMsg, tr);
	}

	@Override
	public void warn(int pLevel, String pTag, String pMsg) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.w(pTag, msg);
				}
			}else{
				this.enable(pLevel, true);
				this.warn(pLevel, this.TAG, pMsg);
			}
		}
	}

	@Override
	public void warn(int pLevel, String pMsg) {
		this.warn(pLevel, this.TAG, pMsg);
	}

	@Override
	public void w(int pLevel, String pTag, String pMsg, Throwable tr) {
		this.warn(pLevel, pTag, pMsg, tr);
	}

	@Override
	public void w(int pLevel, String pMsg, Throwable tr) {
		this.warn(pLevel, this.TAG, pMsg, tr);
	}

	@Override
	public void w(int pLevel, String pTag, String pMsg) {
		this.warn(pLevel, pTag, pMsg);
	}

	@Override
	public void w(int pLevel, String pMsg) {
		this.warn(pLevel, this.TAG, pMsg);
	}

	@Override
	public void error(int pLevel, String pTag, String pMsg, Throwable tr) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.e(pTag, msg, tr);
				}
			}else{
				this.enable(pLevel, true);
				this.error(pLevel, this.TAG, pMsg, tr);
			}
		}
	}

	@Override
	public void error(int pLevel, String pMsg, Throwable tr) {
		this.error(pLevel, this.TAG, pMsg, tr);
	}

	@Override
	public void error(int pLevel, String pTag, String pMsg) {
		if(this.enabled){
			if(this.levels.containsKey(pLevel)){
				if(this.levels.get(pLevel)){
					String msg = String.format("Level: %d - %s ", pLevel, pMsg);
					Log.e(pTag, msg);
				}
			}else{
				this.enable(pLevel, true);
				this.error(pLevel, this.TAG, pMsg);
			}
		}
	}

	@Override
	public void error(int pLevel, String pMsg) {
		this.error(pLevel, this.TAG, pMsg);
	}

	@Override
	public void e(int pLevel, String pTag, String pMsg, Throwable tr) {
		this.error(pLevel, pTag, pMsg, tr);
	}

	@Override
	public void e(int pLevel, String pMsg, Throwable tr) {
		this.error(pLevel, this.TAG, pMsg, tr);
	}

	@Override
	public void e(int pLevel, String pTag, String pMsg) {
		this.error(pLevel, pTag,pMsg);
	}

	@Override
	public void e(int pLevel, String pMsg) {
		this.error(pLevel, this.TAG, pMsg);
	}

}
