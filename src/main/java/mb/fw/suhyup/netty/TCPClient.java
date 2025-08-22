package mb.fw.suhyup.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TCPClient {

	private final String host;
	private final int port;
	private final Bootstrap bootstrap;
	private final EventLoopGroup group;

	public TCPClient(String host, int port) {
		this.host = host;
		this.port = port;
		this.group = new NioEventLoopGroup();

		this.bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) {
						ch.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
								// 응답 처리 필요시
							}
						});
					}
				});
		log.info("TCP Client Start..");
	}

	public void send(String message) throws InterruptedException {
		ChannelFuture future = bootstrap.connect(host, port).sync();
		future.channel().writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
		future.channel().closeFuture().sync();
	}
}
