package mb.fw.suhyup.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "tcp.suhyup-bank")
public class NettyProperties {

	private String host;
    private int port;
}
