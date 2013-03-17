package com.niffy.IsometricWorld.pathfinding;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.util.algorithm.path.astar.AStarPathModifier;
import org.andengine.util.algorithm.path.astar.AStarPathModifier.IAStarPathModifierListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niffy.IsometricWorld.entity.IHumanEntity;

public class PathModListener implements IAStarPathModifierListener {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(PathModListener.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected IHumanEntity mEntity;
	protected AnimatedSprite mSprite;
	protected long[] mSpeed;
	protected int mLastTileDirection = 0;
	// ===========================================================
	// Constructors
	// ===========================================================

	public PathModListener(final IHumanEntity pEntity) {
		this.mEntity = pEntity;
		this.mSprite = this.mEntity.getAnimatedSprite();
		this.mSpeed = this.mEntity.getAnimatedTileFrameSpeed();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onPathWaypointStarted(AStarPathModifier pPathModifier, IEntity pEntity, int pWaypointIndex) {
	}

	@Override
	public void onPathWaypointFinished(AStarPathModifier pPathModifier, IEntity pEntity, int pWaypointIndex) {
	}

	@Override
	public void onPathStarted(AStarPathModifier pPathModifier, IEntity pEntity) {
	}

	@Override
	public void onPathFinished(AStarPathModifier pPathModifier, IEntity pEntity) {
		this.mSprite.stopAnimation(this.mLastTileDirection);
	}

	@Override
	public void onNextMoveUp(AStarPathModifier aStarPathModifier, IEntity pEntity, int pIndex) {
		this.mSprite.animate(new long[] { this.mSpeed[7], this.mSpeed[8] }, 7, 8, true);
		this.mLastTileDirection = 6;
	}

	@Override
	public void onNextMoveRight(AStarPathModifier aStarPathModifier, IEntity pEntity, int pIndex) {
		this.mSprite.animate(new long[] { this.mSpeed[0], this.mSpeed[1] }, 0, 1, true);
		this.mLastTileDirection = 2;
	}

	@Override
	public void onNextMoveLeft(AStarPathModifier aStarPathModifier, IEntity pEntity, int pIndex) {
		this.mSprite.animate(new long[] { this.mSpeed[10], this.mSpeed[11] }, 10, 11, true);
		this.mLastTileDirection = 9;
	}

	@Override
	public void onNextMoveDown(AStarPathModifier aStarPathModifier, IEntity pEntity, int pIndex) {
		this.mSprite.animate(new long[] { this.mSpeed[4], this.mSpeed[5] }, 4, 5, true);
		this.mLastTileDirection = 3;
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
