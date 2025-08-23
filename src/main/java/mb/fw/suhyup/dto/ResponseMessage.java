package mb.fw.suhyup.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseMessage {

	private String interfaceId;
	
	private String transactionId;
	
	private String resultCode;
	
	private String resultMessage;

}
