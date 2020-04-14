<%@page import="com.ats.adminpanel.model.franchisee.SubCategory"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page
	import="java.io.*, java.util.*, java.util.Enumeration, java.text.*"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>Manual Order</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0">


<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
<style type="text/css">
select {
	width: 180px;
	height: 30px;
}
</style>
<style>
[type="radio"]:checked,
[type="radio"]:not(:checked) {
    position: absolute;
    left: -9999px;
}
[type="radio"]:checked + label,
[type="radio"]:not(:checked) + label
{
    position: relative;
    padding-left: 28px;
    cursor: pointer;
    line-height: 20px;
    display: inline-block;
    color: #666;
}
[type="radio"]:checked + label:before,
[type="radio"]:not(:checked) + label:before {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    width: 18px;
    height: 18px;
    border: 1px solid #ddd;
    border-radius: 100%;
    background: #fff;
}
[type="radio"]:checked + label:after,
[type="radio"]:not(:checked) + label:after {
    content: '';
    width: 12px;
    height: 12px;
    background: #F87DA9;
    position: absolute;
    top: 3px;
    left: 3px;
    border-radius: 100%;
    -webkit-transition: all 0.2s ease;
    transition: all 0.2s ease;
}
[type="radio"]:not(:checked) + label:after {
    opacity: 0;
    -webkit-transform: scale(0);
    transform: scale(0);
}
[type="radio"]:checked + label:after {
    opacity: 1;
    -webkit-transform: scale(1);
    transform: scale(1);
}

</style>
<style>
/* Extra styles for the cancel button */
.cancelbtn {
	width: auto;
	padding: 10px 18px;
	background-color: #f44336;
}

.container1 {
	padding: 16px;
	margin-left: 5%;
	margin-right: 5%;
}

/* The Modal (background) */
.modal {
	display: none; /* Hidden by default */
	position: fixed; /* Stay in place */
	z-index: 1; /* Sit on top */
	left: 0;
	top: 0;
	width: 100%; /* Full width */
	height: 100%; /* Full height */
	overflow: auto; /* Enable scroll if needed */
	background-color: rgb(0, 0, 0); /* Fallback color */
	background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
	padding-top: 60px;
}

/* Modal Content/Box */
.modal-content {
	background-color: #fefefe;
	margin: 5% auto 15% auto;
	/* 5% from the top, 15% from the bottom and centered */
	border: 1px solid #888;
	width: 80%; /* Could be more or less, depending on screen size */
}

/* The Close Button (x) */
.close {
	position: absolute;
	right: 25px;
	top: 0;
	color: #000;
	font-size: 35px;
	font-weight: bold;
}

.close:hover, .close:focus {
	color: red;
	cursor: pointer;
}

/* Add Zoom Animation */
.animate {
	-webkit-animation: animatezoom 0.6s;
	animation: animatezoom 0.6s
}

@
-webkit-keyframes animatezoom {
	from {-webkit-transform: scale(0)
}

to {
	-webkit-transform: scale(1)
}

}
@
keyframes animatezoom {
	from {transform: scale(0)
}

to {
	transform: scale(1)
}

}

/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 300px) {
	span.psw {
		display: block;
		float: none;
	}
	.cancelbtn {
		width: 100%;
	}
}
</style>



