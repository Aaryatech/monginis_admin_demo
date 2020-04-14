<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MONGINIS Admin</title>

<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>
    </title>
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/semantic-ui/1.12.0/semantic.min.css" />
 <style type="text/css">
 table {

page-break-inside: auto !important;

} 
.floatedTable {
           display: inline-block; float: left; 
        }
        .floatedTable1 {
           display: inline-block;
        }
       .neo_main_container1
{
   width: 100%; //fallback
   width: 100vw;
}
</style>
  </head>
  <body style="	background-color:white;">
    <!-- code goes here -->

    <!-- scripts -->
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js">
    </script>
    <script type="text/javascript" src="//cdn.rawgit.com/MrRio/jsPDF/master/dist/jspdf.min.js">
    </script>
    <script type="text/javascript" src="//cdn.rawgit.com/niklasvh/html2canvas/0.5.0-alpha2/dist/html2canvas.min.js">
    </script>
    
<script type="text/javascript" src="<c:url value='/resources/js/app.js'/>"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jquery-1.10.2.min.js"></script>


 	<c:forEach items="${spCakeOrder}" var="spCakeOrder"
														varStatus="count"> 
	<c:set var="cls" value=""></c:set><c:choose><c:when test="${count.index/2==0}"><c:set var="cls" value="floatedTable"></c:set></c:when><c:otherwise><c:set var="cls" value="floatedTable1"></c:set></c:otherwise></c:choose>

<table style=" border-bottom: 1px solid grey;margin-top: 25px;margin-right: 19px;margin-bottom:35px;margin-left: 45px;" class="${cls}">
<tbody>
<c:set var="srNo" value="${from}"></c:set>

<tr style="height: 21px;border: 1px solid black;border-bottom: 0px solid black;">
<td style="width: 125px; height: 21px;font-size: 20px;">
&nbsp; &nbsp;&nbsp;SLIP.No-&gt;
</td>
<td style="width: 425px; height: 21px;font-size: 20px;">&nbsp; ${srNo}&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<%-- <b><c:choose><c:when test="${spCakeOrder.frRType==1}">A</c:when><c:when test="${spCakeOrder.frRType==2}">B</c:when><c:when test="${spCakeOrder.frRType==3}">C</c:when></c:choose></b> --%></td>
</tr>

<c:set var="srNo" value="${srNo+1}"></c:set>
<%-- <tr style="height: 20px;">
<td style="width: 125px; height: 20px; border-bottom: 1px solid; font-size: 19px;">&nbsp;</td>
<td style="width: 425px; height: 20px; border-bottom: 1px solid; font-size: 19px;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; M.R.P<strong>:</strong>&nbsp; &nbsp; ${spCakeOrder.slipMrp}</td>
</tr> --%>

<tr style="height: 20px;border-top: 1px solid black;">
<td style="width: 125px; height: 20px;  border-left: 1px solid; border-right: 1px solid;  font-size: 19px;">&nbsp; &nbsp;&nbsp;DEL DT&nbsp; &nbsp;&nbsp;<strong>:</strong></td>
<td style="width: 425px; height: 20px; font-size: 19px; font-weight: bold; border: 1px solid;">&nbsp; &nbsp; ${spCakeOrder.spDeliveryDate}   --   ${spCakeOrder.spDeliveryPlace}</td>
</tr>
<tr style="height: 20px;">
<td style="width: 125px; height: 20px; border: 1px solid; font-size: 19px;">&nbsp; &nbsp;&nbsp;ORD DT &nbsp;<strong>:</strong></td>
<td style="width: 425px; height: 20px; font-size: 19px; border: 1px solid;">&nbsp; &nbsp; ${spCakeOrder.orderDate}</td>
</tr>
<tr style="height: 20px;">      <c:set var = "frName" value = "${fn:toUpperCase(spCakeOrder.frName)}" />
<td style="width: 125px; height: 20px;  border-top: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp; &nbsp;&nbsp;SHOP&nbsp; &nbsp; &nbsp;&nbsp;&nbsp;<strong>:</strong></td>
<td style="width: 425px; height: 20px; border-top: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 22px;">&nbsp; &nbsp; ${frName}</td>
</tr>

<%-- <tr style="height: 20px;"> <c:set var = "frCity" value = "${fn:toUpperCase(spCakeOrder.frCity)}" />
<td style="width: 125px; height: 20px;  border-bottom: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp;<strong>:</strong></td>
<td style="width: 425px; height: 20px; font-size: 20px; border-bottom: 1px solid; border-left: 1px solid; border-right: 1px solid;">&nbsp; &nbsp; <strong>${frCity}</strong></td>
</tr> --%>
<tr style="height: 20px;">
<td style="width: 125px; height: 20px; border: 1px solid; font-size: 19px;">&nbsp; &nbsp;&nbsp;QTY&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; <strong>:</strong></td>
<td style="width: 425px; height: 20px; font-size: 19px; font-weight: bold; border: 1px solid;">&nbsp; &nbsp; 1</td>
</tr>
<tr style="height: 20px;"><c:set var = "spName" value = "${fn:toUpperCase(spCakeOrder.spName)}" />
<td style="width: 125px; height: 20px; border-top: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp; &nbsp;&nbsp;ITEM&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp;<strong>:</strong></td>
<td style="width: 425px; height: 20px; font-size: 16px; font-weight: bold; border-top: 1px solid; border-left: 1px solid; border-right: 1px solid;font-size: 20px;">&nbsp; ${spName} </td>
</tr>
<tr style="height: 20px;">
<td style="width: 125px; height: 20px; border-bottom: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp;</td>
<td style="width: 425px; height: 20px; font-size: 19px; border-bottom: 1px solid; border-left: 1px solid; border-right: 1px solid;">&nbsp;${spCakeOrder.itemId}--${spCakeOrder.exVar1}</td>

