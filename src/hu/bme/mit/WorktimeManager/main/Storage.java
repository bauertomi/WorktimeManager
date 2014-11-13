package hu.bme.mit.WorktimeManager.main;

import java.util.ArrayList;

public class Storage {
	private ArrayList<Record> mRecords = new ArrayList<Record>();
	
	public Record getRow(int id){
		return mRecords.get(id);
	}
	
	public void addRow(Record record){
		mRecords.add(record);
	}
}
