package team.hellobro.gw.api.transform;

import javax.xml.bind.annotation.XmlElement;

public class RequestContents 
{
	private String messageId = "00001";
	private String to = "kwonsm";
	private String status = "0";
	private String remainingBalance = "0101";
	private String messagePrice = "0.002";
	private String network = "br1";
	
	public String getMessageId() 
	{
		return messageId;
	}
	
	@XmlElement
	public void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}
	
	public String getTo() 
	{
		return to;
	}
	
	@XmlElement
	public void setTo(String to) 
	{
		this.to = to;
	}
	
	public String getStatus() 
	{
		return status;
	}
	
	@XmlElement
	public void setStatus(String status) 
	{
		this.status = status;
	}
	
	public String getRemainingBalance() 
	{
		return remainingBalance;
	}
	
	@XmlElement
	public void setRemainingBalance(String remainingBalance) 
	{
		this.remainingBalance = remainingBalance;
	}
	
	public String getMessagePrice() 
	{
		return messagePrice;
	}
	
	@XmlElement
	public void setMessagePrice(String messagePrice) 
	{
		this.messagePrice = messagePrice;
	}
	
	public String getNetwork()
	{
		return network;
	}
	
	@XmlElement
	public void setNetwork(String network) 
	{
		this.network = network;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder str = new StringBuilder();
		str.append("message id:").append(this.messageId);
		str.append(", to:").append(this.to);
		str.append(", message price:").append(this.messagePrice);
		str.append(", network:").append(this.network);
		str.append(", remaining balance:").append(this.remainingBalance);
		str.append(", status:").append(this.status);
		
		return str.toString(); 
	}
}
