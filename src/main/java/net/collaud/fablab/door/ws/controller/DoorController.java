package net.collaud.fablab.door.ws.controller;

import net.collaud.fablab.common.ws.response.DoorStatusResponse;
import net.collaud.fablab.door.io.IOManager;
import org.apache.log4j.Logger;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 *
 * @author gaetan
 */
public class DoorController {
	private static final Logger LOG = Logger.getLogger(DoorController.class);

	//POST request
	public Object create(Request request, Response response) {String action = request.getQueryStringMap().get("action");
		LOG.debug("Request door action : "+action);
		if(action==null){
			return null;
		}
		IOManager io = IOManager.getInstance();
		switch(action){
			case "openPermanently":
				io.openDoorPermanently();
				break;
			case "openShortly":
				io.openDoorShortly();
				break;
			case "closePermanently":
				io.closeDoorPermanenlty();
				break;
			case "exit":
				io.closeDoorAndActivateAlarm();
				break;
			default:
				LOG.error("Unknown action : "+action);
		}
		return new DoorStatusResponse(io.isDoorOpen(), io.isAlarmOn());
	}

	//GET request
	public Object read(Request request, Response response) {
		LOG.debug("Request door status");
		IOManager io = IOManager.getInstance();
		return new DoorStatusResponse(io.isDoorOpen(), io.isAlarmOn());
	}

	public Object update(Request request, Response response) {
		return null;
	}

	public Object delete(Request request, Response response) {
		return null;
	}
}
