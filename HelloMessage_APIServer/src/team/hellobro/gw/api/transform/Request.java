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
		
		boolean isFirst = true;
		for(Message m : this.message)
		{
			if(isFirst)
			{
				str.append(m.toString());
			}
			else
			{
				str.append("\n").append(m.toString());
			}
		}
		
		return str.toString();
	}
}
