package mb.fw.suhyup.dto;

import lombok.Data;

@Data
public class RequestMessage {

	private String interfaceId;
	
	private String requestTime;
	
	private String data;
	
}
