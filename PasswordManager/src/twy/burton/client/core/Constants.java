package twy.burton.client.core;

public class Constants {
	public static String PROGRAM_NAME = "Password Manager";
	public static byte[] PROGRAM_VERSION_ARRAY = {3,1,0};
	public static String PROGRAM_VERSION = PROGRAM_VERSION_ARRAY[0] + "." + PROGRAM_VERSION_ARRAY[1] + "." + PROGRAM_VERSION_ARRAY[2];
	
	public static String HOME_DIRECTORY = System.getProperty("user.home");
	public static String MANAGER_DIRECTORY = HOME_DIRECTORY  + "/.BasicPasswordManager";
	
	// The file that saves a users list of libraries
	public static String LIBRARIES_FILE = MANAGER_DIRECTORY + "/PasswordLibraries.lf";
	
	public static String LIBRARIES_DIRECTORY = MANAGER_DIRECTORY + "/libraries";
	
	public static int NUMBER_OF_TIMES_TO_HASH_PASSWORD = 1000;
	public static int ENCRYPTION_INITIAL_VECTOR_LENGTH = 16;
	
	
	public static int LENGTH_OF_RANDOM_BEFORE_ENCRYPTION = 20;
	public static String VALIDATION_STRING = "28ISD8K12O0DKA2MKK2DSSF"; 
	
	
	// Constants used for random password generation
	public static int DEFAULT_PASSWORD_LENGTH = 20;
	public static char[] PASSWORD_CHARACTERS = {'A','B','C','D','E','F','G','H','I','J'
			,'K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a'
			,'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r'
			,'s','t','u','v','w','x','y','z','%','$','#','(',')','*','0','1','2'
			,'3','4','5','6','7','8','9',';','!','^'};
}
