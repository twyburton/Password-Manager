package twy.burton.core.service;

import twy.burton.utilities.FileAccess;

public class ServiceExtra {

	// This is extra data that can be attached to a service 
	// such as secret questions. e.g. Memorable place
	
	private String key;
	private String value;
	
	public ServiceExtra( String key, String value){
		key = key.replaceAll("\\s+","");
		this.key = key;
		this.value = value;
	}
	
	public ServiceExtra(){
		
	}
	
	/**
	 * Set the service extra key.
	 * @param key A small  key name of the extra.
	 */
	public void setKey( String key ){
		key = key.replaceAll("\\s+","");
		this.key = key;
	}
	
	/**
	 * Set the service extra value.
	 * @param value the value of the extra
	 */
	public void setValue( String value ){
		this.value = value;
	}
	
	/**
	 * Get the service extra key.
	 * @return The key of the service extra 
	 */
	public String getKey(){
		return key;
	}
	
	/**
	 * Get the service extra value.
	 * @return The value of the service extra 
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * Get a byte representation of the service extra that can then be saved in a binary file.
	 * @return The byte representation
	 */
	public byte[] getServiceExtraBytes(){
		
		/*
		 *  FORMAT:
		 *  
		 *  	Length of Extra Key [4] , Service Key [n] , Length of Extra Value [4], Extra Value [n]
		 */
		
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
