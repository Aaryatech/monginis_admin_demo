package com.ats.adminpanel.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.adminpanel.commons.AccessControll;
import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.model.AllFrIdNameList;
import com.ats.adminpanel.model.GetRouteMgmt;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.RouteMaster;
import com.ats.adminpanel.model.RouteMgmt;
import com.ats.adminpanel.model.RouteTime;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.logistics.VehicalMaster;

@Controller
public class RouteMgmtCotroller {

	AllFrIdNameList allFrIdNameList = new AllFrIdNameList();

	@RequestMapping(value = "/addRouteMgmt", method = RequestMethod.GET)
	public ModelAndView addRouteMgmt(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("addRouteMgmt/addrouteMgmt");
		System.out.println("route mgmtdisp");

		Constants.mainAct = 1;
		Constants.subAct = 8;
		RestTemplate restTemplate = new RestTemplate();
		try {

			allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			GetRouteMgmt[] allRouteListResponse = restTemplate.getForObject(Constants.url + "/getRouteMgmtList",
					GetRouteMgmt[].class);

			List<GetRouteMgmt> routeList = new ArrayList<GetRouteMgmt>(Arrays.asList(allRouteListResponse));

			model.addObject("routeList", routeList);
			model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

			RouteTime[] routeTimeArray = restTemplate.getForObject(Constants.url + "/getAllRouteMgmtTimeList",
					RouteTime[].class);

			List<RouteTime> timeList = new ArrayList<RouteTime>(Arrays.asList(routeTimeArray));

			model.addObject("timeList", timeList);

			VehicalMaster[] vehicalMasterArray = restTemplate.getForObject(Constants.url + "/getAllVehicalList",
					VehicalMaster[].class);

			List<VehicalMaster> vehicleList = new ArrayList<VehicalMaster>(Arrays.asList(vehicalMasterArray));

			model.addObject("vehicleList", vehicleList);
		} catch (Exception e) {
			System.out.println("Error in route list display" + e.getMessage());
			e.printStackTrace();
		}
		return model;
	}

	@RequestMapping(value = "/addRouteMgmtProcess", method = RequestMethod.POST)
	public String addRouteProcess(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("masters/route");
		RestTemplate rest = new RestTemplate();
		String[] frIds = request.getParameterValues("frIds");
		String routeName = request.getParameter("route_name");
		String acbType = request.getParameter("acbType");
		int seqNo = Integer.parseInt(request.getParameter("seqNo"));
		int isSameDay = Integer.parseInt(request.getParameter("isSameDay"));
		int vehId = Integer.parseInt(request.getParameter("vehId"));
		int timeRouteId = Integer.parseInt(request.getParameter("timeRouteId"));

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < frIds.length; i++) {
			sb = sb.append(frIds[i] + ",");

		}
		String frIdList = sb.toString();
		frIdList = frIdList.substring(0, frIdList.length() - 1);

		RouteMgmt save = new RouteMgmt();
		save.setRouteName(routeName);
		save.setDelStatus(0);
		save.setIsActive(1);
		save.setIsSameDay(isSameDay);
		save.setRouteType(Integer.parseInt(acbType));
		save.setRouteName(routeName);
		save.setSrNo(seqNo);
		save.setFrIds(frIdList);
		save.setExInt1(vehId);
		save.setExInt2(timeRouteId);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("timeRouteId", timeRouteId);
		RestTemplate restTemplate = new RestTemplate();

		RouteTime time = restTemplate.postForObject("" + Constants.url + "getRouteTimeByTimeRouteId", map,
				RouteTime.class);

		map = new LinkedMultiValueMap<String, Object>();
		map.add("vehicalId", vehId);

		VehicalMaster vehicleMaster = restTemplate.postForObject("" + Constants.url + "getVehicalById", map,
				VehicalMaster.class);

		System.out.println("timeRouteId" + timeRouteId);
		System.out.println("vehId" + vehId);
		System.out.println("timetimetimetimetime" + time.toString());
		System.out.println("vehicleMastervehicleMastervehicleMastervehicleMaster" + vehicleMaster.toString());

		save.setExVar1(vehicleMaster.getVehNo());
		save.setExVar2(time.getName());

