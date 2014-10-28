package net.collaud.fablab.raspberry.xml;

import java.util.Arrays;
import static junit.framework.TestCase.*;
import net.collaud.fablab.door.file.FileHelperFactory;
import net.collaud.fablab.door.xml.XmlParser;
import net.collaud.fablab.door.xml.entities.User;
import net.collaud.fablab.door.xml.entities.Users;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author gaetan
 */
public class TestXmlParser  {
	
	private final Users users1;
	
	@Before
	public void setup(){
		FileHelperFactory.FILE_CONFIG = "src/test/resource/config.properties";
	}

	public TestXmlParser() {
		User user11 = new User("rfid1", "username1");
		User user12 = new User("rfid2", "username2");
		users1 = new Users();
		users1.setListUsers(Arrays.asList(new User[]{
			user11,
			user12
		}));
	}

	@Test
	public void testReadWrite() {
		XmlParser.writeUsers(users1);
		
		Users users2 = XmlParser.readUsers();
		
		assertEquals("Read and write are not the same", users1, users2);
	}
}
