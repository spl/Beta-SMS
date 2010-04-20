/**
 * 
 */
package nl.coralic.beta.sms.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import nl.coralic.beta.sms.utils.http.HttpHandler;

/**
 * @author "Armin Čoralić"
 */
public class SendHandler
{

	private String password = "";
	private String username = "";
	private String from = "";
	private String to = "";
	private ArrayList<String> text;
	private String url = "www.webcalldirect.com";

	public SendHandler()
	{

	}

	/**
	 * 
	 */
	public SendHandler(String password, String username, String from, String to, String text, String url)
	{
		this.password = URLEncoder.encode(password);
		this.username = URLEncoder.encode(username);
		this.from = URLEncoder.encode(from);
		this.to = URLEncoder.encode(to);
		this.text = Utils.splitSmsTextTo160Chars(URLEncoder.encode(text));
		if (url != null && !url.equalsIgnoreCase(""))
		{
			this.url = url;
		}

	}

	public Response send(Context context)
	{
		Response response = null;
		if (text.size() > 1)
		{
			Iterator<String> i = text.iterator();
			while (i.hasNext())
			{
				String uri = "https://" + url + "/myaccount/sendsms.php?username=" + username + "&password=" + password + "&to=" + to + "&text="
						+ i.next() + "&from=" + from;
				HttpHandler hh = new HttpHandler();
				response = new Response(hh.send(uri, context), context); 
				//if there is an error don't send others
				if(!response.isSucceful())
				{
					return response;
				}
			}
			return response;
		}
		else
		{
			String uri = "https://" + url + "/myaccount/sendsms.php?username=" + username + "&password=" + password + "&to=" + to + "&text="
					+ text.get(0) + "&from=" + from;
			HttpHandler hh = new HttpHandler();
			return new Response(hh.send(uri, context), context);
		}
	}

	public FeatureRequestResponse sendFeatureRequest(Context context, String data)
	{
		String uri = "http://betasmsserver.appspot.com/addfeaturerequest?HASH=99dae5fed1878dea1ee46a1bb0f8f149&DATA=" + URLEncoder.encode(data);
		HttpHandler hh = new HttpHandler();
		return new FeatureRequestResponse(hh.send(uri, context), context);
	}
	
	public String getBalance(String url, String user, String pass)
	{
		String returnValue = "*";
		
		List<NameValuePair> postdata = new ArrayList<NameValuePair>();
		postdata.add(new BasicNameValuePair("user", user));
		postdata.add(new BasicNameValuePair("pass", pass));
		
		HttpHandler hh = new HttpHandler();
		HttpClient httpclient = hh.getHttpClient();
		if(hh.doPost(httpclient,"https://"+url+"/myaccount/index.php?part=tplogin",postdata) != null)
		{
			if(hh.doGet(httpclient, "https://"+url+"/myaccount/index.php?part=menu&justloggedin=true") != null)
			{
				String data = hh.doGet(httpclient, "https://"+url+"/myaccount/contacts.php");
				if(data != null)
				{
					returnValue = Utils.getBalance(data);
				}
			}
		}
		httpclient.getConnectionManager().shutdown();	
		return returnValue;
	}
}
