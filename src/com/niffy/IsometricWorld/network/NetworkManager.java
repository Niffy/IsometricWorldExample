package com.niffy.IsometricWorld.network;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.andengine.util.WifiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Message;

import com.niffy.AndEngineLockStepEngine.ILockstepClientListener;
import com.niffy.AndEngineLockStepEngine.ILockstepEngine;
import com.niffy.AndEngineLockStepEngine.ILockstepNetwork;
import com.niffy.AndEngineLockStepEngine.Lockstep;
import com.niffy.AndEngineLockStepEngine.flags.ITCFlags;
import com.niffy.AndEngineLockStepEngine.misc.IHandlerMessage;
import com.niffy.AndEngineLockStepEngine.misc.WeakThreadHandler;
import com.niffy.AndEngineLockStepEngine.options.IBaseOptions;
import com.niffy.AndEngineLockStepEngine.threads.ICommunicationThread;
import com.niffy.AndEngineLockStepEngine.threads.tcp.TCPCommunicationThread;
import com.niffy.AndEngineLockStepEngine.threads.udp.UDPCommunicationThread;
import com.niffy.IsometricWorld.IsometricWorldActivity;
import com.niffy.IsometricWorld.fragments.DialogTextOk;

public class NetworkManager implements ILockstepClientListener, IHandlerMessage {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(NetworkManager.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected IsometricWorldActivity mParent;
	protected IBaseOptions mBaseOptions;
	protected String mAddressString;
	protected InetAddress mAddress;
	protected WeakThreadHandler<IHandlerMessage> mHandler;
	protected ILockstepEngine mLockstepEngine;
	protected ILockstepNetwork mLockstepNetwork;
	protected ICommunicationThread mTCP;
	protected boolean mTCPCreated = false;
	protected ICommunicationThread mUDP;
	protected boolean mUDPCreated = false;
	protected boolean mAllThreadsCreated = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	public NetworkManager(final IsometricWorldActivity pParent, final IBaseOptions pBaseOptions)
			throws UnknownHostException {
		this.mParent = pParent;
		this.mBaseOptions = pBaseOptions;
		try {
			this.mAddressString = WifiUtils.getWifiIPv4Address(pParent.getApplicationContext());
			this.mAddress = InetAddress.getByName(this.mAddressString);
		} catch (UnknownHostException e) {
			log.error("Could not get host IP address", e);
			throw e;
		}
		this.mLockstepEngine = new Lockstep(this, this.mBaseOptions);
		this.mLockstepNetwork = this.mLockstepEngine.getLockstepNetwork();
		this.mHandler = this.mParent.mHander;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void clientConnected(InetAddress pClient) {
		log.info("Client Connected: {}", pClient.toString());
	}

	@Override
	public void clientDisconnected(InetAddress pClient) {
		log.info("Client Disconnected: {}", pClient.toString());
	}

	@Override
	public void clientError(InetAddress pClient, String pMsg) {
		log.info("Client Error: {} Msg: {}", pClient.toString(), pMsg);
	}

	@Override
	public void clientOutOfSync(InetAddress pClient) {
		log.info("Client Out of Sync: {}", pClient.toString());
	}

	@Override
	public void migrate() {
		log.info("Migrating");
	}

	@Override
	public void connected() {
		log.info("Connected to host");
	}

	@Override
	public void connectError() {
		log.info("Connecting error");
	}

	@Override
	public void handlePassedMessage(Message pMessage) {
		log.debug("Handling message: {} ", pMessage.what);
		switch (pMessage.what) {
		case ITCFlags.TCP_THREAD_START:
			this.TCPThreadStart();
			break;
		case ITCFlags.UDP_THREAD_START:
			this.UDPThreadStart();
			break;
		case ITCFlags.CLIENT_CONNECTED:
			this.mLockstepNetwork.handlePassedMessage(pMessage);
			break;
		case ITCFlags.CLIENT_DISCONNECTED:
			this.mLockstepNetwork.handlePassedMessage(pMessage);
			break;
		case ITCFlags.CLIENT_ERROR:
			this.mLockstepNetwork.handlePassedMessage(pMessage);
			break;
		case ITCFlags.NETWORK_ERROR:
			this.mLockstepNetwork.handlePassedMessage(pMessage);
			break;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public void createThreads() {
		Thread pTCP = new TCPCommunicationThread(this.mAddress, this.mHandler, this.mBaseOptions);
		this.mTCP = (ICommunicationThread) pTCP;
		pTCP.start();
		try {
			Thread pUDP = new UDPCommunicationThread(this.mAddress, this.mHandler, this.mBaseOptions);
			this.mUDP = (ICommunicationThread) pUDP;
			pUDP.start();
		} catch (SocketException e) {
			log.error("Could not create UDP thread", e);
			mParent.mNetworkFragment.disableNetworkTouch(true);
			DialogTextOk dialog = new DialogTextOk("UDP Thread Error", e.toString());
			dialog.show(mParent.getSupportFragmentManager(), null);
		}
	}

	protected void TCPThreadStart() {
		log.debug("TCPThreadStarted");
		this.mTCPCreated = true;
		if (!this.mAllThreadsCreated) {
			if (this.mTCPCreated && this.mUDPCreated) {
				this.mAllThreadsCreated = true;
				this.allThreadsRunning();
			}
		}
	}

	protected void UDPThreadStart() {
		log.debug("UDPThreadStarted");
		this.mUDPCreated = true;
		if (!this.mAllThreadsCreated) {
			if (this.mTCPCreated && this.mUDPCreated) {
				this.mAllThreadsCreated = true;
				this.allThreadsRunning();
			}
		}
	}

	protected void allThreadsRunning() {
		log.debug("allThreadsRunning");
		this.mLockstepNetwork.attachTCPThread(this.mTCP);
		this.mLockstepNetwork.attachUDPThread(this.mUDP);
	}

	public void isHostSelected() {
		log.debug("isHostSelected");
		this.mLockstepNetwork.setMainCommunicationThread(this.mTCP);
		this.mLockstepEngine.startInitialCommunications();
	}

	public void isClientSelected(final String pAddress) {
		log.debug("isClientSelected");
		this.mLockstepNetwork.connectTo(pAddress);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
