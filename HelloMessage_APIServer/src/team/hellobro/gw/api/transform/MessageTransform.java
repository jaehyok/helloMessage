package team.hellobro.gw.api.transform;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.balam.exof.module.listener.RequestContext;
import team.balam.exof.module.listener.handler.transform.BadFormatException;
import team.balam.exof.module.listener.handler.transform.HttpTransform;
import team.balam.exof.module.service.ServiceObject;
import team.hellobro.gw.api.RequestContextKey;

public class MessageTransform extends HttpTransform
{
	private static Logger logger = LoggerFactory.getLogger(MessageTransform.class);
	
	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;
	
	static
	{
		try 
		{
			JAXBContext unMaCtx = JAXBContext.newInstance(Request.class);
			unmarshaller = unMaCtx.createUnmarshaller();
			
			JAXBContext maCtx = JAXBContext.newInstance(Response.class);
			marshaller = maCtx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		}
		catch(JAXBException e) 
		{
			logger.error("Can not create jaxb instance.", e);
		}
	}
	
	@Override
	public ServiceObject transform(FullHttpRequest _msg) throws Exception 
	{
		try
		{
			ServiceObject serviceObject = super.transform(_msg);
			serviceObject.setCloseSessionByError(true);
			
			ByteBuf content = _msg.content();
			if(content.isReadable())
			{
				byte[] buf = new byte[content.capacity()];
				content.getBytes(0, buf);
				
				if(logger.isDebugEnabled())
				{
					logger.debug("Request xml : {}", new String(buf));
				}
				
				try
				{
					Request request = unmarshal(new ByteArrayInputStream(buf));
					serviceObject.setServiceParameter(new Object[]{request});
				}
				catch(JAXBException je)
				{
					Response.BAD_REQUEST().send();
					
					throw je;
				}
				
				//FullHttpResponse를 보낼 때 사용하기 위해서 저장.
				//keepAlive등의 설정을 사용하기 우해서
				RequestContext.set(RequestContextKey.HTTP_REQUEST, _msg);
			}
			else
			{
				if(content.hasArray())
				{
					logger.error("Request info.\n{}\n{}", _msg.toString(), new String(content.array()));
				}
				
				Response.NO_CONTENT().send();
				
				throw new Exception("Can not read request. " + content.toString());
			}
			
			return serviceObject;
		}
		catch(Exception e)
		{
			throw new BadFormatException(e);
		}
	}
	
	public static Request unmarshal(ByteArrayInputStream _stream) throws JAXBException
	{
		return (Request)unmarshaller.unmarshal(_stream);
	}
	
	public static String marshal(Response _response) throws JAXBException
	{
		StringWriter responseString = new StringWriter();
		marshaller.marshal(_response, responseString);
		
		return responseString.toString();
	}
}
