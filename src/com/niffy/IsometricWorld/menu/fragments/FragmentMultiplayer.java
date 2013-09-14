package com.niffy.IsometricWorld.menu.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.niffy.IsometricWorld.Consts;
import com.niffy.IsometricWorld.R;
import com.niffy.IsometricWorld.menu.MenuManager;

public class FragmentMultiplayer extends FragmentBase {

	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(FragmentMultiplayer.class);

	// ===========================================================
	// Fields
	// ===========================================================

	protected EditText mPlayerCount;
	protected Button mInvite;
	protected Button mInvitations;

	// ===========================================================
	// Constructors
	// ===========================================================
	public FragmentMultiplayer(final MenuManager pParent) {
		super(pParent);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		log.info("Creating Multiplayer fragment");
		this.view = inflater.inflate(R.layout.frag_multiplayer, container, false);
		this.mPlayerCount = (EditText) this.view.findViewById(R.id.multiplayer_players_textview);
		this.mInvite = (Button) this.view.findViewById(R.id.btn_multiplayer_invite);
		this.mInvitations = (Button) this.view.findViewById(R.id.btn_multiplayer_invite_see);
		this.mInvite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				invite();
			}
		});
		this.mInvitations.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				invitations();
			}
		});

		return this.view;
	}

	@Override
	public void onActivityResult(int requestCode, int responseCode, Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);
		this.mParent.getParent().setSwapFragmentOnResume(false);
		switch (requestCode) {
		case Consts.RC_INVITATION_INBOX:
			log.debug("Back from looking at invites");
			if (responseCode == Activity.RESULT_OK) {
				this.mParent.getParent().handleInvitationInboxResult(responseCode, intent);
			}
			break;
		case Consts.RC_SELECT_PLAYERS:
			log.debug("Back from selecting players to invite");
			if (responseCode == Activity.RESULT_OK) {
				this.mParent.getParent().handleSelectPlayersResult(responseCode, intent);
			}
			break;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	protected void invite() {
		String count = this.mPlayerCount.getText().toString();
		final int pCount = Integer.valueOf(count);
		Intent intent = this.mParent.getParent().passThroughGamesClient().getSelectPlayersIntent(pCount, pCount);
		startActivityForResult(intent, Consts.RC_SELECT_PLAYERS);
		/*
		Intent lobby = new Intent(FragmentMultiplayer.this, Lobby.class);
		lobby.putExtra("players", pCount);
		startActivity(lobby);
		*/
	}

	protected void invitations() {
		Intent intent = this.mParent.getParent().passThroughGamesClient().getInvitationInboxIntent();
		startActivityForResult(intent, Consts.RC_INVITATION_INBOX);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
