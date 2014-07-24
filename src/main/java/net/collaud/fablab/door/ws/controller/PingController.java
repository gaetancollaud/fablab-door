package net.collaud.fablab.door.ws.controller;

import net.collaud.fablab.common.ws.response.PingResponse;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 *
 * @author gaetan
 */
public class PingController {

	public Object create(Request request, Response response) {
		return null;
	}

	public Object read(Request request, Response response) {
		Object content = request.getQueryStringMap().get("content");
		return new PingResponse(String.valueOf(content));
	}

	public Object update(Request request, Response response) {
		return null;
	}

	public Object delete(Request request, Response response) {
		return null;
	}
}