		RouteMgmt routeResponse = rest.postForObject(Constants.url + "/saveRouteManagement", save, RouteMgmt.class);
		System.out.println(routeResponse.toString());
		model = new ModelAndView("addRouteMgmt/addrouteMgmt");
		return "redirect:/addRouteMgmt";
	}

	@RequestMapping(value = "/deleteRouteMgmt/{routeTrayId}", method = RequestMethod.GET)

	public String deleteRouteMgmt(@PathVariable int routeTrayId) {
		RestTemplate rest = new RestTemplate();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("routeTrayId", routeTrayId);
		Info info = rest.postForObject(Constants.url + "deleteRouteMgmt", map, Info.class);
		if (info.getError()) {
			return "redirect:/addRouteMgmt";
		} else {
			return "redirect:/addRouteMgmt";

		}
	}

	RouteMgmt route = null;

	@RequestMapping(value = "/updateRouteMgmt/{routeTrayId}")

	public ModelAndView updateRouteMgmt(@PathVariable int routeTrayId, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("addRouteMgmt/editrouteMgmt");

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("routeTrayId", routeTrayId);
		RestTemplate restTemplate = new RestTemplate();

		System.out.println("routeTrayIdrouteTrayIdrouteTrayId" + routeTrayId);
		try {
			route = restTemplate.postForObject("" + Constants.url + "getRouteByRouteDetailId", map, RouteMgmt.class,
					routeTrayId);

			System.out.println("route" + route.toString());

			allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			GetRouteMgmt[] allRouteListResponse = restTemplate.getForObject(Constants.url + "/getRouteMgmtList",
					GetRouteMgmt[].class);

			List<GetRouteMgmt> routeList = new ArrayList<GetRouteMgmt>(Arrays.asList(allRouteListResponse));

			model.addObject("routeList", routeList);
			model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());
			model.addObject("route", route);

			RouteTime[] routeTimeArray = restTemplate.getForObject(Constants.url + "/getAllRouteMgmtTimeList",
					RouteTime[].class);

			List<RouteTime> timeList = new ArrayList<RouteTime>(Arrays.asList(routeTimeArray));

			model.addObject("timeList", timeList);

			VehicalMaster[] vehicalMasterArray = restTemplate.getForObject(Constants.url + "/getAllVehicalList",
					VehicalMaster[].class);

			List<VehicalMaster> vehicleList = new ArrayList<VehicalMaster>(Arrays.asList(vehicalMasterArray));

			model.addObject("vehicleList", vehicleList);

			List<Integer> frIdList = Stream.of(route.getFrIds().split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());

			model.addObject("frIdList", frIdList);
		} catch (Exception e) {
			System.out.println("Exception In updateRoute:" + e.getMessage());
		}

		return model;

	}

	@RequestMapping(value = "/updateRouteMgmt/editRouteProcessMgmt", method = RequestMethod.POST)

	public String editRouteProcess(HttpServletRequest request, HttpServletResponse response) {

		RestTemplate rest = new RestTemplate();
		String[] frIds = request.getParameterValues("frIds");
		String routeName = request.getParameter("route_name");
		String acbType = request.getParameter("acbType");
		int seqNo = Integer.parseInt(request.getParameter("seqNo"));
		int isSameDay = Integer.parseInt(request.getParameter("isSameDay"));
		int vehId = Integer.parseInt(request.getParameter("vehId"));
		int timeRouteId = Integer.parseInt(request.getParameter("timeRouteId"));

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < frIds.length; i++) {
			sb = sb.append(frIds[i] + ",");

		}
		String frIdList = sb.toString();
		frIdList = frIdList.substring(0, frIdList.length() - 1);

		route.setRouteName(routeName);
		route.setDelStatus(0);
		route.setIsActive(1);
		route.setIsSameDay(isSameDay);
		route.setRouteType(Integer.parseInt(acbType));
		route.setRouteName(routeName);
		route.setSrNo(seqNo);
		route.setFrIds(frIdList);
		route.setExInt1(vehId);
		route.setExInt2(timeRouteId);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("timeRouteId", timeRouteId);
		RestTemplate restTemplate = new RestTemplate();

		RouteTime time = restTemplate.postForObject("" + Constants.url + "getRouteTimeByTimeRouteId", map,
				RouteTime.class);

		map = new LinkedMultiValueMap<String, Object>();
		map.add("vehicalId", vehId);

		VehicalMaster vehicleMaster = restTemplate.postForObject("" + Constants.url + "getVehicalById", map,
				VehicalMaster.class);

		System.out.println("timeRouteId" + timeRouteId);
		System.out.println("vehId" + vehId);
		System.out.println("timetimetimetimetime" + time.toString());
		System.out.println("vehicleMastervehicleMastervehicleMastervehicleMaster" + vehicleMaster.toString());

		route.setExVar2(time.getName());
		route.setExVar1(vehicleMaster.getVehNo());

		RouteMgmt routeResponse = rest.postForObject(Constants.url + "/saveRouteManagement", route, RouteMgmt.class);
		System.out.println(routeResponse.toString());

		return "redirect:/addRouteMgmt";

	}

}
