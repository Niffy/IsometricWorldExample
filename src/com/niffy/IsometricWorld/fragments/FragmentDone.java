package com.niffy.IsometricWorld.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niffy.IsometricWorld.GeneralManager;
import com.niffy.IsometricWorld.IsometricWorldActivity;
import com.niffy.IsometricWorld.R;

public class FragmentDone extends Fragment {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(FragmentDone.class);
	protected IsometricWorldActivity mParent;
	protected View fragementView;
	protected TextView mDone;
	protected TextView mCancel;
	protected GeneralManager mGeneralManager;
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public FragmentDone() {
		super();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.fragementView = inflater.inflate(R.layout.button_done, container, false);
		return this.fragementView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.mDone = (TextView) this.getView().findViewById(R.id.button_done_text_view);
		this.mDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mParent != null){
					if(!mParent.engine.isPaused()){
						if(mGeneralManager != null){
							mGeneralManager.placeDone();
						}
					}
				}
			}
		});
		
		this.mCancel = (TextView) this.getView().findViewById(R.id.button_cancel_text_view);
		this.mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mParent != null){
					if(!mParent.engine.isPaused()){
						if(mGeneralManager != null){
							mGeneralManager.placeCancel();
						}
					}
				}
			}
		});
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setParent(IsometricWorldActivity pParent){
		this.mParent = pParent;
	}
	
	public void setGeneralManager(final GeneralManager pManager){
		this.mGeneralManager = pManager;
	}
	// ===========================================================
	// Methods
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
