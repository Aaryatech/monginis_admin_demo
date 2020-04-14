<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>

	<c:url var="getBillList" value="/getFrWiseItemSaleReport"></c:url>
	<c:url var="getFrListofAllFr" value="/getFrListofAllFrForFrSummery"></c:url>
	<c:url var="getAllCatByAjax" value="/getAllCatByAjax"></c:url>



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
					<i class="fa fa-file-o"></i>Item Group > Item > Shop wise Summary Report
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
				<li class="active">Item Group + Item + Shop wise Summary Report</li>
			</ul>
		</div>
		<!-- END Breadcrumb -->

		<!-- BEGIN Main Content -->
		<div class="box">
			<div class="box-title">
				<h3>
					<i class="fa fa-bars"></i>Report
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

						<label class="col-sm-3 col-lg-2 control-label"> Select
							Sub Category</label>
						<div class="col-sm-6 col-lg-4">

							<select data-placeholder="Choose Sub Category"
								class="form-control chosen" multiple="multiple" tabindex="6"
								id="selectSubCat" name="selectSubCat"
								onchange="setAllFrSelected(this.value)">

								<option value="-1"><c:out value="All" /></option>

								<c:forEach items="${subCatList}" var="sc"
									varStatus="count">
									<option value="${sc.subCatId}"><c:out value="${sc.subCatName}" /></option>
								</c:forEach>
							</select>

						</div>

						<label class="col-sm-3 col-lg-2 control-label"> Report Type</label>
						<div class="col-sm-6 col-lg-4 controls date_select">
							<select data-placeholder="Choose Type"
								class="form-control chosen" id="typeId" name="typeId">

								<option value="1">Consolidated</option>
								<option value="2">Quantity</option>
								<option value="3">Amount</option>
								<option value="4">Taxable Amount</option>

							</select>
						</div>

						
						</div>
						
						
					<div class="form-group">
						
						<div class="col-md-6" style="text-align: center;">
							<button class="btn btn-info" onclick="searchReport()">Search
								Report</button>
							<button class="btn btn-primary" value="PDF" id="PDFButton"
								onclick="genPdf()">PDF</button>
						</div>
					</div>
				</div>

				<br>


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
					<i class="fa fa-list-alt"></i>Report
				</h3>

			</div>

			<form id="submitBillForm"
				action="${pageContext.request.contextPath}/submitNewBill"
				method="post">
				<div class=" box-content">
					<div class="row">
						<div class="col-md-12 table-responsive">
							<table class="table table-bordered table-striped fill-head "
								style="width: 100%" id="table_grid">
								<thead style="background-color: #f3b5db;">
									<tr>
										<th>Item</th>
										<th>Sold Qty</th>
										<th>Sold Amt</th>
										<th>Var Qty</th>
										<th>Var Amt</th>
										<th>Ret Qty</th>
										<th>Ret Amt</th>
										<th>Net Qty</th>
										<th>Net Amt</th>
										<th>Ret Amt %</th>
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
		<p>2017 © Monginis.</p>
	</footer>

	<a id="btn-scrollup" class="btn btn-circle btn-lg" href="#"><i
		class="fa fa-chevron-up"></i></a>



	<script type="text/javascript">
		function searchReport() {
			//	var isValid = validate();

			var selectedSubCat = $("#selectSubCat").val();

			var from_date = $("#fromDate").val();
			var to_date = $("#toDate").val();
			var report_type=$("#typeId").val();
			
			

		
			
			
			if(selectedSubCat==null){
				alert("Please Select Sub Category");
			}else{
				
			
				$('#loader').show();
			$
					.getJSON(
							'${getBillList}',

							{
								sc_id_list : JSON.stringify(selectedSubCat),
								fromDate : from_date,
								toDate : to_date,
								reportType:report_type,
								ajax : 'true'

							},
							function(data) {
								
								alert("RESULT ------- "+JSON.stringify(data));

								$('#table_grid td').remove();
								$('#loader').hide();

								if (data == "") {
									alert("No records found !!");
									document.getElementById("expExcel").disabled = true;
								}else{
									document.getElementById("expExcel").disabled = false;
									document
									.getElementById('range').style.display = 'block';

								}
								var index = 0;
								$
										.each(
												data,
												function(key, sc) {
													
													
													
													var tr = $('<tr></tr>');

													
													tr.append($('<td style="font-weight:bold;"></td>')
															.html(sc.subCatName));
													tr.append($('<td></td>')
															.html(""));
													tr.append($('<td></td>')
															.html(""));
													tr.append($('<td></td>')
															.html(""));
													tr.append($('<td></td>')
															.html(""));
													tr.append($('<td></td>')
															.html(""));
													tr.append($('<td></td>')
															.html(""));
													tr.append($('<td></td>')
															.html(""));
													tr.append($('<td></td>')
															.html(""));
													tr.append($('<td></td>')
															.html(""));

													$('#table_grid tbody')
															.append(tr);

													var scTotalSoldQty = 0;
													var scTotalSoldAmt = 0;

													var scTotalVarQty = 0;
													var scTotalVarAmt = 0;

													var scTotalRetQty = 0;
													var scTotalRetAmt = 0;
													
													var scTotalNetQty = 0;
													var scTotalNetAmt = 0;
													
													var scTotalRetAmtPer = 0;

													$
															.each(
																	sc.itemList,
																	function(
																			key,
																			item) {

																		var tr = $('<tr></tr>');

																		
																		tr.append($('<td style="font-weight:bold;"></td>')
																				.html(item.itemName));
																		tr.append($('<td></td>')
																				.html(""));
																		tr.append($('<td></td>')
																				.html(""));
																		tr.append($('<td></td>')
																				.html(""));
																		tr.append($('<td></td>')
																				.html(""));
																		tr.append($('<td></td>')
																				.html(""));
																		tr.append($('<td></td>')
																				.html(""));
																		tr.append($('<td></td>')
																				.html(""));
																		tr.append($('<td></td>')
																				.html(""));
																		tr.append($('<td></td>')
																				.html(""));

																		$('#table_grid tbody')
																				.append(tr);

																		var totalSoldQty = 0;
																		var totalSoldAmt = 0;

																		var totalVarQty = 0;
																		var totalVarAmt = 0;

																		var totalRetQty = 0;
																		var totalRetAmt = 0;
																		
																		var totalNetQty = 0;
																		var totalNetAmt = 0;

																		var totalRetAmtPer = 0;
																		
																		
																		$
																		.each(
																				item.frList,
																				function(
																						key,
																						fr) {

																					var tr = $('<tr></tr>');

																					if(report_type==1){
																						tr.append($('<td></td>')
																								.html(fr.frName));
																						tr.append($('<td></td>')
																								.html(fr.frSoldQty));
																						tr.append($('<td></td>')
																								.html(fr.frSoldAmt));
																						tr.append($('<td></td>')
																								.html(fr.frVarQty));
																						tr.append($('<td></td>')
																								.html(fr.frVarAmt));
																						tr.append($('<td></td>')
																								.html(fr.frRetQty));
																						tr.append($('<td></td>')
																								.html(fr.frRetAmt));
																						
																					var netQty=(fr.frSoldQty-(fr.frVarQty+fr.frRetQty));
																					var netAmt=(fr.frSoldAmt-(fr.frVarAmt+fr.frRetAmt));
																						
																						
																						tr.append($('<td></td>')
																								.html(""+netQty));
																						tr.append($('<td></td>')
																								.html(""+netAmt.toFixed(2)));
																						
																						var retAmtPer=((fr.frVarAmt+fr.frRetAmt)*100)/fr.frSoldAmt;
																						
																						tr.append($('<td></td>')
																								.html(retAmtPer.toFixed(2)+"%"));

																					}else if(report_type==2){
																						tr.append($('<td></td>')
																								.html(fr.frName));
																						tr.append($('<td></td>')
																								.html(fr.frSoldQty));
																						tr.append($('<td></td>')
																								.html(""));
																						tr.append($('<td></td>')
																								.html(fr.frVarQty));
																						tr.append($('<td></td>')
																								.html(""));
																						tr.append($('<td></td>')
																								.html(fr.frRetQty));
																						tr.append($('<td></td>')
																								.html(""));
																						
																						var netQty=(fr.frSoldQty-(fr.frVarQty+fr.frRetQty));
																						var netAmt=(fr.frSoldAmt-(fr.frVarAmt+fr.frRetAmt));
																						
																						tr.append($('<td></td>')
																								.html(netQty));
																						tr.append($('<td></td>')
																								.html(""));
																						tr.append($('<td></td>')
																								.html(""));

																					}else if(report_type==3){
																					
																					tr.append($('<td></td>')
																							.html(fr.frName));
																					tr.append($('<td></td>')
																							.html(""));
																					tr.append($('<td></td>')
																							.html(fr.frSoldAmt));
																					tr.append($('<td></td>')
																							.html(""));
																					tr.append($('<td></td>')
																							.html(fr.frVarAmt));
																					tr.append($('<td></td>')
																							.html(""));
																					tr.append($('<td></td>')
																							.html(fr.frRetAmt));
																					
																					var netQty=(fr.frSoldQty-(fr.frVarQty+fr.frRetQty));
																					var netAmt=(fr.frSoldAmt-(fr.frVarAmt+fr.frRetAmt));
																					
																					tr.append($('<td></td>')
																							.html(""));
																					tr.append($('<td></td>')
																							.html(netAmt.toFixed(2)));
																					
																					var retAmtPer=((fr.frVarAmt+fr.frRetAmt)*100)/fr.frSoldAmt;
																					
																					tr.append($('<td></td>')
																							.html(retAmtPer.toFixed(2)+"%"));

																					
																					
																					}else if(report_type==4){
																						tr.append($('<td></td>')
																								.html(fr.frName));
																						tr.append($('<td></td>')
																								.html(fr.frSoldQty));
																						tr.append($('<td></td>')
																								.html(fr.frTaxableAmt));
																						tr.append($('<td></td>')
																								.html(fr.frVarQty));
																						tr.append($('<td></td>')
																								.html(fr.frVarTaxableAmt));
																						tr.append($('<td></td>')
																								.html(fr.frRetQty));
																						tr.append($('<td></td>')
																								.html(fr.frRetTaxableAmt));
																						
																						var netQty=(fr.frSoldQty-(fr.frVarQty+fr.frRetQty));
																						var netAmt=(fr.frTaxableAmt-(fr.frVarTaxableAmt+fr.frRetTaxableAmt));
																						
																						tr.append($('<td></td>')
																								.html(netQty));
																						tr.append($('<td></td>')
																								.html(netAmt.toFixed(2)));
																						
																						var retAmtPer=((fr.frVarTaxableAmt+fr.frRetTaxableAmt)*100)/fr.frTaxableAmt;
																						
																						tr.append($('<td></td>')
																								.html(retAmtPer.toFixed(2)+"%"));

																					}
																					
																					if(report_type==4){
																						totalSoldQty=totalSoldQty+fr.frSoldQty;
																						totalSoldAmt=totalSoldAmt+fr.frTaxableAmt;

																						totalVarQty=totalVarQty+fr.frVarQty;
																						totalVarAmt=totalVarAmt+fr.frVarTaxableAmt;

																						totalRetQty=totalRetQty+fr.frRetQty;
																						totalRetAmt=totalRetAmt+fr.frRetTaxableAmt;
																						
																						totalNetQty=totalNetQty+(fr.frSoldQty-(fr.frVarQty+fr.frRetQty));
																						totalNetAmt=totalNetAmt+(fr.frTaxableAmt-(fr.frVarTaxableAmt+fr.frRetTaxableAmt));
																						
																						
																						var retAmtPer=((fr.frVarTaxableAmt+fr.frRetTaxableAmt)*100)/fr.frTaxableAmt;
																						
																						totalRetAmtPer=totalRetAmtPer+retAmtPer;
																						
																					}else{
																						totalSoldQty=totalSoldQty+fr.frSoldQty;
																						totalSoldAmt=totalSoldAmt+fr.frSoldAmt;

																						totalVarQty=totalVarQty+fr.frVarQty;
																						totalVarAmt=totalVarAmt+fr.frVarAmt;

																						totalRetQty=totalRetQty+fr.frRetQty;
																						totalRetAmt=totalRetAmt+fr.frRetAmt;

																						totalNetQty=totalNetQty+(fr.frSoldQty-(fr.frVarQty+fr.frRetQty));
																						totalNetAmt=totalNetAmt+(fr.frSoldAmt-(fr.frVarAmt+fr.frRetAmt));
																						
																						var retAmtPer=((fr.frVarAmt+fr.frRetAmt)*100)/fr.frSoldAmt;
																						
																						totalRetAmtPer=totalRetAmtPer+retAmtPer;

																					}


																					
																					$('#table_grid tbody')
																							.append(tr);

																					

																				})
																				
																				
																				var tr = $('<tr></tr>');

																		if(report_type==1){
																		
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(item.itemName));
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(totalSoldQty));
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(totalSoldAmt.toFixed(2)));
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(totalVarQty));
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(totalVarAmt.toFixed(2)));
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(totalRetQty));
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(totalRetAmt.toFixed(2)));
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(totalNetQty));
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(totalNetAmt.toFixed(2)));
																		tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																				.html(totalRetAmtPer.toFixed(2)+"%"));
																		
																		}else if(report_type==2){
																			
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(item.itemName));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalSoldQty));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(""));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalVarQty));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(""));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalRetQty));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(""));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalNetQty));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(""));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(""));
																			
																		}else if(report_type==3){
																			
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(item.itemName));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(""));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalSoldAmt.toFixed(2)));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(""));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalVarAmt.toFixed(2)));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(""));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalRetAmt.toFixed(2)));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(""));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalNetAmt.toFixed(2)));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalRetAmtPer.toFixed(2)+"%"));
																						
																		}else if(report_type==4){
																			
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(item.itemName));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalSoldQty));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalSoldAmt.toFixed(2)));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalVarQty));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalVarAmt.toFixed(2)));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalRetQty));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalRetAmt.toFixed(2)));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalNetQty));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalNetAmt.toFixed(2)));
																			tr.append($('<td style="font-weight:bold; background-color: #f7d7eb; color: black"></td>')
																					.html(totalRetAmtPer.toFixed(2)+"%"));
																			
																			} 
																
																																		
																		$('#table_grid tbody')
																				.append(tr);
																	
																		scTotalSoldQty=scTotalSoldQty+totalSoldQty;
																		scTotalSoldAmt=scTotalSoldAmt+totalSoldAmt;

																		scTotalVarQty=scTotalVarQty+totalVarQty;
																		scTotalVarAmt=scTotalVarAmt+totalVarAmt;

																		scTotalRetQty=scTotalRetQty+totalRetQty;
																		scTotalRetAmt=scTotalRetAmt+totalRetAmt;
																		
																		scTotalNetQty=scTotalNetQty+totalNetQty;
																		scTotalNetAmt=scTotalNetAmt+totalNetAmt;
																		
																		scTotalRetAmtPer=scTotalRetAmtPer+totalRetAmtPer;


																	})

													var tr = $('<tr></tr>');
													
													if(report_type==1){

													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(sc.subCatName));
													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(scTotalSoldQty));
													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(scTotalSoldAmt.toFixed(2)));
													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(scTotalVarQty));
													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(scTotalVarAmt.toFixed(2)));
													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(scTotalRetQty));
													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(scTotalRetAmt.toFixed(2)));
													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(scTotalNetQty));
													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(scTotalNetAmt.toFixed(2)));
													tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
															.html(scTotalRetAmtPer.toFixed(2)+"%"));

													} else if(report_type==2){

														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(sc.subCatName));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalSoldQty));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(""));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalVarQty));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(""));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalRetQty));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(""));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalNetQty));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(""));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(""));

													} else if(report_type==3){

														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(sc.subCatName));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(""));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalSoldAmt.toFixed(2)));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(""));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalVarAmt.toFixed(2)));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(""));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalRetAmt.toFixed(2)));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(""));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalNetAmt.toFixed(2)));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalRetAmtPer.toFixed(2)+"%"));

													}else if(report_type==4){

														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(sc.subCatName));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalSoldQty));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalSoldAmt.toFixed(2)));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalVarQty));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalVarAmt.toFixed(2)));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalRetQty));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalRetAmt.toFixed(2)));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalNetQty));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalNetAmt.toFixed(2)));
														tr.append($('<td style="font-weight:bold; background-color: #343690; color: white"></td>')
																.html(scTotalRetAmtPer.toFixed(2)+"%"));

														}
													
													
													
													$('#table_grid tbody')
															.append(tr);

													var tr = $('<tr></tr>');

													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white;"></td>')
															.html("0"));
													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white"></td>')
															.html(""));
													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white"></td>')
															.html(""));
													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white"></td>')
															.html(""));
													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white"></td>')
															.html(""));
													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white"></td>')
															.html(""));
													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white"></td>')
															.html(""));
													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white"></td>')
															.html(""));
													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white"></td>')
															.html(""));
													tr.append($('<td style="font-weight:bold; background-color: #ffffff; color: white"></td>')
															.html(""));

													$('#table_grid tbody')
															.append(tr);
													
													

												})
												
												


								

							});
			
		}

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

		function genPdf() {
			var from_date = $("#fromDate").val();
			var to_date = $("#toDate").val();
			var selectedSC = $("#selectSubCat").val();
			var selectedType = $("#typeId").val();

			window
					.open('${pageContext.request.contextPath}/pdfForReport?url=pdf/showFrWiseItemSalePdf/'
							+ from_date + '/' + to_date + '/' + selectedSC+'/'+selectedType);

		}
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

		function exportToExcel() {

			window.open("${pageContext.request.contextPath}/exportToExcelNew");
			document.getElementById("expExcel").disabled = true;
		}
	</script>

	<script>
		function setAllFrSelected(frId) {
			//alert("hii")
			if (frId == -1) {

				$.getJSON('${getFrListofAllFr}', {

					ajax : 'true'
				},
						function(data) {

							var len = data.length;

							//alert(len);

							$('#selectFr').find('option').remove().end()
							$("#selectFr").append(
									$("<option value='-1'>All</option>"));
							for (var i = 0; i < len; i++) {
								$("#selectFr").append(
										$("<option selected ></option>").attr(
												"value", data[i].frId).text(
												data[i].frName));
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
</body>
</html>