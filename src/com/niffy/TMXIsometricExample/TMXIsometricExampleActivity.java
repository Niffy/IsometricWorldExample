package com.niffy.TMXIsometricExample;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.constants.TMXIsometricConstants;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.opengl.GLES20;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

public class TMXIsometricExampleActivity extends BaseGameActivity implements
IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener,
ITMXTilePropertiesListener, IOnMenuItemClickListener{

	//TODO implement object collison, maybe even poly points
	//TODO fix zoom camera to zoom out, sort of works
	//TODO implement menu to change zooms

	private final String TAG = "TMXIsometricExampleActivity";
	//Camera stuff
	public int CAMERA_WIDTH = 720;
	public int CAMERA_HEIGHT = 480;
	public ZoomCamera mCamera;
	public ScrollDetector mScrollDetector;
	public PinchZoomDetector mPinchZoomDetector;
	public float mPinchZoomStartedCameraZoomFactor;
	private float maxZoom = 0;
	private final float zoomDepth = 2; //Smaller this is, the less we zoom in?
	private boolean mClicked = false;

	private HUD mHUD;
	private Text mFPS;
	private Text mXYLoc;
	private Text mTileRowCol1;

	public Engine mEngine;
	public IErrorLog log = null;

	//Map and current layer.
	public TMXLayer currentLayer = null;
	public TMXTiledMap mMap = null;
	//main menu selection
	private static final int SELECT_TILE_HIT = 0;
	private static final int SELECT_TMX_MAP_ISO = 1;
	private static final int SELECT_TMX_MAP_ORTHO = 2;
	private static final int LAYER_SELECTION_ENABLED = 3;
	private static final int DRAW_METHOD = 4;
	private static final int REMOVE_LINES = 5;
	private static final int RESET_CAMERA = 6;
	private static final int BACK_TO_GAME = 7;
	//Are we monitoring tile hits?
	private boolean ENABLE_TILE_HIT = false;
	//What tile hits options there is
	private static final int TILE_ENABLE_DISABLE = 50;
	private static final int TILE_SELECT_LAYER = 51;
	private static final int TILE_SELECT_COLOUR = 52;
	private static final int TILE_HIT_CENTRE = 53;
	private static final int TILE_HIT_POINT = 54;
	private static final int TILE_HIT_BACK = 55;
	//colours to use for hit points
	private static final int COLOUR_BLUE = 100;
	private static final int COLOUR_GREEN = 101;
	private static final int COLOUR_YELLOW = 102;
	private static final int COLOUR_RED = 103;
	//When a map has been selected this helps manage menu and selection.
	private static final int MAP_SELECTED_ISO = 20;
	private static final int MAP_SELECTED_ORTHO = 21;
	//When a drawing method has been selected, this helps mange menu and selection
	private static final int DRAW_METHOD_SELECTED = 200;
	private static final int DRAW_METHOD_BACK = 201;

	//Drawing method selected, default DRAW_METHOD_ISOMETRIC_CULLING_TILED_SOURCE
	private static int SELECTED_DRAW_METHOD = TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_CULLING_TILED_SOURCE;
	//Selected layer, tile hit selection (default centre), colour selected (default yellow)
	private static int SELECTION_LAYER = 0;
	private static int SELECTION_TILE_HIT = 53;
	private static int SELECTION_COLOUR = 102;
	//Selected map to load, default is 0 (in TMXFilesIsometric)
	private static int SELECTED_TMX_MAP = 0;
	private boolean SELECTED_TMX_MAP_ISO_ORTHO = true; //ISO = true
	//menu scenes + colour for menus
	protected Background mMenuBackground = new Background(1f, 1f, 1f, 0.35f);
	protected MenuScene mSceneMasterMenu;
	protected MenuScene mSceneTileHitMenu;
	protected MenuScene mSceneLayerMenu;
	protected MenuScene mSceneColourMenu;
	protected MenuScene mSceneTMXIsoMenu;
	protected MenuScene mSceneTMXORthoMenu;
	protected MenuScene mSceneDrawingMethod;
	org.andengine.util.color.Color selected = new org.andengine.util.color.Color(1f, 0f, 0f);
	org.andengine.util.color.Color unselected = new org.andengine.util.color.Color(0f, 0f, 0f);

	//fonts
	private String mFontFile = "font/Roboto-Condensed.ttf";
	private Font mFont;
	private Font mFont1;
	private Font mFont2;
	private Font mFont3;
	private Font mFont4;
	private Font mFont5;
	private Font mFont6;

	//Lines belong to tile hits
	private ArrayList<Line> mTileLineHits = new ArrayList<Line>();

	//TMXFiles
	private String TMXAssetsLocation = "tmx/";
	private String TMXFileTag = ".tmx";
	private String[] TMXFilesIsometric = 
		{ "isometric_grass_and_water", //0
			"Isometric_32x16_nooffset",  //1
			"Isometric_32x16_with_offset_x", //2
			"Isometric_32x16_with_offset_x_y_odd", //3
			"Isometric_32x16_with_offset_x_y_even", //4
			"Isometric_64_32_with_offset_y", //5
			"Isometric_64_32_with_offset_y_evensize", //6
			"isometricBlocks", //7
			"Large_isometricBlocks" //8
		};

	private String[] TMXFilesOrthographic = { 
			"Ortho_1_32__32"  //0
	};

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.log = new Logging();
		this.log.setTag(this.TAG);
		this.log.enable(false);
		this.log.i(0, "onCreateEngineOptions");
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		CAMERA_WIDTH = displayMetrics.widthPixels;
		CAMERA_HEIGHT = displayMetrics.heightPixels;
		this.mCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mCamera.setZoomFactor(this.zoomDepth);
		EngineOptions eOps = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		return eOps;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		this.log.i(0,"onCreateEngine");
		this.mEngine = new Engine(pEngineOptions);
		return this.mEngine;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
					throws Exception {
		this.log.i(0,"onCreateResources");
		final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture fontTexture1 = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture fontTexture2 = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture fontTexture3 = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture fontTexture4 = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture fontTexture5 = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture fontTexture6 = new BitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		this.mFont = FontFactory.createFromAsset(this.getFontManager(), fontTexture, this.getAssets(), this.mFontFile, 40, true, android.graphics.Color.WHITE);
		this.mFont.load();
		this.mFont1 = FontFactory.createFromAsset(this.getFontManager(), fontTexture1, this.getAssets(), this.mFontFile, 40, true, android.graphics.Color.WHITE);
		this.mFont1.load();
		this.mFont2 = FontFactory.createFromAsset(this.getFontManager(), fontTexture2, this.getAssets(), this.mFontFile, 40, true, android.graphics.Color.WHITE);
		this.mFont2.load();
		this.mFont3 = FontFactory.createFromAsset(this.getFontManager(), fontTexture3, this.getAssets(), this.mFontFile, 40, true, android.graphics.Color.WHITE);
		this.mFont3.load();
		this.mFont4 = FontFactory.createFromAsset(this.getFontManager(), fontTexture4, this.getAssets(), this.mFontFile, 40, true, android.graphics.Color.WHITE);
		this.mFont4.load();
		this.mFont5 = FontFactory.createFromAsset(this.getFontManager(), fontTexture5, this.getAssets(), this.mFontFile, 20, true, android.graphics.Color.GRAY);
		this.mFont5.load();
		this.mFont6 = FontFactory.createFromAsset(this.getFontManager(), fontTexture6, this.getAssets(), this.mFontFile, 40, true, android.graphics.Color.GRAY);
		this.mFont6.load();
		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		this.log.i(0,"onCreateScene");
		Scene mScene = new Scene();
		final FPSLogger fpsLogger = new FPSLogger();
		this.getEngine().registerUpdateHandler(fpsLogger);
		mScene.setBackground(new Background(0.6509f, 0.8156f, 0.7764f));
		this.mSceneMasterMenu = this.create_master_menu();
		this.mSceneTileHitMenu = this.create_tile_hit_menu();
		this.mSceneColourMenu = this.create_colour_selection_menu();
		this.mSceneTMXIsoMenu = this.create_TMX_selection_isometric();
		this.mSceneTMXORthoMenu = this.create_TMX_selection_orthographic();
		this.mSceneDrawingMethod = this.create_drawing_method_menu();
		//We create the layer menu later when attaching the map to the scene

		this.mHUD = new HUD();
		this.getEngine().getCamera().setHUD(this.mHUD);
		this.mFPS = new Text(0, 0, this.mFont5, "FPS:","FPS: XXXXXXXXXXXXXXXXX".length(), this.getVertexBufferObjectManager());
		this.mXYLoc = new Text(0, this.mFPS.getY() +1 + this.mFPS.getFont().getLineHeight(), this.mFont5, "Touch X: Y:","Touch X: XXXXXXXXXXXX Y: XXXXXXXXXX".length(), this.getVertexBufferObjectManager());
		this.mTileRowCol1 = new Text(0, this.mXYLoc.getY() +1 + this.mXYLoc.getFont().getLineHeight(), this.mFont5, "Row: Col:","Row: Not in Bounds Col: Not in Bounds".length(), this.getVertexBufferObjectManager());
		this.mHUD.attachChild(this.mFPS);
		this.mHUD.attachChild(this.mXYLoc);
		this.mHUD.attachChild(this.mTileRowCol1);

		mScene.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				//FPSCounter is used in the example, but Logger seems more accurate to me
				mFPS.setText("FPS: " + fpsLogger.getFPS());
			}
		}));

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		this.log.i(0,"onPopulateScene");
		pScene.setOnSceneTouchListener(this);
		pScene.setTouchAreaBindingOnActionMoveEnabled(true);
		pScene.setOnAreaTouchTraversalFrontToBack();
		this.loadMap(SELECTED_TMX_MAP);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pSceneTouchEvent) {
		this.log.i(1,"onPinchZoomStarted");
		this.mPinchZoomStartedCameraZoomFactor = this.mCamera.getZoomFactor();
		this.mClicked = false;
	}

	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		this.log.i(1,"onPinchZoom");
		this.mCamera.setZoomFactor(Math.min(Math.max(this.maxZoom, this.mPinchZoomStartedCameraZoomFactor * pZoomFactor), this.zoomDepth));
		this.mClicked = false;
	}

	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		this.log.i(1,"onPinchZoomFinished");
		/*
		 * We could have this, but it would only have to mirror onPinchZoom
		 * so why add it?
		 * 
		 */
		//this.mCamera.setZoomFactor(Math.min(Math.max(this.maxZoom, this.mPinchZoomStartedCameraZoomFactor* pZoomFactor),this.zoomDepth));
		this.mClicked = false;
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		this.log.i(1,"onScrollStarted");
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		this.log.i(1,"onScroll");
		final float zoomFactor = mCamera.getZoomFactor();
		float xLocation = -pDistanceX / zoomFactor;
		float yLocation = -pDistanceY / zoomFactor;
		mCamera.offsetCenter(xLocation, yLocation);
		this.mClicked = false;
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		this.log.i(1,"onScrollFinished");
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		this.log.i(2,"onSceneTouchEvent");

		if(this.mPinchZoomDetector != null) {
			this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);
			if(this.mPinchZoomDetector.isZooming()) {
				this.mScrollDetector.setEnabled(false);
			} else {
				if(pSceneTouchEvent.isActionDown()) {
					this.mScrollDetector.setEnabled(true);
				}
				this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		} else {
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		if(pSceneTouchEvent.isActionUp()) {
			if(this.mClicked){
				this.handleActionDown(pScene, pSceneTouchEvent);
			}
			this.mClicked = true;
		}
		return true;
	}

	private void handleActionDown(Scene pScene, TouchEvent pSceneTouchEvent){
		this.log.i(4, String.format("Touch X: %f Y: %f",pSceneTouchEvent.getX(), pSceneTouchEvent.getY() ));
		this.touchMap(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

	}

	public void resetCamera(){
		this.log.i(4, "Reset camera");
		this.mCamera.setZoomFactor(this.zoomDepth);
		this.mCamera.setCenter(0,0);
		this.mXYLoc.setText("Touch X:0 Y:0");
		this.mTileRowCol1.setText("Row: Not in Bounds Col: Not in Bounds");
	}

	/**
	 * Setup the Camera if the map is orthographic. 
	 * @param height {@link Float} overall height of the map
	 * @param width {@link Float} overall width of the map
	 */
	public void setupCameraOrthographic(final float height, final float width){
		this.log.i(4, "Setup camera");
		if (this.CAMERA_WIDTH / height >= this.CAMERA_HEIGHT / width){
			this.maxZoom = this.CAMERA_WIDTH / height;
		}else {
			this.maxZoom = this.CAMERA_HEIGHT / width; 
		}

		final float MAX_CAMERA_BOUND_ADDITION = 30;
		final float pBoundsXMin = -MAX_CAMERA_BOUND_ADDITION;
		final float pBoundsYMin = -MAX_CAMERA_BOUND_ADDITION;
		final float pBoundsXMax = width + MAX_CAMERA_BOUND_ADDITION;
		final float pBoundsYMax = height + MAX_CAMERA_BOUND_ADDITION;
		this.mCamera.setBounds(pBoundsXMin, pBoundsYMin, pBoundsXMax, pBoundsYMax);
		this.mCamera.setBoundsEnabled(true);	
		this.resetCamera();
	}

	/**
	 * Setup the Camera if the map is isometric. 
	 * 
	 * @param height {@link Float} overall height of the map
	 * @param width {@link Float} overall width of the map
	 */
	public void setupCameraIsometric(final float height, final float width){
		this.log.i(4, "Setup camera");
		if (this.CAMERA_WIDTH / height >= this.CAMERA_HEIGHT / width){
			this.maxZoom = this.CAMERA_WIDTH / height;
		}else {
			this.maxZoom = this.CAMERA_HEIGHT / width; 
		}
		/*
		 * We have to consider the map rows and columns do not match,
		 * so the xMin works out the bounds to the left of scene x=0,
		 * xMax to the right of scene x=0.
		 * The left hand side of the map is the rows, while the right is columns.
		 * We need to calculate the length of these for use in the bounds
		 * 
		 * We have to take into account the placement of the tile.
		 * The very left edge of the first tile is X = 0
		 * So when halving the width the right hand side is short changed by 
		 * half a tile width and the left gains, so add half a tile width to 
		 * the xMin and xMax to even this out.
		 */
		final float MAX_CAMERA_BOUND_ADDITION = 60;
		final float halfTileWidth = this.mMap.getTileWidth() /2;
		final float xMin = this.mMap.getTileRows() * halfTileWidth;
		final float xMax = this.mMap.getTileColumns() * halfTileWidth;
		final float pBoundsXMin = halfTileWidth - xMin - MAX_CAMERA_BOUND_ADDITION;
		final float pBoundsYMin = -MAX_CAMERA_BOUND_ADDITION;
		final float pBoundsXMax = halfTileWidth + xMax + MAX_CAMERA_BOUND_ADDITION;
		final float pBoundsYMax = height + MAX_CAMERA_BOUND_ADDITION;
		this.mCamera.setBounds(pBoundsXMin, pBoundsYMin, pBoundsXMax, pBoundsYMax);
		this.mCamera.setBoundsEnabled(true);	
		this.resetCamera();
	}
	
	public void setDrawingMethod(final int pMethod){
		if(this.SELECTED_TMX_MAP_ISO_ORTHO){
			this.mMap.setIsometricDrawMethod(pMethod);
		}
	}

	public void loadMap(final int pSelection){
		this.log.i(4,"Load Map");
		if(this.SELECTED_TMX_MAP_ISO_ORTHO){
			this.loadMap_Iso(pSelection);
		}else{
			this.loadMap_Ortho(pSelection);
		}
	}

	public void loadMap_Iso(final int pSelection){
		this.log.i(5,"loadMap_Iso");
		String location = this.TMXAssetsLocation + this.TMXFilesIsometric[pSelection] + this.TMXFileTag;
		final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.getTextureManager(),
				TextureOptions.DEFAULT, this.getVertexBufferObjectManager(), this);
		TMXTiledMap txMap;
		try{
			txMap = tmxLoader.loadFromAsset(location);
			this.mMap = txMap;
			this.setDrawingMethod(SELECTED_DRAW_METHOD);
			this.attachMap(txMap);
		}catch(final TMXLoadException tmxle){
			this.log.e(5, String.format("Error loading file: %s", location), tmxle);
		}
	}

	public void loadMap_Ortho(final int pSelection){
		this.log.i(5,"loadMap_Ortho");
		String location = this.TMXAssetsLocation + this.TMXFilesOrthographic[pSelection] + this.TMXFileTag;
		final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.getTextureManager(),
				TextureOptions.DEFAULT, this.getVertexBufferObjectManager(), this);
		TMXTiledMap txMap;
		try{
			txMap = tmxLoader.loadFromAsset(location);
			this.mMap = txMap;
			this.attachMap(txMap);
		}catch(final TMXLoadException tmxle){
			this.log.e(5, String.format("Error loading file: %s", location), tmxle);
		}
	}

	public void attachMap(TMXTiledMap pMap){
		this.log.i(5, "attachMap");
		this.getEngine().getScene().detachChildren();
		ArrayList<TMXLayer> layers = pMap.getTMXLayers();
		for (TMXLayer tmxLayer : layers) {
			this.attachLayer(tmxLayer);
		}
		this.currentLayer = pMap.getTMXLayers().get(0);
		this.mSceneLayerMenu = this.create_layer_selection_menu();
		float height = 0;
		float width = 0;
		if(this.SELECTED_TMX_MAP_ISO_ORTHO){
			height = this.mMap.getTileRows() * (this.mMap.getTileWidth() /2);
			width = this.mMap.getTileColumns() * (this.mMap.getTileWidth());
			this.setupCameraIsometric(height, width);
		}else{
			height = this.mMap.getTileRows() * this.mMap.getTileHeight();
			width = this.mMap.getTileColumns() * this.mMap.getTileWidth();
			this.setupCameraOrthographic(height, width);
		}
	}

	public void detatchMap(){
		this.log.i(5, "detatchMap");
		this.getEngine().getScene().detachChildren();
	}

	public void attachLayer(final TMXLayer pLayer){
		this.log.i(5, "attachLayer by TMXLayer");
		if(pLayer != null){
			this.getEngine().getScene().attachChild(pLayer);
		}
	}

	public void attachLayer(final int pLayerNumber){
		this.log.i(5, "attachLayer by layer number");
		if(this.mMap != null){
			TMXLayer pLayer = this.mMap.getTMXLayers().get(pLayerNumber);
			this.attachLayer(pLayer);
		}
	}

	public void detachLayer(final TMXLayer pLayer){
		this.log.i(5, "detachLayer by TMXLayer");
		if(pLayer != null){
			this.getEngine().getScene().detachChild(pLayer);
		}	
	}

	public void detachLayer(final int pLayerNumber){
		this.log.i(5, "detachLayer by layer number");
		if(this.mMap != null){
			TMXLayer pLayer = this.mMap.getTMXLayers().get(pLayerNumber);
			this.detachLayer(pLayer);
		}
	}

	public void touchMap(final float pX, final float pY){
		//Standard method of getting tile
		final float[] pToTiles = this.getEngine().getScene().convertLocalToSceneCoordinates(pX, pY);
		this.currentLayer = this.mMap.getTMXLayers().get(SELECTION_LAYER);
		final TMXTile tmxSelected = this.currentLayer.getTMXTileAt(pToTiles[0], pToTiles[1]);
		if(tmxSelected != null){
			this.log.i(6, String.format("Standard getTMXTileAt - tile found Row: %d Column %d ", tmxSelected.getTileRow(), tmxSelected.getTileColumn()));
			this.mTileRowCol1.setText(String.format("Row: %d Col: %d", tmxSelected.getTileRow(), tmxSelected.getTileColumn()));
		}else{
			this.mTileRowCol1.setText(String.format("Row: %s Col: %s", "Not in Bounds" ,"Not in Bounds"));
		}
		this.mXYLoc.setText(String.format("Touch X: %f Y: %f", pX, pY));

		if(this.SELECTED_TMX_MAP_ISO_ORTHO){
			//Alternative method
			TMXTile TMXTileIsoAlt = this.mMap.getTMXLayers().get(0).getTMXTileAtIsometricAlternative(pToTiles);
			if(TMXTileIsoAlt != null){
				this.log.i(6, String.format("Alternative getTMXTileAt - tile found: Row: %d Col: %d", TMXTileIsoAlt.getTileRow(), TMXTileIsoAlt.getTileColumn()));
			}
		}

		if(this.ENABLE_TILE_HIT){
			if(tmxSelected !=null){
				this.drawIntersect(tmxSelected, pX, pY);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(getEngine().getScene().hasChildScene()){
						getEngine().getScene().getChildScene().back();
					}else{
						getEngine().getScene().setChildScene(mSceneMasterMenu, false, true, true);
					}
				}
			});
		}
		return super.onKeyDown(keyCode, event);
	}

	public void drawOrigin(){
		//this is for use on an isometric map, rather pointless but shows the first tile X point
		float pOrigin = this.mMap.getTMXLayers().get(0).getOrigin();	
		Line line1 = new Line(pOrigin, 0, pOrigin, 2000, this.getVertexBufferObjectManager());
		line1.setColor(new Color(1f, 1f, 0));
		this.getEngine().getScene().attachChild(line1);
		this.mTileLineHits.add(line1);
	}

	public void draw_y_lines(){
		//Useless, isometric only, draw a grid across the X axis
		float x = 0;
		float y = 0;
		float xInc = 16;
		float middle = this.currentLayer.getOrigin();
		for(int i = 0; i < 100; i++){
			Line line1 = new Line(x, y, x, y + 2000, this.getVertexBufferObjectManager());
			x += xInc;
			if(x == middle){
				line1.setColor(0,0,1f);
			}else{
				line1.setColor(1f, 1f, 0);
			}
			this.getEngine().getScene().attachChild(line1);
		}
	}

	public void draw_x_lines(){
		//Useless, isometric only, draw a grid across the Y axis
		float x = 0;
		float y = 0;
		float yInc = 16;
		for(int i = 0; i < 100; i++){
			Line line1 = new Line(x, y, x + 2000, y, this.getVertexBufferObjectManager());
			y += yInc;
			line1.setColor(1f, 1f, 0);
			this.getEngine().getScene().attachChild(line1);
		}
	}

	public void removeLines(){
		this.log.i(7, "removeLines");
		for (Line l : this.mTileLineHits) {
			l.detachSelf();
		}
		this.mTileLineHits.clear();
	}

	public void drawIntersect(TMXTile pTile, final float pX, final float pY){
		this.log.i(7, "drawIntersect");
		if(SELECTION_TILE_HIT == TILE_HIT_CENTRE){
			this.drawIntersectCentre(pTile);
		}else{
			this.drawIntersectPoint(pTile, pX, pY);
		}
	}

	public void drawIntersectCentre(TMXTile pTile){
		this.log.i(7, "drawIntersectCentre");
		float pX = 0;
		float pY = 0;
		float pWidth = this.mMap.getTileWidth()/2;;
		float pHeight = this.mMap.getTileHeight()/2;
		if(this.SELECTED_TMX_MAP_ISO_ORTHO){
			pX = pTile.getTileXIsoCentre();
			pY = pTile.getTileYIsoCentre();
		}else{
			pX = pTile.getTileX() + pWidth;
			pY = pTile.getTileY() + pHeight;
		}	
		if(SELECTION_COLOUR == COLOUR_BLUE){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(0, 0, 1f));
		}else if(SELECTION_COLOUR == COLOUR_GREEN){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(0, 1f, 0));
		}else if(SELECTION_COLOUR == COLOUR_RED){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(1f, 0, 0));
		}else if(SELECTION_COLOUR == COLOUR_YELLOW){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(1f, 1f, 0));
		}
	}

	public void drawIntersectPoint(TMXTile pTile,final float pX, final float pY){
		this.log.i(7, "drawIntersectPoint");
		float pWidth = pTile.getTileWidth() /2;
		float pHeight = pTile.getTileHeight() /2;
		if(SELECTION_COLOUR == COLOUR_BLUE){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(0, 0, 1f));
		}else if(SELECTION_COLOUR == COLOUR_GREEN){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(0, 1f, 0));
		}else if(SELECTION_COLOUR == COLOUR_RED){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(1f, 0, 0));
		}else if(SELECTION_COLOUR == COLOUR_YELLOW){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(1f, 1f, 0));
		}
	}

	public void drawHit(final float x, final float y, final float width, final float height, final Color pColor){
		this.log.i(7, "drawHit");
		Line line1 = new Line(x - width, y, x + width, y, this.getVertexBufferObjectManager());
		line1.setColor(pColor);
		Line line2 = new Line(x, y - height, x, y + height, this.getVertexBufferObjectManager());
		line2.setColor(pColor);
		this.getEngine().getScene().attachChild(line1);
		this.getEngine().getScene().attachChild(line2);
		this.mTileLineHits.add(line1);
		this.mTileLineHits.add(line2);
	}

	public MenuScene create_master_menu(){
		this.log.i(8, "create_master_menu");
		MenuScene masterMenu = new MenuScene(this.getEngine().getCamera());
		masterMenu.setBackground(this.mMenuBackground);

		TextMenuItem selectDrawText = new TextMenuItem(DRAW_METHOD, this.mFont, "Select Isometric Drawing Method", this.getVertexBufferObjectManager());
		final IMenuItem SelectDrawItem = new ColorMenuItemDecorator(selectDrawText,selected , unselected);
		SelectDrawItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		TextMenuItem selectTMXISOText = new TextMenuItem(SELECT_TMX_MAP_ISO, this.mFont, "Select Isometric Map", this.getVertexBufferObjectManager());
		final IMenuItem SelectTMXMapIsoItem = new ColorMenuItemDecorator(selectTMXISOText,selected , unselected);
		SelectTMXMapIsoItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem selectTMXOrthoText = new TextMenuItem(SELECT_TMX_MAP_ORTHO, this.mFont, "Select Orthographic Map", this.getVertexBufferObjectManager());
		final IMenuItem SelectTMXMapOrthoItem = new ColorMenuItemDecorator(selectTMXOrthoText,selected , unselected);
		SelectTMXMapOrthoItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem enableTileHitText = new TextMenuItem(SELECT_TILE_HIT, this.mFont, "Tile touch hits options", this.getVertexBufferObjectManager());
		final IMenuItem EnableTileHitItem = new ColorMenuItemDecorator(enableTileHitText,selected , unselected);
		EnableTileHitItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem RemoveText = new TextMenuItem(REMOVE_LINES, this.mFont, "Remove lines drawn", this.getVertexBufferObjectManager());
		final IMenuItem RemoveItem = new ColorMenuItemDecorator(RemoveText,selected , unselected);
		RemoveItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem CameraResetText = new TextMenuItem(RESET_CAMERA, this.mFont, "Reset Camera", this.getVertexBufferObjectManager());
		final IMenuItem CameraResetItem = new ColorMenuItemDecorator(CameraResetText,selected , unselected);
		CameraResetItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem BackText = new TextMenuItem(BACK_TO_GAME, this.mFont, "Back to map", this.getVertexBufferObjectManager());
		final IMenuItem BackItem = new ColorMenuItemDecorator(BackText,selected , unselected);
		BackItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		masterMenu.addMenuItem(SelectDrawItem);
		masterMenu.addMenuItem(SelectTMXMapIsoItem);
		masterMenu.addMenuItem(SelectTMXMapOrthoItem);
		masterMenu.addMenuItem(EnableTileHitItem);
		masterMenu.addMenuItem(RemoveItem);
		masterMenu.addMenuItem(CameraResetItem);
		masterMenu.addMenuItem(BackItem);
		masterMenu.buildAnimations();
		masterMenu.setBackgroundEnabled(true);
		masterMenu.setOnMenuItemClickListener(this);
		return masterMenu;
	}

	public MenuScene create_tile_hit_menu(){
		this.log.i(8, "create_tile_hit_menu");
		MenuScene hitTileMenu = new MenuScene(this.getEngine().getCamera());
		hitTileMenu.setBackground(this.mMenuBackground);

		if(!this.ENABLE_TILE_HIT){
			TextMenuItem tile_enable_Text = new TextMenuItem(TILE_ENABLE_DISABLE, this.mFont1, "Enable", this.getVertexBufferObjectManager());
			final IMenuItem Tile_Enable_Item = new ColorMenuItemDecorator(tile_enable_Text,selected , unselected);
			Tile_Enable_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			hitTileMenu.addMenuItem(Tile_Enable_Item);
		}else{
			TextMenuItem tile_enable_Text = new TextMenuItem(TILE_ENABLE_DISABLE, this.mFont1, "Disable", this.getVertexBufferObjectManager());
			final IMenuItem Tile_Enable_Item = new ColorMenuItemDecorator(tile_enable_Text,selected , unselected);
			Tile_Enable_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			hitTileMenu.addMenuItem(Tile_Enable_Item);
		}

		TextMenuItem hit_colour_Text = new TextMenuItem(TILE_SELECT_COLOUR, this.mFont1, "Hit colour", this.getVertexBufferObjectManager());
		final IMenuItem hit_colour_Item = new ColorMenuItemDecorator(hit_colour_Text,selected , unselected);
		hit_colour_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem tile_centre_Text = new TextMenuItem(TILE_HIT_CENTRE, this.mFont1, "Tile Centre", this.getVertexBufferObjectManager());
		final IMenuItem Tile_CentreP_Item = new ColorMenuItemDecorator(tile_centre_Text,selected , unselected);
		Tile_CentreP_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem tile_point_Text = new TextMenuItem(TILE_HIT_POINT, this.mFont1, "Tile Point", this.getVertexBufferObjectManager());
		final IMenuItem Tile_point_Item = new ColorMenuItemDecorator(tile_point_Text,selected , unselected);
		Tile_point_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem hit_layer_Text = new TextMenuItem(TILE_SELECT_LAYER, this.mFont1, "Select layer", this.getVertexBufferObjectManager());
		final IMenuItem hit_layer_Item = new ColorMenuItemDecorator(hit_layer_Text,selected , unselected);
		hit_layer_Text.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem back_Text = new TextMenuItem(TILE_HIT_BACK, this.mFont1, "Back", this.getVertexBufferObjectManager());
		final IMenuItem back_Item = new ColorMenuItemDecorator(back_Text,selected , unselected);
		back_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		hitTileMenu.addMenuItem(hit_colour_Item);
		hitTileMenu.addMenuItem(hit_layer_Item);
		hitTileMenu.addMenuItem(Tile_CentreP_Item);
		hitTileMenu.addMenuItem(Tile_point_Item);
		hitTileMenu.addMenuItem(back_Item);
		hitTileMenu.buildAnimations();
		hitTileMenu.setBackgroundEnabled(true);
		hitTileMenu.setOnMenuItemClickListener(this);

		return hitTileMenu;
	}

	public MenuScene create_layer_selection_menu(){
		this.log.i(8, "create_layer_selection_menu");
		MenuScene layerSelectionScene = new MenuScene(this.getEngine().getCamera());
		layerSelectionScene.setBackground(this.mMenuBackground);

		ArrayList<TMXLayer> layers = this.mMap.getTMXLayers();
		for (TMXLayer tmxLayer : layers) {
			TextMenuItem layer_Text = new TextMenuItem(LAYER_SELECTION_ENABLED, this.mFont2, tmxLayer.getName(), this.getVertexBufferObjectManager());
			final IMenuItem layer_item = new ColorMenuItemDecorator(layer_Text,selected , unselected);
			layer_item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			int layer_id = tmxLayer.getIndex();
			layer_item.setUserData(layer_id);
			layerSelectionScene.addMenuItem(layer_item);
		}

		layerSelectionScene.buildAnimations();
		layerSelectionScene.setBackgroundEnabled(true);
		layerSelectionScene.setOnMenuItemClickListener(this);
		return layerSelectionScene;
	}
	
	public MenuScene create_drawing_method_menu(){
		this.log.i(8, "create_drawing_method_menu");
		MenuScene drawingMethodScene = new MenuScene(this.getEngine().getCamera());
		drawingMethodScene.setBackground(this.mMenuBackground);

		TextMenuItem selectAllText = new TextMenuItem(DRAW_METHOD_SELECTED, this.mFont6, "DRAW_METHOD_ISOMETRIC_ALL", this.getVertexBufferObjectManager());
		final IMenuItem SelectAllItem = new ColorMenuItemDecorator(selectAllText,selected , unselected);
		SelectAllItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		SelectAllItem.setUserData(TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_ALL);
		
		TextMenuItem selectSlimText = new TextMenuItem(DRAW_METHOD_SELECTED, this.mFont6, "DRAW_METHOD_ISOMETRIC_CULLING_SLIM ", this.getVertexBufferObjectManager());
		final IMenuItem SelectSlimItem = new ColorMenuItemDecorator(selectSlimText,selected , unselected);
		SelectSlimItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		SelectSlimItem.setUserData(TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_CULLING_SLIM);
		
		TextMenuItem selectPaddingText = new TextMenuItem(DRAW_METHOD_SELECTED, this.mFont6, "DRAW_METHOD_ISOMETRIC_CULLING_PADDING", this.getVertexBufferObjectManager());
		final IMenuItem SelectPaddingItem = new ColorMenuItemDecorator(selectPaddingText,selected , unselected);
		SelectPaddingItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		SelectPaddingItem.setUserData(TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_CULLING_PADDING);
		
		TextMenuItem selectTiledText = new TextMenuItem(DRAW_METHOD_SELECTED, this.mFont6, "DRAW_METHOD_ISOMETRIC_CULLING_TILED_SOURCE", this.getVertexBufferObjectManager());
		final IMenuItem SelectTiledItem = new ColorMenuItemDecorator(selectTiledText,selected , unselected);
		SelectTiledItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		SelectTiledItem.setUserData(TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_CULLING_TILED_SOURCE);

		TextMenuItem selectBackText = new TextMenuItem(DRAW_METHOD_BACK, this.mFont6, "Back", this.getVertexBufferObjectManager());
		final IMenuItem SelectBackItem = new ColorMenuItemDecorator(selectBackText,selected , unselected);
		SelectBackItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		drawingMethodScene.addMenuItem(SelectAllItem);
		drawingMethodScene.addMenuItem(SelectSlimItem);
		drawingMethodScene.addMenuItem(SelectPaddingItem);
		drawingMethodScene.addMenuItem(SelectTiledItem);
		drawingMethodScene.addMenuItem(SelectBackItem);
		drawingMethodScene.buildAnimations();
		drawingMethodScene.setBackgroundEnabled(true);
		drawingMethodScene.setOnMenuItemClickListener(this);
		return drawingMethodScene;
	}

	public MenuScene create_colour_selection_menu(){
		this.log.i(8, "create_colour_selection_menu");
		MenuScene colourSceneMenu = new MenuScene(this.getEngine().getCamera());
		colourSceneMenu.setBackground(this.mMenuBackground);

		TextMenuItem blue_Text = new TextMenuItem(COLOUR_BLUE, this.mFont3, "Blue", this.getVertexBufferObjectManager());
		final IMenuItem blue_Item = new ColorMenuItemDecorator(blue_Text,selected , unselected);
		blue_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		colourSceneMenu.addMenuItem(blue_Item);

		TextMenuItem green_Text = new TextMenuItem(COLOUR_GREEN, this.mFont3, "Green", this.getVertexBufferObjectManager());
		final IMenuItem green_Item = new ColorMenuItemDecorator(green_Text,selected , unselected);
		green_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		colourSceneMenu.addMenuItem(green_Item);

		TextMenuItem red_Text = new TextMenuItem(COLOUR_RED, this.mFont3, "Red", this.getVertexBufferObjectManager());
		final IMenuItem red_Item = new ColorMenuItemDecorator(red_Text,selected , unselected);
		red_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		colourSceneMenu.addMenuItem(red_Item);

		TextMenuItem yellow_Text = new TextMenuItem(COLOUR_YELLOW, this.mFont3, "Yellow", this.getVertexBufferObjectManager());
		final IMenuItem yellow_Item = new ColorMenuItemDecorator(yellow_Text,selected , unselected);
		yellow_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		colourSceneMenu.addMenuItem(yellow_Item);

		colourSceneMenu.buildAnimations();
		colourSceneMenu.setBackgroundEnabled(true);
		colourSceneMenu.setOnMenuItemClickListener(this);
		return colourSceneMenu;
	}

	public MenuScene create_TMX_selection_isometric(){
		this.log.i(8, "create_TMX_selection_isometric");
		MenuScene TMXSelectionMenu = new MenuScene(this.getEngine().getCamera());
		TMXSelectionMenu.setBackground(this.mMenuBackground);

		for (int i = 0; i < this.TMXFilesIsometric.length; i++){
			TextMenuItem mapText = new TextMenuItem(MAP_SELECTED_ISO, this.mFont4, this.TMXFilesIsometric[i], this.getVertexBufferObjectManager());
			final IMenuItem mapItem = new ColorMenuItemDecorator(mapText,selected , unselected);
			mapItem.setUserData(i);
			mapItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			TMXSelectionMenu.addMenuItem(mapItem);
		}
		TMXSelectionMenu.buildAnimations();
		TMXSelectionMenu.setBackgroundEnabled(true);
		TMXSelectionMenu.setOnMenuItemClickListener(this);
		return TMXSelectionMenu;
	}

	public MenuScene create_TMX_selection_orthographic(){
		this.log.i(8, "create_TMX_selection_orthographic");
		MenuScene TMXSelectionMenu = new MenuScene(this.getEngine().getCamera());
		TMXSelectionMenu.setBackground(this.mMenuBackground);

		for (int i = 0; i < this.TMXFilesOrthographic.length; i++){
			TextMenuItem mapText = new TextMenuItem(MAP_SELECTED_ORTHO, this.mFont4, this.TMXFilesOrthographic[i], this.getVertexBufferObjectManager());
			final IMenuItem mapItem = new ColorMenuItemDecorator(mapText,selected , unselected);
			mapItem.setUserData(i);
			mapItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			TMXSelectionMenu.addMenuItem(mapItem);
		}
		TMXSelectionMenu.buildAnimations();
		TMXSelectionMenu.setBackgroundEnabled(true);
		TMXSelectionMenu.setOnMenuItemClickListener(this);
		return TMXSelectionMenu;
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
		case SELECT_TILE_HIT:
			this.mSceneMasterMenu.setChildSceneModal(this.mSceneTileHitMenu);
			return true;
		case REMOVE_LINES:
			this.removeLines();
			return true;
		case BACK_TO_GAME:
			this.mSceneMasterMenu.back();
			return true;
		case LAYER_SELECTION_ENABLED:
			SELECTION_LAYER = (Integer) pMenuItem.getUserData();
			this.currentLayer = this.mMap.getTMXLayers().get(SELECTION_LAYER);
			this.mSceneLayerMenu.back();
			return true;
		case COLOUR_GREEN:
			SELECTION_COLOUR = COLOUR_GREEN;
			this.mSceneColourMenu.back();
			return true;
		case COLOUR_BLUE:
			SELECTION_COLOUR = COLOUR_BLUE;
			this.mSceneColourMenu.back();
			return true;
		case COLOUR_RED:
			SELECTION_COLOUR = COLOUR_RED;
			this.mSceneColourMenu.back();
			return true;
		case COLOUR_YELLOW:
			SELECTION_COLOUR = COLOUR_YELLOW;
			this.mSceneColourMenu.back();
			return true;
		case SELECT_TMX_MAP_ISO:
			this.mSceneMasterMenu.setChildSceneModal(this.mSceneTMXIsoMenu);
			return true;
		case SELECT_TMX_MAP_ORTHO:
			this.mSceneMasterMenu.setChildSceneModal(this.mSceneTMXORthoMenu);
			return true;
		case RESET_CAMERA:
			this.resetCamera();
			return true;
		case MAP_SELECTED_ISO:
			this.SELECTED_TMX_MAP_ISO_ORTHO = true;
			SELECTED_TMX_MAP =(Integer) pMenuItem.getUserData();
			this.detatchMap();
			this.loadMap(SELECTED_TMX_MAP);
			this.mSceneTMXIsoMenu.back();
			return true;
		case MAP_SELECTED_ORTHO:
			this.SELECTED_TMX_MAP_ISO_ORTHO = false;
			SELECTED_TMX_MAP =(Integer) pMenuItem.getUserData();
			this.detatchMap();
			this.loadMap(SELECTED_TMX_MAP);
			this.mSceneTMXORthoMenu.back();
			return true;
		case TILE_ENABLE_DISABLE:
			if(!this.ENABLE_TILE_HIT){
				this.ENABLE_TILE_HIT = true;
			}else{
				this.ENABLE_TILE_HIT = false;
			}
			this.mSceneTileHitMenu.back();
			this.mSceneTileHitMenu = create_tile_hit_menu();
			return true;
		case TILE_SELECT_LAYER:
			pMenuScene.setChildSceneModal(this.mSceneLayerMenu);
			return true;
		case TILE_SELECT_COLOUR:
			pMenuScene.setChildSceneModal(this.mSceneColourMenu);
			return true;
		case TILE_HIT_CENTRE:
			SELECTION_TILE_HIT = TILE_HIT_CENTRE;
			return true;
		case TILE_HIT_POINT:
			SELECTION_TILE_HIT = TILE_HIT_POINT;
			return true;
		case TILE_HIT_BACK:
			this.mSceneTileHitMenu.back();
			return true;
		case DRAW_METHOD:
			this.mSceneMasterMenu.setChildSceneModal(this.mSceneDrawingMethod);
			return true;
		case DRAW_METHOD_BACK:
			this.mSceneDrawingMethod.back();
			return true;
		case DRAW_METHOD_SELECTED:
			SELECTED_DRAW_METHOD = (Integer) pMenuItem.getUserData();
			this.setDrawingMethod(SELECTED_DRAW_METHOD);
			this.mSceneDrawingMethod.back();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onTMXTileWithPropertiesCreated(TMXTiledMap pTMXTiledMap,
			TMXLayer pTMXLayer, TMXTile pTMXTile,
			TMXProperties<TMXTileProperty> pTMXTileProperties) {
		this.log.i(9,"onTMXTileWithPropertiesCreated");
	}

}