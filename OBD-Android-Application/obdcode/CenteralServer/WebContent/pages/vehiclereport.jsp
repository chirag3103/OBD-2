<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="java.util.Iterator"%>
<%@page import="com.util.VehicleModel"%>
<%@page import="com.util.ConnectionManager"%>
<%@page import="com.util.UserAccountModel"%>
<%@page import="java.util.Date"%>
<%@page import="com.util.DatabaseUtility"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.util.StringHelper"%>
<%@page import="com.util.UserModel"%>
<%@page import="com.util.ServerConstants"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href="<%=request.getContextPath()%>/theme/style.css" rel="stylesheet" type="text/css" media="screen" />
<script src="<%=request.getContextPath()%>/jquery.autocomplete-1.1.3/jquery-1.7.2.js"></script>

<%@taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
</head>
<body>
	<div id="wrapper">

		<!-- end #header -->
		
		<div id="welcome">
			<h2 class="title">
				<a href="#">Welcome to <%=ServerConstants.APPLICATION_HEAD%></a>
			</h2>
			
	<%@include file="./tiles/menu.jsp" %>  
			<div class="entry">

				<%
					String uid = StringHelper.n2s(((UserAccountModel) session
									.getAttribute("USER_MODEL")).getUserid());
							String vehicleno = StringHelper.n2s(request
									.getParameter("vehicleno"));
							String month = StringHelper.n2s(request.getParameter("month"));
							String year = StringHelper.n2s(request.getParameter("year"));
							List list = ConnectionManager.getVehicleReport(vehicleno,month, year);
							if(list.size()>0){
								session.setAttribute("DISPLAY_VEHICLE_REPORT", list);
							} else {
								session.removeAttribute("DISPLAY_VEHICLE_REPORT");   
							}
				%>

   
			</div>
		</div>
		<div id="two-columns">

			<div id="two-columns">
				<center>
				<form name="myform" id="myform">
					<fieldset>
						<h2>Vehicles Details</h2>
						<table>
							<tr>
								<td><strong>Select :</strong></td>
								<td><select size="3" title="Select vehicle no"
									id="vehicleno" name="vehicleno">
								</select></td>
								<td><select name="month" id="month" size="3"
									title="Select month">
										<option value="" selected="selected">Select month</option>
										<option value="all" >All</option>
										<option value="1">January</option>
										<option value="2">February</option>
										<option value="3">March</option>
										<option value="4">April</option>
										<option value="5">May</option>
										<option value="6">June</option>
										<option value="7">July</option>
										<option value="8">August</option>
										<option value="9">September</option>
										<option value="10">October</option>
										<option value="11">November</option>
										<option value="12">December</option>
								</select></td>
								<td><select name="year" id="year" size="3"   
									title="Select year">
										<option value="" selected="selected">Select year</option>
										<option value="all" >All</option>
										<%
											Date date = new Date();
											for (int i = 2012; i <= (1900 + date.getYear()); i++) {
										%>
										<option value="<%=i%>"><%=i%></option>
										<%
											}
										%>
								</select></td>
								<td><button class="Button" type="button" name="next"
										onclick="document.forms[0].submit();" /> <span class="btn"
									style="height: 30px; width: 150px; font-weight: bold; font-size: 15">
										<span class="t">Get Vehicle Details</span> <span class="r"><span></span></span>
										<span class="l"></span>
								</span>
									</button></td>
							</tr>

						</table>
					</fieldset>
				</form>
				<br></br>
				<fieldset>
					<span> <%
 	if (list.size() > 0) {
 		if (vehicleno.equalsIgnoreCase("all")) {
			vehicleno = "%";
		}
  		List vehicleList = DatabaseUtility.getBeanList(
  				com.util.VehicleModel.class,
  				ServerConstants.QUERY_VEHICLES_DETAILS,
  				new Object[]{vehicleno});
  		
  		for (Iterator iterator = vehicleList.iterator(); iterator.hasNext();) {
  			VehicleModel vm =  (VehicleModel) iterator.next();
 %>
						<table>
							<tr>
								<td><h4>Driver Name</h4></td>
								<td><input type="text" readonly="readonly" name="dname"
									id="dname" value="<%=vm.getDrivername()%>" minlength="4"></input></td>
								<td><h4>Vehicle No</h4></td>
								<td><input type="text" readonly="readonly" name="vnumber"
									id="vnumber" value="<%=vm.getVehicleno()%>" minlength="4"></input></td>
							</tr>
						</table>
						<%} %>
				</fieldset>
				<fieldset>
					<div style="width: 98%; text-align: left;">
						<display:table name="sessionScope.DISPLAY_VEHICLE_REPORT"
							class="hovertable" requestURI="/pages/vehiclereport.jsp"
							id="secondRuleId" pagesize="10" style=" width:100%;"
							defaultsort="1" defaultorder="ascending" export="true"  
							sort="list">
							<display:setProperty name="export.csv.filename"
								value="Vehicle Report.csv"></display:setProperty>
							<display:setProperty name="export.xml.filename"
								value="Vehicle Report.xml"></display:setProperty>
							<display:setProperty name="export.csv.filename"
								value="Vehicle Report.xls"></display:setProperty>
							<!--  load_pct, temp, rpm, iat, maf, throttlepos, time -->
							<display:column title="PCT Load" sortable="true"
								property="load_pct"></display:column>
								<display:column title="Speed" sortable="true" property="vss"></display:column>
									<display:column title="RPM" sortable="true" property="rpm"></display:column>
							
							<display:column title="IAT" sortable="true" property="iat"></display:column>
							<display:column title="MAF" sortable="true" property="maf"></display:column>
							<display:column title="Throttle Pos" sortable="true"
								property="throttlepos"></display:column>
							<display:column title="Temp" sortable="true" property="temp"></display:column>
							<display:column title="Time" sortable="true" property="time"></display:column>
						</display:table>
					</div>
					<%
						}
					%>
					</span>
				</fieldset>
				</center>
				<%-- <div id="col1" style="text-align: center;">
		
		</div>
		<div id="col2"><img src="<%=request.getContextPath()%>/theme/imgs/logi1n.jpg" alt="" width="320" height="260" class="image-style" /> </div> --%>
			</div>
			<!-- end #page -->
		</div>
		<script>


$(document).ready(function(){
	$("#vehicleno").load("<%=request.getContextPath()%>/pages/tiles/save.jsp?method=getVehicles&userid="+<%=uid%>); 
});
$("#vehicleno").val('<%=vehicleno%>');
$("#month").val('<%=month%>');	
$("#year").val('<%=year%>');
		</script>
</body>
</html>
