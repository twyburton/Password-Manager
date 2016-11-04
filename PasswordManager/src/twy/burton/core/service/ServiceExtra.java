package twy.burton.core.service;

import twy.burton.utilities.FileAccess;

public class ServiceExtra {

	// This is extra data that can be attached to a service 
	// such as secret questions. e.g. Memorable place
	
	private String key;
	private String value;
	
	public ServiceExtra( String key, String value){
		this.key = key;
		this.value = value;
	}
	
	public ServiceExtra(){
		
	}
	
	public void setKey( String key ){
		this.key = key;
	}
	
	public void setValue( String value ){
		this.value = value;
	}
	
	public String getKey(){
		return key;
	}
	
	public String getValue(){
		return value;
	}
	
	public byte[] getServiceExtraBytes(){
		
		// Get the total length
		int servicelength = 4 + 4 + key.getBytes().length + value.getBytes().length;
		
		// Init data array
		byte[] data = new byte[servicelength];
		int i = 0;
		
		// Get data
		byte[] keyBytes = key.getBytes();
		byte[] valueBytes = value.getBytes();
		
		byte[] keyLength = FileAccess.int_to_bb_le( key.getBytes().length );
		byte[] valueLength = FileAccess.int_to_bb_le( value.getBytes().length );
		
		// Write data
		// Key Length
		for( int j = 0 ; j < 4; j++ )
			data[i++] = keyLength[j];
		
		// Key
		for( int j = 0 ; j < keyBytes.length; j++ )
			data[i++] = keyBytes[j];
		
		// Value Length
		for( int j = 0 ; j < 4; j++ )
			data[i++] = valueLength[j];
		
		// value
		for( int j = 0 ; j < valueBytes.length; j++ )
			data[i++] = valueBytes[j];
		
		return data;
		
	}
}
