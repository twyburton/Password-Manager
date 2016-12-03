package twy.burton.core.library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import twy.burton.core.library.legacy.LegacyLibrary;
import twy.burton.core.service.IdServicePair;
import twy.burton.core.service.Service;
import twy.burton.utilities.OutputConsole;

public abstract class PasswordLibrary {

	protected String libraryName;
	protected List<Service> services = new ArrayList<Service>();
	protected String libraryVersion = "Unknown";
	
	public PasswordLibrary(){
		
	}
	
	// Rewrite Library To Store
	public abstract boolean write();
	// Read Library From Store
	protected abstract boolean read();
	// Unlock The Library
	public abstract boolean unlock();
	// Lock The Library
	public void lock(){
		services = new ArrayList<Service>();
	}
	
	public abstract boolean validatePassword( String password );
	
	// Functions for reading and writing library data to the libariesfile
	public abstract byte[] getLibraryStoreString();
	public abstract void createLibraryFromStoreString( byte[] storeString );
	
	
	public abstract String getListIdentifier();
	
	public void setLibraryName( String libraryName ){
		this.libraryName = libraryName;
	}
	
	public String getLibraryName(){
		return libraryName;
	}
	
	
	// ============================================
	//		 Functions for accessing services
	// ============================================
	
	// Add a service to the service list
	public void addService( Service s0 ){
		services.add(s0);
	}
	
	// Return a list of services which have a name containing the name parameter
	public List<IdServicePair> getServiceByName( String name ){
		List<IdServicePair> matches = new ArrayList<IdServicePair>();
		for( int i = 0 ; i < services.size(); i++ ){
			if( services.get(i).getName().toLowerCase().contains( name.toLowerCase() )){
				matches.add(new IdServicePair(i,services.get(i)));
			}
		}
		return matches;
	}
	
	public Service getServiceById( int id ){
		if( id >= 0 && id < services.size()){
			return services.get(id);
		}
		return null;
	}
	
	public List<Service> getServices() {
		return services;
	}
	
	public boolean removeServiceById( int id ){
		
		if( id >= 0 && id < services.size()){
			services.remove(id);
			return true;
		}
		return false;
		
	}
	
	public abstract boolean changeLibraryPassword();
	
	/**
	 * Import a legacy library
	 * 
	 * @return boolean 
	 */
	public boolean importLegacyLibrary(){
		
		Scanner scanner = new Scanner(System.in);
		OutputConsole console = new OutputConsole( scanner );
		
		String path = console.getInput("Path to Legacy Library > ");
		String password = console.getSecurePassword("Legacy Library Password");
		
		// Legacy IV
		String iv_string = "os834jay4mxf781b";
		byte[] iv = iv_string.getBytes();
		
		byte[] key = LegacyLibrary.hashPassword(password).getBytes();
		
		try {
			List<Service> list = LegacyLibrary.readFile(path, key, iv);
			
			if( list == null ){
				return false;
			}
			
			services = list;
			
		} catch (IOException e) {
			return false;
		}
		
		
		return true;
	}
	
	public String getLibraryVersion(){
		return libraryVersion;
	}
}