</tr>
<tr style="height: 20px;"><c:set var = "spSelectedWeight" value = "${fn:toUpperCase(spCakeOrder.spSelectedWeight)}" />
<td style="width: 125px; height: 23px; border: 1px solid; font-size: 19px;">&nbsp; &nbsp;&nbsp;WEIGHT&nbsp;<strong>:</strong></td>
<td style="width: 425px; height: 23px; font-size: 19px; font-weight: bold; border: 1px solid;">&nbsp; &nbsp; &nbsp;${spSelectedWeight} Kg.</td>

</tr>
<tr style="height: 23px;">  <c:set var = "spfName" value = "${fn:toUpperCase(spCakeOrder.spfName)}" />
<td style="width: 125px; height: 20px;border-top: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp; &nbsp;&nbsp;FLAV&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;<strong>:</strong></td>
<td style="width: 425px; height: 20px; font-size: 19px; font-weight: bold; border-top: 1px solid; border-left: 1px solid; border-right: 1px solid;">&nbsp; &nbsp; ${spfName} </td> 

</tr><!-- 
<tr style="height: 20px;">
<td style="width: 125px; height: 20px; border-bottom: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp;</td>
<td style="width: 425px; font-size: 19px; height: 20px; border-bottom: 1px solid; border-left: 1px solid; border-right: 1px solid;">&nbsp;</td>

</tr> -->
<tr style="height: 20px;"><c:set var = "spEvents" value = "${fn:toUpperCase(spCakeOrder.spEvents)}" />
<td style="width: 125px; height: 20px; border-top: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp; &nbsp;&nbsp;MESSG&nbsp;&nbsp;&nbsp; <strong>:</strong></td>
<td style="width: 425px; height: 20px; font-size: 19px; font-weight: bold; border-top: 1px solid; border-left: 1px solid; border-right: 1px solid;">&nbsp; &nbsp; ${spEvents}</td>

</tr>
<tr style="height: 20px;"><c:set var = "spEventsName" value = "${fn:toUpperCase(spCakeOrder.spEventsName)}" />
<td style="width: 125px; height: 20px; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong>:</strong></td>
<td style="width: 425px; height: 20px; font-size: 19px; font-weight: bold; border-left: 1px solid; border-right: 1px solid; ">&nbsp; &nbsp; ${spEventsName} </td>

</tr>
<tr style="height: 20px;"><c:set var = "spInstructions" value = "${fn:toUpperCase(spCakeOrder.spInstructions)}" />
<td style="width: 125px; height: 30px;border-bottom: 1px solid;  border-top: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp;&nbsp;&nbsp;&nbsp;SPL.INST<strong>:</strong></td>
<td style="width: 425px; height: 65px;padding-left:5px;font-weight: bold;  border-bottom: 1px solid;  border-top: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 13px;">&nbsp; ${spInstructions}</td>

</tr><%-- 
<tr style="height: 20px;">
<td style="width: 125px; height: 20px; border-bottom: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp; &nbsp;&nbsp;INST.&nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; <strong>:</strong></td>
<td style="width: 425px; height: 20px; border-bottom: 1px solid; border-left: 1px solid; border-right: 1px solid; font-size: 19px;">&nbsp; &nbsp; ${spInstructions}</td>
 --%>
 <tr style="height: 20px;">
<td style="width: 125px; border-left: 1px solid;height: 20px;font-size: 19px;">&nbsp;&nbsp;&nbsp;&nbsp;PHOTO &nbsp;&nbsp;<strong>:</strong></td>
<td style="width: 425px; border-left: 1px solid;height: 20px;font-size: 19px;border-right: 1px solid;">&nbsp;&nbsp;&nbsp;&nbsp;<c:choose><c:when test="${spCakeOrder.orderPhoto ne ''}"><a  href="${imgUrl2}${spCakeOrder.orderPhoto}" height="60" width="60" style="border:medium;" alt="NA1" target="_blank;">PHOTO1</a></c:when><c:otherwise>NA1</c:otherwise></c:choose>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:choose><c:when test="${spCakeOrder.cusChoicePhoto ne ''}"><a  href="${imgUrl}${spCakeOrder.cusChoicePhoto}" height="60" width="60" style="border:medium;" alt="NA2" target="_blank;">PHOTO2</a></c:when><c:otherwise>NA1</c:otherwise></c:choose></td>

