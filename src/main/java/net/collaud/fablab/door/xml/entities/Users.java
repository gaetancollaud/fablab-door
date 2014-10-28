package net.collaud.fablab.door.xml.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gaetan
 */
@XmlRootElement(name="users")
@XmlAccessorType(XmlAccessType.FIELD)
public class Users {
	
	@XmlElement(name="user")
	private List<User> listUsers;

	public Users() {
		listUsers = new ArrayList<>();
	}

	public List<User> getListUsers() {
		return listUsers;
	}

	public void setListUsers(List<User> listUsers) {
		this.listUsers = listUsers;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 23 * hash + Objects.hashCode(this.listUsers);
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
		final Users other = (Users) obj;
		if (!Objects.equals(this.listUsers, other.listUsers)) {
			return false;
		}
		return true;
	}
}
