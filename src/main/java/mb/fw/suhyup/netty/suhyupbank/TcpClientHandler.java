package mb.fw.suhyup.netty.suhyupbank;

import java.util.concurrent.BlockingQueue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpClientHandler extends ChannelInboundHandlerAdapter  {
	
	BlockingQueue<ByteBuf> asyncResponseQueue;
	
	public TcpClientHandler(BlockingQueue<ByteBuf> asyncResponseQueue2) {
		this.asyncResponseQueue = asyncResponseQueue2;
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
		super.channelActive(ctx);
    }
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}	

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        asyncResponseQueue.offer((ByteBuf) msg);
		ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
    }
}


