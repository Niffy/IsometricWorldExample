package com.niffy.IsometricWorld.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niffy.IsometricWorld.IsometricWorldActivity;
import com.niffy.IsometricWorld.R;

public class FragmentNetwork extends Fragment {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(FragmentNetwork.class);
	protected IsometricWorldActivity mParent;
	protected View fragementView;
	protected TextView mTextView;
	protected boolean mClickable = true;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public FragmentNetwork() {
		super();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.fragementView = inflater.inflate(R.layout.button_network, container, false);
		return this.fragementView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.mTextView = (TextView) this.getView().findViewById(R.id.button_network_text_view);
		this.mTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mParent != null) {
					if (!mParent.engine.isPaused()) {
						if (mClickable) {
							mParent.mGeneralManager.networkClick();
						} else {
							log.warn("Button is disabled");
							DialogTextOk dialog = new DialogTextOk("Network disabled",
									"Button is disabled, most likely due to error");
							dialog.show(getFragmentManager(), null);
						}
					}
				}
			}
		});
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setParent(IsometricWorldActivity pParent) {
		this.mParent = pParent;
	}

	public void disableNetworkTouch(final boolean pDisable) {
		if (pDisable) {
			this.mClickable = false;
		} else {
			this.mClickable = true;
		}

	}
	// ===========================================================
	// Methods
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
