<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <jsp:useBean id="showall" scope="page" class="nl.coralic.beta.sms.ShowAllBean" />
    <%@ page import="nl.coralic.beta.sms.utils.Utils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Beta-SMS Issue tracker</title>
</head>
<body bgcolor="#f5f3e5">
This is an list of all the issues and feature requests for Beta-SMS. If you wish to submit an issue or have a feature request please do so <a href="addnewissue.jsp">HERE</a>.
<p></p>
<table border="1px">
<tr>
<th>ID</th>
<th>DATE</th>
<th>STATUS</th>
<th>TYPE</th>
<th>SUMMERY</th>
<th>COMMENTS</th>
</tr>
<%
	for (int i = 0; i < showall.getAllIssues().size(); i++)
	{
	Utils utils = new Utils();
%>
<tr>
<td><a href="issue.jsp?ID=<%=showall.getAllIssues().get(i).getKey().getId()%>"> <%=showall.getAllIssues().get(i).getKey().getId()%></a></td>
<td><%=utils.formatDate(showall.getAllIssues().get(i).getDate())%></td>
<%
	if(showall.getAllIssues().get(i).getStatus().contains("http://"))
		{
%>
				<td><a href="<%=showall.getAllIssues().get(i).getStatus()%>">DUPLICATE</a></td>
				<%
					}
						else
						{
				%>
				<td><%=showall.getAllIssues().get(i).getStatus()%></td>
				<%
					}
				%>
		<td><%=showall.getAllIssues().get(i).getType()%></td>		
				
				
<%
											if(showall.getAllIssues().get(i).getContent().length() < 41)
												{
										%>
				<td><%=showall.getAllIssues().get(i).getContent()%></td>
				<%
					}
						else
						{
				%>
				<td><%=showall.getAllIssues().get(i).getStatus().substring(0, 40)%></td>
				<%
					}
				%>
				<td><%=showall.getAllIssues().get(i).getComments().size()%></td>
</tr>
<%
	}
	showall.closeConnection();
%>
</table>
</body>
</html>