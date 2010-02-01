/**
 * 
 */
package nl.coralic.beta.sms.utils;

import java.net.URLEncoder;

import nl.coralic.android.utils.http.HttpHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

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

	public Response send()
	{
		
		String uri = "https://" + url + "/myaccount/sendsms.php?username=" + username + "&password=" + password + "&to=" + to + "&text=" + text
				+ "&from=" + from;
		HttpHandler hh = new HttpHandler();

		return new Response(hh.send(uri));
	}

}
