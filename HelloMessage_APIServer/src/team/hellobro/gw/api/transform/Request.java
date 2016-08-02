package team.hellobro.gw.api.transform;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Request 
{
	private List<RequestContents> message = new ArrayList<>(1);
	
	@XmlElement
	public void setMessage(List<RequestContents> messages) 
	{
		this.message = messages;
	}
	
	public List<RequestContents> getMessage() 
	{
		return message;
	}
	
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		
		boolean isFirst = true;
		for(RequestContents m : this.message)
		{
			if(isFirst)
			{
				str.append(m.toString());
				
				isFirst = false;
			}
			else
			{
				str.append("\n").append(m.toString());
			}
		}
		
		return str.toString();
	}
}
