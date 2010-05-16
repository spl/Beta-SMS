package nl.coralic.beta.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.coralic.beta.sms.dataobject.Issues;
import nl.coralic.beta.sms.db.PMF;

@SuppressWarnings("serial")
public class AddCommentServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req, resp);
	}

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String id = req.getParameter("ID");
		String com = req.getParameter("COM");
		resp.setContentType("text/html");
		if (id == null || id.equalsIgnoreCase("") || com == null || com.equalsIgnoreCase(""))
		{
			resp.sendRedirect("http://betasmsserver.coralic.nl");
			return;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + Issues.class.getName() + " WHERE key == " + id;
		List<Issues> issues = (List<Issues>) pm.newQuery(query).execute();
		Issues issue = issues.get(0);
		ArrayList<String> tmp = issue.getComments();
		tmp.add(com);
		issue.setComments(tmp);
		try
		{
			pm.makePersistent(issue);
		}
		finally
		{
			pm.close();
		}

		resp.sendRedirect("issue.jsp?ID="+id);
		return;
	}
}
