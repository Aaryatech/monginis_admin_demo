<%@ page contentType="text/html;charset=UTF-8"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Monginis Admin</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!--base css styles-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/assets/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/assets/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/assets/data-tables/bootstrap3/dataTables.bootstrap.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/assets/chosen-bootstrap/chosen.min.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/assets/bootstrap-timepicker/compiled/timepicker.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/assets/clockface/css/clockface.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/assets/bootstrap-datepicker/css/datepicker.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/assets/bootstrap-daterangepicker/daterangepicker.css" />

<!--flaty css styles-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/flaty.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/flaty-responsive.css">
<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/resources/img/favicon.png">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/common.js"></script>
<!--basic scripts-->

<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
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
<!--page specific plugin scripts-->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/bootstrap-fileupload/bootstrap-fileupload.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/chosen-bootstrap/chosen.jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/clockface/js/clockface.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/bootstrap-timepicker/js/bootstrap-timepicker.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/bootstrap-colorpicker/js/bootstrap-colorpicker.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/bootstrap-daterangepicker/date.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/bootstrap-daterangepicker/daterangepicker.js"></script>
<!-- http://forum.springsource.org/showthread.php?110258-dual-select-dropdown-lists -->
<!-- http://api.jquery.com/jQuery.getJSON/ -->

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/jquery-validation/dist/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets/jquery-validation/dist/additional-methods.min.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi">
    </script>
<script type="text/javascript">

      // Load the Google Transliterate API
      google.load("elements", "1", {
            packages: "transliteration"
          });

      function onLoad(frId) {
        var options = {
            sourceLanguage:
                google.elements.transliteration.LanguageCode.ENGLISH,
            destinationLanguage:
                [google.elements.transliteration.LanguageCode.HINDI],
        //shortcutKey: 'ctrl+g',
            transliterationEnabled: true
        };

        // Create an instance on TransliterationControl with the required
        // options.
        var control =
            new google.elements.transliteration.TransliterationControl(options);

        // Enable transliteration in the textbox with id
        // 'transliterateTextarea'.
        control.makeTransliteratable(['transliterateTextarea']);   showCtype();findFranchiseeData(0)
      }
   
      google.setOnLoadCallback(onLoad);
    </script>
<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
<style type="text/css">
select {
	width: 180px;
	height: 30px;
}
</style>


