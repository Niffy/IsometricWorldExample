package com.niffy.IsometricWorld.pathfinding;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.util.algorithm.path.astar.tile.mod.AStarPathTileModifierSimple.IAStarPathTileModifierListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niffy.IsometricWorld.entity.IHumanEntity;

public class PathTileModListener implements IAStarPathTileModifierListener {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(PathTileModListener.class);

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

	public PathTileModListener(final IHumanEntity pEntity) {
		this.mEntity = pEntity;
		this.mSprite = this.mEntity.getAnimatedSprite();
		this.mSpeed = this.mEntity.getAnimatedTileFrameSpeed();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public void onPathWaypointStarted(EntityModifier pPathModifier, IEntity pEntity, int pWaypointIndex) {
	}

	@Override
	public void onPathWaypointFinished(EntityModifier pPathModifier, IEntity pEntity, int pWaypointIndex) {

	}

	@Override
	public void onPathStarted(EntityModifier pPathModifier, IEntity pEntity) {
	}

	@Override
	public void onPathFinished(EntityModifier pPathModifier, IEntity pEntity) {
		if (this.mSprite != null) {
			this.mSprite.stopAnimation(this.mLastTileDirection);
		}

	}

	@Override
	public void onNextMoveUpRight(EntityModifier aStarPathModifier, IEntity pEntity, int pIndex) {
		this.mSprite.animate(new long[] { this.mSpeed[7], this.mSpeed[8] }, 7, 8, true);
		this.mLastTileDirection = 6;
	}

	@Override
	public void onNextMoveUpLeft(EntityModifier aStarPathModifier, IEntity pEntity, int pIndex) {
		this.mSprite.animate(new long[] { this.mSpeed[10], this.mSpeed[11] }, 10, 11, true);
		this.mLastTileDirection = 9;
	}

	@Override
	public void onNextMoveUp(EntityModifier aStarPathModifier, IEntity pEntity, int pIndex) {
	}

	@Override
	public void onNextMoveRight(EntityModifier aStarPathModifier, IEntity pEntity, int pIndex) {
	}

	@Override
	public void onNextMoveLeft(EntityModifier aStarPathModifier, IEntity pEntity, int pIndex) {
	}

	@Override
	public void onNextMoveDownRight(EntityModifier aStarPathModifier, IEntity pEntity, int pIndex) {
		this.mSprite.animate(new long[] { this.mSpeed[0], this.mSpeed[1] }, 0, 1, true);
		this.mLastTileDirection = 2;
	}

	@Override
	public void onNextMoveDownLeft(EntityModifier aStarPathModifier, IEntity pEntity, int pIndex) {
		this.mSprite.animate(new long[] { this.mSpeed[4], this.mSpeed[5] }, 4, 5, true);
		this.mLastTileDirection = 3;
	}

	@Override
	public void onNextMoveDown(EntityModifier aStarPathModifier, IEntity pEntity, int pIndex) {
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
