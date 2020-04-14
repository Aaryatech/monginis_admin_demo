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
<title>Configure Franchisee</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/resources/img/favicon.png">
<style type="text/css">
select {
    width: 180px;
    height: 30px;
}
</style>


</head>

<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>
	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>


	<c:url var="getHeaders" value="/getHeaders" />

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
			<!-- BEGIN Main Content -->
			<div class="row">
				<div class="col-md-12">
					<div class="row">
						<div class="col-md-12">
							<div class="box">
								<div class="box-title">
									<h3>
										<i class="fa fa-bars"></i> Credit Note Header
									</h3>
									
								</div>

						<div class="box-content">
							<form action="" class="form-horizontal" method="get"
								id="validation-form">

								<div class="form-group">
									<label class="col-sm-3 col-lg-2 control-label">From
										Date</label>
									<div class="col-sm-5 col-lg-3 controls">
										<input class="form-control date-picker" id="from_date" autocomplete="off"
											size="16" type="text" name="from_date" value="${fromDate}"
											required />
									</div>
									<label class="col-sm-3 col-lg-2 control-label">To Date</label>
									<div class="col-sm-5 col-lg-3 controls">
										<input class="form-control date-picker" id="to_date" size="16" autocomplete="off"
											type="text" value="${toDate}" name="to_date" required />
									</div>

								</div>

								<div class="form-group">
									<label class="col-sm-3 col-lg-2 control-label">Franchise</label>

									<div class="col-sm-5 col-lg-3 controls">
										<select data-placeholder="Choose Franchisee"
											 class="form-control chosen"  multiple="multiple" tabindex="6"
											id="selectFr" name="selectFr">
									
											<option value="-1"><c:out value="All"/></option>

											<c:forEach items="${unSelectedFrList}" var="fr"
												varStatus="count2">
												<option value="${fr.frId}"><c:out value="${fr.frName}"/></option>
											</c:forEach>

										</select>
									</div>

									<div
										class="col-sm-25 col-sm-offset-3 col-lg-30 col-lg-offset-0">
										<input type="button" value="Submit" onclick="getHeader()"
											class="btn btn-primary">

									</div>


								</div>

							</form>

							<form action="getCrnCheckedHeaders" class="form-horizontal"
								method="post" id="validation-form">

								<div class="box1">
									<div class="box-title">
										<h3>
											<i class="fa fa-table"></i> Crn List
										</h3>
										<div class="box-tool">
											<a data-action="collapse" href="#"><i
												class="fa fa-chevron-up"></i></a>
											<!--<a data-action="close" href="#"><i class="fa fa-times"></i></a>-->
										</div>
									</div>

									<div class="box-content">

										<div class="clearfix"></div>
										<div class="table-responsive" style="border: 0">
											<table width="100%"
												class="table table-advance table-responsive table-position"
												id="table1">
												<thead style="background-color:#f3b5db; ">
													<tr>
														<!-- <th></th> -->
														<th class="col-md-1"><!-- <input type="checkbox"
															onClick="selectBillNo(this)" /> -->Sr</th>
														<th class="col-md-1">Date</th>
														<th class="col-md-2">CRN Id</th>
														<th class="col-md-2">Inv. No</th>
														<th class="col-md-2">Franchise</th>
														<th class="col-md-2">Taxable Amt</th>
														<th class="col-md-2">Tax Amt</th>
														<th class="col-md-2">Amount</th>
														<th class="col-md-2">Action</th>

													</tr>

												</thead>
												<tbody>
											</table>
										</div>

										
	
									</div>
								</div>
							</form>
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

   <script type="text/javascript">
		function getHeader() {
		
			var fromDate = $("#from_date").val();
			var toDate = $("#to_date").val();
			var selectedFr = $("#selectFr").val();

			$.getJSON('${getHeaders}', {
				fromDate : fromDate,
				toDate : toDate,
				fr_id_list : JSON.stringify(selectedFr),
				ajax : 'true',

			},
			 function(data) {
				var len = data.length;
				$('#table1 td').remove();

				$.each(data,function(key, headers) {
				var tr = $('<tr></tr>');
				
				tr.append($('<td></td>').html(key+1));
/* 			 	tr.append($('<td><input type=checkbox name="select_to_agree" id="select_to_agree'+key+'"  value='+headers.crnId+'></td>'));
 */
			  	tr.append($('<td></td>').html(headers.crnDate));
			  	
			  	tr.append($('<td></td>').html(headers.crnNo));
				tr.append($('<td></td>').html(headers.exVarchar1));//inv no

			  	tr.append($('<td></td>').html(headers.frName));

			  	tr.append($('<td></td>').html((headers.crnTaxableAmt).toFixed()));
			  	
				tr.append($('<td></td>').html((headers.crnTotalTax).toFixed(2)));

			  	tr.append($('<td></td>').html((headers.crnGrandTotal).toFixed(2)));

				tr.append($('<td style="align:right;""><a href="#" class="action_btn" onclick="getCrnDetail('+headers.crnId+')"><abbr title="Detail"><i class="fa fa-list"></i></abbr></a></td>'));
			  
				$('#table1 tbody').append(tr);

			})

			});

		}
	</script>
<script>
$( "#date" ).datepicker({ 
    // Add this line

    stepMonths: 0,
});
</script>
</body>
</html>