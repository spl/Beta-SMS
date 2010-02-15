package nl.coralic.beta.sms;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ShowDataServlet extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + FeatureData.class.getName();
		List<FeatureData> fd = (List<FeatureData>) pm.newQuery(query).execute();
		if (fd.isEmpty())
		{
			resp.getWriter().println("No messages");
		}
		else
		{
			resp.getWriter().println("Messages ["+fd.size()+"]:");
			resp.getWriter().println("");
			for(int i=0;i<fd.size();i++)
			{
				resp.getWriter().println("Date added: " + fd.get(i).getDate());
				resp.getWriter().println("Content: " + fd.get(i).getContent());
				resp.getWriter().println("");
			}
		}

		return;
	}
}
