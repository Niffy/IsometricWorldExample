package com.niffy.IsometricWorld.touch;

import org.andengine.input.touch.TouchEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niffy.IsometricWorld.entity.BaseEntity;

public class TouchManager implements ITouchManager {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(TouchManager.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected ITouchListener mTouchPassThrough;
	protected BaseEntity mBaseEntityTouched;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TouchManager() {
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces ITouchManager
	// ===========================================================
	@Override
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent, float pX, float pY) {
		if (this.mTouchPassThrough != null) {
			this.mTouchPassThrough.onTouchPassThrough(pSceneTouchEvent, pX, pY);
		}
	}

	@Override
	public void setSpriteClicked(BaseEntity pBaseEntity, TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		this.mBaseEntityTouched = pBaseEntity;
	}

	@Override
	public void registerListener(ITouchListener pTouchListener) {
		this.mTouchPassThrough = pTouchListener;
	}

	@Override
	public void unregisterListener(ITouchListener pTouchListener) {
		if (this.mTouchPassThrough == pTouchListener) {
			this.mTouchPassThrough = null;
		}
	}

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
