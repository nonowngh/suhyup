package mb.fw.suhyup.netty.suhyupbank.converter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mb.fw.suhyup.dto.InterfaceSpec;
import mb.fw.suhyup.dto.InterfaceSpec.FieldSpec;
import mb.fw.suhyup.util.PaddingUtils;

public class MessageConverter {

	public static String toMessageString(InterfaceSpec interfaceSpec, Map<String, Object> dataObject,
			Charset charsets) {

		List<FieldSpec> bodyFieldList = interfaceSpec.getBodyFieldList();
		ByteBuf sendBuffer = Unpooled.buffer();
		bodyFieldList.forEach((Consumer<? super FieldSpec>) field -> {
			if ("STRING".equals(field.getType())) {
				PaddingUtils.writeRightPaddingString(sendBuffer, (String) dataObject.get(field.getName()),
						field.getLength());
			}
			if ("INTEGER".equals(field.getType())) {
				PaddingUtils.writeLeftPaddingNumber(sendBuffer, (int) dataObject.get(field.getName()),
						field.getLength());
			}
		});
		return sendBuffer.toString(charsets);
	}

	public static Map<String, Object> toMessageObject(InterfaceSpec interfaceSpec, String dataString, Charset charset) {
		Map<String, Object> dataObject = new LinkedHashMap<>();
		List<FieldSpec> bodyFieldList = interfaceSpec.getBodyFieldList();
		ByteBuf messageBuffer = Unpooled.copiedBuffer(dataString, charset);
		AtomicInteger offset = new AtomicInteger(0);
		bodyFieldList.forEach((Consumer<? super FieldSpec>) field -> {
			if ("STRING".equals(field.getType())) {
				dataObject.put(field.getName(),
						messageBuffer.slice(offset.get(), field.getLength()).toString(charset).trim());
			}
			if ("INTEGER".equals(field.getType())) {
				dataObject.put(field.getName(),
						Integer.valueOf(messageBuffer.slice(offset.get(), field.getLength()).toString(charset)));
			}
			offset.addAndGet(field.getLength());
		});
		return dataObject;
	}

	public static void main(String[] args) throws StreamReadException, DatabindException, IOException {
		ClassPathResource resource = new ClassPathResource("interface-spec.json");
		InputStream input = resource.getInputStream();
		ObjectMapper objectMapper = new ObjectMapper();
		List<InterfaceSpec> interfaceSpecs = objectMapper.readValue(input, new TypeReference<List<InterfaceSpec>>() {
		});
		InterfaceSpec interfaceSpec = interfaceSpecs.get(0);
		List<FieldSpec> bodyFieldList = interfaceSpec.getBodyFieldList();
		ByteBuf sendBuffer = Unpooled.buffer();
		bodyFieldList.forEach((Consumer<? super FieldSpec>) field -> {
			if ("STRING".equals(field.getType())) {
				PaddingUtils.writeRightPaddingString(sendBuffer, field.getName(), field.getLength());
			}
			if ("INTEGER".equals(field.getType())) {
				PaddingUtils.writeLeftPaddingNumber(sendBuffer, 1, field.getLength());
			}
		});

		System.out.println(sendBuffer.toString(StandardCharsets.UTF_8));
	}

}
