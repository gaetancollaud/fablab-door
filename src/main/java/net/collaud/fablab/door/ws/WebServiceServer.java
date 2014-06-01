package net.collaud.fablab.door.ws;

import net.collaud.fablab.door.Constants;
import net.collaud.fablab.door.ws.controller.DoorController;
import net.collaud.fablab.door.ws.controller.PingController;
import net.collaud.fablab.webservice.WebServicePath;
import org.apache.log4j.Logger;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.pipeline.MessageObserver;

/**
 *
 * @author gaetan
 */
public class WebServiceServer {
	
	private static final Logger LOG = Logger.getLogger(WebServiceServer.class);
	
	private static WebServiceServer instance;
	
	public static WebServiceServer getInstance() {
		if (instance == null) {
			instance = new WebServiceServer();
		}
		return instance;
	}
	
	private final RestExpress server;
	
	private WebServiceServer() {
		server = new RestExpress();
		server.setPort(Constants.WEBSERVICE_PORT);
		
		registerUrl("/" + WebServicePath.BASE_URL + "/" + WebServicePath.PING_URL, new PingController());
		registerUrl("/" + WebServicePath.BASE_URL + "/" + WebServicePath.DOOR_URL, new DoorController());
		
		server.addMessageObserver(new MessageObserver(){
			@Override
			protected void onException(Throwable exception, Request request, Response response) {
				LOG.error("Webservice exception with request "+request.getUrl(), exception);
			}
		});
		
		server.bind();
		LOG.info("Rest server started");
	}
	
	private void registerUrl(String url, Object controller) {
		LOG.debug("Register url " + url + " to ctrl " + controller.getClass().getName());
		server.uri(url, controller);
		
	}
}
