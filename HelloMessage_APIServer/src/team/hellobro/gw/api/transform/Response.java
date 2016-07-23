package team.hellobro.gw.api.transform;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import team.balam.exof.module.listener.RequestContext;
import team.hellobro.gw.api.RequestContextKey;

@XmlRootElement
public class Response 
{
	private HttpResponseStatus status;
	private String resultCode;
	
	public Response()
	{
		this.success();
	}
	
	public void success()
	{
		this.status = HttpResponseStatus.OK;
	}
	
	public void fail()
	{
		this.status = HttpResponseStatus.BAD_REQUEST;
	}

	public String getResultCode() 
	{
		return resultCode;
	}

	@XmlElement
	public void setResultCode(String resultCode) 
	{
		this.resultCode = resultCode;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder str = new StringBuilder();
		str.append("result code:").append(this.resultCode);
		
		return str.toString();
	}
	
	public void send() throws Exception
	{
		String responseXml = MessageTransform.marshal(this);
		ByteBuf content = Unpooled.copiedBuffer(responseXml, CharsetUtil.UTF_8);
		
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, this.status, content);
		HttpHeaders responseHeader = response.headers();
		responseHeader.set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		
		FullHttpRequest httpRequest = RequestContext.get(RequestContextKey.HTTP_REQUEST);
		if(HttpUtil.isKeepAlive(httpRequest))
		{
			// Add 'Content-Length' header only for a keep-alive connection.
			responseHeader.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
			// Add keep alive header as per:
			responseHeader.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		
		RequestContext.writeResponse(response);
	}
}
