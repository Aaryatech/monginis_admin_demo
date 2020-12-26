<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>ATS</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/resources/img/ats-logo.png"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/vendor/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/vendor/animate/animate.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/vendor/css-hamburgers/hamburgers.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/vendor/animsition/css/animsition.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/vendor/select2/select2.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/vendor/daterangepicker/daterangepicker.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/util.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/main.css">
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
</style>
</head>
<body class="container bg-overlay">


	<div class="row">
		<div class="col-md-2">&nbsp;</div>
		<div class="col-md-8">
			<div class="login_bx">
				<div class="row">
					<div class="col-md-6">
						<div class="login_left">
						<img src="${pageContext.request.contextPath}/resources/img/ats-logo.png" style="margin: 40% 0px 0px 20%;"> 
						<!-- <h2 class="welcome">Welcome to ATS</h2>
						<p class="welcome_txt">	Lets make ATS a part of everybody’s celebration!!</p>  -->
							
						</div>
					</div>
					<div class="col-md-6">
						<div class="login_right">
							<form class="login100-form validate-form" id="form-login" action="loginProcess" method="post">
				
			     
					<h2 class="login_head">Admin Panel Login</h2>
					

					<div class="wrap-input100 validate-input" data-validate = "Valid username is required" >
						<input class="input100" type="text" id="username" name="username" placeholder="Username">
						<span class="focus-input100-1"></span>
						<span class="focus-input100-2"></span>
					</div>
                    <br>
					<div class="wrap-input100 rs1 validate-input" data-validate="Password is required" >
						<input class="input100" type="password" name="userpassword" id="userpassword" placeholder="Password"  >
						<span class="focus-input100-1"></span>
						<span class="focus-input100-2"></span>
					</div>

					<div class="container-login100-form-btn m-t-20" >
						<button class="login100-form-btn">
							Sign in
						</button>
					</div>

					<%
						if (session.getAttribute("changePassword") != null) {
					%>
					
						<p style="color: white;">Password Change Successfully</p>
					
					<%
						}

						session.removeAttribute("changePassword");
					%>

					<%
						if (session.getAttribute("changePasswordFail") != null) {
					%>
				
						<p style="color: white;">Password Not Changed</p>
					
					<%
						}

						session.removeAttribute("changePasswordFail");
					%>
					<c:if test="${not empty loginResponseMessage}">
   <!-- here would be a message with a result of processing -->
    <div style="color:white;"> ${loginResponseMessage} </div>
        	
</c:if>
<div class="form-group">
				<!-- <div class="controls">
					<label class="checkbox"> <input type="checkbox"
						value="remember" /> Remember me
					</label>
				</div> -->
			</div>
<div class="text-right">
					
						<span class="txt1" >
							<!-- Forgot -->
							<a href="${pageContext.request.contextPath}/forgetPwd"><span class="links">
							Forgot Password</span></a>
						</span>

						<a href="#" class="txt2 hov1">
							<!-- Username / Password? -->
						</a>
					</div>

					<div class="text-center">
						<span class="txt1">
							<!-- Create an account? -->
						</span>

						<a href="#" class="txt2 hov1">
							<!-- Sign up -->
						</a>
					</div>
				</form>
						</div>
					</div>
					
				</div>
			</div>
		</div>
		<div class="col-md-2">&nbsp;</div>
	</div>	


	<!-- <div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100 p-l-55 p-r-55 p-t-65 p-b-50">
				
			</div>
		</div>
	</div> -->

<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/resources/vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/resources/vendor/animsition/js/animsition.min.js"></script>
<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/resources/vendor/bootstrap/js/popper.js"></script>
	<script src="${pageContext.request.contextPath}/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/resources/vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/resources/vendor/countdowntime/countdowntime.js"></script>
<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/resources/js/mains.js"></script>

</body>
</html>
