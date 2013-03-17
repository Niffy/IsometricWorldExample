package com.niffy.IsometricWorld.entity;

import javax.xml.datatype.Duration;

import org.andengine.entity.IIsometricEntity3DSpaceRecalculation;
import org.andengine.entity.sprite.AnimatedSprite;

/**
 * A basic interface to access the properties of a human entity.
 * @author Paul Robinson
 * @since 23 Sep 2012 16:51:37
 */
public interface IHumanEntity extends IIsometricEntity3DSpaceRecalculation {
	/**
	 * Get current X and Y of entity without the offset applied. <br> This should help locate tile
	 * @return {@link Float} array [0] X [1] Y coordinates.
	 */
	public float[] getCurrentCoordinateNoOffset();
	/**
	 * Get current X and Y of entity with the offset applied
	 * @return {@link Float} array [0] X [1] Y coordinates.
	 */
	public float[] getCurrentCoordinateWithOffset();
	/**
	 * Get offset used for {@link AnimatedSprite}
	 * @return {@link Float} [0] X [1] Y
	 */
	public float[] getOffset();
	/**
	 * Get the duration of moving between 2 tile centres. e.g From centre of tile A to centre of tile B. <br>
	 * The duration is in seconds. so <code>1.2</code> equals 1.2 seconds. <code>0.5</code> equals half a second.
	 * @return {@link Duration} of movement between two tiles in seconds.
	 */
	public float getDurationTileToTile();
	/**
	 * Get the animated sprite for the Entity
	 * @return {@link AnimatedSprite}
	 */
	public AnimatedSprite getAnimatedSprite();
	/**
	 * Get the duration required for each frame of the {@link AnimatedSprite}
	 * @return {@link Long} array containing duration of each frame in milliseconds
	 */
	public long[] getAnimatedTileFrameSpeed();
	/**
	 * Set the {@link ISpriteClicked} interface to call when A sprite has been touched.
	 * @param pSpriteClicked {@link ISpriteClicked} to call.
	 */
	public void setOnTouchListenerCallback(final ISpriteClicked pSpriteClicked);
	public void set3DZIndex(final float pZ);
	public float get3DZIndex();
	public void set3DRecalculationPoint(final int pX, final int pY);
	public int[] get3DRecalculationPoint();

}
