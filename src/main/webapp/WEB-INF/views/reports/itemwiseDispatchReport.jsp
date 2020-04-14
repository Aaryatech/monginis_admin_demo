<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
	<c:url var="routListByAbcType" value="/routListByAbcType"></c:url>
	<c:url var="getBillList" value="/getPDispatchReportItemwiseResult"></c:url>
	<c:url var="getMenuListBySectionId" value="/getMenuListBySectionId"></c:url>
	<%-- 	<c:url var="getFranchisees" value="/getFranchiseByRoute"></c:url> --%>
	<c:url var="getAllRoute" value="/getAllRoute"></c:url>
	<c:url var="getAllFranchasiOfMultipleRoute"
		value="/getAllFranchasiOfMultipleRoute"></c:url>


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
					<i class="fa fa-file-o"></i>Item Wise Dispatch Report
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
					<i class="fa fa-bars"></i>Dispatch Report
				</h3>

			</div>
			<form id="validation-form"
				action="${pageContext.request.contextPath}/getPDispatchReportItemwiseResult"
				method="post">

				<div class="box-content">
					<div class="row">


						<div class="form-group">
							<label class="col-sm-3 col-lg-2	 control-label">Delivery
								Date</label>
							<div class="col-sm-5 col-lg-2 controls date_select">
								<input class="form-control date-picker" id="billDate"
									name="billDate" size="10" type="text" value="${todaysDate}" />
							</div>

							<label class="col-sm-3 col-lg-2 control-label"> Route
								Type</label>
							<div class="col-sm-5 col-lg-4 controls">
								<select data-placeholder="Choose Category"
									class="form-control chosen" onchange="routListByAbcType(0)"
									id="abcType" name="abcType">

									<option value="">Select Route Type</option>
									<option value="0">ALL</option>
									<option value="1">A</option>
									<option value="2">B</option>
									<option value="3">C</option>
									<%-- <c:forEach items="${catList}" var="cat" varStatus="count">
									<option value="${cat.catId}"><c:out value="${cat.catName}"/></option>
								</c:forEach> --%>
								</select>
							</div>
						</div>
					</div>
					<br>
					<div class="row">
						<label class="col-sm-3 col-lg-1 control-label">Select
							Route</label>
						<div class="col-sm-5 col-lg-11  controls">
							<select data-placeholder="Select Route"
								class="form-control chosen" name="selectRoute" id="selectRoute"
								multiple="multiple" onchange="FranchasiListByRouteID()">

							</select>

						</div>
					</div>

					<br>
					<div class="row">

						<div class="form-group">
							<label class="col-sm-3 col-lg-1	 control-label">Select
								Franchise</label>

							<div class="col-sm-10 col-lg-11  controls">

								<select data-placeholder="Franchise Name"
									class="form-control chosen" id="frid" name="frid"
									multiple="multiple" required onchange="onFrChange(this.value)">

									<%-- <option value="-1" selected>All</option>
								 
								  <c:forEach items="${franchiseeList}" var="franchiseeList" >
									<option value="${franchiseeList.frId}"><c:out value="${franchiseeList.frName}"/></option>
								</c:forEach>   --%>
								</select>




							</div>
						</div>
					</div>


					<br>
					<div class="row">

						<div class="form-group">

							<label class="col-sm-3 col-lg-1 control-label">Select
								Section</label>
							<div class="col-sm-3 col-lg-2">

								<select data-placeholder="Choose Category"
									class="form-control chosen" onchange="getMenuListBySectionId()"
									id="sectionId" name="sectionId">

									<option value="" selected>Select Section</option>

									<c:forEach items="${sectionList}" var="sectionList">
										<option value="${sectionList.sectionId}"><c:out
												value="${sectionList.sectionName}" /></option>
									</c:forEach>
								</select>
							</div>

							<label class="col-sm-3 col-lg-1 control-label">Select
								Menu </label>
							<div class="col-sm-8 col-lg-8">

								<select data-placeholder="Select Menu "
									class="form-control chosen" id="menuId" name="menuId"
									multiple="multiple" required
									onchange="onMenuChange(this.value)">

									<%--  <option value="0" selected>All</option>
								<c:forEach items="${menuList}" var="menuList" >
									<option value="${menuList.menuId}"><c:out value="${menuList.menuTitle}"/> </option>

								</c:forEach> --%>
								</select>
							</div>

						</div>



					</div>
					<br>

					<div class="row">


						<div class="form-group">

							<label class="col-sm-3 col-lg-1 control-label">Select
								Item </label>
							<div class="col-sm-10 col-lg-10">

								<select data-placeholder="Select Items"
									class="form-control chosen" id="itemId" name="itemId"
									multiple="multiple" required>


									<c:forEach items="${itemsList}" var="itemsList">
										<option value="${itemsList.id}"><c:out
												value="${itemsList.itemName}" /></option>
									</c:forEach>
								</select>

							</div>
							<div class="form-group" align="left">

								<input type="submit" id="submit" class="btn btn-primary"
									value="Search">


							</div>
						</div>



					</div>
			</form>
			<form id="validation-form1"
				action="${pageContext.request.contextPath}/pdf/getPDispatchReportItemwisePdf"
				method="get">
				<input type="hidden" id="dotMatrixJsp" name="dotMatrixJsp" value="0" />
				<input type="hidden" id="bdate" name="bdate" /> <input
					type="hidden" id="abc" name="abc" /> <input type="hidden"
					id="routes" name="routes" /> <input type="hidden" id="frids"
					name="frids" /> <input type="hidden" id="sections" name="sections" />
				<input type="hidden" id="menus" name="menus" /> <input
					type="hidden" id="items" name="items" />
				<div class="form-group" align="center">
					<input type="button" id="pdf" class="btn btn-primary" value="PDF"
						onclick="onPdfClick()">
				</div>
				<div class="form-group" align="center">
					<input type="button" id="pdf" class="btn btn-primary"
						value="PDF For Dot Matrix" onclick="onPdfClickForDotMt()">
				</div>
			</form>
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
		function onPdfClickForDotMt() {
			var sectionId = $("#sectionId").val();
			var abcType = $("#abcType").val();
			var routeIds = $("#selectRoute").val();
			var billDate = $("#billDate").val();
			var selectedfranchase = $("#frid").val();
			var selectedMenu = $("#menuId").val();
			var selecteditems = $("#itemId").val();

			document.getElementById("sections").value = sectionId;
			document.getElementById("abc").value = abcType;
			document.getElementById("routes").value = routeIds;
			document.getElementById("bdate").value = billDate;
			document.getElementById("frids").value = selectedfranchase;
			document.getElementById("menus").value = selectedMenu;
			document.getElementById("items").value = selecteditems;
			document.getElementById("dotMatrixJsp").value = 1;

			var form = document.getElementById("validation-form1");
			form.submit();
		}
	</script>
	<script type="text/javascript">
		function onPdfClick() {
			var sectionId = $("#sectionId").val();
			var abcType = $("#abcType").val();
			var routeIds = $("#selectRoute").val();
			var billDate = $("#billDate").val();
			var selectedfranchase = $("#frid").val();
			var selectedMenu = $("#menuId").val();
			var selecteditems = $("#itemId").val();

			document.getElementById("sections").value = sectionId;
			document.getElementById("abc").value = abcType;
			document.getElementById("routes").value = routeIds;
			document.getElementById("bdate").value = billDate;
			document.getElementById("frids").value = selectedfranchase;
			document.getElementById("menus").value = selectedMenu;
			document.getElementById("items").value = selecteditems;

			var form = document.getElementById("validation-form1");
			form.submit();
		}
	</script>

	<script type="text/javascript">
		function getMenuListBySectionId() {

			var sectionId = $("#sectionId").val();

			if (sectionId == "" || sectionId == null) {

				$('#menuId').find('option').remove().end()
				$("#menuId").trigger("chosen:updated");
			} else {
				$.getJSON('${getMenuListBySectionId}', {

					sectionId : sectionId,
					ajax : 'true'
				}, function(data) {
					var html = '<option value="">Select Section</option>';

					var len = data.length;

					$('#menuId').find('option').remove().end()

					$("#menuId").append(
							$("<option ></option>").attr("value", -1).text(
									"All"));

					for (var i = 0; i < len; i++) {
						$("#menuId")
								.append(
										$("<option></option>").attr("value",
												data[i].menuId).text(
												data[i].menuTitle));
					}
					$("#menuId").trigger("chosen:updated");
				});
			}

		}
		/* function getFranchise(routeId) {
		
			$.getJSON('${getFranchisees}', {
				
				routeId : routeId,
				ajax : 'true'
			}, function(data) {
			 	var html = '<option value="">Select Franchisee</option>';
			
				var len = data.length;
				
				$('#fraId')
			    .find('option')
			    .remove()
			    .end()
			    
			 $("#fraId").append(
		                    $("<option></option>").attr(
		                        "value", 0).text("Select Franchisee")
		                );
				
				for ( var i = 0; i < len; i++) {
		            $("#fraId").append(
		                    $("<option></option>").attr(
		                        "value", data[i].frId).text(data[i].frName)
		                );
				}
				   $("#fraId").trigger("chosen:updated");
			}); 
		} */

		function routListByAbcType(type) {

			var abcType = $("#abcType").val();

			if (type == -1) {

				$.getJSON('${routListByAbcType}', {

					abcType : abcType,
					ajax : 'true'
				}, function(data) {
					var html = '<option value="">Select Routes</option>';

					var len = data.length;

					$('#selectRoute').find('option').remove().end()

					$("#selectRoute").append(
							$("<option></option>").attr("value", -1)
									.text("All"));

					for (var i = 0; i < len; i++) {
						$("#selectRoute").append(
								$("<option selected></option>").attr("value",
										data[i].routeId)
										.text(data[i].routeName));
					}
					$("#selectRoute").trigger("chosen:updated");
					$("#selectRoute").trigger("change");

				});

			} else {

				$.getJSON('${routListByAbcType}', {

					abcType : abcType,
					ajax : 'true'
				}, function(data) {
					var html = '<option value="">Select Routes</option>';

					var len = data.length;

					$('#selectRoute').find('option').remove().end()

					$("#selectRoute").append(
							$("<option></option>").attr("value", -1)
									.text("All"));

					for (var i = 0; i < len; i++) {
						$("#selectRoute").append(
								$("<option ></option>").attr("value",
										data[i].routeId)
										.text(data[i].routeName));
					}
					$("#selectRoute").trigger("chosen:updated");
				});
				/* 	$.getJSON('${getAllRoute}', {
						
						abcType : abcType,
						ajax : 'true'
					}, function(data) {
					 	var html = '<option value="">Select Rout</option>';
					 	
						var len = data.length;
						
						$('#selectRoute')
					    .find('option')
					    .remove()
					    .end()
					    
					 $("#selectRoute").append(
				     $("<option></option>").attr(
				         "value", -1).text("All") ); 
						
						for ( var i = 0; i < len; i++) {
				            $("#selectRoute").append(
				                    $("<option></option>").attr(
				                        "value", data[i].routeId).text(data[i].routeName)
				                );
						}
						   $("#selectRoute").trigger("chosen:updated");
					});
				 */
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
						$("<option></option>").attr("value", -1).text(
								"Select Franchisee"));

				for (var i = 0; i < len; i++) {
					$("#fraId").append(
							$("<option></option>").attr("value", data[i].frId)
									.text(data[i].frName));
				}
				$("#fraId").trigger("chosen:updated");
			});
		}

		function FranchasiListByRouteID() {

			var routeIds = $("#selectRoute").val();

			if (routeIds == "-1") {
				routListByAbcType(-1);
			}

			if (routeIds == "" || routeIds == null) {
				$('#frid').find('option').remove().end();
				$("#frid").trigger("chosen:updated");
			} else {
				$.getJSON('${getAllFranchasiOfMultipleRoute}', {

					routeIds : JSON.stringify(routeIds),
					ajax : 'true'
				}, function(data) {
					var html = '<option value="">Select Franchasie</option>';

					var len = data.length;

					$('#frid').find('option').remove().end()

					$("#frid").append(
							$("<option></option>").attr("value", -1)
									.text("All"));

					for (var i = 0; i < len; i++) {
						$("#frid").append(
								$("<option></option>").attr("value",
										data[i].frId).text(data[i].frName));
					}
					$("#frid").trigger("chosen:updated");
				});
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
						$("<option></option>").attr("value", -1).text(
								"Select Franchisee"));

				for (var i = 0; i < len; i++) {
					$("#fraId").append(
							$("<option></option>").attr("value", data[i].frId)
									.text(data[i].frName));
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
				//document.getElementById("PieChart_div").style = "display:none";
				//document.getElementById("chart_div").style = "display:none";

				var billDate = $("#billDate").val();
				var selectedfranchase = $("#frid").val();
				var selectedMenu = $("#menuId").val();
				var selecteditems = $("#itemId").val();

				$('#loader').show();

				$
						.getJSON(
								'${getBillList}',

								{
									bill_date : billDate,
									fr_id : JSON.stringify(selectedfranchase),
									menu_id : JSON.stringify(selectedMenu),
									item_id : JSON.stringify(selectedMenu),
									ajax : 'true'

								},
								function(data) {
									document.getElementById("submit").disabled = true;
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
									tr = document.getElementById('table_grid').tHead.children[0];
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

														$('#table_grid tbody')
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
		function validate() {

			var billDate = $("#billDate").val();
			var selectedfranchase = $("#frid").val();
			var selectedMenu = $("#menuId").val();
			var selecteditems = $("#itemId").val();

			var isValid = true;

			if (selectCat == "" || selectCat == null) {

				isValid = false;
				alert("Please Select Menu");

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

	</script>

	<script type="text/javascript">
		$(document)
				.ready(
						function() { // if all label selected set all items selected

							$('#frid')
									.change(
											function() {
												var selected = $('#frid').val();
												//alert('hello');

												if (selected == -1) {
													$
															.getJSON(
																	'${franchiseeList}',
																	{
																		catId : JSON
																				.stringify(catId),
																		ajax : 'true'
																	},
																	function(
																			data) {
																		var html = '<option value="">Select Franchise</option>';

																		var len = data.length;

																		$(
																				'#frid')
																				.find(
																						'option')
																				.remove()
																				.end()
																		$(
																				"#frid")
																				.append(
																						$(
																								"<option></option>")
																								.attr(
																										"value",
																										-1)
																								.text(
																										"ALL"));

																		for (var i = 0; i < len; i++) {

																			$(
																					"#frid")
																					.append(
																							$(
																									"<option selected></option>")
																									.attr(
																											"value",
																											data[i].subCatId)
																									.text(
																											data[i].frName));
																		}

																		$(
																				"#selectSubCat")
																				.trigger(
																						"chosen:updated");
																	});
												}
											});
						});
	</script>
	<script>
		function onFrChange(frId) {
			if (frId == -1) {

				var routeIds = $("#selectRoute").val();

				$.getJSON('${getAllFranchasiOfMultipleRoute}', {

					routeIds : JSON.stringify(routeIds),
					ajax : 'true'
				}, function(data) {
					var html = '<option value="">Select Franchaisee</option>';

					var len = data.length;

					$('#frid').find('option').remove().end()

					$("#frid").append(
							$("<option></option>").attr("value", -1)
									.text("All"));

					for (var i = 0; i < len; i++) {
						$("#frid").append(
								$("<option selected></option>").attr("value",
										data[i].frId).text(data[i].frName));
					}
					$("#frid").trigger("chosen:updated");
				});

			}

		}
	</script>
	<script>
		function onMenuChange(menuId) {
			if (menuId == -1) {
				var sectionId = $("#sectionId").val();
				$.getJSON('${getMenuListBySectionId}', {

					sectionId : sectionId,
					ajax : 'true'
				}, function(data) {
					var html = '<option value="">Select Section</option>';

					var len = data.length;

					$('#menuId').find('option').remove().end()

					$("#menuId").append(
							$("<option ></option>").attr("value", -1).text(
									"All"));

					for (var i = 0; i < len; i++) {
						$("#menuId")
								.append(
										$("<option selected></option>").attr(
												"value", data[i].menuId).text(
												data[i].menuTitle));
					}
					$("#menuId").trigger("chosen:updated");
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