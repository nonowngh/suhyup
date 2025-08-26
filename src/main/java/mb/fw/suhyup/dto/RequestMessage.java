package mb.fw.suhyup.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class RequestMessage {

	private String interfaceId;

	private String requestTime;

	private Object data;

	public static void main(String[] args) throws JsonProcessingException {
		Map<String, Object> dataMap = new LinkedHashMap<>();
		dataMap.put("업무구분", "Alice");
		dataMap.put("고객명", 30);
		dataMap.put("고객번호", "dososo");
		dataMap.put("실명(사업자)번호", "12345");
		dataMap.put("휴대전화번호", "01012345678");
		dataMap.put("자택전화번호", "02245865845");

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(dataMap);

		System.out.println(jsonString);
	}

}
