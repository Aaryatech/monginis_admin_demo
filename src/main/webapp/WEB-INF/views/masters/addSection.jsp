<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	 

	<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
	<body>
	
	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>
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
								<i class="fa fa-bars"></i>Section  
							</h3>
							<div class="box-tool">
								<a href="${pageContext.request.contextPath}/showSubCatList"></a> <a data-action="collapse" href="#"><i
									class="fa fa-chevron-up"></i></a>
							</div>
							
						</div>
						<div class="box-content">
							<form action="${pageContext.request.contextPath}/insertSection" class="form-horizontal"
								method="post" id="validation-form">

                        <input type="hidden" name="sectionId" id="sectionId" value="${editSection.sectionId}"/>
                        
                        <div class="form-group">
									<label class="col-sm-3 col-lg-2 control-label" for="item_name">Section Name</label>
									<div class="col-sm-9 col-lg-10 controls">
										<input type="text" name="sectionName" id="sectionName"
											placeholder="Section Name" class="form-control"
											data-rule-required="true" value="${editSection.sectionName}"/>
									</div>
								</div>
								
                              <div class="form-group">
									<label class="col-sm-3 col-lg-2 control-label">Select Menu</label>
									<div class="col-sm-9 col-lg-10 controls">
										<select data-placeholder="Select Menu"
											class="form-control chosen" name="menuIds"  
											id="menuIds" data-rule-required="true" multiple="multiple">
											 
											 <c:choose>
											 	<c:when test="${isEdit==1}">
											 		<c:forEach items="${editSection.menuList}" var="menuList"> 
														 
												 			 		<option value="${menuList.menuId}"  selected><c:out value="${menuList.menuTitle}"></c:out></option>
												 			 	 
													</c:forEach>
													
											 		<c:forEach items="${menuList}" var="menuList"> 
														<c:set var="find" value="0"></c:set>
														 	<c:forEach items="${sectionList}" var="sectionList" >
														 		<c:forEach items="${sectionList.menuList}" var="menuListBySection" >
														 			<c:choose>
														 			 	<c:when test="${menuListBySection.menuId==menuList.menuId}">
														 			 		<c:set var="find" value="1"></c:set>
														 			 	</c:when>	
														 			</c:choose>
														 		</c:forEach> 
														 	</c:forEach>
														 	
														 	<c:choose>
												 			 	<c:when test="${find==0}">
												 			 		<option value="${menuList.menuId}"  ><c:out value="${menuList.menuTitle}"></c:out></option>
												 			 	</c:when>
												 			</c:choose>  
													</c:forEach>
											 	
											 	</c:when>
											 	<c:otherwise>
											 		<c:forEach items="${menuList}" var="menuList"> 
														<c:set var="find" value="0"></c:set>
														 	<c:forEach items="${sectionList}" var="sectionList" >
														 		<c:forEach items="${sectionList.menuList}" var="menuListBySection" >
														 			<c:choose>
														 			 	<c:when test="${menuListBySection.menuId==menuList.menuId}">
														 			 		<c:set var="find" value="1"></c:set>
														 			 	</c:when>	
														 			</c:choose>
														 		</c:forEach> 
														 	</c:forEach>
														 	
														 	<c:choose>
												 			 	<c:when test="${find==0}">
												 			 		<option value="${menuList.menuId}"  ><c:out value="${menuList.menuTitle}"></c:out></option>
												 			 	</c:when>
												 			</c:choose>  
													</c:forEach>
											 	
											 	</c:otherwise>
											 </c:choose>
											

										</select>
									</div>
								</div>
						
								
                              
								<div class="form-group">
									<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2">
										<input type="submit" class="btn btn-primary" value="Submit" onclick="return validate()">
									</div>
								</div>
							</form>
						</div>
						
						
					</div>
				</div>
			</div>
				<div class="box">
			 	<div class="box-title">
					<h3>
						<i class="fa fa-table"></i> Section List
					</h3>
					<div class="box-tool">
						<a data-action="collapse" href="#"><i class="fa fa-chevron-up"></i></a>
						 
					</div>
				</div>
               	<div class="box-content">
 
                 <jsp:include page="/WEB-INF/views/include/tableSearch.jsp"></jsp:include>

							<div class="clearfix"></div>
							
								<div id="table-scroll" class="table-scroll">
							 
									<div id="faux-table" class="faux-table" aria="hidden">
									<table id="table2" class="table table-advance" >
											<thead>
												<tr class="bgpink">
											      <th width="5%">sr</th> 
									        <th class="col-md-1" align="left">Section Name </th>
									        <th  align="left"> Menu Name </th>
									        <th class="col-md-1" align="left">Action</th>
												</tr>
												</thead>
												</table>
									
									</div>
									<div class="table-wrap">
									
										<table id="table1" class="table table-advance" >
											<thead>
												<tr class="bgpink">
											<th width="5%">sr</th> 
									        <th class="col-md-1" align="left">Section Name </th>
									        <th  align="left"> Menu Name </th>
									        <th class="col-md-1" align="left">Action</th>
												</tr>
												</thead>
												<tbody>
						  <c:set var="cnt" value="0"></c:set>
					           <c:forEach items="${sectionList}" var="sectionList" varStatus="count">
				            
									<tr>
										<td><c:out value="${cnt+1}" /><c:set var="cnt" value="${cnt+1}"></c:set></td>
										<td align="left"><c:out value="${sectionList.sectionName}" /></td>
										
										<td align="left">
											 <c:forEach items="${sectionList.menuList}" var="menuList" >
												<c:out value="${menuList.menuTitle}" />,
											</c:forEach> 
										</td>
										
										<td align="left"><a
											href="${pageContext.request.contextPath}/editSection/${sectionList.sectionId}"><span
												class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;

											<a href="${pageContext.request.contextPath}/deleteSection/${sectionList.sectionId}"
											onClick="return confirm('Are you sure want to delete this record');"><span
												class="glyphicon glyphicon-remove"></span></a>
										</td>
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
			<p>2018 © MONGINIS.</p>
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

</body>

</html>