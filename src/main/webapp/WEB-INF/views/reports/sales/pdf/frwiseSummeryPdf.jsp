<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Franchisee Summery Report PDF</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

<style type="text/css">
table {
	border-collapse: collapse;
	font-size: 10;
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




	<c:forEach items="${frList}" var="fr" varStatus="c1">
		<div align="center" style="font-size: 12px;">${FACTORYNAME}</div>
		<%-- <p align="center">${FACTORYADDRESS}</p> --%>
		<div align="center" style="font-size: 12px;">
			${fr.frName}&nbsp;&nbsp;&nbsp;&nbsp;From&nbsp;${fromDate} &nbsp;To
			&nbsp; ${toDate}</div>
		<c:set var="totalDrAMt" value="${0}" />
		<c:set var="sr" value="${0}" />
		<c:set var="totalCrAMt" value="${0}" />
		<table align="center" border="1" cellspacing="0" cellpadding="1"
			id="table_grid" class="table table-bordered" style="font-size: 10px;">
			<thead>
				<tr class="bgpink">

					<th width="7%">Sr.No.</th>
					<th width="10%">Date</th>
					<th width="10%">Type</th>
					<th width="15%">Document</th>
					<th>Order Ref</th>
					<th width="10%">Dr Amt</th>
					<th width="10%">Cr Amt</th>
					<th width="10%">Balance</th>

				</tr>
			</thead>
			<tbody>
				<%-- <tr>
					<td colspan='8' width="200"><c:out value="" /></td>

				</tr> --%>
				<c:forEach items="${salesRepFrList}" var="report" varStatus="count">

					<tr>

						<c:choose>
							<c:when test="${report.frId==fr.frId}">
								<c:set var="sr" value="${sr+1}" />
								<td><c:out value="${sr}" /></td>
								<td style="text-align: center;"><c:out
										value="${report.billDate}" /></td>
								<td style="text-align: center;"><c:out
										value="${report.type}" /></td>
								<td style="text-align: center;"><c:out
										value="${report.invoiceNo}" /></td>
								<td style="text-align: center;"><c:out
										value="${report.orderRef}" /></td>

								<c:choose>
									<c:when test="${report.type eq 'INV'}">

										<td align="right"><fmt:formatNumber type="number"
												maxFractionDigits="2" minFractionDigits="2"
												value="${report.grandTotal}" /></td>
										<td align="right"><fmt:formatNumber type="number"
												maxFractionDigits="2" minFractionDigits="2" value="${0}" /></td>
										<c:set var="totalDrAMt"
											value="${totalDrAMt + report.grandTotal }" />

									</c:when>

									<c:when test="${report.type eq 'RET' || report.type eq 'VER'}">
										<td align="right"><fmt:formatNumber type="number"
												maxFractionDigits="2" minFractionDigits="2" value="${0}" /></td>
										<td align="right"><fmt:formatNumber type="number"
												maxFractionDigits="2" minFractionDigits="2"
												value="${report.grandTotal}" /></td>


										<c:set var="totalCrAMt"
											value="${totalCrAMt + report.grandTotal }" />
									</c:when>
								</c:choose>
								<td align="right"><fmt:formatNumber type="number"
										maxFractionDigits="2" minFractionDigits="2"
										value="${totalDrAMt-totalCrAMt}" /></td>
							</c:when>
						</c:choose>


					</tr>

				</c:forEach>

				<tr>

					<td colspan='1'><b>Total</b></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>

					<td align="right"><b><fmt:formatNumber type="number"
								maxFractionDigits="2" minFractionDigits="2"
								value="${totalDrAMt}" /></b></td>

					<td align="right"><b><fmt:formatNumber type="number"
								maxFractionDigits="2" minFractionDigits="2"
								value="${totalCrAMt}" /></b></td>
					<td align="right"><b><fmt:formatNumber type="number"
								maxFractionDigits="2" minFractionDigits="2"
								value="${totalDrAMt-totalCrAMt}" /></b></td>
					<!--  <td><b>Total</b></td> -->
				</tr>
			</tbody>
		</table>
		<div style="page-break-after: always;"></div>


	</c:forEach>


	<!-- END Main Content 			    <tr style=" page-break-after:always;"><td></td></tr>
	-->

</body>
</html>