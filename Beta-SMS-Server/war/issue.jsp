<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <jsp:useBean id="issue" scope="page" class="nl.coralic.beta.sms.ShowIssueBean" />
        <%@ page import="nl.coralic.beta.sms.utils.Utils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Beta-SMS Issue tracker</title>
</head>
<body bgcolor="#f5f3e5">
<%
String ID = request.getParameter("ID");
Utils utils = new Utils();
if(ID == null && ID.equalsIgnoreCase(""))
{
response.sendRedirect("http://betasmsserver.coralic.nl");
return;
}
issue.findIssue(ID);
 %>
<a href="/">Index</a><br/><br/>
This is the issue: <%=issue.getIssues().getKey().getId()%>
<p></p>
Date: <%=utils.formatDate(issue.getIssues().getDate())%><br/>
STATUS:<%
	if(issue.getIssues().getStatus().contains("http://"))
		{
%>
				<a href="<%=issue.getIssues().getStatus()%>">DUPLICATE</a>
				<%
					}
						else
						{
				%>
				<%=issue.getIssues().getStatus()%>
				<%
					}
				%><br/>
TYPE: <%=issue.getIssues().getType()%><br/>
SUMMERY: <br/>
<%=issue.getIssues().getContent()%><br/><br/><br/>
COMMENTS: <br/><br/>
<%
	for (int i = 0; i < issue.getIssues().getComments().size(); i++)
	{
%>
			<table><tr><td>
				<%=issue.getIssues().getComments().get(i)%>
				</td></tr></table>
			<%
				}
			%>
<br/><br/>
<form action="/addcomment" method="post">
<input type="hidden" name="ID" value="<%=issue.getIssues().getKey().getId()%>"/>
<table>
<tr>
<td>
Text (Mandatory): </td>
<td><TEXTAREA name="COM" COLS="40" ROWS="6"></TEXTAREA></td>
</tr>
<tr>
<td></td>
<td><input type="submit" value="Submit" /></td>
</tr>
</table>
</form>
<%
issue.closeConnection();
 %>
</body>
</html>