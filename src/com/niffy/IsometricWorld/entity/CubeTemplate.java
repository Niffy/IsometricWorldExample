package com.niffy.IsometricWorld.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.drawable.Drawable;

public class CubeTemplate {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(CubeTemplate.class);

	protected int m3DWidth = 0;
	protected int m3DLength = 0;
	protected int m3DHeight = 0;

	protected float m3DX = 0;
	protected float m3DY = 0;
	protected float m3DZ = 0;

	protected float mYOffset = 0;
	protected float mXOffset = 0;
	protected String mFileName = "null";

	protected int mTilesBlockedRows = 0;
	protected int mTilesBlockedCol = 0;
	protected Drawable mDrawable;

	public CubeTemplate() {

	}

	public CubeTemplate(String pFileName) {
		this.mFileName = pFileName;
	}

	public void setWLH(final int pWidth, final int pLength, final int pHeight) {
		this.m3DWidth = pWidth;
		this.m3DHeight = pHeight;
		this.m3DLength = pLength;
	}

	public void setXYZ(final float pX, final float pY, final float pZ) {
		this.m3DX = pX;
		this.m3DY = pY;
		this.m3DZ = pZ;
	}

	public int getM3DWidth() {
		return this.m3DWidth;
	}

	public void setM3DWidth(int m3dWidth) {
		this.m3DWidth = m3dWidth;
	}

	public int getM3DLength() {
		return this.m3DLength;
	}

	public void setM3DLength(int m3dLength) {
		this.m3DLength = m3dLength;
	}

	public int getM3DHeight() {
		return this.m3DHeight;
	}

	public void setM3DHeight(int m3dHeight) {
		this.m3DHeight = m3dHeight;
	}

	public float getM3DX() {
		return m3DX;
	}

	public void setM3DX(float m3dx) {
		m3DX = m3dx;
	}

	public float getM3DY() {
		return m3DY;
	}

	public void setM3DY(float m3dy) {
		m3DY = m3dy;
	}

	public float getM3DZ() {
		return m3DZ;
	}

	public void setM3DZ(float m3dz) {
		m3DZ = m3dz;
	}

	public float getmYOffset() {
		return mYOffset;
	}

	public void setmYOffset(float mYOffset) {
		this.mYOffset = mYOffset;
	}

	public float getmXOffset() {
		return mXOffset;
	}

	public void setmXOffset(float mXOffset) {
		this.mXOffset = mXOffset;
	}

	public String getmFileName() {
		return mFileName;
	}

	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}

	public void setDrawable(final Drawable pDrawable) {
		this.mDrawable = pDrawable;
	}

	public Drawable getDrawable() {
		return this.mDrawable;
	}

	public void setTileRowsBlocked(final int pCount) {
		this.mTilesBlockedRows = pCount;
	}

	public int getTileRowsBlocked() {
		return this.mTilesBlockedRows;
	}

	public void setTileColsBlocked(final int pCount) {
		this.mTilesBlockedCol = pCount;
	}

	public int getTileColsBlocked() {
		return this.mTilesBlockedCol;
	}
}
