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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.commons.DateConvertor;
import com.ats.adminpanel.model.AllFrIdNameList;
import com.ats.adminpanel.model.AllMenus;
import com.ats.adminpanel.model.AllRoutesListResponse;
import com.ats.adminpanel.model.ErrorMessage;
import com.ats.adminpanel.model.FranchiseForDispatch;
import com.ats.adminpanel.model.ItemListForDispatchReport;
import com.ats.adminpanel.model.ItemListStatioinWise;
import com.ats.adminpanel.model.PDispatchReport;
import com.ats.adminpanel.model.PDispatchReportList;
import com.ats.adminpanel.model.Route;
import com.ats.adminpanel.model.RouteMaster;
import com.ats.adminpanel.model.SectionMaster;
import com.ats.adminpanel.model.StaionListWithFranchiseeList;
import com.ats.adminpanel.model.TypeWiseItemTotal;
import com.ats.adminpanel.model.franchisee.FrNameIdByRouteId;
import com.ats.adminpanel.model.franchisee.FrNameIdByRouteIdResponse;
import com.ats.adminpanel.model.franchisee.FranchiseeAndMenuList;
import com.ats.adminpanel.model.franchisee.FranchiseeList;
import com.ats.adminpanel.model.franchisee.Menu;
import com.ats.adminpanel.model.franchisee.SubCategory;
import com.ats.adminpanel.model.item.AllItemsListResponse;
import com.ats.adminpanel.model.item.CategoryListResponse;
import com.ats.adminpanel.model.item.Item;
import com.ats.adminpanel.model.item.MCategoryList;
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
public class DispachReport {

	String todaysDate;
	AllFrIdNameList allFrIdNameList = new AllFrIdNameList();
	List<Integer> stationList = new ArrayList<>();
	List<RouteMaster> list = new ArrayList<>();
	List<Item> itemList = new ArrayList<Item>();
	List<StaionListWithFranchiseeList> staionListWithFranchiseeList = new ArrayList<StaionListWithFranchiseeList>();
	List<AllMenus> menuList = new ArrayList<AllMenus>();

	@RequestMapping(value = "/getAllFranchasiOfMultipleRoute", method = RequestMethod.GET)
	@ResponseBody
	public List<FranchiseForDispatch> getAllFranchasiOfMultipleRoute(HttpServletRequest request,
			HttpServletResponse response) {

		// List<FrNameIdByRouteId> frNameIdByRouteIdList = new ArrayList<>();
		List<FranchiseForDispatch> frNameIdByRouteIdList = null;

		try {

			String RouteIds = request.getParameter("routeIds");

			RouteIds = RouteIds.substring(1, RouteIds.length() - 1);
			RouteIds = RouteIds.replaceAll("\"", "");
			System.out.println("RouteIds==" + RouteIds.toString());

			List<String> RoutList = new ArrayList<>();
			RoutList = Arrays.asList(RouteIds);
			System.out.println("RouteIds==" + RoutList.toString());

			StringBuilder sbForRouteFrId = new StringBuilder();
			for (int i = 0; i < RoutList.size(); i++) {

				sbForRouteFrId = sbForRouteFrId.append(RoutList.get(i).toString() + ",");

			}

			String strFrIdRouteWise = sbForRouteFrId.toString();
			RouteIds = strFrIdRouteWise.substring(0, strFrIdRouteWise.length() - 1);
			System.out.println("fr Id Route WISE = " + RouteIds);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("routeId", RouteIds);
			RestTemplate restTemplate = new RestTemplate();

			// FranchiseForDispatch[] frNameId
			FranchiseForDispatch[] frNameId = restTemplate.postForObject(
					Constants.url + "getFranchiseForDispatchRouteIDByPass5", map, FranchiseForDispatch[].class);

			frNameIdByRouteIdList = new ArrayList<>(Arrays.asList(frNameId));

			System.out.println("route wise franchisee " + frNameIdByRouteIdList);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return frNameIdByRouteIdList;

	}

	@RequestMapping(value = "/ItemwiseDispatchReport", method = RequestMethod.GET)
	public ModelAndView showItemwiseDispatchReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/itemwiseDispatchReport");

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);

			RestTemplate restTemplate = new RestTemplate();

			AllItemsListResponse allItemsListResponse = restTemplate.getForObject(Constants.url + "getAllItems",
					AllItemsListResponse.class);
			List<Item> itemsList = null;
			itemsList = allItemsListResponse.getItems();

			System.out.println("Item List" + itemsList);

			model.addObject("itemsList", itemsList);

			List<FranchiseeList> franchiseeList = null;

			FranchiseeAndMenuList franchiseeAndMenuList = restTemplate
					.getForObject(Constants.url + "getFranchiseeAndMenu", FranchiseeAndMenuList.class);
			franchiseeList = franchiseeAndMenuList.getAllFranchisee();

			model.addObject("franchiseeList", franchiseeList);

			/*
			 * Menu[] allMenus = restTemplate.getForObject(Constants.url +
			 * "/getAllMenuList", Menu[].class); menuList = new ArrayList
			 * (Arrays.asList(allMenus));
			 */

			model.addObject("menuList", menuList);
			model.addObject("todaysDate", todaysDate);

