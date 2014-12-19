package hu.bme.mit.WorktimeManager.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <p>
 * Az osztĂˇly a jĂˇtĂ©k hĂˇlĂłzati interfĂ©szĂ©nek kliens oldalĂˇt valĂłsĂ­tja meg.
 * </p>
 * <p>
 * A tĂˇvoli gĂ©pen futĂł szervernek a {@link ControlEvent} osztĂˇly Ăˇltal
 * reprezentĂˇlt vezĂ©rlĹ‘utasĂ­tĂˇsokat kĂĽld, Ă©s a jĂˇtĂ©k aktuĂˇlis ĂˇllapotĂˇt tĂˇrolĂł
 * {@link GameState} osztĂˇlyt fogad a szervertĹ‘l.
 * </p>
 */
public class NetworkClient extends NetworkHelper<String, String> {

	//3000 volt
	private static final int TIMEOUT = 30000;

	/**
	 * A hĂˇlĂłzati kapcsolat fĹ‘bb kliensoldali esemĂ©nyeinek lekezelĂ©sĂ©t lehetĹ‘vĂ©
	 * tĂ©vĹ‘ interfĂ©sz.
	 */
	public interface NetworkClientListener extends NetworkReceiveListener, NetworkConnectionListener {
	}

	public NetworkClient() {
	}

	public NetworkClient(NetworkClientListener listener) throws NullPointerException {
		this();
		addConnectionListener(listener);
		addReceiveListener(listener);
	}

	public NetworkClient(NetworkReceiveListener listener) throws NullPointerException {
		this();
		addReceiveListener(listener);
	}

	public NetworkClient(NetworkConnectionListener listener) throws NullPointerException {
		this();
		addConnectionListener(listener);
	}

	/**
	 * KapcsolĂłdik a paramĂ©terben kapott IP cĂ­mre. A kapcsolat lĂ©trejĂ¶ttĂ©t a
	 * {@link NetworkClientListener} interfĂ©sz
	 * {@link NetworkClientListener#onConnect()} fĂĽggvĂ©ny meghĂ­vĂˇsa jelzi.
	 * Sikertelen kapcsolĂłdĂˇs, vagy idĹ‘ tĂşllĂ©pĂ©s esetĂ©n a
	 * {@link NetworkClientListener#onDisconnect()} fĂĽggvĂ©ny hĂ­vĂłdik.
	 * 
	 * @param address
	 *            A cĂ­m, amihez kapcsolĂłdni szeretnĂ©nk
	 * @throws IOException
	 */
	public void connect(InetAddress address) throws IOException {
		try {
			mClientSocket = new Socket();
			mClientSocket.connect(new InetSocketAddress(address, PORT), TIMEOUT);
			initCommunication();

			if (mReceiveListeners != null) {
				synchronized (mReceiveListeners) {
					for (NetworkConnectionListener listener : mConnectionListeners) {
						listener.onConnect();
					}
				}
			}

			startReceiving();
		} catch (IOException e) {
			disconnect();
			throw e;
		}
	}
}
