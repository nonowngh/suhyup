package mb.fw.suhyup.netty.suhyupbank.message;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mb.fw.suhyup.util.PaddingUtils;

public class TcpHeader {

	private int msgLength; //전문 전체 길이(4)
	private String processType; // 업무 구분(3)
	private String orgCode; // 기관 코드(3)
	private String msgTypeCode; // 전문 종별 코드(4)
	private String txTypeCode; // 거래 구분 코드(6)
	private String statusCode; // 상태 코드(3)
 	private String srFlag; // 송수신 FLAG(1)
	private String resultTypeCode; // 응답 구분 코드(1)
	private String resultCode; // 응답 코드(4)
	private String sndTime; // 전송 일시(14)
	private String msgNo; // 전문 관리 번호(12)
	private String filler; // 필러(19)
	private int bodyLength; //전문 전체 길이(header + body)
	
	private ByteBuf headerBuffer;
	
    public static final int HEADER_LENGTH = 74;
    
	private static Charset charsets = StandardCharsets.UTF_8;

	public TcpHeader(String processType, String orgCode, String msgTypeCode, String txTypeCode, String statusCode,
			String srFlag, String resultTypeCode, String resultCode, String sndTime, String msgNo, int bodyLength) {
		this.processType = processType;
		this.orgCode = orgCode;
		this.msgTypeCode = msgTypeCode;
		this.txTypeCode = txTypeCode;
		this.statusCode = statusCode;
		this.srFlag = srFlag;
		this.resultTypeCode = resultTypeCode;
		this.resultCode = resultCode;
		this.sndTime = sndTime;
		this.msgNo = msgNo;
		this.bodyLength = bodyLength;
	}
	
	public TcpHeader(ByteBuf totalBuffer) {
		this.headerBuffer = totalBuffer.slice(0, HEADER_LENGTH);
	}

	public ByteBuf makeSendHeader() {
		ByteBuf buffer = Unpooled.buffer(HEADER_LENGTH + bodyLength);
		PaddingUtils.writeLeftPaddingNumber(buffer, (HEADER_LENGTH + bodyLength - 4), 4);
		PaddingUtils.writeRightPaddingString(buffer, processType, 3);
		PaddingUtils.writeRightPaddingString(buffer, orgCode, 3);
		PaddingUtils.writeRightPaddingString(buffer, msgTypeCode, ４);
		PaddingUtils.writeRightPaddingString(buffer, txTypeCode, ６);
		PaddingUtils.writeRightPaddingString(buffer, statusCode, 3);
		PaddingUtils.writeRightPaddingString(buffer, srFlag, 1);
		PaddingUtils.writeRightPaddingString(buffer, resultTypeCode, 1);
		PaddingUtils.writeRightPaddingString(buffer, resultCode, 4);
		PaddingUtils.writeRightPaddingString(buffer, sndTime, 14);
		PaddingUtils.writeRightPaddingString(buffer, msgNo, 12);
		PaddingUtils.writeRightPaddingString(buffer, "", 19);
		return buffer;
	}

	public int getHeaderLength() {
		return HEADER_LENGTH;
	}

	public String getMsgTypeCode() {		
		return headerBuffer.slice(10, 4).toString(charsets).trim();
	}
}
