package twy.burton.core.library;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.crypto.BadPaddingException;

import twy.burton.core.Constants;
import twy.burton.core.service.Service;
import twy.burton.core.service.ServiceExtra;
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
	
	
	private byte[] getDataToWriteToFile(){
		// == PREPARE THE FILE TO WRITE ==
			List<Byte> data = new ArrayList<Byte>();
			
			// == GET DATA TO WRITE ==
			// Add random data and validation string
			SecureRandom ran = new SecureRandom();
			byte[] randomPadding = new byte[Constants.LENGTH_OF_RANDOM_BEFORE_ENCRYPTION]; 
			ran.nextBytes(randomPadding);
			for( int i = 0 ; i < Constants.LENGTH_OF_RANDOM_BEFORE_ENCRYPTION; i++ )
				data.add( randomPadding[i] );
			
			byte[] valid = Constants.VALIDATION_STRING.getBytes();
			for( int i = 0 ; i < valid.length; i++ )
				data.add(valid[i]);
			
			// library version
			data.add(Constants.PROGRAM_VERSION_ARRAY[0]);
			data.add(Constants.PROGRAM_VERSION_ARRAY[1]);
			data.add(Constants.PROGRAM_VERSION_ARRAY[2]);
			
			// Number of services
			int numberServices = services.size();
			byte[] number = FileAccess.int_to_bb_le(numberServices);
			for( int i = 0 ; i < 4; i++ ){
				data.add(number[i]);
			}
			
			// services
			for( int i = 0 ; i < numberServices; i++ ){
				byte[] s = services.get(i).getServiceBytes();
				
				number = FileAccess.int_to_bb_le(s.length);
				for( int j = 0 ; j < 4; j++ ){
					data.add(number[j]);
				}
				
				for( int j = 0 ; j < s.length; j++ ){
					data.add(s[j]);
				}
				
			}
			
			// == WRITE TO FILE ==
			// Convert data list to byte array
			byte[] bytes = new byte[data.size()];
			for( int i = 0 ; i < bytes.length; i++ ){
				bytes[i] = data.get(i);
			}
			
			return bytes;
	}
	
	@Override
	public boolean write() {
		
		byte[] bytes = getDataToWriteToFile();
		
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
		
		// == REMOVE RANDOM AND VALIDATION DATA ==
		int preLength = Constants.LENGTH_OF_RANDOM_BEFORE_ENCRYPTION + Constants.VALIDATION_STRING.length();
		int storeLength = data.length - preLength;
		byte[] store = new byte[ storeLength ];
		
		for( int i = 0 ; i < store.length;i++){
			store[i] = data[ i + preLength ];
		}
		
		// == POPULATE OBJECT ==
		int p = 0;
		
		// Library version
		byte[] libraryVersion = new byte[3];
		libraryVersion[0] = store[p++];
		libraryVersion[1] = store[p++];
		libraryVersion[2] = store[p++];
		
		this.libraryVersion = libraryVersion[0] + "." + libraryVersion[1] + "." + libraryVersion[2];
		
		services = new ArrayList<Service>();
		// Get Number of services
		byte[] n_services = new byte[4];
		for( int i = 0 ; i < n_services.length; i++ ) n_services[i] = store[p++];
		
		int numberServices = FileAccess.bb_to_int_le(n_services);
		System.out.println("Loading " + numberServices + " Services...");
		for( int i = 0 ; i < numberServices; i++ ){
			
			Service service = new Service();
			
			// Get service length
			byte[] inte = new byte[4];
			for( int j = 0 ; j < inte.length; j++ ) inte[j] = store[p++];
			//int serviceLength = FileAccess.bb_to_int_le(inte);
			
			// Get serviceName
			inte = new byte[4];
			for( int j = 0 ; j < inte.length; j++ ) inte[j] = store[p++];
			int serviceNameLength = FileAccess.bb_to_int_le(inte);
			
			String serviceName = "";
			for( int j = 0 ; j < serviceNameLength ; j++ )
				serviceName += (char)store[p++];
			service.setName(serviceName);
			
			// Get service username
			inte = new byte[4];
			for( int j = 0 ; j < inte.length; j++ ) inte[j] = store[p++];
			int serviceUsernameLength = FileAccess.bb_to_int_le(inte);
			
			String serviceUsername = "";
			for( int j = 0 ; j < serviceUsernameLength ; j++ )
				serviceUsername += (char)store[p++];
			service.setUsername(serviceUsername);
			
			// Get service password
			inte = new byte[4];
			for( int j = 0 ; j < inte.length; j++ ) inte[j] = store[p++];
			int servicePasswordLength = FileAccess.bb_to_int_le(inte);
			
			String servicePassword = "";
			for( int j = 0 ; j < servicePasswordLength ; j++ )
				servicePassword += (char)store[p++];
			service.setPassword(servicePassword);
			
			
			// Get service extras
			inte = new byte[4];
			for( int j = 0 ; j < inte.length; j++ ) inte[j] = store[p++];
			int nServiceExtras = FileAccess.bb_to_int_le(inte);
			
			// Go though service extras
			for( int u = 0 ; u < nServiceExtras; u++ ){
				p += 4;
				
				ServiceExtra extra = new ServiceExtra();
				
				// Get extra key
				inte = new byte[4];
				for( int j = 0 ; j < inte.length; j++ ) inte[j] = store[p++];
				int extraKeyLength = FileAccess.bb_to_int_le(inte);
				
				String extraKey = "";
				for( int j = 0 ; j < extraKeyLength ; j++ )
					extraKey += (char)store[p++];
				extra.setKey(extraKey);
				
				// Get extra value
				inte = new byte[4];
				for( int j = 0 ; j < inte.length; j++ ) inte[j] = store[p++];
				int extraValueLength = FileAccess.bb_to_int_le(inte);
				
				String extraValue = "";
				for( int j = 0 ; j < extraValueLength ; j++ )
					extraValue += (char)store[p++];
				extra.setValue(extraValue);
				
				service.addServiceExtra(extra);
			}
					
			
			this.addService(service);
		}
				
		
		return true;
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
			if( currentFileData[i + Constants.LENGTH_OF_RANDOM_BEFORE_ENCRYPTION] != Constants.VALIDATION_STRING.getBytes()[i] ){
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
		byte[] bytes = getDataToWriteToFile();
		
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
