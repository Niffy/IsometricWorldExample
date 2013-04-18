package com.niffy.IsometricWorld.network;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
import com.niffy.IsometricWorld.fragments.NetworkStatusDialog;

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
	protected NetworkStatusDialog mNetworkStatusDialog;
	protected ArrayList<Integer> mLockstepFlags;

	// ===========================================================
	// Constructors
	// ===========================================================

	public NetworkManager(final IsometricWorldActivity pParent, final IBaseOptions pBaseOptions,
			final NetworkStatusDialog pNetworkStatusDialog) throws UnknownHostException {
		this.mParent = pParent;
		this.mBaseOptions = pBaseOptions;
		this.mNetworkStatusDialog = pNetworkStatusDialog;
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
		this.mLockstepFlags = new ArrayList<Integer>(ITCFlags.LOCKSTEP_FLAGS.length);
		for (int flag : ITCFlags.LOCKSTEP_FLAGS) {
			this.mLockstepFlags.add(flag);
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void clientConnected(InetAddress pClient) {
		log.info("Client Connected: {}", pClient.toString());
		this.mNetworkStatusDialog.addNewLine("Client Conencted: " + pClient.toString());
		this.mLockstepNetwork.migrate();
	}

	@Override
	public void clientDisconnected(InetAddress pClient) {
		log.info("Client Disconnected: {}", pClient.toString());
		this.mNetworkStatusDialog.addNewLine("Client Disconnected: " + pClient.toString());
	}

	@Override
	public void clientError(InetAddress pClient, String pMsg) {
		log.info("Client Error: {} Msg: {}", pClient.toString(), pMsg);
		this.mNetworkStatusDialog.addNewLine("Client Error: " + pMsg + "Client: " + pClient.toString());
	}

	@Override
	public void clientOutOfSync(InetAddress pClient) {
		log.info("Client Out of Sync: {}", pClient.toString());
		this.mNetworkStatusDialog.addNewLine("Client out of sync: " + pClient.toString());
	}

	@Override
	public void migrate() {
		log.info("Migrating");
		this.mNetworkStatusDialog.addNewLine("We are mirgrating");
	}

	@Override
	public void connected() {
		log.info("Connected to host");
		this.mNetworkStatusDialog.addNewLine("Connected to host");
	}

	@Override
	public void connectError() {
		log.info("Connecting error");
		this.mNetworkStatusDialog.addNewLine("Connect Error");
	}

	@Override
	public void networkError(String pError) {
		log.info("Network error");
		this.mNetworkStatusDialog.addNewLine(pError);
	}

	@Override
	public void handlePassedMessage(Message pMessage) {
		log.debug("Handling message: {} ", pMessage.what);
		if(this.mLockstepFlags.contains(pMessage.what)){
			this.mLockstepNetwork.handlePassedMessage(pMessage);
		}
		switch (pMessage.what) {
		case ITCFlags.TCP_THREAD_START:
			this.TCPThreadStart();
			break;
		case ITCFlags.UDP_THREAD_START:
			this.UDPThreadStart();
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
		TCPCommunicationThread pTCP = new TCPCommunicationThread(this.mAddress, this.mHandler, this.mBaseOptions);
		this.mTCP = (ICommunicationThread) pTCP;
		pTCP.start();
		//Looper tcpLooper = pTCP.getLooper();
		//pTCP.setHandler(new WeakThreadHandler<IHandlerMessage>(pTCP, tcpLooper));
		
		try {
			UDPCommunicationThread pUDP = new UDPCommunicationThread(this.mAddress, this.mHandler, this.mBaseOptions);
			this.mUDP = (ICommunicationThread) pUDP;
			pUDP.start();
			//Looper udpLooper = pUDP.getLooper();
			//pUDP.setHandler(new WeakThreadHandler<IHandlerMessage>(pUDP, udpLooper));
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
		this.mNetworkStatusDialog.show(this.mParent.getSupportFragmentManager(), null);
		this.mNetworkStatusDialog.addNewLine("Acting as host");
	}

	public void isClientSelected(final String pAddress) {
		log.debug("isClientSelected");
		this.mLockstepNetwork.connectTo(pAddress);
		this.mNetworkStatusDialog.show(this.mParent.getSupportFragmentManager(), null);
		this.mNetworkStatusDialog.addNewLine("Acting as client");
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
