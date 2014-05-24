package net.collaud.fablab.door.ws.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import net.collaud.fablab.webservice.response.OpenDoorResponse;

/**
 * Jersey REST client generated for REST resource:DoorResource [door]<br>
 * USAGE:
 * <pre>
 *        DoorClient client = new DoorClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author gaetan
 */
public class DoorClient {
	private WebTarget webTarget;
	private Client client;
	private static final String BASE_URI = "http://localhost:8080//api";

	public DoorClient() {
		client = javax.ws.rs.client.ClientBuilder.newClient();
		webTarget = client.target(BASE_URI).path("door");
	}

	public OpenDoorResponse open(String rfid, String token) throws ClientErrorException {
		WebTarget resource = webTarget;
		resource = resource.queryParam("rfid", rfid);
		resource = resource.queryParam("token", token);
		resource = resource.path("open");
		return resource.request(MediaType.APPLICATION_XML).get(OpenDoorResponse.class);
	}

	public void close() {
		client.close();
	}
	
	public static void main(String[] args) {
		DoorClient dc = new DoorClient();
		OpenDoorResponse openResponse = dc.open("6F005C7E99D4", "salut");
		System.out.println("granted : " +openResponse.isGranted());
		if(openResponse.isError()){
			System.out.println("error "+openResponse.getErrors().get(0));
		}
	}
	
}
