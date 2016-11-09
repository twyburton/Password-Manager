package twy.burton.core.service;

public class IdServicePair {

	private int serviceId;
	private Service service;
	
	public IdServicePair( int serviceId , Service service ){
		this.serviceId = serviceId;
		this.service = service;
	}
	
	public int getServiceId(){
		return serviceId;
	}
	
	public Service getService(){
		return service;
	}
	
}
