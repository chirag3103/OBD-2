<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">



<%@page import="com.util.MasterModel"%>
<%@page import="com.util.UserAccountModel"%>
<%@page import="java.util.Date"%>
<%@page import="com.util.ConnectionManager"%>
<%@page import="com.util.StringHelper"%>
<%@page import="com.util.UserModel"%>
<%@page import="com.util.ServerConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OBD Driver Report</title>
<link href="<%=request.getContextPath()%>/theme/looks.css" rel="stylesheet" type="text/css" media="screen" />
<link href="<%=request.getContextPath()%>/theme/style.css" rel="stylesheet" type="text/css" media="screen" />
	<script src="<%=request.getContextPath()%>/jquery.autocomplete-1.1.3/jquery-1.7.2.js"></script>
</head>  
<body>   

<div id="wrapper" style="text-align: center;"> 

 

	<div id="welcome">
		<h2 class="title"><a href="#">Welcome to <%=ServerConstants.APPLICATION_HEAD%></a></h2>
		<%@include file="./tiles/menu.jsp" %>   
		<div class="entry">

				<%
					String uid = StringHelper.n2s(((UserAccountModel) session
									.getAttribute("USER_MODEL")).getUserid());
							String month = StringHelper.n2s(request.getParameter("month"));
							String year = StringHelper.n2s(request.getParameter("year"));
							String severe = StringHelper.n2s(request.getParameter("severe"));
							List list= new ArrayList<com.util.MasterModel>();   
							List list1=new ArrayList<MasterModel>();
							if (month != "" && year != "" && severe != "") {		
								list= ConnectionManager.getDriverReport(month, year, severe, uid); 
							} else if (month == "" && year != "" && severe != ""){
								list = ConnectionManager.getDriverReport(month, year, severe, uid);		
							}
							
							if(list.size()>0){
								session.setAttribute("DRIVER_REPORT", list);
							}else{
								session.removeAttribute("DRIVER_REPORT");
							}
				%>


			</div>
	</div>
		<div id="two-columns">
		<!-- for viewing vehicle details -->
			<div id="popup_box"
				style="width: 550px; height: 350px; overflow-y: scroll;display: none;">
				<!-- OUR PopupBox DIV-->
				<span id="popup_text">View Driver Details</span> <a id="popupBoxClose"><img src="<%=request.getContextPath()%>/theme/images/Close Button.png"></a>
			</div>
			<div id="two-columns">
				<center>
				<form name="myform" id="myform">
				<fieldset>
				<h2>Vehicles Details</h2>
					<table>
						<tr>
							<td><strong>Select :</strong></td>
								<td><select name="severe" id="severe" size="3" title="Select severity">
								
																		<option value="">Select Behavior</option>
																		<option value="efficient">Efficient</option>
																		<option value="less efficient">Less Efficient</option>
																		<option value="rash">Rash</option>
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
									<span class="t">Show</span> <span class="r"><span></span></span>
									<span class="l"></span>
							</span>
								</button></td>
						</tr>

					</table>
				</fieldset>
				</form>
				<br></br>
				<fieldset>
				<span>
				
											<%
																if (list.size() > 0) {
																	com.util.MasterModel mm1=	((com.util.MasterModel)list.get(0));
																
															%>
						
										<div style="width: 98%; text-align: left;">
							<display:table name="sessionScope.DRIVER_REPORT"
								class="hovertable" requestURI="/pages/driverreport.jsp" id="driverRuleId" pagesize="4" style=" width:100%;"
								defaultsort="1" defaultorder="ascending" export="true"
								sort="list">
								<%
														String vehicleId="";
														com.util.MasterModel mm=null;
														try{
															mm=((com.util.MasterModel) pageContext.getAttribute("driverRuleId"));
												
														}catch(Exception e){
															e.printStackTrace();
														}
														%>
								<display:setProperty name="export.csv.filename"
									value="driver report.csv"></display:setProperty>
								<display:setProperty name="export.xml.filename"
									value="driver report.xml"></display:setProperty>
								<display:setProperty name="export.csv.filename"
									value="driver report.xls"></display:setProperty>
								<!--  vehicleId, fullname, drivername, vehicleno -->
								<display:column title="Sr.No" sortable="true"><%=pageContext.getAttribute("driverRuleId_rowNum")%></display:column>
								<display:column title="Vehicle No" sortable="true"><%=mm.getVehicleno()%></display:column>
								<display:column title="Driver Name" sortable="true"><%=mm.getDrivername() %></display:column>

								<display:column title="View Details" sortable="true"
									media="html">
									<a href="#" title="view details"
										onclick="fnView('<%=mm.getVehicleId() %>','<%=month%>','<%=year%>','<%=severe%>')"> <img
										src="<%=request.getContextPath()%>/theme/images/view3.png"
										height="25px" width="70px" />
									</a>
								</display:column>
							</display:table>
						</div>						   
													<%}else{%>
													<label style="font-family: verdana;font-size: 12px;color: navy;">No Record Found....</label>
													<%} %>
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

$("#severe").val('<%=severe%>');
$("#month").val('<%=month%>');	
$("#year").val('<%=year%>');

function fnView(id,month,year,severe){
	$.ajax({
		  type: "POST",
		  url: "<%=request.getContextPath()%>/pages/reportvehicleview.jsp?id="+ id+ "&severe="+ severe+"&month="+ month+ "&year="+ year,
					dataType : "text"
				}).done(function(msg) {
			$("#popup_text").html(msg);
			$('#popup_box').fadeIn("slow");
		});
}

$('#popupBoxClose').click(function() {	
	$('#popup_box').fadeOut("slow");
});

</script>

</body>
</html>

