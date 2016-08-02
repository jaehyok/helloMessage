package team.hellobro.gw.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import team.hellobro.gw.api.transform.Message;

public class MessagePoolConnection
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private JedisPool connectionPool;
	
	public void init(String _host, int _port, int _idle, int _max)
	{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMinIdle(_idle);
		config.setMaxTotal(_max);
		
		this.connectionPool = new JedisPool(config, _host, _port);
		
		if(this.logger.isInfoEnabled())
		{
			this.logger.info("Redis connection pool is created. host:{}, port:{}, idle:{}, max:{}", _host, _port, _idle, _max);
		}
	}
	
	public void sendSms(Message _message) throws Exception
	{
		if(this.logger.isInfoEnabled())
		{
			this.logger.info("Insert message to redis : {}", _message);	
		}
		
		Jedis jedis = null;
		
		try
		{
			jedis = this.connectionPool.getResource();
		}
		catch(Exception e)
		{
			this.logger.error("Can not insert message to redis message pool.", _message, e);
			
			throw e;
		}
		finally
		{
			if(jedis != null)
			{
				jedis.close();
			}
		}
	}
	
	public void destroy()
	{
		this.connectionPool.destroy();
		
		if(this.logger.isInfoEnabled())
		{
			this.logger.info("Redis connection pool is closed.");
		}
	}
}
