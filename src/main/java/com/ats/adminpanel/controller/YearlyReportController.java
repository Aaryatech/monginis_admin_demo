package com.ats.adminpanel.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.commons.DateConvertor;
import com.ats.adminpanel.model.AllFrIdName;
import com.ats.adminpanel.model.AllFrIdNameList;
import com.ats.adminpanel.model.ExportToExcel;
import com.ats.adminpanel.model.billing.Company;
import com.ats.adminpanel.model.franchisee.SubCategory;
import com.ats.adminpanel.model.item.CategoryListResponse;
import com.ats.adminpanel.model.item.MCategoryList;
import com.ats.adminpanel.yearlyreport.TempFrList;
import com.ats.adminpanel.yearlyreport.TempItemList;
import com.ats.adminpanel.yearlyreport.TempSubCatList;
import com.ats.adminpanel.yearlyreport.YearlyReport;

@Controller
@Scope("session")
public class YearlyReportController {

	String todaysDate;
	ArrayList<SubCategory> subCatAList;

	AllFrIdNameList allFrIdNameList = new AllFrIdNameList();
	List<MCategoryList> mCategoryList;

	// -------AJAX---------FR LIST-------------

	@RequestMapping(value = "/getAllFrListForReport", method = RequestMethod.GET)
	@ResponseBody
	public List<AllFrIdName> getFrListofAllFrForReport(HttpServletRequest request, HttpServletResponse response) {

		return allFrIdNameList.getFrIdNamesList();

	}

	// -----------YEARLY REPORT----------------------

