
<%@page import="java.util.ArrayList"%>
<%@page import="com.util.VehicleModel"%>
<%@page import="com.util.ServerConstants"%>
<%@page import="com.util.OBDModel"%>
<%@page import="com.util.DatabaseUtility"%>
<%@page import="com.util.StringHelper"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/theme/tablecss.js"></script>

<style>
#popup_box {
	display: none; /* Hide the DIV */
	position: fixed;
	_position: absolute; /* hack for internet explorer 6 */
	height: 330px;
	width: 700px;
	background: #FFFFFF;
	left: 300px;
	top: 200px;
	z-index: 100;
	/* Layering ( on-top of others), if you have lots of layers: I just maximized, you can change it yourself */
	margin-left: 15px;
	/* additional features, can be omitted */
	border: 2px solid #0000ff;
	padding: 15px;
	font-size: 12px;
	-moz-box-shadow: 0 0 5px #0000ff;
	-webkit-box-shadow: 0 0 5px #0000ff;
	box-shadow: 0 0 5px #0000ff;
}

/* This is for the positioning of the Close Link */
#popupBoxClose {
	font-size: 12px;
	line-height: 15px;
	right: 5px;
	top: 5px;
	position: absolute;
	color: #6fa5e2;
	font-weight: 500;
}

table.gridtable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
	width: 100%;
}

table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}

table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}

.styleTable {
	border-collapse: separate;
	width: 100%;
}

.styleTable TD {
	font-weight: normal !important;
	padding: .4em;
	border-top-width: 0px !important;
}

.styleTable TH {
	text-align: center;
	padding: .8em .4em;
}

.styleTable TD.first,.styleTable TH.first {
	border-left-width: 0px !important;
}
</style>
</head>

<body>
	<div id="popup_box">
		<span id="popup_text"> </span> <a id="popupBoxClose"><img
			src="<%=request.getContextPath()%>/pages/images/Close Button.png" /></a>
	</div>
	<%
		String vehicleid = StringHelper.n2s(request.getParameter("id"));
		String month = StringHelper.n2s(request.getParameter("month"));
		String year = StringHelper.n2s(request.getParameter("year"));
		String severe = StringHelper.n2s(request.getParameter("severe"));
		int total = 0;
		if (month.equalsIgnoreCase("all")) {
			month = "%";
		}
		if (year.equalsIgnoreCase("all")) {
			year = "%";
		}

		List list = null;

		if (vehicleid != "" && month != "" && year != "") {
			list = DatabaseUtility.getBeanList(OBDModel.class,
					ServerConstants.QUERY_VEHICLE_REPORT_MONTHLY,
					new Object[]{vehicleid, month + "-" + year});
		} else if (vehicleid != "" && month == "" && year != "") {
			list = DatabaseUtility.getBeanList(OBDModel.class,
					ServerConstants.QUERY_VEHICLE_REPORT_YEARLY,
					new Object[]{vehicleid, year});
		}
	%>

	<fieldset>
		<span> <%
 	List vehicleList = DatabaseUtility.getBeanList(VehicleModel.class,
 			ServerConstants.QUERY_VEHICLES_DETAILS,
 			new Object[]{vehicleid});
 	VehicleModel vm1 = (VehicleModel) vehicleList.get(0);
 %>

			<table>
				<tr>
					<td><h4>Driver Name</h4></td>
					<td><input type="text" readonly="readonly" name="dname"
						id="dname" value="<%=vm1.getDrivername()%>"></input></td>
					<td><h4>Vehicle No</h4></td>
					<td><input type="text" readonly="readonly" name="vnumber"
						id="vnumber" value="<%=vm1.getVehicleno()%>"></input></td>
				</tr>
			</table>
		</span>
	</fieldset>

	<div>
		<!-- load_pct, temp, rpm, iat, maf, throttlepos, time -->
		<table id="Table1">
			<tr>
				<th>Speed</th>
				<th>Load PCT</th>
				<th>RPM</th>
				<th>IAT</th>
				<th>MAF</th>
				<th>Throttle Pos</th>
				<th>Temp</th>
				<th>Time</th>
			</tr>
			<%
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					OBDModel rm = (OBDModel) itr.next();
					float speed = Float.parseFloat(rm.getSpeed());

					if (speed < 50 && speed > 10
							&& severe.equalsIgnoreCase("Efficient")) {
			%>
			<tr>
				<td><%=rm.getSpeed()%></td>
				<td><%=rm.getLoad_pct()%></td>
				<td><%=rm.getRpm()%></td>
				<td><%=rm.getIat()%></td>
				<td><%=rm.getMaf()%></td>
				<td><%=rm.getThrottlepos()%></td>
				<td><%=rm.getTemp()%></td>
				<td><%=rm.getTime()%></td>
			</tr>
			<%
				}
					if (speed > 50 && speed < 90
							&& severe.equalsIgnoreCase("less efficient")) {
			%>
			<tr>
				<td><%=rm.getSpeed()%></td>
				<td><%=rm.getLoad_pct()%></td>
				<td><%=rm.getRpm()%></td>
				<td><%=rm.getIat()%></td>
				<td><%=rm.getMaf()%></td>
				<td><%=rm.getThrottlepos()%></td>
				<td><%=rm.getTemp()%></td>
				<td><%=rm.getTime()%></td>
			</tr>
			<%
				}
					if (speed > 90 && severe.equalsIgnoreCase("rash")) {
			%>
			<tr>
				<td><%=rm.getSpeed()%></td>
				<td><%=rm.getLoad_pct()%></td>
				<td><%=rm.getRpm()%></td>
				<td><%=rm.getIat()%></td>
				<td><%=rm.getMaf()%></td>
				<td><%=rm.getThrottlepos()%></td>
				<td><%=rm.getTemp()%></td>
				<td><%=rm.getTime()%></td>
			</tr>
			<%
				}
			%>
			<%
				}
			%>

		</table>

	</div>


	<script>
		$(document).ready(function() {
			$("#Table1").styleTable();
		});
	</script>
</body>
</html>
