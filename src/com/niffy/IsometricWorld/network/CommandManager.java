package com.niffy.IsometricWorld.network;

import java.util.ArrayList;

import org.andengine.engine.lockstep.ILockstep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.os.Message;

import com.niffy.AndEngineLockStepEngine.ILockstepNetwork;
import com.niffy.AndEngineLockStepEngine.flags.ITCFlags;
import com.niffy.AndEngineLockStepEngine.messages.IMessage;
import com.niffy.AndEngineLockStepEngine.messages.MessageError;
import com.niffy.AndEngineLockStepEngine.messages.pool.MessagePool;
import com.niffy.AndEngineLockStepEngine.misc.IHandlerMessage;
import com.niffy.AndEngineLockStepEngine.misc.WeakThreadHandler;
import com.niffy.AndEngineLockStepEngine.options.IBaseOptions;
import com.niffy.AndEngineLockStepEngine.threads.ICommunicationThread;

public class CommandManager implements ICommunicationThread, ICommand {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(CommandManager.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected Commander mCommander;
	protected IBaseOptions mBaseOptions;
	protected ILockstep mLockstep;
	protected ILockstepNetwork mLockstepNetwork;
	protected ArrayList<String> mClients;
	protected MessagePool<IMessage> mMessagePool;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public CommandManager(final IBaseOptions pBaseOptions, final ILockstep pLockstepEngine,
			final ILockstepNetwork pLockstepNetwork) {
		this.mBaseOptions = pBaseOptions;
		this.mCommander = new Commander(this, this.mBaseOptions);
		this.mLockstep = pLockstepEngine;
		this.mLockstepNetwork = pLockstepNetwork;
		this.mClients = new ArrayList<String>();
		this.mMessagePool = new MessagePool<IMessage>();
		/* TODO populate message pool */
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces ICommunicationThread
	// ===========================================================
	@Override
	public void handlePassedMessage(Message pMessage) {
		Bundle bundle;
		String ip;
		byte[] data;
		switch (pMessage.what) {
		case ITCFlags.RECIEVE_MESSAGE_CLIENT:
			bundle = pMessage.getData();
			ip = bundle.getString("ip");
			data = bundle.getByteArray("data");
			this.mCommander.reconstructData(ip, data);
			break;
		}
	}

	@Override
	public WeakThreadHandler<IHandlerMessage> getParentHandler() {
		/* IGNORE, we don't need this */
		return null;
	}

	@Override
	public WeakThreadHandler<IHandlerMessage> getHandler() {
		/* IGNORE, we don't need this */
		return null;
	}

	@Override
	public void handleErrorMessage(String pAddress, MessageError pMessage) {
		/* TODO do we need to handle any errors here? */
	}

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public boolean isTerminated() {
		return false;
	}

	@Override
	public void terminate() {
		/* TODO cleanup */
	}

	@Override
	public void windowNotEmpty(String pAddress) {
		/* TODO handle this */
	}

	@Override
	public <T extends IMessage> int sendMessage(String pAddress, T pMessage, boolean pTCP) {
		/* TODO are we going to pass to lockstep network? */
		this.mLockstepNetwork.sendMessage(null, pMessage, pTCP);
		return 0;
	}

	@Override
	public IMessage obtainMessage(int pFlag) {
		return this.mMessagePool.obtainMessage(pFlag);
	}

	@Override
	public <T extends IMessage> void recycleMessage(T pMessage) {
		this.mMessagePool.recycleMessage(pMessage);
	}

	@Override
	public void addClient(String pAddress) {
		/* TODO Do we need to keep track here? */
		this.mClients.add(pAddress);
	}

	@Override
	public ArrayList<String> getClients() {
		/* TODO Do we need to keep track here? */
		return this.mClients;
	}

	@Override
	public void removeClient(String pAddress) {
		/* TODO Do we need to keep track here? */
		this.mClients.remove(pAddress);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces ICommand
	// ===========================================================
	@Override
	public <T extends IMessage> int sendCommand(T pMessage, ICommandCallback pCallback) {
		return this.mCommander.sendMessage(null, pMessage, false);
	}
	
	@Override
	public <T extends IMessage> int sendCommandReliable(T pMessage, ICommandCallback pCallback) {
		return this.mCommander.sendMessage(null, pMessage, true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
