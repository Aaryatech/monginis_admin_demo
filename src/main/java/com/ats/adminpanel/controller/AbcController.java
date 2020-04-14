package com.ats.adminpanel.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.ats.adminpanel.model.AllFrIdNameList;
import com.ats.adminpanel.model.AllspMessageResponse;
import com.ats.adminpanel.model.ErrorMessage;
import com.ats.adminpanel.model.FlavourConf;
import com.ats.adminpanel.model.FlavourList;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.Main;
import com.ats.adminpanel.model.SearchSpCakeResponse;
import com.ats.adminpanel.model.SpCakeOrder;
import com.ats.adminpanel.model.SpCakeResponse;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.flavours.Flavour;
import com.ats.adminpanel.model.franchisee.AllMenuResponse;
import com.ats.adminpanel.model.franchisee.FranchiseeList;
import com.ats.adminpanel.model.franchisee.Menu;
import com.ats.adminpanel.model.grngvn.FrSetting;
import com.ats.adminpanel.model.manspbill.SpecialCake;
import com.ats.adminpanel.model.masters.SpMessage;

@Controller
public class AbcController {

	FlavourList flavourList;
	int billNo = 0;
	public AllFrIdNameList allFrIdNameList = new AllFrIdNameList();
	SpecialCake specialCake = new SpecialCake();
	List<SpMessage> spMessageList;

	List<Menu> menuList;

