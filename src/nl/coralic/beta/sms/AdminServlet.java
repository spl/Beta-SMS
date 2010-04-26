package nl.coralic.beta.sms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.coralic.beta.sms.dataobject.Features;
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
			if (doit != null && doit.equalsIgnoreCase("deletefeature"))
			{
				deletefeature(req.getParameter("ID"));
				resp.sendRedirect("admin");
			}
			if (doit != null && doit.equalsIgnoreCase("deletecomment"))
			{
				deletecomment(req.getParameter("ID"), req.getParameter("COMID"));
				resp.sendRedirect("admin");
			}
			if (doit != null && doit.equalsIgnoreCase("updatestatus"))
			{
				updatestatus(req.getParameter("ID"), req.getParameter("status"));
				resp.sendRedirect("admin");
			}
			printAll(resp.getWriter());

		}
		else
		{
			resp.getWriter().println("<p>Please <a href=\"" + userService.createLoginURL(thisURL) + "\">sign in</a>.</p>");
		}

	}

	private void printAll(PrintWriter out)
	{
		Utils utils = new Utils();

		PersistenceManager pm = PMF.get().getPersistenceManager();

		String query = "select from " + Features.class.getName();
		List<Features> fd = (List<Features>) pm.newQuery(query).execute();

		out.println("<table id=\"listalldata\">");
		if (fd.isEmpty())
		{
			out.println("<tr>");
			out.println("<td>");
			out.println("No data");
			out.println("</td>");
			out.println("</tr>");
		}
		else
		{
			out.println("<tr>");
			out.println("<th>");
			out.println("ID");
			out.println("</th>");
			out.println("<th>");
			out.println("DATE");
			out.println("</th>");
			out.println("<th>");
			out.println("STATUS");
			out.println("</th>");
			out.println("<th>");
			out.println("EMAIL");
			out.println("</th>");
			out.println("<th>");
			out.println("CONTENT");
			out.println("</th>");
			out.println("<th>");
			out.println("DEBUG");
			out.println("</th>");
			out.println("<th>");
			out.println("COMMENTS");
			out.println("</th>");
			out.println("</tr>");
			for (int i = 0; i < fd.size(); i++)
			{
				out.println("<tr>");
				out.println("<td>");
				out.println("<a href=\"admin?DOIT=deletefeature&ID=" + fd.get(i).getKey().getId() + "\">" + fd.get(i).getKey().getId() + "</a>");
				out.println("</td>");
				out.println("<td>");
				out.println(utils.formatDate(fd.get(i).getDate()));
				out.println("</td>");
				out.println("<td>");
				out.println("<form name=\"delete\" action=\"admin?DOIT=updatestatus&ID="+fd.get(i).getKey().getId()+"\" method=\"post\">");
				out.println("Add comment:<br/>");
				out.println("<input type=\"text\" name=\"status\" value="+fd.get(i).getStatus()+">");
				out.println("<input type=\"submit\" value=\"Submit\" />");
				out.println("</form>");
				out.println("</td>");
				out.println("<td>");
				out.println(fd.get(i).getEmail());
				out.println("</td>");
				out.println("<td>");
				out.println(fd.get(i).getContent());
				out.println("</td>");

				out.println("<td>");
				if(fd.get(i).getDebug() == null)
				{
				out.println("NULL");
				}
				else
				{
					out.println(fd.get(i).getDebug().getValue());
				}
				out.println("</td>");

				out.println("<td>");
				out.println("<table>");
				for (int ii = 0; ii < fd.get(i).getComments().size(); ii++)
				{
					out.println("<td>");
					out.println("<a href=\"admin?DOIT=deletecomment&ID=" + fd.get(i).getKey().getId() + "&COMID="+ii+"\">" + fd.get(i).getComments().get(ii) + "</a>");
					out.println("</td>");
				}
				out.println("</table>");

				out.println("</td>");

				out.println("</tr>");
			}
		}
		out.println("</table>");
		pm.close();
		return;
	}
	
	public void deletefeature(String id)
	{
		if (id == null || !id.equalsIgnoreCase(""))
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "select from " + Features.class.getName() + " WHERE key == " + id;
			List<Features> fd = (List<Features>) pm.newQuery(query).execute();

			pm.deletePersistent(fd.get(0));
			pm.close();
		}
	}
	public void deletecomment(String id, String comid)
	{
		if (id == null || !id.equalsIgnoreCase("") || comid == null || !comid.equalsIgnoreCase(""))
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "select from " + Features.class.getName() + " WHERE key == " + id;
			List<Features> fd = (List<Features>) pm.newQuery(query).execute();

			Features f = fd.get(0);
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
			String query = "select from " + Features.class.getName() + " WHERE key == " + id;
			List<Features> fd = (List<Features>) pm.newQuery(query).execute();
			Features f = fd.get(0);
			f.setStatus(status);
			pm.makePersistent(f);
			pm.close();
		}
	}
}
