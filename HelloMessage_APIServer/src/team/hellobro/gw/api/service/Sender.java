package team.hellobro.gw.api.service;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.balam.exof.module.service.annotation.Service;
import team.balam.exof.module.service.annotation.ServiceDirectory;
import team.balam.exof.module.service.annotation.Startup;
import team.hellobro.gw.api.transform.Request;
import team.hellobro.gw.api.transform.RequestContents;
import team.hellobro.gw.api.transform.Response;
import team.hellobro.gw.api.transform.ResponseContents;

@ServiceDirectory
public class Sender 
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MessagePoolConnection messagePoolConnection;
	
	@Startup(serviceName="sms")
	public void init(String _isTest, String _redisHost, String _redisPort, String _idleConnectionSize, String _maxConnectionSize)
	{
		if(this.logger.isInfoEnabled())
		{
			this.logger.info("Init Sender Service. isTest:{}, Redis host:{}, Redis port:{}, Connection pool idle size:{} / max size:{}",
					_isTest, _redisHost, _redisPort, _idleConnectionSize, _maxConnectionSize);
		}
		
		if("yes".equalsIgnoreCase(_isTest))
		{
			this.messagePoolConnection = new TestMessagePoolConnection();
		}
		else
		{
			this.messagePoolConnection = new MessagePoolConnection();
		}
		
		int port = Integer.parseInt(_redisPort);
		int idle = Integer.parseInt(_idleConnectionSize);
		int max = Integer.parseInt(_maxConnectionSize);
		
		this.messagePoolConnection.init(_redisHost, port, idle, max);
	}
	
	@Service(name="sms")
	public void sendSms(Request _request) throws Exception
	{
		List<ResponseContents> resultList = new LinkedList<>();
		Response response = new Response();
		response.setStatus(HttpResponseStatus.OK);
		response.setResult(resultList);
		
		List<RequestContents> messageList = _request.getMessage();
		for(RequestContents message : messageList)
		{
			String resultCode = "100";
			
			try
			{
				this.messagePoolConnection.sendSms(message);
			}
			catch(Exception e)
			{
				resultCode = "900";
				
				this.logger.error("Can not insert redis message pool.", e);
			}
			finally
			{
				ResponseContents responseContents = new ResponseContents();
				responseContents.setMessageId(message.getMessageId());
				responseContents.setGatewayId("gateway id");
				responseContents.setResultCode(resultCode);
				resultList.add(responseContents);
			}
		}
		
		if(this.logger.isInfoEnabled())
		{
			this.logger.info("[SMS]Response : {}", response.toString());
		}
		
		response.send();
	}
}
