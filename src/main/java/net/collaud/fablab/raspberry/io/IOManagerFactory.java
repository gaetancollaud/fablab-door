package net.collaud.fablab.raspberry.io;

import java.net.Inet4Address;
import net.collaud.fablab.raspberry.Constants;

/**
 *
 * @author gaetan
 */
abstract public class IOManagerFactory {

	private static IPX800 ipx800;

	public static RelayManager getBestRelayManager() {
		if (ipx800 == null) {
			ipx800 = new IPX800(Constants.IPX800_ADDR, Constants.IPX800_PORT);
		}
		return ipx800;
	}

}
