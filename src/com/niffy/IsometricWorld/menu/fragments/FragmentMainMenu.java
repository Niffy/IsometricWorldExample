package com.niffy.IsometricWorld.menu.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.niffy.IsometricWorld.R;
import com.niffy.IsometricWorld.menu.MenuManager;

public class FragmentMainMenu extends FragmentBase implements GameHelperListener {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(FragmentMainMenu.class);

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
	public FragmentMainMenu(final MenuManager pParent) {
		super(pParent);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		log.info("Creating MainMenuView");
		this.view = inflater.inflate(R.layout.frag_main_menu, container, false);
		this.mSignInButton = (SignInButton) this.view.findViewById(R.id.button_sign_in);
		this.mSinglePlayer = (Button) this.view.findViewById(R.id.btn_main_menu_single_palyer);
		this.mMultiplayer = (Button) this.view.findViewById(R.id.btn_main_menu_multiplayer);
		this.mSettings = (Button) this.view.findViewById(R.id.btn_main_menu_settings);
		this.mLoadGame = (Button) this.view.findViewById(R.id.btn_main_menu_load_game);
		this.mExit = (Button) this.view.findViewById(R.id.btn_main_menu_exit);
		this.mSignOut = (Button) this.view.findViewById(R.id.btn_main_menu_sign_out);

		this.mSignInButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mParent.signInOutClicked(true);
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
		return this.view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(this.isVisible()){
			if(this.mParent.getParent().isSignedIn()){
				this.swapSignInOut();
			}
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		this.mParent.getParent().registerSignInListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		this.mParent.getParent().removeSignInListener(this);
	}

	@Override
	public void onSignInFailed() {
		this.signInStatus(false);
	}

	@Override
	public void onSignInSucceeded() {
		this.signInStatus(true);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public void signInStatus(final boolean pStatus) {
		this.mSignedIn = pStatus;
		this.swapSignInOut();
	}

	protected void singlePlayerClick() {
		log.debug("singlePlayerClick");
	}

	protected void multiplayerClick() {
		log.debug("multiplayerClick");
		this.mParent.showMultiplayerMenu();
	}

	protected void loadGameClick() {
		log.debug("loadGame");
	}

	protected void settingsClick() {
		log.debug("settingsClick");
	}

	protected void exitClick() {
		log.debug("exitClick");
	}

	protected void signOutClick() {
		log.debug("signOutClick");
		this.mParent.signInOutClicked(false);
	}

	protected void swapSignInOut() {
		log.debug("swapSignInOut: {}", this.mSignedIn);
		if (this.mSignedIn) {
			this.mSignInButton.setVisibility(View.GONE);
			this.mSignOut.setVisibility(View.VISIBLE);
			this.mMultiplayer.setVisibility(View.VISIBLE);
		} else {
			this.mSignOut.setVisibility(View.GONE);
			this.mMultiplayer.setVisibility(View.GONE);
			this.mSignInButton.setVisibility(View.VISIBLE);
		}
	}


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
