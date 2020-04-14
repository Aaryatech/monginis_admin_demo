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
<title>Item Group > Item > Shop wise Summary Report PDF</title>
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
		<h5>Sales Report (Franchisee Wise Item Sale)
			&nbsp;&nbsp;&nbsp;&nbsp; From &nbsp; ${fromDate} &nbsp;To &nbsp;
			${toDate}</h5>
	</div>
	<table align="center" border="1" cellspacing="0" cellpadding="1"
		id="table_grid" class="table table-bordered">
		<thead>
			<tr class="bgpink">

										<th> </th>
										<th>Sold Qty</th>
										<th>Sold Amt</th>
										<th>Var Qty</th>
										<th>Var Amt</th>
										<th>Ret Qty</th>
										<th>Ret Amt</th>
										<th>Net Qty</th>
										<th>Net Amt</th>
										<th>Ret Amt %</th>

			</tr>
		</thead>
		<tbody>
		
		<c:forEach items="${reportList}" var="sc" varStatus="c1">
		
		<tr>
			<td width="200"><c:out value="${sc.subCatName}" /></td>
		</tr>
		
		
		<c:set var="scTotalSoldQty" value="${0}" />
		<c:set var="scTotalSoldAmt" value="${0}" />
		<c:set var="scTotalVarQty" value="${0}" />
		<c:set var="scTotalVarAmt" value="${0}" />
		<c:set var="scTotalRetQty" value="${0}" />
		<c:set var="scTotalRetAmt" value="${0}" />
		<c:set var="scTotalNetQty" value="${0}" />
		<c:set var="scTotalNetAmt" value="${0}" />
		<c:set var="scTotalRetAmtPer" value="${0}" />
		
		
		
		<c:forEach items="${sc.itemList}" var="item" varStatus="count">
		
		<c:set var="itemTotalSoldQty" value="${0}" />
		<c:set var="itemTotalSoldAmt" value="${0}" />
		<c:set var="itemTotalVarQty" value="${0}" />
		<c:set var="itemTotalVarAmt" value="${0}" />
		<c:set var="itemTotalRetQty" value="${0}" />
		<c:set var="itemTotalRetAmt" value="${0}" />
		<c:set var="itemTotalNetQty" value="${0}" />
		<c:set var="itemTotalNetAmt" value="${0}" />
		<c:set var="itemTotalRetAmtPer" value="${0}" />
		
		<tr>
			<td width="0"><c:out value="${item.itemName}" /></td>		
		</tr>		
		
		<c:forEach items="${item.frList}" var="fr" varStatus="count">
		
		  <c:if test = "${report_type ==1}"> 

		<tr>
			<td width="0"><c:out value="${fr.frName}" /></td>
			<td width="0" align="right"><c:out value="${fr.frSoldQty}" /></td>
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${fr.frSoldAmt}" /></td>
			<td width="0" align="right"><c:out value="${fr.frVarQty}" /></td>
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${fr.frVarAmt}" /></td>
			<td width="0" align="right"><c:out value="${fr.frRetQty}" /></td>
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${fr.frRetAmt}" /></td>
			
			<c:set var="netQty" value="${fr.frSoldQty - (fr.frVarQty+fr.frRetQty)}" />
			
			<td width="0" align="right"><c:out value="${netQty}" /></td>
			
			<c:set var="netAmt" value="${fr.frSoldAmt - (fr.frVarAmt+fr.frRetAmt)}" />
			
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${netAmt}" /></td>
			
			<c:set var="retAmtPer" value="${((fr.frVarAmt+fr.frRetAmt)*100)/fr.frSoldAmt}" />
			
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${retAmtPer}" />%</td>
					
		</tr>	
			
		 </c:if> 
		 
		  <c:if test = "${report_type ==2}"> 

		<tr>
			<td width="0"><c:out value="${fr.frName}" /></td>
			<td width="0" align="right"><c:out value="${fr.frSoldQty}" /></td>
			<td width="0" align="right"><c:out value="" /></td>
			<td width="0" align="right"><c:out value="${fr.frVarQty}" /></td>
			<td width="0" align="right"><c:out value="" /></td>
			<td width="0" align="right"><c:out value="${fr.frRetQty}" /></td>
			<td width="0" align="right"><c:out value="" /></td>
			
			<c:set var="netQty" value="${fr.frSoldQty - (fr.frVarQty+fr.frRetQty)}" />
			
			<td width="0" align="right"><c:out value="${netQty}" /></td>
			
			<c:set var="netAmt" value="${fr.frSoldAmt - (fr.frVarAmt+fr.frRetAmt)}" />
			
			<td width="0" align="right"><c:out value="" /></td>
			
			<c:set var="retAmtPer" value="${((fr.frVarAmt+fr.frRetAmt)*100)/fr.frSoldAmt}" />
			
			<td width="0" align="right"><c:out value="" /></td>
					
		</tr>	
			
		 </c:if> 
		 
		 
		 
		 <c:if test = "${report_type ==3}"> 

		<tr>
			<td width="0"><c:out value="${fr.frName}" /></td>
			<td width="0" align="right"><c:out value="" /></td>
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${fr.frSoldAmt}" /></td>
			<td width="0" align="right"><c:out value="" /></td>
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${fr.frVarAmt}" /></td>
			<td width="0" align="right"><c:out value="" /></td>
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${fr.frRetAmt}" /></td>
			
			<c:set var="netQty" value="${fr.frSoldQty - (fr.frVarQty+fr.frRetQty)}" />
			
			<td width="0" align="right"><c:out value="" /></td>
			
			<c:set var="netAmt" value="${fr.frSoldAmt - (fr.frVarAmt+fr.frRetAmt)}" />
			
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${netAmt}" /></td>
			
			<c:set var="retAmtPer" value="${((fr.frVarAmt+fr.frRetAmt)*100)/fr.frSoldAmt}" />
			
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${retAmtPer}" />%</td>
					
		</tr>	
			
		 </c:if> 
		 
		 <c:if test = "${report_type ==4}"> 

		<tr>
			<td width="0"><c:out value="${fr.frName}" /></td>
			<td width="0" align="right"><c:out value="${fr.frSoldQty}" /></td>
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${fr.frTaxableAmt}" /></td>
			<td width="0" align="right"><c:out value="${fr.frVarQty}" /></td>
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${fr.frVarTaxableAmt}" /></td>
			<td width="0" align="right"><c:out value="${fr.frRetQty}" /></td>
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${fr.frRetTaxableAmt}" /></td>
			
			<c:set var="netQty" value="${fr.frSoldQty - (fr.frVarQty+fr.frRetQty)}" />
			
			<td width="0" align="right"><c:out value="${netQty}" /></td>
			
			<c:set var="netAmt" value="${fr.frTaxableAmt - (fr.frVarTaxableAmt+fr.frRetTaxableAmt)}" />
			
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${netAmt}" /></td>
			
			<c:set var="retAmtPer" value="${((fr.frVarTaxableAmt+fr.frRetTaxableAmt)*100)/fr.frTaxableAmt}" />
			
			<td width="0" align="right"><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${retAmtPer}" />%</td>
					
		</tr>	
			
		 </c:if> 
	
	
		<c:choose>
			<c:when test="${report_type ==4}">
			
			<c:set var="itemTotalSoldQty"	value="${itemTotalSoldQty + fr.frSoldQty }" />
		<c:set var="itemTotalSoldAmt" value="${itemTotalSoldAmt + fr.frTaxableAmt}" />
		<c:set var="itemTotalVarQty" value="${itemTotalVarQty + fr.frVarQty}" />
		<c:set var="itemTotalVarAmt" value="${itemTotalVarAmt + fr.frVarTaxableAmt}" />
		<c:set var="itemTotalRetQty" value="${itemTotalRetQty + fr.frRetQty}" />
		<c:set var="itemTotalRetAmt" value="${itemTotalRetAmt + fr.frRetTaxableAmt}" />
		<c:set var="itemTotalNetQty" value="${itemTotalNetQty + (fr.frSoldQty-(fr.frVarQty+fr.frRetQty))}" />
		<c:set var="itemTotalNetAmt" value="${itemTotalNetAmt + (fr.frTaxableAmt-(fr.frVarTaxableAmt+fr.frRetTaxableAmt))}" />
		<c:set var="itemTotalRetAmtPer" value="${itemTotalRetAmtPer + (((fr.frVarTaxableAmt+fr.frRetTaxableAmt)*100)/fr.frTaxableAmt)}" />
		
			</c:when>
			<c:otherwise>
			
			<c:set var="itemTotalSoldQty"	value="${itemTotalSoldQty + fr.frSoldQty }" />
		<c:set var="itemTotalSoldAmt" value="${itemTotalSoldAmt + fr.frSoldAmt}" />
		<c:set var="itemTotalVarQty" value="${itemTotalVarQty + fr.frVarQty}" />
		<c:set var="itemTotalVarAmt" value="${itemTotalVarAmt + fr.frVarAmt}" />
		<c:set var="itemTotalRetQty" value="${itemTotalRetQty + fr.frRetQty}" />
		<c:set var="itemTotalRetAmt" value="${itemTotalRetAmt + fr.frRetAmt}" />
		<c:set var="itemTotalNetQty" value="${itemTotalNetQty + (fr.frSoldQty-(fr.frVarQty+fr.frRetQty))}" />
		<c:set var="itemTotalNetAmt" value="${itemTotalNetAmt + (fr.frSoldAmt-(fr.frVarAmt+fr.frRetAmt))}" />
		<c:set var="itemTotalRetAmtPer" value="${itemTotalRetAmtPer + (((fr.frVarAmt+fr.frRetAmt)*100)/fr.frSoldAmt)}" />
			
			</c:otherwise>
		</c:choose>
		
		
		</c:forEach>
		
		<tr>
		
		 <c:if test = "${report_type ==1}"> 
		
		
			<td width="0"><c:out value="${item.itemName}" /></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${itemTotalSoldQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalSoldAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${itemTotalVarQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalVarAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${itemTotalRetQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalRetAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="0"
								value="${itemTotalNetQty}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalNetAmt}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalRetAmtPer}" />%</b></td>	
			
			</c:if>
			
			
			
			 <c:if test = "${report_type ==2}"> 
		
		
			<td width="0"><c:out value="${item.itemName}" /></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${itemTotalSoldQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${itemTotalVarQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${itemTotalRetQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="0"
								value="${itemTotalNetQty}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	
			
			</c:if>
			
			
			
			 <c:if test = "${report_type ==3}"> 
		
		
			<td width="0"><c:out value="${item.itemName}" /></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalSoldAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalVarAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalRetAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="0"
								value="" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalNetAmt}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalRetAmtPer}" />%</b></td>	
			
			</c:if>
			
			 <c:if test = "${report_type ==4}"> 
		
		
			<td width="0"><c:out value="${item.itemName}" /></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${itemTotalSoldQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalSoldAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${itemTotalVarQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalVarAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${itemTotalRetQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalRetAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="0"
								value="${itemTotalNetQty}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalNetAmt}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${itemTotalRetAmtPer}" />%</b></td>	
			
			</c:if>
			
			
			

		</tr>		
		
		
			
			<c:set var="scTotalSoldQty"	value="${scTotalSoldQty + itemTotalSoldQty }" />
		<c:set var="scTotalSoldAmt" value="${scTotalSoldAmt + itemTotalSoldAmt}" />
		<c:set var="scTotalVarQty" value="${scTotalVarQty + itemTotalVarQty}" />
		<c:set var="scTotalVarAmt" value="${scTotalVarAmt + itemTotalVarAmt}" />
		<c:set var="scTotalRetQty" value="${scTotalRetQty + itemTotalRetQty}" />
		<c:set var="scTotalRetAmt" value="${scTotalRetAmt + itemTotalRetAmt}" />
		<c:set var="scTotalNetQty" value="${scTotalNetQty + itemTotalNetQty}" />
		<c:set var="scTotalNetAmt" value="${scTotalNetAmt + itemTotalNetAmt}" />
		<c:set var="scTotalRetAmtPer" value="${scTotalRetAmtPer + itemTotalRetAmtPer}" />
		
		
		</c:forEach>
		
		<tr>
			
			 <c:if test = "${report_type ==1}"> 
			 
				<td width="200"><c:out value="${sc.subCatName}" /></td>
				
				<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${scTotalSoldQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalSoldAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${scTotalVarQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalVarAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${scTotalRetQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalRetAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="0"
								value="${scTotalNetQty}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalNetAmt}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalRetAmtPer}" />%</b></td>	
			
			</c:if>
			
			
			<c:if test = "${report_type ==2}"> 
			 
				<td width="200"><c:out value="${sc.subCatName}" /></td>
				
				<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${scTotalSoldQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${scTotalVarQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${scTotalRetQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="0"
								value="${scTotalNetQty}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="" /></b></td>	
			
			</c:if>
			
			
			<c:if test = "${report_type ==3}"> 
			 
				<td width="200"><c:out value="${sc.subCatName}" /></td>
				
				<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalSoldAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalVarAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalRetAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="0"
								value="" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalNetAmt}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalRetAmtPer}" />%</b></td>	
			
			</c:if>
			
			
			<c:if test = "${report_type ==4}"> 
			 
				<td width="200"><c:out value="${sc.subCatName}" /></td>
				
				<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${scTotalSoldQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalSoldAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${scTotalVarQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalVarAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="0" minFractionDigits="0"
								value="${scTotalRetQty}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalRetAmt}" /></b></td>	

			<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="0"
								value="${scTotalNetQty}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalNetAmt}" /></b></td>	
								
								<td width="0" align="right"><b><fmt:formatNumber
								type="number" maxFractionDigits="2" minFractionDigits="2"
								value="${scTotalRetAmtPer}" />%</b></td>	
			
			</c:if>
			
			
			
			
		</tr>
		
		
		
		</c:forEach>

		</tbody>
	</table>


	<!-- END Main Content -->

</body>
</html>