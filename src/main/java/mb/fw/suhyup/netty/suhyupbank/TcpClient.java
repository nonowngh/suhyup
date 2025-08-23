package mb.fw.suhyup.netty.suhyupbank;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
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
import lombok.extern.slf4j.Slf4j;
import mb.fw.suhyup.configuration.NettyProperties;

@Slf4j
@Component
public class TcpClient {

	@Autowired(required = false)
	NettyProperties properties;

	public String sendMessage(String message) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Channel channel = null;
        try {
        	BlockingQueue<String> asyncResponseQueue = new LinkedBlockingQueue<String>();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                     .channel(NioSocketChannel.class)
                     .handler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                             ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), new TcpClientHandler(asyncResponseQueue));
                         }
                     });

            // 서버에 연결
            ChannelFuture future = bootstrap.connect(properties.getHost(), properties.getPort()).sync();
            
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        // 연결 성공 후 메시지 보내기
                        future.channel().writeAndFlush(message).awaitUninterruptibly();
                    } else {
                        log.error("tcp 서버 연결 오류");
                    }
                }
            });
//            future.channel().writeAndFlush(message).awaitUninterruptibly();
            
            String responseMessage = null;
			responseMessage = asyncResponseQueue.poll(properties.getClientTimeoutSec(), TimeUnit.SECONDS);
            if(responseMessage == null) {
            	log.error("No response message from Tcp server.");
            	return "Timeout Error : Tcp Server No Response";
            }			
            return responseMessage;
        } finally {
        	if (channel != null && channel.isActive()) channel.close();
            group.shutdownGracefully();
        }
    }
}
