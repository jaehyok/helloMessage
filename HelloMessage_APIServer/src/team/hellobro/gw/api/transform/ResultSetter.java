package team.hellobro.gw.api.transform;

import io.netty.handler.codec.http.HttpResponseStatus;

public interface ResultSetter 
{
	static void SUCCESS(Response _response)
	{
		_response.setResult("success", "100", HttpResponseStatus.OK);
	}
	
	static void FAIL(Response _response)
	{
		_response.setResult("fail", "900", HttpResponseStatus.BAD_REQUEST);
	}
}
