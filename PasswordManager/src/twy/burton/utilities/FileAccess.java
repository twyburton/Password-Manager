package twy.burton.utilities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FileAccess {
	
	public static byte[] int_to_bb_le(int myInteger){
	    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
	}

	public static int bb_to_int_le(byte[] byteBarray){
	    return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
}