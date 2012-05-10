package com.niffy.TMXIsometricExample;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
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
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLayerObjectTiles;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
/**
 * All gfx (apart from the isometric_grass_and_water tileset) have been created by me.
 * MFX Zap sound by JimPurbrick used under the 
 * http://creativecommons.org/licenses/by/3.0/ 
 * url to download
 * http://www.freesound.org/people/JimPurbrick/sounds/11152/
 * 
 * @author Paul Robinson
 *
 */
public class TMXIsometricExampleActivity extends BaseGameActivity implements
IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener,
ITMXTilePropertiesListener, IOnMenuItemClickListener, MenuConstants{

	//TODO implement drawing poly points
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

	//menu scenes + colour for menus
	protected Background mMenuBackground = new Background(1f, 1f, 1f, 0.35f);
	protected MenuScene mSceneMasterMenu;
	protected MenuScene mSceneObjectMenu;
	protected MenuScene mSceneObjectLayerMenu;
	protected MenuScene mSceneTileHitMenu;
	protected MenuScene mSceneLayerMenu;
	protected MenuScene mSceneColourMenu;
	protected MenuScene mSceneSelectMap;
	protected MenuScene mSceneTMXIsoMenu;
	protected MenuScene mSceneTMXORthoMenu;
	protected MenuScene mSceneDrawingMethod;

	org.andengine.util.color.Color selected = new org.andengine.util.color.Color(1f, 0f, 0f);
	org.andengine.util.color.Color unselected = new org.andengine.util.color.Color(0f, 0f, 0f);
	org.andengine.util.color.Color objectLines = new org.andengine.util.color.Color(0.4823f, 0.8313f, 0.3254f);

	//fonts
	private String mFontFile = "font/Roboto-Condensed.ttf";
	private FontManager mFontManager;
	private MenuAttributes mMenuAttributes;
	private MenuDrawer mMenuDrawer;
	private MenuManager mMenuManager;
	private MapHandler mMapHandler;
	//The zap!
	private Sound mZap;
	private final String mSound = "11152__jimpurbrick__polysixslowinglaser1.wav";
	//Little friend..
	private Vibrator mLittleFriend;

	//Lines belong to tile hits, objects
	public ArrayList<Line> mDrawnLines = new ArrayList<Line>();
	public ArrayList<TMXLayerObjectTiles> mTileObject = new ArrayList<TMXLayerObjectTiles>();

	//TMXFiles
	public String TMXAssetsLocation = "tmx/";
	public String TMXFileTag = ".tmx";
	public String[] TMXFilesIsometric = 
		{ "isometric_grass_and_water", //0
			"Isometric_32x16_nooffset",  //1
			"Isometric_32x16_with_offset_x", //2
			"Isometric_32x16_with_offset_x_y_odd", //3
			"Isometric_32x16_with_offset_x_y_even", //4
			"Isometric_64_32_with_offset_y", //5
			"Isometric_64_32_with_offset_y_evensize", //6
			"isometricBlocks", //7
			"Large_isometricBlocks", //8
			"5x5Object",//9
			"Large_isometricBlocks_wObject" //10
		};

	public String[] TMXFilesOrthographic = { 
			"Ortho_1_32__32",  //0
			"Ortho_1_32__32_objects" //1
	};

	public boolean[][] TilesBlocked;

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
		eOps.getAudioOptions().setNeedsSound(true);
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
		this.mMenuAttributes = new MenuAttributes();
		this.mFontManager = new FontManager(this, this.mFontFile);
		this.mMenuDrawer = new MenuDrawer(this, this.mMenuBackground, this.mFontManager, this.mMenuAttributes);
		this.mMapHandler = new MapHandler(this, this.mMenuDrawer, this.mMenuAttributes);
		this.mMenuManager = new MenuManager(this, this.mMenuDrawer, this.mMenuAttributes, this.mMapHandler);

		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);
		this.mLittleFriend = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.mZap = SoundFactory.createSoundFromAsset(this.getEngine().getSoundManager(), this, this.mSound);
		} catch (final IOException e) {
			Debug.e(e);
		}

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
		this.mSceneMasterMenu = this.mMenuDrawer.create_master_menu();
		this.mSceneObjectMenu = this.mMenuDrawer.create_object_menu();
		this.mSceneTileHitMenu = this.mMenuDrawer.create_tile_hit_menu(); 
		this.mSceneColourMenu = this.mMenuDrawer.create_colour_selection_menu();
		this.mSceneSelectMap = this.mMenuDrawer.create_map_type_selection();
		this.mSceneTMXIsoMenu = this.mMenuDrawer.create_TMX_selection_isometric();
		this.mSceneTMXORthoMenu = this.mMenuDrawer.create_TMX_selection_orthographic();
		this.mSceneDrawingMethod = this.mMenuDrawer.create_drawing_method_menu();

		this.mHUD = new HUD();
		this.getEngine().getCamera().setHUD(this.mHUD);
		this.mFPS = new Text(0, 0, this.mFontManager.Font_HUD, "FPS:","FPS: XXXXXXXXXXXXXXXXX".length(), this.getVertexBufferObjectManager());
		this.mXYLoc = new Text(0, this.mFPS.getY() +1 + this.mFPS.getFont().getLineHeight(), this.mFontManager.Font_HUD, "Touch X: Y:","Touch X: XXXXXXXXXXXX Y: XXXXXXXXXX".length(), this.getVertexBufferObjectManager());
		this.mTileRowCol1 = new Text(0, this.mXYLoc.getY() +1 + this.mXYLoc.getFont().getLineHeight(), this.mFontManager.Font_HUD, "Row: Col:","Row: Not in Bounds Col: Not in Bounds".length(), this.getVertexBufferObjectManager());
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
		this.mMapHandler.loadMap(this.mMenuAttributes.SELECTED_TMX_MAP);
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




	public void touchMap(final float pX, final float pY){
		//Standard method of getting tile
		final float[] pToTiles = this.getEngine().getScene().convertLocalToSceneCoordinates(pX, pY);
		this.currentLayer = this.mMap.getTMXLayers().get(this.mMenuAttributes.SELECTION_LAYER);
		final TMXTile tmxSelected = this.currentLayer.getTMXTileAt(pToTiles[0], pToTiles[1]);
		if(tmxSelected != null){
			this.log.i(6, String.format("Standard getTMXTileAt - tile found Row: %d Column %d ", tmxSelected.getTileRow(), tmxSelected.getTileColumn()));
			this.mTileRowCol1.setText(String.format("Row: %d Col: %d", tmxSelected.getTileRow(), tmxSelected.getTileColumn()));
		}else{
			this.mTileRowCol1.setText(String.format("Row: %s Col: %s", "Not in Bounds" ,"Not in Bounds"));
		}
		this.mXYLoc.setText(String.format("Touch X: %f Y: %f", pX, pY));

		if(this.mMenuAttributes.SELECTED_TMX_MAP_ISO_ORTHO){
			//Alternative method
			TMXTile TMXTileIsoAlt = this.mMap.getTMXLayers().get(0).getTMXTileAtIsometricAlternative(pToTiles);
			if(TMXTileIsoAlt != null){
				this.log.i(6, String.format("Alternative getTMXTileAt - tile found: Row: %d Col: %d", TMXTileIsoAlt.getTileRow(), TMXTileIsoAlt.getTileColumn()));
			}
		}

		if(this.mMenuAttributes.ENABLE_TILE_HIT){
			if(tmxSelected !=null){
				this.drawIntersect(tmxSelected, pX, pY);
			}
		}
	}

	public void removeLines(){
		this.log.i(7, "removeLines");
		for (Line l : this.mDrawnLines) {
			l.detachSelf();
		}
		for(TMXLayerObjectTiles o : this.mTileObject){
			o.detachSelf();
		}
		this.mTileObject.clear();
		this.mDrawnLines.clear();
		this.clearBlockedTiles();
	}

	public void clearBlockedTiles(){
		final int tileColumns = this.mMap.getTileColumns();
		final int tileRows = this.mMap.getTileRows();
		for (int j = 0; j < tileRows; j++) {
			for (int i = 0; i < tileColumns; i++) {
				this.TilesBlocked[j][i] = false;
			}
		}
	}

	public void drawIntersect(TMXTile pTile, final float pX, final float pY){
		this.log.i(7, "drawIntersect");
		if(this.TilesBlocked[pTile.getTileRow()][pTile.getTileColumn()] == true){
			if(this.mMenuAttributes.TILE_HIT_SOUND_ENABLED){
				this.mZap.play();
			}
			if(this.mMenuAttributes.TILE_HIT_VIBRATE_ENABLED){
				this.mLittleFriend.vibrate(300);
			}
			return;
		}
		if(this.mMenuAttributes.SELECTION_TILE_HIT == TILE_HIT_CENTRE){
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
		if(this.mMenuAttributes.SELECTED_TMX_MAP_ISO_ORTHO){
			pX = pTile.getTileXIsoCentre();
			pY = pTile.getTileYIsoCentre();
		}else{
			pX = pTile.getTileX() + pWidth;
			pY = pTile.getTileY() + pHeight;
		}	
		if(this.mMenuAttributes.SELECTION_COLOUR == COLOUR_BLUE){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(0, 0, 1f));
		}else if(this.mMenuAttributes.SELECTION_COLOUR == COLOUR_GREEN){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(0, 1f, 0));
		}else if(this.mMenuAttributes.SELECTION_COLOUR == COLOUR_RED){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(1f, 0, 0));
		}else if(this.mMenuAttributes.SELECTION_COLOUR == COLOUR_YELLOW){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(1f, 1f, 0));
		}
	}

	public void drawIntersectPoint(TMXTile pTile,final float pX, final float pY){
		this.log.i(7, "drawIntersectPoint");
		float pWidth = pTile.getTileWidth() /2;
		float pHeight = pTile.getTileHeight() /2;
		if(this.mMenuAttributes.SELECTION_COLOUR == COLOUR_BLUE){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(0, 0, 1f));
		}else if(this.mMenuAttributes.SELECTION_COLOUR == COLOUR_GREEN){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(0, 1f, 0));
		}else if(this.mMenuAttributes.SELECTION_COLOUR == COLOUR_RED){
			this.drawHit(pX, pY, pWidth, pHeight, new Color(1f, 0, 0));
		}else if(this.mMenuAttributes.SELECTION_COLOUR == COLOUR_YELLOW){
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
		this.mDrawnLines.add(line1);
		this.mDrawnLines.add(line2);
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


	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		return this.mMenuManager.handle(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
	}

	@Override
	public void onTMXTileWithPropertiesCreated(TMXTiledMap pTMXTiledMap,
			TMXLayer pTMXLayer, TMXTile pTMXTile,
			TMXProperties<TMXTileProperty> pTMXTileProperties) {
		this.log.i(9,"onTMXTileWithPropertiesCreated");
	}

}