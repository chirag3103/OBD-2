<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">



<%@page import="java.util.Iterator"%>
<%@page import="com.util.BehaviourModel"%>
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

 <%
  	ArrayList<BehaviourModel> arr = ConnectionManager
  			.getEfficiencyReport();   
  	session.setAttribute("LIST21", arr);
  	ArrayList<BehaviourModel> cluster1=new ArrayList<BehaviourModel>();
  	ArrayList<BehaviourModel> cluster2=new ArrayList<BehaviourModel>();
  	ArrayList<BehaviourModel> cluster3=new ArrayList<BehaviourModel>();
  	
	for (Iterator iterator = arr.iterator(); iterator.hasNext();) {
		BehaviourModel behaviourModel = (BehaviourModel) iterator.next();
		if(behaviourModel.getCluster()==0){
			cluster1.add(behaviourModel);
		}else if(behaviourModel.getCluster()==1){
			cluster2.add(behaviourModel);
		}else if(behaviourModel.getCluster()==2){
			cluster3.add(behaviourModel);
		}
	}
	session.setAttribute("cluster1", cluster1);
	session.setAttribute("cluster2", cluster2);
	session.setAttribute("cluster3", cluster3);
	
  %>    

	<div id="welcome">
		<h2 class="title"><a href="#">Welcome to <%=ServerConstants.APPLICATION_HEAD%></a></h2>
		<%@include file="./tiles/menu.jsp" %>   
		<div class="entry">

			

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
				<h2>Driver Behaviour Details</h2>
			
				</fieldset>
				</form>
				<br></br>
				<fieldset>
				<span>
				
						
										<div style="width: 98%; text-align: left;">
			<h1>ALL Records</h1><BR>
							<display:table name="sessionScope.LIST21" class="hovertable"
								requestURI="/pages/driver_behaviour_report.jsp"
								id="driverRuleId" pagesize="10" style=" width:100%;"
								defaultsort="1" defaultorder="ascending" export="false"
								sort="list">



								<!--  vehicleId, fullname, drivername, vehicleno -->
								<display:column title="Sr.No" sortable="true"><%=pageContext.getAttribute("driverRuleId_rowNum")%></display:column>
								<display:column title="Vehicle No" sortable="true"
									property="vehicleId"></display:column>
								<display:column title="Driver Name" sortable="true"
									property="driverName"></display:column>
								<display:column title="Efficient %" sortable="true"
									property="noOfEfficientPer"></display:column>
								<display:column title="Rash %" sortable="true"
									property="noOfInefficientPer"></display:column>

								<display:column title="Total Distance" sortable="true"
									property="distance"></display:column>
								<display:column title="AvgSpeed Efficient Km/hr" sortable="true"
									property="avgSpeedEff"></display:column>
								<display:column title="AvgSpeed Rash Km/hr" sortable="true"
									property="avgSpeedInEff"></display:column>
								<display:column title="Date" sortable="true" property="date"></display:column>
							</display:table><BR>
					
					
					
					
					
						<h1>Cluster - 0 Efficient Driving</h1><BR>
							<display:table name="sessionScope.cluster1" class="hovertable"
								requestURI="/pages/driver_behaviour_report.jsp"
								id="driverRuleId" pagesize="10" style=" width:100%;"
								defaultsort="1" defaultorder="ascending" export="false"
								sort="list">



								<!--  vehicleId, fullname, drivername, vehicleno -->
								<display:column title="Sr.No" sortable="true"><%=pageContext.getAttribute("driverRuleId_rowNum")%></display:column>
								<display:column title="Vehicle No" sortable="true"
									property="vehicleId"></display:column>
								<display:column title="Driver Name" sortable="true"
									property="driverName"></display:column>
								<display:column title="Efficient %" sortable="true"
									property="noOfEfficientPer"></display:column>
								<display:column title="Rash %" sortable="true"
									property="noOfInefficientPer"></display:column>

								<display:column title="Total Distance" sortable="true"
									property="distance"></display:column>
								<display:column title="AvgSpeed Efficient Km/hr" sortable="true"
									property="avgSpeedEff"></display:column>
								<display:column title="AvgSpeed Rash Km/hr" sortable="true"
									property="avgSpeedInEff"></display:column>
								<display:column title="Date" sortable="true" property="date"></display:column>
							</display:table><BR>
							
							
							
							
									<h1>Cluster - 1 Less Efficient Driving</h1><BR>
							<display:table name="sessionScope.cluster2" class="hovertable"
								requestURI="/pages/driver_behaviour_report.jsp"
								id="driverRuleId" pagesize="10" style=" width:100%;"
								defaultsort="1" defaultorder="ascending" export="false"
								sort="list">



								<!--  vehicleId, fullname, drivername, vehicleno -->
								<display:column title="Sr.No" sortable="true"><%=pageContext.getAttribute("driverRuleId_rowNum")%></display:column>
								<display:column title="Vehicle No" sortable="true"
									property="vehicleId"></display:column>
								<display:column title="Driver Name" sortable="true"
									property="driverName"></display:column>
								<display:column title="Efficient %" sortable="true"
									property="noOfEfficientPer"></display:column>
								<display:column title="Rash %" sortable="true"
									property="noOfInefficientPer"></display:column>

								<display:column title="Total Distance" sortable="true"
									property="distance"></display:column>
								<display:column title="AvgSpeed Efficient Km/hr" sortable="true"
									property="avgSpeedEff"></display:column>
								<display:column title="AvgSpeed Rash Km/hr" sortable="true"
									property="avgSpeedInEff"></display:column>
								<display:column title="Date" sortable="true" property="date"></display:column>
							</display:table><BR>
							
							
							
							
							<h1>Cluster -2 Rash Driving</h1><BR>
							<display:table name="sessionScope.cluster3" class="hovertable"
								requestURI="/pages/driver_behaviour_report.jsp"
								id="driverRuleId" pagesize="10" style=" width:100%;"
								defaultsort="1" defaultorder="ascending" export="false"
								sort="list">



								<!--  vehicleId, fullname, drivername, vehicleno -->
								<display:column title="Sr.No" sortable="true"><%=pageContext.getAttribute("driverRuleId_rowNum")%></display:column>
								<display:column title="Vehicle No" sortable="true"
									property="vehicleId"></display:column>
								<display:column title="Driver Name" sortable="true"
									property="driverName"></display:column>
								<display:column title="Efficient %" sortable="true"
									property="noOfEfficientPer"></display:column>
								<display:column title="Rash %" sortable="true"
									property="noOfInefficientPer"></display:column>

								<display:column title="Total Distance" sortable="true"
									property="distance"></display:column>
								<display:column title="AvgSpeed Efficient Km/hr" sortable="true"
									property="avgSpeedEff"></display:column>
								<display:column title="AvgSpeed Rash Km/hr" sortable="true"
									property="avgSpeedInEff"></display:column>
								<display:column title="Date" sortable="true" property="date"></display:column>
							</display:table><BR>		
							
						</div>						   
												
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

