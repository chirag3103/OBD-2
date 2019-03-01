<%@page import="java.nio.charset.Charset"%>
<%@page import="com.util.UserAccountModel"%>
<%@page import="com.util.StringHelper"%>
<%@page import="com.util.ConnectionManager"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="display" uri="/WEB-INF/displaytag-12.tld"%>
<html>
<head>
<title>OBD Home Page</title>
<link href="<%=request.getContextPath()%>/theme/style.css" rel="stylesheet" type="text/css" media="screen" />

<style type="text/css">
@import "gallery.css";
</style>
</head>
<body>
<div id="wrapper" style="text-align: center;">
<BR><BR><BR>
<h2>OBD Admin Login Portal </h2>   
<BR><BR><BR>


<%
if(request.getParameter("submit")!=null){
	String username=StringHelper.n2s(request.getParameter("username"));
	String pwd=StringHelper.n2s(request.getParameter("pwd"));
	List list=ConnectionManager.validateUser(username, pwd);
	if(list.size()>0){
		session.setAttribute("USER_MODEL", (UserAccountModel)list.get(0));
		%>
		<script>
			window.location.href='<%=request.getContextPath()%>/pages/vehicle.jsp';
		</script>
		<%  
	}else{		
		out.println("<h1>Invalid Credentials</h1>");
	}
	
}

%>
<form action=""	 method="post">
		<input name="methodId" value="loginUser" type="hidden">
		<table style="width: 60%;">
		<tr>
		<td>
<img src="<%=request.getContextPath()%>/theme/images/large gears.gif"/></td>
		<td>
		<table align="center" class="SIMPLE"  style="padding: 10px;font-size: 18px;" >


  <tr   >
 
    <td  colspan="2" style="text-align: center;"><h1>Login</h1> </td>
 </tr>
  <tr class="even">
    <td>UserName </td>
    <td><input type="text" name="username"  required="required" value="admin123" /></td>  
  </tr>
  <tr class="even"> 
    <td>Password</td>
    <td ><input type="Password" name="pwd"  required="required" value="admin123"/></td>
  </tr>
  <tr  class="even">
    <td> <input type="submit" name="submit" value="Login">&nbsp;</td>
    <td>    <input type="button" onclick="window.location.href='<%=request.getContextPath()%>/pages/addOwner.jsp';" name="submit2" value="Register">&nbsp;</td>
  </tr>
  <tr class="even">
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>

</table>
		
		</td>
		</tr>
		
		</table>

</form>
</div>


</body>
</html>
