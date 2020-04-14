<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<style>
table{
  width:100%;
  border:1px solid #ddd;
}

tr:hover {
	background-color: #ffa;
}

td, th {
	position: relative;
}

</style>
<style>
.alert {
  padding: 20px;
  background-color: #f44336;
  color: white;
  opacity: 1;
  transition: opacity 0.6s;
  margin-bottom: 15px;
}

.alert.success {background-color: #4CAF50;}
.alert.info {background-color: #2196F3;}
.alert.warning {background-color: #ff9800;}

.closebtn {
  margin-left: 15px;
  color: white;
  font-weight: bold;
  float: right;
  font-size: 16px;
  line-height: 17px;
  cursor: pointer;
  transition: 0.3s;
}

.closebtn:hover {
  color: black;
}
</style>

<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>

    <c:url var="callSearchOrdersProcessByItem" value="/searchOrdersProcessByItem" />
	<c:url var="callSearchOrdersProcess" value="/searchOrdersProcess" />
	<c:url var="callChangeQty" value="/callChangeQty" />
	<c:url var="callDeleteOrder" value="/callDeleteOrder" />
	<c:url var="callDeleteOrderMultiple" value="/callDeleteOrderMultiple" />
	<c:url var="updateOrderDetails" value="/updateOrderDetails" />
	<c:url var="getMenusListByCatId" value="/getMenusListByCatId" />
	<c:url var="getItemListByMenuId" value="/getItemsByMenuIdMultiple"></c:url>



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
		<!-- 	<div class="page-title">
				<div>
					<h1>
						<i class="fa fa-file-o"></i>Orders
					</h1>

				</div>
			</div> -->
			<!-- END Page Title -->


			<c:set var="isEdit" value="0">
			</c:set>
			<c:set var="isView" value="0">
			</c:set>
			<c:set var="isDelete" value="0">
			</c:set>

			<%-- <input type="hidden" id="modList"
				value="${sessionScope.newModuleList}">

			<c:forEach items="${sessionScope.newModuleList}" var="modules">
				<c:forEach items="${modules.subModuleJsonList}" var="subModule">

					<c:choose>
						<c:when test="${subModule.subModuleMapping eq 'showOrders'}">

							<c:choose>
								<c:when test="${subModule.editReject=='visible'}">
									<c:set var="isEdit" value="1">
									</c:set>
								</c:when>
								<c:otherwise>
									<c:set var="isEdit" value="0">
									</c:set>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${subModule.view=='visible'}">
									<c:set var="isView" value="1">
									</c:set>
								</c:when>
								<c:otherwise>
									<c:set var="isView" value="0">
									</c:set>
								</c:otherwise>
							</c:choose>


							<c:choose>
								<c:when test="${subModule.deleteRejectApprove=='visible'}">
									<c:set var="isDelete" value="1">
									</c:set>
								</c:when>
								<c:otherwise>
									<c:set var="isDelete" value="0">
									</c:set>
								</c:otherwise>
							</c:choose>

						</c:when>
					</c:choose>
				</c:forEach>
			</c:forEach> --%>

		<%-- 	<input type="hidden" id="isDelete" value="${isDelete}"> <input
				type="hidden" id="isEdit" value="${isEdit}">
 --%>

<c:choose>
<c:when test="${orderStatus==1}">
<div class="alert">
  <span class="closebtn">&times;</span>  
  <strong>Failed!</strong> ${message}
</div>
</c:when>
<c:when test="${orderStatus==2}">
<div class="alert success">
  <span class="closebtn">&times;</span>  
  <strong>Success!</strong> ${message}
</div>
</c:when>
</c:choose>

			<!-- BEGIN Main Content -->
			<div class="row">
				<div class="col-md-12">
					<div class="box">
						<div class="box-title">
							<h3>
								<i class="fa fa-bars"></i>Split Orders
							</h3>
							<div class="box-tool">
								<a href="">Back to List</a> <a data-action="collapse" href="#"><i
									class="fa fa-chevron-up"></i></a>
							</div>
							<!-- <div class="box-tool">
								<a data-action="collapse" href="#"><i
									class="fa fa-chevron-up"></i></a> <a data-action="close" href="#"><i
									class="fa fa-times"></i></a>
							</div> -->
						</div>


						<div class="box-content">
							<form action="${pageContext.request.contextPath}/searchOrdersProcess" class="form-horizontal"
								method="post" id="validation-form">

								<div class="form-group">
									<label class="col-sm-3 col-lg-1 control-label">Franchisee
									</label>
									<div class="col-sm-3 col-lg-3 controls">

										<select data-placeholder="Select Franchisee"
											class="form-control chosen" multiple="multiple" tabindex="6"
											name="fr_id" id="fr_id" >

											<option value="0">All</option>
											<c:forEach items="${franchiseeList}" var="franchiseeList">
												<option value="${franchiseeList.frId}">${franchiseeList.frName}</option>

											</c:forEach>

										</select>

									</div>
									<!-- 	</div>
							
                        <div class="form-group"> -->
									<!-- <label class="col-sm-1 col-lg-1 control-label"> <b>OR</b></label><label
										class="col-sm-1 col-lg-1 control-label">Route</label>
									<div class="col-sm-1 col-lg-4 controls"> -->
										<input data-placeholder="Select Route" type="hidden"
										 name="selectRoute"
											id="selectRoute" onchange="disableFr()">
											<%--<option value="0">Select Route</option>
											 <c:forEach items="${routeList}" var="route" varStatus="count">
												<option value="${route.routeId}"><c:out value="${route.routeName}"/> </option>

											</c:forEach> --%>
									<!-- 	</select> -->

								<!-- 	</div>
								</div>

								<div class="form-group"> -->
									<label for="textfield2" class="col-xs-3 col-lg-1 control-label">Menu</label>
									<div class="col-sm-9 col-lg-3 controls">
										<select class="form-control chosen" 
											tabindex="6" name="menuId" id="menuId" onchange="getItemsByMenuId()">
											<option value="">Select Menu</option>
											<c:forEach items="${menuList}" var="menuList">
												<option value="${menuList.menuId}">${menuList.menuTitle}</option>

											</c:forEach>

										</select>
									</div>
								<!-- </div>

								<div class="form-group"> -->
									<label class="col-sm-3 col-lg-1 control-label">Prod.Date</label>
									<div class="col-sm-5 col-lg-2 controls">
										<input class="form-control date-picker" id="date" size="16"
											type="text" name="date" value="${date}" required />
									</div>
								<!-- </div>


								<div align="center" class="form-group">
									<div
										class="col-sm-25 col-sm-offset-3 col-lg-30 col-lg-offset-0"> -->
									


									</div>
									<div class="form-group">
											<label class="col-sm-3 col-lg-1 control-label">Items</label>
											<div class="col-sm-9 col-lg-10 controls">
												<select data-placeholder="Select Items" name="items[]"
													class="form-control chosen" tabindex="-1" id="item" multiple="multiple"
													data-rule-required="true">
                                                      <option value=""> </option>
													<optgroup label="All Items">
														<option value=""></option>
													
													</optgroup>
												</select>
												
											</div>
												<input type="button" class="btn btn-primary" value="Submit"
											id="callSubmit" onclick="callSearch()">
										</div>
										
										
									<div align="center" id="loader" style="display: none">

										<span>
											<h4>
												<font color="#343690">Loading</font>
											</h4>
										</span> <span class="l-1"></span> <span class="l-2"></span> <span
											class="l-3"></span> <span class="l-4"></span> <span
											class="l-5"></span> <span class="l-6"></span>
									</div>
</form>
<form action="${pageContext.request.contextPath}/saveSplitOrders" class="form-horizontal"
								method="post" id="validation-form1">

								<div class="box">
									<div class="box-content">

										<div class="clearfix"></div>
										<div id="table-scroll" class="table-scroll">
							
											<div class="table-wrap">

												<table id="table1" class="table table-advance" border="1">
													<thead>
														<tr class="bgpink">
															<th width="148" style="width: 18px" align="left">Sr</th>
															<th width="198" align="left">Franchisee Name</th>
															<th width="190" align="left">Item Name</th>
															<th width="120" align="left">Delivery</th>
															<th width="90" align="left">Qty</th>
															<th width="80" align="left">Split Qty</th>
															<th width="199" align="left">Update Qty </th>
														</tr>
													</thead>
													
													<tbody>
												</table>
											</div>
<br>
									<div class="form-group" align="left"  id="opt">
									<div class="col-sm-9 col-lg-3 controls">
										<select class="form-control chosen" 
											 name="splitMenuId" id="splitMenuId">
											<option value="">Select Menu</option>
											
										</select>
									</div>
										<div >
										  <label class=" col-md-2">Production Date</label>
										<div class="col-md-2">
										<input  name="split_production_date" id="split_production_date" type="text" class="form-control date-picker"/>
										  </div>
                                       <label class=" col-md-2">Delivery Date</label>
										<div class="col-md-2">
										<input  name="split_delivery_date" id="split_delivery_date" type="text" class="form-control date-picker"/>
										  </div>
										
									  </div>
<div class="col-md-1">
	<input type="submit" class="btn btn-primary"  value="Submit"  disabled="disabled" id="callupdate">
</div>	
</div>
</div>
</div></div>
</form>
</div>
</div>
				</div>
			</div></div>
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
	<!--page specific plugin scripts-->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/jquery-validation/dist/jquery.validate.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets/jquery-validation/dist/additional-methods.min.js"></script>
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
<!-- 
<script type="text/javascript">
function updateDetails()
{
	
	var delDate=$("#delivery_date").val();
	var prodDate=$("#production_date").val();
	 if(prodDate==""){
		
			alert("Please Select Production Date");
		}else if(delDate==""){
			alert("Please Select Delivery Date");
		}
		else{
	var selectedItems = new Array();
	
	var checkedVals = $('.selorder:checkbox:checked').map(function() {
	    return this.value;
	}).get();
	
	if(checkedVals=="")
		{
		 alert("Please select atleast one order!!")
		}else{
	
	$.getJSON('${updateOrderDetails}', {

		ids:checkedVals.join(","),
		delDate:delDate,
		prodDate:prodDate,
		ajax : 'true'

	}, function(data) {
		
		if(data.error==false)
			{
			
			  alert("Delivery Date updated Successfully.");
			  callSearch();
			}
		else
			{
			 alert("Delivery Date Not Updated.");
			}
	});
		}
	}
}
</script> -->
<script type="text/javascript">
	function validate() {
	
		var menu =$("#menuId").val();
		var selectFr =$("#fr_id").val();
		var selectItem =$("#item").val();

		var isValid = true;

		if(selectFr==""||selectFr==null){
			isValid = false;
			alert("Please Select Franchise");
		}else if (menu == "" || menu == null) {

			isValid = false;
			alert("Please Select Menu");
		}else if (selectItem == "" || selectItem == null) {

			isValid = false;
			alert("Please Select Items");
		}
		return isValid;

	}
</script>
<script type="text/javascript">
function callSearch() {

	//$('#all').prop('checked', false);

	//var isDelete=document.getElementById("isDelete").value;
//	var isEdit=document.getElementById("isEdit").value;
	
//	alert("isDelete" +isDelete);
	//alert("isEdit" +isEdit);
	$('#table1 td').remove();

	var isValid=validate();
	if(isValid==true){
	var menuIds=$("#menuId").val();
	$.getJSON('${getMenusListByCatId}', {
		menuId: menuIds,
		ajax : 'true'
	}, function(data) {
	 	var html = '<option value="">Select Menu</option>';
		var len = data.length;
		
		$('#splitMenuId')
	    .find('option')
	    .remove()
	    .end()
	    
	// $("#splitMenuId").append($("<option></option>").attr( "value",0).text("ALL"));
		
		for ( var i = 0; i < len; i++) {
            $("#splitMenuId").append($("<option ></option>").attr("value", data[i].menuId).text(data[i].menuTitle));
		}
		   $("#splitMenuId").trigger("chosen:updated");
	}); 
	var itemId=$("#item").val();
	var array=[];
	
	var routeIds=$("#selectRoute").val();
	var frIds=$("#fr_id").val();
		

	var date = $("#date").val(); 
	$('#loader').show();
if(itemId=="-1" || itemId==""){
$.getJSON('${callSearchOrdersProcess}', {

	fr_id_list : JSON.stringify(frIds),
	item_id_list : JSON.stringify(menuIds),
	route_id:routeIds,
	date : date,
	
	ajax : 'true'

}, function(data) { 
	//document.getElementById("expExcel").disabled=true;
	document.getElementById("callupdate").disabled=false;
	//document.getElementById("calldelete").disabled=true;
	$("#opt").css("display","block");

	$('#loader').hide();
	var len = data.length;



	$.each(data,function(key, orders) {
		//document.getElementById("expExcel").disabled=false;
	//	document.getElementById("calldelete").disabled=false;

	//	document.getElementById('range').style.display = 'block';
	var tr = $('<tr></tr>');
	
  	tr.append($('<td></td>').html(key+1));
  	tr.append($('<td></td>').html(orders.frName));
  	tr.append($('<td></td>').html(orders.itemName));
	tr.append($('<td></td>').html(orders.deliveryDate));
	tr.append($('<td></td>').html(orders.orderQty));
  	tr.append($('<td></td>').html("<input type=text onkeypress='return IsNumeric(event);' ondrop='return false;' onpaste='return false;' style='text-align: center;' class='form-control' min=0 id=splitQty"+orders.orderId+""+orders.id+"  value=0  name=splitQty"+orders.orderId+""+orders.id+"  onchange='onQtyChange("+orders.id+","+orders.orderId+","+orders.orderQty+",this.value)' >"));
  	tr.append($('<td></td>').html("<input type=text onkeypress='return IsNumeric(event);' ondrop='return false;' onpaste='return false;' style='text-align: center;' class='form-control' min=0 id=updatedQty"+orders.orderId+" value="+orders.orderQty+" name=updatedQty"+orders.orderId+" >"));
	$('#table1 tbody').append(tr); 

})

});
}
else
	{
	

	$.getJSON('${callSearchOrdersProcessByItem}', {

		fr_id_list : JSON.stringify(frIds),
		item_id_list : JSON.stringify(menuIds),
		itemId: JSON.stringify(itemId),
		route_id:routeIds,
		date : date,
		
		ajax : 'true'

	}, function(data) {
		document.getElementById("callupdate").disabled=false;

		$("#opt").css("display","block");

		$('#loader').hide();
		var len = data.length;


		$('#table1 td').remove();

		$.each(data,function(key, orders) {
		
			document.getElementById('range').style.display = 'block';
		var tr = $('<tr></tr>');
		
	  	tr.append($('<td></td>').html(key+1));

	  	tr.append($('<td></td>').html(orders.frName));

	  	tr.append($('<td></td>').html(orders.itemName));

		tr.append($('<td></td>').html(orders.deliveryDate));
	  	tr.append($('<td></td>').html(orders.orderQty));
	  	tr.append($('<td></td>').html("<input type=number onkeypress='return IsNumeric(event);' ondrop='return false;' onpaste='return false;' style='text-align: center;' class='form-control' min=0 id=splitQty"+orders.id+" value=0 name=splitQty"+orders.id+" onchange='onQtyChange("+orders.id+","+orders.orderId+","+orders.orderQty+",this.value)' >"));
	  	tr.append($('<td></td>').html("<input type=number onkeypress='return IsNumeric(event);' ondrop='return false;' onpaste='return false;' style='text-align: center;' class='form-control' min=0 id=updatedQty"+orders.orderId+" value="+orders.orderQty+" name=updatedQty"+orders.orderId+" >"));
	
	 
		$('#table1 tbody').append(tr);

	})

	});
	}
	}
}

</script>
<script type="text/javascript">
function onQtyChange(id,orderId,orderQty,splitQty)
{
	if(splitQty<=orderQty)
		{
		  var actUpdatedQty=orderQty-splitQty;
		  document.getElementById("updatedQty"+orderId).value=actUpdatedQty;
		}else
			{
			alert("Please enter less qty than order qty!!");
			}
}
</script>
<script>
function getItemsByMenuId() {
	
	var menuIds=$("#menuId").val();
	 if(menuIds=="" || menuIds==null){
				$('#item')
			    .find('option')
			    .remove()
			    .end()
			    $("#item").trigger("chosen:updated");
		 }else{
				$.getJSON('${getItemListByMenuId}', {
					
					menuId: JSON.stringify(menuIds),
					ajax : 'true'
				}, function(data) {
				 	var html = '<option value="">Select Item</option>';
				
					var len = data.length;
					
					$('#item')
				    .find('option')
				    .remove()
				    .end()
				    
				 $("#item").append($("<option></option>").attr( "value",-1).text("ALL"));
					
					for ( var i = 0; i < len; i++) {
			            $("#item").append(
			                    $("<option ></option>").attr(
			                        "value", data[i].id).text(data[i].itemName)
			                );
					}
					   $("#item").trigger("chosen:updated");
				}); 
				
		 }
}
	
	</script>
</body>
</html>