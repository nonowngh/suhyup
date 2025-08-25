package mb.fw.suhyup.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;

public class PaddingUtils {
	public static void writeRightPaddingString(ByteBuf buf, String value, int fixedLength) {
	    byte[] rawBytes = value.getBytes(StandardCharsets.UTF_8);
	    int paddingLength = fixedLength - rawBytes.length;

	    if (paddingLength < 0) {
	        buf.writeBytes(Arrays.copyOf(rawBytes, fixedLength));
	    } else {
	        buf.writeBytes(rawBytes);
	        for (int i = 0; i < paddingLength; i++) {
	            buf.writeByte(' '); // ASCII space padding
	        }
	    }
	}
	
	public static void writeLeftPaddingNumber(ByteBuf buf, int value, int fixedLength) {
	    String numberStr = String.format("%0" + fixedLength + "d", value);
	    buf.writeBytes(numberStr.getBytes(StandardCharsets.US_ASCII));
	}
}
