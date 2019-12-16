import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class MyHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof WebSocketFrame) {
            if(msg instanceof TextWebSocketFrame){
                String in = ((TextWebSocketFrame) msg).text();
                System.out.println(in);

                ctx.writeAndFlush(new TextWebSocketFrame("ok"));
            }
        }
    }
}