</head>
<body onload="findFranchiseeData(${billNo})">

	<c:url var="getFlavourBySpfId" value="/getFlavourBySpfId" />
	<c:url var="findAddOnRate" value="/getAddOnRate" />
	<c:url var="findFranchiseeData" value="/findFranchiseeData" />
	<c:url var="addSpOrderInList" value="/addSpOrderInList" />

	<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
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

			<div class="row">
				<div class="col-md-12">
					<div class="row">
						<div class="col-md-12">
							<div class="box">
								<div class="box-title">
									<h3>
										<i class="fa fa-bars"></i>Multiple SP Manual Bill
									</h3>
								</div>


								<c:set var="allFranchiseeAndMenuList"
									value="${allFranchiseeAndMenuList}" />
								<div class="box-content">
									<form
										action="${pageContext.request.contextPath}/getSpCakeForManBill"
										class="form-horizontal" id="validation-form" method="post">


										<div class="form-group">
											<div class="col-md-2">
												Franchise Name: <font size="3" color="red">*</font>
											</div>
											<div class="col-md-4">
												<select data-placeholder="Select Franchisee" name="fr_id"
													class="form-control chosen" tabindex="-1" id="fr_id"
													data-rule-required="true" onchange="findFranchiseeData(0)" >
													<option value=""></option>
													<!-- <optgroup label="All Franchisee"> -->
													<option value="">Select Franchise</option>
													<c:forEach items="${unSelectedFrList}" var="franchiseeList">
														<c:choose>
															<c:when test="${frId==franchiseeList.frId}">
																<option selected value="${franchiseeList.frId}">${franchiseeList.frName}</option>
															</c:when>
															<c:otherwise>
																<option value="${franchiseeList.frId}">${franchiseeList.frName}</option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</select>
											</div>
											
												
											<!-- By Rate --><input type="radio" name="sel_rate" id="sel_rate"
															checked value="1" hidden="true">
											<!-- By MRP  --><input type="radio" name="sel_rate" id="sel_rate"
															value="0" hidden="true">
													
											<label class=" col-md-2 control-label menu_label">Select Menu:<font size="3" color="red">*</font></label>
						<div class=" col-md-4 controls menu_select">

							<select data-placeholder="Choose Menu"
								class="form-control chosen" tabindex="6" id="selectMenu"
								name="selectMenu">

								<option value="-1"><c:out value=""/></option>

								<c:forEach items="${unSelectedMenuList}" var="unSelectedMenu"
									varStatus="count">
									<option value="${unSelectedMenu.menuId}"><c:out value="${unSelectedMenu.menuTitle}"/></option>
								</c:forEach>

							</select>
						</div>
											
											
											
											
											
														
											

										</div>
											<div class="form-group">
											
											<div class="col-md-2" style="text-align:right;">
												Delivery Date: <font size="3" color="red" >*</font>
											</div>
											<div class="col-md-2">
												<c:choose>
													<c:when test="${menuId==46}">
														<input id="date" class="form-control date-picker"
															value="${currentDate}" name="datepicker" type="text"
															readonly>
														<input id="datepicker" class="form-control"
															value="${currentDate}" name="datepicker" type="hidden" />

													</c:when>
													<c:otherwise>
														<input id="datepicker" class="form-control date-picker"
															autocomplete="off" value="${date}" required
															name="datepicker" type="text" required>
													</c:otherwise>
												</c:choose>
											</div>
											
										
											<div class="col-md-1" align="left">Production Date:</div>
											<div class="col-md-2" align="left">
												<input id="spProdDate" readonly
													data-date-format="dd-mm-yyyy" value="${date}"
													autocomplete="off" class="form-control date-picker"
													placeholder="" name="spProdDate"  type="text" required >
											</div>
						
						</div>
									<%-- Sac Comment </form>
									<hr>

									<form
										action="${pageContext.request.contextPath}/insertManualSpBill"
										method="post" class="form-horizontal" name="from_ord"
										id="validation-form1" enctype="multipart/form-data"
										onsubmit="return validate()"> --%>
										<input type="hidden" name="fr_id" value="${frId}"> <input
											type="hidden" name="billBy" value="${billBy}"> <input
											type="hidden" name="menu_title" value="${menuTitle}">
										<input type="hidden" name="mode_add" id="mode_add"
											value="add_book"> <input type="hidden" name="sp_id"
											id="sp_id" value="${specialCake.spId}"> <input
											type="hidden" name="sp_min_weight" id="sp_min_weight"
											value="${specialCake.spMinwt}"> <input type="hidden"
											name="sp_max_weight" id="sp_max_weight"
											value="${specialCake.spMaxwt}"> <input type="hidden"
											name="sp_est_del_date" id="sp_est_del_date"
											value="${convDate}"> <input type="hidden"
											name="sp_pro_time" id="sp_pro_time"
											value="${specialCake.spBookb4}"> <input type="hidden"
											name="production_time" id="production_time"
											value="${specialCake.spBookb4} "> <input
											type="hidden" name="sp_code" id="sp_code"
											value="${specialCake.spCode}"> <input type="hidden"
											name="sp_name" id="sp_name" value="${specialCake.spName}">
										<input type="hidden" name="fr_code" id="fr_code" value="4">
										<input type="hidden" name="spPhoUpload" id="spPhoUpload"
											value="${specialCake.spPhoupload}"> <input
											type="hidden" name="isCustCh" id="isCustCh"
											value="${specialCake.isCustChoiceCk}"> <input
											type="hidden" name="prevImage" id="prevImage"
											value="${specialCake.spImage}"> <input type="hidden"
											name="isCustChoiceCk" id="isCustChoiceCk"
											value="${specialCake.isCustChoiceCk}"> <input
											type="hidden" name="spPhoUpload" id="spPhoUpload"
											value="${specialCake.spPhoupload}"> <input
											type="hidden" name="isSlotUsed" id="isSlotUsed"
											value="${specialCake.isSlotUsed}">
										<%-- Sac comment <div class="form-group">
											<div class="col-md-2">Cake Name</div>
											<div class="col-md-3" id="spDesc" style="color: #eb62ad;">
												-${specialCake.spName}</div>
											<div class="col-md-2">
												Min Weight <b>${specialCake.spMinwt} Kg </b>
											</div>
											<div class="col-md-2">
												Max Weight <b>${specialCake.spMaxwt} Kg </b>
											</div>
											<c:set var="dbRate" scope="session" value="${sprRate}" />
											<input type="hidden" name="spBackendRate" id="spBackendRate"
												value="0">
											${spBackendRate}
											<!-----------------------1-------------------------------->
											<h4 class="inrbox" id="INR" style="font-weight: bold;">
												<span style="padding: 5px; font-weight: bold;">INR -
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> &nbsp;&nbsp;0
												${(sprRate*specialCake.spMinwt)}
											</h4>
											<input type="hidden" name="sp_grand" id="sp_grand" value="0">
											${(sprRate*specialCake.spMinwt)}
											<!-----------------------1---End-------------------------->
										</div> --%>

										<div class="form-group">
											<input type="hidden" name="sptype" id="sptype" value="1" />
											
											 <div class="col-md-1">
												Sp List <font size="5" color="red">*</font>
											</div>
											<div class="col-md-2">
												<select data-placeholder="Select Menu" name="sp_cake_id"
													class="form-control chosen" tabindex="-1" id="sp_cake_id"
													data-rule-required="true">
													<option value="">Select Special Cake</option>
													<c:forEach items="${specialCakeList}" var="spCake">
														<c:choose>
															<c:when test="${specialCake.spCode==spCake.spCode}">
																<option selected value="${spCake.spCode}">${spCake.spCode}
																	- ${spCake.spName}</option>
															</c:when>
															<c:otherwise>
																<option value="${spCake.spCode}">${spCake.spCode}
																	- ${spCake.spName}</option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</select>
											</div> 

											<div class="col-md-1">
												Flavour<font size="5" color="red">*</font>
											</div>
											<div class="col-md-2">
												<select data-placeholder="Select Flavour" name="spFlavour"
													required class="form-control chosen" tabindex="-1"
													id="spFlavour">
													<option value="">Select Flavour</option>
													<c:forEach items="${filterFlavoursList}" var="flavoursList">
														<option value="${flavoursList.spfId}">${flavoursList.spfName}</option>
													</c:forEach>

												</select>
											</div>
											
											<div class="col-md-1">
												Weight<font size="5" color="red">*</font>
											</div>
											<div class="col-md-1">
												<input type="hidden" name="dbRate" id="dbRate" value="0">
																								<input type="hidden" name="flav_name" id="flav_name" value="-">
												
												
												<%-- ${sprRate} --%>

												<select name="spwt" id="spwt" class="form-control"
													onchange="onChange()" required>
													<%-- '${sprRate}' --%>
													<c:forEach items="${weightList}" var="weightList">
														<option value="${weightList}">${weightList}</option>
													</c:forEach>
												</select>
											</div>
											
											<div class="col-md-1">
												Rate<font size="2" color="red">*</font><div id="sac_sp_rate"></div>
												
											
											</div>
											<input type="button" class="button btn-primary" value="Add" onclick="addSpCake()"> 
											</div>
											
															<div class="box-content">

										<div class="clearfix"></div>
										
										<div id="table-scroll" class="table-scroll">
							 
									<div id="faux-table" class="faux-table" aria="hidden">
									<table id="table2" class="table table-advance" border="1">
											<thead>
												<tr class="bgpink">
										<th  class="col-md-1"align="left">Sr
															</th>
															
														<th class="col-md-2" align="center">SP Name</th>
														<th class="col-md-2" align="center">SP Flavour</th>
															<th class="col-md-2" align="center">SP Weight</th>
														<th class="col-md-2" align="center">SP Amount</th>
														<th class="col-md-1" align="center">Action</th>
												</tr>
												</thead>
												</table>
									
									</div>
									<div class="table-wrap">
									
										<table  class="table table-advance" id="table_grid"  border="1">
											<thead>
												<tr class="bgpink">
										<th  class="col-md-1" align="left">Sr
															</th>
														<th class="col-md-2" align="center">SP Code</th>
														<th class="col-md-2" align="center">SP Flavour</th>
															<th class="col-md-2" align="center">SP Weight</th>
														<th class="col-md-2" align="center">SP Amount</th>
														<!-- <th class="col-md-1" align="center">Action</th> -->
												</tr>
												</thead>
										
										
												<tbody>
											
												
												</tbody>
												
											</table>
										</div>
									</div>


								</div>
											
											</form>
											<!--------------------------2------------------------------->
											<!--Sac comment <div class="col-md-1" style="border: 1px dashed;">
												<b>Type</b>
											</div>
											<div class="col-md-1" style="border: 1px dashed;">
												<span style="color: #eb4bad;"><b>Premium</b></span>
											</div> -->
											<!--------------------------2- End-------------------------->
										
									<%--Sac comment 	<div class="form-group">
											
											<div class="col-md-2">
												<select name="sp_event" id="sp_event"
													class="form-control chosen"
													data-placeholder="Select Message" required>

													<c:forEach items="${eventList}" var="eventList">
														<option value="${eventList.spMsgText}"><c:out
																value="${eventList.spMsgText}" /></option>
													</c:forEach>
												</select>
											</div>

											<div class="col-md-1">
												Name<font size="5" color="red"></font>
											</div>
											<div class="col-md-3">
												<input class="form-control" placeholder="Name"
													name="event_name" type="text" id="event_name" required>
											</div>
											<!--------------------------3------------------------------->
											<div class="col-md-1" style="border: 1px dashed;">
												<b>Price</b>
											</div>
											<div class="col-md-1" id="price"
												style="border: 1px dashed; font-weight: bold;">
												0
												${sprRate*specialCake.spMinwt}
											</div>
											<input name="sp_calc_price" id="sp_calc_price" value="0"
												type="hidden">
											${sprRate*specialCake.spMinwt}
											<!--------------------------3 End--------------------------->

										</div> --%>
										<!-- Sac comment <div class="form-group">
								

											<div class="col-md-1"
												style="border: 1px dashed; font-weight: bold;">Add
												Rate</div>
											<div class="col-md-1" id="rate"
												style="border: 1px dashed; font-weight: bold;">00</div>
											<input name="sp_add_rate" id="sp_add_rate" type="hidden"
												value="0">
										</div> -->
