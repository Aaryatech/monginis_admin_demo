package com.ats.adminpanel.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.ats.adminpanel.model.AllRoutesListResponse;
import com.ats.adminpanel.model.SpecialCake;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.Route;
import com.ats.adminpanel.model.RouteMaster;
import com.ats.adminpanel.model.SpCakeResponse;

@Controller
public class RouteController {

	@RequestMapping(value = "/addRouteProcess", method = RequestMethod.POST)
	public String addRouteProcess(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("masters/route");
		RestTemplate rest = new RestTemplate();

		String routeName = request.getParameter("route_name");
		int acbType = Integer.parseInt(request.getParameter("acbType"));
		int seqNo = Integer.parseInt(request.getParameter("seqNo"));

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("routeName", routeName);
		map.add("acbType", acbType);
		map.add("seqNo", seqNo);

		RouteMaster save = new RouteMaster();
		save.setRouteName(routeName);
		save.setAbcType(acbType);
		save.setSeqNo(seqNo);

		RouteMaster routeResponse = rest.postForObject(Constants.url + "/saveRoute", save, RouteMaster.class);
		model = new ModelAndView("masters/route");
		return "redirect:/addroute";
	}

	@RequestMapping(value = "/addroute", method = RequestMethod.GET)
	public ModelAndView addroute(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showAddNewFranchisee", "showAddNewFranchisee", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("masters/route");
			System.out.println("route disp");

			Constants.mainAct = 1;
			Constants.subAct = 8;
			RestTemplate restTemplate = new RestTemplate();
			try {

				RouteMaster[] allRouteListResponse = restTemplate.getForObject(Constants.url + "/showRouteListNew",
						RouteMaster[].class);

				List<RouteMaster> routeList = new ArrayList<RouteMaster>(Arrays.asList(allRouteListResponse));

				model.addObject("routeList", routeList);
			} catch (Exception e) {
				System.out.println("Error in route list display" + e.getMessage());
				e.printStackTrace();
			}
		}
		return model;
	}

	@RequestMapping(value = "/deleteRoute/{routeId}", method = RequestMethod.GET)

	public String deleteRoute(@PathVariable String[] routeId) {
		ModelAndView model = new ModelAndView("masters/route");
		RestTemplate rest = new RestTemplate();
		String strRouteIds = new String();
		for (int i = 0; i < routeId.length; i++) {
			strRouteIds = strRouteIds + "," + routeId[i];
		}
		strRouteIds = strRouteIds.substring(1);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("routeId", strRouteIds);
		Info info = rest.postForObject(Constants.url + "deleteRoute", map, Info.class);
		if (info.getError()) {
			return "redirect:/addroute";
		} else {
			return "redirect:/addroute";

		}
	}

	@RequestMapping(value = "/updateRoute/{routeId}")

	public ModelAndView updateRoute(@PathVariable int routeId, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/editRoute");

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("routeId", routeId);
		RestTemplate restTemplate = new RestTemplate();
		try {
			RouteMaster route = restTemplate.getForObject("" + Constants.url + "getRouteNew?routeId={routeId}",
					RouteMaster.class, routeId);

			RouteMaster[] allRouteListResponse = restTemplate.getForObject(Constants.url + "/showRouteListNew",
					RouteMaster[].class);

			List<RouteMaster> routeList = new ArrayList<RouteMaster>(Arrays.asList(allRouteListResponse));

			model.addObject("routeList", routeList);
			model.addObject("route", route);
		} catch (Exception e) {
			System.out.println("Exception In updateRoute:" + e.getMessage());
		}

		return model;

	}

	@RequestMapping(value = "/updateRoute/editRouteProcess", method = RequestMethod.POST)

	public String editRouteProcess(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/route");

		RestTemplate restTemplate = new RestTemplate();

		String routeName = request.getParameter("route_name");
		int routeId = Integer.parseInt(request.getParameter("route_id"));
		int acbType = Integer.parseInt(request.getParameter("acbType"));
		int seqNo = Integer.parseInt(request.getParameter("seqNo"));

		RouteMaster save = new RouteMaster();
		save.setRouteName(routeName);
		save.setAbcType(acbType);
		save.setSeqNo(seqNo);
		save.setRouteId(routeId);

		RouteMaster routeResponse = restTemplate.postForObject("" + Constants.url + "saveRoute", save,
				RouteMaster.class);

		return "redirect:/addroute";

	}

}
