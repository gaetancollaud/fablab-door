package net.collaud.fablab.door.ws.controller;

import net.collaud.fablab.common.ws.exception.WebServiceException;
import net.collaud.fablab.door.file.ConfigFileHelper;
import net.collaud.fablab.door.file.FileHelperFactory;
import org.restexpress.Request;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
public class AbstractController {

	protected void checkToken(Request request) throws WebServiceException {
		String token = request.getQueryStringMap().get("token");
		String tokenConf = FileHelperFactory.getConfig().get(ConfigFileHelper.WEBSERVICE_TOKEN);
		if(token==null || !token.equals(tokenConf)){
			throw new WebServiceException("Invalid token : "+token);
		}
	}
}
