package net.collaud.fablab.raspberry.xml.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author gaetan
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

	public User() {
	}

	public User(String RFID, String name) {
		this.rfid = RFID;
		this.name = name;
	}

	@XmlAttribute(required = true)
	private String rfid;

	@XmlAttribute(required = true)
	private String name;

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
}