</head>
<body onload="showPdf('${billNo}')">

	<c:url var="setAllItemSelected" value="/setAllItemSelected" />
	<c:url var="findFranchiseeData" value="/findFranchiseeData" />
	<c:url var="findItemsByCatId" value="/getItemsOfMenuIdWithDate" />
	<c:url var="findItemsByCatIdForMulFr" value="/getItemsOfMenuIdForMulFr" />
	<c:url var="findAllMenus" value="/getMenuForOrder" />
	<c:url var="getAllMenu" value="/getAllMenu" />
	<c:url var="insertItem" value="/insertItem" />
	<c:url var="deleteItems" value="/deleteItems" />
	<c:url var="generateManualBill" value="/generateManualBill" />
	<c:url var="getItemsByCatIdManOrder" value="/getItemsByCatIdManOrder" />
	<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>

	<c:url var="getFrListofAllFr" value="/getFrListofAllFrManualOrder"></c:url>
	<c:url var="getPreviewManualOrder" value="/getPreviewManualOrder"></c:url>


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
						<i class="fa fa-file-o"></i> Manual Order
					</h1>
				</div>
			</div>
			<!-- END Page Title -->


			<!-- BEGIN Main Content -->
			<div class="row">
				<div class="col-md-12">
					<div class="row">
						<div class="col-md-12">
							<div class="box">
								<div class="box-title">
									<h3>
										<i class="fa fa-bars"></i> Manual Order
									</h3>
									<div class="box-tool">
										<a href="${pageContext.request.contextPath}/"></a> <a
											data-action="collapse" href="#"><i
											class="fa fa-chevron-up"></i></a>
									</div>
								</div>

								<form
									action="${pageContext.request.contextPath}/generateManualBill"
									class="form-horizontal" method="post" id="formId">


									<div class="box-content">
										<%-- <form action="${pageContext.request.contextPath}/addManualOrder" class="form-horizontal"
										id="validation-form" method="post"> --%>
										<div class="form-group">
											<label class="col-sm-3 col-lg-2 control-label">Order
												Type</label> <label class="col-sm-3 col-lg-2 control-label">
												<input type="radio" name="ordertype" class="order" value="0"
												id="or1" checked onchange="checkCheckedStatus()"> <label
												for="or1"> Manual Order</label>
											</label> <label class="col-sm-3 col-lg-2 control-label"> <input
												type="radio" name="ordertype" class="order" value="1"
												id="or2" onchange="checkCheckedStatus()"> <label
												for="or2"> Manual Bill </label>
											</label> <label class="col-sm-3 col-lg-2 control-label"> <input
												type="radio" name="ordertype" class="order" value="2"
												id="or3" onchange="checkCheckedStatus()"> <label
												for="or3"> Multiple FR Bill </label>
											</label>
										</div>

										<input type="hidden" name="flagRate" value="0" id="flagRate" />


										<div class="form-group" id="singleFr">
											<label class="col-sm-3 col-lg-2 control-label">Franchisee</label>
											<div class="col-sm-9 col-lg-5 controls">
												<select data-placeholder="Select Franchisee" name="fr_id"
													class="form-control chosen" tabindex="-1" id="fr_id"
													onchange="findFranchiseeData(this.value)">
													<option value="0">Select Franchisee</option>
													<c:forEach
														items="${allFranchiseeAndMenuList.getAllFranchisee()}"
														var="franchiseeList">
														<option value="${franchiseeList.frId}">${franchiseeList.frName}</option>

													</c:forEach>


												</select>
											</div>
										</div>
										<div class="form-group" style="display: none;" id="mulFr">
											<label class="col-sm-3 col-lg-2 control-label">Franchisee</label>
											<div class="col-sm-9 col-lg-5 controls">
												<select data-placeholder="Select Franchisee" name="fr_id1"
													class="form-control chosen" tabindex="-1" id="fr_id1"
													multiple="multiple" onchange="setAllFrSelected(this.value)">
													<option value="-1"><c:out value="All" /></option>
													<c:forEach
														items="${allFranchiseeAndMenuList.getAllFranchisee()}"
														var="franchiseeList">
														<c:if test="${franchiseeList.frRateCat==1}">
															<option value="${franchiseeList.frId}">${franchiseeList.frName}</option>
														</c:if>

													</c:forEach>


												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 col-lg-2 control-label">Menu</label>
											<div class="col-sm-9 col-lg-5 controls">
												<select data-placeholder="Select Menu" name="menu"
													class="form-control chosen" tabindex="-1" id="menu"
													data-rule-required="true"
													onchange="onCatIdChangeForManOrder(this.value)">
													<option value="0">Select Menu</option>


												</select>
											</div>
											<label class="col-sm-3 col-lg-2	 control-label">Select
												Section</label>
											<div class="col-sm-6 col-lg-3 controls date_select">
												<select data-placeholder="Choose Menu"
													class="form-control chosen" id="sectionId" name="sectionId">

													<option value="">Select Section</option>

													<c:forEach items="${sectionList}" var="sectionList">
														<option value="${sectionList.sectionId}"><c:out
																value="${sectionList.sectionName}" /></option>
													</c:forEach>


												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 col-lg-2 control-label">Order</label>
											<label class="col-sm-3 col-lg-2 control-label"> <input
												type="radio" name="typename" class="type" value="0"
												checked="" id="t1" onchange="checkOrderByStatus()">
												<label for="t1">Billing</label>
											</label> <label class="col-sm-3 col-lg-2 control-label"> <input
												type="radio" name="typename" class="type" value="1" id="t2"
												onchange="checkOrderByStatus()"> <label for="t2">By
													MRP</label>
											</label>
										</div>
										<div class="form-group">
											<div id="singleOrder">
												<label class="col-sm-3 col-lg-2 control-label">Party
													Name</label>
												<div class="col-sm-9 col-lg-2 controls">
													<input type="text" name="frName" value="-" id="frName"
														class="form-control" />

												</div>
												<label class="col-sm-3 col-lg-1 control-label">GSTIN</label>
												<div class="col-sm-9 col-lg-2 controls">
													<input type="text" name="gstin" value="-" id="gstin"
														class="form-control" />
												</div>
												<label class="col-sm-3 col-lg-1 control-label">Address</label>
												<div class="col-sm-9 col-lg-2 controls">
													<input type="text" name="address" value="-" id="address"
														class="form-control" />
												</div>
												<input type="button" class="btn btn-primary" id="searchBtn"
													value="Search" onclick="onSearch()">
											</div>
											<div style="display: none;" id="mulOrder">
												<label class="col-sm-3 col-lg-2 control-label">Item</label>
												<div class="col-sm-9 col-lg-5 controls">
													<select data-placeholder="Choose Item"
														class="form-control chosen" id="itemId" name="itemId">

														<option value="">Select Item</option>

														<c:forEach items="${itemList}" var="itemList">
															<option value="${itemList.id}"><c:out
																	value="${itemList.itemName}" /></option>
														</c:forEach>


													</select>


												</div>
												<label class="col-sm-3 col-lg-1 control-label">Qty</label>
												<div class="col-sm-9 col-lg-2 controls">
													<input type="text" name="qty" value="0" id="qty"
														class="form-control" />
												</div>
												<input type="button" class="btn btn-primary" id="searchBtn"
													value="Add" onclick="onSearchMulFr()">

											</div>


										</div>
										<!-- 	<div class="form-group">
											<label class="col-sm-3 col-lg-2 control-label">Item</label>
											<div class="col-sm-9 col-lg-5 controls">
												<select data-placeholder="Select Item" name="items"
													class="form-control chosen" tabindex="-1" id="items"
													data-rule-required="true" >
                                                   	<option value="0">Select Item </option>
												</select>
												
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 col-lg-2 control-label">Qty</label>
											<div class="col-sm-9 col-lg-5 controls">
											<input type="text" name="qty" value="0"	id="qty" class="form-control"/>
												
											</div>
										</div> 
										<div class="form-group">
											<div
												class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2">
												<input type="button" class="btn btn-primary"
													value="Add Item" onclick="onSearch(this.value)"  >
												
											</div>
										</div>-->
										<!-- </form> -->
									</div>
									<div align="center" id="loader"
										style="background-color: white; display: none;">

										<span
											style="background-color: white; font-size: 15px; text-align: center;">
											<font color="#343690" style="background-color: white;"></font>

										</span> <span class="l-1"></span> <span class="l-2"></span> <span
											class="l-3"></span> <span class="l-4"></span> <span
											class="l-5"></span> <span class="l-6"></span>
									</div>


									<div class="box-content">
										<div class="col-md-9"></div>
										<label for="search" class="col-md-3" id="search"> <i
											class="fa fa-search" style="font-size: 20px"></i> <input
											type="text" id="myInput" onkeyup="myFunction()"
											autocomplete="off" style="border-radius: 25px;"
											placeholder="Search items by Name" title="Type in a name">
										</label> <br> <br> <br>
										<div class="row">
											<div class="col-md-12 table-responsive">
												<table class="table table-bordered table-striped fill-head "
													style="width: 100%" id="table_grid">
													<thead style="background-color: #f3b5db;">
														<tr>
															<th style="text-align: center;">Sr.No.</th>
															<th style="text-align: center;">Item Name</th>
															<th style="text-align: center;">Min Qty</th>
															<th style="text-align: center;">Qty</th>
															<th style="text-align: center;" id="discth">Disc</th>
															<th style="text-align: center;">MRP</th>
															<th style="text-align: center;">Rate</th>
															<th style="text-align: center;">Total</th>
														</tr>
													</thead>
													<tbody>

													</tbody>
												</table>
											</div>

										</div>
										<div class="row">

											<div class="form-group">
												<div id="singleOrder">
													<label class="col-md-2  control-label">Production
														Date</label>
													<div class="col-md-2 controls">
														<input class="form-control date-picker" id="prodDate"
															name="prodDate" size="30" type="text" required="required" />

													</div>
													<label class="col-md-2  control-label">Delivery
														Date</label>
													<div class="col-md-2  controls">
														<input class="form-control date-picker" id="deliveryDate"
															name="deliveryDate" size="30" type="text"
															required="required" />
													</div>



													<div class="col-md-4" style="text-align: center">

														<input type="button" class="btn btn-info"
															value="Preview Order" name="preview" id="preview"
															style="display: none;"
															onclick="showPreviewOrder();document.getElementById('id01').style.display='block'"
															> <input type="submit"
															class="btn btn-info" value="ORDER" name="submitorder"
															id="submitorder" disabled> <input type="submit"
															class="btn btn-info" value="ORDER_&_BILL"
															name="submitbill" id="submitbill" style="display: none;"
															disabled>
													</div>

												</div>
											</div>


										</div>

									</div>
								</form>
								<!-- <div align="center" id="loader1" style="display: none">	

					<span>
						<h4>
							<font color="#343690">Saving your order,please wait</font>
						</h4>
					</span> <span class="l-1"></span> <span class="l-2"></span> <span
						class="l-3"></span> <span class="l-4"></span> <span class="l-5"></span>
					<span class="l-6"></span>
				</div>	 -->


								<div id="id01" class="modal">

									<div class="container1" style="background-color: #ffffff">
										<h1>
											<i class="fa fa-file-o"></i> Order Preview
										</h1>
									</div>

									<div class="container1" style="background-color: #ffffff">

										<div class="col-md-12 table-responsive">
											<table class="table table-bordered table-striped fill-head "
												style="width: 100%" id="table_grid11">
												<thead style="background-color: #f3b5db;">
													<tr>
														<th style="text-align: center;">SR.NO.</th>
														<th style="text-align: center;">ITEM NAME</th>
														<th style="text-align: center;">Qty</th>
														<th style="text-align: center;">MRP</th>
														<th style="text-align: center;">RATE</th>
														<th style="text-align: center;">TOTAL</th>

													</tr>
												</thead>
												<tbody>

												</tbody>
											</table>
										</div>

									</div>

									<div class="container1" style="background-color: #ffffff">
										<button type="button"
											onclick="document.getElementById('id01').style.display='none'"
											class="btn btn-info">Cancel</button>
									</div>
								</div>



							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- END Main Content -->

			<footer>
				<p>2019 © MONGINIS.</p>
			</footer>

			<a id="btn-scrollup" class="btn btn-circle btn-lg" href="#"><i
				class="fa fa-chevron-up"></i></a>
		</div>
		<!-- END Content -->
	</div>
	<!-- END Container -->
	<script
		src="${pageContext.request.contextPath}/resources/assets/bootstrap/js/bootstrap.min.js"></script>
	<script>
		$(function() {
			$('#typeselector').change(function() {
				$('.formgroup').hide();
				$('#' + $(this).val()).show();
			});
		});
	</script>
	<script type="text/javascript">
		/* $(document).ready(function() {
		
		
		 $('#menu').change( */
		function onSearch() {

			var isValid = validation();
			if (isValid) {
				var type = $('.type:checked').val();
				var ordertype = $('.order:checked').val();//alert(ordertype);

				$('#table_grid td').remove();

				$('#loader').show();

				$
						.getJSON(
								'${findItemsByCatId}',
								{
									menuId : $('#menu').val(),
									frId : $('#fr_id').val(),
									by : type,
									ordertype : ordertype,
									ajax : 'true'
								},
								function(data) {

									$('#preview').show();

									$('#loader').hide();
									var len = data.length;
									document.getElementById("submitorder").disabled = false;
									document.getElementById("submitbill").disabled = false;
									$('#table_grid td').remove();

									//alert("json - "+data.prodDate);

									$('#prodDate').val(data.prodDate);
									$('#deliveryDate').val(data.deliveryDate);

									$
											.each(
													data.orders,
													function(key, item) {

														var tr = $('<tr></tr>');

														tr
																.append($(
																		'<td></td>')
																		.html(
																				key + 1));

														tr
																.append($(
																		'<td></td>')
																		.html(
																				item.itemName));
														tr
																.append($(
																		'<td></td>')
																		.html(
																				item.minQty
																						+ '<input type="hidden" value='+item.minQty+'	id=minqty'+item.itemId+""+item.frId+'  />'));
														if (ordertype == 1) {
															tr
																	.append($(
																			'<td style="text-align:right;" class="col-md-1"></td>')
																			.html(
																					'<input type="number" class="form-control" onchange="onChangeBill('
																							+ item.orderRate
																							+ ','
																							+ item.itemId
																							+ ','
																							+ item.frId
																							+ ')"   width=20px;  name=qty'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' id=qty'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' value='
																							+ item.orderQty
																							+ ' > '));
															tr
																	.append($(
																			'<td style="text-align:right;" class="col-md-1"></td>')
																			.html(
																					'<input type="number" class="form-control"  min="0"  width=20px; onchange="onChangeBill('
																							+ item.orderRate
																							+ ','
																							+ item.itemId
																							+ ','
																							+ item.frId
																							+ ')"  name=discper'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' id=discper'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' value='
																							+ item.isPositive
																							+ ' > '));
														} else {
															tr
																	.append($(
																			'<td style="text-align:right;" class="col-md-1"></td>')
																			.html(
																					'<input type="number" class="form-control" onchange="onChange('
																							+ item.orderRate
																							+ ','
																							+ item.itemId
																							+ ','
																							+ item.frId
																							+ ')"   width=20px;  name=qty'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' id=qty'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' value='
																							+ item.orderQty
																							+ ' > '));

															if (item.isPositive > 0) {
																tr
																		.append($(
																				'<td style="text-align:right;" class="col-md-1"></td>')
																				.html(
																						'Y'));
															} else {
																tr
																		.append($(
																				'<td style="text-align:right;" class="col-md-1"></td>')
																				.html(
																						'N'));
															}

														}
														tr
																.append($(
																		'<td style="text-align:right;"></td>')
																		.html(
																				item.orderMrp
																						.toFixed(2)));

														tr
																.append($(
																		'<td style="text-align:right;"></td>')
																		.html(
																				item.orderRate
																						.toFixed(2)));
														var total = item.orderQty
																* item.orderRate;
														tr
																.append($(
																		'<td style="text-align:right;" id=total'+item.itemId+""+item.frId+' ></td>')
																		.html(
																				total
																						.toFixed(2)));

														$('#table_grid tbody')
																.append(tr);
													});
								});
			}
			document.getElementById("flagRate").value = 1;
		}
		function onSearchMulFr() {

			var isValid = validation1();
			if (isValid) {

				var flagRate = $('#flagRate').val();
				var type = $('.type:checked').val();
				var ordertype = $('.order:checked').val();//alert(ordertype);
				var itemId = $('#itemId').val();
				var qty = $('#qty').val();
				var frId = $('#fr_id1').val();

				$('#loader').show();

				$
						.getJSON(
								'${findItemsByCatIdForMulFr}',
								{
									menuId : $('#menu').val(),
									frIdStr : JSON.stringify(frId),
									by : type,
									ordertype : ordertype,
									itemId : itemId,
									qty : qty,
									flagRate : flagRate,
									ajax : 'true'
								},
								function(data) {

									$('#loader').hide();
									var len = data.length;
									document.getElementById("submitorder").disabled = false;
									document.getElementById("submitbill").disabled = false;
									$('#table_grid td').remove();

									$
											.each(
													data,
													function(key, item) {

														var tr = $('<tr></tr>');

														tr
																.append($(
																		'<td></td>')
																		.html(
																				key + 1));

														tr
																.append($(
																		'<td></td>')
																		.html(
																				item.itemName));
														tr
																.append($(
																		'<td></td>')
																		.html(
																				item.minQty
																						+ '<input type="hidden" value='+item.minQty+'	id=minqty'+item.itemId+""+item.frId+'  />'));
														if (ordertype == 1
																|| ordertype == 2) {
															tr
																	.append($(
																			'<td style="text-align:right;" class="col-md-1"></td>')
																			.html(
																					'<input type="number" class="form-control" onchange="onChangeBill('
																							+ item.orderRate
																							+ ','
																							+ item.itemId
																							+ ','
																							+ item.frId
																							+ ')"   width=20px;  name=qty'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' id=qty'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' value='
																							+ item.orderQty
																							+ '  > '));
															tr
																	.append($(
																			'<td style="text-align:right;" class="col-md-1"></td>')
																			.html(
																					'<input type="number" class="form-control"  min="0"  width=20px; onchange="onChangeBill('
																							+ item.orderRate
																							+ ','
																							+ item.itemId
																							+ ','
																							+ item.frId
																							+ ')"  name=discper'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' id=discper'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' value='
																							+ item.isPositive
																							+ '  > '));
														} else {
															tr
																	.append($(
																			'<td style="text-align:right;" class="col-md-1"></td>')
																			.html(
																					'<input type="number" class="form-control" onchange="onChange('
																							+ item.orderRate
																							+ ','
																							+ item.itemId
																							+ ','
																							+ item.frId
																							+ ')"   width=20px;  name=qty'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' id=qty'
																							+ item.itemId
																							+ ""
																							+ item.frId
																							+ ' value='
																							+ item.orderQty
																							+ '   > '));

															if (item.isPositive > 0) {
																tr
																		.append($(
																				'<td style="text-align:right;" class="col-md-1"></td>')
																				.html(
																						'Y'));
															} else {
																tr
																		.append($(
																				'<td style="text-align:right;" class="col-md-1"></td>')
																				.html(
																						'N'));
															}

														}
														tr
																.append($(
																		'<td style="text-align:right;"></td>')
																		.html(
																				item.orderMrp
																						.toFixed(2)));

														tr
																.append($(
																		'<td style="text-align:right;"></td>')
																		.html(
																				item.orderRate
																						.toFixed(2)));
														var total = item.orderQty
																* item.orderRate;
														tr
																.append($(
																		'<td style="text-align:right;" id=total'+item.itemId+""+item.frId+' ></td>')
																		.html(
																				total
																						.toFixed(2)));

														$('#table_grid tbody')
																.append(tr);
													});
								});
			}
			document.getElementById("flagRate").value = 0;
		}
	</script>

	<script type="text/javascript">
		function onChange(rate, id, frId) {

			//calculate total value  
			var qty = $('#qty' + id + '' + frId).val();

			var minqty = $('#minqty' + id + '' + frId).val();

			if (qty % minqty == 0) {
				var total = rate * qty;

				$('#total' + id + '' + frId).html(total);
			} else {
				var total = 0;

				alert("Please Enter Qty Multiple of Minimum Qty");
				$('#qty' + id + '' + frId).val(0);

				$('#total' + id + '' + frId).html(total);
				$('#qty' + id + '' + frId).focus();
			}
		}
	</script>
	<script type="text/javascript">
		function onChangeBill(rate, id, frId) {

			//calculate total value  
			var qty = $('#qty' + id + '' + frId).val();
			var discper = $('#discper' + id + '' + frId).val();

			var minqty = $('#minqty' + id + '' + frId).val();

			if (qty % minqty == 0) {
				var total = rate * qty;
				var disc = (total * discper) / 100;
				total = total - disc;
				$('#total' + id + '' + frId).html(total.toFixed(2));
			} else {
				var total = 0;

				alert("Please Enter Qty Multiple of Minimum Qty");
				$('#qty' + id + '' + frId).val(0);

				$('#total' + id + '' + frId).html(total);
				$('#qty' + id + '' + frId).focus();
			}
		}
	</script>
	<!-- <script type="text/javascript">
