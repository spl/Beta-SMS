package nl.coralic.beta.sms;

import java.io.IOException;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class AddFeatureRequestServlet extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		/*
		 * We only accept two param's: HASH = identify our application (99dae5fed1878dea1ee46a1bb0f8f149) DATA = users feature request
		 */

		resp.setContentType("text/plain");

		// Get param's and check if they are valid
		String HASH = req.getParameter("HASH");
		String DATA = req.getParameter("DATA");
		if (HASH == null || !HASH.equalsIgnoreCase("99dae5fed1878dea1ee46a1bb0f8f149") || DATA == null)
		{
			resp.getWriter().println("ERROR");
			return;
		}

		// Save data to db
		Date date = new Date();
		FeatureData fd = new FeatureData(DATA, date);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			pm.makePersistent(fd);
		}
		finally
		{
			pm.close();
		}
		resp.getWriter().println("DONE");
		return;
	}
}
