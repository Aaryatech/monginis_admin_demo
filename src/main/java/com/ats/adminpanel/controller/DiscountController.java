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

import com.ats.adminpanel.commons.AccessControll;
import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.model.AllRoutesListResponse;
import com.ats.adminpanel.model.ConfigureFrBean;
import com.ats.adminpanel.model.ConfigureFrListResponse;
import com.ats.adminpanel.model.Discount;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.Route;
import com.ats.adminpanel.model.SpCakeResponse;
import com.ats.adminpanel.model.SpecialCake;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.franchisee.AllFranchiseeAndMenu;
import com.ats.adminpanel.model.franchisee.CommonConf;
import com.ats.adminpanel.model.franchisee.FranchiseeAndMenuList;
import com.ats.adminpanel.model.franchisee.FranchiseeList;
import com.ats.adminpanel.model.franchisee.Menu;
import com.ats.adminpanel.model.item.CategoryListResponse;
import com.ats.adminpanel.model.item.FrItemStockConfiResponse;
import com.ats.adminpanel.model.item.FrItemStockConfigure;
import com.ats.adminpanel.model.item.Item;
import com.ats.adminpanel.model.item.MCategoryList;

@Controller
@Scope("session")
public class DiscountController {

	public List<MCategoryList> mCategoryList = null;
	ArrayList<Item> itemsList = null;
	public CategoryListResponse categoryListResponse;

	FranchiseeAndMenuList franchiseeAndMenuList;

	private static final Logger logger = LoggerFactory.getLogger(FranchiseeController.class);

