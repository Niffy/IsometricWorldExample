package com.niffy.IsometricWorld.network;

import com.niffy.AndEngineLockStepEngine.flags.MessageFlag;
import com.niffy.AndEngineLockStepEngine.messages.IMessage;

public interface ICommand {

	/**
	 * Send a command to everyone
	 * 
	 * @param pCallback
	 *            {@link ICommandCallback} to call when an Ack has been
	 *            acknowledge.
	 * @param pMessage
	 *            Message to send, <b>MUST</b> extend {@link IMessage}
	 * @return {@link Integer} with the command number sent.
	 */
	public <T extends IMessage> int sendCommand(final T pMessage, final ICommandCallback pCallback);
	
	public <T extends IMessage> int sendCommandReliable(final T pMessage, final ICommandCallback pCallback);

	/**
	 * Obtain a message.
	 * 
	 * @param pFlag
	 *            {@link Integer} of flag from {@link MessageFlag}
	 * @return message which extends {@link IMessage}, to use
	 * @throws MessagePoolException
	 */
	public IMessage obtainMessage(final int pFlag);

	/**
	 * Recycle a message
	 * 
	 * @param pMessage
	 *            {@link IMessage} to put back into the pool
	 */
	public <T extends IMessage> void recycleMessage(final T pMessage);
}
