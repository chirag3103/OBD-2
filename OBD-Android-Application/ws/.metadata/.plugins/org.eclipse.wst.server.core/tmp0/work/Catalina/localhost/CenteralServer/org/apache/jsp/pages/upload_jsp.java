/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.39
 * Generated at: 2016-06-01 16:24:43 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.pages;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.util.OBDServerModel;
import com.util.DBUtils;
import com.util.OwnerInfoModel;
import java.util.HashMap;
import java.util.Iterator;
import com.util.StringHelper;
import com.util.ConnectionManager;
import java.util.List;
import com.util.*;
import com.util.ServerConstants;
import com.util.UserAccountModel;

public final class upload_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(2);
    _jspx_dependants.put("/WEB-INF/displaytag-12.tld", Long.valueOf(1399753273477L));
    _jspx_dependants.put("/pages/./tiles/menu.jsp", Long.valueOf(1464798174211L));
  }

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("   \r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>Upload File</title>\r\n");
      out.write("<link href=\"");
      out.print(request.getContextPath());
      out.write("/theme/style.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\r\n");
      out.write("<script src=\"");
      out.print(request.getContextPath());
      out.write("/jquery.autocomplete-1.1.3/jquery-1.7.2.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<style type=\"text/css\">\r\n");
      out.write("@import \"gallery.css\";\r\n");
      out.write("</style>\r\n");
      out.write("\r\n");
      out.write("</head>\r\n");
      out.write("<body style=\"\">\r\n");
      out.write("<div id=\"wrapper\" style=\"text-align: center;\">\r\n");
      out.write("<BR>\r\n");
      out.write("\r\n");
      out.write("\r\n");

Object o=session.getAttribute("USER_MODEL");
UserAccountModel um=null;
if(o==null){
	
      out.write("\r\n");
      out.write("\t<script>\r\n");
      out.write("\twindow.location.href=\"");
      out.print(request.getContextPath());
      out.write("/pages/login.jsp\";\r\n");
      out.write("\t</script>\r\n");
      out.write("\t");

}else{
	um=(UserAccountModel)o;
}
boolean isAdmin=false;
if(um.getRole()== ServerConstants.ROLE_ADMIN){ 
	isAdmin=true;
}


      out.write("\r\n");
      out.write("<h2>Welcome ");
      out.print(um.getFullname() );
      out.write("</h2>   \r\n");
      out.write("<BR>\r\n");
      out.write('\r');
      out.write('\n');
if(isAdmin){ 
      out.write("\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/addownervehicle.jsp\">Add/Delete Owner</a>&nbsp;&nbsp;&nbsp;\r\n");
}else{ 
      out.write("\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/addownervehicle.jsp\">Edit Details</a>&nbsp;&nbsp;&nbsp;\r\n");
} 
      out.write("\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/addalert.jsp\">Add/Delete Alerts</a>&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/vehicle.jsp\">Live Data</a>&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/driverperformance.jsp\">Driver Performance</a>&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/vehiclereport.jsp\"><span><span>Vehicle Report</span></span></a>&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/driverreport.jsp\" ><span><span>Driver Report</span></span></a>&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/driver_behaviour_report.jsp\" ><span><span>Driver Behaviour Report</span></span></a>&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/upload.jsp\" ><span><span>Upload File</span></span></a>&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/map.jsp?sfacility=12\" ><span><span>View Map</span></span></a>&nbsp;&nbsp;&nbsp;\r\n");
      out.write("<!-- style=\"background-color: yellow;\" -->\r\n");
      out.write("<a href=\"");
      out.print(request.getContextPath());
      out.write("/pages/login.jsp\">Logout</a>");
      out.write("  \r\n");
      out.write("\r\n");
      out.write("<BR><BR>Upload File to Server<BR> \r\n");
      out.write("\r\n");
      out.write("\t<form method=\"post\" enctype=\"multipart/form-data\"\r\n");
      out.write("\t\taction=\"");
      out.print(request.getContextPath());
      out.write("/pages/tiles/uploadAction.jsp?methodId=uploadFunction\"\r\n");
      out.write("\t\ttarget=\"myFrame\">\r\n");
      out.write("\t\tUpload File<BR>\r\n");
      out.write("\t\t<BR> Select File \r\n");
      out.write("\t\t<input type=\"file\" value=\"\" name=\"myProfilePicture\"><BR>\r\n");
      out.write("\t\t<BR>\r\n");
      out.write("\t\t<BR> <input type=\"submit\" value=\"Upload File\" name=\"btn\">\r\n");
      out.write("\t</form>\r\n");
      out.write("\t<iframe name=\"myFrame\" width=\"400px\" height=\"100px\" style=\"border-style: none;border: none;\" ></iframe>\r\n");
      out.write("\t\r\n");
      out.write("\t</div>\r\n");
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\t\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</html>\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
