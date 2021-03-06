package com.ats.adminpanel.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.adminpanel.commons.AccessControll;
import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.model.AllEventListResponse;
import com.ats.adminpanel.model.CustList;
import com.ats.adminpanel.model.FlavourConf;
import com.ats.adminpanel.model.FlavourList;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.Route;
import com.ats.adminpanel.model.SpCakeResponse;
import com.ats.adminpanel.model.SpecialCake;
import com.ats.adminpanel.model.SubCategoryRes;
import com.ats.adminpanel.model.RawMaterial.RmItemSubCategory;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.events.Event;
import com.ats.adminpanel.model.flavours.AllFlavoursListResponse;
import com.ats.adminpanel.model.flavours.Flavour;
import com.ats.adminpanel.model.item.AllItemsListResponse;
import com.ats.adminpanel.model.item.CategoryListResponse;
import com.ats.adminpanel.model.item.Item;
import com.ats.adminpanel.model.item.SubCategory;
import com.ats.adminpanel.model.masters.AllRatesResponse;
import com.ats.adminpanel.model.masters.AllspMessageResponse;
import com.ats.adminpanel.model.masters.Rate;
import com.ats.adminpanel.model.masters.SpMessage;
import com.ats.adminpanel.model.modules.ErrorMessage;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
@Controller
public class MastersController {

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	@RequestMapping(value = "/addFlavour")
	public String addFlavour(HttpServletRequest request, HttpServletResponse response) {

		String spfName = request.getParameter("spf_name");
		double spfAdonRate = Double.parseDouble(request.getParameter("spf_adon_rate"));
		int spType = Integer.parseInt(request.getParameter("sp_type"));

		ModelAndView mav = new ModelAndView("masters/flavours");

		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("spfName", spfName);
		map.add("spfAdonRate", spfAdonRate);
		map.add("spType", spType);
		ErrorMessage errorResponse = rest.postForObject("" + Constants.url + "insertFlavour", map, ErrorMessage.class);
		System.out.println(errorResponse.toString());

		/*
		 * if(errorResponse.getError()) { mav = new ModelAndView("masters/flavours");
		 * 
		 * }else { mav = new ModelAndView("masters/flavours");
		 * 
		 * }
		 */

		return "redirect:/flavoursList";

	}

	@RequestMapping(value = "/addMessage")
	public String addMessage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("masters/messages");

		String message = request.getParameter("message");

		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("spMsgText", message);

		ErrorMessage errorResponse = rest.postForObject("" + Constants.url + "insertspMessage", map,
				ErrorMessage.class);

