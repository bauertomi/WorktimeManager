package hu.bme.mit.WorktimeManager.main;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class Message {
	private String mID;

	public static Message parse(byte[] data) throws BufferUnderflowException {
		final ByteBuffer bb = ByteBuffer.wrap(data);

		while (bb.getInt(0) != 0xDEADBEEF) {
			bb.get();
		}

		if (bb.getInt() == 0xDEADBEEF) {
			byte chk = (byte) ((byte) 0xDE + (byte) 0xAD + (byte) 0xBE + (byte) 0xEF);
			byte len = bb.get();
			byte[] uidData = new byte[10];
			bb.get(uidData);
			byte checksum = bb.get();

			chk += len;
			for (byte b : uidData) {
				chk += b;
			}
			if (chk == checksum) {

				StringBuilder uidBuilder = new StringBuilder();
				for (int i = len - 1; i >= 0; i--) {
					uidBuilder.append(Integer.toHexString(uidData[i] & 0xFF));
				}

				return new Message(uidBuilder.toString());
			} else {
				return null;
			}
		}

		return null;
	}

	public Message(String id) {
		mID = id;
	}

	public String getID() {
		return mID;
	}

	public void setID(String id) {
		mID = id;
	}

}
