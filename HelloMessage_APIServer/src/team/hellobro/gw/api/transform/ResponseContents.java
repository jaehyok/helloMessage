package team.hellobro.gw.api.transform;

public class ResponseContents 
{
	private String messageId;
	private String gatewayId;
	private String resultCode;
	
	public String getMessageId() 
	{
		return messageId;
	}
	
	public void setMessageId(String messageId) 
	{
		this.messageId = messageId;
	}
	
	public String getGatewayId() 
	{
		return gatewayId;
	}
	
	public void setGatewayId(String gatewayId) 
	{
		this.gatewayId = gatewayId;
	}
	
	public String getResultCode() 
	{
		return resultCode;
	}
	
	public void setResultCode(String resultCode) 
	{
		this.resultCode = resultCode;
	}
	
	@Override
	public String toString() 
	{
		return "Message ID:" + this.messageId + ", Gateway ID:" + this.gatewayId + ", Result Code:" + this.resultCode;
	}
}
