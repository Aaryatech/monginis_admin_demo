<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.ats.adminpanel.commons.Constants" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Monginis</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->



<style type="text/css">
table {
	border-collapse: collapse;
	font-size: 14;
	width: 100%;
	page-break-inside: auto !important
}

p {
	color: black;
	font-family: arial;
	font-size: 60%;
	margin-top: 0;
	padding: 0;
}

h6 {
	color: black;
	font-family: arial;
	font-size: 80%;
}

th {
	background-color: #EA3291;
	color: white;
}
</style>
</head>
<body onload="myFunction()">
	<h4 style="text-align: center; font-weight: bold; font-size: 18px;">MONGINIS
		${Constants.CITY}</h4>
	<h6 style="text-align: center; font-weight: bold; font-size: 18px;">Itemwise
		Dispatch Report Pdf</h6>


	<div class="box-content">



		<div class="row">
			<div class="col-md-12 table-responsive">
				<div style="">

					<table class="table table-bordered" style="width: 100%"
						id="table_grid" border="1">

						<thead style="background-color: #f3b5db;">
							<tr>
								<th
									style="text-align: center; font-weight: bold; font-size: 18px;">Sr.No.</th>
								<th
									style="text-align: center; font-weight: bold; font-size: 18px;">Franchise
									Name</th>
								<c:set var="itemCount" value="2"></c:set>
								<c:forEach items="${Items}" var="Items">

									<th
										style="text-align: left; font-weight: bold; font-size: 18px;">${Items.itemName}</th>
									<c:set var="itemCount" value="${itemCount+1}"></c:set>

								</c:forEach>


							</tr>
						</thead>
						<tbody>

							<c:set var="sr" value="0"></c:set>
							<c:forEach items="${FrNameList}" var="FrNameList"
								varStatus="count">





								<tr>
									<td
										style="text-align: left; font-weight: bold; font-size: 20px;"><c:out
											value="${sr+1}" /> <c:set var="sr" value="${sr+1}"></c:set></td>
									<td
										style="text-align: left; font-weight: bold; font-size: 20px;"><c:out
											value="${FrNameList.frName}" /></td>
									<c:forEach items="${Items}" var="items">
										<c:set var="orQty" value="0"></c:set>

										<td
											style="text-align: right; font-weight: bold; font-size: 20px;"><c:forEach
												items="${dispatchReportList}" var="dispatchReportList">

												<c:choose>
													<c:when test="${dispatchReportList.frId==FrNameList.frId}">
														<c:choose>
															<c:when test="${dispatchReportList.itemId==items.id}">
																<c:set var="orQty"
																	value="${dispatchReportList.orderQty}"></c:set>
															</c:when>
														</c:choose>
													</c:when>
												</c:choose>

											</c:forEach> ${orQty}</td>
									</c:forEach>
								</tr>
							</c:forEach>


						</tbody>
					</table>
				</div>

			</div>
		</div>
	</div>

	<!-- END Main Content -->

</body>
</html>