<%-- Sac comment
										<div class="form-group">
											<!-- <div class="col-md-2"> -->
											<!-- Customer Name -->
											<!-- </div> -->
											<!-- <div class="col-md-3"> -->
											<input class="form-control" placeholder="Customer Name"
												required name="sp_cust_name" type="hidden" id="sp_cust_name"
												required value="-">
											<!-- </div> -->

											<!-- <div class="col-md-1"> -->
											<!-- DOB -->
											<!-- </div> -->
											<!-- <div class="col-md-3" > -->
											<input id="datepicker4" data-date-format="dd-mm-yyyy"
												required autocomplete="off" class="form-control date-picker"
												placeholder="" name="datepicker4" type="hidden"
												value="${currentDate}" required>
											<div class="col-md-2">Franchise Name</div>
											<div class="col-md-3">
												<input name="fr_name" id="fr_name" class="form-control"
													type="text">
											</div>

											<div class="col-md-1">GST No</div>
											<div class="col-md-3">
												<input name="gst_no" id="gst_no" class="form-control"
													type="text" value="-">
											</div>

											<!-- </div> -->

											<!-----------------------4-------------------------------->
											<div class="col-md-1">
												<b>E.Charges</b>
											</div>
											<div class="col-md-1">
												<input name="sp_ex_charges" required id="sp_ex_charges"
													type="text" value="0" oninput="chChange()"
													style="width: 80px; border-radius: 10px; text-align: center; height: 27px;">
											</div>
											<!-----------------------4 End---------------------------->

										</div> --%>

										<%--Sac comment <div class="form-group">

											<div class="col-md-2" id="englishDiv" style="display: none;">
												<textarea id="textarea" name="sp_inst2" cols="" rows=""
													style="visibility: hidden; width: 240px; height: 50px"
													maxlength="300">-</textarea>
											</div>
											<div class="col-md-2">Order No:</div>
											<div class="col-md-3">
												<input class="form-control" placeholder="Order No"
													name="sp_place" id="sp_place" type="text" value="${spNo}"
													readonly>
											</div>
											<div class="col-md-1">
												Menu<font size="5" color="red">*</font>
											</div>
											<div class="col-md-3">
												<select name="spMenuId" class="form-control chosen"
													data-placeholder="Menu" id="spMenuId" required>
													<option value="">Select Menu</option>
													<c:forEach items="${frMenuList}" var="frMenuList">
														<c:choose>
															<c:when test="${frMenuList.mainCatId==5}">
																<option value="${frMenuList.menuId}">
																	<c:out value="${frMenuList.menuTitle}" /></option>
															</c:when>
														</c:choose>
													</c:forEach>
												</select>
											</div>
											<!--  <div class="col-md-1" ></div> -->
											<!-----------------------5-------------------------------->
											<div class="col-md-1" style="font-weight: bold;">Discount(%)</div>
											<div class="col-md-1">
												<input name="sp_disc" id="sp_disc" required type="text"
													value="0" oninput="chChange()"
													style="width: 80px; border-radius: 10px; text-align: center; height: 27px;">
											</div>
											<!-----------------------5-End-------------------------->
										</div> --%>

							<%--Sac comment			<div class="form-group">
											<div class="col-md-2">
												<!-- Franchise Name -->
											</div>
											<div class="col-md-3">
												<!-- <input name="fr_name" id="fr_name" class="form-control" type="text"> -->
											</div>

											<div class="col-md-1">
												<!-- GST  No -->
											</div>
											<div class="col-md-3">
												<!-- <input name="gst_no" id="gst_no" class="form-control" type="text" value="-"> -->
											</div>
											<!-----------------------6-------------------------------->
											<div class="col-md-1"
												style="border: 1px dashed; font-weight: bold;">
												<b>Sub Total</b>
											</div>
											<div class="col-md-1"
												style="border: 1px dashed; font-weight: bold;" id="subtotal">
												0
												${sprRate*specialCake.spMinwt}
											</div>
											<input name="sp_sub_total" id="sp_sub_total" type="hidden"
												value="0">
											${sprRate*specialCake.spMinwt}
											<!-----------------------6-End------------------------------->


										</div>

										<div class="form-group">
											<div class="col-md-2">
												<!-- Cust Email -->
											</div>
											<div class="col-md-3">
												<input name="cust_email" id="cust_email"
													class="form-control" type="hidden" value="-">
											</div>
											<div class="col-md-1">
												<!-- Cust Mobile. -->
											</div>
											<div class="col-md-3">
												<input name="cust_mobile" id="cust_mobile"
													class="form-control" type="hidden" value="-">
											</div>
											<!--  <div class="col-md-4" style="text-align: center;"></div> -->
											<!-----------------------7-------------------------------->
											<div class="col-md-1" style="border: 1px dashed;">
												<b>GST (%)</b>
											</div>
											<div class="col-md-1" id="taxPer3"
												style="border: 1px dashed; font-weight: bold;">
												${specialCake.spTax1+specialCake.spTax2}</div>
											<input type="hidden" id="tax3" name="tax3"
												value="${specialCake.spTax1+specialCake.spTax2}">
											<!-----------------------7-End------------------------------->
										</div>
										<div class="form-group">
											<div class="col-md-2">
												<!-- Order No: -->
											</div>
											<div class="col-md-3">
												<input class="form-control" placeholder="Order No" name="sp_place" id="sp_place" type="text" value="${spNo}" readonly>
											</div>
											<div class="col-md-1">
												<!-- Select Menu -->
											</div>
											<div class="col-md-3"></div>


											<!---------------------8-------------------------------->
											<div class="col-md-1"
												style="border: 1px dashed; font-weight: bold;">GST RS.</div>
											<c:set var="varGstRs"
												value="${(((sprRate*specialCake.spMinwt)*100)/((specialCake.spTax1+specialCake.spTax2)+100))*(specialCake.spTax1+specialCake.spTax2)/100}" />
											<fmt:formatNumber var="fGstRs" minFractionDigits="2"
												maxFractionDigits="2" type="number" value="${varGstRs}" />

											<div class="col-md-1" id="gstrs"
												style="border: 1px dashed; font-weight: bold;">
												0
												<c:out value="${fGstRs}" />
											</div>
											<input type="hidden" id="gst_rs" name="gst_rs" value="0">
											 ${fGstRs}
											<!---------------------8-End----------------------------->
										</div>
										<div class="form-group">

											<c:choose>
												<c:when test="${specialCake.isCustChoiceCk=='1'}">
													<div class="col-md-1">Photo Cake1</div>

													<div class="col-sm-2 col-lg-2 controls">
														<div class="fileupload fileupload-new"
															data-provides="fileupload">
															<div class="fileupload-new img-thumbnail"
																style="width: 120px; height: 40px;">
																<img
																	src="http://www.placehold.it/200x150/EFEFEF/AAAAAA&amp;text=no+image"
																	alt="" />
															</div>
															<div
																class="fileupload-preview fileupload-exists img-thumbnail"
																style="max-width: 85px; max-height: 40px; line-height: 20px;"></div>
															<div>
																<span class="btn btn-default btn-file"><span
																	class="fileupload-new">Select image</span> <span
																	class="fileupload-exists">Change</span> <input
																	type="file" class="file-input" id="order_photo"
																	name="order_photo" /></span> <a href="#"
																	class="btn btn-default fileupload-exists"
																	data-dismiss="fileupload">Remove</a>
															</div>
														</div>

													</div>
													<div class="col-md-1">Photo Cake2</div>

													<div class="col-sm-2 col-lg-2 controls">
														<div class="fileupload fileupload-new"
															data-provides="fileupload">
															<div class="fileupload-new img-thumbnail"
																style="width: 120px; height: 40px;">
																<img
																	src="http://www.placehold.it/200x150/EFEFEF/AAAAAA&amp;text=no+image"
																	alt="" />
															</div>
															<div
																class="fileupload-preview fileupload-exists img-thumbnail"
																style="max-width: 85px; max-height: 40px; line-height: 20px;"></div>
															<div>
																<span class="btn btn-default btn-file"><span
																	class="fileupload-new">Select image</span> <span
																	class="fileupload-exists">Change</span> <input
																	type="file" class="file-input" id="cust_choice_ck"
																	name="cust_choice_ck" /></span> <a href="#"
																	class="btn btn-default fileupload-exists"
																	data-dismiss="fileupload">Remove</a>
															</div>
														</div>

													</div>



												</c:when>
												<c:when test="${specialCake.spPhoupload=='1'}">
													<div class="col-md-1">Photo Cake</div>
													<div class="col-sm-2 col-lg-2 controls">
														<div class="fileupload fileupload-new"
															data-provides="fileupload">
															<div class="fileupload-new img-thumbnail"
																style="width: 100px; height: 50px;">
																<img
																	src="http://www.placehold.it/200x150/EFEFEF/AAAAAA&amp;text=no+image"
																	alt="" />
															</div>
															<div
																class="fileupload-preview fileupload-exists img-thumbnail"
																style="max-width: 85px; max-height: 40px; line-height: 20px;"></div>
															<div>
																<span class="btn btn-default btn-file"><span
																	class="fileupload-new">Select image</span> <span
																	class="fileupload-exists">Change</span> <input
																	type="file" class="file-input" id="order_photo"
																	name="order_photo" /></span> <a href="#"
																	class="btn btn-default fileupload-exists"
																	data-dismiss="fileupload">Remove</a>
															</div>
														</div>

													</div>
													<div class="col-md-3"></div>
												</c:when>
												<c:otherwise>
													<div class="col-md-6"></div>
												</c:otherwise>
											</c:choose>
											<div class="col-md-2" style="text-align: center;">
												<input type="hidden" name="hdnbt" id="hdnbt" value="0" /> <input
													class="btn btn-primary" value="Order"
													onclick="callSubmit()" type="button" id="click"
													name=orderClick> <input name="billClick"
													type="button" class="btn btn-primary"
													onclick="callBillSubmit()" value="Order&Bill"
													id="billClick">
											</div>
											<div class="col-md-4" style="text-align: center;"></div>
											<!----------------------9-------------------------------->
											<c:set var="varMgstamt"
												value="${(((sprRate*specialCake.spMinwt)*100)/((specialCake.spTax1+specialCake.spTax2)+100))}" />
											<fmt:formatNumber var="fMgstamt" minFractionDigits="2"
												maxFractionDigits="2" type="number" value="${varMgstamt}" />

											<div class="col-md-2" id="mgstamt"
												style="border: 1px dashed; font-weight: bold;">
												AMT- 0
												<c:out value="${fMgstamt}"></c:out>
											</div>

											<input type="hidden" name="m_gst_amt" id="m_gst_amt"
												type="hidden" value="0" />
											"${fMgstamt}

											<div class="col-md-2" id="tot"
												style="border: 1px dashed; font-weight: bold;">
												TOTAL - 0
												${(sprRate*specialCake.spMinwt)}
											</div>
											<input type="hidden" name="total_amt" id="total_amt"
												value="0">
											${(sprRate*specialCake.spMinwt)}

											<!----------------------9----End-------------------------->
											<input type="hidden" id="tax1" name="tax1"
												value="${specialCake.spTax1}"> <input type="hidden"
												id="tax2" name="tax2" value="${specialCake.spTax2}">

											<c:if
												test="${specialCake.spTax1==0 or specialCake.spTax1==0.00}">
												<input type="hidden" id="t1amt" name="t1amt" value="0.0">
											</c:if>

											<c:if
												test="${specialCake.spTax1!=0 or specialCake.spTax1!=0.00}">
												<input type="hidden" id="t1amt" name="t1amt" value="0">
												${(sprRate*specialCake.spMinwt)*(specialCake.spTax1)/100}
											</c:if>
											<c:if
												test="${specialCake.spTax2==0 or specialCake.spTax2!=0.00}">
												<input type="hidden" id="t2amt" name="t2amt" value="0.0">
											</c:if>
											<c:if
												test="${specialCake.spTax2!=0 or specialCake.spTax2!=0.00}">
												<input type="hidden" id="t2amt" name="t2amt" value="0">
												 ${(sprRate*specialCake.spMinwt)*(specialCake.spTax2)/100}
											</c:if>
											<input type="hidden" id="dbAdonRate" name="dbAdonRate">
											<input type="hidden" id="dbPrice" name="dbPrice" value="0">
											 ${sprRate}
											Sac comment  <input type="hidden" id="sp_id" name="sp_id"
												value="${specialCake.spId}">
										</div> --%>
										<%-- Sac comment <div class="form-group">
											<div id="ctype1">
												<div class="col-md-2" id="cktype">
													<!-- Cake Type -->
												</div>
												<div class="col-md-2">
													<input class="form-control" name="ctype" type="hidden"
														id="ctype" value="">
												</div>
											</div>
											<input class="texboxitemcode" name="temp" type="hidden"
												id="temp" value="${cutSec}">
											<!-- <div class="col-md-1">Cust GST</div>
						<div class="col-md-2">-->
											<input name="cust_gst_no" id="cust_gst_no"
												class="form-control" type="hidden" value="-">
											<!-- </div> -->
										</div>
										<textarea id="transliterateTextarea" name="sp_inst1" cols=""
											rows=""
											style="visibility: hidden; width: 240px; height: 50px"
											maxlength="300">-</textarea> --%>

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
		<script type="text/javascript">
		function addSpCake(){
			alert("Hi");
			
			   $.ajax({
			       type: "POST",
			            url: "${pageContext.request.contextPath}/addSpOrderInList",
			            data: $("#validation-form").serialize(),
			            dataType: 'json',
			    success: function(data){
			  
			    	
			    	
					//$('#loader').hide();
					if (data == "") {
						alert("No records found !!");

					}
					$('#table_grid td').remove();

					$.each(data,function(key, ordData) {

										var index = key + 1;

										var tr = $('<tr></tr>');

										tr.append($(
												'<td class="col-sm-1"></td>')
												.html(key + 1));
										
										tr
										.append($(
												'<td class="col-md-1"></td>')
												.html(ordData.spCode));
										
										tr
										.append($(
												'<td class="col-md-2"></td>')
												.html(ordData.flavour));
										
										tr
										.append($(
												'<td class="col-md-2"></td>')
												.html(ordData.spWeight));
													
												
										tr
										.append($(
												'<td class="col-md-1"></td>')
												.html(ordData.spAmt));	
															
										/* tr
										.append($(
												'<td class="col-md-1">&nbsp;&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/viewBillDetails?sellBillNo='+ ordData.itemId+'&billDate='+ordData.itemId+'&frName='+ordData.frId+'" class="action_btn" name='+'><abbr title="Details"><i class="fa fa-list"></i></abbr></a></td>'));								
																 */
										$('#table_grid tbody').append(
												tr);
										
									
									})
					
			    }
			    }).done(function() {
			   
			    });
			
		}
		
		</script>
		
	<script type="text/javascript">
		function callSubmit() {
			var isValid=validate();
			if(isValid){
				var isInsert=confirm("Do you want to save your ORDER     !");
	             if(isInsert==true)	{
			document.getElementById("hdnbt").value=0;
			var form=document.getElementById("validation-form1");
			form.submit();
	             }
			}
		}
		function callBillSubmit() {
			var isValid=validate();
			if(isValid){
				 var isInsert=confirm("Do you want to save your ORDER &  BILL     !");
             if(isInsert==true)	{
			document.getElementById("hdnbt").value=1;
			var form=document.getElementById("validation-form1");
			form.submit();
             }
			}
		}
		</script>
	<script type="text/javascript">

			function spTypeChange(spType) {

				findFranchiseeData(0);
				$.getJSON('${getFlavourBySpfId}', {
					spType : spType,
					ajax : 'true'
				}, function(data) {
					var html = '<option value="">Select Flavour</option>';
					//alert(JSON.stringify(data));
					var len = data.length;
					
					for ( var i = 0; i < len; i++) {
						html += '<option value="' + data[i].spfId + '">'
								+ data[i].spfName + '</option>';
					}
					html += '</option>';
					$('#spFlavour').html(html);
				    $("#spFlavour").trigger("chosen:updated");

				});
			}
