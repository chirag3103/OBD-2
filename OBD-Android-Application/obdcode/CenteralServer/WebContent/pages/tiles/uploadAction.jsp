
<%@page import="com.util.DataImporter"%>
<%@page import="com.util.StringHelper"%>
<%@page import="java.io.OutputStream"%>

<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="javax.imageio.ImageIO"%>

<%@page import="java.io.File"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStream"%>

<%@page import="java.util.HashMap"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	String sMethod = StringHelper.n2s(request.getParameter("methodId"));
	String returnString = "";
	boolean bypasswrite = false;
	System.out.println(sMethod);
	HashMap parameters = StringHelper.displayRequest(request);
	
	 if (sMethod.equalsIgnoreCase("uploadFunction")) {

		parameters = StringHelper.parseMultipartRequest(request);		

		String myProfileName = StringHelper.n2s(parameters
				.get("myProfilePicture"));
		System.out.println(" Pic " + myProfileName);
   
		FileItem fi = (FileItem) parameters.get("myProfilePictureITEM");

		InputStream is = fi.getInputStream();
		
		System.out.println("picname " + myProfileName);			

		String fname = (String) parameters.get("myProfilePicture");
		String fnameTxt = (String) parameters
				.get("myProfilePictureName");
		final File f=new File( fname);
		fi.write(f);
		
		try{
			System.out.println(f.getCanonicalPath());
		
		String s=			DataImporter.uploadData(f);
			
			out.write(s);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}


	
%>


