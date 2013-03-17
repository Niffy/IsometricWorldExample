package com.niffy.IsometricWorld;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niffy.IsometricWorld.entity.CubeTemplate;
import com.niffy.IsometricWorld.touch.ITouchListener;

public class ObjectPlacer implements ITouchListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(ObjectPlacer.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected GeneralManager mGeneralManager;
	protected IsometricWorldActivity mParent;
	protected CubeTemplate mTemplate;
	protected Sprite mSprite;
	protected float m3DX = 0;
	protected float m3DY = 0;
	protected float m3DZ = 0;
	protected int mBlockRowOrigin = 0;
	protected int mBlockColOrigin = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ObjectPlacer(IsometricWorldActivity pParent, GeneralManager pGeneralManager, CubeTemplate pTemplate) {
		this.mGeneralManager = pGeneralManager;
		this.mParent = pParent;
		this.mTemplate = pTemplate;
		this.mParent.mTouchManager.registerListener(this);

		this.mSprite = new Sprite(0, 0, mParent.mTextures.get(pTemplate.getmFileName()),
				this.mParent.getVertexBufferObjectManager());
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onTouchPassThrough(TouchEvent pSceneTouchEvent, float pX, float pY) {
		if (pSceneTouchEvent.isActionUp()) {
			final int[] pLoc = this.mParent.mMapHandler.getTileAt(pX, pY);
			if (pLoc != null) {
				log.debug("Tile not null: R:{} C:{}", pLoc[0], pLoc[1]);
				if (this.mSprite.hasParent()) {
					this.mSprite.detachSelf();
				}

				boolean free = this.isFree(pLoc[0], pLoc[1], this.mTemplate.getTileColsBlocked(),
						this.mTemplate.getTileRowsBlocked());
				if (!free) {
					log.debug("Is not free, tiles blocked");
				} else {
					log.debug("Using R: {} C: {}", pLoc[0], pLoc[1]);
					this.mBlockRowOrigin = pLoc[0];
					this.mBlockColOrigin = pLoc[1];

					final float[] pTileCen = this.mParent.mMapHandler.getTileCentre(pLoc);
					final float pDrawX = pTileCen[0] - this.mTemplate.getmXOffset();
					final float pDrawY = pTileCen[1] - this.mTemplate.getmYOffset();

					this.mSprite.setPosition(pDrawX, pDrawY);
					this.mSprite.set3DSize(this.mTemplate.getM3DWidth(), this.mTemplate.getM3DLength(),
							this.mTemplate.getM3DHeight());
					this.m3DX = this.mParent.mMapHandler.getTMXTiledMap().getTileHeight() * pLoc[1];
					this.m3DY = this.mParent.mMapHandler.getTMXTiledMap().getTileHeight() * pLoc[0];
					this.m3DZ = 0;
					this.mSprite.set3DPosition(this.m3DX, this.m3DY, this.m3DZ);
					this.mParent.getEngine().getScene().attachChild(this.mSprite);
					this.mParent.getEngine().getScene().sortChildren();
				}
			} else {
				log.debug("tile found is null");
			}
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isFree(final int pRow, final int pCol, final int pWidth, final int pLength) {
		boolean isfree = false;
		for (int i = pRow; i < pRow + pLength; i++) {
			for (int j = pCol; j < pCol + pWidth; j++) {
				boolean pBool = this.mParent.mMapHandler.getTileManager().isBlocked(i, j);
				if (pBool) {
					log.debug("isBlocked: R: {} C: {}", i, j);
					isfree = false;
					break;
				} else {
					log.debug("isFree: R: {} C: {}", i, j);
					isfree = true;
				}
			}
		}
		return isfree;
	}

	public void addBlock(final int pRow, final int pCol, final int pWidth, final int pLength) {
		for (int i = pRow; i < pRow + pLength; i++) {
			for (int j = pCol; j < pCol + pWidth; j++) {
				log.debug("blocking: R: {} C: {}", i, j);
				this.mParent.mMapHandler.getTileManager().addBlock(i, j);
			}
		}
	}

	public void cancel() {
		this.mParent.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				mParent.getEngine().getScene().detachChild(mSprite);
			}
		});
		//this.mParent.getEngine().getScene().detachChild(this.mSprite);
		this.mParent.mTouchManager.unregisterListener(this);
	}

	public void done() {
		this.addBlock(this.mBlockRowOrigin, this.mBlockColOrigin, this.mTemplate.getTileColsBlocked(),
				this.mTemplate.getTileRowsBlocked());
		this.mParent.mTouchManager.unregisterListener(this);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
