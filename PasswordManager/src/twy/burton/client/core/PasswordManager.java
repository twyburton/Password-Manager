package twy.burton.client.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import twy.burton.client.core.library.LocalPasswordLibrary;
import twy.burton.client.core.library.PasswordLibrary;
import twy.burton.client.core.library.RemotePasswordLibrary;
import twy.burton.utilities.FileAccess;
import twy.burton.utilities.TConsole;
import twy.burton.utilities.Style;

public class PasswordManager {

	private List<PasswordLibrary> libraries;
	private PasswordLibrary activeLibrary;
	
	
	public PasswordManager(){
		libraries = new ArrayList<PasswordLibrary>();
	}

	public void addLibrary( PasswordLibrary pl ){
		libraries.add(pl);
	}
	
	public PasswordLibrary getActiveLibrary(){
		return activeLibrary;
	}
	
	public boolean setActiveLibrary( int i ){
		
		if( activeLibrary != null ) activeLibrary.lock();
		
		if ( i >= 0 && i < libraries.size() ){
			activeLibrary = libraries.get(i);
			TConsole.println("Unlocking " + activeLibrary.getLibraryName() + "..." , Style.GREEN);
			boolean status = activeLibrary.unlock();
			if( status ) {
				TConsole.println( "Library Version: " + activeLibrary.getLibraryVersion() );
				TConsole.println(activeLibrary.getLibraryName() + " Unlocked" , Style.GREEN);
				return true;
			}
			TConsole.println("Incorrect Password!" , Style.RED);
		} else {
			TConsole.println("Library Does Not Exist!" , Style.RED);
		}
		activeLibrary = null;
		return false;
	}
	
	public boolean lock(){		
		if( activeLibrary != null ) activeLibrary.lock();
		activeLibrary = null;
		return true;
	}
	
	public List<PasswordLibrary> getLibraries(){
		return libraries;
	}
	
	public void writeLibrariesFile(){
		
		try {
			
			File librariesFile = new File( Constants.LIBRARIES_FILE );
			FileOutputStream out = new FileOutputStream( librariesFile );
			
			// Write number of libraries to file
			byte[] numberLibraries = FileAccess.int_to_bb_le( libraries.size() );
			out.write(numberLibraries);
			
			for( int i = 0 ; i < libraries.size(); i++ ){
				
				// Get the string representation of the library
				byte[] store = libraries.get(i).getLibraryStoreString();
				// Write size of data to file
				out.write( FileAccess.int_to_bb_le(store.length) );
				// Write data to file
				out.write( store );
				// Write type of library
				byte libraryType = 0;
				if( libraries.get(i) instanceof LocalPasswordLibrary ){
					libraryType = 1;
				} else if ( libraries.get(i) instanceof RemotePasswordLibrary ){
					libraryType = 2;
				}
				out.write( libraryType );
				
			}
			
			out.close();
			
		} catch ( IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean readLibrariesFile(){
		
		try {
			
			File librariesFile = new File( Constants.LIBRARIES_FILE );
			FileInputStream in = new FileInputStream( librariesFile );
			
			byte[] number = new byte[4];
			in.read(number, 0, 4);
			int numberLibraries = FileAccess.bb_to_int_le(number);
			
			for( int i = 0 ; i < numberLibraries ; i++ ){
				// Get Size of library
				in.read(number, 0, 4);
				int librarySize = FileAccess.bb_to_int_le(number);
				
				// Read in library data
				byte[] library = new byte[librarySize];
				in.read(library, 0, librarySize);
				
				// Read library Type
				int libraryType = in.read();
				
				// Create and add to libraries array
				//String storeString = "";
				//for( int j = 0 ; j < library.length; j++ ){
				//	storeString += (char) library[j];
				//}
				
				PasswordLibrary lib = null;
				
				if( libraryType == 1 ){
					// Local
					lib = new LocalPasswordLibrary();
					lib.createLibraryFromStoreString(library);
				} else if ( libraryType == 2 ){
					// Remote
					lib = new RemotePasswordLibrary();
					lib.createLibraryFromStoreString(library);
				}
				
				libraries.add(lib);
			}

			in.close();
			
		} catch ( IOException e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
}
