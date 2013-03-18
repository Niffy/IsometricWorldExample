/**
 * 
 */
package com.niffy.IsometricWorld;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.ConvertIsometricPixelToScene;
import org.andengine.extension.tmx.util.TMXTileSetSourceManager;
import org.andengine.extension.tmx.util.constants.TMXIsometricConstants;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Paul Robinson
 * 
 */
public class MapHandler {

	private final Logger log = LoggerFactory.getLogger(MapHandler.class);

	public final IsometricWorldActivity mParent;
	protected TMXTiledMap mTMXTiledMap;
	protected TMXLayer mTMXLayer;
	protected TileManager mTileManager;
	protected ConvertIsometricPixelToScene mConvertPixelToScene;

	public MapHandler(final IsometricWorldActivity pParent) {
		this.mParent = pParent;

	}

	public void setMap(final TMXTiledMap pMap) {
		this.mTMXTiledMap = pMap;
		this.mConvertPixelToScene = new ConvertIsometricPixelToScene(this.mTMXTiledMap);
	}

	public void loadMap_Iso(final String pFullPath) {
		String location = pFullPath;
		TMXLoader tmxLoader = new TMXLoader(this.mParent.getAssets(), this.mParent.getTextureManager(),
				TextureOptions.DEFAULT, this.mParent.getVertexBufferObjectManager(), null, new TMXTileSetSourceManager(
						1));
		tmxLoader.setAllocateTiles(false);
		tmxLoader.setUseLowMemoryVBO(true);
		tmxLoader.setStoreGID(true);
		TMXTiledMap txMap;
		try {
			txMap = tmxLoader.loadFromAsset(location);
			this.mTMXTiledMap = txMap;
			this.mConvertPixelToScene = new ConvertIsometricPixelToScene(this.mTMXTiledMap);
			this.mTMXTiledMap.setIsometricDrawMethod(TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_CULLING_TILED_SOURCE);
			this.mTileManager = new TileManager(this.mTMXTiledMap.getTileColumns(), this.mTMXTiledMap.getTileRows());
			this.mTMXLayer = this.mTMXTiledMap.getTMXLayers().get(0);
			this.attachMap(txMap);
		} catch (final TMXLoadException tmxle) {
			log.error("TMXLoadException", tmxle);
		}
	}

	public void attachMap(TMXTiledMap pMap) {
		this.setMap(pMap);
		this.mParent.getEngine().getScene().detachChildren();
		ArrayList<TMXLayer> layers = pMap.getTMXLayers();
		int i = -1;
		for (TMXLayer tmxLayer : layers) {
			this.attachLayer(tmxLayer, i);
			i--;
			/*
			 * We decrease i backwards so the titled map is always the lowest thing getting drawn. i.e everything is drawn on top of it 
			 */
		}
		float height = 0;
		float width = 0;
		height = this.mTMXTiledMap.getTileRows() * (this.mTMXTiledMap.getTileWidth() / 2);
		width = this.mTMXTiledMap.getTileColumns() * (this.mTMXTiledMap.getTileWidth());
		this.mParent.setupCameraIsometric(height, width);
	}

	public void detatchMap() {
		this.mParent.getEngine().getScene().detachChildren();
	}

	public void attachLayer(final TMXLayer pLayer, final int pZ) {
		if (pLayer != null) {
			IEntity pEntity = (IEntity) pLayer;
			pEntity.setZIndex(pZ);
			pEntity.setSkipSort(true);
			this.mParent.getEngine().getScene().attachChild(pEntity);
		}
	}

	public void detachLayer(final TMXLayer pLayer) {
		if (pLayer != null) {
			this.mParent.getEngine().getScene().detachChild(pLayer);
		}
	}

	public TMXTiledMap getTMXTiledMap() {
		return this.mTMXTiledMap;
	}

	public void setTMXLayer(TMXLayer pTMXLayer) {
		this.mTMXLayer = pTMXLayer;
	}

	public TMXLayer getTMXLayer() {
		return this.mTMXLayer;
	}

	public int[] getTileDimensions() {
		return this.mTMXTiledMap.getTileDimensions();
	}

	public float[] getTileCentre(int pRow, int pCol) {
		return this.mTMXLayer.getTileCentre(pCol, pRow);
	}

	public float[] getTileCentre(int[] pLoc) {
		return this.getTileCentre(pLoc[0], pLoc[1]);
	}

	public int[] getTileAt(float[] pLoc) {
		return this.getTileAt(pLoc[0], pLoc[1]);
	}

	public int[] getTileAt(float pX, float pY) {
		if (this.mTMXLayer.getAllocateTiles()) {
			final TMXTile tile = this.mTMXLayer.getTMXTileAt(pX, pY);
			if (tile != null) {
				final int pRow = tile.getTileRow();
				final int pCol = tile.getTileColumn();
				return new int[] { pRow, pCol };
			}
		} else {
			final int[] pLoc = this.mTMXLayer.getRowColAtIsometric(new float[] { pX, pY });
			return pLoc;
		}
		return null;
	}

	public float[] getTileDrawOrigin(int pRow, int pCol) {
		final float[] pCentre = this.mTMXLayer.getTileCentre(pCol, pRow);
		final float pX = pCentre[0] - (this.mTMXTiledMap.getTileWidth() / 2);
		final float pY = pCentre[1] - (this.mTMXTiledMap.getTileHeight() / 2);
		return new float[] { pX, pY };
	}

	public float[] getTileDrawOrigin(int[] pRowCol) {
		return this.getTileDrawOrigin(pRowCol[0], pRowCol[1]);
	}

	public void setTileManager(TileManager pTileManager) {
		this.mTileManager = pTileManager;
	}

	public TileManager getTileManager() {
		return this.mTileManager;
	}

}
