package mb.fw.suhyup.configuration;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mb.fw.suhyup.dto.InterfaceSpec;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "interface.spec.enable-json-file", havingValue = "true")
public class InterfaceSpecConfig {

	@Value("classpath:interface-spec.json")
	Resource jsonFile;

	@Bean
	List<InterfaceSpec> initInterfaceSpec() throws Exception {
		try (InputStream is = jsonFile.getInputStream()) {
			log.info("deserialize interface spec json file");
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(is, new TypeReference<List<InterfaceSpec>>() {
			});
		} catch (Exception e) {
			log.error("deserialize 'if_masseage_spec.json' file error!", e);
			throw e;
		}
	}

}