</script>
	<script type="text/javascript">
$(document).ready(function() { 
	$('#spFlavour').change(
			
			function() {
				var spId=document.getElementById("sp_id").value;
				//alert("spId" +spId);
				//alert("flav Id " +$(this).val());
				var spfName=$("#spFlavour option:selected").html();
				alert("spfName " +spfName)
				 document.getElementById("flav_name").value=spfName;
				 
				$.getJSON('${findAddOnRate}', {
					spId:spId,
					spfId : $(this).val(),
					ajax : 'true'
				}, function(data) {
					
				//alert("Flavour Data " +JSON.stringify(data));
				
				
				var billByradio = $("input[name='sel_rate']:checked").val();
				if(billByradio==1){
					 document.getElementById("sac_sp_rate").innerHTML=data.rate;
				}else{
					 document.getElementById("sac_sp_rate").innerHTML=data.mrp;
				}
				
				
					 $('#rate').empty();	
					 $("#dbAdonRate").val(0);//data.spfAdonRate
					$("#rate").html(data.mrp);//data.spfAdonRate
					document.getElementById("dbPrice").value=data.mrp;//new
					document.getElementById("dbRate").value=data.mrp;//new
					document.getElementById("spBackendRate").value=data.rate;//new
					
					document.getElementById("sp_add_rate").setAttribute('value',0);//data.spfAdonRate
				
					
					
					var wt = $('#spwt').find(":selected").text();
					
					var flavourAdonRate=0;//data.spfAdonRate;
					
					var tax3 = parseFloat($("#tax3").val());
					var tax1 = parseFloat($("#tax1").val());
					var tax2 = parseFloat($("#tax2").val());
					var sp_ex_charges= parseFloat($("#sp_ex_charges").val());
					//alert("sp_ex_charges"+sp_ex_charges);
					var sp_disc=parseFloat($("#sp_disc").val());
					//alert("sp_disc"+sp_disc);
					var price =data.mrp;// $("#dbPrice").val();
				
					var totalFlavourAddonRate= wt*flavourAdonRate;
					var billBy=${billBy};
					//alert("Bill by " +billBy);
					
					if(billBy==1){
						totalFlavourAddonRate= wt*(flavourAdonRate*0.8);
					}	totalFlavourAddonRate.toFixed(2);
					//alert("spFlavour .chnge " +totalFlavourAddonRate)
					
					//totalFlavourAddonRate.toFixed(2);

					 var totalCakeRate= wt*price;
					 var totalAmount=parseFloat(totalCakeRate+totalFlavourAddonRate)+sp_ex_charges;
					 var disc_amt=(totalAmount*sp_disc)/100;
					 totalAmount=totalAmount-disc_amt;
					 
					 var mrpBaseRate=parseFloat((totalAmount*100)/(tax3+100));
				    /*  var gstInRs=parseFloat((mrpBaseRate*tax3)/100);
				     
				        var taxPerPerc1=parseFloat((tax1*100)/tax3);
						var taxPerPerc2=parseFloat((tax2*100)/tax3);
			         
						var tax1Amt=parseFloat((gstInRs*taxPerPerc1)/100);
						var tax2Amt=parseFloat((gstInRs*taxPerPerc2)/100); */
						var gstInRs=0;
						var taxPerPerc1=0;
						var taxPerPerc2=0;
						var tax1Amt=0;
						var tax2Amt=0;
						if(tax3==0)
							{
							    gstInRs=0;
							
							}
					    else
						{
						   gstInRs=(mrpBaseRate*tax3)/100;
							
						   if(tax1==0)
							{
							   taxPerPerc1=0;
							}
						   else
							{
							    taxPerPerc1=parseFloat((tax1*100)/tax3);
							    tax1Amt=parseFloat((gstInRs*taxPerPerc1)/100);

							}
						   if(tax2==0)
							{
							   taxPerPerc2=0;
							}
						   else
							{
								taxPerPerc2=parseFloat((tax2*100)/tax3);
								tax2Amt=parseFloat((gstInRs*taxPerPerc2)/100);

							}
						}
						
					  var grandTotal=parseFloat(totalCakeRate+totalFlavourAddonRate);
					  
					    $('#price').html(totalCakeRate);$('#sp_calc_price').html(totalCakeRate);
						$('#rate').html(totalFlavourAddonRate);$('#sp_add_rate').html(totalFlavourAddonRate);
						document.getElementById("sp_add_rate").setAttribute('value',totalFlavourAddonRate);
						$('#subtotal').html(totalAmount);
						
						document.getElementById("sp_sub_total").setAttribute('value',totalAmount);
						$('#INR').html('INR-'+totalAmount);
						document.getElementById("sp_grand").setAttribute('value',totalAmount);
						$('#tot').html('TOTAL-'+totalAmount);
						document.getElementById("total_amt").setAttribute('value',totalAmount);
						//$('#rmAmt').html(grandTotal);
						//document.getElementById("rm_amount").setAttribute('value',totalAmount);
						
						document.getElementById("t1amt").setAttribute('value',tax1Amt.toFixed(2));
						
						document.getElementById("t2amt").setAttribute('value',tax2Amt.toFixed(2));
						
						$('#gstrs').html(gstInRs.toFixed(2)); 
						document.getElementById("gst_rs").setAttribute('value',gstInRs.toFixed(2));
						$('#mgstamt').html('AMT-'+mrpBaseRate.toFixed(2)); 
						document.getElementById("m_gst_amt").setAttribute('value',mrpBaseRate.toFixed(2));
						
				});
			});
});
</script>

	<script type="text/javascript">
		function onChange() {
			var dbRate=$("#dbRate").val();

			var wt = $('#spwt').find(":selected").text();
			var flavourAdonRate =$("#dbAdonRate").val();
					
			var tax3 = parseFloat($("#tax3").val());
			var tax1 = parseFloat($("#tax1").val());
			var tax2 = parseFloat($("#tax2").val());
			//alert("tax1:"+tax1+"tax2"+tax2+"tax3"+tax3);
			var sp_ex_charges= parseFloat($("#sp_ex_charges").val());
			var sp_disc=parseFloat($("#sp_disc").val());
			//document.getElementById("adv").value=0;
					
			var totalCakeRate = wt*dbRate;
				
			var billBy=${billBy};
			//alert("Bill by " +billBy);
			var totalFlavourAddonRate = wt*flavourAdonRate;
			//alert("totalFlavourAddonRate by " +totalFlavourAddonRate);
			if(billBy==1){
				totalFlavourAddonRate= wt*(flavourAdonRate*0.8);
				//alert("totalFlavourAddonRate in if by =1 " +totalFlavourAddonRate);
			}	
			//alert("spFlavour .chnge " +totalFlavourAddonRate)
			totalFlavourAddonRate.toFixed(2);
		    var add=parseFloat(totalCakeRate+totalFlavourAddonRate);
		    var grandTotal=parseFloat(add);
			var spSubtotal=add+sp_ex_charges;
			var disc_amt=(spSubtotal*sp_disc)/100;
			
			spSubtotal=spSubtotal-disc_amt;
			var mrpBaseRate=parseFloat((spSubtotal*100)/(tax3+100));
			
			var gstInRs=0;
			var taxPerPerc1=0;
			var taxPerPerc2=0;
			var tax1Amt=0;
			var tax2Amt=0;
			if(tax3==0)
				{
				    gstInRs=0;
				
				}
		    else
			{
			   gstInRs=(mrpBaseRate*tax3)/100;
				
			   if(tax1==0)
				{
				   taxPerPerc1=0;
				}
			   else
				{
				    taxPerPerc1=parseFloat((tax1*100)/tax3);
				    tax1Amt=parseFloat((gstInRs*taxPerPerc1)/100);

				}
			   if(tax2==0)
				{
				   taxPerPerc2=0;
				}
			   else
				{
					taxPerPerc2=parseFloat((tax2*100)/tax3);
					tax2Amt=parseFloat((gstInRs*taxPerPerc2)/100);

				}
			}
			
         

			$('#gstrs').html(gstInRs.toFixed(2));  
			document.getElementById("gst_rs").setAttribute('value',gstInRs.toFixed(2));

			var mGstAmt=mrpBaseRate;
			$('#mgstamt').html('AMT-'+mGstAmt.toFixed(2)); 
			document.getElementById("m_gst_amt").setAttribute('value',mGstAmt.toFixed(2));
			
			$('#price').html(wt*dbRate);
			$('sp_calc_price').html(wt*dbRate);
			//$('#rate').html(wt*flavourAdonRate);	
			//document.getElementById("sp_add_rate").setAttribute('value',wt*flavourAdonRate);
			
			$('#rate').html(totalFlavourAddonRate);$('#sp_add_rate').html(totalFlavourAddonRate);
			document.getElementById("sp_add_rate").setAttribute('value',totalFlavourAddonRate);
			$('#subtotal').html(spSubtotal);	
			document.getElementById("sp_sub_total").setAttribute('value',spSubtotal);
			
			$('#INR').html('INR-'+spSubtotal);
			document.getElementById("sp_grand").setAttribute('value',spSubtotal);
			$('#tot').html('TOTAL-'+spSubtotal);
			document.getElementById("total_amt").setAttribute('value',spSubtotal);
			//$('#rmAmt').html(spSubtotal);
		//	document.getElementById("rm_amount").setAttribute('value',spSubtotal);
			
			document.getElementById("t1amt").setAttribute('value',tax1Amt.toFixed(2));
			
			document.getElementById("t2amt").setAttribute('value',tax2Amt.toFixed(2));
			
	}</script>

	<script type="text/javascript">
		$(document)
				.ready(
						function() { // if all label selected set all items selected

							$('#fr_id')
									.change(
											function() {
												var selected = $('#fr_id')
														.val();

												if (selected == -1) {
													$
															.getJSON(
																	'${setAllFrIdSelected}',
																	{
																		//	selected : selected,
																		ajax : 'true'
																	},
																	function(
																			data) {
																		var html = '<option value="">Select Franchise</option>';

																		var len = data.length;

																		$(
																				'#fr_id')
																				.find(
																						'option')
																				.remove()
																				.end()
																		$(
																				"#fr_id")
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
																					"#fr_id")
																					.append(
																							$(
																									"<option selected></option>")
																									.attr(
																											"value",
																											data[i].frId)
																									.text(
																											data[i].frName));
																		}

																		$(
																				"#fr_id")
																				.trigger(
																						"chosen:updated");
																	});
												}
											});
						});
	</script>
	<script type="text/javascript">
		$(document)
				.ready(
						function() { // if all label selected set all items selected

							$('#menu')
									.change(
											function() {
												var selected = $('#menu').val();

												if (selected == -1) {
													$
															.getJSON(
																	'${setAllMenuSelected}',
																	{
																		//	selected : selected,
																		ajax : 'true'
																	},
																	function(
																			data) {
																		var html = '<option value="">Select Menus</option>';

																		var len = data.length;

																		$(
																				'#menu')
																				.find(
																						'option')
																				.remove()
																				.end()
																		$(
																				"#menu")
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
																					"#menu")
																					.append(
																							$(
																									"<option selected></option>")
																									.attr(
																											"value",
																											data[i].settingId)
																									.text(
																											data[i].menuTitle));
																		}

																		$(
																				"#menu")
																				.trigger(
																						"chosen:updated");
																	});
												}
											});
						});
	</script>

	<script>

