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
<title>OBD Add Owner Vehicle</title>
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

<BR><BR>Add Owner Vehicle<BR> 


<!-- 	<table style="width: 100%;text-align: left;"> -->
<!-- 		   		<tr> -->
<%-- 		   		<td colspan="2" style="text-align: center;"><img src="<%=request.getContextPath()%>/theme/images/Owner-128.png" height="128" width="128"> --%>
<!-- 		   		</tr> -->
<!-- 	</table> -->
	
			
			<%
			String query="";
			if(isAdmin)
				query = "SELECT * from useraccounts where role="+ServerConstants.ROLE_USER+" ";
			else
				query = "SELECT * from useraccounts where role="+ServerConstants.ROLE_USER+" and userid="+um.getUserid();
			System.out.println(query);
			List list = ConnectionManager.getBeanList(UserAccountModel.class, query);
			
			session.setAttribute("RESULT_USER", list);
			%>  

   
<display:table   name="sessionScope.RESULT_USER" requestURI="/pages/addownervehicle.jsp"  id="searchTableId"  pagesize="20"  style=" width:60%;margin: auto;font-size:12px;"  defaultsort="1"  defaultorder="ascending"  export= "false"  sort="list" >
    <display:setProperty name="export.csv" value="false" /> 
    <display:setProperty name="export.xml" value="false" />
    <display:setProperty name="export.xls" value="false" />
    <display:setProperty name="paging.banner.placement" value="bottom"  />
    
   	<display:column  title="User ID"  sortable="true"  property="userid">
    </display:column>
   	<display:column  title="Owner Name"  sortable="true" property="fullname">   
   	</display:column>
   	<display:column  sortable="true">   
   	<%=((UserAccountModel)(pageContext.getAttribute("searchTableId"))).getPhoneno()%>
   	<a href="#" 
   	onclick="fnUpdateDetails('<%=((UserAccountModel)(pageContext.getAttribute("searchTableId"))).getUserid()%>','<%=((UserAccountModel)(pageContext.getAttribute("searchTableId"))).getPhoneno()%>');">
   	<img width="20px" height="20px" src="<%=request.getContextPath()%>/theme/images/icoedit.png"/></a>
   	</display:column> 
   	      
	
	<display:column sortable="true" 	title="Add Car" media="html" >
   	<a href="#" onclick="fnAddDetails('<%=((UserAccountModel)(pageContext.getAttribute("searchTableId"))).getUserid()%>','<%=((UserAccountModel)(pageContext.getAttribute("searchTableId"))).getUserid()%>')">Add Owner Vehicle & Driver Details</a>
    </display:column>
	
	<display:column sortable="true" 	title="Delete " media="html" >
   	<a href="#" onclick="fnDeleteDetails('<%=((UserAccountModel)(pageContext.getAttribute("searchTableId"))).getUserid()%>')">Delete Owner</a>
    </display:column>
    
	</display:table>

	
	<br>
	<BR>
	<div id="divId" style="display: none;font-size:12px;"> 
	<table style="margin: auto;font-size:12px;">
	<tr><td colspan="2" style="text-align: center;"><h1>Add Driver Details:</h1></td>
	</tr>
	<tr>
		<td>Owner ID</td>
		<td><input type="text" name = "ownerid"  readonly="readonly" id="ownerid"></input></td>
	</tr>
	<tr>
		<td>Driver Name</td>
		<td><input type="text" name = "drivername"  id="drivername"></input></td>    
	</tr>
	<tr>
		<td>Vehicle No</td>
		<td><input type="text" name = "vno"  id="vno"></input></td>
	</tr>
		<tr>
		<td>IMEI No.(Phone)</td>
		<td><input type="text" name = "imei"  id="imei"></input></td>
	</tr>
		<tr>
		<td colspan="2" style="text-align: center;"><input type="button" value="Add Vehicle & Driver" onclick="fnAdd();"/></td>
	
	</tr>
	</table>
	
	</div>
	<BR>
	<div id="divId2" style="font-size: 12px;" >
	<img id="imgId" src="<%=request.getContextPath()%>/theme/images/loading-icon.gif" >
	</div>
	</div>
	</div>   
	<!-- end #page --> 
	<!-- end #footer -->

</div>


</div>

<script>
$('#divId2').html('<img id="imgId" src="<%=request.getContextPath()%>/theme/images/loading-icon.gif" >');

function fnAddDetails(uid,name){
	$("#divId").show();
	$("#ownerid").val(uid);
	$("#drivername").val('');
	$("#vno").val('');
	$("#imei").val('');
	$('#divId2').load('<%=request.getContextPath()%>/pages/driverlist.jsp?userid='+uid);
	$('#divId2').show();
}



function fnDeleteDetails(ownerid){
	dataString='ownerid='+ownerid;
	if(confirm("Are you sure ?")){
		$.ajax({
			  type: "POST",
			  url: "<%=request.getContextPath()%>/pages/tiles/save.jsp?method=delUser",
			  dataType: "text",
			  data:dataString
		}).done(function(msg) {  
			alert(msg.trim());
			window.location.reload();
		});	
		}
}

function fnUpdateDetails(userid,phoneno){
	newphoneno=prompt('Please enter new Phone no - Old Phone No - '+phoneno,phoneno);
	
	if(newphoneno==null)
		return;
	if(phoneno==newphoneno){
		return;
	}
	dataString='userid='+userid+"&phoneno="+newphoneno;
	if(confirm("Are you sure ?")){
		$.ajax({
			  type: "POST",
			  url: "<%=request.getContextPath()%>/pages/tiles/save.jsp?method=updateContact",
			  dataType: "text",
			  data:dataString
		}).done(function(msg) {  
			alert('Phone No has been updated');
			window.location.reload();
		});	
		}
}

function fnAdd(){
	ownerid=$("#ownerid").val();
	drivername=$("#drivername").val();
	vno=$("#vno").val();
	imei=$("#imei").val();
	
	
	if(ownerid.length<=0){
		alert("Owner ID Value Empty!");
	}else if(drivername.length<=0){
		alert("Driver Name Value Empty!");
	}else if(vno.length<=0){
		alert("Vehicle Number Value Empty!");
	}else if(imei.length<=0){
		alert("IMEI Empty!");
	}else{
		dataString='ownerid='+ownerid+'&drivername='+drivername+'&vno='+vno+'&imei='+imei;
	//	alert(dataString);
		$.ajax({
			  type: "POST",
			  url: "<%=request.getContextPath()%>/pages/tiles/save.jsp?method=addOwner",
			  dataType: "text",
			  data:dataString
			}).done(function( msg ) {
				alert(msg.trim());
				window.location.reload();
		});
	}
	
	
}


</script>

</body>



</html>
