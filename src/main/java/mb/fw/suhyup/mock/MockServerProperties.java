package mb.fw.suhyup.mock;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "mockserver")
public class MockServerProperties {

	private boolean enabled;
	private int port;
}
