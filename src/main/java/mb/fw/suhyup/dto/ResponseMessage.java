package mb.fw.suhyup.dto;

import lombok.Data;

@Data
public class ResponseMessage {

	private String interfaceId;
	
	private String transactionId;
	
	private String resultCode;
	
	private String resultMessage;

}
