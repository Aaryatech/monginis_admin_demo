<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/tableSearch.css">


<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>


	<c:url var="getBillList" value="/getSaleReportBillwiseAllFr"></c:url>


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
		<!-- <div class="page-title">
			<div>
				<h1>
					<i class="fa fa-file-o"></i>Billwise Sale Report All Fr
				</h1>
				<h4></h4>
			</div>
		</div> -->
		<!-- END Page Title -->

		<!-- BEGIN Breadcrumb -->
		<%-- 	<div id="breadcrumbs">
			<ul class="breadcrumb">
				<li><i class="fa fa-home"></i> <a
					href="${pageContext.request.contextPath}/home">Home</a> <span
					class="divider"><i class="fa fa-angle-right"></i></span></li>
				<li class="active">Bill Report</li>
			</ul>
		</div> --%>
		<!-- END Breadcrumb -->

		<!-- BEGIN Main Content -->
		<div class="box">
			<div class="box-title">
				<h3>
					<i class="fa fa-bars"></i>Bill-wise & HSN-wise Report
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
				<%-- <div class="row">
					<div class="form-group">
						<label class="col-sm-3 col-lg-2 control-label">Select
							Route</label>
						<div class="col-sm-6 col-lg-4 controls">
							<select data-placeholder="Select Route"
								class="form-control chosen" name="selectRoute" id="selectRoute"
								onchange="disableFr()">
								<option value="0">Select Route</option>
								<c:forEach items="${routeList}" var="route" varStatus="count">
									<option value="${route.routeId}"><c:out value="${route.routeName}"/> </option>

								</c:forEach>
							</select>

						</div>

						<label class="col-sm-3 col-lg-2 control-label"><b>OR </b>
							Select Franchisee</label>
						<div class="col-sm-6 col-lg-4">

							<select data-placeholder="Choose Franchisee"
								class="form-control chosen" multiple="multiple" tabindex="6"
								id="selectFr" name="selectFr" onchange="disableRoute()">

								<option value="-1"><c:out value="All"/></option>

								<c:forEach items="${unSelectedFrList}" var="fr"
									varStatus="count">
									<option value="${fr.frId}"><c:out value="${fr.frName}"/></option>
								</c:forEach>
							</select>

						</div>
					</div>
				</div> --%>

				<br>
				<div class="row">
					<div class="col-md-12" style="text-align: center;">
						<button class="btn btn-primary" onclick="searchReport()">Search</button>


						<button class="btn btn-primary" value="PDF" id="PDFButton"
							onclick="genPdf()">PDF</button>


						<%-- <a
							href='${pageContext.request.contextPath}/pdfForReport?url=showSaleReportBillwiseAllFrPdf'
							target="_blank">PDF</a>
 --%>
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


				<div class="col-md-9" style="padding-top: 5px;"></div>
				<label for="search" class="col-md-3" id="search"> <i
					class="fa fa-search" style="font-size: 20px"></i> <input
					type="text" style="border-radius: 25px;" id="myInput"
					onkeyup="myFunction()" placeholder="Search By Party Name & Date"
					title="Type in a name">
				</label>
			</div>
		</div>


		<div class="box">
			<!-- <div class="box-title">
				<h3>
					<i class="fa fa-list-alt"></i>Bill Report
				</h3>

			</div> -->

			<form id="submitBillForm"
				action="${pageContext.request.contextPath}/submitNewBill"
				method="post">
				<div class="box-content">
					<div class="row">
						<div class="col-md-12 table-responsive">
							<table class="table table-bordered table-striped fill-head "
								style="width: 100%" id="table_grid">
								<thead style="background-color: #f3b5db;">
									<tr>
										<th style="text-align: center;">Sr.No.</th>
										<th style="text-align: center;">Invoice No</th>
										<th style="text-align: center;" class="col-md-1">Date</th>
										<th style="text-align: center;">Party Name</th>
										<!-- <th style="text-align: center;">City</th> -->
										<th style="text-align: center;">GSTIN</th>
										<th style="text-align: center;">HSNCD</th>
										<th style="text-align: center;">Tax Rate</th>
										<th style="text-align: center;">Taxable Value</th>
										<th style="text-align: center;">CGST</th>
										<th style="text-align: center;">SGST</th>
										<th style="text-align: center;">IGST</th>
										<th style="text-align: center;">Tax Amt</th>
										<th style="text-align: center;">Total Amt</th>

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
			</form>
		</div>
	</div>
	<!-- END Main Content -->

	<footer>
		<p>2018 © Monginis.</p>
	</footer>

	<a id="btn-scrollup" class="btn btn-circle btn-lg" href="#"><i
		class="fa fa-chevron-up"></i></a>


	<script type="text/javascript">
		function searchReport() {
			//	var isValid = validate();
			//alert("HI ");
			//var selectedFr = $("#selectFr").val();
			//var routeId=$("#selectRoute").val();

			var from_date = $("#fromDate").val();
			var to_date = $("#toDate").val();

			$('#loader').show();

			$.getJSON('${getBillList}',

			{
				//fr_id_list : JSON.stringify(selectedFr),
				fromDate : from_date,
				toDate : to_date,
				//route_id:routeId,
				ajax : 'true'

			}, function(data) {

				$('#table_grid td').remove();
				$('#loader').hide();

				var totalBasicValue = 0;
				var totalCgst = 0;
				var totalSgst = 0;
				var totalIgst = 0;

				if (data == "") {
					alert("No records found !!");
					document.getElementById("expExcel").disabled = true;

				}

				$.each(data, function(key, report) {
					
					//alert("OP - "+JSON.stringify(report));
					
					document.getElementById("expExcel").disabled = false;
					document.getElementById('range').style.display = 'block';
					var index = key + 1;
					//var tr = "<tr>";	
					totalBasicValue = totalBasicValue + report.taxableAmtSum;
					totalCgst = totalCgst + report.cgstRsSum;
					totalSgst = totalSgst + report.sgstRsSum;
					totalIgst = totalIgst + report.igstRsSum;

					var tr = $('<tr></tr>');
					tr.append($('<td></td>').html(key + 1));
					tr.append($('<td></td>').html(report.invoiceNo));
					tr.append($('<td></td>').html(report.billDate));
					tr.append($('<td></td>').html(report.frName));
					/* tr.append($('<td></td>').html(report.frCity)); */
					tr.append($('<td></td>').html(report.frGstNo));
					tr.append($('<td></td>').html(report.itemHsncd));

					if (report.isSameState == 1) {

						var taxRate = report.itemTax1 + report.itemTax2;
						taxRate = taxRate.toFixed();
						tr.append($('<td style="text-align:right;"></td>')
								.html(taxRate));
						//	tr.append($('<td></td>').html(report.sgstSum));
						//	tr.append($('<td></td>').html(0));
					} else {
						//tr.append($('<td></td>').html(0));
						//	tr.append($('<td></td>').html(0));
						tr.append($('<td style="text-align:right;"></td>')
								.html(report.itemTax3));
					}
					//tr.append($('<td></td>').html(report.igstSum));
					tr.append($('<td style="text-align:right;"></td>').html(
							report.taxableAmtSum.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							report.cgstRsSum.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							report.sgstRsSum.toFixed(2)));
					tr.append($('<td style="text-align:right;"></td>').html(
							report.igstRsSum.toFixed(2)));
					
					var tax_amt=report.cgstRsSum+report.sgstRsSum;
					var tot_amt=report.taxableAmtSum+report.cgstRsSum+report.sgstRsSum;
					
					tr.append($('<td style="text-align:right;"></td>').html(
							tax_amt.toFixed(2)));
					
					tr.append($('<td style="text-align:right;"></td>').html(
							tot_amt.toFixed(2)));

					$('#table_grid tbody').append(tr);

				})

				var tr = $('<tr></tr>');

				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				tr.append($('<td></td>').html(""));
				/* tr.append($('<td></td>').html("")); */

				tr.append($('<td style="font-weight:bold;"></td>')
						.html("Total"));

				tr.append($('<td style="text-align:right;"></td>').html(
						totalBasicValue.toFixed(2)));
				tr.append($('<td style="text-align:right;"></td>').html(
						totalCgst.toFixed(2)));
				tr.append($('<td style="text-align:right;"></td>').html(
						totalSgst.toFixed(2)));
				tr.append($('<td style="text-align:right;"></td>').html(
						totalIgst.toFixed(2)));

				$('#table_grid tbody').append(tr);

			});

		}
	</script>


	<script type="text/javascript">
		function validate() {

			var selectedFr = $("#selectFr").val();
			var selectedMenu = $("#selectMenu").val();
			var selectedRoute = $("#selectRoute").val();

			var isValid = true;

			if (selectedFr == "" || selectedFr == null) {

				if (selectedRoute == "" || selectedRoute == null) {
					alert("Please Select atleast one ");
					isValid = false;
				}
				//alert("Please select Franchise/Route");

			} else if (selectedMenu == "" || selectedMenu == null) {

				isValid = false;
				alert("Please select Menu");

			}
			return isValid;

		}
	</script>

	<script type="text/javascript">
		function updateTotal(orderId, rate) {

			var newQty = $("#billQty" + orderId).val();

			var total = parseFloat(newQty) * parseFloat(rate);

			$('#billTotal' + orderId).html(total);
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
		function genPdf() {
			var from_date = $("#fromDate").val();
			var to_date = $("#toDate").val();

			window
					.open('${pageContext.request.contextPath}/pdfForReport?url=pdf/showSaleReportBillwiseAllFrPdf/'
							+ from_date + '/' + to_date);

		}
		function exportToExcel() {

			window.open("${pageContext.request.contextPath}/exportToExcelNew");
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
	<script>
		function myFunction() {
			var input, filter, table, tr, td, td1, i;
			input = document.getElementById("myInput");
			filter = input.value.toUpperCase();
			table = document.getElementById("table_grid");
			tr = table.getElementsByTagName("tr");
			for (i = 0; i < tr.length; i++) {
				td = tr[i].getElementsByTagName("td")[3];
				td1 = tr[i].getElementsByTagName("td")[2];
				if (td || td1) {
					if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
						tr[i].style.display = "";
					} else if (td1.innerHTML.toUpperCase().indexOf(filter) > -1) {
						tr[i].style.display = "";
					} else {
						tr[i].style.display = "none";
					}
				}
			}//end of for

		}
	</script>
</body>
</html>