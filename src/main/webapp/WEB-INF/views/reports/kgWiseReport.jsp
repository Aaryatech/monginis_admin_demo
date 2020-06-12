<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>


<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>

	<c:url var="getSpKgWiseList" value="/getSpKgWiseList"></c:url>
	<c:url var="getAllFrListForSalesReportAjax"
		value="/getAllFrListForSalesReportAjax"></c:url>

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
					<i class="fa fa-file-o"></i>Special Cake Kg-wise Report
				</h1>
				<h4></h4>
			</div>
		</div>
		<!-- END Page Title -->

		<div class="box">
			<div class="box-title">
				<h3>
					<i class="fa fa-bars"></i>Special Cake Kg-wise Report
				</h3>

			</div>

			<div class="box-content">
				<div class="row">
					<div class="form-group">
						<label class="col-sm-3 col-lg-2	 control-label">From Date</label>
						<div class="col-sm-6 col-lg-4 controls date_select">
							<input class="form-control date-picker" id="fromDate"
								name="fromDate" size="30" type="text" value="${todaysDate}" />
						</div>

						<!-- </div>

					<div class="form-group  "> -->

						<label class="col-sm-3 col-lg-2	 control-label">To Date</label>
						<div class="col-sm-6 col-lg-4 controls date_select">
							<input class="form-control date-picker" id="toDate" name="toDate"
								size="30" type="text" value="${todaysDate}" />
						</div>
					</div>

				</div>


				<br>

				<!-- <div class="col-sm-9 col-lg-5 controls">
 -->
				<div class="row">
					<div class="form-group">
						<label class="col-sm-3 col-lg-2 control-label">Franchisee</label>
						<div class="col-sm-6 col-lg-10">

							<select data-placeholder="Choose Franchisee"
								class="form-control chosen" multiple="multiple" tabindex="6"
								id="selectFr" name="selectFr"
								onchange="setAllFranchisee(this.value);">

								<option value="-1"><c:out value="All" /></option>

								<c:forEach items="${unSelectedFrList}" var="fr"
									varStatus="count">
									<option value="${fr.frId}"><c:out value="${fr.frName}" /></option>
								</c:forEach>
							</select>

						</div>
					</div>
				</div>

				<br>
				<div class="row">
					<div class="col-md-12" style="text-align: center;">
						<button class="btn btn-primary" onclick="searchReport()">Search</button>
						<!-- 	<button class="btn btn-primary" value="PDF" id="PDFButton"
							onclick="genPdf()">PDF</button> -->

					</div>
				</div>


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

			<input type="button" class="btn btn-primary"
				onclick="tableToExcel('table_grid', 'name', 'Special_Cake_KgWise_Report.xls')"
				value="Export to Excel">

			<div class=" box-content">

				<div class="row">
					<div class="col-md-12 table-responsive">
						<table class="table table-bordered table-striped fill-head "
							style="width: 100%" id="table_grid">
							<thead style="background-color: #f3b5db;">
								<tr>
									<th>Sr.No.</th>
									<th>Item</th>
									<th>Bill Qty</th>
									<th>Bill Value</th>
									<th>Return Qty</th>
									<th>Return Value</th>
									<th>Net Qty</th>
									<th>Net Value</th>
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
	</div>
	<!-- END Main Content -->

	<footer>
		<p>2019 © Monginis.</p>
	</footer>

	<a id="btn-scrollup" class="btn btn-circle btn-lg" href="#"><i
		class="fa fa-chevron-up"></i></a>


	<script type="text/javascript">
		function searchReport() {
			var selectedFr = $("#selectFr").val();
			var from_date = $("#fromDate").val();
			var to_date = $("#toDate").val();
			$('#loader').show();

			$
					.getJSON(
							'${getSpKgWiseList}',
							{
								fr_id_list : JSON.stringify(selectedFr),
								fromDate : from_date,
								toDate : to_date,
								ajax : 'true'

							},
							function(data) {

								$('#table_grid td').remove();
								$('#loader').hide();

								if (data == "") {
									alert("No records found !!");
									document.getElementById("expExcel").disabled = true;

								}
								var spQtyTotal = 0;
								var spValueTotal = 0;
								var crnQtyTotal = 0;
								var grnGvnAmtTotal = 0;
								var netValueGrandTotal = 0;
								var netQtyGrandTotal = 0;
								$
										.each(
												data.kgList,
												function(key, kg) {
													var tr = $('<tr style="color:red;font-weight:bold;"></tr>');

													tr.append($('<td></td>')
															.html("KG"));
													tr.append($('<td ></td>')
															.html("" + kg));
													tr
															.append($(
																	'<td  style="text-align:right;"></td>')
																	.html("_"));
													tr
															.append($(
																	'<td  style="text-align:right;"></td>')
																	.html("_"));
													tr
															.append($(
																	'<td  style="text-align:right;"></td>')
																	.html("_"));
													tr
															.append($(
																	'<td  style="text-align:right;"></td>')
																	.html("_"));
													tr
															.append($(
																	'<td  style="text-align:right;"></td>')
																	.html("_"));
													tr
															.append($(
																	'<td  style="text-align:right;"></td>')
																	.html("_"));
													$('#table_grid tbody')
															.append(tr);
													var spQty = 0;
													var spValue = 0;
													var crnQty = 0;
													var grnGvnAmt = 0;
													var netQtyTotal = 0;
													var netValueTotal = 0;
													$
															.each(
																	data.summaryDaoList,
																	function(
																			key,
																			report) {

																		if (kg == report.spSelectedWeight) {
																			srNo = key + 1;
																			var tr = $('<tr></tr>');
																			tr
																					.append($(
																							'<td></td>')
																							.html(
																									srNo));
																			tr
																					.append($(
																							'<td ></td>')
																							.html(
																									report.spName));
																			tr
																					.append($(
																							'<td  style="text-align:right;"></td>')
																							.html(
																									addCommas(report.spQty)));

																			var netQty = report.spQty
																					- report.crnQty;
																			netQtyTotal = netQtyTotal
																					+ netQty;
																			netQtyGrandTotal = netQtyGrandTotal
																					+ netQty;

																			var netValue = report.spValue
																					- report.grnGvnAmt;
																			netValueTotal = netValueTotal
																					+ netValue;
																			netValueGrandTotal = netValueGrandTotal
																					+ netValue;

																			spQty = spQty
																					+ report.spQty;
																			spQtyTotal = spQtyTotal
																					+ report.spQty;

																			tr
																					.append($(
																							'<td  style="text-align:right;"></td>')
																							.html(
																									addCommas(report.spValue
																											.toFixed(2))));

																			spValue = spValue
																					+ report.spValue;
																			spValueTotal = spValueTotal
																					+ report.spValue;
																			tr
																					.append($(
																							'<td  style="text-align:right;"></td>')
																							.html(
																									addCommas(report.crnQty)));

																			crnQty = crnQty
																					+ report.crnQty;
																			crnQtyTotal = crnQtyTotal
																					+ report.crnQty;
																			tr
																					.append($(
																							'<td  style="text-align:right;"></td>')
																							.html(
																									addCommas(report.grnGvnAmt
																											.toFixed(2))));

																			grnGvnAmt = grnGvnAmt
																					+ report.grnGvnAmt;
																			grnGvnAmtTotal = grnGvnAmtTotal
																					+ report.grnGvnAmt;
																			tr
																					.append($(
																							'<td  style="text-align:right;"></td>')
																							.html(
																									addCommas(netQty
																											.toFixed(2))));
																			tr
																					.append($(
																							'<td  style="text-align:right;"></td>')
																							.html(
																									addCommas(netValue
																											.toFixed(2))));
																			$(
																					'#table_grid tbody')
																					.append(
																							tr);
																		}
																	})
													var tr = $('<tr style="font-weight:bold;"></tr>');

													tr
															.append($(
																	'<td style="font-weight:bold;"></td>')
																	.html(
																			"TOTAL Of Kg "
																					+ kg));
													tr
															.append($(
																	'<td style="font-weight:bold;"></td>')
																	.html(""));
													tr
															.append($(
																	'<td  style="text-align:right;font-weight:bold;"></td>')
																	.html(
																			""
																					+ addCommas(spQty
																							.toFixed(2))));
													tr
															.append($(
																	'<td  style="text-align:right;font-weight:bold;"></td>')
																	.html(
																			""
																					+ addCommas(spValue
																							.toFixed(2))));
													tr
															.append($(
																	'<td  style="text-align:right;font-weight:bold;"></td>')
																	.html(
																			""
																					+ addCommas(crnQty
																							.toFixed(2))));
													tr
															.append($(
																	'<td  style="text-align:right;font-weight:bold;"></td>')
																	.html(
																			""
																					+ addCommas(grnGvnAmt
																							.toFixed(2))));
													tr
															.append($(
																	'<td  style="text-align:right;font-weight:bold;"></td>')
																	.html(
																			""
																					+ addCommas(netQtyTotal
																							.toFixed(2))));
													tr
															.append($(
																	'<td  style="text-align:right;font-weight:bold;"></td>')
																	.html(
																			""
																					+ addCommas(netValueTotal
																							.toFixed(2))));
													$('#table_grid tbody')
															.append(tr);
												});
								var tr = $('<tr style="font-weight:bold;"></tr>');

								tr.append($(
										'<td style="font-weight:bold;"></td>')
										.html("TOTAL"));
								tr.append($(
										'<td  style="font-weight:bold;"></td>')
										.html(" "));
								tr
										.append($(
												'<td  style="text-align:right;font-weight:bold;"></td>')
												.html(
														""
																+ addCommas(spQtyTotal
																		.toFixed(2))));
								tr
										.append($(
												'<td  style="text-align:right;font-weight:bold;"></td>')
												.html(
														""
																+ addCommas(spValueTotal
																		.toFixed(2))));
								tr
										.append($(
												'<td  style="text-align:right;font-weight:bold;"></td>')
												.html(
														""
																+ addCommas(crnQtyTotal
																		.toFixed(2))));
								tr
										.append($(
												'<td  style="text-align:right;font-weight:bold;"></td>')
												.html(
														""
																+ addCommas(grnGvnAmtTotal
																		.toFixed(2))));
								tr
										.append($(
												'<td  style="text-align:right;font-weight:bold;"></td>')
												.html(
														""
																+ addCommas(netQtyGrandTotal
																		.toFixed(2))));
								tr
										.append($(
												'<td  style="text-align:right;font-weight:bold;"></td>')
												.html(
														""
																+ addCommas(netValueGrandTotal
																		.toFixed(2))));
								$('#table_grid tbody').append(tr);

							});

		}
	</script>


	<script type="text/javascript">
		function addCommas(x) {

			x = String(x).toString();
			var afterPoint = '';
			if (x.indexOf('.') > 0)
				afterPoint = x.substring(x.indexOf('.'), x.length);
			x = Math.floor(x);
			x = x.toString();
			var lastThree = x.substring(x.length - 3);
			var otherNumbers = x.substring(0, x.length - 3);
			if (otherNumbers != '')
				lastThree = ',' + lastThree;
			return otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",")
					+ lastThree + afterPoint;
		}
	</script>

	<script type="text/javascript">
		function validate() {

			var selectedFr = $("#selectFr").val();
			var selectedRoute = $("#selectRoute").val();

			var isValid = true;

			if ((selectedFr == "" || selectedFr == null)
					&& (selectedRoute == 0)) {

				alert("Please Select Route  Or Franchisee");
				isValid = false;

			}
			return isValid;

		}
	</script>


	<script type="text/javascript">
		function setAllFranchisee(frId) {
			if (frId == -1) {
				$.getJSON('${getAllFrListForSalesReportAjax}', {
					ajax : 'true'
				}, function(data) {
					var len = data.length;
					$('#selectFr').find('option').remove().end()

					$("#selectFr").append(
							$("<option ></option>").attr("value", -1).text(
									"Select All"));

					for (var i = 0; i < len; i++) {

						$("#selectFr").append(
								$("<option selected></option>").attr("value",
										data[i].frId).text(data[i].frName));
					}

					$("#selectFr").trigger("chosen:updated");
				});
			}
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
</body>
</html>