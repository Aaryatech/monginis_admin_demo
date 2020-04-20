<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Dashboard - Admin</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

<!--base css styles-->
<link rel="stylesheet"
	href="resources/assets/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet"
	href="resources/assets/font-awesome/css/font-awesome.min.css">

<!--page specific css styles-->

<!--flaty css styles-->
<link rel="stylesheet" href="resources/css/flaty.css">
<link rel="stylesheet" href="resources/css/flaty-responsive.css">

<link rel="shortcut icon" href="resources/img/favicon.png">

<style type="text/css">
.bg-overlay {
    background: linear-gradient(rgba(0,0,0,.4), rgba(0,0,0,.4)), url("${pageContext.request.contextPath}/resources/img/cake.jpg");
   background-repeat: no-repeat;
    background-size: cover;
    background-position: center center;
    color: #fff;
    height:auto;
    width:auto;
    padding-top: 0px;
}    

.login_bx{background: #FFF; border-radius:10px; color:#000; margin: 200px 0 0 0;}
.login_left{padding:40px 10px 30px 40px;}
.login_right{width: 100%; border-radius:0 10px 10px 0; padding:45px 20px; background: #ec268f; min-height: 240px;
text-align: center; color: #FFF;} 
.login_right img{width: 150px;}
.login_head{color: #333; font-size: 22px; margin: 0 0 20px 0; text-align: center; font-weight: 400; font-family: 'Source Sans Pro', sans-serif;}
.welcome{color: #FFF; font-size: 15px; font-weight: 600; font-family: 'Source Sans Pro', sans-serif; 
text-transform: uppercase; margin: 15px 0 5px 0;}
.welcome_txt{color: #ffe3f2; font-size: 13px; line-height: 18px;}

</style>

</head>
<body class="container bg-overlay"><!-- login-page -->



	<div class="row">
		<div class="col-md-2">&nbsp;</div>
		<div class="col-md-8">
			<div class="login_bx">
				<div class="row">
					<div class="col-md-6">
						<div class="login_left">
							<form id="form-forgot" action="${pageContext.request.contextPath}/getUserInfo" method="post">
			<h3 class="login_head">Forgot password</h3>
			
			<div class="form-group">
				<div class="controls">
					<input type="text" placeholder="Username" class="form-control" id="username" name="username" required/>
				</div>
			</div>
			<div class="form-group">
				<div class="controls">
					<button type="submit" class="btn btn-primary form-control">Send OTP</button>
				</div>
			</div>
			
			<p class="clearfix">
				<a href="${pageContext.request.contextPath}/login" class="goto-login pull-left">Back to Login</a>
			</p>
		</form>
						</div>
					</div>
					<div class="col-md-6">
						<div class="login_right">
						<img src="${pageContext.request.contextPath}/resources/img/monginislogo.png">
						<h2 class="welcome">Welcome to Monginis</h2>
						<p class="welcome_txt">	Lets make Monginis a part of everybody’s celebration!!</p>
							
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-2">&nbsp;</div>
	</div>


	<!-- BEGIN Main Content -->
	<div class="login-wrapper">
	
	<!-- BEGIN Forgot Password Form -->
		
		<!-- END Forgot Password Form -->
	
	<%-- 
		<!-- BEGIN Login Form -->
		<form id="form-login" action="loginProcess" method="post">
			<h3>Forget Password</h3>
		
		


			<hr />
			<div class="form-group">
				<div class="controls">
					<input type="text" placeholder="Username" class="	form-control"
						path="username" name="username" id="username" required/>

				</div>
			</div>
			<!-- <div class="form-group">
				<div class="controls">
					<input type="password" placeholder="Password" class="form-control"
						path="userpassword" name="userpassword" id="userpassword"  required/>
				</div>
			</div> -->
			<!-- <div class="form-group">
				<div class="controls">
					<label class="checkbox"> <input type="checkbox"
						value="remember" /> Remember me
					</label>
				</div>
			</div> -->
			<div class="form-group">
				<div class="controls">
					<button type="submit" class="btn btn-primary form-control" onclick="getUserDate()">Submit
						</button>
				</div>
				
				<c:if test="${not empty loginResponseMessage}">
   <!-- here would be a message with a result of processing -->
    <div> ${loginResponseMessage} </div>
        	
</c:if>
				
				
			</div>
			<hr />
			<p class="clearfix">
				<a href="#" class="goto-forgot pull-left">Forgot Password?</a> <a
					href="#" class="goto-register pull-right">Sign up now</a>
			</p>
		</form>
		<!-- END Login Form -->
 --%>
		

	
	</div>
	<!-- END Main Content -->

	<!--basic scripts-->
	<script
		src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="resources/assets/jquery/jquery-2.0.3.min.js"><\/script>')</script>
	<script src="resources/assets/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript">
function getUserInfo(){
	var username = $("#username").val();
	alert(username);
	$(document)
	.ready(
			function() {
				$
						.getJSON(
								'${getUserInfo}',
								{
									username : username,
									ajax : 'true'
								},
								function(data) {
									//alert(JSON.stringify(data))
									

								});

			});
}

</script>

	<script type="text/javascript">
            function goToForm(form)
            {
                $('.login-wrapper > form:visible').fadeOut(500, function(){
                    $('#form-' + form).fadeIn(500);
                });
            }
            $(function() {
                $('.goto-login').click(function(){
                    goToForm('login');
                });
                $('.goto-forgot').click(function(){
                    goToForm('forgot');
                });
                $('.goto-register').click(function(){
                    goToForm('register');
                });
            });
        </script>
</body>
</html>
