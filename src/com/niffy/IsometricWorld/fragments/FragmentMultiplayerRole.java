package com.niffy.IsometricWorld.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.niffy.IsometricWorld.IsometricWorldActivity;
import com.niffy.IsometricWorld.R;

public class FragmentMultiplayerRole extends DialogFragment {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(FragmentMultiplayerRole.class);
	protected IsometricWorldActivity mParent;
	protected View fragementView;
	protected RadioGroup mGroup;
	protected RadioButton mHostButton;
	protected RadioButton mClientButton;
	protected boolean mCancel = true;
	protected IDialogMultiplayerRoleReturn mRoleSelecter;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public FragmentMultiplayerRole(final IDialogMultiplayerRoleReturn pDialogMultiplayerRoleReturn) {
		super();
		this.mRoleSelecter = pDialogMultiplayerRoleReturn;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.getDialog().setCanceledOnTouchOutside(true);
		this.fragementView = inflater.inflate(R.layout.fragment_dialog_multiplayer_role, container, false);
		this.mGroup = (RadioGroup) this.fragementView.findViewById(R.id.multiplayer_dialog_role_group);
		this.mHostButton = (RadioButton) this.fragementView.findViewById(R.id.multiplayer_dialog_role_host);
		this.mClientButton = (RadioButton) this.fragementView.findViewById(R.id.multiplayer_dialog_role_client);

		this.mHostButton.setChecked(false);
		this.mClientButton.setChecked(true);

		this.mHostButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCancel = false;
				if (mRoleSelecter != null) {
					mRoleSelecter.isHost();
				}
				getDialog().dismiss();
			}
		});

		this.mClientButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCancel = false;
				if (mRoleSelecter != null) {
					mRoleSelecter.isClient();
				}
				getDialog().dismiss();
			}
		});
		return this.fragementView;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (mRoleSelecter != null) {
			if (this.mCancel) {
				mRoleSelecter.cancel();
			}
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setParent(IsometricWorldActivity pParent) {
		this.mParent = pParent;
	}
	// ===========================================================
	// Methods
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
