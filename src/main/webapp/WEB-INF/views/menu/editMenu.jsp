<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<!---------------Script For Translate Special Instructions------->
<script type="text/javascript">
	// Load the Google Transliterate API
	google.load("elements", "1", {
		packages : "transliteration"
	});

	function onLoad() {
		var options = {
			sourceLanguage : google.elements.transliteration.LanguageCode.ENGLISH,
			destinationLanguage : [ google.elements.transliteration.LanguageCode.MARATHI ],
			shortcutKey : 'ctrl+g',
			transliterationEnabled : true
		};

		// Create an instance on TransliterationControl with the required
		// options.
		var control = new google.elements.transliteration.TransliterationControl(
				options);

		// Enable transliteration in the textbox with id
		// 'transliterateTextarea'.
		control.makeTransliteratable([ 'transliterateTextarea' ]);
		control.makeTransliteratable([ 'transliterateTextarea1' ]);
	}
	google.setOnLoadCallback(onLoad);
</script>
<!--------------------------------END------------------------------------>
<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
<body>
	<jsp:include page="/WEB-INF/views/include/logout.jsp"></jsp:include>

	<%@ page import="java.util.Calendar"%>
	<%@ page import="java.util.Date"%>
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
						<i class="fa fa-file-o"></i> Menu
					</h1>

				</div>
			</div>
			<!-- END Page Title -->




			<!-- BEGIN Main Content -->
			<div class="row">
				<div class="col-md-12">
					<div class="box">
						<div class="box-title">
							<h3>
								<i class="fa fa-bars"></i> Add Menu
							</h3>
							<div class="box-tool">
								<a href="${pageContext.request.contextPath}/showMenus">Back
									to List</a> <a data-action="collapse" href="#"><i
									class="fa fa-chevron-up"></i></a>
							</div>

						</div>
						<div class="box-content">
							<form action="updateMenuProcess" class="form-horizontal"
								id="validation-form" method="post" enctype="multipart/form-data">




								<div class="form-group">
									<div class="col2">
										<label class="col-sm-3 col-lg-2 control-label">Select
											Category </label>
										<div class="col-sm-9 col-lg-3 controls">
											<select name="catId" id="catId" class="form-control chosen"
												placeholder="Select Category  " data-rule-required="true">
												<option value="">Select Category</option>
												<c:forEach items="${catList}" var="catList"
													varStatus="count">
													<c:choose>
														<c:when test="${catList.catId==menu.catId}">
															<option value="${catList.catId}" selected><c:out
																	value="${catList.catName}" /></option>
														</c:when>
														<c:otherwise>
															<option value="${catList.catId}"><c:out
																	value="${catList.catName}" /></option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</div>
									</div>

								</div>

								<input type="hidden" value="${menu.menuId}" name="menuId">


								<div class="form-group">
									<label class="col-sm-3 col-lg-2 control-label">Menu
										Title </label>
									<div class="col-sm-9 col-lg-10 controls">
										<input type="text" name="menuTitle" id="menuTitle"
											value="${menu.menuTitle}" placeholder="Menu title"
											class="form-control" data-rule-required="true" />
									</div>
								</div>



								<div class="form-group">
									<label class="col-sm-3 col-lg-2 control-label">Photo1</label>
									<div class="col-sm-6 col-lg-3 controls">
										<div class="fileupload fileupload-new"
											data-provides="fileupload">
											<div class="fileupload-new img-thumbnail"
												style="width: 200px; height: 150px;">
												<img src="${url}${menu.menuImage}"
													onerror="this.src='${pageContext.request.contextPath}/resources/img/No_Image_Available.jpg';" />
											</div>
											<div
												class="fileupload-preview fileupload-exists img-thumbnail"
												style="max-width: 200px; max-height: 150px; line-height: 20px;"></div>
											<div>
												<span class="btn btn-default btn-file"><span
													class="fileupload-new">Select image</span> <span
													class="fileupload-exists">Change</span> <input type="file"
													class="file-input" name="photo1" id="photo1"
													value="${menu.menuImage}" /></span> <a href="#"
													class="btn btn-default fileupload-exists"
													data-dismiss="fileupload">Remove</a>
											</div>
										</div>

									</div>

									<label class="col-sm-3 col-lg-2 control-label">Photo2</label>
									<div class="col-sm-6 col-lg-3 controls">
										<div class="fileupload fileupload-new"
											data-provides="fileupload">
											<div class="fileupload-new img-thumbnail"
												style="width: 200px; height: 150px;">
												<img src="${url}${album.selectedMenuImage}"
													onerror="this.src='${pageContext.request.contextPath}/resources/img/No_Image_Available.jpg';" />
											</div>
											<div
												class="fileupload-preview fileupload-exists img-thumbnail"
												style="max-width: 200px; max-height: 150px; line-height: 20px;"></div>
											<div>
												<span class="btn btn-default btn-file"><span
													class="fileupload-new">Select image</span> <span
													class="fileupload-exists">Change</span> <input type="file"
													class="file-input" name="photo2" id="photo2"
													value="${menu.selectedMenuImage}" /></span> <a href="#"
													class="btn btn-default fileupload-exists"
													data-dismiss="fileupload">Remove</a>
											</div>
										</div>

									</div>
								</div>


								<div class="form-group">
									<label class="col-sm-3 col-lg-2 control-label">Description
										Details</label>
									<div class="col-sm-9 col-lg-10 controls">
										<textarea class="form-control" rows="3" name="menuDesc"
											id="transliterateTextarea1" data-rule-required="true">${menu.menuDesc}</textarea>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-3 col-lg-2 control-label">Is Same
										Day Applicable</label>
									<div class="col-sm-9 col-lg-10 controls">
										<select class="form-control input-sm"
											name="isSameDayAppicable" id="isSameDayAppicable">

											<c:choose>
												<c:when test="${menu.isSameDayApplicable==0}">
													<option value="0" Selected>Regular</option>
													<option value="1">Same Day regular</option>
													<option value="2">Regular with limit</option>
													<option value="3">Regular cake As SP Order</option>
													<option value="4">Delivery And Production Date</option>
												</c:when>
												<c:when test="${menu.isSameDayApplicable==1}">
													<option value="0">Regular</option>
													<option value="1" Selected>Same Day regular</option>
													<option value="2">Regular with limit</option>
													<option value="3">Regular cake As SP Order</option>
													<option value="4">Delivery And Production Date</option>
												</c:when>

												<c:when test="${menu.isSameDayApplicable==2}">
													<option value="0">Regular</option>
													<option value="1">Same Day regular</option>
													<option value="2" Selected>Regular with limit</option>
													<option value="3">Regular cake As SP Order</option>
													<option value="4">Delivery And Production Date</option>
												</c:when>


												<c:when test="${menu.isSameDayApplicable==3}">
													<option value="0">Regular</option>
													<option value="1">Same Day regular</option>
													<option value="2">Regular with limit</option>
													<option value="3" Selected>Regular cake As SP
														Order</option>
													<option value="4">Delivery And Production Date</option>
												</c:when>



												<c:when test="${menu.isSameDayApplicable==4}">
													<option value="0">Regular</option>
													<option value="1">Same Day regular</option>
													<option value="2">Regular with limit</option>
													<option value="3">Regular cake As SP Order</option>
													<option value="4" Selected>Delivery And Production
														Date</option>
												</c:when>
												
												<c:otherwise><option value="0">Regular</option>
													<option value="1">Same Day regular</option>
													<option value="2">Regular with limit</option>
													<option value="3">Regular cake As SP Order</option>
													<option value="4"  >Delivery And Production
														Date</option></c:otherwise>
											</c:choose>
										</select>
									</div>
								</div>


								<div class="form-group">
									<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2">
										<button type="submit" class="btn btn-primary">
											<i class="fa fa-check"></i> Save
										</button>
										<!--<button type="button" class="btn">Cancel</button>-->
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>




			<!-- END Main Content -->



			<footer>
				<p>2017 © MONGINIS.</p>
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
