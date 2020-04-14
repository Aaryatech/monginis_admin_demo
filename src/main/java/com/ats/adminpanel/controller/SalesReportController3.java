package com.ats.adminpanel.controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.adminpanel.commons.AccessControll;
import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.commons.DateConvertor;
import com.ats.adminpanel.model.AllFrIdName;
import com.ats.adminpanel.model.AllFrIdNameList;
import com.ats.adminpanel.model.ExportToExcel;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.billing.Company;
import com.ats.adminpanel.model.franchisee.SalesFrListSummery;
import com.ats.adminpanel.model.franchisee.SalesReportFranchisee;
import com.ats.adminpanel.model.franchisee.SubCategory;
import com.ats.adminpanel.model.item.CategoryListResponse;
import com.ats.adminpanel.model.item.MCategoryList;
import com.ats.adminpanel.model.salesreport2.SubCatFrReport;
import com.ats.adminpanel.model.salesreport2.SubCatFrReportList;
import com.ats.adminpanel.model.salesreport3.FrWiseItemSaleList;
import com.ats.adminpanel.model.salesreport3.ItemWiseFrList;
import com.ats.adminpanel.model.salesreport3.SubCatWiseItemList;
import com.ats.adminpanel.model.salesreport3.TempFrWiseSubCat;
import com.ats.adminpanel.model.salesreport3.TempSubCatWiseBillData;
import com.ats.adminpanel.model.salesreport3.YearlySaleData;

@Controller
@Scope("session")
public class SalesReportController3 {

	String todaysDate;
	ArrayList<SubCategory> subCatAList;

	AllFrIdNameList allFrIdNameList = new AllFrIdNameList();
	List<MCategoryList> mCategoryList;

