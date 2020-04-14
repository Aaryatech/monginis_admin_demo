package com.ats.adminpanel.controller;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import com.ats.adminpanel.commons.AccessControll;
import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.commons.DateConvertor;
import com.ats.adminpanel.model.AllFrIdName;
import com.ats.adminpanel.model.AllFrIdNameList;
import com.ats.adminpanel.model.AllRoutesListResponse;
import com.ats.adminpanel.model.DispatchReport;
import com.ats.adminpanel.model.DispatchReportList;
import com.ats.adminpanel.model.ExportToExcel;
import com.ats.adminpanel.model.FranchiseForDispatch;
import com.ats.adminpanel.model.GrandTotalBillWise;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.NonRegFrTaxDao;
import com.ats.adminpanel.model.PDispatchReport;
import com.ats.adminpanel.model.PDispatchReportList;
import com.ats.adminpanel.model.POrder;
import com.ats.adminpanel.model.Route;
import com.ats.adminpanel.model.SpFlavourSummaryDao;
import com.ats.adminpanel.model.SpFlavourSummaryDaoResponse;
import com.ats.adminpanel.model.SpKgSummaryDao;
import com.ats.adminpanel.model.SpKgSummaryDaoResponse;
import com.ats.adminpanel.model.Tax1Report;
import com.ats.adminpanel.model.Tax2Report;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.creditnote.GetCreditNoteReport;
import com.ats.adminpanel.model.creditnote.GetCreditNoteReportList;
import com.ats.adminpanel.model.franchisee.AllMenuResponse;
import com.ats.adminpanel.model.franchisee.FrNameIdByRouteId;
import com.ats.adminpanel.model.franchisee.FrNameIdByRouteIdResponse;
import com.ats.adminpanel.model.franchisee.FranchiseeAndMenuList;
import com.ats.adminpanel.model.franchisee.FranchiseeList;
import com.ats.adminpanel.model.franchisee.Menu;
import com.ats.adminpanel.model.franchisee.SubCategory;
import com.ats.adminpanel.model.item.CategoryListResponse;
import com.ats.adminpanel.model.item.FrItemStockConfigureList;
import com.ats.adminpanel.model.item.Item;
import com.ats.adminpanel.model.item.MCategoryList;
import com.ats.adminpanel.model.salesreport.RoyaltyListBean;
import com.ats.adminpanel.model.salesreport.SalesReportBillwise;
import com.ats.adminpanel.model.salesreport.SalesReportBillwiseAllFr;
import com.ats.adminpanel.model.salesreport.SalesReportDateMonth;
import com.ats.adminpanel.model.salesreport.SalesReportItemwise;
import com.ats.adminpanel.model.salesreport.SalesReportRoyalty;
import com.ats.adminpanel.model.salesreport.SalesReportRoyaltyFr;
import com.ats.adminpanel.model.salesvaluereport.SalesReturnQtyReportList;
import com.ats.adminpanel.model.salesvaluereport.SalesReturnValueDaoList;
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
public class SalesReportController {

	List<MCategoryList> mCategoryList;
	List<String> frList = new ArrayList<>();
	AllFrIdNameList allFrIdNameList = new AllFrIdNameList();
	List<SalesReportBillwise> saleListForPdf;// it is Static

	String todaysDate;
	List<SalesReportRoyalty> royaltyListForPdf;

	List<SalesReportBillwiseAllFr> staticSaleByAllFr;

	List<SalesReportRoyaltyFr> royaltyFrList;

	List<SalesReportRoyaltyFr> staticRoyaltyFrList;

	List<SalesReportItemwise> staticSaleListItemWise;

	RoyaltyListBean staticRoyaltyBean = new RoyaltyListBean();

	float getRoyPer() {

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		RestTemplate restTemplate = new RestTemplate();

		String settingKey = new String();

		settingKey = "roy_percentage";

		map.add("settingKeyList", settingKey);

		FrItemStockConfigureList settingList = restTemplate.postForObject(Constants.url + "getDeptSettingValue", map,
				FrItemStockConfigureList.class);

		float royPer = settingList.getFrItemStockConfigure().get(0).getSettingValue();

		return royPer;
	}

	@RequestMapping(value = "/showSaleReports", method = RequestMethod.GET)
	public ModelAndView showSaleReporPage(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/viewSalesReports");

		return model;

	}

	@RequestMapping(value = "/showSaleReportByDate", method = RequestMethod.GET)
	public ModelAndView showSaleReportByDate(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSaleReportByDate", "showSaleReportByDate", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/sales/billwisesalesbydate");

