package mb.fw.suhyup.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "tcp.client")
public class NettyProperties {

	private String host;
    private int port;
    private int clientTimeoutSec;

}
