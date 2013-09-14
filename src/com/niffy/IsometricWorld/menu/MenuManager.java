package com.niffy.IsometricWorld.menu;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.niffy.IsometricWorld.MainActivity;
import com.niffy.IsometricWorld.R;
import com.niffy.IsometricWorld.menu.fragments.FragmentInviteDialog;
import com.niffy.IsometricWorld.menu.fragments.FragmentMainMenu;
import com.niffy.IsometricWorld.menu.fragments.FragmentMultiplayer;

public class MenuManager implements GameHelperListener {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(MenuManager.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected FragmentManager mFragmentManager;
	protected MainActivity mParent;
	protected FragmentMainMenu mMainMenu;
	protected FragmentMultiplayer mMultiplayer;
	protected FragmentInviteDialog mInviteDialog;
	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuManager(final FragmentManager pFragmentManager, final MainActivity pMainActivity) {
		this.mFragmentManager = pFragmentManager;
		this.mParent = pMainActivity;
		this.mMainMenu = new FragmentMainMenu(this);
		this.mMultiplayer = new FragmentMultiplayer(this);
		this.mInviteDialog = new FragmentInviteDialog(this);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onSignInFailed() {
		this.signInStatus(false);
	}

	@Override
	public void onSignInSucceeded() {
		this.signInStatus(false);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public MainActivity getParent(){
		return this.mParent;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	public void showSignInMenu(){
		FragmentTransaction ft = this.mFragmentManager.beginTransaction();
		ft.replace(R.id.main_layout_relative, this.mMainMenu);
		ft.commit();
	}
	
	public void showMultiplayerMenu(){
		FragmentTransaction ft = this.mFragmentManager.beginTransaction();
		ft.replace(R.id.main_layout_relative, this.mMultiplayer);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public void showInviteDialog(){
		this.mInviteDialog.show(this.mFragmentManager, null);
		//this.mInviteDialog.getDialog().setCanceledOnTouchOutside(false);
	}
	
	public void dismissInviteDialog(){
		this.mInviteDialog.dismiss();
	}
	
	public void signInOutClicked(boolean pStatus){
		this.mParent.signInOut(pStatus);
		if(!pStatus){
			this.mMainMenu.signInStatus(pStatus);
		}
	}
	
	public void signInStatus(boolean pStatus){
		if(pStatus){
			if(this.mMainMenu.isVisible()){
				this.mMainMenu.signInStatus(true);
			}
		}
	}


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
