/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.39
 * Generated at: 2017-02-16 17:10:46 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.pages;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class addOwner_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

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
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<script src=\"");
      out.print(request.getContextPath());
      out.write("/jquery.autocomplete-1.1.3/jquery-1.7.2.js\"></script>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\r\n");
      out.write("<link href=\"");
      out.print(request.getContextPath());
      out.write("/theme/style.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\r\n");
      out.write("<title>Insert title here</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("  <BR><BR><BR>\r\n");
      out.write("\r\n");
      out.write("<form id=\"myform\" name=\"myform\">\r\n");
      out.write("<table style=\"margin: auto;font-size:12px;\">\r\n");
      out.write("\t<tr><td colspan=\"2\" style=\"text-align: center;\"><h1>Owner Registration:</h1></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>Full Name</td>\r\n");
      out.write("\t\t<td><input type=\"text\" name = \"fname\"  id=\"fname\"></input></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>Phone No</td>\r\n");
      out.write("\t\t<td><input type=\"text\" name = \"phoneNo\"  id=\"phoneNo\"></input></td>    \r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>Login Id</td>\r\n");
      out.write("\t\t<td><input type=\"text\" name = \"login\"  id=\"login\"></input></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t<td>Password</td>\r\n");
      out.write("\t\t<td><input type=\"password\" name = \"pass\"  id=\"pass\"></input></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t<td>Confirm Password</td>\r\n");
      out.write("\t\t<td><input type=\"password\" name = \"cpass\"  id=\"cpass\"></input></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t\t<tr>\r\n");
      out.write("\t\t<td colspan=\"2\" style=\"text-align: center;\"><input type=\"button\" value=\"Register Me\" onclick=\"fnAdd();\"/></td>\r\n");
      out.write("\t\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t</table>\r\n");
      out.write("\t</form>\r\n");
      out.write("\t<script>\r\n");
      out.write("\tfunction fnAdd(){\r\n");
      out.write("\t\tfname=$(\"#fname\").val();\r\n");
      out.write("\t\tphoneNo=$(\"#phoneNo\").val();\r\n");
      out.write("\t\tlogin=$(\"#login\").val();\r\n");
      out.write("\t\tcpass=$(\"#cpass\").val();\r\n");
      out.write("\t\tpass=$(\"#pass\").val();\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tif(fname.length<=0){\r\n");
      out.write("\t\t\talert(\"Please enter first name!\");\r\n");
      out.write("\t\t}else if(phoneNo.length<=0){\r\n");
      out.write("\t\t\talert(\"Please enter phone no\");\r\n");
      out.write("\t\t}else if(login.length<=0){\r\n");
      out.write("\t\t\talert(\"Please enter Login!\");\r\n");
      out.write("\t\t}else if(cpass!=pass){\r\n");
      out.write("\t\t\talert(\"Password and confirm password are not same!\");\r\n");
      out.write("\t\t}else{\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\t$.ajax({\r\n");
      out.write("\t\t\t\t  type: \"POST\",\r\n");
      out.write("\t\t\t\t  url: \"");
      out.print(request.getContextPath());
      out.write("/pages/tiles/save.jsp?method=createNewUser\",\r\n");
      out.write("\t\t\t\t  dataType: \"text\",\r\n");
      out.write("\t\t\t\t  data:$('#myform').serialize()\r\n");
      out.write("\t\t\t\t}).done(function( msg ) {\r\n");
      out.write("\t\t\t\t\talert(msg.trim());\r\n");
      out.write("\t\t\t\t\twindow.location.reload();\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\t</script>\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
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