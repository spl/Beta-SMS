package nl.coralic.beta.sms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.coralic.beta.sms.dataobject.Issues;
import nl.coralic.beta.sms.db.PMF;
import nl.coralic.beta.sms.utils.Utils;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AdminServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req, resp);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{

		UserService userService = UserServiceFactory.getUserService();

		String thisURL = req.getRequestURI();
		if (req.getUserPrincipal() != null)
		{
			resp.getWriter().println(
					"<p>Hello, " + req.getUserPrincipal().getName() + "!  You can <a href=\"" + userService.createLogoutURL(thisURL)
							+ "\">sign out</a>.</p>");

			String doit = req.getParameter("DOIT");
			if (doit != null && doit.equalsIgnoreCase("deleteissue"))
			{
				deleteissue(req.getParameter("ID"));
				resp.sendRedirect("/admin/index.jsp");
			}
			if (doit != null && doit.equalsIgnoreCase("deletecomment"))
			{
				deletecomment(req.getParameter("ID"), req.getParameter("COMID"));
				resp.sendRedirect("/admin/issue.jsp?ID="+req.getParameter("ID"));
			}
			if (doit != null && doit.equalsIgnoreCase("updatestatus"))
			{
				updatestatus(req.getParameter("ID"), req.getParameter("STATUS"));
				resp.sendRedirect("/admin/issue.jsp?ID="+req.getParameter("ID"));
			}
			if (doit != null && doit.equalsIgnoreCase("updatetype"))
			{
				updatetype(req.getParameter("ID"), req.getParameter("TYPE"));
				resp.sendRedirect("/admin/issue.jsp?ID="+req.getParameter("ID"));
			}
			resp.sendRedirect("/admin/index.jsp");

		}
		else
		{
			resp.getWriter().println("<p>Please <a href=\"" + userService.createLoginURL(thisURL) + "\">sign in</a>.</p>");
		}

	}
	
	@SuppressWarnings("unchecked")
	public void deleteissue(String id)
	{
		if (id == null || !id.equalsIgnoreCase(""))
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "select from " + Issues.class.getName() + " WHERE key == " + id;
			List<Issues> fd = (List<Issues>) pm.newQuery(query).execute();
			pm.deletePersistent(fd.get(0));
			pm.close();
		}
	}
	public void deletecomment(String id, String comid)
	{
		if (id == null || !id.equalsIgnoreCase("") || comid == null || !comid.equalsIgnoreCase(""))
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "select from " + Issues.class.getName() + " WHERE key == " + id;
			List<Issues> fd = (List<Issues>) pm.newQuery(query).execute();

			Issues f = fd.get(0);
			ArrayList<String> tmp = f.getComments();
			tmp.remove(Integer.valueOf(comid).intValue());
			f.setComments(tmp);
			pm.makePersistent(f);
			pm.close();
		}
	}
	public void updatestatus(String id,String status)
	{
		if (id == null || !id.equalsIgnoreCase("") || status == null || !status.equalsIgnoreCase(""))
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "select from " + Issues.class.getName() + " WHERE key == " + id;
			List<Issues> fd = (List<Issues>) pm.newQuery(query).execute();
			Issues f = fd.get(0);
			f.setStatus(status);
			pm.makePersistent(f);
			pm.close();
		}
	}
	public void updatetype(String id,String type)
	{
		if (id == null || !id.equalsIgnoreCase("") || type == null || !type.equalsIgnoreCase(""))
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "select from " + Issues.class.getName() + " WHERE key == " + id;
			List<Issues> fd = (List<Issues>) pm.newQuery(query).execute();
			Issues f = fd.get(0);
			f.setType(type);
			pm.makePersistent(f);
			pm.close();
		}
	}
}
