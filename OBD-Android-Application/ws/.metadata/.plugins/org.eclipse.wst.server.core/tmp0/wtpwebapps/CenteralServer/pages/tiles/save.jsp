<%@page import="org.jfree.chart.renderer.xy.XYLineAndShapeRenderer"%>
<%@page import="org.jfree.chart.axis.ValueAxis"%>
<%@page import="org.jfree.chart.plot.XYPlot"%>
<%@page import="org.jfree.chart.axis.CategoryLabelPositions"%>
<%@page import="org.jfree.chart.axis.CategoryAxis"%>
<%@page import="org.jfree.chart.plot.CategoryPlot"%>
<%@page import="org.jfree.chart.plot.PlotOrientation"%>
<%@page import="java.io.IOException"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="org.jfree.data.xy.XYDataset"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.chart.ChartFactory"%>
<%@page import="com.util.LineChart2D"%>
<%@page import="com.util.OwnerInfoModel"%>
<%@page import="com.util.ConnectionManager"%>
<%@page import="com.util.StringHelper"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.apache.commons.collections.functors.ForClosure"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.util.DBUtils"%>
<%@page import="org.json.JSONTokener"%>
<%@page import="org.json.JSONObject"%>

<%
	String method = StringHelper.n2s(request.getParameter("method"));
	String id = StringHelper.n2s(request.getParameter("id"));
	System.out.println(method + " " + id);
	
	final java.util.HashMap parameters = StringHelper.displayRequest(request);
	String str = "";

	if (method.equalsIgnoreCase("getDomain")) {
		System.out.println("Into getDomain");
		str = ConnectionManager.getDomain();
		response.getWriter().write(str);
		response.getWriter().close();
	}
	else if (method.equalsIgnoreCase("logout")) {
session.removeAttribute("USER_MODEL");
	%>
	<script>
	window.location.href='<%=request.getContextPath()%>/pages/index.jsp';
	</script>
	<%
	}
	
	else if(method.equalsIgnoreCase("addAlert")){
	String ownerid = StringHelper.n2s(request.getParameter("ownerid"));
	String type= StringHelper.n2s(request.getParameter("type"));
	String val= StringHelper.n2s(request.getParameter("val"));
	String vehicelId= StringHelper.n2s(request.getParameter("vehicelId"));
	
	boolean b = ConnectionManager.setAlert(ownerid, type, val,vehicelId);
	if(b){   
		out.println("Alert Added In Database!");
	}
	else{
		out.println("Error in Adding Alert In Database!");
	}
	}
	
	
	else if(method.equalsIgnoreCase("addOwner")){
		String ownerid = StringHelper.n2s(request.getParameter("ownerid"));
		String drivername= StringHelper.n2s(request.getParameter("drivername"));
		String vno= StringHelper.n2s(request.getParameter("vno"));
		String imei= StringHelper.n2s(request.getParameter("imei"));
		
		boolean b = ConnectionManager.addOwner(ownerid, drivername, vno, imei);
		if(b){
			out.println("Vehicle Details Added In Database!");
		}
		else{
			out.println("Error in Adding Vehicle Details In Database!");
		}
	
	
	}else if(method.equalsIgnoreCase("createNewUser")){
		
		
		boolean b = ConnectionManager.createNewUser(parameters);
		if(b){
			out.println("User has been registered");
		}
		else{
			out.println("Error adding user details!");
		}
	
	
	}
	
	
	else if(method.equalsIgnoreCase("deleteDriver")){
		String ownerid = StringHelper.n2s(request.getParameter("ownerid"));
		String vehicleid = StringHelper.n2s(request.getParameter("vehicleid"));
		boolean b =ConnectionManager.deleteDriver(vehicleid);
	
		if(b){
			out.println("Driver Details Deleted From Database!");
		}
		else{
			out.println("Error in Deleting Database!");
		}
		
	
	
	
	}else if(method.equalsIgnoreCase("deleteVehicle")){
		String vehicleId = StringHelper.n2s(request.getParameter("vehicleId"));
		String qu="delete from vehicles where vehicleId="+vehicleId;
		ConnectionManager.executeUpdate(qu);
	}else if(method.equalsIgnoreCase("updateContact")){
		String userid = StringHelper.n2s(request.getParameter("userid"));
		String phoneno = StringHelper.n2s(request.getParameter("phoneno"));
		String qu="update useraccounts set phoneno='"+phoneno+"' where userid="+userid;
		ConnectionManager.executeUpdate(qu);
	}
	
	
	else if(method.equalsIgnoreCase("deleteAlert")){
		String ownerid = StringHelper.n2s(request.getParameter("alertid"));
		
String qu="delete from alerts where alertId="+ownerid;
ConnectionManager.executeUpdate(qu);
	
	}
	
	else if(method.equalsIgnoreCase("delUser")){
		String ownerid = StringHelper.n2s(request.getParameter("ownerid"));
		boolean b = ConnectionManager.delOwner(ownerid);
		if(b){
			out.println("User Details Deleted From Database!");
		}
		else{
			out.println("Error in Deleting Database!");
		}
	}	else if(method.equalsIgnoreCase("generateDriverPerformance")){
		List list =(List)session.getAttribute("LIST2");
		
		 XYDataset pop = LineChart2D.createDataset2(list);
		  
		  JFreeChart chart = ChartFactory.createTimeSeriesChart(
		  "Driver Performance Graph",
		  "Date",
		  "VechicleSpeed",
		  pop,
		  true,
		  true,   
		  false);
		  
		  XYPlot plot = (XYPlot)chart.getPlot();
	       
	        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        renderer.setSeriesLinesVisible(0, true);
	        renderer.setSeriesShapesVisible(1, true);
	        renderer.setSeriesShapesVisible(2, true);
	        plot.setRenderer(renderer);

	      
	        
		  ValueAxis xAxis =(ValueAxis) plot.getDomainAxis();
		  xAxis.setVerticalTickLabels(true);
		  xAxis.setTickLabelsVisible(true);
		  try {
			  ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 1000, 500);
		//  ChartUtilities.saveChartAsJPEG(new File("D:\\chart.jpg"), chart, 500, 300);
		  } catch (IOException e) {   
		  System.err.println("Problem occurred creating chart.");
		  }
		  return;
		  
	}
	
	
	else if (method.equalsIgnoreCase("send")) {

				String iat = StringHelper.nullObjectToStringEmpty(parameters
						.get("iat"));
				String maf = StringHelper.nullObjectToStringEmpty(parameters
						.get("maf"));
				String throttlepos = StringHelper
						.nullObjectToStringEmpty(parameters.get("throttlepos"));
				
				String load_pct = StringHelper.nullObjectToStringEmpty(parameters.get("load_pct"));
				String vss = StringHelper.nullObjectToStringEmpty(parameters.get("vss"));
				String rpm = StringHelper.nullObjectToStringEmpty(parameters.get("rpm"));
				String temp = StringHelper.nullObjectToStringEmpty(parameters.get("temp"));
				String imei = StringHelper.nullObjectToStringEmpty(parameters.get("imei"));
				String latsend = StringHelper.nullObjectToStringEmpty(parameters.get("latsend"));
				String lngsend = StringHelper.nullObjectToStringEmpty(parameters.get("lngsend"));
				long time= StringHelper.n2long(parameters.get("time"));
				String dt=StringHelper.getDBDate(time);
				OwnerInfoModel oi  = ConnectionManager.getOwnerId(imei);
				String ownerid;
				String ownerno;
				if(oi!=null){
					try{
						String query = "INSERT INTO obdserver (load_pct, temp, rpm, vss, iat, maf, throttlepos, vehicleId, latsend, lngsend,timeLong,`time`)  values(?,?,?,?,?,?,?,?,?,?,?,?)";
						System.out.println(query);
						int i = ConnectionManager.executeUpdate(query, new Object[] { load_pct,temp, rpm, vss, iat, maf, throttlepos, oi.getVehicleId(), latsend, lngsend,time,dt});
						
						
						boolean b = ConnectionManager.setOBDList(load_pct, iat, maf, throttlepos, vss, rpm, temp,  latsend, lngsend,oi);
						if(b){
							System.out.println("Writing Data To Server From Client " +b);
						}else{
							System.out.println("Server Writing Error / Connection Error! \n Please Restart The Server!" +b);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
				
			
	
		response.getWriter().write("DONE");
		response.getWriter().close();
		
 
	}else if (method.equalsIgnoreCase("getVehicles")) {
		str = ConnectionManager.getVehicleList(parameters); 
		response.getWriter().write(str);
		response.getWriter().close();
	}
	
	
	
	
%>