function chChange() {
	var wt = $('#spwt').find(":selected").text();
	var flavourAdonRate =$("#dbAdonRate").val();
	var tax3 = parseFloat($("#tax3").val());
	var tax1 = parseFloat($("#tax1").val());
	var tax2 = parseFloat($("#tax2").val());
	//document.getElementById("adv").value=0;
	var sp_ex_charges= parseFloat($("#sp_ex_charges").val());
	var sp_disc=parseFloat($("#sp_disc").val());
	//alert("sp_disc"+sp_disc);
	var dbRate = $("#dbPrice").val();//dbRate
	//alert("tax1:"+tax1+"tax2"+tax2+"tax3"+tax3);
	var billBy=${billBy};
	
	var totalCakeRate = wt*dbRate;
	var totalFlavourAddonRate = wt*flavourAdonRate;
	if(billBy==1){
		totalFlavourAddonRate= wt*(flavourAdonRate*0.8);
		//alert("totalFlavourAddonRate in if by =1 " +totalFlavourAddonRate);
	}	
    var add=parseFloat(totalCakeRate+totalFlavourAddonRate);
    var grandTotal=parseFloat(add);
    //alert("without sp_ex_charges"+add);
	var spSubtotal=add+sp_ex_charges;
	//alert("with sp_ex_charges"+spSubtotal);
	//document.getElementById("adv").value=0;
	
	var disc_amt=(spSubtotal*sp_disc)/100;
	//alert("disc_amt"+disc_amt);
	
	spSubtotal=spSubtotal-disc_amt;
	
	
	//alert("final "+spSubtotal);
	
	
	var mrpBaseRate=parseFloat((spSubtotal*100)/(tax3+100));
	
	var gstInRs=0;
	var taxPerPerc1=0;
	var taxPerPerc2=0;
	var tax1Amt=0;
	var tax2Amt=0;
	if(tax3==0)
		{
		    gstInRs=0;
		
		}
    else
	{
	   gstInRs=(mrpBaseRate*tax3)/100;
		
	   if(tax1==0)
		{
		   taxPerPerc1=0;
		}
	   else
		{
		    taxPerPerc1=parseFloat((tax1*100)/tax3);
		    tax1Amt=parseFloat((gstInRs*taxPerPerc1)/100);

		}
	   if(tax2==0)
		{
		   taxPerPerc2=0;
		}
	   else
		{
			taxPerPerc2=parseFloat((tax2*100)/tax3);
			tax2Amt=parseFloat((gstInRs*taxPerPerc2)/100);

		}
	}
	
 

	$('#gstrs').html(gstInRs.toFixed(2));  document.getElementById("gst_rs").setAttribute('value',gstInRs.toFixed(2));

	var mGstAmt=mrpBaseRate;
	$('#mgstamt').html('AMT-'+mGstAmt.toFixed(2));  document.getElementById("m_gst_amt").setAttribute('value',mGstAmt.toFixed(2));
	
	$('#price').html(wt*dbRate);
	document.getElementById("sp_calc_price").value=wt*dbRate;
	$('#rate').html(wt*flavourAdonRate);	
	document.getElementById("sp_add_rate").setAttribute('value',wt*flavourAdonRate);
	//$('#subtotal').html(grandTotal);	
	
	$('#subtotal').html(spSubtotal);	
	/* document.getElementById("sp_sub_total").setAttribute('value',add); */
	document.getElementById("sp_sub_total").setAttribute('value',spSubtotal);
	
	$('#INR').html('INR-'+spSubtotal);
	document.getElementById("sp_grand").setAttribute('value',spSubtotal);
	$('#tot').html('TOTAL-'+spSubtotal);
	document.getElementById("total_amt").setAttribute('value',spSubtotal);
	//$('#rmAmt').html(spSubtotal);
	//document.getElementById("rm_amount").setAttribute('value',spSubtotal);
	
	document.getElementById("t1amt").setAttribute('value',tax1Amt.toFixed(2));
	
	document.getElementById("t2amt").setAttribute('value',tax2Amt.toFixed(2));
	
}


