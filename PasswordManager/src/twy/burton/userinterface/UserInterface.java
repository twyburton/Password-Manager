package twy.burton.userinterface;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import twy.burton.core.Constants;
import twy.burton.core.PasswordManager;
import twy.burton.core.library.LocalPasswordLibrary;
import twy.burton.core.library.PasswordLibrary;
import twy.burton.core.service.IdServicePair;
import twy.burton.core.service.Service;
import twy.burton.core.service.ServiceExtra;
import twy.burton.utilities.OutputConsole;
import twy.burton.utilities.PasswordGenerator;
import twy.burton.utilities.Timestamp;

public class UserInterface {

	private Scanner scanner = new Scanner(System.in);
	private boolean running = true;
	private OutputConsole console = new OutputConsole( scanner );
	private PasswordManager pm = new PasswordManager();
	
	/**
	 * Print user interface header
	 */
	public UserInterface(){
		console.clear();
		console.println( Style.STYLE_UNDERLINE_ON + Constants.PROGRAM_NAME + " " + Constants.PROGRAM_VERSION + Style.STYLE_UNDERLINE_OFF );
	}
	
	/**
	 *  Perform first time setup. This includes creating directories and blank library files.
	 */
	public void firstSetup(){
		
		// Check directory Exists
		String[] required_folders = {Constants.MANAGER_DIRECTORY, Constants.LIBRARIES_DIRECTORY};
		for( int i = 0 ; i < required_folders.length; i++ ){
			File folder = new File(required_folders[i]);
			if( !folder.exists() ){
				folder.mkdirs();
			}
		}
		
		// Check Library file exists
		File libraries_file = new File(Constants.LIBRARIES_FILE);
		if( libraries_file.exists() ){
			pm.readLibrariesFile();
		} else {
			pm.writeLibrariesFile();
		}
		
	}
	
