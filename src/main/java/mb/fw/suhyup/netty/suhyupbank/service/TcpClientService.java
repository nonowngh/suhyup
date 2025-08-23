package mb.fw.suhyup.netty.suhyupbank.service;

import org.springframework.stereotype.Service;

import mb.fw.suhyup.netty.suhyupbank.TcpClient;

@Service
public class TcpClientService {

    private final TcpClient tcpClient;
    
    public TcpClientService(TcpClient tcpClient) {
    	this.tcpClient = tcpClient;
    }
    
    public String sendRequest(String message) throws Exception {
        return tcpClient.sendMessage(message);
    }
    
    

}
