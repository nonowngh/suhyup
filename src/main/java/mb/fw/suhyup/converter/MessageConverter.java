package mb.fw.suhyup.converter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import mb.fw.suhyup.converter.JsonBodySpec.FieldSpec;
import mb.fw.suhyup.util.PaddingUtils;

@Component
@Slf4j
public class MessageConverter {

	private Resource jsonFile;
	List<JsonBodySpec> interfaceSpecs;
	
	public MessageConverter(@Value("classpath:if_message_spec.json") Resource jsonFile) {
		this.jsonFile = jsonFile;
	}
	
	@PostConstruct
	public void initConverter() {
		try (InputStream is = jsonFile.getInputStream()) {
			ObjectMapper objectMapper = new ObjectMapper();
			interfaceSpecs = objectMapper.readValue(is, new TypeReference<List<JsonBodySpec>>() {
			});
		} catch (Exception e) {
			log.error("deserialize 'if_masseage_spec.json' file error!", e);
		}
	}

	public String toMessageString(String interfaceId, Map<String, Object> dataObject, Charset charsets) {
		Optional<JsonBodySpec> result = interfaceSpecs.stream()
				.filter(interfaceSpec -> interfaceId.equals(interfaceSpec.getInterfaceId())).findFirst();
		if (result.isPresent()) {
			JsonBodySpec interfaceSpec = result.get();
			List<FieldSpec> bodyFieldList = interfaceSpec.getBodyFieldList();
			ByteBuf sendBuffer = Unpooled.buffer();
			bodyFieldList.forEach((Consumer<? super FieldSpec>) field -> {
				if ("STRING".equals(field.getType())) {
					PaddingUtils.writeRightPaddingString(sendBuffer, (String) dataObject.get(field.getName()), field.getLength());
				}
				if ("INTEGER".equals(field.getType())) {
					PaddingUtils.writeLeftPaddingNumber(sendBuffer, (int) dataObject.get(field.getName()), field.getLength());
				}
			});
			return sendBuffer.toString(charsets);
		}
		return null;
	}

	public Map<String, Object> toMessageObject(String interfaceId, String dataString, Charset charset) {
		Optional<JsonBodySpec> result = interfaceSpecs.stream()
				.filter(interfaceSpec -> interfaceId.equals(interfaceSpec.getInterfaceId())).findFirst();
		Map<String, Object> dataObject = new LinkedHashMap<>();
		if (result.isPresent()) {
			JsonBodySpec interfaceSpec = result.get();
			List<FieldSpec> bodyFieldList = interfaceSpec.getBodyFieldList();
			ByteBuf messageBuffer = Unpooled.copiedBuffer(dataString, charset);
			AtomicInteger offset = new AtomicInteger(0);
			bodyFieldList.forEach((Consumer<? super FieldSpec>) field -> {
				if ("STRING".equals(field.getType())) {
					dataObject.put(field.getName(), messageBuffer.slice(offset.get(), field.getLength()).toString(charset).trim());
				}
				if ("INTEGER".equals(field.getType())) {
					dataObject.put(field.getName(), Integer.valueOf(messageBuffer.slice(offset.get(), field.getLength()).toString(charset)));
				}
				offset.addAndGet(field.getLength());
			});
			return dataObject;
		}else return null;
	}

	public static void main(String[] args) throws StreamReadException, DatabindException, IOException {
		ClassPathResource resource = new ClassPathResource("if_message_spec.json");
		InputStream input = resource.getInputStream();
		ObjectMapper objectMapper = new ObjectMapper();
		List<JsonBodySpec> interfaceSpecs = objectMapper.readValue(input, new TypeReference<List<JsonBodySpec>>() {
		});
		JsonBodySpec interfaceSpec = interfaceSpecs.get(0);
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
