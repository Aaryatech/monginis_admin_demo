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
<title>Sales Report Billwise PDF</title>
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
	<h3 align="center">${FACTORYNAME}</h3>
	<p align="center">${FACTORYADDRESS}</p>
	<div align="center">
		<h5>Sales Report (Franchisee Sub Category - Item Wise) &nbsp;&nbsp;&nbsp;&nbsp; From
			&nbsp; ${fromDate} &nbsp;To &nbsp; ${toDate}</h5>
	</div>
	<table align="center" border="1" cellspacing="0" cellpadding="1"
		id="table_grid" class="table table-bordered">
		<thead>
			<tr class="bgpink">
				<th>Sr.No.</th>
				<th>Item Name</th>
				<th>Sold Qty</th>
				<th>Sold Amt</th>
				<th>Var Qty</th>
				<th>Var Amt</th>
				<th>Ret Qty</th>
				<th>Ret Amt</th>
				<th>Net Qty</th>
				<th>Net Amt</th>
				<th>Ret Amt</th>
			</tr>
		</thead>
		<tbody>

			<c:set var="grandTotalSoldQty" value="${0}" />
			<c:set var="grandTotalSoldAmt" value="${0}" />
			<c:set var="grandTotalVarQty" value="${0}" />
			<c:set var="grandTotalVarAmt" value="${0}" />
			<c:set var="grandTotalRetQty" value="${0}" />
			<c:set var="grandTotalRetAmt" value="${0}" />
			<c:set var="grandTotalNetQty" value="${0}" />
			<c:set var="grandTotalNetAmt" value="${0}" />
			<c:set var="grandRetAmtPer" value="${0}" />

			<c:forEach items="${frList}" var="fr" varStatus="c1">
				<c:set var="totalSoldQty" value="${0}" />
				<c:set var="totalSoldAmt" value="${0}" />
				<c:set var="totalVarQty" value="${0}" />
				<c:set var="totalVarAmt" value="${0}" />
				<c:set var="totalRetQty" value="${0}" />
				<c:set var="totalRetAmt" value="${0}" />
				<c:set var="totalNetQty" value="${0}" />
				<c:set var="totalNetAmt" value="${0}" />
				<c:set var="retAmtPer" value="${0}" />
				<c:set var="sr" value="${0}" />
				<tr>
					<td width="0"></td>
					<td width="200"><c:out value="${fr.frName}" /></td>

				</tr>

				<c:forEach items="${subCatList}" var="subCat" varStatus="c1">

					<c:set var="SoldQty" value="${0}" />
					<c:set var="SoldAmt" value="${0}" />
					<c:set var="VarQty" value="${0}" />
					<c:set var="VarAmt" value="${0}" />
					<c:set var="RetQty" value="${0}" />
					<c:set var="RetAmt" value="${0}" />
					<c:set var="NetQty" value="${0}" />
					<c:set var="NetAmt" value="${0}" />
					<c:set var="AmtPer" value="${0}" />

					<tr>
						<td width="0"></td>
						<td width="200"><c:out value="${subCat.subCatName}" /></td>

					</tr>

					<c:forEach items="${subCatFrReportList}" var="report"
						varStatus="count">
						<tr>

							<c:choose>
								<c:when test="${report.subCatId==subCat.subCatId}">




									<c:choose>
										<c:when test="${report.frId==fr.frId}">
											<c:set var="sr" value="${sr+1}" />
											<td width="20"><c:out value="${sr}" /></td>

											<td width="100"><c:out value="${report.itemName}" /></td>
											<td width="10" align="right"><fmt:formatNumber
													type="number" maxFractionDigits="2" minFractionDigits="2"
													value="${report.soldQty}" /></td>

											<td width="10" align="right"><fmt:formatNumber
													type="number" maxFractionDigits="2" minFractionDigits="2"
													value="${report.soldAmt}" /></td>


											<td width="10" align="right"><fmt:formatNumber
													type="number" maxFractionDigits="2" minFractionDigits="2"
													value="${report.varQty}" /></td>


											<td width="10" align="right"><fmt:formatNumber
													type="number" maxFractionDigits="2" minFractionDigits="2"
													value="${report.varAmt}" /></td>


											<td width="10" align="right"><fmt:formatNumber
													type="number" maxFractionDigits="2" minFractionDigits="2"
													value="${report.retQty}" /></td>


											<td width="10" align="right"><fmt:formatNumber
													type="number" maxFractionDigits="2" minFractionDigits="2"
													value="${report.retAmt}" /></td>


											<td width="10" align="right"><fmt:formatNumber
													type="number" maxFractionDigits="2" minFractionDigits="2"
													value="${report.netQty}" /></td>


											<td width="10" align="right"><fmt:formatNumber
													type="number" maxFractionDigits="2" minFractionDigits="2"
													value="${report.netAmt}" /></td>

											<td width="10" align="right"><fmt:formatNumber
													type="number" maxFractionDigits="2" minFractionDigits="2"
													value="${report.retAmtPer}" /></td>

											<c:set var="totalSoldQty"
												value="${totalSoldQty+(report.soldQty)}" />
											<c:set var="totalSoldAmt"
												value="${totalSoldAmt+(report.soldAmt)}" />
											<c:set var="totalVarQty"
												value="${totalVarQty+(report.varQty)}" />
											<c:set var="totalVarAmt"
												value="${totalVarAmt+(report.varAmt)}" />
											<c:set var="totalRetQty"
												value="${totalRetQty+(report.retQty)}" />
											<c:set var="totalRetAmt"
												value="${totalRetAmt+(report.retAmt)}" />
											<c:set var="totalNetQty"
												value="${totalNetQty+(report.netQty)}" />
											<c:set var="totalNetAmt"
												value="${totalNetAmt+(report.netAmt)}" />
											<c:set var="retAmtPer"
												value="${retAmtPer+(report.retAmtPer)}" />



											<c:set var="SoldQty" value="${SoldQty+(report.soldQty)}" />
											<c:set var="SoldAmt" value="${SoldAmt+(report.soldAmt)}" />
											<c:set var="VarQty" value="${VarQty+(report.varQty)}" />
											<c:set var="VarAmt" value="${VarAmt+(report.varAmt)}" />
											<c:set var="RetQty" value="${RetQty+(report.retQty)}" />
											<c:set var="RetAmt" value="${RetAmt+(report.retAmt)}" />
											<c:set var="NetQty" value="${NetQty+(report.netQty)}" />
											<c:set var="NetAmt" value="${NetAmt+(report.netAmt)}" />
											<c:set var="AmtPer" value="${AmtPer+(report.retAmtPer)}" />


										</c:when>
									</c:choose>
								</c:when>
							</c:choose>
						</tr>

					</c:forEach>

					<tr>

						<td colspan='2'><b>Total</b></td>


						<td width="10" align="right"><b><fmt:formatNumber
									type="number" maxFractionDigits="2" minFractionDigits="2"
									value="${SoldQty}" /></b></td>

						<td width="10" align="right"><b><fmt:formatNumber
									type="number" maxFractionDigits="2" minFractionDigits="2"
									value="${SoldAmt}" /></b></td>
						<td width="10" align="right"><b><fmt:formatNumber
									type="number" maxFractionDigits="2" minFractionDigits="2"
									value="${VarQty}" /></b></td>

						<td width="10" align="right"><b><fmt:formatNumber
									type="number" maxFractionDigits="2" minFractionDigits="2"
									value="${VarAmt}" /></b></td>


						<td width="10" align="right"><b><fmt:formatNumber
									type="number" maxFractionDigits="2" minFractionDigits="2"
									value="${RetQty}" /></b></td>


						<td width="10" align="right"><b><fmt:formatNumber
									type="number" maxFractionDigits="2" minFractionDigits="2"
									value="${RetAmt}" /></b></td>


						<td width="10" align="right"><b><fmt:formatNumber
									type="number" maxFractionDigits="2" minFractionDigits="2"
									value="${NetQty}" /></b></td>



						<td width="10" align="right"><b><fmt:formatNumber
									type="number" maxFractionDigits="2" minFractionDigits="2"
									value="${NetAmt}" /></b></td>

						<td width="10" align="right"><b><fmt:formatNumber
									type="number" maxFractionDigits="2" minFractionDigits="2"
									value="${AmtPer}" /></b></td>
					</tr>
				</c:forEach>



				<c:set var="grandTotalSoldQty"
					value="${grandTotalSoldQty+totalSoldQty}" />
				<c:set var="grandTotalSoldAmt"
					value="${grandTotalSoldAmt+totalSoldAmt}" />
				<c:set var="grandTotalVarQty"
					value="${grandTotalVarQty+totalVarQty}" />
				<c:set var="grandTotalVarAmt"
					value="${grandTotalVarAmt+totalVarAmt}" />
				<c:set var="grandTotalRetQty"
					value="${grandTotalRetQty+totalRetQty}" />
				<c:set var="grandTotalRetAmt"
					value="${grandTotalRetAmt+totalRetAmt}" />
				<c:set var="grandTotalNetQty"
					value="${grandTotalNetQty+totalNetQty}" />
				<c:set var="grandTotalNetAmt"
					value="${grandTotalNetAmt+totalNetAmt}" />
				<c:set var="grandRetAmtPer" value="${grandRetAmtPer+retAmtPer}" />


				<tr>

					<td colspan='2'><b>Franchisee Total</b></td>


					<td width="10" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${totalSoldQty}" /></b></td>

					<td width="10" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${totalSoldAmt}" /></b></td>
					<td width="10" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${totalVarQty}" /></b></td>

					<td width="10" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${totalVarAmt}" /></b></td>


					<td width="10" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${totalRetQty}" /></b></td>


					<td width="10" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${totalRetAmt}" /></b></td>


					<td width="10" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${totalNetQty}" /></b></td>



					<td width="10" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${totalNetAmt}" /></b></td>

					<td width="10" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${retAmtPer}" /></b></td>
				</tr>
			</c:forEach>




			<tr>

				<td colspan='2'><b>GRAND TOTAL</b></td>


				<td width="10" align="right"><b><fmt:formatNumber
							type="number" maxFractionDigits="2" minFractionDigits="2"
							value="${grandTotalSoldQty}" /></b></td>

				<td width="10" align="right"><b><fmt:formatNumber
							type="number" maxFractionDigits="2" minFractionDigits="2"
							value="${grandTotalSoldAmt}" /></b></td>
				<td width="10" align="right"><b><fmt:formatNumber
							type="number" maxFractionDigits="2" minFractionDigits="2"
							value="${grandTotalVarQty}" /></b></td>

				<td width="10" align="right"><b><fmt:formatNumber
							type="number" maxFractionDigits="2" minFractionDigits="2"
							value="${grandTotalVarAmt}" /></b></td>


				<td width="10" align="right"><b><fmt:formatNumber
							type="number" maxFractionDigits="2" minFractionDigits="2"
							value="${grandTotalRetQty}" /></b></td>


				<td width="10" align="right"><b><fmt:formatNumber
							type="number" maxFractionDigits="2" minFractionDigits="2"
							value="${grandTotalRetAmt}" /></b></td>


				<td width="10" align="right"><b><fmt:formatNumber
							type="number" maxFractionDigits="2" minFractionDigits="2"
							value="${grandTotalNetQty}" /></b></td>



				<td width="10" align="right"><b><fmt:formatNumber
							type="number" maxFractionDigits="2" minFractionDigits="2"
							value="${grandTotalNetAmt}" /></b></td>

				<td width="10" align="right"><b><fmt:formatNumber
							type="number" maxFractionDigits="2" minFractionDigits="2"
							value="${grandRetAmtPer}" /></b></td>
			</tr>

		</tbody>
	</table>


	<!-- END Main Content -->

</body>
</html>