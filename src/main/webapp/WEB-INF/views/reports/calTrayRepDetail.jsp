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
					<i class="fa fa-file-o"></i>Calculate Tray
				</h1>
				<h4></h4>
			</div>
		</div>
		<!-- END Page Title -->

		<!-- BEGIN Breadcrumb -->
		<%-- <div id="breadcrumbs">
			<ul class="breadcrumb">
				<li><i class="fa fa-home"></i> <a
					href="${pageContext.request.contextPath}/home">Home</a> <span
					class="divider"><i class="fa fa-angle-right"></i></span></li>
				<li class="active">Dispatch Report</li>
			</ul>
		</div> --%>
		<!-- END Breadcrumb -->

		<!-- BEGIN Main Content -->
		<div class="box">
			<div class="box-title">
				<h3>
					<i class="fa fa-bars"></i>Calculate Tray
				</h3>

			</div>

			<div class="box-content">

				<div class="row"></div>


				<div class="box-content">


					<br>

					<div class="row">
						<div class="col-md-12 table-responsive">
							<div style="">

								<c:choose>
									<c:when test="${submit1==1}">

										<c:set var="totalRouteOrderQty" value="0"></c:set>
										<c:set var="totalRouteTrayQty" value="0"></c:set>
										<c:set var="frSubCount" value="0"></c:set>

										<c:forEach items="${routeListForFr1}" var="routeList"
											varStatus="count">
											<div class="row">
												<div class="form-group">
													<label class="col-sm-3 col-lg-2	 control-label">Route
														Name : </label>
													<div class="col-sm-6 col-lg-4 controls date_select">
														${routeList.routeName}</div>

												</div>
											</div>
											<table class="table table-bordered  " style="width: 100%"
												id="table_gridFR" border="1">
												<thead style="background-color: #f3b5db;">
													<tr>
														<th>Sr.No.</th>
														<th width="60%">Franchisee Name</th>
														<th width="20%">SubCategory Name</th>
														<th width="10%">Qty</th>
														<th width="10%">Tray Qty</th>
													</tr>
												</thead>
												<tbody>


													<c:set var="totalOrderQty" value="0"></c:set>
													<c:set var="totalTrayQty" value="0"></c:set>

													<c:set var="totalOrderFRQty" value="0"></c:set>
													<c:set var="totalTrayFRQty" value="0"></c:set>

													<c:forEach items="${routeList.getFranchiseeList}"
														var="frNameIdByRouteIdList" varStatus="c1">

														<c:forEach items="${calListForFr}" var="calListForFr"
															varStatus="count">

															<c:set var="totalOrderFRQty"
																value="${totalTrayFRQty+totalOrderQty}"></c:set>

															<c:set var="totalTrayFRQty"
																value="${totalTrayFRQty+totalOrderQty}"></c:set>

															<c:choose>
																<c:when
																	test="${frNameIdByRouteIdList.frId==calListForFr.frId}">

																	<c:choose>
																		<c:when
																			test="${calListForFr.frRouteId==routeList.routeTrayId}">



																			<c:set var="totalOrderQty"
																				value="${totalOrderQty+(calListForFr.orderQty)}"></c:set>

																			<c:set var="totalTrayQty"
																				value="${totalTrayQty+(calListForFr.trayQty)}"></c:set>

																			<c:set var="frSubCount" value="${frSubCount+1}"></c:set>

																			<tr>
																				<td style="text-align: center">${frSubCount}</td>
																				<td align="left"><c:out
																						value="${calListForFr.frName}" /></td>
																				<td align="left"><c:out
																						value="${calListForFr.subCatName}" /></td>

																				<td align="right"><c:out
																						value="${calListForFr.orderQty}" /></td>
																				<td align="right"><c:out
																						value="${calListForFr.trayQty}" /></td>
																			</tr>


																		</c:when>
																	</c:choose>
																</c:when>
															</c:choose>

														</c:forEach>





													</c:forEach>





													<c:set var="totalRouteOrderQty"
														value="${totalRouteOrderQty+totalOrderQty}"></c:set>
													<c:set var="totalRouteTrayQty"
														value="${totalRouteTrayQty+totalTrayQty}"></c:set>
													<tr>
														<td></td>
														<td></td>
														<td>Total</td>
														<td align="right"><c:out value="${totalOrderQty}" /></td>
														<td align="right"><c:out value="${totalTrayQty}" /></td>

													</tr>
												</tbody>
											</table>
										</c:forEach>
										<table class="table table-bordered  " style="width: 100%"
											id="table_grid12">
											<thead style="background-color: #f3b5db;">
											</thead>
											<tbody>
												<tr>
													<td width="60%"></td>
													<td width="20%">Total</td>
													<td width="10%" align="right"><c:out
															value="${totalRouteOrderQty}" /></td>
													<td width="10%" align="right"><c:out
															value="${totalRouteTrayQty}" /></td>
												</tr>
											</tbody>
										</table>

										<button class="btn btn-primary" value="1" id="submit1"
											name="submit1" onclick="genPdf1()">PDF</button>
										<input id="date" name="date" value="${date}" type="hidden">
										<input id="routeIds" name="routeIds" value="${routeIds}"
											type="hidden">
										<input id="menuIds" name="menuIds" value="${menuIds}"
											type="hidden">

										<!-- 	<input type="button"
											onclick="tableToExcel('table_gridFR', 'name', 'CalculateTray.xls')"
											value="Export to Excel"> -->
									</c:when>

									<c:when test="${submit2==2}">

										<c:forEach items="${routeListForFr1}" var="routeList"
											varStatus="count">
											<div class="row">
												<div class="form-group">
													<label class="col-sm-3 col-lg-2	 control-label">Route
														Name : </label>
													<div class="col-sm-6 col-lg-4 controls date_select">
														${routeList.routeName}</div>

												</div>
											</div>
											<table class="table table-bordered  " style="width: 100%"
												id="table_gridFR2" border="1">
												<thead style="background-color: #f3b5db;">
													<tr>
														<th width="10%">Sr.No.</th>
														<th width="70%">Franchisee Name</th>
														<th width="10%">Qty</th>
														<th width="10%">Tray Qty</th>
													</tr>
												</thead>
												<tbody>
													<c:set var="totalFRFinalOrderQty" value="0" />
													<c:set var="totalFRFinalTrayQty" value="0" />
													<c:set var="frCount" value="0"></c:set>


													<c:forEach items="${routeList.getFranchiseeList}"
														var="frNameIdByRouteIdList" varStatus="count">
														<c:set var="totalFROrderQty" value="0" />
														<c:set var="totalFRTrayQty" value="0" />


														<c:forEach items="${calListForFr}" var="calListForFr"
															varStatus="cnt">

															<c:choose>
																<c:when
																	test="${calListForFr.frId==frNameIdByRouteIdList.frId}">

																	<c:set var="totalFROrderQty"
																		value="${totalFROrderQty+calListForFr.orderQty}" />

																	<c:set var="totalFRTrayQty"
																		value="${totalFRTrayQty+calListForFr.trayQty}" />
																</c:when>
															</c:choose>

														</c:forEach>
														<c:set var="frCount" value="${frCount+1}"></c:set>

														<c:set var="totalFRFinalOrderQty"
															value="${totalFROrderQty+totalFRFinalOrderQty}" />

														<c:set var="totalFRFinalTrayQty"
															value="${totalFRTrayQty+totalFRFinalTrayQty}" />



														<tr>
															<td style="text-align: center">${frCount}</td>
															<td align="left"><c:out
																	value="${frNameIdByRouteIdList.frName}" /></td>



															<td align="right"><c:out value="${totalFROrderQty}" /></td>
															<td align="right"><c:out value="${totalFRTrayQty}" /></td>


														</tr>
													</c:forEach>

													<tr>
														<td></td>
														<td>Total</td>

														<td align="right"><c:out
																value="${totalFRFinalOrderQty}" /></td>
														<td align="right"><c:out
																value="${totalFRFinalTrayQty}" /></td>


													</tr>




												</tbody>
											</table>
										</c:forEach>
										<button class="btn btn-primary" value="2" id="submit2"
											name="submit2" onclick="genPdf2()">PDF</button>
										<input id="date" name="date" value="${date}" type="hidden">
										<input id="routeIds" name="routeIds" value="${routeIds}"
											type="hidden">
										<input id="menuIds" name="menuIds" value="${menuIds}"
											type="hidden">

										<!-- <input type="button"
											onclick="tableToExcel('table_gridFR2', 'name', 'CalculateTray.xls')"
											value="Export to Excel"> -->
									</c:when>


									<c:when test="${submit3==3}">

										<table class="table table-bordered  " style="width: 100%"
											id="table_gridFR3" border="1">
											<thead style="background-color: #f3b5db;">
												<tr>
													<th>Sr.No.</th>
													<th>Route Name</th>
													<th>Qty</th>
													<th>Tray Qty</th>

												</tr>
											</thead>
											<tbody>

												<c:set var="FinalTotalOrderQty" value="0" />
												<c:set var="FinalTotalTrayQty" value="0" />



												<c:forEach items="${routeListForFr}" var="routeList"
													varStatus="count">
													<c:set var="totalROrderQty" value="0" />
													<c:set var="totalRTrayQty" value="0" />
													<c:forEach items="${calListForFr}" var="calListForFr"
														varStatus="cnt">

														<c:choose>
															<c:when
																test="${calListForFr.frRouteId==routeList.routeTrayId}">

																<c:set var="totalROrderQty"
																	value="${totalROrderQty+calListForFr.orderQty}" />

																<c:set var="totalRTrayQty"
																	value="${totalRTrayQty+calListForFr.trayQty}" />
															</c:when>
														</c:choose>




													</c:forEach>
													<c:set var="FinalTotalOrderQty"
														value="${totalROrderQty+FinalTotalOrderQty}" />

													<c:set var="FinalTotalTrayQty"
														value="${totalRTrayQty+FinalTotalTrayQty}" />



													<tr>
														<td width="10%" style="text-align: center">${count.index+1}</td>
														<td width="70%" align="left"><c:out
																value="${routeList.routeName}" /></td>

														<td width="10%" align="right"><c:out
																value="${totalROrderQty}" /></td>
														<td width="10%" align="right"><c:out
																value="${totalRTrayQty}" /></td>


													</tr>
												</c:forEach>

												<tr>
													<td width="10%"></td>
													<td width="70%">Total</td>

													<td width="10%" align="right"><c:out
															value="${FinalTotalOrderQty}" /></td>
													<td width="10%" align="right"><c:out
															value="${FinalTotalTrayQty}" /></td>


												</tr>





											</tbody>
										</table>

										<button class="btn btn-primary" value="3" id="submit3"
											name="submit3" onclick="genPdf3()">PDF</button>

										<input id="date" name="date" value="${date}" type="hidden">
										<input id="routeIds" name="routeIds" value="${routeIds}"
											type="hidden">
										<input id="menuIds" name="menuIds" value="${menuIds}"
											type="hidden">

										<!-- <input type="button"
											onclick="tableToExcel('table_gridFR3', 'name', 'CalculateTray.xls')"
											value="Export to Excel"> -->
									</c:when>

									<c:when test="${submit4==4}">

										<c:set var="totalSubOrderQtyFinal" value="0"></c:set>
										<c:set var="totalRouteTrayQtyFinal" value="0"></c:set>

										<table class="table table-bordered  " style="width: 100%"
											id="table_gridFR4" border="1">
											<thead style="background-color: #f3b5db;">
												<tr>
													<th>Sr.No.</th>
													<th>Route Name</th>
													<th>SubCategory Name</th>
													<th>Qty</th>
													<th>Tray Qty</th>


												</tr>
											</thead>
											<tbody>



												<c:forEach items="${routeListForFr}" var="routeList"
													varStatus="count">
													<c:set var="SubOrderQty" value="0"></c:set>
													<c:set var="SubTrayQty" value="0"></c:set>
													<c:set var="srNo" value="0"></c:set>
													<c:forEach items="${subCatAList}" var="subCatAList"
														varStatus="c1">



														<c:set var="totalSubOrderQty" value="0"></c:set>
														<c:set var="totalSubTrayQty" value="0"></c:set>

														<c:forEach items="${calListForFr}" var="calListForFr"
															varStatus="cnt">
															<c:choose>
																<c:when
																	test="${routeList.routeTrayId==calListForFr.frRouteId}">
																	<c:choose>
																		<c:when
																			test="${subCatAList.subCatId==calListForFr.subCatId}">
																			<c:set var="totalSubOrderQty"
																				value="${totalSubOrderQty+calListForFr.orderQty}" />

																			<c:set var="totalSubTrayQty"
																				value="${totalSubTrayQty+calListForFr.trayQty}" />
																		</c:when>
																	</c:choose>
																</c:when>
															</c:choose>


														</c:forEach>



														<tr>
															<c:choose>
																<c:when test="${totalSubOrderQty>0}">
																	<c:set var="srNo" value="${srNo+1}"></c:set>


																	<td width="5%" style="text-align: center">${srNo}</td>
																	<td width="50%" align="left"><c:choose>
																			<c:when test="${srNo==1}">
																				<c:out value="${routeList.routeName}" />
																			</c:when>
																			<c:otherwise>

																			</c:otherwise>
																		</c:choose></td>
																	<td width="25%" align="left"><c:out
																			value="${subCatAList.subCatName}" /></td>

																	<td width="10%" align="right"><c:out
																			value="${totalSubOrderQty}" /></td>
																	<td width="10%" align="right"><c:out
																			value="${totalSubTrayQty}" /></td>

																	<c:set var="SubOrderQty"
																		value="${totalSubOrderQty+SubOrderQty}" />

																	<c:set var="SubTrayQty"
																		value="${totalSubTrayQty+SubTrayQty}" />
																</c:when>
															</c:choose>

														</tr>
													</c:forEach>

													<c:set var="totalSubOrderQtyFinal"
														value="${totalSubOrderQtyFinal+SubOrderQty}" />

													<c:set var="totalRouteTrayQtyFinal"
														value="${totalRouteTrayQtyFinal+SubTrayQty}" />
													<c:choose>
														<c:when test="${SubOrderQty>0}">
															<tr>



																<td width="5%" style="text-align: center"></td>
																<td width="50%" align="left"></td>
																<td width="25%" align="left"><b>Total</b></td>

																<td width="10%" align="right"><b><c:out
																			value="${SubOrderQty}" /></b></td>
																<td width="10%" align="right"><b><c:out
																			value="${SubTrayQty}" /></b></td>

															</tr>
														</c:when>
													</c:choose>

												</c:forEach>
											</tbody>
										</table>
										<table class="table table-bordered  " style="width: 100%"
											id="table_grid" border="1">
											<thead style="background-color: #f3b5db;">
											</thead>
											<tbody>
												<tr>

													<td width="80%"><b>Total</b></td>
													<td width="10%" align="right"><b><c:out
																value="${totalSubOrderQtyFinal}" /></b></td>
													<td width="10%" align="right"><b><c:out
																value="${totalRouteTrayQtyFinal}" /></b></td>

												</tr>

											</tbody>
										</table>

										<button class="btn btn-primary" value="4" id="submit4"
											name="submit4" onclick="genPdf4()">PDF</button>
										<input id="date" name="date" value="${date}" type="hidden">
										<input id="routeIds" name="routeIds" value="${routeIds}"
											type="hidden">
										<input id="menuIds" name="menuIds" value="${menuIds}"
											type="hidden">

										<!-- 	<input type="button"
											onclick="tableToExcel('table_gridFR4', 'name', 'CalculateTray.xls')"
											value="Export to Excel"> -->

									</c:when>

								</c:choose>
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
			function genPdf1() {

				var date = $("#date").val();
				var menuIds = $("#menuIds").val();
				var routeIds = $("#routeIds").val();
				var submit = $("#submit1").val();

				window
						.open('pdfForDisReport?url=pdf/getCalculateTrayReportPDF/'
								+ date
								+ '/'
								+ routeIds
								+ '/'
								+ menuIds
								+ '/'
								+ submit);

			}

			function genPdf2() {

				var date = $("#date").val();
				var menuIds = $("#menuIds").val();
				var routeIds = $("#routeIds").val();
				var submit = $("#submit2").val();

				window
						.open('pdfForDisReport?url=pdf/getCalculateTrayReportPDF/'
								+ date
								+ '/'
								+ routeIds
								+ '/'
								+ menuIds
								+ '/'
								+ submit);

			}

			function genPdf3() {

				var date = $("#date").val();
				var menuIds = $("#menuIds").val();
				var routeIds = $("#routeIds").val();
				var submit = $("#submit3").val();

				window
						.open('pdfForDisReport?url=pdf/getCalculateTrayReportPDF/'
								+ date
								+ '/'
								+ routeIds
								+ '/'
								+ menuIds
								+ '/'
								+ submit);

			}

			function genPdf4() {

				var date = $("#date").val();
				var menuIds = $("#menuIds").val();
				var routeIds = $("#routeIds").val();
				var submit = $("#submit4").val();

				window
						.open('pdfForDisReport?url=pdf/getCalculateTrayReportPDF/'
								+ date
								+ '/'
								+ routeIds
								+ '/'
								+ menuIds
								+ '/'
								+ submit);

			}

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
</body>
</html>