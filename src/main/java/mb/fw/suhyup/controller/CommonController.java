package mb.fw.suhyup.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mb.fw.suhyup.dto.RequestMessage;
import mb.fw.suhyup.dto.ResponseMessage;
import mb.fw.suhyup.netty.suhyupbank.service.TcpClientService;

@RestController
@RequestMapping("/esb/api")
@Slf4j
public class CommonController {
	
	@Autowired
	TcpClientService tcpClientService;

	@PostMapping("call-suhyupbank-message")
    public ResponseMessage callSBMessage(@RequestBody RequestMessage requestMessage) throws Exception {
		
		String requestStr = requestMessage.getData().toString();
		log.info("suhyupbank call data : [{}], size : {}(bytes)", requestStr, requestStr.getBytes().length);
		
		return ResponseMessage.builder()
				.interfaceId(requestMessage.getInterfaceId())
				.resultCode("200")
				.resultData(tcpClientService.sendRequest(requestStr))
				.build();
    }
	
	@PostMapping("call-suhyupbank")
    public ResponseMessage callSB(@RequestBody RequestMessage requestMessage) throws Exception {
		
		Map<String, Object> dataObject = (Map<String, Object>) requestMessage.getData();
		log.info("suhyupbank call data : [{}]", dataObject);
		
		return ResponseMessage.builder()
				.interfaceId(requestMessage.getInterfaceId())
				.resultCode("200")
				.resultData(tcpClientService.sendRequestConvertMessage(requestMessage.getInterfaceId(), dataObject))
				.build();
    }
}