	@RequestMapping(value = "/showFrWiseItemSaleReport", method = RequestMethod.GET)
	public ModelAndView showFrWiseItemSaleReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showFrWiseItemSaleReport", "showFrWiseItemSaleReport", "1", "0", "0",
				"0", newModuleList);

		model = new ModelAndView("reports/sales/frWiseItemSold");

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);

			RestTemplate restTemplate = new RestTemplate();

			SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
					SubCategory[].class);

			subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));

			model.addObject("todaysDate", todaysDate);
			model.addObject("subCatList", subCatAList);

		} catch (Exception e) {

			System.out.println("Exc in showFrWiseItemSaleReport--  " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	// --------AJAX-----------

	FrWiseItemSaleList frWiseItemSaleList = new FrWiseItemSaleList();

	@RequestMapping(value = "/getFrWiseItemSaleReport", method = RequestMethod.GET)
	public @ResponseBody List<FrWiseItemSaleList> getFrwiseItemSaleReport(HttpServletRequest request,
			HttpServletResponse response) {

		List<SubCategory> subCatList = new ArrayList<>();
		List<FrWiseItemSaleList> result = new ArrayList<>();

		String fromDate = "";
		String toDate = "";
		String reportType = "";
		try {
			String selectedSubCat = request.getParameter("sc_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			reportType = request.getParameter("reportType");

			selectedSubCat = selectedSubCat.substring(1, selectedSubCat.length() - 1);
			selectedSubCat = selectedSubCat.replaceAll("\"", "");

			List<String> subCatIdList = new ArrayList<>();
			subCatIdList = Arrays.asList(selectedSubCat);

			System.err.println("SC ID LIST ----- " + subCatIdList);

			if (subCatIdList.contains("-1")) {
				// subCatIdList.clear();
				List<String> tempScIdList = new ArrayList();

				if (subCatAList != null) {

					for (int i = 0; i < subCatAList.size(); i++) {
						tempScIdList.add(String.valueOf(subCatAList.get(i).getSubCatId()));
					}
				}
				System.err.println("SUB CAT ID ARRAY --------- " + tempScIdList);
				selectedSubCat = tempScIdList.toString().substring(1, tempScIdList.toString().length() - 1);
				selectedSubCat = selectedSubCat.replaceAll("\"", "");

			}

			System.out.println("selectedSubCatAfter------------------  " + selectedSubCat);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			System.out.println("Inside If all Sub Cat Selected ");

			List<Integer> ids = new ArrayList();
			ids.add(1);
			ids.add(2);
			ids.add(3);
			ids.add(4);
			ids.add(5);

			String frDt = "", toDt = "";
			try {
				frDt = DateConvertor.convertToYMD(fromDate);
				toDt = DateConvertor.convertToYMD(toDate);
			} catch (Exception e) {
			}

			map.add("fromDate", frDt);
			map.add("toDate", toDt);
			map.add("subCatIdList", selectedSubCat);

			ParameterizedTypeReference<List<FrWiseItemSaleList>> typeRef = new ParameterizedTypeReference<List<FrWiseItemSaleList>>() {
			};
			ResponseEntity<List<FrWiseItemSaleList>> responseEntity = restTemplate.exchange(
					Constants.url + "getFrWiseItemSoldReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			// result = restTemplate.postForObject(Constants.url +
			// "getFrWiseItemSoldReport", map, List.class);

			result = responseEntity.getBody();

			System.err.println("REPORT --------------------- " + result);

		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			e.printStackTrace();

		}

		try {

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add(" ");
			rowData.add("Sold Qty");
			rowData.add("Sold Amt");
			rowData.add("Var Qty");
			rowData.add("Var Amt");
			rowData.add("Ret Qty");
			rowData.add("Ret Amt");
			rowData.add("Net Qty");
			rowData.add("Net Amt");
			rowData.add("Ret Amt %");

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

			for (int j = 0; j < result.size(); j++) {

				FrWiseItemSaleList sc = result.get(j);
				System.err.println("SUB CAT ---------------------------- " + sc.getSubCatName());

				int scTotalQty = 0;
				float scTotalAmt = 0.0f;
				int scTotalVarQty = 0;
				float scTotalVarAmt = 0.0f;
				int scTotalRetQty = 0;
				float scTotalRetAmt = 0.0f;
				int scTotalNetQty = 0;
				float scTotalNetAmt = 0.0f;
				float scTotalRetAmtPer = 0.0f;

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();

				rowData.add("" + sc.getSubCatName());
				rowData.add("");
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

				for (int i = 0; i < result.get(j).getItemList().size(); i++) {

					SubCatWiseItemList item = result.get(j).getItemList().get(i);

					int itemTotalQty = 0;
					float itemTotalAmt = 0.0f;
					int itemTotalVarQty = 0;
					float itemTotalVarAmt = 0.0f;
					int itemTotalRetQty = 0;
					float itemTotalRetAmt = 0.0f;
					int itemTotalNetQty = 0;
					float itemTotalNetAmt = 0.0f;
					float itemTotalRetAmtPer = 0.0f;

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();

					rowData.add("" + item.getItemName());
					rowData.add("");
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

					for (int f = 0; f < item.getFrList().size(); f++) {

						ItemWiseFrList fr = item.getFrList().get(f);

						int netQty = fr.getFrSoldQty() + (fr.getFrVarQty() + fr.getFrRetQty());
						float netAmt = fr.getFrSoldAmt() + (fr.getFrVarAmt() + fr.getFrRetAmt());
						float netTaxableAmt = fr.getFrTaxableAmt()
								+ (fr.getFrVarTaxableAmt() + fr.getFrRetTaxableAmt());
						float retAmtPer = ((fr.getFrVarAmt() + fr.getFrRetAmt()) * 100 / fr.getFrSoldAmt());
						float retTaxableAmtPer = ((fr.getFrVarTaxableAmt() + fr.getFrRetTaxableAmt()) * 100
								/ fr.getFrTaxableAmt());

						expoExcel = new ExportToExcel();
						rowData = new ArrayList<String>();

						if (reportType.equalsIgnoreCase("1")) {

							rowData.add("" + fr.getFrName());
							rowData.add("" + fr.getFrSoldQty());
							rowData.add("" + fr.getFrSoldAmt());
							rowData.add("" + fr.getFrVarQty());
							rowData.add("" + fr.getFrVarAmt());
							rowData.add("" + fr.getFrRetQty());
							rowData.add("" + fr.getFrRetAmt());
							rowData.add("" + netQty);
							rowData.add("" + netAmt);
							rowData.add("" + retAmtPer + "%");

						} else if (reportType.equalsIgnoreCase("2")) {

							rowData.add("" + fr.getFrName());
							rowData.add("" + fr.getFrSoldQty());
							rowData.add("");
							rowData.add("" + fr.getFrVarQty());
							rowData.add("");
							rowData.add("" + fr.getFrRetQty());
							rowData.add("");
							rowData.add("" + netQty);
							rowData.add("");
							rowData.add("");

						} else if (reportType.equalsIgnoreCase("3")) {

							rowData.add("" + fr.getFrName());
							rowData.add("");
							rowData.add("" + fr.getFrSoldAmt());
							rowData.add("");
							rowData.add("" + fr.getFrVarAmt());
							rowData.add("");
							rowData.add("" + fr.getFrRetAmt());
							rowData.add("");
							rowData.add("" + netAmt);
							rowData.add("" + retAmtPer);

						} else if (reportType.equalsIgnoreCase("4")) {

							rowData.add("" + fr.getFrName());
							rowData.add("" + fr.getFrSoldQty());
							rowData.add("" + fr.getFrTaxableAmt());
							rowData.add("" + fr.getFrVarQty());
							rowData.add("" + fr.getFrVarTaxableAmt());
							rowData.add("" + fr.getFrRetQty());
							rowData.add("" + fr.getFrRetTaxableAmt());
							rowData.add("" + netQty);
							rowData.add("" + netTaxableAmt);
							rowData.add("" + retTaxableAmtPer + "%");

						}

						if (reportType.equalsIgnoreCase("4")) {
							itemTotalQty = itemTotalQty + fr.getFrSoldQty();
							itemTotalAmt = itemTotalAmt + fr.getFrTaxableAmt();
							itemTotalVarQty = itemTotalVarQty + fr.getFrVarQty();
							itemTotalVarAmt = itemTotalVarAmt + fr.getFrVarTaxableAmt();
							itemTotalRetQty = itemTotalRetQty + fr.getFrRetQty();
							itemTotalRetAmt = itemTotalRetAmt + fr.getFrRetTaxableAmt();
							itemTotalNetQty = itemTotalNetQty + netQty;
							itemTotalNetAmt = itemTotalNetAmt + netTaxableAmt;
							itemTotalRetAmtPer = itemTotalRetAmtPer + retTaxableAmtPer;
						} else {
							itemTotalQty = itemTotalQty + fr.getFrSoldQty();
							itemTotalAmt = itemTotalAmt + fr.getFrSoldAmt();
							itemTotalVarQty = itemTotalVarQty + fr.getFrVarQty();
							itemTotalVarAmt = itemTotalVarAmt + fr.getFrVarAmt();
							itemTotalRetQty = itemTotalRetQty + fr.getFrRetQty();
							itemTotalRetAmt = itemTotalRetAmt + fr.getFrRetAmt();
							itemTotalNetQty = itemTotalNetQty + netQty;
							itemTotalNetAmt = itemTotalNetAmt + netAmt;
							itemTotalRetAmtPer = itemTotalRetAmtPer + retAmtPer;

						}

						expoExcel.setRowData(rowData);
						exportToExcelList.add(expoExcel);

					}

					// ------------ITEM TOTAL-----------------------
					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();

					if (reportType.equalsIgnoreCase("1")) {

						rowData.add("" + item.getItemName());
						rowData.add("" + itemTotalQty);
						rowData.add("" + itemTotalAmt);
						rowData.add("" + itemTotalVarQty);
						rowData.add("" + itemTotalVarAmt);
						rowData.add("" + itemTotalRetQty);
						rowData.add("" + itemTotalRetAmt);
						rowData.add("" + itemTotalNetQty);
						rowData.add("" + itemTotalNetAmt);
						rowData.add("" + itemTotalRetAmtPer + "%");

					} else if (reportType.equalsIgnoreCase("2")) {

						rowData.add("" + item.getItemName());
						rowData.add("" + itemTotalQty);
						rowData.add("");
						rowData.add("" + itemTotalVarQty);
						rowData.add("");
						rowData.add("" + itemTotalRetQty);
						rowData.add("");
						rowData.add("" + itemTotalNetQty);
						rowData.add("");
						rowData.add("");

					} else if (reportType.equalsIgnoreCase("3")) {

						rowData.add("" + item.getItemName());
						rowData.add("");
						rowData.add("" + itemTotalAmt);
						rowData.add("");
						rowData.add("" + itemTotalVarAmt);
						rowData.add("");
						rowData.add("" + itemTotalRetAmt);
						rowData.add("");
						rowData.add("" + itemTotalNetAmt);
						rowData.add("" + itemTotalRetAmtPer + "%");

					} else if (reportType.equalsIgnoreCase("4")) {

						rowData.add("" + item.getItemName());
						rowData.add("" + itemTotalQty);
						rowData.add("" + itemTotalAmt);
						rowData.add("" + itemTotalVarQty);
						rowData.add("" + itemTotalVarAmt);
						rowData.add("" + itemTotalRetQty);
						rowData.add("" + itemTotalRetAmt);
						rowData.add("" + itemTotalNetQty);
						rowData.add("" + itemTotalNetAmt);
						rowData.add("" + itemTotalRetAmtPer + "%");

					}

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);

					/*
					 * expoExcel = new ExportToExcel(); rowData = new ArrayList<String>();
					 * 
					 * rowData.add(""); rowData.add(""); rowData.add(""); rowData.add("");
					 * rowData.add(""); rowData.add(""); rowData.add(""); rowData.add("");
					 * rowData.add(""); rowData.add("");
					 * 
					 * expoExcel.setRowData(rowData); exportToExcelList.add(expoExcel);
					 */

					scTotalQty = scTotalQty + itemTotalQty;
					scTotalAmt = scTotalAmt + itemTotalAmt;
					scTotalVarQty = scTotalVarQty + itemTotalVarQty;
					scTotalVarAmt = scTotalVarAmt + itemTotalVarAmt;
					scTotalRetQty = scTotalRetQty + itemTotalRetQty;
					scTotalRetAmt = scTotalRetAmt + itemTotalRetAmt;
					scTotalNetQty = scTotalNetQty + itemTotalNetQty;
					scTotalNetAmt = scTotalNetAmt + itemTotalNetAmt;
					scTotalRetAmtPer = scTotalRetAmtPer + itemTotalRetAmtPer;
				}

				// ------------SUB CAT TOTAL------------
				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();

				if (reportType.equalsIgnoreCase("1")) {

					rowData.add("" + sc.getSubCatName());
					rowData.add("" + scTotalQty);
					rowData.add("" + scTotalAmt);
					rowData.add("" + scTotalVarQty);
					rowData.add("" + scTotalVarAmt);
					rowData.add("" + scTotalRetQty);
					rowData.add("" + scTotalRetAmt);
					rowData.add("" + scTotalNetQty);
					rowData.add("" + scTotalNetAmt);
					rowData.add("" + scTotalRetAmtPer + "%");

				} else if (reportType.equalsIgnoreCase("2")) {

					rowData.add("" + sc.getSubCatName());
					rowData.add("" + scTotalQty);
					rowData.add("");
					rowData.add("" + scTotalVarQty);
					rowData.add("");
					rowData.add("" + scTotalRetQty);
					rowData.add("");
					rowData.add("" + scTotalNetQty);
					rowData.add("");
					rowData.add("");

				} else if (reportType.equalsIgnoreCase("3")) {

					rowData.add("" + sc.getSubCatName());
					rowData.add("");
					rowData.add("" + scTotalAmt);
					rowData.add("");
					rowData.add("" + scTotalVarAmt);
					rowData.add("");
					rowData.add("" + scTotalRetAmt);
					rowData.add("");
					rowData.add("" + scTotalNetAmt);
					rowData.add("" + scTotalRetAmtPer + "%");

				} else if (reportType.equalsIgnoreCase("4")) {

					rowData.add("" + sc.getSubCatName());
					rowData.add("" + scTotalQty);
					rowData.add("" + scTotalAmt);
					rowData.add("" + scTotalVarQty);
					rowData.add("" + scTotalVarAmt);
					rowData.add("" + scTotalRetQty);
					rowData.add("" + scTotalRetAmt);
					rowData.add("" + scTotalNetQty);
					rowData.add("" + scTotalNetAmt);
					rowData.add("" + scTotalRetAmtPer + "%");

				}

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

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
				rowData.add("");

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);

			}

			// rowData.add("" + roundUp(drTotalAmt - crTotalAmt));

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelListNew", exportToExcelList);
			session.setAttribute("excelNameNew", "ItemSaleReportFranchiseeWise");
			session.setAttribute("reportNameNew", "Item Group -> Item -> Shop wise Summary Report");
			session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
			session.setAttribute("mergeUpto1", "$A$1:$J$1");
			session.setAttribute("mergeUpto2", "$A$2:$J$2");

		} catch (Exception e) {
			System.err.println("--------------EXC - ");
			e.printStackTrace();
		}

		return result;
	}

	@RequestMapping(value = "pdf/showFrWiseItemSalePdf/{fromDate}/{toDate}/{selectedSC}/{selectedType} ", method = RequestMethod.GET)
	public ModelAndView showFrWiseItemSalePdf(@PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String selectedSC, @PathVariable int selectedType, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/pdf/frWiseItemSalePdf");

		List<FrWiseItemSaleList> result = new ArrayList();

		try {

			// String selectedSubCat="";

			System.out.println("selectedSubCatBefore------------------" + selectedSC);

			List<String> subCatIdList = new ArrayList<>();
			subCatIdList = Arrays.asList(selectedSC);

			System.err.println("SC ID LIST ----- " + subCatIdList);

			if (subCatIdList.contains("-1")) {
				// subCatIdList.clear();
				List<String> tempScIdList = new ArrayList();

				if (subCatAList != null) {

					for (int i = 0; i < subCatAList.size(); i++) {
						tempScIdList.add(String.valueOf(subCatAList.get(i).getSubCatId()));
					}
				}
				System.err.println("SUB CAT ID ARRAY --------- " + tempScIdList);
				selectedSC = tempScIdList.toString().substring(1, tempScIdList.toString().length() - 1);
				selectedSC = selectedSC.replaceAll("\"", "");

			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			System.out.println("Inside If all Sub Cat Selected ");

			List<Integer> ids = new ArrayList();
			ids.add(1);
			ids.add(2);
			ids.add(3);
			ids.add(4);
			ids.add(5);

			String frDt = "", toDt = "";
			try {
				frDt = DateConvertor.convertToYMD(fromDate);
				toDt = DateConvertor.convertToYMD(toDate);
			} catch (Exception e) {
			}

			map.add("fromDate", frDt);
			map.add("toDate", toDt);
			map.add("subCatIdList", selectedSC);

			result = restTemplate.postForObject(Constants.url + "getFrWiseItemSoldReport", map, List.class);

			System.err.println("REPORT --------------------- " + result);

			model.addObject("reportList", result);
			model.addObject("FACTORYNAME", Constants.FACTORYNAME);
			model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
			model.addObject("fromDate", fromDate);
			model.addObject("toDate", toDate);
			model.addObject("report_type", selectedType);

		} catch (Exception e) {
			System.out.println("get sale Report Bill Wise " + e.getMessage());
			e.printStackTrace();

		}

		return model;
	}

	// -----------------YEARLY
	// REPORT--------------------------------------------------

	@RequestMapping(value = "/showYearlyFrSubCatSaleReport", method = RequestMethod.GET)
	public ModelAndView showYearlyFrSubCatSaleReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		model = new ModelAndView("reports/sales/yearlyFrSubCatSale");

		String yearStartDate = "";

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);
			yearStartDate = date.format(formatters);
			;

			RestTemplate restTemplate = new RestTemplate();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}

			try {
				Company company = restTemplate.getForObject(Constants.url + "/getCompany", Company.class);
				yearStartDate = DateConvertor.convertToDMY(company.getFromDate());
			} catch (Exception e) {
				e.printStackTrace();
			}

			model.addObject("yearStartDate", yearStartDate);
			model.addObject("todaysDate", todaysDate);
			model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

			CategoryListResponse categoryListResponse;

			categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
					CategoryListResponse.class);

			mCategoryList = categoryListResponse.getmCategoryList();

			model.addObject("mCategoryList", mCategoryList);

			SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
					SubCategory[].class);

			subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));

		} catch (Exception e) {

			System.out.println("Exc in showYearlyFrSubCatSaleReport--  " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	// ----AJAX----GET SUB CATEGORY LIST BY CAT ID-------------------

	@RequestMapping(value = "/getSubCatListByCatId", method = RequestMethod.GET)
	public @ResponseBody List<SubCategory> getSubCatByCatIdForReport(HttpServletRequest request,
			HttpServletResponse response) {

		List<SubCategory> subCatList = new ArrayList<SubCategory>();
		try {
			RestTemplate restTemplate = new RestTemplate();
			String selectedCat = request.getParameter("catId");
			boolean isAllCatSelected = false;

			System.out.println(
					"System.out.println(selectedCat);System.out.println(selectedCat);System.out.println(selectedCat);"
							+ selectedCat);

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			} else {
				selectedCat = selectedCat.substring(1, selectedCat.length() - 1);
				selectedCat = selectedCat.replaceAll("\"", "");
			}

			System.out.println(selectedCat);
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("catId", selectedCat);
			map.add("isAllCatSelected", isAllCatSelected);

			subCatList = restTemplate.postForObject(Constants.url + "getSubCatListByCatIdInForDisp", map, List.class);
			System.out.println(subCatList.toString());

		} catch (Exception e) {

		}

		return subCatList;
	}

	// -------AJAX---------FR LIST-------------

	@RequestMapping(value = "/getAllFrList", method = RequestMethod.GET)
	@ResponseBody
	public List<AllFrIdName> getFrListofAllFrForFrSummery(HttpServletRequest request, HttpServletResponse response) {

		return allFrIdNameList.getFrIdNamesList();

	}

	// ----AJAX-------------SEARCH YEARLY REPORT-------------

	@RequestMapping(value = "/getYearlyFrSubCatSaleReport", method = RequestMethod.GET)
	public @ResponseBody List<YearlySaleData> getYearlyFrSubCatSaleReport(HttpServletRequest request,
			HttpServletResponse response) {

		List<YearlySaleData> reportList = new ArrayList<>();

		String fromDate = "";
		String toDate = "";
		try {
			System.out.println("Inside get Sale Bill Wise");
			String selectedFr = request.getParameter("fr_id_list");
			String selectedSubCatIdList = request.getParameter("subCat_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");

			System.out.println("selectedFrBefore------------------" + selectedFr);

			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			selectedSubCatIdList = selectedSubCatIdList.substring(1, selectedSubCatIdList.length() - 1);
			selectedSubCatIdList = selectedSubCatIdList.replaceAll("\"", "");

			System.out.println("selectedFrAfter------------------" + selectedFr);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			System.out.println("Inside If all fr Selected ");

			String frDt = "", toDt = "";
			try {
				frDt = DateConvertor.convertToYMD(fromDate);
				toDt = DateConvertor.convertToYMD(toDate);
			} catch (Exception e) {
			}

			map.add("fromDate", frDt);
			map.add("toDate", toDt);
			map.add("frIdList", selectedFr);
			map.add("subCatIdList", selectedSubCatIdList);

			ParameterizedTypeReference<List<YearlySaleData>> typeRef = new ParameterizedTypeReference<List<YearlySaleData>>() {
			};
			ResponseEntity<List<YearlySaleData>> responseEntity = restTemplate.exchange(
					Constants.url + "getYearlyFrSubCatSaleReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			reportList = responseEntity.getBody();

			System.err.println("REPORT ------------------- " + reportList);

		} catch (Exception e) {
			System.out.println("get Yearly sale Report  " + e.getMessage());
			e.printStackTrace();

		}

		/*
		 * List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();
		 * 
		 * ExportToExcel expoExcel = new ExportToExcel(); List<String> rowData = new
		 * ArrayList<String>();
		 * 
		 * rowData.add("Sr"); rowData.add("Sub Category Name"); rowData.add("Sold Qty");
		 * rowData.add("Sold Amt"); rowData.add("Var Qty"); rowData.add("Var Amt");
		 * rowData.add("Ret Qty"); rowData.add("Ret Amt"); rowData.add("Net Qty");
		 * rowData.add("Net Amt"); rowData.add("Ret Per Amt");
		 * 
		 * expoExcel.setRowData(rowData); int srno = 1;
		 * exportToExcelList.add(expoExcel);
		 * 
		 * for (int j = 0; j < frListFinal.size(); j++) {
		 * 
		 * float totalSoldQty = 0; float totalSoldAmt = 0; float totalVarQty = 0; float
		 * totalVarAmt = 0; float totalRetQty = 0; float totalRetAmt = 0; float
		 * totalNetQty = 0; float totalNetAmt = 0; float retAmtPer = 0;
		 * 
		 * expoExcel = new ExportToExcel(); rowData = new ArrayList<String>();
		 * 
		 * rowData.add(""); rowData.add("" + frListFinal.get(j).getFrName());
		 * rowData.add(""); rowData.add(""); rowData.add(""); rowData.add("");
		 * rowData.add(""); rowData.add(""); rowData.add(""); rowData.add("");
		 * rowData.add("");
		 * 
		 * expoExcel.setRowData(rowData); exportToExcelList.add(expoExcel);
		 * 
		 * for (int i = 0; i < subCatFrReportList.size(); i++) {
		 * 
		 * if (frListFinal.get(j).getFrId() == subCatFrReportList.get(i).getFrId()) {
		 * 
		 * expoExcel = new ExportToExcel(); rowData = new ArrayList<String>();
		 * 
		 * totalSoldQty = totalSoldQty + subCatFrReportList.get(i).getSoldQty();
		 * totalSoldAmt = totalSoldAmt + subCatFrReportList.get(i).getSoldAmt();
		 * totalVarQty = totalVarQty + subCatFrReportList.get(i).getVarQty();
		 * totalVarAmt = totalVarAmt + subCatFrReportList.get(i).getVarAmt();
		 * totalRetQty = totalRetQty + subCatFrReportList.get(i).getRetQty();
		 * totalRetAmt = totalRetAmt + subCatFrReportList.get(i).getRetAmt();
		 * totalNetQty = totalNetQty + subCatFrReportList.get(i).getNetQty();
		 * totalNetAmt = totalNetAmt + subCatFrReportList.get(i).getNetQty(); retAmtPer
		 * = retAmtPer + subCatFrReportList.get(i).getRetAmtPer();
		 * 
		 * expoExcel = new ExportToExcel(); rowData = new ArrayList<String>();
		 * 
		 * rowData.add("" + srno);
		 * rowData.add(subCatFrReportList.get(i).getSubCatName());
		 * 
		 * rowData.add("" + roundUp(subCatFrReportList.get(i).getSoldQty()));
		 * rowData.add("" + roundUp(subCatFrReportList.get(i).getSoldQty()));
		 * rowData.add("" + roundUp(subCatFrReportList.get(i).getVarQty()));
		 * rowData.add("" + roundUp(subCatFrReportList.get(i).getVarAmt()));
		 * rowData.add("" + roundUp(subCatFrReportList.get(i).getRetQty()));
		 * 
		 * rowData.add("" + roundUp(subCatFrReportList.get(i).getRetAmt()));
		 * rowData.add("" + roundUp(subCatFrReportList.get(i).getNetQty()));
		 * rowData.add("" + roundUp(subCatFrReportList.get(i).getNetQty()));
		 * rowData.add("" + roundUp(subCatFrReportList.get(i).getRetAmtPer()));
		 * 
		 * srno = srno + 1;
		 * 
		 * expoExcel.setRowData(rowData); exportToExcelList.add(expoExcel); } }
		 * 
		 * expoExcel = new ExportToExcel(); rowData = new ArrayList<String>();
		 * rowData.add(" "); rowData.add("Total");
		 * 
		 * rowData.add("" + roundUp(totalSoldQty)); rowData.add("" +
		 * roundUp(totalSoldAmt)); rowData.add("" + roundUp(totalVarQty));
		 * rowData.add("" + roundUp(totalVarAmt)); rowData.add("" +
		 * roundUp(totalRetQty)); rowData.add("" + roundUp(totalRetAmt));
		 * 
		 * rowData.add("" + roundUp(totalNetQty)); rowData.add("" +
		 * roundUp(totalNetAmt)); rowData.add("" + roundUp(retAmtPer));
		 * 
		 * expoExcel.setRowData(rowData); exportToExcelList.add(expoExcel); }
		 * 
		 * HttpSession session = request.getSession();
		 * session.setAttribute("exportExcelListNew", exportToExcelList);
		 * session.setAttribute("excelNameNew", "SaleBillWiseDate");
		 * session.setAttribute("reportNameNew", "Bill-wise Report");
		 * session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: "
		 * + toDate + " "); session.setAttribute("mergeUpto1", "$A$1:$K$1");
		 * session.setAttribute("mergeUpto2", "$A$2:$K$2");
		 */

		return reportList;
	}

	@RequestMapping(value = "/displayYearlyReport", method = RequestMethod.GET)
	public ModelAndView displayYearlyReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;

		model = new ModelAndView("reports/sales/displayFrSubCatYearlyReport");

		List<YearlySaleData> reportList = new ArrayList<>();

		String fromDate = "";
		String toDate = "";
		String selectedType = "";

		try {

			String selectedFrArray[] = request.getParameterValues("selectFr");
			String selectedSubCatIdArray[] = request.getParameterValues("item_grp2");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			selectedType = request.getParameter("typeId");

			String selectedFr = "";
			StringBuilder sb = new StringBuilder();

			if (selectedFrArray.length > 0) {
				for (int i = 0; i < selectedFrArray.length; i++) {
					System.out.println("fr Ids List " + selectedFrArray[i]);

					sb = sb.append(selectedFrArray[i] + ",");
				}

				selectedFr = sb.toString();
				selectedFr = selectedFr.substring(0, selectedFr.length() - 1);
			}

			System.err.println("FR IDS -------------- " + selectedFr);

			String selectedSubCatIdList = "";
			StringBuilder sb1 = new StringBuilder();

			List<String> scIds = new ArrayList<>();
			scIds = Arrays.asList(selectedSubCatIdArray);

			if (scIds.contains("-1")) {
				// subCatIdList.clear();
				List<String> tempScIdList = new ArrayList();

				if (subCatAList != null) {

					for (int i = 0; i < subCatAList.size(); i++) {
						tempScIdList.add(String.valueOf(subCatAList.get(i).getSubCatId()));
					}
				}
				System.err.println("SUB CAT ID ARRAY --------- " + tempScIdList);
				selectedSubCatIdList = tempScIdList.toString().substring(1, tempScIdList.toString().length() - 1);
				selectedSubCatIdList = selectedSubCatIdList.replaceAll("\"", "");

			} else {

				if (selectedSubCatIdArray.length > 0) {
					for (int i = 0; i < selectedSubCatIdArray.length; i++) {
						System.out.println("sc Ids List " + selectedSubCatIdArray[i]);

						sb1 = sb1.append(selectedSubCatIdArray[i] + ",");
					}

					selectedSubCatIdList = sb1.toString();
					selectedSubCatIdList = selectedSubCatIdList.substring(0, selectedSubCatIdList.length() - 1);
				}
			}

			System.out.println("selectedFrAfter------------------" + selectedSubCatIdList);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();

			System.out.println("Inside If all fr Selected ");

			String frDt = "", toDt = "";
			try {
				frDt = DateConvertor.convertToYMD(fromDate);
				toDt = DateConvertor.convertToYMD(toDate);
			} catch (Exception e) {
			}

			map.add("fromDate", frDt);
			map.add("toDate", toDt);
			map.add("frIdList", selectedFr);
			map.add("subCatIdList", selectedSubCatIdList);

			ParameterizedTypeReference<List<YearlySaleData>> typeRef = new ParameterizedTypeReference<List<YearlySaleData>>() {
			};
			ResponseEntity<List<YearlySaleData>> responseEntity = restTemplate.exchange(
					Constants.url + "getYearlyFrSubCatSaleReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			reportList = new ArrayList<>();
			reportList = responseEntity.getBody();

			DecimalFormat df = new DecimalFormat("0.00");

			System.err.println("REPORT ------------------- " + reportList);

			if (reportList != null) {

				for (int i = 0; i < reportList.size(); i++) {

					float monthTotalSoldQty = 0;
					float monthTotalSoldAmt = 0;
					float monthTotalVarQty = 0;
					float monthTotalVarAmt = 0;
					float monthTotalRetQty = 0;
					float monthTotalRetAmt = 0;
					float monthTotalNetQty = 0;
					float monthTotalNetAmt = 0;
					float monthTotalRetAmtPer = 0;

					float monthTotalSoldTaxableAmt = 0;
					float monthTotalVarTaxableAmt = 0;
					float monthTotalRetTaxableAmt = 0;
					float monthTotalNetTaxableAmt = 0;
					float monthTotalRetTaxableAmtPer = 0;

					for (int j = 0; j < reportList.get(i).getDataList().size(); j++) {

						float frTotalSoldQty = 0;
						float frTotalSoldAmt = 0;
						float frTotalVarQty = 0;
						float frTotalVarAmt = 0;
						float frTotalRetQty = 0;
						float frTotalRetAmt = 0;
						float frTotalNetQty = 0;
						float frTotalNetAmt = 0;
						float frTotalRetAmtPer = 0;
						float frTotalSoldTaxableAmt = 0;
						float frTotalVarTaxableAmt = 0;
						float frTotalRetTaxableAmt = 0;
						float frTotalNetTaxableAmt = 0;
						float frTotalRetTaxableAmtPer = 0;

						TempFrWiseSubCat frData = reportList.get(i).getDataList().get(j);

						for (int k = 0; k < frData.getSubCatList().size(); k++) {

							TempSubCatWiseBillData data = frData.getSubCatList().get(k);

							frTotalSoldQty = frTotalSoldQty + data.getSoldQty();
							frTotalSoldAmt = frTotalSoldAmt + data.getSoldAmt();
							frTotalVarQty = frTotalVarQty + data.getVarQty();
							frTotalVarAmt = frTotalVarAmt + data.getVarAmt();
							frTotalRetQty = frTotalRetQty + data.getRetQty();
							frTotalRetAmt = frTotalRetAmt + data.getRetAmt();
							frTotalNetQty = frTotalNetQty + (data.getSoldQty() - (data.getVarQty() + data.getRetQty()));
							frTotalNetAmt = frTotalNetAmt + (data.getSoldAmt() - (data.getVarAmt() + data.getRetAmt()));

							if (data.getSoldAmt() == 0) {
								frTotalRetAmtPer = frTotalRetAmtPer + 0;

							} else {
								frTotalRetAmtPer = frTotalRetAmtPer
										+ (((data.getVarAmt() + data.getRetAmt()) * 100) / data.getSoldAmt());
							}

							frTotalSoldTaxableAmt = frTotalSoldTaxableAmt + data.getTaxableAmt();
							frTotalVarTaxableAmt = frTotalVarTaxableAmt + data.getVarTaxableAmt();
							frTotalRetTaxableAmt = frTotalRetTaxableAmt + data.getRetTaxableAmt();
							frTotalNetTaxableAmt = frTotalNetTaxableAmt
									+ (data.getTaxableAmt() - (data.getVarTaxableAmt() + data.getRetTaxableAmt()));

							if (data.getTaxableAmt() == 0) {
								frTotalRetTaxableAmtPer = frTotalRetTaxableAmtPer + 0;
							} else {
								frTotalRetTaxableAmtPer = frTotalRetTaxableAmtPer
										+ (((data.getVarTaxableAmt() + data.getRetTaxableAmt()) * 100)
												/ data.getTaxableAmt());
							}

						}

						frData.setFrTotalSoldQty(frTotalSoldQty);
						frData.setFrTotalSoldAmt(frTotalSoldAmt);

						frData.setFrTotalVarQty(frTotalVarQty);
						frData.setFrTotalVarAmt(frTotalVarAmt);

						frData.setFrTotalRetQty(frTotalRetQty);
						frData.setFrTotalRetAmt(frTotalRetAmt);

						frData.setFrTotalNetQty(frTotalNetQty);
						frData.setFrTotalNetAmt(frTotalNetAmt);

						frData.setFrTotalRetAmtPer(frTotalRetAmtPer);

						frData.setFrTotalSoldTaxableAmt(frTotalSoldTaxableAmt);

						frData.setFrTotalVarTaxableAmt(frTotalVarTaxableAmt);

						frData.setFrTotalRetTaxableAmt(frTotalRetTaxableAmt);

						frData.setFrTotalNetTaxableAmt(frTotalNetTaxableAmt);

						frData.setFrTotalRetTaxableAmtPer(frTotalRetTaxableAmtPer);

						// -----------MONTHLY SUM--------------------------
						monthTotalSoldQty = monthTotalSoldQty + frTotalSoldQty;
						monthTotalSoldAmt = monthTotalSoldAmt + frTotalSoldAmt;
						monthTotalVarQty = monthTotalVarQty + frTotalVarQty;
						monthTotalVarAmt = monthTotalVarAmt + frTotalVarAmt;
						monthTotalRetQty = monthTotalRetQty + frTotalRetQty;
						monthTotalRetAmt = monthTotalRetAmt + frTotalRetAmt;
						monthTotalNetQty = monthTotalNetQty + frTotalNetQty;
						monthTotalNetAmt = monthTotalNetAmt + frTotalNetAmt;
						monthTotalRetAmtPer = monthTotalRetAmtPer + frTotalRetAmtPer;

						monthTotalSoldTaxableAmt = monthTotalSoldTaxableAmt + frTotalSoldTaxableAmt;
						monthTotalVarTaxableAmt = monthTotalVarTaxableAmt + frTotalVarTaxableAmt;
						monthTotalRetTaxableAmt = monthTotalRetTaxableAmt + frTotalRetTaxableAmt;
						monthTotalNetTaxableAmt = monthTotalNetTaxableAmt + frTotalNetTaxableAmt;
						monthTotalRetTaxableAmtPer = monthTotalRetTaxableAmtPer + frTotalRetTaxableAmtPer;

					}

					reportList.get(i).setMonthTotalSoldQty(monthTotalSoldQty);
					reportList.get(i).setMonthTotalSoldAmt(monthTotalSoldAmt);

					reportList.get(i).setMonthTotalVarQty(monthTotalVarQty);
					reportList.get(i).setMonthTotalVarAmt(monthTotalVarAmt);

					reportList.get(i).setMonthTotalRetQty(monthTotalRetQty);
					reportList.get(i).setMonthTotalRetAmt(monthTotalRetAmt);

					reportList.get(i).setMonthTotalNetQty(monthTotalNetQty);
					reportList.get(i).setMonthTotalNetAmt(monthTotalNetAmt);

					reportList.get(i).setMonthTotalRetAmtPer(monthTotalRetAmtPer);

					reportList.get(i).setMonthTotalSoldTaxableAmt(monthTotalSoldTaxableAmt);

					reportList.get(i).setMonthTotalVarTaxableAmt(monthTotalVarTaxableAmt);

					reportList.get(i).setMonthTotalRetTaxableAmt(monthTotalRetTaxableAmt);

					reportList.get(i).setMonthTotalNetTaxableAmt(monthTotalNetTaxableAmt);

					reportList.get(i).setMonthTotalRetTaxableAmtPer(monthTotalRetTaxableAmtPer);

				}
			}

			System.err.println("-------------- REPORT---------------- " + reportList);

			model.addObject("reportList", reportList);
			model.addObject("reportType", selectedType);
			
			model.addObject("fromDate", fromDate);
			model.addObject("toDate", toDate);
			model.addObject("selectedFr", selectedFrArray);
			model.addObject("selectedSubCat", selectedSubCatIdArray);
			
			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();
			}
			model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

			SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
					SubCategory[].class);

			subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));
			
			model.addObject("subCategoryList", subCatAList);
			
			

		} catch (Exception e) {

			System.out.println("Exc in display yearly report--  " + e.getMessage());
			e.printStackTrace();

		}

		try {

			List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

			ExportToExcel expoExcel = new ExportToExcel();
			List<String> rowData = new ArrayList<String>();

			rowData.add(" ");
			for (int i = 0; i < reportList.size(); i++) {
				rowData.add(" ");
				rowData.add(" ");
				rowData.add(" ");
				rowData.add(" ");
				rowData.add(reportList.get(i).getDateStr());
				rowData.add(" ");
				rowData.add(" ");
				rowData.add(" ");
				rowData.add(" ");

			}
			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();

			rowData.add(" ");

			for (int i = 0; i < reportList.size(); i++) {

				rowData.add("Sold Qty");
				rowData.add("Sold Amt");
				rowData.add("Var Qty");
				rowData.add("Var Amt");
				rowData.add("Ret Qty");
				rowData.add("Ret Amt");
				rowData.add("Net Qty");
				rowData.add("Net Amt");
				rowData.add("Ret Amt %");

			}
			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

			if (reportList != null) {

				YearlySaleData monthData = reportList.get(0);

				for (int f = 0; f < monthData.getDataList().size(); f++) {

					TempFrWiseSubCat frData = monthData.getDataList().get(f);

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();

					rowData.add("" + frData.getFrName());

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);

					for (int s = 0; s < frData.getSubCatList().size(); s++) {

						TempSubCatWiseBillData scData = frData.getSubCatList().get(s);

						expoExcel = new ExportToExcel();
						rowData = new ArrayList<String>();

						rowData.add("" + scData.getSubCatName());

						for (int m = 0; m < reportList.size(); m++) {

							for (int f1 = 0; f1 < reportList.get(m).getDataList().size(); f1++) {

								if (reportList.get(m).getDataList().get(f1).getFrId() == frData.getFrId()) {
									for (int sc1 = 0; sc1 < reportList.get(m).getDataList().get(f1).getSubCatList()
											.size(); sc1++) {
										if (reportList.get(m).getDataList().get(f1).getSubCatList().get(sc1)
												.getSubCatId() == scData.getSubCatId()) {

											TempSubCatWiseBillData scData1 = reportList.get(m).getDataList().get(f1)
													.getSubCatList().get(sc1);

											// rowData.add("" + scData1.getSubCatName());

											if (selectedType.equalsIgnoreCase("1")) {

												rowData.add("" + scData1.getSoldQty());
												rowData.add("" + scData1.getSoldAmt());

												rowData.add("" + scData1.getVarQty());
												rowData.add("" + scData1.getVarAmt());

												rowData.add("" + scData1.getRetQty());
												rowData.add("" + scData1.getRetAmt());

												rowData.add("" + (scData1.getSoldQty()
														- (scData1.getVarQty() + scData1.getRetQty())));
												rowData.add("" + (scData1.getSoldAmt()
														- (scData1.getVarAmt() + scData1.getRetAmt())));

												if (scData1.getSoldAmt() == 0) {
													rowData.add("0.00%");
												} else {
													rowData.add(
															"" + (((scData1.getVarAmt() + scData1.getRetAmt()) * 100)
																	/ scData1.getSoldAmt()) + "%");
												}

											} else if (selectedType.equalsIgnoreCase("2")) {

												rowData.add("" + scData1.getSoldQty());
												rowData.add("");

												rowData.add("" + scData1.getVarQty());
												rowData.add("");

												rowData.add("" + scData1.getRetQty());
												rowData.add("");

												rowData.add("" + (scData1.getSoldQty()
														- (scData1.getVarQty() + scData1.getRetQty())));
												rowData.add("");

												rowData.add("");

											} else if (selectedType.equalsIgnoreCase("3")) {

												rowData.add("");
												rowData.add("" + scData1.getSoldAmt());

												rowData.add("");
												rowData.add("" + scData1.getVarAmt());

												rowData.add("");
												rowData.add("" + scData1.getRetAmt());

												rowData.add("");
												rowData.add("" + (scData1.getSoldAmt()
														- (scData1.getVarAmt() + scData1.getRetAmt())));

												if (scData1.getSoldAmt() == 0) {
													rowData.add("0.00%");
												} else {
													rowData.add(
															"" + (((scData1.getVarAmt() + scData1.getRetAmt()) * 100)
																	/ scData1.getSoldAmt()) + "%");
												}

											} else if (selectedType.equalsIgnoreCase("4")) {

												rowData.add("" + (int) scData1.getSoldQty());
												rowData.add("" + scData1.getTaxableAmt());

												rowData.add("" + (int) scData1.getVarQty());
												rowData.add("" + scData1.getVarTaxableAmt());

												rowData.add("" + (int) scData1.getRetQty());
												rowData.add("" + scData1.getRetTaxableAmt());

												rowData.add("" + (int) (scData1.getSoldQty()
														- (scData1.getVarQty() + scData1.getRetQty())));
												rowData.add("" + (scData1.getTaxableAmt()
														- (scData1.getVarTaxableAmt() + scData1.getRetTaxableAmt())));

												if (scData1.getTaxableAmt() == 0) {
													rowData.add("0.00%");
												} else {
													rowData.add("" + (((scData1.getVarTaxableAmt()
															+ scData1.getRetTaxableAmt()) * 100)
															/ scData1.getTaxableAmt()) + "%");
												}

											}

											break;
										}
									}

									break;
								}
							}
						}
						expoExcel.setRowData(rowData);
						exportToExcelList.add(expoExcel);

					}

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();

					rowData.add("Total");

					for (int k = 0; k < reportList.size(); k++) {
						for (int n = 0; n < reportList.get(k).getDataList().size(); n++) {

							TempFrWiseSubCat fr = reportList.get(k).getDataList().get(n);

							if (frData.getFrId() == fr.getFrId()) {

								if (selectedType.equalsIgnoreCase("1")) {

									rowData.add("" + (int) fr.getFrTotalSoldQty());
									rowData.add("" + fr.getFrTotalSoldAmt());

									rowData.add("" + (int) fr.getFrTotalVarQty());
									rowData.add("" + fr.getFrTotalVarAmt());

									rowData.add("" + (int) fr.getFrTotalRetQty());
									rowData.add("" + fr.getFrTotalRetAmt());

									rowData.add("" + (int) fr.getFrTotalNetQty());
									rowData.add("" + fr.getFrTotalNetAmt());

									rowData.add("" + fr.getFrTotalRetAmtPer() + "%");

								} else if (selectedType.equalsIgnoreCase("2")) {

									rowData.add("" + (int) fr.getFrTotalSoldQty());
									rowData.add("");

									rowData.add("" + (int) fr.getFrTotalVarQty());
									rowData.add("");

									rowData.add("" + (int) fr.getFrTotalRetQty());
									rowData.add("");

									rowData.add("" + (int) fr.getFrTotalNetQty());
									rowData.add("");

									rowData.add("");

								} else if (selectedType.equalsIgnoreCase("3")) {

									rowData.add("");
									rowData.add("" + fr.getFrTotalSoldAmt());

									rowData.add("");
									rowData.add("" + fr.getFrTotalVarAmt());

									rowData.add("");
									rowData.add("" + fr.getFrTotalRetAmt());

									rowData.add("");
									rowData.add("" + fr.getFrTotalNetAmt());

									rowData.add("" + fr.getFrTotalRetAmtPer() + "%");

								} else if (selectedType.equalsIgnoreCase("4")) {

									rowData.add("" + (int) fr.getFrTotalSoldQty());
									rowData.add("" + fr.getFrTotalSoldTaxableAmt());

									rowData.add("" + (int) fr.getFrTotalVarQty());
									rowData.add("" + fr.getFrTotalVarTaxableAmt());

									rowData.add("" + (int) fr.getFrTotalRetQty());
									rowData.add("" + fr.getFrTotalRetTaxableAmt());

									rowData.add("" + (int) fr.getFrTotalNetQty());
									rowData.add("" + fr.getFrTotalNetTaxableAmt());

									rowData.add("" + fr.getFrTotalRetTaxableAmtPer() + "%");

								}

							}

						}
					}

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);

				}

			} // if

			expoExcel = new ExportToExcel();
			rowData = new ArrayList<String>();

			rowData.add("TOTAL");

			for (int i = 0; i < reportList.size(); i++) {

				YearlySaleData data = reportList.get(i);

				if (selectedType.equalsIgnoreCase("1")) {

					rowData.add("" + (int) data.getMonthTotalSoldQty());
					rowData.add("" + data.getMonthTotalSoldAmt());

					rowData.add("" + (int) data.getMonthTotalVarQty());
					rowData.add("" + data.getMonthTotalVarAmt());

					rowData.add("" + (int) data.getMonthTotalRetQty());
					rowData.add("" + data.getMonthTotalRetAmt());

					rowData.add("" + (int) data.getMonthTotalNetQty());
					rowData.add("" + data.getMonthTotalNetAmt());

					rowData.add("" + data.getMonthTotalRetAmtPer() + "%");

				} else if (selectedType.equalsIgnoreCase("2")) {

					rowData.add("" + (int) data.getMonthTotalSoldQty());
					rowData.add("");

					rowData.add("" + (int) data.getMonthTotalVarQty());
					rowData.add("");

					rowData.add("" + (int) data.getMonthTotalRetQty());
					rowData.add("");

					rowData.add("" + (int) data.getMonthTotalNetQty());
					rowData.add("");

					rowData.add("");

				} else if (selectedType.equalsIgnoreCase("3")) {

					rowData.add("");
					rowData.add("" + data.getMonthTotalSoldAmt());

					rowData.add("");
					rowData.add("" + data.getMonthTotalVarAmt());

					rowData.add("");
					rowData.add("" + data.getMonthTotalRetAmt());

					rowData.add("");
					rowData.add("" + data.getMonthTotalNetAmt());

					rowData.add("" + data.getMonthTotalRetAmtPer() + "%");

				} else if (selectedType.equalsIgnoreCase("4")) {

					rowData.add("" + (int) data.getMonthTotalSoldQty());
					rowData.add("" + data.getMonthTotalSoldTaxableAmt());

					rowData.add("" + (int) data.getMonthTotalVarQty());
					rowData.add("" + data.getMonthTotalVarTaxableAmt());

					rowData.add("" + (int) data.getMonthTotalRetQty());
					rowData.add("" + data.getMonthTotalRetTaxableAmt());

					rowData.add("" + (int) data.getMonthTotalNetQty());
					rowData.add("" + data.getMonthTotalNetTaxableAmt());

					rowData.add("" + data.getMonthTotalRetTaxableAmtPer() + "%");

				}

			}

			expoExcel.setRowData(rowData);
			exportToExcelList.add(expoExcel);

			// rowData.add("" + roundUp(drTotalAmt - crTotalAmt));

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelListNew", exportToExcelList);
			session.setAttribute("excelNameNew", "ItemSaleYearlyFranchiseeWise");
			session.setAttribute("reportNameNew", "Item Group -> Item -> Shop wise Yearly Report");
			session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
			session.setAttribute("mergeUpto1", "$A$1:$J$1");
			session.setAttribute("mergeUpto2", "$A$2:$J$2");
			session.setAttribute("mergeUpto2", "$A$2:$J$2");

		} catch (Exception e) {
			System.err.println("--------------EXC - ");
			e.printStackTrace();
		}

		return model;

	}

}
