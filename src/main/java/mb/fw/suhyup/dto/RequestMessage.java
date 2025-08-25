package mb.fw.suhyup.dto;

import java.util.Map;

import lombok.Data;

@Data
public class RequestMessage {

	private String interfaceId;
	
	private String requestTime;
	
	private String dataString;
	
	private Map<String, Object> data;
	
}
