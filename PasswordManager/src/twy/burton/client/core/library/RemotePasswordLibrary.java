package twy.burton.client.core.library;

public class RemotePasswordLibrary extends PasswordLibrary {

	private String username;
	private String url;
	
	public RemotePasswordLibrary(){
		super();
	}
	
	
	@Override
	public boolean write() {
		return false;
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean read() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public byte[] getLibraryStoreString() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void createLibraryFromStoreString(byte[] storeString) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getListIdentifier() {
		return libraryName + " " + username + "@" + url;
	}


	@Override
	public boolean unlock() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean validatePassword(String password) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean changeLibraryPassword() {
		// TODO Auto-generated method stub
		return false;
	}

}
