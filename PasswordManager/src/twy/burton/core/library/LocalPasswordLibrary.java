package twy.burton.core.library;

import java.io.File;
import java.util.Scanner;

import javax.crypto.BadPaddingException;

import twy.burton.core.Constants;
import twy.burton.security.PMAES;
import twy.burton.userinterface.Style;
import twy.burton.utilities.FileAccess;
import twy.burton.utilities.OutputConsole;

public class LocalPasswordLibrary extends PasswordLibrary {

	
	private String fileName;
	
	
	public LocalPasswordLibrary(){
		super();
	}
	
	public void setFileName( String fileName ){
		this.fileName = fileName;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	@Override
	public boolean write() {
		
		byte[] bytes = this.getByteRepresentation();
		
		// Input
		Scanner scan = new Scanner(System.in);
		OutputConsole con = new OutputConsole( scan);
		
		// File
		String file = Constants.LIBRARIES_DIRECTORY + "/" + fileName ;
		File f = new File( file );
		String password = null;
		if( f.exists() ){
			// File already exists Validate Password before overwrite
			password = con.getSecurePassword("Manager Password");
			
			if( !this.validatePassword(password)){
				con.println( "Incorrect Password" , Style.RED);
				return false;
			}
		
		} else {
			// Get new password
			password = con.getSecurePassword("New Manager Password");
			String password2 = con.getSecurePassword("Retype Manager Password");
			
			if( !password.equals(password2) ){
				con.println( "Passwords Do Not Match" , Style.RED);
				return false;
			}
		}
		
		// Write File
		return PMAES.writeEncryptedFile(file, password, bytes);
		
	}

	@Override
	protected boolean read() {

		// == GET PASSWORD == 
		Scanner scan = new Scanner(System.in);
		OutputConsole con = new OutputConsole( scan);
		String password = con.getSecurePassword("Manager Password");
		
		// == VALIDATE PASSWORD ==
		if( !validatePassword(password) ){
			return false;
		}
		
		// == READ FILE ==
		String filename = Constants.LIBRARIES_DIRECTORY + "/" + fileName ;
		byte[] data;
		try {
			data = PMAES.readEncryptedFile(filename, password);
		} catch (BadPaddingException e) {
			// Most likely bad password. Should have been caught above.
			return false;
		}
		
		return this.byteRepresentationToObject(data);
	
	}


	@Override
	public byte[] getLibraryStoreString() {
		
		int length = 4 + 4 + fileName.getBytes().length + libraryName.getBytes().length;
		byte[] store = new byte[length];
		
		byte[] filePathSize = FileAccess.int_to_bb_le(fileName.getBytes().length);
		byte[] libraryNameSize = FileAccess.int_to_bb_le(libraryName.getBytes().length);
		
		int i = 0;
		for( int j = 0 ; j < 4; j++ )
			store[i++] = filePathSize[j];
		
		for( int j = 0 ; j < fileName.getBytes().length ; j++ )
			store[i++] = fileName.getBytes()[j];
		
		for( int j = 0 ; j < 4; j++ )
			store[i++] = libraryNameSize[j];
		
		for( int j = 0 ; j < libraryName.getBytes().length ; j++ )
			store[i++] = libraryName.getBytes()[j];
		
		return store;
		
	}


	@Override
	public void createLibraryFromStoreString(byte[] store) {
		
		int i = 0;
		
		// FilePath size
		byte[] size = new byte[4];
		for( int j = 0 ; j < 4; j++ )
			size[j] = store[i++];
		int filePathLength = FileAccess.bb_to_int_le( size );
		
		// Get filePath
		String filePath = "";
		for( int j = 0 ; j < filePathLength; j++ )
			filePath += (char)store[i++];
		
		// Get libraryName size
		for( int j = 0 ; j < 4; j++ )
			size[j] = store[i++];
		int libraryNameLength = FileAccess.bb_to_int_le( size );
		
		// Get libraryName
		String libraryName = "";
		for( int j = 0 ; j < libraryNameLength; j++ )
			libraryName += (char)store[i++];
		
		this.fileName = filePath;
		this.libraryName = libraryName;
		
	}

	@Override
	public String getListIdentifier() {
		return libraryName;
	}

	@Override
	public boolean validatePassword( String password ){
		String file = Constants.LIBRARIES_DIRECTORY + "/" + fileName ;
		byte[] currentFileData;
		try {
			currentFileData = PMAES.readEncryptedFile(file , password);
		} catch (ArrayIndexOutOfBoundsException | BadPaddingException e ){
			// Bad padding. Therefore wrong password.
			return false;
		}
		
		for( int i = 0 ; i < Constants.VALIDATION_STRING.length() ; i++ ){
			if( currentFileData[i] != Constants.VALIDATION_STRING.getBytes()[i] ){
				// Not Valid Password
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean unlock() {
		return read();
	}
	
	@Override
	public boolean changeLibraryPassword(){
		byte[] bytes = getByteRepresentation();
		
		// Input
		Scanner scan = new Scanner(System.in);
		OutputConsole con = new OutputConsole( scan);
		
		// File
		String file = Constants.LIBRARIES_DIRECTORY + "/" + fileName ;
		File f = new File( file );
		if( f.exists() ){
			// File already exists Validate Password before overwrite
			String password = con.getSecurePassword("Old Manager Password");
			
			if( !this.validatePassword(password)){
				con.println( "Incorrect Password" , Style.RED);
				return false;
			}
			
			// Get new password
			password = con.getSecurePassword("New Manager Password");
			String password2 = con.getSecurePassword("Retype Manager Password");
			
			if( !password.equals(password2) ){
				con.println( "Passwords Do Not Match" , Style.RED);
				return false;
			}

			// Write File
			return PMAES.writeEncryptedFile(file, password, bytes);		
						
		}
		return false;
		
	}
	
}