	@RequestMapping(value = "/showAddMultiSpManBill", method = RequestMethod.GET)
	public ModelAndView showAddMultiSpManBill(HttpServletRequest request, HttpServletResponse response) {

		
		  spOrderList = new ArrayList<SpCakeOrder>();
		  tempSpOrdList=new ArrayList<TempSpOrder>();
		
		ModelAndView model = null;
		specialCake = new SpecialCake();
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showAddMultiSpManBill", "showAddMultiSpManBill", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == false) {

			model = new ModelAndView("accessDenied");

		} else {
			try {
				model = new ModelAndView("mas_sp_ord/multi_man_spOrd");
				RestTemplate restTemplate = new RestTemplate();
				MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

				allFrIdNameList = new AllFrIdNameList();
				try {

					allFrIdNameList = restTemplate.getForObject(Constants.url + "getAllFrIdName",
							AllFrIdNameList.class);

				} catch (Exception e) {
					System.out.println("Exception in getAllFrIdName" + e.getMessage());
					e.printStackTrace();

				}

				SpCakeResponse spCakeResponse = restTemplate
						.getForObject(Constants.url + "showSpecialCakeListOrderBySpCode", SpCakeResponse.class);
				System.out.println("SpCake Controller SpCakeList Response " + spCakeResponse.toString());
				List<com.ats.adminpanel.model.SpecialCake> specialCakeList = new ArrayList<com.ats.adminpanel.model.SpecialCake>();

				specialCakeList = spCakeResponse.getSpecialCake();

				model.addObject("specialCakeList", specialCakeList);// 1 object to be used in jsp 2 actual object
				model.addObject("specialCake", specialCake);// 1 object to be used in jsp 2 actual object
				model.addObject("frId", 0);// 1 object to be used in jsp 2 actual object

				flavourList = restTemplate.getForObject(Constants.url + "/showFlavourList", FlavourList.class);

				AllspMessageResponse allspMessageList = restTemplate.getForObject(Constants.url + "getAllSpMessage",
						AllspMessageResponse.class);

				spMessageList = allspMessageList.getSpMessage();
				model.addObject("eventList", spMessageList);

				// System.out.println("Special Cake List:" + specialCakeList.toString());
				model.addObject("spNo", "0");
				String pattern = "dd-MM-yyyy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

				String date = simpleDateFormat.format(new Date());
				model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());
				model.addObject("billBy", 1);
				model.addObject("date", date);

				AllMenuResponse allMenuResponse = restTemplate.getForObject(Constants.url + "getAllMenu",
						AllMenuResponse.class);

				menuList = allMenuResponse.getMenuConfigurationPage();
				model.addObject("unSelectedMenuList", menuList);

			} catch (Exception e) {

				System.err.println("Exce in showManualBill" + e.getMessage());
				e.printStackTrace();

			}
			model.addObject("billNo", billNo);
			billNo = 0;
		}

		return model;
	}

	@RequestMapping(value = "/getSpCakeForMultiSpBill", method = RequestMethod.POST)
	public @ResponseBody Object getSpCakeForMultiSpBill(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;

		TempData tempData = new TempData();
		try {
			specialCake = new SpecialCake();
			model = new ModelAndView("manualBill/add_man_bill");
			RestTemplate restTemplate = new RestTemplate();

			String spCode = request.getParameter("sp_cake_id");
			List<Float> weightList = new ArrayList<>();

			int frId = Integer.parseInt(request.getParameter("fr_id"));
			int billBy = Integer.parseInt(request.getParameter("sel_rate"));
			System.err.println("Bill By " + billBy);

			model.addObject("frId", frId);
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("spCode", spCode);
			try {
				SearchSpCakeResponse searchSpCakeResponse = restTemplate
						.postForObject(Constants.url + "/searchSpecialCake", map, SearchSpCakeResponse.class);
				ErrorMessage errorMessage = searchSpCakeResponse.getErrorMessage();
				System.err.println("Selected Special Cake 111111111111" + searchSpCakeResponse.toString());

				specialCake = searchSpCakeResponse.getSpecialCake();
				tempData.setSpecialCake(specialCake);
				model.addObject("specialCake", specialCake);
				int cutSec = searchSpCakeResponse.getSpCakeSup().getCutSection();
				model.addObject("cutSec", cutSec);
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, Integer.parseInt(specialCake.getSpBookb4()));
				model.addObject("convDate", new SimpleDateFormat("dd-MM-yyyy").format(c.getTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addObject("unSelectedFrList", allFrIdNameList.getFrIdNamesList());

			SpCakeResponse spCakeResponse = restTemplate
					.getForObject(Constants.url + "showSpecialCakeListOrderBySpCode", SpCakeResponse.class);
			// System.out.println("SpCake Controller SpCakeList Response 0000000" +
			// spCakeResponse.toString());

			List<com.ats.adminpanel.model.SpecialCake> specialCakeList = new ArrayList<com.ats.adminpanel.model.SpecialCake>();

			specialCakeList = spCakeResponse.getSpecialCake();

			model.addObject("specialCakeList", specialCakeList);

			FranchiseeList frDetails = restTemplate.getForObject(Constants.url + "getFranchisee?frId={frId}",
					FranchiseeList.class, frId);

			float sprRate;
			float spBackendRate;

			/*
			 * float minWt = Float.valueOf(specialCake.getSpMinwt());
			 * 
			 * float maxWt = Float.valueOf(specialCake.getSpMaxwt());
			 * 
			 * weightList.add(minWt); float currentWt = minWt; while (currentWt < maxWt) {
			 * currentWt = currentWt + specialCake.getSpRate2();// spr rate 2 means weight
			 * increment by weightList.add(currentWt); } while (currentWt < 2) { currentWt =
			 * currentWt + 0.5f;//spr rate 2 means weight increment by if(currentWt<=2) {
			 * weightList.add(currentWt); }
			 * 
			 * } float max=2;
			 */

			// float minWt = Float.valueOf(specialCake.getSpMinwt());
			float minWt = 1;
			float maxWt = 15;
			// float maxWt = Float.valueOf(specialCake.getSpMaxwt());

			weightList.add(minWt);
			float currentWt = minWt;
			/*
			 * while (currentWt < maxWt) { currentWt = currentWt +
			 * specialCake.getSpRate2();// spr rate 2 means weight increment by
			 * weightList.add(currentWt); }
			 */
			while (currentWt < 15) {
				currentWt = currentWt + 0.5f;// spr rate 2 means weight increment by
				if (currentWt <= 15) {
					weightList.add(currentWt);
				}

			}
			float max = 15;
			while (max < maxWt) {
				max = max + specialCake.getSpRate2();
				weightList.add(max);
			}
			System.out.println("Weight List for SP Cake: " + weightList.toString());

			if (frDetails.getFrRateCat() == 1) {
				System.err.println("Rate cat 1");

				if (billBy == 0) { // means calc by mrp
					sprRate = specialCake.getMrpRate1();
					spBackendRate = specialCake.getMrpRate1();
				} else {// means calc by rate

					sprRate = specialCake.getSpRate1();
					spBackendRate = specialCake.getSpRate1();
				}

			} else {
				System.err.println("Rate cat no");

				if (billBy == 0) {

					sprRate = specialCake.getMrpRate3();
					spBackendRate = specialCake.getMrpRate3();
				} else {

					sprRate = specialCake.getSpRate3();
					spBackendRate = specialCake.getSpRate3();
				}

			}
			// --------------------------New For
			// Flavors----------------------------------------
			List<Flavour> filterFlavoursList = new ArrayList<>();
			map = new LinkedMultiValueMap<String, Object>();
			map.add("spId", specialCake.getSpId());
			flavourList = restTemplate.postForObject(Constants.url + "/showFlavourListBySpId", map, FlavourList.class);
			List<Flavour> flavoursArrayList = flavourList.getFlavour();

			// for (int i = 0; i < flavoursArrayList.size(); i++) {
			// filterFlavoursList.add(flavoursArrayList.get(i));
			// }
			List<String> list = Arrays.asList(specialCake.getErpLinkcode().split(","));
			System.err.println("list" + specialCake.getErpLinkcode());
			for (Flavour flavour : flavoursArrayList) {

				if (list.contains("" + flavour.getSpfId())) {
					flavour.setSpfAdonRate(0.0);

				}

				filterFlavoursList.add(flavour);

			}

			// ------------------------------------------------------------------
			System.err.println("sprRate " + sprRate);

			tempData.setBillBy(billBy);
			tempData.setFilterFlavoursList(filterFlavoursList);
			tempData.setSpBackendRate(spBackendRate);

			tempData.setSprRate(sprRate);
			tempData.setWeightList(weightList);

			model.addObject("sprRate", sprRate);
			model.addObject("spBackendRate", spBackendRate);
			model.addObject("filterFlavoursList", filterFlavoursList);
			model.addObject("weightList", weightList);
			model.addObject("billBy", billBy);
			String spNo = "";
			try {
				spNo = getSpNo(frId);
			} catch (Exception e) {
				spNo = "";
				e.printStackTrace();
			}

			tempData.setSpNo(spNo);

			List<Menu> allMenuList = restTemplate.getForObject(Constants.url + "getAllMenuList", List.class);
			model.addObject("frMenuList", allMenuList);
			System.err.println("frMenuList" + allMenuList.toString());
			// System.out.println("Special Cake List:" + specialCakeList.toString());
			model.addObject("spNo", spNo);
			AllspMessageResponse allspMessageList = restTemplate.getForObject(Constants.url + "getAllSpMessage",
					AllspMessageResponse.class);

			spMessageList = allspMessageList.getSpMessage();
			model.addObject("eventList", spMessageList);
			model.addObject("frId", frId);
			model.addObject("billBy", billBy);
			String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.addObject("currentDate", currentDate);
			model.addObject("date", currentDate);

		} catch (Exception e) {
			System.err.println("Exce in getSpCakeForManBill" + e.getMessage());
			e.printStackTrace();
		}
		return tempData;

	}

	List<SpCakeOrder> spOrderList = new ArrayList<SpCakeOrder>();
	List<TempSpOrder> tempSpOrdList=new ArrayList<TempSpOrder>();

	@RequestMapping(value = "/addSpOrderInList", method = RequestMethod.POST)
	public @ResponseBody Object addSpOrderInList(HttpServletRequest request, HttpServletResponse response) {
		SpCakeOrder spCakeOrder = null;
		try {

			RestTemplate restTemplate = new RestTemplate();

			int frId = Integer.parseInt(request.getParameter("fr_id"));
			int menuId = Integer.parseInt(request.getParameter("selectMenu"));
			String spCode = request.getParameter("sp_cake_id");
			String flav_name = request.getParameter("flav_name");

			String spProdDate = request.getParameter("spProdDate");

			Date prodDate = Main.stringToDate(spProdDate);
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date currentDate = new Date();

			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);

			// Current Date
			Date orderDate = c.getTime();

			java.sql.Date sqlProdDate = new java.sql.Date(prodDate.getTime());

			FranchiseeList frDetails = restTemplate.getForObject(Constants.url + "getFranchisee?frId={frId}",
					FranchiseeList.class, frId);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map = new LinkedMultiValueMap<String, Object>();
			map.add("spCode", spCode);

			SearchSpCakeResponse searchSpCakeResponse = restTemplate.postForObject(Constants.url + "/searchSpecialCake",
					map, SearchSpCakeResponse.class);
			ErrorMessage errorMessage = searchSpCakeResponse.getErrorMessage();
			System.err.println("Selected Special Cake 111111111111" + searchSpCakeResponse.toString());

			specialCake = searchSpCakeResponse.getSpecialCake();

			int spfId = Integer.parseInt(request.getParameter("spFlavour"));
			FlavourConf flavourConf = new FlavourConf();
			float spWeight = Float.parseFloat(request.getParameter("spwt"));
			map = new LinkedMultiValueMap<String, Object>();
			map.add("spId", specialCake.getSpId());
			map.add("spfId", spfId);
			flavourConf = restTemplate.postForObject(Constants.url + "getFlConfByIds", map, FlavourConf.class);
			System.err.println("flavour is " + flavourConf.toString());

			float spBackendRate = spWeight * flavourConf.getRate();
			int rateType=Integer.parseInt(request.getParameter("sel_rate"));
			float	spGrandTotal=spWeight * flavourConf.getMrp();
			
			
			
			spCakeOrder = new SpCakeOrder();
			String na = "-";
			
			spCakeOrder.setCustEmail(na);
			spCakeOrder.setCustGstin(na);
			spCakeOrder.setDisc(0);
			spCakeOrder.setExInt1(0);
			spCakeOrder.setExInt2(0);
			spCakeOrder.setExtraCharges(0);
			spCakeOrder.setExVar1(na);
			spCakeOrder.setExVar2(na);
			
			spCakeOrder.setIsAllocated(0);
			spCakeOrder.setIsBillGenerated(0);
			spCakeOrder.setIsSlotUsed(0);
			
			spCakeOrder.setOrderPhoto(na);
			spCakeOrder.setOrderPhoto2(na);
			spCakeOrder.setRmAmount(0);
			
			
			
			spCakeOrder.setSlipNo(na);
			spCakeOrder.setSpAdvance(0);
			spCakeOrder.setSpBookedForName(na);
			
			spCakeOrder.setSpCustMobNo(na);
			spCakeOrder.setSpCustName(na);
			
			spCakeOrder.setSpEvents(na);
			spCakeOrder.setSpInstructions(na);
			spCakeOrder.setSpDeliveryPlace(na);
			spCakeOrder.setSpBookForMobNo(na);

			
			spCakeOrder.setFrCode(frDetails.getFrCode());
			spCakeOrder.setFrId(frId);
		
			spCakeOrder.setItemId(spCode);
			spCakeOrder.setMenuId(menuId);
			spCakeOrder.setOrderDate(dateFormat.format(orderDate));
			spCakeOrder.setSpBackendRate(spBackendRate);



			// spCakeOrder.setSpBookForDob(new java.sql.Date("2019","01","01"));
			spCakeOrder.setSpBookForDob(sqlProdDate);

			spCakeOrder.setSpCustDob(sqlProdDate);
			
			spCakeOrder.setSpDeliveryDate(sqlProdDate);
			spCakeOrder.setSpEstDeliDate(sqlProdDate);
			
			spCakeOrder.setSpFlavourId(spfId);
			spCakeOrder.setSpMaxWeight(Float.parseFloat(specialCake.getSpMaxwt()));
			spCakeOrder.setSpMinWeight(Float.parseFloat(specialCake.getSpMinwt()));
			
			spCakeOrder.setSpOrderNo(0);
			
			spCakeOrder.setSpPrice(spBackendRate);
			spCakeOrder.setSpProdDate(sqlProdDate);
			spCakeOrder.setSpProdTime(Integer.parseInt(specialCake.getSpBookb4()));
			spCakeOrder.setSpSelectedWeight(spWeight);
			spCakeOrder.setSpSubTotal(spBackendRate);
			spCakeOrder.setSpTotalAddRate(0);
			spCakeOrder.setSpType(1);

			spCakeOrder.setTax2Amt(100);
			spCakeOrder.setTax2((float) specialCake.getSpTax2());
			spCakeOrder.setTax1Amt(200);
			spCakeOrder.setTax1((float) specialCake.getSpTax1());
			spCakeOrder.setSpGrandTotal(spGrandTotal);
			
			TempData tempData = new TempData();
			
			TempSpOrder tempSpOrd=new TempSpOrder();
			
			
			 tempSpOrd.setFlavour(flav_name);
			 tempSpOrd.setIndex(spOrderList.size());
			 tempSpOrd.setSpAmt(spBackendRate);
			 tempSpOrd.setSpCode(specialCake.getSpCode());
			 tempSpOrd.setSpId(specialCake.getSpId());
			 tempSpOrd.setSpName(specialCake.getSpName());
			 tempSpOrd.setSpWeight(spWeight);
			 tempSpOrdList.add(tempSpOrd);
			 	

		} catch (Exception e) {

		}

		spOrderList.add(spCakeOrder);

		return tempSpOrdList;

	}

	public String getSpNo(int frId) {
		String spNoNewStr = "";
		try {

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			RestTemplate restTemplate = new RestTemplate();
			map.add("frId", frId);
			FrSetting frSetting = restTemplate.postForObject(Constants.url + "getFrSettingValue", map, FrSetting.class);

			int spNo = frSetting.getSpNo();

			int length = String.valueOf(spNo).length();
			length = 5 - length;
			StringBuilder spNoNew = new StringBuilder(frSetting.getFrCode() + "-");

			for (int i = 0; i < length; i++) {
				String j = "0";
				spNoNew.append(j);
			}
			spNoNew.append(String.valueOf(spNo));
			spNoNewStr = "" + spNoNew;

		} catch (Exception e) {

		}

		return spNoNewStr;

	}
}
