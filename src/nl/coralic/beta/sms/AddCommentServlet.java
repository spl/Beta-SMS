package nl.coralic.beta.sms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import nl.coralic.beta.sms.dataobject.Features;
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
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		if (id == null || id.equalsIgnoreCase("") || com == null || com.equalsIgnoreCase(""))
		{
			out.println("<table id=\"onefeature\">");
			out.println("<tr>");
			out.println("<td>");
			out.println("No data");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			return;
		}

		// Utils utils = new Utils();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + Features.class.getName() + " WHERE key == " + id;
		List<Features> fd = (List<Features>) pm.newQuery(query).execute();
		Features f = fd.get(0);
		ArrayList<String> tmp = f.getComments();
		tmp.add(com);
		f.setComments(tmp);
		try
		{
			pm.makePersistent(f);
		}
		finally
		{
			pm.close();
		}

		resp.sendRedirect("getfeature?ID="+id);
		return;
	}
}
