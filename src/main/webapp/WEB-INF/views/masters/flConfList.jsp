<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
 #overlay {
    position: fixed;
    display: none;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(165, 110, 169, 0.5);
    z-index: 2;
    cursor: pointer;
}
#text {
   position: absolute;
    top: 50%;
    left: 50%;
    font-size: 25px;
    color: white;
    transform: translate(-50%,-50%);
    -ms-transform: translate(-50%,-50%);
}
 
 </style>
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>

	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
	    <c:url var="updateFlavourConf" value="/updateFlavourConf" />
		 
<div id="overlay" >  <div id="text">Updating ,Please Wait...

</div></div>
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
			<!-- <div class="page-title">
				<div>
					<h1>
						<i class="fa fa-file-o"></i>Sub Category
					</h1>

				</div>
			</div> -->
			<!-- END Page Title -->

			<!-- BEGIN Main Content -->
			<div class="row">
				<div class="col-md-12">
					<div class="box">
						<div class="box-title">
							<h3>
								<i class="fa fa-bars"></i>Flavour Conf List
							</h3>
							<div class="box-tool">
								<a href="${pageContext.request.contextPath}/showSubCatList"></a>
								<a data-action="collapse" href="#"><i
									class="fa fa-chevron-up"></i></a>
							</div>

						</div>


					</div>
				</div>
			</div>
			<div class="box">
				
				<div class="box-content">

				 	<jsp:include page="/WEB-INF/views/include/tableSearch.jsp"></jsp:include> 

					<div class="clearfix"></div>

					<div id="table-scroll" class="table-scroll">

					
						<div class="table-wrap">

							<table id="table1" class="table table-advance">
								<thead>
									<tr class="bgpink">
										<th class="col-md-1">Sr.No.</th>
										<th class="col-md-4">Sp Name</th>
										<th class="col-md-3">Flavour Name</th>
										<th class="col-md-3">Mrp</th>
										<th class="col-md-3">Rate</th>
										
										 <th class="col-md-3">Action</th> 
									</tr>
								</thead>
								<tbody>

									<c:forEach items="${flList}" var="flList" varStatus="count">




										<tr>
											<td style="text-align: center">${count.index+1}</td>
											<td align="left">
						
								<c:forEach items="${specialCakeList}" var="sp"
									varStatus="count">
									<c:choose>
									<c:when test="${flList.spId==sp.spId}">
									<c:out value="${sp.spName}" />
									</c:when>
									<c:otherwise>
									</c:otherwise>
									</c:choose>
								</c:forEach> 
								
											
											</td>
											<td align="left"  class="col-md-1">
							
							 <c:forEach items="${flavoursList}" var="fl"
									varStatus="count">
								<c:choose>
								<c:when test="${flList.spfId==fl.spfId}">
								<c:out value="${fl.spfName}" />
								</c:when>
								<c:otherwise>
								</c:otherwise>
								</c:choose>
									
							</c:forEach> 
										</td>

										<td align="left" class="col-md-1"><input type="text" style="height: 34px;  padding: 6px 12px;  font-size: 14px;line-height: 1.428571429;  color: #555;  vertical-align: middle;background-color: #fff;    border: 1px solid #ccc;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
    -webkit-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;" oninput='return calcRate(this.value,${flList.flavId})' onchange="updateFlavourConf(${flList.flavId})"  name="mrp${flList.flavId}" id="mrp${flList.flavId}" value="${flList.mrp}" /></td>
											<%-- <td align="left"><c:out value="-" /></td> --%>
<td align="left" class="col-md-1"><input type="text" style="height: 34px;  padding: 6px 12px;  font-size: 14px;line-height: 1.428571429;  color: #555;  vertical-align: middle;background-color: #fff;    border: 1px solid #ccc;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
    -webkit-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;" name="rate${flList.flavId}" id="rate${flList.flavId}" onchange="updateFlavourConf(${flList.flavId})"   value="${flList.rate}" /></td>
	<td> <a href="${pageContext.request.contextPath}/deleteFlavourConf/${flList.flavId}"
			onClick="return confirm('Are you sure want to delete this record');"><span
			class="glyphicon glyphicon-remove"></span></a></td>

										</tr>
									</c:forEach>
								</tbody>
							</table>
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
  <script type="text/javascript">
       function updateFlavourConf(flavId) {
    		var rate=document.getElementById("rate"+flavId).value;
    		var mrp=document.getElementById("mrp"+flavId).value;
    	   on();
				$.getJSON('${updateFlavourConf}', {
					flavId:flavId,
					rate:rate,
					mrp:mrp,
					ajax : 'true'
				}, function(data) {
					off();
					if(data.error==false)
						{
						alert("Value Updated Successfully.");
						}else
							{
							alert("Value Not Updated.");
							}
					
				});
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
<script>
		function myFunction() {
			var input, filter, table, tr, td, td1, i;
			input = document.getElementById("myInput");
			filter = input.value.toUpperCase();
			table = document.getElementById("table1");
			tr = table.getElementsByTagName("tr");
			for (i = 0; i < tr.length; i++) {
				td = tr[i].getElementsByTagName("td")[1];
				td1 = tr[i].getElementsByTagName("td")[2];

				if (td) {
					if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
						tr[i].style.display = "";
					} else if (td1.innerHTML.toUpperCase().indexOf(filter) > -1) {
						tr[i].style.display = "";
					} else {
						tr[i].style.display = "none";
					}
				}
			}
		}
	</script>
  <script type="text/javascript">
     function calcRate(mrp,spfId)
     {
    	 var rate=mrp-(mrp*21/100);
    	 document.getElementById("rate"+spfId).value=Math.round(rate);
    	 
     }
     </script>
     <script>
function on() {
    document.getElementById("overlay").style.display = "block";
}

function off() {
    document.getElementById("overlay").style.display = "none";
}
</script>
</body>

</html>