</script>

	<script>
function showDiv(elem){
   if(elem.value == 1)
	   {
         document.getElementById('marathiDiv').style.display= "block";
         document.getElementById('transliterateTextarea').value = '';
         document.getElementById('englishDiv').style="display:none";
	   }
   else if(elem.value == 2)
   {
	   document.getElementById('englishDiv').style.display = "block";
	   document.getElementById('textarea').value = '';
	   document.getElementById('marathiDiv').style="display:none";
   }
 
}
</script>

	<script type="text/javascript">
function showCtype(){
var temp=document.getElementById('temp').value;
	/* if(temp==0)
		{
		document.getElementById('cktype').innerHTML = 'Alphabetical';

		}else if(temp==1)
		{
			document.getElementById('cktype').innerHTML = 'Numerical';
		}else
	if (temp == 2 ) { */  ///regular
		document.getElementById("ctype1").style = "display:none" //hide numeric
/* 	}
 */	
}

</script>
	<!------------------------------BLANK VALIDATION FOR SPCODE------------------------------------------>

	<script type="text/javascript">
function validateForm() {
    var spCode = document.forms["form"]["sp_code"].value;
    if (spCode == "") {
        alert("Special Cake Code must be filled out");
        document.getElementById('sp_code').focus();
        return false;
    }
}
</script>
	<!-------------------------------------------ALL VALIDATIONS----------------------------------------->
	<script type="text/javascript">
