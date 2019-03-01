<%@page import="com.util.AlertModel"%>
<%@page import="com.util.StringHelper"%>
<%@page import="com.util.ConnectionManager"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="display" uri="/WEB-INF/displaytag-12.tld"%>
<html>
<head>
<title>OBD Home Page</title>
<link href="<%=request.getContextPath()%>/theme/looks.css" rel="stylesheet" type="text/css" media="screen" />

</head>
<body >

<div id="wrapper" style="text-align: center;width: 50%;">
<h3>User Alert </h2>   
<%   
	List list=ConnectionManager.getAlertList(StringHelper.n2s(request.getParameter("vehicleid")));
	request.setAttribute("LIST", list);   
	%>

	<display:table name="LIST" id="LIST"  class="simple" style="font-size:12px;"
			defaultsort="1" defaultorder="ascending" export="true" pagesize="10"> 

			<display:setProperty name="export.pdf.filename" value="report.pdf" />

			<display:setProperty name="export.csv.filename" value="report.csv" />

			<display:setProperty name="export.excel.filename" value="report.xls" />
			<display:setProperty name="export.csv" value="false" />
			<display:setProperty name="export.xml" value="false" />  
			<display:setProperty name="export.xls" value="false" />  
			<display:column property="alertId" title="Alert Id" sortable="true"
				headerClass="sortable" />
				

			<display:column property="alerttype" title="Alert Type" sortable="true" />
			<display:column property="val" title="Value"
				sortable="true" />
			<display:column>
			<a href="#" onclick="fnDelete('<%=((AlertModel)(pageContext.getAttribute("LIST"))).getAlertId()%>');">Delete</a>
			</display:column>
			
		</display:table>

</div>
<script>
function fnDelete(alertid){
	dataString='alertid='+alertid;
	if(confirm("Are you sure ?")){
		$.ajax({
			  type: "POST",
			  url: "<%=request.getContextPath()%>/pages/tiles/save.jsp?method=deleteAlert",
			  dataType: "text",
			  data:dataString
		}).done(function(msg) {  
			alert('')
		});	
		}
}
</script>
</body>
</html>
