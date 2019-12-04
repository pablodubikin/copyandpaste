package pab.copyandpaste.model;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * This class has timestamps and add/mod users, for every other class to extend.
 *	 
 * @author pab
 */
public class DbObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer addUser;
	
	private Integer modUser;
	
	private DateTime addDate;
	
	private DateTime modDate;

	public DbObject() {
		super();
	}

	public DbObject(Integer addUser, Integer modUser, DateTime addDate, DateTime modDate) {
		super();
		this.addUser = addUser;
		this.modUser = modUser;
		this.addDate = addDate;
		this.modDate = modDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addDate == null) ? 0 : addDate.hashCode());
		result = prime * result + ((addUser == null) ? 0 : addUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbObject other = (DbObject) obj;
		if (addDate == null) {
			if (other.addDate != null)
				return false;
		} else if (!addDate.equals(other.addDate))
			return false;
		if (addUser == null) {
			if (other.addUser != null)
				return false;
		} else if (!addUser.equals(other.addUser))
			return false;
		if (modDate == null) {
			if (other.modDate != null)
				return false;
		} else if (!modDate.equals(other.modDate))
			return false;
		if (modUser == null) {
			if (other.modUser != null)
				return false;
		} else if (!modUser.equals(other.modUser))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DbObject [addUser=" + addUser + ", modUser=" + modUser + ", addDate=" + addDate + ", modDate=" + modDate
				+ "]";
	}

	public Integer getAddUser() {
		return addUser;
	}

	public void setAddUser(Integer addUser) {
		this.addUser = addUser;
	}

	public Integer getModUser() {
		return modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	public DateTime getAddDate() {
		return addDate;
	}

	public void setAddDate(DateTime addDate) {
		this.addDate = addDate;
	}

	public DateTime getModDate() {
		return modDate;
	}

	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}
}