$(document).ready(function() {
	
	
    $('#menu').change(
            function() {
            	
                $.getJSON('${findItemsByCatId}', {
                    menuId : $(this).val(),
                    ajax : 'true'
                }, function(data) {
                    var len = data.length;

					$('#items')
				    .find('option')
				    .remove()
				    .end()
				    $("#items").append(
                                 $("<option></option>").attr(
                                     "value","0").text("Select Item")
                             );
                    for ( var i = 0; i < len; i++) {
                    	 
                                
                        $("#items").append(
                                $("<option></option>").attr(
                                    "value", data[i].id).text(data[i].name)
                            );
                    }

                    $("#items").trigger("chosen:updated");
                });
            });
});
</script> -->


	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$('#fr_id')
									.change(

											function() {
												$('#table_grid td').remove();
												$
														.getJSON(
																'${findAllMenus}',
																{
																	fr_id : $(
																			this)
																			.val(),
																	ajax : 'true'
																},
																function(data) {
																	var html = '<option value="0">Menu</option>';

																	var len = data.length;

																	$('#menu')
																			.find(
																					'option')
																			.remove()
																			.end()

																	$("#menu")
																			.append(
																					$(
																							"<option></option>")
																							.attr(
																									"value",
																									"0")
																							.text(
																									"Select Menu"));

																	for (var i = 0; i < len; i++) {
																		$(
																				"#menu")
																				.append(
																						$(
																								"<option></option>")
																								.attr(
																										"value",
																										data[i].menuId)
																								.text(
																										data[i].menuTitle));
																	}
																	$("#menu")
																			.trigger(
																					"chosen:updated");
																});
											});
						});
	</script>
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$("#add")
									.click(
											function() {

												var isValid = validation();
												if (isValid) {

													var frId = $(
															'#fr_id option:selected')
															.val();
													var menuId = $(
															'#menu option:selected')
															.val();
													var itemId = $(
															'#items option:selected')
															.val();
													var qty = $("#qty").val();

													$('#loader').show();

													$
															.getJSON(
																	'${insertItem}',
																	{
																		frId : frId,
																		menuId : menuId,
																		itemId : itemId,
																		qty : qty,
																		ajax : 'true',

																	},
																	function(
																			data) {

																		$(
																				'#loader')
																				.hide();
																		var len = data.length;
																		document
																				.getElementById("Submit").disabled = false;

																		$(
																				'#table_grid td')
																				.remove();

																		$
																				.each(
																						data,
																						function(
																								key,
																								item) {

																							var tr = $('<tr></tr>');

																							tr
																									.append($(
																											'<td></td>')
																											.html(
																													key + 1));

																							tr
																									.append($(
																											'<td></td>')
																											.html(
																													item.itemName));

																							tr
																									.append($(
																											'<td style="text-align:right;"></td>')
																											.html(
																													item.orderQty));

																							tr
																									.append($(
																											'<td style="text-align:right;"></td>')
																											.html(
																													item.orderMrp));

																							tr
																									.append($(
																											'<td style="text-align:right;"></td>')
																											.html(
																													item.orderRate
																															+ '<input type="hidden" value='+item.minQty+' id=minqty'+item.itemId+' />'));
																							var total = item.orderQty
																									* item.orderRate;
																							tr
																									.append($(
																											'<td style="text-align:right;"></td>')
																											.html(
																													total
																															.toFixed(2)));

																							tr
																									.append($(
																											'<td style="text-align:center;"></td>')
																											.html(
																													"<a href='#' class='action_btn' onclick=deleteItem("
																															+ key
																															+ ")><i class='fa fa-trash-o  fa-lg'></i></a>"));

																							$(
																									'#table_grid tbody')
																									.append(
																											tr);

																						});

																	});
													//document.getElementById("fr_id").selectedIndex = "0";
													//$("#fr_id").trigger("chosen:updated");
													//document.getElementById("menu").selectedIndex = "0";
													//$('#menu')
													// .find('option')
													// .remove()
													// .end()
													//$("#menu").trigger("chosen:updated");
													document
															.getElementById("items").selectedIndex = "0";
													/* 	$('#items')
													 .find('option')
													 .remove()
													 .end() */
													$("#items").trigger(
															"chosen:updated");

													document
															.getElementById("qty").value = 0;

												}
											});

						});
	</script>
	<script type="text/javascript">
		function validation() {

			var frId = $('#fr_id').val();
			var menuId = $('#menu').val();
			//var itemId=$('#items').val();
			//var qty=$("#qty").val();
			var sectionId = $('#sectionId').val();
			var isValid = true;
			if (frId == "" || frId == 0) {
				isValid = false;
				alert("Please Select Franchisee");
			} else if (menuId == "" || menuId == 0) {
				isValid = false;
				alert("Please Select Menu ");
			} else if (sectionId == "" || sectionId == 0) {
				isValid = false;
				alert("Please Select Section ");
			}
			return isValid;
		}
		function validation1() {

			var frId = $('#fr_id1').val();
			var menuId = $('#menu').val();
			var itemId = $('#itemId').val();
			var qty = $("#qty").val();
			var sectionId = $('#sectionId').val();
			var isValid = true;
			if (frId == "" || frId == 0) {
				isValid = false;
				alert("Please Select Franchisee");
			} else if (menuId == "" || menuId == 0) {
				isValid = false;
				alert("Please Select Menu ");
			} else if (sectionId == "" || sectionId == 0) {
				isValid = false;
				alert("Please Select Section ");
			} else if (itemId == "" || itemId == 0) {
				isValid = false;
				alert("Please Select Item ");
			} else if (qty == "" || qty == 0) {
				isValid = false;
				alert("Please Enter valid Qty ");
			}
			return isValid;
		}
	</script>
	<script type="text/javascript">
		function deleteItem(key) {
			var isDel = confirm('Are you sure want to delete this record');
			if (isDel == true) {
				$
						.getJSON(
								'${deleteItems}',
								{

									key : key,

									ajax : 'true',

								},
								function(data) {

									var len = data.length;
									if (len == 0) {
										document.getElementById("Submit").disabled = true;

									}
									$('#table_grid td').remove();

									$
											.each(
													data,
													function(key, item) {

														var tr = $('<tr></tr>');

														tr
																.append($(
																		'<td></td>')
																		.html(
																				key + 1));

														tr
																.append($(
																		'<td></td>')
																		.html(
																				item.itemName));

														tr
																.append($(
																		'<td style="text-align:right;"></td>')
																		.html(
																				item.orderQty));

														tr
																.append($(
																		'<td style="text-align:right;"></td>')
																		.html(
																				item.orderMrp));

														tr
																.append($(
																		'<td style="text-align:right;"></td>')
																		.html(
																				item.orderRate));
														var total = item.orderQty
																* item.orderRate;
														tr
																.append($(
																		'<td style="text-align:right;"></td>')
																		.html(
																				total
																						.toFixed(2)));

														tr
																.append($(
																		'<td style="text-align:center;"></td>')
																		.html(
																				"<a href='#' class='action_btn' onclick=deleteItem("
																						+ key
																						+ ")><abbr title='Delete'><i class='fa fa-trash-o  fa-lg'></i></abbr></a>"));

														$('#table_grid tbody')
																.append(tr);

													});
								});
			}
		}
	</script>
	<script type="text/javascript">
		function generateBill() {
			$('#loader1').show();

			$.getJSON('${generateManualBill}', {
				ajax : 'true'
			}, function(data) {
				$('#table_grid td').remove();
				if (data.length != 0) {
					alert("Orders Inserted Successfully");
					document.getElementById("Submit").disabled = true;

				}
				$('#loader1').hide();

			});

		}
	</script>
	<script type="text/javascript">
		function findFranchiseeData(frId) {

			if ($('#or3').is(':checked') == false) {
				$
						.getJSON(
								'${findFranchiseeData}',
								{
									fr_id : frId,
									ajax : 'true'
								},
								function(data) {
									if (data.length != 0) {
										document.getElementById("frName").value = data.frName;
										document.getElementById("gstin").value = data.frGstNo;
										document.getElementById("address").value = data.frAddress;
									}

								});
			}
		}
	</script>
	<script type="text/javascript">
		function showPdf(billNo) {

			if (billNo != 0) {
				window.open(
						'${pageContext.request.contextPath}/pdf?url=pdf/showBillPdf/By-Road/0/'
								+ billNo, '_blank');

			}

		}
	</script>
	<script type="text/javascript">
		function checkCheckedStatus() {

			$('#preview').hide();

			if ($('#or1').is(':checked')) {
				document.getElementById("searchBtn").disabled = false;
				document.getElementById("submitbill").style.display = "none";
				document.getElementById("submitorder").disabled = true;
				$("#submitorder").show();
				document.getElementById("flagRate").value = 1;
				document.getElementById("submitorder").style.backgroundColor = "blue";
				$("#mulFr").hide();
				$("#singleFr").show();
				$("#mulOrder").hide();
				$("#singleOrder").show();
				$('#table_grid td').remove();
				var frId = document.getElementById("fr_id").value;
				if (frId != "" && frId != 0) {
					findFranchiseeData(frId);
				}
				// document.getElementById("fr_id").selectedIndex = "0";
				//	$("#fr_id").trigger("chosen:updated");
				// document.getElementById("submitbill").style.backgroundColor = "#ffeadd";
			} else if ($('#or2').is(':checked')) {
				document.getElementById("searchBtn").disabled = false;
				document.getElementById("submitbill").disabled = true;
				$("#submitbill").show();
				document.getElementById("flagRate").value = 1;
				document.getElementById("submitbill").style.backgroundColor = "blue";
				// document.getElementById("submitorder").style.backgroundColor = "#ffeadd";
				document.getElementById("submitorder").style.display = "none";
				$("#mulFr").hide();
				$("#singleFr").show();
				$("#mulOrder").hide();
				$("#singleOrder").show();
				$('#table_grid td').remove();
				var frId = document.getElementById("fr_id").value;
				if (frId != "" && frId != 0) {
					findFranchiseeData(frId);
				}
				//document.getElementById("fr_id").selectedIndex = "0";
				//	$("#fr_id").trigger("chosen:updated");
			} else if ($('#or3').is(':checked')) {
				document.getElementById("searchBtn").disabled = false;
				document.getElementById("submitbill").disabled = true;
				$("#submitbill").show();
				$('#table_grid td').remove();
				document.getElementById("flagRate").value = 1;
				document.getElementById("qty").value = 0;
				$('#itemId').find('option').remove().end()
				$("#itemId").trigger("chosen:updated");

				document.getElementById("submitbill").style.backgroundColor = "blue";
				// document.getElementById("submitorder").style.backgroundColor = "#ffeadd";
				document.getElementById("submitorder").style.display = "none";
				$("#singleFr").hide();
				$("#mulFr").show();
				$("#mulOrder").show();
				$("#singleOrder").hide();
				//document.getElementById("fr_id").selectedIndex = "0";
				//	$("#fr_id").trigger("chosen:updated");
				document.getElementById("frName").value = "-";
				document.getElementById("gstin").value = "-";
				document.getElementById("address").value = "-";

				$.getJSON('${getAllMenu}', {
					ajax : 'true'
				}, function(data) {
					var html = '<option value="0">Menu</option>';

					var len = data.length;

					$('#menu').find('option').remove().end()

					$("#menu").append(
							$("<option></option>").attr("value", "0").text(
									"Select Menu"));

					for (var i = 0; i < len; i++) {
						$("#menu")
								.append(
										$("<option></option>").attr("value",
												data[i].menuId).text(
												data[i].menuTitle));
					}
					$("#menu").trigger("chosen:updated");
				});
			}

		}
		function checkOrderByStatus() {
			var isConfirm = confirm('Do you want to change order By ?');
			var ordertype = $('.order:checked').val();
			if (isConfirm) {
				if ($('#t1').is(':checked')) {

					if (ordertype == 2) {
						document.getElementById("flagRate").value = 1;
						onSearchMulFr();
						document.getElementById("flagRate").value = 0;
					} else {
						onSearch();
					}
				} else if ($('#t2').is(':checked')) {
					if (ordertype == 2) {
						document.getElementById("flagRate").value = 1;
						onSearchMulFr();
						document.getElementById("flagRate").value = 0;
					} else {
						onSearch();
					}
				}
			}
		}
		function onCatIdChangeForManOrder(menuId) {

			$.getJSON('${getItemsByCatIdManOrder}', {
				menuId : menuId,
				ajax : 'true'
			}, function(data) {
				var len = data.length;
				$('#itemId').find('option').remove().end()

				$("#itemId").append(
						$("<option></option>").attr("value", "0").text(
								"Select Item"));

				for (var i = 0; i < len; i++) {
					$("#itemId").append(
							$("<option></option>").attr("value", data[i].id)
									.text(
											data[i].itemName + "--MinQty: "
													+ data[i].minQty));
				}
				$("#itemId").trigger("chosen:updated");

			});

		}
	</script>
	<script>
		function myFunction() {
			var input, filter, table, tr, td, td1, i;
			input = document.getElementById("myInput");
			filter = input.value.toUpperCase();
			table = document.getElementById("table_grid");
			tr = table.getElementsByTagName("tr");
			for (i = 0; i < tr.length; i++) {
				td = tr[i].getElementsByTagName("td")[2];
				td1 = tr[i].getElementsByTagName("td")[1];
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



	<script>
		function setAllFrSelected(frId) {
			//alert("frId" + frId);
			//alert("hii")
			if (frId == -1) {

				$.getJSON('${getFrListofAllFr}', {

					ajax : 'true'
				}, function(data) {

					var len = data.length;

					//alert(len);

					$('#fr_id1').find('option').remove().end()
					$("#fr_id1").append($("<option value='-1'>All</option>"));
					for (var i = 0; i < len; i++) {
						$("#fr_id1").append(
								$("<option selected ></option>").attr("value",
										data[i].frId).text(data[i].frName));
					}
					$("#fr_id1").trigger("chosen:updated");
				});
			}
		}
	</script>

	<script type="text/javascript">
		function showPreviewOrder() {
			//alert("hi");

			$
					.ajax({
						type : "POST",
						url : "${pageContext.request.contextPath}/generateManualBillPreview",
						data : $("#formId").serialize(),
						dataType : 'json',
						success : function(data) {

							//alert(data);

							$('#table_grid11 td').remove();

							$.each(data, function(key, item) {

								var tr = $('<tr></tr>');

								tr.append($('<td></td>').html(key + 1));

								tr.append($('<td></td>').html(item.itemName));

								tr.append($(
										'<td style="text-align:right;"></td>')
										.html(item.orderQty));
								
								tr.append($(
								'<td style="text-align:right;"></td>')
								.html(item.orderMrp));
								
								tr.append($(
								'<td style="text-align:right;"></td>')
								.html(item.orderRate));
								
								var tot=item.orderQty*item.orderRate;
								
								tr.append($(
								'<td style="text-align:right;"></td>')
								.html(tot.toFixed(2)));

								$('#table_grid11 tbody').append(tr);

							});

						}

					});
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