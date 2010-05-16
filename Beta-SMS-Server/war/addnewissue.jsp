<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Beta-SMS Issue tracker</title>
</head>
<body bgcolor="#f5f3e5">
<a href="/">Index</a><br/><br/>
Add a new Issue/Request new feature.<p></p>
<form action="/addissue" method="post">
<input type="hidden" name="HASH" value="99dae5fed1878dea1ee46a1bb0f8f149"/>
<input type="hidden" name="WEB" value="true"/>
<table>
<tr>
<td>
Email (Optional): </td>
<td><input name="MAIL" type="text"/></td>
</tr>
<tr>
<td>
Type (Mandatory): </td>
<td><select name="TYPE">
  <option>Bug</option>
  <option>Feature</option>
  <option>Other</option>
</select></td>
</tr>
<tr>
<td>
Text (Mandatory): </td>
<td><TEXTAREA name="DATA" COLS="40" ROWS="6"></TEXTAREA></td>
</tr>
<tr>
<td></td>
<td><input type="submit" value="Submit" /></td>
</tr>
</table>
</form>
</body>
</html>