package com.niffy.IsometricWorld.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niffy.AndEngineLockStepEngine.flags.IntendedFlag;
import com.niffy.AndEngineLockStepEngine.flags.MessageFlag;
import com.niffy.AndEngineLockStepEngine.messages.MessageAck;
import com.niffy.AndEngineLockStepEngine.options.IBaseOptions;
import com.niffy.AndEngineLockStepEngine.packet.PacketHandler;
import com.niffy.AndEngineLockStepEngine.threads.ICommunicationThread;

public class Commander extends PacketHandler {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(Commander.class);

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public Commander(final ICommunicationThread pParent, final IBaseOptions pBaseOptions) {
		super(pParent, pBaseOptions);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void reconstructData(String pAddress, byte[] pData) {
		try {
			InetAddress addressCast = InetAddress.getByName(pAddress);
			final ByteArrayInputStream bInput = new ByteArrayInputStream(pData);
			DataInputStream dis = new DataInputStream(bInput);
			final int version = dis.readInt();
			final int sequence = dis.readInt();
			final boolean requireAck = dis.readBoolean();
			final int intended = dis.readInt();
			final int flag = dis.readInt();
			this.handleIncomingPacket(addressCast, version, sequence, requireAck, intended, flag, dis, pData);
		} catch (UnknownHostException e) {
			log.error("Could not reconstruct data as could not cast address: {}", pAddress, e);
			/* TODO handle error */
		} catch (IOException e) {
			log.error("Could not reconstruct data. Error with input stream. Address: {}", pAddress, e);
			log.debug("Could not reconstruct data. Error with input stream. Data: {}", pData);
			/* TODO handle error */
		}
	}

	@Override
	protected void handleIncomingPacket(final InetAddress pFrom, final int pVersion, final int pSequence,
			final boolean pRequireAck, final int pIntended, final int pFlag, final DataInputStream pDataInput,
			final byte[] pData) throws IOException {
		if (!pRequireAck) {
			/* An ack is not required for this message so carry on processing */
		} else {
			/* An ack is required so send one! */
			MessageAck ack = (MessageAck) this.obtainMessage(MessageFlag.ACK);
			ack.setAckForSequnce(pSequence);
			ack.setIntended(IntendedFlag.NETWORK);
			ack.setRequireAck(false);
			this.mAckManager.addSentAck(pFrom, pSequence);
			this.sendMessage(pFrom, ack, false); /* We use ACKS for UDP don't need it for TCP! */
			this.recycleMessage(ack);
		}
		if (pIntended == IntendedFlag.CLIENT) {
			//this.passToClient(pFrom, pVersion, pSequence, pRequireAck, pIntended, pFlag, pDataInput, pData);
			/* TODO read in the remaining data */
		} else {
			final Object[] pArray = { pVersion, pIntended, pFlag, pSequence, pFrom };
			log.warn("Unknown intended recipient: Version: {} Intended: {} Flag: {} Sequence: {} From: {}", pArray);
		}
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
