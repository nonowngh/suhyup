package mb.fw.suhyup.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mb.fw.suhyup.netty.TCPClient;

@Configuration
public class NettyConfiguration {	
    
	@Bean
    TCPClient tcpClient(NettyProperties props) {
        return new TCPClient(props.getHost(), props.getPort());
    }
}
