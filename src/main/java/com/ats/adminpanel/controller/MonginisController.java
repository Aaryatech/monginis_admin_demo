package com.ats.adminpanel.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.adminpanel.commons.AccessControll;
import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.Orders;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.franchisee.FranchiseeAndMenuList;

public class MonginisController {

	FranchiseeAndMenuList franchiseeAndMenuList;
	List<Orders> orderList = new ArrayList<Orders>();
	int billNo = 0;
	
	@RequestMapping(value = "/showManualOrder", method = RequestMethod.GET)
	public ModelAndView showManualOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showManualOrder", "showManualOrder", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("orders/manualOrder");
			try {
				RestTemplate restTemplate = new RestTemplate();
				franchiseeAndMenuList = restTemplate.getForObject(Constants.url + "getFranchiseeAndMenu",
						FranchiseeAndMenuList.class);
				orderList = new ArrayList<Orders>();
				System.out.println("Franchisee Response " + franchiseeAndMenuList.getAllFranchisee());

				model.addObject("allFranchiseeAndMenuList", franchiseeAndMenuList);
				model.addObject("billNo", billNo);
				billNo = 0;
			} catch (Exception e) {
				System.out.println("Franchisee Controller Exception " + e.getMessage());
			}
		}
		return model;

	}
}
