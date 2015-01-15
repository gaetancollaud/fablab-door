package net.collaud.fablab.door.xml.entities;

import java.util.Calendar;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import net.collaud.fablab.door.App;
import org.apache.log4j.Logger;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Schedule {

	private static final Logger LOG = Logger.getLogger(Schedule.class);

	public Schedule() {
	}

	public Schedule(String dayOfWeek, String start, String end) {
		this.dayOfWeek = dayOfWeek;
		this.start = start;
		this.end = end;
	}

	@XmlAttribute(required = true)
	private String dayOfWeek;

	@XmlAttribute(required = true)
	private String start;

	@XmlAttribute(required = true)
	private String end;

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public boolean hasAccessAtDate(Calendar date) {
		try {
			String[] dayOfWeekSplit = dayOfWeek.split(",");
			boolean dowOk = false;
			for (String dow : dayOfWeekSplit) {
				int dowInt = Integer.parseInt(dow);
				if (date.get(Calendar.DAY_OF_WEEK) == dowInt) {
					dowOk = true;
					break;
				}
			}
			if (!dowOk) {
				return false;
			}

			String[] startSplit = start.split(":");
			String[] endSplit = end.split(":");
			Integer startH = Integer.parseInt(startSplit[0]);
			Integer startM = Integer.parseInt(startSplit[1]);
			Integer endH = Integer.parseInt(endSplit[0]);
			Integer endM = Integer.parseInt(endSplit[1]);

			int hour = date.get(Calendar.HOUR_OF_DAY);
			int min = date.get(Calendar.MINUTE);

			if (hour < startH || hour > endH) {
				return false;
			}
			if (hour == startH && min < startM) {
				return false;
			}
			if (hour == endH && min > endM) {
				return false;
			}
			return true;
		} catch (Exception ex) {
			LOG.error("Cannot define if has access to schedule", ex);
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 37 * hash + Objects.hashCode(this.dayOfWeek);
		hash = 37 * hash + Objects.hashCode(this.start);
		hash = 37 * hash + Objects.hashCode(this.end);
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
		final Schedule other = (Schedule) obj;
		if (!Objects.equals(this.dayOfWeek, other.dayOfWeek)) {
			return false;
		}
		if (!Objects.equals(this.start, other.start)) {
			return false;
		}
		if (!Objects.equals(this.end, other.end)) {
			return false;
		}
		return true;
	}
}
