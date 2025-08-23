package mb.fw.suhyup.netty.suhyupbank;

import java.util.concurrent.BlockingQueue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@lombok.extern.slf4j.Slf4j
public class TcpClientHandler extends ChannelInboundHandlerAdapter  {
	
	BlockingQueue<String> asyncResponseQueue;
	
	public TcpClientHandler(BlockingQueue<String> asyncResponseQueue) {
		this.asyncResponseQueue = asyncResponseQueue;
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("client channelActive : {}", ctx.channel().toString());
		super.channelActive(ctx);
    }
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("client channelInactive : {}", ctx.channel().toString());
		super.channelInactive(ctx);
	}	

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	ByteBuf buf = (ByteBuf) msg;
		String received = buf.toString(CharsetUtil.UTF_8);
        log.info("Receive message from server: " + received);
        asyncResponseQueue.offer(received);
		ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
    }
}


