<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>

	<c:url var="getBillList" value="/getSaleBillwise"></c:url>

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

		<div class="box">
			<div class="box-title">
				<h3>
					<i class="fa fa-bars"></i>Non Reg Franchise-wise Tax Report
				</h3>

			</div>

			<div class="box-content">
				<form
					action="${pageContext.request.contextPath}/showNonRegisteredFrTaxReport"
					class="form-horizontal" method="get" id="validation-form">
					<div class="row">


						<div class="form-group">
							<label class="col-sm-3 col-lg-2	 control-label">From Date</label>
							<div class="col-sm-6 col-lg-2 controls date_select">
								<input class="form-control date-picker" id="fromDate"
									autocomplete="off" name="fromDate" size="30" type="text"
									value="${fromDate}" placeholder="From Date" />
							</div>

							<!-- </div>

					<div class="form-group  "> -->

							<label class="col-sm-3 col-lg-2	 control-label">To Date</label>
							<div class="col-sm-6 col-lg-2 controls date_select">
								<input class="form-control date-picker" id="toDate"
									autocomplete="off" placeholder="To Date" name="toDate"
									size="30" type="text" value="${toDate}" />
							</div>
							<!-- </div>

				</div>


				<div class="row">
					<div class="col-md-12" style="text-align: center;"> -->
							<input type="submit" class="btn btn-info" value="Search" />
						</div>
					</div>
				</form>


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
			<div class=" box-content">
				<div class="row">
					<div class="col-md-12 table-responsive">
						<table class="table table-bordered table-striped fill-head "
							style="width: 100%" id="table_grid">
							<thead style="background-color: #f3b5db;">
								<tr>
									<th>Sr.No.</th>
									<th>Franchise</th>
									<th>Tax%</th>
									<th>Bill Taxable</th>
									<th>Bill SGST</th>
									<th>Bill CGST</th>
									<th>Bill Total</th>
									<th>CRN Taxable</th>
									<th>CRN SGST</th>
									<th>CRN CGST</th>
									<th>CRN Total</th>
									<th>Tot.Taxable</th>
									<th>Tot.SGST</th>
									<th>Tot.CGST</th>
									<th>Tot.Total</th>
								</tr>
							</thead>
							<tbody>
								<c:set var="billTaxableAmt" value="0" />
								<c:set var="billCgstAmt" value="0" />
								<c:set var="billSgstAmt" value="0" />
								<c:set var="billIgstAmt" value="0" />
								<c:set var="billGrandAmt" value="0" />
								<c:set var="crnTaxableAmt" value="0" />
								<c:set var="crnCgstAmt" value="0" />
								<c:set var="crnSgstAmt" value="0" />
								<c:set var="crnIgstAmt" value="0" />
								<c:set var="crnGrandAmt" value="0" />
								<c:set var="actTaxableAmt" value="0" />
								<c:set var="actCgstAmt" value="0" />
								<c:set var="actSgstAmt" value="0" />
								<c:set var="actIgstAmt" value="0" />
								<c:set var="actGrandAmt" value="0" />
								<c:forEach items="${taxReportList}" var="taxList"
									varStatus="count">
									<tr>
										<c:set var="actTaxableAmt"
											value="${actTaxableAmt+(taxList.billTaxableAmt-taxList.crnTaxableAmt)}" />
										<c:set var="actCgstAmt"
											value="${actCgstAmt+(taxList.billCgstAmt-taxList.crnCgstAmt)}" />
										<c:set var="actSgstAmt"
											value="${actSgstAmt+(taxList.billSgstAmt-taxList.crnSgstAmt)}" />
										<c:set var="actIgstAmt"
											value="${actIgstAmt+(taxList.billIgstAmt-taxList.crnIgstAmt)}" />
										<c:set var="actGrandAmt"
											value="${actGrandAmt+(taxList.billGrandAmt-taxList.crnGrandAmt)}" />
										<td><c:out value="${count.index+1}" /></td>
										<td><c:out value="${taxList.frName}" /></td>
										<td style="text-align: right;"><c:out
												value="${taxList.taxPer}" /></td>
										<td style="text-align: right;"><c:out
												value="${taxList.billTaxableAmt}" /></td>
										<td style="text-align: right;"><c:out
												value="${taxList.billSgstAmt}" /></td>

										<td style="text-align: right;"><c:out
												value="${taxList.billCgstAmt}" /></td>
										<td style="text-align: right;"><c:out
												value="${taxList.billGrandAmt}" /></td>
										<td style="text-align: right;"><c:out
												value="${taxList.crnTaxableAmt}" /></td>
										<td style="text-align: right;"><c:out
												value="${taxList.crnSgstAmt}" /></td>

										<td style="text-align: right;"><c:out
												value="${taxList.crnCgstAmt}" /></td>
										<td style="text-align: right;"><c:out
												value="${taxList.crnGrandAmt}" /></td>

										<td style="text-align: right;"><fmt:formatNumber
												type="number" maxFractionDigits="2" minFractionDigits="2"
												value="${taxList.billTaxableAmt-taxList.crnTaxableAmt}" /></td>

										<td style="text-align: right;"><fmt:formatNumber
												type="number" maxFractionDigits="2" minFractionDigits="2"
												value="${taxList.billSgstAmt-taxList.crnSgstAmt}" /></td>

										<td style="text-align: right;"><fmt:formatNumber
												type="number" maxFractionDigits="2" minFractionDigits="2"
												value="${taxList.billCgstAmt-taxList.crnCgstAmt}" /></td>

										<td style="text-align: right;"><fmt:formatNumber
												type="number" maxFractionDigits="2" minFractionDigits="2"
												value="${taxList.billGrandAmt-taxList.crnGrandAmt}" /></td>

										<c:set var="billTaxableAmt"
											value="${billTaxableAmt+taxList.billTaxableAmt}" />
										<c:set var="billCgstAmt"
											value="${billCgstAmt+taxList.billCgstAmt}" />
										<c:set var="billSgstAmt"
											value="${billSgstAmt+taxList.billSgstAmt}" />
										<c:set var="billIgstAmt"
											value="${billIgstAmt+taxList.billIgstAmt}" />
										<c:set var="billGrandAmt"
											value="${billGrandAmt+taxList.billGrandAmt}" />
										<c:set var="crnTaxableAmt"
											value="${crnTaxableAmt+taxList.crnTaxableAmt}" />
										<c:set var="crnCgstAmt"
											value="${crnCgstAmt+taxList.crnCgstAmt}" />
										<c:set var="crnSgstAmt"
											value="${crnSgstAmt+taxList.crnSgstAmt}" />
										<c:set var="crnIgstAmt"
											value="${crnIgstAmt+taxList.crnIgstAmt}" />
										<c:set var="crnGrandAmt"
											value="${crnGrandAmt+taxList.crnGrandAmt}" />

									</tr>
								</c:forEach>

								<tr>
									<td></td>
									<td></td>


									<td style="text-align: left;">Total</td>
									<td style="text-align: right;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${billTaxableAmt}" /></td>
									<td style="text-align: right;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${billSgstAmt}" /></td>
									<td style="text-align: right;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${billCgstAmt}" /></td>
									<td style="text-align: right;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${billGrandAmt}" /></td>
									<td style="text-align: right;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${crnTaxableAmt}" /></td>
									<td style="text-align: right;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${crnSgstAmt}" /></td>
									<td style="text-align: right;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${crnCgstAmt}" /></td>
									<td style="text-align: right;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${crnGrandAmt}" /></td>
									<td style="text-align: left;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${actTaxableAmt}" /></td>
									<td style="text-align: left;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${actSgstAmt}" /></td>
									<td style="text-align: left;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${actCgstAmt}" /></td>
									<td style="text-align: left;"><fmt:formatNumber
											type="number" maxFractionDigits="2" minFractionDigits="2"
											value="${actGrandAmt}" /></td>
								</tr>

							</tbody>
						</table>
					</div>
					<div class="form-group" id="range">



						<div class="col-sm-3  controls">
							<input type="button" id="expExcel" class="btn btn-primary"
								value="EXPORT TO Excel" onclick="exportToExcel();">
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

	<script>
		$('.datepicker').datepicker({
			format : {
				format : 'mm/dd/yyyy',
				startDate : '-3d'
			}
		});
	</script>

	<script type="text/javascript">
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
</body>
</html>