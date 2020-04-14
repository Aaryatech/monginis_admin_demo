package com.ats.adminpanel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.model.franchisee.FrNameIdByRouteIdResponse;

@Controller
public class NotifController {

	
	
	@RequestMapping(value = "/sendPushNotif", method = RequestMethod.GET)
	public @ResponseBody Object sendPushNotif(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView("login");
		
		String fromDate = request.getParameter("from_date");
		String toDate = request.getParameter("to_date");
		
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		RestTemplate restTemplate = new RestTemplate();
		
		map.add("fromDate", fromDate);
		map.add("toDate", toDate);
		map.add("compId", 4);

		String frNameId = restTemplate.postForObject(Constants.url + "sendPushNotifApi",
				map, String.class);
		
		return model;

	}
	
}