</tr>
</tbody>
</table>	

<%-- 
	<c:if test="${count.index==2}"><div style="page-break-after: always;"></div>
</c:if>	 --%><%-- 												
<table width="100%" style="font-family:arial; font-size:12px; border:1px solid #000000; margin-bottom:40px;">
  <tr bgcolor=lightgrey >
    <td colspan="3" style="font-size:20px; border-bottom:1px solid #000000; padding:8px 7px;" align="center" >AURANGABAD MONGINIS</td>
  </tr>
  <tr >
  
    <td width="15%"  style="border-bottom:1px solid #000000; padding:8px 20px; ">${from}</td>
    <td width="45%"  style="font-family:arial; font-size:20px; border-left: 1px solid  #000000;border-bottom:1px solid #000000; padding:8px 20px;font-weight:bold;" align="center">${spCakeOrder.frName}</td>
    <td width="40%"  style="border-bottom:1px solid #000000; padding:8px; border-left: 1px solid  #000000;">${spCakeOrder.orderDate}</td>
  </tr>
  <tr>
    <td width="15%" style="font-size:12px; border-bottom:1px solid #000000; padding:1px 7px;font-weight:bold;">Sp,Cake Code / Name</td>
    <td style="font-family:arial; font-size:20px;border-left: 1px solid  #000000; border-bottom:1px solid #000000;padding:1px 7px; font-weight:bold;">${spCakeOrder.spName}--${spCakeOrder.itemId} </td>
      <td style="font-size:20px; border-bottom:1px solid #000000; padding:5px 7px;border-left: 1px solid  #000000; font-weight:bold;">Message-- ${spCakeOrder.spEvents} ${spCakeOrder.spEventsName}</td>
  
  </tr>
  <tr>
    <td style="border-bottom:1px solid #000000; padding:5px 7px;font-weight:bold;">Weight</td>
    <td style="font-size:20px; border-bottom:1px solid #000000;border-left: 1px solid  #000000; padding:5px 7px;font-weight:bold;">${spCakeOrder.spSelectedWeight} kg</td>
    <td style="font-size:20px; border-bottom:1px solid #000000; padding:5px 7px;border-left: 1px solid  #000000; font-weight:bold;">Flavour-- ${spCakeOrder.spfName}</td>
  </tr>
  <tr>
    <td style="font-size:12px; border-bottom:1px solid #000000; padding:10px 7px;font-weight:bold;">Special Instructions</td>
    <td colspan="2" style="font-family:arial; font-size:20px; border-left: 1px solid  #000000;border-bottom:1px solid #000000; padding:5px 7px;font-weight:bold;">${spCakeOrder.spInstructions}</td>
  </tr>
 <!--  <tr>
    <td width="15%" style="font-size:12px; border-bottom:1px solid #000000; padding:10px 7px;" >Message on Cake</td>
    <td colspan="2" style=" font-size:20px; border-bottom:1px solid #000000;border-left: 1px solid  #000000; padding:5px 7px;font-weight:bold;" >Happy Birthday Anushka</td>
  </tr> -->
  <tr>
    <td  style=" font-size:12px;padding:5px 7px;font-weight:bold;border-bottom:1px solid #000000;">Date of Delivery </td>
    <td style=" font-size:20px;padding:5px 7px;font-weight:bold;border-left: 1px solid  #000000;border-bottom:1px solid #000000;">${spCakeOrder.spDeliveryDate}</td>
    <td  style=" font-size:20px;padding:5px 7px;font-weight:bold;border-left: 1px solid  #000000;border-bottom:1px solid #000000;">Place of Delivery-- ${spCakeOrder.spDeliveryPlace}</td>
  </tr>

<c:set var="from" value="${from+1}"/>
<tr >
<td style=" font-size:12px;padding:5px 7px;font-weight:bold;border-left: 1px solid  #000000;border-bottom:1px solid #000000;">Photo</td>
<td style="text-align:left; font-size:20px;font-weight:bold;border-left: 1px solid  #000000;border-bottom:1px solid #000000;">Photo 1 :<img src="${imgUrl2}${spCakeOrder.orderPhoto}" height="60" width="60" style="border:medium;" alt="${imgUrl}${spCakeOrder.orderPhoto}"></td>

<td  style=" text-align:left;font-size:20px;font-weight:bold;border-left: 1px solid  #000000;border-bottom:1px solid #000000;">Photo 2 :<img src="${imgUrl}${spCakeOrder.cusChoicePhoto}" height="60" width="60" alt="${imgUrl2}${spCakeOrder.cusChoicePhoto}"></td>
</tr></table> --%>

 </c:forEach>
 	<!-- scripts -->
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script type="text/javascript" src="//cdn.rawgit.com/niklasvh/html2canvas/0.5.0-alpha2/dist/html2canvas.min.js"></script>
	<script type="text/javascript" src="//cdn.rawgit.com/MrRio/jsPDF/master/dist/jspdf.min.js"></script>
	<script type="text/javascript" src="app.js"></script>
</body>
</html>