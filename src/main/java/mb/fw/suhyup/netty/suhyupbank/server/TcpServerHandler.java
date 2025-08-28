package mb.fw.suhyup.netty.suhyupbank.server;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import mb.fw.suhyup.dto.InterfaceSpec;
import mb.fw.suhyup.netty.suhyupbank.converter.MessageConverter;
import mb.fw.suhyup.netty.suhyupbank.message.TcpHeader;
import mb.fw.suhyup.service.WebClientService;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {

	private final WebClientService webClientService;
	private final List<InterfaceSpec> interfaceSpecList;

	public TcpServerHandler(WebClientService webClientService, List<InterfaceSpec> interfaceSpecList) {
		this.webClientService = webClientService;
		this.interfaceSpecList = interfaceSpecList;
	}

	private static Charset charsets = StandardCharsets.UTF_8;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf inbytes = (ByteBuf) msg;
		TcpHeader tcpHeader = new TcpHeader(inbytes);
		String messageTypeCode = tcpHeader.getMsgTypeCode();
		Optional<InterfaceSpec> result = interfaceSpecList.stream()
				.filter(interfaceSpec -> messageTypeCode.equals(interfaceSpec.getMessageTypeCode())).findFirst();
		if (result.isPresent()) {
			InterfaceSpec interfaceSpec = result.get();
			String receivedBodyMessage = inbytes.toString(tcpHeader.getHeaderLength(),
					inbytes.readableBytes() - tcpHeader.getHeaderLength(), CharsetUtil.UTF_8);
			Map<String, Object> requestObject = MessageConverter.toMessageObject(interfaceSpec, receivedBodyMessage,
					charsets);
			ctx.writeAndFlush(Unpooled.copiedBuffer(webClientService.callApp(requestObject), charsets));
		}
		inbytes.release();
		ctx.close();
	}
}
