package mb.fw.suhyup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mb.fw.suhyup.dto.RequestMessage;
import mb.fw.suhyup.netty.TCPClient;

@RestController
@RequestMapping("/esb/api")
@Slf4j
public class CommonController {
	
	@Autowired
	TCPClient tcpClient;

	@PostMapping("call-suhyupbank")
    public String echoMessage(@RequestBody RequestMessage requestMessage) {
		log.debug("in message : {}", requestMessage);
		
		String requestStr = requestMessage.getData();
		log.info("suhyupbank call data : [{}], size : {}(bytes)", requestStr, requestStr.getBytes().length);
        
		try {
			tcpClient.send(requestStr);
		} catch (InterruptedException e) {
			log.error("error : ", e);
		}
		return "Received: " + requestMessage;
    }
}
