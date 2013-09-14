package com.niffy.IsometricWorld;

import java.util.ArrayList;
import java.util.List;

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
import org.andengine.opengl.util.GLState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.niffy.IsometricWorld.menu.MenuManager;
import com.niffy.IsometricWorld.touch.ITouchManager;
import com.niffy.IsometricWorld.touch.TouchManager;

public class MainActivity extends BaseFragmentActivity implements IOnSceneTouchListener, IScrollDetectorListener,
		IPinchZoomDetectorListener, OnInvitationReceivedListener, RoomUpdateListener, RoomStatusUpdateListener,
		RealTimeMessageReceivedListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(MainActivity.class);

	// ===========================================================
	// Fields
	// ===========================================================
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

	public MenuManager mMenuManager;
	public boolean mActivityResume = false;
	public boolean mSwapFragmentOnResume = true;
	public ArrayList<GameHelperListener> mSigninListeners = new ArrayList<GameHelperListener>();
	
    public Invitation mCurrentInvitation = null;
	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		String pStringKey = this.getResources().getString(R.string.settings_key_logging);
		String pLoggingLevel = prefs.getString(pStringKey, "10000");
		int pLevel = Integer.valueOf(pLoggingLevel);
		ConfigureLog.configure("IsometricWorld.log", 10, pLevel, 5 * 1024 * 1024);
		log.info("Starting game up");
		this.mMenuManager = new MenuManager(this.getSupportFragmentManager(), this);
		// this.mHelper.enableDebugLog(true, "bong");
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.rendersurfaceview;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces GameHelper.GameHelperListener
	// ===========================================================

	@Override
	public void onSignInFailed() {
		log.error("onSignInFailed");
		for (GameHelperListener listener : this.mSigninListeners) {
			listener.onSignInFailed();
		}
	}

	@Override
	public void onSignInSucceeded() {
		log.debug("onSignInSucceeded");
		this.getGamesClient().registerInvitationListener(this);
		for (GameHelperListener listener : this.mSigninListeners) {
			listener.onSignInSucceeded();
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces OnInvitationReceivedListener
	// ===========================================================

	@Override
	public void onInvitationReceived(Invitation invitation) {
		log.debug("onInvitationReceived");
		this.mCurrentInvitation = invitation;
		this.mMenuManager.showInviteDialog();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces Standard android
	// ===========================================================
	@Override
	public void onActivityResult(int requestCode, int responseCode, Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);
		log.debug("onActivityResult");
		switch(requestCode){
		case Consts.RC_WAITING_ROOM:
			if (responseCode == Activity.RESULT_OK) {
				log.debug("Came back from waiting room aok");
			}else{
				log.debug("Left the waiting	 room somehow... result not ok");
			}
			break;
		}
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		log.debug("onResumeFragments");
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces RoomStatusUpdateListener
	// ===========================================================

	@Override
	public void onRealTimeMessageReceived(RealTimeMessage arg0) {
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces RoomUpdateListener
	// ===========================================================
	@Override
	public void onJoinedRoom(int arg0, Room room) {
		log.debug("onJoinedRoom");
		this.showWaitingRoom(room);
	}

	@Override
	public void onLeftRoom(int arg0, String arg1) {
		log.debug("onLeftRoom");
	}

	@Override
	public void onRoomConnected(int arg0, Room room) {
		log.debug("onRoomConnected");
	}

	@Override
	public void onRoomCreated(int arg0, Room room) {
		log.debug("onRoomCreated");
		this.showWaitingRoom(room);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces RoomStatusUpdateListener
	// ===========================================================

	@Override
	public void onConnectedToRoom(Room room) {
		log.debug("onConnectedToRoom");
	}

	@Override
	public void onDisconnectedFromRoom(Room room) {
		log.debug("onDisconnectedFromRoom");
	}

	@Override
	public void onP2PConnected(String arg0) {
		log.debug("onP2PConnected");
	}

	@Override
	public void onP2PDisconnected(String room) {
		log.debug("onP2PDisconnected");
	}

	@Override
	public void onPeerDeclined(Room room, List<String> arg1) {
		log.debug("onPeerDeclined");
	}

	@Override
	public void onPeerInvitedToRoom(Room room, List<String> arg1) {
		log.debug("onPeerInvitedToRoom");
	}

	@Override
	public void onPeerJoined(Room room, List<String> arg1) {
		log.debug("onPeerJoined");
	}

	@Override
	public void onPeerLeft(Room room, List<String> arg1) {
		log.debug("onPeerLeft");
	}

	@Override
	public void onPeersConnected(Room room, List<String> arg1) {
		log.debug("onPeersConnected");
	}

	@Override
	public void onPeersDisconnected(Room room, List<String> arg1) {
		log.debug("onPeersDisconnected");
	}

	@Override
	public void onRoomAutoMatching(Room room) {
		log.debug("onRoomAutoMatching");
	}

	@Override
	public void onRoomConnecting(Room room) {
		log.debug("onRoomConnecting");
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces Mix of android and andengine
	// ===========================================================

	@Override
	protected void onStart() {
		super.onStart();
		log.debug("onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		log.debug("onStop");
		if (this.isFinishing()) {
			log.debug("onStop - activity is finishing");
		} else {
			log.debug("onStop - activity is NOT finishing, but is going into background");
			this.mActivityResume = true;
		}
	}

	@Override
	protected synchronized void onCreateGame() {
		super.onCreateGame();
		log.debug("onCreateGame");
		// this.mMenuManager.showSignInMenu();
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		log.debug("onResume: mActivityResume: {}", mActivityResume);
		if (this.mActivityResume) {
			this.mActivityResume = false;
		} else {
			if (this.mSwapFragmentOnResume) {
				this.mMenuManager.showSignInMenu();
			}
		}
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

	@Override
	public synchronized void onGameCreated() {
		super.onGameCreated();
		log.debug("onGameCreated");
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		log.debug("onCreateEngineOptions");
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
		log.debug("onCreateEngine");
		this.engine = new FixedStepMaxFPSEngine(pEngineOptions, this.update_per_second);
		return this.engine;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		log.debug("onCreateResources");
		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);
		this.mTouchManager = new TouchManager();
		// We did create menu manager here
		try {
			this.zoomDepth = getResources().getInteger(R.integer.zoomDepth);
			this.maxZoom = getResources().getInteger(R.integer.zoomMax);
		} catch (NotFoundException e) {
			this.zoomDepth = 2;
			this.maxZoom = 2;
		}
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		log.debug("onCreateScene");
		Scene mScene = new Scene();
		final FPSLogger fpsLogger = new FPSLogger();
		this.getEngine().registerUpdateHandler(fpsLogger);
		mScene.setBackground(new Background(0.6509f, 0.8156f, 0.7764f));
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		log.debug("onPopulateScene");
		pScene.setOnSceneTouchListener(this);
		pScene.setTouchAreaBindingOnActionMoveEnabled(true);
		pScene.setOnAreaTouchTraversalFrontToBack();
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
		log.debug("onGameCreated");
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
			/*
			 * We need to only enable this when required!
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
			*/
			/*
			 * We need to be smart and be able to call an object which requires this!
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
			
			*/
		}
		return true;
	}

	/**
	 * @see org.andengine.ui.activity.fragments.compatibility.BaseGameFragment#onSurfaceCreated(org.andengine.opengl.util.GLState)
	 */
	@Override
	public synchronized void onSurfaceCreated(GLState pGLState) {
		super.onSurfaceCreated(pGLState);
		log.debug("onSurfaceCreated");
	}

	/**
	 * @see org.andengine.ui.activity.fragments.compatibility.BaseGameFragment#onSurfaceChanged(org.andengine.opengl.util.GLState,
	 *      int, int)
	 */
	@Override
	public synchronized void onSurfaceChanged(GLState pGLState, int pWidth, int pHeight) {
		super.onSurfaceChanged(pGLState, pWidth, pHeight);
		log.debug("onSurfaceChanged");
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public GamesClient passThroughGamesClient() {
		return this.getGamesClient();
	}

	public boolean isSignedIn() {
		return this.mHelper.isSignedIn();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void signInOut(boolean pStatus) {
		if (pStatus) {
			this.beginUserInitiatedSignIn();
		} else {
			this.signOut();
		}
	}

	public void registerSignInListener(GameHelper.GameHelperListener pGameHelperListener) {
		if (!this.mSigninListeners.contains(pGameHelperListener)) {
			this.mSigninListeners.add(pGameHelperListener);
		}
	}

	public void removeSignInListener(GameHelper.GameHelperListener pGameHelperListener) {
		if (this.mSigninListeners.contains(pGameHelperListener)) {
			this.mSigninListeners.remove(pGameHelperListener);
		}
	}

	/**
	 * When {@link #onResumeFragments()} should we show the main menu or don't
	 * make any changes to fragments?
	 * 
	 * @param pSwapOnResume
	 */
	public void setSwapFragmentOnResume(final boolean pSwapOnResume) {
		this.mSwapFragmentOnResume = pSwapOnResume;
	}

	public void inviteDeclined() {
		log.debug("inviteDeclined");
		this.mHelper.getGamesClient().declineRoomInvitation(this.mCurrentInvitation.getInvitationId());
		this.mCurrentInvitation = null;
		this.mMenuManager.dismissInviteDialog();
	}

	public void inviteAccepted() {
		log.debug("inviteAccepted");
		this.mMenuManager.dismissInviteDialog();
		this.acceptInviteToRoom(this.mCurrentInvitation.getInvitationId());
	}

	public void handleSelectPlayersResult(int response, Intent data) {
		final ArrayList<String> invitees = data.getStringArrayListExtra(GamesClient.EXTRA_PLAYERS);
		log.debug("Got {} invitees", invitees.size());

		Bundle autoMatchCriteria = null;
		int minAutoMatchPlayers = data.getIntExtra(GamesClient.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
		int maxAutoMatchPlayers = data.getIntExtra(GamesClient.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
		if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
			autoMatchCriteria = RoomConfig.createAutoMatchCriteria(minAutoMatchPlayers, maxAutoMatchPlayers, 0);
			log.debug("Automatch criteria: {}" + autoMatchCriteria);
		}

		// create the room
		log.debug("creating room");
		RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
		rtmConfigBuilder.addPlayersToInvite(invitees);
		rtmConfigBuilder.setMessageReceivedListener(this);
		rtmConfigBuilder.setRoomStatusUpdateListener(this);
		if (autoMatchCriteria != null) {
			rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
		}
		getGamesClient().createRoom(rtmConfigBuilder.build());
	}

	public void handleInvitationInboxResult(int response, Intent data) {
		log.debug("handleInvitationInboxResult");
		Invitation inv = data.getExtras().getParcelable(GamesClient.EXTRA_INVITATION);
		acceptInviteToRoom(inv.getInvitationId());
	}

	private void acceptInviteToRoom(String invId) {
		log.debug("acceptInviteToRoom: {}", invId);
		RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
		roomConfigBuilder.setInvitationIdToAccept(invId).setMessageReceivedListener(this)
				.setRoomStatusUpdateListener(this);
		getGamesClient().joinRoom(roomConfigBuilder.build());
	}
	
	private void showWaitingRoom(Room room) {
		log.debug("showWaitingRoom");

        // minimum number of players required for our game
        final int MIN_PLAYERS = 2;
        Intent i = getGamesClient().getRealTimeWaitingRoomIntent(room, MIN_PLAYERS);

        // show waiting room UI
        startActivityForResult(i, Consts.RC_WAITING_ROOM);
    }
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
