package twy.burton.client.compression;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.SecureRandom;

import org.junit.Test;

public class CompressionUtilsTest {

	@Test
	public void test() throws IOException {
		SecureRandom ran = new SecureRandom();
		byte[] bytes = new byte[4096*4096*5];
		ran.nextBytes(bytes);
		
		CompressionUtils.compress(bytes);
	}

}
