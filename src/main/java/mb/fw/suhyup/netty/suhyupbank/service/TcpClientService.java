package mb.fw.suhyup.netty.suhyupbank.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Service;

import mb.fw.suhyup.converter.MessageConverter;
import mb.fw.suhyup.netty.suhyupbank.TcpClient;

@Service
public class TcpClientService {

	private final TcpClient tcpClient;
	private final MessageConverter converter;
	
	private static Charset charsets = StandardCharsets.UTF_8;

	public TcpClientService(TcpClient tcpClient, MessageConverter converter) {
		this.tcpClient = tcpClient;
		this.converter = converter;
	}

	public String sendRequest(String message) throws Exception {
		return tcpClient.sendMessage(message);
	}

	public Map<String, Object> sendRequestConvertMessage(String interfaceId, Map<String, Object> dataObject) throws Exception {
		String responseMessage = tcpClient
				.sendMessage(converter.toMessageString(interfaceId, dataObject, charsets));
		return converter.toMessageObject(interfaceId, responseMessage, charsets);
	}

}
