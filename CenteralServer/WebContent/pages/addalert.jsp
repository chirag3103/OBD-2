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

<html>
<head>
<title>OBD Add Alert</title>
<link href="<%=request.getContextPath()%>/theme/style.css" rel="stylesheet" type="text/css" media="screen" />
<link href="<%=request.getContextPath()%>/theme/looks.css" rel="stylesheet" type="text/css" media="screen" />
<script src="<%=request.getContextPath()%>/jquery.autocomplete-1.1.3/jquery-1.7.2.js"></script>


</head>
<body>
<div id="wrapper" style="text-align: center;">
<BR><BR><BR>
<%@include file="./tiles/menu.jsp" %>  
<BR><BR>
<BR><BR>Add Alerts<BR> 

<form id="commentForm" method="post" name="myform">

	
	<fieldset>
			<%
			
			List list=null;
			if(isAdmin)
				list=ConnectionManager.getAllOwners();
			else
				list=ConnectionManager.getAllOwners(um.getUserid());
			
			session.setAttribute("RESULT", list);
			%>

	<%@page import="com.util.*"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<div style="margin: auto;width:80%; background-position: center;background-repeat: no-repeat;background-image: url('<%=request.getContextPath()%>/theme/images/alert.png'); ">
<display:table  class="simple" name="sessionScope.RESULT" requestURI="/pages/addalert.jsp"  id="searchTableId"  pagesize="20"     defaultsort="1"  defaultorder="ascending"  export= "false"  sort="list"  style="font-size:12px;">
    <display:setProperty name="export.csv" value="false" /> 
    <display:setProperty name="export.xml" value="false" />
    <display:setProperty name="export.xls" value="false" />
    <display:setProperty name="paging.banner.placement" value="bottom"  />
    	<display:column title="Sr.No" sortable="true"><%=pageContext.getAttribute("searchTableId_rowNum")%></display:column>
   	<display:column  title="Owner ID"  sortable="true"  property="ownerId">
    </display:column>
     	<display:column  title="Owner Name"  sortable="true" property="fullname">   
   	</display:column>
   	<display:column  title="Owner Phone No"  sortable="true"  property="phoneno">	
	</display:column>
	
   		<display:column  title="Vehicle No."  sortable="true"  property="vehicleno">	
	</display:column>
     	<display:column  title="Driver Name"  sortable="true"  property="drivername">
    </display:column>
   	<display:column  title="Driver IMEI"  sortable="true"  property="imei">	
	</display:column>
   
	
	<display:column sortable="true" 	title="Add Alert" media="html" >
   	<a href="#" onclick="fnAddAlertDetails('<%=((OwnerInfoModel)(pageContext.getAttribute("searchTableId"))).getUserid()%>','<%=StringHelper.nullObjectToStringEmpty(((OwnerInfoModel)(pageContext.getAttribute("searchTableId"))).getImei())%>','<%=StringHelper.nullObjectToStringEmpty(((OwnerInfoModel)(pageContext.getAttribute("searchTableId"))).getVehicleno())%>','<%=((OwnerInfoModel)(pageContext.getAttribute("searchTableId"))).getVehicleId()%>')">Add Alert</a>
    </display:column>
    <display:column sortable="true" 	title="Add Alert" media="html" >
   	<a href="#" onclick="fnViewAlertDetails('<%=((OwnerInfoModel)(pageContext.getAttribute("searchTableId"))).getVehicleId()%>')">View Alert</a>
    </display:column>
	<display:column sortable="true" 	title="Delete " media="html" >
   	<a href="#" onclick="fnDeleteDetails('<%=((OwnerInfoModel)(pageContext.getAttribute("searchTableId"))).getUserid()%>','<%=((OwnerInfoModel)(pageContext.getAttribute("searchTableId"))).getVehicleId()%>')">Delete Owner Vehicle Details</a>
    </display:column>
	</display:table>
</div>	
	
	
	
	
	<br><br>
	<table style="width:80%;margin: auto;">
	<tr>
	<td>
	<div id="divId" style="display: none;">
	<p>Add Alert Details:</p>
				<table style="margin: auto;font-size: 12px;">
						<tr>
							<td>Owner ID</td>
							<td><input type="text" name="ownerid" readonly="readonly"
								id="ownerid"></input></td>
						</tr>
						<tr>
							<td>Vehicle ID</td>
							<td><input type="text" name="vehicelId" readonly="readonly"
								id="vehicelId"></input></td>
						</tr>
						<tr>
							<td>Vehicle Name</td>
							<td><input type="text" name="vehicleno" readonly="readonly"
								id="vehicleno"></input></td>
						</tr>
					
						<tr>
							<td>IMEI</td>
							<td><input type="text" name="imei" readonly="readonly"
								id="imei"></input></td>
						</tr>
					
						<tr>
							<td>Alert Type</td>
							<td><select id="type" name="type"><option value="S">Speed Alert</option><option value="P">Position Alert</option></select></td>
						</tr>
					<tr>
							<td>Alert Type</td>
							<td><input type="text" name="val" 
								id="val"></input></td>
						</tr>
					<tr>
					<td colspan="2"><input type="button" value="Add Alert" onclick="fnAdd();" /></td>
					</tr>
					
					</table>
				
				
	</div>
	</td>
	<td>
	<div id="divAlrtId1" style="text-align: center;width: 100%;">
		<center>
	<div id="divAlrtId" style="display: none;text-align: center;width: 100%;margin: auto;">
	</div>
	</center>
	</div>
	
	</td>
	</tr>
	</table>
	
		
	</div>
	</div>        
	<!-- end #page --> 
	<!-- end #footer -->

</div>
	</fieldset>
	</form>



	
	
	

	

</div>


<script>
function fnAddAlertDetails(id,imei,vehicleno,vehicelId){
	$("#divId").show();
	$("#ownerid").val(id);
	$("#val").val('');   
	$("#vehicleno").val(vehicleno);
	$("#imei").val(imei);
	$("#vehicelId").val(vehicelId);
	fnViewAlertDetails(vehicelId);
}


function fnViewAlertDetails(vehicleid){
	$('#divAlrtId').load('<%=request.getContextPath()%>/pages/alertdata.jsp?vehicleid='+vehicleid);
	$('#divAlrtId').show();
}
function fnDeleteDetails(ownerid, vehicleid){
	dataString='ownerid='+ownerid+"&vehicleid="+vehicleid;
	if(confirm("Are you sure ?")){
		$.ajax({
			  type: "POST",
			  url: "<%=request.getContextPath()%>/pages/tiles/save.jsp?method=deleteDriver",
			  dataType: "text",
			  data:dataString
		}).done(function(msg) {  
			alert(msg.trim());
			window.location.reload();
		});	
		}
}



function fnAdd(){
	ownerid=$("#ownerid").val();
	type=$("#type").val();
	val =$("#val").val();
	vehicelId=$("#vehicelId").val();  
	if(val.length<=0){  
		alert("Alert Type Value Empty!");
	}
	dataString='ownerid='+ownerid+"&type="+type+"&val="+val+"&vehicelId="+vehicelId;
	//alert(dataString);
	$.ajax({
		  type: "POST",
		  url: "<%=request.getContextPath()%>/pages/tiles/save.jsp?method=addAlert",
		  dataType: "text",
		  data:dataString
		}).done(function( msg ) {
			alert(msg.trim());
			window.location.reload();
	});
}


</script>

</body>



</html>
