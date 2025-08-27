package mb.fw.suhyup.netty.suhyupbank;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import mb.fw.suhyup.configuration.NettyProperties;
import mb.fw.suhyup.netty.suhyupbank.message.TcpHeader;

@Slf4j
@Component
public class TcpClient {

	@Autowired(required = false)
	NettyProperties properties;

	public String sendMessage(String message, String msgTypeCode, String txTypeCode) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		Channel channel = null;
		try {
			BlockingQueue<ByteBuf> asyncResponseQueue = new LinkedBlockingQueue<ByteBuf>();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
//					ch.pipeline().addLast(new mb.fw.net.common.codec.LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4, true));
					ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), new TcpClientHandler(asyncResponseQueue));
				}
			});

			ChannelFuture cf = bootstrap.connect(properties.getHost(), properties.getPort()).sync();

			ByteBuf bodyBuffer = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
			TcpHeader tcpHeader = new TcpHeader("CAS", "999", msgTypeCode, txTypeCode, "", "B", "0", "0000", "20250825125130",
					"CAS912345678", bodyBuffer.readableBytes());
			ByteBuf headerBuffer = tcpHeader.makeSendHeader();
			ByteBuf sendBuffer = Unpooled.wrappedBuffer(headerBuffer, bodyBuffer);
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						future.channel().writeAndFlush(sendBuffer).awaitUninterruptibly();
					} else {
						log.error("tcp 서버 연결 오류");
					}
				}
			});

			ByteBuf resBuffer = null;
			resBuffer = asyncResponseQueue.poll(properties.getClientTimeoutSec(), TimeUnit.SECONDS);
			if (resBuffer == null) {
				log.error("Response timeout error");
				return "No response message from Tcp server";
			}

			String receivedBodyMessage = resBuffer.toString(tcpHeader.getHeaderLength(),
					resBuffer.readableBytes() - tcpHeader.getHeaderLength(), CharsetUtil.UTF_8);
			log.info("Receive message from server: " + receivedBodyMessage);

			return receivedBodyMessage;
		} finally {
			if (channel != null && channel.isActive())
				channel.close();
			group.shutdownGracefully();
		}
	}
}
