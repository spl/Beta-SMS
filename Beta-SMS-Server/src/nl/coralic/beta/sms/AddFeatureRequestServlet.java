package nl.coralic.beta.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import nl.coralic.beta.sms.dataobject.Features;
import nl.coralic.beta.sms.db.PMF;
import nl.coralic.beta.sms.utils.Utils;

@SuppressWarnings("serial")
public class AddFeatureRequestServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req, resp);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		/*
		 * We only accept four param's: HASH = identify our application (99dae5fed1878dea1ee46a1bb0f8f149) DATA = users feature request DEBUG = logcat
		 * text MAIL = users mail
		 */

		resp.setContentType("text/plain");

		// Get param's and check if they are valid
		String HASH = req.getParameter("HASH");
		String DATA = req.getParameter("DATA");
		String DEBUG = req.getParameter("DEBUG");
		String MAIL = req.getParameter("MAIL");
		if (HASH == null || !HASH.equalsIgnoreCase("99dae5fed1878dea1ee46a1bb0f8f149") || DATA == null)
		{
			resp.getWriter().println("ERROR");
			return;
		}

		// Save data to db
		Date date = new Date();
		ArrayList<String> tmp = new ArrayList<String>();
		Features fd = new Features(DATA, date, tmp, "Unknown", DEBUG, MAIL);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			fd = pm.makePersistent(fd);
			Utils utils = new Utils();
			if (fd.getEmail() != null && !fd.getEmail().equalsIgnoreCase(""))
			{
				utils.sendMail(String.valueOf(fd.getKey().getId()), fd.getEmail());
			}
		}
		finally
		{
			pm.close();
		}
		resp.getWriter().println(fd.getKey().getId());
		return;
	}
}
