package hu.bme.mit.WorktimeManager.main;

import java.util.Calendar;

import com.sun.jmx.snmp.Timestamp;

public class Record {
	private String mID;
	private Timestamp mTimeStamp;

	Calendar cal = Calendar.getInstance();

	public Record(Message message, Timestamp timeStamp) {
		mID = message.getID();
		mTimeStamp = timeStamp;
	}

	public void setID(String id) {
		mID = id;
	}

	public void setTimeStamp(Timestamp TimeStamp) {
		mTimeStamp = TimeStamp;
	}

	public Timestamp getTimeStamp() {
		Timestamp time = new Timestamp();
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
