package twy.burton.core.service;

import java.util.ArrayList;
import java.util.List;

import twy.burton.utilities.FileAccess;

public class Service {

	private String name;
	private String username;
	private String password;
	
	private List<ServiceExtra> extras;
	
	public Service(){
		extras = new ArrayList<ServiceExtra>();
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
	
	public byte[] getServiceBytes(){
		
		// Get service extra stuff
		int extraLength = 0;
		for( int i = 0 ; i < extras.size(); i++ ){
			extraLength += 4;
			extraLength += extras.get(i).getServiceExtraBytes().length;
		}
		
		// Get all
		byte[] nameBytes = name.getBytes();
		byte[] usernameBytes = username.getBytes();
		byte[] passwordBytes = password.getBytes(); 
		
		// Get total length
		int length = 4 + 4 + 4 + 4 + nameBytes.length + usernameBytes.length + passwordBytes.length + extraLength;
		
		byte[] data = new byte[length];
		int x = 0;
		
		// Add basic
		// Service Name
		byte[] number = FileAccess.int_to_bb_le(nameBytes.length);
		for( int i = 0 ; i < 4; i++ )
			data[x++] = number[i];
		
		for( int i = 0 ; i < nameBytes.length; i++ )
			data[x++] = nameBytes[i];
		
		// Service username
		number = FileAccess.int_to_bb_le(usernameBytes.length);
		for( int i = 0 ; i < 4; i++ )
			data[x++] = number[i];
		
		for( int i = 0 ; i < usernameBytes.length; i++ )
			data[x++] = usernameBytes[i];
		
		// Service Password
		number = FileAccess.int_to_bb_le(passwordBytes.length);
		for( int i = 0 ; i < 4; i++ )
			data[x++] = number[i];
		
		for( int i = 0 ; i < passwordBytes.length; i++ )
			data[x++] = passwordBytes[i];
		
		// Extras
		number = FileAccess.int_to_bb_le(extras.size());
		for( int i = 0 ; i < 4; i++ )
			data[x++] = number[i];
		
		for( int i = 0 ; i < extras.size(); i++ ){
			
			byte[] extra = extras.get(i).getServiceExtraBytes();
			number = FileAccess.int_to_bb_le(extra.length);
			for( int j = 0 ; j < 4; j++ )
				data[x++] = number[j];
			
			for( int j = 0; j < extra.length; j++ ){
				data[x++] = extra[j];
			}
			
		
		}
		
		return data;
	}
	
}