		return "redirect:/showSpMessages";
	}

	@RequestMapping(value = "/addRate")
	public String addRate(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Add Rate Request");

		ModelAndView mav = new ModelAndView("masters/rates");

		String sprName = request.getParameter("spr_name");
		double sprRate = Double.parseDouble(request.getParameter("spr_rate"));
		double sprAdOnRate = Double.parseDouble(request.getParameter("spr_adon_rate"));

		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("sprName", sprName);
		map.add("sprRate", sprRate);
		map.add("sprAddOnRate", sprAdOnRate);
		ErrorMessage errorResponse = rest.postForObject(Constants.url + "insertRate", map, ErrorMessage.class);
		System.out.println(errorResponse.toString());

		return "redirect:/showRates";
	}

	@RequestMapping(value = "/showRates")
	public ModelAndView showAllRates(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showRates", "showRates", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			mav = new ModelAndView("accessDenied");

		} else {
			mav = new ModelAndView("masters/rates");

			Constants.mainAct = 1;
			Constants.subAct = 12;

			RestTemplate restTemplate = new RestTemplate();
			AllRatesResponse allRatesListResponse = restTemplate.getForObject(Constants.url + "getAllRates",
					AllRatesResponse.class);

			List<Rate> ratesList = new ArrayList<Rate>();
			ratesList = allRatesListResponse.getRates();
			mav.addObject("ratesList", ratesList);
		}
		return mav;

	}

	// update rate
	@RequestMapping(value = "/updateRate/{sprId}")

	public ModelAndView redirectToUpdateRate(@PathVariable int sprId, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/editrate");

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("sprId", sprId);
		RestTemplate restTemplate = new RestTemplate();

		Rate rate = restTemplate.getForObject(Constants.url + "getRate?sprId={sprId}", Rate.class, sprId);
		System.out.println("spr add on rate=" + rate.getSprAddOnRate());

		model.addObject("rate", rate);

		return model;

	}

	@RequestMapping(value = "/updateRate/updateRateProcess", method = RequestMethod.POST)

	public String redirectToUpdateRateProcess(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/rates");

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		try {
			RestTemplate restTemplate = new RestTemplate();

			String sprName = request.getParameter("spr_name");
			int sprId = Integer.parseInt(request.getParameter("spr_id"));
			int sprRate = Integer.parseInt(request.getParameter("spr_rate"));
			int sprAdOnRate = Integer.parseInt(request.getParameter("spr_adon_rate"));

			map.add("sprId", sprId);
			map.add("sprRate", sprRate);
			map.add("sprAddOnRate", sprAdOnRate);
			map.add("sprName", sprName);

			String rateResponse = restTemplate.postForObject("" + Constants.url + "updateRate", map, String.class);

			AllEventListResponse allEventsListResponse = restTemplate.getForObject(Constants.url + "showEventList",
					AllEventListResponse.class);

		} catch (Exception e) {

		}
		return "redirect:/showRates";

	}

	@RequestMapping(value = "/showSpMessages")
	public ModelAndView showAllMessages(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSpMessages", "showSpMessages", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			mav = new ModelAndView("accessDenied");

		} else {
			mav = new ModelAndView("masters/messages");
			Constants.mainAct = 1;
			Constants.subAct = 9;

			RestTemplate restTemplate = new RestTemplate();
			AllspMessageResponse allspMessageList = restTemplate.getForObject(Constants.url + "getAllSpMessage",
					AllspMessageResponse.class);

			List<SpMessage> spMessageList = new ArrayList<SpMessage>();
			spMessageList = allspMessageList.getSpMessage();
			mav.addObject("spMessageList", spMessageList);
		}
		return mav;

	}

	@RequestMapping(value = "/addAndShowEvents")
	public ModelAndView showAllEvents(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("List Event Request");
		ModelAndView mav = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("addAndShowEvents", "addAndShowEvents", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			mav = new ModelAndView("accessDenied");

		} else {
			mav = new ModelAndView("masters/events");
			Constants.mainAct = 1;
			Constants.subAct = 11;

			RestTemplate restTemplate = new RestTemplate();
			AllEventListResponse allEventsListResponse = restTemplate.getForObject(Constants.url + "showEventList",
					AllEventListResponse.class);

			List<com.ats.adminpanel.model.Event> eventsList = new ArrayList<com.ats.adminpanel.model.Event>();
			eventsList = allEventsListResponse.getEvent();
			mav.addObject("eventsList", eventsList);
		}
		return mav;

	}

	@RequestMapping(value = "/flavoursList")
	public ModelAndView showAllFlavours(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("List Flavour Request");
		ModelAndView mav = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("flavoursList", "flavoursList", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			mav = new ModelAndView("accessDenied");

		} else {
			mav = new ModelAndView("masters/flavours");
			Constants.mainAct = 1;
			Constants.subAct = 10;

			RestTemplate restTemplate = new RestTemplate();
			AllFlavoursListResponse allFlavoursListResponse = restTemplate
					.getForObject(Constants.url + "showFlavourList", AllFlavoursListResponse.class);

			List<Flavour> flavoursList = new ArrayList<Flavour>();
			List<Integer> spTypeArray = new ArrayList<Integer>();
			flavoursList = allFlavoursListResponse.getFlavour();
			System.out.println("LIst of flavours" + flavoursList.toString());
			mav.addObject("flavoursList", flavoursList);
			String spTypeName = "";
			for (int i = 0; i < flavoursList.size(); i++) {
				int sptype = flavoursList.get(i).getSpType();

				switch (sptype) {
				case 2:
					spTypeName = "FC";
					break;
				case 1:
					spTypeName = "Chocolate";
					break;
				default:
					spTypeName = "";
					break;

				}
				spTypeArray.add(sptype);

			}

			mav.addObject("spTypeName", spTypeName);
			mav.addObject("spTypeArray", spTypeArray);
		}
		return mav;

	}

	@RequestMapping(value = "/addEvent", method = RequestMethod.POST)
	public String addEvent(HttpServletRequest request, HttpServletResponse response) {

		String speName = request.getParameter("spe_name");

		ModelAndView mav = new ModelAndView("masters/events");

		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("speName", speName);

		ErrorMessage errorResponse = rest.postForObject(Constants.url + "insertEvent", map, ErrorMessage.class);
		System.out.println(errorResponse.toString());

		/*
		 * if(errorResponse.getError()) { mav = new ModelAndView("masters/flavours");
		 * 
		 * }else { mav = new ModelAndView("masters/flavours");
		 * 
		 * }
		 */

		return "redirect:/addAndShowEvents";

	}

	/*
	 * //from mahesh
	 * 
	 * @RequestMapping(value="/deleteFlavour/{spfId}",method=RequestMethod.GET)
	 * public String deleteFlavours(@PathVariable int spfId) {
	 * 
	 * //String id=request.getParameter("id");
	 * 
	 * ModelAndView mav = new ModelAndView("masters/flavours");
	 * 
	 * RestTemplate rest = new RestTemplate(); MultiValueMap<String, Object> map =
	 * new LinkedMultiValueMap<String, Object>(); map.add("spfId",spfId);
	 * 
	 * ErrorMessage errorResponse =
	 * rest.postForObject("http://localhost:8089/deleteFlavour",
	 * map,ErrorMessage.class); System.out.println(errorResponse.toString());
	 * 
	 * if(errorResponse.getError()) { return "redirect:/flavoursList";
	 * 
	 * }else { return "redirect:/flavoursList";
	 * 
	 * }
	 * 
	 * }
	 */

