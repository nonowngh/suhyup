package mb.fw.suhyup.configuration;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import mb.fw.suhyup.dto.InterfaceSpec;
import mb.fw.suhyup.netty.suhyupbank.server.TcpServer;
import mb.fw.suhyup.service.WebClientService;

@Data
@Configuration
@ConditionalOnProperty(name = "tcp.server.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "tcp.server")
public class NettyServerProperties {

    private int bindPort;
    
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    TcpServer tcpServer(WebClientService webClientService, List<InterfaceSpec> interfaceSpecList) {
    	return new TcpServer(bindPort, webClientService, interfaceSpecList);
    }
}