			SectionMaster[] sectionMasterArray = restTemplate.getForObject(Constants.url + "/getSectionListOnly",
					SectionMaster[].class);
			List<SectionMaster> sectionList = new ArrayList<SectionMaster>(Arrays.asList(sectionMasterArray));
			model.addObject("sectionList", sectionList);

		} catch (Exception e) {

			System.out.println("Exc in show sales report bill wise  " + e.getMessage());
			e.printStackTrace();
		}

		return model;

	}

	@RequestMapping(value = "/getPDispatchReportItemwiseResult", method = RequestMethod.POST)
	public ModelAndView getDispatchReportitemwise(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/ItemwiseDispatchReportResult");
		List<PDispatchReport> dispatchReportList = new ArrayList<PDispatchReport>();
		List<Item> ItemList = new ArrayList<Item>();
		PDispatchReportList dispatchReports = new PDispatchReportList();
		try {

			System.out.println("Inside get Dispatch Report");
			String billDate = request.getParameter("billDate");
			String[] selectedFranchase = request.getParameterValues("frid");
			String[] selectedMenu = request.getParameterValues("menuId");
			String[] selectedItem = request.getParameterValues("itemId");
			System.out.println("billDate" + billDate);
			System.out.println("selectedFranchase" + selectedFranchase);
			System.out.println("selectedMenu" + selectedMenu);
			System.out.println("selectedItem" + selectedItem);

			boolean isAllFranchaseSelected = false;
			boolean isAllMenuSelected = false;
			boolean isAllItemSelected = false;

			String strselectedFranchase = new String();
			for (int i = 0; i < selectedFranchase.length; i++) {
				strselectedFranchase = strselectedFranchase + "," + selectedFranchase[i];
			}
			strselectedFranchase = strselectedFranchase.substring(1, strselectedFranchase.length());
			strselectedFranchase = strselectedFranchase.replaceAll("\"", "");

			String strselectedMenu = new String();
			for (int i = 0; i < selectedMenu.length; i++) {
				strselectedMenu = strselectedMenu + "," + selectedMenu[i];
			}
			strselectedMenu = strselectedMenu.substring(1, strselectedMenu.length());
			strselectedMenu = strselectedMenu.replaceAll("\"", "");

			String strselectedItem = new String();
			for (int i = 0; i < selectedItem.length; i++) {
				strselectedItem = strselectedItem + "," + selectedItem[i];
			}
			strselectedItem = strselectedItem.substring(1, strselectedItem.length());
			strselectedItem = strselectedItem.replaceAll("\"", "");

			List<Integer> frids = Stream.of(strselectedFranchase.split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());

			System.out.println("strselectedFranchase" + strselectedFranchase.toString());
			System.out.println("strselectedMenu" + strselectedMenu.toString());
			System.out.println("strselectedItem" + strselectedItem.toString());

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			RestTemplate restTemplate = new RestTemplate();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}

			List<FrNameIdByRouteId> FrNameList = new ArrayList<>();

			for (int i = 0; i < allFrIdNameList.getFrIdNamesList().size(); i++) {
				for (int j = 0; j < frids.size(); j++) {
					if (frids.get(j) == allFrIdNameList.getFrIdNamesList().get(i).getFrId()) {
						FrNameIdByRouteId objFranchace = new FrNameIdByRouteId();
						objFranchace.setFrId(allFrIdNameList.getFrIdNamesList().get(i).getFrId());
						objFranchace.setFrName(allFrIdNameList.getFrIdNamesList().get(i).getFrName());
						objFranchace.setFrRouteId(0);
						FrNameList.add(objFranchace);
					}

				}
			}

			map = new LinkedMultiValueMap<String, Object>();
			map.add("menu", strselectedMenu);
			map.add("productionDate", billDate);
			map.add("frId", strselectedFranchase);
			map.add("ItemId", strselectedItem);

			ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
			};

			ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
					Constants.url + "getPDispatchItemReportMenuwise", HttpMethod.POST, new HttpEntity<>(map), typeRef);
			System.out.println("Items:" + responseEntity.toString());

			dispatchReportList = responseEntity.getBody();

			System.out.println("dispatchReportList = " + dispatchReportList.toString());

			map = new LinkedMultiValueMap<String, Object>();
			map.add("itemList", strselectedItem);

			ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
			};

			ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(Constants.url + "getItemsByItemId",
					HttpMethod.POST, new HttpEntity<>(map), typeRef1);

			System.out.println("Items = " + responseEntity1.getBody());

			ItemList = responseEntity1.getBody();
			System.out.println("Items = " + ItemList);

			model.addObject("dispatchReportList", dispatchReportList);
			model.addObject("FrNameList", FrNameList);
			model.addObject("Items", ItemList);

		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}

		return model;

	}

	@RequestMapping(value = "pdf/getPDispatchReportItemwisePdf", method = RequestMethod.GET)
	public ModelAndView getPDispatchReportItemwisePdf(HttpServletRequest request, HttpServletResponse response) {
		int dotMatrixJsp = Integer.parseInt(request.getParameter("dotMatrixJsp"));
		ModelAndView model = new ModelAndView("reports/itemWiseDispatchReportPdf");
		if (dotMatrixJsp == 1) {
			model = new ModelAndView("reports/itemWiseDispatchReportPdfForDt");
		}
		List<PDispatchReport> dispatchReportList = new ArrayList<PDispatchReport>();
		List<Item> ItemList = new ArrayList<Item>();
		PDispatchReportList dispatchReports = new PDispatchReportList();
		try {

			System.out.println("Inside get Dispatch Report");
			String billDate = request.getParameter("bdate");
			String[] selectedFranchase = request.getParameterValues("frids");
			String[] selectedMenu = request.getParameterValues("menus");
			String[] selectedItem = request.getParameterValues("items");
			System.out.println("billDate" + billDate);
			System.out.println("selectedFranchase" + selectedFranchase);
			System.out.println("selectedMenu" + selectedMenu);
			System.out.println("selectedItem" + selectedItem);

			boolean isAllFranchaseSelected = false;
			boolean isAllMenuSelected = false;
			boolean isAllItemSelected = false;

			String strselectedFranchase = new String();
			for (int i = 0; i < selectedFranchase.length; i++) {
				strselectedFranchase = strselectedFranchase + "," + selectedFranchase[i];
			}
			strselectedFranchase = strselectedFranchase.substring(1, strselectedFranchase.length());
			strselectedFranchase = strselectedFranchase.replaceAll("\"", "");

			String strselectedMenu = new String();
			for (int i = 0; i < selectedMenu.length; i++) {
				strselectedMenu = strselectedMenu + "," + selectedMenu[i];
			}
			strselectedMenu = strselectedMenu.substring(1, strselectedMenu.length());
			strselectedMenu = strselectedMenu.replaceAll("\"", "");

			String strselectedItem = new String();
			for (int i = 0; i < selectedItem.length; i++) {
				strselectedItem = strselectedItem + "," + selectedItem[i];
			}
			strselectedItem = strselectedItem.substring(1, strselectedItem.length());
			strselectedItem = strselectedItem.replaceAll("\"", "");

			List<Integer> frids = Stream.of(strselectedFranchase.split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());

			System.out.println("strselectedFranchase" + strselectedFranchase.toString());
			System.out.println("strselectedMenu" + strselectedMenu.toString());
			System.out.println("strselectedItem" + strselectedItem.toString());

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			RestTemplate restTemplate = new RestTemplate();

			allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}

			List<FrNameIdByRouteId> FrNameList = new ArrayList<>();

			for (int i = 0; i < allFrIdNameList.getFrIdNamesList().size(); i++) {
				for (int j = 0; j < frids.size(); j++) {
					if (frids.get(j) == allFrIdNameList.getFrIdNamesList().get(i).getFrId()) {
						FrNameIdByRouteId objFranchace = new FrNameIdByRouteId();
						objFranchace.setFrId(allFrIdNameList.getFrIdNamesList().get(i).getFrId());
						objFranchace.setFrName(allFrIdNameList.getFrIdNamesList().get(i).getFrName());
						objFranchace.setFrRouteId(0);
						FrNameList.add(objFranchace);
					}

				}
			}

			map = new LinkedMultiValueMap<String, Object>();
			map.add("menu", strselectedMenu);
			map.add("productionDate", billDate);
			map.add("frId", strselectedFranchase);
			map.add("ItemId", strselectedItem);

			ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
			};

			ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
					Constants.url + "getPDispatchItemReportMenuwise", HttpMethod.POST, new HttpEntity<>(map), typeRef);
			System.out.println("Items:" + responseEntity.toString());

			dispatchReportList = responseEntity.getBody();

			System.out.println("dispatchReportList = " + dispatchReportList.toString());

			map = new LinkedMultiValueMap<String, Object>();
			map.add("itemList", strselectedItem);

			ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
			};

			ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(Constants.url + "getItemsByItemId",
					HttpMethod.POST, new HttpEntity<>(map), typeRef1);

			System.out.println("Items = " + responseEntity1.getBody());

			ItemList = responseEntity1.getBody();
			System.out.println("Items = " + ItemList);

			model.addObject("dispatchReportList", dispatchReportList);
			model.addObject("FrNameList", FrNameList);
			model.addObject("Items", ItemList);
			model.addObject("billDate", billDate);

		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}

		return model;

	}

	@RequestMapping(value = "/showPDispatchItemReportNew", method = RequestMethod.GET)
	public ModelAndView showPDispatchItemReportNew(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/puneDispatchReport");

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);

			RestTemplate restTemplate = new RestTemplate();

			Integer[] array = restTemplate.getForObject(Constants.url + "/itemListGroupByStationNo", Integer[].class);
			stationList = new ArrayList<Integer>(Arrays.asList(array));
			model.addObject("stationList", stationList);

			/*
			 * Menu[] allMenus = restTemplate.getForObject(Constants.url +
			 * "/getAllMenuList", Menu[].class); menuList = new
			 * ArrayList<Menu>(Arrays.asList(allMenus));
			 */

			model.addObject("menuList", menuList);
			model.addObject("stationList", stationList);
			model.addObject("todaysDate", todaysDate);

			SectionMaster[] sectionMasterArray = restTemplate.getForObject(Constants.url + "/getSectionListOnly",
					SectionMaster[].class);
			List<SectionMaster> sectionList = new ArrayList<SectionMaster>(Arrays.asList(sectionMasterArray));
			model.addObject("sectionList", sectionList);

		} catch (Exception e) {

			System.out.println("Exc in show sales report bill wise  " + e.getMessage());
			e.printStackTrace();
		}

		return model;

	}

	@RequestMapping(value = "/getMenuListBySectionId", method = RequestMethod.GET)
	@ResponseBody
	public List<AllMenus> getMenuListBySectionId(HttpServletRequest request, HttpServletResponse response) {

		list = new ArrayList<>();

		try {

			int sectionId = Integer.parseInt(request.getParameter("sectionId"));

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("sectionId", sectionId);
			RestTemplate restTemplate = new RestTemplate();

			SectionMaster routeMaster = restTemplate.postForObject(Constants.url + "/getSectionById", map,
					SectionMaster.class);

			menuList = routeMaster.getMenuList();

			System.out.println("menuList" + menuList.toString());

		} catch (Exception e) {

			e.printStackTrace();
		}

		return menuList;

	}

	@RequestMapping(value = "/routListByAbcType", method = RequestMethod.GET)
	@ResponseBody
	public List<RouteMaster> routListByAbcType(HttpServletRequest request, HttpServletResponse response) {

		list = new ArrayList<>();

		try {

			int abcType = Integer.parseInt(request.getParameter("abcType"));

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("abcType", abcType);
			RestTemplate restTemplate = new RestTemplate();

			RouteMaster[] routeMaster = restTemplate.postForObject(Constants.url + "/showRouteListNewByabcType", map,
					RouteMaster[].class);
			list = new ArrayList<RouteMaster>(Arrays.asList(routeMaster));

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;

	}

	@RequestMapping(value = "/searchPDispatchItemReportNew", method = RequestMethod.GET)
	public ModelAndView searchPDispatchItemReportNew(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/searchPDispatchItemReportNew");

		try {

			String date = request.getParameter("billDate");
			String[] stationId = request.getParameterValues("stanId");
			int abcType = Integer.parseInt(request.getParameter("abcType"));
			int routId = Integer.parseInt(request.getParameter("selectRoute"));
			String[] menuId = request.getParameterValues("menuId");

			String stationIds = new String();
			String abcTypes = new String();
			String menuIds = new String();

			for (int i = 0; i < menuId.length; i++) {
				menuIds = menuIds + "," + menuId[i];
			}
			menuIds = menuIds.substring(1, menuIds.length());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < stationId.length; i++) {
				sb = sb.append(stationId[i] + ",");
			}

			String stns = sb.toString();
			stns = stns.substring(0, stns.length() - 1);
			List<Integer> stIds = Stream.of(stns.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			if (stIds.contains(-1)) {

				for (int i = 0; i < stationList.size(); i++) {

					stationIds = stationIds + "," + stationList.get(i);
				}
				stationIds = stationIds.substring(1, stationIds.length());

			} else {
				stationIds = String.valueOf(stns);
			}
			System.err.println("stationIds" + stationIds.toString());
			if (abcType == 0) {

				abcTypes = "1,2,3";

			} else {
				abcTypes = String.valueOf(abcType);
			}

			if (menuIds.contains("0")) {

				menuIds = new String();

				for (int i = 0; i < menuList.size(); i++) {
					menuIds = menuIds + "," + menuList.get(i).getMenuId();
				}
				menuIds = menuIds.substring(1, menuIds.length());
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("date", DateConvertor.convertToYMD(date));
			map.add("abcType", abcTypes);
			map.add("stationNos", stationIds);
			map.add("routId", routId);
			map.add("menuIds", menuIds);

			System.out.println("map " + map);

			RestTemplate restTemplate = new RestTemplate();

			StaionListWithFranchiseeList[] array = restTemplate.postForObject(Constants.url + "/getAbcDepatchReport",
					map, StaionListWithFranchiseeList[].class);

			staionListWithFranchiseeList = new ArrayList<StaionListWithFranchiseeList>(Arrays.asList(array));

			System.err.println(staionListWithFranchiseeList);

			List<ItemListStatioinWise> itemListStatioinWiseList = new ArrayList<>();

			Item[] item = restTemplate.getForObject(Constants.url + "/getItemListForDispatchReport", Item[].class);

			itemList = new ArrayList<Item>(Arrays.asList(item));

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				ItemListStatioinWise itemListStatioinWise = new ItemListStatioinWise();
				itemListStatioinWise.setStationNo(staionListWithFranchiseeList.get(i).getStationNo());
				List<TypeWiseItemTotal> typeWiseItemTotalList = new ArrayList<>();

				for (int j = 0; j < itemList.size(); j++) {

					if (itemList.get(j).getItemMrp2() == staionListWithFranchiseeList.get(i).getStationNo()) {

						TypeWiseItemTotal itemListForDispatchReport = new TypeWiseItemTotal();
						itemListForDispatchReport.setItemId(itemList.get(j).getId());
						itemListForDispatchReport.setItemName(itemList.get(j).getItemName());
						typeWiseItemTotalList.add(itemListForDispatchReport);
					}

				}
				itemListStatioinWise.setTypeWiseItemTotalList(typeWiseItemTotalList);
				itemListStatioinWiseList.add(itemListStatioinWise);
			}

			System.err.println();
			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				for (int j = 0; j < itemListStatioinWiseList.size(); j++) {

					if (itemListStatioinWiseList.get(j).getStationNo() == staionListWithFranchiseeList.get(i)
							.getStationNo()) {

						for (int k = 0; k < staionListWithFranchiseeList.get(i).getList().size(); k++) {

							if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 1) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setaTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getaTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 2) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setbTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getbTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}

								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 3) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setcTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getcTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							}

						}

					}

				}

			}

			System.err.println("itemListStatioinWiseList" + itemListStatioinWiseList);

			allFrIdNameList = restTemplate.getForObject(Constants.url + "/getAllFrIdName", AllFrIdNameList.class);
			model.addObject("staionListWithFranchiseeList", staionListWithFranchiseeList);
			model.addObject("itemList", itemList);
			model.addObject("allFrIdNameList", allFrIdNameList.getFrIdNamesList());
			model.addObject("itemListStatioinWiseList", itemListStatioinWiseList);
			model.addObject("date", date);
			model.addObject("stationId", stns);
			model.addObject("abcType", abcType);
			model.addObject("routId", routId);
			model.addObject("menuIds", menuIds);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return model;

	}

	/*
	 * commented on 8 April for dispatch Stationwise itext pdf new logic
	 */ @RequestMapping(value = "/pdf/getPDispatchReportNewPdf/{date}/{stationId}/{abcType}/{routId}/{menuIds}", method = RequestMethod.GET)
	public ModelAndView getPDispatchReportNewPdf(@PathVariable String date, @PathVariable String stationId,
			@PathVariable int abcType, @PathVariable int routId, @PathVariable String menuIds,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/dispatchPReportNewPdf");

		try {

			RestTemplate restTemplate = new RestTemplate();
			String stationIds = new String();
			String abcTypes = new String();
			List<Integer> stIds = Stream.of(stationId.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			if (stIds.contains(-1)) {

				Integer[] array = restTemplate.getForObject(Constants.url + "/itemListGroupByStationNo",
						Integer[].class);
				List<Integer> stationList = new ArrayList<Integer>(Arrays.asList(array));
				model.addObject("stationList", stationList);

				for (int i = 0; i < stationList.size(); i++) {

					stationIds = stationIds + "," + stationList.get(i);
				}
				stationIds = stationIds.substring(1, stationIds.length());

			} else {
				stationIds = String.valueOf(stationId);
			}
			System.out.println(stationIds + "stationIds");
			if (abcType == 0) {

				abcTypes = "1,2,3";

			} else {
				abcTypes = String.valueOf(abcType);
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("date", DateConvertor.convertToYMD(date));
			map.add("abcType", abcTypes);
			map.add("stationNos", stationIds);
			map.add("routId", routId);
			map.add("menuIds", menuIds);
			System.out.println("map " + map);

			StaionListWithFranchiseeList[] array = restTemplate.postForObject(Constants.url + "/getAbcDepatchReportMin",
					map, StaionListWithFranchiseeList[].class);

			List<StaionListWithFranchiseeList> staionListWithFranchiseeList = new ArrayList<StaionListWithFranchiseeList>(
					Arrays.asList(array));

			System.err.println(staionListWithFranchiseeList);

			Item[] item = restTemplate.getForObject(Constants.url + "/getItemListForDispatchReport", Item[].class);

			List<Item> itemList = new ArrayList<Item>(Arrays.asList(item));

			AllFrIdNameList allFrIdNameList = restTemplate.getForObject(Constants.url + "/getAllFrIdName",
					AllFrIdNameList.class);

			List<ItemListStatioinWise> itemListStatioinWiseList = new ArrayList<>();

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				ItemListStatioinWise itemListStatioinWise = new ItemListStatioinWise();
				itemListStatioinWise.setStationNo(staionListWithFranchiseeList.get(i).getStationNo());
				List<TypeWiseItemTotal> typeWiseItemTotalList = new ArrayList<>();

				for (int j = 0; j < itemList.size(); j++) {

					if (itemList.get(j).getItemMrp2() == staionListWithFranchiseeList.get(i).getStationNo()) {

						TypeWiseItemTotal itemListForDispatchReport = new TypeWiseItemTotal();
						itemListForDispatchReport.setItemId(itemList.get(j).getId());
						itemListForDispatchReport.setItemName(itemList.get(j).getItemName());
						typeWiseItemTotalList.add(itemListForDispatchReport);
					}

				}
				itemListStatioinWise.setTypeWiseItemTotalList(typeWiseItemTotalList);
				itemListStatioinWiseList.add(itemListStatioinWise);
			}

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				for (int j = 0; j < itemListStatioinWiseList.size(); j++) {

					if (itemListStatioinWiseList.get(j).getStationNo() == staionListWithFranchiseeList.get(i)
							.getStationNo()) {

						for (int k = 0; k < staionListWithFranchiseeList.get(i).getList().size(); k++) {

							if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 1) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setaTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getaTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 2) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setbTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getbTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}

								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 3) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setcTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getcTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							}

						}

					}

				}

			}

			System.err.println("itemListStatioinWiseList" + itemListStatioinWiseList.toString());
			System.err.println("staionListWithFranchiseeList" + staionListWithFranchiseeList);
			model.addObject("itemListStatioinWiseList", itemListStatioinWiseList);
			model.addObject("staionListWithFranchiseeList", staionListWithFranchiseeList);
			model.addObject("itemList", itemList);
			model.addObject("allFrIdNameList", allFrIdNameList.getFrIdNamesList());
			model.addObject("date", date);
			model.addObject("abcType", abcType);
		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	@RequestMapping(value = "/pdf/getPDispatchReportNewPdf1/{date}/{stationId}/{abcType}/{routId}/{menuIds}", method = RequestMethod.GET)
	public ModelAndView getPDispatchReportNewPdf1(@PathVariable String date, @PathVariable String stationId,
			@PathVariable int abcType, @PathVariable int routId, @PathVariable String menuIds,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/dispatchPReportNewPdf");

		try {

			RestTemplate restTemplate = new RestTemplate();
			String stationIds = new String();
			String abcTypes = new String();
			List<Integer> stIds = Stream.of(stationId.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			if (stIds.contains(-1)) {

				Integer[] array = restTemplate.getForObject(Constants.url + "/itemListGroupByStationNo",
						Integer[].class);
				List<Integer> stationList = new ArrayList<Integer>(Arrays.asList(array));
				model.addObject("stationList", stationList);

				for (int i = 0; i < stationList.size(); i++) {

					stationIds = stationIds + "," + stationList.get(i);
				}
				stationIds = stationIds.substring(1, stationIds.length());

			} else {
				stationIds = String.valueOf(stationId);
			}
			System.out.println(stationIds + "stationIds");
			if (abcType == 0) {

				abcTypes = "1,2,3";

			} else {
				abcTypes = String.valueOf(abcType);
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("date", DateConvertor.convertToYMD(date));
			map.add("abcType", abcTypes);
			map.add("stationNos", stationIds);
			map.add("routId", routId);
			map.add("menuIds", menuIds);
			System.out.println("map " + map);

			StaionListWithFranchiseeList[] array = restTemplate.postForObject(
					Constants.url + "/getAbcDepatchReportMin1", map, StaionListWithFranchiseeList[].class);

			List<StaionListWithFranchiseeList> staionListWithFranchiseeList = new ArrayList<StaionListWithFranchiseeList>(
					Arrays.asList(array));

			System.err.println(staionListWithFranchiseeList);

			Item[] item = restTemplate.getForObject(Constants.url + "/getItemListForDispatchReport", Item[].class);

			List<Item> itemList = new ArrayList<Item>(Arrays.asList(item));

			AllFrIdNameList allFrIdNameList = restTemplate.getForObject(Constants.url + "/getAllFrIdName",
					AllFrIdNameList.class);

			List<ItemListStatioinWise> itemListStatioinWiseList = new ArrayList<>();

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				ItemListStatioinWise itemListStatioinWise = new ItemListStatioinWise();
				itemListStatioinWise.setStationNo(staionListWithFranchiseeList.get(i).getStationNo());
				List<TypeWiseItemTotal> typeWiseItemTotalList = new ArrayList<>();

				for (int j = 0; j < itemList.size(); j++) {

					if (itemList.get(j).getItemMrp2() == staionListWithFranchiseeList.get(i).getStationNo()) {

						TypeWiseItemTotal itemListForDispatchReport = new TypeWiseItemTotal();
						itemListForDispatchReport.setItemId(itemList.get(j).getId());
						itemListForDispatchReport.setItemName(itemList.get(j).getItemName());
						typeWiseItemTotalList.add(itemListForDispatchReport);
					}

				}
				itemListStatioinWise.setTypeWiseItemTotalList(typeWiseItemTotalList);
				itemListStatioinWiseList.add(itemListStatioinWise);
			}

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				for (int j = 0; j < itemListStatioinWiseList.size(); j++) {

					if (itemListStatioinWiseList.get(j).getStationNo() == staionListWithFranchiseeList.get(i)
							.getStationNo()) {

						for (int k = 0; k < staionListWithFranchiseeList.get(i).getList().size(); k++) {

							if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 1) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setaTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getaTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 2) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setbTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getbTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}

								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 3) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setcTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getcTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							}

						}

					}

				}

			}

			System.err.println("itemListStatioinWiseList" + itemListStatioinWiseList.toString());
			System.err.println("staionListWithFranchiseeList" + staionListWithFranchiseeList);
			model.addObject("itemListStatioinWiseList", itemListStatioinWiseList);
			model.addObject("staionListWithFranchiseeList", staionListWithFranchiseeList);
			model.addObject("itemList", itemList);
			model.addObject("allFrIdNameList", allFrIdNameList.getFrIdNamesList());
			model.addObject("date", date);
			model.addObject("abcType", abcType);
		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	@RequestMapping(value = "getPDispatchReportNewPdforDtMatrix/{date}/{stationId}/{abcType}/{routId}/{menuIds}", method = RequestMethod.GET)
	public ModelAndView getPDispatchReportNewPdforDtMatrix(@PathVariable String date, @PathVariable String stationId,
			@PathVariable int abcType, @PathVariable int routId, @PathVariable String menuIds,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/dispatchPReportNewPdfDTMatrix");

		try {

			RestTemplate restTemplate = new RestTemplate();
			String stationIds = new String();
			String abcTypes = new String();
			List<Integer> stIds = Stream.of(stationId.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			if (stIds.contains(-1)) {

				Integer[] array = restTemplate.getForObject(Constants.url + "/itemListGroupByStationNo",
						Integer[].class);
				List<Integer> stationList = new ArrayList<Integer>(Arrays.asList(array));
				model.addObject("stationList", stationList);

				for (int i = 0; i < stationList.size(); i++) {

					stationIds = stationIds + "," + stationList.get(i);
				}
				stationIds = stationIds.substring(1, stationIds.length());

			} else {
				stationIds = String.valueOf(stationId);
			}
			System.out.println(stationIds + "stationIds");
			if (abcType == 0) {

				abcTypes = "1,2,3";

			} else {
				abcTypes = String.valueOf(abcType);
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("date", DateConvertor.convertToYMD(date));
			map.add("abcType", abcTypes);
			map.add("stationNos", stationIds);
			map.add("routId", routId);
			map.add("menuIds", menuIds);
			System.out.println("map " + map);

			StaionListWithFranchiseeList[] array = restTemplate.postForObject(Constants.url + "/getAbcDepatchReportMin",
					map, StaionListWithFranchiseeList[].class);

			List<StaionListWithFranchiseeList> staionListWithFranchiseeList = new ArrayList<StaionListWithFranchiseeList>(
					Arrays.asList(array));

			System.err.println(staionListWithFranchiseeList);

			Item[] item = restTemplate.getForObject(Constants.url + "/getItemListForDispatchReport", Item[].class);

			List<Item> itemList = new ArrayList<Item>(Arrays.asList(item));

			AllFrIdNameList allFrIdNameList = restTemplate.getForObject(Constants.url + "/getAllFrIdName",
					AllFrIdNameList.class);

			List<ItemListStatioinWise> itemListStatioinWiseList = new ArrayList<>();

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				ItemListStatioinWise itemListStatioinWise = new ItemListStatioinWise();
				itemListStatioinWise.setStationNo(staionListWithFranchiseeList.get(i).getStationNo());
				List<TypeWiseItemTotal> typeWiseItemTotalList = new ArrayList<>();

				for (int j = 0; j < itemList.size(); j++) {

					if (itemList.get(j).getItemMrp2() == staionListWithFranchiseeList.get(i).getStationNo()) {

						TypeWiseItemTotal itemListForDispatchReport = new TypeWiseItemTotal();
						itemListForDispatchReport.setItemId(itemList.get(j).getId());
						itemListForDispatchReport.setItemName(itemList.get(j).getItemName());
						typeWiseItemTotalList.add(itemListForDispatchReport);
					}

				}
				itemListStatioinWise.setTypeWiseItemTotalList(typeWiseItemTotalList);
				itemListStatioinWiseList.add(itemListStatioinWise);
			}

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				for (int j = 0; j < itemListStatioinWiseList.size(); j++) {

					if (itemListStatioinWiseList.get(j).getStationNo() == staionListWithFranchiseeList.get(i)
							.getStationNo()) {

						for (int k = 0; k < staionListWithFranchiseeList.get(i).getList().size(); k++) {

							if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 1) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setaTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getaTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 2) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setbTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getbTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}

								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 3) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setcTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getcTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							}

						}

					}

				}

			}

			System.err.println("itemListStatioinWiseList" + itemListStatioinWiseList.toString());
			System.err.println("staionListWithFranchiseeList" + staionListWithFranchiseeList);
			model.addObject("itemListStatioinWiseList", itemListStatioinWiseList);
			model.addObject("staionListWithFranchiseeList", staionListWithFranchiseeList);
			model.addObject("itemList", itemList);
			model.addObject("allFrIdNameList", allFrIdNameList.getFrIdNamesList());
			model.addObject("date", date);
			model.addObject("abcType", abcType);
		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	@RequestMapping(value = "/pdf/getPDispatchReportMinPdf/{date}/{stationId}/{abcType}/{routId}/{menuIds}", method = RequestMethod.GET)
	public ModelAndView getPDispatchReportMinPdf(@PathVariable String date, @PathVariable String stationId,
			@PathVariable int abcType, @PathVariable int routId, @PathVariable String menuIds,
			HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/sales/dispatchPReportNewPdf");

		try {

			RestTemplate restTemplate = new RestTemplate();
			String stationIds = new String();
			String abcTypes = new String();
			List<Integer> stIds = Stream.of(stationId.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			if (stIds.contains(-1)) {

				Integer[] array = restTemplate.getForObject(Constants.url + "/itemListGroupByStationNo",
						Integer[].class);
				List<Integer> stationList = new ArrayList<Integer>(Arrays.asList(array));
				model.addObject("stationList", stationList);

				for (int i = 0; i < stationList.size(); i++) {

					stationIds = stationIds + "," + stationList.get(i);
				}
				stationIds = stationIds.substring(1, stationIds.length());

			} else {
				stationIds = String.valueOf(stationId);
			}
			System.out.println(stationIds + "stationIds");
			if (abcType == 0) {

				abcTypes = "1,2,3";

			} else {
				abcTypes = String.valueOf(abcType);
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("date", DateConvertor.convertToYMD(date));
			map.add("abcType", abcTypes);
			map.add("stationNos", stationIds);
			map.add("routId", routId);
			map.add("menuIds", menuIds);
			System.out.println("map " + map);

			StaionListWithFranchiseeList[] array = restTemplate.postForObject(Constants.url + "/getAbcDepatchReport",
					map, StaionListWithFranchiseeList[].class);

			List<StaionListWithFranchiseeList> staionListWithFranchiseeList = new ArrayList<StaionListWithFranchiseeList>(
					Arrays.asList(array));

			System.err.println(staionListWithFranchiseeList);

			Item[] item = restTemplate.getForObject(Constants.url + "/getItemListForDispatchReport", Item[].class);

			List<Item> itemList = new ArrayList<Item>(Arrays.asList(item));

			AllFrIdNameList allFrIdNameList = restTemplate.getForObject(Constants.url + "/getAllFrIdName",
					AllFrIdNameList.class);

			List<ItemListStatioinWise> itemListStatioinWiseList = new ArrayList<>();

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				ItemListStatioinWise itemListStatioinWise = new ItemListStatioinWise();
				itemListStatioinWise.setStationNo(staionListWithFranchiseeList.get(i).getStationNo());
				List<TypeWiseItemTotal> typeWiseItemTotalList = new ArrayList<>();

				for (int j = 0; j < itemList.size(); j++) {

					if (itemList.get(j).getItemMrp2() == staionListWithFranchiseeList.get(i).getStationNo()) {

						TypeWiseItemTotal itemListForDispatchReport = new TypeWiseItemTotal();
						itemListForDispatchReport.setItemId(itemList.get(j).getId());
						itemListForDispatchReport.setItemName(itemList.get(j).getItemName());
						typeWiseItemTotalList.add(itemListForDispatchReport);
					}

				}
				itemListStatioinWise.setTypeWiseItemTotalList(typeWiseItemTotalList);
				itemListStatioinWiseList.add(itemListStatioinWise);
			}

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				for (int j = 0; j < itemListStatioinWiseList.size(); j++) {

					if (itemListStatioinWiseList.get(j).getStationNo() == staionListWithFranchiseeList.get(i)
							.getStationNo()) {

						for (int k = 0; k < staionListWithFranchiseeList.get(i).getList().size(); k++) {

							if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 1) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setaTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getaTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 2) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setbTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getbTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}

								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 3) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setcTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getcTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							}

						}

					}

				}

			}

			model.addObject("itemListStatioinWiseList", itemListStatioinWiseList);
			model.addObject("staionListWithFranchiseeList", staionListWithFranchiseeList);
			model.addObject("itemList", itemList);
			model.addObject("allFrIdNameList", allFrIdNameList.getFrIdNamesList());
			model.addObject("date", date);
			model.addObject("abcType", abcType);
		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}
		return model;

	}

	@RequestMapping(value = "/getPDispatchReportStationwisePdf/{date}/{stationId}/{abcType}/{routId}/{menuIds}", method = RequestMethod.GET)
	public void getPDispatchReportStationwisePdf(@PathVariable String date, @PathVariable int stationId,
			@PathVariable int abcType, @PathVariable int routId, @PathVariable String menuIds,
			HttpServletRequest request, HttpServletResponse response) {

		try {

			RestTemplate restTemplate = new RestTemplate();
			String stationIds = new String();
			String abcTypes = new String();

			if (stationId == -1) {

				Integer[] array = restTemplate.getForObject(Constants.url + "/itemListGroupByStationNo",
						Integer[].class);
				List<Integer> stationList = new ArrayList<Integer>(Arrays.asList(array));
				// model.addObject("stationList", stationList);

				for (int i = 0; i < stationList.size(); i++) {

					stationIds = stationIds + "," + stationList.get(i);
				}
				stationIds = stationIds.substring(1, stationIds.length());

			} else {
				stationIds = String.valueOf(stationId);
			}

			if (abcType == 0) {

				abcTypes = "1,2,3";

			} else {
				abcTypes = String.valueOf(abcType);
			}

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("date", DateConvertor.convertToYMD(date));
			map.add("abcType", abcTypes);
			map.add("stationNos", stationIds);
			map.add("routId", routId);
			map.add("menuIds", menuIds);
			System.out.println("map " + map);

			StaionListWithFranchiseeList[] array = restTemplate.postForObject(Constants.url + "/getAbcDepatchReport",
					map, StaionListWithFranchiseeList[].class);

			List<StaionListWithFranchiseeList> staionListWithFranchiseeList = new ArrayList<StaionListWithFranchiseeList>(
					Arrays.asList(array));

			System.err.println(staionListWithFranchiseeList);

			Item[] item = restTemplate.getForObject(Constants.url + "/getItemListForDispatchReport", Item[].class);

			List<Item> itemList = new ArrayList<Item>(Arrays.asList(item));

			AllFrIdNameList allFrIdNameList = restTemplate.getForObject(Constants.url + "/getAllFrIdName",
					AllFrIdNameList.class);

			List<ItemListStatioinWise> itemListStatioinWiseList = new ArrayList<>();

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				ItemListStatioinWise itemListStatioinWise = new ItemListStatioinWise();
				itemListStatioinWise.setStationNo(staionListWithFranchiseeList.get(i).getStationNo());
				List<TypeWiseItemTotal> typeWiseItemTotalList = new ArrayList<>();

				for (int j = 0; j < itemList.size(); j++) {

					if (itemList.get(j).getItemMrp2() == staionListWithFranchiseeList.get(i).getStationNo()) {

						TypeWiseItemTotal itemListForDispatchReport = new TypeWiseItemTotal();
						itemListForDispatchReport.setItemId(itemList.get(j).getId());
						itemListForDispatchReport.setItemName(itemList.get(j).getItemName());
						typeWiseItemTotalList.add(itemListForDispatchReport);
					}

				}
				itemListStatioinWise.setTypeWiseItemTotalList(typeWiseItemTotalList);
				itemListStatioinWiseList.add(itemListStatioinWise);
			}

			for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

				for (int j = 0; j < itemListStatioinWiseList.size(); j++) {

					if (itemListStatioinWiseList.get(j).getStationNo() == staionListWithFranchiseeList.get(i)
							.getStationNo()) {

						for (int k = 0; k < staionListWithFranchiseeList.get(i).getList().size(); k++) {

							if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 1) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setaTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getaTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 2) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setbTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getbTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}

								}

							} else if (staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 3) {

								for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k).getItemList()
										.size(); m++) {
									for (int l = 0; l < itemListStatioinWiseList.get(j).getTypeWiseItemTotalList()
											.size(); l++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getItemId() == itemListStatioinWiseList.get(j)
														.getTypeWiseItemTotalList().get(l).getItemId()) {
											itemListStatioinWiseList.get(j).getTypeWiseItemTotalList().get(l)
													.setcTotal(itemListStatioinWiseList.get(j)
															.getTypeWiseItemTotalList().get(l).getcTotal()
															+ staionListWithFranchiseeList.get(i).getList().get(k)
																	.getItemList().get(m).getOrderQty());
											break;
										}
									}
								}

							}

						}

					}

				}

			}

			// model.addObject("itemListStatioinWiseList", itemListStatioinWiseList);
			// model.addObject("staionListWithFranchiseeList",
			// staionListWithFranchiseeList);
			// model.addObject("itemList", itemList);
			// model.addObject("allFrIdNameList", allFrIdNameList.getFrIdNamesList());
			// model.addObject("date", date);
			// model.addObject("abcType", abcType);

			BufferedOutputStream outStream = null;
			Document document = new Document(PageSize.A4);

			String FILE_PATH = Constants.REPORT_SAVE;
			File file = new File(FILE_PATH);

			PdfWriter writer = null;

			FileOutputStream out = new FileOutputStream(FILE_PATH);
			try {
				writer = PdfWriter.getInstance(document, out);
			} catch (DocumentException e) {

				e.printStackTrace();
			}
			document.open();

			try {
				for (int i = 0; i < staionListWithFranchiseeList.size(); i++) {

					Paragraph stNo = new Paragraph(
							"Station No:" + staionListWithFranchiseeList.get(i).getStationNo()
									+ " &nbsp; Production Date :" + date,
							new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK));
					document.add(stNo);
					if (abcType == 0) {
						document.add(new Paragraph("  All Routes\n"));
					} else if (abcType == 1) {
						document.add(new Paragraph("A Route\n"));
					} else if (abcType == 2) {
						document.add(new Paragraph("B Route\n"));
					} else {
						document.add(new Paragraph("C Route\n"));
					}

					PdfPTable table = new PdfPTable(4);
					table.setHeaderRows(1);

					table.setWidthPercentage(100);
					table.setWidths(new float[] { 0.4f, 4.0f, 1.9f, 1.7f });
					Font headFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
					Font headFont1 = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
					Font f = new Font(FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLUE);
					Font f1 = new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL, BaseColor.BLACK);
					PdfPCell hcell = new PdfPCell();

					hcell = new PdfPCell(new Phrase("Sr.", headFont1));
					hcell.setPadding(5);
					hcell.setBackgroundColor(BaseColor.PINK);
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(hcell);

					hcell = new PdfPCell(new Phrase("FRANCHISE NAME", headFont1));
					hcell.setBackgroundColor(BaseColor.PINK);
					hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					hcell.setPadding(5);
					table.addCell(hcell);
					int itemCount = 2;
					for (int j = 0; j < itemList.size(); i++) {
						if (itemList.get(j).getItemMrp2() == staionListWithFranchiseeList.get(i).getStationNo()) {
							hcell = new PdfPCell(new Phrase("" + itemList.get(j).getItemName(), headFont1));
							hcell.setPadding(5);
							hcell.setBackgroundColor(BaseColor.PINK);
							hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
							table.addCell(hcell);

							itemCount = itemCount + 1;

						}
					}

					if (abcType == 0 || abcType == 1) {
						int srNo = 0;
						for (int k = 0; k < staionListWithFranchiseeList.get(i).getList().size(); k++) {
							for (int l = 0; l < allFrIdNameList.getFrIdNamesList().size(); l++) {
								if (allFrIdNameList.getFrIdNamesList().get(l).getFrId() == staionListWithFranchiseeList
										.get(i).getList().get(k).getFrId()
										&& staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 1) {

									PdfPCell cell;
									srNo = srNo + 1;
									cell = new PdfPCell(new Phrase((srNo + 1) + "", headFont));
									cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setHorizontalAlignment(Element.ALIGN_CENTER);
									cell.setPadding(4);
									table.addCell(cell);

									cell = new PdfPCell(new Phrase(
											"" + staionListWithFranchiseeList.get(i).getList().get(k).getFrName(),
											headFont));
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
									cell.setHorizontalAlignment(Element.ALIGN_LEFT);
									cell.setPaddingRight(2);
									cell.setPadding(4);
									table.addCell(cell);

									for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k)
											.getItemList().size(); m++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getOrderQty() > 0) {
											cell = new PdfPCell(new Phrase("" + staionListWithFranchiseeList.get(i)
													.getList().get(k).getItemList().get(m).getOrderQty(), headFont));
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setHorizontalAlignment(Element.ALIGN_LEFT);
											cell.setPaddingRight(2);
											cell.setPadding(4);
											table.addCell(cell);
										} else {
											cell = new PdfPCell(new Phrase("", headFont));
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setHorizontalAlignment(Element.ALIGN_LEFT);
											cell.setPaddingRight(2);
											cell.setPadding(4);
											table.addCell(cell);
										}

									}

								}

							}
						}

						PdfPCell cell;

						cell = new PdfPCell(new Phrase("Route A Total", headFont));
						cell.setColspan(2);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setPadding(4);
						table.addCell(cell);

						for (int n = 0; n < itemListStatioinWiseList.size(); n++) {
							if (itemListStatioinWiseList.get(n).getStationNo() == staionListWithFranchiseeList.get(i)
									.getStationNo()) {
								for (int o = 0; o < itemListStatioinWiseList.get(n).getTypeWiseItemTotalList()
										.size(); o++) {
									cell = new PdfPCell(new Phrase("" + itemListStatioinWiseList.get(n)
											.getTypeWiseItemTotalList().get(o).getbTotal(), headFont));
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setHorizontalAlignment(Element.ALIGN_LEFT);
									cell.setPaddingRight(2);
									cell.setPadding(4);
									table.addCell(cell);
								}
							}

						}

					}
					if (abcType == 0 || abcType == 2) {

						int srNo = 0;
						for (int k = 0; k < staionListWithFranchiseeList.get(i).getList().size(); k++) {
							for (int l = 0; l < allFrIdNameList.getFrIdNamesList().size(); l++) {
								if (allFrIdNameList.getFrIdNamesList().get(l).getFrId() == staionListWithFranchiseeList
										.get(i).getList().get(k).getFrId()
										&& staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 2) {

									PdfPCell cell;
									srNo = srNo + 1;
									cell = new PdfPCell(new Phrase((srNo + 1) + "", headFont));
									cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setHorizontalAlignment(Element.ALIGN_CENTER);
									cell.setPadding(4);
									table.addCell(cell);

									cell = new PdfPCell(new Phrase(
											"" + staionListWithFranchiseeList.get(i).getList().get(k).getFrName(),
											headFont));
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
									cell.setHorizontalAlignment(Element.ALIGN_LEFT);
									cell.setPaddingRight(2);
									cell.setPadding(4);
									table.addCell(cell);

									for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k)
											.getItemList().size(); m++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getOrderQty() > 0) {
											cell = new PdfPCell(new Phrase("" + staionListWithFranchiseeList.get(i)
													.getList().get(k).getItemList().get(m).getOrderQty(), headFont));
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setHorizontalAlignment(Element.ALIGN_LEFT);
											cell.setPaddingRight(2);
											cell.setPadding(4);
											table.addCell(cell);
										} else {
											cell = new PdfPCell(new Phrase("", headFont));
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setHorizontalAlignment(Element.ALIGN_LEFT);
											cell.setPaddingRight(2);
											cell.setPadding(4);
											table.addCell(cell);
										}

									}

								}

							}
						}

						PdfPCell cell;

						cell = new PdfPCell(new Phrase("Route B Total", headFont));
						cell.setColspan(2);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setPadding(4);
						table.addCell(cell);

						for (int n = 0; n < itemListStatioinWiseList.size(); n++) {
							if (itemListStatioinWiseList.get(n).getStationNo() == staionListWithFranchiseeList.get(i)
									.getStationNo()) {
								for (int o = 0; o < itemListStatioinWiseList.get(n).getTypeWiseItemTotalList()
										.size(); o++) {
									cell = new PdfPCell(new Phrase("" + itemListStatioinWiseList.get(n)
											.getTypeWiseItemTotalList().get(o).getaTotal(), headFont));
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setHorizontalAlignment(Element.ALIGN_LEFT);
									cell.setPaddingRight(2);
									cell.setPadding(4);
									table.addCell(cell);
								}
							}

						}

					}

					if (abcType == 0 || abcType == 3) {

						int srNo = 0;
						for (int k = 0; k < staionListWithFranchiseeList.get(i).getList().size(); k++) {
							for (int l = 0; l < allFrIdNameList.getFrIdNamesList().size(); l++) {
								if (allFrIdNameList.getFrIdNamesList().get(l).getFrId() == staionListWithFranchiseeList
										.get(i).getList().get(k).getFrId()
										&& staionListWithFranchiseeList.get(i).getList().get(k).getAbcType() == 3) {

									PdfPCell cell;
									srNo = srNo + 1;
									cell = new PdfPCell(new Phrase((srNo + 1) + "", headFont));
									cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setHorizontalAlignment(Element.ALIGN_CENTER);
									cell.setPadding(4);
									table.addCell(cell);

									cell = new PdfPCell(new Phrase(
											"" + staionListWithFranchiseeList.get(i).getList().get(k).getFrName(),
											headFont));
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
									cell.setHorizontalAlignment(Element.ALIGN_LEFT);
									cell.setPaddingRight(2);
									cell.setPadding(4);
									table.addCell(cell);

									for (int m = 0; m < staionListWithFranchiseeList.get(i).getList().get(k)
											.getItemList().size(); m++) {
										if (staionListWithFranchiseeList.get(i).getList().get(k).getItemList().get(m)
												.getOrderQty() > 0) {
											cell = new PdfPCell(new Phrase("" + staionListWithFranchiseeList.get(i)
													.getList().get(k).getItemList().get(m).getOrderQty(), headFont));
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setHorizontalAlignment(Element.ALIGN_LEFT);
											cell.setPaddingRight(2);
											cell.setPadding(4);
											table.addCell(cell);
										} else {
											cell = new PdfPCell(new Phrase("", headFont));
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setHorizontalAlignment(Element.ALIGN_LEFT);
											cell.setPaddingRight(2);
											cell.setPadding(4);
											table.addCell(cell);
										}

									}

								}

							}
						}

						PdfPCell cell;

						cell = new PdfPCell(new Phrase("Rout C Total", headFont));
						cell.setColspan(2);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setPadding(4);
						table.addCell(cell);

						for (int n = 0; n < itemListStatioinWiseList.size(); n++) {
							if (itemListStatioinWiseList.get(n).getStationNo() == staionListWithFranchiseeList.get(i)
									.getStationNo()) {
								for (int o = 0; o < itemListStatioinWiseList.get(n).getTypeWiseItemTotalList()
										.size(); o++) {
									cell = new PdfPCell(new Phrase("" + itemListStatioinWiseList.get(n)
											.getTypeWiseItemTotalList().get(o).getcTotal(), headFont));
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setHorizontalAlignment(Element.ALIGN_LEFT);
									cell.setPaddingRight(2);
									cell.setPadding(4);
									table.addCell(cell);
								}
							}

						}

					}
					PdfPCell cell;

					cell = new PdfPCell(new Phrase(
							"Station " + staionListWithFranchiseeList.get(i).getStationNo() + " Total ", headFont));
					cell.setColspan(2);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setPadding(4);
					table.addCell(cell);

					for (int n = 0; n < itemListStatioinWiseList.size(); n++) {
						if (itemListStatioinWiseList.get(n).getStationNo() == staionListWithFranchiseeList.get(i)
								.getStationNo()) {
							for (int o = 0; o < itemListStatioinWiseList.get(n).getTypeWiseItemTotalList()
									.size(); o++) {
								cell = new PdfPCell(new Phrase((itemListStatioinWiseList.get(n)
										.getTypeWiseItemTotalList().get(o).getaTotal()
										+ itemListStatioinWiseList.get(n).getTypeWiseItemTotalList().get(o).getbTotal()
										+ itemListStatioinWiseList.get(n).getTypeWiseItemTotalList().get(o).getcTotal())
										+ "", headFont));
								cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								cell.setPaddingRight(2);
								cell.setPadding(4);
								table.addCell(cell);
							}
						}

					}

					/*
					 * for(int j=0;j<subCatAList.size();j++) { int flagnew=0; int srNo=1; for(int
					 * k=0;k<itemsList.size();k++) {
					 * if(itemsList.get(k).getItemGrp2()==subCatAList.get(j).getSubCatId()) { int
					 * editQty=0;
					 * 
					 * for(int l=0;l<frNameIdByRouteIdList.size();l++) {
					 * 
					 * if(frNameIdByRouteIdList.get(l).getFrId()==frListOrdersPresent.get(i)) {
					 * for(int m=0;m<dispatchReportList.size();m++) {
					 * 
					 * 
					 * if(dispatchReportList.get(m).getItemId()==itemsList.get(k).getId()) {
					 * if(dispatchReportList.get(m).getFrId()==frNameIdByRouteIdList.get(l).getFrId(
					 * )) { editQty=dispatchReportList.get(m).getEditQty(); }
					 * 
					 * }
					 * 
					 * 
					 * } }
					 * 
					 * }
					 * 
					 * if(editQty>0) { if(flagnew==0) { PdfPCell cell;
					 * 
					 * cell = new PdfPCell(new Phrase("", headFont));
					 * cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setPadding(4);
					 * table.addCell(cell);
					 * 
					 * cell = new PdfPCell(new Phrase("" +subCatAList.get(j).getSubCatName() ,
					 * headFont)); cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					 * cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					 * cell.setHorizontalAlignment(Element.ALIGN_LEFT); cell.setPaddingRight(2);
					 * cell.setPadding(4); table.addCell(cell);
					 * 
					 * cell = new PdfPCell(new Phrase("", headFont));
					 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					 * cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setPaddingRight(2);
					 * cell.setPadding(4); table.addCell(cell);
					 * 
					 * cell = new PdfPCell(new Phrase("", headFont));
					 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					 * cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					 * cell.setHorizontalAlignment(Element.ALIGN_LEFT); cell.setPaddingRight(2);
					 * cell.setPadding(4); table.addCell(cell);
					 * 
					 * flagnew=1; }
					 * 
					 * PdfPCell cell;
					 * 
					 * cell = new PdfPCell(new Phrase(""+srNo, headFont));
					 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setPadding(4);
					 * table.addCell(cell);
					 * 
					 * srNo=srNo+1;
					 * 
					 * cell = new PdfPCell(new Phrase("" +itemsList.get(k).getItemName() ,
					 * headFont)); cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					 * cell.setHorizontalAlignment(Element.ALIGN_LEFT); cell.setPaddingRight(2);
					 * cell.setPadding(4); table.addCell(cell);
					 * 
					 * cell = new PdfPCell(new Phrase(""+editQty, headFont));
					 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setPaddingRight(2);
					 * cell.setPadding(4); table.addCell(cell);
					 * 
					 * cell = new PdfPCell(new Phrase("", headFont));
					 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					 * cell.setHorizontalAlignment(Element.ALIGN_LEFT); cell.setPaddingRight(2);
					 * cell.setPadding(4); table.addCell(cell);
					 * 
					 * }
					 * 
					 * 
					 * 
					 * } } }
					 */

					DateFormat DF = new SimpleDateFormat("dd-MM-yyyy");
					String reportDate = DF.format(new Date());
					/*
					 * for(int l=0;l<frNameIdByRouteIdList.size();l++) {
					 * if(frNameIdByRouteIdList.get(l).getFrId()==frListOrdersPresent.get(i)) {
					 * 
					 * Paragraph company = new
					 * Paragraph("MONGINIS -- SHOP NAME :--"+frNameIdByRouteIdList.get(l).getFrName(
					 * ) + "\n", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD,
					 * BaseColor.MAGENTA)); company.setAlignment(Element.ALIGN_CENTER);
					 * document.add(company); Paragraph header=new
					 * Paragraph(" Dispatch Sheet    Dispatch Date:  "+billDate,f1);
					 * document.add(header); } }
					 */
					document.add(new Paragraph("\n"));
					document.add(table);
					document.add(new Paragraph("\n"));

					int totalPages = writer.getPageNumber();

					System.out.println("Page no " + totalPages);
					document.newPage();
				}
				document.close();
				// Atul Sir code to open a Pdf File
				if (file != null) {

					String mimeType = URLConnection.guessContentTypeFromName(file.getName());

					if (mimeType == null) {

						mimeType = "application/pdf";

					}

					response.setContentType(mimeType);

					response.addHeader("content-disposition", String.format("inline; filename=\"%s\"", file.getName()));

					response.setContentLength((int) file.length());

					InputStream inputStream = null;
					try {
						inputStream = new BufferedInputStream(new FileInputStream(file));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					try {
						FileCopyUtils.copy(inputStream, response.getOutputStream());
					} catch (IOException e) {
						System.out.println("Excep in Opening a Pdf File");
						e.printStackTrace();
					}
				}

			} catch (DocumentException ex) {

				System.out.println("Pdf Generation Error: BOm Prod  View Prod" + ex.getMessage());

				ex.printStackTrace();

			}
		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}
		// return model;

	}

	@RequestMapping(value = "/addSection", method = RequestMethod.GET)
	public ModelAndView addSection(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("masters/addSection");

		try {

			RestTemplate restTemplate = new RestTemplate();

			SectionMaster[] array = restTemplate.getForObject(Constants.url + "/getSectionList", SectionMaster[].class);
			List<SectionMaster> sectionList = new ArrayList<SectionMaster>(Arrays.asList(array));
			model.addObject("sectionList", sectionList);

			Menu[] allMenus = restTemplate.getForObject(Constants.url + "/getAllMenuList", Menu[].class);
			List<Menu> menuList = new ArrayList<Menu>(Arrays.asList(allMenus));

			model.addObject("menuList", menuList);

		} catch (Exception e) {

			System.out.println("Exc in show sales report bill wise  " + e.getMessage());
			e.printStackTrace();
		}
		return model;

	}

	@RequestMapping(value = "/insertSection", method = RequestMethod.POST)
	public String deleteSection(HttpServletRequest request, HttpServletResponse response) {

		RestTemplate restTemplate = new RestTemplate();

		try {

			String sectionId = request.getParameter("sectionId");
			String sectionName = request.getParameter("sectionName");
			String[] menuId = request.getParameterValues("menuIds");

			String menuIds = new String();

			for (int i = 0; i < menuId.length; i++) {

				menuIds = menuIds + "," + menuId[i];

			}
			menuIds.substring(1, menuIds.length());

			SectionMaster save = new SectionMaster();

			if (sectionId.equalsIgnoreCase("") || sectionId.equalsIgnoreCase(null)) {

			} else {
				save.setSectionId(Integer.parseInt(sectionId));
			}

			save.setSectionName(sectionName);
			save.setMenuIds(menuIds);

			SectionMaster res = restTemplate.postForObject(Constants.url + "/saveSection", save, SectionMaster.class);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "redirect:/addSection";

	}

	@RequestMapping(value = "/deleteSection/{sectionId}", method = RequestMethod.GET)
	public String deleteSection(@PathVariable int sectionId, HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("masters/addSection");

		try {

			RestTemplate restTemplate = new RestTemplate();

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("sectionId", sectionId);

			ErrorMessage res = restTemplate.postForObject(Constants.url + "/deleteSection", map, ErrorMessage.class);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "redirect:/addSection";

	}

	@RequestMapping(value = "/editSection/{sectionId}", method = RequestMethod.GET)
	public ModelAndView editSection(@PathVariable int sectionId, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("masters/addSection");

		try {

			RestTemplate restTemplate = new RestTemplate();

			SectionMaster[] array = restTemplate.getForObject(Constants.url + "/getSectionList", SectionMaster[].class);
			List<SectionMaster> sectionList = new ArrayList<SectionMaster>(Arrays.asList(array));
			model.addObject("sectionList", sectionList);

			Menu[] allMenus = restTemplate.getForObject(Constants.url + "/getAllMenuList", Menu[].class);
			List<Menu> menuList = new ArrayList<Menu>(Arrays.asList(allMenus));

			model.addObject("menuList", menuList);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("sectionId", sectionId);
			SectionMaster editSection = restTemplate.postForObject(Constants.url + "/getSectionById", map,
					SectionMaster.class);
			model.addObject("editSection", editSection);
			model.addObject("isEdit", 1);

		} catch (Exception e) {

			System.out.println("Exc in show sales report bill wise  " + e.getMessage());
			e.printStackTrace();
		}
		return model;

	}

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

	@RequestMapping(value = "/pdfForDisReportNew", method = RequestMethod.GET)
	public void showPDF1(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Inside PDf For Report URL ");
		String url = request.getParameter("url");
		System.out.println("URL " + url);

		File f = new File(Constants.DISPATCH_PATH);
		// File f = new File("/opt/apache-tomcat-8.5.6/webapps/uploads/report.pdf");
		// File f =new File("/home/ats-12/Report.pdf");

		try {
			runConverter1(Constants.ReportURL + url, f, request, response);
			// runConverter("www.google.com", f,request,response);

		} catch (IOException e) {

			System.out.println("Pdf conversion exception " + e.getMessage());
		}

		// get absolute path of the application
		ServletContext context = request.getSession().getServletContext();
		String appPath = context.getRealPath("");
		String filePath =Constants.DISPATCH_PATH;

		// String filePath ="/home/lenovo/Documents/pdf/Report.pdf";

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

				Dimension landscapeA4 = pd4ml.changePageOrientation(PD4Constants.A4);
				pd4ml.setPageSize(landscapeA4);

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

	@RequestMapping(value = "/SpCakeDispatchReport", method = RequestMethod.GET)
	public ModelAndView SpCakeDispatchReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/spCakeDispatchReport");

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);

			RestTemplate restTemplate = new RestTemplate();

			List<FranchiseeList> franchiseeList = null;

			FranchiseeAndMenuList franchiseeAndMenuList = restTemplate
					.getForObject(Constants.url + "getFranchiseeAndMenu", FranchiseeAndMenuList.class);
			franchiseeList = franchiseeAndMenuList.getAllFranchisee();

			model.addObject("franchiseeList", franchiseeList);

			/*
			 * Menu[] allMenus = restTemplate.getForObject(Constants.url +
			 * "/getAllMenuList", Menu[].class); menuList = new ArrayList
			 * (Arrays.asList(allMenus));
			 */

			model.addObject("menuList", menuList);
			model.addObject("todaysDate", todaysDate);

			SectionMaster[] sectionMasterArray = restTemplate.getForObject(Constants.url + "/getSectionListOnly",
					SectionMaster[].class);
			List<SectionMaster> sectionList = new ArrayList<SectionMaster>(Arrays.asList(sectionMasterArray));
			model.addObject("sectionList", sectionList);

		} catch (Exception e) {

			System.out.println("Exc in show sales report bill wise  " + e.getMessage());
			e.printStackTrace();
		}

		return model;

	}

	// ------------------------------------------------------Sumit Special cake
	// dispatch report SUMIT 19 Apr 2019
	// ------------------------------------------------------------------
	@RequestMapping(value = "pdf/getPDispatchFranchasewiseSpCake", method = RequestMethod.GET)
	public ModelAndView getPDispatchReportFranchasiwiseSpecialCakePdf(HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/specialCakeFranchasiWiseDispatchReportPdf");
		List<PDispatchReport> dispatchReportList = new ArrayList<PDispatchReport>();

		PDispatchReportList dispatchReports = new PDispatchReportList();
		try {
			System.out.println("Inside get Dispatch Report");
			String billDate = request.getParameter("bdate");
			String[] selectedFranchase = request.getParameterValues("frids");
			String[] selectedMenu = request.getParameterValues("menus");

			System.out.println("billDate" + billDate);
			System.out.println("selectedFranchase" + selectedFranchase);
			System.out.println("selectedMenu" + selectedMenu);

			String strselectedFranchase = new String();
			for (int i = 0; i < selectedFranchase.length; i++) {
				strselectedFranchase = strselectedFranchase + "," + selectedFranchase[i];
			}
			strselectedFranchase = strselectedFranchase.substring(1, strselectedFranchase.length());
			strselectedFranchase = strselectedFranchase.replaceAll("\"", "");

			String strselectedMenu = new String();
			for (int i = 0; i < selectedMenu.length; i++) {
				strselectedMenu = strselectedMenu + "," + selectedMenu[i];
			}
			strselectedMenu = strselectedMenu.substring(1, strselectedMenu.length());
			strselectedMenu = strselectedMenu.replaceAll("\"", "");

			System.out.println("strselectedFranchase" + strselectedFranchase.toString());
			System.out.println("strselectedMenu" + strselectedMenu.toString());

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			RestTemplate restTemplate = new RestTemplate();
			map = new LinkedMultiValueMap<String, Object>();
			map.add("menu", strselectedMenu);
			map.add("deliveryDate", billDate);
			map.add("frId", strselectedFranchase);

			ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
			};

			ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
					Constants.url + "getPDispatchFranchasewiseSpCake", HttpMethod.POST, new HttpEntity<>(map), typeRef);
			System.out.println("Items:" + responseEntity.toString());

			dispatchReportList = responseEntity.getBody();

			System.out.println("dispatchReportList = " + dispatchReportList.toString());

			model.addObject("dispatchReportList", dispatchReportList);

		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}

		return model;

	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------

}
