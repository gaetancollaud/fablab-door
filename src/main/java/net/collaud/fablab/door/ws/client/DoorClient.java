package net.collaud.fablab.door.ws.client;

import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import net.collaud.fablab.common.file.FileHelper;
import net.collaud.fablab.common.ws.WebServicePath;
import net.collaud.fablab.common.ws.client.AbstractClient;
import net.collaud.fablab.common.ws.data.DoorAction;
import net.collaud.fablab.common.ws.data.UserWithRFID;
import net.collaud.fablab.common.ws.exception.WebServiceException;
import net.collaud.fablab.door.file.ConfigFileHelper;
import net.collaud.fablab.door.file.FileHelperFactory;

/**
 *
 * @author gaetan
 */
public class DoorClient extends AbstractClient{
	private final WebTarget baseWebTarget;
	private final Client client;
	private final String token;

	public DoorClient() {
		client = ClientBuilder.newClient();
		FileHelper<ConfigFileHelper> config = FileHelperFactory.getConfig();
		token = config.get(ConfigFileHelper.WEBSERVICE_TOKEN);
		String host = "http://"+config.get(ConfigFileHelper.WEBSERVICE_TARGET_ADDR, "localhost")+":"+config.get(ConfigFileHelper.WEBSERVICE_TARGET_PORT, "80");
		baseWebTarget = client.target(host).path(WebServicePath.BASE_URL).path(WebServicePath.DOOR_URL);
	}

//	public OpenDoorResponse open(String rfid) throws  WebServiceException {
//		WebTarget resource = baseWebTarget.path(WebServicePath.DOOR_REQUEST_OPEN);
//		resource = resource.queryParam(WebServicePath.PARAM_RFID, rfid);
//		resource = resource.queryParam(WebServicePath.PARAM_TOKEN, token);
//		return requestXml(resource, OpenDoorResponse.class);
//	}
	
//	public void doorStatus(boolean doorOpen, boolean alarmOn, String lastRfid) throws WebServiceException{
//		WebTarget resource = baseWebTarget.path(WebServicePath.DOOR_STATUS);
//		resource = resource.queryParam(WebServicePath.PARAM_DOOR_OPEN, doorOpen);
//		resource = resource.queryParam(WebServicePath.PARAM_ALARM_ON, alarmOn);
//		resource = resource.queryParam(WebServicePath.PARAM_RFID, lastRfid);
//		requestXml(resource, Object.class);
//	}
	
	public List<UserWithRFID> getAuthorizedUsers() throws WebServiceException{
		WebTarget resource = baseWebTarget.path(WebServicePath.DOOR_AUTH_USERS);
		resource = resource.queryParam(WebServicePath.PARAM_TOKEN, token);
		return requestXml(resource, List.class);
	}
	
	public void doorEvent(String rfid, DoorAction action) throws WebServiceException{
		WebTarget resource = baseWebTarget.path(WebServicePath.DOOR_EVENT);
		resource = resource.queryParam(WebServicePath.PARAM_DOOR_EVENT_ACTION, action);
		resource = resource.queryParam(WebServicePath.PARAM_RFID, rfid);
		resource = resource.queryParam(WebServicePath.PARAM_TOKEN, token);
		requestXml(resource, Object.class);
	}
	

	public void close() {
		client.close();
	}
	
//	public static void main(String[] args) throws WebServiceException {
//		DoorClient dc = new DoorClient();
//		//OpenDoorResponse openResponse = dc.open("6F005C7E99D4", "salut");
//		OpenDoorResponse openResponse = dc.open("badsfas");
//		System.out.println("granted : " +openResponse.isGranted());
//		if(openResponse.isError()){
//			System.out.println("error "+openResponse.getErrors().get(0));
//		}
//		System.out.println("==================================");
//		dc.doorStatus(false, true, "myrfid");
//		dc.close();
//	}
	
}
