package com.ats.adminpanel.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
import com.ats.adminpanel.model.AllMenus;
import com.ats.adminpanel.model.CalCulateTray;
import com.ats.adminpanel.model.FranchiseForDispatch;
import com.ats.adminpanel.model.GetRouteMgmt;
import com.ats.adminpanel.model.ItemListStatioinWise;
import com.ats.adminpanel.model.RouteMaster;
import com.ats.adminpanel.model.RouteMgmt;
import com.ats.adminpanel.model.SectionMaster;
import com.ats.adminpanel.model.StaionListWithFranchiseeList;
import com.ats.adminpanel.model.TypeWiseItemTotal;
import com.ats.adminpanel.model.franchisee.SubCategory;
import com.ats.adminpanel.model.item.Item;

@Controller
@Scope("session")
public class CalculateTrayRepCon {

	String todaysDate;

	@RequestMapping(value = "/showCalculateTrayReport", method = RequestMethod.GET)
	public ModelAndView showCalculateTrayReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/calTrayReport");

		try {
			ZoneId z = ZoneId.of("Asia/Calcutta");

			LocalDate date = LocalDate.now(z);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
			todaysDate = date.format(formatters);

			RestTemplate restTemplate = new RestTemplate();

			GetRouteMgmt[] allRouteListResponse = restTemplate.getForObject(Constants.url + "/getRouteMgmtList",
					GetRouteMgmt[].class);

			List<GetRouteMgmt> routeList = new ArrayList<GetRouteMgmt>(Arrays.asList(allRouteListResponse));

			model.addObject("routeList", routeList);

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

	List<RouteMgmt> routeList = new ArrayList<>();
	List<AllMenus> menuList = new ArrayList<AllMenus>();

	@RequestMapping(value = "/routListByDelType", method = RequestMethod.GET)
	@ResponseBody
	public List<RouteMgmt> routListByDelType(HttpServletRequest request, HttpServletResponse response) {

		routeList = new ArrayList<>();

		try {

			int delType = Integer.parseInt(request.getParameter("delType"));

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("isSameDay", delType);
			RestTemplate restTemplate = new RestTemplate();

			RouteMgmt[] routeMaster = restTemplate.postForObject(Constants.url + "/getAllRouteMgmtListByDelType", map,
					RouteMgmt[].class);
			routeList = new ArrayList<RouteMgmt>(Arrays.asList(routeMaster));
			System.out.println("listlistlistlistlistlist" + routeList.toString());

		} catch (Exception e) {

			e.printStackTrace();
		}

		return routeList;

	}

	@RequestMapping(value = "/searchCalculateTrayReport", method = RequestMethod.GET)
	public ModelAndView searchCalculateTrayReport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/calTrayRepDetail");

		try {

			String date = request.getParameter("billDate");

			String[] routeIdList = request.getParameterValues("selectRoute");
			// -------------------------------------------------------------------------------
			int sectionId = Integer.parseInt(request.getParameter("sectionId"));

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("sectionId", sectionId);
			RestTemplate restTemplate = new RestTemplate();

			SectionMaster routeMasters = restTemplate.postForObject(Constants.url + "/getSectionById", map,
					SectionMaster.class);

			menuList = routeMasters.getMenuList();
			// -------------------------------------------------------------------------------
			System.out.println("routeIdListrouteIdListrouteIdListrouteIdList" + routeIdList);

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < routeIdList.length; i++) {
				sb = sb.append(routeIdList[i] + ",");
			}

			String routeIds = sb.toString();
			routeIds = routeIds.substring(0, routeIds.length() - 1);

			System.out.println("routeIdsrouteIdsrouteIdsrouteIdsrouteIdsrouteIdsrouteIdsrouteIds" + routeIds);

			String[] menuId = request.getParameterValues("menuId");

			String menuIds = new String();

			for (int i = 0; i < menuId.length; i++) {
				menuIds = menuIds + "," + menuId[i];
			}
			menuIds = menuIds.substring(1, menuIds.length());

			System.out.println("menuIdsmenuIdsmenuIdsmenuIdsmenuIdsmenuIds" + menuIds);

			sb = new StringBuilder();

			if (menuIds.contains("0")) {

				menuIds = new String();

				for (int i = 0; i < menuList.size(); i++) {
					menuIds = menuIds + "," + menuList.get(i).getMenuId();
				}
				menuIds = menuIds.substring(1, menuIds.length());
			}

			// RestTemplate restTemplate = new RestTemplate();

			map = new LinkedMultiValueMap<>();// change
			map.add("routeIdList", routeIds);

			RouteMgmt[] routeMaster = restTemplate.postForObject(Constants.url + "/getFranByMultipleRouteTrayId", map,
					RouteMgmt[].class);
			List<RouteMgmt> routeListForFr = new ArrayList<RouteMgmt>(Arrays.asList(routeMaster));
			System.out.println("RouteListRouteListRouteListRouteListRouteList" + routeListForFr.toString());

			String frIds = new String();

			for (int i = 0; i < routeListForFr.size(); i++) {
				frIds = frIds + "," + routeListForFr.get(i).getFrIds();

			}
			frIds = frIds.substring(1, frIds.length());

			System.out.println("frIdsfrIdsfrIdsfrIdsfrIdsfrIds" + frIds);

			map = new LinkedMultiValueMap<>();
			map.add("deliveryDate", DateConvertor.convertToYMD(date));
			map.add("frIdList", frIds);
			map.add("menuIdList", menuIds);
			map.add("routeIdList", routeIds);

			CalCulateTray[] calCulateTray = restTemplate.postForObject(Constants.url + "/getAllCalTrayReport", map,
					CalCulateTray[].class);
			List<CalCulateTray> calListForFr = new ArrayList<CalCulateTray>(Arrays.asList(calCulateTray));
			System.out.println("calListForFr" + calListForFr.toString());

			model.addObject("calListForFr", calListForFr);
			model.addObject("routeListForFr", routeListForFr);
			model.addObject("frIds", frIds);

			String submit1 = request.getParameter("submit1");
			String submit2 = request.getParameter("submit2");

			String submit3 = request.getParameter("submit3");

			String submit4 = request.getParameter("submit4");
			if (submit1 != null) {
				model.addObject("submit1", 1);
			} else if (submit2 != null) {

				model.addObject("submit2", 2);
			} else if (submit3 != null) {

				model.addObject("submit3", 3);
			} else if (submit4 != null) {

				model.addObject("submit4", 4);
			}

			SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
					SubCategory[].class);

