<%@page import="com.util.VehicleModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.util.ServerConstants"%>
<%@page import="com.util.DatabaseUtility"%>
<%@page import="com.util.StringHelper"%>
<%@page import="com.util.MasterModel"%>
<%@page import="com.util.ConnectionManager"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="<%=request.getContextPath()%>/theme/style.css" rel="stylesheet" type="text/css" media="screen" />
<script src="<%=request.getContextPath()%>/jquery.autocomplete-1.1.3/jquery-1.7.2.js"></script>
<script
    src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
</head>
<body >
<div id="wrapper" style="text-align: center;"> 

 

	<div id="welcome">
<form action="">
	<%@include file="./tiles/menu.jsp" %>   
<%
double lat=0;  
double lng=0;
int  vid=StringHelper.n2i((request.getParameter("vehicleNo"))) ;
if(vid>0){
System.out.println("vehicle id"+vid);
String query="SELECT  latsend, lngsend,  obdid FROM `obdserver` where vehicleid='"+vid+"'order by obdid asc limit 1;";
List l=ConnectionManager.getBeanList(MasterModel.class, query);
MasterModel mm=(MasterModel)l.get(0);
lat=StringHelper.n2d(mm.getLatsend());
lng=StringHelper.n2d(mm.getLngsend());
}
%>
<div class="entry">
<%%>
<%
	List vehicleList = DatabaseUtility.getBeanList(
  				com.util.VehicleModel.class,
  				ServerConstants.QUERY_VEHICLES_DETAILS2,
  				new Object[]{});
  		%>  
  		<BR><BR>
  		Select Vehicle &nbsp;&nbsp;	<select name="vehicleNo" id="vehicleNo"  onchange="document.forms[0].submit();"
									title="vehicleNo">
									<option value="0">Select</option>
  		<%
  		for (Iterator iterator = vehicleList.iterator(); iterator.hasNext();) {
  			VehicleModel vm =  (VehicleModel) iterator.next();
 %>
				
<option value="<%=vm.getVehicleId()%>"><%=vm.getDrivername()%>-<%=vm.getVehicleno() %></option>
									
						<%} %>
</select>
				<div id="map" style="height:500px; width:1000px"></div>  
<div style="padding-left: 300px;" ><input type="button" value="Display" onclick="fnview('<%=lat%>','<%=lng%>')"> </div>  

			</div>
			</form>
			</div></div>
</body>

<script>
function fnview(lats,lngs) {
	
	var lt=Number(lats);
	var ln=Number(lngs);
	  //var myLatLng = {lat: 18.5073958, lng: 73.7871018};
	  var myLatLng = {lat: lt, lng: ln};  

	  var map = new google.maps.Map(document.getElementById('map'),{
	    zoom:16,
	    center: myLatLng
	  });

	  var marker = new google.maps.Marker({
	    position: myLatLng,
	    map: map,
	    title: 'Hello World!'
	  });
	}
fnview('<%=lat%>','<%=lng%>');
document.getElementById('vehicleNo').value='<%=vid%>';
</script>
</html>