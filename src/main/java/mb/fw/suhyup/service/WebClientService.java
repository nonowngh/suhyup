package mb.fw.suhyup.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebClientService {

	private final WebClient webClient;

	public WebClientService(WebClient webClient) {
		this.webClient = webClient;
	}

//	public Mono<String> callApp(Map<String,Object> requestObject) {
//        return webClient.post()
//        		.bodyValue(requestObject)
//                .retrieve()
//                .bodyToMono(String.class);
//    }
	
	public String callApp(Map<String,Object> requestObject) {
        return webClient.post()
        		.bodyValue(requestObject)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
