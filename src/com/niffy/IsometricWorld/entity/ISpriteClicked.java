package com.niffy.IsometricWorld.entity;

import org.andengine.input.touch.TouchEvent;

public interface ISpriteClicked {
	/**
	 * Human Sprite clicked.
	 * @param pHumanEntity
	 */
	public void setSpriteClicked(final IHumanEntity pHumanEntity, final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
	public void setSpriteClicked(final IHumanEntity pHumanEntity, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
}
