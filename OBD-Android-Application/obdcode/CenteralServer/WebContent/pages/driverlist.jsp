<%@page import="com.util.VehicleModel"%>
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
<body style="font-size: 12px;">
<center>
<div id="wrapper" style="text-align: center;width: 80%;">

<h3>Driver List </h2>   
<%   
List vdata=ConnectionManager.getVehicleData();

	List list=ConnectionManager.getDriverList(StringHelper.n2s(request.getParameter("userid")));
	request.setAttribute("LIST", list);   
	%>    

	<display:table name="LIST" id="LIST"  class="simple"
			defaultsort="1" defaultorder="ascending" style="font-size:10px;"  pagesize="10"> 

			<display:setProperty name="export.csv" value="false" />
			<display:setProperty name="export.xml" value="false" />    
			<display:setProperty name="export.xls" value="false" />  
			<display:column property="vehicleId" title="Vehicle Id" sortable="true"
				headerClass="sortable" />
				<display:column property="drivername" title="Driver Name" sortable="true"
				headerClass="sortable" />
<display:column property="drivername" title="Driver Name" sortable="true"
				headerClass="sortable" />
			<display:column property="vehicleno" title="Vehicle No" sortable="true" />
			<display:column property="imei" title="IMEI"
				sortable="true" />
				<display:column title="Last Update"
				sortable="true" >
				<%=ConnectionManager.getVehicleTime(vdata,((VehicleModel)(pageContext.getAttribute("LIST"))).getVehicleId())%>
   
				</display:column>
				
			<display:column title="Current 	Address"> 
<%=ConnectionManager.getVehicleData(vdata,((VehicleModel)(pageContext.getAttribute("LIST"))).getVehicleId())%>
   
			</display:column>
			
			<display:column title="Delete">
			<a href="#" onclick="fnDelete('<%=((VehicleModel)(pageContext.getAttribute("LIST"))).getVehicleId()%>');">Delete</a>
			</display:column>
			
		</display:table>

</div>
</center>
<script>
function fnDelete(vehicleId){
	dataString='vehicleId='+vehicleId;   
	if(confirm("Are you sure ?")){
		$.ajax({
			  type: "POST",
			  url: "<%=request.getContextPath()%>/pages/tiles/save.jsp?method=deleteVehicle",
			  dataType: "text",
			  data:dataString
		}).done(function(msg) {  
			alert('Driver has been deleted');
		});	
		}
}
</script>

</body>
</html>
