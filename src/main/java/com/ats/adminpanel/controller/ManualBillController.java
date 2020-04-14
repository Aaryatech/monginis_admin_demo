package com.ats.adminpanel.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ats.adminpanel.commons.AccessControll;
import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.commons.VpsImageUpload;
import com.ats.adminpanel.model.AllFrIdNameList;
import com.ats.adminpanel.model.AllspMessageResponse;
import com.ats.adminpanel.model.ConfigureFrBean;
import com.ats.adminpanel.model.ConfigureFrListResponse;
import com.ats.adminpanel.model.ErrorMessage;
import com.ats.adminpanel.model.FlavourConf;
import com.ats.adminpanel.model.FlavourList;
import com.ats.adminpanel.model.GenerateBill;
import com.ats.adminpanel.model.GenerateBillList;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.Main;
import com.ats.adminpanel.model.SearchSpCakeResponse;
import com.ats.adminpanel.model.SectionMaster;
import com.ats.adminpanel.model.SpCakeOrder;
import com.ats.adminpanel.model.SpCakeOrderRes;
import com.ats.adminpanel.model.SpCakeResponse;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.billing.PostBillDataCommon;
import com.ats.adminpanel.model.billing.PostBillDetail;
import com.ats.adminpanel.model.billing.PostBillHeader;
import com.ats.adminpanel.model.flavours.Flavour;
import com.ats.adminpanel.model.franchisee.FranchiseeList;
import com.ats.adminpanel.model.franchisee.Menu;
import com.ats.adminpanel.model.grngvn.FrSetting;
import com.ats.adminpanel.model.item.FrItemStockConfigure;
import com.ats.adminpanel.model.item.FrItemStockConfigureList;
import com.ats.adminpanel.model.manspbill.SpecialCake;
import com.ats.adminpanel.model.masters.SpMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@Scope("session")
public class ManualBillController {
	FlavourList flavourList;
    int billNo=0;
	public AllFrIdNameList allFrIdNameList = new AllFrIdNameList();
	SpecialCake specialCake = new SpecialCake();
	List<SpMessage> spMessageList;

