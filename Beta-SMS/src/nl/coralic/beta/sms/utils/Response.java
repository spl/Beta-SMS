/**
 * 
 */
package nl.coralic.beta.sms.utils;

import java.io.StringBufferInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;

/**
 * @author "Armin Čoralić"
 */
public class Response
{
	private String response = "";
	private String error = "";
	private boolean succeful = false;

	public Response(String data)
	{
		// do something
		if (!data.startsWith("Error:"))
		{
			data = data.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
			Document doc = getDocument(data);

			if (doc != null)
			{
				Element root = doc.getDocumentElement();
				root.normalize();

				if (root.getElementsByTagName("resultstring").item(0).getFirstChild().getNodeValue().equalsIgnoreCase("success"))
				{
					setResponse("Sms send.");
					setSucceful(true);
				}
				else
				{
					String tmpCause = root.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();

					if (tmpCause.equalsIgnoreCase(""))
					{
						setError("Uknown cause, faild.");
					}
					else
					{
						setError(tmpCause);
					}
					setSucceful(false);
				}
			}
			else
			{
				Log.d(Const.TAG_SEND, "Error doc is null");
				setError("Error: Parsing response");
				setSucceful(false);
			}
		}
		else
		{
			setError(data);
			setSucceful(false);
		}

	}

	public String getResponse()
	{
		return response;
	}

	public void setResponse(String response)
	{
		this.response = response;
	}

	public String getError()
	{
		return error;
	}

	private void setError(String error)
	{
		this.error = error;
	}

	public boolean isSucceful()
	{
		return succeful;
	}

	private void setSucceful(boolean succeful)
	{
		this.succeful = succeful;
	}

	private Document getDocument(String data)
	{
		Document ret = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringBufferInputStream sbis = new StringBufferInputStream(data);
			ret = builder.parse(sbis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			setError("Error: Parsing response");
			setSucceful(false);
		}
		return ret;
	}

}