	@RequestMapping(value = "/addDiscount")
	public ModelAndView AddDiscountData(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showAddNewFranchisee", "showAddNewFranchisee", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			mav = new ModelAndView("accessDenied");

		} else {
			mav = new ModelAndView("masters/addDiscount");

			try {
				RestTemplate restTemplate = new RestTemplate();
				franchiseeAndMenuList = restTemplate.getForObject(Constants.url + "getFranchiseeAndMenu",
						FranchiseeAndMenuList.class);

				mav.addObject("allFranchiseeAndMenuList", franchiseeAndMenuList);

				categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				mCategoryList = new ArrayList<MCategoryList>();

				mCategoryList = categoryListResponse.getmCategoryList();

				mav.addObject("mCategoryList", mCategoryList);
				mav.addObject("isEdit", 0);
			} catch (Exception e) {
				logger.info("Franchisee Controller Exception " + e.getMessage());
			}
		}
		return mav;
	}

	@RequestMapping(value = "/setAllFrIdSelectedForDisc", method = RequestMethod.GET)
	public @ResponseBody List<FranchiseeList> setAllFrIdSelectedForDisc() {
		logger.info("inside ajax call for fr all selected");

		return franchiseeAndMenuList.getAllFranchisee();
	}

	@RequestMapping(value = "/addDiscountProcess", method = RequestMethod.POST)
	public String SaveDiscount(HttpServletRequest request, HttpServletResponse response) {
		try {
			int discId = 0;
			try {
				discId = Integer.parseInt(request.getParameter("discId"));
			} catch (Exception e) {
				discId = 0;
			}
			int isActive = Integer.parseInt(request.getParameter("is_active"));
			String[] itemShow = request.getParameterValues("items");
			String[] frId = request.getParameterValues("fr_id");
			int catId = Integer.parseInt(request.getParameter("catId"));
			float discPer = Float.parseFloat(request.getParameter("disc_per"));
			RestTemplate rest = new RestTemplate();

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < itemShow.length; i++) {
				sb = sb.append(itemShow[i] + ",");

			}
			String items = sb.toString();
			items = items.substring(0, items.length() - 1);
			logger.info("items" + items);

			StringBuilder sb1 = new StringBuilder();
			for (int i = 0; i < frId.length; i++) {
				sb1 = sb1.append(frId[i] + ",");

			}
			String frIdList = sb1.toString();
			frIdList = frIdList.substring(0, frIdList.length() - 1);
			logger.info("frIds" + frIdList);

			Discount disc = new Discount();
			disc.setDiscId(discId);
			disc.setItemId(items);
			disc.setFranchId(frIdList);
			disc.setCategoryId(catId);
			disc.setDiscPer(discPer);
			disc.setDelStatus(0);
			disc.setIsActive(isActive);
			disc.setSubCategoryId(0);

			disc.setInt1(0);
			disc.setInt2(0);
			disc.setInt3(0);
			disc.setVar1("");
			disc.setVar2("");
			disc.setVar3("");

			Discount discount = rest.postForObject(Constants.url + "/saveDiscount", disc, Discount.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/addDiscount";

	}

	@RequestMapping(value = "/getItemsByCatagoryID", method = RequestMethod.GET)
	public @ResponseBody List<CommonConf> getItemsByCatagoryID(HttpServletRequest request,
			HttpServletResponse response) {
		List<CommonConf> commonConfList = new ArrayList<CommonConf>();

		// try {
		int catId = Integer.parseInt(request.getParameter("cat_id"));
		System.out.println("cat Id " + catId);
		RestTemplate restTemplate = new RestTemplate();
		List<SpecialCake> specialCakeList = new ArrayList<SpecialCake>();
		/*
		 * MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,
		 * Object>(); map.add("itemGrp1", catId);
		 * 
		 * 
		 * Item[] item = restTemplate.postForObject(Constants.url +
		 * "getItemsByCatIdAndSortId", map, Item[].class);
		 * 
		 * itemsList = new ArrayList<Item>(Arrays.asList(item));
		 * System.out.println("Item List: "+itemsList.toString());
		 */

		if (catId == 5) {
			SpCakeResponse spCakeResponse = restTemplate.getForObject(Constants.url + "showSpecialCakeList",
					SpCakeResponse.class);
			logger.info("SpCake Controller SpCakeList Response " + spCakeResponse.toString());

			specialCakeList = spCakeResponse.getSpecialCake();

			for (SpecialCake specialCake : specialCakeList) {
				CommonConf commonConf = new CommonConf();
				commonConf.setId(specialCake.getSpId());
				commonConf.setName(specialCake.getSpCode() + "-" + specialCake.getSpName());
				commonConfList.add(commonConf);
				logger.info("spCommonConf" + commonConf.toString());
			}

			logger.info("------------------------");
		} else {
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("itemGrp1", catId);

			Item[] item = restTemplate.postForObject(Constants.url + "getItemsByCatId", map, Item[].class);
			ArrayList<Item> itemList = new ArrayList<Item>(Arrays.asList(item));
			logger.info("Filter Item List " + itemList.toString());

			for (Item items : itemList) {
				CommonConf commonConf = new CommonConf();
				commonConf.setId(items.getId());
				commonConf.setName(items.getItemName());
				commonConfList.add(commonConf);
				logger.info("itemCommonConf" + commonConf.toString());
			}
			logger.info("------------------------");
		}
		/*
		 * } catch (Exception e) {
		 * System.out.println("Exception in /AJAX getItemsByCatId"); }
		 */
		System.err.println("commonConfList" + commonConfList.toString());
		return commonConfList;
	}

	@RequestMapping(value = "/showDiscountList", method = RequestMethod.GET)
	public ModelAndView ShowDiscountList() {

		ModelAndView mav = new ModelAndView("masters/listAllDiscount");
		try {
			RestTemplate rest = new RestTemplate();
			List<Discount> discList = rest.getForObject(Constants.url + "/getAllDiscount", List.class);
			mav.addObject("discList", discList);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return mav;
	}

	@RequestMapping(value = "/deleteDiscount/{discId}", method = RequestMethod.GET)

	public String deleteCustomer(@PathVariable("discId") int id) {

		try {
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("id", id);
			Info info = restTemplate.postForObject(Constants.url + "/deleteDiscountId", map, Info.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "redirect:/showDiscountList";
	}

	@RequestMapping(value = "/updateDiscount/{discId}", method = RequestMethod.GET)
	public ModelAndView updateDiscount(@PathVariable int discId) {
		ModelAndView model = new ModelAndView("masters/addDiscount");
		List<CommonConf> commonConfList = new ArrayList<CommonConf>();

		try {
			RestTemplate restTemplate = new RestTemplate();
			List<SpecialCake> specialCakeList = new ArrayList<SpecialCake>();

			franchiseeAndMenuList = restTemplate.getForObject(Constants.url + "getFranchiseeAndMenu",
					FranchiseeAndMenuList.class);
			categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
					CategoryListResponse.class);
			mCategoryList = new ArrayList<MCategoryList>();

			mCategoryList = categoryListResponse.getmCategoryList();

			model.addObject("mCategoryList", mCategoryList);
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("id", discId);

			Discount discount = restTemplate.postForObject(Constants.url + "getDiscountById", map, Discount.class);

			/*
			 * map = new LinkedMultiValueMap<String, Object>(); map.add("itemGrp1",
			 * discount.getCategoryId());
			 * 
			 * Item[] item = restTemplate.postForObject(Constants.url +
			 * "getItemsByCatIdAndSortId", map, Item[].class);
			 * 
			 * itemsList = new ArrayList<Item>(Arrays.asList(item));
			 * 
			 * List<Integer> frids = Stream.of(discount.getFranchId().split(","))
			 * .map(Integer::parseInt) .collect(Collectors.toList()); List<Integer> itemids
			 * = Stream.of(discount.getItemId().split(",")) .map(Integer::parseInt)
			 * .collect(Collectors.toList()); List<Item> nonSelected=new ArrayList<Item>();
			 * nonSelected.addAll(itemsList); List<Item> selected=new ArrayList<Item>();
			 * if(itemids.size()>0) { for(int i=0;i<itemids.size();i++) {
			 * 
			 * for(int j=0;j<itemsList.size();j++) {
			 * if(itemsList.get(j).getId()==itemids.get(i)) {
			 * selected.add(itemsList.get(j)); nonSelected.remove(itemsList.get(j)); } }
			 * 
			 * } } model.addObject("nonSelectedItems", nonSelected);
			 * model.addObject("selectedItems", selected);
			 */

			if (discount.getCategoryId() == 5) {
				SpCakeResponse spCakeResponse = restTemplate.getForObject(Constants.url + "showSpecialCakeList",
						SpCakeResponse.class);
				logger.info("SpCake Controller SpCakeList Response " + spCakeResponse.toString());

				specialCakeList = spCakeResponse.getSpecialCake();

				for (SpecialCake specialCake : specialCakeList) {
					CommonConf commonConf = new CommonConf();
					commonConf.setId(specialCake.getSpId());
					commonConf.setName(specialCake.getSpCode() + "-" + specialCake.getSpName());
					commonConfList.add(commonConf);
					logger.info("spCommonConf" + commonConf.toString());
				}

			} else {
				map = new LinkedMultiValueMap<String, Object>();
				map.add("itemGrp1", discount.getCategoryId());

				Item[] item = restTemplate.postForObject(Constants.url + "getItemsByCatIdAndSortId", map, Item[].class);
				ArrayList<Item> itemList = new ArrayList<Item>(Arrays.asList(item));
				logger.info("Filter Item List " + itemList.toString());

				for (Item items : itemList) {
					CommonConf commonConf = new CommonConf();
					commonConf.setId(items.getId());
					commonConf.setName(items.getItemName());
					commonConfList.add(commonConf);
					logger.info("itemCommonConf" + commonConf.toString());
				}

			}

			List<Integer> frids = Stream.of(discount.getFranchId().split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());
			List<Integer> itemids = Stream.of(discount.getItemId().split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());
			List<CommonConf> nonSelected = new ArrayList<CommonConf>();
			nonSelected.addAll(commonConfList);
			List<CommonConf> selected = new ArrayList<CommonConf>();
			if (itemids.size() > 0) {
				for (int i = 0; i < itemids.size(); i++) {

					for (int j = 0; j < commonConfList.size(); j++) {
						if (commonConfList.get(j).getId() == itemids.get(i)) {
							selected.add(commonConfList.get(j));
							nonSelected.remove(commonConfList.get(j));
						}
					}

				}
			}
			model.addObject("nonSelectedItems", nonSelected);
			model.addObject("selectedItems", selected);

			List<FranchiseeList> nonSelectedFr = new ArrayList<FranchiseeList>();
			nonSelectedFr.addAll(franchiseeAndMenuList.getAllFranchisee());
			List<FranchiseeList> selectedFr = new ArrayList<FranchiseeList>();
			if (frids.size() > 0) {
				for (int i = 0; i < frids.size(); i++) {

					for (int j = 0; j < franchiseeAndMenuList.getAllFranchisee().size(); j++) {
						if (franchiseeAndMenuList.getAllFranchisee().get(j).getFrId() == frids.get(i)) {
							selectedFr.add(franchiseeAndMenuList.getAllFranchisee().get(j));
							nonSelectedFr.remove(franchiseeAndMenuList.getAllFranchisee().get(j));
						}
					}

				}
			}
			model.addObject("nonSelectedFr", nonSelectedFr);
			model.addObject("selectedFr", selectedFr);
			model.addObject("discount", discount);
			model.addObject("isEdit", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;

	}

}