function validate() {
	
	 var phoneNo = /^\d{10}$/;  
	
     var eventName,spId,spCustName,spPlace,spCustMob,spType,spFlavour,spCode,spWt,deliveryDate,spProdDate,custDob,frName,gstNo,custEmail,spMenuId,custGstNo;
     eventName = document.getElementById("event_name").value;
     //spCustName=document.getElementById("sp_cust_name").value;
   //  spCustMob=document.getElementById("cust_mobile").value; 
     spType=document.getElementById("sptype").value; 
     spCode=document.getElementById("sp_cake_id").value;
     spFlavour=document.getElementById("spFlavour").value;
     deliveryDate=document.getElementById("datepicker").value;
     spProdDate=document.getElementById("spProdDate").value;
     custDob=document.getElementById("datepicker4").value;
     frName=document.getElementById("fr_name").value;
   //  gstNo=document.getElementById("gst_no").value;
   //  custEmail=document.getElementById("cust_email").value;
     spMenuId=document.getElementById("spMenuId").value;
   //  custGstNo=document.getElementById("cust_gst_no").value;
     
     spWt=document.getElementById("spwt").value;
    var isValid=true; 
    
    if (spCode == "") {
        alert("Special Cake Code must be filled out");
      
        isValid= false;
    }else  
    if (spType == "") {
        alert("Please Select Flavour Type");
      
        isValid= false;
    }else if (spFlavour == "") {
        alert("Please Select Flavour");
  
        isValid=false;
    }else  if (spWt == "") {
        alert("Please Select Special Cake Weight");
      
        isValid= false;
    }/* else  if (eventName == "") {
        alert("Please Enter Message/Name");
        document.getElementById('event_name').focus();
        
        isValid=false;
    } */else  if (deliveryDate == "") {
        alert("Please Select Date of Delivery");
        document.getElementById('datepicker').focus();
        
        isValid=false;
    }else  if (spProdDate == "") {
        alert("Please Select Date of Production");
        document.getElementById('spProdDate').focus();
        
        isValid=false;
    }  else if (spCustName == "") {
        alert("Please Enter Customer Name");
        document.getElementById('sp_cust_name').focus();

        isValid= false;
    }
    else if (custDob== "") {
        alert("Please Select Customer DOB");
        document.getElementById('datepicker4').focus();

        isValid= false;
    }
    else if (frName== "") {
    	alert("Please Select Franchise Name");
        document.getElementById('fr_name').focus();
        isValid= false;
    }
    else if (gstNo== "") {
    	alert("Please Enter GST No.");
        document.getElementById('gst_no').focus();
        isValid= false;
    }
    else if (custEmail== "") {
    	alert("Please Enter Email Of Customer");
        document.getElementById('cust_email').focus();
        isValid= false;
    }
   /*  else  if(!spCustMob.match(phoneNo))  
	  {  
    	 alert("Not a valid Mobile Number");  
	     document.getElementById('cust_mobile').value="";
	     document.getElementById('cust_mobile').focus();
	     return false;  
	  }   */
	  else   if (spMenuId== "") {
	 
		  alert("Please Select Menu");
	        document.getElementById('cust_email').focus();
	        isValid= false;
	  }  else   if (custGstNo== "") {
		  alert("Please Enter Customer GST No.");
	        document.getElementById('custGstNo').focus();
	        isValid= false;
	  }  
   
    return isValid;
 
}
</script>

	<script type="text/javascript">
