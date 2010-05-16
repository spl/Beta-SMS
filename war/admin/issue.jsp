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
<a href="/admin">Index</a><br/><br/>
This is the issue: <%=issue.getIssues().getKey().getId()%> <a href="/admin?DOIT=deleteissue&ID=<%=issue.getIssues().getKey().getId()%>">DELETE</a>
<p></p>
Date: <%=utils.formatDate(issue.getIssues().getDate())%><br/>
STATUS:
<form action="/admin" method="post">
<input type="hidden" name="DOIT" value="updatestatus"/>
<input type="hidden" name="ID" value="<%=issue.getIssues().getKey().getId()%>"/>
<input name="STATUS" type="text" value="<%=issue.getIssues().getStatus()%>"/>
<input type="submit" value="Submit" />
</form>
TYPE: (use: Bug / Feature / Other )
<form action="/admin" method="post">
<input type="hidden" name="DOIT" value="updatetype"/>
<input type="hidden" name="ID" value="<%=issue.getIssues().getKey().getId()%>"/>
<input name="TYPE" type="text" value="<%=issue.getIssues().getType()%>"/>
<input type="submit" value="Submit" />
</form>
 <br/>
SUMMERY: <br/>
<%=issue.getIssues().getContent()%><br/><br/><br/>
COMMENTS: <br/><br/>
<%
	for (int i = 0; i < issue.getIssues().getComments().size(); i++)
	{
%>
			<table><tr><td><a href="/admin?DOIT=deletecomment&ID=<%=issue.getIssues().getKey().getId()%>&COMID=<%=i%>">DELETE</a>
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