package net.collaud.fablab.door.xml.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author gaetan
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

	@XmlAttribute(required = true)
	private String rfid;

	@XmlAttribute(required = true)
	private String name;

	@XmlElement(name = "schedule")
	private List<Schedule> schedule;

	public User() {
	}

	public User(String rfid, String name, List<Schedule> schedule) {
		this.rfid = rfid;
		this.name = name;
		this.schedule = schedule;
	}

	public User(String rfid, String name) {
		this.rfid = rfid;
		this.name = name;
	}

	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Schedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}
	
	public boolean hasAccessAtDate(Calendar date){
		return schedule.stream().anyMatch(s -> s.hasAccessAtDate(date));
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 31 * hash + Objects.hashCode(this.rfid);
		hash = 31 * hash + Objects.hashCode(this.name);
		hash = 31 * hash + Objects.hashCode(this.schedule);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if (!Objects.equals(this.rfid, other.rfid)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.schedule, other.schedule)) {
			return false;
		}
		return true;
	}
}
