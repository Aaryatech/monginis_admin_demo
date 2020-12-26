<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<style type="text/css">
.bg-overlay {
	background: linear-gradient(rgba(0, 0, 0, .4), rgba(0, 0, 0, .4)),
		url("${pageContext.request.contextPath}/resources/img/cake.jpeg");
	/*  background-repeat: no-repeat; */
	/* background-size: cover;
    background-position: center center; */
	color: black;
	height: auto;
	width: auto;
	padding-left: 50px;
}
</style>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
.buttonload {
	background-color: transparent; /* Green background */
	border: none; /*Remove borders */
	color: #ec268f; /* White text */
	/*   padding: 12px 20px; /* Some padding */ */
	font-size: 15px; /* Set a font-size */
	display: none;
}

/* Add a right margin to each icon */
.fa {
	margin-left: 12px;
	margin-right: 8px;
}
</style>
</head>
<c:url var="getDashboardData" value="/getDashboardData"></c:url>
<c:url var="getDashboardYearlyData" value="/getDashboardYearlyData"></c:url>

<c:url var="getDashYearlyDataForGraph"
	value="/getDashYearlyDataForGraph"></c:url>





<body class="container bg-overlay" style="padding-left: 0px; padding-right:0px;"
	onload="drawGraph()">

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
	<!-- BEGIN Container -->



	<div class="container1" id="main-container">

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

			<form action="${pageContext.request.contextPath}/showDashboard"
				class="form-horizontal" method="get">

				<!-- BEGIN Breadcrumb -->
				<div  style="background: #FFF; padding:10px; clear:both; margin: 0 0 20px 0;">
				<div class="row" id="breadcrumbs">
				
				
				<div class="col-md-1">
					<label class="control-label" style="padding-top: 8px;">Date</label>
				</div>
				<div class="col-md-5 controls">
					<input class="form-control date-picker" id="date" type="text"
						name="date" value="${dispDate}" placeholder="Date" autocomplete="off" required />
				</div>
				<div class="col-md-2 controls">
								<button type="button" class="btn btn-primary"
									onclick="searchData()" style="margin: 2px 0 0 0;">Search</button>

							</div>
							<div class="col-md-2">
								<button class="buttonload" id="loader">
									<i class="fa fa-spinner fa-spin"></i>Loading
								</button>
							</div>
				
				
				<!-- <div >
					<ul class="breadcrumb">
						<li class="active">
							
							</li>
					</ul>
				</div> --></div></div>
				<!-- END Breadcrumb -->


				<!-- BEGIN Tiles -->
				<div class="row">

					<div class="col-md-12">
						<div class="row">

							<%-- 	<c:forEach items="${orderCounts}"  var ="orderCounts"> --%>
							<!-- <a href="resoucres/index.php/orders/list_all"> -->
							
							<div class="col-md-4">
								<div class="multi_bx blue_bg">
									<h2 class="multi_title blue_head"><c:out value="Sales"></c:out></h2>
									<div class="sale_one">
										<div class="sale_one_l" id="daysaletext"> <c:out value="Day Sales On ${dispDate}"></c:out> </div>
										<div class="sale_one_r" id="daysalevalue"> <span><i class="fa fa-rupee"></i></span>  <c:out value="${daySale}"></c:out></div>
									</div>
									
									<div class="sale_one">
										<div class="sale_one_l" id="monthsaletext"> <c:out value="Month to date sales as on ${dispDate}"></c:out></div>
										<div class="sale_one_r" id="monthsalevalue"> <span><i class="fa fa-rupee"></i></span> <c:out value="${monthSale}"></c:out></div>
									</div>
									
									<div class="sale_one">
										<div class="sale_one_l" id="yearsaletext"> <c:out value="Year to month sales as on ${dispDate}"></c:out></div>
										<div class="sale_one_r" id="yearsalevalue"> <span><i class="fa fa-rupee"></i></span> <c:out value="${yearSale}"></c:out></div>
									</div>
									
									
								</div>
							</div>
							
							<div class="col-md-4">
								<div class="multi_bx pink_bg">
									<h2 class="multi_title pink_head"><c:out value="GRN"></c:out></h2>
									<div class="sale_one">
										<div class="sale_one_l" id="daygrntext"> <c:out value="Day GRN On ${dispDate}"></c:out> </div>
										<div class="sale_one_r" id="daygrnvalue"> <span><i class="fa fa-rupee"></i></span>  <c:out value="${dayGrn}"></c:out></div>
									</div>
									
									<div class="sale_one">
										<div class="sale_one_l" id="monthgrntext"> <c:out value="Month to date GRN as on ${dispDate}"></c:out></div>
										<div class="sale_one_r" id="monthgrnvalue"> <span><i class="fa fa-rupee"></i></span> <c:out value="${dayGrn}"></c:out></div>
									</div>
									
									<div class="sale_one">
										<div class="sale_one_l" id="yeargrntext"> <c:out value="Year to month GRN as on ${dispDate}"></c:out></div>
										<div class="sale_one_r" id="yeargrnvalue"> <span><i class="fa fa-rupee"></i></span> <c:out value="${yearGrn}"></c:out></div>
									</div>
									
									
									
								</div>
							</div>
								
							
		<div class="col-md-4">
			<div class="multi_bx sky_bg">
				<h2 class="multi_title sky_head"><c:out value="GVN"></c:out></h2>
				<div class="sale_one">
					<div class="sale_one_l" id="daygvntext"> <c:out value="Day GVN On ${dispDate}"></c:out> </div>
					<div class="sale_one_r" id="daygvnvalue"> <span><i class="fa fa-rupee"></i></span>  <c:out value="${dayGvn}"></c:out></div>
				</div>
				
				<div class="sale_one">
					<div class="sale_one_l" id="monthgvntext"> <c:out value="Month to Date GVN as on ${dispDate}"></c:out></div>
					<div class="sale_one_r" id="monthgvnvalue"> <span><i class="fa fa-rupee"></i></span> <c:out value="${monthGvn}"></c:out></div>
				</div>				
				
				<div class="sale_one">
					<div class="sale_one_l" id="yeargvntext"> <c:out value="Year to Month GVN as on ${dispDate}"></c:out></div>
					<div class="sale_one_r" id="yeargvnvalue"> <span><i class="fa fa-rupee"></i></span> <c:out value="${yearGvn}"></c:out></div>
				</div>
				
				
				
			</div>
		</div>
							
							
							
							
							
							<%-- <div class="col-md-4">
								<div class="tile tile-light-pink"
									style="height: auto; border: 5px solid #decfbc; border-radius: 25px; 
									background: linear-gradient(-45deg, #ffec61, #f321d7);">
									<!-- <div class="img">
										<i class="fa fa-rupee"></i>
									</div> -->
									<div class="content1">
										<p class="title"
											style="font-size: 15px; background-color: #e79938; text-align: center;">
											<c:out value="Sales"></c:out>
										</p>

										<p class="title"
											style="font-size: 15px; background-color: #374390;"
											id="daysaletext">
											<c:out value="Day Sales On ${dispDate}"></c:out>
										</p>

										<p class="title" style="text-align: center; font-size: 18px;"
											id="daysalevalue">
											<i class="fa fa-rupee"></i>&nbsp;&nbsp;
											<c:out value="${daySale}"></c:out>
										</p>
										<br>
										<p class="title"
											style="font-size: 15px; background-color: #374390;"
											id="monthsaletext">
											<c:out value="Month to date sales as on ${dispDate}"></c:out>
										</p>

										<p class="title" style="text-align: center; font-size: 18px;"
											id="monthsalevalue">
											<i class="fa fa-rupee"></i>&nbsp;&nbsp;
											<c:out value="${monthSale}"></c:out>
										</p>
										<br>
										<p class="title"
											style="font-size: 15px; background-color: #374390;"
											id="yearsaletext">
											<c:out value="Year to month sales as on ${dispDate}"></c:out>
										</p>

										<p class="title" style="text-align: center; font-size: 18px;"
											id="yearsalevalue">
											<i class="fa fa-rupee"></i>&nbsp;&nbsp;
											<c:out value="${yearSale}"></c:out>
										</p>
									</div>
								</div>

							</div> --%>
							
							<%-- <div class="col-md-4">
								<div class="tile tile-light-pink"
									style="height: auto; border: 5px solid #decfbc; border-radius: 25px; background: linear-gradient(-45deg, #ffec61, #f321d7);">
									<!-- <div class="img">
										<i class="fa fa-rupee"></i>
									</div> -->
									<div class="content1">
										<p class="title"
											style="font-size: 15px; background-color: #e79938; text-align: center;">
											<c:out value="GRN"></c:out>
										</p>

										<p class="title"
											style="font-size: 15px; background-color: #374390;"
											id="daygrntext">
											<c:out value="Day GRN On ${dispDate}"></c:out>
										</p>

										<p class="title" style="text-align: center; font-size: 18px;"
											id="daygrnvalue">
											<i class="fa fa-rupee"></i>&nbsp;&nbsp;
											<c:out value="${dayGrn}"></c:out>
										</p>
										<br>
										<p class="title"
											style="font-size: 15px; background-color: #374390;"
											id="monthgrntext">
											<c:out value="Month to date GRN as on ${dispDate}"></c:out>
										</p>

										<p class="title" style="text-align: center; font-size: 18px;"
											id="monthgrnvalue">
											<i class="fa fa-rupee"></i>&nbsp;&nbsp;
											<c:out value="${monthGrn}"></c:out>
										</p>
										<br>
										<p class="title"
											style="font-size: 15px; background-color: #374390;"
											id="yeargrntext">
											<c:out value="Year to month GRN as on ${dispDate}"></c:out>
										</p>

										<p class="title" style="text-align: center; font-size: 18px;"
											id="yeargrnvalue">
											<i class="fa fa-rupee"></i>&nbsp;&nbsp;
											<c:out value="${yearGrn}"></c:out>
										</p>
									</div>
								</div>

							</div> --%>
							
							<%-- <div class="col-md-4">
								<div class="tile tile-light-pink"
									style="height: auto; border: 5px solid #decfbc; border-radius: 25px; background: linear-gradient(-45deg, #ffec61, #f321d7);">

									<div class="content1">
										<p class="title"
											style="font-size: 15px; background-color: #e79938; text-align: center;">
											<c:out value="GVN"></c:out>
										</p>
										<p class="title"
											style="font-size: 15px; background-color: #374390;"
											id="daygvntext">
											<c:out value="Day GVN On ${dispDate}"></c:out>
										</p>

										<p class="title" style="text-align: center; font-size: 18px;"
											id="daygvnvalue">
											<i class="fa fa-rupee"></i>&nbsp;&nbsp;
											<c:out value="${dayGvn}"></c:out>
										</p>
										<br>
										<p class="title"
											style="font-size: 15px; background-color: #374390;"
											id="monthgvntext">
											<c:out value="Month to Date GVN as on ${dispDate}"></c:out>
										</p>

										<p class="title" style="text-align: center; font-size: 18px;"
											id="monthgvnvalue">
											<i class="fa fa-rupee"></i>&nbsp;&nbsp;
											<c:out value="${monthGvn}"></c:out>
										</p>
										<br>
										<p class="title"
											style="font-size: 15px; background-color: #374390;"
											id="yeargvntext">
											<c:out value="Year to Month GVN as on ${dispDate}"></c:out>
										</p>

										<p class="title" style="text-align: center; font-size: 18px;"
											id="yeargvnvalue">
											<i class="fa fa-rupee"></i>&nbsp;&nbsp;
											<c:out value="${yearGvn}"></c:out>
										</p>
									</div>
								</div>

							</div> --%>




						</div>


					</div>

				</div>


				<div id="breadcrumbs">
					<ul class="breadcrumb">
						<li class="active" style="width: 800px;">
							<!-- Month Sales --> <label
							class="col-sm-3 col-lg-2 control-label" style="padding-top: 4px;">Month
								Sales</label>
							<div class="col-sm-5 col-lg-4 controls">
								<select id="year" name="year" class="form-control">

									<option value="2019-2020">2019-2020</option>
									<option value="2020-2021">2020-2021</option>
								</select>
							</div>
							<div class="col-sm-5 col-lg-3 controls">
								<button type="submit" class="btn btn-primary" id="search"
									name="search">Search</button>

								<button type="submit" class="btn btn-primary" id="graph"
									name="graph" onclick="searchYearlyData()">Graph</button>

							</div>
							<div class="col-sm-2">
								<button class="buttonload" id="loader1">
									<i class="fa fa-spinner fa-spin"></i>Loading
								</button>
							</div>


						</li>
					</ul>
				</div>

				<c:if test="${showGraph==0}">
					<div class="row">

						<div class="col-md-12">
							<div class="row" id="yearDiv">


								<c:forEach var="data" items="${data}">

									<c:if test="${data.net>0}">

										<div class="col-md-4">
											<div class="multi_bx pink_bg">												
													<p class="multi_title pink_head">
														<c:out value="${data.monthStr}"></c:out>
													</p>
													
													<div class="row_one">
														<div class="row_one_l"><c:out value="Sales "></c:out></div>
														<div class="row_one_r">
														<span><i class="fa fa-rupee"></i></span>
														<fmt:formatNumber type="number" minFractionDigits="2"
															maxFractionDigits="2" value="${data.sale}" /></div>
															<div class="clr"></div>
													</div>
													
													<div class="small_title">
														<c:out value="GRN "></c:out>
													</div>	
													
			<div class="grn_row">
				<div class="grn_one">
					<div class="grn_one_l">Grn1 :</div>
					<div class="grn_one_r"><i class="fa fa-rupee"></i>
					<fmt:formatNumber type="number" minFractionDigits="2"
						maxFractionDigits="2" value="${data.grn1}" /></div>
					<div class="clr"></div>
				</div>
				
				<div class="grn_one">
					<div class="grn_one_l">Grn2 :</div>
					<div class="grn_one_r"><i class="fa fa-rupee"></i>
					<fmt:formatNumber type="number" minFractionDigits="2"
					maxFractionDigits="2" value="${data.grn2}" /></div>
					<div class="clr"></div>
				</div>
				
				<div class="grn_one">
					<div class="grn_one_l">Grn3 :</div>
					<div class="grn_one_r"><i class="fa fa-rupee"></i>
					<fmt:formatNumber type="number" minFractionDigits="2"
					maxFractionDigits="2" value="${data.grn3}" /></span></div>
					<div class="clr"></div>
				</div>
			</div>
			
			<div class="total_gvn">
				<div class="grn_one_l">Total GRN:</div>
				<div class="grn_one_r"><i class="fa fa-rupee"></i>
				<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2"
				value="${data.grnTotal}" /></div>
				<div class="clr"></div>
			</div>
			
			<div class="row_one" style="padding-top:10px; border-top:1px dashed #FFF;">
				<div class="row_one_l"><c:out value="GVN "></c:out> </div>
				<div class="row_one_r">
				<i class="fa fa-rupee"></i>
				<fmt:formatNumber type="number" minFractionDigits="2"
				maxFractionDigits="2" value="${data.gvn}" /></div>
				<div class="clr"></div>
			</div>
			
			<div class="row_one" style="font-size: 20px; border:none; margin-bottom: 0; padding-bottom: 0;">
				<div class="row_one_l"><c:out value="Net Sale "></c:out>  </div>
				<div class="row_one_r">
					<i class="fa fa-rupee"></i>
					<fmt:formatNumber type="number" minFractionDigits="2"
					maxFractionDigits="2" value="${data.net}" /></div>
				<div class="clr"></div>
			</div>
													
										
											</div>

										</div>

									</c:if>

								</c:forEach>





								<%-- <c:forEach var="entry" items="${months}">
							<c:forEach var="monthData" items="${monthDataList}">
								<c:choose>
									<c:when test="${monthData.month==entry.key}">
										<div class="col-md-4">
											<div class="tile tile-light-pink"
												style="height: auto; border: 5px solid #decfbc;">

												<div class="content1">
													<p class="title"
														style="font-size: 15px; background-color: #e79938; text-align: center;">
														<c:out value="${entry.value} ${monthData.year}"></c:out>
													</p>
													<p class="title"
														style="font-size: 15px; background-color: #374390;">
														<c:out value="Sales "></c:out>
													</p>

													<p class="title"
														style="text-align: right; font-size: 18px;">
														<i class="fa fa-rupee"></i>&nbsp;&nbsp;
														<c:out value="${monthData.salesAmt}"></c:out>
														&nbsp;&nbsp;&nbsp;&nbsp;
													</p>

													<p class="title"
														style="font-size: 15px; background-color: #374390;">
														<c:out value="GRN "></c:out>
													</p>

													<p class="title" style="text-align: left; font-size: 14px;">
														&nbsp;&nbsp;Grn1: <i class="fa fa-rupee"></i>&nbsp;&nbsp;<span
															style="float: right; font-weight: bold;"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2" value="${monthData.grnAmt1}" />&nbsp;&nbsp;&nbsp;&nbsp;</span>
														<br> &nbsp;&nbsp;Grn2: <i class="fa fa-rupee"></i>&nbsp;&nbsp;<span
															style="float: right; font-weight: bold;"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2" value="${monthData.grnAmt2}" />&nbsp;&nbsp;&nbsp;&nbsp;</span>
														<br> &nbsp;&nbsp;Grn3: <i class="fa fa-rupee"></i>&nbsp;&nbsp;<span
															style="float: right; font-weight: bold;"><fmt:formatNumber
																type="number" minFractionDigits="2"
																maxFractionDigits="2" value="${monthData.grnAmt3}" />&nbsp;&nbsp;&nbsp;&nbsp;</span>
													<hr>
													&nbsp;&nbsp;Total GRN: <i class="fa fa-rupee"></i>&nbsp;&nbsp;<span
														style="float: right; font-weight: bold; font-size: 17px;"><fmt:formatNumber
															type="number" minFractionDigits="2" maxFractionDigits="2"
															value="${monthData.grnTotalAmt}" />&nbsp;&nbsp;&nbsp;&nbsp;</span>

													</p>
													<p class="title"
														style="font-size: 15px; background-color: #374390;">
														<c:out value="GVN "></c:out>
													</p>

													<p class="title"
														style="text-align: right; font-size: 18px;">
														<i class="fa fa-rupee"></i>&nbsp;&nbsp;
														<fmt:formatNumber type="number" minFractionDigits="2"
															maxFractionDigits="2" value="${monthData.gvnAmt}" />
														&nbsp;&nbsp;&nbsp;&nbsp;
													</p>

													<p class="title"
														style="font-size: 15px; background-color: #374390;">
														<c:out value="Net Sale "></c:out>
													</p>

													<p class="title"
														style="text-align: right; font-size: 18px;">
														<i class="fa fa-rupee"></i>&nbsp;&nbsp;<b><c:out
																value="${monthData.netSaleAmt}"></c:out></b>&nbsp;&nbsp;&nbsp;&nbsp;
													</p>

												</div>
											</div>

										</div>
									</c:when>
								</c:choose>
							</c:forEach>
						</c:forEach> --%>

							</div>
						</div>
					</div>
				</c:if>

				<c:if test="${showGraph==1}">
					<div class="row">

						<div class="col-md-12">
							<div id="line_chart_div"></div>
						</div>

					</div>
				</c:if>

			</form>


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
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>

	<!--flaty scripts-->
	<script src="${pageContext.request.contextPath}/resources/js/flaty.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/flaty-demo-codes.js"></script>
	<script type="text/javascript">
		function searchData() {

			$('#loader').show();
			document.getElementById('daysalevalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;' + 0;
			document.getElementById('daygrnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;' + 0;
			document.getElementById('daygvnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;' + 0;
			document.getElementById('monthsalevalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;' + 0;
			document.getElementById('monthgrnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;' + 0;
			document.getElementById('monthgvnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;' + 0;
			document.getElementById('yearsalevalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;' + 0;
			document.getElementById('yeargrnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;' + 0;
			document.getElementById('yeargvnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;' + 0;

			var date = $("#date").val();
			$
					.getJSON(
							'${getDashboardData}',
							{
								date : date,
								ajax : 'true'

							},
							function(data) {
								$('#loader').hide();

								if (data == "") {
									alert("No records found !!");

								}
								document.getElementById('daysaletext').innerHTML = 'Day Sales On '
										+ date;
								document.getElementById('daygrntext').innerHTML = 'Day GRN On '
										+ date;
								document.getElementById('daygvntext').innerHTML = 'Day GVN On '
										+ date;
								document.getElementById('monthsaletext').innerHTML = ' Month to date Sales as on '
										+ date;
								document.getElementById('monthgrntext').innerHTML = ' Month to date GRN as on '
										+ date;
								document.getElementById('monthgvntext').innerHTML = ' Month to date GVN as on '
										+ date;

								document.getElementById('yearsaletext').innerHTML = ' Year to month Sales as on '
										+ date;
								document.getElementById('yeargrntext').innerHTML = ' Year to month GRN as on '
										+ date;
								document.getElementById('yeargvntext').innerHTML = ' Year to month GVN as on '
										+ date;
								if (data.daySale != null) {
									document.getElementById('daysalevalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;'
											+ data.daySale.grandTotal;
								}
								if (data.dayGrn != null) {
									document.getElementById('daygrnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;'
											+ data.dayGrn.crnGrandTotal;
								}
								if (data.dayGvn != null) {
									document.getElementById('daygvnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;'
											+ data.dayGvn.crnGrandTotal;
								}
								if (data.monthSale != null) {
									document.getElementById('monthsalevalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;'
											+ data.monthSale.grandTotal;
								}
								if (data.monthGrn != null) {
									document.getElementById('monthgrnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;'
											+ data.monthGrn.crnGrandTotal;
								}
								if (data.monthGvn != null) {
									document.getElementById('monthgvnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;'
											+ data.monthGvn.crnGrandTotal;
								}
								if (data.yearSale != null) {
									document.getElementById('yearsalevalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;'
											+ data.yearSale.grandTotal;
								}
								if (data.yearGrn != null) {
									document.getElementById('yeargrnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;'
											+ data.yearGrn.crnGrandTotal;
								}
								if (data.yearGvn != null) {
									document.getElementById('yeargvnvalue').innerHTML = ' <i class="fa fa-rupee"></i>&nbsp;&nbsp;'
											+ data.yearGvn.crnGrandTotal;
								}
							});
		}
	</script>


	<script type="text/javascript">
		function drawGraph() {

			//alert("hi - ");

			google.charts.load('current', {
				'packages' : [ 'corechart', 'bar' ]
			});
			google.charts.setOnLoadCallback(drawStuffCat);

			var type = $
			{
				type
			}
			;

		}
	</script>


	<script type="text/javascript">
		function drawStuffCat() {

			var chartDiv = document.getElementById('line_chart_div');

			var dataTable = new google.visualization.DataTable();

			dataTable.addColumn('string', 'Month'); // Implicit domain column.
			//dataTable.addColumn('number', 'Sale '); // Implicit data column. 
			dataTable.addColumn('number', 'GRN-GVN ');
			//dataTable.addColumn('number', 'GVN ');
			dataTable.addColumn('number', 'Net ');

			//alert("in");

			$.getJSON('${getDashYearlyDataForGraph}',

			{
				ajax : 'true'

			}, function(chartsBardata) {

				//alert(JSON.stringify(chartsBardata));
				var len = chartsBardata.length;
				//alert("LEN - " + len);

				$.each(chartsBardata, function(key, chartsBardata) {
					
					var grngvn=parseInt(chartsBardata.grnTotal)+parseInt(chartsBardata.gvn);

					dataTable.addRows([ [ chartsBardata.monthStr,
							//parseInt(chartsBardata.sale),
							parseInt(grngvn),
							parseInt(chartsBardata.net) ] ]);

				});

				//alert(11);

				var materialOptions = {
					width : 1150,
					height : 500,
					//isStacked: 'percent',
					bar: { groupWidth: '45%' },
					isStacked:true,
					chart : {
						title : ' ',
						subtitle : ' '
					},
					series : {
						0 : {
							axis : 'distance'
						}
					// Bind series 1 to an axis named 'brightness'.
					},
					axes : {
						y : {
							distance : {
								label : 'Amount'
							}
						// Left y-axis.

						}
					}
				};

				var materialChart = new google.visualization.ColumnChart(chartDiv);

				function drawMaterialChart() {
					materialChart.draw(dataTable, google.charts.Bar
							.convertOptions(materialOptions));
				}

				drawMaterialChart();

			});

		}
	</script>

	<!-- CHARTS -->

	<script type="text/javascript"
		src="https://www.gstatic.com/charts/loader.js"></script>

	<!-- ------ -->

</body>
</html>
