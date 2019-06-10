package application;

import java.util.ArrayList;


public class RecordList {
	
	private ArrayList<Record> records = new ArrayList<Record>();
	
	public void setRecordList(ArrayList<Record> records) {
		this.records = records;
	}
	public ArrayList<Record> getRecordList(){
		return records;
	}
}