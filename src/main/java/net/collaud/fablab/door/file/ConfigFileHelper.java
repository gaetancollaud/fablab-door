package net.collaud.fablab.door.file;

import net.collaud.fablab.common.file.KeyEnum;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
public enum ConfigFileHelper implements KeyEnum {

	RFID_PORT_PREFIX,
	USERS_FILE,
	EVENT_LOG_FILE,
	IPX800_ADDR,
	IPX800_PORT,
	WEBSERVICE_PORT,
	WEBSERVICE_TARGET_ADDR,
	WEBSERVICE_TARGET_PORT,
	WEBSERVICE_TOKEN,
	OPEN_DOOR_SHORTLY_DELAY;


	@Override
	public String getKey() {
		return toString();
	}

}
