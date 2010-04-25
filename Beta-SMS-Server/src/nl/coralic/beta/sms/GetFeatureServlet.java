package nl.coralic.beta.sms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import nl.coralic.beta.sms.dataobject.Features;
import nl.coralic.beta.sms.db.PMF;
import nl.coralic.beta.sms.utils.Utils;

@SuppressWarnings("serial")
public class GetFeatureServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req, resp);
	}

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{

		String id = req.getParameter("ID");
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		if (id == null || id.equalsIgnoreCase(""))
		{
			out.println("<a href=\"showall\">Index</a>");
			out.println("<table id=\"onefeature\"  bgcolor=\"#f5f3e5\">");
			out.println("<tr>");
			out.println("<td>");
			out.println("No data");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<a href=\"showall\">Index</a>");
		}
		else
		{
			Utils utils = new Utils();
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "select from " + Features.class.getName() + " WHERE key == " + id;
			List<Features> fd = (List<Features>) pm.newQuery(query).execute();

			Features f = fd.get(0);
			out.println("<a href=\"showall\">Index</a>");
			out.println("<table id=\"onefeature\"  bgcolor=\"#f5f3e5\">");
			out.println("<tr>");
			out.println("<td>");
			out.println("<h3>ID: " + f.getKey().getId() + "</h3><br/>");
			out.println("Date: " + utils.formatDate(f.getDate()) + "<br/>");
			out.println("Status: ");
			if(f.getStatus().contains("http://"))
			{
				out.println("<a href=\""+f.getStatus()+"\">" + f.getStatus() + "</a>");
			}
			else
			{
				out.println(f.getStatus());
			}
			out.println("<br/>");
			out.println("Content: <br/>" + f.getContent() + "<br/><br/><br/>");
			for (int i = 0; i < f.getComments().size(); i++)
			{
				out.println("Comment: " + f.getComments().get(i) + "<br/>");
			}
			out.println("<br/>");
			out.println("<form name=\"addcoment\" action=\"addcomment?ID="+f.getKey().getId()+"\" method=\"post\">");
			out.println("Add comment:<br/>");
			out.println("<TEXTAREA COLS=\"40\" ROWS=\"6\" name=\"COM\" ></TEXTAREA><br/>");
			out.println("<input type=\"submit\" value=\"Submit\" />");
			out.println("</form>");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<a href=\"showall\">Index</a>");
			pm.close();
		}

		return;
	}
}
