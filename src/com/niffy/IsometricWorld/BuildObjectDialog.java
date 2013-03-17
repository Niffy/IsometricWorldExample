package com.niffy.IsometricWorld;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.niffy.IsometricWorld.entity.CubeTemplate;

public class BuildObjectDialog extends DialogFragment {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(BuildObjectDialog.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected IsometricWorldActivity mParent;
	protected GeneralManager mMenuManager;
	protected View mView;
	protected ListView mListView;
	protected ImageView mImageView;
	protected TextView mWidth;
	protected TextView mLength;
	protected TextView mHeight;
	protected TextView mDone;
	protected TextView mCancel;
	protected ObjectListAdapter mAdapter;
	protected ArrayList<CubeTemplate> mData;
	protected CubeTemplate mSelected;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BuildObjectDialog(final IsometricWorldActivity pParent, final GeneralManager pMenuManager) {
		this.mParent = pParent;
		this.mMenuManager = pMenuManager;
		this.mData = this.mParent.mData;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setCanceledOnTouchOutside(true);
		this.mView = inflater.inflate(R.layout.dialog_build_object, container);
		this.mListView = (ListView) this.mView.findViewById(R.id.listview);
		this.mImageView = (ImageView) this.mView.findViewById(R.id.details_image);
		this.mWidth = (TextView) this.mView.findViewById(R.id.details_width_text);
		this.mLength = (TextView) this.mView.findViewById(R.id.details_length_text);
		this.mHeight = (TextView) this.mView.findViewById(R.id.details_height_text);
		this.mDone = (TextView) this.mView.findViewById(R.id.details_done);
		this.mDone.setEnabled(false);
		this.mCancel = (TextView) this.mView.findViewById(R.id.details_cancel);
		this.mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMenuManager != null) {
					if (!mParent.engine.isPaused()) {
						mMenuManager.productionCancel();
						log.info("We didn't want to do anything");
					}
				}
			}
		});

		this.mDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMenuManager != null) {
					if (!mParent.engine.isPaused()) {
						if (mSelected != null) {
							log.info("We selected something!");
							mMenuManager.productionDone(mSelected);
						}
					}
				}
			}
		});

		this.mListView.setTextFilterEnabled(true);
		this.mListView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
		this.mListView.setClickable(true);

		this.mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				int p = (Integer) myView.getTag();
				CubeTemplate found = mData.get(p);
				if (found != null) {
					log.info("Clicked: {} - {}", p, found.getmFileName());
					mSelected = found;
					updateDetails();
				}
			}
		});
		try {
			this.mAdapter = new ObjectListAdapter(this.mData, this.mParent.getApplicationContext());
			this.mListView.setAdapter(this.mAdapter);
		} catch (Exception e) {
			log.error("Could not create MapSelectionAdapter", e);
		}
		this.getDialog().setTitle(getResources().getString(R.string.app_name));
		return mView;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	protected void updateDetails(){
		this.mImageView.setImageDrawable(this.mSelected.getDrawable());
		this.mWidth.setText(String.valueOf(this.mSelected.getM3DWidth()));
		this.mLength.setText(String.valueOf(this.mSelected.getM3DLength()));
		this.mHeight.setText(String.valueOf(this.mSelected.getM3DHeight()));
		this.mDone.setEnabled(true);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
