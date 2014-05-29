package net.collaud.fablab.door;

import net.collaud.fablab.webservice.WebServicePath;
import org.restexpress.RestExpress;

/**
 *
 * @author gaetan
 */
public class WebServiceServer {
	public static void main(String[] args) {
		RestExpress server = new RestExpress();
		server.setPort(8083);
		server.setName("Rest service");
		
		server.uri("/"+WebServicePath.BASE_URL+"/"+WebServicePath.PING_URL, new WSPingController());
		
		server.bind();
		server.awaitShutdown();
	}
}
