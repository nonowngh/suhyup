package mb.fw.suhyup.dto;

import java.util.List;

import lombok.Data;

@Data
public class InterfaceSpec {

	private String interfaceId;

	private String messageTypeCode;

	private String transactionTypeCode;

	private List<FieldSpec> bodyFieldList;

	@Data
	public static class FieldSpec {
		
		String name;
		
		String type;
		
		int length;
	}

}
