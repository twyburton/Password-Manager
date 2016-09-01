package twy.burton.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import twy.burton.core.library.LocalPasswordLibrary;
import twy.burton.core.library.PasswordLibrary;
import twy.burton.core.library.RemotePasswordLibrary;
import twy.burton.utilities.FileAccess;

public class PasswordManager {

	private List<PasswordLibrary> libraries;
	private PasswordLibrary activeLibrary;
	
	
	public PasswordManager(){
		
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
				byte[] store = libraries.get(i).getLibraryStoreString().getBytes();
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
				String storeString = "";
				for( int j = 0 ; j < library.length; j++ ){
					storeString += (char) library[j];
				}
				
				if( libraryType == 1 ){
					// Local
					LocalPasswordLibrary lib = new LocalPasswordLibrary();
					lib.createLibraryFromStoreString(storeString);
				} else if ( libraryType == 2 ){
					// Remote
					RemotePasswordLibrary lib = new RemotePasswordLibrary();
					lib.createLibraryFromStoreString(storeString);
				}
			}

			in.close();
			
		} catch ( IOException e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
}
