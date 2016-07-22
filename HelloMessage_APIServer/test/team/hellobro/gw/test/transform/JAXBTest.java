package team.hellobro.gw.test.transform;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

public class JAXBTest 
{
	@Test
	public void xmlToObject()
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(Request.class);
			
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			Request request = new Request();
			List<Message> list = new LinkedList<>();
			list.add(new Message());
			request.setMessage(list);
			
			marshaller.marshal(request, System.out);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			Assert.fail();
		}
	}
	
	@Test
	public void objectToXml()
	{
		String xml = "<request>"
					+ "		<message>"
					+ "			<to>821029652189</to>"
					+ "			<messageId>0500000040E10D9C</messageId>"
					+ "			<status>0</status>"
					+ "			<remainingBalance>251.34424000</remainingBalance>"
					+ "			<messagePrice>0.04000000</messagePrice>"
					+ "			<network>45002</network>"
					+ "		</message>"
					+ "</request>";
		
		try
		{
			JAXBContext jc = JAXBContext.newInstance(Request.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			
			Request request = (Request)unmarshaller.unmarshal(new StringReader(xml));
			
			System.out.println(request.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			Assert.fail();
		}
	}
}
