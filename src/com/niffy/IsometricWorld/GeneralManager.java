/**
 * 
 */
package com.niffy.IsometricWorld;

import java.util.ArrayList;

import org.andengine.util.adt.array.ArrayUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.niffy.IsometricWorld.entity.CubeTemplate;
import com.niffy.IsometricWorld.fragments.FragmentDone;

/**
 * @author Paul Robinson
 * 
 */
public class GeneralManager {
	protected IsometricWorldActivity mParent = null;
	protected MapHandler mMapHandler;
	protected int[] mFreeFragmentSlots;

	protected BuildObjectDialog mBuild;
	protected ObjectPlacer mObjectPlacer;
	protected FragmentDone mFragmentDone;

	public GeneralManager(final IsometricWorldActivity pParent, final MapHandler pMapHandler) {
		this.mParent = pParent;
		this.mMapHandler = pMapHandler;
		this.getFreeFragmentSlots();
	}

	public void placeHuman() {
		new HumanPlacer(this.mParent, this);
	}

	public void showObjectMenu() {
		if(this.mObjectPlacer == null){
			this.mBuild = new BuildObjectDialog(this.mParent, this);
			this.mBuild.show(this.mParent.getSupportFragmentManager(), null);
		}
	}

	public void productionDone(CubeTemplate pTemplate) {
		this.mBuild.dismiss();
		this.mBuild = null;
		this.addPlacementButtons();
		this.mObjectPlacer = new ObjectPlacer(this.mParent, this, pTemplate);
	}

	public void productionCancel() {
		this.mBuild.dismiss();
		this.mBuild = null;
	}

	public void placeCancel() {
		if (this.mObjectPlacer != null) {
			this.mObjectPlacer.cancel();
			this.mObjectPlacer = null;
			this.removeFragment(this.mFragmentDone);
			this.mFragmentDone = null;
		}
	}

	public void placeDone() {
		if (this.mObjectPlacer != null) {
			this.mObjectPlacer.done();
			this.mObjectPlacer = null;
			this.removeFragment(this.mFragmentDone);
			this.mFragmentDone = null;
		}
	}

	/*
	 * Add the done and cancel button to the main layout 
	 */
	public void addPlacementButtons() {
		this.mFragmentDone = new FragmentDone();
		this.mFragmentDone.setGeneralManager(this);
		this.mFragmentDone.setParent(this.mParent);
		this.addFragment(this.mFragmentDone);
	}
	
	public void networkClick(){
		
	}

	private void addFragment(final Fragment pFragment) {
		FragmentTransaction ft = this.mParent.getSupportFragmentManager().beginTransaction();
		ft.disallowAddToBackStack();
		ft.add(this.mFreeFragmentSlots[0], pFragment, null);
		ft.commit();
	}

	private void removeFragment(final Fragment pFragment) {
		FragmentTransaction ft = this.mParent.getSupportFragmentManager().beginTransaction();
		ft.remove(pFragment);
		ft.commit();
	}

	private void getFreeFragmentSlots() {
		RelativeLayout fragment_swing_holder = (RelativeLayout) mParent.findViewById(R.id.bottombar);
		int swingLocations = fragment_swing_holder.getChildCount();
		ArrayList<Integer> resSwingIDFound = new ArrayList<Integer>();
		for (int i = 0; i < swingLocations; i++) {
			int nestedSwingHolder = fragment_swing_holder.getChildAt(i).getId();
			resSwingIDFound.add(nestedSwingHolder);
		}
		this.mFreeFragmentSlots = ArrayUtils.toIntArray(resSwingIDFound);
	}

}
