package hu.bme.mit.WorktimeManager.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A hĂˇlĂłzati rĂ©teg kĂ¶zĂ¶s elemeit egyesĂ­tĹ‘ absztrakt osztĂˇly
 * 
 * @param <ReceiveType>
 *            A vĂˇrt objektumok tĂ­pusa
 * @param <SendType>
 *            A kĂĽldĂ¶tt objektumok tĂ­pusa
 */
public abstract class NetworkHelper<ReceiveType, SendType> {

	protected static final int PORT = 8766;

	protected Socket mClientSocket;
	private InputStream mInput;
	private ObjectOutputStream mOutput;
	private ReceiverThread mReceiverThread;
	protected ArrayList<NetworkConnectionListener> mConnectionListeners = new ArrayList<>();
	protected ArrayList<NetworkReceiveListener> mReceiveListeners = new ArrayList<>();

	/**
	 * A hĂˇlĂłzati kapcsolat ĂˇllapotvĂˇltozĂˇsait jelzĹ‘ interfĂ©sz.
	 */
	public interface NetworkConnectionListener {
		/**
		 * Akkor hĂ­vĂłdik, ha sikerĂĽlt csatlakozni a mĂˇsik jĂˇtĂ©koshoz.
		 */
		void onConnect();

		/**
		 * Akkor hĂ­vĂłdik, ha megszakadt a kapcsolat a mĂˇsik jĂˇtĂ©kossal.
		 */
		void onDisconnect();
	}

	/**
	 * A hĂˇlĂłzati kapcsolaton Ăşj adat beĂ©rkezĂ©sĂ©t jelzĹ‘ interfĂ©sz.
	 * 
	 * @param <ReceiveType>
	 *            A vĂˇrt objektum tĂ­pusa
	 */
	public interface NetworkReceiveListener {
		/**
		 * Akkor hĂ­vĂłdik, ha Ăşj objektum Ă©rkezett a hĂˇlĂłzaton.
		 * 
		 * @param data
		 *            A beĂ©rkezett objektum
		 */
		void onReceive(String data);
	}

	public void addConnectionListener(NetworkConnectionListener listener) throws NullPointerException {
		if (listener == null) {
			throw new NullPointerException();
		}
		synchronized (mConnectionListeners) {
			mConnectionListeners.add(listener);
		}
	}

	public void removeConnectionListener(NetworkConnectionListener listener) {
		synchronized (mConnectionListeners) {
			mConnectionListeners.remove(listener);
		}
	}

	public void addReceiveListener(NetworkReceiveListener listener) throws NullPointerException {
		if (listener == null) {
			throw new NullPointerException();
		}
		synchronized (mReceiveListeners) {
			mReceiveListeners.add(listener);
		}
	}

	public void removeReceiveListener(NetworkReceiveListener listener) {
		synchronized (mReceiveListeners) {
			mReceiveListeners.remove(listener);
		}
	}

	protected class ReceiverThread extends Thread {

		private AtomicBoolean mRunning = new AtomicBoolean(true);
		private ArrayList<NetworkReceiveListener> mListeners;
		//private Calendar mCalendar = Calendar.getInstance();

		public ReceiverThread(ArrayList<NetworkReceiveListener> listeners) {
			mListeners = listeners;
		}

		@Override
		public void run() {
			if (mInput == null) {
				return;
			}
			while (mRunning.get()) {
				try {
					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
					final byte[] buff = new byte[1024];
					mInput.read(buff);
					final ByteBuffer bb = ByteBuffer.wrap(buff);

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
							for (int i = len-1; i >= 0; i--) {
								uidBuilder.append(Integer.toHexString(uidData[i] & 0xFF));
							}

							System.out.println("New RFID uid: " + uidBuilder.toString());

							if (mListeners != null) {
								synchronized (NetworkHelper.this.mReceiveListeners) {
									for (NetworkReceiveListener listener : mListeners) {
										listener.onReceive(uidBuilder.toString());
									}
								}
							}
						} else {
							System.out.println("Packet received with wrong checksum.");
						}
					}

				} catch (BufferUnderflowException e) {
					// loop back
				} catch (InterruptedException e) {
					return;
				} catch (Exception e) {
					// e.printStackTrace();
					System.err.println("Stopped receiving data from the network.");
					disconnect();
					return;
				}
			}
		}

		public void setRunning(boolean run) {
			mRunning.set(run);
		}
	}

	protected void initCommunication() throws IOException {
		mOutput = new ObjectOutputStream(mClientSocket.getOutputStream());
		mOutput.flush();
		mInput = mClientSocket.getInputStream();
	}

	/**
	 * A {@link ReceiverThread} indĂ­tĂˇsa.
	 */
	protected void startReceiving() {
		mReceiverThread = new ReceiverThread(mReceiveListeners);
		mReceiverThread.start();
	}

	/**
	 * A {@link ReceiverThread} leĂˇllĂ­tĂˇsa.
	 */
	protected void stopReceiving() {
		if (mReceiverThread != null) {
			try {
				mReceiverThread.setRunning(false);
				mReceiverThread.interrupt();
				mReceiverThread.join();
				mReceiverThread = null;
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Kapcsolat bontĂˇsa. MeghĂ­vĂˇsa kĂ¶telezĹ‘ az erĹ‘forrĂˇsok
	 * felszabadĂ­tĂˇsa Ă©rdekben.
	 */
	public void disconnect() {
		stopReceiving();

		try {
			mInput.close();
		} catch (Exception e) {
		} finally {
			mInput = null;
		}
		try {
			mOutput.close();
		} catch (Exception e) {
		} finally {
			mOutput = null;
		}
		try {
			mClientSocket.close();
		} catch (Exception e) {
		} finally {
			mClientSocket = null;
		}
		try {
			ArrayList<NetworkConnectionListener> listeners = new ArrayList<>();
			synchronized (mConnectionListeners) {
				for (NetworkConnectionListener listener : mConnectionListeners) {
					listeners.add(listener);
				}
			}
			for (NetworkConnectionListener listener : listeners) {
				listener.onDisconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adat kĂĽldĂ©se a mĂˇsik gĂ©pnek
	 * 
	 * @param data
	 *            A kĂĽldendĹ‘ objektum
	 * @return Sikeres kĂĽldĂ©s esetĂ©n true, egyĂ©bkĂ©nt false
	 */
	protected boolean send(SendType data) {
		if (mClientSocket == null || mOutput == null) {
			return false;
		}
		try {
			mOutput.reset();
			mOutput.writeObject(data);
			mOutput.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
