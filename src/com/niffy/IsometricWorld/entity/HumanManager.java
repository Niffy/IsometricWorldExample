package com.niffy.IsometricWorld.entity;

import java.util.ArrayList;

import org.andengine.input.touch.TouchEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niffy.IsometricWorld.GeneralManager;
import com.niffy.IsometricWorld.IsometricWorldActivity;
import com.niffy.IsometricWorld.MapHandler;
import com.niffy.IsometricWorld.pathfinding.PathFindingManager;
import com.niffy.IsometricWorld.touch.ITouchListener;

public class HumanManager implements ISpriteClicked, ITouchListener {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(HumanManager.class);

	// ===========================================================
	// Fields
	// ===========================================================
	public ArrayList<IHumanEntity> mHumans;
	protected IHumanEntity mHumanEntity;
	public IsometricWorldActivity mParent;
	public GeneralManager mGeneralManager;
	public MapHandler mMapHandler;
	public PathFindingManager mPathFindingManager;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HumanManager(IsometricWorldActivity pParent, GeneralManager pGeneralManager) {
		this.mParent = pParent;
		this.mGeneralManager = pGeneralManager;
		this.mHumans = new ArrayList<IHumanEntity>();
	}

	public void setMapHandler(MapHandler pMapHandler) {
		this.mMapHandler = pMapHandler;
		this.mPathFindingManager = new PathFindingManager(this.mMapHandler, this.mMapHandler.getTileManager());
	}

	@Override
	public void setSpriteClicked(IHumanEntity pHumanEntity, TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		this.mHumanEntity = pHumanEntity;
		this.mParent.mTouchManager.registerListener(this);
	}

	@Override
	public void setSpriteClicked(IHumanEntity pHumanEntity, float pTouchAreaLocalX, float pTouchAreaLocalY) {

	}

	@Override
	public void onTouchPassThrough(TouchEvent pSceneTouchEvent, float pX, float pY) {
		if (this.mHumanEntity != null) {
			if (pSceneTouchEvent.isActionUp()) {
				this.mParent.mTouchManager.unregisterListener(this);
				final float[] pTiles = this.mParent.getEngine().getScene().convertLocalToSceneCoordinates(pX, pY);
				this.mPathFindingManager.findPath(this.mHumanEntity, pTiles);
			}
		}
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