// event update
	@RequestMapping(value = "/updateEvent/{speId}")

	public ModelAndView redirectToUpdateEvent(@PathVariable int speId, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/editEvent");

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("speId", speId);
		RestTemplate restTemplate = new RestTemplate();

		Event event = restTemplate.getForObject(Constants.url + "getEvent?speId={speId}", Event.class, speId);

		model.addObject("event", event);

		return model;

	}

	@RequestMapping(value = "/updateEvent/updateEventProcess", method = RequestMethod.POST)

	public ModelAndView fjdfhfj(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/events");

		try {
			RestTemplate restTemplate = new RestTemplate();

			String speName = request.getParameter("event_name");
			int speId = Integer.parseInt(request.getParameter("spe_id"));

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("id", speId);
			map.add("speName", speName);

			String eventResponse = restTemplate.postForObject("" + Constants.url + "updateEvent", map, String.class);

			AllEventListResponse allEventsListResponse = restTemplate.getForObject(Constants.url + "showEventList",
					AllEventListResponse.class);

			List<com.ats.adminpanel.model.Event> eventsList = new ArrayList<com.ats.adminpanel.model.Event>();
			eventsList = allEventsListResponse.getEvent();
			model.addObject("eventsList", eventsList);
		} catch (Exception e) {

		}
		return model;

	}

// end of event update///deleteFlavour/{spfId}==changed on 5 aug
	@RequestMapping(value = "/updateFlavourStatus/{spfId}/{status}", method = RequestMethod.GET)
	public String deleteFlavour(@PathVariable String[] spfId,@PathVariable int status) {

		// String id=request.getParameter("id");

		ModelAndView mav = new ModelAndView("masters/flavours");
		String strSpfIdIds = new String();
		for (int i = 0; i < spfId.length; i++) {
			strSpfIdIds = strSpfIdIds + "," + spfId[i];
		}
		strSpfIdIds = strSpfIdIds.substring(1);
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("spfId", strSpfIdIds);
		map.add("status", status);
		ErrorMessage errorResponse = restTemplate.postForObject(Constants.url + "updateFlavourStatus", map,
				ErrorMessage.class);
		System.out.println(errorResponse.toString());

		if (errorResponse.getError()) {
			return "redirect:/flavoursList";

		} else {
			return "redirect:/flavoursList";

		}

	}
