package mb.fw.suhyup.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mb.fw.suhyup.dto.RequestMessage;

@RestController
@RequestMapping("/esb/api")
@Slf4j
public class CommonController {

	@PostMapping("call-suhyupbank")
    public String echoMessage(@RequestBody RequestMessage requestMessage) {
		log.debug("in message : {}", requestMessage);
		
		log.info("suhyupbank call data : [{}], size : {}(bytes)", requestMessage.getData(), requestMessage.getData().getBytes().length);
        return "Received: " + requestMessage;
    }
}
