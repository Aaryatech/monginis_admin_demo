<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>

	<c:url var="machineListOrVehicleList" value="/machineListOrVehicleList"></c:url>
	<c:url var="serchLogisticsList" value="/serchLogisticsList"></c:url>


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
					<i class="fa fa-file-o"></i>AMC List
				</h1>
				<h4>Bill for franchises</h4>
			</div>
		</div> -->
		<!-- END Page Title -->


		<!-- BEGIN Main Content -->
		<div class="box" id="pending">
			<div class="box-title">
				<h3>
					<i class="fa fa-bars"></i>Logistics List
				</h3>
				<div class="box-tool"></div>
				<!-- <div class="box-tool">
				<a  onclick="showdatewisetable()">Show Datewise All Record</a> 
				</div> -->

			</div>


			<div class=" box-content">

				<div class="box-content">

					<div class="row">


						<div class="form-group">
							<label class="col-sm-2 col-lg-2	 control-label">From
								Date*</label>
							<div class="col-sm-2 col-lg-2 controls date_select">
								<input class="form-control date-picker" id="fromDate"
									name="fromDate" size="30" type="text" value="${todaysDate}" />
							</div>

							<!-- </div>

					<div class="form-group  "> -->

							<label class="col-sm-2 col-lg-2	 control-label">To Date*</label>
							<div class="col-sm-2 col-lg-2 controls date_select">
								<input class="form-control date-picker" id="toDate"
									name="toDate" size="30" type="text" value="${todaysDate}" />
							</div>

							<label class="col-sm-2 col-lg-2	 control-label">Select</label>
							<div class="col-sm-2 col-lg-2 controls">
								<select name="servType" id="servType"
									class="form-control chosen" tabindex="6" required>

									<option value="-1">All</option>
									<option value="1">Regular</option>
									<option value="2">Breakdown</option>
								</select>
							</div>



						</div>
					</div>

				</div>
				<div class="box-content">
					<div class="row">
						<div class="form-group">
							<label class="col-sm-3 col-lg-2	 control-label">Select
								Type*</label>

							<div class="col-sm-3 col-lg-3 controls">
								<select name="typeId" id="typeId" class="form-control chosen"
									tabindex="6" required>
									<option value="">Select Option</option>
									<option value="1">Vehicle</option>
									<option value="2">Machine</option>
								</select>
							</div>


							<div class="col-md-2">Select Machine Or Vehicle*</div>
							<div class="col-md-3">

								<select name="vehIdList" id="vehIdList"
									class="form-control chosen" tabindex="6" multiple="multiple"
									onchange="setSelectedVehicle(this.value)" required>

								</select>
							</div>

							<div align="center" class="form-group">

								<input type="button" class="btn btn-primary" value="Search"
									onclick="serchAmc()">

								<button class="btn btn-primary" value="PDF" id="PDFButton"
									onclick="genPdf()" disabled>PDF</button>

								<div align="center" id="loader" style="display: none">

									<span>
										<h4>
											<font color="#343690">Loading</font>
										</h4>
									</span> <span class="l-1"></span> <span class="l-2"></span> <span
										class="l-3"></span> <span class="l-4"></span> <span
										class="l-5"></span> <span class="l-6"></span>
								</div>


							</div>
						</div>
					</div>
				</div>
				<br> <br>




				<div class="row">
					<div class="col-md-12 table-responsive">
						<table class="table table-bordered table-striped fill-head "
							style="width: 100%" id="table_grid">
							<thead style="background-color: #f3b5db;">
								<tr>
									<th>Sr.No.</th>
									<th>Vehicle Or Machine Name</th>

									<th>Taxable Amt</th>
									<th>Tax Amt</th>
									<th>Total</th>
									<th>Action</th>


								</tr>
							</thead>

							<tbody>


							</tbody>
						</table>
					</div>




					<div class="col-sm-3  controls">
						<input type="button" id="expExcel" class="btn btn-primary"
							value="EXPORT TO Excel" onclick="exportToExcel();" disabled>
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
		$(document)
				.ready(
						function() {
							$('#typeId')
									.change(
											function() {
												//alert("makeId"+$(this).val());
												var typeId = $(this).val();

												$
														.getJSON(
																'${machineListOrVehicleList}',
																{

																	typeId : $(
																			this)
																			.val(),
																	ajax : 'true'
																},
																function(data) {

																	var html = '<option value="-1"   >All</option>';

																	var len = data.length;
																	for (var i = 0; i < len; i++) {
																		html += '<option value="' + data[i].mechId + '">'
																				+ data[i].mechName
																				+ '</option>';
																	}
																	html += '</option>';
																	$(
																			'#vehIdList')
																			.html(
																					html);
																	$(
																			"#vehIdList")
																			.trigger(
																					"chosen:updated");

																});

											})

						});

		function setSelectedVehicle(temp) {

			var typeId = $('#typeId').val();
			if (temp == -1) {
				$
						.getJSON(
								'${machineListOrVehicleList}',
								{

									typeId : typeId,
									ajax : 'true'
								},
								function(data) {

									var html = '<option value="-1"   >All</option>';

									var len = data.length;
									for (var i = 0; i < len; i++) {
										html += '<option value="' + data[i].mechId + '" selected>'
												+ data[i].mechName
												+ '</option>';
									}
									html += '</option>';
									$('#vehIdList').html(html);
									$("#vehIdList").trigger("chosen:updated");

								});
			}
		}

		function serchAmc() {
			//alert("Hii");

			var typeId = document.getElementById("typeId").value;
			var servType = document.getElementById("servType").value;
			var fromDate = document.getElementById("fromDate").value;
			var toDate = document.getElementById("toDate").value;
			//var vehIdList = document.getElementById("vehIdList").value;
			var vehIdList = $('#vehIdList').val();
			/* alert(typeId);
			alert(servType);
			alert(fromDate);
			alert(toDate);
			alert(vehIdList);
			 */
			$('#loader').show();
			$.getJSON('${serchLogisticsList}',

			{

				typeId : typeId,
				fromDate : fromDate,
				toDate : toDate,
				servType : servType,
				vehIdList : JSON.stringify(vehIdList),
				ajax : 'true'

			}, function(data) {

				var taxableTotal = 0;
				var taxTotal = 0;
				var finalTotal = 0;

				$('#loader').hide();
				$('#table_grid td').remove();

				document.getElementById("PDFButton").disabled = false;
				document.getElementById("expExcel").disabled = false;
				if (data == "") {
					alert("No records found !!");
					document.getElementById("expExcel").disabled = true;
					document.getElementById("PDFButton").disabled = true;
				}
				$.each(data, function(key, itemList) {

					var type;
					if (itemList.typeId == 1) {
						type = "Vehicle";
					} else {
						type = "Machine";
					}
					var tr = $('<tr></tr>');
					tr.append($('<td ></td>').html(key + 1));
					tr.append($('<td></td>').html(itemList.vehNo));
					tr.append($('<td style="text-align:right;"></td>').html(
							itemList.taxableAmt));
					tr.append($('<td style="text-align:right;"></td>').html(
							itemList.taxAmt));
					tr.append($('<td style="text-align:right;"></td>').html(
							itemList.total));
					tr.append($('<td style="text-align:center;"></td>').html(
							' <a>   <span class="fa fa-list" id="edit'
									+ itemList.vehId + '" onClick=detailList('
									+ itemList.vehId + ');> </span> </a> '));

					taxableTotal = taxableTotal + itemList.taxableAmt;
					taxTotal = taxTotal + itemList.taxAmt;
					finalTotal = finalTotal + itemList.total;

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
				tr.append($('<td style="text-align:right;"></td>').html(
						finalTotal.toFixed(2)));
				tr.append($('<td></td>').html(""));

				$('#table_grid tbody').append(tr);

			});

		}
	</script>

	<script type="text/javascript">
		function detailList(vehId) {
			var fromdate = $("#fromDate").val();
			var todate = $("#toDate").val();
			window
					.open('${pageContext.request.contextPath}/showLogisticsReportDetailList/'
							+ vehId + '/' + fromdate + '/' + todate + '/');

		}
	</script>

	<script>
		function genPdf() {
			window
					.open('${pageContext.request.contextPath}/getLogisticsReportPdf');
		}
	</script>
	<script type="text/javascript">
		function exportToExcel() {

			window.open("${pageContext.request.contextPath}/exportToExcel");
			document.getElementById("expExcel").disabled = true;
		}
	</script>



</body>
</html>