
<%@page import="com.util.LineChart2D"%>
<%@page import="org.jfree.chart.plot.PlotOrientation"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@page import="org.jfree.data.xy.XYSeries"%>
<%@page import="org.jfree.data.xy.XYSeriesCollection"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="org.jfree.data.xy.DefaultXYDataset"%>
<%@page import="org.jfree.data.xy.XYDataset"%>
<%@page import="org.jfree.chart.plot.XYPlot"%>
<%@page import="org.jfree.chart.axis.DateAxis"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.File"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.chart.ChartFactory"%>
<%@page import="org.jfree.data.time.TimeSeriesCollection"%>
<%@page import="org.jfree.data.time.Day"%>
<%@page import="org.jfree.data.time.TimeSeries"%>
<%@page import="com.util.ServerConstants"%>
<%@page import="com.util.UserAccountModel"%>
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
<%@taglib prefix="display" uri="/WEB-INF/displaytag-12.tld"%>
<html>
<head>
<title>OBD Home Page</title>
<link href="<%=request.getContextPath()%>/theme/style.css" rel="stylesheet" type="text/css" media="screen" />

</head>
<body >
<div id="wrapper" style="text-align: center;">
<BR><BR><BR>
<%@include file="./tiles/menu.jsp" %>

<BR><BR>
OBD Data As Per Owner<BR> 
<%
if(!isAdmin){
	%>
	Only Admin Can view this page.
	<%
	return;
} 
%>


<form 
		id="commentForm" method="post" name="myform" >
		<B>Driver-Vehicle No</B> 
	<select name="vehicleId" id="vehicleId"	onchange="document.forms[0].submit();">
	<%=ConnectionManager.getDBCombo("SELECT vehicleId,concat(drivername,'-',vehicleno) as valu FROM vehicles")%>
	</select>
	  
 	<%

  List list = null;
  String vehicleId=StringHelper.nullObjectToStringEmpty(request.getParameter("vehicleId"));
  String sql = "";
  
 	if(vehicleId.length()>0){
 		sql = "Select *,date_format(o.`time`, '%d-%b-%y %h:%i:%s %p') as `time`,o.time as udate from  obdserver o,vehicles v where v.vehicleId=o.vehicleId and v.vehicleId="+vehicleId+" order by o.time desc";
 		%>
 		<script>
 		document.getElementById('vehicleId').value='<%=vehicleId%>';
 		</script>  
 		<%
 	}else{
 		sql = "Select *,date_format(o.`time`, '%d-%b-%y %h:%i:%s %p') as `time`,o.time as udate from  obdserver o,vehicles v where v.vehicleId=o.vehicleId  order by o.time desc";
 	}
  
  list = DBUtils.getBeanList(OBDServerModel.class,sql);
  System.out.println("The Control is in the delete adds and list size is " + list.size());
  session.setAttribute("LIST2", list);
    %>
  <img src="<%=request.getContextPath()%>/pages/tiles/save.jsp?method=generateDriverPerformance"/>
<div style="margin: auto;width: 70%;">
<display:table  class="simple" name="sessionScope.LIST2" requestURI="/pages/driverperformance.jsp"  id="searchTableId"  pagesize="20"  style=" margin: auto;width:100%;font-size:12px;"   export= "false"  sort="list" >
    <display:setProperty name="export.csv" value="false" /> 
    <display:setProperty name="export.xml" value="false" />
    <display:setProperty name="export.xls" value="false" />
    <display:setProperty name="paging.banner.placement" value="bottom"  />
   
   	
    <display:column  title="Owner ID"  sortable="true"  property="ownerid">
    </display:column>
    
    <display:column title="IMEI">
	<%=((OBDServerModel)(pageContext.getAttribute("searchTableId"))).getImei()%>
	</display:column>
    <display:column title="Driver Name">
	<%=((OBDServerModel)(pageContext.getAttribute("searchTableId"))).getDrivername()%>
	</display:column>
	<display:column title="Vehicle No">
	<%=((OBDServerModel)(pageContext.getAttribute("searchTableId"))).getVehicleno()%>
	</display:column>
    <display:column  title="Time"  sortable="true"  property="time">
    </display:column>
   
	
	<display:column sortable="true" 	title="OBD Data Received" media="html" >
	<%=((OBDServerModel)(pageContext.getAttribute("searchTableId"))).getVss()%>
 
    </display:column>
    
  
	
	</display:table>  
</div>



	</form>



	
	
	

	

</div>


</body>



</html>
