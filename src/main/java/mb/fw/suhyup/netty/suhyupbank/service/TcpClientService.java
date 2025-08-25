package mb.fw.suhyup.netty.suhyupbank.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import mb.fw.suhyup.converter.MessageConverter;
import mb.fw.suhyup.netty.suhyupbank.TcpClient;

@Service
public class TcpClientService {

    private final TcpClient tcpClient;
    private final MessageConverter converter;
    
    public TcpClientService(TcpClient tcpClient, MessageConverter converter) {
    	this.tcpClient = tcpClient;
    	this.converter = converter;
    }
    
    public String sendRequest(String message) throws Exception {
        return tcpClient.sendMessage(message);
    }

	public String sendRequest(String interfaceId, Map<String, Object> dataObject) throws Exception {
		String message = converter.toMessageString(interfaceId, dataObject);
		
		return tcpClient.sendMessage(message);
	}    

}
