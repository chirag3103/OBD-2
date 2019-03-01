<%@page import="com.util.OBDServerModel"%>
<%@page import="com.util.DBUtils"%>
<%@page import="com.util.OwnerInfoModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.util.StringHelper"%>   
<%@page import="com.util.ConnectionManager"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@page import="com.util.*"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<html>
<head>
<title>Upload File</title>
<link href="<%=request.getContextPath()%>/theme/style.css" rel="stylesheet" type="text/css" media="screen" />
<script src="<%=request.getContextPath()%>/jquery.autocomplete-1.1.3/jquery-1.7.2.js"></script>

<style type="text/css">
@import "gallery.css";
</style>

</head>
<body style="">
<div id="wrapper" style="text-align: center;">
<BR>
<%@include file="./tiles/menu.jsp" %>  

<BR><BR>Upload File to Server<BR> 

	<form method="post" enctype="multipart/form-data"
		action="<%=request.getContextPath()%>/pages/tiles/uploadAction.jsp?methodId=uploadFunction"
		target="myFrame">
		Upload File<BR>
		<BR> Select File 
		<input type="file" value="" name="myProfilePicture"><BR>
		<BR>
		<BR> <input type="submit" value="Upload File" name="btn">
	</form>
	<iframe name="myFrame" width="400px" height="100px" style="border-style: none;border: none;" ></iframe>
	
	</div>

	
	


</body>



</html>
