package hu.bme.mit.WorktimeManager.main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {
	private String mID;
	private Date mTimeStamp;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss");

	public Record(Message message, Date timeStamp) {
		mID = message.getID();
		mTimeStamp = timeStamp;
	}

	public void setID(String id) {
		mID = id;
	}

	public void setTimeStamp(Date timeStamp) {
		mTimeStamp = timeStamp;
	}

	public Date getTimeStamp() {
		return mTimeStamp;
	}
	
	public Object getColumn(int columnID) {
		switch (columnID) {
		case 0:
			return mDateFormat.format(mTimeStamp);
		case 1:
			return mID;
		default:
			throw new IllegalArgumentException();
		}
	}
}
