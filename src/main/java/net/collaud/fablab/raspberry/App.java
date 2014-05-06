package net.collaud.fablab.raspberry;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import net.collaud.fablab.raspberry.io.IOManagerFactory;
import net.collaud.fablab.raspberry.io.RelayManager;
import net.collaud.fablab.raspberry.serial.SerialInterface;
import net.collaud.fablab.raspberry.serial.SerialInterfaceFactory;
import net.collaud.fablab.raspberry.xml.XmlParser;
import net.collaud.fablab.raspberry.xml.entities.User;
import net.collaud.fablab.raspberry.xml.entities.Users;
import org.apache.log4j.Logger;

public class App implements Observer {

	private static final Logger LOG = Logger.getLogger(App.class);

	private static SerialInterface itf;

	private final RelayManager relayManager;

	private final Map<String, User> users;

	public static void main(String[] args) {
		LOG.info("Starting application");
		App app = new App();
		app.run();
	}

	public void createOneUser() {
		LOG.info("create one User");
		Users listUsers = new Users();
		listUsers.getListUsers().add(new User("6F005CC09467", "gaetan"));
		XmlParser.writeUsers(listUsers);
	}

	public App() {
		Users usersTmp = XmlParser.readUsers();

		relayManager = IOManagerFactory.getBestRelayManager();

		users = new HashMap<>();
		for (User u : usersTmp.getListUsers()) {
			users.put(u.getRfid().toUpperCase(), u);
		}

		LOG.info("Start serial interface");
		itf = SerialInterfaceFactory.getBestInterface(Constants.RFID_PORT_PREFIX);
		itf.addObserver(this);
	}

	public void run() {
		int v = 1;
		try {
//			while (v == 1) {
//				Thread.sleep(5000);
//				relayManager.setRelay(RelayManager.Relay.RELAY_DOOR, true);
//				Thread.sleep(5000);
//				relayManager.setRelay(RelayManager.Relay.RELAY_DOOR, false);
//
//			}

			while (true) {
				Thread.sleep(10 * 60 * 1000);
				LOG.info("Still alive");
			}
		} catch (InterruptedException ex) {
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof String) {
			String rfid = ((String) arg).trim().toUpperCase();
			User u = users.get(rfid);
			if (u == null) {
				LOG.warn("Refused for RFID " + rfid);
			} else {
				LOG.info("Granted for user " + u.getName());
				relayManager.setRelay(RelayManager.Relay.RELAY_DOOR, true);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException ex) {
				}
				relayManager.setRelay(RelayManager.Relay.RELAY_DOOR, false);
			}
		} else {
			LOG.error("Observer received wrong arg : " + arg.toString());
		}
	}
}
