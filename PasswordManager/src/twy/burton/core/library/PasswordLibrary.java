package twy.burton.core.library;

import java.util.List;

import twy.burton.core.Service;

public abstract class PasswordLibrary {

	protected String libraryName;
	protected List<Service> services = null;
	
	public PasswordLibrary(){
		
	}
	
	// Rewrite Library To Store
	public abstract void write();
	// Read Library From Store
	public abstract boolean read();	
	
	
	// Functions for reading and writing library data to the libariesfile
	public abstract String getLibraryStoreString();
	public abstract void createLibraryFromStoreString( String storeString );
	
	
	
	public void setLibraryName( String libraryName ){
		this.libraryName = libraryName;
	}
	
	public String getLibraryName(){
		return libraryName;
	}
	
}

