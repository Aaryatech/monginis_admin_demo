<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
	<c:url var="getFrListByRouteId" value="/getFrListByRouteId"></c:url>

	<c:url var="serchTrayDetailReportList"
		value="/serchTrayDetailReportList"></c:url>
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
						<i class="fa fa-bars"></i>Tray Report
					</h3>

				</div>
				<div class="box-content">

					<div class="row">


						<label class="col-sm-1 col-lg-2 control-label"><b>Select
								Route</b> </label>
						<div class="col-sm- col-lg-2">

							<select data-placeholder="Choose Route"
								class="form-control chosen" tabindex="6" id="routeId"
								onchange="onRouteChange(this.value)" name="routeId">
									<option value="">Select Route</option>
								<c:forEach items="${routeMgmtList}" var="route"
									varStatus="count">
									<option value="${route.routeTrayId}"><c:out
											value="${route.routeName}" /></option>
								</c:forEach>
							</select>

						</div>


						<label class="col-sm-1 col-lg-2 control-label"><b>Select
								Franchisee</b> </label>
						<div class="col-sm- col-lg-2">

							<select data-placeholder="Choose Franchisee"
								class="form-control chosen" multiple="multiple" tabindex="6"
								id="frIdList" name="frIdList">



								<%-- <c:forEach items="${unSelectedFrList}" var="fr"
									varStatus="count">
									<option value="${fr.frId}"><c:out value="${fr.frName}" /></option>
								</c:forEach> --%>
							</select>

						</div>

						<label class="col-sm-2 col-lg-2	 control-label">Select</label>
						<div class="col-sm-2 col-lg-2 controls">
							<select name="trayType" id="trayType" class="form-control chosen"
								tabindex="6" onchange="selectTable111()">

								<option value="-1">All</option>
								<option value="1">Out Tray</option>
								<option value="2">In Tray</option>
								<option value="3">Balance Tray</option>
							</select>
						</div>

					</div>
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


				<div class=" box-content" id="allTable">
					<div class="row">
						<div class="col-md-12 table-responsive">
							<table class="w3-table w3-bordered" style="width: 100%"
								id="table_grid">
								<thead>
									<tr bgcolor=#5ab4da style="color: white;">
										<th>Sr.No.</th>
										<th>Fr Name</th>
										<th>Out Tray</th>
										<th>Out Boxes</th>
										<th>Out Lead</th>

										<th>In Tray</th>
										<th>In Boxes</th>
										<th>In Lead</th>

										<th>Balance Tray</th>
										<th>Balance Boxes</th>
										<th>Balance Lead</th>

									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>

						<div class="form-group">
							<div class="col-sm-3  controls">
								<input type="button"
									onclick="tableToExcel('table_grid', 'name', 'RoyaltySummaryReport.xls')"
									value="Export to Excel">

							</div>
						</div>
					</div>

				</div>

				<div id="outTrayTable" style="display: none;">
					<div class="row">
						<div class="col-md-12 table-responsive">
							<table class="w3-table w3-bordered" style="width: 100%"
								id="table_grid1">
								<thead>
									<tr bgcolor=#5ab4da style="color: white;">
										<th>Sr.No.</th>
										<th>Fr Name</th>
										<th>Out Tray</th>
										<th>Out Boxes</th>
										<th>Out Lead</th>

									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
						<div class="form-group">
							<div class="col-sm-3  controls">
								<input type="button"
									onclick="tableToExcel('table_grid1', 'name', 'RoyaltySummaryReport.xls')"
									value="Export to Excel">

							</div>
						</div>
					</div>

				</div>

				<div id="inTrayTable" style="display: none;">
					<div class="row">
						<div class="col-md-12 table-responsive">
							<table class="w3-table w3-bordered" style="width: 100%"
								id="table_grid2">
								<thead>
									<tr style="color: white;">
										<th>Sr.No.</th>
										<th>Fr Name</th>
										<th>In Tray</th>
										<th>In Boxes</th>
										<th>In Lead</th>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
						<div class="form-group">
							<div class="col-sm-3  controls">
								<input type="button"
									onclick="tableToExcel('table_grid2', 'name', 'RoyaltySummaryReport.xls')"
									value="Export to Excel">

							</div>
						</div>
					</div>

				</div>

				<div id="balanceTrayTable" style="display: none;">
					<div class="row">
						<div class="col-md-12 table-responsive">
							<table class="w3-table w3-bordered" style="width: 100%"
								id="table_grid3">
								<thead>
									<tr style="color: white;">
										<th>Sr.No.</th>
										<th>Fr Name</th>
										<th>Balance Tray</th>
										<th>Balance Boxes</th>
										<th>Balance Lead</th>

									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
						<div class="form-group">
							<div class="col-sm-3  controls">
								<input type="button"
									onclick="tableToExcel('table_grid3', 'name', 'RoyaltySummaryReport.xls')"
									value="Export to Excel">

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

			//

			var from_date = $("#fromDate").val();
			var to_date = $("#toDate").val();
			var trayType = document.getElementById("trayType").value;
			var frIdList = $('#frIdList').val();

			$('#loader').show();

			$.getJSON('${serchTrayDetailReportList}',

			{
				fromDate : from_date,
				toDate : to_date,
				trayType : trayType,
				frIdList : JSON.stringify(frIdList),
				ajax : 'true'

			}, function(data) {
				//alert(data);

				$('#table_grid td').remove();
				$('#table_grid1 td').remove();
				$('#table_grid2 td').remove();
				$('#table_grid3 td').remove();
				$('#loader').hide();
				document.getElementById("PDFButton").disabled = false;
				//document.getElementById("expExcel").disabled = false;
				if (data == "") {
					alert("No records found !!");
					//document.getElementById("expExcel").disabled = true;
					document.getElementById("PDFButton").disabled = true;
				}

				if (trayType == -1) {

					var outTraySmallTotal = 0;
					var outTrayBigTotal = 0;
					var outTrayLeadTotal = 0;

					var inTraySmallTotal = 0;
					var inTrayBigTotal = 0;
					var inTrayLeadTotal = 0;

					var balTraySmallTotal = 0;
					var balTrayBigTotal = 0;
					var balTrayLeadTotal = 0;

					$('#outTrayTable').hide();
					$('#balanceTrayTable').hide();
					$('#inTrayTable').hide();

					$.each(data, function(key, report) {

						var index = key + 1;
						var tr = $('<tr></tr>');
						tr.append($('<td></td>').html(index));

						tr.append($('<td ></td>').html(report.frName));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.outtraySmall));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.outtrayBig));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.outtrayLead));

						tr.append($('<td style="text-align:right;"></td>')
								.html(report.intraySmall));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.intrayBig));

						tr.append($('<td style="text-align:right;"></td>')
								.html(report.intrayLead));

						tr.append($('<td style="text-align:right;"></td>')
								.html(report.balanceSmall));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.balanceBig));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.balanceLead));

						outTraySmallTotal = outTraySmallTotal
								+ report.outtraySmall;
						outTrayBigTotal = outTrayBigTotal + report.outtrayBig;
						outTrayLeadTotal = outTrayLeadTotal
								+ report.outtrayLead;

						inTraySmallTotal = inTraySmallTotal
								+ report.intraySmall;
						inTrayBigTotal = inTrayBigTotal + report.intrayBig;
						inTrayLeadTotal = inTrayLeadTotal + report.intrayLead;

						balTraySmallTotal = balTraySmallTotal
								+ report.balanceSmall;
						balTrayBigTotal = balTrayBigTotal + report.balanceBig;
						balTrayLeadTotal = balTrayLeadTotal
								+ report.balanceLead;

						$('#table_grid tbody').append(tr);

					})

					var tr = $('<tr></tr>');

					tr.append($('<td></td>').html(""));

					tr.append($('<td style="font-weight:bold;"></td>').html(
							"Total"));
					tr.append($('<td style="text-align:right;"></td>').html(
							outTraySmallTotal.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							outTrayBigTotal.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							outTrayLeadTotal.toFixed(2)));

					tr.append($('<td style="text-align:right;"></td>').html(
							inTraySmallTotal.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							inTrayBigTotal.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							inTrayLeadTotal.toFixed(2)));

					tr.append($('<td style="text-align:right;"></td>').html(
							balTraySmallTotal.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							balTrayBigTotal.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							balTrayLeadTotal.toFixed(2)));

					$('#table_grid tbody').append(tr);

				} else if (trayType == 1) {

					var outTraySmallTotal = 0;
					var outTrayBigTotal = 0;
					var outTrayLeadTotal = 0;

					$('#outTrayTable').show();
					$('#balanceTrayTable').hide();
					$('#inTrayTable').hide();
					$('#allTable').hide();
					$.each(data, function(key, report) {

						var index = key + 1;
						var tr = $('<tr></tr>');
						tr.append($('<td></td>').html(index));

						tr.append($('<td></td>').html(report.frName));

						tr.append($('<td style="text-align:right;"></td>')
								.html(report.outtraySmall));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.outtrayBig));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.outtrayLead));

						outTraySmallTotal = outTraySmallTotal
								+ report.outtraySmall;
						outTrayBigTotal = outTrayBigTotal + report.outtrayBig;
						outTrayLeadTotal = outTrayLeadTotal
								+ report.outtrayLead;

						$('#table_grid1 tbody').append(tr);

					})

					var tr = $('<tr></tr>');

					tr.append($('<td></td>').html(""));

					tr.append($('<td style="font-weight:bold;"></td>').html(
							"Total"));
					tr.append($('<td style="text-align:right;"></td>').html(
							outTraySmallTotal.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							outTrayBigTotal.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							outTrayLeadTotal.toFixed(2)));

					$('#table_grid1 tbody').append(tr);

				}

				else if (trayType == 2) {

					var inTraySmallTotal = 0;
					var inTrayBigTotal = 0;
					var inTrayLeadTotal = 0;

					$('#outTrayTable').hide();
					$('#balanceTrayTable').hide();
					$('#inTrayTable').show();
					$('#allTable').hide();
					$.each(data, function(key, report) {

						var index = key + 1;
						var tr = $('<tr></tr>');
						tr.append($('<td></td>').html(index));

						tr.append($('<td></td>').html(report.frName));

						tr.append($('<td style="text-align:right;"></td>')
								.html(report.intraySmall));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.intrayBig));

						tr.append($('<td style="text-align:right;"></td>')
								.html(report.intrayLead));

						inTraySmallTotal = inTraySmallTotal
								+ report.intraySmall;
						inTrayBigTotal = inTrayBigTotal + report.intrayBig;
						inTrayLeadTotal = inTrayLeadTotal + report.intrayLead;

						$('#table_grid2 tbody').append(tr);

					})

					var tr = $('<tr></tr>');

					tr.append($('<td></td>').html(""));

					tr.append($('<td style="font-weight:bold;"></td>').html(
							"Total"));

					tr.append($('<td style="text-align:right;"></td>').html(
							inTraySmallTotal));
					tr.append($('<td style="text-align:right;"></td>').html(
							inTrayBigTotal));
					tr.append($('<td style="text-align:right;"></td>').html(
							inTrayLeadTotal));

					$('#table_grid2 tbody').append(tr);

				} else if (trayType == 3) {

					var balTraySmallTotal = 0;
					var balTrayBigTotal = 0;
					var balTrayLeadTotal = 0;

					$('#outTrayTable').hide();
					$('#balanceTrayTable').show();
					$('#inTrayTable').hide();
					$('#allTable').hide();
					$.each(data, function(key, report) {

						var index = key + 1;
						var tr = $('<tr></tr>');
						tr.append($('<td></td>').html(index));

						tr.append($('<td></td>').html(report.frName));

						tr.append($('<td style="text-align:right;"></td>')
								.html(report.balanceSmall));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.balanceBig));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.balanceLead));

						balTraySmallTotal = balTraySmallTotal
								+ report.balanceSmall;
						balTrayBigTotal = balTrayBigTotal + report.balanceBig;
						balTrayLeadTotal = balTrayLeadTotal
								+ report.balanceLead;

						$('#table_grid3 tbody').append(tr);

					})

					var tr = $('<tr></tr>');

					tr.append($('<td></td>').html(""));

					tr.append($('<td style="font-weight:bold;"></td>').html(
							"Total"));

					tr.append($('<td style="text-align:right;"></td>').html(
							balTraySmallTotal));
					tr.append($('<td style="text-align:right;"></td>').html(
							balTrayBigTotal));
					tr.append($('<td style="text-align:right;"></td>').html(
							balTrayLeadTotal));

					$('#table_grid3 tbody').append(tr);

				}

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
			var trayType = document.getElementById("trayType").value;
			window
					.open('${pageContext.request.contextPath}/getTrayDetailReportPdf/'
							+ trayType + '/');
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






	<script type="text/javascript">
		function onRouteChange(routeId) {

			$.getJSON('${getFrListByRouteId}', {

				routeId : routeId,
				ajax : 'true'
			}, function(data) {

				var len = data.length;

				$('#frIdList').find('option').remove().end()
				$("#frIdList").append($("<option value='-1'> </option>"));
				for (var i = 0; i < len; i++) {
					$("#frIdList").append(
							$("<option ></option>").attr("value", data[i].frId)
									.text(data[i].frName));
				}
				$("#frIdList").trigger("chosen:updated");
			});

		}
	</script>
	<script type="text/javascript">
		function tableToExcel(table, name, filename) {
			let uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><title></title><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>', base64 = function(
					s) {
				return window.btoa(decodeURIComponent(encodeURIComponent(s)))
			}, format = function(s, c) {
				return s.replace(/{(\w+)}/g, function(m, p) {
					return c[p];
				})
			}

			if (!table.nodeType)
				table = document.getElementById(table)
			var ctx = {
				worksheet : name || 'Worksheet',
				table : table.innerHTML
			}

			var link = document.createElement('a');
			link.download = filename;
			link.href = uri + base64(format(template, ctx));
			link.click();
		}
	</script>
	<!-- 	<script type="text/javascript">
		function selectTable111() {
			alert("Hii");

			var trayType = parseInt(document.getElementById("trayType").value);
			alert(trayType);
			  if (trayType==1)) {
			   alert("in trayType111");
			   
			}
			 

		}
	</script> -->



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