			// Constants.mainAct =2;
			// Constants.subAct =20;

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}
				List<AllFrIdName> selectedFrListAll = new ArrayList();
				List<Menu> selectedMenuList = new ArrayList<Menu>();

				System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());

				model.addObject("todaysDate", todaysDate);
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);
				CategoryListResponse categoryListResponse;

				categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);

				mCategoryList = categoryListResponse.getmCategoryList();

				model.addObject("mCategoryList", mCategoryList);
			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}
		return model;

	}

	@RequestMapping(value = "/showTaxReport", method = RequestMethod.GET)
	public ModelAndView showTaxReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();
		String fromDate = "";
		String toDate = "";
		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showTaxReport", "showTaxReport", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/tax/tax1Report");
			// Constants.mainAct =2;
			// Constants.subAct =20;
			List<Tax1Report> taxReportList = null;

			try {

				RestTemplate restTemplate = new RestTemplate();

				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");

				if (fromDate == null && toDate == null) {
					Date date = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					fromDate = formatter.format(date);
					toDate = formatter.format(date);
				}
				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<Tax1Report>> typeRef = new ParameterizedTypeReference<List<Tax1Report>>() {
				};
				ResponseEntity<List<Tax1Report>> responseEntity = restTemplate.exchange(Constants.url + "getTax1Report",
						HttpMethod.POST, new HttpEntity<>(map), typeRef);

				taxReportList = responseEntity.getBody();
				model.addObject("taxReportList", taxReportList);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);

				GrandTotalBillWise[] grandTotalBillWise = restTemplate
						.postForObject(Constants.url + "getGrandTotalBillWise", map, GrandTotalBillWise[].class);
				List<GrandTotalBillWise> headerList = new ArrayList<GrandTotalBillWise>(
						Arrays.asList(grandTotalBillWise));

				// exportToExcel
				List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

				ExportToExcel expoExcel = new ExportToExcel();
				List<String> rowData = new ArrayList<String>();

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				rowData.add("Sr.No.");
				rowData.add("GSTIN/UIN of Recipient");
				rowData.add("Receiver Name");
				rowData.add("Invoice No");
				rowData.add("Invoice date");
				rowData.add("Invoice Value");
				rowData.add("Place of Supply");
				rowData.add("Reverse Charge");

				rowData.add("Applicable % of Tax Rate");
				rowData.add("Invoice Type");
				rowData.add("E Commerce GSTIN");
				rowData.add("Rate");
				rowData.add("Taxable Value");
				rowData.add("Cess Amount");

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

				float taxableAmt = 0.0f;
				float cgstSum = 0.0f;
				float sgstSum = 0.0f;

				float totalTax = 0.0f;
				float totalFinal = 0.0f;
				float grandTotal = 0.0f;

				for (int i = 0; i < taxReportList.size(); i++) {
					float finalTotal = 0;
					for (int j = 0; j < taxReportList.size(); j++) {

						if (taxReportList.get(j).getBillNo() == taxReportList.get(i).getBillNo()) {
							finalTotal = finalTotal + taxReportList.get(j).getGrandTotal();
						}
					}

					taxableAmt = taxableAmt + taxReportList.get(i).getTaxableAmt();
					cgstSum = cgstSum + taxReportList.get(i).getCgstAmt();
					sgstSum = sgstSum + taxReportList.get(i).getSgstAmt();

					totalTax = totalTax + taxReportList.get(i).getTotalTax();
					grandTotal = grandTotal + taxReportList.get(i).getGrandTotal();
					totalFinal = totalFinal + finalTotal;

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();
					rowData.add((i + 1) + "");
					rowData.add("" + taxReportList.get(i).getFrGstNo());
					rowData.add("" + taxReportList.get(i).getFrName());
					rowData.add("" + taxReportList.get(i).getInvoiceNo());
					rowData.add("" + taxReportList.get(i).getBillDate());

					rowData.add("" + finalTotal);
					rowData.add("" + Constants.STATE);
					rowData.add("N");
					rowData.add(" ");

					rowData.add("Regular");
					rowData.add(" ");

					rowData.add("" + (taxReportList.get(i).getCgstPer() + taxReportList.get(i).getSgstPer()));
					rowData.add("" + taxReportList.get(i).getTaxableAmt());
					rowData.add("0");

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);

				}

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();

				rowData.add("Total");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("" + roundUp(totalFinal));
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

				session.setAttribute("exportExcelListNew", exportToExcelList);
				session.setAttribute("excelNameNew", "Tax1Report");
				session.setAttribute("reportNameNew", "Tax_Repot1");
				session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
				session.setAttribute("mergeUpto1", "$A$1:$N$1");
				session.setAttribute("mergeUpto2", "$A$2:$N$2");

				DecimalFormat df = new DecimalFormat("#.00");

				List<ExportToExcel> exportToExcelList1 = new ArrayList<ExportToExcel>();

				ExportToExcel expoExcel1 = new ExportToExcel();
				List<String> rowData1 = new ArrayList<String>();

				expoExcel1 = new ExportToExcel();
				rowData1 = new ArrayList<String>();
				rowData1.add("Id");
				rowData1.add("DATE");
				rowData1.add("VOUCHER TYPE");
				rowData1.add("VOUCHER NUMBER");
				rowData1.add("LEDGER NAME");
				rowData1.add("LEDGER AMT");
				rowData1.add("AMT TYPE");
				rowData1.add("NARRATION");

				expoExcel1.setRowData(rowData1);
				exportToExcelList1.add(expoExcel1);

				for (int j = 0; j < headerList.size(); j++) {

					expoExcel1 = new ExportToExcel();
					rowData1 = new ArrayList<String>();
					rowData1.add(headerList.get(j).getInvoiceNo());
					rowData1.add(" " + headerList.get(j).getBillDate());
					rowData1.add("Sales");
					rowData1.add(headerList.get(j).getInvoiceNo());
					rowData1.add(headerList.get(j).getFrName());
					rowData1.add(" " + (int) Math.ceil(headerList.get(j).getGrandTotal()));
					rowData1.add("Dr");
					rowData1.add(" ");
					expoExcel1.setRowData(rowData1);
					exportToExcelList1.add(expoExcel1);

					double finalAmt = 0;

					for (int i = 0; i < taxReportList.size(); i++) {

						if (headerList.get(j).getBillNo() == taxReportList.get(i).getBillNo()) {
							expoExcel1 = new ExportToExcel();
							rowData1 = new ArrayList<String>();
							rowData1.add(headerList.get(j).getInvoiceNo());
							rowData1.add("");
							rowData1.add("");
							rowData1.add("");
							rowData1.add("Sales Gst " + taxReportList.get(i).getTaxPer() + "%");
							rowData1.add(" " + df.format(taxReportList.get(i).getTaxableAmt()));
							rowData1.add("Cr");
							rowData1.add(" ");
							expoExcel1.setRowData(rowData1);
							exportToExcelList1.add(expoExcel1);

							BigDecimal bd = new BigDecimal(taxReportList.get(i).getTaxableAmt()).setScale(2,
									RoundingMode.HALF_UP);
							double taxable = bd.doubleValue();
							bd = new BigDecimal(taxReportList.get(i).getCgstAmt()).setScale(2, RoundingMode.HALF_UP);
							double cgstAmt = bd.doubleValue();
							bd = new BigDecimal(taxReportList.get(i).getSgstAmt()).setScale(2, RoundingMode.HALF_UP);
							double sgstAmt = bd.doubleValue();

							finalAmt = finalAmt + taxable + cgstAmt + sgstAmt;

							expoExcel1 = new ExportToExcel();
							rowData1 = new ArrayList<String>();
							rowData1.add(headerList.get(j).getInvoiceNo());
							rowData1.add("");
							rowData1.add("");
							rowData1.add("");
							rowData1.add("CGST " + taxReportList.get(i).getCgstPer() + "%");
							rowData1.add(" " + df.format(taxReportList.get(i).getCgstAmt()));
							rowData1.add("Cr");
							rowData1.add(" ");
							expoExcel1.setRowData(rowData1);
							exportToExcelList1.add(expoExcel1);

							expoExcel1 = new ExportToExcel();
							rowData1 = new ArrayList<String>();
							rowData1.add(headerList.get(j).getInvoiceNo());
							rowData1.add("");
							rowData1.add("");
							rowData1.add("");
							rowData1.add("SGST " + taxReportList.get(i).getSgstPer() + "%");
							rowData1.add(" " + df.format(taxReportList.get(i).getSgstAmt()));
							rowData1.add("Cr");
							rowData1.add(" ");
							expoExcel1.setRowData(rowData1);
							exportToExcelList1.add(expoExcel1);
						}

					}

					expoExcel1 = new ExportToExcel();
					rowData1 = new ArrayList<String>();
					rowData1.add(headerList.get(j).getInvoiceNo());
					rowData1.add("");
					rowData1.add("");
					rowData1.add("");
					rowData1.add("kasar / vatav ");
					rowData1.add(" " + ((int) Math.ceil(headerList.get(j).getGrandTotal()) - finalAmt));
					rowData1.add("Cr");
					rowData1.add(" ");
					expoExcel1.setRowData(rowData1);
					exportToExcelList1.add(expoExcel1);
				}

				session.setAttribute("exportToExcelTally", exportToExcelList1);
				session.setAttribute("excelNameNewTally", "Tax1ReportTally");
				session.setAttribute("reportNameNewTally", "Tax_Repot_Tally");
				session.setAttribute("searchByNewTally", "From Date: " + fromDate + "  To Date: " + toDate + " ");
				session.setAttribute("mergeUpto1Tally", "$A$1:$H$1");
				session.setAttribute("mergeUpto2Tally", "$A$2:$H$2");

			} catch (Exception e) {
				System.out.println("Exc in Tax Report" + e.getMessage());
				e.printStackTrace();
			}
		}

		return model;

	}

	@RequestMapping(value = "/showTax2Report", method = RequestMethod.GET)
	public ModelAndView showTax2Report(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		String fromDate = "";
		String toDate = "";

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showTax2Report", "showTax2Report", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/tax/tax2Report");
			// Constants.mainAct =2;
			// Constants.subAct =20;
			List<Tax2Report> taxReportList = null;
			fromDate = "";
			toDate = "";
			try {

				RestTemplate restTemplate = new RestTemplate();

				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");

				if (fromDate == null && toDate == null) {
					Date date = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					fromDate = formatter.format(date);
					toDate = formatter.format(date);
				}
				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<Tax2Report>> typeRef = new ParameterizedTypeReference<List<Tax2Report>>() {
				};
				ResponseEntity<List<Tax2Report>> responseEntity = restTemplate.exchange(Constants.url + "getTax2Report",
						HttpMethod.POST, new HttpEntity<>(map), typeRef);

				taxReportList = responseEntity.getBody();
				model.addObject("taxReportList", taxReportList);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);

				// exportToExcel
				List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

				ExportToExcel expoExcel = new ExportToExcel();
				List<String> rowData = new ArrayList<String>();

				rowData.add("Sr.No.");
				rowData.add("Invoice No");
				rowData.add("Bill No");
				rowData.add("Bill Date");
				rowData.add("Party Name");
				rowData.add("GSTIN");

				rowData.add("Sell @ 28%");
				rowData.add("Sell @ 18%");
				rowData.add("Sell @ 12%");
				rowData.add("Sell @ 5%");
				rowData.add("Sell @ 0%");
				rowData.add("Taxable Value");
				rowData.add("SGST @ 14%");
				rowData.add("CGST @ 14%");
				rowData.add("SGST @ 9%");
				rowData.add("CGST @ 9%");
				rowData.add("SGST @ 6%");
				rowData.add("CGST @ 6%");
				rowData.add("SGST @ 2.5%");
				rowData.add("CGST @ 2.5%");
				rowData.add("SGST @ 0%");
				rowData.add("CGST @ 0%");
				rowData.add("SGST Value");
				rowData.add("CGST Value");
				rowData.add("GROSS BILL");

				float totalTaxableValue = 0.0f;
				float totalCgstValue = 0.0f;
				float totalSgstValue = 0.0f;
				float totalGrossBill = 0.0f;

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);
				for (int i = 0; i < taxReportList.size(); i++) {
					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();
					rowData.add((i + 1) + "");
					rowData.add("" + taxReportList.get(i).getInvoiceNo());
					rowData.add("" + taxReportList.get(i).getBillNo());
					rowData.add("" + taxReportList.get(i).getBillDate());

					rowData.add("" + taxReportList.get(i).getFrName());
					rowData.add("" + taxReportList.get(i).getFrGstNo());

					rowData.add("" + taxReportList.get(i).getTaxableAmtTwentyEight());
					rowData.add("" + taxReportList.get(i).getTaxableAmtEighteen());
					rowData.add("" + taxReportList.get(i).getTaxableAmtTwelve());
					rowData.add("" + taxReportList.get(i).getTaxableAmtFive());
					rowData.add("" + taxReportList.get(i).getTaxableAmtZero());
					float taxableAmt = Math.round((taxReportList.get(i).getTaxableAmtTwentyEight()
							+ taxReportList.get(i).getTaxableAmtEighteen() + taxReportList.get(i).getTaxableAmtTwelve()
							+ taxReportList.get(i).getTaxableAmtFive() + taxReportList.get(i).getTaxableAmtZero()));

					rowData.add("" + taxableAmt);
					rowData.add("" + taxReportList.get(i).getSgstAmtTwentyEight());
					rowData.add("" + taxReportList.get(i).getCgstAmtTwentyEight());
					rowData.add("" + taxReportList.get(i).getSgstAmtEighteen());
					rowData.add("" + taxReportList.get(i).getCgstAmtEighteen());
					rowData.add("" + taxReportList.get(i).getSgstAmtTwelve());
					rowData.add("" + taxReportList.get(i).getCgstAmtTwelve());
					rowData.add("" + taxReportList.get(i).getSgstAmtFive());
					rowData.add("" + taxReportList.get(i).getCgstAmtFive());
					rowData.add("" + taxReportList.get(i).getSgstAmtZero());
					rowData.add("" + taxReportList.get(i).getCgstAmtZero());
					float sgstSum = Math.round((taxReportList.get(i).getSgstAmtTwentyEight()
							+ taxReportList.get(i).getSgstAmtEighteen() + taxReportList.get(i).getSgstAmtTwelve()
							+ taxReportList.get(i).getSgstAmtFive() + taxReportList.get(i).getSgstAmtZero()));
					float cgstSum = Math.round((taxReportList.get(i).getCgstAmtTwentyEight()
							+ taxReportList.get(i).getCgstAmtEighteen() + taxReportList.get(i).getCgstAmtTwelve()
							+ taxReportList.get(i).getCgstAmtFive() + taxReportList.get(i).getCgstAmtZero()));
					float grossSell = Math.round((taxableAmt + sgstSum + cgstSum));
					rowData.add("" + sgstSum);
					rowData.add("" + cgstSum);
					rowData.add("" + grossSell);

					totalTaxableValue = totalTaxableValue + taxableAmt;
					totalCgstValue = totalCgstValue + cgstSum;
					totalSgstValue = totalSgstValue + sgstSum;
					totalGrossBill = totalGrossBill + grossSell;

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
				rowData.add("");
				rowData.add("");
				rowData.add("Total");
				rowData.add("");

				rowData.add("" + roundUp(totalTaxableValue));

				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("");
				rowData.add("" + roundUp(totalSgstValue));
				rowData.add("" + roundUp(totalCgstValue));

				rowData.add("" + roundUp(totalGrossBill));

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

				session = request.getSession();
				session.setAttribute("exportExcelListNew", exportToExcelList);
				session.setAttribute("excelNameNew", "Tax_Repot2");
				session.setAttribute("reportNameNew", "Bill-wise Report");
				session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
				session.setAttribute("mergeUpto1", "$A$1:$L$1");
				session.setAttribute("mergeUpto2", "$A$2:$L$2");

			} catch (Exception e) {
				System.out.println("Exc in Tax Report" + e.getMessage());
				e.printStackTrace();
			}
		}

		return model;

	}

	@RequestMapping(value = "/getSaleBillwise", method = RequestMethod.GET)
	public @ResponseBody List<SalesReportBillwise> saleReportBillWise(HttpServletRequest request,
			HttpServletResponse response) {

		List<SalesReportBillwise> saleList = new ArrayList<>();
		String fromDate = "";
		String toDate = "";
		try {
			System.out.println("Inside get Sale Bill Wise");
			String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			String routeId = request.getParameter("route_id");

			String selectedCat = request.getParameter("cat_id_list");
			List<String> catIdList = new ArrayList<>();
			selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
			selectedCat = selectedCat.replaceAll("\"", "");
			catIdList = Arrays.asList(selectedCat);
			System.err.println("cat Id List " + catIdList.toString());

			boolean isAllFrSelected = false;
			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			frList = new ArrayList<>();
			frList = Arrays.asList(selectedFr);

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (frList.contains("-1")) {
				isAllFrSelected = true;
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catIdList", selectedCat);

				ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwise>>() {
				};
				ResponseEntity<List<SalesReportBillwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportBillwiseAllFrSelected", HttpMethod.POST, new HttpEntity<>(map),
						typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				saleListForPdf = saleList;
				System.out.println("sales List Bill Wise " + saleList.toString());

			} else {
				System.out.println("Inside else Few fr Selected ");

				map.add("frIdList", selectedFr);
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catIdList", selectedCat);

				ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwise>>() {
				};
				ResponseEntity<List<SalesReportBillwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportBillwise", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				saleListForPdf = saleList;
				System.out.println("sales List Bill Wise " + saleList.toString());

			}
		} catch (

		Exception e) {
			System.out.println("get sale Report Bill Wise " + e.getMessage());
			e.printStackTrace();

		}

		// exportToExcel
		List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

		ExportToExcel expoExcel = new ExportToExcel();
		List<String> rowData = new ArrayList<String>();

		rowData.add("Sr");
		rowData.add("Invoice No");
		rowData.add("Invoice Date");

		rowData.add("Franchise");
		rowData.add("City");
		rowData.add("GST No");
		rowData.add("Taxable Amt");
		rowData.add("CGST Amt");
		rowData.add("SGST Amt");

		rowData.add("IGST Amt");
		rowData.add("Total Tax Amt");
		rowData.add("Total");

		expoExcel.setRowData(rowData);
		int srno = 1;
		exportToExcelList.add(expoExcel);
		float taxableAmt = 0.0f;
		float cgstSum = 0.0f;
		float sgstSum = 0.0f;
		float igstSum = 0.0f;
		float totalTax = 0.0f;
		float grandTotal = 0.0f;

		for (int i = 0; i < saleList.size(); i++) {

			taxableAmt = taxableAmt + saleList.get(i).getTaxableAmt();
			cgstSum = cgstSum + saleList.get(i).getCgstSum();
			sgstSum = sgstSum + saleList.get(i).getSgstSum();
			igstSum = igstSum + saleList.get(i).getIgstSum();
			totalTax = totalTax + saleList.get(i).getTotalTax();
			grandTotal = grandTotal + saleList.get(i).getGrandTotal();

			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();

			rowData.add("" + srno);
			rowData.add(saleList.get(i).getInvoiceNo());
			rowData.add(saleList.get(i).getBillDate());

			rowData.add(saleList.get(i).getFrName());

			rowData.add(saleList.get(i).getFrCity());
			rowData.add(saleList.get(i).getFrGstNo());
			rowData.add("" + roundUp(saleList.get(i).getTaxableAmt()));
			rowData.add("" + roundUp(saleList.get(i).getCgstSum()));
			rowData.add("" + roundUp(saleList.get(i).getSgstSum()));

			rowData.add("" + roundUp(saleList.get(i).getIgstSum()));
			rowData.add("" + roundUp(saleList.get(i).getTotalTax()));
			rowData.add("" + roundUp(saleList.get(i).getGrandTotal()));
			srno = srno + 1;

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

		}
		expoExcel = new ExportToExcel();
		rowData = new ArrayList<String>();

		rowData.add("");
		rowData.add("Total");
		rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("" + roundUp(taxableAmt));
		rowData.add("" + roundUp(cgstSum));
		rowData.add("" + roundUp(sgstSum));
		rowData.add("" + roundUp(igstSum));
		rowData.add("" + roundUp(totalTax));
		rowData.add("" + roundUp(grandTotal));

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		HttpSession session = request.getSession();
		session.setAttribute("exportExcelListNew", exportToExcelList);
		session.setAttribute("excelNameNew", "SaleBillWiseDate");
		session.setAttribute("reportNameNew", "Bill-wise Report");
		session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
		session.setAttribute("mergeUpto1", "$A$1:$L$1");
		session.setAttribute("mergeUpto2", "$A$2:$L$2");

		return saleList;
	}

	@RequestMapping(value = "/getAllCatByAjax", method = RequestMethod.GET)
	public @ResponseBody List<MCategoryList> getAllCatByAjax(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * RestTemplate restTemplate = new RestTemplate(); CategoryListResponse
		 * categoryListResponse; categoryListResponse =
		 * restTemplate.getForObject(Constants.url + "showAllCategory",
		 * CategoryListResponse.class);
		 * 
		 * mCategoryList = categoryListResponse.getmCategoryList();
		 */
		System.out.println("mCategoryList111" + mCategoryList);
		return mCategoryList;
	}

	@RequestMapping(value = "pdf/showSaleReportByDatePdf/{fDate}/{tDate}/{selectedFr}/{routeId}/{selectedCat}/", method = RequestMethod.GET)
	public ModelAndView showSaleReportByDatePdf(@PathVariable String fDate, @PathVariable String tDate,
			@PathVariable String selectedFr, @PathVariable String routeId, @PathVariable String selectedCat,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/pdf/billwisesalesbydatePdf");

		List<SalesReportBillwise> saleList = new ArrayList<>();

		boolean isAllFrSelected = false;
		try {

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (selectedFr.equalsIgnoreCase("-1")) {
				isAllFrSelected = true;
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");
				map.add("catIdList", selectedCat);
				map.add("fromDate", fDate);
				map.add("toDate", tDate);
				ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwise>>() {
				};
				ResponseEntity<List<SalesReportBillwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportBillwiseAllFrSelected", HttpMethod.POST, new HttpEntity<>(map),
						typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				saleListForPdf = saleList;
				System.out.println("sales List Bill Wise " + saleList.toString());

			} else {
				System.out.println("Inside else Few fr Selected ");
				map.add("catIdList", selectedCat);
				map.add("frIdList", selectedFr);
				map.add("fromDate", fDate);
				map.add("toDate", tDate);

				ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwise>>() {
				};
				ResponseEntity<List<SalesReportBillwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportBillwise", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				saleListForPdf = saleList;
				System.out.println("sales List Bill Wise " + saleList.toString());

			}

		} catch (

		Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		model.addObject("fromDate", fDate);

		model.addObject("toDate", tDate);
		model.addObject("FACTORYNAME", Constants.FACTORYNAME);
		model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
		model.addObject("report", saleList);

		return model;
	}

	// report 2
	@RequestMapping(value = "/showSaleReportByFr", method = RequestMethod.GET)
	public ModelAndView showSaleReportByFr(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		com.ats.adminpanel.model.Info view = AccessControll.checkAccess("showSaleReportByFr", "showSaleReportByFr", "1",
				"0", "0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/sales/billwisesalebyfr");
			System.out.println("inside showSaleReportByFr ");
			// Constants.mainAct =2;
			// Constants.subAct =20;
			session = request.getSession();

			System.out.println("session Id in show Page  " + session.getId());

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				String todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}
				List<AllFrIdName> selectedFrListAll = new ArrayList();
				List<Menu> selectedMenuList = new ArrayList<Menu>();

				System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());

				model.addObject("todaysDate", todaysDate);
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);
				CategoryListResponse categoryListResponse;

				categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);

				mCategoryList = categoryListResponse.getmCategoryList();

				model.addObject("mCategoryList", mCategoryList);
			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}
		return model;

	}

	@RequestMapping(value = "/getSaleBillwiseByFr", method = RequestMethod.GET)
	public @ResponseBody List<SalesReportBillwise> getSaleBillwiseByFr(HttpServletRequest request,
			HttpServletResponse response) {

		List<SalesReportBillwise> saleList = new ArrayList<>();
		String fromDate = "";
		String toDate = "";

		try {
			System.out.println("Inside get Sale Bill Wise");
			String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			String routeId = request.getParameter("route_id");

			String selectedCat = request.getParameter("cat_id_list");
			List<String> catIdList = new ArrayList<>();
			selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
			selectedCat = selectedCat.replaceAll("\"", "");
			catIdList = Arrays.asList(selectedCat);

			boolean isAllFrSelected = false;
			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			frList = new ArrayList<>();
			frList = Arrays.asList(selectedFr);

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (frList.contains("-1")) {
				isAllFrSelected = true;
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwise>>() {
				};
				ResponseEntity<List<SalesReportBillwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportBillwiseByFrAllFr", HttpMethod.POST, new HttpEntity<>(map),
						typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				String name = "Sachin";

				HttpSession session = request.getSession();

				System.out.println("session Id  In Ajax Call " + session.getId());
				session.setAttribute("pdfData", saleList);

				session.setAttribute("name", name);
				saleListForPdf = saleList;

				System.out.println("sales List Bill Wise " + saleList.toString());

			} else {
				System.out.println("Inside else Few fr Selected ");

				map.add("frIdList", selectedFr);
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwise>>() {
				};
				ResponseEntity<List<SalesReportBillwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportBillwiseByFr", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				String name = "Sachin";

				HttpSession session = request.getSession();

				System.out.println("session Id  In Ajax Call " + session.getId());
				session.setAttribute("pdfData", saleList);

				session.setAttribute("name", name);
				saleListForPdf = saleList;

				System.out.println("sales List Bill Wise " + saleList.toString());

			}
		} catch (

		Exception e) {
			System.out.println("get sale Report Bill Wise " + e.getMessage());
			e.printStackTrace();

		}

		// exportToExcel
		List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

		ExportToExcel expoExcel = new ExportToExcel();
		List<String> rowData = new ArrayList<String>();

		rowData.add("SR NO");
		rowData.add("Franchise Name");
		rowData.add("Franchise City");
		rowData.add("Franchise GST Number");
		rowData.add("Taxable Amount");
		rowData.add("SGST Amount");
		rowData.add("CGST Amount");
		rowData.add("IGST Amount");
		rowData.add("Total Tax");
		rowData.add("Grand Total");

		int SrNo = 1;

		expoExcel.setRowData(rowData);
		float taxableAmt = 0.0f;
		float cgstSum = 0.0f;
		float sgstSum = 0.0f;
		float igstSum = 0.0f;
		float totalTax = 0.0f;
		float grandTotal = 0.0f;
		exportToExcelList.add(expoExcel);
		for (int i = 0; i < saleList.size(); i++) {
			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();

			rowData.add("" + SrNo);
			taxableAmt = taxableAmt + saleList.get(i).getTaxableAmt();
			cgstSum = cgstSum + saleList.get(i).getCgstSum();
			sgstSum = sgstSum + saleList.get(i).getSgstSum();
			igstSum = igstSum + saleList.get(i).getIgstSum();
			totalTax = totalTax + saleList.get(i).getTotalTax();
			grandTotal = grandTotal + saleList.get(i).getGrandTotal();

			rowData.add(saleList.get(i).getFrName());

			rowData.add(saleList.get(i).getFrCity());
			rowData.add(saleList.get(i).getFrGstNo());
			rowData.add("" + saleList.get(i).getTaxableAmt());
			rowData.add("" + saleList.get(i).getSgstSum());
			rowData.add("" + saleList.get(i).getCgstSum());
			rowData.add("" + saleList.get(i).getIgstSum());
			rowData.add("" + saleList.get(i).getTotalTax());
			rowData.add("" + saleList.get(i).getGrandTotal());

			SrNo = SrNo + 1;

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

		}

		expoExcel = new ExportToExcel();
		rowData = new ArrayList<String>();

		rowData.add("");
		rowData.add("Total");

		rowData.add("");
		rowData.add("");
		rowData.add("" + roundUp(taxableAmt));
		rowData.add("" + roundUp(cgstSum));
		rowData.add("" + roundUp(sgstSum));
		rowData.add("" + roundUp(igstSum));
		rowData.add("" + roundUp(totalTax));
		rowData.add("" + roundUp(grandTotal));

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		HttpSession session = request.getSession();
		session.setAttribute("exportExcelListNew", exportToExcelList);
		session.setAttribute("excelNameNew", "SaleBillWiseFr");
		session.setAttribute("reportNameNew", "Franchise-wise Report");
		session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
		session.setAttribute("mergeUpto1", "$A$1:$J$1");
		session.setAttribute("mergeUpto2", "$A$2:$J$2");

		return saleList;
	}

	@RequestMapping(value = "pdf/showSaleBillwiseByFrPdf/{fromDate}/{toDate}/{selectedFr}/{routeId}/{selectedCat}", method = RequestMethod.GET)
	public ModelAndView showSaleBillwiseByFrPdf(@PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String selectedFr, @PathVariable String routeId, @PathVariable String selectedCat,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("reports/sales/pdf/billwisesalebyfrPdf");

		List<SalesReportBillwise> saleList = new ArrayList<>();
		boolean isAllFrSelected = false;

		try {

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (selectedFr.equalsIgnoreCase("-1")) {
				isAllFrSelected = true;
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwise>>() {
				};
				ResponseEntity<List<SalesReportBillwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportBillwiseByFrAllFr", HttpMethod.POST, new HttpEntity<>(map),
						typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				String name = "Sachin";

				HttpSession session = request.getSession();

				System.out.println("session Id  In Ajax Call " + session.getId());
				session.setAttribute("pdfData", saleList);

				session.setAttribute("name", name);
				saleListForPdf = saleList;

				System.out.println("sales List Bill Wise " + saleList.toString());

			} else {

				System.out.println("Inside else Few fr Selected mgg" + selectedFr + "ui");

				map.add("frIdList", selectedFr);
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwise>>() {
				};
				ResponseEntity<List<SalesReportBillwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportBillwiseByFr", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				String name = "";

				HttpSession session = request.getSession();

				System.out.println("session Id  In Ajax Call " + session.getId());
				session.setAttribute("pdfData", saleList);

				session.setAttribute("name", name);
				saleListForPdf = saleList;

				System.out.println("sales List Bill Wise " + saleList.toString());

			}

			model.addObject("fromDate", fromDate);

			model.addObject("toDate", toDate);
			model.addObject("FACTORYNAME", Constants.FACTORYNAME);
			model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
			model.addObject("report", saleListForPdf);
		} catch (

		Exception e) {
			System.out.println("get sale Report Bill Wise1 " + e.getMessage());
			e.printStackTrace();

		}

		return model;
	}

	@RequestMapping(value = "/getFrListForDatewiseReport", method = RequestMethod.GET)
	@ResponseBody
	public List<AllFrIdName> getFrListForDatewiseReport(HttpServletRequest request, HttpServletResponse response) {

		return allFrIdNameList.getFrIdNamesList();
	}

	// report 3
	@RequestMapping(value = "/showSaleReportGrpByDate", method = RequestMethod.GET)
	public ModelAndView showSaleReportGrpByDate(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		com.ats.adminpanel.model.Info view = AccessControll.checkAccess("showSaleReportGrpByDate",
				"showSaleReportGrpByDate", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/sales/billwisesalesgrpbydate");
			System.out.println("inside showSaleReportGrpByDate ");
			// Constants.mainAct =2;
			// Constants.subAct =20;

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				String todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}
				List<AllFrIdName> selectedFrListAll = new ArrayList();
				List<Menu> selectedMenuList = new ArrayList<Menu>();

				System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());

				model.addObject("todaysDate", todaysDate);
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);

			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}
		return model;

	}

	// ..===================Neha===19/07/2019==============

	@RequestMapping(value = "/getSaleBillwiseGrpByDate", method = RequestMethod.GET)
	public @ResponseBody List<SalesReportDateMonth> getSaleBillwiseGrpByDate(HttpServletRequest request,
			HttpServletResponse response) {

		List<SalesReportDateMonth> saleList = new ArrayList<>();
		String fromDate = "";
		String toDate = "";

		try {
			System.out.println("Inside get Sale Bill Wise");
			String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			String routeId = request.getParameter("route_id");

			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			frList = new ArrayList<>();
			frList = Arrays.asList(selectedFr);

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			System.out.println("Inside else Few fr Selected ");

			map.add("frIdList", selectedFr);
			map.add("fromDate", fromDate);
			map.add("toDate", toDate);

			System.out.println(map);
			ParameterizedTypeReference<List<SalesReportDateMonth>> typeRef = new ParameterizedTypeReference<List<SalesReportDateMonth>>() {
			};
			ResponseEntity<List<SalesReportDateMonth>> responseEntity = restTemplate
					.exchange(Constants.url + "getDatewiseReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			saleList = responseEntity.getBody();

			for (int i = 0; i < saleList.size(); i++) {

				float netGrandTotal = (saleList.get(i).getGrandTotal()
						- (saleList.get(i).getGrnGrandTotal() + saleList.get(i).getGvnGrandTotal()));
				float netTaxableAmt = (saleList.get(i).getTaxableAmt()
						- (saleList.get(i).getGrnTaxableAmt() + saleList.get(i).getGvnTaxableAmt()));

				float netTotalTax = (saleList.get(i).getTotalTax()
						- (saleList.get(i).getGrnTotalTax() + saleList.get(i).getGvnTotalTax()));
				saleList.get(i).setNetGrandTotal(netGrandTotal);
				saleList.get(i).setNetTaxableAmt(netTaxableAmt);
				saleList.get(i).setNetTotalTax(netTotalTax);
			}

			System.out.println("sales List Bill Wise " + saleList.toString());

		} catch (

		Exception e) {
			System.out.println("get sale Report Bill Wise " + e.getMessage());
			e.printStackTrace();

		}

		// exportToExcel

		List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

		ExportToExcel expoExcel = new ExportToExcel();
		List<String> rowData = new ArrayList<String>();

		rowData.add("SR NO");

		rowData.add("Bill Date");
		rowData.add("Taxable Amount");
		rowData.add("Tax Amount");
		rowData.add("Grand Total");

		rowData.add("GRN Taxable Amount");
		rowData.add("GRN Tax Amount");
		rowData.add("GRN Grand Total");

		rowData.add("GVN Taxable Amount");
		rowData.add("GVN Tax Amount");
		rowData.add("GVN Grand Total");

		rowData.add("Net Taxable Amount");
		rowData.add("Net Tax Amount");
		rowData.add("Net Grand Total");

		float totalGrandTotal = 0f;
		float totalTax = 0f;
		float totalTaxableAmt = 0f;

		float totalGrnGrandTotal = 0f;
		float totalGrnTaxableAmt = 0f;
		float totalGrnTax = 0f;

		float totalGvnGrandTotal = 0f;
		float totalGvnTax = 0f;
		float totalGvnTaxableAmt = 0f;

		float totalNetGrandTotal = 0f;
		float totalNetTax = 0f;
		float totalNetTaxableAmt = 0f;

		int srno = 1;
		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);
		for (int i = 0; i < saleList.size(); i++) {

			totalGrnGrandTotal = totalGrnGrandTotal + saleList.get(i).getGrnGrandTotal();
			totalGrnTaxableAmt = totalGrnTaxableAmt + saleList.get(i).getGrnTaxableAmt();
			totalGrnTax = totalGrnTax + saleList.get(i).getGrnTotalTax();

			totalGrandTotal = totalGrandTotal + saleList.get(i).getGrandTotal();
			totalTax = totalTax + saleList.get(i).getTotalTax();
			totalTaxableAmt = totalTaxableAmt + saleList.get(i).getTaxableAmt();

			totalGvnGrandTotal = totalGvnGrandTotal + saleList.get(i).getGvnGrandTotal();
			totalGvnTax = totalGvnTax + saleList.get(i).getGvnTotalTax();
			totalGvnTaxableAmt = totalGvnTaxableAmt + saleList.get(i).getGvnTaxableAmt();

			totalNetGrandTotal = totalNetGrandTotal + saleList.get(i).getNetGrandTotal();
			totalNetTax = totalNetTax + saleList.get(i).getNetTotalTax();
			totalNetTaxableAmt = totalNetTaxableAmt + saleList.get(i).getNetTaxableAmt();

			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();

			rowData.add("" + srno);

			rowData.add(saleList.get(i).getBillDate());
			rowData.add("" + saleList.get(i).getTaxableAmt());
			rowData.add("" + saleList.get(i).getTotalTax());
			rowData.add("" + saleList.get(i).getGrandTotal());

			rowData.add("" + saleList.get(i).getGrnTaxableAmt());
			rowData.add("" + saleList.get(i).getGrnTotalTax());
			rowData.add("" + saleList.get(i).getGrnGrandTotal());

			rowData.add("" + saleList.get(i).getGvnTaxableAmt());
			rowData.add("" + saleList.get(i).getGvnTotalTax());
			rowData.add("" + saleList.get(i).getGvnGrandTotal());

			rowData.add("" + saleList.get(i).getNetTaxableAmt());
			rowData.add("" + saleList.get(i).getNetTotalTax());
			rowData.add("" + saleList.get(i).getNetGrandTotal());

			srno = srno + 1;
			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

		}

		expoExcel = new ExportToExcel();
		rowData = new ArrayList<String>();

		rowData.add("Total");

		rowData.add("");

		rowData.add("" + roundUp(totalTax));
		rowData.add("" + roundUp(totalTaxableAmt));
		rowData.add("" + roundUp(totalGrandTotal));

		rowData.add("" + roundUp(totalGrnTaxableAmt));
		rowData.add("" + roundUp(totalGrnTax));
		rowData.add("" + roundUp(totalGrnGrandTotal));

		rowData.add("" + roundUp(totalGvnTaxableAmt));
		rowData.add("" + roundUp(totalGvnTax));
		rowData.add("" + roundUp(totalGvnGrandTotal));

		rowData.add("" + roundUp(totalNetTaxableAmt));
		rowData.add("" + roundUp(totalNetTax));
		rowData.add("" + roundUp(totalNetGrandTotal));

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		HttpSession session = request.getSession();
		session.setAttribute("exportExcelListNew", exportToExcelList);
		session.setAttribute("excelNameNew", "SaleBillWiseDate");
		session.setAttribute("reportNameNew", "View Billwise Sale Grp By Date");
		session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
		session.setAttribute("mergeUpto1", "$A$1:$G$1");
		session.setAttribute("mergeUpto2", "$A$2:$G$2");

		return saleList;
	}

	/*
	 * @RequestMapping(value = "/getSaleBillwiseGrpByDate", method =
	 * RequestMethod.GET) public @ResponseBody List<SalesReportBillwise>
	 * getSaleBillwiseGrpByDate(HttpServletRequest request, HttpServletResponse
	 * response) {
	 * 
	 * List<SalesReportBillwise> saleList = new ArrayList<>(); String fromDate = "";
	 * String toDate = "";
	 * 
	 * try { System.out.println("Inside get Sale Bill Wise"); String selectedFr =
	 * request.getParameter("fr_id_list"); fromDate =
	 * request.getParameter("fromDate"); toDate = request.getParameter("toDate");
	 * String routeId = request.getParameter("route_id");
	 * 
	 * boolean isAllFrSelected = false; selectedFr = selectedFr.substring(1,
	 * selectedFr.length() - 1); selectedFr = selectedFr.replaceAll("\"", "");
	 * 
	 * frList = new ArrayList<>(); frList = Arrays.asList(selectedFr);
	 * 
	 * if (!routeId.equalsIgnoreCase("0")) {
	 * 
	 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
	 * Object>();
	 * 
	 * RestTemplate restTemplate = new RestTemplate();
	 * 
	 * map.add("routeId", routeId);
	 * 
	 * FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url
	 * + "getFrNameIdByRouteId", map, FrNameIdByRouteIdResponse.class);
	 * 
	 * List<FrNameIdByRouteId> frNameIdByRouteIdList =
	 * frNameId.getFrNameIdByRouteIds();
	 * 
	 * System.out.println("route wise franchisee " +
	 * frNameIdByRouteIdList.toString());
	 * 
	 * StringBuilder sbForRouteFrId = new StringBuilder(); for (int i = 0; i <
	 * frNameIdByRouteIdList.size(); i++) {
	 * 
	 * sbForRouteFrId =
	 * sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() +
	 * ",");
	 * 
	 * }
	 * 
	 * String strFrIdRouteWise = sbForRouteFrId.toString(); selectedFr =
	 * strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
	 * System.out.println("fr Id Route WISE = " + selectedFr);
	 * 
	 * } // end of if
	 * 
	 * if (frList.contains("-1")) { isAllFrSelected = true; }
	 * 
	 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
	 * Object>(); RestTemplate restTemplate = new RestTemplate();
	 * 
	 * if (isAllFrSelected) {
	 * 
	 * System.out.println("Inside If all fr Selected ");
	 * 
	 * map.add("fromDate", fromDate); map.add("toDate", toDate);
	 * 
	 * ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new
	 * ParameterizedTypeReference<List<SalesReportBillwise>>() { };
	 * ResponseEntity<List<SalesReportBillwise>> responseEntity =
	 * restTemplate.exchange( Constants.url + "getSaleReportBillwiseByDateAllFr",
	 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
	 * 
	 * saleList = responseEntity.getBody(); saleListForPdf = new ArrayList<>();
	 * 
	 * saleListForPdf = saleList;
	 * 
	 * System.out.println("sales List Bill Wise " + saleList.toString());
	 * 
	 * } else { System.out.println("Inside else Few fr Selected ");
	 * 
	 * map.add("frIdList", selectedFr); map.add("fromDate", fromDate);
	 * map.add("toDate", toDate);
	 * 
	 * ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new
	 * ParameterizedTypeReference<List<SalesReportBillwise>>() { };
	 * ResponseEntity<List<SalesReportBillwise>> responseEntity =
	 * restTemplate.exchange( Constants.url + "getSaleReportBillwiseByDate",
	 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
	 * 
	 * saleList = responseEntity.getBody(); saleListForPdf = new ArrayList<>();
	 * 
	 * saleListForPdf = saleList;
	 * 
	 * System.out.println("sales List Bill Wise " + saleList.toString());
	 * 
	 * } } catch (
	 * 
	 * Exception e) { System.out.println("get sale Report Bill Wise " +
	 * e.getMessage()); e.printStackTrace();
	 * 
	 * }
	 * 
	 * // exportToExcel List<ExportToExcel> exportToExcelList = new
	 * ArrayList<ExportToExcel>();
	 * 
	 * ExportToExcel expoExcel = new ExportToExcel(); List<String> rowData = new
	 * ArrayList<String>();
	 * 
	 * rowData.add("SR NO");
	 * 
	 * rowData.add("Bill Date"); rowData.add("Taxable Amount");
	 * rowData.add("SGST Amount"); rowData.add("CGST Amount");
	 * rowData.add("IGST Amount"); rowData.add("Grand Total");
	 * 
	 * float taxableAmt = 0.0f; float cgstSum = 0.0f; float sgstSum = 0.0f; float
	 * igstSum = 0.0f;
	 * 
	 * float grandTotal = 0.0f;
	 * 
	 * int srno = 1; expoExcel.setRowData(rowData);
	 * exportToExcelList.add(expoExcel); for (int i = 0; i < saleList.size(); i++) {
	 * 
	 * taxableAmt = taxableAmt + saleList.get(i).getTaxableAmt(); cgstSum = cgstSum
	 * + saleList.get(i).getCgstSum(); sgstSum = sgstSum +
	 * saleList.get(i).getSgstSum(); igstSum = igstSum +
	 * saleList.get(i).getIgstSum();
	 * 
	 * grandTotal = grandTotal + saleList.get(i).getGrandTotal();
	 * 
	 * expoExcel = new ExportToExcel(); rowData = new ArrayList<String>();
	 * 
	 * rowData.add("" + srno);
	 * 
	 * rowData.add(saleList.get(i).getBillDate()); rowData.add("" +
	 * saleList.get(i).getTaxableAmt());
	 * 
	 * rowData.add("" + saleList.get(i).getSgstSum()); rowData.add("" +
	 * saleList.get(i).getCgstSum()); rowData.add("" +
	 * saleList.get(i).getIgstSum()); rowData.add("" +
	 * saleList.get(i).getGrandTotal());
	 * 
	 * srno = srno + 1; expoExcel.setRowData(rowData);
	 * exportToExcelList.add(expoExcel);
	 * 
	 * }
	 * 
	 * expoExcel = new ExportToExcel(); rowData = new ArrayList<String>();
	 * 
	 * rowData.add("Total");
	 * 
	 * rowData.add(""); rowData.add("" + roundUp(taxableAmt)); rowData.add("" +
	 * roundUp(cgstSum)); rowData.add("" + roundUp(sgstSum)); rowData.add("" +
	 * roundUp(igstSum)); rowData.add("" + roundUp(grandTotal));
	 * 
	 * expoExcel.setRowData(rowData); exportToExcelList.add(expoExcel);
	 * 
	 * HttpSession session = request.getSession();
	 * session.setAttribute("exportExcelListNew", exportToExcelList);
	 * session.setAttribute("excelNameNew", "SaleBillWiseDate");
	 * session.setAttribute("reportNameNew", "View Billwise Sale Grp By Date");
	 * session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: "
	 * + toDate + " "); session.setAttribute("mergeUpto1", "$A$1:$G$1");
	 * session.setAttribute("mergeUpto2", "$A$2:$G$2");
	 * 
	 * return saleList; }
	 */

	@RequestMapping(value = "pdf/showSaleBillwiseGrpByDatePdf/{fromDate}/{toDate}/{selectedFr}/{routeId}", method = RequestMethod.GET)
	public ModelAndView showSaleBillwiseGrpByDate(@PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String selectedFr, @PathVariable String routeId, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/pdf/billwisesalesgrpbydatePdf");

		boolean isAllFrSelected = false;

		/*
		 * try { System.out.println("Inside get Sale Bill Wise");
		 * 
		 * frList = new ArrayList<>(); frList = Arrays.asList(selectedFr);
		 * 
		 * if (!routeId.equalsIgnoreCase("0")) {
		 * 
		 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
		 * Object>();
		 * 
		 * RestTemplate restTemplate = new RestTemplate();
		 * 
		 * map.add("routeId", routeId);
		 * 
		 * FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url
		 * + "getFrNameIdByRouteId", map, FrNameIdByRouteIdResponse.class);
		 * 
		 * List<FrNameIdByRouteId> frNameIdByRouteIdList =
		 * frNameId.getFrNameIdByRouteIds();
		 * 
		 * System.out.println("route wise franchisee " +
		 * frNameIdByRouteIdList.toString());
		 * 
		 * StringBuilder sbForRouteFrId = new StringBuilder(); for (int i = 0; i <
		 * frNameIdByRouteIdList.size(); i++) {
		 * 
		 * sbForRouteFrId =
		 * sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() +
		 * ",");
		 * 
		 * }
		 * 
		 * String strFrIdRouteWise = sbForRouteFrId.toString(); selectedFr =
		 * strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
		 * System.out.println("fr Id Route WISE = " + selectedFr);
		 * 
		 * } // end of if
		 * 
		 * if (selectedFr.equalsIgnoreCase("-1")) { isAllFrSelected = true; }
		 * 
		 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
		 * Object>(); RestTemplate restTemplate = new RestTemplate();
		 * 
		 * if (isAllFrSelected) {
		 * 
		 * System.out.println("Inside If all fr Selected ");
		 * 
		 * map.add("fromDate", fromDate); map.add("toDate", toDate);
		 * 
		 * ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new
		 * ParameterizedTypeReference<List<SalesReportBillwise>>() { };
		 * ResponseEntity<List<SalesReportBillwise>> responseEntity =
		 * restTemplate.exchange( Constants.url + "getSaleReportBillwiseByDateAllFr",
		 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
		 * 
		 * saleList = responseEntity.getBody(); saleListForPdf = new ArrayList<>();
		 * 
		 * saleListForPdf = saleList;
		 * 
		 * System.out.println("sales List Bill Wise " + saleList.toString());
		 * 
		 * } else { System.out.println("Inside else Few fr Selected ");
		 * 
		 * map.add("frIdList", selectedFr); map.add("fromDate", fromDate);
		 * map.add("toDate", toDate);
		 * 
		 * ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new
		 * ParameterizedTypeReference<List<SalesReportBillwise>>() { };
		 * ResponseEntity<List<SalesReportBillwise>> responseEntity =
		 * restTemplate.exchange( Constants.url + "getSaleReportBillwiseByDate",
		 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
		 * 
		 * saleList = responseEntity.getBody(); saleListForPdf = new ArrayList<>();
		 * 
		 * saleListForPdf = saleList;
		 * 
		 * System.out.println("sales List Bill Wise " + saleList.toString());
		 * 
		 * }
		 * 
		 * } catch (
		 * 
		 * Exception e) {
		 * 
		 * System.out.println("Exce in show Sale Bill wise by fr PDF " +
		 * e.getMessage()); e.printStackTrace();
		 * 
		 * }
		 */

		List<SalesReportDateMonth> saleList = new ArrayList<>();

		try {
			System.out.println("Inside get Sale Bill Wise");

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			System.out.println("Inside else Few fr Selected ");

			map.add("frIdList", selectedFr);
			map.add("fromDate", fromDate);
			map.add("toDate", toDate);

			ParameterizedTypeReference<List<SalesReportDateMonth>> typeRef = new ParameterizedTypeReference<List<SalesReportDateMonth>>() {
			};
			ResponseEntity<List<SalesReportDateMonth>> responseEntity = restTemplate
					.exchange(Constants.url + "getDatewiseReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			saleList = responseEntity.getBody();

			for (int i = 0; i < saleList.size(); i++) {

				float netGrandTotal = (saleList.get(i).getGrandTotal()
						- (saleList.get(i).getGrnGrandTotal() + saleList.get(i).getGvnGrandTotal()));
				float netTaxableAmt = (saleList.get(i).getTaxableAmt()
						- (saleList.get(i).getGrnTaxableAmt() + saleList.get(i).getGvnTaxableAmt()));

				float netTotalTax = (saleList.get(i).getTotalTax()
						- (saleList.get(i).getGrnTotalTax() + saleList.get(i).getGvnTotalTax()));
				saleList.get(i).setNetGrandTotal(netGrandTotal);
				saleList.get(i).setNetTaxableAmt(netTaxableAmt);
				saleList.get(i).setNetTotalTax(netTotalTax);
			}

			System.out.println("sales List Bill Wise " + saleList.toString());

		} catch (

		Exception e) {
			System.out.println("get sale Report Bill Wise " + e.getMessage());
			e.printStackTrace();

		}

		model.addObject("fromDate", fromDate);

		model.addObject("toDate", toDate);
		model.addObject("FACTORYNAME", Constants.FACTORYNAME);
		model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
		model.addObject("report", saleList);
		return model;
	}

	// getSaleReportBillwiseByMonth

	@RequestMapping(value = "/showSaleReportByMonth", method = RequestMethod.GET)
	public ModelAndView showSaleReportByMonth(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSaleReportByMonth", "showSaleReportByMonth", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/sales/billwisesalebymonth");

			// Constants.mainAct =2;
			// Constants.subAct =20;

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}
				List<AllFrIdName> selectedFrListAll = new ArrayList();
				List<Menu> selectedMenuList = new ArrayList<Menu>();

				System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());

				model.addObject("todaysDate", todaysDate);
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);

			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}
		return model;

	}

	// ..===================Neha===19/07/2019==============

	@RequestMapping(value = "/getSaleBillwiseGrpByMonth", method = RequestMethod.GET)
	public @ResponseBody List<SalesReportDateMonth> getSaleBillwiseGrpByMonth(HttpServletRequest request,
			HttpServletResponse response) {

		List<SalesReportDateMonth> saleList = new ArrayList<>();
		String fromDate = "";
		String toDate = "";

		try {
			System.out.println("Inside get Sale Bill Wise");
			String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			String routeId = request.getParameter("route_id");

			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			frList = new ArrayList<>();
			frList = Arrays.asList(selectedFr);

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			System.out.println("Inside else Few fr Selected ");

			map.add("frIdList", selectedFr);
			map.add("fromDate", fromDate);
			map.add("toDate", toDate);

			ParameterizedTypeReference<List<SalesReportDateMonth>> typeRef = new ParameterizedTypeReference<List<SalesReportDateMonth>>() {
			};
			ResponseEntity<List<SalesReportDateMonth>> responseEntity = restTemplate
					.exchange(Constants.url + "getMonthwiseReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			saleList = responseEntity.getBody();

			for (int i = 0; i < saleList.size(); i++) {

				float netGrandTotal = (saleList.get(i).getGrandTotal()
						- (saleList.get(i).getGrnGrandTotal() + saleList.get(i).getGvnGrandTotal()));
				float netTaxableAmt = (saleList.get(i).getTaxableAmt()
						- (saleList.get(i).getGrnTaxableAmt() + saleList.get(i).getGvnTaxableAmt()));

				float netTotalTax = (saleList.get(i).getTotalTax()
						- (saleList.get(i).getGrnTotalTax() + saleList.get(i).getGvnTotalTax()));
				saleList.get(i).setNetGrandTotal(netGrandTotal);
				saleList.get(i).setNetTaxableAmt(netTaxableAmt);
				saleList.get(i).setNetTotalTax(netTotalTax);
			}

			System.out.println("sales List Bill Wise " + saleList.toString());

		} catch (

		Exception e) {
			System.out.println("get sale Report Bill Wise " + e.getMessage());
			e.printStackTrace();

		}

		// exportToExcel

		List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

		ExportToExcel expoExcel = new ExportToExcel();
		List<String> rowData = new ArrayList<String>();

		rowData.add("SR NO");

		rowData.add("Month");
		rowData.add("Taxable Amount");
		rowData.add("Tax Amount");
		rowData.add("Grand Total");

		rowData.add("GRN Taxable Amount");
		rowData.add("GRN Tax Amount");
		rowData.add("GRN Grand Total");

		rowData.add("GVN Taxable Amount");
		rowData.add("GVN Tax Amount");
		rowData.add("GVN Grand Total");

		rowData.add("Net Taxable Amount");
		rowData.add("Net Tax Amount");
		rowData.add("Net Grand Total");

		float totalGrandTotal = 0f;
		float totalTax = 0f;
		float totalTaxableAmt = 0f;

		float totalGrnGrandTotal = 0f;
		float totalGrnTaxableAmt = 0f;
		float totalGrnTax = 0f;

		float totalGvnGrandTotal = 0f;
		float totalGvnTax = 0f;
		float totalGvnTaxableAmt = 0f;

		float totalNetGrandTotal = 0f;
		float totalNetTax = 0f;
		float totalNetTaxableAmt = 0f;

		int srno = 1;
		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);
		for (int i = 0; i < saleList.size(); i++) {

			totalGrnGrandTotal = totalGrnGrandTotal + saleList.get(i).getGrnGrandTotal();
			totalGrnTaxableAmt = totalGrnTaxableAmt + saleList.get(i).getGrnTaxableAmt();
			totalGrnTax = totalGrnTax + saleList.get(i).getGrnTotalTax();

			totalGrandTotal = totalGrandTotal + saleList.get(i).getGrandTotal();
			totalTax = totalTax + saleList.get(i).getTotalTax();
			totalTaxableAmt = totalTaxableAmt + saleList.get(i).getTaxableAmt();

			totalGvnGrandTotal = totalGvnGrandTotal + saleList.get(i).getGvnGrandTotal();
			totalGvnTax = totalGvnTax + saleList.get(i).getGvnTotalTax();
			totalGvnTaxableAmt = totalGvnTaxableAmt + saleList.get(i).getGvnTaxableAmt();

			totalNetGrandTotal = totalNetGrandTotal + saleList.get(i).getNetGrandTotal();
			totalNetTax = totalNetTax + saleList.get(i).getNetTotalTax();
			totalNetTaxableAmt = totalNetTaxableAmt + saleList.get(i).getNetTaxableAmt();

			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();

			rowData.add("" + srno);

			rowData.add(saleList.get(i).getMonth());
			rowData.add("" + saleList.get(i).getTaxableAmt());
			rowData.add("" + saleList.get(i).getTotalTax());
			rowData.add("" + saleList.get(i).getGrandTotal());

			rowData.add("" + saleList.get(i).getGrnTaxableAmt());
			rowData.add("" + saleList.get(i).getGrnTotalTax());
			rowData.add("" + saleList.get(i).getGrnGrandTotal());

			rowData.add("" + saleList.get(i).getGvnTaxableAmt());
			rowData.add("" + saleList.get(i).getGvnTotalTax());
			rowData.add("" + saleList.get(i).getGvnGrandTotal());

			rowData.add("" + saleList.get(i).getNetTaxableAmt());
			rowData.add("" + saleList.get(i).getNetTotalTax());
			rowData.add("" + saleList.get(i).getNetGrandTotal());

			srno = srno + 1;
			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

		}

		expoExcel = new ExportToExcel();
		rowData = new ArrayList<String>();

		rowData.add("Total");

		rowData.add("");

		rowData.add("" + roundUp(totalTax));
		rowData.add("" + roundUp(totalTaxableAmt));
		rowData.add("" + roundUp(totalGrandTotal));

		rowData.add("" + roundUp(totalGrnTaxableAmt));
		rowData.add("" + roundUp(totalGrnTax));
		rowData.add("" + roundUp(totalGrnGrandTotal));

		rowData.add("" + roundUp(totalGvnTaxableAmt));
		rowData.add("" + roundUp(totalGvnTax));
		rowData.add("" + roundUp(totalGvnGrandTotal));

		rowData.add("" + roundUp(totalNetTaxableAmt));
		rowData.add("" + roundUp(totalNetTax));
		rowData.add("" + roundUp(totalNetGrandTotal));

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		HttpSession session = request.getSession();
		session.setAttribute("exportExcelListNew", exportToExcelList);
		session.setAttribute("excelNameNew", "SaleBillWiseDate");
		session.setAttribute("reportNameNew", "View Billwise Sale Grp By Date");
		session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
		session.setAttribute("mergeUpto1", "$A$1:$G$1");
		session.setAttribute("mergeUpto2", "$A$2:$G$2");

		return saleList;
	}

	/*
	 * @RequestMapping(value = "/getSaleBillwiseGrpByMonth", method =
	 * RequestMethod.GET) public @ResponseBody List<SalesReportBillwise>
	 * getSaleBillwiseGrpByMonth(HttpServletRequest request, HttpServletResponse
	 * response) {
	 * 
	 * List<SalesReportBillwise> saleList = new ArrayList<>(); String fromDate = "";
	 * String toDate = ""; try { System.out.println("Inside get Sale Bill Wise");
	 * String selectedFr = request.getParameter("fr_id_list"); fromDate =
	 * request.getParameter("fromDate"); toDate = request.getParameter("toDate");
	 * String routeId = request.getParameter("route_id");
	 * 
	 * boolean isAllFrSelected = false; selectedFr = selectedFr.substring(1,
	 * selectedFr.length() - 1); selectedFr = selectedFr.replaceAll("\"", "");
	 * 
	 * if (!routeId.equalsIgnoreCase("0")) {
	 * 
	 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
	 * Object>();
	 * 
	 * RestTemplate restTemplate = new RestTemplate();
	 * 
	 * map.add("routeId", routeId);
	 * 
	 * FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url
	 * + "getFrNameIdByRouteId", map, FrNameIdByRouteIdResponse.class);
	 * 
	 * List<FrNameIdByRouteId> frNameIdByRouteIdList =
	 * frNameId.getFrNameIdByRouteIds();
	 * 
	 * System.out.println("route wise franchisee " +
	 * frNameIdByRouteIdList.toString());
	 * 
	 * StringBuilder sbForRouteFrId = new StringBuilder(); for (int i = 0; i <
	 * frNameIdByRouteIdList.size(); i++) {
	 * 
	 * sbForRouteFrId =
	 * sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() +
	 * ",");
	 * 
	 * }
	 * 
	 * String strFrIdRouteWise = sbForRouteFrId.toString(); selectedFr =
	 * strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
	 * System.out.println("fr Id Route WISE = " + selectedFr);
	 * 
	 * } // end of if
	 * 
	 * if (frList.contains("-1")) { isAllFrSelected = true; }
	 * 
	 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
	 * Object>(); RestTemplate restTemplate = new RestTemplate();
	 * 
	 * if (isAllFrSelected) {
	 * 
	 * System.out.println("Inside If all fr Selected ");
	 * 
	 * map.add("fromDate", fromDate); map.add("toDate", toDate);
	 * 
	 * ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new
	 * ParameterizedTypeReference<List<SalesReportBillwise>>() { };
	 * ResponseEntity<List<SalesReportBillwise>> responseEntity =
	 * restTemplate.exchange( Constants.url + "getSaleReportBillwiseByMonthAllFr",
	 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
	 * 
	 * saleList = responseEntity.getBody(); saleListForPdf = new ArrayList<>();
	 * 
	 * saleListForPdf = saleList;
	 * 
	 * System.out.println("sales List Bill Wise " + saleList.toString());
	 * 
	 * } else { System.out.println("Inside else Few fr Selected ");
	 * 
	 * map.add("frIdList", selectedFr); map.add("fromDate", fromDate);
	 * map.add("toDate", toDate);
	 * 
	 * ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new
	 * ParameterizedTypeReference<List<SalesReportBillwise>>() { };
	 * ResponseEntity<List<SalesReportBillwise>> responseEntity =
	 * restTemplate.exchange( Constants.url + "getSaleReportBillwiseByMonth",
	 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
	 * 
	 * saleList = responseEntity.getBody(); saleListForPdf = new ArrayList<>();
	 * 
	 * saleListForPdf = saleList;
	 * 
	 * System.out.println("sales List Bill Wise " + saleList.toString());
	 * 
	 * } } catch (
	 * 
	 * Exception e) { System.out.println("get sale Report Bill Wise " +
	 * e.getMessage()); e.printStackTrace();
	 * 
	 * }
	 * 
	 * // exportToExcel List<ExportToExcel> exportToExcelList = new
	 * ArrayList<ExportToExcel>();
	 * 
	 * ExportToExcel expoExcel = new ExportToExcel(); List<String> rowData = new
	 * ArrayList<String>();
	 * 
	 * rowData.add("Sr No"); rowData.add("Month"); rowData.add("Franchisee Name");
	 * rowData.add("Franchisee City"); rowData.add("Franchisee Gst No");
	 * rowData.add("SGST sum"); rowData.add("CGST sum"); rowData.add("IGST sum");
	 * rowData.add("Total Tax"); rowData.add("Taxable Amt");
	 * rowData.add("Grand Total");
	 * 
	 * float taxableAmt = 0.0f; float cgstSum = 0.0f; float sgstSum = 0.0f; float
	 * igstSum = 0.0f; float grandTotal = 0.0f; float totalTax = 0.0f;
	 * 
	 * expoExcel.setRowData(rowData); exportToExcelList.add(expoExcel); for (int i =
	 * 0; i < saleList.size(); i++) { expoExcel = new ExportToExcel(); rowData = new
	 * ArrayList<String>();
	 * 
	 * taxableAmt = taxableAmt + saleList.get(i).getTaxableAmt(); cgstSum = cgstSum
	 * + saleList.get(i).getCgstSum(); sgstSum = sgstSum +
	 * saleList.get(i).getSgstSum(); igstSum = igstSum +
	 * saleList.get(i).getIgstSum(); totalTax = totalTax +
	 * saleList.get(i).getTotalTax(); grandTotal = grandTotal +
	 * saleList.get(i).getGrandTotal();
	 * 
	 * rowData.add("" + (i + 1)); rowData.add("" + saleList.get(i).getMonth());
	 * rowData.add(saleList.get(i).getFrName());
	 * rowData.add(saleList.get(i).getFrCity());
	 * rowData.add(saleList.get(i).getFrGstNo()); rowData.add("" +
	 * saleList.get(i).getSgstSum()); rowData.add("" +
	 * saleList.get(i).getCgstSum()); rowData.add("" +
	 * saleList.get(i).getIgstSum()); rowData.add("" +
	 * saleList.get(i).getTotalTax()); rowData.add("" +
	 * saleList.get(i).getTaxableAmt()); rowData.add("" +
	 * saleList.get(i).getGrandTotal());
	 * 
	 * expoExcel.setRowData(rowData); exportToExcelList.add(expoExcel);
	 * 
	 * }
	 * 
	 * expoExcel = new ExportToExcel(); rowData = new ArrayList<String>();
	 * rowData.add(""); rowData.add("Total"); rowData.add(""); rowData.add("");
	 * rowData.add(""); rowData.add("" + roundUp(sgstSum)); rowData.add("" +
	 * roundUp(cgstSum)); rowData.add("" + roundUp(igstSum)); rowData.add("" +
	 * roundUp(totalTax)); rowData.add("" + roundUp(taxableAmt)); rowData.add("" +
	 * roundUp(grandTotal));
	 * 
	 * expoExcel.setRowData(rowData); exportToExcelList.add(expoExcel);
	 * 
	 * HttpSession session = request.getSession();
	 * session.setAttribute("exportExcelListNew", exportToExcelList);
	 * session.setAttribute("excelNameNew", "SalesReportBillwise");
	 * session.setAttribute("reportNameNew", "Month-wise Report");
	 * session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: "
	 * + toDate + " "); session.setAttribute("mergeUpto1", "$A$1:$L$1");
	 * session.setAttribute("mergeUpto2", "$A$2:$L$2");
	 * 
	 * return saleList;
	 * 
	 * }
	 */

	@RequestMapping(value = "pdf/showSaleBillwiseGrpByMonthPdf/{fromDate}/{toDate}/{selectedFr}/{routeId}", method = RequestMethod.GET)
	public ModelAndView showSaleBillwiseGrpByMonthPdf(@PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String selectedFr, @PathVariable String routeId, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/pdf/billwisesalesgrpbymonthPdf");

		boolean isAllFrSelected = false;

		List<SalesReportDateMonth> saleList = new ArrayList<>();

		try {
			System.out.println("Inside get Sale Bill Wise");

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			System.out.println("Inside else Few fr Selected ");

			map.add("frIdList", selectedFr);
			map.add("fromDate", fromDate);
			map.add("toDate", toDate);

			ParameterizedTypeReference<List<SalesReportDateMonth>> typeRef = new ParameterizedTypeReference<List<SalesReportDateMonth>>() {
			};
			ResponseEntity<List<SalesReportDateMonth>> responseEntity = restTemplate
					.exchange(Constants.url + "getMonthwiseReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			saleList = responseEntity.getBody();

			for (int i = 0; i < saleList.size(); i++) {

				float netGrandTotal = (saleList.get(i).getGrandTotal()
						- (saleList.get(i).getGrnGrandTotal() + saleList.get(i).getGvnGrandTotal()));
				float netTaxableAmt = (saleList.get(i).getTaxableAmt()
						- (saleList.get(i).getGrnTaxableAmt() + saleList.get(i).getGvnTaxableAmt()));

				float netTotalTax = (saleList.get(i).getTotalTax()
						- (saleList.get(i).getGrnTotalTax() + saleList.get(i).getGvnTotalTax()));
				saleList.get(i).setNetGrandTotal(netGrandTotal);
				saleList.get(i).setNetTaxableAmt(netTaxableAmt);
				saleList.get(i).setNetTotalTax(netTotalTax);
			}

			System.out.println("sales List Bill Wise " + saleList.toString());

		} catch (

		Exception e) {
			System.out.println("get sale Report Bill Wise " + e.getMessage());
			e.printStackTrace();

		}

		model.addObject("fromDate", fromDate);

		model.addObject("toDate", toDate);
		model.addObject("FACTORYNAME", Constants.FACTORYNAME);
		model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
		model.addObject("report", saleList);
		return model;
	}
	/*
	 * @RequestMapping(value =
	 * "pdf/showSaleBillwiseGrpByMonthPdf/{fromDate}/{toDate}/{selectedFr}/{routeId}",
	 * method = RequestMethod.GET) public ModelAndView
	 * showSaleBillwiseGrpByMonthPdf(@PathVariable String fromDate, @PathVariable
	 * String toDate,
	 * 
	 * @PathVariable String selectedFr, @PathVariable String routeId,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * ModelAndView model = new
	 * ModelAndView("reports/sales/pdf/billwisesalesgrpbymonthPdf");
	 * List<SalesReportBillwise> saleList = new ArrayList<>(); boolean
	 * isAllFrSelected = false;
	 * 
	 * try { System.out.println("Inside get Sale Bill Wise");
	 * 
	 * if (!routeId.equalsIgnoreCase("0")) {
	 * 
	 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
	 * Object>();
	 * 
	 * RestTemplate restTemplate = new RestTemplate();
	 * 
	 * map.add("routeId", routeId);
	 * 
	 * FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url
	 * + "getFrNameIdByRouteId", map, FrNameIdByRouteIdResponse.class);
	 * 
	 * List<FrNameIdByRouteId> frNameIdByRouteIdList =
	 * frNameId.getFrNameIdByRouteIds();
	 * 
	 * System.out.println("route wise franchisee " +
	 * frNameIdByRouteIdList.toString());
	 * 
	 * StringBuilder sbForRouteFrId = new StringBuilder(); for (int i = 0; i <
	 * frNameIdByRouteIdList.size(); i++) {
	 * 
	 * sbForRouteFrId =
	 * sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() +
	 * ",");
	 * 
	 * }
	 * 
	 * String strFrIdRouteWise = sbForRouteFrId.toString(); selectedFr =
	 * strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
	 * System.out.println("fr Id Route WISE = " + selectedFr);
	 * 
	 * } // end of if
	 * 
	 * if (selectedFr.equalsIgnoreCase("-1")) { isAllFrSelected = true; }
	 * 
	 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
	 * Object>(); RestTemplate restTemplate = new RestTemplate();
	 * 
	 * if (isAllFrSelected) {
	 * 
	 * System.out.println("Inside If all fr Selected ");
	 * 
	 * map.add("fromDate", fromDate); map.add("toDate", toDate);
	 * 
	 * ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new
	 * ParameterizedTypeReference<List<SalesReportBillwise>>() { };
	 * ResponseEntity<List<SalesReportBillwise>> responseEntity =
	 * restTemplate.exchange( Constants.url + "getSaleReportBillwiseByMonthAllFr",
	 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
	 * 
	 * saleList = responseEntity.getBody(); saleListForPdf = new ArrayList<>();
	 * 
	 * saleListForPdf = saleList;
	 * 
	 * System.out.println("sales List Bill Wise " + saleList.toString());
	 * 
	 * } else { System.out.println("Inside else Few fr Selected ");
	 * 
	 * map.add("frIdList", selectedFr); map.add("fromDate", fromDate);
	 * map.add("toDate", toDate);
	 * 
	 * ParameterizedTypeReference<List<SalesReportBillwise>> typeRef = new
	 * ParameterizedTypeReference<List<SalesReportBillwise>>() { };
	 * ResponseEntity<List<SalesReportBillwise>> responseEntity =
	 * restTemplate.exchange( Constants.url + "getSaleReportBillwiseByMonth",
	 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
	 * 
	 * saleList = responseEntity.getBody(); saleListForPdf = new ArrayList<>();
	 * 
	 * saleListForPdf = saleList;
	 * 
	 * System.out.println("sales List Bill Wise " + saleList.toString());
	 * 
	 * }
	 * 
	 * } catch (
	 * 
	 * Exception e) { System.out.println("Exce in show Sale Bill wise by fr PDF " +
	 * e.getMessage()); e.printStackTrace(); } model.addObject("fromDate",
	 * fromDate); model.addObject("FACTORYNAME", Constants.FACTORYNAME);
	 * model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
	 * model.addObject("toDate", toDate);
	 * 
	 * model.addObject("report", saleListForPdf); return model; }
	 */

	// *******************************************************************//
	// Royalty Sale

	@RequestMapping(value = "/showSaleRoyaltyByCat", method = RequestMethod.GET)
	public ModelAndView showSaleRoyaltyByCat(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSaleRoyaltyByCat", "showSaleRoyaltyByCat", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/sales/salesroyaltybycat");

			// Constants.mainAct =2;
			// Constants.subAct =20;

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}
				List<AllFrIdName> selectedFrListAll = new ArrayList();
				List<Menu> selectedMenuList = new ArrayList<Menu>();

				System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());

				model.addObject("todaysDate", todaysDate);
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);
				model.addObject("royPer", getRoyPer());

			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}

		return model;

	}

	@RequestMapping(value = "/getSaleRoyaltyByCat", method = RequestMethod.GET)
	public @ResponseBody RoyaltyListBean getSaleRoyaltyByCat(HttpServletRequest request, HttpServletResponse response) {

		List<SalesReportBillwise> saleList = new ArrayList<>();
		List<SalesReportRoyalty> royaltyList = new ArrayList<>();
		RoyaltyListBean royaltyBean = new RoyaltyListBean();

		try {
			System.out.println("Inside get Sale Bill Wise");
			String selectedFr = request.getParameter("fr_id_list");
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String routeId = request.getParameter("route_id");

			boolean isAllFrSelected = false;
			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			frList = new ArrayList<>();
			frList = Arrays.asList(selectedFr);

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (frList.contains("-1")) {
				isAllFrSelected = true;
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");

				// web serviece: getSalesReportRoyaltyAllFr

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<SalesReportRoyalty>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() {
				};
				ResponseEntity<List<SalesReportRoyalty>> responseEntity = restTemplate.exchange(
						Constants.url + "getSalesReportRoyaltyAllFr", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				royaltyList = responseEntity.getBody();
				royaltyListForPdf = new ArrayList<>();

			} else {
				System.out.println("Inside else Few fr Selected ");

				map.add("frIdList", selectedFr);
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<SalesReportRoyalty>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() {
				};
				ResponseEntity<List<SalesReportRoyalty>> responseEntity = restTemplate.exchange(
						Constants.url + "getSalesReportRoyalty", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				royaltyList = responseEntity.getBody();
				royaltyListForPdf = new ArrayList<>();
			}

			royaltyListForPdf = royaltyList;

			System.out.println("royaltyList List Bill Wise " + saleList.toString());

			CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
					CategoryListResponse.class);
			List<MCategoryList> categoryList;
			categoryList = categoryListResponse.getmCategoryList();
			// allFrIdNameList = new AllFrIdNameList();
			System.out.println("Category list  " + categoryList);

			royaltyBean.setCategoryList(categoryList);
			royaltyBean.setSalesReportRoyalty(royaltyList);
			staticRoyaltyBean = royaltyBean;

		} catch (

		Exception e) {
			System.out.println("get sale Report royaltyList by cat " + e.getMessage());
			e.printStackTrace();

		}
		// exportToExcel
		List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

		ExportToExcel expoExcel = new ExportToExcel();
		List<String> rowData = new ArrayList<String>();

		rowData.add("Sr.No.");
		rowData.add("Category Name");
		rowData.add("Item Name");
		rowData.add("Sale Qty");
		rowData.add("Sale Value");

		rowData.add("GRN Qty");
		rowData.add("GRN Value");
		rowData.add("GVN Qty");
		rowData.add("GVN Value");

		rowData.add("Net Qty");
		rowData.add("Net Value");
		rowData.add("Royalty %");
		rowData.add("Royalty Amt");
		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);
		if (!royaltyBean.getSalesReportRoyalty().isEmpty()) {
			for (int i = 0; i < royaltyList.size(); i++) {
				int index = 1;
				index = index + i;
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();

				rowData.add("" + index);
				rowData.add("" + royaltyList.get(i).getCat_name());

				rowData.add("" + royaltyList.get(i).getItem_name());

				rowData.add("" + roundUp(royaltyList.get(i).gettBillQty()));
				rowData.add("" + roundUp(royaltyList.get(i).gettBillTaxableAmt()));

				rowData.add("" + roundUp(royaltyList.get(i).gettGrnQty()));

				rowData.add("" + roundUp(royaltyList.get(i).gettGrnTaxableAmt()));
				rowData.add("" + roundUp(royaltyList.get(i).gettGvnQty()));
				rowData.add("" + roundUp(royaltyList.get(i).gettGvnTaxableAmt()));

				float netQty = royaltyList.get(i).gettBillQty()
						- (royaltyList.get(i).gettGrnQty() + royaltyList.get(i).gettGvnQty());

				float netValue = royaltyList.get(i).gettBillTaxableAmt()
						- (royaltyList.get(i).gettGrnTaxableAmt() + royaltyList.get(i).gettGvnTaxableAmt());
				float royPer = getRoyPer();
				float rAmt = netValue * royPer / 100;

				rowData.add("" + roundUp(netQty));
				rowData.add("" + roundUp(netValue));
				rowData.add("" + roundUp(royPer));
				rowData.add("" + roundUp(rAmt));

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}
		}

		HttpSession session = request.getSession();
		session.setAttribute("exportExcelList", exportToExcelList);
		session.setAttribute("excelName", "RoyaltyByCatList");

		return royaltyBean;

	}

	public static float roundUp(float d) {
		return BigDecimal.valueOf(d).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	@RequestMapping(value = "pdf/showSaleRoyaltyByCatPdf/{fromDate}/{toDate}/{selectedFr}/{routeId}", method = RequestMethod.GET)
	public ModelAndView showSaleBil(@PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String selectedFr, @PathVariable String routeId, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/pdf/salesroyaltybycatPdf");
		List<SalesReportBillwise> saleList = new ArrayList<>();
		List<SalesReportRoyalty> royaltyList = new ArrayList<>();
		RoyaltyListBean royaltyBean = new RoyaltyListBean();
		boolean isAllFrSelected = false;

		try {
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			RestTemplate restTemplate = new RestTemplate();

			float royPer = getRoyPer();

			if (!routeId.equalsIgnoreCase("0")) {

				map = new LinkedMultiValueMap<String, Object>();
				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (selectedFr.equalsIgnoreCase("-1")) {
				isAllFrSelected = true;
			}

			map = new LinkedMultiValueMap<String, Object>();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");
				// getSalesReportRoyaltyAllFr
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<SalesReportRoyalty>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() {
				};
				ResponseEntity<List<SalesReportRoyalty>> responseEntity = restTemplate.exchange(
						Constants.url + "getSalesReportRoyaltyAllFr", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				royaltyList = responseEntity.getBody();

			} else {
				System.out.println("Inside else Few fr Selected ");

				map.add("frIdList", selectedFr);
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<SalesReportRoyalty>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() {
				};
				ResponseEntity<List<SalesReportRoyalty>> responseEntity = restTemplate.exchange(
						Constants.url + "getSalesReportRoyalty", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				royaltyList = responseEntity.getBody();

			}
			royaltyListForPdf = new ArrayList<>();

			royaltyListForPdf = royaltyList;

			System.out.println("royaltyList List Bill Wise " + saleList.toString());

			CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
					CategoryListResponse.class);
			List<MCategoryList> categoryList;
			categoryList = categoryListResponse.getmCategoryList();
			// allFrIdNameList = new AllFrIdNameList();
			System.out.println("Category list  " + categoryList);

			royaltyBean.setCategoryList(categoryList);
			royaltyBean.setSalesReportRoyalty(royaltyList);
			staticRoyaltyBean = royaltyBean;
			model.addObject("royPer", royPer);

		} catch (

		Exception e) {
			System.out.println("get sale Report royaltyList by cat " + e.getMessage());
			e.printStackTrace();

		}

		model.addObject("fromDate", fromDate);

		model.addObject("toDate", toDate);
		model.addObject("FACTORYNAME", Constants.FACTORYNAME);
		model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
		model.addObject("catList", staticRoyaltyBean.getCategoryList());
		model.addObject("royaltyList", staticRoyaltyBean.getSalesReportRoyalty());
		return model;
	}

	// royalty FR wise

	@RequestMapping(value = "/showSaleRoyaltyByFr", method = RequestMethod.GET)
	public ModelAndView showSaleRoyaltyByFr(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSaleRoyaltyByFr", "showSaleRoyaltyByFr", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/sales/salesroyaltybyfr");

			// Constants.mainAct =2;
			// Constants.subAct =20;

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}
				List<AllFrIdName> selectedFrListAll = new ArrayList();
				List<Menu> selectedMenuList = new ArrayList<Menu>();

				System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());
				model.addObject("royPer", getRoyPer());
				model.addObject("todaysDate", todaysDate);
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);

			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}
		return model;

	}

	@RequestMapping(value = "/getSaleRoyaltyByFr", method = RequestMethod.GET)
	public @ResponseBody List<SalesReportRoyaltyFr> getSaleRoyaltyByFr(HttpServletRequest request,
			HttpServletResponse response) {
		String fromDate = "";
		String toDate = "";

		try {
			System.out.println("Inside get Sale royalty by fr");
			String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			String routeId = request.getParameter("route_id");

			boolean isAllFrSelected = false;
			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			frList = new ArrayList<>();
			frList = Arrays.asList(selectedFr);

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (frList.contains("-1")) {
				isAllFrSelected = true;
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected /getSaleRoyaltyByFr :getSalesReportRoyaltyFrAllFr");
				// Web Service :getSalesReportRoyaltyFrAllFr
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<SalesReportRoyaltyFr>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyaltyFr>>() {
				};
				ResponseEntity<List<SalesReportRoyaltyFr>> responseEntity = restTemplate.exchange(
						Constants.url + "getSalesReportRoyaltyFrAllFr", HttpMethod.POST, new HttpEntity<>(map),
						typeRef);

				royaltyFrList = new ArrayList<>();
				royaltyFrList = responseEntity.getBody();

			} else {
				System.out.println("Inside else Few fr Selected /getSaleRoyaltyByFr :getSalesReportRoyaltyFr");

				map.add("frIdList", selectedFr);
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<SalesReportRoyaltyFr>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyaltyFr>>() {
				};
				ResponseEntity<List<SalesReportRoyaltyFr>> responseEntity = restTemplate.exchange(
						Constants.url + "getSalesReportRoyaltyFr", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				royaltyFrList = new ArrayList<>();
				royaltyFrList = responseEntity.getBody();
			}
			// royaltyListForPdf=new ArrayList<>();

			// royaltyListForPdf=royaltyList;
			staticRoyaltyFrList = new ArrayList<>();

			staticRoyaltyFrList = royaltyFrList;
			System.out.println("royalty List List royaltyFr List " + royaltyFrList.toString());

			// allFrIdNameList = new AllFrIdNameList();

		} catch (

		Exception e) {
			System.out.println("get sale Report royaltyList by cat " + e.getMessage());
			e.printStackTrace();

		}

		float saleValue = 0.0f;
		float grnValue = 0.0f;
		float gvnValue = 0.0f;
		float netValueTotal = 0.0f;
		float royaltyAmt = 0.0f;

		// exportToExcel
		List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

		ExportToExcel expoExcel = new ExportToExcel();
		List<String> rowData = new ArrayList<String>();
		float royPer = getRoyPer();
		rowData.add("Sr No");
		rowData.add("Franchise Name");
		rowData.add("Franchise City");
		rowData.add("Sales Value");
		rowData.add("GRN Value");
		rowData.add("GVN Value");
		rowData.add("Net Value");
		rowData.add("Royalty %");
		rowData.add("Royalty Amount");

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);
		for (int i = 0; i < royaltyFrList.size(); i++) {
			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();

			rowData.add("" + i + 1);
			rowData.add(royaltyFrList.get(i).getFrName());
			rowData.add(royaltyFrList.get(i).getFrCity());
			rowData.add("" + royaltyFrList.get(i).gettBillTaxableAmt());
			rowData.add("" + royaltyFrList.get(i).gettGrnTaxableAmt());
			rowData.add("" + royaltyFrList.get(i).gettGvnTaxableAmt());
			rowData.add("" + (royaltyFrList.get(i).gettBillTaxableAmt()
					- (royaltyFrList.get(i).gettGvnTaxableAmt() + royaltyFrList.get(i).gettGrnTaxableAmt())));
			rowData.add("" + royPer);
			float netValue = royaltyFrList.get(i).gettBillTaxableAmt()
					- (royaltyFrList.get(i).gettGrnTaxableAmt() + royaltyFrList.get(i).gettGvnTaxableAmt());

			float rAmt = netValue * royPer / 100;
			rowData.add("" + rAmt);

			saleValue = saleValue + royaltyFrList.get(i).gettBillTaxableAmt();
			grnValue = grnValue + royaltyFrList.get(i).gettGrnTaxableAmt();
			gvnValue = gvnValue + royaltyFrList.get(i).gettGvnTaxableAmt();
			netValueTotal = netValueTotal + netValue;
			royaltyAmt = royaltyAmt + rAmt;

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

		}

		expoExcel = new ExportToExcel();
		rowData = new ArrayList<String>();

		rowData.add("");
		rowData.add("Total");
		rowData.add("");
		rowData.add("" + roundUp(saleValue));
		rowData.add("" + roundUp(grnValue));
		rowData.add("" + roundUp(gvnValue));
		rowData.add("" + roundUp(netValueTotal));
		rowData.add("");
		rowData.add("" + roundUp(royaltyAmt));

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		HttpSession session = request.getSession();
		session.setAttribute("exportExcelListNew", exportToExcelList);
		session.setAttribute("excelNameNew", "RoyaltyFrList");
		session.setAttribute("reportNameNew", "Bill-wise Report");
		session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
		session.setAttribute("mergeUpto1", "$A$1:$L$1");
		session.setAttribute("mergeUpto2", "$A$2:$L$2");

		return royaltyFrList;

	}

	// royalty fr pdf is not done

	// done pdf
	@RequestMapping(value = "pdf/showSaleRoyaltyByFrPdf/{fromDate}/{toDate}/{selectedFr}/{routeId}", method = RequestMethod.GET)
	public ModelAndView showSaleRoyaltyByFrPdf(@PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String selectedFr, @PathVariable String routeId, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/pdf/salesroyaltybyfrPdf");
		boolean isAllFrSelected = false;

		try {
			System.out.println("Inside get Sale royalty by fr");

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (selectedFr.equalsIgnoreCase("-1")) {
				isAllFrSelected = true;
			}
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<SalesReportRoyaltyFr>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyaltyFr>>() {
				};
				ResponseEntity<List<SalesReportRoyaltyFr>> responseEntity = restTemplate.exchange(
						Constants.url + "getSalesReportRoyaltyFrAllFr", HttpMethod.POST, new HttpEntity<>(map),
						typeRef);

				royaltyFrList = new ArrayList<>();
				royaltyFrList = responseEntity.getBody();

				// getSalesReportRoyaltyFrAllFr

			} else {
				System.out.println("Inside else Few fr Selected ");

				map.add("frIdList", selectedFr);
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<SalesReportRoyaltyFr>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyaltyFr>>() {
				};
				ResponseEntity<List<SalesReportRoyaltyFr>> responseEntity = restTemplate.exchange(
						Constants.url + "getSalesReportRoyaltyFr", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				royaltyFrList = new ArrayList<>();
				royaltyFrList = responseEntity.getBody();

			}
			// royaltyListForPdf=new ArrayList<>();

			// royaltyListForPdf=royaltyList;
			staticRoyaltyFrList = new ArrayList<>();

			staticRoyaltyFrList = royaltyFrList;
			System.out.println("royalty List List royaltyFr List " + royaltyFrList.toString());

			// allFrIdNameList = new AllFrIdNameList();

		} catch (

		Exception e) {
			System.out.println("get sale Report royaltyList by Fr " + e.getMessage());
			e.printStackTrace();

		}
		model.addObject("royPer",

				getRoyPer());

		model.addObject("fromDate", fromDate);
		model.addObject("FACTORYNAME", Constants.FACTORYNAME);
		model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
		model.addObject("toDate", toDate);

		model.addObject("report", staticRoyaltyFrList);

		return model;
	}

	// report no 8
	@RequestMapping(value = "/showSaleReportItemwise", method = RequestMethod.GET)
	public ModelAndView showSaleReportItemwise(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSaleReportItemwise", "showSaleReportItemwise", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/sales/salesreportitemwise");

			// Constants.mainAct =2;
			// Constants.subAct =20;

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}
				List<AllFrIdName> selectedFrListAll = new ArrayList();
				List<Menu> selectedMenuList = new ArrayList<Menu>();
				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);

				mCategoryList = categoryListResponse.getmCategoryList();
				System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());
				model.addObject("catList", mCategoryList);

				model.addObject("todaysDate", todaysDate);
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);

			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}

		return model;

	}

	@RequestMapping(value = "/getSaleReportItemwise", method = RequestMethod.GET)
	public @ResponseBody List<SalesReportItemwise> getSaleReportItemwise(HttpServletRequest request,
			HttpServletResponse response) {

		List<SalesReportItemwise> saleList = new ArrayList<>();
		String fromDate = "";
		String toDate = "";

		try {
			System.out.println("Inside get Sale Item  Wise");
			String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			String routeId = request.getParameter("route_id");
			int catId = Integer.parseInt(request.getParameter("catId"));

			boolean isAllFrSelected = false;
			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			frList = new ArrayList<>();
			frList = Arrays.asList(selectedFr);

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (frList.contains("-1")) {
				isAllFrSelected = true;

				// No frIds for Filter:it us based on Item Selection :
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catId", catId);
			} else {
				System.out.println("Inside else Few fr Selected ");

				map.add("frIdList", selectedFr);
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catId", catId);
				ParameterizedTypeReference<List<SalesReportItemwise>> typeRef = new ParameterizedTypeReference<List<SalesReportItemwise>>() {
				};
				ResponseEntity<List<SalesReportItemwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportItemwise", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				staticSaleListItemWise = saleList;
				// saleListForPdf=saleList;

				System.out.println("sales List Bill Wise " + saleList.toString());

			}
		} catch (Exception e) {
			System.out.println("get sale Report Bill Wise " + e.getMessage());
			e.printStackTrace();

		}

		// exportToExcel
		List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

		ExportToExcel expoExcel = new ExportToExcel();
		List<String> rowData = new ArrayList<String>();
		rowData.add("SR.NO.");
		rowData.add("ITEM NAME");
		rowData.add("HSN CODE");
		rowData.add("BILL QTY SUM");
		rowData.add("TAXABLE AMT");
		rowData.add("ITEM TAX1");
		rowData.add("ITEM TAX2");
		rowData.add("ITEM TAX3");
		rowData.add("SGST SUM");
		rowData.add("CGST SUM");
		rowData.add("IGST SUM");
		rowData.add("TOTAL GST");

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		int billQtyAmt = 0;
		float taxableAmt = 0.0f;
		float tax1 = 0.0f;
		float tax2 = 0.0f;
		float tax3 = 0.0f;
		float cgstSum = 0.0f;
		float sgstSum = 0.0f;
		float igstSum = 0.0f;

		float grandTotal = 0.0f;
		for (int i = 0; i < saleList.size(); i++) {
			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();
			rowData.add((i + 1) + "");

			billQtyAmt = billQtyAmt + saleList.get(i).getBillQtySum();
			taxableAmt = taxableAmt + saleList.get(i).getTaxableAmtSum();
			cgstSum = cgstSum + saleList.get(i).getCgstRsSum();
			sgstSum = sgstSum + saleList.get(i).getSgstRsSum();
			igstSum = igstSum + saleList.get(i).getIgstRsSum();
			tax1 = tax1 + saleList.get(i).getItemTax1();
			tax2 = tax2 + saleList.get(i).getItemTax2();
			tax3 = tax3 + saleList.get(i).getItemTax3();
			grandTotal = grandTotal + saleList.get(i).getSgstRsSum() + saleList.get(i).getCgstRsSum()
					+ saleList.get(i).getIgstRsSum();

			rowData.add("" + saleList.get(i).getItemName());
			rowData.add("" + saleList.get(i).getItemHsncd());
			rowData.add("" + saleList.get(i).getBillQtySum());
			rowData.add("" + roundUp(saleList.get(i).getTaxableAmtSum()));
			rowData.add("" + roundUp(saleList.get(i).getItemTax1()));
			rowData.add("" + roundUp(saleList.get(i).getItemTax2()));
			rowData.add("" + roundUp(saleList.get(i).getItemTax3()));

			rowData.add("" + roundUp(saleList.get(i).getSgstRsSum()));
			rowData.add("" + roundUp(saleList.get(i).getCgstRsSum()));

			rowData.add("" + roundUp(saleList.get(i).getIgstRsSum()));
			float totalGst = saleList.get(i).getSgstRsSum() + saleList.get(i).getCgstRsSum()
					+ saleList.get(i).getIgstRsSum();
			rowData.add("" + roundUp(totalGst));

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

		}

		expoExcel = new ExportToExcel();
		rowData = new ArrayList<String>();

		rowData.add("");
		rowData.add("Total");

		rowData.add("");
		rowData.add("" + billQtyAmt);
		rowData.add("" + roundUp(taxableAmt));
		rowData.add("" + roundUp(tax1));
		rowData.add("" + roundUp(tax2));
		rowData.add("" + roundUp(tax3));

		rowData.add("" + roundUp(sgstSum));
		rowData.add("" + roundUp(cgstSum));
		rowData.add("" + roundUp(igstSum));

		rowData.add("" + roundUp(grandTotal));

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		HttpSession session = request.getSession();
		session.setAttribute("exportExcelListNew", exportToExcelList);
		session.setAttribute("excelNameNew", "SaleReportItemWise");
		session.setAttribute("reportNameNew", "Item-wise Sale Report");
		session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
		session.setAttribute("mergeUpto1", "$A$1:$L$1");
		session.setAttribute("mergeUpto2", "$A$2:$L$2");
		return saleList;

	}

	// pdf for r8 to be done
	// pdf for r8
	@RequestMapping(value = "pdf/showSaleReportItemwisePdf/{fromDate}/{toDate}/{selectedFr}/{routeId}/{catId}", method = RequestMethod.GET)
	public ModelAndView showSaleReportItemwisePdf(@PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String selectedFr, @PathVariable String routeId, @PathVariable int catId,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/pdf/salesreportitemwisePdf");

		List<SalesReportItemwise> saleList = new ArrayList<>();
		boolean isAllFrSelected = false;

		try {
			System.out.println("Inside get Sale Item  Wise");

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (selectedFr.equalsIgnoreCase("-1")) {
				isAllFrSelected = true;
			}
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catId", catId);
			} else {
				System.out.println("Inside else Few fr Selected ");

				map.add("frIdList", selectedFr);
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("catId", catId);
				ParameterizedTypeReference<List<SalesReportItemwise>> typeRef = new ParameterizedTypeReference<List<SalesReportItemwise>>() {
				};
				ResponseEntity<List<SalesReportItemwise>> responseEntity = restTemplate.exchange(
						Constants.url + "getSaleReportItemwise", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				saleList = responseEntity.getBody();
				saleListForPdf = new ArrayList<>();
				staticSaleListItemWise = saleList;
				// saleListForPdf=saleList;

				System.out.println("sales List Bill Wise " + saleList.toString());

			}
		} catch (Exception e) {
			System.out.println("get sale Report Bill Wise " + e.getMessage());
			e.printStackTrace();

		}

		model.addObject("fromDate", fromDate);

		model.addObject("toDate", toDate);
		model.addObject("FACTORYNAME", Constants.FACTORYNAME);
		model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
		model.addObject("report", staticSaleListItemWise);

		return model;
	}

	// report 7
	@RequestMapping(value = "/showSaleReportBillwiseAllFr", method = RequestMethod.GET)
	public ModelAndView showSaleReportBillwiseAllFr(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSaleReportBillwiseAllFr", "showSaleReportBillwiseAllFr", "1", "0",
				"0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/sales/salesreportbillallfr");

			// Constants.mainAct =2;
			// Constants.subAct =20;

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}
				List<AllFrIdName> selectedFrListAll = new ArrayList();
				List<Menu> selectedMenuList = new ArrayList<Menu>();

				System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());

				model.addObject("todaysDate", todaysDate);
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);

			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}

		return model;

	}

	@RequestMapping(value = "/getSaleReportBillwiseAllFr", method = RequestMethod.GET)
	public @ResponseBody List<SalesReportBillwiseAllFr> getSaleReportBillwiseAllFr(HttpServletRequest request,
			HttpServletResponse response) {
		String fromDate = "";
		String toDate = "";
		List<SalesReportBillwiseAllFr> saleList = new ArrayList<>();

		try {
			System.out.println("Inside get Sale Item  Wise");
			// String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			String routeId = request.getParameter("route_id");

			String selectedFr;

			// boolean isAllFrSelected = false;
			// selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			// selectedFr = selectedFr.replaceAll("\"", "");

			// frList = new ArrayList<>();
			// frList = Arrays.asList(selectedFr);

			/*
			 * if (!routeId.equalsIgnoreCase("0")) {
			 * 
			 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
			 * Object>();
			 * 
			 * RestTemplate restTemplate = new RestTemplate();
			 * 
			 * map.add("routeId", routeId);
			 * 
			 * FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url
			 * + "getFrNameIdByRouteId", map, FrNameIdByRouteIdResponse.class);
			 * 
			 * List<FrNameIdByRouteId> frNameIdByRouteIdList =
			 * frNameId.getFrNameIdByRouteIds();
			 * 
			 * System.out.println("route wise franchisee " +
			 * frNameIdByRouteIdList.toString());
			 * 
			 * StringBuilder sbForRouteFrId = new StringBuilder(); for (int i = 0; i <
			 * frNameIdByRouteIdList.size(); i++) {
			 * 
			 * sbForRouteFrId =
			 * sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() +
			 * ",");
			 * 
			 * }
			 * 
			 * String strFrIdRouteWise = sbForRouteFrId.toString(); selectedFr =
			 * strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
			 * System.out.println("fr Id Route WISE = " + selectedFr);
			 * 
			 * } // end of if
			 * 
			 * if (frList.contains("-1")) { //isAllFrSelected = true; }
			 */

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			map.add("fromDate", fromDate);
			map.add("toDate", toDate);

			ParameterizedTypeReference<List<SalesReportBillwiseAllFr>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwiseAllFr>>() {
			};
			ResponseEntity<List<SalesReportBillwiseAllFr>> responseEntity = restTemplate.exchange(
					Constants.url + "getSaleReportBillwiseAllFr", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			saleList = responseEntity.getBody();
			staticSaleByAllFr = new ArrayList<>();
			staticSaleByAllFr = saleList;
			// saleListForPdf=saleList;

			System.out.println("sales List Bill Wise all fr  " + saleList.toString());

		} catch (Exception e) {
			System.out.println("get sale Report Bill Wise all Fr " + e.getMessage());
			e.printStackTrace();

		}

		// exportToExcel
		List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

		ExportToExcel expoExcel = new ExportToExcel();
		List<String> rowData = new ArrayList<String>();
		rowData.add("Sr.");
		//rowData.add("Bill No");
		rowData.add("Invoice No");
		rowData.add("Bill Date");

		rowData.add("Franchisee");
		rowData.add("City");
		rowData.add("Fr Gst No");
		//rowData.add("Item Name");
		rowData.add("Item Hsn");
		rowData.add("SGST %");
		rowData.add("CGST %");
		rowData.add("IGST %");
		rowData.add("SGST sum");
		rowData.add("CGST sum");
		rowData.add("IGST sum");
		rowData.add("Taxable Amt");

		float taxableAmt = 0.0f;
		float cgstSum = 0.0f;
		float tax1 = 0.0f;
		float tax2 = 0.0f;
		float tax3 = 0.0f;
		float sgstSum = 0.0f;
		float igstSum = 0.0f;

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);
		for (int i = 0; i < saleList.size(); i++) {
			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();
			rowData.add("" + (i + 1));
			//rowData.add("" + saleList.get(i).getBillNo());
			rowData.add(saleList.get(i).getInvoiceNo());
			rowData.add(saleList.get(i).getBillDate());

			rowData.add(saleList.get(i).getFrName());

			rowData.add(saleList.get(i).getFrCity());
			rowData.add(saleList.get(i).getFrGstNo());
			//rowData.add("" + saleList.get(i).getItemName());
			rowData.add("" + saleList.get(i).getItemHsncd());
			rowData.add("" + saleList.get(i).getItemTax1());

			rowData.add("" + saleList.get(i).getItemTax2());
			rowData.add("" + saleList.get(i).getItemTax3());
			rowData.add("" + saleList.get(i).getSgstRsSum());
			rowData.add("" + saleList.get(i).getCgstRsSum());

			rowData.add("" + saleList.get(i).getIgstRsSum());
			rowData.add("" + saleList.get(i).getTaxableAmtSum());

			taxableAmt = taxableAmt + saleList.get(i).getTaxableAmtSum();
			cgstSum = cgstSum + saleList.get(i).getCgstRsSum();
			sgstSum = sgstSum + saleList.get(i).getSgstRsSum();
			igstSum = igstSum + saleList.get(i).getIgstRsSum();
			tax1 = tax1 + saleList.get(i).getItemTax1();
			tax2 = tax2 + saleList.get(i).getItemTax2();
			tax3 = tax3 + saleList.get(i).getItemTax3();

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

		}

		expoExcel = new ExportToExcel();
		rowData = new ArrayList<String>();

		//rowData.add("");
		rowData.add("");
		rowData.add("Total");
		//rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("");
		rowData.add("" + roundUp(tax1));
		rowData.add("" + roundUp(tax2));
		rowData.add("" + roundUp(tax3));
		rowData.add("" + roundUp(sgstSum));
		rowData.add("" + roundUp(cgstSum));
		rowData.add("" + roundUp(igstSum));
		rowData.add("" + roundUp(taxableAmt));

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		HttpSession session = request.getSession();
		session.setAttribute("exportExcelListNew", exportToExcelList);
		session.setAttribute("excelNameNew", "SalesReportBillwiseAllFr");
		session.setAttribute("reportNameNew", "Bill-wise & HSN Code-wise Report");
		session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
		session.setAttribute("mergeUpto1", "$A$1:$P$1");
		session.setAttribute("mergeUpto2", "$A$2:$P$2");

		return saleList;
	}
	// pdf to be done

	// pdf report 7
	@RequestMapping(value = "pdf/showSaleReportBillwiseAllFrPdf/{fromDate}/{toDate}", method = RequestMethod.GET)
	public ModelAndView showSaleReportBillwiseAllFrPdf(@PathVariable String fromDate, @PathVariable String toDate,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/pdf/salereportbillallfrPdf");

		List<SalesReportBillwiseAllFr> saleList = new ArrayList<>();

		try {
			System.out.println("Inside get Sale Item  Wise");

			String selectedFr;

			// boolean isAllFrSelected = false;
			// selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			// selectedFr = selectedFr.replaceAll("\"", "");

			// frList = new ArrayList<>();
			// frList = Arrays.asList(selectedFr);

			/*
			 * if (!routeId.equalsIgnoreCase("0")) {
			 * 
			 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
			 * Object>();
			 * 
			 * RestTemplate restTemplate = new RestTemplate();
			 * 
			 * map.add("routeId", routeId);
			 * 
			 * FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url
			 * + "getFrNameIdByRouteId", map, FrNameIdByRouteIdResponse.class);
			 * 
			 * List<FrNameIdByRouteId> frNameIdByRouteIdList =
			 * frNameId.getFrNameIdByRouteIds();
			 * 
			 * System.out.println("route wise franchisee " +
			 * frNameIdByRouteIdList.toString());
			 * 
			 * StringBuilder sbForRouteFrId = new StringBuilder(); for (int i = 0; i <
			 * frNameIdByRouteIdList.size(); i++) {
			 * 
			 * sbForRouteFrId =
			 * sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() +
			 * ",");
			 * 
			 * }
			 * 
			 * String strFrIdRouteWise = sbForRouteFrId.toString(); selectedFr =
			 * strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
			 * System.out.println("fr Id Route WISE = " + selectedFr);
			 * 
			 * } // end of if
			 * 
			 * if (frList.contains("-1")) { //isAllFrSelected = true; }
			 */

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			System.out.println("Inside else Few fr Selected ");

			// map.add("frIdList", selectedFr);
			map.add("fromDate", fromDate);
			map.add("toDate", toDate);

			ParameterizedTypeReference<List<SalesReportBillwiseAllFr>> typeRef = new ParameterizedTypeReference<List<SalesReportBillwiseAllFr>>() {
			};
			ResponseEntity<List<SalesReportBillwiseAllFr>> responseEntity = restTemplate.exchange(
					Constants.url + "getSaleReportBillwiseAllFr", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			saleList = responseEntity.getBody();
			staticSaleByAllFr = new ArrayList<>();
			staticSaleByAllFr = saleList;
			// saleListForPdf=saleList;

			System.out.println("sales List Bill Wise all fr  " + saleList.toString());

		} catch (Exception e) {
			System.out.println("get sale Report Bill Wise all Fr " + e.getMessage());
			e.printStackTrace();

		}
		model.addObject("fromDate", fromDate);

		model.addObject("toDate", toDate);
		model.addObject("FACTORYNAME", Constants.FACTORYNAME);
		model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
		model.addObject("report", staticSaleByAllFr);

		return model;
	}

	// report no 10 conso by category report

	@RequestMapping(value = "/showSaleReportRoyConsoByCat", method = RequestMethod.GET)
	public ModelAndView showSaleReportRoyConsoByCat(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSaleReportRoyConsoByCat", "showSaleReportRoyConsoByCat", "1", "0",
				"0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/sales/salesconsbycat");

			// Constants.mainAct =2;
			// Constants.subAct =20;

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}

				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<MCategoryList> categoryList;
				categoryList = categoryListResponse.getmCategoryList();

				System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());

				model.addObject("catList", categoryList);

				model.addObject("todaysDate", todaysDate);
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);
				model.addObject("royPer", getRoyPer());

			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}
		return model;

	}

	// ----------------------------Show Dispatch Item
	// List-----------------------------
	@RequestMapping(value = "/showDispatchItemReport", method = RequestMethod.GET)
	public ModelAndView showDispatchItemReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/dispatchReport");

		// Constants.mainAct =2;
		// Constants.subAct =20;

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);

			RestTemplate restTemplate = new RestTemplate();

			// get Routes

			AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
					AllRoutesListResponse.class);

			List<Route> routeList = new ArrayList<Route>();

			routeList = allRouteListResponse.getRoute();

			// end get Routes

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}

			CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
					CategoryListResponse.class);
			List<MCategoryList> categoryList;
			categoryList = categoryListResponse.getmCategoryList();

			System.out.println(" Fr " + allFrIdNameList.getFrIdNamesList());

			model.addObject("catList", categoryList);

			model.addObject("todaysDate", todaysDate);
			model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

			model.addObject("routeList", routeList);

		} catch (Exception e) {

			System.out.println("Exc in show sales report bill wise  " + e.getMessage());
			e.printStackTrace();
		}

		return model;

	}

	ArrayList<Menu> selectedMenuList = null;

	// ---------------------------------------------------------------------------------------------------------
	@RequestMapping(value = "/showPDispatchItemReport", method = RequestMethod.GET)
	public ModelAndView showPDispatchItemReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showPDispatchItemReport", "showPDispatchItemReport", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("reports/pDispatchReport");

			// Constants.mainAct =2;
			// Constants.subAct =20;

			try {
				ZoneId z = ZoneId.of("Asia/Calcutta");

				LocalDate date = LocalDate.now(z);
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
				todaysDate = date.format(formatters);

				RestTemplate restTemplate = new RestTemplate();

				// get Routes

				AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
						AllRoutesListResponse.class);

				List<Route> routeList = new ArrayList<Route>();

				routeList = allRouteListResponse.getRoute();

				// end get Routes

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}

				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<MCategoryList> categoryList;
				categoryList = categoryListResponse.getmCategoryList();

				System.out.println(" Fra " + allFrIdNameList.getFrIdNamesList());
				List<Menu> menuList = null;
				try {
					AllMenuResponse allMenuResponse = restTemplate.getForObject(Constants.url + "getAllMenu",
							AllMenuResponse.class);

					menuList = allMenuResponse.getMenuConfigurationPage();

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}
				selectedMenuList = new ArrayList<Menu>();

				for (int i = 0; i < menuList.size(); i++) {

					if (menuList.get(i).getMainCatId() != 5) {
						selectedMenuList.add(menuList.get(i));
					}

				}
				model.addObject("menuList", selectedMenuList);
				model.addObject("catList", categoryList);

				model.addObject("todaysDate", todaysDate);
				model.addObject("frListRes", allFrIdNameList.getFrIdNamesList());

				model.addObject("routeList", routeList);

			} catch (Exception e) {

				System.out.println("Exc in show sales report bill wise  " + e.getMessage());
				e.printStackTrace();
			}
		}
		return model;

	}

	@RequestMapping(value = "/getAllMenusForDisp", method = RequestMethod.GET)
	public @ResponseBody List<Menu> getAllMenusForDisp() {

		return selectedMenuList;
	}

	// ---------------------------------------------------------------------------------------------------------
	@RequestMapping(value = "/getDispatchReportByRoute", method = RequestMethod.GET)
	public @ResponseBody DispatchReportList getDispatchReportByRoute(HttpServletRequest request,
			HttpServletResponse response) {

		List<DispatchReport> dispatchReportList = new ArrayList<DispatchReport>();
		DispatchReportList dispatchReports = new DispatchReportList();
		try {
			System.out.println("Inside get Dispatch Report");
			String billDate = request.getParameter("bill_date");
			String routeId = request.getParameter("route_id");
			String selectedCat = request.getParameter("cat_id_list");

			boolean isAllCatSelected = false;
			String selectedFr = null;

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			} else {
				selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
				selectedCat = selectedCat.replaceAll("\"", "");
				System.out.println("selectedCat" + selectedCat.toString());
			}
			List<String> catList = new ArrayList<>();
			catList = Arrays.asList(selectedCat);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			RestTemplate restTemplate = new RestTemplate();

			map.add("routeId", routeId);

			FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId", map,
					FrNameIdByRouteIdResponse.class);

			List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

			System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

			StringBuilder sbForRouteFrId = new StringBuilder();
			for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

				sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

			}

			String strFrIdRouteWise = sbForRouteFrId.toString();
			selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
			System.out.println("fr Id Route WISE = " + selectedFr);

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			}

			map = new LinkedMultiValueMap<String, Object>();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}

			if (isAllCatSelected) {
				map = new LinkedMultiValueMap<String, Object>();

				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<MCategoryList> categoryList = categoryListResponse.getmCategoryList();
				// List<Integer> cateList=new ArrayList<>();
				StringBuilder cateList = new StringBuilder();

				for (MCategoryList mCategoryList : categoryList) {
					// cateList.add(mCategoryList.getCatId());
					cateList = cateList.append(mCategoryList.getCatId().toString() + ",");
				}

				String catlist = cateList.toString();
				selectedCat = catlist.substring(0, catlist.length() - 1);
				System.out.println("cateList" + selectedCat.toString());
				System.out.println("selectedFr" + selectedFr.toString());
				System.out.println("billDate" + billDate.toString());

				map.add("categories", selectedCat);
				map.add("billDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<DispatchReport>> typeRef = new ParameterizedTypeReference<List<DispatchReport>>() {
				};

				ResponseEntity<List<DispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.out.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);
				System.out.println("Items:" + responseEntity1.toString());
				SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				ArrayList<SubCategory> subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));
				System.out.println("subCatAList:" + subCatAList.toString());
				dispatchReports.setDispatchReportList(dispatchReportList);
				dispatchReports.setFrList(frNameIdByRouteIdList);
				dispatchReports.setItemList(responseEntity1.getBody());
				dispatchReports.setSubCatList(subCatAList);

			} else {
				System.out.println("selectedCat" + selectedCat.toString());
				System.out.println("selectedFr" + selectedFr.toString());

				map.add("categories", selectedCat);
				map.add("billDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<DispatchReport>> typeRef = new ParameterizedTypeReference<List<DispatchReport>>() {
				};

				ResponseEntity<List<DispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);
				System.out.println("Items:" + responseEntity.toString());

				dispatchReportList = responseEntity.getBody();
				System.out.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catId", selectedCat);
				ParameterizedTypeReference<List<SubCategory>> typeRef2 = new ParameterizedTypeReference<List<SubCategory>>() {
				};

				ResponseEntity<List<SubCategory>> responseEntity2 = restTemplate
						.exchange(Constants.url + "getSubCatList", HttpMethod.POST, new HttpEntity<>(map), typeRef2);

				dispatchReports.setDispatchReportList(dispatchReportList);
				dispatchReports.setFrList(frNameIdByRouteIdList);
				dispatchReports.setItemList(responseEntity1.getBody());
				dispatchReports.setSubCatList(responseEntity2.getBody());
			}

		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}

		return dispatchReports;

	}

	List<PDispatchReport> pDispatchReportList = null;

	@RequestMapping(value = "/getPDispatchReportByRoute", method = RequestMethod.GET)
	public @ResponseBody PDispatchReportList getPDispatchReportByRoute(HttpServletRequest request,
			HttpServletResponse response) {

		List<PDispatchReport> dispatchReportList = new ArrayList<PDispatchReport>();
		PDispatchReportList dispatchReports = new PDispatchReportList();
		try {
			System.out.println("Inside get Dispatch Report");
			String billDate = request.getParameter("bill_date");
			String routeId = request.getParameter("route_id");
			String selectedCat = request.getParameter("cat_id_list");

			boolean isAllCatSelected = false;
			String selectedFr = null;

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			} else {
				selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
				selectedCat = selectedCat.replaceAll("\"", "");
				System.out.println("selectedCat" + selectedCat.toString());
			}
			List<String> catList = new ArrayList<>();
			catList = Arrays.asList(selectedCat);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			RestTemplate restTemplate = new RestTemplate();

			map.add("routeId", routeId);

			FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId", map,
					FrNameIdByRouteIdResponse.class);

			List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

			System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

			StringBuilder sbForRouteFrId = new StringBuilder();
			for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

				sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

			}

			String strFrIdRouteWise = sbForRouteFrId.toString();
			selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
			System.out.println("fr Id Route WISE = " + selectedFr);

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			}

			map = new LinkedMultiValueMap<String, Object>();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}

			if (isAllCatSelected) {
				map = new LinkedMultiValueMap<String, Object>();

				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<MCategoryList> categoryList = categoryListResponse.getmCategoryList();
				// List<Integer> cateList=new ArrayList<>();
				StringBuilder cateList = new StringBuilder();

				for (MCategoryList mCategoryList : categoryList) {
					// cateList.add(mCategoryList.getCatId());
					cateList = cateList.append(mCategoryList.getCatId().toString() + ",");
				}

				String catlist = cateList.toString();
				selectedCat = catlist.substring(0, catlist.length() - 1);
				System.out.println("cateList" + selectedCat.toString());
				System.out.println("selectedFr" + selectedFr.toString());
				System.out.println("productionDate" + billDate.toString());

				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				pDispatchReportList = responseEntity.getBody();
				System.out.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);
				System.out.println("Items:" + responseEntity1.toString());
				SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				ArrayList<SubCategory> subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));
				System.out.println("subCatAList:" + subCatAList.toString());
				dispatchReports.setDispatchReportList(dispatchReportList);
				dispatchReports.setFrList(frNameIdByRouteIdList);
				dispatchReports.setItemList(responseEntity1.getBody());
				dispatchReports.setSubCatList(subCatAList);

			} else {
				System.out.println("selectedCat" + selectedCat.toString());
				map = new LinkedMultiValueMap<String, Object>();
				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);
				System.out.println("Items:" + responseEntity.toString());

				dispatchReportList = responseEntity.getBody();
				pDispatchReportList = responseEntity.getBody();
				System.out.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catId", selectedCat);
				ParameterizedTypeReference<List<SubCategory>> typeRef2 = new ParameterizedTypeReference<List<SubCategory>>() {
				};

				ResponseEntity<List<SubCategory>> responseEntity2 = restTemplate.exchange(
						Constants.url + "getSubCatListForDis", HttpMethod.POST, new HttpEntity<>(map), typeRef2);// new
																													// change
																													// getSubCatList
																													// to
																													// getSubCatListForDis

				dispatchReports.setDispatchReportList(dispatchReportList);
				dispatchReports.setFrList(frNameIdByRouteIdList);
				dispatchReports.setItemList(responseEntity1.getBody());
				dispatchReports.setSubCatList(responseEntity2.getBody());
			}

		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}

		return dispatchReports;

	}

	@RequestMapping(value = "/submitEditedQty", method = RequestMethod.POST)
	public String submitEditedQty(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("reports/pDispatchReport");
		try {
			System.err.println("#####################pDispatchReportList##########################"
					+ pDispatchReportList.toString());

			if (!pDispatchReportList.isEmpty()) {
				List<POrder> orderList = new ArrayList<>();

				for (int i = 0; i < pDispatchReportList.size(); i++) {
					POrder order = null;
					try {
						int editedQty = Integer.parseInt(request.getParameter("itemQty"
								+ pDispatchReportList.get(i).getFrId() + "" + pDispatchReportList.get(i).getItemId()
								+ "" + pDispatchReportList.get(i).getOrderId()));
						order = new POrder();
						order.setOrderId(pDispatchReportList.get(i).getOrderId());
						order.setEditQty(editedQty);
					} catch (Exception e) {
						System.err.println("orderQty:" + pDispatchReportList.get(i).getFrId() + ""
								+ pDispatchReportList.get(i).getItemId() + ""
								+ pDispatchReportList.get(i).getOrderId());
						e.printStackTrace();
					}
					if (order != null) {
						orderList.add(order);
					}
				}
				RestTemplate restTemplate = new RestTemplate();
				List<POrder> orderLists = restTemplate.postForObject(Constants.url + "updateEditedQty", orderList,
						List.class);
				System.err.println("orderLists" + orderLists.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/showPDispatchItemReport";
	}

	@RequestMapping(value = "/getFranchiseByRouteMul", method = RequestMethod.GET)
	public @ResponseBody List<FranchiseForDispatch> getFranchiseByRouteMul(
			@RequestParam(value = "routeId", required = true) String routeId) {
		RestTemplate restTemplate = new RestTemplate();

		List<FranchiseForDispatch> frNameIdByRouteIdList = null;
		try {

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("routeId", routeId);

			FranchiseForDispatch[] frNameId = restTemplate
					.postForObject(Constants.url + "getFranchiseForDispatchRouteID", map, FranchiseForDispatch[].class);

			frNameIdByRouteIdList = new ArrayList<>(Arrays.asList(frNameId));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return frNameIdByRouteIdList;
	}

	@RequestMapping(value = "/getFranchiseByRoute", method = RequestMethod.GET)
	public @ResponseBody List<FranchiseForDispatch> getFranchiseByRoute(
			@RequestParam(value = "routeId", required = true) int routeId) {
		RestTemplate restTemplate = new RestTemplate();

		List<FranchiseForDispatch> frNameIdByRouteIdList = null;
		try {
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			// System.err.println(routeId);
			map.add("routeId", routeId);

			FranchiseForDispatch[] frNameId = restTemplate.postForObject(Constants.url + "getFranchiseForDispatch", map,
					FranchiseForDispatch[].class);

			frNameIdByRouteIdList = new ArrayList<>(Arrays.asList(frNameId));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return frNameIdByRouteIdList;
	}

	@RequestMapping(value = "/getFranchises", method = RequestMethod.GET)
	public @ResponseBody List<FranchiseeList> getFranchises() {
		RestTemplate restTemplate = new RestTemplate();

		List<FranchiseeList> franchiseeList = null;
		try {
			FranchiseeAndMenuList franchiseeAndMenuList = restTemplate
					.getForObject(Constants.url + "getFranchiseeAndMenu", FranchiseeAndMenuList.class);
			franchiseeList = franchiseeAndMenuList.getAllFranchisee();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return franchiseeList;
	}

	@RequestMapping(value = "pdf/getDispatchReportPdf/{billDate}/{routeId}/{selectedCat}", method = RequestMethod.GET)
	public ModelAndView getDispatchReportPdf(@PathVariable String billDate, @PathVariable String routeId,
			@PathVariable String selectedCat, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("reports/sales/dispatchReportPdf");
		RestTemplate restTemplate = new RestTemplate();

		List<DispatchReport> dispatchReportList = new ArrayList<DispatchReport>();
		DispatchReportList dispatchReports = new DispatchReportList();
		try {

			System.out.println("Inside get Dispatch Report");
			// String billDate = request.getParameter("bill_date");
			// String routeId = request.getParameter("route_id");
			// String selectedCat=request.getParameter("cat_id_list");
			AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
					AllRoutesListResponse.class);

			List<Route> routeList = new ArrayList<Route>();

			routeList = allRouteListResponse.getRoute();
			String routeName = "def";
			for (int i = 0; i < routeList.size(); i++) {

				if (routeList.get(i).getRouteId() == Integer.parseInt(routeId)) {
					routeName = routeList.get(i).getRouteName();
					break;

				}
			}
			boolean isAllCatSelected = false;
			String selectedFr = null;

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			} else {
				// selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
				// selectedCat = selectedCat.replaceAll("\"", "");
				// System.out.println("selectedCat"+selectedCat.toString());
			}
			List<String> catList = new ArrayList<>();
			catList = Arrays.asList(selectedCat);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("routeId", routeId);

			FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId", map,
					FrNameIdByRouteIdResponse.class);

			List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

			System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

			StringBuilder sbForRouteFrId = new StringBuilder();
			for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

				sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

			}

			String strFrIdRouteWise = sbForRouteFrId.toString();
			selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
			System.out.println("fr Id Route WISE = " + selectedFr);

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			}

			map = new LinkedMultiValueMap<String, Object>();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}

			if (isAllCatSelected) {
				map = new LinkedMultiValueMap<String, Object>();

				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<MCategoryList> categoryList = categoryListResponse.getmCategoryList();

				StringBuilder cateList = new StringBuilder();
				// List<String> cateList = new ArrayList<>();
				for (MCategoryList mCategoryList : categoryList) {
					cateList = cateList.append(mCategoryList.getCatId().toString() + ",");
					// cateList.add("" + mCategoryList.getCatId());
				}
				System.err.println(cateList);
				String catlist = cateList.toString();
				selectedCat = catlist.substring(0, catlist.length() - 1);
				map.add("categories", selectedCat);
				map.add("billDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<DispatchReport>> typeRef = new ParameterizedTypeReference<List<DispatchReport>>() {
				};

				ResponseEntity<List<DispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.out.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				ArrayList<SubCategory> subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));

				/*
				 * if(!dispatchReportList.isEmpty()&&!responseEntity1.getBody().isEmpty()&&!
				 * frNameIdByRouteIdList.isEmpty()) { for(int
				 * j=0;j<responseEntity1.getBody().size();j++) { for(int
				 * i=0;i<frNameIdByRouteIdList.size();i++) { boolean flag=false; for(int
				 * k=0;k<dispatchReportList.size();k++) {
				 * if(dispatchReportList.get(k).getFrId()==frNameIdByRouteIdList.get(i).getFrId(
				 * ) &&
				 * dispatchReportList.get(k).getItemId()==responseEntity1.getBody().get(j).getId
				 * ()) { flag=true; break;
				 * 
				 * } } if(flag==false) { DispatchReport dispachReport=new DispatchReport();
				 * dispachReport.setBillDetailNo(0);
				 * dispachReport.setFrId(frNameIdByRouteIdList.get(i).getFrId());
				 * dispachReport.setItemId(responseEntity1.getBody().get(j).getId());
				 * dispachReport.setBillQty(0); dispatchReportList.add(dispachReport); } } } }
				 */
				model.addObject("dispatchReportList", dispatchReportList);
				model.addObject("frList", frNameIdByRouteIdList);
				model.addObject("itemList", responseEntity1.getBody());
				model.addObject("subCatList", subCatAList);

			} else {
				System.out.println("selectedCat" + selectedCat.toString());
				System.out.println("selectedFr" + selectedFr.toString());

				map.add("categories", selectedCat);
				map.add("billDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<DispatchReport>> typeRef = new ParameterizedTypeReference<List<DispatchReport>>() {
				};

				ResponseEntity<List<DispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.out.println("############################dispatchReportList######################## = "
						+ dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catId", selectedCat);
				ParameterizedTypeReference<List<SubCategory>> typeRef2 = new ParameterizedTypeReference<List<SubCategory>>() {
				};

				ResponseEntity<List<SubCategory>> responseEntity2 = restTemplate
						.exchange(Constants.url + "getSubCatList", HttpMethod.POST, new HttpEntity<>(map), typeRef2);

				/*
				 * if(!dispatchReportList.isEmpty()&&!responseEntity1.getBody().isEmpty()&&!
				 * frNameIdByRouteIdList.isEmpty()) { for(int
				 * j=0;j<responseEntity1.getBody().size();j++) { for(int
				 * i=0;i<frNameIdByRouteIdList.size();i++) { boolean flag=false; for(int
				 * k=0;k<dispatchReportList.size();k++) {
				 * if(dispatchReportList.get(k).getFrId()==frNameIdByRouteIdList.get(i).getFrId(
				 * ) &&
				 * dispatchReportList.get(k).getItemId()==responseEntity1.getBody().get(j).getId
				 * ()) { flag=true; break;
				 * 
				 * } } if(flag==false) { DispatchReport dispachReport=new DispatchReport();
				 * dispachReport.setBillDetailNo(0);
				 * dispachReport.setFrId(frNameIdByRouteIdList.get(i).getFrId());
				 * dispachReport.setItemId(responseEntity1.getBody().get(j).getId());
				 * dispachReport.setBillQty(0); dispatchReportList.add(dispachReport); } } } }
				 */

				model.addObject("dispatchReportList", dispatchReportList);
				model.addObject("frList", frNameIdByRouteIdList);
				model.addObject("itemList", responseEntity1.getBody());
				model.addObject("subCatList", responseEntity2.getBody());
			}
			model.addObject("routeName", routeName);

		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	@RequestMapping(value = "pdf/getPDispatchReportPdf/{billDate}/{routeId}/{selectedCat}", method = RequestMethod.GET)
	public ModelAndView getPDispatchReportPdf(@PathVariable String billDate, @PathVariable String routeId,
			@PathVariable String selectedCat, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("reports/sales/dispatchPReportPdf");
		RestTemplate restTemplate = new RestTemplate();

		List<PDispatchReport> dispatchReportList = new ArrayList<PDispatchReport>();
		PDispatchReportList dispatchReports = new PDispatchReportList();
		try {
			String convertedDate = "";
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateFormat.parse(billDate));
				cal.add(Calendar.DATE, 1);
				convertedDate = dateFormat.format(cal.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Inside get Dispatch Report");
			// String billDate = request.getParameter("bill_date");
			// String routeId = request.getParameter("route_id");
			// String selectedCat=request.getParameter("cat_id_list");
			AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
					AllRoutesListResponse.class);

			List<Route> routeList = new ArrayList<Route>();

			routeList = allRouteListResponse.getRoute();
			String routeName = "def";
			for (int i = 0; i < routeList.size(); i++) {

				if (routeList.get(i).getRouteId() == Integer.parseInt(routeId)) {
					routeName = routeList.get(i).getRouteName();
					break;

				}
			}
			boolean isAllCatSelected = false;
			String selectedFr = null;

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			} else {
				// selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
				// selectedCat = selectedCat.replaceAll("\"", "");
				// System.out.println("selectedCat"+selectedCat.toString());
			}
			List<String> catList = new ArrayList<>();
			catList = Arrays.asList(selectedCat);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("routeId", routeId);

			FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId", map,
					FrNameIdByRouteIdResponse.class);

			List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

			System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

			StringBuilder sbForRouteFrId = new StringBuilder();
			for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

				sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

			}

			String strFrIdRouteWise = sbForRouteFrId.toString();
			selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
			System.out.println("fr Id Route WISE = " + selectedFr);

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			}

			map = new LinkedMultiValueMap<String, Object>();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}

			if (isAllCatSelected) {
				map = new LinkedMultiValueMap<String, Object>();

				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<MCategoryList> categoryList = categoryListResponse.getmCategoryList();

				StringBuilder cateList = new StringBuilder();
				// List<String> cateList = new ArrayList<>();
				for (MCategoryList mCategoryList : categoryList) {
					cateList = cateList.append(mCategoryList.getCatId().toString() + ",");
					// cateList.add("" + mCategoryList.getCatId());
				}
				System.err.println(cateList);
				String catlist = cateList.toString();
				selectedCat = catlist.substring(0, catlist.length() - 1);
				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.out.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				ArrayList<SubCategory> subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));

				/*
				 * if(!dispatchReportList.isEmpty()&&!responseEntity1.getBody().isEmpty()&&!
				 * frNameIdByRouteIdList.isEmpty()) { for(int
				 * j=0;j<responseEntity1.getBody().size();j++) { for(int
				 * i=0;i<frNameIdByRouteIdList.size();i++) { boolean flag=false; for(int
				 * k=0;k<dispatchReportList.size();k++) {
				 * if(dispatchReportList.get(k).getFrId()==frNameIdByRouteIdList.get(i).getFrId(
				 * ) &&
				 * dispatchReportList.get(k).getItemId()==responseEntity1.getBody().get(j).getId
				 * ()) { flag=true; break;
				 * 
				 * } } if(flag==false) { DispatchReport dispachReport=new DispatchReport();
				 * dispachReport.setBillDetailNo(0);
				 * dispachReport.setFrId(frNameIdByRouteIdList.get(i).getFrId());
				 * dispachReport.setItemId(responseEntity1.getBody().get(j).getId());
				 * dispachReport.setBillQty(0); dispatchReportList.add(dispachReport); } } } }
				 */
				model.addObject("dispatchReportList", dispatchReportList);
				model.addObject("frList", frNameIdByRouteIdList);
				model.addObject("itemList", responseEntity1.getBody());
				model.addObject("subCatList", subCatAList);

			} else {
				System.out.println("selectedCat" + selectedCat.toString());
				System.out.println("selectedFr" + selectedFr.toString());

				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.out.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catId", selectedCat);
				ParameterizedTypeReference<List<SubCategory>> typeRef2 = new ParameterizedTypeReference<List<SubCategory>>() {
				};

				ResponseEntity<List<SubCategory>> responseEntity2 = restTemplate.exchange(
						Constants.url + "getSubCatListForDis", HttpMethod.POST, new HttpEntity<>(map), typeRef2);

				model.addObject("dispatchReportList", dispatchReportList);
				model.addObject("frList", frNameIdByRouteIdList);
				model.addObject("itemList", responseEntity1.getBody());
				model.addObject("subCatList", responseEntity2.getBody());
			}
			model.addObject("routeName", routeName);
			model.addObject("convertedDate", convertedDate);
			model.addObject("FACTORYNAME", Constants.FACTORYNAME);
			model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	@RequestMapping(value = "pdf/getDispatchPReportPdfForBill/{billDate}/{routeId}/{selectedCat}/{frId}", method = RequestMethod.GET)
	public ModelAndView getDispatchPReportPdfForBill(@PathVariable String billDate, @PathVariable String routeId,
			@PathVariable String selectedCat, @PathVariable int frId, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("reports/sales/dispatchReportPPdfBill");/* dispatchReportPPdfBill */
		RestTemplate restTemplate = new RestTemplate();

		List<PDispatchReport> dispatchReportList = new ArrayList<PDispatchReport>();
		PDispatchReportList dispatchReports = new PDispatchReportList();
		try {
			String convertedDate = "";
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateFormat.parse(billDate));
				cal.add(Calendar.DATE, 1);
				convertedDate = dateFormat.format(cal.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Inside get Dispatch Report");
			// String billDate = request.getParameter("bill_date");
			// String routeId = request.getParameter("route_id");
			// String selectedCat=request.getParameter("cat_id_list");
			AllRoutesListResponse allRouteListResponse = restTemplate.getForObject(Constants.url + "showRouteList",
					AllRoutesListResponse.class);

			List<Route> routeList = new ArrayList<Route>();

			routeList = allRouteListResponse.getRoute();
			String routeName = "def";
			for (int i = 0; i < routeList.size(); i++) {

				if (routeList.get(i).getRouteId() == Integer.parseInt(routeId)) {
					routeName = routeList.get(i).getRouteName();
					break;

				}
			}
			boolean isAllCatSelected = false;
			String selectedFr = null;

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			} else {
				// selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
				// selectedCat = selectedCat.replaceAll("\"", "");
				// System.out.println("selectedCat"+selectedCat.toString());
			}
			List<String> catList = new ArrayList<>();
			catList = Arrays.asList(selectedCat);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("routeId", routeId);

			FranchiseForDispatch[] frNameId = restTemplate.postForObject(Constants.url + "getFranchiseForDispatch", map,
					FranchiseForDispatch[].class);

			List<FranchiseForDispatch> frNameIdByRouteIdList = new ArrayList<>(Arrays.asList(frNameId));

			System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

			StringBuilder sbForRouteFrId = new StringBuilder();
			for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

				sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId() + ",");

			}

			String strFrIdRouteWise = sbForRouteFrId.toString();
			selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
			System.out.println("fr Id Route WISE = " + selectedFr);

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			}

			map = new LinkedMultiValueMap<String, Object>();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}

			if (isAllCatSelected) {
				map = new LinkedMultiValueMap<String, Object>();

				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<MCategoryList> categoryList = categoryListResponse.getmCategoryList();

				StringBuilder cateList = new StringBuilder();
				// List<String> cateList = new ArrayList<>();
				for (MCategoryList mCategoryList : categoryList) {
					cateList = cateList.append(mCategoryList.getCatId().toString() + ",");
					// cateList.add("" + mCategoryList.getCatId());
				}
				System.err.println(cateList);
				String catlist = cateList.toString();
				selectedCat = catlist.substring(0, catlist.length() - 1);
				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.err.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				ArrayList<SubCategory> subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));

				/*
				 * if(!dispatchReportList.isEmpty()&&!responseEntity1.getBody().isEmpty()&&!
				 * frNameIdByRouteIdList.isEmpty()) { for(int
				 * j=0;j<responseEntity1.getBody().size();j++) { for(int
				 * i=0;i<frNameIdByRouteIdList.size();i++) { boolean flag=false; for(int
				 * k=0;k<dispatchReportList.size();k++) {
				 * if(dispatchReportList.get(k).getFrId()==frNameIdByRouteIdList.get(i).getFrId(
				 * ) &&
				 * dispatchReportList.get(k).getItemId()==responseEntity1.getBody().get(j).getId
				 * ()) { flag=true; break;
				 * 
				 * } } if(flag==false) { DispatchReport dispachReport=new DispatchReport();
				 * dispachReport.setBillDetailNo(0);
				 * dispachReport.setFrId(frNameIdByRouteIdList.get(i).getFrId());
				 * dispachReport.setItemId(responseEntity1.getBody().get(j).getId());
				 * dispachReport.setBillQty(0); dispatchReportList.add(dispachReport); } } } }
				 */
				model.addObject("dispatchReportList", dispatchReportList);
				model.addObject("frList", frNameIdByRouteIdList);
				model.addObject("itemList", responseEntity1.getBody());
				model.addObject("subCatList", subCatAList);

			} else {
				System.out.println("selectedCat" + selectedCat.toString());
				System.out.println("selectedFr" + selectedFr.toString());

				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.err.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catId", selectedCat);
				ParameterizedTypeReference<List<SubCategory>> typeRef2 = new ParameterizedTypeReference<List<SubCategory>>() {
				};

				ResponseEntity<List<SubCategory>> responseEntity2 = restTemplate
						.exchange(Constants.url + "getSubCatList", HttpMethod.POST, new HttpEntity<>(map), typeRef2);

				model.addObject("dispatchReportList", dispatchReportList);
				model.addObject("frList", frNameIdByRouteIdList);
				model.addObject("itemList", responseEntity1.getBody());
				model.addObject("subCatList", responseEntity2.getBody());
			}
			model.addObject("routeName", routeName);
			model.addObject("frId", frId);
			model.addObject("convertedDate", convertedDate);
			model.addObject("FACTORYNAME", Constants.FACTORYNAME);
			model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	@RequestMapping(value = "pdf/getDispatchPReportPdfForDispatch/{billDate}/{menuId}/{routeId}/{selectedCat}/{frId}", method = RequestMethod.GET)
	public ModelAndView getDispatchPReportPdfForDispatch(@PathVariable String billDate, @PathVariable String menuId,
			@PathVariable String routeId, @PathVariable String selectedCat, @PathVariable String frId,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("reports/sales/dispatchMini");/* dispatchReportPPdfBill */
		RestTemplate restTemplate = new RestTemplate();

		List<PDispatchReport> dispatchReportList = new ArrayList<PDispatchReport>();
		// PDispatchReportList dispatchReports = new PDispatchReportList();
		try {
			String convertedDate = "";
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateFormat.parse(billDate));
				cal.add(Calendar.DATE, 1);
				convertedDate = dateFormat.format(cal.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// System.out.println("Inside get Dispatch Report");
			// String billDate = request.getParameter("bill_date");
			// String routeId = request.getParameter("route_id");
			// String selectedCat=request.getParameter("cat_id_list");
			/*
			 * AllRoutesListResponse allRouteListResponse =
			 * restTemplate.getForObject(Constants.url + "showRouteList",
			 * AllRoutesListResponse.class);
			 * 
			 * List<Route> routeList = new ArrayList<Route>();
			 * 
			 * routeList = allRouteListResponse.getRoute(); String routeName = "def"; for
			 * (int i = 0; i < routeList.size(); i++) {
			 * 
			 * if (routeList.get(i).getRouteId() == Integer.parseInt(routeId)) { routeName =
			 * routeList.get(i).getRouteName(); break;
			 * 
			 * } }
			 */
			boolean isAllCatSelected = false;
			String selectedFr = null;

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			} else {
				// selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
				// selectedCat = selectedCat.replaceAll("\"", "");
				// System.out.println("selectedCat"+selectedCat.toString());
			}
			// List<String> catList = new ArrayList<>();
			// catList = Arrays.asList(selectedCat);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			String strFrIdRouteWise = frId.toString();// prev
														// was
														// sbForRouteFrId.toString();
			selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length());
			System.out.println("fr Id Route WISE = " + selectedFr + "frId" + frId);
			// -----------------new------------------------
			map.add("frIds", frId);

			FranchiseForDispatch[] frNameId = restTemplate
					.postForObject(Constants.url + "getFranchiseForDispatchByFrIds", map, FranchiseForDispatch[].class);

			List<FranchiseForDispatch> frNameIdByRouteIdList = new ArrayList<>(Arrays.asList(frNameId));

			System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());
			// ---------------------------------------------
			/*
			 * map.add("routeId", routeId);
			 * 
			 * FranchiseForDispatch[] frNameId = restTemplate.postForObject(Constants.url +
			 * "getFranchiseForDispatch", map, FranchiseForDispatch[].class);
			 * 
			 * List<FranchiseForDispatch> frNameIdByRouteIdList =new
			 * ArrayList<>(Arrays.asList(frNameId));
			 * 
			 * System.out.println("route wise franchisee " +
			 * frNameIdByRouteIdList.toString());
			 * 
			 * StringBuilder sbForRouteFrId = new StringBuilder(); for (int i = 0; i <
			 * frNameIdByRouteIdList.size(); i++) {
			 * 
			 * sbForRouteFrId =
			 * sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId()+ ",");
			 * 
			 * }
			 */
			/*
			 * String strFrIdRouteWise = frId.toString();//prev was
			 * sbForRouteFrId.toString(); selectedFr = strFrIdRouteWise.substring(0,
			 * strFrIdRouteWise.length() - 1); System.out.println("fr Id Route WISE = " +
			 * selectedFr);
			 * 
			 * if (selectedCat.contains("-1")) { isAllCatSelected = true; }
			 * 
			 * map = new LinkedMultiValueMap<String, Object>();
			 * 
			 * allFrIdNameList = new AllFrIdNameList(); try {
			 * 
			 * allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
			 * AllFrIdNameList.class);
			 * 
			 * } catch (Exception e) { System.out.println("Exception in getAllFrIdName" +
			 * e.getMessage()); e.printStackTrace();
			 * 
			 * }
			 */

			if (isAllCatSelected) {
				map = new LinkedMultiValueMap<String, Object>();

				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<MCategoryList> categoryList = categoryListResponse.getmCategoryList();

				StringBuilder cateList = new StringBuilder();
				// List<String> cateList = new ArrayList<>();
				for (MCategoryList mCategoryList : categoryList) {
					cateList = cateList.append(mCategoryList.getCatId().toString() + ",");
					// cateList.add("" + mCategoryList.getCatId());
				}
				System.err.println(cateList);
				String catlist = cateList.toString();
				selectedCat = catlist.substring(0, catlist.length() - 1);
				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);
				map.add("menuId", menuId);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.err.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				ArrayList<SubCategory> subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));

				model.addObject("dispatchReportList", dispatchReportList);
				model.addObject("frList", frNameIdByRouteIdList);
				model.addObject("itemList", responseEntity1.getBody());
				model.addObject("subCatList", subCatAList);

			} else {
				System.out.println("selectedCat" + selectedCat.toString());
				System.out.println("selectedFr" + selectedFr.toString());

				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);
				map.add("menuId", menuId);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.err.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catId", selectedCat);
				ParameterizedTypeReference<List<SubCategory>> typeRef2 = new ParameterizedTypeReference<List<SubCategory>>() {
				};

				ResponseEntity<List<SubCategory>> responseEntity2 = restTemplate.exchange(
						Constants.url + "getSubCatListForDis", HttpMethod.POST, new HttpEntity<>(map), typeRef2);

				model.addObject("dispatchReportList", dispatchReportList);
				model.addObject("frList", frNameIdByRouteIdList);
				model.addObject("itemList", responseEntity1.getBody());
				model.addObject("subCatList", responseEntity2.getBody());
			}
			// model.addObject("routeName", routeName);commented
			// model.addObject("frId", frId);commented
			model.addObject("convertedDate", convertedDate);
			model.addObject("FACTORYNAME", Constants.FACTORYNAME);
			model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
			List<Integer> frList = Stream.of(selectedFr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			List<Integer> frListOrdersPresent = new ArrayList<>();
			for (int l = 0; l < frList.size(); l++) {
				int flag = 0;
				for (int m = 0; m < dispatchReportList.size(); m++) {
					if (dispatchReportList.get(m).getFrId() == frList.get(l)) {
						flag = 1;
						break;
					}
				}
				if (flag == 1) {
					frListOrdersPresent.add(frList.get(l));
				}
			}

			model.addObject("frListSelected", frListOrdersPresent);

		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	// --------------------------------------------------------------------------------------------------
	@RequestMapping(value = "/getSaleReportRoyConsoByCat", method = RequestMethod.GET)
	public @ResponseBody RoyaltyListBean getSaleReportRoyConsoByCat(HttpServletRequest request,
			HttpServletResponse response) {
		String fromDate = "";
		String toDate = "";
		List<SalesReportBillwise> saleList = new ArrayList<>();
		List<SalesReportRoyalty> royaltyList = new ArrayList<>();
		RoyaltyListBean royaltyBean = new RoyaltyListBean();

		try {
			System.out.println("Inside get Sale Bill Wise");
			String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			String routeId = request.getParameter("route_id");
			int getBy = Integer.parseInt(request.getParameter("getBy"));
			int type = Integer.parseInt(request.getParameter("type"));
			int isGraph = Integer.parseInt(request.getParameter("is_graph"));

			String selectedCat = request.getParameter("cat_id_list");

			boolean isAllFrSelected = false;
			boolean isAllCatSelected = false;
			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			// frList = new ArrayList<>();
			// frList = Arrays.asList(selectedFr);

			frList = Stream.of(selectedFr.split(",")).collect(Collectors.toList());

			selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
			selectedCat = selectedCat.replaceAll("\"", "");

			List<String> catList = Stream.of(selectedCat.split(",")).collect(Collectors.toList());

			// List<String> catList = new ArrayList<>();
			// catList = Arrays.asList(selectedCat);

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (frList.contains("-1")) {
				isAllFrSelected = true;
			}

			if (catList.contains("-1")) {
				isAllCatSelected = true;
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			// new code

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");
				if (isAllCatSelected) {
					map.add("catIdList", 0);
				} else {
					map.add("catIdList", selectedCat);
				}

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("getBy", getBy);
				map.add("type", type);
				if (isGraph == 0) {
					ParameterizedTypeReference<List<SalesReportRoyalty>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() {
					};

					ResponseEntity<List<SalesReportRoyalty>> responseEntity = restTemplate.exchange(
							Constants.url + "getSaleReportRoyConsoByCatAllFr", HttpMethod.POST, new HttpEntity<>(map),
							typeRef);

					royaltyList = responseEntity.getBody();
					royaltyListForPdf = new ArrayList<>();

					royaltyListForPdf = royaltyList;
				}

				/*
				 * if (isGraph == 1) { ParameterizedTypeReference<List<SalesReportRoyalty>>
				 * typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() { };
				 * 
				 * ResponseEntity<List<SalesReportRoyalty>> responseEntity =
				 * restTemplate.exchange( Constants.url + "getSaleReportRoyConsoByCatForGraph",
				 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
				 * 
				 * royaltyList = responseEntity.getBody(); royaltyListForPdf = new
				 * ArrayList<>();
				 * 
				 * royaltyListForPdf = royaltyList; }
				 */

			} // end of if all fr Selected
			else {

				// few fr Selected

				if (isAllCatSelected) {
					System.err.println("-1 present -------------------------------------------------- ");
					map.add("catIdList", 0);
				} else {
					map.add("catIdList", selectedCat);
				}

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				map.add("frIdList", selectedFr);
				map.add("getBy", getBy);
				map.add("type", type);
				if (isGraph == 0) {
					ParameterizedTypeReference<List<SalesReportRoyalty>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() {
					};

					ResponseEntity<List<SalesReportRoyalty>> responseEntity = restTemplate.exchange(
							Constants.url + "getSaleReportRoyConsoByCat", HttpMethod.POST, new HttpEntity<>(map),
							typeRef);

					royaltyList = responseEntity.getBody();
					royaltyListForPdf = new ArrayList<>();

					royaltyListForPdf = royaltyList;
				}

				/*
				 * if (isGraph == 1) { ParameterizedTypeReference<List<SalesReportRoyalty>>
				 * typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() { };
				 * 
				 * ResponseEntity<List<SalesReportRoyalty>> responseEntity =
				 * restTemplate.exchange( Constants.url + "getSaleReportRoyConsoByCatForGraph",
				 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
				 * 
				 * royaltyList = responseEntity.getBody(); royaltyListForPdf = new
				 * ArrayList<>();
				 * 
				 * royaltyListForPdf = royaltyList; }
				 */

			} // end of else

			// new code

			/*
			 * if (isAllCatSelected) {
			 * 
			 * System.out.println("Inside If all fr Selected ");
			 * 
			 * map.add("catIdList", 0);
			 * 
			 * map.add("fromDate", fromDate); map.add("toDate", toDate);
			 * 
			 * } else {
			 * 
			 * map.add("catIdList", selectedCat); map.add("fromDate", fromDate);
			 * map.add("toDate", toDate);
			 * 
			 * }
			 * 
			 * if (isAllFrSelected) {
			 * 
			 * System.out.println("Inside If all fr Selected "); map.add("fromDate",
			 * fromDate); map.add("toDate", toDate);
			 * 
			 * // getSaleReportRoyConsoByCatAllFr
			 * 
			 * } else { System.out.println("Inside else Few fr Selected "); //
			 * map.add("catIdList", selectedCat); map.add("frIdList", selectedFr);
			 * map.add("fromDate", fromDate); map.add("toDate", toDate); if (isGraph == 0) {
			 * ParameterizedTypeReference<List<SalesReportRoyalty>> typeRef = new
			 * ParameterizedTypeReference<List<SalesReportRoyalty>>() { };
			 * 
			 * ResponseEntity<List<SalesReportRoyalty>> responseEntity =
			 * restTemplate.exchange( Constants.url + "getSaleReportRoyConsoByCat",
			 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
			 * 
			 * royaltyList = responseEntity.getBody(); royaltyListForPdf = new
			 * ArrayList<>();
			 * 
			 * royaltyListForPdf = royaltyList; }
			 * 
			 * if (isGraph == 1) { ParameterizedTypeReference<List<SalesReportRoyalty>>
			 * typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() { };
			 * 
			 * ResponseEntity<List<SalesReportRoyalty>> responseEntity =
			 * restTemplate.exchange( Constants.url + "getSaleReportRoyConsoByCatForGraph",
			 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
			 * 
			 * royaltyList = responseEntity.getBody(); royaltyListForPdf = new
			 * ArrayList<>();
			 * 
			 * royaltyListForPdf = royaltyList; }
			 */

			System.out.println("royaltyList List Bill Wise " + royaltyList.toString());

			CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
					CategoryListResponse.class);
			List<MCategoryList> categoryList;

			categoryList = categoryListResponse.getmCategoryList();
			// allFrIdNameList = new AllFrIdNameList();
			System.out.println("Category list  " + categoryList);
			List<MCategoryList> tempList = new ArrayList<>();

			// royaltyBean.setCategoryList(categoryList);
			Map<Integer, String> catNameId = new HashMap<Integer, String>();

			for (int i = 0; i < categoryList.size(); i++) {

				for (int j = 0; j < royaltyList.size(); j++) {

					if (categoryList.get(i).getCatId() == royaltyList.get(j).getCatId()) {
						catNameId.put(categoryList.get(i).getCatId(), categoryList.get(i).getCatName());

						if (!tempList.contains(categoryList.get(i))) {

							tempList.add(categoryList.get(i));

						}
					}

				}
			}
			System.out.println("temp list " + categoryList.toString() + "size of t List " + categoryList.size());
			royaltyBean.setCategoryList(tempList);
			royaltyBean.setSalesReportRoyalty(royaltyList);
			staticRoyaltyBean = royaltyBean;

		} catch (

		Exception e) {
			System.out.println("get sale Report royaltyList by cat " + e.getMessage());
			e.printStackTrace();

		}
		// exportToExcel
		List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

		ExportToExcel expoExcel = new ExportToExcel();
		List<String> rowData = new ArrayList<String>();

		rowData.add("Sr.No.");
		rowData.add("Category Name");
		rowData.add("Item Name");
		rowData.add("Sale Qty");
		rowData.add("Sale Value");

		rowData.add("GRN Qty");
		rowData.add("GRN Value");
		rowData.add("GVN Qty");
		rowData.add("GVN Value");

		rowData.add("Net Qty");
		rowData.add("Net Value");
		float royPer = getRoyPer();
		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		float saleQty = 0.0f;
		float saleValue = 0.0f;
		float grnQty = 0.0f;
		float grnValue = 0.0f;
		float gvnQty = 0.0f;
		float gvnValue = 0.0f;
		float netQtyTotal = 0.0f;
		float netValueTotal = 0.0f;

		if (!royaltyBean.getSalesReportRoyalty().isEmpty()) {
			for (int i = 0; i < royaltyList.size(); i++) {
				int index = 1;
				index = index + i;
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();

				rowData.add("" + index);
				rowData.add("" + royaltyList.get(i).getCat_name());

				rowData.add("" + royaltyList.get(i).getItem_name());

				rowData.add("" +

						roundUp(royaltyList.get(i).gettBillQty()));
				rowData.add("" + roundUp(royaltyList.get(i).gettBillTaxableAmt()));

				rowData.add("" + roundUp(royaltyList.get(i).gettGrnQty()));

				rowData.add("" + roundUp(royaltyList.get(i).gettGrnTaxableAmt()));
				rowData.add("" + roundUp(royaltyList.get(i).gettGvnQty()));
				rowData.add("" + roundUp(royaltyList.get(i).gettGvnTaxableAmt()));

				float netQty = royaltyList.get(i).gettBillQty()
						- (royaltyList.get(i).gettGrnQty() + royaltyList.get(i).gettGvnQty());

				float netValue = royaltyList.get(i).gettBillTaxableAmt()
						- (royaltyList.get(i).gettGrnTaxableAmt() + royaltyList.get(i).gettGvnTaxableAmt());
				// float royPer = getRoyPer();

				float rAmt = netValue * royPer / 100;

				rowData.add("" + roundUp(netQty));
				rowData.add("" + roundUp(netValue));
				/*
				 * rowData.add(""+roundUp(royPer)); rowData.add(""+roundUp(rAmt));
				 */

				saleQty = saleQty + royaltyList.get(i).gettBillQty();
				saleValue = saleValue + royaltyList.get(i).gettBillTaxableAmt();
				grnQty = grnQty + royaltyList.get(i).gettGrnQty();
				grnValue = grnValue + royaltyList.get(i).gettGrnTaxableAmt();
				gvnQty = gvnQty + royaltyList.get(i).gettGvnQty();
				gvnValue = gvnValue + royaltyList.get(i).gettGvnTaxableAmt();
				netQtyTotal = netQtyTotal + netQty;
				netValueTotal = netValueTotal + netValue;

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}
		}

		expoExcel = new ExportToExcel();
		rowData = new ArrayList<String>();

		rowData.add("");
		rowData.add("Total");
		rowData.add("");

		rowData.add("" + roundUp(saleQty));
		rowData.add("" + roundUp(saleValue));
		rowData.add("" + roundUp(grnQty));
		rowData.add("" + roundUp(grnValue));
		rowData.add("" + roundUp(gvnQty));
		rowData.add("" + roundUp(gvnValue));
		rowData.add("" + roundUp(netQtyTotal));
		rowData.add("" + roundUp(netValueTotal));

		expoExcel.setRowData(rowData);
		exportToExcelList.add(expoExcel);

		System.out.println("exportToExcelList" + exportToExcelList);
		HttpSession session = request.getSession();
		session.setAttribute("exportExcelListNew", exportToExcelList);
		session.setAttribute("excelNameNew", "RoyaltyConsolidatedCatList");
		session.setAttribute("reportNameNew", "View Sales Royalty by Category");
		session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
		session.setAttribute("mergeUpto1", "$A$1:$K$1");
		session.setAttribute("mergeUpto2", "$A$2:$K$2");

		return royaltyBean;

	}

	@RequestMapping(value = "pdf/getSaleReportRoyConsoByCatPdf/{fromDate}/{toDate}/{selectedFr}/{routeId}/{selectedCat}/{isGraph}/{getBy}/{type}", method = RequestMethod.GET)
	public ModelAndView getSaleReportRoyConsoByCat(@PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String selectedFr, @PathVariable String routeId, @PathVariable String selectedCat,
			@PathVariable int isGraph, @PathVariable int getBy, @PathVariable int type, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("reports/sales/pdf/salesconsbycatPdf");

		List<SalesReportBillwise> saleList = new ArrayList<>();
		List<SalesReportRoyalty> royaltyList = new ArrayList<>();
		RoyaltyListBean royaltyBean = new RoyaltyListBean();
		boolean isAllFrSelected = false;
		boolean isAllCatSelected = false;
		try {
			System.out.println("Inside get Sale Bill Wise");
			/*
			 * 
			 * 
			 * selectedFr = selectedFr.substring(1, selectedFr.length() - 1); selectedFr =
			 * selectedFr.replaceAll("\"", "");
			 * 
			 * selectedCat = selectedCat.substring(1, selectedCat.length() - 1); selectedCat
			 * = selectedCat.replaceAll("\"", "");
			 * 
			 * List<String> catList=new ArrayList<>(); catList=Arrays.asList(selectedCat);
			 * frList = new ArrayList<>(); frList = Arrays.asList(selectedFr);
			 */

			if (!routeId.equalsIgnoreCase("0")) {

				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				RestTemplate restTemplate = new RestTemplate();

				map.add("routeId", routeId);

				FrNameIdByRouteIdResponse frNameId = restTemplate.postForObject(Constants.url + "getFrNameIdByRouteId",
						map, FrNameIdByRouteIdResponse.class);

				List<FrNameIdByRouteId> frNameIdByRouteIdList = frNameId.getFrNameIdByRouteIds();

				System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());

				StringBuilder sbForRouteFrId = new StringBuilder();
				for (int i = 0; i < frNameIdByRouteIdList.size(); i++) {

					sbForRouteFrId = sbForRouteFrId.append(frNameIdByRouteIdList.get(i).getFrId().toString() + ",");

				}

				String strFrIdRouteWise = sbForRouteFrId.toString();
				selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
				System.out.println("fr Id Route WISE = " + selectedFr);

			} // end of if

			if (selectedFr.contains("-1")) {
				isAllFrSelected = true;
			}

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			if (isAllFrSelected) {

				System.out.println("Inside If all fr Selected ");
				if (isAllCatSelected) {
					map.add("catIdList", 0);
				} else {
					map.add("catIdList", selectedCat);
				}

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);
				map.add("getBy", getBy);
				map.add("type", type);
				if (isGraph == 0) {
					ParameterizedTypeReference<List<SalesReportRoyalty>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() {
					};

					ResponseEntity<List<SalesReportRoyalty>> responseEntity = restTemplate.exchange(
							Constants.url + "getSaleReportRoyConsoByCatAllFr", HttpMethod.POST, new HttpEntity<>(map),
							typeRef);

					royaltyList = responseEntity.getBody();
					royaltyListForPdf = new ArrayList<>();

					royaltyListForPdf = royaltyList;
				}

				/*
				 * if (isGraph == 1) { ParameterizedTypeReference<List<SalesReportRoyalty>>
				 * typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() { };
				 * 
				 * ResponseEntity<List<SalesReportRoyalty>> responseEntity =
				 * restTemplate.exchange( Constants.url + "getSaleReportRoyConsoByCatForGraph",
				 * HttpMethod.POST, new HttpEntity<>(map), typeRef);
				 * 
				 * royaltyList = responseEntity.getBody(); royaltyListForPdf = new
				 * ArrayList<>();
				 * 
				 * royaltyListForPdf = royaltyList; }
				 */

			} // end of if all fr Selected
			else {

				// few fr Selected

				if (isAllCatSelected) {
					map.add("catIdList", 0);
				} else {
					map.add("catIdList", selectedCat);
				}

				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				map.add("frIdList", selectedFr);
				map.add("getBy", getBy);
				map.add("type", type);
				if (isGraph == 0) {
					ParameterizedTypeReference<List<SalesReportRoyalty>> typeRef = new ParameterizedTypeReference<List<SalesReportRoyalty>>() {
					};

					ResponseEntity<List<SalesReportRoyalty>> responseEntity = restTemplate.exchange(
							Constants.url + "getSaleReportRoyConsoByCat", HttpMethod.POST, new HttpEntity<>(map),
							typeRef);

					royaltyList = responseEntity.getBody();
					royaltyListForPdf = new ArrayList<>();

					royaltyListForPdf = royaltyList;
				}
			}

			System.out.println("royaltyList List Bill Wise " + royaltyList.toString());

			CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
					CategoryListResponse.class);
			List<MCategoryList> categoryList;

			categoryList = categoryListResponse.getmCategoryList();
			// allFrIdNameList = new AllFrIdNameList();
			System.out.println("Category list  " + categoryList);
			List<MCategoryList> tempList = new ArrayList<>();

			// royaltyBean.setCategoryList(categoryList);
			Map<Integer, String> catNameId = new HashMap<Integer, String>();

			for (int i = 0; i < categoryList.size(); i++) {

				for (int j = 0; j < royaltyList.size(); j++) {

					if (categoryList.get(i).getCatId() == royaltyList.get(j).getCatId()) {
						catNameId.put(categoryList.get(i).getCatId(), categoryList.get(i).getCatName());

						if (!tempList.contains(categoryList.get(i))) {

							tempList.add(categoryList.get(i));

						}
					}

				}

				// }

				System.out.println("temp list " + tempList.toString() + "size of t List " + tempList.size());
				royaltyBean.setCategoryList(tempList);
				royaltyBean.setSalesReportRoyalty(royaltyList);
				model.addObject("royaltyList", royaltyBean);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("FACTORYNAME", Constants.FACTORYNAME);
				model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
				model.addObject("royPer", getRoyPer());

			}
		} catch (

		Exception e) {
			System.out.println("get sale Report royaltyList by cat " + e.getMessage());
			e.printStackTrace();

		}

		return model;

	}

	// --------------------------------------------------------------------------------------------------
	@RequestMapping(value = "/showMonthlySalesQtyWiseReport", method = RequestMethod.GET)
	public ModelAndView showMonthlySalesQtyWiseReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		/*
		 * HttpSession session = request.getSession();
		 * 
		 * List<ModuleJson> newModuleList = (List<ModuleJson>)
		 * session.getAttribute("newModuleList"); Info view =
		 * AccessControll.checkAccess("showSaleReportByDate", "showSaleReportByDate",
		 * "1", "0", "0", "0", newModuleList); if (view.getError() == true) { model =
		 * new ModelAndView("accessDenied");
		 * 
		 * } else {
		 */
		model = new ModelAndView("reports/sales/monthlysalesqtyreport");
		RestTemplate restTemplate = new RestTemplate();

		try {
			String year = request.getParameter("year");

			if (year != "") {
				String[] yrs = year.split("-"); // returns an array with the 2 parts

				MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

				map.add("fromYear", yrs[0]);
				map.add("toYear", yrs[1]);

				SalesReturnQtyReportList[] salesReturnQtyReportListRes = restTemplate.postForObject(
						Constants.url + "getSalesReturnQtyReport", map, SalesReturnQtyReportList[].class);

				List<SalesReturnQtyReportList> salesReturnQtyReportList = new ArrayList<SalesReturnQtyReportList>(
						Arrays.asList(salesReturnQtyReportListRes));

				SubCategory[] subCategoryList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				ArrayList<SubCategory> subCatList = new ArrayList<SubCategory>(Arrays.asList(subCategoryList));

				LinkedHashMap<Integer, SalesReturnQtyReportList> salesReturnQtyReport = new LinkedHashMap<>();

				for (int i = 0; i < salesReturnQtyReportList.size(); i++) {
					salesReturnQtyReport.put(i, salesReturnQtyReportList.get(i));
					float totBillQty = 0;
					float totGrnQty = 0;
					float totGvnQty = 0;
					for (int k = 0; k < salesReturnQtyReportList.get(i).getSalesReturnQtyDaoList().size(); k++) {
						totBillQty = totBillQty
								+ salesReturnQtyReportList.get(i).getSalesReturnQtyDaoList().get(k).getBillQty();
						totGrnQty = totGrnQty
								+ salesReturnQtyReportList.get(i).getSalesReturnQtyDaoList().get(k).getGrnQty();
						totGvnQty = totGvnQty
								+ salesReturnQtyReportList.get(i).getSalesReturnQtyDaoList().get(k).getGvnQty();
					}
					salesReturnQtyReportList.get(i).setTotBillQty(totBillQty);
					salesReturnQtyReportList.get(i).setTotGrnQty(totGrnQty);
					salesReturnQtyReportList.get(i).setTotGvnQty(totGvnQty);
				}

				model.addObject("salesReturnQtyReport", salesReturnQtyReport);
				model.addObject("subCatList", subCatList);

				// exportToExcel
				List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

				ExportToExcel expoExcel = new ExportToExcel();
				List<String> rowData = new ArrayList<String>();
				rowData.add("Sr.");
				rowData.add("Group Name");
				for (int i = 0; i < salesReturnQtyReport.size(); i++) {
					rowData.add(salesReturnQtyReport.get(i).getMonth() + " Gross Sale");
					rowData.add(salesReturnQtyReport.get(i).getMonth() + " GVN Qty");
					rowData.add(salesReturnQtyReport.get(i).getMonth() + " GRN Qty");
					rowData.add(salesReturnQtyReport.get(i).getMonth() + " Total");
				}
				rowData.add("Total Gross Sale");
				rowData.add("Total GVN Qty");
				rowData.add("Total GRN Qty");

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);
				float totBillQty = 0.0f;
				float totGrnQty = 0.0f;
				float totGvnQty = 0.0f;
				for (int k = 0; k < subCatList.size(); k++) {

					float billQty = 0.0f;
					float grnQty = 0.0f;
					float gvnQty = 0.0f;

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();
					rowData.add("" + (k + 1));
					rowData.add("" + subCatList.get(k).getSubCatName());
					for (int i = 0; i < salesReturnQtyReport.size(); i++) {
						for (int j = 0; j < salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().size(); j++) {

							if (salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j)
									.getSubCatId() == subCatList.get(k).getSubCatId()) {
								rowData.add("" + roundUp(
										salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j).getBillQty()));
								rowData.add("" + roundUp(
										salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j).getGvnQty()));
								rowData.add("" + roundUp(
										salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j).getGrnQty()));
								rowData.add("" + roundUp(salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j)
										.getBillQty()
										- (salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j).getGvnQty()
												+ salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j)
														.getGrnQty())));
								billQty = billQty
										+ salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j).getBillQty();
								grnQty = grnQty
										+ salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j).getGrnQty();
								gvnQty = gvnQty
										+ salesReturnQtyReport.get(i).getSalesReturnQtyDaoList().get(j).getGvnQty();
							}

						}
					}
					rowData.add("" + roundUp(billQty));
					rowData.add("" + roundUp(gvnQty));
					rowData.add("" + roundUp(grnQty));
					totBillQty = totBillQty + billQty;
					totGrnQty = totGrnQty + grnQty;
					totGvnQty = totGvnQty + gvnQty;

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);

				}
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				rowData.add("");
				rowData.add("Total");
				for (int i = 0; i < salesReturnQtyReport.size(); i++) {
					rowData.add("" + roundUp(salesReturnQtyReport.get(i).getTotBillQty()));
					rowData.add("" + roundUp(salesReturnQtyReport.get(i).getTotGvnQty()));
					rowData.add("" + roundUp(salesReturnQtyReport.get(i).getTotGrnQty()));
					rowData.add(roundUp(
							(salesReturnQtyReport.get(i).getTotBillQty() - (salesReturnQtyReport.get(i).getTotGvnQty()
									+ salesReturnQtyReport.get(i).getTotGrnQty())))
							+ "");
				}
				rowData.add("" + totBillQty);
				rowData.add("" + totGrnQty);
				rowData.add("" + totGvnQty);
				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

				HttpSession session = request.getSession();
				session.setAttribute("exportExcelList", exportToExcelList);
				session.setAttribute("excelName", "MonthlySalesReturnQtyReport");

			}
		} catch (Exception e) {

		}
		// }
		return model;

	}
	// monthwisesalespercentage

	@RequestMapping(value = "/showMonthlySalesPercentageReport", method = RequestMethod.GET)
	public ModelAndView showMonthlySalesPercentageReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;

		model = new ModelAndView("reports/sales/monthwisesalespercentage");
		RestTemplate restTemplate = new RestTemplate();

		try {
			String year = request.getParameter("year");

			if (year != "") {
				String[] yrs = year.split("-"); // returns an array with the 2 parts

				MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

				map.add("fromYear", yrs[0]);
				map.add("toYear", yrs[1]);

				SalesReturnValueDaoList[] salesReturnValueReportListRes = restTemplate.postForObject(
						Constants.url + "getSalesReturnValueReport", map, SalesReturnValueDaoList[].class);

				List<SalesReturnValueDaoList> salesReturnValueReportList = new ArrayList<SalesReturnValueDaoList>(
						Arrays.asList(salesReturnValueReportListRes));

				SubCategory[] subCategoryList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				ArrayList<SubCategory> subCatList = new ArrayList<SubCategory>(Arrays.asList(subCategoryList));

				LinkedHashMap<Integer, SalesReturnValueDaoList> salesReturnValueReport = new LinkedHashMap<>();

				for (int i = 0; i < salesReturnValueReportList.size(); i++) {
					salesReturnValueReport.put(i, salesReturnValueReportList.get(i));
					float totBillAmt = 0;
					float totGrnValue = 0;
					float totGvnValue = 0;
					for (int k = 0; k < salesReturnValueReportList.get(i).getSalesReturnQtyValueList().size(); k++) {
						
						float temp=salesReturnValueReportList.get(i).getSalesReturnQtyValueList().get(k).getGrandTotal()-(salesReturnValueReportList.get(i).getSalesReturnQtyValueList().get(k).getGrnQty()+salesReturnValueReportList.get(i).getSalesReturnQtyValueList().get(k).getGvnQty());
						
						totBillAmt = totBillAmt
								+ temp;
						totGrnValue = totGrnValue
								+ salesReturnValueReportList.get(i).getSalesReturnQtyValueList().get(k).getGrnQty();
						totGvnValue = totGvnValue
								+ salesReturnValueReportList.get(i).getSalesReturnQtyValueList().get(k).getGvnQty();
					}
					salesReturnValueReportList.get(i).setTotBillAmt(totBillAmt);
					salesReturnValueReportList.get(i).setTotGrnQty(totGrnValue);
					salesReturnValueReportList.get(i).setTotGvnQty(totGvnValue);
				}

				System.out.println("LIst===========" + salesReturnValueReportList.toString());

				model.addObject("salesReturnValueReport", salesReturnValueReport);
				model.addObject("subCatList", subCatList);

				// exportToExcel

				List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

				ExportToExcel expoExcel = new ExportToExcel();
				List<String> rowData = new ArrayList<String>();
				rowData.add("Sr.");
				rowData.add("Group Name");
				for (int i = 0; i < salesReturnValueReport.size(); i++) {
					rowData.add(salesReturnValueReport.get(i).getMonth() + " Total");
					rowData.add(salesReturnValueReport.get(i).getMonth() + "%");
				}
				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

				for (int k = 0; k < subCatList.size(); k++) {

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();
					rowData.add("" + (k + 1));
					rowData.add("" + subCatList.get(k).getSubCatName());
					for (int i = 0; i < salesReturnValueReport.size(); i++) {
						for (int j = 0; j < salesReturnValueReport.get(i).getSalesReturnQtyValueList().size(); j++) {

							if (salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j)
									.getSubCatId() == subCatList.get(k).getSubCatId()) {

								rowData.add("" + roundUp(salesReturnValueReport.get(i).getSalesReturnQtyValueList()
										.get(j).getGrandTotal()
										- (salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j).getGvnQty()
												+ salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j)
														.getGrnQty())));

								if (salesReturnValueReport.get(i).getTotBillAmt() > 0) {
									rowData.add("" + roundUp(((salesReturnValueReport.get(i)
											.getSalesReturnQtyValueList().get(j).getGrandTotal()
											- (salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j)
													.getGvnQty()
													+ salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j)
															.getGrnQty()))
											* 100) / salesReturnValueReport.get(i).getTotBillAmt()));
								} else {
									rowData.add("0.00");
								}
							}
						}
					}
					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);
				}

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				rowData.add("");
				rowData.add("Total");
				for (int i = 0; i < salesReturnValueReport.size(); i++) {
					rowData.add("" + salesReturnValueReport.get(i).getTotBillAmt());
					rowData.add("0.00");
				}

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

				System.err.println("exportToExcelList" + exportToExcelList.toString());
				HttpSession session = request.getSession();
				session.setAttribute("exportExcelList", exportToExcelList);
				session.setAttribute("excelName", "MonthlySalesPerContrReport");

			}
		} catch (Exception e) {

		}
		// }
		return model;

	}

	@RequestMapping(value = "/showMonthlySalesValueWiseReport", method = RequestMethod.GET)
	public ModelAndView showMonthlySalesValueWiseReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		/*
		 * HttpSession session = request.getSession();
		 * 
		 * List<ModuleJson> newModuleList = (List<ModuleJson>)
		 * session.getAttribute("newModuleList"); Info view =
		 * AccessControll.checkAccess("showSaleReportByDate", "showSaleReportByDate",
		 * "1", "0", "0", "0", newModuleList); if (view.getError() == true) { model =
		 * new ModelAndView("accessDenied");
		 * 
		 * } else {
		 */
		model = new ModelAndView("reports/sales/monthwisesalesreturnvalue");
		RestTemplate restTemplate = new RestTemplate();

		try {
			String year = request.getParameter("year");

			if (year != "") {
				String[] yrs = year.split("-"); // returns an array with the 2 parts

				MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

				map.add("fromYear", yrs[0]);
				map.add("toYear", yrs[1]);

				SalesReturnValueDaoList[] salesReturnValueReportListRes = restTemplate.postForObject(
						Constants.url + "getSalesReturnValueReport", map, SalesReturnValueDaoList[].class);

				List<SalesReturnValueDaoList> salesReturnValueReportList = new ArrayList<SalesReturnValueDaoList>(
						Arrays.asList(salesReturnValueReportListRes));

				SubCategory[] subCategoryList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				ArrayList<SubCategory> subCatList = new ArrayList<SubCategory>(Arrays.asList(subCategoryList));

				LinkedHashMap<Integer, SalesReturnValueDaoList> salesReturnValueReport = new LinkedHashMap<>();

				for (int i = 0; i < salesReturnValueReportList.size(); i++) {
					salesReturnValueReport.put(i, salesReturnValueReportList.get(i));
					float totBillAmt = 0;
					float totGrnValue = 0;
					float totGvnValue = 0;
					for (int k = 0; k < salesReturnValueReportList.get(i).getSalesReturnQtyValueList().size(); k++) {
						totBillAmt = totBillAmt
								+ salesReturnValueReportList.get(i).getSalesReturnQtyValueList().get(k).getGrandTotal();
						totGrnValue = totGrnValue
								+ salesReturnValueReportList.get(i).getSalesReturnQtyValueList().get(k).getGrnQty();
						totGvnValue = totGvnValue
								+ salesReturnValueReportList.get(i).getSalesReturnQtyValueList().get(k).getGvnQty();
					}
					salesReturnValueReportList.get(i).setTotBillAmt(totBillAmt);
					salesReturnValueReportList.get(i).setTotGrnQty(totGrnValue);
					salesReturnValueReportList.get(i).setTotGvnQty(totGvnValue);
				}

				model.addObject("salesReturnValueReport", salesReturnValueReport);
				model.addObject("subCatList", subCatList);

				// exportToExcel
				List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

				ExportToExcel expoExcel = new ExportToExcel();
				List<String> rowData = new ArrayList<String>();
				rowData.add("Sr.");
				rowData.add("Group Name");
				for (int i = 0; i < salesReturnValueReport.size(); i++) {
					rowData.add(salesReturnValueReport.get(i).getMonth() + " Gross Sale");
					rowData.add(salesReturnValueReport.get(i).getMonth() + " GVN Value");
					rowData.add(salesReturnValueReport.get(i).getMonth() + " GRN Value");
					rowData.add(salesReturnValueReport.get(i).getMonth() + " Total");
				}
				rowData.add("Total Gross Sale");
				rowData.add("Total GRN Value");
				rowData.add("Total GVN Value");
				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);
				float totBillAmt = 0.0f;
				float totGrnAmt = 0.0f;
				float totGvnAmt = 0.0f;
				for (int k = 0; k < subCatList.size(); k++) {

					float grandTotal = 0.0f;
					float grnQty = 0.0f;
					float gvnQty = 0.0f;

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();
					rowData.add("" + (k + 1));
					rowData.add("" + subCatList.get(k).getSubCatName());
					for (int i = 0; i < salesReturnValueReport.size(); i++) {
						for (int j = 0; j < salesReturnValueReport.get(i).getSalesReturnQtyValueList().size(); j++) {

							if (salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j)
									.getSubCatId() == subCatList.get(k).getSubCatId()) {
								rowData.add("" + roundUp(salesReturnValueReport.get(i).getSalesReturnQtyValueList()
										.get(j).getGrandTotal()));
								rowData.add("" + roundUp(
										salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j).getGvnQty()));
								rowData.add("" + roundUp(
										salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j).getGrnQty()));
								rowData.add("" + roundUp(salesReturnValueReport.get(i).getSalesReturnQtyValueList()
										.get(j).getGrandTotal()
										- (salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j).getGvnQty()
												+ salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j)
														.getGrnQty())));
								grandTotal = grandTotal + salesReturnValueReport.get(i).getSalesReturnQtyValueList()
										.get(j).getGrandTotal();
								grnQty = grnQty
										+ salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j).getGrnQty();
								gvnQty = gvnQty
										+ salesReturnValueReport.get(i).getSalesReturnQtyValueList().get(j).getGvnQty();
							}

						}
					}
					rowData.add("" + roundUp(grandTotal));
					rowData.add("" + roundUp(grnQty));
					rowData.add("" + roundUp(gvnQty));

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);
					totBillAmt = totBillAmt + grandTotal;
					totGrnAmt = totGrnAmt + grnQty;
					totGvnAmt = totGvnAmt + gvnQty;

				}

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				rowData.add("");
				rowData.add("Total");
				for (int i = 0; i < salesReturnValueReport.size(); i++) {
					rowData.add("" + roundUp(salesReturnValueReport.get(i).getTotBillAmt()));
					rowData.add("" + roundUp(salesReturnValueReport.get(i).getTotGvnQty()));
					rowData.add("" + roundUp(salesReturnValueReport.get(i).getTotGrnQty()));
					rowData.add(roundUp((salesReturnValueReport.get(i).getTotBillAmt()
							- (salesReturnValueReport.get(i).getTotGvnQty()
									+ salesReturnValueReport.get(i).getTotGrnQty())))
							+ "");
				}
				rowData.add("" + totBillAmt);
				rowData.add("" + totGrnAmt);
				rowData.add("" + totGvnAmt);
				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);
				System.err.println("exportToExcelList" + exportToExcelList.toString());
				HttpSession session = request.getSession();
				session.setAttribute("exportExcelList", exportToExcelList);
				session.setAttribute("excelName", "MonthlySalesReturnValueReport");

			}
		} catch (Exception e) {

		}
		// }
		return model;

	}

	// -----------------------------------------KG-Wise
	// Report---------------------------------------------
	@RequestMapping(value = "/showKgWiseReport", method = RequestMethod.GET)
	public ModelAndView showKgWiseReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		/*
		 * HttpSession session = request.getSession();
		 * 
		 * List<ModuleJson> newModuleList = (List<ModuleJson>)
		 * session.getAttribute("newModuleList"); Info view =
		 * AccessControll.checkAccess("showSaleRoyaltyByFr", "showSaleRoyaltyByFr", "1",
		 * "0", "0", "0", newModuleList);
		 * 
		 * if (view.getError() == true) {
		 * 
		 * model = new ModelAndView("accessDenied");
		 * 
		 * } else {
		 */
		model = new ModelAndView("reports/kgWiseReport");

		// Constants.mainAct =2;
		// Constants.subAct =20;

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");
			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);

			RestTemplate restTemplate = new RestTemplate();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}
			StringBuilder sbFrId = new StringBuilder();
			for (int i = 0; i < allFrIdNameList.getFrIdNamesList().size(); i++) {

				sbFrId = sbFrId.append(allFrIdNameList.getFrIdNamesList().get(i).getFrId() + ",");

			}

			String strFrId = sbFrId.toString();
			strFrId = strFrId.substring(0, strFrId.length() - 1);
			System.out.println("fr Id  = " + strFrId);
			model.addObject("todaysDate", todaysDate);
			model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());
			model.addObject("frId", strFrId);

		} catch (Exception e) {

			System.out.println("Exc in KG wise Report" + e.getMessage());
			e.printStackTrace();
		}
		// }
		return model;

	}

	// getSpKgWiseList
	@RequestMapping(value = "/getSpKgWiseList", method = RequestMethod.GET)
	public @ResponseBody SpKgSummaryDaoResponse getSpKgWiseList(HttpServletRequest request,
			HttpServletResponse response) {

		String fromDate = "";
		String toDate = "";
		SpKgSummaryDaoResponse spKgSummaryDaoResponse = new SpKgSummaryDaoResponse();
		List<SpKgSummaryDao> spKgSummaryDaoList = null;
		try {
			String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();
			map.add("frId", selectedFr);
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));

			SpKgSummaryDao[] spKgSummaryDaoRes = restTemplate.postForObject(Constants.url + "getSpKgSummaryReport", map,
					SpKgSummaryDao[].class);

			spKgSummaryDaoList = new ArrayList<SpKgSummaryDao>(Arrays.asList(spKgSummaryDaoRes));
			TreeSet<Float> kgList = new TreeSet<Float>();
			for (SpKgSummaryDao spKgSummaryDao : spKgSummaryDaoList) {
				kgList.add(spKgSummaryDao.getSpSelectedWeight());
			}
			spKgSummaryDaoResponse.setKgList(kgList);
			spKgSummaryDaoResponse.setSummaryDaoList(spKgSummaryDaoList);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return spKgSummaryDaoResponse;
	}

	// ---------------------------------------------------------------------------------------------------
	@RequestMapping(value = "/showNonRegisteredFrTaxReport", method = RequestMethod.GET)
	public ModelAndView showNonRegisteredFrTaxReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();
		String fromDate = "";
		String toDate = "";
		/*
		 * List<ModuleJson> newModuleList = (List<ModuleJson>)
		 * session.getAttribute("newModuleList"); Info view =
		 * AccessControll.checkAccess("showTaxReport", "showTaxReport", "1", "0", "0",
		 * "0", newModuleList);
		 * 
		 * if (view.getError() == true) {
		 * 
		 * model = new ModelAndView("accessDenied");
		 * 
		 * } else {
		 */
		model = new ModelAndView("reports/tax/nonregfrtax");
		List<NonRegFrTaxDao> taxReportList = null;

		try {

			RestTemplate restTemplate = new RestTemplate();

			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");

			if (fromDate == null && toDate == null) {

			} else {
				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
				map.add("fromDate", fromDate);
				map.add("toDate", toDate);

				ParameterizedTypeReference<List<NonRegFrTaxDao>> typeRef = new ParameterizedTypeReference<List<NonRegFrTaxDao>>() {
				};
				ResponseEntity<List<NonRegFrTaxDao>> responseEntity = restTemplate.exchange(
						Constants.url + "getNonRegFrTaxReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				taxReportList = responseEntity.getBody();
				model.addObject("taxReportList", taxReportList);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);

				// exportToExcel
				List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

				ExportToExcel expoExcel = new ExportToExcel();
				List<String> rowData = new ArrayList<String>();

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();
				rowData.add("Sr.No.");
				rowData.add("Franchise");
				rowData.add("Tax%");
				rowData.add("Bill Taxable");
				rowData.add("Bill SGST");
				rowData.add("BILL CGST");
				// rowData.add("BILL IGST");
				rowData.add("BILL Total");
				rowData.add("CRN Taxable");
				rowData.add("CRN SGST");
				rowData.add("CRN CGST");
				// rowData.add("CRN IGST");
				rowData.add("CRN Total");
				rowData.add("Taxable Amt");
				rowData.add("SGST");
				rowData.add("CGST");
				// rowData.add("IGST");
				rowData.add("Grand Amt");

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

				float billTaxableAmt = 0.0f;
				float billSgstAmt = 0.0f;
				float billCgstAmt = 0.0f;
				float billIgstAmt = 0.0f;
				float billGrandAmt = 0.0f;
				float crnTaxableAmt = 0.0f;
				float crnSgstAmt = 0.0f;
				float crnCgstAmt = 0.0f;
				float crnIgstAmt = 0.0f;
				float crnGrandAmt = 0.0f;
				float actTaxableAmtTot = 0.0f;
				float actSgstAmtTot = 0.0f;
				float actCgstAmtTot = 0.0f;
				float actIgstAmtTot = 0.0f;
				float actGrandAmtTot = 0.0f;

				for (int i = 0; i < taxReportList.size(); i++) {

					billTaxableAmt = billTaxableAmt + taxReportList.get(i).getBillTaxableAmt();
					billSgstAmt = billSgstAmt + taxReportList.get(i).getBillSgstAmt();
					billCgstAmt = billCgstAmt + taxReportList.get(i).getBillCgstAmt();
					billIgstAmt = billIgstAmt + taxReportList.get(i).getBillIgstAmt();
					billGrandAmt = billGrandAmt + taxReportList.get(i).getBillGrandAmt();
					crnTaxableAmt = crnTaxableAmt + taxReportList.get(i).getCrnTaxableAmt();
					crnSgstAmt = crnSgstAmt + taxReportList.get(i).getCrnSgstAmt();
					crnCgstAmt = crnCgstAmt + taxReportList.get(i).getCrnCgstAmt();
					crnIgstAmt = crnIgstAmt + taxReportList.get(i).getCrnIgstAmt();
					crnGrandAmt = crnGrandAmt + taxReportList.get(i).getCrnGrandAmt();
					float actTaxableAmt = taxReportList.get(i).getBillTaxableAmt()
							- taxReportList.get(i).getCrnTaxableAmt();
					float actSgstAmt = taxReportList.get(i).getBillSgstAmt() - taxReportList.get(i).getCrnSgstAmt();
					float actCgstAmt = taxReportList.get(i).getBillCgstAmt() - taxReportList.get(i).getCrnCgstAmt();
					float actIgstAmt = taxReportList.get(i).getBillIgstAmt() - taxReportList.get(i).getCrnIgstAmt();
					float actGrandAmt = taxReportList.get(i).getBillGrandAmt() - taxReportList.get(i).getCrnGrandAmt();
					actTaxableAmtTot = actTaxableAmtTot + actTaxableAmt;
					actSgstAmtTot = actSgstAmtTot + actSgstAmt;
					actCgstAmtTot = actCgstAmtTot + actCgstAmt;
					actIgstAmtTot = actIgstAmtTot + actIgstAmt;
					actGrandAmtTot = actGrandAmtTot + actGrandAmt;

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();
					rowData.add((i + 1) + "");
					rowData.add("" + taxReportList.get(i).getFrName());
					rowData.add("" + taxReportList.get(i).getTaxPer());
					rowData.add("" + taxReportList.get(i).getBillTaxableAmt());
					rowData.add("" + taxReportList.get(i).getBillSgstAmt());
					rowData.add("" + taxReportList.get(i).getBillCgstAmt());
					// rowData.add("" + taxReportList.get(i).getBillIgstAmt());
					rowData.add("" + taxReportList.get(i).getBillGrandAmt());
					rowData.add("" + taxReportList.get(i).getCrnTaxableAmt());
					rowData.add("" + taxReportList.get(i).getCrnSgstAmt());
					rowData.add("" + taxReportList.get(i).getCrnCgstAmt());
					// rowData.add("" + taxReportList.get(i).getCrnIgstAmt());
					rowData.add("" + taxReportList.get(i).getCrnGrandAmt());
					rowData.add("" + actTaxableAmt);
					rowData.add("" + actSgstAmt);
					rowData.add("" + actCgstAmt);
					// rowData.add("" +actIgstAmt);
					rowData.add("" + actGrandAmt);

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);

				}

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();

				rowData.add("Total");
				rowData.add("");
				rowData.add("");
				rowData.add("" + billTaxableAmt);
				rowData.add("" + billSgstAmt);
				rowData.add("" + billCgstAmt);
				// rowData.add(""+billIgstAmt);
				rowData.add("" + billGrandAmt);
				rowData.add("" + crnTaxableAmt);
				rowData.add("" + crnSgstAmt);
				rowData.add("" + crnCgstAmt);
				// rowData.add(""+crnIgstAmt);
				rowData.add("" + crnGrandAmt);
				rowData.add("" + actTaxableAmtTot);
				rowData.add("" + actSgstAmtTot);
				rowData.add("" + actCgstAmtTot);
				// rowData.add(""+actIgstAmtTot);
				rowData.add("" + actGrandAmtTot);

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

				session.setAttribute("exportExcelListNew", exportToExcelList);
				session.setAttribute("excelNameNew", "NonRegFrTaxReport");
				session.setAttribute("reportNameNew", "Non Registered Franchise Tax Report");
				session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
				session.setAttribute("mergeUpto1", "$A$1:$R$1");
				session.setAttribute("mergeUpto2", "$A$2:$R$2");
			}
		} catch (Exception e) {
			System.out.println("Exc in Tax Report" + e.getMessage());
			e.printStackTrace();
		}

		return model;

	}

	// ------------------------------------------------------------------------------------------------------
	private Dimension format = PD4Constants.A4;
	private boolean landscapeValue = false;
	private int topValue = 8;
	private int leftValue = 0;
	private int rightValue = 0;
	private int bottomValue = 8;
	private String unitsValue = "m";
	private String proxyHost = "";
	private int proxyPort = 0;

	private int userSpaceWidth = 750;
	private static int BUFFER_SIZE = 1024;

	@RequestMapping(value = "/pdfForReport", method = RequestMethod.GET)
	public void showPDF(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Inside PDf For Report URL ");
		String url = request.getParameter("url");
		System.out.println("URL " + url);

		File f = new File(Constants.SALES_REPORT_PATH);
		// File f = new File("/home/ats-12/Report.pdf");

		try {
			runConverter(Constants.ReportURL + url, f, request, response);
			// runConverter("www.google.com", f,request,response);

		} catch (IOException e) {

			System.out.println("Pdf conversion exception " + e.getMessage());
		}

		// get absolute path of the application
		ServletContext context = request.getSession().getServletContext();
		String appPath = context.getRealPath("");
		// String filePath = "/home/ats-12/Report.pdf";

		String filePath = Constants.SALES_REPORT_PATH;

		// construct the complete absolute path of the file
		String fullPath = appPath + filePath;
		File downloadFile = new File(filePath);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(downloadFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// get MIME type of the file
			String mimeType = context.getMimeType(fullPath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/pdf";
			}
			System.out.println("MIME type: " + mimeType);

			String headerKey = "Content-Disposition";

			// response.addHeader("Content-Disposition", "attachment;filename=report.pdf");
			response.setContentType("application/pdf");

			OutputStream outStream;

			outStream = response.getOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void runConverter(String urlstring, File output, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		if (urlstring.length() > 0) {
			if (!urlstring.startsWith("http://") && !urlstring.startsWith("file:")) {
				urlstring = "http://" + urlstring;
			}
			System.out.println("PDF URL " + urlstring);
			java.io.FileOutputStream fos = new java.io.FileOutputStream(output);

			PD4ML pd4ml = new PD4ML();

			try {

				Dimension landscapeA4 = pd4ml.changePageOrientation(PD4Constants.A4);
				pd4ml.setPageSize(landscapeA4);
				pd4ml.enableSmartTableBreaks(true);
				PD4PageMark footer = new PD4PageMark();

				footer.setPageNumberTemplate("Page $[page] of $[total]");
				footer.setPageNumberAlignment(PD4PageMark.RIGHT_ALIGN);
				footer.setFontSize(10);
				footer.setAreaHeight(20);

				pd4ml.setPageFooter(footer);

			} catch (Exception e) {
				System.out.println("Pdf conversion method excep " + e.getMessage());
			}

			if (unitsValue.equals("mm")) {
				pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));
			} else {
				pd4ml.setPageInsets(new Insets(topValue, leftValue, bottomValue, rightValue));
			}

			pd4ml.setHtmlWidth(userSpaceWidth);

			pd4ml.render(urlstring, fos);
		}
	}

	@RequestMapping(value = "/pdfForDisReport", method = RequestMethod.GET)
	public void showPDF1(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Inside PDf For Report URL ");
		String url = request.getParameter("url");
		System.out.println("URL " + url);

		File f = new File(Constants.SALES_REPORT_PATH);
		// File f = new File("/opt/apache-tomcat-8.5.6/webapps/uploads/report.pdf");
		// File f = new File("/home/ats-12/Report.pdf");

		try {
			runConverter1(Constants.ReportURL + url, f, request, response);
			// runConverter("www.google.com", f,request,response);

		} catch (IOException e) {

			System.out.println("Pdf conversion exception " + e.getMessage());
		}

		// get absolute path of the application
		ServletContext context = request.getSession().getServletContext();
		String appPath = context.getRealPath("");
		String filePath = Constants.SALES_REPORT_PATH;

		// String filePath = "/home/ats-12/Report.pdf";

		// construct the complete absolute path of the file
		String fullPath = appPath + filePath;
		File downloadFile = new File(filePath);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(downloadFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// get MIME type of the file
			String mimeType = context.getMimeType(fullPath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/pdf";
			}
			System.out.println("MIME type: " + mimeType);

			String headerKey = "Content-Disposition";

			// response.addHeader("Content-Disposition", "attachment;filename=report.pdf");
			response.setContentType("application/pdf");

			OutputStream outStream;

			outStream = response.getOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void runConverter1(String urlstring, File output, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		if (urlstring.length() > 0) {
			if (!urlstring.startsWith("http://") && !urlstring.startsWith("file:")) {
				urlstring = "http://" + urlstring;
			}
			System.out.println("PDF URL " + urlstring);
			java.io.FileOutputStream fos = new java.io.FileOutputStream(output);

			PD4ML pd4ml = new PD4ML();
			pd4ml.enableSmartTableBreaks(true);
			try {

				try {
					pd4ml.setPageSize(landscapeValue ? pd4ml.changePageOrientation(format) : format);
				} catch (Exception e) {
					e.printStackTrace();
				}

				/*
				 * Dimension landscapeA4 = pd4ml.changePageOrientation(PD4Constants.A4);
				 * pd4ml.setPageSize(landscapeA4);
				 */
				PD4PageMark footer = new PD4PageMark();

				footer.setPageNumberTemplate("Page $[page] of $[total]");
				footer.setPageNumberAlignment(PD4PageMark.RIGHT_ALIGN);
				footer.setFontSize(10);
				footer.setAreaHeight(20);

				pd4ml.setPageFooter(footer);

			} catch (Exception e) {
				System.out.println("Pdf conversion method excep " + e.getMessage());
			}

			if (unitsValue.equals("mm")) {
				pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));
			} else {
				pd4ml.setPageInsets(new Insets(topValue, leftValue, bottomValue, rightValue));
			}

			pd4ml.setHtmlWidth(userSpaceWidth);

			pd4ml.render(urlstring, fos);
		}
	}

	// Anmol-->26-11-2019--->SP_FLAVOUR_WISE_REPORT
	@RequestMapping(value = "/showFlavourWiseSPReport", method = RequestMethod.GET)
	public ModelAndView showFlavourWiseSPReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;

		model = new ModelAndView("reports/flavourWiseSpReport");

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");
			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);

			RestTemplate restTemplate = new RestTemplate();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}
			StringBuilder sbFrId = new StringBuilder();
			for (int i = 0; i < allFrIdNameList.getFrIdNamesList().size(); i++) {

				sbFrId = sbFrId.append(allFrIdNameList.getFrIdNamesList().get(i).getFrId() + ",");

			}

			String strFrId = sbFrId.toString();
			strFrId = strFrId.substring(0, strFrId.length() - 1);
			System.out.println("fr Id  = " + strFrId);
			model.addObject("todaysDate", todaysDate);
			model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());
			model.addObject("frId", strFrId);

		} catch (Exception e) {

			System.out.println("Exc in Flavour wise SP Report" + e.getMessage());
			e.printStackTrace();
		}

		return model;
	}

	// Anmol-->26-11-2019--->SP_FLAVOUR_WISE_REPORT_2
	@RequestMapping(value = "/showFlavourWiseSPReportFlvrSort", method = RequestMethod.GET)
	public ModelAndView showFlavourWiseSPReportFlvrSort(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;

		model = new ModelAndView("reports/flavourWiseSpReport2");

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");
			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);

			RestTemplate restTemplate = new RestTemplate();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}
			StringBuilder sbFrId = new StringBuilder();
			for (int i = 0; i < allFrIdNameList.getFrIdNamesList().size(); i++) {

				sbFrId = sbFrId.append(allFrIdNameList.getFrIdNamesList().get(i).getFrId() + ",");

			}

			String strFrId = sbFrId.toString();
			strFrId = strFrId.substring(0, strFrId.length() - 1);
			System.out.println("fr Id  = " + strFrId);
			model.addObject("todaysDate", todaysDate);
			model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());
			model.addObject("frId", strFrId);

		} catch (Exception e) {

			System.out.println("Exc in Flavour wise SP Report" + e.getMessage());
			e.printStackTrace();
		}

		return model;
	}

	// Anmol-->26-11-2019--->Ajax_Flavour_wise_Report_search--------------
	@RequestMapping(value = "/getFlavourWiseList", method = RequestMethod.GET)
	public @ResponseBody SpFlavourSummaryDaoResponse getFlavourWiseList(HttpServletRequest request,
			HttpServletResponse response) {

		String fromDate = "";
		String toDate = "";
		SpFlavourSummaryDaoResponse spFlavourSummaryDaoResponse = new SpFlavourSummaryDaoResponse();
		List<SpFlavourSummaryDao> spFlavourSummaryDaoList = null;
		try {
			String selectedFr = request.getParameter("fr_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();
			map.add("frId", selectedFr);
			map.add("fromDate", DateConvertor.convertToYMD(fromDate));
			map.add("toDate", DateConvertor.convertToYMD(toDate));

			System.err.println("FR IDS ---------------- " + selectedFr);
			System.err.println("DATE ---------------- " + DateConvertor.convertToYMD(fromDate) + "   to    "
					+ DateConvertor.convertToYMD(toDate));

			SpFlavourSummaryDao[] spFlavourSummaryDaoRes = restTemplate
					.postForObject(Constants.url + "getSpFlavourSummaryReport", map, SpFlavourSummaryDao[].class);

			spFlavourSummaryDaoList = new ArrayList<SpFlavourSummaryDao>(Arrays.asList(spFlavourSummaryDaoRes));

			System.err.println("FLAVOUR WISE REPORT --------  " + spFlavourSummaryDaoList);

			TreeSet<Integer> flavourIdList = new TreeSet<Integer>();
			for (SpFlavourSummaryDao spFlvrSummaryDao : spFlavourSummaryDaoList) {
				flavourIdList.add(spFlvrSummaryDao.getSpFlavourId());
			}
			spFlavourSummaryDaoResponse.setFlavourIdList(flavourIdList);
			spFlavourSummaryDaoResponse.setSummaryDaoList(spFlavourSummaryDaoList);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return spFlavourSummaryDaoResponse;
	}

}
