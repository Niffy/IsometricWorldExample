package com.niffy.IsometricWorld.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
import com.niffy.AndEngineLockStepEngine.threads.CommunicationHandler;
import com.niffy.AndEngineLockStepEngine.threads.ICommunicationHandler;
import com.niffy.AndEngineLockStepEngine.threads.ICommunicationThread;
import com.niffy.AndEngineLockStepEngine.threads.nio.ClientSelector;
import com.niffy.AndEngineLockStepEngine.threads.nio.IClientSelector;
import com.niffy.AndEngineLockStepEngine.threads.nio.IServerSelector;
import com.niffy.AndEngineLockStepEngine.threads.nio.ServerSelector;
import com.niffy.AndEngineLockStepEngine.threads.nio.UDPSelector;
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
	protected ICommunicationHandler mCommunicationHandler;
	protected IServerSelector mTCPServer;
	protected IClientSelector mTCPClient;
	protected IClientSelector mUDPClient;
	protected boolean mTCPServerCreated = false;
	protected boolean mTCPClientCreated = false;
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
		if (this.mLockstepFlags.contains(pMessage.what)) {
			this.mLockstepNetwork.handlePassedMessage(pMessage);
		}
		switch (pMessage.what) {
		case ITCFlags.TCP_CLIENT_SELECTOR_START:

			break;
		case ITCFlags.TCP_SERVER_SELECTOR_START:

			break;
		case ITCFlags.UDP_CLIENT_SELECTOR_START:

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
		InetSocketAddress pAddress = new InetSocketAddress(this.mAddress, this.mBaseOptions.getTCPServerPort());
		InetSocketAddress pAddressClient = new InetSocketAddress(this.mAddress, this.mBaseOptions.getTCPClientPort());
		InetSocketAddress pAddressUDP = new InetSocketAddress(this.mAddress, this.mBaseOptions.getUDPPort());
		this.mCommunicationHandler = new CommunicationHandler("Main communication thread", pAddress, this.mHandler,
				this.mBaseOptions);

		try {
			this.mTCPServer = new ServerSelector("ServerThread", pAddress, this.mCommunicationHandler.getHandler(),
					this.mBaseOptions);
			this.mTCPClient = new ClientSelector("ClientThread", pAddressClient,
					this.mCommunicationHandler.getHandler(), this.mBaseOptions);
			this.mUDPClient = new UDPSelector("UDPThread", pAddressUDP, this.mCommunicationHandler.getHandler(),
					this.mBaseOptions);
		} catch (IOException e) {
			log.error("Could not create selecor threads", e);
		}

		this.mCommunicationHandler.setTCPClientSelectorThread(this.mTCPClient);
		this.mCommunicationHandler.setTCPServerSelectorThread(this.mTCPServer);
		this.mCommunicationHandler.setUDPSelectorThread(this.mUDPClient);
	}

	protected void TCPServerStart() {
		log.debug("TCPServerStart");
		this.mTCPServerCreated = true;
		if (!this.mAllThreadsCreated) {
			if (this.mTCPServerCreated && this.mTCPClientCreated && this.mUDPCreated) {
				this.mAllThreadsCreated = true;
			}
		}
	}

	protected void TCPClientStart() {
		log.debug("TCPClientStart");
		this.mTCPClientCreated = true;
		if (!this.mAllThreadsCreated) {
			if (this.mTCPServerCreated && this.mTCPClientCreated && this.mUDPCreated) {
				this.mAllThreadsCreated = true;
			}
		}
	}

	protected void UDPThreadStart() {
		log.debug("UDPThreadStart");
		this.mUDPCreated = true;
		if (!this.mAllThreadsCreated) {
			if (this.mTCPServerCreated && this.mTCPClientCreated && this.mUDPCreated) {
				this.mAllThreadsCreated = true;
			}
		}
	}

	public void isHostSelected() {
		log.debug("isHostSelected");
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