//flavour update 

	@RequestMapping(value = "/updateFlavour/{spfId}", method = RequestMethod.GET)

	public ModelAndView redirectToUpdateFlavour(@PathVariable int spfId, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/editflavours");
		try {

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			RestTemplate restTemplate = new RestTemplate();

			Flavour flavour = restTemplate.getForObject(Constants.url + "getFlavour?spfId={spfId}", Flavour.class,
					spfId);
			System.out.println("flav are" + flavour.getSpfName());
			model.addObject("flavour", flavour);

			int spType = flavour.getSpType();
			System.out.println("sp type before edit ==" + spType);
			String strSpType = String.valueOf(spType);
			model.addObject("strSpType", strSpType);
			// model.addObject("spType", spType);
			String strSpTypeName = "";
			switch (spType) {
			case 0:
				strSpTypeName = "FC";
				break;
			case 1:
				strSpTypeName = "Chocolate";
				break;
			default:
				strSpTypeName = "";
				break;

			}

			model.addObject("strSpTypeName", strSpTypeName);

		} catch (Exception e) {
			System.out.println("error in getting flavour" + e.getMessage());
			e.printStackTrace();
		}

		return model;

	}

	@RequestMapping(value = "/updateFlavour/updateFlavourProcess", method = RequestMethod.POST)

	public String updateFlavour(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/flavours");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		int speId = Integer.parseInt(request.getParameter("spe_id"));
		int spType = Integer.parseInt(request.getParameter("sp_type"));
		String spfName = request.getParameter("spf_name");
		double spfAdOnRate = Double.parseDouble(request.getParameter("spf_adon_rate"));
		System.out.println("sp type after edit==" + spType);

		RestTemplate restTemplate = new RestTemplate();
		map.add("id", speId);
		map.add("spType", spType);
		map.add("spfName", spfName);
		map.add("spfAdOnRate", spfAdOnRate);

		String flavourResponse = restTemplate.postForObject("" + Constants.url + "updateFlavour", map, String.class);
		/*
		 * AllFlavoursListResponse allFlavoursListResponse=restTemplate.getForObject(
		 * Constants.url+"/showFlavourList", AllFlavoursListResponse.class);
		 * model.addObject(allFlavoursListResponse);
		 */

		/*
		 * List<com.ats.adminpanel.model.Event> eventsList= new
		 * ArrayList<com.ats.adminpanel.model.Event>();
		 * eventsList=allEventsListResponse.getEvent();
		 * model.addObject("eventsList",eventsList);
		 */
		return "redirect:/flavoursList";
		// return model;

	}

	@RequestMapping(value = "/deleteEvent/{speId}", method = RequestMethod.GET)
	public String deleteEvent(@PathVariable int speId) {

		// String id=request.getParameter("id");

		ModelAndView mav = new ModelAndView("masters/events");

		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", speId);

		ErrorMessage errorResponse = rest.postForObject(Constants.url + "deleteEvent", map, ErrorMessage.class);
		System.out.println(errorResponse.toString());

		if (errorResponse.getError()) {
			return "redirect:/addAndShowEvents";

		} else {
			return "redirect:/addAndShowEvents";

		}

	}

	// from mahesh
	@RequestMapping(value = "/deleteRate/{sprId}", method = RequestMethod.GET)
	public String deleteRates(@PathVariable int sprId) {

		// String id=request.getParameter("id");

		ModelAndView mav = new ModelAndView("masters/rates");

		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("sprId", sprId);

		ErrorMessage errorResponse = rest.postForObject(Constants.url + "deleteRates", map, ErrorMessage.class);
		System.out.println(errorResponse.toString());

		if (errorResponse.getError()) {
			return "redirect:/showRates";

		} else {
			return "redirect:/showRates";

		}

	}

