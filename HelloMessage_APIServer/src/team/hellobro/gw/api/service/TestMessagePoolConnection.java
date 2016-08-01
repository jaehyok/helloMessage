package team.hellobro.gw.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hellobro.gw.api.transform.Request;

public class TestMessagePoolConnection extends MessagePoolConnection
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void init(String _host, int _port, int _idle, int _max)
	{
		this.logger.info("Redis connection pool info : {}:{}, idle:{}, max:{}", _host, _port, _idle, _max);
	}
	
	@Override
	public void sendSms(Request _request) throws Exception
	{
		this.logger.info("Insert request to redis : {}", _request);
	}
}
