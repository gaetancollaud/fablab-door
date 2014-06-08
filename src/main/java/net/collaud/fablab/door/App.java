package net.collaud.fablab.door;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import net.collaud.fablab.door.file.ConfigFileHelper;
import net.collaud.fablab.door.file.FileHelperFactory;
import net.collaud.fablab.door.io.IOManager;
import net.collaud.fablab.door.serial.SerialInterface;
import net.collaud.fablab.door.serial.SerialInterfaceFactory;
import net.collaud.fablab.door.ws.WebServiceServer;
import net.collaud.fablab.door.xml.XmlParser;
import net.collaud.fablab.door.xml.entities.User;
import net.collaud.fablab.door.xml.entities.Users;
import org.apache.log4j.Logger;

public class App implements Observer {

	private static final Logger LOG = Logger.getLogger(App.class);

	private static SerialInterface itf;

	private final IOManager ioManager;

	private final Map<String, User> users;

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		LOG.info("Starting application");
		App app = new App();
		LOG.info("Application started in " + (System.currentTimeMillis() - time) + "ms");
		app.run();
	}

	public void createOneUser() {
		LOG.info("create one User");
		Users listUsers = new Users();
		listUsers.getListUsers().add(new User("6F005CC09467", "gaetan"));
		XmlParser.writeUsers(listUsers);
	}

	public App() {
		WebServiceServer.getInstance(); //start webservice

		Users usersTmp = XmlParser.readUsers();

		ioManager = IOManager.getInstance();

		users = new HashMap<>();
		usersTmp.getListUsers().stream().forEach((u) -> users.put(u.getRfid().toUpperCase(), u));

		LOG.info("Start serial interface");
		itf = SerialInterfaceFactory.getBestInterface(FileHelperFactory.getConfig().get(ConfigFileHelper.RFID_PORT_PREFIX));
		itf.addObserver(this);

	}

	public void run() {
		try {
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

				ioManager.openDoorShortly();
			}
		} else {
			LOG.error("Observer received wrong arg : " + arg.toString());
		}
	}
}
