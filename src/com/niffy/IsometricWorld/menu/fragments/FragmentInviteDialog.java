package com.niffy.IsometricWorld.menu.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.niffy.IsometricWorld.R;
import com.niffy.IsometricWorld.menu.MenuManager;

public class FragmentInviteDialog extends FragmentDialogBase {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(FragmentInviteDialog.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected Button mAccept;
	protected Button mDecline;
	protected boolean mClickAccept = false;
	protected boolean mClickDecline = false;
	// ===========================================================
	// Constructors
	// ===========================================================

	public FragmentInviteDialog(final MenuManager pParent) {
		super(pParent);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		log.info("Creating invite fragment");
		this.view = inflater.inflate(R.layout.frag_dialog_invite, container, false);
		
		this.mAccept = (Button) this.view.findViewById(R.id.frag_dialog_invite_accept);
		this.mDecline = (Button) this.view.findViewById(R.id.frag_dialog_invite_decline);
		this.mAccept.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mClickAccept = true;
				mParent.getParent().inviteAccepted();
			}
		});
		this.mDecline.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mClickDecline = true;
				mParent.getParent().inviteDeclined();
			}
		});
		this.getDialog().setCanceledOnTouchOutside(false);
		return this.view;
	}


	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if(!this.mClickAccept && !this.mClickDecline){
			this.mParent.getParent().inviteDeclined();
		}
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