			ArrayList<SubCategory> subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));
			System.out.println("subCatAList:" + subCatAList.toString());

			model.addObject("subCatAList", subCatAList);
			map = new LinkedMultiValueMap<>();
			map.add("frIds", frIds);

			FranchiseForDispatch[] frNameId = restTemplate
					.postForObject(Constants.url + "getFranchiseForDispatchByFrIds", map, FranchiseForDispatch[].class);

			List<FranchiseForDispatch> frNameIdByRouteIdList = new ArrayList<>(Arrays.asList(frNameId));

			model.addObject("frNameIdByRouteIdList", frNameIdByRouteIdList);
			model.addObject("date", date);
			model.addObject("routeIds", routeIds);
			model.addObject("menuIds", menuIds);

			map = new LinkedMultiValueMap<>();// change
			map.add("routeIdList", routeIds);

			GetRouteMgmt[] routeMaster1 = restTemplate.postForObject(Constants.url + "/getFranByMultipleRouteTrayIdNew",
					map, GetRouteMgmt[].class);
			List<GetRouteMgmt> routeListForFr1 = new ArrayList<GetRouteMgmt>(Arrays.asList(routeMaster1));
			System.out.println("RouteListRouteListRouteListRouteListRouteList" + routeListForFr1.toString());

			model.addObject("routeListForFr1", routeListForFr1);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return model;

	}

	@RequestMapping(value = "/getMenuListBySectionIdForTray", method = RequestMethod.GET)
	@ResponseBody
	public List<AllMenus> getMenuListBySectionIdForTray(HttpServletRequest request, HttpServletResponse response) {

		menuList = new ArrayList<>();

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

	// calculateTrayDetailPdf

	@RequestMapping(value = "/pdf/getCalculateTrayReportPDF/{date}/{routeIds}/{menuIds}/{submit}", method = RequestMethod.GET)
	public ModelAndView getPDispatchReportNewPdf(@PathVariable String date, @PathVariable String routeIds,
			@PathVariable String menuIds, @PathVariable String submit, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("reports/calculateTrayDetailPdf");

		try {

			RestTemplate restTemplate = new RestTemplate();

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("routeIdList", routeIds);

			RouteMgmt[] routeMaster = restTemplate.postForObject(Constants.url + "/getFranByMultipleRouteTrayId", map,
					RouteMgmt[].class);
			List<RouteMgmt> routeListForFr = new ArrayList<RouteMgmt>(Arrays.asList(routeMaster));
			System.out.println("RouteListRouteListRouteListRouteListRouteList" + routeListForFr.toString());
			String frIds = new String();

			for (int i = 0; i < routeListForFr.size(); i++) {
				frIds = frIds + "," + routeListForFr.get(i).getFrIds();

			}
			frIds = frIds.substring(1, frIds.length());

			System.out.println("frIdsfrIdsfrIdsfrIdsfrIdsfrIds" + frIds);

			map = new LinkedMultiValueMap<>();
			map.add("deliveryDate", DateConvertor.convertToYMD(date));
			map.add("frIdList", frIds);
			map.add("menuIdList", menuIds);
			map.add("routeIdList", routeIds);

			CalCulateTray[] calCulateTray = restTemplate.postForObject(Constants.url + "/getAllCalTrayReport", map,
					CalCulateTray[].class);
			List<CalCulateTray> calListForFr = new ArrayList<CalCulateTray>(Arrays.asList(calCulateTray));

			model.addObject("calListForFr", calListForFr);
			model.addObject("routeListForFr", routeListForFr);
			model.addObject("frIds", frIds);

			System.out.println("calListForFrcalListForFrcalListForFrcalListForFr" + calListForFr.toString());

			System.out.println("submit1submit1submit1submit1submit1submit1submit1submit1submit1submit1" + submit);

			if (submit.contains("1")) {
				model.addObject("submit1", submit);
			} else if (submit.contains("2")) {

				model.addObject("submit2", 2);
			} else if (submit.contains("3")) {

				model.addObject("submit3", 3);
			} else if (submit.contains("4")) {

				model.addObject("submit4", 4);
			}

			SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
					SubCategory[].class);

			ArrayList<SubCategory> subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));
			System.out.println("subCatAList:" + subCatAList.toString());

			model.addObject("subCatAList", subCatAList);
			map = new LinkedMultiValueMap<>();
			map.add("frIds", frIds);

			FranchiseForDispatch[] frNameId = restTemplate
					.postForObject(Constants.url + "getFranchiseForDispatchByFrIds", map, FranchiseForDispatch[].class);

			List<FranchiseForDispatch> frNameIdByRouteIdList = new ArrayList<>(Arrays.asList(frNameId));

			model.addObject("frNameIdByRouteIdList", frNameIdByRouteIdList);
			model.addObject("date", date);
			model.addObject("routeIds", routeIds);
			model.addObject("menuIds", menuIds);

			map = new LinkedMultiValueMap<>();// change
			map.add("routeIdList", routeIds);

			GetRouteMgmt[] routeMaster1 = restTemplate.postForObject(Constants.url + "/getFranByMultipleRouteTrayIdNew",
					map, GetRouteMgmt[].class);
			List<GetRouteMgmt> routeListForFr1 = new ArrayList<GetRouteMgmt>(Arrays.asList(routeMaster1));
			System.out.println("RouteListRouteListRouteListRouteListRouteList" + routeListForFr1.toString());

			model.addObject("routeListForFr1", routeListForFr1);

		} catch (

		Exception e) {

			e.printStackTrace();
		}

		return model;

	}

}