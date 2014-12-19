package hu.bme.mit.WorktimeManager.main;

import java.util.Calendar;
import java.util.Date;

import com.sun.jmx.snmp.Timestamp;

public class Record {
	private String mID;
	private Date mTimeStamp;

	Calendar cal = Calendar.getInstance();

	public Record(Message message, Date timeStamp) {
		mID = message.getID();
		mTimeStamp = timeStamp;
	}

	public void setID(String id) {
		mID = id;
	}

	public void setTimeStamp(Date TimeStamp) {
		mTimeStamp = TimeStamp;
	}

	public Date getTimeStamp() {
		Date time = new Date();
		return time;
	}

	public Object getColumn(int columnID) {
		switch (columnID) {
		case 0:
			return mTimeStamp;
		case 1:
			return mID;
		default:
			throw new IllegalArgumentException();
		}
	}
}
