package team.hellobro.gw.test.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslContext;
import io.netty.util.CharsetUtil;

public class HttpsClientInitializer extends ChannelInitializer<SocketChannel>
{
	private final SslContext sslContext;
	
	public HttpsClientInitializer(SslContext _ctx)
	{
		this.sslContext = _ctx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ch.pipeline().addLast(sslContext.newHandler(ch.alloc()))
			.addLast(new HttpClientCodec())
			.addLast(new HttpSnoopClientHandler());
	}
	
	public class HttpSnoopClientHandler extends SimpleChannelInboundHandler<HttpObject>
	{
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception
		{
			if(msg instanceof HttpResponse)
			{
				HttpResponse response = (HttpResponse)msg;
				System.out.println(response.toString());
			}
			
			if(msg instanceof HttpContent)
			{
				HttpContent content = (HttpContent)msg;
				
				System.out.println(content.content().toString(CharsetUtil.UTF_8));
				
				if(content instanceof LastHttpContent)
				{
					ctx.close();
				}
			}
		}
	}
}
