package com.niffy.IsometricWorld.touch;

import org.andengine.input.touch.TouchEvent;
/**
 * If you want to take control of handling the touch of the user, implement this and call
 * {@link ITouchManager#}
 * @author Paul Robinson
 * @since 10 Nov 2012 16:36:26
 */
public interface ITouchListener {
	/**
	 * The user touched the scene and the touch passed through via this method.
	 * @param pSceneTouchEvent
	 * @param pX
	 * @param pY
	 */
	public void onTouchPassThrough(final TouchEvent pSceneTouchEvent, final float pX, final float pY);
}
