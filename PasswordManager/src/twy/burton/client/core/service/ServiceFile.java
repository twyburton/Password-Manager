package twy.burton.client.core.service;

import twy.burton.utilities.FileAccess;

/**
 * This class represents an encrypted file that can be stored in the library 
 * @author twyburton
 *
 */
public class ServiceFile {

	private String name;
	private byte[] data;

	public ServiceFile( String name, byte[] data ){
		name = name.replaceAll("\\s+","");
		this.name = name;
		this.data = data;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setData(byte[] data){
		this.data = data;
	}
	
	public byte[] getData(){
		return data;
	}
	
	public byte[] getServiceFileBytes(){
		
		/*
		 *  FORMAT:
		 *  
		 *  	Length of File Name [4] , File Name [n] , Length of File Data [4], File Data [n]
		 */
		
		// Get the total length
		int length = 4 + 4 + name.getBytes().length + data.length;
		
		// Init data array
		byte[] bytes = new byte[length];
		int i = 0;
		
		// Get data
		byte[] nameBytes = name.getBytes();
		
		byte[] NameLength = FileAccess.int_to_bb_le( name.getBytes().length );
		byte[] DataLength = FileAccess.int_to_bb_le( data.length );
		
		// Write data
		// Name Length
		for( int j = 0 ; j < 4; j++ )
			bytes[i++] = NameLength[j];
		
		// Name
		for( int j = 0 ; j < nameBytes.length; j++ )
			bytes[i++] = nameBytes[j];
		
		// Data Length
		for( int j = 0 ; j < 4; j++ )
			bytes[i++] = DataLength[j];
		
		// Data
		for( int j = 0 ; j < data.length; j++ )
			bytes[i++] = data[j];
		
		return bytes;
		
	}
}
