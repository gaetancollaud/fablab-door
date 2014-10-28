package net.collaud.fablab.raspberry.xml;

import java.util.Arrays;
import java.util.Calendar;
import static junit.framework.TestCase.*;
import net.collaud.fablab.door.file.FileHelperFactory;
import net.collaud.fablab.door.xml.XmlParser;
import net.collaud.fablab.door.xml.entities.Schedule;
import net.collaud.fablab.door.xml.entities.User;
import net.collaud.fablab.door.xml.entities.Users;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author gaetan
 */
public class TestSchedule  {
	private final User user1;
	private final User user2;
	
	@Before
	public void setup(){
		FileHelperFactory.FILE_CONFIG = "src/test/resource/config.properties";
	}

	public TestSchedule() {
		Schedule schedule1 = new Schedule("2,3,4,5,6", "8:00", "12:00");
		Schedule schedule2 = new Schedule("2,3,4,5,6", "13:00", "17:00");
		Schedule schedule3 = new Schedule("1,7", "7:00", "18:00");
		user1 = new User("rfid1", "username1", Arrays.asList(new Schedule[]{schedule1, schedule2}));
		user2 = new User("rfid2", "username2", Arrays.asList(new Schedule[]{schedule3}));
	}

	@Test
	public void testScheduleOk() {
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.MONDAY, 8, 00)));
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.TUESDAY, 8, 10)));
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.WEDNESDAY, 10, 10)));
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.THURSDAY, 11, 59)));
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.FRIDAY, 12, 00)));
		
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.MONDAY, 13, 00)));
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.TUESDAY, 13, 01)));
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.WEDNESDAY, 15, 30)));
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.THURSDAY, 16, 59)));
		assertTrue(user1.hasAccessAtDate(getCalendar(Calendar.FRIDAY, 17, 00)));
		
		assertTrue(user2.hasAccessAtDate(getCalendar(Calendar.SATURDAY, 7, 00)));
		assertTrue(user2.hasAccessAtDate(getCalendar(Calendar.SUNDAY, 7, 01)));
		assertTrue(user2.hasAccessAtDate(getCalendar(Calendar.SATURDAY, 12, 30)));
		assertTrue(user2.hasAccessAtDate(getCalendar(Calendar.SUNDAY, 17, 59)));
		assertTrue(user2.hasAccessAtDate(getCalendar(Calendar.SATURDAY, 18, 00)));
	}
	
	@Test
	public void testScheduleNoOk(){
		assertFalse(user1.hasAccessAtDate(getCalendar(Calendar.MONDAY, 7, 59)));
		assertFalse(user1.hasAccessAtDate(getCalendar(Calendar.FRIDAY, 12, 01)));
		
		assertFalse(user1.hasAccessAtDate(getCalendar(Calendar.MONDAY, 12, 59)));
		assertFalse(user1.hasAccessAtDate(getCalendar(Calendar.FRIDAY, 17, 01)));
		
		assertFalse(user1.hasAccessAtDate(getCalendar(Calendar.SATURDAY, 10, 00)));
		assertFalse(user1.hasAccessAtDate(getCalendar(Calendar.SUNDAY, 10, 00)));
		
		assertFalse(user2.hasAccessAtDate(getCalendar(Calendar.SATURDAY, 6, 59)));
		assertFalse(user2.hasAccessAtDate(getCalendar(Calendar.SUNDAY, 18, 01)));
		
		assertFalse(user2.hasAccessAtDate(getCalendar(Calendar.MONDAY, 10, 00)));
		assertFalse(user2.hasAccessAtDate(getCalendar(Calendar.TUESDAY, 10, 00)));
		assertFalse(user2.hasAccessAtDate(getCalendar(Calendar.WEDNESDAY, 10, 00)));
		assertFalse(user2.hasAccessAtDate(getCalendar(Calendar.THURSDAY, 10, 00)));
		assertFalse(user2.hasAccessAtDate(getCalendar(Calendar.FRIDAY, 10, 00)));
	}
	
	protected Calendar getCalendar(int dayOfWeek, int hour, int min){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		return c;
	}
}
