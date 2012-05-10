/**
 * 
 */
package com.niffy.TMXIsometricExample;

import java.util.ArrayList;

import org.andengine.entity.primitive.Line;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLayerObjectTiles;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.ConvertIsometricPixelToScene;
import org.andengine.extension.tmx.util.constants.TMXObjectType;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;

import android.util.Log;

/**
 * @author Paul Robinson
 *
 */
public class MapHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final String TAG = "MapHandler";
	private final TMXIsometricExampleActivity parent;
	private TMXTiledMap map;
	private final MenuDrawer menuDrawer;
	private final MenuAttributes attributes;
	private ConvertIsometricPixelToScene mConvertPixelToScene;
		
	// ===========================================================
	// Constructors
	// ===========================================================

	public MapHandler(final TMXIsometricExampleActivity pParent, 
			final MenuDrawer pMenuDrawer, final MenuAttributes pMenuAttributes){
		this.parent = pParent;
		this.menuDrawer = pMenuDrawer;
		this.attributes = pMenuAttributes;
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setMap(final TMXTiledMap pMap){
		this.map = pMap;
		this.mConvertPixelToScene = new ConvertIsometricPixelToScene(this.map);
	}

	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void setDrawingMethod(final int pMethod){
		if(this.attributes.SELECTED_TMX_MAP_ISO_ORTHO){
			this.map.setIsometricDrawMethod(pMethod);
		}
	}
	
	public void loadMap(final int pSelection){
		if(this.attributes.SELECTED_TMX_MAP_ISO_ORTHO){
			this.loadMap_Iso(pSelection);
		}else{
			this.loadMap_Ortho(pSelection);
		}
	}

	public void loadMap_Iso(final int pSelection){
		String location = this.parent.TMXAssetsLocation + this.parent.TMXFilesIsometric[pSelection] + this.parent.TMXFileTag;
		final TMXLoader tmxLoader = new TMXLoader(this.parent.getAssets(), this.parent.getTextureManager(),
				TextureOptions.DEFAULT, this.parent.getVertexBufferObjectManager(), this.parent);
		TMXTiledMap txMap;
		try{
			txMap = tmxLoader.loadFromAsset(location);
			this.map = txMap;
			this.parent.mMap = this.map;
			this.mConvertPixelToScene = new ConvertIsometricPixelToScene(this.map);
			this.setDrawingMethod(this.attributes.SELECTED_DRAW_METHOD);
			this.attachMap(txMap);
		}catch(final TMXLoadException tmxle){
			Log.e(TAG, String.format("Error loading file: %s", location), tmxle);
		}
	}

	public void loadMap_Ortho(final int pSelection){
		String location = this.parent.TMXAssetsLocation + this.parent.TMXFilesOrthographic[pSelection] + this.parent.TMXFileTag;
		final TMXLoader tmxLoader = new TMXLoader(this.parent.getAssets(), this.parent.getTextureManager(),
				TextureOptions.DEFAULT, this.parent.getVertexBufferObjectManager(), this.parent);
		TMXTiledMap txMap;
		try{
			txMap = tmxLoader.loadFromAsset(location);
			this.map = txMap;
			this.parent.mMap = this.map;
			this.mConvertPixelToScene = new ConvertIsometricPixelToScene(this.map);
			this.attachMap(txMap);
		}catch(final TMXLoadException tmxle){
			Log.e(TAG, String.format("Error loading file: %s", location), tmxle);
		}
	}
	
	public void attachMap(TMXTiledMap pMap){
		this.setMap(pMap);
		this.parent.getEngine().getScene().detachChildren();
		ArrayList<TMXLayer> layers = pMap.getTMXLayers();
		for (TMXLayer tmxLayer : layers) {
			this.attachLayer(tmxLayer);
		}
		this.parent.TilesBlocked = new boolean[pMap.getTileRows()][pMap.getTileColumns()];
		this.parent.currentLayer = pMap.getTMXLayers().get(0);
		this.parent.mSceneObjectLayerMenu = this.menuDrawer.create_object_layer();
		float height = 0;
		float width = 0;
		if(this.attributes.SELECTED_TMX_MAP_ISO_ORTHO){
			height = this.map.getTileRows() * (this.map.getTileWidth() /2);
			width = this.map.getTileColumns() * (this.map.getTileWidth());
			this.parent.setupCameraIsometric(height, width);
		}else{
			height = this.map.getTileRows() * this.map.getTileHeight();
			width = this.map.getTileColumns() * this.map.getTileWidth();
			this.parent.setupCameraOrthographic(height, width);
		}
	}
	

	public void detatchMap(){
		this.parent.getEngine().getScene().detachChildren();
	}

	public void attachLayer(final TMXLayer pLayer){
		if(pLayer != null){
			this.parent.getEngine().getScene().attachChild(pLayer);
		}
	}

	public void attachLayer(final int pLayerNumber){
		if(this.map != null){
			TMXLayer pLayer = this.map.getTMXLayers().get(pLayerNumber);
			this.attachLayer(pLayer);
		}
	}

	public void detachLayer(final TMXLayer pLayer){
		if(pLayer != null){
			this.parent.getEngine().getScene().detachChild(pLayer);
		}	
	}

	public void detachLayer(final int pLayerNumber){
		if(this.map != null){
			TMXLayer pLayer = this.map.getTMXLayers().get(pLayerNumber);
			this.detachLayer(pLayer);
		}
	}

	public void attachAllObjectLayers(){
		ArrayList<TMXObjectGroup> groups = this.map.getTMXObjectGroups();
		for (TMXObjectGroup objG : groups) {
			TMXObjectType type = objG.getObjectType();
			if(type.equals(TMXObjectType.MIXED)){
				this.attachMixedObjectLayer(objG);
			}else if(type.equals(TMXObjectType.RECTANGLE)){
				this.attachRectangleObjectLayer(objG);
			}else if(type.equals(TMXObjectType.POLYGON)){
				this.attachPolygonObjectLayer(objG);
			}else if(type.equals(TMXObjectType.POLYLINE)){
				this.attachPolylineObjectLayer(objG);
			}else if(type.equals(TMXObjectType.EMPTY)){
				Log.w(TAG, String.format("Could not attach TMXObjectGroup %s as it has no objects", objG.getName()));
			}else if(type.equals(TMXObjectType.TILEOBJECT)){
				this.attachTileObjectLayer(objG);
			}else if(type.equals(TMXObjectType.UNKNOWN)){
				Log.w(TAG, String.format("Could not attach TMXObjectGroup %s as its object type could not be determined", objG.getName()));
			}else{
				Log.w(TAG, String.format("Could not attach TMXObjectGroup %s as we could not handle the TMXObjectType %s ", objG.getName(),type.toString()));
			}
		}
	}
	
	public void attachObjectLayer(final String pTMXObjectGroupName){
		ArrayList<TMXObjectGroup> groups = this.map.getTMXObjectGroups();
		for (TMXObjectGroup tmxObjectGroup : groups) {
			if(tmxObjectGroup.getName().equals(pTMXObjectGroupName)){
				this.attachObjectLayer(tmxObjectGroup);
			}
		}
	}
	
	public void attachObjectLayer(final TMXObjectGroup pTMXObjectGroup){
		TMXObjectType type = pTMXObjectGroup.getObjectType();
		if(type.equals(TMXObjectType.MIXED)){
			this.attachMixedObjectLayer(pTMXObjectGroup);
		}else if(type.equals(TMXObjectType.RECTANGLE)){
			this.attachRectangleObjectLayer(pTMXObjectGroup);
		}else if(type.equals(TMXObjectType.POLYGON)){
			this.attachPolygonObjectLayer(pTMXObjectGroup);
		}else if(type.equals(TMXObjectType.POLYLINE)){
			this.attachPolylineObjectLayer(pTMXObjectGroup);
		}else if(type.equals(TMXObjectType.EMPTY)){
			Log.w(TAG, String.format("Could not attach TMXObjectGroup %s as it has no objects", pTMXObjectGroup.getName()));
		}else if(type.equals(TMXObjectType.TILEOBJECT)){
			this.attachTileObjectLayer(pTMXObjectGroup);
		}else if(type.equals(TMXObjectType.UNKNOWN)){
			Log.w(TAG, String.format("Could not attach TMXObjectGroup %s as its object type could not be determined", pTMXObjectGroup.getName()));
		}else{
			Log.w(TAG, String.format("Could not attach TMXObjectGroup %s as we could not handle the TMXObjectType %s ", pTMXObjectGroup.getName(),type.toString()));
		}
	}
	
	public void attachTileObjectLayer(final TMXObjectGroup pTMXObjectGroup){
		TMXLayerObjectTiles tmxOTL = new TMXLayerObjectTiles(this.map, null, this.parent.getVertexBufferObjectManager(), pTMXObjectGroup);
		this.attachTileObjectLayer(tmxOTL);
	}
	
	public void attachTileObjectLayer(final String objectGroupName){
		for (TMXObjectGroup objG : this.map.getTMXObjectGroups()) {
			if(objG.getName().equals(objectGroupName)){
				this.attachTileObjectLayer(objG);
			}
		}
	}
	
	public void attachTileObjectLayer(final TMXLayerObjectTiles pTMXLayerObjectTiles){
		if(pTMXLayerObjectTiles.getCount() <=0){
		}else{
			this.parent.mTileObject.add(pTMXLayerObjectTiles);
			this.parent.getEngine().getScene().attachChild(pTMXLayerObjectTiles);
			this.createBlockedTiles(pTMXLayerObjectTiles);
		}
	}
	
	public void attachRectangleObjectLayer(final TMXObjectGroup pTMXObjectGroup){
		ArrayList<TMXObject> objects = pTMXObjectGroup.getTMXObjects();
		for (TMXObject tmxObject : objects) {
			this.attachRectangleObject(tmxObject);
		}
	}
	
	public void attachRectangleObject(final TMXObject pTMXObject){
		final float[][] pSceneCoordinates = this.mConvertPixelToScene.rectangleObjectToScene(pTMXObject.getX(), pTMXObject.getY(),
				pTMXObject.getWidth(), pTMXObject.getHeight());
		for(int i = 0; i < 4; i ++){
			float pX1 = pSceneCoordinates[i][0];
			float pY1 = pSceneCoordinates[i][1];
			float pX2 = 0;
			float pY2 = 0;
			if(i == 3){
				pX2 = pSceneCoordinates[0][0];
				pY2 = pSceneCoordinates[0][1];
			}else{
				pX2 = pSceneCoordinates[i+1][0];
				pY2 = pSceneCoordinates[i+1][1];
			}
			Line line = new Line(pX1, pY1, pX2, pY2, this.parent.getVertexBufferObjectManager());
			line.setColor(this.parent.objectLines);
			line.setLineWidth(3f);
			this.parent.mDrawnLines.add(line);
			this.parent.getEngine().getScene().attachChild(line);
		}
	}
	
	public void attachPolygonObjectLayer(final TMXObjectGroup pTMXObjectGroup){
		ArrayList<TMXObject> objects = pTMXObjectGroup.getTMXObjects();
		for (TMXObject tmxObject : objects) {
			this.attachPolyPointObject(tmxObject);
		}
	}
	
	public void attachPolylineObjectLayer(final TMXObjectGroup pTMXObjectGroup){
		ArrayList<TMXObject> objects = pTMXObjectGroup.getTMXObjects();
		for (TMXObject tmxObject : objects) {
			this.attachPolyPointObject(tmxObject);
		}
	}
	
	public void attachPolyPointObject(final TMXObject pTMXObject){
		final float[][] pSceneCoordinates = this.mConvertPixelToScene.objectToScene(pTMXObject);
		for(int i = 0; i < pSceneCoordinates.length; i ++){
			float pX1 = pSceneCoordinates[i][0];
			float pY1 = pSceneCoordinates[i][1];
			float pX2 = 0;
			float pY2 = 0;
			if(i == pSceneCoordinates.length -1){
				pX2 = pSceneCoordinates[0][0];
				pY2 = pSceneCoordinates[0][1];
			}else{
				pX2 = pSceneCoordinates[i+1][0];
				pY2 = pSceneCoordinates[i+1][1];
			}
			Line line = new Line(pX1, pY1, pX2, pY2, this.parent.getVertexBufferObjectManager());
			line.setColor(this.parent.objectLines);
			line.setLineWidth(3f);
			this.parent.mDrawnLines.add(line);
			this.parent.getEngine().getScene().attachChild(line);
		}
	}	
	
	public void attachMixedObjectLayer(final TMXObjectGroup pTMXObjectGroup){
		ArrayList<TMXObject> objects = pTMXObjectGroup.getTMXObjects();
		for (TMXObject tmxObject : objects) {
			TMXObjectType type = tmxObject.getObjectType();
			if(type.equals(TMXObjectType.MIXED)){
				Log.w(TAG, String.format("Could not attach TMXObject %s as it has multiple objects, check TMX file", tmxObject.getName()));
			}else if(type.equals(TMXObjectType.RECTANGLE)){
				this.attachRectangleObject(tmxObject);
			}else if(type.equals(TMXObjectType.POLYGON)){
				this.attachPolyPointObject(tmxObject);
			}else if(type.equals(TMXObjectType.POLYLINE)){
				this.attachPolyPointObject(tmxObject);
			}else if(type.equals(TMXObjectType.EMPTY)){
				Log.w(TAG, String.format("Could not attach TMXObject %s as it is empty", tmxObject.getName()));
			}else if(type.equals(TMXObjectType.TILEOBJECT)){
				Log.w(TAG, String.format("Could not attach TMXObject %s as you shouldn't be mixing object layers", tmxObject.getName()));
			}else if(type.equals(TMXObjectType.UNKNOWN)){
				Log.w(TAG, "Could not attach TMXObject as its object type could not be determined");
			}else{
				Log.w(TAG, String.format("Could not attach TMXObject as we could not handle the TMXObjectType %s", type.toString()));
			}
		}
	}
	
	public void createBlockedTiles(final TMXLayerObjectTiles pTMXLayerObjectTiles){
		final int tileColumns = this.map.getTileColumns();
		final int tileRows = this.map.getTileRows();
		TMXTile[][] temp = pTMXLayerObjectTiles.getTMXTiles();
		for (int j = 0; j < tileRows; j++) {
			for (int i = 0; i < tileColumns; i++) {
				if(temp[j][i] != null){
					if(this.parent.TilesBlocked[j][i] == false){
						this.parent.TilesBlocked[j][i] = true;
					}
				}
			}
		}
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
