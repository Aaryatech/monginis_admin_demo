<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>

	<c:url var="getVehicleAvg" value="/getVehicleAvgBetDate"></c:url>
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
							<div class="col-sm-3 col-lg-3 controls date_select">
								<input class="form-control date-picker" id="fromDate"
									name="fromDate" size="30" type="text" value="${todaysDate}" />
							</div>

							<!-- </div>

					<div class="form-group  "> -->

							<label class="col-sm-3 col-lg-2	 control-label">To Date</label>
							<div class="col-sm-3 col-lg-3 controls date_select">
								<input class="form-control date-picker" id="toDate"
									name="toDate" size="30" type="text" value="${todaysDate}" />
							</div>

							<div class="col-md-2">
								<button class="btn btn-info" onclick="searchReport()">Search</button>
								<button class="btn btn-primary" value="PDF" id="PDFButton"
									onclick="genPdf()" disabled>PDF</button>
							</div>
						</div>

					</div>


					<div class="row"></div>


					<div align="center" id="loader" style="display: none">

						<span>
							<h4>
								<font color="#343690">Loading</font>
							</h4>
						</span> <span class="l-1"></span> <span class="l-2"></span> <span
							class="l-3"></span> <span class="l-4"></span> <span class="l-5"></span>
						<span class="l-6"></span>
					</div>

				</div>
			</div>


			<div class="box">
				<div class="box-title">
					<h3>
						<i class="fa fa-list-alt"></i>Vehicle Avg. Report
					</h3>

				</div>


				<div class=" box-content">
					<div class="row">
						<div class="col-md-12 table-responsive">
							<table class="w3-table w3-bordered" style="width: 100%"
								id="table_grid">
								<thead>
									<tr bgcolor=#5ab4da style="color: white;">
										<th>Sr.No.</th>
										<th>Vehicle No</th>

										<th>Running Kms.</th>
										<th>Diesel</th>
										<th>Actual Avg.</th>
										<th>Action</th>


									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
						<div class="form-group" style="display: none;" id="range">



							<div class="col-sm-3  controls">
								<input type="button" id="expExcel" class="btn btn-primary"
									value="EXPORT TO Excel" onclick="exportToExcel();"
									disabled="disabled">
							</div>
						</div>
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
		function searchReport() {
			//	var isValid = validate();

			var from_date = $("#fromDate").val();
			var to_date = $("#toDate").val();

			$('#loader').show();

			$.getJSON('${getVehicleAvg}',

			{
				fromDate : from_date,
				toDate : to_date,
				ajax : 'true'

			}, function(data) {

				var taxableTotal = 0;
				var taxTotal = 0;
				var finalTotal = 0;

				$('#table_grid td').remove();
				$('#loader').hide();
				document.getElementById("PDFButton").disabled = false;
				document.getElementById("expExcel").disabled = false;
				if (data == "") {
					alert("No records found !!");
					document.getElementById("expExcel").disabled = true;
					document.getElementById("PDFButton").disabled = true;
				}

				$.each(data, function(key, report) {
					document.getElementById('range').style.display = 'block';
					var color = 0;
					var actualAvg = parseFloat(report.vehRunningKm
							/ report.diesel);
					var minAvg = parseInt(report.vehMiniAvg);
					if (actualAvg < minAvg) {
						color = 1;
					}
					var index = key + 1;
					if (color == 1) {
						var tr = $('<tr bgcolor=#f77676></tr>');
					} else {
						var tr = $('<tr></tr>');
					}
					tr.append($('<td></td>').html(key + 1));

					tr.append($('<td></td>').html(report.vehNo));

					tr.append($('<td style="text-align:right;"></td>').html(
							report.vehRunningKm));

					tr.append($('<td style="text-align:right;"></td>').html(
							report.diesel));
					if (actualAvg > 0) {
						tr.append($('<td style="text-align:right;"></td>')
								.html(actualAvg.toFixed(2)));
					} else {
						tr.append($('<td style="text-align:right;"></td>')
								.html(0));

					}

					tr.append($('<td  style="text-align:center;"></td>').html(
							' <a>   <span class="fa fa-list" id="edit'
									+ report.vehId + '" onClick=detailList('
									+ report.vehId + ');> </span> </a> '));

					taxableTotal = taxableTotal + report.vehRunningKm;
					taxTotal = taxTotal + report.diesel;

					$('#table_grid tbody').append(tr);

				})

				var tr = $('<tr></tr>');

				tr.append($('<td></td>').html(""));

				tr.append($('<td style="font-weight:bold;"></td>')
						.html("Total"));
				tr.append($('<td style="text-align:right;"></td>').html(
						taxableTotal.toFixed(2)));
				tr.append($('<td style="text-align:right;"></td>').html(
						taxTotal.toFixed(2)));

				tr.append($('<td></td>').html(""));

				$('#table_grid tbody').append(tr);

			});

		}
	</script>

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
					.open('${pageContext.request.contextPath}/getVehAvgBetDateReportPdf');
		}
	</script>
	<script type="text/javascript">
		function exportToExcel() {

			window.open("${pageContext.request.contextPath}/exportToExcel");
			document.getElementById("expExcel").disabled = true;
		}
	</script>

	<script type="text/javascript">
		function detailList(vehId) {
			var fromdate = $("#fromDate").val();
			var todate = $("#toDate").val();
			window
					.open('${pageContext.request.contextPath}/showVehAvgDetailBetDate/'
							+ vehId + '/' + fromdate + '/' + todate + '/');

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