package team.hellobro.gw.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import team.hellobro.gw.api.transform.Request;

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
			this.logger.info("Redis connection pool is created.");
		}
	}
	
	public void sendSms(Request _request) throws Exception
	{
		Jedis jedis = null;
		
		try
		{
			jedis = this.connectionPool.getResource();
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
