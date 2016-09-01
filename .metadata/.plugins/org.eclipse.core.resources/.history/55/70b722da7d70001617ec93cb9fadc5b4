package twy.burton.core;

import java.util.List;

public class Service {

	private String name;
	private String username;
	private String password;
	
	private List<ServiceExtra> extras;
	
	public Service(){
		
	}
	
	
	// ==== Basic Service Functions ====
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	// ==== Service Extras Functions ====
	public void addServiceExtra( ServiceExtra serviceExtra ){
		extras.add( serviceExtra );
	}
	
	public List<ServiceExtra> getServiceExtras(){
		return extras;
	}
	
	public ServiceExtra getServiceExtra( int i ){
		return extras.get(i);
	}
	
	public int getServiceExtrasSize(){
		return extras.size();
	}
	
}