	public void run(){
		
		console.println( pm.getLibraries().size() + " Libraries Available\n", Style.GREEN );
		
		while( running ){
			
			// ===== GET USER INPUT =====
			String prompt = Style.CYAN + "> " + Style.WHITE;
			
			if( pm.getActiveLibrary() != null )
				prompt = "/" + Style.CYAN + pm.getActiveLibrary().getLibraryName() + Style.GREEN + "$ " + Style.WHITE;
			
			String[] input = console.getSeperatedInput(prompt);
			
			// ===== ACT ON INPUT =====
			// Exit the system
			if( input[0].equals( "exit" ) || input[0].equals("quit") ){
				running = false;
			}
			
			// Help display
			else if ( input[0].equals( "help" ) || input[0].equals( "?" ) || input[0].equals( "man" ) ){
				console.println(Style.STYLE_UNDERLINE_ON + "Help" + Style.STYLE_UNDERLINE_OFF);
				
				console.println("");
				console.println("<Param> - Required, [Param] - Optional");
				console.println("");
				
				console.println("exit - Exit password manager");
				console.println("help - Help page");
				
				console.println("info - Program infomation");
				console.println("random [Password Length] - Program infomation");
				
				console.println("");
				
				if( pm.getActiveLibrary() == null ){
					console.println("ls - List password libraries");
					console.println("unlock [Library ID] - Unlock the library identified by Library ID");
					console.println("createlocal - Create a local library");
					console.println("importlocal - Import a local library");
					console.println("importlegacy - Import a legacy local library");
				} else {
					console.println("ls - List all services");
					console.println("password - Change the library password");
					console.println("");
					
					console.println("get <Service Name> - Returns a list of services containing the string <Service Name>");
					console.println("new [Password Length] - Add a new service with a randomly generated password");
					console.println("remove <Service ID> - Remove service with the service ID <Service ID>");
					console.println("gen <Service ID> [Password Length] - Generate a new password for the service with the service ID <Service ID>");
					console.println("set <Service ID> - Manualy set the password for the service with the service ID <Service ID>");
					console.println("extraadd <Service ID> - Add a new service extra for the service with ID <Service ID>");
					console.println("extraremove <Service ID> <Key> - Remove the service extra with key <Key> for the service with ID <Service ID>");
					
				}
			}
			
			// -- Clear --
			else if ( input[0].equals( "clear" )  ){
				System.out.print("\033[H\033[2J");  
			    System.out.flush();
			}
			
			// -- Print random string --
			else if( input[0].equals("random") ){
				
				int length = Constants.DEFAULT_PASSWORD_LENGTH;
				if( input.length == 2 ){
					length = Integer.parseInt( input[1] );
				}
				
				console.println( PasswordGenerator.generateRandomPassword(length));
			}
			
			// -- List libraries or services if library is active --
			else if ( input[0].equals("ls") ){
				if( pm.getActiveLibrary() == null ){
					
					List<PasswordLibrary> libs = pm.getLibraries();
					console.print(Style.STYLE_UNDERLINE_ON + "Libraries" + Style.STYLE_UNDERLINE_OFF);
					
					for( int i = 0 ; i < libs.size(); i++ ){
						console.print( "\n" + i + " " + libs.get(i).getLibraryName() );
					}
					
					console.print("\n");
					
					
				} else {
					PasswordLibrary lib = pm.getActiveLibrary();
					console.print(Style.STYLE_UNDERLINE_ON + "Services" + Style.STYLE_UNDERLINE_OFF);
					
					for( int i = 0 ; i < lib.getServices().size(); i++ ){
						console.print( "\n" + i + " " + lib.getServices().get(i).getName() );
					}
					
					console.print("\n");
				}
			}
			
			// -- Unlock a library --
			else if ( input[0].equals( "unlock" ) ) {
				if( input.length == 2 ){
					int libraryNumber = Integer.parseInt(input[1]);
					pm.setActiveLibrary( libraryNumber );
				} else if (input.length == 1 ){
					pm.setActiveLibrary(0);
				} else {
					console.println("Usage: unlock <Library ID>");
				}
			}
			
			// -- Lock library --
			else if ( input[0].equals( "lock" ) ) {
				pm.lock();
			}
			
			// -- Create a local library --
			else if ( input[0].equals( "createlocal" ) ) {
				
				String libraryName = console.getInput("Library Name> ");
				String libraryFileName = UUID.randomUUID().toString() + ".pm";
				
				LocalPasswordLibrary lpl = new LocalPasswordLibrary();
				lpl.setLibraryName(libraryName);
				lpl.setFileName(libraryFileName);
				
				lpl.write();
				pm.addLibrary(lpl);
				pm.writeLibrariesFile();
				
			}
			
			// -- Create a local library --
			else if ( input[0].equals( "importlegacy" ) ) {
				
				String libraryName = console.getInput("Library Name> ");
				String libraryFileName = UUID.randomUUID().toString() + ".pm";
				
				LocalPasswordLibrary lpl = new LocalPasswordLibrary();
				lpl.setLibraryName(libraryName);
				lpl.setFileName(libraryFileName);
				
				if(lpl.importLegacyLibrary()){
					
					lpl.write();
					pm.addLibrary(lpl);
					pm.writeLibrariesFile();
					
					console.println("Library imported.",Style.GREEN);
					
				} else {
					console.println("Library NOT imported.",Style.RED);
				}
				
				
				
			}
			
			// -- Create a remote library --
			else if ( input[0].equals( "createremote" ) ) {
		
			}
			
			// -- Print details about the program --
			else if ( input[0].equals( "info" ) ) {
				console.println("\nVersion: " + Constants.PROGRAM_NAME + " " + Constants.PROGRAM_VERSION );
				console.println("Default Password Length: " + Constants.DEFAULT_PASSWORD_LENGTH);
				console.println("Password Character Set: " + Arrays.toString(Constants.PASSWORD_CHARACTERS));
				console.println("");
			}
			
			
			// === FUNCTIONS WHEN LIBRARY IS ACTIVE ===
			else if ( pm.getActiveLibrary() != null ){
				// -- get --
				if( input[0].equals("get") ){
					if( input.length == 2 ){
						List<IdServicePair> matches = pm.getActiveLibrary().getServiceByName( input[1] );
						
						console.println("");
						console.println("\t" + Style.MAGENTA + Style.STYLE_UNDERLINE_ON + matches.size() + " Match(es)"
								+ Style.STYLE_UNDERLINE_OFF + Style.WHITE);
						
						if( matches.size() > 0 ){
							for( int i = 0 ; i < matches.size() ; i++ ){
								Service ser = matches.get(i).getService();
								int id = matches.get(i).getServiceId();
								
								console.println( "\t" + id + " /" + Style.CYAN + pm.getActiveLibrary().getLibraryName() + Style.GREEN 
										+ "$" + Style.WHITE + ser.getName() + "\t" + ser.getUsername() 
										+ "\t" + ser.getPassword() );
								for( int u = 0 ; u < ser.getServiceExtras().size(); u++ ){
									ServiceExtra extra = ser.getServiceExtras().get(u);
									console.println("\t\t/" + Style.CYAN + pm.getActiveLibrary().getLibraryName() + Style.GREEN
											+ "$" + Style.WHITE + ser.getName() + "." + extra.getKey() + "\t" + extra.getValue());
								}
								console.println("");
								
								if( matches.size() == 1 && i == 0){
									// Copy password to clipboard
									StringSelection stringSelection = new StringSelection( ser.getPassword() );
									Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
									clpbrd.setContents(stringSelection, null);
								}
								
							}
							
							
						} else {
							console.println("\tNo Matches");
							console.println("");
						}
						
					} else {
						console.println("Usage: get <service name>");
					}
				}
				
				// -- New --
				else if( input[0].equals("new") ){
					
					String name = console.getInput("Service Name > ");
					String username = console.getInput("Service Username > ");
					
					Service ser = new Service();
					ser.setName(name);
					ser.setUsername(username);
					
					int passwordLength = Constants.DEFAULT_PASSWORD_LENGTH;
					if( input.length == 2 ){
						passwordLength = Integer.parseInt( input[1] );
					}
					
					String password = PasswordGenerator.generateRandomPassword(passwordLength);
					ser.setPassword(password);
					
					ser.addServiceExtra("LastUpdated", Timestamp.getTimestamp() );
					
					pm.getActiveLibrary().addService(ser);
					
					while ( !pm.getActiveLibrary().write() );
					
				}
				
				// -- Remove --
				else if( input[0].equals("remove") ){
					if( input.length == 2 ){
						try {
							int serviceid = Integer.parseInt( input[1] );
						
							if( pm.getActiveLibrary().getServiceById(serviceid) != null ){
								console.println("Are you sure you want to remove the service " 
										+ pm.getActiveLibrary().getServiceById(serviceid).getName()+ "? ");
								console.println("(This cannot be undone!)");
								String yes_no_input = console.getInput("[y/n] ");
								if( yes_no_input.toLowerCase().equals("y") ){
									if ( pm.getActiveLibrary().removeServiceById( serviceid )){
										
										while ( !pm.getActiveLibrary().write() );
										
									} else {
										console.println("Service does not exist.",Style.RED);
									}
								} else {
									console.println("The service has NOT been removed.",Style.GREEN);
								}
								
							} else {
								console.println("Service does not exist.",Style.RED);
							}
							
						} catch (NumberFormatException e) {
							console.println("Usage: remove <service ID>");
						}
						
					} else {
						console.println("Usage: remove <service ID>");
					}
				}
				
				// -- Set --
				else if( input[0].equals("set") ){
					if( input.length == 2 ){
						try {
							int serviceid = Integer.parseInt( input[1] );
						
							if( pm.getActiveLibrary().getServiceById(serviceid) != null ){
								
								String oldPassword = pm.getActiveLibrary().getServiceById(serviceid).getPassword();
								String password = console.getInput("New Password > ");
								String password2 = console.getInput("Retype New Password > ");
								
								if( password.equals(password2)){
									console.println("Are you sure you want to change the password for '" 
											+ pm.getActiveLibrary().getServiceById(serviceid).getName()+ "'? ");
									console.println("(This cannot be undone!)");
									String yes_no_input = console.getInput("[y/n] ");
									if( yes_no_input.toLowerCase().equals("y") ){
										
										Service ser = pm.getActiveLibrary().getServiceById(serviceid);
										
										ser.setPassword(password);
										ser.deleteServiceExtra("LastUpdated");
										ser.addServiceExtra("LastUpdated", Timestamp.getTimestamp() );
										
										while ( !pm.getActiveLibrary().write() );
										
										console.println("The password has been changed.",Style.GREEN);
										console.println("Old Password: " + oldPassword );
										
									} else {
										console.println("The password has NOT been changed.",Style.RED);
									}
								} else {
									console.println("The entered password do not match. The password has NOT been changed.",Style.RED);
								}
								
								
								
							} else {
								console.println("Service does not exist.",Style.RED);
							}
							
						} catch (NumberFormatException e) {
							console.println("Usage: set <service ID>");
						}
						
					} else {
						console.println("Usage: set <service ID>");
					}
				}
				
				
				// -- Gen --
				else if( input[0].equals("gen") ){
					if( input.length >= 2 ){
						try {
							int serviceid = Integer.parseInt( input[1] );
							int passwordLength = Constants.DEFAULT_PASSWORD_LENGTH;
							if( input.length == 3 ){
								passwordLength = Integer.parseInt( input[2] );
							}
						
							Service ser = pm.getActiveLibrary().getServiceById(serviceid);
							
							if( ser != null ){
								console.println("Are you sure you want to re-generate the password for " 
										+ pm.getActiveLibrary().getServiceById(serviceid).getName()+ "? ");
								console.println("(This cannot be undone!)");
								console.println("Old Password: " + ser.getPassword());
								String yes_no_input = console.getInput("[y/n] ");
								if( yes_no_input.toLowerCase().equals("y") ){
									
									String newPassword = PasswordGenerator.generateRandomPassword(passwordLength);
									ser.setPassword(newPassword);
									console.println("The new password is: " + newPassword);
									
									ser.deleteServiceExtra("LastUpdated");
									ser.addServiceExtra("LastUpdated", Timestamp.getTimestamp() );
									
									while ( !pm.getActiveLibrary().write() );
									
									console.println("The old password will NOT be shown again!", Style.RED);
									
								} else {
									console.println("The password has NOT been changed.",Style.GREEN);
								}
							} else {
								console.println("Service does not exist.",Style.RED);
							}
							
							
						} catch (NumberFormatException e) {
							console.println("Usage: gen <service ID> [Password Length]");
						}
						
					} else {
						console.println("Usage: gen <service ID> [Password Length]");
					}
				}
				
				// -- Add service extra --
				else if( input[0].equals("extraadd") ){
					if( input.length == 2 ){
						try {
							int serviceId = Integer.parseInt(input[1]);
							Service ser = pm.getActiveLibrary().getServiceById(serviceId);
		
							if( ser != null ){
								String extraKey = console.getInput("Extra Key > ");
								String extraValue = console.getInput("Extra Value > ");
								
								ServiceExtra extra = new ServiceExtra();
								extra.setKey(extraKey);
								extra.setValue(extraValue);
								
								ser.addServiceExtra(extra);
								
								while ( !pm.getActiveLibrary().write() );
								
								console.println("The Service Extra has been added.", Style.GREEN);
								
							} else { 
								console.println("Service does not exist.",Style.RED);
							}
						
						} catch (NumberFormatException e) {
							console.println("Usage: extraadd <service ID>");
						}
					} else {
						console.println("Usage: extraadd <service ID>");
					}
				}
				
				// -- Add service extra --
				else if( input[0].equals("extraremove") ){
					if( input.length == 3 ){
						try {
							int serviceId = Integer.parseInt(input[1]);
							Service ser = pm.getActiveLibrary().getServiceById(serviceId);
							
							if( ser != null ){
								
								console.println("Are you sure you want to remove the service extra '" + input[2] + "' for " 
										+ pm.getActiveLibrary().getServiceById(serviceId).getName()+ "? ");
								console.println("(This cannot be undone!)");
								String yes_no_input = console.getInput("[y/n] ");
								if( yes_no_input.toLowerCase().equals("y") ){
									
									for( int i = 0 ; i < ser.getServiceExtras().size(); i++ ){
										ServiceExtra ext = ser.getServiceExtras().get(i);
										if( ext.getKey().equals(input[2]) ){
											console.println( "\t" + ext.getKey() + "\t" + ext.getValue() );
											ser.getServiceExtras().remove(i);
										}
									}
									
									while ( !pm.getActiveLibrary().write() );
									
									console.println("\tThe above service extra(s) have been deleted. You will not be able to see them again!", Style.RED); 
									
									
								} else {
									console.println("The service extra(s) has not been removed.",Style.GREEN);
								}
								
							} else { 
								console.println("Service does not exist.",Style.RED);
							}
							
							
						} catch (NumberFormatException e) {
							console.println("Usage: extraremove <service ID> <Key>");
						}
					} else {
						console.println("Usage: extraremove <service ID> <Key>");
					}
				}
				
				// -- password --
				else if( input[0].equals("password") ){
					if( pm.getActiveLibrary().changeLibraryPassword() ){
						console.println("Your library password has been changed.",Style.GREEN);
					} else {
						console.println("Your library password has NOT been changed.",Style.RED);
					}
				}
			}

			
		}
		
		scanner.close();
		console.clear();
		
	}
	
}
