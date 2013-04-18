package com.niffy.IsometricWorld.fragments;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niffy.IsometricWorld.IsometricWorldActivity;
import com.niffy.IsometricWorld.R;

public class NetworkStatusDialog extends DialogFragment {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(NetworkStatusDialog.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected IsometricWorldActivity mParent;
	protected View mView;
	protected TextView mStatus;
	protected TextView mCancel;
	protected static ArrayList<String> mData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public NetworkStatusDialog(final IsometricWorldActivity pParent) {
		this.mParent = pParent;
		NetworkStatusDialog.mData = new ArrayList<String>();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setCanceledOnTouchOutside(true);
		this.mView = inflater.inflate(R.layout.dialog_network_status, container);
		this.mStatus = (TextView) this.mView.findViewById(R.id.status_text_view);
		this.mCancel = (TextView) this.mView.findViewById(R.id.status_cancel);
		this.mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		this.getDialog().setTitle(getResources().getString(R.string.app_name));
		this.mStatus.setText("");
		for (String pLine : NetworkStatusDialog.mData) {
			this.mStatus.append("\n");
			this.mStatus.append(pLine);
		}
		return mView;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public void addNewLine(final String pLine) {
		NetworkStatusDialog.mData.add(pLine);
		if(this.mStatus != null){
			this.mStatus.append("\\n");
			this.mStatus.append(pLine);
		}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
