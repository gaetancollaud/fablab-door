package net.collaud.fablab.door.io.ipx800;

import java.util.Optional;
import net.collaud.fablab.common.file.FileHelper;
import net.collaud.fablab.door.file.ConfigFileHelper;
import net.collaud.fablab.door.file.FileHelperFactory;
import net.collaud.fablab.door.io.IOSystem;
import org.apache.log4j.Logger;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
public class IPX800 extends IOSystem {

	private static final Logger LOG = Logger.getLogger(IPX800.class);

	private Optional<IPX800Connector> connector;
	private IPX800ConnectorMaintainer maintainer;

	public IPX800() {
		maintainer = new IPX800ConnectorMaintainer();
		maintainer.start();
	}

	@Override
	public void setDoorOpen(boolean open) {
		connector.ifPresent(con -> con.setRelay(IPXRelays.Relay.RELAY_1, open));
	}

	@Override
	public void setAlarmOnPressed(boolean pressed) {
		connector.ifPresent(con -> con.setRelay(IPXRelays.Relay.RELAY_4, pressed));
	}

	@Override
	public void setAlarmOffPressed(boolean pressed) {
		connector.ifPresent(con -> con.setRelay(IPXRelays.Relay.RELAY_5, pressed));
	}

	@Override
	public void setLedAlarmOn(boolean on) {
	}

	@Override
	public void setLedDoorOpen(boolean on) {
	}

	@Override
	public void setLedDoorClose(boolean on) {
	}

	public class IPX800ConnectorMaintainer extends Thread {

		@Override
		public void run() {
			try {
				while (true) {
					FileHelper<ConfigFileHelper> config = FileHelperFactory.getConfig();
					IPX800Connector con = new IPX800Connector(config.get(ConfigFileHelper.IPX800_ADDR), config.getAsInt(ConfigFileHelper.IPX800_PORT));
					con.connect();
					connector = Optional.of(con);
					con.waitForDisconnect();
					LOG.error("IPX800 disconnected, try to connect again");
					Thread.sleep(1000);
				}
			} catch (InterruptedException ex) {
			}
		}
	}

}
