<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>

<body>
	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
	<!-- BEGIN Container -->



	<div class="container" id="main-container">

		<!-- BEGIN Sidebar -->
		<div id="sidebar" class="navbar-collapse collapse">

			<jsp:include page="/WEB-INF/views/include/navigation.jsp"></jsp:include>

			<div id="sidebar-collapse" class="visible-lg">
				<i class="fa fa-angle-double-left"></i>
			</div>
			<!-- END Sidebar Collapse Button -->
		</div>
		<!-- END Sidebar -->


		<!-- BEGIN Content -->
		<div id="main-content">
			<!-- BEGIN Page Title -->
			<div class="page-title">
				<div>
					<h1>
						<i class="fa fa-dashboard"></i> Dashboard
					</h1>
					<!--<h4>Overview, stats, chat and more</h4>-->
				</div>
			</div>
			<!-- END Page Title -->

			<!-- BEGIN Breadcrumb -->
			<div id="breadcrumbs">
				<ul class="breadcrumb">
					<li class="active"><i class="fa fa-home"></i> Home</li>
				</ul>
			</div>
			<!-- END Breadcrumb -->
			<form action="${pageContext.request.contextPath}/searchOrdersCount"
				method="post" id="validation-form">
				<div class="container" id="main-container">
					<div class="col-md-1">
						<div class="col1title" style="margin: 6px 0 0 0">Date:</div>
					</div>
					<div class="col-md-3">
						<input class="form-control" placeholder="Date"
							name="from_datepicker" style="border-radius: 25px;"
							id="from_datepicker" type="date" format="dd-mm-yyyy"
							value="${cDate}" />
					</div>
					<div class="col-md-2">
						<input type="submit" name="submit" id="submit"
							class="btn btn-primary" />

					</div>
				</div>
			</form>
			<br> <br>
			<!-- BEGIN Tiles -->
			<div class="row">

				<!-- new html -->
				<!-- <div class="col-md-3">
					<div class="home_one bg_one">
						<div class="home_pic">
							<div class="home_pic_l">
								<i class="fa fa-shopping-cart"></i>
							</div>
							<div class="home_pic_r">605</div>
						</div>
						<p class="home_txt">1st Delivery Cake & Pastries 11:00 PM</p>
					</div>
				</div>
				<div class="col-md-3">
					<div class="home_one bg_two">
						<div class="home_pic">
							<div class="home_pic_l">
								<i class="fa fa-shopping-cart"></i>
							</div>
							<div class="home_pic_r">605</div>
						</div>
						<p class="home_txt">1st Delivery Cake & Pastries 11:00 PM</p>
					</div>
				</div>
				<div class="col-md-3">
					<div class="home_one bg_three">
						<div class="home_pic">
							<div class="home_pic_l">
								<i class="fa fa-shopping-cart"></i>
							</div>
							<div class="home_pic_r">605</div>
						</div>
						<p class="home_txt">1st Delivery Cake & Pastries 11:00 PM</p>
					</div>
				</div>  -->

				<div style="clear: both"></div>



				<c:set value="1" var="flag"></c:set>

				<c:forEach items="${orderCounts}" var="orderCounts" varStatus="loop">
					<!-- <a href="resoucres/index.php/orders/list_all"> -->

					<c:choose>

						<c:when test="${flag==1}">
							<div class="col-md-3">
								<div class="home_one bg_one">
									<div class="home_pic">
										<div class="home_pic_l">
											<i class="fa fa-shopping-cart"></i>
										</div>
										<div class="home_pic_r">${orderCounts.total}</div>
									</div>
									<p class="home_txt">${orderCounts.menuTitle}</p>
								</div>
							</div>
						</c:when>

						<c:when test="${flag==2}">
							<div class="col-md-3">
								<div class="home_one bg_two">
									<div class="home_pic">
										<div class="home_pic_l">
											<i class="fa fa-shopping-cart"></i>
										</div>
										<div class="home_pic_r">${orderCounts.total}</div>
									</div>
									<p class="home_txt">${orderCounts.menuTitle}</p>
								</div>
							</div>
						</c:when>


						<c:when test="${flag==3}">
							<div class="col-md-3">
								<div class="home_one bg_three">
									<div class="home_pic">
										<div class="home_pic_l">
											<i class="fa fa-shopping-cart"></i>
										</div>
										<div class="home_pic_r">${orderCounts.total}</div>
									</div>
									<p class="home_txt">${orderCounts.menuTitle}</p>
								</div>
							</div>
						</c:when>

					</c:choose>


					<c:choose>
						<c:when test="${flag>=3}">
							<c:set value="1" var="flag"></c:set>
						</c:when>
						<c:otherwise>
							<c:set value="${flag+1}" var="flag"></c:set>
						</c:otherwise>
					</c:choose>



					<%-- <div class="col-md-3">
								<div class="tile tile-orange">
									<div class="img">
										<i class="fa fa-shopping-cart fa-5x" ></i>
									</div>
									<div class="content">
										<p class="big" ><c:out value="${orderCounts.total}"></c:out></p>
										<p class="title"><c:out value="${orderCounts.menuTitle}"></c:out></p>
									</div>
								</div>

							</div>  --%>


				</c:forEach>

				<!-- 
						<a href="resoucres/index.php/orders/list_all">
							<div class="col-md-3">
								<div class="tile tile-orange">
									<div class="img">
										<i class="fa fa-comments"></i>
									</div>
									<div class="content">
										<p class="big">17</p>
										<p class="title">Cakes & Pastries</p>
									</div>
								</div>
							</div>
						</a> <a href="resoucres/index.php/orders/special">
							<div class="col-md-3">
								<div class="tile tile-orange">
									<div class="img">
										<i class="fa fa-comments"></i>
									</div>
									<div class="content">
										<p class="big">26</p>
										<p class="title">Special Cake</p>
									</div>
								</div>
							</div>
						</a> <a href="resoucres/index.php/orders/list_all">
							<div class="col-md-3">
								<div class="tile tile-orange">
									<div class="img">
										<i class="fa fa-comments"></i>
									</div>
									<div class="content">
										<p class="big">119</p>
										<p class="title">Trading & Packing</p>
									</div>
								</div>
							</div>
						</a>
					</div>


				</div>

				<div class="col-md-12">
					<div class="row">
						<a href="resoucres/index.php/orders/list_all">
							<div class="col-md-3">
								<div class="tile tile-orange">
									<div class="img">
										<i class="fa fa-comments"></i>
									</div>
									<div class="content">
										<p class="big">0</p>
										<p class="title">Pack Product Booking</p>
									</div>
								</div>
							</div>
						</a> <a href="resoucres/index.php/orders/list_all">
							<div class="col-md-3">
								<div class="tile tile-orange">
									<div class="img">
										<i class="fa fa-comments"></i>
									</div>
									<div class="content">
										<p class="big">26</p>
										<p class="title">Special Day Cakes</p>
									</div>
								</div>
							</div>
						</a>
 -->







			</div>


			<footer>
			<p>2021 © ATS.</p>
			</footer>

			<a id="btn-scrollup" class="btn btn-circle btn-lg" href="#"><i
				class="fa fa-chevron-up"></i></a>

		</div>
		<!-- END Content -->
	</div>
	<!-- END Container -->
	<!--basic scripts-->
	<script
		src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
	<script>
		window.jQuery
				|| document
						.write('<script src="${pageContext.request.contextPath}/resources/assets/jquery/jquery-2.0.3.min.js"><\/script>')
	</script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/bootstrap/js/bootstrap.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/jquery-slimscroll/jquery.slimscroll.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/jquery-cookie/jquery.cookie.js"></script>

	<!--page specific plugin scripts-->
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.resize.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.pie.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.stack.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.crosshair.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.tooltip.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/sparkline/jquery.sparkline.min.js"></script>

	<!--flaty scripts-->
	<script src="${pageContext.request.contextPath}/resources/js/flaty.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/flaty-demo-codes.js"></script>

</body>
</html>
