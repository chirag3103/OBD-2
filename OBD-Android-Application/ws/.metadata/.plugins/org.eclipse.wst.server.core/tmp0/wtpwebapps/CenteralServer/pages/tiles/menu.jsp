<%@page import="com.util.ServerConstants"%>
<%@page import="com.util.UserAccountModel"%>
<%
Object o=session.getAttribute("USER_MODEL");
UserAccountModel um=null;
if(o==null){
	%>
	<script>
	window.location.href="<%=request.getContextPath()%>/pages/login.jsp";
	</script>
	<%
}else{
	um=(UserAccountModel)o;
}
boolean isAdmin=false;
if(um.getRole()== ServerConstants.ROLE_ADMIN){ 
	isAdmin=true;
}

%>
<h2>Welcome <%=um.getFullname() %></h2>   
<BR>
<%-- <a href="<%=request.getContextPath()%>/pages/vehicle.jsp?Fetch=1">Vehicle Report</a>&nbsp;&nbsp;&nbsp; --%>
<%if(isAdmin){ %>
<a href="<%=request.getContextPath()%>/pages/addownervehicle.jsp">Add/Delete Owner</a>&nbsp;&nbsp;&nbsp;
<%}else{ %>
<a href="<%=request.getContextPath()%>/pages/addownervehicle.jsp">Edit Details</a>&nbsp;&nbsp;&nbsp;
<%} %>
<a href="<%=request.getContextPath()%>/pages/addalert.jsp">Add/Delete Alerts</a>&nbsp;&nbsp;&nbsp;
<a href="<%=request.getContextPath()%>/pages/vehicle.jsp">Live Data</a>&nbsp;&nbsp;&nbsp;
<a href="<%=request.getContextPath()%>/pages/driverperformance.jsp">Driver Performance</a>&nbsp;&nbsp;&nbsp;
<a href="<%=request.getContextPath()%>/pages/vehiclereport.jsp"><span><span>Vehicle Report</span></span></a>&nbsp;&nbsp;&nbsp;
<a href="<%=request.getContextPath()%>/pages/driverreport.jsp" ><span><span>Driver Report</span></span></a>&nbsp;&nbsp;&nbsp;
<a href="<%=request.getContextPath()%>/pages/driver_behaviour_report.jsp" ><span><span>Driver Behaviour Report</span></span></a>&nbsp;&nbsp;&nbsp;
<a href="<%=request.getContextPath()%>/pages/upload.jsp" ><span><span>Upload File</span></span></a>&nbsp;&nbsp;&nbsp;
<a href="<%=request.getContextPath()%>/pages/map.jsp?sfacility=12" ><span><span>View Map</span></span></a>&nbsp;&nbsp;&nbsp;
<!-- style="background-color: yellow;" -->
<a href="<%=request.getContextPath()%>/pages/login.jsp">Logout</a>