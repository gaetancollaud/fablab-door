package net.collaud.fablab.door;

/**
 *
 * @author gaetan
 */
public class Constants {
	public static final String RFID_PORT_PREFIX = "/dev/ttyUSB";
	public static final String USER_FILE = "/fablab/users.xml";
	public static final String EVENTS_LOG_FILE = "events.log";
	
	public static final String IPX800_ADDR = "192.168.170.4";
	public static final int IPX800_PORT = 9870;
	public static final int WEBSERVICE_PORT = 8083;
	public static final int DOOR_OPEN_SHORTLY_DELAY = 5*1000; //5 sec
}
