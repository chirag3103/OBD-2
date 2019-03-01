<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="<%=request.getContextPath()%>/jquery.autocomplete-1.1.3/jquery-1.7.2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="<%=request.getContextPath()%>/theme/style.css" rel="stylesheet" type="text/css" media="screen" />
<title>Insert title here</title>
</head>
<body>
  <BR><BR><BR>

<form id="myform" name="myform">
<table style="margin: auto;font-size:12px;">
	<tr><td colspan="2" style="text-align: center;"><h1>Owner Registration:</h1></td>
	</tr>
	<tr>
		<td>Full Name</td>
		<td><input type="text" name = "fname"  id="fname"></input></td>
	</tr>
	<tr>
		<td>Phone No</td>
		<td><input type="text" name = "phoneNo"  id="phoneNo"></input></td>    
	</tr>
	<tr>
		<td>Login Id</td>
		<td><input type="text" name = "login"  id="login"></input></td>
	</tr>
		<tr>
		<td>Password</td>
		<td><input type="password" name = "pass"  id="pass"></input></td>
	</tr>
		<tr>
		<td>Confirm Password</td>
		<td><input type="password" name = "cpass"  id="cpass"></input></td>
	</tr>
		<tr>
		<td colspan="2" style="text-align: center;"><input type="button" value="Register Me" onclick="fnAdd();"/></td>
	
	</tr>
	</table>
	</form>
	<script>
	function fnAdd(){
		fname=$("#fname").val();
		phoneNo=$("#phoneNo").val();
		login=$("#login").val();
		cpass=$("#cpass").val();
		pass=$("#pass").val();
		
		if(fname.length<=0){
			alert("Please enter first name!");
		}else if(phoneNo.length<=0){
			alert("Please enter phone no");
		}else if(login.length<=0){
			alert("Please enter Login!");
		}else if(cpass!=pass){
			alert("Password and confirm password are not same!");
		}else{
		
		
			$.ajax({
				  type: "POST",
				  url: "<%=request.getContextPath()%>/pages/tiles/save.jsp?method=createNewUser",
				  dataType: "text",
				  data:$('#myform').serialize()
				}).done(function( msg ) {
					alert(msg.trim());
					window.location.reload();
			});
		}
		
		
	}
	
	</script>
</body>
</html>