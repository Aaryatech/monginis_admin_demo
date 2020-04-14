package com.ats.adminpanel.controller;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.ats.adminpanel.commons.AccessControll;
import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.model.AllFrIdName;
import com.ats.adminpanel.model.AllFrIdNameList;
import com.ats.adminpanel.model.FrMenu;

import com.ats.adminpanel.model.GetFrMenus;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.item.CategoryListResponse;
import com.ats.adminpanel.model.item.Item;
import com.ats.adminpanel.model.item.MCategoryList;
import com.ats.adminpanel.model.stock.PostFrItemStockCommon;
import com.ats.adminpanel.model.stock.PostFrItemStockDetail;
import com.ats.adminpanel.model.stock.PostFrItemStockHeader;
import com.sun.org.apache.bcel.internal.generic.FMUL;

@Controller
@Scope("session")
public class StockController {

	private static final Logger logger = LoggerFactory.getLogger(StockController.class);
	List<FrMenu> filterFrMenus = new ArrayList<FrMenu>();
	List<Item> itemList;
	String frId;
	String menuId = "0";
	List<PostFrItemStockDetail> detailList = new ArrayList<PostFrItemStockDetail>();

	@RequestMapping(value = "/showFrOpeningStock")
	public ModelAndView showFrOpeningStock(HttpServletRequest request, HttpServletResponse response) {

		logger.info("/showFrOpeningStock request mapping.");

		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showFrOpeningStock", "showFrOpeningStock", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("stock/fropeningstock");
			Constants.mainAct = 2;
			Constants.subAct = 18;

			RestTemplate restTemplate = new RestTemplate();

			AllFrIdNameList allFrIdNameList = new AllFrIdNameList();
			try {

				allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName", AllFrIdNameList.class);

			} catch (Exception e) {
				System.out.println("Exception in getAllFrIdName" + e.getMessage());
				e.printStackTrace();

			}
			// ---------------------------------4-jan-2019------------------------------------
			CategoryListResponse itemsWithCategoryResponseList = restTemplate
					.getForObject(Constants.url + "showAllCategory", CategoryListResponse.class);

			List<MCategoryList> itemsWithCategoriesList = itemsWithCategoryResponseList.getmCategoryList();

			for (int i = 0; i < itemsWithCategoriesList.size(); i++) {

				// System.out.println("cat id== " + itemsWithCategoriesList.get(i).getCatId());
				if (itemsWithCategoriesList.get(i).getCatId() == 5) {

					itemsWithCategoriesList.remove(i);

				}

			}

			for (int i = 0; i < itemsWithCategoriesList.size(); i++) {

				if (itemsWithCategoriesList.get(i).getCatId() == 6) {

					itemsWithCategoriesList.remove(i);

				}

			}

			model.addObject("catList", itemsWithCategoriesList);
			// ---------------------------------4-jan-2019------------------------------------
			model.addObject("frList", allFrIdNameList.getFrIdNamesList());
		}
		return model;
	}

	// AJAX Call for menu
	/*
	 * @RequestMapping(value = "/getMenuListByFr", method = RequestMethod.GET)
	 * public @ResponseBody List<FrMenu> getMenuListByFr(HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * logger.info("/getMenuListByFr AJAX Call mapping."); try { frId =
	 * request.getParameter("fr_id");
	 * 
	 * RestTemplate restTemplate = new RestTemplate();
	 * 
	 * MultiValueMap<String, Object> menuMap = new LinkedMultiValueMap<String,
	 * Object>(); menuMap.add("frId", frId);
	 * 
	 * GetFrMenus getFrMenus = restTemplate.postForObject(Constants.url +
	 * "getFrConfigMenus", menuMap, GetFrMenus.class);
	 * 
	 * filterFrMenus = new ArrayList<FrMenu>();
	 * 
	 * for (int i = 0; i < getFrMenus.getFrMenus().size(); i++) {
	 * 
	 * FrMenu frMenu = getFrMenus.getFrMenus().get(i);
	 * 
	 * if (frMenu.getMenuId() == 26 || frMenu.getMenuId() == 31 ||
	 * frMenu.getMenuId() == 33 || frMenu.getMenuId() == 34 || frMenu.getMenuId() ==
	 * 49) {
	 * 
	 * filterFrMenus.add(frMenu);
	 * 
	 * }
	 * 
	 * } System.err.println("Menus " +filterFrMenus); }catch (Exception e) {
	 * System.err.println("Exc in "); System.err.println("dvld");
	 * e.printStackTrace(); } return filterFrMenus; }
	 */
	// AJAX Call for Items
	@RequestMapping(value = "/getItemListById", method = RequestMethod.GET)
	public @ResponseBody List<PostFrItemStockDetail> getItems(HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("/getItemListById AJAX Call mapping.");

		/*
		 * menuId = request.getParameter("menu_id");//catId from jsp
		 */
		int catId = Integer.parseInt(request.getParameter("menu_id")); // catId from jsp
		System.out.println("req param menuId " + catId);
		int frId = Integer.parseInt(request.getParameter("frId")); // catId from jsp'
		System.out.println("req param frId " + frId);
		RestTemplate restTemplate = new RestTemplate();

		/*
		 * String itemShow = null; int catId = 0;
		 * 
		 * for (int i = 0; i < filterFrMenus.size(); i++) {
		 * 
		 * if (filterFrMenus.get(i).getMenuId() == Integer.parseInt(menuId)) {
		 * 
		 * catId = filterFrMenus.get(i).getCatId(); itemShow =
		 * filterFrMenus.get(i).getItemShow();
		 * 
		 * System.out.println("Item Show List is: " + itemShow);
		 * 
		 * break; }
		 * 
		 * }
		 */

		MultiValueMap<String, Object> menuMap = new LinkedMultiValueMap<String, Object>();
		/* menuMap.add("itemIdList", itemShow); */
		menuMap.add("frId", frId);
		menuMap.add("catId", catId);

		ParameterizedTypeReference<List<PostFrItemStockDetail>> typeRef = new ParameterizedTypeReference<List<PostFrItemStockDetail>>() {
		};
		ResponseEntity<List<PostFrItemStockDetail>> responseEntity = restTemplate
				.exchange(Constants.url + "getCurrentOpStock", HttpMethod.POST, new HttpEntity<>(menuMap), typeRef);
		detailList = responseEntity.getBody();

		System.out.println("Item List " + detailList.toString());

		return detailList;
	}

	// Save item opening stock

	@RequestMapping(value = "/saveFrOpeningStockProcess", method = RequestMethod.POST)
	public String saveOpeningStock(HttpServletRequest request, HttpServletResponse response) {

		logger.info("/showFrOpeningStock request mapping.");

		ModelAndView model = new ModelAndView("stock/fropeningstock");

		for (int i = 0; i < detailList.size(); i++) {

			String stockQty = request.getParameter("stockQty" + detailList.get(i).getItemId());
			System.out.println("new qty " + stockQty);

			detailList.get(i).setRegOpeningStock(Integer.parseInt(stockQty));

		}

		RestTemplate restTemplate = new RestTemplate();

		List<PostFrItemStockDetail> info = restTemplate.postForObject(Constants.url + "postFrOpStockDetailList",
				detailList, List.class);

		return "redirect:/showFrOpeningStock";
	}

}
