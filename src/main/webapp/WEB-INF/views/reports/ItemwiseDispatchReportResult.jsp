<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
 --%>
<!-- <script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script> -->

<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>

<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
	<c:url var="routListByAbcType" value="/routListByAbcType"></c:url>
	<c:url var="getBillList" value="/getPDispatchReportByRoute"></c:url>
	<c:url var="getFranchisees" value="/getFranchiseByRoute"></c:url>
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
					<i class="fa fa-file-o"></i>Dispatch Item Report
				</h1>
				<h4></h4>
			</div>
		</div>
		<div class="box">
			<div class="box-title">
				<h3>
					<i class="fa fa-bars"></i>Dispatch Report
				</h3>

			</div>

			<div class="box-content">

				<div class="row">

					<div class="form-group" align="center">
						<!-- 
					 <button class="btn btn-primary" value="PDF" id="PDFButton"
							onclick="genPdf()">PDF</button> -->
						<input id="dispatchReportList" name="dispatchReportList"
							value="${dispatchReportList}" type="hidden"> <input
							id="FrNameList" name="FrNameList" value="${FrNameList}"
							type="hidden"> <input id="Items" name="Items"
							value="${Items}" type="hidden">


					</div>
				</div>


			</div>


			<%--  <c:forEach items="${dispatchReportList}" var="dispatchReportList"> --%>

			<div class="box-content">



				<div class="row">
					<div class="col-md-12 table-responsive">
						<div style="">

							<table class="table table-bordered" style="width: 100%"
								id="table_grid">

								<thead style="background-color: #f3b5db;">
									<tr>
										<th>Sr.No.</th>
										<th>Franchise Name</th>
										<c:set var="itemCount" value="2"></c:set>
										<c:forEach items="${Items}" var="Items">


											<th>${Items.itemName}</th>
											<c:set var="itemCount" value="${itemCount+1}"></c:set>



										</c:forEach>


									</tr>
								</thead>
								<tbody>

									<c:set var="sr" value="0"></c:set>
									<c:forEach items="${FrNameList}" var="FrNameList"
										varStatus="count">





										<tr>
											<td><c:out value="${sr+1}" /> <c:set var="sr"
													value="${sr+1}"></c:set></td>
											<td align="left"><c:out value="${FrNameList.frName}" />
											</td>
											<c:forEach items="${Items}" var="items">
												<c:set var="orQty" value="0"></c:set>

												<td style="text-align: right;"><c:forEach
														items="${dispatchReportList}" var="dispatchReportList">

														<c:choose>
															<c:when
																test="${dispatchReportList.frId==FrNameList.frId}">
																<c:choose>
																	<c:when test="${dispatchReportList.itemId==items.id}">
																		<c:set var="orQty"
																			value="${dispatchReportList.orderQty}"></c:set>
																	</c:when>
																</c:choose>
															</c:when>
														</c:choose>

													</c:forEach> ${orQty}</td>
											</c:forEach>
										</tr>
									</c:forEach>


								</tbody>
							</table>
						</div>

					</div>


				</div>
				<input type="button"
					onclick="tableToExcel('table_grid', 'name', 'ItemwiseDispatch.xls')"
					value="Export to Excel">
			</div>

		</div>
		<footer>
			<p>2019 © Monginis.</p>
		</footer>

		<a id="btn-scrollup" class="btn btn-circle btn-lg" href="#"><i
			class="fa fa-chevron-up"></i></a>

		<script type="text/javascript">
			function tableToExcel(table, name, filename) {
				let uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><title></title><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>', base64 = function(
						s) {
					return window
							.btoa(decodeURIComponent(encodeURIComponent(s)))
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

		<script type="text/javascript">
			function routListByAbcType() {

				var abcType = $("#abcType").val();

				if (abcType != 0) {

					$.getJSON('${routListByAbcType}', {

						abcType : abcType,
						ajax : 'true'
					}, function(data) {
						var html = '<option value="">Select Rout</option>';

						var len = data.length;

						$('#selectRoute').find('option').remove().end()

						$("#selectRoute").append(
								$("<option></option>").attr("value", 0).text(
										"All"));

						for (var i = 0; i < len; i++) {
							$("#selectRoute").append(
									$("<option></option>").attr("value",
											data[i].routeId).text(
											data[i].routeName));
						}
						$("#selectRoute").trigger("chosen:updated");
					});
				} else {
					$('#selectRoute').find('option').remove().end()
					$("#selectRoute")
							.append(
									$("<option></option>").attr("value", 0)
											.text("All"));
					$("#selectRoute").trigger("chosen:updated");
				}
			}

			function getFranchise(routeId) {

				$.getJSON('${getFranchisees}', {

					routeId : routeId,
					ajax : 'true'
				}, function(data) {
					var html = '<option value="">Select Franchisee</option>';

					var len = data.length;

					$('#fraId').find('option').remove().end()

					$("#fraId").append(
							$("<option></option>").attr("value", 0).text(
									"Select Franchisee"));

					for (var i = 0; i < len; i++) {
						$("#fraId").append(
								$("<option></option>").attr("value",
										data[i].frId).text(data[i].frName));
					}
					$("#fraId").trigger("chosen:updated");
				});
			}
		</script>
		<script type="text/javascript">
			function searchReport() {
				var isValid = validate();
				if (isValid == true) {
					//document.getElementById('chart').style.display = "block";
					document.getElementById("PieChart_div").style = "display:none";
					document.getElementById("chart_div").style = "display:none";

					var routeId = $("#selectRoute").val();

					var routeName = $("#selectRoute option:selected").text();
					document.getElementById("routeName").innerText = "Route: "
							+ $("#selectRoute option:selected").text();
					document.getElementById("routeName").style.fontWeight = "900";
					var isGraph = 0;

					var selectedCat = $("#selectCat").val();

					var billDate = $("#billDate").val();
					$('#loader').show();

					$
							.getJSON(
									'${getBillList}',

									{
										bill_date : billDate,
										route_id : routeId,
										cat_id_list : JSON
												.stringify(selectedCat),
										routeName : routeName,
										ajax : 'true'

									},
									function(data) {
										document.getElementById("submit").disabled = false;
										$('#table_grid th').remove();

										$('#table_grid td').remove();

										$('#loader').hide();
										var frListLength = data.frList.length;
										if (data == "") {
											alert("No records found !!");
											document.getElementById("expExcel").disabled = true;
											document.getElementById("submit").disabled = true;
										}

										var tr;
										tr = document
												.getElementById('table_grid').tHead.children[0];
										tr.insertCell(0).outerHTML = "<th align='left'>Sr.No.</th>"

										tr.insertCell(1).outerHTML = "<th style='width=170px'>ItemName</th>"
										var i = 0;
										var j = 0;
										$
												.each(
														data.frList,
														function(key, fr) {
															i = key + 2;
															tr.insertCell(i).outerHTML = "<th style='width=80px'>"
																	+ fr.frName
																	+ "</th>"
														});//franchise for end    
										/* 								         tr.insertCell(i+1).outerHTML = "<th style='font-weight:bold'>Total</th>"
										 */

										$
												.each(
														data.subCatList,
														function(key, subCat) {
															var tr = $('<tr></tr>');
															tr
																	.append($(
																			'<td></td>')
																			.html(
																					" "));
															tr
																	.append($(
																			'<td style="font-weight:bold"></td>')
																			.html(
																					subCat.subCatName));
															for (var i = 0; i <= frListLength; i++) {
																tr
																		.append($(
																				'<td></td>')
																				.html(
																						" "));
															}

															$(
																	'#table_grid tbody')
																	.append(tr);

															var srNo = 0;
															$
																	.each(
																			data.itemList,
																			function(
																					key,
																					item) {
																				if (item.itemGrp2 == subCat.subCatId) {
																					srNo = srNo + 1;
																					var tr = $('<tr></tr>');
																					tr
																							.append($(
																									'<td></td>')
																									.html(
																											srNo));
																					tr
																							.append($(
																									'<td></td>')
																									.html(
																											item.itemName));
																					$
																							.each(
																									data.frList,
																									function(
																											key,
																											franchise) {
																										//alert(franchise.frId)
																										var orderId = 'o';
																										var orderQty = 0;
																										var isBillGenerated = 0;
																										$
																												.each(
																														data.dispatchReportList,
																														function(
																																key,
																																report) {
																															if (franchise.frId == report.frId
																																	&& item.id == report.itemId) {
																																orderId = report.orderId;
																																orderQty = report.orderQty;
																																isBillGenerated = report.isBillGenerated;
																															}

																														});
																										if (isBillGenerated == 2) {
																											tr
																													.append($(
																															'<td style="color:blue;"></td>')
																															.html(
																																	"Qty: "
																																			+ orderQty
																																			+ " <input type=number style='text-align:right; width:60px' min=0 class=form-control name=itemQty"+franchise.frId+""+item.id+""+orderId+" id=itemQty"+franchise.frId+""+item.id+""+orderId+" value=0 disabled/>"));
																										} else {
																											if (orderId == 'o') {
																												tr
																														.append($(
																																'<td style="color:red;"></td>')
																																.html(
																																		"Qty: "
																																				+ orderQty
																																				+ " <input type=number style='text-align:right; width:60px' min=0 class=form-control name=itemQty"+franchise.frId+""+item.id+""+orderId+" id=itemQty"+franchise.frId+""+item.id+""+orderId+" value=0 disabled/>"));
																											} else {
																												tr
																														.append($(
																																'<td style="color:green;"></td>')
																																.html(
																																		"Qty: "
																																				+ orderQty
																																				+ " <input type=number style='text-align:right; width:60px' min=0 class=form-control name=itemQty"+franchise.frId+""+item.id+""+orderId+" id=itemQty"+franchise.frId+""+item.id+""+orderId+" value=0 />"));

																											}
																										}
																									});
																					/*  tr
																						.append($(
																								'<td></td>')
																								.html("<input type=text style='text-align:right; width:60px' class=form-control name=total"+key+""+item.id+" id=total"+key+""+item.id+" value=0 disabled/>"));
																					 */$(
																							'#table_grid tbody')
																							.append(
																									tr);
																				}//end if
																			});//itemList for end
														});//subCatList for end
										//	var srNo = 0;
										$
												.each(
														data.dispatchReportList,
														function(key, report) {

															document
																	.getElementById('itemQty'
																			+ report.frId
																			+ ''
																			+ report.itemId
																			+ ''
																			+ report.orderId).value = report.editQty;

														});

										/* $.each(data.itemList,
													function(key,item) {
												 var total=0;
										 		 $.each(data.frList, function(key,franchise){  	
										 			 var tot=parseFloat($("#itemQty"+franchise.frId+''+item.id).val());
										 			
										 			 total=total+tot;
										 			 
										     		
										
										 		 });
										 		  document.getElementById('total'+key+''+item.id).value =total;
										 	});	 */

									});
				}

			}
		</script>


		<script type="text/javascript">
			function showChart() {

				$("#PieChart_div").empty();
				$("#chart_div").empty();
				//document.getElementById('chart').style.display = "block";
				document.getElementById("table_grid").style = "display:none";
				document.getElementById("PieChart_div").style = "block";
				document.getElementById("chart_div").style = "block";

				var selectedFr = $("#selectFr").val();
				var routeId = $("#selectRoute").val();
				var selectedCat = $("#selectCat").val();
				var from_date = $("#fromDate").val();
				var to_date = $("#toDate").val();

				var isGraph = 1;
				$('#loader').show();

				$
						.getJSON(
								'${getBillList}',

								{
									fr_id_list : JSON.stringify(selectedFr),
									fromDate : from_date,
									toDate : to_date,
									route_id : routeId,
									cat_id_list : JSON.stringify(selectedCat),
									is_graph : isGraph,

									ajax : 'true'

								},
								function(data) {

									$('#loader').hide();

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
												'Category'); // Implicit domain column.
										dataTable.addColumn('number', 'NetQty'); // Implicit data column.
										dataTable.addColumn('number',
												'NetValue');

										var piedataTable = new google.visualization.DataTable();
										piedataTable.addColumn('string',
												'Category'); // Implicit domain column.
										piedataTable.addColumn('number',
												'NetValue');

										$
												.each(
														data.categoryList,
														function(key, cat) {
															var netQty = 0;
															var netValue = 0;
															$
																	.each(
																			data.salesReportRoyalty,
																			function(
																					key,
																					report) {

																				if (cat.catId === report.catId) {
																					netQty = netQty
																							+ report.tBillQty
																							- (report.tGrnQty + report.tGvnQty);
																					//netQty=netQty.toFixed(2);
																					netValue = netValue
																							+ report.tBillTaxableAmt
																							- (report.tGrnTaxableAmt + report.tGvnTaxableAmt);
																					//netValue=netValue.toFixed(2);
																					var catName = report.cat_name;
																					//alert("CatName"+catName);

																					//alert("netValue"+netValue);
																					//alert("netQty"+netQty);

																					dataTable
																							.addRows([
																									[
																											catName,
																											netQty,
																											netValue ], ]);

																					piedataTable
																							.addRows([
																									[
																											catName,
																											netValue ], ]);
																				}
																			})

														})
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

											materialChart
													.draw(
															dataTable,
															google.charts.Bar
																	.convertOptions(materialOptions));

										}

										var chart = new google.visualization.ColumnChart(
												document
														.getElementById('chart_div'));

										var Piechart = new google.visualization.PieChart(
												document
														.getElementById('PieChart_div'));
										chart
												.draw(
														dataTable,
														{
															width : 1000,
															height : 600,
															title : 'Sales Summary Group By Month'
														});

										Piechart
												.draw(
														piedataTable,
														{
															width : 1000,
															height : 600,
															title : 'Sales Summary Group By Month',
															is3D : true
														});
										// drawMaterialChart();
									}
									;

								});

			}
		</script>


		<script type="text/javascript">
			function validate() {

				var selectCat = $("#selectCat").val();
				var selectedRoute = $("#selectRoute").val();

				var isValid = true;

				if (selectedRoute == "0" || selectedRoute == null) {

					alert("Please Select Route");
					isValid = false;

				} else if (selectCat == "" || selectCat == null) {

					isValid = false;
					alert("Please Select Category");

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

				var date = $("#date").val();
				var stationId = $("#stationId").val();
				var abcType = $("#abcType").val();
				var routId = $("#routId").val();
				var menuIds = $("#menuIds").val();

				window.open('pdfForDisReport?url=pdf/getPDispatchReportNewPdf/'
						+ date + '/' + stationId + '/' + abcType + '/' + routId
						+ '/' + menuIds);

			}
			function exportToExcel() {

				window.open("${pageContext.request.contextPath}/exportToExcel");
				document.getElementById("expExcel").disabled = true;
			}
			function genPdfBill() {
				var billDate = $("#billDate").val();
				var routeId = $("#selectRoute").val();
				var selectedCat = $("#selectCat").val();
				var frId = $("#fraId").val();

				window
						.open('pdfForDisReport?url=pdf/getDispatchPReportPdfForBill/'
								+ billDate
								+ '/'
								+ routeId
								+ '/'
								+ selectedCat
								+ '/' + frId);

			}
		</script>
		<script type="text/javascript">
			function genDispatchPdf() {
				var billDate = $("#billDate").val();
				var routeId = $("#selectRoute").val();
				var selectedCat = $("#selectCat").val();
				var frId = $("#fraId").val();

				window.open('pdf/getDispatchPReportPdfForDispatch/' + billDate
						+ '/' + routeId + '/' + selectedCat + '/' + frId);

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