// from mahesh
	@RequestMapping(value = "/deleteSpMessage/{spMsgId}", method = RequestMethod.GET)
	public String deleteSpMessage(@PathVariable String[] spMsgId) {

		// String id=request.getParameter("id");

		ModelAndView mav = new ModelAndView("masters/messages");

		String strMsgIds = new String();
		for (int i = 0; i < spMsgId.length; i++) {
			strMsgIds = strMsgIds + "," + spMsgId[i];
		}
		strMsgIds = strMsgIds.substring(1);

		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("spMsgId", strMsgIds);

		ErrorMessage errorResponse = rest.postForObject(Constants.url + "deleteSpMessage", map, ErrorMessage.class);
		System.out.println(errorResponse.toString());

		if (errorResponse.getError()) {
			return "redirect:/showSpMessages";

		} else {
			return "redirect:/showSpMessages";

		}

	}

	@RequestMapping(value = "/updateSpMessage/{spMsgId}", method = RequestMethod.GET)

	public ModelAndView redirectToUpdateSpMessage(@PathVariable("spMsgId") int msgId, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/editmessage");

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		// msgId=Integer.parseInt(request.getParameter("spMsgId"));
		map.add("msgId", msgId);
		// String message=request.getParameter("message");
		// map.add("spMsgText", message);
		RestTemplate restTemplate = new RestTemplate();
		/*
		 * Route route=restTemplate .postForObject(""+Constants.url+"/getRoute",
		 * map,Route.class);
		 */
		SpMessage spMessage = restTemplate.getForObject("" + Constants.url + "getSpMessage?msgId={msgId}",
				SpMessage.class, msgId);

		model.addObject("spmessage", spMessage);

		return model;

	}

	@RequestMapping(value = "/updateSpMessage/updateSpMessageProcess", method = RequestMethod.POST)

	public String redirectToUpdateRouteProcess(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/messages");

		RestTemplate restTemplate = new RestTemplate();

		String spMsgText = request.getParameter("message");
		int spMsgId = Integer.parseInt(request.getParameter("spMsgId"));

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		map.add("spMsgId", spMsgId);
		map.add("spMsgText", spMsgText);

		String routeResponse = restTemplate.postForObject("" + Constants.url + "updateSpMessage", map, String.class);

		return "redirect:/showSpMessages";

	}

	@RequestMapping(value = "/showSubCatList")
	public ModelAndView showSubCatList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSubCatList", "showSubCatList", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			mav = new ModelAndView("accessDenied");

		} else {

			mav = new ModelAndView("masters/subcategory");
			/*
			 * Constants.mainAct=1; Constants.subAct=9;
			 */
			try {

				RestTemplate restTemplate = new RestTemplate();
				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<SubCategory> subCatList = new ArrayList<SubCategory>();

				for (int i = 0; i < categoryListResponse.getmCategoryList().size(); i++) {
					subCatList.addAll(categoryListResponse.getmCategoryList().get(i).getSubCategoryList());

				}
				mav.addObject("catList", categoryListResponse.getmCategoryList());
				mav.addObject("subCatList", subCatList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mav;

	}

	@RequestMapping(value = "/updateSubCategory/{subCatId}", method = RequestMethod.GET)
	public ModelAndView updateSubCategory(@PathVariable("subCatId") int subCatId, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("masters/subcategory");
		try {
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("subCatId", subCatId);
			RestTemplate restTemplate = new RestTemplate();

			SubCategoryRes subCategory = restTemplate.postForObject(Constants.url + "getSubCategory", map,
					SubCategoryRes.class);

			model.addObject("subCategory", subCategory);
			CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
					CategoryListResponse.class);
			List<SubCategory> subCatList = new ArrayList<SubCategory>();

			for (int i = 0; i < categoryListResponse.getmCategoryList().size(); i++) {
				subCatList.addAll(categoryListResponse.getmCategoryList().get(i).getSubCategoryList());

			}
			model.addObject("catList", categoryListResponse.getmCategoryList());
			model.addObject("subCatList", subCatList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return model;
	}

	@RequestMapping(value = "/deleteSubCategory/{subCatId}", method = RequestMethod.GET)
	public String deleteSubCategory(@PathVariable int subCatId) {
		try {
			RestTemplate rest = new RestTemplate();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("subCatId", subCatId);

			ErrorMessage errorResponse = rest.postForObject(Constants.url + "deleteSubCategory", map,
					ErrorMessage.class);
			System.err.println("errorResponse" + errorResponse.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/showSubCatList";
	}

	@RequestMapping(value = "/addSubCategoryProcess", method = RequestMethod.POST)
	public String addSubCategoryProcess(HttpServletRequest request, HttpServletResponse response) {

		try {

			String subCatId = request.getParameter("subCatId");

			String subCatName = request.getParameter("sub_cat_name");

			int catId = Integer.parseInt(request.getParameter("cat_id"));

			SubCategoryRes subCategory = new SubCategoryRes();
			if (subCatId == null || subCatId == "") {
				subCategory.setSubCatId(0);
			} else {
				subCategory.setSubCatId(Integer.parseInt(subCatId));
			}

			subCategory.setCatId(catId);
			subCategory.setSubCatName(subCatName);
			subCategory.setDelStatus(0);

			RestTemplate restTemplate = new RestTemplate();

			ErrorMessage errorMessage = restTemplate.postForObject(Constants.url + "/saveSubCategory", subCategory,
					ErrorMessage.class);
			System.out.println("Response: " + errorMessage.toString());
		} catch (Exception e) {
			System.out.println("Exception In Add  SubCategory Process:" + e.getMessage());

			return "redirect:/showSubCatList";

		}

		return "redirect:/showSubCatList";
	}

	@RequestMapping(value = "/showCutomerList")
	public ModelAndView showCutomerList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		/*
		 * Info view = AccessControll.checkAccess("showCutomerList", "showCutomerList",
		 * "1", "0", "0", "0", newModuleList);
		 * 
		 * if (view.getError() == true) {
		 * 
		 * mav = new ModelAndView("masters/custList");
		 * 
		 * } else {
		 */

		mav = new ModelAndView("masters/custList");
		/*
		 * Constants.mainAct=1; Constants.subAct=9;
		 */
		try {

			RestTemplate restTemplate = new RestTemplate();
			CustList[] custList1 = restTemplate.getForObject(Constants.url + "getCutslList", CustList[].class);
			List<CustList> custList = new ArrayList<CustList>(Arrays.asList(custList1));

			System.out.println("custListcustListcustListcustList" + custList.toString());

			mav.addObject("custList", custList);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return mav;

	}
	List<Flavour> flavoursList=new ArrayList<Flavour>();
	@RequestMapping(value = "/showFlavorConfiguration", method = RequestMethod.GET)
	public ModelAndView showFlavorConfiguration(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView("masters/flavourConf");
		
		try {
			HttpSession session = request.getSession();

			/*List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
			Info view = AccessControll.checkAccess("showFlavorConfiguration", "showFlavorConfiguration", "1", "0", "0", "0", newModuleList);

			if (view.getError() == true) {

				model = new ModelAndView("accessDenied");

			} else {*/
				RestTemplate restTemplate = new RestTemplate();

				SpCakeResponse spCakeResponse = restTemplate
						.getForObject(Constants.url + "showSpecialCakeListOrderBySpCode", SpCakeResponse.class);
				System.out.println("SpCake Controller SpCakeList Response " + spCakeResponse.toString());
				List<com.ats.adminpanel.model.SpecialCake> specialCakeList = new ArrayList<com.ats.adminpanel.model.SpecialCake>();

				specialCakeList = spCakeResponse.getSpecialCake();

				model.addObject("specialCakeList", specialCakeList);
				String strSp="";
				for(int i=0;i<specialCakeList.size();i++)
				{
					strSp=specialCakeList.get(i).getSpId()+","+strSp;
				}
				strSp = strSp.substring(1, strSp.length() - 1);
				model.addObject("strSp", strSp);
                System.out.println("strSp"+strSp);

				//--------------------------New For Flavors----------------------------------------

				FlavourList flavourList = restTemplate.getForObject(Constants.url + "/showFlavourList", FlavourList.class);
				 flavoursList = flavourList.getFlavour();
				model.addObject("flavoursList", flavoursList);
				String strFlavours="";
				for(int i=0;i<flavoursList.size();i++)
				{
					strFlavours=flavoursList.get(i).getSpfId()+","+strFlavours;
				}
			//	strFlavours = strFlavours.substring(1, strFlavours.length() - 1);
				model.addObject("strFlavours", strFlavours);
                System.out.println("strFlavours"+strFlavours);
				//------------------------------------------------------------------
				
			/*}*/
		}
        catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	@RequestMapping(value = "/showAllSpSelected", method = RequestMethod.GET)
	public @ResponseBody List<SpecialCake> showAllSpSelected(HttpServletRequest request, HttpServletResponse response) {
		RestTemplate restTemplate = new RestTemplate();

		SpCakeResponse spCakeResponse = restTemplate
				.getForObject(Constants.url + "showSpecialCakeListOrderBySpCode", SpCakeResponse.class);
		System.out.println("SpCake Controller SpCakeList Response " + spCakeResponse.toString());
		List<com.ats.adminpanel.model.SpecialCake> specialCakeList = new ArrayList<com.ats.adminpanel.model.SpecialCake>();

		specialCakeList = spCakeResponse.getSpecialCake();
		return specialCakeList;
	}

	
	@RequestMapping(value = "/findFlConf", method = RequestMethod.GET)
	public @ResponseBody List<Flavour> findFlConf(HttpServletRequest request, HttpServletResponse response) {
		List<Flavour> flavorListRes=new ArrayList<Flavour>();
		System.err.println("findFlConf");
		try {
			String spFlavour=request.getParameter("spFlavour");
			spFlavour = spFlavour.substring(1, spFlavour.length() - 1);
			spFlavour = spFlavour.replaceAll("\"", "");
			List<Integer> spFlavourList = Stream.of(spFlavour.split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());
			RestTemplate restTemplate = new RestTemplate();
			FlavourList flavourList = restTemplate.getForObject(Constants.url + "/showFlavourList", FlavourList.class);
			 flavoursList = flavourList.getFlavour();
			 System.err.println("spFlavourList"+spFlavourList.toString());
			 System.err.println("flavoursList@@"+flavoursList.toString());
			for(Flavour flavour:flavoursList)
			{
				for(int i=0;i<spFlavourList.size();i++)
				{
					if(flavour.getSpfId()==spFlavourList.get(i)) {
			        	flavorListRes.add(flavour);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flavorListRes;
	}
	@RequestMapping(value = "/saveFlavourConf", method = RequestMethod.POST)
	public String saveFlavourConf(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			RestTemplate restTemplate = new RestTemplate();

			String[] spIds=request.getParameterValues("sp");
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < spIds.length; i++) {

				sb = sb.append(spIds[i] + ",");

			}
			String spId = sb.toString();

			spId = spId.substring(0, spId.length() - 1);
			List<Integer> spIdsList = Stream.of(spId.split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());
			
			String[] spFlavours=request.getParameterValues("fl");
			StringBuilder sb1 = new StringBuilder();

			for (int i = 0; i < spFlavours.length; i++) {

				sb1 = sb1.append(spFlavours[i] + ",");

			}
			String spfId = sb1.toString();
			spfId = spfId.substring(0, spfId.length() - 1);
            System.out.println(spfId+"spfId");
			List<Integer> spFlavourList = Stream.of(spfId.split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());
			FlavourList flavourList = restTemplate.getForObject(Constants.url + "/showFlavourList", FlavourList.class);
			 flavoursList = flavourList.getFlavour();
			 List<Flavour> flavorListRes=new ArrayList<>();
				for(Flavour flavour:flavoursList)
				{
					for(int i=0;i<spFlavourList.size();i++)
					{
						if(flavour.getSpfId()==spFlavourList.get(i)) {
				        	flavorListRes.add(flavour);
						}
					}
				}
			List<FlavourConf> flavourConfList=new ArrayList<FlavourConf>();
			for(int i=0;i<spIdsList.size();i++)	{
			for(int j=0;j<flavorListRes.size();j++)
			{
				System.err.println("spFlavourList.get(j)"+flavorListRes.get(j));
				float rate=Float.parseFloat(request.getParameter("rate"+flavorListRes.get(j).getSpfId()));
				float mrp=Float.parseFloat(request.getParameter("mrp"+flavorListRes.get(j).getSpfId()));
                if(rate!=0 && mrp!=0) {
				 FlavourConf flConf=new FlavourConf();
				 flConf.setFlavId(0);
				 flConf.setSpId(spIdsList.get(i));
				 flConf.setSpfId(flavorListRes.get(j).getSpfId());
				 flConf.setRate(Math.round(rate));
				 flConf.setMrp(Math.round(mrp));
				 flConf.setSpType(flavorListRes.get(j).getSpType());
				 flConf.setExVar1("-");
				 flConf.setExInt1(0);
				 flConf.setDelStatus(0);
				 flavourConfList.add(flConf);
                }
			}
			}

			List<FlavourConf> resp = restTemplate.postForObject(Constants.url + "/saveFlavourConf", flavourConfList,
					List.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/showFlavorConfiguration";
	}
	@RequestMapping(value = "/flConfList", method = RequestMethod.GET)
	public ModelAndView flConfList(HttpServletRequest request, HttpServletResponse response) {
	
		ModelAndView mav =new ModelAndView("masters/flConfList");
	
			RestTemplate restTemplate = new RestTemplate();
			AllFlavoursListResponse allFlavoursListResponse = restTemplate
					.getForObject(Constants.url + "showFlavourList", AllFlavoursListResponse.class);

			List<Flavour> flavoursList = allFlavoursListResponse.getFlavour();
			SpCakeResponse spCakeResponse = restTemplate
					.getForObject(Constants.url + "showSpecialCakeListOrderBySpCode", SpCakeResponse.class);
			System.out.println("SpCake Controller SpCakeList Response " + spCakeResponse.toString());
			List<com.ats.adminpanel.model.SpecialCake> specialCakeList = new ArrayList<com.ats.adminpanel.model.SpecialCake>();

			specialCakeList = spCakeResponse.getSpecialCake();
			
			List<FlavourConf>  flList = restTemplate.getForObject(Constants.url + "getAllFlConf", List.class);
			mav.addObject("flList", flList);
			mav.addObject("specialCakeList", specialCakeList);
			mav.addObject("flavoursList", flavoursList);
		
		return mav;

	}
	
	@RequestMapping(value = "/updateFlavourConf", method = RequestMethod.GET)
	public @ResponseBody Info updateFlavourConf(HttpServletRequest request, HttpServletResponse response) {
		Info info=new Info();
		try {
			int flavId=Integer.parseInt(request.getParameter("flavId"));
		    float rate=Float.parseFloat(request.getParameter("rate"));
		    float mrp=Float.parseFloat(request.getParameter("mrp"));
		    
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("flavId", flavId);
			map.add("rate", rate);
			map.add("mrp", mrp);
			RestTemplate restTemplate = new RestTemplate();
			 info = restTemplate.postForObject(Constants.url + "/updateFlavourConf",map, Info.class);
			} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	@RequestMapping(value = "/deleteFlavourConf/{flavId}", method = RequestMethod.GET)

	public String deleteFlavourConf(@PathVariable("flavId") int flavId) {

		try {
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("flavId", flavId);
			Info info = restTemplate.postForObject(Constants.url + "/deleteFlavourConf", map, Info.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "redirect:/flConfList";
	}
}