	@RequestMapping(value = "/showManualBill", method = RequestMethod.GET)
	public ModelAndView showManualBill(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
		specialCake = new SpecialCake();
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showManualBill", "showManualBill", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			try {
				model = new ModelAndView("manualBill/add_man_bill");
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

			} catch (Exception e) {

				System.err.println("Exce in showManualBill" + e.getMessage());
				e.printStackTrace();

			}
			model.addObject("billNo", billNo);
			billNo = 0;
		}

		return model;
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

	// getSpCakeForManBill
	@RequestMapping(value = "/getSpCakeForManBill", method = RequestMethod.POST)
	public ModelAndView getSpCakeForManBill(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = null;
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

			/*float minWt = Float.valueOf(specialCake.getSpMinwt());

			float maxWt = Float.valueOf(specialCake.getSpMaxwt());

			weightList.add(minWt);
			float currentWt = minWt;
			while (currentWt < maxWt) {
				currentWt = currentWt + specialCake.getSpRate2();// spr rate 2 means weight increment by
				weightList.add(currentWt);
			}
			while (currentWt < 2) {
				currentWt = currentWt + 0.5f;//spr rate 2 means weight increment by 
				if(currentWt<=2) {
				weightList.add(currentWt);
				}
				
			}
			float max=2;*/
			
			
			//float minWt = Float.valueOf(specialCake.getSpMinwt());
			float minWt = 1;
			float maxWt = 15;
			//float maxWt = Float.valueOf(specialCake.getSpMaxwt());

			weightList.add(minWt);
			float currentWt = minWt;
		/*	while (currentWt < maxWt) {
				currentWt = currentWt + specialCake.getSpRate2();// spr rate 2 means weight increment by
				weightList.add(currentWt);
			}*/
			while (currentWt < 15) {
				currentWt = currentWt + 0.5f;//spr rate 2 means weight increment by 
				if(currentWt<=15) {
				weightList.add(currentWt);
				}
				
			}
			float max=15;
			while(max<maxWt)
			{
				max=max+specialCake.getSpRate2();
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
					spBackendRate =specialCake.getMrpRate3(); 
				} else {

					sprRate = specialCake.getSpRate3();
					spBackendRate = specialCake.getSpRate3();
				}

			}
			//--------------------------New For Flavors----------------------------------------
			List<Flavour> filterFlavoursList=new ArrayList<>();
			 map = new LinkedMultiValueMap<String, Object>();
			 map.add("spId", specialCake.getSpId());
			flavourList = restTemplate.postForObject(Constants.url + "/showFlavourListBySpId",map, FlavourList.class);
				List<Flavour> flavoursArrayList = flavourList.getFlavour();

			//for (int i = 0; i < flavoursArrayList.size(); i++) {
             //	filterFlavoursList.add(flavoursArrayList.get(i));
			//}
			List<String> list = Arrays.asList(specialCake.getErpLinkcode().split(","));
			System.err.println("list"+specialCake.getErpLinkcode());
			for (Flavour flavour : flavoursArrayList) {
				
					if (list.contains(""+flavour.getSpfId())) {
						flavour.setSpfAdonRate(0.0);
					
					}
				
					filterFlavoursList.add(flavour);

			}
			
			//------------------------------------------------------------------
			System.err.println("sprRate " + sprRate);
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
			String currentDate =new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.addObject("currentDate", currentDate);
			model.addObject("date", currentDate);
			model.addObject("date", currentDate);

			SectionMaster[] sectionMasterArray = restTemplate.getForObject(Constants.url + "/getSectionListOnly",
					SectionMaster[].class);
			List<SectionMaster> sectionList = new ArrayList<SectionMaster>(Arrays.asList(sectionMasterArray));
			model.addObject("sectionList", sectionList);
			
		} catch (Exception e) {
			System.err.println("Exce in getSpCakeForManBill" + e.getMessage());
			e.printStackTrace();
		}
		return model;

	}

	@RequestMapping(value = "/getFlavourBySpfId", method = RequestMethod.GET)
	public @ResponseBody List<Flavour> flavourById(@RequestParam(value = "spType", required = true) int spType) {

		List<Flavour> flavoursListWithAddonRate = new ArrayList<Flavour>();

		System.out.println("SpType:: " + spType);

		List<Flavour> flavoursList = new ArrayList<Flavour>();
		List<Flavour> filterFlavoursList = new ArrayList<Flavour>();

		RestTemplate restTemplate = new RestTemplate();

		// flavourList = restTemplate.getForObject(Constants.url + "/showFlavourList",
		// FlavourList.class);
		// flavoursList = flavourList.getFlavour();

		try {

			flavoursList = flavourList.getFlavour();

			// System.out.println("FLAVOURS:" + flavoursList.toString());

			// System.out.println("DB Sptype" + flavoursList.get(0).getSpType());

			for (int i = 0; i < flavoursList.size(); i++) {

				//if (flavoursList.get(i).getSpType() == spType) {
					filterFlavoursList.add(flavoursList.get(i));

				//}
			}
			for (Flavour flavour : filterFlavoursList) {
				if (specialCake.getIsAddonRateAppli() == 1) {
					List<String> list = Arrays.asList(specialCake.getErpLinkcode().split(","));
					if (list.contains("" + flavour.getSpfId())) {
						flavour.setSpfAdonRate(0.0);
					}
					flavoursListWithAddonRate.add(flavour);
					// System.err.println(flavour.getSpfId());
					// System.err.println(flavoursListWithAddonRate.toString());
				} else {
					flavour.setSpfAdonRate(0.0);
					flavoursListWithAddonRate.add(flavour);
				}
			}
			System.out.println("Filter flavour List " + flavoursListWithAddonRate.toString());
		} catch (Exception e) {
			System.out.println("Exception in getFlavourBySpfId() AJAX CALL");

		}

		// return flavoursListWithAddonRate;

		 return flavoursListWithAddonRate;

		 //	return flavoursList;

	}

	private static final Logger logger = LoggerFactory.getLogger(ManualBillController.class);

	// ------------------------Get Addon Rate AJAX
	// method(spcakeorder)-----------------------------------
	@RequestMapping(value = "/getAddOnRate", method = RequestMethod.GET)
	public @ResponseBody FlavourConf getAddOnRate(@RequestParam(value = "spfId", required = true) int spfId,@RequestParam(value = "spId", required = true) int spId) {
		FlavourConf flavourConf=new FlavourConf();
		
		System.err.println("Hi" +spfId + "spId  " +spId);
		/*List<Flavour> flavoursList = new ArrayList<Flavour>();
		Flavour filteredFlavour = new Flavour();
		flavoursList = flavourList.getFlavour();

		for (Flavour flavour : flavoursList) {

			if (flavour.getSpfId() == spfId) {

				filteredFlavour = flavour;
			}
		}
		return filteredFlavour;*/
		RestTemplate restTemplate = new RestTemplate();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("spId", spId);
		map.add("spfId", spfId);
		flavourConf = restTemplate.postForObject(Constants.url + "getFlConfByIds", map, FlavourConf.class);
System.err.println("flavour is " +flavourConf.toString());
		return flavourConf;
	}


	// insertManualSpBill
	@RequestMapping(value = "/insertManualSpBill", method = RequestMethod.POST)
	public String insertManualSpBill(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "order_photo", required = false) List<MultipartFile> orderPhoto,
			@RequestParam(value = "cust_choice_ck", required = false) List<MultipartFile> custChoiceCk)
			throws JsonProcessingException {
		
		SpCakeOrderRes spCakeOrderRes = new SpCakeOrderRes();
		ModelAndView model = null;
		float exCharges = 0.0f;
		Calendar calender = Calendar.getInstance();
	     SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
		try {
			
			model = new ModelAndView("manualBill/add_man_bill");
			RestTemplate restTemplate = new RestTemplate();
			
			int billClick = Integer.parseInt(request.getParameter("hdnbt"));
			logger.info("billClick" + billClick);
			String frName = request.getParameter("fr_name");
			logger.info("frName" + frName);
			
			String gstNo = request.getParameter("gst_no");
			logger.info("gstNo" + gstNo);

			int frId = Integer.parseInt(request.getParameter("fr_id"));
			logger.info("frId" + frId);
			
			int billBy = Integer.parseInt(request.getParameter("billBy"));
			logger.info("billBy" + billBy);

			FranchiseeList frDetails = restTemplate.getForObject(Constants.url + "getFranchisee?frId={frId}",
					FranchiseeList.class, frId);

			int spId = Integer.parseInt(request.getParameter("sp_id"));

			int spMenuId = Integer.parseInt(request.getParameter("spMenuId"));
			logger.info("1spId" + spId);
			
			String spCode = request.getParameter("sp_code");
			logger.info("2spCode" + spCode);

			String spName = request.getParameter("sp_name");
			logger.info("3spName" + spName);

			String spMinWeight = request.getParameter("sp_min_weight");
			logger.info("5spMinWeight" + spMinWeight);

			String spMaxWeight = request.getParameter("sp_max_weight");
			logger.info("6spMaxWeight" + spMaxWeight);

			String spProTime = request.getParameter("sp_pro_time");
			logger.info("7spProTime" + spProTime);

			int prodTime = Integer.parseInt(spProTime);
			logger.info("prodTime" + prodTime);

			String spEdt = request.getParameter("sp_est_del_date");
			logger.info("8spEdt" + spEdt);

			int spType = Integer.parseInt(request.getParameter("sptype"));
			logger.info("9spType" + spType);

			String spFlavour = request.getParameter("spFlavour");
			logger.info("10spFlavour" + spFlavour);

			float spWeight = Float.parseFloat(request.getParameter("spwt"));
			logger.info("11spWeight" + spWeight);

			String spEvents = request.getParameter("sp_event");
			logger.info("12spEvents" + spEvents);
			
			String spDeliveryDt = request.getParameter("datepicker");
			logger.info("14spDeliveryDt" + spDeliveryDt);
			
			String spProdDate = request.getParameter("spProdDate");
			logger.info("15spProdDate" + spProdDate);

			String spCustDOB = request.getParameter("datepicker4");
			logger.info("17spCustDOB" + spCustDOB);

			String spCustName = request.getParameter("sp_cust_name");
			logger.info("21spCustName" + spCustName);

			String spGrand = request.getParameter("sp_grand");
			logger.info("23spGrand" + spGrand);

			String spPrice = request.getParameter("sp_calc_price");
			logger.info("24spPrice" + spPrice);

			String spAddRate = request.getParameter("sp_add_rate");
			logger.info("25spAddRate" + spAddRate);
			
			float dbAdonRate = Float.parseFloat(request.getParameter("dbAdonRate"));
			logger.info("dbAdonRate" + dbAdonRate);
			
			float spSubTotal = Float.parseFloat(request.getParameter("sp_sub_total"));
			logger.info("26spSubTotal" + spSubTotal);

			float tax1 = Float.parseFloat(request.getParameter("tax1"));
			logger.info("27tax1" + tax1);

			float tax2 = Float.parseFloat(request.getParameter("tax2"));
			logger.info("28tax2" + tax2);

			float tax1Amt = Float.parseFloat(request.getParameter("t1amt"));
			logger.info("29tax1Amt" + tax1Amt);

			float tax2Amt = Float.parseFloat(request.getParameter("t2amt"));
			logger.info("30tax2Amt" + tax2Amt);

			String rmAmount = request.getParameter("rm_amount");
			logger.info("31rmAmount" + rmAmount);
	
     		String spPlace = request.getParameter("sp_place");
			logger.info("33spPlace" + spPlace);

			exCharges = Float.parseFloat(request.getParameter("sp_ex_charges"));
			logger.info("34exCharges" + exCharges);

			float disc = Float.parseFloat(request.getParameter("sp_disc"));
			logger.info("35discper" + disc);

			String ctype = request.getParameter("ctype");
			logger.info("36ctype" + ctype);

			String spPhoUpload = request.getParameter("spPhoUpload");
			logger.info("spPhoUpload" + spPhoUpload);
			String eventName = request.getParameter("event_name");
			logger.info("eventName" + eventName);

			String isCustCh = request.getParameter("isCustCh");
			logger.info("isCustCh" + isCustCh);

			String productionTime = request.getParameter("production_time");
			logger.info("productionTime" + productionTime);

			int isSlotUsed = Integer.parseInt(request.getParameter("isSlotUsed")); // isSlotUsed
			logger.info("isSlotUsed" + isSlotUsed);

			String spImage = request.getParameter("prevImage");
			logger.info("spImage" + spImage);

			// ---------isCustSpCk And isSpPhoUpload Special Cake Value(1/0)-------
			int isCustSpCk = Integer.parseInt(request.getParameter("isCustChoiceCk"));
			logger.info("isCustSpCk" + isCustSpCk);

			int isSpPhoUpload = Integer.parseInt(request.getParameter("spPhoUpload"));
			logger.info("isSpPhoUpload" + isSpPhoUpload);

			String addonRatePerKG = request.getParameter("addonRatePerKG");
			logger.info("addonRatePerKG" + addonRatePerKG);

			String custEmail = request.getParameter("cust_email");
			logger.info("custEmail" + custEmail);
			String custGstNo = request.getParameter("cust_gst_no");

			String spCustMobNo = request.getParameter("cust_mobile");
			logger.info("addonRatePerKG" + addonRatePerKG);

			float backendSpRate = Float.parseFloat(request.getParameter("spBackendRate"));
			logger.info("backendSpRate" + backendSpRate);

			String curTimeStamp = new SimpleDateFormat("yyyy_MM_dd.HH_mm_ss").format(new Date());
			logger.info("curTimeStamp" + curTimeStamp);
			String sectionId=request.getParameter("sectionId");
			String custChCk = "";
			String orderPhoto1 = "";

			if (isSpPhoUpload == 1) {
				logger.info("Empty image");

				VpsImageUpload upload = new VpsImageUpload();

				try {
					if (orderPhoto.get(0).getOriginalFilename() == "") {
						orderPhoto1 = "";
					} else {
						orderPhoto1 = curTimeStamp + "" + orderPhoto.get(0).getOriginalFilename();

						LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
						FileOutputStream fo = new FileOutputStream(orderPhoto1);
						fo.write(orderPhoto.get(0).getBytes());
						fo.close();
						map.add("file", new FileSystemResource(orderPhoto1));
						map.add("imageName", orderPhoto1);
						map.add("type", "ph1");
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.MULTIPART_FORM_DATA);

						HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
						Info infoRes = restTemplate.postForObject(Constants.url + "/photoUpload", requestEntity,
								Info.class);

					}

				} catch (IOException e) {

					System.out.println("Exce in File Upload In Sp Cake Photo Insert " + e.getMessage());
					e.printStackTrace();
				}

			}

			if (isCustSpCk == 1) {

				VpsImageUpload upload = new VpsImageUpload();
				try {
					if (orderPhoto.get(0).getOriginalFilename() == "") {
						orderPhoto1 = "";
					} else {
						orderPhoto1 = curTimeStamp + "" + orderPhoto.get(0).getOriginalFilename();
						LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
						FileOutputStream fo = new FileOutputStream(orderPhoto1);
						fo.write(orderPhoto.get(0).getBytes());
						fo.close();
						map.add("file", new FileSystemResource(orderPhoto1));
						map.add("imageName", orderPhoto1);
						map.add("type", "ph1");
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.MULTIPART_FORM_DATA);

						HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
						Info infoRes = restTemplate.postForObject(Constants.url + "/photoUpload", requestEntity,
								Info.class);
					}

					if (custChoiceCk.get(0).getOriginalFilename() == "") {
						custChCk = "";
					} else {
						custChCk = curTimeStamp + "" + custChoiceCk.get(0).getOriginalFilename();

						LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
						FileOutputStream fo = new FileOutputStream(custChCk);
						fo.write(custChoiceCk.get(0).getBytes());
						fo.close();
						map.add("file", new FileSystemResource(custChCk));
						map.add("imageName", custChCk);
						map.add("type", "ph2");
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.MULTIPART_FORM_DATA);

						HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
						Info infoRes = restTemplate.postForObject(Constants.url + "/photoUpload", requestEntity,
								Info.class);
					}

					System.out.println("upload method called for two photo   " + orderPhoto.get(0).getName());

				} catch (IOException e) {

					System.out.println("Exce in File Upload In Sp Cake Photo Insert " + e.getMessage());
					e.printStackTrace();
				}

			}
			SpCakeOrder spCakeOrder = new SpCakeOrder();
			spCakeOrder.setFrCode(frDetails.getFrCode());

			spCakeOrder.setFrId(frId);

			// -----Order Date And Production Date------
			Date prodDate = Main.stringToDate(spProdDate);
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date currentDate = new Date();

			// convert date to calendar
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);

			// Current Date
			Date orderDate = c.getTime();

			java.sql.Date sqlProdDate = new java.sql.Date(prodDate.getTime());

			System.out.println("Todays date is: " + currentDate);
			System.out.println("Prod date is: " + prodDate);

			// ---------------------------------------------------------------

			final SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date date = new Date();
			try {

				date = dmyFormat.parse(spCustDOB);
				System.out.println(date);
				System.out.println(dmyFormat.format(date));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			java.util.Date utilSpEdt = new java.util.Date();
			try {
				utilSpEdt = dmyFormat.parse(spEdt);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			java.util.Date utilSpDeliveryDt = new java.util.Date();
			try {
				utilSpDeliveryDt = dmyFormat.parse(spDeliveryDt);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			java.sql.Date sqlBookForDob = new java.sql.Date(prodDate.getTime());
			java.sql.Date sqlSpCustDOB = new java.sql.Date(date.getTime());
			java.sql.Date sqlSpEdt = new java.sql.Date(utilSpEdt.getTime());

			java.sql.Date sqlSpDeliveryDt = new java.sql.Date(utilSpDeliveryDt.getTime());

			spCakeOrder.setItemId(spCode);
			spCakeOrder.setOrderDate(dateFormat.format(orderDate));
			spCakeOrder.setRmAmount(spSubTotal);
			spCakeOrder.setSpTotalAddRate(Float.valueOf(spAddRate));
			spCakeOrder.setSpAdvance(0);

			spCakeOrder.setSpBookedForName("-");
			spCakeOrder.setSpBookForDob(sqlBookForDob);
			spCakeOrder.setSpBookForMobNo(getInvoiceNo(frId, frDetails.getFrCode()));
			spCakeOrder.setSpCustDob(sqlSpCustDOB);
			spCakeOrder.setSpInstructions("-");
			spCakeOrder.setOrderPhoto(orderPhoto1);
			spCakeOrder.setOrderPhoto2(custChCk);

			spCakeOrder.setSpCustMobNo(spCustMobNo);
			spCakeOrder.setSpCustName(spCustName);
			spCakeOrder.setSpDeliveryDate(sqlSpDeliveryDt);
			spCakeOrder.setSpEstDeliDate(sqlSpEdt);
			spCakeOrder.setSpEvents(spEvents);
			spCakeOrder.setSpEventsName(eventName);
			spCakeOrder.setSpFlavourId(Integer.parseInt(spFlavour));
			spCakeOrder.setSpGrandTotal(Float.parseFloat(spGrand));
			spCakeOrder.setSpId(spId);
			spCakeOrder.setSpMaxWeight(Float.valueOf(spMaxWeight));
			spCakeOrder.setSpMinWeight(Float.valueOf(spMinWeight));
			spCakeOrder.setSpSelectedWeight(spWeight);

			String spNo = "";
			try {
				spNo = getSpNo(frId);
			} catch (Exception e) {
				spNo = spPlace;
				e.printStackTrace();
			}
			spCakeOrder.setSpDeliveryPlace(spNo);
			spCakeOrder.setSpPrice(Float.valueOf(spPrice));
			spCakeOrder.setSpProdDate(sqlProdDate);
			spCakeOrder.setSpProdTime(Integer.parseInt(spProTime));
			spCakeOrder.setSpSubTotal(spSubTotal);
			spCakeOrder.setSpType(spType);

			spCakeOrder.setTax1(tax1);
			spCakeOrder.setTax1Amt(tax1Amt);
			spCakeOrder.setTax2Amt(tax2Amt);
			spCakeOrder.setTax2(tax2);

			spCakeOrder.setMenuId(spMenuId);
			spCakeOrder.setIsSlotUsed(isSlotUsed);
			spCakeOrder.setIsAllocated(0);

			spCakeOrder.setExtraCharges(exCharges);
	
			spCakeOrder.setDisc(disc);
			spCakeOrder.setExVar1(ctype);
			spCakeOrder.setCustEmail(custEmail);
			spCakeOrder.setCustGstin(custGstNo);
			spCakeOrder.setSpCustMobNo(spCustMobNo);
			spCakeOrder.setSlipNo("0");// slipNo

			float intAddonRatePerKG = (dbAdonRate * 0.8f);
			float extraCharges = (exCharges * 0.8f);
			float floatBackEndRate = ((backendSpRate + intAddonRatePerKG) * spWeight) + extraCharges;
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			spCakeOrder.setSpBackendRate(floatBackEndRate);
			try {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Content-Type", "application/json");
				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = mapper.writeValueAsString(spCakeOrder);

				HttpEntity<String> httpEntity = new HttpEntity<String>(jsonInString.toString(), httpHeaders);
				spCakeOrderRes = restTemplate.postForObject(Constants.url + "/placeSpCakeOrder", httpEntity,
						SpCakeOrderRes.class);
				
				System.out.println("ORDER PLACED " + spCakeOrderRes.toString());
				spCakeOrder.setSpInstructions(spCakeOrderRes.getSpCakeOrder().getSpInstructions());
				
				SpCakeOrder spCake = spCakeOrderRes.getSpCakeOrder();
				
				if (spCakeOrderRes.getErrorMessage().isError() != true) {
					
					System.out.println("ORDER PLACED " + spCakeOrderRes.toString());
					map = new LinkedMultiValueMap<String, Object>();
					map = new LinkedMultiValueMap<String, Object>();

					map.add("frId", frDetails.getFrId());
					FrSetting frSetting = restTemplate.postForObject(Constants.url + "getFrSettingValue", map,
							FrSetting.class);

					int sellBillNo = frSetting.getSellBillNo();

					sellBillNo = sellBillNo + 1;

					map = new LinkedMultiValueMap<String, Object>();

					map.add("frId", frId);
					map.add("sellBillNo", sellBillNo);

					Info info = restTemplate.postForObject(Constants.url + "updateFrSettingBillNo", map, Info.class);

					map = new LinkedMultiValueMap<String, Object>();
					map.add("frId", frDetails.getFrId());

					Info updateFrSettingGrnGvnNo = restTemplate.postForObject(Constants.url + "updateFrSettingSpNo",
							map, Info.class);
					if(billClick==1) {
					try {
						PostBillDataCommon postBillDataCommon = new PostBillDataCommon();
						List<PostBillHeader> postBillHeaderList = new ArrayList<PostBillHeader>();
						List<PostBillDetail> postBillDetailsList = new ArrayList();
						PostBillHeader header = new PostBillHeader();
						header.setBillDate(new Date());
						header.setPartyAddress(frDetails.getFrAddress());
						header.setPartyGstin(gstNo);
						header.setPartyName(frName);
						header.setFrCode(frDetails.getFrCode());
						header.setDelStatus(0);
						header.setFrId(frDetails.getFrId());

						float sumTaxableAmt = 0, sumTotalTax = 0, sumGrandTotal = 0;
						float sumDiscAmt = 0;

						PostBillDetail billDetail = new PostBillDetail();

						Float orderRate = floatBackEndRate;//backend rate
						Float baseRate = (orderRate * 100) / (100 + (tax1 + tax2));
						baseRate = roundUp(baseRate);
						Float taxableAmt = baseRate;

						float sgstRs = (taxableAmt * tax1) / 100;
						float cgstRs = (taxableAmt * tax2) / 100;
						float igstRs = (taxableAmt * (tax1 + tax2)) / 100;
						Float totalTax = sgstRs + cgstRs;
						float discAmt = 0;
						float discPer = 0.0f;
						
						if (frDetails.getIsSameState() == 1) {
							baseRate = (orderRate * 100) / (100 + (tax1 + tax2));
							taxableAmt=baseRate;
							// ----------------------------------------------------------
							discAmt = ((taxableAmt * discPer) / 100); // new row added
							System.out.println("discAmt: " + discAmt);// new row added
							sumDiscAmt = sumDiscAmt + discAmt;

							taxableAmt = taxableAmt - discAmt; // new row added
							// ----------------------------------------------------------
							sgstRs = (taxableAmt * tax1) / 100;
							cgstRs = (taxableAmt * tax2) / 100;
							igstRs = 0;
							totalTax = sgstRs + cgstRs;
						}
						else {
							baseRate =(orderRate * 100) / (100 + (tax1 + tax2));
							taxableAmt =baseRate;
							// ----------------------------------------------------------
							discAmt = ((taxableAmt * discPer) / 100); // new row added
							System.out.println("discAmt: " + discAmt);// new row added
							sumDiscAmt = sumDiscAmt + discAmt;

							taxableAmt = taxableAmt - discAmt; // new row added
							// ----------------------------------------------------------
							sgstRs = 0;
							cgstRs = 0;
							igstRs = (taxableAmt * (tax1 + tax2)) / 100;
							totalTax = igstRs;
						}
						sgstRs = roundUp(sgstRs);
						cgstRs = roundUp(cgstRs);
						igstRs = roundUp(igstRs);
						totalTax = roundUp(totalTax);

						Float grandTotal = totalTax + taxableAmt;
						grandTotal = roundUp(grandTotal);

						sumTaxableAmt = sumTaxableAmt + taxableAmt;
						sumTaxableAmt = roundUp(sumTaxableAmt);

						sumTotalTax = sumTotalTax + totalTax;
						sumTotalTax = roundUp(sumTotalTax);

						sumGrandTotal = sumGrandTotal + grandTotal;
						sumGrandTotal = roundUp(sumGrandTotal);

						billDetail.setOrderId(spCake.getSpOrderNo());//Sp Order No
						billDetail.setMenuId(spMenuId);
						billDetail.setCatId(5);
						billDetail.setItemId(spId);
						billDetail.setOrderQty(1);
						billDetail.setBillQty(1);

						if (frDetails.getFrRateCat() == 1) {
							billDetail.setMrp(specialCake.getMrpRate1());
						} else {
							billDetail.setMrp(specialCake.getMrpRate3());
						}
						billDetail.setRateType(frDetails.getFrRateCat());
						billDetail.setRate(floatBackEndRate);
						billDetail.setBaseRate(roundUp(baseRate));
						billDetail.setTaxableAmt(roundUp(taxableAmt));
						billDetail.setDiscPer(discPer);// new
						billDetail.setRemark("" + discAmt);// new
						billDetail.setSgstPer(tax1);
						billDetail.setSgstRs(sgstRs);
						billDetail.setCgstPer(tax2);
						billDetail.setCgstRs(cgstRs);
						billDetail.setIgstPer((tax1 + tax2));
						billDetail.setIgstRs(igstRs);
						billDetail.setTotalTax(totalTax);
						billDetail.setGrandTotal(grandTotal);
						billDetail.setDelStatus(0);
						billDetail.setIsGrngvnApplied(0);
						billDetail.setGrnType(3);// newly added
						billDetail.setHsnCode(Constants.SPHSN);//hardcoded
						header.setSgstSum(billDetail.getSgstRs());
						header.setCgstSum(billDetail.getCgstRs());
						header.setIgstSum(billDetail.getIgstRs());

						DateFormat Df = new SimpleDateFormat("dd-MM-yyyy");
						String deliveryDate = Df.format(header.getBillDate());
						String calculatedDate = incrementDate(deliveryDate, 1);

						if (spMenuId == 88) {
							calculatedDate = incrementDate(deliveryDate, 1);
						}
						Date expiryDate = null;
						try {
							expiryDate = Df.parse(calculatedDate);
						} catch (ParseException e) {

							e.printStackTrace();
						}
						billDetail.setExpiryDate(expiryDate);
						postBillDetailsList.add(billDetail);
						header.setTaxableAmt(sumTaxableAmt);
						header.setGrandTotal(Math.round(sumGrandTotal));
						header.setDiscAmt(sumDiscAmt);
						header.setTotalTax(sumTotalTax);
						header.setTaxApplicable((int)(tax1 + tax2));
						header.setStatus(1);
						header.setBillTime(sdf1.format(calender.getTime()));
						ZoneId zoneId = ZoneId.of("Asia/Calcutta");
						ZonedDateTime zdt = ZonedDateTime.now(zoneId);

						SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss ");
						TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
						Date d = new Date();
						sdf.setTimeZone(istTimeZone);
						String strtime = sdf.format(d);

						DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar cal = Calendar.getInstance();

						header.setRemark(dateFormat1.format(cal.getTime()));
						header.setTime(strtime);
						header.setVehNo("-");
						header.setExVarchar1(sectionId);
						header.setExVarchar2("0");
						header.setPostBillDetailsList(postBillDetailsList);
						postBillHeaderList.add(header);

						postBillDataCommon.setPostBillHeadersList(postBillHeaderList);

						PostBillHeader[] infoRes = restTemplate.postForObject(Constants.url + "insertBillData",
							postBillDataCommon, PostBillHeader[].class);

						System.out.println("Info Data insertBillData response " + infoRes.toString());
						List<PostBillHeader> billRespList = new ArrayList<PostBillHeader>(Arrays.asList(infoRes));

						billNo = billRespList.get(0).getBillNo();
						System.out.println("Save Res Data " + infoRes.toString());
					}catch (Exception e) {
						e.printStackTrace();
					}
					}//bill end 
		        }

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			System.err.println("Exx in insertManualSpBill " + e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/showManualBill";

	}

	public static float roundUp(float d) {
		return BigDecimal.valueOf(d).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public String incrementDate(String date, int day) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));

		} catch (ParseException e) {
			System.out.println("Exception while incrementing date " + e.getMessage());
			e.printStackTrace();
		}
		c.add(Calendar.DATE, day); // number of days to add
		date = sdf.format(c.getTime());

		return date;

	}

	public String getInvoiceNo(int frId, String frCode) {

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		RestTemplate restTemplate = new RestTemplate();

		map.add("frId", frId);
		FrSetting frSetting = restTemplate.postForObject(Constants.url + "getFrSettingValue", map, FrSetting.class);

		int settingValue = frSetting.getSellBillNo();

		System.out.println("Setting Value Received " + settingValue);
		int year = Year.now().getValue();
		String curStrYear = String.valueOf(year);
		curStrYear = curStrYear.substring(2);

		int preMarchYear = Year.now().getValue() - 1;
		String preMarchStrYear = String.valueOf(preMarchYear);
		preMarchStrYear = preMarchStrYear.substring(2);

		System.out.println("Pre MArch year ===" + preMarchStrYear);

		int nextYear = Year.now().getValue() + 1;
		String nextStrYear = String.valueOf(nextYear);
		nextStrYear = nextStrYear.substring(2);

		System.out.println("Next  year ===" + nextStrYear);

		int postAprilYear = nextYear + 1;
		String postAprilStrYear = String.valueOf(postAprilYear);
		postAprilStrYear = postAprilStrYear.substring(2);

		System.out.println("Post April   year ===" + postAprilStrYear);

		java.util.Date date = new Date();
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		int month = cale.get(Calendar.MONTH);

		if (month <= 3) {

			curStrYear = preMarchStrYear + curStrYear;
			System.out.println("Month <= 3::Cur Str Year " + curStrYear);
		} else if (month >= 4) {

			curStrYear = curStrYear + nextStrYear;
			System.out.println("Month >=4::Cur Str Year " + curStrYear);
		}
		int length = String.valueOf(settingValue).length();

		String invoiceNo = null;

		if (length == 1)

			invoiceNo = curStrYear + "-" + "0000" + settingValue;
		if (length == 2)

			invoiceNo = curStrYear + "-" + "000" + settingValue;

		if (length == 3)

			invoiceNo = curStrYear + "-" + "00" + settingValue;

		if (length == 4)

			invoiceNo = curStrYear + "-" + "0" + settingValue;

		invoiceNo = frCode + invoiceNo;
		System.out.println("*** settingValue= " + settingValue);
		return invoiceNo;

	}
	
	
}
