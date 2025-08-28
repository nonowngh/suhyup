package mb.fw.suhyup.mock;

import javax.annotation.PostConstruct;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.stereotype.Component;

@Component
public class MockServerStarter {
	private ClientAndServer mockServer;

	  @PostConstruct
    public void startServer() {
        mockServer = ClientAndServer.startClientAndServer(1080);

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/suhyup/app-api")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withBody("A00000000000000000000000000000000000000000000000030dososo   12345        0101234567802245865845   ")
        );
    }

    public void stopServer() {
        mockServer.stop();
    }
}
