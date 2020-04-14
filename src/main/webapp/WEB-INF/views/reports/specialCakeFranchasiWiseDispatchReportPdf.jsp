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
	<h4 align="center">MONGINIS ${Constants.CITY}</h4>
	<h6 align="center">Franchise Wise Special Cake Report</h6>


	<div class="box-content">



		<div class="row">
			<div class="col-md-12 table-responsive">
				<div style="">

					<table class="table table-bordered" style="width: 100%"
						id="table_grid" border="1">

						<thead style="background-color: #f3b5db;">
							<tr>
								<th>Sr.No.</th>
								<th>Franchise Code</th>
								<th>Franchise Name</th>
								<th>Total Qty</th>


							</tr>
						</thead>
						<tbody>

							<c:set var="sr" value="0"></c:set>
							<c:forEach items="${dispatchReportList}" var="dispatchReportList">


								<tr>
									<td><c:out value="${sr+1}" /> <c:set var="sr"
											value="${sr+1}"></c:set></td>

									<td align="left"><c:out
											value="${dispatchReportList.catName}" /></td>
									<td align="left"><c:out
											value="${dispatchReportList.frName}" /></td>
									<td style="text-align: right;"><c:set var="orQty"
											value="${dispatchReportList.orderQty}"></c:set> ${orQty}</td>
								</tr>


							</c:forEach>


						</tbody>
					</table>

				</div>

			</div>

		</div>

		<input type="button"
			onclick="tableToExcel('table_grid', 'name', 'SpecialCake.xls')"
			value="Export to Excel">
	</div>

	<!-- END Main Content -->

	<script type="text/javascript">
		function tableToExcel(table, name, filename) {
			let uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><title></title><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>', base64 = function(
					s) {
				return window.btoa(decodeURIComponent(encodeURIComponent(s)))
			}, format = function(s, c) {
				return s.replace(/{(\w+)}/g, function(m, p) {
					return c[p];
				})
			}

			if (!table.nodeType)
				table = document.getElementById(table)
			var ctx = {
				worksheet : name || 'Worksheet',
				table : table.innerHTML
			}

			var link = document.createElement('a');
			link.download = filename;
			link.href = uri + base64(format(template, ctx));
			link.click();
		}
	</script>
</body>
</html>