package mb.fw.suhyup.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mb.fw.suhyup.dto.InterfaceSpec;
import mb.fw.suhyup.netty.suhyupbank.TcpClient;
import mb.fw.suhyup.netty.suhyupbank.converter.MessageConverter;

@Service
public class TcpClientService {

	private final TcpClient tcpClient;
	
	private static Charset charsets = StandardCharsets.UTF_8;
	
	@Autowired(required = false)
	List<InterfaceSpec> interfaceSpecList;	

	public TcpClientService(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	public String send(String requestMessage) throws Exception {
		return tcpClient.sendMessage(requestMessage, "0600", "300002");
	}

	public Object sendConvertMessage(String interfaceId, Map<String, Object> requestObject) throws Exception {
		Optional<InterfaceSpec> result = interfaceSpecList.stream()
				.filter(interfaceSpec -> interfaceId.equals(interfaceSpec.getInterfaceId())).findFirst();
		if (result.isPresent()) {
			InterfaceSpec interfaceSpec = result.get();
			String responseMessage = tcpClient.sendMessage(MessageConverter.toMessageString(interfaceSpec, requestObject, charsets), interfaceSpec.getMessageTypeCode(), interfaceSpec.getTransactionTypeCode());
			return MessageConverter.toMessageObject(interfaceSpec, responseMessage, charsets);
		}
		return "interface-spec 설정 파일에 해당 인터페이스 아이디가 명시되지 않았습니다.";
	}

}
