package net.collaud.fablab.raspberry.xml;

import java.io.File;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.collaud.fablab.raspberry.Constants;
import net.collaud.fablab.raspberry.xml.entities.Users;
import org.apache.log4j.Logger;

/**
 *
 * @author gaetan
 */
abstract public class XmlParser {

	private static final Logger LOG = Logger.getLogger(XmlParser.class);

	public static Users readUsers() {
		LOG.debug("Reading users");
		try {
			JAXBContext jc = JAXBContext.newInstance(Users.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File xml = new File(Constants.USER_FILE);
			Users users = (Users) unmarshaller.unmarshal(xml);
			return users;
		} catch (JAXBException ex) {
			LOG.error("Cannot read users", ex);
		}
		return new Users();
	}

	public static void writeUsers(Users users) {
		LOG.debug("Writing users");
		try {
			JAXBContext jc = JAXBContext.newInstance(Users.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			File xml = new File(Constants.USER_FILE);
			marshaller.marshal(users, xml);
		} catch (JAXBException ex) {
			LOG.error("Cannot write users", ex);
		}
	}
}
