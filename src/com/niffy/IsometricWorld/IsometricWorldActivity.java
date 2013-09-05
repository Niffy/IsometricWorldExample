package com.niffy.IsometricWorld;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepMaxFPSEngine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.fragments.compatibility.LayoutGameFragment;
import org.andengine.util.texturepack.TexturePack;
import org.andengine.util.texturepack.TexturePackLoader;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;
import org.andengine.util.texturepack.exception.TexturePackParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;

import com.niffy.AndEngineLockStepEngine.misc.IHandlerMessage;
import com.niffy.AndEngineLockStepEngine.misc.WeakThreadHandler;
import com.niffy.AndEngineLockStepEngine.options.BaseOptions;
import com.niffy.AndEngineLockStepEngine.options.IBaseOptions;
import com.niffy.IsometricWorld.entity.CubeTemplate;
import com.niffy.IsometricWorld.entity.HumanManager;
import com.niffy.IsometricWorld.fragments.FragmentBuild;
import com.niffy.IsometricWorld.fragments.FragmentHuman;
import com.niffy.IsometricWorld.fragments.FragmentMultiplayerRole;
import com.niffy.IsometricWorld.fragments.FragmentNetwork;
import com.niffy.IsometricWorld.fragments.NetworkStatusDialog;
import com.niffy.IsometricWorld.network.NetworkManager;
import com.niffy.IsometricWorld.touch.ITouchManager;
import com.niffy.IsometricWorld.touch.TouchManager;

