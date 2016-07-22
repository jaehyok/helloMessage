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
			JAXBContext jctx = JAXBContext.newInstance(Request.class);
			
			marshaller = jctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			unmarshaller = jctx.createUnmarshaller();
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
			
			ByteBuf content = _msg.content();
			if(content.isReadable())
			{
				byte[] buf = new byte[content.capacity()];
				content.getBytes(0, buf);
				
				if(logger.isInfoEnabled())
				{
					logger.info("Request xml : {}", new String(buf));
				}
				
				Request request = unmarshal(new ByteArrayInputStream(buf));
				serviceObject.setRequest(request);
				
				RequestContext.set(RequestContextKey.HTTP_REQUEST, _msg);
			}
			else
			{
				if(content.hasArray())
				{
					logger.error("Request xml : {}", new String(content.array()));
				}
				
				throw new Exception("Can not read request. " + content.toString());
			}
			
			return serviceObject;
		}
		catch(Exception e)
		{
			RequestContext.getSession().close();
			
			throw e;
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
