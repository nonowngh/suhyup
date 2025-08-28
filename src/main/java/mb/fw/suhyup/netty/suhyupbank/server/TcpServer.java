package mb.fw.suhyup.netty.suhyupbank.server;

import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import mb.fw.suhyup.dto.InterfaceSpec;
import mb.fw.suhyup.service.WebClientService;

@Slf4j
public class TcpServer {

	private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    
    private int bindPort;
    
    private final WebClientService webClientService;
    
    private final List<InterfaceSpec> interfaceSpecList;	
    
	public TcpServer(int bindPort, WebClientService webClientService, List<InterfaceSpec> interfaceSpecList) {
		this.bindPort = bindPort;
		this.webClientService = webClientService;
		this.interfaceSpecList = interfaceSpecList;
	}

	public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        Thread serverThread = new Thread(() -> {
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) {
                         ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), new TcpServerHandler(webClientService, interfaceSpecList));
                     }
                 });

                ChannelFuture f = b.bind(bindPort).sync();
                log.info("suhyup-tcp-server started on port " + bindPort);
                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                shutdown();
            }
        });

        serverThread.setName("suhyup-tcp-server");
        serverThread.start();
    }

    public void shutdown() {
        if (bossGroup != null) bossGroup.shutdownGracefully();
        if (workerGroup != null) workerGroup.shutdownGracefully();
        log.info("suhyup-tcp-server shutdown");
    }
}
