<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>

<style>
#pieChart1 {
	width: 100%;
	height: 200px;
	display: block;
}
</style>

<body onload="radioSelection(${radio});drawGraph();drawChart()">
	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
	<!-- BEGIN Container -->

	<c:url var="getCatGraphData" value="/getCatGraphData"></c:url>
	<c:url var="getSubCatDataByCat" value="/getSubCatDataByCat"></c:url>
	<c:url var="getSubCatDataByCatForFilter"
		value="/getSubCatDataByCatForFilter"></c:url>

	<c:url var="getItemListBySubCat" value="/getItemListBySubCat"></c:url>

	<c:url var="getItemListBySubCatForFilter"
		value="/getItemListBySubCatForFilter"></c:url>

	<c:url var="getFrGraphData" value="/getFrGraphData"></c:url>

	<c:url var="getRouteGraphData" value="/getRouteGraphData"></c:url>

	<c:url var="getFrGraphDataByRoute" value="/getFrGraphDataByRoute"></c:url>



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
						<i class="fa fa-dashboard"></i> Sales Dashboard
					</h1>
					<!--<h4>Overview, stats, chat and more</h4>-->
				</div>
			</div>
			<!-- END Page Title -->


			<form style="background: #FFF; padding:10px;"
				action="${pageContext.request.contextPath}/getSalesDashboardData">

				<div class="container" id="main-container">
					<div class="col-md-1">
						<label class="radio-inline" style="margin: 6px 0 0 0;"> <input type="radio"
							name="rdDate" id="rdDate1" value="1" ${radio==1 ? 'checked' : ''}
							checked="checked" onchange="radioSelection(this.value)" />
							Today's
						</label>
					</div>

					<div class="col-md-1">
						<label class="radio-inline" style="margin: 6px 0 0 0;"> <input type="radio"
							name="rdDate" id="rdDate2" value="2" ${radio==2 ? 'checked' : ''}
							onchange="radioSelection(this.value)"> Weekly
						</label>
					</div>

					<div class="col-md-1">
						<label class="radio-inline" style="margin: 6px 0 0 0;"> <input type="radio"
							name="rdDate" id="rdDate3" value="3" ${radio==3 ? 'checked' : ''}
							onchange="radioSelection(this.value)"> Monthly
						</label>
					</div>

					<div class="col-md-1">
						<label class="radio-inline" style="margin: 6px 0 0 0;"> <input type="radio"
							name="rdDate" id="rdDate4" value="4" ${radio==4 ? 'checked' : ''}
							onchange="radioSelection(this.value)"> Custom
						</label>
					</div>


					<div class="col-md-6" id="customDiv" style="display: none;">
						<div class="row">

							<label class="col-md-1 control-label" style="text-align: right; margin:7px 0 0 0;">From</label>
							<div class="col-md-5">
								<input class="form-control" placeholder="Date" name="fromDate"
									style="border-radius: 25px;" id="fromDate" type="date"
									format="dd-mm-yyyy" value="${fromDate}" required />
							</div>

							<label class="col-md-1 control-label" style="text-align: right; margin:7px 0 0 0;">To</label>

							<div class="col-md-5">
								<input class="form-control" placeholder="Date" name="toDate"
									style="border-radius: 25px;" id="toDate" type="date"
									format="dd-mm-yyyy" value="${toDate}" required />
							</div>

						</div>


					</div>
					<div class="col-md-2">
						<input type="submit" name="Search" id="submit"
							class="btn btn-primary" />
					</div>
				</div>
			</form>
  

			<!-- BEGIN Tiles -->
			<div class="row">
				
				
					<div class="col-md-4">
						<div class="total_one bg_one">
							<div class="total_l"><i class="fa fa-dashboard"></i></div>
							<div class="total_r">
								<fmt:formatNumber var="sale" minFractionDigits="2"
							maxFractionDigits="2" type="number" value="${totalAmt.totalSale}" />

						<label class="control-label total_head">Total Sale</label> <label
							class="control-label total_subhead">${sale}</label>
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="total_one bg_two">
							<div class="total_l"><i class="fa fa-dashboard"></i></div>
							<div class="total_r"><fmt:formatNumber var="crn" minFractionDigits="2"
							maxFractionDigits="2" type="number" value="${totalAmt.totalCrn}" />

						<label class="control-label total_head">Credit Note Sale</label> <label
							class="control-label total_subhead">${crn}</label></div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="total_one bg_three">
							<div class="total_l"><i class="fa fa-dashboard"></i></div>
							<div class="total_r"><fmt:formatNumber var="net" minFractionDigits="2"
							maxFractionDigits="2" type="number" value="${totalAmt.netTotal}" />

						<label class="control-label total_head">Net Sale</label> <label
							class="control-label total_subhead">${net}</label></div>
						</div>
					</div>

					


				
			</div>
			
		
			<div style="background: #FFF; padding:10px; margin: 0 0 20px 0;">
			<div class="row">
				<div class="col-md-3">

					<label class="radio-inline"> <input type="radio"
						name="rdSale" id="rdFrSale" value="1"
						${radioFrRoute==1 ? 'checked' : ''} checked="checked"
						onchange="radioSaleSelection(this.value)" /> Franchisee Wise Sale
					</label>

				</div>

				<div class="col-md-3">
					<label class="radio-inline"> <input type="radio"
						name="rdSale" id="rdRouteSale" value="2"
						${radioFrRoute==2 ? 'checked' : ''}
						onchange="radioSaleSelection(this.value)" /> Route Wise Sale
					</label>

				</div>

				<div class="col-md-6"></div>

			</div> </div>

			<div class="col-md-12" id="frDiv" style="background: #FFF; padding: 10px;">
				<div>

					<div id="frBarChart1"></div>

				</div>
			</div>
			
			
			
			<div id="routeDiv" style="display: none;">
				
				<div class="col-md-6" style="background: #FFF; padding: 10px;">
					<div id="routeBarChart"></div>
				</div>

				<div class="col-md-6" style="background: #FFF; padding: 10px;">
					<div id="frBarChart2"></div>
				</div>
				

			</div>


			<div style="margin: 20px 0;">
				<div class="row">

					<div class="col-md-8">
						<div  style="background: #FFF; padding: 10px;">
							<div id="lineChart1"></div>
						</div>

					</div>
					
					<div class="col-md-4">
					
						<div class="col-md-12">

						<div ><!-- class="row" -->
							<div style="background: #FFF; padding: 10px;">
								<label class="radio-inline"> <input type="radio"
									name="rdPie" id="rdPieSale" value="1"
									${radioPie==1 ? 'checked' : ''} checked="checked"
									onchange="drawPieChartByFilter(this.value)" /> Sale
								</label> <label class="radio-inline"> <input type="radio"
									name="rdPie" id="rdPieCrn" value="2"
									${radioPie==2 ? 'checked' : ''}
									onchange="drawPieChartByFilter(this.value)" /> Credit Note
								</label> <label class="radio-inline"> <input type="radio"
									name="rdPie" id="rdPieNet" value="3"
									${radioPie==3 ? 'checked' : ''}
									onchange="drawPieChartByFilter(this.value)" /> Net
								</label>

							</div>
						</div>

						<div class="row" style="display: inline-block; width: 100%;">
							<div
								style="float: none !important; width: 100% !important; margin: 0 0 15px 0;">
								<div id="pieChart1"></div>
							</div>

							<div class="clr"></div>

						</div>

					</div>
					
					
					<div class="col-md-12">

						<div class="row">
							<div class="table-responsive">
								<div
									style="overflow: scroll; height: 100%; width: 100%; overflow: auto">

									<table class="table table-bordered table-striped fill-head "
										style="width: 100%" id="table_grid1">
										<thead style="background-color: #ec268f;">
											<tr>
												<th style="text-align: center;">Sub Category</th>
												<th style="text-align: center;">Amount</th>

											</tr>
										</thead>
										<tbody>

										</tbody>
									</table>
								</div>
							</div>
						</div>

					</div>
					
					</div>

					

					

				</div>
			</div>

			

			<div class="row">
				<div class="col-md-12 table-responsive">
					<div
						style="overflow: scroll; height: 100%; width: 100%; overflow: auto">

						<table class="table table-bordered table-striped fill-head "
							style="width: 100%" id="table_grid">
							<thead style="background-color: #ec268f;">
								<tr>
									<th style="text-align: center;">Sr.No.</th>
									<th style="text-align: center;">Item Name</th>
									<th style="text-align: center;">Sale Qty</th>
									<th style="text-align: center;">Sale Amt</th>
									<th style="text-align: center;">CRN Qty</th>
									<th style="text-align: center;">CRN Amt</th>
									<th style="text-align: center;">Net Qty</th>
									<th style="text-align: center;">Net Amt</th>
									<th style="text-align: center;">Contribution</th>

								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
					</div>
				</div>
			</div>

			


			<footer>
			<p>2018 © MONGINIS.</p>
			</footer>

			<a id="btn-scrollup" class="btn btn-circle btn-lg" href="#"><i
				class="fa fa-chevron-up"></i></a>

		</div>
		<!-- END Content -->
	</div>
	<!-- END Container -->


	<script type="text/javascript">
		function radioSaleSelection(val) {
			if (val == 1) {
				$('#frDiv').show();
				$('#routeDiv').hide();
			} else {
				$('#frDiv').hide();
				$('#routeDiv').show();
				drawRouteBarChart();
			}
		}
	</script>


	<script type="text/javascript">
		function radioSelection(val) {

			if (val == 4) {
				$('#customDiv').show();
			} else {
				$('#customDiv').hide();
			}
		}
	</script>


	<script type="text/javascript">
		function drawGraph() {

			//alert("hi - ");

			 google.charts.load('current', {
				'packages' : [ 'corechart', 'bar' ]
			});
			google.charts.setOnLoadCallback(drawStuffCat); 
			
			

			google.charts.load("current", {
				packages : [ "corechart" ]
			});
			google.charts.setOnLoadCallback(drawPieChart1);

			google.charts.load('current', {
				'packages' : [ 'corechart', 'bar' ]
			});
			google.charts.setOnLoadCallback(drawFrBarChart);
			
			google.charts.load('current', {
				'packages' : [ 'corechart', 'bar' ]
			});
			google.charts.setOnLoadCallback(drawRouteBarChart);
			
			google.charts.load('current', {
				'packages' : [ 'corechart', 'bar' ]
			});
			google.charts.setOnLoadCallback(drawFrRouteWiseBarChart);

			var type = $
			{
				type
			}
			;

		}
	</script>

	<script type="text/javascript">
		function drawStuffCat() {

			var chartDiv = document.getElementById('lineChart1');

			var dataTable = new google.visualization.DataTable();

			dataTable.addColumn('string', 'Category'); // Implicit domain column.
			//dataTable.addColumn('number', 'Sale'); // Implicit data column. 
			dataTable.addColumn('number', 'Credit Note');
			dataTable.addColumn('number', 'Net');

			//alert("in");

			$.getJSON('${getCatGraphData}',

			{
				ajax : 'true'

			}, function(chartsBardata) {

				//alert(JSON.stringify(chartsBardata));
				var len = chartsBardata.length;
				//alert("LEN - " + len);

				$.each(chartsBardata, function(key, chartsBardata) {

					dataTable.addRows([ [ chartsBardata.catName,
							//parseInt(chartsBardata.sale),
							parseInt(chartsBardata.crn),
							parseInt(chartsBardata.net) ] ]);

				});

				//alert(11);

				var materialOptions = {
					width : 600,
					height : 400,
					//isStacked: 'percent',
					bar: { groupWidth: '45%' },
					isStacked:true,
					chart : {
						title : ' ',
						subtitle : ' '
					},
					series : {
						0 : {
							axis : 'distance'
						}
					// Bind series 1 to an axis named 'brightness'.
					},
					axes : {
						y : {
							distance : {
								label : 'Amount'
							}
						// Left y-axis.

						}
					}
				};

				var materialChart = new google.visualization.ColumnChart(chartDiv);

				function selectQtyHandler() {
					// alert("hii");
					var selectedItem = materialChart.getSelection()[0];
					if (selectedItem) {
						// alert("hii selectedItem");
						i = selectedItem.row, 0;

						//alert("hii selectedItem" + chartsBardata[i].catId);
						drawPieChart1(chartsBardata[i].catId,
								chartsBardata[i].catName);

					}
				}

				google.visualization.events.addListener(materialChart,
						'select', selectQtyHandler);

				function drawMaterialChart() {
					materialChart.draw(dataTable, google.charts.Bar
							.convertOptions(materialOptions));
				}

				drawMaterialChart();
				drawPieChart1(chartsBardata[len - 1].catId,
						chartsBardata[len - 1].catName);

			});

		}
	</script>


	<script type="text/javascript">
		function drawPieChart1(catId, catName) {
			//alert("hii donut ch");
			//to draw donut chart

			var fromDate = document.getElementById('fromDate').value;
			var toDate = document.getElementById('toDate').value;
			var radio = 0;

			if (document.getElementById('rdDate1').checked) {
				radio = 1;
			} else if (document.getElementById('rdDate2').checked) {
				radio = 2;
			} else if (document.getElementById('rdDate3').checked) {
				radio = 3;
			} else if (document.getElementById('rdDate4').checked) {
				radio = 4;
			}

			var type = 1;
			if (document.getElementById('rdPieSale').checked) {
				type = 1;
			} else if (document.getElementById('rdPieCrn').checked) {
				type = 2;
			} else if (document.getElementById('rdPieNet').checked) {
				type = 3;
			}

			//alert(type);
			//alert(fromDate);

			var chart;
			var datag = '';
			var a = "";
			var dataSale = [];
			var Header = [ 'Category', 'Amount', 'ID' ];
			dataSale.push(Header);

			$.getJSON('${getSubCatDataByCat}', {
				radio : radio,
				fromDate : fromDate,
				toDate : toDate,
				catId : catId,
				ajax : 'true'
			}, function(chartsdata) {
				//alert("---" + JSON.stringify(chartsdata));

				$('#table_grid1 td').remove();
					
				var len = chartsdata.length;
				datag = datag + '[';
				$.each(chartsdata, function(key, chartsdata) {
					var temp = [];

					if (type == 1) {
						var sale = parseFloat(chartsdata.sale);
						if (sale < 0) {
							sale = 0;
						}

						temp.push(chartsdata.subCatName + " ("
								+ (parseFloat(sale).toFixed(2)) + ")",
								(parseFloat(sale)),
								parseInt(chartsdata.subCatId));

					} else if (type == 2) {

						var crn = parseFloat(chartsdata.crn);
						if (crn < 0) {
							crn = 0;
						}

						temp.push(chartsdata.subCatName + " ("
								+ (parseFloat(crn).toFixed(2)) + ")",
								(parseFloat(crn)),
								parseInt(chartsdata.subCatId));

					} else if (type == 3) {

						var net = parseFloat(chartsdata.net);
						if (net < 0) {
							net = 0;
						}

						temp.push(chartsdata.subCatName + " ("
								+ (parseFloat(net).toFixed(2)) + ")",
								(parseFloat(net)),
								parseInt(chartsdata.subCatId));

					}

					dataSale.push(temp);
					
					
					//SET TABLE

					var tr = $('<tr></tr>');

					tr.append($('<td></td>').html(chartsdata.subCatName));

					if (type == 1) {
						tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(sale))));
					}else if (type == 2) {
						tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(crn))));
					}else if (type == 3) {
						tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(net))));
					}
					

					$('#table_grid1 tbody').append(tr);

				});

				//console.log(dataSale);
				var data1 = google.visualization.arrayToDataTable(dataSale);

				var options = {
					width:400,
	                height:200,
					title : ' ',
					pieHole : 0.4,
					backgroundColor : 'transparent',
					pieSliceText : 'none',
					sliceVisibilityThreshold : 0,
					legend : {
						position : 'none',
						labeledValueText : 'both',
						textStyle : {
							color : 'red',
							fontSize : 10
						}
					},
					is3D : true,
				};
				//  alert(222);
				chart = new google.visualization.PieChart(document
						.getElementById('pieChart1'));

				function selectQtyHandler() {
					// alert("hii");
					var selectedItem = chart.getSelection()[0];
					if (selectedItem) {
						// alert("hii selectedItem");
						i = selectedItem.row, 0;

						getItemListBySubCatId(chartsdata[i].subCatId,
								chartsdata[i].catId, chartsdata[i].subCatName);

					}
				}

				google.visualization.events.addListener(chart, 'select',
						selectQtyHandler);
				chart.draw(data1, options);

				getItemListBySubCatId(chartsdata[0].subCatId,
						chartsdata[0].catId, chartsdata[0].subCatName);

			});

		}
	</script>


	<script type="text/javascript">
		function drawPieChartByFilter(type) {
			//alert("hii donut ch");
			//to draw donut chart

			var chart;
			var datag = '';
			var a = "";
			var dataSale = [];
			var Header = [ 'Category', 'Amount', 'ID' ];
			dataSale.push(Header);

			$.getJSON('${getSubCatDataByCatForFilter}', {
				ajax : 'true'
			}, function(chartsdata) {
				//alert("---" + JSON.stringify(chartsdata));
				
				$('#table_grid1 td').remove();

				var len = chartsdata.length;
				datag = datag + '[';
				$.each(chartsdata, function(key, chartsdata) {
					var temp = [];

					if (type == 1) {
						var sale = parseFloat(chartsdata.sale);
						if (sale < 0) {
							sale = 0;
						}

						temp.push(chartsdata.subCatName + " ("
								+ (parseFloat(sale).toFixed(2)) + ")",
								(parseFloat(sale)),
								parseInt(chartsdata.subCatId));

					} else if (type == 2) {

						var crn = parseFloat(chartsdata.crn);
						if (crn < 0) {
							crn = 0;
						}

						temp.push(chartsdata.subCatName + " ("
								+ (parseFloat(crn).toFixed(2)) + ")",
								(parseFloat(crn)),
								parseInt(chartsdata.subCatId));

					} else if (type == 3) {

						var net = parseFloat(chartsdata.net);
						if (net < 0) {
							net = 0;
						}

						temp.push(chartsdata.subCatName + " ("
								+ (parseFloat(net).toFixed(2)) + ")",
								(parseFloat(net)),
								parseInt(chartsdata.subCatId));

					}

					dataSale.push(temp);
					
					//SET TABLE
					
					var tr = $('<tr></tr>');

					tr.append($('<td></td>').html(chartsdata.subCatName));

					if (type == 1) {
						tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(sale))));
					}else if (type == 2) {
						tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(crn))));
					}else if (type == 3) {
						tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(net))));
					}
					

					$('#table_grid1 tbody').append(tr);

				});

				//console.log(dataSale);
				var data1 = google.visualization.arrayToDataTable(dataSale);

				var options = {
					title : ' ',
					pieHole : 0.4,
					backgroundColor : 'transparent',
					pieSliceText : 'none',
					sliceVisibilityThreshold : 0,
					legend : {
						position : 'none',
						labeledValueText : 'both',
						textStyle : {
							color : 'red',
							fontSize : 10
						}
					},
					is3D : true,
				};
				//  alert(222);
				chart = new google.visualization.PieChart(document
						.getElementById('pieChart1'));

				function selectQtyHandler() {
					// alert("hii");
					var selectedItem = chart.getSelection()[0];
					if (selectedItem) {
						// alert("hii selectedItem");
						i = selectedItem.row, 0;

						getItemListBySubCatId(chartsdata[i].subCatId,
								chartsdata[i].catId, chartsdata[i].subCatName);

					}
				}

				google.visualization.events.addListener(chart, 'select',
						selectQtyHandler);
				chart.draw(data1, options);

				getItemListBySubCatId(chartsdata[0].subCatId,
						chartsdata[0].catId, chartsdata[0].subCatName);

			});

		}
	</script>


	<script type="text/javascript">
		function getItemListBySubCatId(id, catId, name) {

			var fromDate = document.getElementById('fromDate').value;
			var toDate = document.getElementById('toDate').value;
			var radio = 0;

			if (document.getElementById('rdDate1').checked) {
				radio = 1;
			} else if (document.getElementById('rdDate2').checked) {
				radio = 2;
			} else if (document.getElementById('rdDate3').checked) {
				radio = 3;
			} else if (document.getElementById('rdDate4').checked) {
				radio = 4;
			}

			var type = 1;
			if (document.getElementById('rdPieSale').checked) {
				type = 1;
			} else if (document.getElementById('rdPieCrn').checked) {
				type = 2;
			} else if (document.getElementById('rdPieNet').checked) {
				type = 3;
			}

			$.getJSON('${getItemListBySubCat}', {
				subCatId : id,
				catId : catId,
				fromDate : fromDate,
				toDate : toDate,
				radio : radio,
				ajax : 'true'
			}, function(data) {

				$('#table_grid td').remove();
				
				var saleTotal=0;
				var saleQtyTotal=0;
				var crnTotal=0;
				var crnQtyTotal=0;
				var netTotal=0;
				var netQtyTotal=0;
				
				var tot=0;
				var contriTot=0;
				
				$.each(data, function(key, item) {
					tot=tot+parseFloat(item.net);
				});

				$.each(data, function(key, item) {

					var index = key + 1;

					var tr = $('<tr></tr>');

					tr.append($('<td></td>').html(key + 1));

					tr.append($('<td></td>').html(item.itemName));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.saleQty))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.sale))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.crnQty))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.crn))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.netQty))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.net))));
					
					var contri=0;
					if(tot>0){
						contri=(parseFloat(item.net)*100)/tot;	
					}
					
					contriTot=contriTot+contri;
					
					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseFloat(contri).toFixed(2))));

					$('#table_grid tbody').append(tr);
					
					saleTotal=saleTotal+item.sale;
					saleQtyTotal=saleQtyTotal+item.saleQty;
					crnTotal=crnTotal+item.crn;
					crnQtyTotal=crnQtyTotal+item.crnQty;
					netTotal=netTotal+item.net;
					netQtyTotal=netQtyTotal+item.netQty;

				})
				
				var tr = $('<tr></tr>');

				tr.append($('<td style="font-weight: bold;" colspan=2></td>').html("TOTAL"));

				//tr.append($('<td style="font-weight: bold;"></td>').html(" "));

				tr.append($('<td style="font-weight: bold; text-align:right;"></td>').html(addCommas(parseInt(saleQtyTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(saleTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(crnQtyTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(crnTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(netQtyTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(netTotal))));
				
				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseFloat(contriTot).toFixed(2))));

				$('#table_grid tbody').append(tr);

			});
		}
	</script>


	<script type="text/javascript">
		function getItemListBySubCatIdForFilter() {

			var type = 1;
			if (document.getElementById('rdPieSale').checked) {
				type = 1;
			} else if (document.getElementById('rdPieCrn').checked) {
				type = 2;
			} else if (document.getElementById('rdPieNet').checked) {
				type = 3;
			}

			$.getJSON('${getItemListBySubCatForFilter}', {
				ajax : 'true'
			}, function(data) {

				$('#table_grid td').remove();
				
				var saleTotal=0;
				var saleQtyTotal=0;
				var crnTotal=0;
				var crnQtyTotal=0;
				var netTotal=0;
				var netQtyTotal=0;
				
				var tot=0;
				var contriTot=0;
				
				$.each(data, function(key, item) {
					tot=tot+parseFloat(item.net);
				});

				$.each(data, function(key, item) {

					var index = key + 1;

					var tr = $('<tr></tr>');

					tr.append($('<td></td>').html(key + 1));

					tr.append($('<td></td>').html(item.itemName));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.saleQty))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.sale))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.crnQty))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.crn))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.netQty))));

					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseInt(item.net))));
					
					var contri=0;
					if(tot>0){
						contri=(parseFloat(item.net)*100)/tot;	
					}
					
					contriTot=contriTot+contri;
					
					tr.append($('<td style="text-align:right;"></td>').html(addCommas(parseFloat(contri).toFixed(2))));

					$('#table_grid tbody').append(tr);
					
					saleTotal=saleTotal+item.sale;
					saleQtyTotal=saleQtyTotal+item.saleQty;
					crnTotal=crnTotal+item.crn;
					crnQtyTotal=crnQtyTotal+item.crnQty;
					netTotal=netTotal+item.net;
					netQtyTotal=netQtyTotal+item.netQty;

				})

				var tr = $('<tr></tr>');

				tr.append($('<td style="font-weight: bold;" colspan=2></td>').html("TOTAL"));

				//tr.append($('<td style="font-weight: bold;"></td>').html(" "));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(saleQtyTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(saleTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(crnQtyTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(crnTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(netQtyTotal))));

				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseInt(netTotal))));
				
				tr.append($('<td style="font-weight: bold;  text-align:right;"></td>').html(addCommas(parseFloat(contriTot).toFixed(2))));

				$('#table_grid tbody').append(tr);
				
			});
		}
	</script>



	<script type="text/javascript">
		function drawFrBarChart() {

			var chartDiv = document.getElementById('frBarChart1');

			var dataTable = new google.visualization.DataTable();

			dataTable.addColumn('string', 'Franchisee'); // Implicit domain column.
			dataTable.addColumn('number', 'Sale'); // Implicit data column. 

			//alert("in");

			$.getJSON('${getFrGraphData}', {
				ajax : 'true'

			}, function(chartsBardata) {

				//alert(JSON.stringify(chartsBardata));
				var len = chartsBardata.length;
				//alert("LEN - " + len);

				$.each(chartsBardata, function(key, chartsBardata) {

					dataTable.addRows([ [ chartsBardata.frName,
							parseInt(chartsBardata.sale) ] ]);

				});

				//alert(11);

				var materialOptions = {
					width : 1100,
					height : 300,
					chart : {
						title : ' ',
						subtitle : ' '
					},
					series : {
						0 : {
							axis : 'distance'
						}
					// Bind series 1 to an axis named 'brightness'.
					},
					axes : {
						y : {
							distance : {
								label : 'Amount'
							}
						// Left y-axis.

						}
					}
				};

				var materialChart = new google.charts.Bar(chartDiv);
				//var materialChart = new google.visualization.ColumnChart(chartDiv);

				function selectQtyHandler() {
					// alert("hii");
					var selectedItem = materialChart.getSelection()[0];
					if (selectedItem) {
						// alert("hii selectedItem");
						i = selectedItem.row, 0;

						//alert("hii selectedItem" + chartsBardata[i].catId);
						//drawPieChart1(chartsBardata[i].catId,
								//chartsBardata[i].catName);

					}
				}

				google.visualization.events.addListener(materialChart,
						'select', selectQtyHandler);

				function drawMaterialChart() {
					materialChart.draw(dataTable, google.charts.Bar
							.convertOptions(materialOptions));
				}

				drawMaterialChart();

			});

		}
	</script>


	<script type="text/javascript">
		function drawRouteBarChart() {

			var chartDiv = document.getElementById('routeBarChart');

			var dataTable = new google.visualization.DataTable();

			dataTable.addColumn('string', 'Route'); // Implicit domain column.
			dataTable.addColumn('number', 'Sale'); // Implicit data column. 

			//alert("in");

			$.getJSON('${getRouteGraphData}', {
				ajax : 'true'

			}, function(chartsBardata) {

				//alert(JSON.stringify(chartsBardata));
				var len = chartsBardata.length;
				//alert("LEN - " + len);

				$.each(chartsBardata, function(key, chartsBardata) {

					dataTable.addRows([ [ chartsBardata.routeName,
							parseInt(chartsBardata.sale) ] ]);

				});

				//alert(11);

				var materialOptions = {
					width : 550,
					height : 250,
					chart : {
						title : ' ',
						subtitle : ' '
					},
					series : {
						0 : {
							axis : 'distance'
						}
					// Bind series 1 to an axis named 'brightness'.
					},
					axes : {
						y : {
							distance : {
								label : 'Amount'
							}
						// Left y-axis.

						}
					}
				};

				var materialChart = new google.charts.Bar(chartDiv);

				function selectQtyHandler() {
					// alert("hii");
					var selectedItem = materialChart.getSelection()[0];
					if (selectedItem) {
						// alert("hii selectedItem");
						i = selectedItem.row, 0;

						//alert("hii selectedItem" + chartsBardata[i].catId);
						drawFrRouteWiseBarChart(chartsBardata[i].routeId);

					}
				}

				google.visualization.events.addListener(materialChart,
						'select', selectQtyHandler);

				function drawMaterialChart() {
					materialChart.draw(dataTable, google.charts.Bar
							.convertOptions(materialOptions));
				}
				
				

				drawMaterialChart();
				drawFrRouteWiseBarChart(chartsBardata[0].routeId);

			});

		}
	</script>


	<script type="text/javascript">
		function drawFrRouteWiseBarChart(id) {

			var chartDiv = document.getElementById('frBarChart2');

			var dataTable = new google.visualization.DataTable();

			dataTable.addColumn('string', 'Franchisee'); // Implicit domain column.
			dataTable.addColumn('number', 'Sale'); // Implicit data column. 

			//alert("in");

			$.getJSON('${getFrGraphDataByRoute}', {
				routeId : id,
				ajax : 'true'

			}, function(chartsBardata) {

				//alert(JSON.stringify(chartsBardata));
				var len = chartsBardata.length;
				//alert("LEN - " + len);

				$.each(chartsBardata, function(key, chartsBardata) {

					dataTable.addRows([ [ chartsBardata.frName,
							parseInt(chartsBardata.sale) ] ]);

				});

				//alert(11);

				var materialOptions = {
					width : 550,
					height : 250,
					chart : {
						title : ' ',
						subtitle : ' '
					},
					series : {
						0 : {
							axis : 'distance'
						}
					// Bind series 1 to an axis named 'brightness'.
					},
					axes : {
						y : {
							distance : {
								label : 'Amount'
							}
						// Left y-axis.

						}
					}
				};

				var materialChart = new google.charts.Bar(chartDiv);

				function selectQtyHandler() {
					// alert("hii");
					var selectedItem = materialChart.getSelection()[0];
					if (selectedItem) {
						// alert("hii selectedItem");
						i = selectedItem.row, 0;

						//alert("hii selectedItem" + chartsBardata[i].catId);
						drawPieChart1(chartsBardata[i].catId,
								chartsBardata[i].catName);

					}
				}

				google.visualization.events.addListener(materialChart,
						'select', selectQtyHandler);

				function drawMaterialChart() {
					materialChart.draw(dataTable, google.charts.Bar
							.convertOptions(materialOptions));
				}

				drawMaterialChart();

			});

		}
	</script>



	<script type="text/javascript">

	function addCommas(x){

		x=String(x).toString();
		 var afterPoint = '';
		 if(x.indexOf('.') > 0)
		    afterPoint = x.substring(x.indexOf('.'),x.length);
		 x = Math.floor(x);
		 x=x.toString();
		 var lastThree = x.substring(x.length-3);
		 var otherNumbers = x.substring(0,x.length-3);
		 if(otherNumbers != '')
		     lastThree = ',' + lastThree;
		 return otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",") + lastThree + afterPoint;
		} 

	</script>


	<!-- CHARTS -->

	<script type="text/javascript"
		src="https://www.gstatic.com/charts/loader.js"></script>

	<!-- ------ -->


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
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.resize.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.pie.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.stack.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.crosshair.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/flot/jquery.flot.tooltip.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/sparkline/jquery.sparkline.min.js"></script>

	<!--flaty scripts-->
	<script src="${pageContext.request.contextPath}/resources/js/flaty.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/flaty-demo-codes.js"></script>

</body>
</html>
