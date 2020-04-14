<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>

	<c:url var="getVehicleAvg" value="/getVehicleAvg"></c:url>
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
						<i class="fa fa-file-o"></i>Report
					</h1>
					<h4></h4>
				</div>
			</div>
			<!-- END Page Title -->

			<!-- BEGIN Breadcrumb -->
			<div id="breadcrumbs">
				<ul class="breadcrumb">
					<li><i class="fa fa-home"></i> <a
						href="${pageContext.request.contextPath}/home">Home</a> <span
						class="divider"><i class="fa fa-angle-right"></i></span></li>
					<li class="active">Bill Report</li>
				</ul>
			</div>
			<!-- END Breadcrumb -->

			<!-- BEGIN Main Content -->
			<div class="box">
				<div class="box-title">
					<h3>
						<i class="fa fa-bars"></i>Vehicle Avg. Report
					</h3>

				</div>


				<div class="box-content">

					<div class="row">


						<div class="form-group">
							<label class="col-sm-3 col-lg-2	 control-label">From Date</label>
							<div class="col-sm-6 col-lg-4 controls date_select">
								<input class="form-control date-picker" id="fromDate" readonly
									name="fromDate" size="30" type="text" value="${fromDate}" />
							</div>

							<!-- </div>

					<div class="form-group  "> -->

							<label class="col-sm-3 col-lg-2	 control-label">To Date</label>
							<div class="col-sm-6 col-lg-4 controls date_select">
								<input class="form-control date-picker" id="toDate" readonly
									name="toDate" size="30" type="text" value="${toDate}" />
							</div>
						</div>

					</div>


				</div>
			</div>


			<div class="box">


				<div class=" box-content">
					<div class="row">
						<div class="col-md-12 table-responsive">
							<table class="w3-table w3-bordered" style="width: 100%"
								id="table_grid">
								<thead>
									<tr bgcolor=#5ab4da style="color: white;">
										<th>Sr.No.</th>
										<th>Vehicle Or Machine Name</th>
										<th>Invoice No.</th>

										<th>Taxable Amt</th>
										<th>Tax Amt</th>
										<th>Total</th>
										<th>Serv Type</th>



									</tr>


								</thead>
								<tbody>
									<c:set var="taxableTotal" value="0" />
									<c:set var="taxTotal" value="0" />
									<c:set var="finalTotal" value="0" />
									<%
										int c = 1;
									%>
									<c:forEach items="${logisList}" var="logisList">



										<tr>
											<td><%=c++%> <c:out value="${c}" /></td>
											<td align="left"><c:out value="${logisList.vehNo}" /></td>
											<td align="left"><c:out value="${logisList.billNo}" /></td>
											<td align="right"><c:out value="${logisList.taxableAmt}" /></td>
											<td align="right"><c:out value="${logisList.taxAmt}" /></td>
											<td align="right"><c:out value="${logisList.total}" /></td>
											<c:set var="ServType"></c:set>
											<c:choose>
												<c:when test="${logisList.servType==1}">
													<c:set var="ServType" value="Regular"></c:set>
												</c:when>
												<c:when test="${logisList.servType==2}">
													<c:set var="ServType" value="Breakdown"></c:set>
												</c:when>
											</c:choose>
											<td align="right"><c:out value="${ServType}" /></td>


											<c:set var="taxableTotal"
												value="${taxableTotal+logisList.taxableAmt}" />
											<c:set var="taxTotal" value="${taxTotal+logisList.taxAmt}" />
											<c:set var="finalTotal" value="${finalTotal+logisList.total}" />
										</tr>
									</c:forEach>

									<tr>
										<td align="left"></td>
										<td align="left"></td>
										<td align="left">Total</td>
										<td align="right"><fmt:formatNumber type="number"
												maxFractionDigits="2" minFractionDigits="2"
												value="${taxableTotal}" /></td>
										<td align="right"><fmt:formatNumber type="number"
												maxFractionDigits="2" minFractionDigits="2"
												value="${taxTotal}" /></td>

										<td align="right"><fmt:formatNumber type="number"
												maxFractionDigits="2" minFractionDigits="2"
												value="${finalTotal}" /></td>

										<td align="left"></td>



									</tr>

								</tbody>
							</table>
						</div>



					</div>

					<div class="col-sm-3  controls">
						<input type="button" id="expExcel" class="btn btn-primary"
							value="EXPORT TO Excel" onclick="exportToExcel();">

						<button class="btn btn-primary" value="PDF" id="PDFButton"
							onclick="genPdf()">PDF</button>

					</div>

				</div>
			</div>

			<!-- END Main Content -->

			<footer>
				<p>2018 © Monginis.</p>
			</footer>

			<a id="btn-scrollup" class="btn btn-circle btn-lg" href="#"><i
				class="fa fa-chevron-up"></i></a>
		</div>
	</div>


	<script type="text/javascript">
		function validate() {
			var date = $("#date").val();
			var isValid = true;

			if (date == "" || date == null) {

				alert("Please Select Date");
				isValid = false;
			}
			return isValid;
		}
	</script>


	<script>
		function genPdf() {
			window
					.open('${pageContext.request.contextPath}/getLogisticsDetailReportPdf');
		}
	</script>
	<script type="text/javascript">
		function exportToExcel() {

			window.open("${pageContext.request.contextPath}/exportToExcel");
			document.getElementById("expExcel").disabled = true;
		}
	</script>

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
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/chosen-bootstrap/chosen.jquery.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-inputmask/bootstrap-inputmask.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/jquery-tags-input/jquery.tagsinput.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/jquery-pwstrength/jquery.pwstrength.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-fileupload/bootstrap-fileupload.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-duallistbox/duallistbox/bootstrap-duallistbox.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/dropzone/downloads/dropzone.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-timepicker/js/bootstrap-timepicker.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/clockface/js/clockface.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-colorpicker/js/bootstrap-colorpicker.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-daterangepicker/date.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-daterangepicker/daterangepicker.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-switch/static/js/bootstrap-switch.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-wysihtml5/wysihtml5-0.3.0.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/bootstrap-wysihtml5/bootstrap-wysihtml5.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/ckeditor/ckeditor.js"></script>

	<!--flaty scripts-->
	<script src="${pageContext.request.contextPath}/resources/js/flaty.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/flaty-demo-codes.js"></script>
</body>
</html>