package com.niffy.IsometricWorld;

import org.andengine.ui.activity.fragments.compatibility.LayoutGameFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.plus.PlusClient;
import com.google.example.games.basegameutils.GameHelper;

/**
 * Example base class for games. This implementation takes care of setting up
 * the GamesClient object and managing its lifecycle. Subclasses only need to
 * override the @link{#onSignInSucceeded} and @link{#onSignInFailed} abstract
 * methods. To initiate the sign-in flow when the user clicks the sign-in
 * button, subclasses should call @link{#beginUserInitiatedSignIn}. By default,
 * this class only instantiates the GamesClient object. If the PlusClient or
 * AppStateClient objects are also wanted, call the BaseGameActivity(int)
 * constructor and specify the requested clients. For example, to request
 * PlusClient and GamesClient, use BaseGameActivity(CLIENT_GAMES | CLIENT_PLUS).
 * To request all available clients, use BaseGameActivity(CLIENT_ALL).
 * Alternatively, you can also specify the requested clients via
 * 
 * @link{#setRequestedClients , but you must do so before @link{#onCreate} gets
 *                            called, otherwise the call will have no effect.
 * 
 * @author Bruno Oliveira (Google)
 */
public abstract class BaseFragmentActivity extends LayoutGameFragment implements GameHelper.GameHelperListener {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(BaseFragmentActivity.class);

	// ===========================================================
	// Fields
	// ===========================================================

	// The game helper object. This class is mainly a wrapper around this
	// object.
	protected GameHelper mHelper;

	// We expose these constants here because we don't want users of this class
	// to have to know about GameHelper at all.
	public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
	public static final int CLIENT_APPSTATE = GameHelper.CLIENT_APPSTATE;
	public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
	public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

	// Requested clients. By default, that's just the games client.
	protected int mRequestedClients = CLIENT_GAMES;

	// stores any additional scopes.
	private String[] mAdditionalScopes;

	protected String mDebugTag = "BaseGameActivity";
	protected boolean mDebugLog = false;

	// ===========================================================
	// Constructors
	// ===========================================================
	/** Constructs a BaseFragmentActivity with default client (GamesClient). */
	public BaseFragmentActivity() {
		super();
		mHelper = new GameHelper(this);
	}

	/**
	 * Constructs a BaseFragmentActivity with the requested clients.
	 * 
	 * @param requestedClients
	 *            The requested clients (a combination of CLIENT_GAMES,
	 *            CLIENT_PLUS and CLIENT_APPSTATE).
	 */
	protected BaseFragmentActivity(int requestedClients) {
		super();
		setRequestedClients(requestedClients);
	}

	/**
	 * Sets the requested clients. The preferred way to set the requested
	 * clients is via the constructor, but this method is available if for some
	 * reason your code cannot do this in the constructor. This must be called
	 * before onCreate in order to have any effect. If called after onCreate,
	 * this method is a no-op.
	 * 
	 * @param requestedClients
	 *            A combination of the flags CLIENT_GAMES, CLIENT_PLUS and
	 *            CLIENT_APPSTATE, or CLIENT_ALL to request all available
	 *            clients.
	 * @param additionalScopes
	 *            . Scopes that should also be requested when the auth request
	 *            is made.
	 */
	protected void setRequestedClients(int requestedClients, String... additionalScopes) {
		mRequestedClients = requestedClients;
		mAdditionalScopes = additionalScopes;
	}

	@Override
	protected int getLayoutID() {

		return 0;
	}

	@Override
	protected int getRenderSurfaceViewID() {

		return 0;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		mHelper = new GameHelper(this);
		if (mDebugLog) {
			mHelper.enableDebugLog(mDebugLog, mDebugTag);
		}
		mHelper.setup(this, mRequestedClients, mAdditionalScopes);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mHelper.onStop();
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		mHelper.onActivityResult(request, response, data);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	protected GamesClient getGamesClient() {
		return mHelper.getGamesClient();
	}

	protected AppStateClient getAppStateClient() {
		return mHelper.getAppStateClient();
	}

	protected PlusClient getPlusClient() {
		return mHelper.getPlusClient();
	}

	protected boolean isSignedIn() {
		return mHelper.isSignedIn();
	}

	protected void beginUserInitiatedSignIn() {
		mHelper.beginUserInitiatedSignIn();
	}

	protected void signOut() {
		mHelper.signOut();
	}

	protected void showAlert(String title, String message) {
		mHelper.showAlert(title, message);
	}

	protected void showAlert(String message) {
		mHelper.showAlert(message);
	}

	protected void enableDebugLog(boolean enabled, String tag) {
		mDebugLog = true;
		mDebugTag = tag;
		if (mHelper != null) {
			mHelper.enableDebugLog(enabled, tag);
		}
	}

	protected String getInvitationId() {
		return mHelper.getInvitationId();
	}

	protected void reconnectClients(int whichClients) {
		mHelper.reconnectClients(whichClients);
	}

	protected String getScopes() {
		return mHelper.getScopes();
	}

	protected String[] getScopesArray() {
		return mHelper.getScopesArray();
	}

	protected boolean hasSignInError() {
		return mHelper.hasSignInError();
	}

	protected GameHelper.SignInFailureReason getSignInError() {
		return mHelper.getSignInError();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
