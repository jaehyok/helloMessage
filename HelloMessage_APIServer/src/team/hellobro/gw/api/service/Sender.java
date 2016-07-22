package team.hellobro.gw.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.balam.exof.module.service.annotation.Service;
import team.balam.exof.module.service.annotation.ServiceDirectory;
import team.hellobro.gw.api.transform.Request;

@ServiceDirectory
public class Sender 
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Service(name="sms")
	public void sendSms(Request _request) throws Exception
	{
		if(this.logger.isDebugEnabled())
		{
			this.logger.debug("[SMS]Receive content : {}", _request.toString());
		}
	}
}
