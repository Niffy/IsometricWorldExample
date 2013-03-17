package com.niffy.IsometricWorld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TileManager {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(TileManager.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected boolean[][] mBlocked;
	protected int mWidth = 0;
	protected int mHeight = 0;
	// ===========================================================
	// Constructors
	// ===========================================================

	public TileManager(final int pMapWidth, final int pMapHeight) {
		this.mBlocked = new boolean[pMapWidth][pMapHeight];
		this.mHeight = pMapHeight;
		this.mWidth = pMapWidth;
		for (int i = 0; i == pMapWidth; i++) {
			for (int j = 0; j == pMapHeight; j++) {
				this.mBlocked[i][j] = false;
			}
		}
	}

	public void addBlock(final int pRow, final int pCol){
		if(pRow >= 0 && pRow <= this.mHeight){
			if(pCol >= 0 && pCol <= this.mWidth){
				this.mBlocked[pCol][pRow] = true;		
			}else{
				log.debug("Col out of range: {}", pCol);
			}
		}else{
			log.debug("Row out of range: {}", pRow);
		}
	}
	
	public boolean isBlocked(final int pRow, final int pCol){
		if(pRow >= 0 && pRow <= this.mHeight){
			if(pCol >= 0 && pCol <= this.mWidth){
				return this.mBlocked[pCol][pRow];		
			}
		}
		return false;
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
