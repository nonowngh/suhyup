package mb.fw.suhyup.converter;

import java.util.List;

import lombok.Data;

@Data
public class JsonBodySpec {

	private String interfaceId;

	private String messageId;

	private List<FieldSpec> bodyFieldList;

	@Data
	public static class FieldSpec {
		
		String name;
		
		String type;
		
		int length;
	}

}