function findFranchiseeData(billNo)
{
	if(billNo>0)
	showPdf(billNo);
	
	  var frId=document.getElementById("fr_id").value;
	$.getJSON(
					'${findFranchiseeData}',
					{
						fr_id:frId,
						ajax : 'true'
					},
					function(data) {
						if(data.length!=0)
							{
                              document.getElementById("fr_name").value=data.frName;
                              document.getElementById("gst_no").value=data.frGstNo;
                            //  document.getElementById("address").value=data.frAddress;
							}
						
					});
	
}
</script>
	<script type="text/javascript">
function showPdf(billNo)
{
	if(billNo!=0)
		{
        window.open('${pageContext.request.contextPath}/pdf?url=pdf/showBillPdf/By-Road/0/'+billNo,'_blank');

		}
	

}
</script>
	<script type="text/javascript">
	
	
	
 jQuery(function() {
   jQuery('#sp_cake_id').change(function() {
	   
	   $.ajax({
	       type: "POST",
	            url: "${pageContext.request.contextPath}/getSpCakeForMultiSpBill",
	            data: $("#validation-form").serialize(),
	            dataType: 'json',
	    success: function(data){
	  
	    	//alert("rate " +JSON.stringify(data.sprRate));
	    	//alert("backend rate " +JSON.stringify(data.spBackendRate));
	    	
	    	
			for (var i = 0; i < data.filterFlavoursList.length; i++) {

				$(
						"#spFlavour")
						.append(
								$(
										"<option selected></option>")
										.attr(
												"value",
												data.filterFlavoursList[i].spfId)
										.text(
												data.filterFlavoursList[i].spfName));
			}

			$("#spFlavour").trigger("chosen:updated");
			
         			
			for (var i = 0; i < data.weightList.length; i++) {

				$(
						"#spwt")
						.append(
								$(
										"<option ></option>")
										.attr(
												"value",
												data.weightList[i])
										.text(
												data.weightList[i]));
			}

			$("#spwt").trigger("chosen:updated");
            document.getElementById("sp_id").value=data.specialCake.spId;
            
			
	    }
	    }).done(function() {
	   
	    });
	   
   });
});
</script>
</body>
</html>