package team.hellobro.gw.api.transform;

import io.netty.handler.codec.http.HttpResponseStatus;

public interface Response 
{
	void send() throws Exception;
	
	static Response SUCCESS()
	{
		HttpResponseStatus status = HttpResponseStatus.OK;
		return new ResponseImpl("Success", status.code(), status);
	}
	
	static Response BAD_REQUEST()
	{
		HttpResponseStatus status = HttpResponseStatus.BAD_REQUEST;
		return new ResponseImpl("Bad request", status.code(), status);
	}
	
	static Response NO_CONTENT()
	{
		HttpResponseStatus status = HttpResponseStatus.NO_CONTENT;
		return new ResponseImpl("No Content", status.code(), status);
	}
	
	static Response INTERNAL_SERVER_ERROR()
	{
		HttpResponseStatus status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
		return new ResponseImpl("Can not send sms. API Server Error", status.code(), status);
	}
}
