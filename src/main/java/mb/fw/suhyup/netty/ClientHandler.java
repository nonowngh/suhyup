package mb.fw.suhyup.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
	private final String sendMessage;

    public ClientHandler(String sendMessage) {
        this.sendMessage = sendMessage;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 서버에 메시지 전송
        ctx.writeAndFlush(sendMessage);
    }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