public class IsometricWorldActivity extends LayoutGameFragment implements IOnSceneTouchListener,
		IScrollDetectorListener, IPinchZoomDetectorListener, IHandlerMessage {

	private final Logger log = LoggerFactory.getLogger(IsometricWorldActivity.class);

	public int CAMERA_WIDTH = 720;
	public int CAMERA_HEIGHT = 480;
	public ZoomCamera mCamera;
	public ScrollDetector mScrollDetector;
	public PinchZoomDetector mPinchZoomDetector;
	public float mPinchZoomStartedCameraZoomFactor;
	private float maxZoom = 0;
	private float zoomDepth = 2;
	private boolean mClicked = false;

	public FixedStepMaxFPSEngine engine;
	public int update_per_second = 1;
	public ITouchManager mTouchManager;

	public FragmentBuild mBuildFragment;
	public FragmentHuman mHumanFragment;
	public FragmentNetwork mNetworkFragment;
	public FragmentMultiplayerRole mNetworkRoleDialog;
	public NetworkStatusDialog mNetworkStatusDialog;
	public GeneralManager mGeneralManager;
	public MapHandler mMapHandler;
	public HumanManager mHumanManager;

	public String TMXAssetsLocation = "tmx/";
	public String TMXFileTag = ".tmx";
	public String TMXFile = "isometricBlocks";
	public String mFullMapPath = this.TMXAssetsLocation + TMXFile + TMXFileTag;

	public ArrayList<CubeTemplate> mData;
	public HashMap<String, ITextureRegion> mTextures;
	public TexturePackTextureRegionLibrary mSpritesheetTexturePackTextureRegionLibrary;

	public TexturePack mSpritesheetTexturePack;
	private String mFolder = "spritesheet/";
	private String mDataFile = "cubes.xml";
	private String mGraphicsLocation = "gfx/";
	/**
	 * Something we can access in an inner class
	 */
	private IHandlerMessage mHandlerMessage;
	public WeakThreadHandler<IHandlerMessage> mHander;
	public NetworkManager mNetworkManager;
	public IBaseOptions mBaseOptions;

	@Override
	public void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		ConfigureLog.configure("isoWorld.txt", 10, 10000, 5 * 1024 * 1024);
		log.info("Starting game!");
		/*
		 * We have mHandlerMessage so we can access it in the runnable about to be created.
		 * We create it on the UI thread as that has a looper, as Andengine is a GLThread 
		 * that doesn't have a looper therefore messages cannot be processed
		 */
		this.mHandlerMessage = this;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mHander = new WeakThreadHandler<IHandlerMessage>(mHandlerMessage);
			}
		});
	}

	@Override
	protected synchronized void onCreateGame() {
		super.onCreateGame();
		log.debug("onCreateGame");
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		log.debug("onResume");
	}

	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();
		log.debug("onResumeGame");
	}

	@Override
	public void onReloadResources() {
		super.onReloadResources();
		log.debug("onReloadResources");
	}

	@Override
	public void onPause() {
		super.onPause();
		log.debug("onPause");
	}

	@Override
	public synchronized void onPauseGame() {
		super.onPauseGame();
		log.debug("onPauseGame");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		log.debug("onDestroy");
	}

	@Override
	public void onDestroyResources() {
		super.onDestroyResources();
		log.debug("onDestroyResources");
	}

	@Override
	public synchronized void onGameDestroyed() {
		super.onGameDestroyed();
		log.debug("onGameDestroyed");
	}

	protected int getLayoutID() {
		return R.layout.main;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.rendersurfaceview;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		CAMERA_WIDTH = displayMetrics.widthPixels;
		CAMERA_HEIGHT = displayMetrics.heightPixels;
		this.mCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mCamera.setZoomFactor(this.zoomDepth);
		EngineOptions eOps = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				this.mCamera);
		return eOps;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		this.engine = new FixedStepMaxFPSEngine(pEngineOptions, this.update_per_second);
		return this.engine;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		this.mMapHandler = new MapHandler(this);
		this.mGeneralManager = new GeneralManager(this, this.mMapHandler);
		this.mHumanManager = new HumanManager(this, this.mGeneralManager);

		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);
		this.mTouchManager = new TouchManager();
		try {
			this.zoomDepth = getResources().getInteger(R.integer.zoomDepth);
			this.maxZoom = getResources().getInteger(R.integer.zoomMax);
		} catch (NotFoundException e) {
			this.zoomDepth = 2;
			this.maxZoom = 2;
		}

		try {
			TexturePackLoader pTexturePackLoader = new TexturePackLoader(this.getAssets(), this.getEngine()
					.getTextureManager());
			String pAssetPath = this.mFolder + this.mDataFile;
			String pAssetBasePath = this.mGraphicsLocation;

			mSpritesheetTexturePack = pTexturePackLoader.loadFromAsset(pAssetPath, pAssetBasePath);
			mSpritesheetTexturePack.loadTexture();
			mSpritesheetTexturePackTextureRegionLibrary = mSpritesheetTexturePack.getTexturePackTextureRegionLibrary();
			this.produceTemplates();
		} catch (final TexturePackParseException e) {
			log.error("Error reading in texture pack", e);
		}

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		Scene mScene = new Scene();
		final FPSLogger fpsLogger = new FPSLogger();
		this.getEngine().registerUpdateHandler(fpsLogger);
		mScene.setBackground(new Background(0.6509f, 0.8156f, 0.7764f));
		mScene.setTimeModifedUpdater(this.getEngine());
		mScene.setIsometricToSort(true);
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		pScene.setOnSceneTouchListener(this);
		pScene.setTouchAreaBindingOnActionMoveEnabled(true);
		pScene.setOnAreaTouchTraversalFrontToBack();
		this.mBuildFragment = (FragmentBuild) this.getSupportFragmentManager().findFragmentById(
				R.id.fragment_topbar_build_object);
		this.mHumanFragment = (FragmentHuman) this.getSupportFragmentManager().findFragmentById(
				R.id.fragment_topbar_build_human);
		this.mNetworkFragment = (FragmentNetwork) this.getSupportFragmentManager().findFragmentById(
				R.id.fragment_topbar_network);
		this.mBuildFragment.setParent(this);
		this.mHumanFragment.setParent(this);
		this.mNetworkFragment.setParent(this);
		this.mHumanFragment.setGeneralManager(this.mGeneralManager);
		this.mMapHandler.loadMap_Iso(this.mFullMapPath);
		this.mHumanManager.setMapHandler(this.mMapHandler);
		this.mNetworkRoleDialog = new FragmentMultiplayerRole(this.mGeneralManager);
		this.mGeneralManager.setNetworkRoleDialog(this.mNetworkRoleDialog);
		this.mNetworkStatusDialog = new NetworkStatusDialog(this);
		this.mGeneralManager.setNetworkStatusDialog(this.mNetworkStatusDialog);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mCamera.getZoomFactor();
		this.mClicked = false;
	}

	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
		this.mCamera.setZoomFactor(Math.min(
				Math.max(this.maxZoom, this.mPinchZoomStartedCameraZoomFactor * pZoomFactor), this.zoomDepth));
		this.mClicked = false;
	}

	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
		/*
		 * We could have this, but it would only have to mirror onPinchZoom
		 * so why add it?
		 * 
		 */
		// this.mCamera.setZoomFactor(Math.min(Math.max(this.maxZoom,
		// this.mPinchZoomStartedCameraZoomFactor*
		// pZoomFactor),this.zoomDepth));
		this.mClicked = false;
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		final float zoomFactor = mCamera.getZoomFactor();
		float xLocation = -pDistanceX / zoomFactor;
		float yLocation = +pDistanceY / zoomFactor;
		mCamera.offsetCenter(xLocation, yLocation);
		this.mClicked = false;
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (!this.engine.isPaused()) {
			if (this.mPinchZoomDetector != null) {
				this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);
				if (this.mPinchZoomDetector.isZooming()) {
					this.mScrollDetector.setEnabled(false);
				} else {
					if (pSceneTouchEvent.isActionDown()) {
						this.mScrollDetector.setEnabled(true);
					}
					this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
				}
			} else {
				this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
			}
			if (pSceneTouchEvent.isActionUp()) {
				if (this.mClicked) {
					log.info("Scene touch");
					final float[] pToTiles = this
							.getEngine()
							.getScene()
							.convertLocalCoordinatesToSceneCoordinates(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					int[] alt = this.mMapHandler.getTMXLayer().getRowColAtIsometric(pToTiles);
					int[] found = this.mMapHandler.getTileAt(pToTiles);
					if (found != null) {
						final float[] pCen = this.mMapHandler.getTileCentre(found);
						log.info("TileTouch: X: {} Y:{}", pCen[0], pCen[1]);
						log.info("Tile R: {} C: {}", found[0], found[1]);
						log.info("Alt R: {} C: {}", alt[0], alt[1]);
					}
					this.mTouchManager.onSceneTouchEvent(pSceneTouchEvent, pSceneTouchEvent.getX(),
							pSceneTouchEvent.getY());
				}
				this.mClicked = true;
			}
		}
		return true;
	}

	@Override
	public synchronized void onGameCreated() {
		super.onGameCreated();
		this.produceBaseOptions();
		try {
			this.mNetworkManager = new NetworkManager(this, this.mBaseOptions, this.mNetworkStatusDialog);
			this.mGeneralManager.setNetworkManager(this.mNetworkManager);
			this.mNetworkManager.createThreads();
		} catch (UnknownHostException e) {
			log.error("could not create network manager", e);
		}
	}

	public void resetCamera() {
		this.mCamera.setZoomFactor(this.zoomDepth);
		this.mCamera.setCenter(0, 0);
	}

	/**
	 * Setup the Camera if the map is isometric.
	 * 
	 * @param height
	 *            {@link Float} overall height of the map
	 * @param width
	 *            {@link Float} overall width of the map
	 */
	public void setupCameraIsometric(final float height, final float width) {
		if (this.CAMERA_WIDTH / height >= this.CAMERA_HEIGHT / width) {
			this.maxZoom = this.CAMERA_WIDTH / height;
		} else {
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
		final float halfTileWidth = this.mMapHandler.getTMXTiledMap().getTileWidth() / 2;
		final float xMin = this.mMapHandler.getTMXTiledMap().getTileRows() * halfTileWidth;
		final float xMax = this.mMapHandler.getTMXTiledMap().getTileColumns() * halfTileWidth;
		final float pBoundsXMin = halfTileWidth - xMin - MAX_CAMERA_BOUND_ADDITION;
		final float pBoundsYMin = -height + MAX_CAMERA_BOUND_ADDITION;
		final float pBoundsXMax = halfTileWidth + xMax + MAX_CAMERA_BOUND_ADDITION;
		final float pBoundsYMax = MAX_CAMERA_BOUND_ADDITION;
		this.mCamera.setBounds(pBoundsXMin, pBoundsYMin, pBoundsXMax, pBoundsYMax);
		this.mCamera.setBoundsEnabled(true);
		this.resetCamera();

	}

	public TiledTextureRegion getTextureTiled(String pSource, int pRows, int pCols) {
		ITextureRegion txm = this.mSpritesheetTexturePackTextureRegionLibrary.get(pSource);
		TiledTextureRegion found = TiledTextureRegion.create(this.mSpritesheetTexturePack.getTexture(),
				(int) txm.getTextureX(), (int) txm.getTextureY(), (int) txm.getWidth(), (int) txm.getHeight(), pCols,
				pRows);
		return found;
	}

	public void produceTemplates() {
		this.mTextures = new HashMap<String, ITextureRegion>();
		CubeTemplate one = new CubeTemplate("32x32x32.png");
		one.setWLH(32, 32, 32);
		one.setmYOffset(17);
		one.setmXOffset(0);
		one.setTileColsBlocked(1);
		one.setTileRowsBlocked(1);
		one.setDrawable(this.getResources().getDrawable(R.drawable.a32_32_32));
		this.mTextures
				.put(one.getmFileName(), this.mSpritesheetTexturePackTextureRegionLibrary.get(one.getmFileName()));

		CubeTemplate two = new CubeTemplate("32x32x64.png");
		two.setWLH(32, 32, 64);
		two.setmYOffset(32);
		two.setmXOffset(0);
		two.setTileColsBlocked(1);
		two.setTileRowsBlocked(1);
		two.setDrawable(this.getResources().getDrawable(R.drawable.a32_32_64));
		this.mTextures
				.put(two.getmFileName(), this.mSpritesheetTexturePackTextureRegionLibrary.get(two.getmFileName()));

		CubeTemplate three = new CubeTemplate("32x64x32.png");
		three.setWLH(32, 64, 32);
		three.setmYOffset(9);
		three.setmXOffset(-16);
		three.setTileColsBlocked(1);
		three.setTileRowsBlocked(2);
		three.setDrawable(this.getResources().getDrawable(R.drawable.a32_64_32));
		this.mTextures.put(three.getmFileName(),
				this.mSpritesheetTexturePackTextureRegionLibrary.get(three.getmFileName()));

		CubeTemplate four = new CubeTemplate("32x128x32.png");
		four.setWLH(32, 128, 32);
		four.setmYOffset(0);
		four.setmXOffset(-32);
		four.setTileColsBlocked(1);
		four.setTileRowsBlocked(3);
		four.setDrawable(this.getResources().getDrawable(R.drawable.a32_128_32));
		this.mTextures.put(four.getmFileName(),
				this.mSpritesheetTexturePackTextureRegionLibrary.get(four.getmFileName()));

		CubeTemplate five = new CubeTemplate("64x32x32.png");
		five.setWLH(64, 32, 32);
		five.setmYOffset(8);
		five.setmXOffset(16);
		five.setTileColsBlocked(2);
		five.setTileRowsBlocked(1);
		five.setDrawable(this.getResources().getDrawable(R.drawable.a64_32_32));
		this.mTextures.put(five.getmFileName(),
				this.mSpritesheetTexturePackTextureRegionLibrary.get(five.getmFileName()));

		CubeTemplate six = new CubeTemplate("64x64x32.png");
		six.setWLH(64, 64, 32);
		six.setmYOffset(1);
		six.setmXOffset(0);
		six.setTileColsBlocked(2);
		six.setTileRowsBlocked(2);
		six.setDrawable(this.getResources().getDrawable(R.drawable.a64_64_32));
		this.mTextures
				.put(six.getmFileName(), this.mSpritesheetTexturePackTextureRegionLibrary.get(six.getmFileName()));

		CubeTemplate seven = new CubeTemplate("128x32x32.png");
		seven.setWLH(128, 32, 32);
		seven.setmYOffset(0);
		seven.setmXOffset(32);
		seven.setTileColsBlocked(3);
		seven.setTileRowsBlocked(1);
		seven.setDrawable(this.getResources().getDrawable(R.drawable.a128_32_32));
		this.mTextures.put(seven.getmFileName(),
				this.mSpritesheetTexturePackTextureRegionLibrary.get(seven.getmFileName()));

		this.mData = new ArrayList<CubeTemplate>();
		this.mData.add(one);
		this.mData.add(two);
		this.mData.add(three);
		this.mData.add(four);
		this.mData.add(five);
		this.mData.add(six);
		this.mData.add(seven);
	}

	public void produceBaseOptions() {
		this.mBaseOptions = new BaseOptions();
		this.mBaseOptions.setAckWindowSize(100);
		this.mBaseOptions.setClientName("Player!");
		this.mBaseOptions.setNetworkBufferSize(512);
		this.mBaseOptions.setPingRTT(0); /* TODO */
		this.mBaseOptions.setStandardTickLength(100); /* TODO in milliseconds? */
		this.mBaseOptions.setStepsBeforeCrisis(2);
		this.mBaseOptions.setTCPClientPort(1777);
		this.mBaseOptions.setTCPServerPort(1778);
		this.mBaseOptions.setUDPPort(1779);
		this.mBaseOptions.setVersionNumber(0);/* TODO game version*/
		this.mBaseOptions.setCountdown(5);
	}

	@Override
	public void handlePassedMessage(Message pMessage) {
		log.debug("Handle message: {}", pMessage.what);
		if (this.mNetworkManager != null) {
			this.mNetworkManager.handlePassedMessage(pMessage);
		}

	}
}