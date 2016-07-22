package team.hellobro.gw.test.client;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

public class SendTest 
{
	@Test
	public void sendRequest()
	{
		try
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
			
			URL url = new URL("http://localhost:2000/hellomessage/sms");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			
			OutputStream out = con.getOutputStream();
			out.write(xml.getBytes());
			
			InputStream in = con.getInputStream();
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			
			System.out.println("Response message : " + con.getResponseMessage());
			
			byte[] buf = new byte[1024];
			int read;
			
			while((read = in.read(buf)) != -1)
			{
				result.write(buf, 0, read);
			}
			
			System.out.println(result.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			Assert.fail();
		}
	}
}
