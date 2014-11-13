package hu.bme.mit.WorktimeManager.main;


public class Record {
	private String mID;
	
	public Record(Message message) {
		mID = message.getID();
	} 
	
	public void setID(String id) {
		mID = id;
	}
	
}