	@RequestMapping(value = "/showFrSubCatItemYearlyReport", method = RequestMethod.GET)
	public ModelAndView showFrSubCatItemYearlyReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		model = new ModelAndView("reports/sales/yearlyFrSubCatItemReport");

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

		} catch (Exception e) {

			System.out.println("Exc in showFrSubCatItemYearlyReport--  " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	// ----AJAX-------------SEARCH YEARLY REPORT-------------

	@RequestMapping(value = "/getFrSubCatItemYearlyReport", method = RequestMethod.GET)
	public @ResponseBody List<YearlyReport> getFrSubCatItemYearlyReport(HttpServletRequest request,
			HttpServletResponse response) {

		List<YearlyReport> reportList = new ArrayList<>();

		String fromDate = "";
		String toDate = "";
		String selectedType = "";
		try {
			RestTemplate restTemplate = new RestTemplate();

			System.err.println("Inside get Sale Bill Wise");
			String selectedFr = request.getParameter("fr_id_list");
			String selectedCatIdList = request.getParameter("cat_id_list");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			selectedType = request.getParameter("selectedType");

			System.out.println("selectedFrBefore------------------" + selectedFr);

			selectedFr = selectedFr.substring(1, selectedFr.length() - 1);
			selectedFr = selectedFr.replaceAll("\"", "");

			selectedCatIdList = selectedCatIdList.substring(1, selectedCatIdList.length() - 1);
			selectedCatIdList = selectedCatIdList.replaceAll("\"", "");

			System.out.println("selectedFrAfter------------------" + selectedFr);

			System.err.println("CAT ---------1----------- " + selectedCatIdList);

			if (selectedCatIdList.equalsIgnoreCase("-1")) {

				CategoryListResponse categoryListResponse;

				categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);

				mCategoryList = categoryListResponse.getmCategoryList();

				// subCatIdList.clear();
				List<String> tempcIdList = new ArrayList();

				if (mCategoryList != null) {

					for (int i = 0; i < mCategoryList.size(); i++) {
						tempcIdList.add(String.valueOf(mCategoryList.get(i).getCatId()));
					}
				}
				System.err.println("CAT ID ARRAY --------- " + tempcIdList);
				selectedCatIdList = tempcIdList.toString().substring(1, tempcIdList.toString().length() - 1);
				selectedCatIdList = selectedCatIdList.replaceAll(" ", "");

			}

			System.err.println("CAT -------------------- " + selectedCatIdList);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

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
			map.add("catIdList", selectedCatIdList);

			ParameterizedTypeReference<List<YearlyReport>> typeRef = new ParameterizedTypeReference<List<YearlyReport>>() {
			};
			ResponseEntity<List<YearlyReport>> responseEntity = restTemplate.exchange(
					Constants.url + "getYearlyFrSubCatItemSaleReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			reportList = responseEntity.getBody();

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

					for (int j = 0; j < reportList.get(i).getFrList().size(); j++) {

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

						TempFrList frData = reportList.get(i).getFrList().get(j);

						for (int k = 0; k < frData.getTempSubCatList().size(); k++) {

							float scTotalSoldQty = 0;
							float scTotalSoldAmt = 0;
							float scTotalVarQty = 0;
							float scTotalVarAmt = 0;
							float scTotalRetQty = 0;
							float scTotalRetAmt = 0;
							float scTotalNetQty = 0;
							float scTotalNetAmt = 0;
							float scTotalRetAmtPer = 0;
							float scTotalSoldTaxableAmt = 0;
							float scTotalVarTaxableAmt = 0;
							float scTotalRetTaxableAmt = 0;
							float scTotalNetTaxableAmt = 0;
							float scTotalRetTaxableAmtPer = 0;

							TempSubCatList scData = frData.getTempSubCatList().get(k);

							for (int m = 0; m < scData.getTempItemList().size(); m++) {

								TempItemList data = scData.getTempItemList().get(m);

								scTotalSoldQty = scTotalSoldQty + data.getSoldQty();
								scTotalSoldAmt = scTotalSoldAmt + data.getSoldAmt();
								scTotalVarQty = scTotalVarQty + data.getVarQty();
								scTotalVarAmt = scTotalVarAmt + data.getVarAmt();
								scTotalRetQty = scTotalRetQty + data.getRetQty();
								scTotalRetAmt = scTotalRetAmt + data.getRetAmt();
								scTotalNetQty = scTotalNetQty
										+ (data.getSoldQty() - (data.getVarQty() + data.getRetQty()));
								scTotalNetAmt = scTotalNetAmt
										+ (data.getSoldAmt() - (data.getVarAmt() + data.getRetAmt()));

								if (data.getSoldAmt() == 0) {
									scTotalRetAmtPer = scTotalRetAmtPer + 0;

								} else {
									scTotalRetAmtPer = scTotalRetAmtPer
											+ (((data.getVarAmt() + data.getRetAmt()) * 100) / data.getSoldAmt());
								}

								scTotalSoldTaxableAmt = scTotalSoldTaxableAmt + data.getTaxableAmt();
								scTotalVarTaxableAmt = scTotalVarTaxableAmt + data.getVarTaxableAmt();
								scTotalRetTaxableAmt = scTotalRetTaxableAmt + data.getRetTaxableAmt();
								scTotalNetTaxableAmt = scTotalNetTaxableAmt
										+ (data.getTaxableAmt() - (data.getVarTaxableAmt() + data.getRetTaxableAmt()));

								if (data.getTaxableAmt() == 0) {
									scTotalRetTaxableAmtPer = scTotalRetTaxableAmtPer + 0;
								} else {
									scTotalRetTaxableAmtPer = scTotalRetTaxableAmtPer
											+ (((data.getVarTaxableAmt() + data.getRetTaxableAmt()) * 100)
													/ data.getTaxableAmt());
								}

							}

							scData.setScTotalSoldQty(scTotalSoldQty);
							scData.setScTotalSoldAmt(scTotalSoldAmt);

							scData.setScTotalVarQty(scTotalVarQty);
							scData.setScTotalVarAmt(scTotalVarAmt);

							scData.setScTotalRetQty(scTotalRetQty);
							scData.setScTotalRetAmt(scTotalRetAmt);

							scData.setScTotalNetQty(scTotalNetQty);
							scData.setScTotalNetAmt(scTotalNetAmt);

							scData.setScTotalRetAmtPer(scTotalRetAmtPer);

							scData.setScTotalSoldTaxableAmt(scTotalSoldTaxableAmt);

							scData.setScTotalVarTaxableAmt(scTotalVarTaxableAmt);

							scData.setScTotalRetTaxableAmt(scTotalRetTaxableAmt);

							scData.setScTotalNetTaxableAmt(scTotalNetTaxableAmt);

							scData.setScTotalRetTaxableAmtPer(scTotalRetTaxableAmtPer);

							frTotalSoldQty = frTotalSoldQty + scTotalSoldQty;
							frTotalSoldAmt = frTotalSoldAmt + scTotalSoldAmt;
							frTotalVarQty = frTotalVarQty + scTotalVarQty;
							frTotalVarAmt = frTotalVarAmt + scTotalVarAmt;
							frTotalRetQty = frTotalRetQty + scTotalRetQty;
							frTotalRetAmt = frTotalRetAmt + scTotalRetAmt;
							frTotalNetQty = frTotalNetQty + scTotalNetQty;
							frTotalNetAmt = frTotalNetAmt + scTotalNetAmt;

							frTotalRetAmtPer = frTotalRetAmtPer + scTotalRetAmtPer;

							frTotalSoldTaxableAmt = frTotalSoldTaxableAmt + scTotalSoldTaxableAmt;
							frTotalVarTaxableAmt = frTotalVarTaxableAmt + scTotalVarTaxableAmt;
							frTotalRetTaxableAmt = frTotalRetTaxableAmt + scTotalRetTaxableAmt;
							frTotalNetTaxableAmt = frTotalNetTaxableAmt + scTotalNetTaxableAmt;

							frTotalRetTaxableAmtPer = frTotalRetTaxableAmtPer + scTotalRetTaxableAmtPer;

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

		} catch (Exception e) {
			System.out.println("get Yearly sale Report  " + e.getMessage());
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

				YearlyReport monthData = reportList.get(0);

				for (int f = 0; f < monthData.getFrList().size(); f++) {

					TempFrList frData = monthData.getFrList().get(f);

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();

					rowData.add("" + frData.getFrName());

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);

					for (int s = 0; s < frData.getTempSubCatList().size(); s++) {

						TempSubCatList scData = frData.getTempSubCatList().get(s);

						expoExcel = new ExportToExcel();
						rowData = new ArrayList<String>();

						rowData.add("" + scData.getSubCatName());

						expoExcel.setRowData(rowData);
						exportToExcelList.add(expoExcel);

						for (int d = 0; d < scData.getTempItemList().size(); d++) {

							TempItemList itemData = scData.getTempItemList().get(d);

							expoExcel = new ExportToExcel();
							rowData = new ArrayList<String>();

							rowData.add("" + itemData.getItemName());

							for (int m = 0; m < reportList.size(); m++) {

								for (int f1 = 0; f1 < reportList.get(m).getFrList().size(); f1++) {

									if (reportList.get(m).getFrList().get(f1).getFrId() == frData.getFrId()) {

										for (int sc1 = 0; sc1 < reportList.get(m).getFrList().get(f1)
												.getTempSubCatList().size(); sc1++) {

											if (reportList.get(m).getFrList().get(f1).getTempSubCatList().get(sc1)
													.getSubCatId() == scData.getSubCatId()) {

												for (int i1 = 0; i1 < reportList.get(m).getFrList().get(f1)
														.getTempSubCatList().get(sc1).getTempItemList().size(); i1++) {

													if (reportList.get(m).getFrList().get(f1).getTempSubCatList()
															.get(sc1).getTempItemList().get(i1)
															.getItemId() == itemData.getItemId()) {

														TempItemList itemData1 = reportList.get(m).getFrList().get(f1)
																.getTempSubCatList().get(sc1).getTempItemList().get(i1);

														if (selectedType.equalsIgnoreCase("1")) {

															rowData.add("" + itemData1.getSoldQty());
															rowData.add("" + itemData1.getSoldAmt());

															rowData.add("" + itemData1.getVarQty());
															rowData.add("" + itemData1.getVarAmt());

															rowData.add("" + itemData1.getRetQty());
															rowData.add("" + itemData1.getRetAmt());

															rowData.add("" + (itemData1.getSoldQty()
																	- (itemData1.getVarQty() + itemData1.getRetQty())));
															rowData.add("" + (itemData1.getSoldAmt()
																	- (itemData1.getVarAmt() + itemData1.getRetAmt())));

															if (itemData1.getSoldAmt() == 0) {
																rowData.add("0.00%");
															} else {

																/*System.err.println("% -----  var+ret = "
																		+ itemData1.getVarAmt() + itemData1.getRetAmt()
																		+ "      SOLD = " + itemData1.getSoldAmt()
																		+ "         RESULT = "
																		+ (((itemData1.getVarAmt()
																				+ itemData1.getRetAmt()) * 100)
																				/ itemData1.getSoldAmt()));*/

																rowData.add("" + (((itemData1.getVarAmt()
																		+ itemData1.getRetAmt()) * 100)
																		/ itemData1.getSoldAmt()) + "%");
															}

														} else if (selectedType.equalsIgnoreCase("2")) {

															rowData.add("" + itemData1.getSoldQty());
															rowData.add("");

															rowData.add("" + itemData1.getVarQty());
															rowData.add("");

															rowData.add("" + itemData1.getRetQty());
															rowData.add("");

															rowData.add("" + (itemData1.getSoldQty()
																	- (itemData1.getVarQty() + itemData1.getRetQty())));
															rowData.add("");

															rowData.add("");

														} else if (selectedType.equalsIgnoreCase("3")) {

															rowData.add("");
															rowData.add("" + itemData1.getSoldAmt());

															rowData.add("");
															rowData.add("" + itemData1.getVarAmt());

															rowData.add("");
															rowData.add("" + itemData1.getRetAmt());

															rowData.add("");
															rowData.add("" + (itemData1.getSoldAmt()
																	- (itemData1.getVarAmt() + itemData1.getRetAmt())));

															if (itemData1.getSoldAmt() == 0) {
																rowData.add("0.00%");
															} else {
																rowData.add("" + (((itemData1.getVarAmt()
																		+ itemData1.getRetAmt()) * 100)
																		/ itemData1.getSoldAmt()) + "%");
															}

														} else if (selectedType.equalsIgnoreCase("4")) {

															rowData.add("" + (int) itemData1.getSoldQty());
															rowData.add("" + itemData1.getTaxableAmt());

															rowData.add("" + (int) itemData1.getVarQty());
															rowData.add("" + itemData1.getVarTaxableAmt());

															rowData.add("" + (int) itemData1.getRetQty());
															rowData.add("" + itemData1.getRetTaxableAmt());

															rowData.add("" + (int) (itemData1.getSoldQty()
																	- (itemData1.getVarQty() + itemData1.getRetQty())));
															rowData.add("" + (itemData1.getTaxableAmt()
																	- (itemData1.getVarTaxableAmt()
																			+ itemData1.getRetTaxableAmt())));

															if (itemData1.getTaxableAmt() == 0) {
																rowData.add("0.00%");
															} else {
																rowData.add("" + (((itemData1.getVarTaxableAmt()
																		+ itemData1.getRetTaxableAmt()) * 100)
																		/ itemData1.getTaxableAmt()) + "%");
															}

														}

														break;

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

						rowData.add("Total------------------------");

						for (int k = 0; k < reportList.size(); k++) {
							for (int n = 0; n < reportList.get(k).getFrList().size(); n++) {

								TempFrList fr = reportList.get(k).getFrList().get(n);

								if (frData.getFrId() == fr.getFrId()) {

									for (int m = 0; m < fr.getTempSubCatList().size(); m++) {

										TempSubCatList sc = fr.getTempSubCatList().get(m);

										if (scData.getSubCatId() == sc.getSubCatId()) {

											if (selectedType.equalsIgnoreCase("1")) {

												rowData.add("" + (int) sc.getScTotalSoldQty());
												rowData.add("" + sc.getScTotalSoldAmt());

												rowData.add("" + (int) sc.getScTotalVarQty());
												rowData.add("" + sc.getScTotalVarAmt());

												rowData.add("" + (int) sc.getScTotalRetQty());
												rowData.add("" + sc.getScTotalRetAmt());

												rowData.add("" + (int) sc.getScTotalNetQty());
												rowData.add("" + sc.getScTotalNetAmt());

												
												
												
												rowData.add("" + sc.getScTotalRetAmtPer() + "%");

											} else if (selectedType.equalsIgnoreCase("2")) {

												rowData.add("" + (int) sc.getScTotalSoldQty());
												rowData.add("");

												rowData.add("" + (int) sc.getScTotalVarQty());
												rowData.add("");

												rowData.add("" + (int) sc.getScTotalRetQty());
												rowData.add("");

												rowData.add("" + (int) sc.getScTotalNetQty());
												rowData.add("");

												rowData.add("");

											} else if (selectedType.equalsIgnoreCase("3")) {

												rowData.add("");
												rowData.add("" + sc.getScTotalSoldAmt());

												rowData.add("");
												rowData.add("" + sc.getScTotalVarAmt());

												rowData.add("");
												rowData.add("" + sc.getScTotalRetAmt());

												rowData.add("");
												rowData.add("" + sc.getScTotalNetAmt());

												rowData.add("" + sc.getScTotalRetAmtPer() + "%");

											} else if (selectedType.equalsIgnoreCase("4")) {

												rowData.add("" + (int) sc.getScTotalSoldQty());
												rowData.add("" + sc.getScTotalSoldTaxableAmt());

												rowData.add("" + (int) sc.getScTotalVarQty());
												rowData.add("" + sc.getScTotalVarTaxableAmt());

												rowData.add("" + (int) sc.getScTotalRetQty());
												rowData.add("" + sc.getScTotalRetTaxableAmt());

												rowData.add("" + (int) sc.getScTotalNetQty());
												rowData.add("" + sc.getScTotalNetTaxableAmt());

												rowData.add("" + sc.getScTotalRetTaxableAmtPer() + "%");

											}

										}

									}

								}

							}
						}

						expoExcel.setRowData(rowData);
						exportToExcelList.add(expoExcel);

					}

					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();

					rowData.add("Total------------------------");

					for (int k = 0; k < reportList.size(); k++) {
						for (int n = 0; n < reportList.get(k).getFrList().size(); n++) {

							TempFrList fr = reportList.get(k).getFrList().get(n);

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

			rowData.add("-----TOTAL-----");

			for (int i = 0; i < reportList.size(); i++) {

				YearlyReport data = reportList.get(i);

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

			HttpSession session = request.getSession();
			session.setAttribute("exportExcelListNew", exportToExcelList);
			session.setAttribute("excelNameNew", "FrWiseSubCatWiseItemSaleYearlyReport");
			session.setAttribute("reportNameNew", "Franchisee Wise Sub Cat Wise Item Wise Monthly Report");
			session.setAttribute("searchByNew", "From Date: " + fromDate + "  To Date: " + toDate + " ");
			session.setAttribute("mergeUpto1", "$A$1:$J$1");
			session.setAttribute("mergeUpto2", "$A$2:$J$2");
			session.setAttribute("mergeUpto2", "$A$2:$J$2");

		} catch (

		Exception e) {
			System.err.println("--------------EXC - ");
			e.printStackTrace();
		}

		return reportList;
	}

}
