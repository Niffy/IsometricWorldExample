package com.niffy.IsometricWorld;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.andengine.util.adt.array.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.niffy.IsometricWorld.entity.CubeTemplate;
import com.niffy.IsometricWorld.fragments.FragmentDone;
import com.niffy.IsometricWorld.fragments.FragmentMultiplayerRole;
import com.niffy.IsometricWorld.fragments.IDialogMultiplayerRoleReturn;
import com.niffy.IsometricWorld.fragments.IPAddressInput;
import com.niffy.IsometricWorld.fragments.IPAddressInput.IDialogInputReturn;
import com.niffy.IsometricWorld.network.NetworkManager;

public class GeneralManager implements IDialogMultiplayerRoleReturn, IDialogInputReturn {

	private final Logger log = LoggerFactory.getLogger(GeneralManager.class);

	protected IsometricWorldActivity mParent = null;
	protected MapHandler mMapHandler;
	protected int[] mFreeFragmentSlots;

	protected BuildObjectDialog mBuild;
	protected ObjectPlacer mObjectPlacer;
	protected FragmentDone mFragmentDone;
	protected FragmentMultiplayerRole mNetworkRole;
	/*
	 * Has the networking started? helps determine if to show role dialog or status
	 */
	protected boolean mNetworkRoleSelected = false;
	protected NetworkManager mNetworkManager;

	public GeneralManager(final IsometricWorldActivity pParent, final MapHandler pMapHandler) {
		this.mParent = pParent;
		this.mMapHandler = pMapHandler;
		this.getFreeFragmentSlots();
	}

	public void setNetworkRoleDialog(final FragmentMultiplayerRole pRole) {
		this.mNetworkRole = pRole;
	}

	public void setNetworkManager(final NetworkManager pNetworkManager) {
		this.mNetworkManager = pNetworkManager;
	}

	public void placeHuman() {
		new HumanPlacer(this.mParent, this);
	}

	public void showObjectMenu() {
		if (this.mObjectPlacer == null) {
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

	public void networkClick() {
		this.mNetworkRole.show(this.mParent.getSupportFragmentManager(), null);
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

	@Override
	public void isHost() {
		log.info("Act as host");
		this.mNetworkRoleSelected = true;
	}

	@Override
	public void isClient() {
		log.info("Act as client");
		this.mNetworkRoleSelected = true;
		IPAddressInput input = new IPAddressInput(this);
		input.show(this.mParent.getSupportFragmentManager(), null);
	}

	@Override
	public void cancel() {
		log.info("Cancel network role selection");
		this.mNetworkRoleSelected = false;
	}

	@Override
	public void returnInput(String pInput) {
		if (pInput != null) {
			try {
				InetAddress pAddress = InetAddress.getByName(pInput);
				this.mNetworkManager.isClientSelected(pAddress);
			} catch (UnknownHostException e) {
				log.error("could not cast ip address from string", e);
			}
		} else {
			log.error("Cannot get IP address input as return string is null, Did you cancel?");
		}
	}
}
