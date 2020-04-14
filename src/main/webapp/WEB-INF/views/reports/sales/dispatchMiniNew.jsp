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
<title>Dispatch Report Pdf</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

<style type="text/css">
#container {
    column-count:1;
   }
#tbl {
    
}
</style>
<style type="text/css">
 table {
	border-collapse: collapse;
	font-size: 10;
	width:100%;
    page-break-inside: auto !important 
} 
p  {
    color: black;
    font-family: arial;
    font-size: 50%;
	margin-top: 0;
	padding: 0;
}
h6  {
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
<body>

<c:forEach items="${frListSelected}" var="frId" varStatus="ct">
<h5 align="center">MONGINIS &nbsp;&nbsp;
<c:forEach items="${frList}" var="fr" varStatus="count">
			<c:if test="${fr.frId==frId}">
			<b>${fr.frName}</b>
			</c:if>
			</c:forEach></h5>
	<div align="center"> <h5>		
Production Start Date &nbsp; ${billDate} &nbsp;&nbsp; Dispatch Sheet &nbsp;&nbsp; Route ${routeName} &nbsp;&nbsp;Dispatch Date: ${convertedDate}</h5></div>

<div id="container"  >
<table  align="center" border="1" cellspacing="0" cellpadding="6" 
		id="table_grid" class="table table-bordered">
		<thead>
			<tr class="bgpink">
				<th height="25">Sr</th>
				<th>Item Name</th>
			    <th style="font-size: 12px">Disp Qty</th>
			    <th>Sent</th>
			</tr>
		</thead>
		<tbody>
			<c:set var="allTotal" value="0" />

			<c:forEach items="${subCatList}" var="subCat" varStatus="count">
			<c:set var="flag" value="0" />
		
				<c:set var="srNo" value="1"></c:set>
				<c:forEach items="${itemList}" var="item" varStatus="count">
				<c:set var="total" value="0" />
                <c:set var="frTotal" value="0" />
					<c:choose>
						<c:when test="${item.itemGrp2==subCat.subCatId}">

							
								<c:set var="editQty" value="0"></c:set>
								<c:forEach items="${frList}" var="fr" varStatus="count">
									<c:if test="${fr.frId==frId}">
					
								<c:forEach items="${dispatchReportList}" var="report" varStatus="count">
								<c:choose>
								<c:when test="${report.itemId==item.id}">
								<c:choose>
								<c:when test="${report.frId==fr.frId}">
							
								<c:set var="editQty" value="${report.editQty}"></c:set>
								
								</c:when>
								</c:choose>
								</c:when>
								
								</c:choose>
								<c:set var="frTotal" value="${report.editQty+frTotal}" />
								</c:forEach>
								
									</c:if>
								</c:forEach>
								<c:if test="${editQty>0}">
								<c:if test="${flag==0}">
								<tr>
                    <td bgcolor="lightgray"><c:out value="" /></td>
					<td bgcolor="lightgray"><b>${subCat.subCatName}</b></td>
					<td bgcolor="lightgray"><c:out value="" />
					
					 <td bgcolor="lightgray"><c:out value="" /></td>
				     </tr> <c:set var="flag" value="1" /></c:if>
								 <tr>
								<td width="30px" style="text-align:center;"><c:out value="${srNo}" /></td>
								<c:set var="srNo" value="${srNo+1}"></c:set>
								<td width="200px" ><c:out value="${item.itemName}" /></td>
								<td width="50px" style="text-align:center;font-weight: bold;">${editQty}</td> 
								<td width="30px" align="right"></td> 
									
							</tr>
							</c:if>
						</c:when>
					</c:choose>


				</c:forEach>
	       

			</c:forEach>
		
		</tbody>
	</table>
</div>		<div style="page-break-after: always;"></div>


</c:forEach>

</body>

</body>
</html>