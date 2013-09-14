package com.niffy.IsometricWorld.menu.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.niffy.IsometricWorld.menu.MenuManager;

public class FragmentDialogBase extends DialogFragment{
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(FragmentDialogBase.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected View view;
	protected MenuManager mParent;
	// ===========================================================
	// Constructors
	// ===========================================================

	public FragmentDialogBase(final MenuManager pParent) {
		this.mParent = pParent;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.debug("onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		log.debug("onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		log.debug("onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		log.debug("onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		log.debug("onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		log.debug("onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		log.debug("onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		log.debug("onStop");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		log.debug("onActivityResult");
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		log.debug("onActivityCreated");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		log.debug("onAttach");
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		log.debug("onDismiss");
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
