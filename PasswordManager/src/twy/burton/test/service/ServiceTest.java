package twy.burton.test.service;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import twy.burton.core.service.Service;
import twy.burton.core.service.ServiceExtra;

public class ServiceTest {

	@Test
	public void ServiceCreation() {
		
		Service s = new Service();
		s.setName("ServiceName");
		s.setUsername("ServiceUsername");
		s.setPassword("password");
		
		assertEquals( "ServiceName", s.getName() );
		assertEquals( "ServiceUsername", s.getUsername() );
		assertEquals( "password", s.getPassword() );
		
		ServiceExtra se = new ServiceExtra();
		se.setKey("Key");
		se.setValue("Value");
		
		assertEquals( "Key", se.getKey() );
		assertEquals( "Value", se.getValue() );
		
	}

}