package com.niffy.IsometricWorld.entity;

import org.andengine.entity.IEntity;
import org.andengine.entity.IIsometricEntity3DSpaceRecalculation;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.shape.ITouchAreaListener;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niffy.IsometricWorld.MapHandler;

public class HumanEntity implements IHumanEntity, ITouchAreaListener, IIsometricEntity3DSpaceRecalculation {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(HumanEntity.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected AnimatedSprite mSprite;
	protected int mRows = 0;
	protected int mColumns = 0;
	protected float[] mOffset = new float[] { 0, 0 };
	protected float mDurationTileToTile = 0; // This should be 1.2f
	protected long[] mAnimatedTileSpeed;
	protected ISpriteClicked mSpriteClicked;
	protected boolean mExecutingPath = false;
	protected MapHandler mMapManager;
	/**
	 * [0] X [1] Y
	 */
	protected int[] m3DRecalculatePoint = new int[] { 0, 0 };
	protected float m3DZ = 0;
	protected Line[] mLine = new Line[2];
	protected Line[] m3DLine = new Line[2];

	// ===========================================================
	// Constructors
	// ===========================================================
	/**
	 * 
	 * @param pAnimatedSprite
	 *            {@link AnimatedSprite} of human.
	 * @param pRows
	 *            {@link Integer} Amount of rows in sprite sheet for
	 *            {@link AnimatedSprite}
	 * @param pColumns
	 *            {@link Integer} Amount of columns in sprite sheet for
	 *            {@link AnimatedSprite}
	 * @param pOffset
	 *            {@link Float} of offset for sprite, can be 0,0. [0] X [1] Y
	 * @param pDurationTileToTile
	 *            {@link Float} Duration of going from the centre of one tile to
	 *            the next.
	 * @param mAnimatedTileSpeed
	 *            {@link Long} Duration of each frame in animated sprite.
	 */
	public HumanEntity(final AnimatedSprite pAnimatedSprite, final int pRows, final int pColumns,
			final float[] pOffset, final float pDurationTileToTile, final long pAnimatedTileSpeed,
			final MapHandler pMapManager) {
		this.mSprite = pAnimatedSprite;
		this.mRows = pRows;
		this.mColumns = pColumns;
		this.mOffset = pOffset;
		this.mDurationTileToTile = pDurationTileToTile;
		this.mAnimatedTileSpeed = new long[(this.mRows * this.mColumns)];
		for (int i = 0; i < this.mAnimatedTileSpeed.length; i++) {
			this.mAnimatedTileSpeed[i] = pAnimatedTileSpeed;
		}
		this.mSprite.setOnTouchAreaInterface(this);
		this.mMapManager = pMapManager;
	}

	/**
	 * 
	 * @param pAnimatedSprite
	 *            {@link AnimatedSprite} of human.
	 * @param pRows
	 *            {@link Integer} Amount of rows in sprite sheet for
	 *            {@link AnimatedSprite}
	 * @param pColumns
	 *            {@link Integer} Amount of columns in sprite sheet for
	 *            {@link AnimatedSprite}
	 * @param pOffset
	 *            {@link Float} of offset for sprite, can be 0,0. [0] X [1] Y
	 * @param pDurationTileToTile
	 *            {@link Float} Duration of going from the centre of one tile to
	 *            the next.
	 * @param mAnimatedTileSpeed
	 *            {@link Long} Array Duration of each frame in animated sprite.
	 */
	public HumanEntity(final AnimatedSprite pAnimatedSprite, final int pRows, final int pColumns,
			final float[] pOffset, final float pDurationTileToTile, final long[] pAnimatedTileSpeed,
			final MapHandler pMapManager) {
		this.mSprite = pAnimatedSprite;
		this.mRows = pRows;
		this.mColumns = pColumns;
		this.mOffset = pOffset;
		this.mDurationTileToTile = pDurationTileToTile;
		this.mAnimatedTileSpeed = pAnimatedTileSpeed;
		this.mSprite.setOnTouchAreaInterface(this);
		this.mMapManager = pMapManager;

	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces - IHumanEntity
	// ===========================================================
	@Override
	public float[] getCurrentCoordinateNoOffset() {
		float x = this.mSprite.getX();
		float y = this.mSprite.getY();
		x -= this.mOffset[0];
		y -= this.mOffset[1];
		return new float[] { x, y };
	}

	@Override
	public float[] getCurrentCoordinateWithOffset() {
		float x = this.mSprite.getX();
		float y = this.mSprite.getY();
		return new float[] { x, y };
	}

	@Override
	public float[] getOffset() {
		return this.mOffset;
	}

	@Override
	public float getDurationTileToTile() {
		return this.mDurationTileToTile;
	}

	@Override
	public AnimatedSprite getAnimatedSprite() {
		return this.mSprite;
	}

	@Override
	public long[] getAnimatedTileFrameSpeed() {
		return this.mAnimatedTileSpeed;
	}

	@Override
	public void setOnTouchListenerCallback(ISpriteClicked pSpriteClicked) {
		this.mSpriteClicked = pSpriteClicked;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces - ITouchAreaListener
	// ===========================================================

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionUp()) {
			if (this.mSpriteClicked != null) {
				this.mSpriteClicked.setSpriteClicked(this, pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		}
		return true;
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

	@Override
	public void recalculate(IEntity pEntity, float pDrawX, float pDrawY) {

		final float pX = pDrawX + this.m3DRecalculatePoint[0];
		final float pY = pDrawY + this.m3DRecalculatePoint[1];
		float pSectX = 0;
		float pSectY = 0;
		float resultY = 0;
		float resultX = 0;
		Color pColour = Color.BLACK;
		float[] pDrawOrigin = new float[] { 0,0 };
		int[] pLoc = this.mMapManager.getTileAt(pX, pY);
		if (pLoc != null) {
			/* Now get the top tile point*/
			pDrawOrigin = this.mMapManager.getTileCentre(pLoc);
			pDrawOrigin[1] -= (this.mMapManager.getTMXTiledMap().getTileHeight() /2);
			float p3DX = this.mMapManager.getTMXTiledMap().getTileHeight() * pLoc[1];
			float p3DY = this.mMapManager.getTMXTiledMap().getTileHeight() * pLoc[0];
			/*
			 * Determine if we are on the left or right hand side of the top tile point
			 */
			if(pX > pDrawOrigin[0]){
				/*
				 * On the right hand side
				 */
				float d1 = pX - pDrawOrigin[0];
				float a = d1 / 2;
				float d2 = pY - pDrawOrigin[1];
				resultY = d2 - a;
				resultX =  d1 + resultY;
				final float[] pObjects = new float[]{d1,a,d2};
				pColour = Color.PINK;
				pSectX = (resultX - resultY) + pDrawOrigin[0];
				pSectY = (((resultX - resultY) /2) + resultY) + pDrawOrigin[1];
			}else{
				/*
				 * On the left hand side
				 */
				float d1 = pDrawOrigin[0] - pX;
				float a = d1 / 2;
				float d2 = pY - pDrawOrigin[1];
				resultY = d2 + a;
				resultX =  d1 - resultY;
				final float[] pObjects = new float[]{d1,a,d2};
				pColour = Color.RED;
				pSectX = pDrawOrigin[0] - (resultY - resultX);
				pSectY = (((resultX - resultY) /2) + resultY) + pDrawOrigin[1];
			}
			
			p3DX += resultX;
			p3DY += resultY;
			this.mSprite.set3DPosition(p3DX, p3DY, this.m3DZ);

		}
		pSectX = (resultX - resultY) + pDrawOrigin[0];
		pSectY = (((resultX - resultY) /2) + resultY) + pDrawOrigin[1];
		
		//this.drawLineIntersect(pSectX, pSectY,pColour);
	}

	@Override
	public void set3DRecalculationPoint(int pX, int pY) {
		this.m3DRecalculatePoint = new int[] { pX, pY };
	}

	@Override
	public int[] get3DRecalculationPoint() {

		return this.m3DRecalculatePoint;
	}

	@Override
	public void set3DZIndex(float pZ) {
		this.m3DZ = pZ;
	}

	@Override
	public float get3DZIndex() {
		return this.m3DZ;
	}

	public void drawLineIntersect(float pX, float pY, Color pColour) {
		for (final Line line : this.m3DLine) {
			if (line != null) {
				this.mMapManager.mParent.runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						line.detachSelf();
					}
				});
			}
		}
		float pX1 = pX - 20;
		float pX2 = pX + 20;
		float pY1 = pY;
		float pY2 = pY;
		Line pLine1 = new Line(pX1, pY1, pX2, pY2, this.mMapManager.mParent.getVertexBufferObjectManager());
		pLine1.setColor(pColour);
		pLine1.setSkipSort(true);
		pX1 = pX;
		pX2 = pX;
		pY1 = pY + 20;
		pY2 = pY - 20;
		Line pLine2 = new Line(pX1, pY1, pX2, pY2, this.mMapManager.mParent.getVertexBufferObjectManager());
		pLine2.setColor(pColour);
		pLine2.setSkipSort(true);
		this.m3DLine[0] = pLine1;
		this.m3DLine[1] = pLine2;
		this.mMapManager.mParent.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				mMapManager.mParent.getEngine().getScene().attachChild(m3DLine[0]);
				mMapManager.mParent.getEngine().getScene().attachChild(m3DLine[1]);
			}
		});
	}


}
