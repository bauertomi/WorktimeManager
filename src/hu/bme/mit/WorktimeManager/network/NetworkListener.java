package hu.bme.mit.WorktimeManager.network;

import hu.bme.mit.WorktimeManager.network.NetworkDiscover.NetworkDiscoverListener;
import hu.bme.mit.WorktimeManager.network.NetworkHelper.NetworkConnectionListener;

import java.net.InetAddress;

public class NetworkListener implements NetworkConnectionListener, NetworkDiscoverListener {



	@Override
	public void onConnect() {
	}

	@Override
	public void onDisconnect() {
	}

	@Override
	public void onDiscover(InetAddress address, NetworkDiscover networkDiscover) {
	}

	@Override
	public void onDiscoveredTimeout(InetAddress address) {
	}

}

