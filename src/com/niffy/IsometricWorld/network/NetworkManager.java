package com.niffy.IsometricWorld.network;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Message;

import com.niffy.AndEngineLockStepEngine.IControlInformation;
import com.niffy.AndEngineLockStepEngine.ILockstepClientListener;
import com.niffy.AndEngineLockStepEngine.ILockstepEngine;
import com.niffy.AndEngineLockStepEngine.ILockstepNetwork;
import com.niffy.AndEngineLockStepEngine.ILockstepStepChangeListener;
import com.niffy.AndEngineLockStepEngine.Lockstep;
import com.niffy.AndEngineLockStepEngine.flags.ITCFlags;
import com.niffy.AndEngineLockStepEngine.misc.IHandlerMessage;
import com.niffy.AndEngineLockStepEngine.misc.WeakThreadHandler;
import com.niffy.AndEngineLockStepEngine.options.IBaseOptions;
import com.niffy.AndEngineLockStepEngine.threads.CommunicationHandler;
import com.niffy.AndEngineLockStepEngine.threads.ICommunicationHandler;
import com.niffy.AndEngineLockStepEngine.threads.ICommunicationThread;
import com.niffy.IsometricWorld.IsometricWorldActivity;
import com.niffy.IsometricWorld.fragments.NetworkStatusDialog;

public class NetworkManager implements ILockstepClientListener, IHandlerMessage, IControlInformation, ILockstepStepChangeListener {
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
	protected String mAddress;
	protected WeakThreadHandler<IHandlerMessage> mHandler;
	protected ILockstepEngine mLockstepEngine;
	protected ILockstepNetwork mLockstepNetwork;
	protected ICommunicationHandler mCommunicationHandler;
	protected NetworkStatusDialog mNetworkStatusDialog;
	protected ArrayList<Integer> mLockstepFlags;
	protected ICommunicationThread mCommandManager;
	protected int mConnectedPlayers = 0;
	protected boolean mHost = false;
	// ===========================================================
	// Constructors
	// ===========================================================

	public NetworkManager(final IsometricWorldActivity pParent, final IBaseOptions pBaseOptions,
			final NetworkStatusDialog pNetworkStatusDialog) throws UnknownHostException {
		this.mParent = pParent;
		this.mBaseOptions = pBaseOptions;
		this.mNetworkStatusDialog = pNetworkStatusDialog;
		this.mLockstepEngine = new Lockstep(this, this.mBaseOptions, this);
		this.mLockstepNetwork = this.mLockstepEngine.getLockstepNetwork();
		this.mParent.engine.setLockstep(this.mLockstepEngine);
		this.mLockstepEngine.subscribeStepChangeListener(this);
		this.mHandler = this.mParent.mHander;
		this.mLockstepFlags = new ArrayList<Integer>(ITCFlags.LOCKSTEP_FLAGS.length);
		for (int flag : ITCFlags.LOCKSTEP_FLAGS) {
			this.mLockstepFlags.add(flag);
		}
		this.mCommandManager = new CommandManager(this.mBaseOptions, this.mLockstepEngine, this.mLockstepNetwork);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void clientConnected(String pClient) {
		log.info("Client Connected: {}", pClient.toString());
		this.mNetworkStatusDialog.addNewLine("Client Conencted: " + pClient.toString());
		this.mCommandManager.addClient(pClient);
		this.mConnectedPlayers++;
		if(this.mHost){
			if(this.mConnectedPlayers == 1){
				this.mLockstepEngine.start();
			}
		}
	}

	@Override
	public void clientDisconnected(String pClient) {
		log.info("Client Disconnected: {}", pClient.toString());
		this.mNetworkStatusDialog.addNewLine("Client Disconnected: " + pClient.toString());
		this.mCommandManager.removeClient(pClient);
		this.mConnectedPlayers--;
	}

	@Override
	public void clientError(String pClient, String pMsg) {
		log.info("Client Error: {} Msg: {}", pClient.toString(), pMsg);
		this.mNetworkStatusDialog.addNewLine("Client Error: " + pMsg + "Client: " + pClient.toString());
		/* TODO handle error pass to command manger? */
		this.mConnectedPlayers--;
	}

	@Override
	public void clientOutOfSync(String pClient) {
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
		case ITCFlags.MAIN_COMMUNICATION_START:
			log.debug("main comm thread MAIN_COMMUNICATION_START");
			break;
		case ITCFlags.RECIEVE_MESSAGE_CLIENT:
			this.mCommandManager.handlePassedMessage(pMessage);
			break;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public ICommand getCommandManager(){
		return (ICommand) this.mCommandManager;
	}
	
	public void shutdownNetwork(){
		this.mCommunicationHandler.terminate();
	}
	// ===========================================================
	// Methods
	// ===========================================================
	public void createThreads() {
		log.debug("creating threads");
		InetSocketAddress pAddress = new InetSocketAddress(this.mAddress, this.mBaseOptions.getTCPServerPort());
		this.mCommunicationHandler = new CommunicationHandler("Main communication thread", this.mHandler,
				this.mBaseOptions);
		Thread thread = (Thread) this.mCommunicationHandler;
		thread.start();
	}

	@Override
	public void countdownStarted() {
		log.debug("countdownStarted");
	}

	@Override
	public void countdown(int pSecondsToGo) {
		log.debug("countdown");
	}

	@Override
	public void countdownFinished() {
		log.debug("countdownFinished");
	}

	@Override
	public void canNowPause() {
		log.debug("canNowPause");
	}

	@Override
	public void canNowResume() {
		log.debug("canNowResume");
	}

	@Override
	public void lockstepStepChange(int pGameStep) {
		
	}
	
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
