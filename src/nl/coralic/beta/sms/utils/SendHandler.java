/**
 * 
 */
package nl.coralic.beta.sms.utils;

import java.net.URLEncoder;

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
	private String text = "";
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
		this.text = URLEncoder.encode(text);
		if (url != null && !url.equalsIgnoreCase(""))
		{
			this.url = url;
		}

	}

	public Response send(Context context)
	{
		
		String uri = "https://" + url + "/myaccount/sendsms.php?username=" + username + "&password=" + password + "&to=" + to + "&text=" + text
				+ "&from=" + from;
		HttpHandler hh = new HttpHandler();

		return new Response(hh.send(uri, context), context);
	}
	
	public FeatureRequestResponse sendFeatureRequest(Context context, String data)
	{
		String uri = "http://betasmsserver.appspot.com/addfeaturerequest?HASH=99dae5fed1878dea1ee46a1bb0f8f149&DATA="+URLEncoder.encode(data);
		HttpHandler hh = new HttpHandler();
		return new FeatureRequestResponse(hh.send(uri, context), context);
	}

}
