package com.ats.adminpanel.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.commons.DateConvertor;
import com.ats.adminpanel.model.AllFrIdNameList;
import com.ats.adminpanel.model.ExportToExcel;
import com.ats.adminpanel.model.reportv2.CrNoteRegItem;
import com.ats.adminpanel.model.reportv2.CrNoteRegSp;
import com.ats.adminpanel.model.reportv2.CrNoteRegisterList;
import com.ats.adminpanel.model.reportv2.GstRegisterItem;
import com.ats.adminpanel.model.reportv2.GstRegisterList;
import com.ats.adminpanel.model.reportv2.GstRegisterSp;
import com.ats.adminpanel.model.reportv2.SalesReport;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
@Scope("session")
public class ReportControllerV2 {
	public static float roundUp(float d) {
		return BigDecimal.valueOf(d).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public AllFrIdNameList allFrIdNameList = new AllFrIdNameList();

	@RequestMapping(value = "/getSalesReportV2", method = RequestMethod.GET)
	public ModelAndView getSalesReportV2(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/v2/fr_sales_report");

		try {

			RestTemplate restTemplate = new RestTemplate();

			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			String todaysDate = date.format(formatters);

			allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			/*
			 * AllRoutesListResponse allRouteListResponse =
			 * restTemplate.getForObject(Constants.url + "showRouteList",
			 * AllRoutesListResponse.class);
			 * 
			 * List<Route> routeList = new ArrayList<Route>();
			 * 
			 * routeList = allRouteListResponse.getRoute();
			 * 
			 * model.addObject("routeList", routeList);
			 */
			model.addObject("todaysDate", todaysDate);
			model.addObject("allFrIdNameList", allFrIdNameList.getFrIdNamesList());

		} catch (Exception e) {
			System.out.println("Exce in showRegCakeSpOrderReport " + e.getMessage());
			e.printStackTrace();
		}

		return model;

	}

	List<SalesReport> saleReportList;

	@RequestMapping(value = "/getSalesReport", method = RequestMethod.GET)
	public @ResponseBody List<SalesReport> callGetRegCakeAsSp(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException {

		String frIdString = "";
		String fromDate = "";
		String toDate = "";

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		RestTemplate restTemplate = new RestTemplate();

		System.out.println("inside getSalesReport ajax call");

		frIdString = request.getParameter("fr_id_list");
		fromDate = request.getParameter("fromDate");
		toDate = request.getParameter("toDate");
		try {

			frIdString = frIdString.substring(1, frIdString.length() - 1);
			frIdString = frIdString.replaceAll("\"", "");

			List<String> franchIds = new ArrayList();

			franchIds = Arrays.asList(frIdString);
			if (franchIds.contains("-1")) {
				map.add("frIdList", -1);

			} else {

				map.add("frIdList", frIdString);
			}
			System.err.println("frId string " + frIdString);
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));

			map.add("toDate", DateConvertor.convertToYMD(toDate));

			SalesReport[] saleRepArray = restTemplate.postForObject(Constants.url + "getSalesReportV2", map,
					SalesReport[].class);
			saleReportList = new ArrayList<>(Arrays.asList(saleRepArray));

			System.err.println("saleReportList " + saleReportList.toString());

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No.");
			rowData.add("Party Code");
			rowData.add("Party Name");
			rowData.add("Sales");
			rowData.add("G.V.N");
			rowData.add("NET Value");
			rowData.add("G.R.N");
			rowData.add("NET Value");
			rowData.add("In Lakh");
			rowData.add("Return %");

			float salesValue = 0;
			float gvn = 0.0f;
			float gvnNetValue = 0.0f;
			float grn = 0.0f;
			float grnNetValue = 0.0f;
			float inLaskTotal = 0.0f;
			float returnTotal = 0.0f;

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			for (int i = 0; i < saleReportList.size(); i++) {
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				rowData.add("" + (i + 1));
				rowData.add("" + saleReportList.get(i).getFrCode());
				rowData.add("" + saleReportList.get(i).getFrName());
				rowData.add("" + roundUp(saleReportList.get(i).getSaleValue()));
				rowData.add("" + roundUp(saleReportList.get(i).getGvnValue()));
				float netVal1 = (saleReportList.get(i).getSaleValue()) - (saleReportList.get(i).getGvnValue());
				float netVal2 = (netVal1) - (saleReportList.get(i).getGrnValue());
				float inLac = (netVal2) / 100000;
				float retPer = 0.0f;
				if (saleReportList.get(i).getGrnValue() > 0) {
					retPer = ((saleReportList.get(i).getGrnValue()) / (saleReportList.get(i).getSaleValue() / 100));
				}
				rowData.add("" + roundUp(netVal1));
				rowData.add("" + roundUp(saleReportList.get(i).getGrnValue()));
				rowData.add("" + roundUp(netVal2));
				rowData.add("" + roundUp(inLac));
				rowData.add("" + roundUp(retPer));

				salesValue = salesValue + saleReportList.get(i).getSaleValue();
				gvn = gvn + saleReportList.get(i).getGvnValue();
				gvnNetValue = gvnNetValue + netVal1;
				grn = grn + saleReportList.get(i).getGrnValue();
				grnNetValue = grnNetValue + netVal2;
				inLaskTotal = inLaskTotal + inLac;
				
				//returnTotal = returnTotal + retPer;

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();

			rowData.add("");
			rowData.add("Total");
			rowData.add("");

			rowData.add("" + roundUp(salesValue));
			rowData.add("" + roundUp(gvn));
			rowData.add("" + roundUp(gvnNetValue));
			rowData.add("" + roundUp(grn));
			rowData.add("" + roundUp(grnNetValue));
			rowData.add("" + roundUp(inLaskTotal));
			
			returnTotal=(grn*100)/salesValue;
			rowData.add("" + roundUp(returnTotal));

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelListNew", exportToExcelList);
			session.setAttribute("excelNameNew", "SalesReport");
			session.setAttribute("reportNameNew", "Sales Report By Franchise");
			session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
			session.setAttribute("mergeUpto1", "$A$1:$L$1");
			session.setAttribute("mergeUpto2", "$A$2:$L$2");

		} catch (Exception e) {

			e.printStackTrace();

		}
		return saleReportList;
	}

	// postProductionPlanDetaillist
	@RequestMapping(value = "/getSalesReportPdf", method = RequestMethod.GET)
	public void showProdByOrderPdf(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException {

		Document document = new Document(PageSize.A4);
		document.setPageSize(PageSize.A4.rotate());
		// ByteArrayOutputStream out = new ByteArrayOutputStream();

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		System.out.println("time in Gen Bill PDF ==" + dateFormat.format(cal.getTime()));
		String timeStamp = dateFormat.format(cal.getTime());
		String FILE_PATH = Constants.REPORT_SAVE;
		File file = new File(FILE_PATH);

		PdfWriter writer = null;

		FileOutputStream out = new FileOutputStream(FILE_PATH);

		try {
			writer = PdfWriter.getInstance(document, out);
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		PdfPTable table = new PdfPTable(9);
		try {
			table.setHeaderRows(1);
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 0.7f, 3.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f });
			Font headFont = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
			Font headFont1 = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
			Font f = new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.UNDERLINE, BaseColor.BLUE);

			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Sr.No.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Party Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Sales", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("GVN", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("NET Value", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("GRN", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("NET Value", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("IN Lacs", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Return%", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);
			float totalSaleValue = 0.0f;
			float totalGvnValue = 0.0f;
			float totalNetVal1 = 0.0f;
			float totalGrnValue = 0.0f;
			float totalNetVal2 = 0.0f;
			float totalInLac = 0.0f;
			float totalRetPer = 0.0f;
			int index = 0;
			for (int j = 0; j < saleReportList.size(); j++) {

				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				float netVal1 = (saleReportList.get(j).getSaleValue()) - (saleReportList.get(j).getGvnValue());
				float netVal2 = (netVal1) - (saleReportList.get(j).getGrnValue());
				float inLac = (netVal2) / 100000;
				float retPer = 0.0f;
				if (saleReportList.get(j).getGrnValue() > 0) {
					retPer = ((saleReportList.get(j).getGrnValue()) / (saleReportList.get(j).getSaleValue() / 100));
				}

				totalSaleValue = totalSaleValue + saleReportList.get(j).getSaleValue();
				totalGvnValue = totalGvnValue + saleReportList.get(j).getGvnValue();
				totalNetVal1 = totalNetVal1 + netVal1;
				totalGrnValue = totalGrnValue + saleReportList.get(j).getGrnValue();
				totalNetVal2 = totalNetVal2 + netVal2;
				totalInLac = totalInLac + inLac;
				//totalRetPer = totalRetPer + retPer;

				cell = new PdfPCell(new Phrase(String.valueOf(saleReportList.get(j).getFrName()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(roundUp(saleReportList.get(j).getSaleValue()) + "", headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(roundUp(saleReportList.get(j).getGvnValue()) + "", headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(roundUp(netVal1)), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(roundUp(saleReportList.get(j).getGrnValue())), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(roundUp(netVal2)), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(roundUp(inLac)), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(roundUp(retPer)), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

			}

			PdfPCell cell;

			cell = new PdfPCell(new Phrase("Total", headFont1));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("", headFont1));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			double d = Double.parseDouble("" + totalSaleValue);
			String strTotalSaleValue = String.format("%.2f", d);

			cell = new PdfPCell(new Phrase("" + strTotalSaleValue, headFont1));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			double d1 = Double.parseDouble("" + totalGvnValue);
			String strTotalGvnValue = String.format("%.2f", d1);

			cell = new PdfPCell(new Phrase("" + strTotalGvnValue, headFont1));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			double d2 = Double.parseDouble("" + totalNetVal1);
			String strTotalNetVal1 = String.format("%.2f", d2);

			cell = new PdfPCell(new Phrase("" + strTotalNetVal1, headFont1));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			double d3 = Double.parseDouble("" + totalGrnValue);
			String strTotalGrnValue = String.format("%.2f", d3);

			cell = new PdfPCell(new Phrase("" + strTotalGrnValue, headFont1));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			double d4 = Double.parseDouble("" + totalNetVal2);
			String strTotalNetVal2 = String.format("%.2f", d4);

			cell = new PdfPCell(new Phrase("" + strTotalNetVal2, headFont1));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			double d5 = Double.parseDouble("" + totalInLac);
			String strTotalInLac = String.format("%.2f", d5);

			cell = new PdfPCell(new Phrase("" + strTotalInLac, headFont1));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			totalRetPer=(totalGrnValue*100)/totalSaleValue;

			double d6 = Double.parseDouble("" + totalRetPer);
			String strTotalRetPer = String.format("%.2f", d6);

			cell = new PdfPCell(new Phrase("" + strTotalRetPer, headFont1));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			document.open();

			Paragraph heading = new Paragraph("Sales Report by Franchisee");
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);

			DateFormat DF = new SimpleDateFormat("dd-MM-yyyy");
			String reportDate = DF.format(new Date());

			document.add(new Paragraph("\n"));

			document.add(table);

			document.close();

			if (file != null) {

				String mimeType = URLConnection.guessContentTypeFromName(file.getName());

				if (mimeType == null) {

					mimeType = "application/pdf";

				}

				response.setContentType(mimeType);

				response.addHeader("content-disposition", String.format("inline; filename=\"%s\"", file.getName()));

				// response.setHeader("Content-Disposition", String.format("attachment;
				// filename=\"%s\"", file.getName()));

				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				try {
					FileCopyUtils.copy(inputStream, response.getOutputStream());
				} catch (IOException e) {
					System.out.println("Excep in Opening a Pdf File");
					e.printStackTrace();
				}

			}

		} catch (DocumentException ex) {

			System.out.println("Pdf Generation Error: Prod From Orders" + ex.getMessage());

			ex.printStackTrace();

		}
	}

	// getGstRegister

	@RequestMapping(value = "/showGstRegister", method = RequestMethod.GET)
	public ModelAndView showGstRegister(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/v2/gst_register");

		try {

			RestTemplate restTemplate = new RestTemplate();

			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			String todaysDate = date.format(formatters);

			allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			model.addObject("todaysDate", todaysDate);
			model.addObject("allFrIdNameList", allFrIdNameList.getFrIdNamesList());

		} catch (Exception e) {
			System.out.println("Exce in showRegCakeSpOrderReport " + e.getMessage());
			e.printStackTrace();
		}

		return model;
	}

	List<GstRegisterItem> gstRegItemList = new ArrayList<>();
	// getGstRegister Ajax

	@RequestMapping(value = "/getGstRegister", method = RequestMethod.GET)
	public @ResponseBody List<GstRegisterItem> getGstRegister(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException {

		String frIdString = "";

		String fromDate = "";
		String toDate = "";

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		RestTemplate restTemplate = new RestTemplate();

		System.out.println("inside getSalesReport ajax call");

		frIdString = request.getParameter("fr_id_list");
		fromDate = request.getParameter("fromDate");
		toDate = request.getParameter("toDate");
		try {

			frIdString = frIdString.substring(1, frIdString.length() - 1);
			frIdString = frIdString.replaceAll("\"", "");

			List<String> franchIds = new ArrayList();

			franchIds = Arrays.asList(frIdString);
			if (franchIds.contains("-1")) {
				map.add("frIdList", -1);

			} else {

				map.add("frIdList", frIdString);
			}
			System.err.println("frId string " + frIdString);
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));

			map.add("toDate", DateConvertor.convertToYMD(toDate));

			GstRegisterList gstArray = restTemplate.postForObject(Constants.url + "getGstRegister", map,
					GstRegisterList.class);

			List<GstRegisterSp> gstRegSpList = new ArrayList<>();

			gstRegItemList = gstArray.getGstRegItemList();
			gstRegSpList = gstArray.getGstRegSpList();
			for (int j = 0; j < gstRegSpList.size(); j++) {
				int flag = 0;

				for (int i = 0; i < gstRegItemList.size(); i++) {

					if (gstRegItemList.get(i).getBillNo() == gstRegSpList.get(j).getBillNo()
							&& gstRegItemList.get(i).getHsnCode().equals(gstRegSpList.get(j).getHsnCode())) {
						flag = 1;
						gstRegItemList.get(i)
								.setBillQty(gstRegItemList.get(i).getBillQty() + gstRegSpList.get(j).getBillQty());

						gstRegItemList.get(i).setTaxableAmt(
								(gstRegItemList.get(i).getTaxableAmt() + gstRegSpList.get(j).getTaxableAmt()));

						gstRegItemList.get(i)
								.setCgstAmt((gstRegItemList.get(i).getCgstAmt() + gstRegSpList.get(j).getCgstAmt()));
						gstRegItemList.get(i)
								.setSgstAmt((gstRegItemList.get(i).getSgstAmt() + gstRegSpList.get(j).getSgstAmt()));
						gstRegItemList.get(i).setGrandTotal(
								(gstRegItemList.get(i).getGrandTotal() + gstRegSpList.get(j).getGrandTotal()));

					}

				}

				if (flag == 0) {

					System.err.println("New hsn code item found ");

					GstRegisterItem regItem = new GstRegisterItem();

					regItem.setBillDate(gstRegSpList.get(j).getBillDate());
					regItem.setBillDetailNo(gstRegSpList.get(j).getBillDetailNo());
					regItem.setBillNo(gstRegSpList.get(j).getBillNo());
					regItem.setBillQty(gstRegSpList.get(j).getBillQty());
					regItem.setCgstAmt(gstRegSpList.get(j).getCgstAmt());
					regItem.setCgstPer(gstRegSpList.get(j).getCgstPer());
					regItem.setFrGstNo(gstRegSpList.get(j).getFrGstNo());
					regItem.setFrName(gstRegSpList.get(j).getFrName());
					regItem.setGrandTotal(gstRegSpList.get(j).getGrandTotal());
					regItem.setHsnCode(gstRegSpList.get(j).getHsnCode());
					regItem.setInvoiceNo(gstRegSpList.get(j).getInvoiceNo());
					regItem.setSgstAmt(gstRegSpList.get(j).getSgstAmt());
					regItem.setSgstPer(gstRegSpList.get(j).getSgstPer());
					regItem.setTaxableAmt(gstRegSpList.get(j).getTaxableAmt());
					regItem.setTaxPer(gstRegSpList.get(j).getTaxPer());
					regItem.setTotalTax(gstRegSpList.get(j).getTotalTax());

					gstRegItemList.add(regItem);
				}
			}

			System.err.println("gstRegItemList combined  " + gstRegItemList.toString());

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No.");
			rowData.add("Invoice No");
			rowData.add("Invoice Date");
			rowData.add("Party Name");
			rowData.add("GST No");
			rowData.add("HSN Code");
			rowData.add("Bill Qty");
			rowData.add("Taxable Amt");
			rowData.add("CGST %");
			rowData.add("CGST Amt");
			rowData.add("SGST %");
			rowData.add("SGST Amt");
			rowData.add("Total Amt");
			rowData.add("Bill Amt");

			float billQty = 0.0f;
			float taxableAmt = 0.0f;
			float cgstSum = 0.0f;
			float sgstSum = 0.0f;
			float grandTotal = 0.0f;
			float totalFinal = 0.0f;

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			for (int i = 0; i < gstRegItemList.size(); i++) {
				float totalAmt = 0.0f;
				for (int j = 0; j < gstRegItemList.size(); j++) {
					if (gstRegItemList.get(i).getBillNo() == gstRegItemList.get(j).getBillNo()) {
						totalAmt = totalAmt + roundUp(gstRegItemList.get(j).getGrandTotal());
					}

				}

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				rowData.add("" + (i + 1));
				rowData.add("" + gstRegItemList.get(i).getInvoiceNo());
				rowData.add("" + gstRegItemList.get(i).getBillDate());
				rowData.add("" + gstRegItemList.get(i).getFrName());
				rowData.add("" + gstRegItemList.get(i).getFrGstNo());

				rowData.add("" + gstRegItemList.get(i).getHsnCode());
				rowData.add("" + roundUp(gstRegItemList.get(i).getBillQty()));
				rowData.add("" + roundUp(gstRegItemList.get(i).getTaxableAmt()));
				rowData.add("" + gstRegItemList.get(i).getCgstPer());
				rowData.add("" + roundUp(gstRegItemList.get(i).getCgstAmt()));
				rowData.add("" + gstRegItemList.get(i).getSgstPer());
				rowData.add("" + roundUp(gstRegItemList.get(i).getSgstAmt()));

				rowData.add("" + roundUp(gstRegItemList.get(i).getGrandTotal()));
				rowData.add("" + roundUp(totalAmt));
				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

				billQty = billQty + gstRegItemList.get(i).getBillQty();
				taxableAmt = taxableAmt + gstRegItemList.get(i).getTaxableAmt();
				cgstSum = cgstSum + gstRegItemList.get(i).getCgstAmt();
				sgstSum = sgstSum + gstRegItemList.get(i).getSgstAmt();
				grandTotal = grandTotal + gstRegItemList.get(i).getGrandTotal();
				totalFinal = totalFinal + totalAmt;

			}

			/*
			 * expoExcel = new ExportToExcel(); rowData = new ArrayList<String>();
			 * 
			 * rowData.add(""); rowData.add("Total"); rowData.add(""); rowData.add("");
			 * rowData.add(""); rowData.add(""); rowData.add("" + roundUp(billQty));
			 * rowData.add("" + roundUp(taxableAmt)); rowData.add("" + roundUp(cgstSum));
			 * rowData.add("" + roundUp(sgstSum)); rowData.add("" + roundUp(grandTotal));
			 * rowData.add("" + roundUp(totalFinal));
			 * 
			 * expoExcel.setRowData(rowData); exportToExcelList.add(expoExcel);
			 */

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelListNew", exportToExcelList);
			session.setAttribute("excelNameNew", "GST Register Report");
			session.setAttribute("reportNameNew", "GST Register Report By Franchise");
			session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
			session.setAttribute("mergeUpto1", "$A$1:$L$1");
			session.setAttribute("mergeUpto2", "$A$2:$L$2");

		} catch (Exception e) {

			e.printStackTrace();

		}
		return gstRegItemList;
	}

	// getGSt Reg Pdf
	@RequestMapping(value = "/getGstRegisterPdf/{fromdate}/{todate}", method = RequestMethod.GET)
	public void getGstRegisterPdf(@PathVariable String fromdate, @PathVariable String todate,
			HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {

		Document document = new Document(PageSize.A4);
		// ByteArrayOutputStream out = new ByteArrayOutputStream();
		document.setPageSize(PageSize.A4.rotate());
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		System.out.println("time in Gen Bill PDF ==" + dateFormat.format(cal.getTime()));
		String timeStamp = dateFormat.format(cal.getTime());
		String FILE_PATH = Constants.REPORT_SAVE;
		File file = new File(FILE_PATH);

		PdfWriter writer = null;

		FileOutputStream out = new FileOutputStream(FILE_PATH);

		try {
			writer = PdfWriter.getInstance(document, out);
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		PdfPTable table = new PdfPTable(13);
		table.setHeaderRows(1);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(
					new float[] { 0.7f, 1.9f, 1.9f, 3.0f, 2.0f, 1.9f, 1.2f, 1.9f, 1.2f, 1.2f, 1.2f, 1.2f, 1.9f });
			Font headFont = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
			Font headFont1 = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
			Font f = new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.UNDERLINE, BaseColor.BLUE);

			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Sr.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Invoice No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Invoice Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Party Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("GST No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("HSN Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Bill Qty", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Taxable Amt", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("CGST %", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("CGST Amt", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("SGST %", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("SGST Amt", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Bill Amt", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			int index = 0;
			for (int j = 0; j < gstRegItemList.size(); j++) {

				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(gstRegItemList.get(j).getInvoiceNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(gstRegItemList.get(j).getBillDate()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(gstRegItemList.get(j).getFrName()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(gstRegItemList.get(j).getFrGstNo()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(gstRegItemList.get(j).getHsnCode()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(roundUp(gstRegItemList.get(j).getBillQty())), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(gstRegItemList.get(j).getTaxableAmt()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(gstRegItemList.get(j).getCgstPer()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(gstRegItemList.get(j).getCgstAmt()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(gstRegItemList.get(j).getSgstPer()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(gstRegItemList.get(j).getSgstAmt()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(
						new Phrase(String.valueOf(roundUp(gstRegItemList.get(j).getGrandTotal())), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

			}
			document.open();

			Paragraph heading = new Paragraph("GST Register Report  \n From Date:" + fromdate + "   To Date:" + todate);
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);

			DateFormat DF = new SimpleDateFormat("dd-MM-yyyy");
			String reportDate = DF.format(new Date());

			document.add(new Paragraph("\n"));

			document.add(table);

			document.close();

			if (file != null) {

				String mimeType = URLConnection.guessContentTypeFromName(file.getName());

				if (mimeType == null) {

					mimeType = "application/pdf";

				}

				response.setContentType(mimeType);

				response.addHeader("content-disposition", String.format("inline; filename=\"%s\"", file.getName()));

				// response.setHeader("Content-Disposition", String.format("attachment;
				// filename=\"%s\"", file.getName()));

				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				try {
					FileCopyUtils.copy(inputStream, response.getOutputStream());
				} catch (IOException e) {
					System.out.println("Excep in Opening a Pdf File");
					e.printStackTrace();
				}

			}

		} catch (DocumentException ex) {

			System.out.println("Pdf Generation Error: Prod From Orders" + ex.getMessage());

			ex.printStackTrace();

		}
	}

	// showCRNoteRegister

	@RequestMapping(value = "/showCRNoteRegister", method = RequestMethod.GET)
	public ModelAndView showCRNoteRegister(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/v2/crNote_register");

		try {

			RestTemplate restTemplate = new RestTemplate();

			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			String todaysDate = date.format(formatters);

			allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			model.addObject("todaysDate", todaysDate);
			model.addObject("allFrIdNameList", allFrIdNameList.getFrIdNamesList());

		} catch (Exception e) {
			System.out.println("Exce in showRegCakeSpOrderReport " + e.getMessage());
			e.printStackTrace();
		}

		return model;
	}

	List<CrNoteRegItem> crNoteRegItemList = new ArrayList<>();
	// getCRNoteRegister Ajax

	@RequestMapping(value = "/getCRNoteRegister", method = RequestMethod.GET)
	public @ResponseBody List<CrNoteRegItem> getCRNoteRegister(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException {

		String frIdString = "";

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		RestTemplate restTemplate = new RestTemplate();

		System.out.println("inside getCRNoteRegister ajax call");

		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		try {

			/*
			 * frIdString = frIdString.substring(1, frIdString.length() - 1); frIdString =
			 * frIdString.replaceAll("\"", "");
			 * 
			 * List<String> franchIds = new ArrayList();
			 * 
			 * franchIds = Arrays.asList(frIdString); if (franchIds.contains("-1")) {
			 * map.add("frIdList", -1);
			 * 
			 * } else {
			 * 
			 * map.add("frIdList", frIdString); } System.err.println("frId string " +
			 * frIdString);
			 */
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));

			map.add("toDate", DateConvertor.convertToYMD(toDate));
			
			

			CrNoteRegisterList crnArray = restTemplate.postForObject(Constants.url + "getCrNoteRegister", map,
					CrNoteRegisterList.class);

			List<CrNoteRegSp> crnRegSpList = new ArrayList<>();

			crNoteRegItemList = crnArray.getCrNoteRegItemList();
			crnRegSpList = crnArray.getCrNoteRegSpList();

			for (int j = 0; j < crnRegSpList.size(); j++) {
				int flag = 0;

				for (int i = 0; i < crNoteRegItemList.size(); i++) {

					if (crNoteRegItemList.get(i).getCrnId() == crnRegSpList.get(j).getCrnId()
							&& crNoteRegItemList.get(i).getHsnCode().equals(crnRegSpList.get(j).getHsnCode())) {
						flag = 1;
						crNoteRegItemList.get(i)
								.setCrnQty(crNoteRegItemList.get(i).getCrnQty() + crnRegSpList.get(j).getCrnQty());

						crNoteRegItemList.get(i).setCrnTaxable(
								(crNoteRegItemList.get(i).getCrnTaxable() + crnRegSpList.get(j).getCrnTaxable()));

						crNoteRegItemList.get(i)
								.setCgstAmt((crNoteRegItemList.get(i).getCgstAmt() + crnRegSpList.get(j).getCgstAmt()));
						crNoteRegItemList.get(i)
								.setSgstAmt((crNoteRegItemList.get(i).getSgstAmt() + crnRegSpList.get(j).getSgstAmt()));
						crNoteRegItemList.get(i)
								.setCrnAmt((crNoteRegItemList.get(i).getCrnAmt() + crnRegSpList.get(j).getCrnAmt()));

					}

				}

				if (flag == 0) {

					System.err.println("New hsn code item found ");

					CrNoteRegItem regItem = new CrNoteRegItem();

					regItem.setCrnDate(crnRegSpList.get(j).getCrnDate());

					regItem.setBillDate(crnRegSpList.get(j).getBillDate());
					regItem.setCrndId(crnRegSpList.get(j).getCrndId());
					regItem.setCrnId(crnRegSpList.get(j).getCrnId());
					regItem.setCrnQty(crnRegSpList.get(j).getCrnQty());
					regItem.setCgstAmt(crnRegSpList.get(j).getCgstAmt());
					regItem.setCgstPer(crnRegSpList.get(j).getCgstPer());
					regItem.setFrGstNo(crnRegSpList.get(j).getFrGstNo());
					regItem.setFrName(crnRegSpList.get(j).getFrName());
					regItem.setCrnAmt(crnRegSpList.get(j).getCrnAmt());
					regItem.setHsnCode(crnRegSpList.get(j).getHsnCode());
					regItem.setInvoiceNo(crnRegSpList.get(j).getInvoiceNo());
					regItem.setSgstAmt(crnRegSpList.get(j).getSgstAmt());
					regItem.setSgstPer(crnRegSpList.get(j).getSgstPer());
					regItem.setCrnTaxable(crnRegSpList.get(j).getCrnTaxable());

					regItem.setFrCode(crnRegSpList.get(j).getFrCode());

					crNoteRegItemList.add(regItem);
				}
			}

			System.err.println("crNoteRegItemList combined  " + crNoteRegItemList.toString());

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add("Sr. No.");
			rowData.add("CRN No");
			rowData.add("CRN Date");
			rowData.add("Invoice No");
			rowData.add("Invoice Date");
			rowData.add("Party Name");
			rowData.add("GST No");
			rowData.add("HSN Code");
			rowData.add("CRN Qty");
			rowData.add("Taxable Amt");
			rowData.add("CGST %");
			rowData.add("CGST Amt");
			rowData.add("SGST %");
			rowData.add("SGST Amt");
			rowData.add("Total Amt");
			rowData.add("CRN Amt");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);
			float crnQty = 0.0f;
			float crnTaxable = 0.0f;
			float cgstAmt = 0.0f;
			float sgstAmt = 0.0f;
			float crnAmt = 0.0f;

			for (int i = 0; i < crNoteRegItemList.size(); i++) {

				float crnTotalAmt = 0;
				for (int k = 0; k < crNoteRegItemList.size(); k++) {

					if (crNoteRegItemList.get(i).getCrnId() == crNoteRegItemList.get(k).getCrnId()) {
						crnTotalAmt = crnTotalAmt + roundUp(crNoteRegItemList.get(k).getCrnAmt());
					}
				}

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				rowData.add("" + (i + 1));
				rowData.add("" + crNoteRegItemList.get(i).getFrCode());
				rowData.add("" + crNoteRegItemList.get(i).getCrnDate());
				rowData.add("" + crNoteRegItemList.get(i).getInvoiceNo());
				rowData.add("" + crNoteRegItemList.get(i).getBillDate());
				rowData.add("" + crNoteRegItemList.get(i).getFrName());
				rowData.add("" + crNoteRegItemList.get(i).getFrGstNo());

				rowData.add("" + crNoteRegItemList.get(i).getHsnCode());
				rowData.add("" + roundUp(crNoteRegItemList.get(i).getCrnQty()));

				crnQty = crnQty + crNoteRegItemList.get(i).getCrnQty();
				crnTaxable = crnTaxable + crNoteRegItemList.get(i).getCrnTaxable();
				cgstAmt = cgstAmt + crNoteRegItemList.get(i).getCgstAmt();
				sgstAmt = sgstAmt + crNoteRegItemList.get(i).getSgstAmt();
				crnAmt = crnAmt + crNoteRegItemList.get(i).getCrnAmt();

				rowData.add("" + roundUp(crNoteRegItemList.get(i).getCrnTaxable()));
				rowData.add("" + crNoteRegItemList.get(i).getCgstPer());
				rowData.add("" + roundUp(crNoteRegItemList.get(i).getCgstAmt()));
				rowData.add("" + crNoteRegItemList.get(i).getSgstPer());
				rowData.add("" + roundUp(crNoteRegItemList.get(i).getSgstAmt()));

				rowData.add("" + roundUp(crNoteRegItemList.get(i).getCrnAmt()));
				rowData.add("" + roundUp(crnTotalAmt));

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();
			rowData.add("");
			rowData.add("");
			rowData.add("");
			rowData.add("");
			rowData.add("");
			rowData.add("");
			rowData.add("");
			rowData.add("Total");
			rowData.add("" + roundUp(crnQty));
			rowData.add("" + roundUp(crnTaxable));
			rowData.add("");
			rowData.add("" + roundUp(cgstAmt));
			rowData.add("");
			rowData.add("" + roundUp(sgstAmt));
			rowData.add("" + Math.round(crnAmt));
			rowData.add("");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelListNew", exportToExcelList);
			session.setAttribute("excelNameNew", "CR Note Register");
			session.setAttribute("reportNameNew", "Credit Note Register Report");
			session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
			session.setAttribute("mergeUpto1", "$A$1:$P$1");
			session.setAttribute("mergeUpto2", "$A$2:$P$2");

		} catch (Exception e) {

			e.printStackTrace();

		}
		return crNoteRegItemList;
	}

	// getCRN Reg Pdf
	@RequestMapping(value = "/getCRNoteRegisterPdf/{fromdate}/{todate}", method = RequestMethod.GET)
	public void getCRNoteRegisterPdf(@PathVariable String fromdate, @PathVariable String todate,
			HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {

		Document document = new Document(PageSize.A4);
		document.setPageSize(PageSize.A4.rotate());
		// ByteArrayOutputStream out = new ByteArrayOutputStream();

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		System.out.println("timegetCRNoteRegisterPdf PDF ==" + dateFormat.format(cal.getTime()));
		String timeStamp = dateFormat.format(cal.getTime());
		String FILE_PATH = Constants.REPORT_SAVE;
		File file = new File(FILE_PATH);

		PdfWriter writer = null;

		FileOutputStream out = new FileOutputStream(FILE_PATH);

		try {
			writer = PdfWriter.getInstance(document, out);
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		PdfPTable table = new PdfPTable(16);
		table.setHeaderRows(1);
		try {
			System.out.println("Inside PDF Table try");
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 0.7f, 1.1f, 2.0f, 2.1f, 2.3f, 2.0f, 2.2f, 1.2f, 1.2f, 1.2f, 0.9f, 1.2f, 1.2f,
					0.9f, 1.2f, 1.2f });
			Font headFont = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
			Font headFont1 = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
			Font f = new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.UNDERLINE, BaseColor.BLUE);

			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Sr.", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("CRN No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("CRN Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Invoice No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Invoice Date", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Party Name", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("GST No", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("HSN Code", headFont1));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Bill Qty", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Taxable Amt", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("CGST %", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("CGST Amt", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("SGST %", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("SGST Amt", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Total Amt", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Crn Amt", headFont1)); // Varience title replaced with P2 Production
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hcell.setBackgroundColor(BaseColor.PINK);
			table.addCell(hcell);
			
			
			
			float totTaxableAmt=0,totBillQty=0,totCgstAmt=0,totSgstAmt=0,totCrnAmt=0,totAmt=0;

			int index = 0;
			for (int j = 0; j < crNoteRegItemList.size(); j++) {

				float crnTotalAmt = 0;
				for (int k = 0; k < crNoteRegItemList.size(); k++) {

					if (crNoteRegItemList.get(j).getCrnId() == crNoteRegItemList.get(k).getCrnId()) {
						System.err.println("crNoteRegItemList.get(j).getCrnId()" + crNoteRegItemList.get(j).getCrnId()
								+ "crnAmt:" + crNoteRegItemList.get(j).getCrnAmt());
						crnTotalAmt = crnTotalAmt + roundUp(crNoteRegItemList.get(k).getCrnAmt());
					}
				}
				index++;
				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(index), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("" + crNoteRegItemList.get(j).getFrCode(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(1);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(crNoteRegItemList.get(j).getCrnDate()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(1);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(crNoteRegItemList.get(j).getInvoiceNo(), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(1);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(crNoteRegItemList.get(j).getBillDate()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(1);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(crNoteRegItemList.get(j).getFrName()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(1);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(crNoteRegItemList.get(j).getFrGstNo()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPaddingRight(1);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(crNoteRegItemList.get(j).getHsnCode()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(1);
				table.addCell(cell);

				cell = new PdfPCell(
						new Phrase(String.valueOf(roundUp(crNoteRegItemList.get(j).getCrnQty())), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(
						new Phrase(String.valueOf(roundUp(crNoteRegItemList.get(j).getCrnTaxable())), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(crNoteRegItemList.get(j).getCgstPer()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(
						new Phrase(String.valueOf(roundUp(crNoteRegItemList.get(j).getCgstAmt())), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(crNoteRegItemList.get(j).getSgstPer()), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(
						new Phrase(String.valueOf(roundUp(crNoteRegItemList.get(j).getSgstAmt())), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(
						new Phrase(String.valueOf(roundUp(crNoteRegItemList.get(j).getCrnAmt())), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(roundUp(crnTotalAmt)), headFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(8);
				table.addCell(cell);
				
				
				 totTaxableAmt=totTaxableAmt+crNoteRegItemList.get(j).getCrnTaxable();
				 totBillQty=totBillQty+crNoteRegItemList.get(j).getCrnQty();
				 totCgstAmt=totCgstAmt+crNoteRegItemList.get(j).getCgstAmt();
				 totSgstAmt=totSgstAmt+crNoteRegItemList.get(j).getSgstAmt();
				 totCrnAmt=totCrnAmt+crNoteRegItemList.get(j).getCrnAmt();
				 totAmt=totAmt+crnTotalAmt;

			}
			
			PdfPCell cell;
			
			cell = new PdfPCell(new Phrase(" ", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" ", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" ", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" ", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" ", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" ", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" ", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("TOTAL", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(String.valueOf(roundUp(totBillQty)), headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(String.valueOf(roundUp(totTaxableAmt)), headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" ", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(String.valueOf(roundUp(totCgstAmt)), headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" ", headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(String.valueOf(roundUp(totSgstAmt)), headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(String.valueOf(roundUp(totAmt)), headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(String.valueOf(roundUp(totCrnAmt)), headFont));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(8);
			table.addCell(cell);
			
			
			
			
			document.open();

			Paragraph heading = new Paragraph(
					"Credit Note Register Report \n From Date:" + fromdate + " To Date:" + todate);
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);

			DateFormat DF = new SimpleDateFormat("dd-MM-yyyy");
			String reportDate = DF.format(new Date());

			document.add(new Paragraph("\n"));

			document.add(table);

			document.close();

			if (file != null) {

				String mimeType = URLConnection.guessContentTypeFromName(file.getName());

				if (mimeType == null) {

					mimeType = "application/pdf";

				}

				response.setContentType(mimeType);

				response.addHeader("content-disposition", String.format("inline; filename=\"%s\"", file.getName()));

				// response.setHeader("Content-Disposition", String.format("attachment;
				// filename=\"%s\"", file.getName()));

				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				try {
					FileCopyUtils.copy(inputStream, response.getOutputStream());
				} catch (IOException e) {
					System.out.println("Excep in Opening a Pdf File");
					e.printStackTrace();
				}

			}

		} catch (DocumentException ex) {

			System.out.println("Pdf Generation Error: Prod From Orders" + ex.getMessage());

			ex.printStackTrace();

		}
	}

}
