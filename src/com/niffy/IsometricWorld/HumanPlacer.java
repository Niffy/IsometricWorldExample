package com.niffy.IsometricWorld;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niffy.IsometricWorld.entity.HumanEntity;
import com.niffy.IsometricWorld.entity.IHumanEntity;
import com.niffy.IsometricWorld.touch.ITouchListener;

public class HumanPlacer implements ITouchListener {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(HumanPlacer.class);

	// ===========================================================
	// Fields
	// ===========================================================

	protected GeneralManager mGeneralManager;
	protected IsometricWorldActivity mParent;
	protected AnimatedSprite mSprite;
	protected TiledTextureRegion mTextureRegion;
	protected int m3DWidth = 0;
	protected int m3DLength = 0;
	protected int m3DHeight = 0;

	protected float m3DX = 0;
	protected float m3DY = 0;
	protected float m3DZ = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HumanPlacer(IsometricWorldActivity pParent, GeneralManager pGeneralManager) {
		this.mGeneralManager = pGeneralManager;
		this.mParent = pParent;
		this.mParent.mTouchManager.registerListener(this);
		this.mTextureRegion = this.mParent.getTextureTiled("man-titled.png", 4, 3);
		this.m3DWidth = 15;
		this.m3DLength = 16;
		this.m3DHeight = 40;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onTouchPassThrough(TouchEvent pSceneTouchEvent, float pX, float pY) {
		if (pSceneTouchEvent.isActionUp()) {
			final float[] pToTiles = this.mParent.getEngine().getScene().convertLocalCoordinatesToSceneCoordinates(pX, pY);
			this.drawHuman(pToTiles);
		}
	}

	public void drawHuman(final float[] pLoc) {
		int[] tileRC = this.mParent.mMapHandler.getTileAt(pLoc);
		if (tileRC == null) {
			return;
		}
		float[] loc = this.mParent.mMapHandler.getTileCentre(tileRC);
		/*
		 * offset[0] = X offset
		 * offset[1] = Y Offset
		 */
		float[] offset = new float[2];
		offset[0] = 0;
		offset[1] = 16f;
		/*
		 * As our offsets are already negative, adding to the tile centre will really subtract instead
		 */
		loc[0] += offset[0];
		loc[1] += offset[1];
		final AnimatedSprite humanSprite = new AnimatedSprite(loc[0], loc[1], this.mTextureRegion,
				this.mParent.getVertexBufferObjectManager());
		humanSprite.setCullingEnabled(true);
		humanSprite.registerForTimeModifier(true);
		humanSprite.recalculate3DSpace(true);
		IHumanEntity humanEntity = new HumanEntity(humanSprite, 4, 3, offset, 1.2f, 200, this.mParent.mMapHandler);
		humanEntity.setOnTouchListenerCallback(this.mParent.mHumanManager);
		humanSprite.setRecalculate3DSpaceXYZ(humanEntity);
		humanSprite.set3DSize(this.m3DWidth, this.m3DLength, this.m3DHeight);
		/*
		 * We could calculate the 3DXY now, but since we're going to move the human about, we'll wait for the recalculation to fix it for us
		 */
		humanEntity.set3DRecalculationPoint(0, 5);
		this.mParent.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				mParent.getEngine().getScene().attachChild(humanSprite);
				mParent.getEngine().getScene().sortChildren();
				mParent.getEngine().getScene().registerTouchArea(humanSprite);
			}
		});
		this.mParent.mHumanManager.mHumans.add(humanEntity);
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
