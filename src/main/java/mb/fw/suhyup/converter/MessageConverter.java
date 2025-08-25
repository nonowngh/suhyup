package mb.fw.suhyup.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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

@Component
@Slf4j
public class MessageConverter {

	@Value("classpath:if_masseage_spec.json")
	private Resource jsonFile;

	List<JsonBodySpec> interfaceSpecs;

	public MessageConverter() {
		try {
			InputStream input = jsonFile.getInputStream();
			ObjectMapper objectMapper = new ObjectMapper();
			interfaceSpecs = objectMapper.readValue(input, new TypeReference<List<JsonBodySpec>>() {
			});
		} catch (Exception e) {
			log.error("deserialize 'if_masseage_spec.json' file error!", e);
		}
	}

	public String toMessageString(String interfaceId, Map<String, Object> dataObject) {
		Optional<JsonBodySpec> result = interfaceSpecs.stream()
				.filter(interfaceSpec -> interfaceId.equals(interfaceSpec.getInterfaceId())).findFirst();
		if (result.isPresent()) {
//			JsonBodySpec interfaceSpec = result.get();
//			List<FieldSpec> bodyFieldList = interfaceSpec.getBodyFieldList();
//			bodyFieldList.forEach(field -> );
		}
		return null;
	}

	public static void main(String[] args) throws StreamReadException, DatabindException, IOException {
		ClassPathResource resource = new ClassPathResource("if_masseage_spec.json");
		InputStream input = resource.getInputStream();
		ObjectMapper objectMapper = new ObjectMapper();
		List<JsonBodySpec> interfaceSpecs = objectMapper.readValue(input, new TypeReference<List<JsonBodySpec>>() {
		});
		JsonBodySpec interfaceSpec = interfaceSpecs.get(0);
		List<FieldSpec> bodyFieldList = interfaceSpec.getBodyFieldList();
		ByteBuf sendBuffer = Unpooled.buffer();
		bodyFieldList.forEach((Consumer<? super FieldSpec>)field -> {
			
			if("STRING".equals(field.getType())){
				field
			}
			field.getName());
		});
		System.out.println(bodyFieldList);
	}

}
