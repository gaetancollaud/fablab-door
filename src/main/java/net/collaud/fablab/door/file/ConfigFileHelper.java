package net.collaud.fablab.door.file;

import net.collaud.fablab.common.file.KeyEnum;

/**
 *
 * @author gaetan
 */
public enum ConfigFileHelper implements KeyEnum {

	RFID_PORT_PREFIX("RFID_PORT_PREFIX"),
	USERS_FILE("USERS_FILE"),
	EVENT_LOG_FILE("EVENT_LOG_FILE"),
	IPX800_ADDR("IPX800_ADDR"),
	IPX800_PORT("IPX800_PORT"),
	WEBSERVICE_PORT("WEBSERVICE_PORT"),
	WEBSERVICE_TARGET_ADDR("WEBSERVICE_TARGET_ADDR"),
	WEBSERVICE_TARGET_PORT("WEBSERVICE_TARGET_PORT"),
	WEBSERVICE_TOKEN("WEBSERVICE_TOKEN"),
	OPEN_DOOR_SHORTLY_DELAY("OPEN_DOOR_SHORTLY_DELAY");

	private ConfigFileHelper(String key) {
		this.key = key;
	}

	private final String key;

	@Override
	public String getKey() {
		return key;
	}

}
