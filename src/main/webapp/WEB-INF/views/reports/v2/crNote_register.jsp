<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 	 <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
 --%>
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>
	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
	<c:url var="getCRNoteRegister" value="/getCRNoteRegister"></c:url>

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
					<i class="fa fa-file-o"></i>Credit Note-Wise HSN wise Report
				</h1>
				<h4></h4>
			</div>
		</div>
		<!-- END Page Title -->


		<!-- BEGIN Main Content -->
		<div class="box">
			<div class="box-title">
				<h3>
					<i class="fa fa-bars"></i>Credit Note-Wise HSN wise Report
				</h3>

			</div>

			<div class="box-content">
				<div class="row">


					<div class="form-group">
						<label class="col-sm-3 col-lg-1	 control-label">From Date</label>
						<div class="col-sm-6 col-lg-2 controls date_select">
							<input class="form-control date-picker" id="fromDate"
								name="fromDate" size="30" type="text" value="${todaysDate}" />
						</div>

						<!-- </div>

					<div class="form-group  "> -->

						<label class="col-sm-3 col-lg-1	 control-label">To Date</label>
						<div class="col-sm-6 col-lg-2 controls date_select">
							<input class="form-control date-picker" id="toDate" name="toDate"
								size="30" type="text" value="${todaysDate}" />
						</div>



						<div class="col-sm-6 col-lg-4">
							<button class="btn btn-primary" onclick="searchReport()">Search</button>
							<input type="button" id="expExcel" class="btn btn-primary"
								value="Export To Excel" onclick="exportToExcel();"
								disabled="disabled">


							<button class="btn btn-primary" value="PDF" id="PDFButton"
								onclick="genPdf()" disabled="disabled">PDF</button>

							<%-- <a href="${pageContext.request.contextPath}/pdfForReport?url=showSaleBillwiseByFrPdf"
								target="_blank">PDF</a> --%>
						</div>
					</div>

				</div>

				<div class="row">
					<div class="col-md-12" style="text-align: center;">
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

			</div>


		</div>

		<div class="box">
			<div class=" box-content">
				<div class="row">
					<div class="col-md-12 table-responsive" style="overflow-x: auto;">

						<table class="table table-bordered table-striped fill-head "
							style="width: 100%" id="table_grid">
							<thead style="background-color: #f3b5db;">
								<tr>
									<th style="text-align: center;">Sr</th>
									<th style="text-align: center;">CRN No</th>
									<th style="text-align: center;">CRN Date</th>
									<th style="text-align: center;">Invoice No</th>
									<th style="text-align: center;">Invoice Date</th>
									<th style="text-align: center;">Party Name</th>
									<th style="text-align: center;">GST No</th>
									<th style="text-align: center;">HSN Code</th>
									<th style="text-align: center;">CRN Qty</th>
									<th style="text-align: center;">Taxable Amt</th>
									<th style="text-align: center;">CGST %</th>
									<th style="text-align: center;">CGST Amt</th>
									<th style="text-align: center;">SGST %</th>
									<th style="text-align: center;">SGST Amt</th>
									<th style="text-align: center;">CRN Amt</th>
									<th style="text-align: center;">Tax Amt</th>
									<th style="text-align: center;">Total Amt</th>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
						<div class="form-group" style="display: none;" id="range">



							<div class="col-sm-3  controls"></div>
						</div>
						<div align="center" id="showchart" style="display: none"></div>

						<div id="chart" style="background-color: white; display: none;">
							<br> <br> <br>
							<hr>


							<div id="chart_div" style="width: 100%; height: 100%;"></div>


							<div id="PieChart_div" style="width: 100%; height: 100%;"></div>


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
			//	var isValid = validate();
			//document.getElementById('chart').style.display ="display:none";
			document.getElementById("table_grid").style = "block";

			var from_date = $("#fromDate").val();
			var to_date = $("#toDate").val();

			$('#loader').show();

			$.getJSON('${getCRNoteRegister}',

			{
				fromDate : from_date,
				toDate : to_date,
				ajax : 'true'

			}, function(data) {

				$('#table_grid td').remove();
				$('#loader').hide();

				if (data == "") {
					alert("No records found !!");
					document.getElementById("expExcel").disabled = true;
					document.getElementById("PDFButton").disabled = true;

				}
				var crnQty = 0;
				var crnTaxable = 0;
				var cgstAmt = 0;
				var sgstAmt = 0;
				var crnAmt = 0;
				var taxAmtFinal = 0;
				var totAmtFinal = 0;

				$.each(data, function(key, report) {

					document.getElementById("expExcel").disabled = false;
					document.getElementById("PDFButton").disabled = false;

					document.getElementById('range').style.display = 'block';

					var index = key + 1;
					//var tr = "<tr>";
					var tr = $('<tr></tr>');
					tr.append($('<td></td>').html("" + index));
					tr.append($('<td></td>').html(report.frCode));
					tr.append($('<td></td>').html(report.crnDate));
					tr.append($('<td></td>').html(report.invoiceNo));
					tr.append($('<td></td>').html(report.billDate));
					tr.append($('<td style="text-align:left;"></td>').html(
							report.frName));
					tr.append($('<td style="text-align:left;"></td>').html(
							report.frGstNo));

					crnQty = crnQty + report.crnQty;
					crnTaxable = crnTaxable + report.crnTaxable;
					cgstAmt = cgstAmt + report.cgstAmt;
					sgstAmt = sgstAmt + report.sgstAmt;
					crnAmt = crnAmt + report.crnAmt;

					tr.append($('<td style="text-align:left;"></td>').html(
							report.hsnCode));

					tr.append($('<td style="text-align:center;"></td>').html(
							addCommas(report.crnQty)));

					tr.append($('<td style="text-align:right;"></td>').html(
							addCommas(report.crnTaxable.toFixed(2))));
					tr.append($('<td style="text-align:right;"></td>').html(
							report.cgstPer.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							addCommas(report.cgstAmt.toFixed(2))));

					tr.append($('<td style="text-align:right;"></td>').html(
							report.sgstPer.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							addCommas(report.sgstAmt.toFixed(2))));
					tr.append($('<td style="text-align:right;"></td>').html(
							addCommas(report.crnAmt.toFixed(2))));

					var tax_amt = report.cgstAmt + report.sgstAmt;
					var tot_amt = report.crnTaxable + report.cgstAmt
							+ report.sgstAmt;

					tr.append($('<td style="text-align:right;"></td>').html(
							addCommas(tax_amt.toFixed(2))));

					tr.append($('<td style="text-align:right;"></td>').html(
							addCommas(tot_amt.toFixed(2))));

					taxAmtFinal = taxAmtFinal + tax_amt;
					totAmtFinal = totAmtFinal + tot_amt;

					$('#table_grid tbody').append(tr);

				})
				var tr = $('<tr></tr>');
				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				tr.append($('<td style="font-weight:12px;"></td>').html(""));
				tr.append($('<td></td>').html("Total"));
				tr.append($('<td style="font-weight:12px;"></td>').html(
						"" + addCommas(crnQty.toFixed(2))));
				tr.append($('<td style="font-weight:12px;"></td>').html(
						"" + addCommas(crnTaxable.toFixed(2))));
				tr.append($('<td></td>').html(""));
				tr.append($('<td style="font-weight:12px;"></td>').html(
						"" + addCommas(cgstAmt.toFixed(2))));
				tr.append($('<td></td>').html(""));
				tr.append($('<td style="font-weight:12px;"></td>').html(
						"" + addCommas(sgstAmt.toFixed(2))));
				tr.append($('<td style="font-weight:12px;"></td>').html(
						"" + addCommas(crnAmt.toFixed(0))));

				tr.append($('<td style="font-weight:12px;"></td>').html(
						"" + addCommas(taxAmtFinal.toFixed(0))));

				tr.append($('<td style="font-weight:12px;"></td>').html(
						"" + addCommas(totAmtFinal.toFixed(0))));

				$('#table_grid tbody').append(tr);

			});

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


	<script>
		$('.datepicker').datepicker({
			format : {
				/*
				 * Say our UI should display a week ahead,
				 * but textbox should store the actual date.
				 * This is useful if we need UI to select local dates,
				 * but store in UTC
				 */
				format : 'mm/dd/yyyy',
				startDate : '-3d'
			}
		});
	</script>

	<script type="text/javascript">
		function disableFr() {

			//alert("Inside Disable Fr ");
			document.getElementById("selectFr").disabled = true;

		}

		function disableRoute() {

			//alert("Inside Disable route ");
			var x = document.getElementById("selectRoute")
			//alert(x.options.length);
			var i;
			for (i = 0; i < x; i++) {
				document.getElementById("selectRoute").options[i].disabled;
				//document.getElementById("pets").options[2].disabled = true;
			}
			//document.getElementById("selectRoute").disabled = true;

		}
	</script>

	<script type="text/javascript">
		function showChart() {

			$("#PieChart_div").empty();
			$("#chart_div").empty();
			document.getElementById('chart').style.display = "block";
			document.getElementById("table_grid").style = "display:none";

			var selectedFr = $("#selectFr").val();
			var routeId = $("#selectRoute").val();

			var from_date = $("#fromDate").val();
			var to_date = $("#toDate").val();

			//document.getElementById('btn_pdf').style.display = "block";
			$
					.getJSON(
							'${getBillList}',

							{
								fr_id_list : JSON.stringify(selectedFr),
								fromDate : from_date,
								toDate : to_date,
								route_id : routeId,
								ajax : 'true'

							},
							function(data) {

								//alert(data);
								if (data == "") {
									alert("No records found !!");

								}
								var i = 0;

								google.charts.load('current', {
									'packages' : [ 'corechart', 'bar' ]
								});
								google.charts.setOnLoadCallback(drawStuff);

								function drawStuff() {

									// alert("Inside DrawStuff");

									var chartDiv = document
											.getElementById('chart_div');
									document.getElementById("chart_div").style.border = "thin dotted red";

									var PiechartDiv = document
											.getElementById('PieChart_div');
									document.getElementById("PieChart_div").style.border = "thin dotted red";

									var dataTable = new google.visualization.DataTable();
									dataTable.addColumn('string',
											'Franchisee Name'); // Implicit domain column.
									dataTable.addColumn('number', 'Base Value'); // Implicit data column.
									dataTable.addColumn('number', 'Total');

									var piedataTable = new google.visualization.DataTable();
									piedataTable.addColumn('string',
											'Franchisee Name'); // Implicit domain column.
									piedataTable.addColumn('number', 'Total');

									$
											.each(
													data,
													function(key, report) {

														// alert("In Data")
														var baseValue = report.taxableAmt;

														var total;

														if (report.isSameState == 1) {
															total = parseFloat(report.taxableAmt)
																	+ parseFloat(report.cgstSum
																			+ report.sgstSum);
														} else {

															total = report.taxableAmt
																	+ report.igstSum;
														}

														//var total=report.taxableAmt+report.sgstSum+report.cgstSum;
														//alert("total ==="+total);
														//alert("base Value "+baseValue);

														var frName = report.frName;
														//alert("frNAme "+frName);
														//var date= item.billDate+'\nTax : ' + item.tax_per + '%';

														dataTable
																.addRows([

																		[
																				frName,
																				baseValue,
																				total ],

																// ["Sai", 12,14],
																//["Sai", 12,16],
																// ["Sai", 12,18],
																// ["Sai", 12,19],

																]);

														piedataTable.addRows([

														[ frName, total ],

														]);
													}) // end of  $.each(data,function(key, report) {-- function

									// Instantiate and draw the chart.

									var materialOptions = {

										width : 500,
										chart : {
											title : 'Date wise Tax Graph',
											subtitle : 'Total tax & Taxable Amount per day',

										},
										series : {
											0 : {
												axis : 'distance'
											}, // Bind series 0 to an axis named 'distance'.
											1 : {
												axis : 'brightness'
											}
										// Bind series 1 to an axis named 'brightness'.
										},
										axes : {
											y : {
												distance : {
													label : 'Total Tax'
												}, // Left y-axis.
												brightness : {
													side : 'right',
													label : 'Taxable Amount'
												}
											// Right y-axis.
											}
										}
									};

									function drawMaterialChart() {
										var materialChart = new google.charts.Bar(
												chartDiv);

										// alert("mater chart "+materialChart);
										materialChart
												.draw(
														dataTable,
														google.charts.Bar
																.convertOptions(materialOptions));
										// button.innerText = 'Change to Classic';
										// button.onclick = drawClassicChart;
									}

									var chart = new google.visualization.ColumnChart(
											document
													.getElementById('chart_div'));

									var Piechart = new google.visualization.PieChart(
											document
													.getElementById('PieChart_div'));
									chart.draw(dataTable, {
										title : 'Sales Summary Group By Fr'
									});

									Piechart.draw(piedataTable, {
										title : 'Sales Summary Group By Fr',
										is3D : true
									});
									// drawMaterialChart();
								}
								;

							});

		}

		function genPdf() {
			var fromdate = $("#fromDate").val();
			var todate = $("#toDate").val();
			window
					.open('${pageContext.request.contextPath}/getCRNoteRegisterPdf/'
							+ fromdate + '/' + todate + '/');

		}
		function exportToExcel() {

			window.open("${pageContext.request.contextPath}/exportToExcelNew");
			document.getElementById("expExcel").disabled = true;
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