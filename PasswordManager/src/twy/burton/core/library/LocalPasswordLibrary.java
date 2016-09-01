package twy.burton.core.library;

public class LocalPasswordLibrary extends PasswordLibrary {

	
	private String filePath;
	
	
	public LocalPasswordLibrary(){
		super();
	}
	
	public void setFilePath( String filePath ){
		this.filePath = filePath;
	}
	
	public String getFilePath(){
		return filePath;
	}
	
	
	@Override
	public void write() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean read() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getLibraryStoreString() {
		
		return null;
	}


	@Override
	public void createLibraryFromStoreString(String storeString) {
		// TODO Auto-generated method stub
		
	}

	
	
}
