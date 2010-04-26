package nl.coralic.beta.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Text;

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
		System.out.println("Add issue");
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
		System.out.println("Create object");
		Features fd = new Features(DATA, date, tmp, "Unknown", new Text(DEBUG), MAIL);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			System.out.println("Persist object");
			fd = pm.makePersistent(fd);
			Utils utils = new Utils();
			if (fd.getEmail() != null && !fd.getEmail().equalsIgnoreCase(""))
			{
				System.out.println("Try to send mail");
				utils.sendMail(String.valueOf(fd.getKey().getId()), fd.getEmail());
			}
		}
		finally
		{
			System.out.println("Close pm");
			pm.close();
		}
		resp.getWriter().println(fd.getKey().getId());
		return;
	}
}
