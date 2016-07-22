package team.hellobro.gw.api.transform;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Request 
{
	private List<Message> message = new ArrayList<>(1);
	
	@XmlElement
	public void setMessage(List<Message> messages) 
	{
		this.message = messages;
	}
	
	public List<Message> getMessage() 
	{
		return message;
	}
	
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		
		for(Message m : this.message)
		{
			str.append(m.toString()).append("\n");
		}
		
		return str.toString();
	}
}
