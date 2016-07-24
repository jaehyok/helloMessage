package team.hellobro.gw.test.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

public class SendTest 
{
	private String data = "<request>"
			+ "		<message>"
			+ "			<to>821029652189</to>"
			+ "			<messageId>0500000040E10D9C</messageId>"
			+ "			<status>0</status>"
			+ "			<remainingBalance>251.34424000</remainingBalance>"
			+ "			<messagePrice>0.04000000</messagePrice>"
			+ "			<network>45002</network>"
			+ "		</message>"
			+ "</request>";
	@Test
	public void sendHttp()
	{
		try
		{
			
			URL url = new URL("https://localhost:2000/hellomessage/sms");
//			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals("localhost"));
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setDoInput(true);
			con.setDoOutput(true);
			
			OutputStream out = con.getOutputStream();
			out.write(data.getBytes());
			
			InputStream in = con.getInputStream();
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			
			System.out.println("Response message : " + con.getResponseMessage());
			
			byte[] buf = new byte[1024];
			int read;
			
			while((read = in.read(buf)) != -1)
			{
				result.write(buf, 0, read);
			}
			
			System.out.println(result.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			Assert.fail();
		}
	}
	
	@Test
	public void sendHttps()
	{
		try
		{
//			DOMConfigurator.configure("/Users/kwonsm/git/helloMessage/HelloMessage_APIServer/env/Log4j.xml");
			
			URI uri = new URI("https://localhost:2000/hellomessage/sms");
			SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			
			EventLoopGroup group = new NioEventLoopGroup();
			try
			{
				Bootstrap boot = new Bootstrap();
				boot.group(group);
				boot.channel(NioSocketChannel.class).handler(new HttpsClientInitializer(sslContext));
				
				Channel channel = boot.connect("localhost", 2000).sync().channel();
				
				FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.getRawPath());
				request.headers().set(HttpHeaderNames.HOST, uri.getHost());
				request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
				request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
				request.headers().add(HttpHeaderNames.CONTENT_TYPE, "application/xml");
				
				ByteBuf contentBuf = Unpooled.copiedBuffer(data.getBytes());
				request.headers().set(HttpHeaderNames.CONTENT_LENGTH, contentBuf.readableBytes());
				request.content().clear().writeBytes(contentBuf);
				
				System.out.println(request);
				
				channel.writeAndFlush(request);
				
				channel.closeFuture().sync();
			}
			finally
			{
				group.shutdownGracefully();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			Assert.fail();
		}
	}
}
