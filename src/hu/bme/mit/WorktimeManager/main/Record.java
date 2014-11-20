package hu.bme.mit.WorktimeManager.main;

import java.util.Date;


public class Record {
	private String mID;
	private Date mTimeStamp;
	
	public Record(Message message, Date timeStamp) {
		mID = message.getID();
		mTimeStamp = timeStamp;
	} 
	
	public void setID(String id) {
		mID = id;
	}
	
	public Object getColumn(int columnID){
		switch (columnID) {
		case 0:
			return mID;
//TODO
		default:
			throw new IllegalArgumentException();
		}
	}
}
