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
		this.createThreads();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void clientConnected(InetAddress pClient) {
	}

	@Override
	public void clientDisconnected(InetAddress pClient) {
	}

	@Override
	public void clientError(InetAddress pClient, String pMsg) {
	}

	@Override
	public void clientOutOfSync(InetAddress pClient) {
	}

	@Override
	public void migrate() {
	}

	@Override
	public void handlePassedMessage(Message pMessage) {
		log.debug("Handling message: ", pMessage.what);
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
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	protected void createThreads() {
		this.mParent.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Thread pTCP = new TCPCommunicationThread(mAddress, mHandler, mBaseOptions);
				mTCP = (ICommunicationThread) pTCP;
				pTCP.start();
			}
		});
		this.mParent.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread pUDP = new UDPCommunicationThread(mAddress, mHandler, mBaseOptions);
					mUDP = (ICommunicationThread) pUDP;
					pUDP.start();
				} catch (SocketException e) {
					log.error("Could not create UDP thread", e);
				}
			}
		});
	}

	protected void TCPThreadStart() {
		this.mTCPCreated = true;
		if (!this.mAllThreadsCreated) {
			if (this.mTCPCreated && this.mUDPCreated) {
				this.mAllThreadsCreated = true;
				this.allThreadsRunning();
			}
		}
	}

	protected void UDPThreadStart() {
		this.mUDPCreated = true;
		if (!this.mAllThreadsCreated) {
			if (this.mTCPCreated && this.mUDPCreated) {
				this.mAllThreadsCreated = true;
				this.allThreadsRunning();
			}
		}
	}
	
	protected void allThreadsRunning(){
		this.mLockstepNetwork.attachTCPThread(this.mTCP);
		this.mLockstepNetwork.attachUDPThread(this.mUDP);
		this.mLockstepEngine.startInitialCommunications();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
