package com.niffy.IsometricWorld.touch;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.niffy.IsometricWorld.entity.BaseEntity;

public interface ITouchManager {
	/**
	 * Pass the {@link ITouchManager} reference when setting a sprite touch via
	 * {@link Sprite#setOnTouchAreaInterface(org.andengine.entity.shape.ITouchAreaListener)}
	 * 
	 * 
	 * @param pBaseEntity
	 *            {@link BaseEntity} clicked
	 * @param pSceneTouchEvent
	 * @param pTouchAreaLocalX
	 * @param pTouchAreaLocalY
	 */
	public void setSpriteClicked(final BaseEntity pBaseEntity, final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY);
	/**
	 * Pass the main actitvy touch to this manager.
	 * 
	 * @param pSceneTouchEvent
	 * @param pX
	 * @param pY
	 */
	public void onSceneTouchEvent(final TouchEvent pSceneTouchEvent, final float pX, final float pY);

	/**
	 * Register an {@link ITouchListener} to get pass the touch event through;
	 * 
	 * @param pTouchListener
	 *            {@link ITouchListener} to pass the touch event to.
	 */
	public void registerListener(final ITouchListener pTouchListener);

	/**
	 * Unregister an {@link ITouchListener} to stop getting the touch event
	 * passed through
	 * 
	 * @param pTouchListener
	 *            {@link ITouchListener} to stop getting the touch event pass
	 *            through
	 */
	public void unregisterListener(final ITouchListener pTouchListener);
}
