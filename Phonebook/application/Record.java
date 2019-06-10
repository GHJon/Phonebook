package application;

public class Record {
	private String name;
	private String state;
	private String phone;
	
	public Record(String name, String state, String phone) {
			this.name = name;
			this.state = state;
			this.phone = phone;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	

}
