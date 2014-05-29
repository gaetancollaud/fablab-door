package net.collaud.fablab.door;

import net.collaud.fablab.webservice.response.PingResponse;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 *
 * @author gaetan
 */
public class WSPingController {

	private static final String STATUS_RESPONSE_HEADER = "http_response_code";

	public Object create(Request request, Response response) {
		return new PingResponse("create");
	}

	public Object read(Request request, Response response) {
		Object content = request.getQueryStringMap().get("content");
		return new PingResponse(String.valueOf(content));
	}

	public Object update(Request request, Response response) {
		return new PingResponse("update");
	}

	public Object delete(Request request, Response response) {
		return new PingResponse("delete");
	}
}
