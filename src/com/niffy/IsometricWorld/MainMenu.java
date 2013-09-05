package com.niffy.IsometricWorld;

import org.andengine.ui.activity.fragments.compatibility.BaseGameServicesActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.google.example.games.basegameutils.GameHelper;

public class MainMenu extends BaseGameServicesActivity implements GameHelper.GameHelperListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(MainMenu.class);

	// ===========================================================
	// Fields
	// ===========================================================

	protected SignInButton mSignInButton;
	protected Button mSinglePlayer;
	protected Button mMultiplayer;
	protected Button mSettings;
	protected Button mLoadGame;
	protected Button mExit;
	protected Button mSignOut;
	protected boolean mSignedIn = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		String pStringKey = this.getResources().getString(R.string.settings_key_logging);
		String pLoggingLevel = prefs.getString(pStringKey, "20000");
		int pLevel = Integer.valueOf(pLoggingLevel);
		ConfigureLog.configure("IsometricWorld.log", 10, pLevel, 5 * 1024 * 1024);
		log.info("Starting game up");
		
		setContentView(com.niffy.IsometricWorld.R.layout.default_start);
		this.mSignInButton = (SignInButton) findViewById(R.id.button_sign_in);
		this.mSinglePlayer = (Button) findViewById(R.id.btn_main_menu_single_palyer);
		this.mMultiplayer = (Button) findViewById(R.id.btn_main_menu_multiplayer);
		this.mSettings = (Button) findViewById(R.id.btn_main_menu_settings);
		this.mLoadGame = (Button) findViewById(R.id.btn_main_menu_load_game);
		this.mExit = (Button) findViewById(R.id.btn_main_menu_exit);
		this.mSignOut = (Button) findViewById(R.id.btn_main_menu_sign_out);
		this.mSignInButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				beginUserInitiatedSignIn();
			}
		});
		this.mSinglePlayer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				singlePlayerClick();
			}
		});
		this.mMultiplayer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				multiplayerClick();
			}
		});
		this.mSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				settingsClick();
			}
		});
		this.mLoadGame.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadGameClick();
			}
		});
		this.mExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				exitClick();
			}
		});
		this.mSignOut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				signOutClick();
			}
		});
	}

	@Override
	public void onSignInFailed() {
		log.error("onSignInFailed");
		this.mSignedIn = false;
		this.swapSignInOut();
	}

	@Override
	public void onSignInSucceeded() {
		log.error("onSignInSucceeded");
		this.mSignedIn = true;
		this.swapSignInOut();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	protected void singlePlayerClick() {
		log.error("singlePlayerClick");
	}

	protected void multiplayerClick() {
		log.error("multiplayerClick");
	}
	
	protected void loadGameClick() {
		log.error("loadGame");
	}

	protected void settingsClick() {
		log.error("settingsClick");
	}

	protected void exitClick() {
		log.error("exitClick");
	}
	
	protected void signOutClick(){
		log.error("signOutClick");
		signOut();
		
	}
	
	protected void swapSignInOut(){
		log.error("swapSignInOut: {}", this.mSignedIn);
		if(this.mSignedIn){
			this.mSignInButton.setVisibility(View.GONE);
			this.mSignOut.setVisibility(View.VISIBLE);
			this.mMultiplayer.setVisibility(View.VISIBLE);
		}else{
			this.mSignOut.setVisibility(View.GONE);
			this.mMultiplayer.setVisibility(View.GONE);
			this.mSignInButton.setVisibility(View.VISIBLE);
		}
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
