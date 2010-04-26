package nl.coralic.beta.sms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import nl.coralic.beta.sms.dataobject.Features;
import nl.coralic.beta.sms.db.PMF;
import nl.coralic.beta.sms.utils.Utils;

@SuppressWarnings("serial")
public class ShowAllServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(ShowAllServlet.class.getName());
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req, resp);
	}

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/html");

		Utils utils = new Utils();

		PersistenceManager pm = PMF.get().getPersistenceManager();

		String query = "select from " + Features.class.getName();
		List<Features> fd = (List<Features>) pm.newQuery(query).execute();

		PrintWriter out = resp.getWriter();
		out.println("<table id=\"listalldata\" bgcolor=\"#f5f3e5\">");
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
			out.println("SUMMERY");
			out.println("</th>");
			out.println("<th>");
			out.println("COMMENTS");
			out.println("</th>");
			out.println("</tr>");
			for (int i = 0; i < fd.size(); i++)
			{
				out.println("<tr>");
				out.println("<td>");
				out.println("<a href=\"getfeature?ID=" + fd.get(i).getKey().getId() + "\">" + fd.get(i).getKey().getId() + "</a>");
				out.println("</td>");
				out.println("<td>");
				out.println(utils.formatDate(fd.get(i).getDate()));
				out.println("</td>");
				out.println("<td>");
				if(fd.get(i).getStatus().contains("http://"))
				{
					out.println("<a href=\""+fd.get(i).getStatus()+"\">" + fd.get(i).getStatus() + "</a>");
				}
				else
				{
					out.println(fd.get(i).getStatus());
				}
				out.println("</td>");
				out.println("<td>");
				if (fd.get(i).getContent().length() < 41)
				{
					out.println(fd.get(i).getContent());
				}
				else
				{
					out.println(fd.get(i).getContent().substring(0, 40));
				}
				out.println("</td>");
				out.println("<td>");
				out.println(fd.get(i).getComments().size());
				out.println("</td>");
				out.println("</tr>");
			}
		}
		out.println("</table>");
		pm.close();
		return;
	}
}
