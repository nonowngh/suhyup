package mb.fw.suhyup.configuration;

import org.crsh.console.jline.internal.Log;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "web.app-service")
public class WebClientConfig {
	
	@Setter
	private String url;
	
	@Bean
    WebClient webClient(WebClient.Builder builder) {
		Log.info("init web-client -> " + url);
        return builder.baseUrl(url).build();
    }
}
