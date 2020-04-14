package com.ats.adminpanel.controller;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.ats.adminpanel.model.AllEventListResponse;
import com.ats.adminpanel.model.SpecialCake;
import com.ats.adminpanel.model.Event;
import com.ats.adminpanel.model.EventNameId;
import com.ats.adminpanel.model.ExportToExcel;
import com.ats.adminpanel.model.Info;
import com.ats.adminpanel.model.SpCake;
import com.ats.adminpanel.model.SpCakeResponse;
import com.ats.adminpanel.model.SpCakeSupplement;
import com.ats.adminpanel.model.RawMaterial.RawMaterialUom;
import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.flavours.AllFlavoursListResponse;
import com.ats.adminpanel.model.flavours.Flavour;
import com.ats.adminpanel.model.franchisee.Menu;
import com.ats.adminpanel.model.masters.AllRatesResponse;
import com.ats.adminpanel.model.masters.GetSpCkSupplement;
import com.ats.adminpanel.model.masters.Rate;
import com.ats.adminpanel.model.mastexcel.SpCakeList;

@Controller
public class SpecialCakeController {
	private static final Logger logger = LoggerFactory.getLogger(SpecialCakeController.class);

	List<Event> eventList = new ArrayList<Event>();

	@RequestMapping(value = "/findEventList", method = RequestMethod.GET)
	public @ResponseBody List<Event> findEventList() {
		try {
			RestTemplate restTemplate = new RestTemplate();

			AllEventListResponse allEventListResponse = restTemplate.getForObject(Constants.url + "showEventList",
					AllEventListResponse.class);

			eventList = allEventListResponse.getEvent();
			System.out.println("Event List" + eventList.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return eventList;
	}

	@RequestMapping(value = "/addSpCake", method = RequestMethod.GET)

	public ModelAndView redirectToAddSpCake(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("addSpCake", "addSpCake", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {

			model = new ModelAndView("spcake/addspcake");
			Constants.mainAct = 1;
			Constants.subAct = 6;
			RestTemplate restTemplate = new RestTemplate();
			try {

				Integer maxSpId = restTemplate.getForObject(Constants.url + "getUniqueSpCode", Integer.class);

				int maxSpIdLenth = String.valueOf(maxSpId).length();
				maxSpIdLenth = 5 - maxSpIdLenth;
				StringBuilder spCode = new StringBuilder("SP");

				for (int i = 0; i < maxSpIdLenth; i++) {
					String j = "0";
					spCode.append(j);
				}
				spCode.append(String.valueOf(maxSpId));
				model.addObject("spCode", spCode);

				AllEventListResponse allEventListResponse = restTemplate.getForObject(Constants.url + "showEventList",
						AllEventListResponse.class);

				eventList = allEventListResponse.getEvent();
				System.out.println("Event List" + eventList.toString());
				model.addObject("eventList", eventList);

				// for rate
				AllRatesResponse allRatesResponse = restTemplate.getForObject(Constants.url + "getAllRates",
						AllRatesResponse.class);

				List<Rate> rateList = new ArrayList<Rate>();
				rateList = allRatesResponse.getRates();
				System.out.println("Rate List" + rateList.toString());
				// model.addObject("rateList", rateList);
				List<RawMaterialUom> rawMaterialUomList = restTemplate
						.getForObject(Constants.url + "rawMaterial/getRmUom", List.class);
				model.addObject("rmUomList", rawMaterialUomList);

			} catch (Exception e) {
				System.out.println("Error in event list display" + e.getMessage());
			}
		}
		return model;
	}

	@RequestMapping(value = "/showSpecialCake", method = RequestMethod.GET)

	public ModelAndView redirectToSpCakeList(HttpServletRequest request, HttpServletResponse response) {

		System.out.println("inside ViewSpCkeList");
		ModelAndView model = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSpecialCake", "showSpecialCake", "1", "0", "0", "0", newModuleList);

		if (view.getError() == true) {

			model = new ModelAndView("accessDenied");

		} else {
			model = new ModelAndView("spcake/spcakelist");
			Constants.mainAct = 1;
			Constants.subAct = 7;
			RestTemplate restTemplate = new RestTemplate();

			try {
				SpCakeResponse spCakeResponse = restTemplate.getForObject(Constants.url + "showSpecialCakeList",
						SpCakeResponse.class);
				System.out.println("SpCake Controller SpCakeList Response " + spCakeResponse.toString());
				List<SpecialCake> specialCakeList = new ArrayList<SpecialCake>();

				specialCakeList = spCakeResponse.getSpecialCake();
				System.out.println("my cake list");

				System.out.println("CakeList=" + specialCakeList.toString());
				System.out.println("--------------------");

				System.out.println("name=" + specialCakeList.get(0).getSpName());
				System.out.println("type=" + specialCakeList.get(0).getSpType());

				model.addObject("specialCakeList", specialCakeList);// 1 object to be used in jsp 2 actual object
				model.addObject("url", Constants.SPCAKE_IMAGE_URL);
				// exportToExcel

				SpCakeList spResponse = restTemplate.getForObject(Constants.url + "tally/getAllExcelSpCake",
						SpCakeList.class);

				List<ExportToExcel> exportToExcelList = new ArrayList<ExportToExcel>();

				ExportToExcel expoExcel = new ExportToExcel();
				List<String> rowData = new ArrayList<String>();

				rowData.add("Sr. No.");
				rowData.add("Id");
				rowData.add("Sp Code");
				rowData.add("Sp Name");
				rowData.add("Category");
				rowData.add("Group1");
				rowData.add("Group2");
				rowData.add("HsnCode");
				rowData.add("UOM");
				rowData.add("ErpLink");
				rowData.add("Rate1");
				rowData.add("Rate2");
				rowData.add("Rate3");
				rowData.add("Mrp1");
				rowData.add("Mrp2");
				rowData.add("Mrp3");
				rowData.add("Sgst %");
				rowData.add("Cgst %");
				rowData.add("Igst %");
				rowData.add("Cess %");

				expoExcel.setRowData(rowData);
				exportToExcelList.add(expoExcel);
				List<com.ats.adminpanel.model.mastexcel.SpecialCake> excelSpCake = spResponse.getSpecialCakeList();
				for (int i = 0; i < excelSpCake.size(); i++) {
					expoExcel = new ExportToExcel();
					rowData = new ArrayList<String>();
					rowData.add("" + (i + 1));
					rowData.add("" + excelSpCake.get(i).getId());
					rowData.add(excelSpCake.get(i).getSpCode());
					rowData.add(excelSpCake.get(i).getItemName());
					rowData.add(excelSpCake.get(i).getItemGroup());
					rowData.add(excelSpCake.get(i).getSubGroup());
					rowData.add(excelSpCake.get(i).getSubSubGroup());
					rowData.add(excelSpCake.get(i).getHsnCode());

					rowData.add(excelSpCake.get(i).getUom());
					rowData.add(excelSpCake.get(i).getErpLinkCode());
					rowData.add("" + excelSpCake.get(i).getSpRate1());
					rowData.add("" + excelSpCake.get(i).getSpRate2());
					rowData.add("" + excelSpCake.get(i).getSpRate3());
					rowData.add("" + excelSpCake.get(i).getMrpRate1());
					rowData.add("" + excelSpCake.get(i).getMrpRate2());
					rowData.add("" + excelSpCake.get(i).getMrpRate3());
					rowData.add("" + excelSpCake.get(i).getSgstPer());
					rowData.add("" + excelSpCake.get(i).getCgstPer());
					rowData.add("" + excelSpCake.get(i).getIgstPer());
					rowData.add("" + excelSpCake.get(i).getCessPer());

					expoExcel.setRowData(rowData);
					exportToExcelList.add(expoExcel);

				}

				session = request.getSession();
				session.setAttribute("exportExcelList", exportToExcelList);
				session.setAttribute("excelName", "spCakeList");

				List<ExportToExcel> exportExcelListDummy = new ArrayList<ExportToExcel>();

				expoExcel = new ExportToExcel();
				rowData = new ArrayList<String>();

				rowData.add("Sp Code");
				rowData.add("Sp Name");
				rowData.add("Sp Type");
				rowData.add("Min. Weight");
				rowData.add("Max. Weight");
				rowData.add("Book before(days)");
				rowData.add("SGST %");
				rowData.add("CGST %");
				rowData.add("IGST %");
				rowData.add("ERP LINK CODE");
				rowData.add("is Used?(1/0)");
				rowData.add("Sp Photo Upload(1/0)");
				rowData.add("Is Time 2 Appli?");
				rowData.add("Base Code");
				rowData.add("Sp Description");
				rowData.add("Order Qty");
				rowData.add("Order Discount");
				rowData.add("Is Cust Choice Cake?(1,0)");
				rowData.add("Is Addon Rate Applicable?(1,0)");
				rowData.add("MRP1");
				rowData.add("MRP2");
				rowData.add("MRP3");
				rowData.add("Rate1");
				rowData.add("Rate2");
				rowData.add("Rate3");

				expoExcel.setRowData(rowData);
				exportExcelListDummy.add(expoExcel);

				session.setAttribute("exportExcelListDummy", exportExcelListDummy);
				session.setAttribute("excelName", "SpecialCakeExcelImportFormat");

			} catch (Exception e) {
				System.out.println("Show Sp Cake List Excep: " + e.getMessage());
			}
		}
		return model;
	}

	/*
	 * @RequestMapping(value = "/showSpecialCake")
	 * 
	 * public ModelAndView redirectToSpecialcakeshow(HttpServletRequest request,
	 * HttpServletResponse response) {
	 * 
	 * ModelAndView model = new ModelAndView("spcake/spcakelist");
	 * 
	 * return model; }
	 */

	@RequestMapping(value = "/uploadSpByFile", method = RequestMethod.POST)
	public String uploadItemsByFile(Model model, @RequestParam("file") MultipartFile excelfile,
			HttpServletRequest request, HttpServletResponse response) {

		try {

			// Creates a workbook object from the uploaded excelfile
			XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
			// Creates a worksheet object representing the first sheet
			XSSFSheet worksheet = workbook.getSheetAt(0);
			// Reads the data in excel file until last row is encountered

			List<SpecialCake> specialCakeRes = new ArrayList<SpecialCake>();

			System.out.println("Last Row Number is " + worksheet.getLastRowNum());

			for (int i = 1; i <= worksheet.getLastRowNum(); i++) {
				// Creates an object for the UserInfo Model

				SpecialCake specialCake = new SpecialCake();

				// Creates an object representing a single row in excel
				XSSFRow row = worksheet.getRow(i);

				// System.err.println("row = " +row.toString());
				// Sets the Read data to the model class
				// user.setId((int) row.getCell(0).getNumericCellValue());
				try {
					specialCake.setSpId(0);

					specialCake.setSpCode(ItemController.getCellValueAsString(row.getCell(0)));

					specialCake.setSpName(ItemController.getCellValueAsString(row.getCell(1)));

					specialCake.setSpType(Integer.parseInt(ItemController.getCellValueAsString(row.getCell(2))));

					specialCake.setSpMinwt(ItemController.getCellValueAsString(row.getCell(3)));

					specialCake.setSpMaxwt(ItemController.getCellValueAsString(row.getCell(4)));

					specialCake.setSpBookb4(ItemController.getCellValueAsString(row.getCell(5)));

					specialCake.setSpImage("");

					specialCake.setSpTax1(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(6))));

					specialCake.setSpTax2(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(7))));

					specialCake.setSpTax3(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(8))));
					specialCake.setSpeIdlist("");
					specialCake.setErpLinkcode(ItemController.getCellValueAsString(row.getCell(9)));
					specialCake.setIsUsed(Integer.parseInt(ItemController.getCellValueAsString(row.getCell(10))));

					specialCake.setSpPhoupload(Integer.parseInt(ItemController.getCellValueAsString(row.getCell(11))));

					specialCake.setTimeTwoappli(Integer.parseInt(ItemController.getCellValueAsString(row.getCell(12))));
					specialCake.setBaseCode(Integer.parseInt(ItemController.getCellValueAsString(row.getCell(13))));
					specialCake.setSpDesc(ItemController.getCellValueAsString(row.getCell(14)));
					specialCake.setOrderQty(Integer.parseInt(ItemController.getCellValueAsString(row.getCell(15))));
					specialCake
							.setOrderDiscount(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(16))));
					specialCake
							.setIsCustChoiceCk(Integer.parseInt(ItemController.getCellValueAsString(row.getCell(17))));
					specialCake.setIsAddonRateAppli(
							Integer.parseInt(ItemController.getCellValueAsString(row.getCell(18))));
					specialCake.setMrpRate1(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(19))));
					specialCake.setMrpRate2(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(20))));
					specialCake.setMrpRate3(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(21))));
					specialCake.setSpRate1(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(22))));
					specialCake.setSpRate2(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(23))));
					specialCake.setSpRate3(Float.parseFloat(ItemController.getCellValueAsString(row.getCell(24))));
					specialCake.setSprId(1);
					specialCakeRes.add(specialCake);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			workbook.close();

			// userService.addListUser(lstUser);

			// model.addAttribute("bean", bean);

			System.out.println("Excel File Arraylist " + specialCakeRes.toString());
			System.out.println("Excel File Arraylist Size: " + specialCakeRes.size());

			RestTemplate rest = new RestTemplate();
			List<SpecialCake> info = rest.postForObject(Constants.url + "/insertSpList", specialCakeRes, List.class);
			System.out.println("Response " + info.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/showSpecialCake";

	}

	@RequestMapping(value = "/addSpCakeProcess", method = RequestMethod.POST)

	public String redirectToLogin56(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("sp_image") List<MultipartFile> file) {
		ModelAndView model = new ModelAndView("spcake/addspcake");

		RestTemplate rest = new RestTemplate();

		String code = request.getParameter("spc_code");

		String name = request.getParameter("spc_name");

		int type = Integer.parseInt(request.getParameter("spc_type"));

		String minwt = request.getParameter("min_weight");

		String maxwt = request.getParameter("max_weight");

		String bookb4 = request.getParameter("book_before");

		String spDesc = request.getParameter("sp_desc");

		int orderQty = Integer.parseInt(request.getParameter("order_qty"));

		float orderDisc = Float.parseFloat(request.getParameter("order_disc"));

		float spRate1 = Float.parseFloat(request.getParameter("sp_rate1"));

		float spRate2 = Float.parseFloat(request.getParameter("sp_rate2"));

		float spRate3 = Float.parseFloat(request.getParameter("sp_rate3"));

		float mrpRate1 = Float.parseFloat(request.getParameter("mrp_rate1"));

		float mrpRate2 = Float.parseFloat(request.getParameter("mrp_rate2"));

		float mrpRate3 = Float.parseFloat(request.getParameter("mrp_rate3"));

		double tx1 = Double.parseDouble(request.getParameter("tax_1"));

		double tx2 = Double.parseDouble(request.getParameter("tax_2"));

		double tx3 = Double.parseDouble(request.getParameter("tax_3"));

		String[] eventtypes = (request.getParameterValues("spe_id_list[]"));

		String[] erplinkcode = request.getParameterValues("erplinkcode");

		int isCustChoiceCk = Integer.parseInt(request.getParameter("is_cust_choice_ck"));

		int isAddonRateAppli = Integer.parseInt(request.getParameter("is_addon_rate_appli"));

		int type2app = Integer.parseInt(request.getParameter("type_2_applicable"));

		int isused = Integer.parseInt(request.getParameter("is_used"));

		int allowphupload = Integer.parseInt(request.getParameter("allowphupload"));

		int isSlotUsed = Integer.parseInt(request.getParameter("isSlotUsed"));

		int noOfChars = Integer.parseInt(request.getParameter("no_of_char"));

		// --------------------------------Sp Supplement Data--------------------
		String spHsncd = "";
		int uomId = 0;
		float spCess = 0.0f;
		String spUom = "";
		int cutSection = 0;
		try {

			spHsncd = request.getParameter("spck_hsncd");

			uomId = Integer.parseInt(request.getParameter("spck_uom"));

			spCess = Float.parseFloat(request.getParameter("sp_cess"));

			spUom = request.getParameter("sp_uom_name");

			cutSection = Integer.parseInt(request.getParameter("cut_section"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ------------------------------------------------------
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < eventtypes.length; i++) {

			sb = sb.append(eventtypes[i] + ",");

		}
		String strEvents = sb.toString();

		strEvents = strEvents.substring(0, strEvents.length() - 1);
		// --------------------------------------------------------
		String strFlavours = "";
		if (erplinkcode != null) {
			StringBuilder flav = new StringBuilder();

			for (int i = 0; i < erplinkcode.length; i++) {

				flav = flav.append(erplinkcode[i] + ",");

			}
			strFlavours = flav.toString();

			strFlavours = strFlavours.substring(0, strFlavours.length() - 1);
		}
		// ---------------------------------------------------------

		VpsImageUpload upload = new VpsImageUpload();

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()));

		String curTimeStamp = sdf.format(cal.getTime());
		String spImage = null;
		spImage = curTimeStamp + "-" + file.get(0).getOriginalFilename();

		try {

			LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			String tempFileName;
			FileOutputStream fo;

			tempFileName = curTimeStamp + "-" + file.get(0).getOriginalFilename();
			fo = new FileOutputStream(tempFileName);
			fo.write(file.get(0).getBytes());
			fo.close();
			map.add("file", new FileSystemResource(tempFileName));
			map.add("imageName", spImage);
			map.add("type", "sp");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
			Info infoRes = rest.postForObject(Constants.url + "/photoUpload", requestEntity, Info.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * upload.saveUploadedFiles(file, Constants.SPCAKE_IMAGE_TYPE, curTimeStamp +
		 * "-" + file.get(0).getOriginalFilename());
		 * System.out.println("upload method called for image Upload " +
		 * file.toString());
		 */

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("spCode", code);
		map.add("spName", name);
		map.add("spType", type);
		map.add("spMinwt", minwt);
		map.add("spMaxwt", maxwt);
		map.add("spBookb4", bookb4);

		map.add("spImage", spImage);
		map.add("spTax1", tx1);
		map.add("spTax2", tx2);
		map.add("spTax3", tx3);
		map.add("speIdlist", strEvents);
		map.add("erpLinkcode", strFlavours);
		map.add("timeTwoappli", type2app);
		map.add("isUsed", isused);
		map.add("spPhoupload", allowphupload);
		map.add("noOfChars", noOfChars);

		map.add("spDesc", spDesc);
		map.add("orderQty", orderQty);
		map.add("orderDiscount", orderDisc);
		map.add("isCustChoiceCk", isCustChoiceCk);
		map.add("isAddonRateAppli", isAddonRateAppli);
		map.add("mrpRate1", mrpRate1);
		map.add("mrpRate2", mrpRate2);
		map.add("mrpRate3", mrpRate3);
		map.add("spRate1", spRate1);
		map.add("spRate2", spRate2);
		map.add("spRate3", spRate3);
		map.add("isSlotUsed", isSlotUsed);

		try {
			SpecialCake spcakeResponse = rest.postForObject(Constants.url + "insertSpecialCake", map,
					SpecialCake.class);
			if (spcakeResponse != null) {

				SpCakeSupplement spCakeSupplement = new SpCakeSupplement();
				spCakeSupplement.setId(0);
				spCakeSupplement.setUomId(uomId);
				spCakeSupplement.setSpId(spcakeResponse.getSpId());// add errorMessage in else
				spCakeSupplement.setSpUom(spUom);
				spCakeSupplement.setSpHsncd(spHsncd);
				spCakeSupplement.setSpCess(spCess);
				spCakeSupplement.setDelStatus(0);
				spCakeSupplement.setIsTallySync(0);
				spCakeSupplement.setCutSection(cutSection);

				RestTemplate restTemplate = new RestTemplate();

				Info info = restTemplate.postForObject(Constants.url + "/saveSpCakeSup", spCakeSupplement, Info.class);

			} else {
				model = new ModelAndView("addSpCake");
			}

		} catch (Exception e) {
			System.out.println("AddSpCakeProcess Excep: " + e.getMessage());
		}

		return "redirect:/showSpecialCake";

	}

	@RequestMapping(value = "/deleteSpecialCake/{spIdList}", method = RequestMethod.GET)
	public String deleteSpecialCake(@PathVariable String[] spIdList) {
		ModelAndView model = new ModelAndView("spcake/spcakelist");
		String strSpIds = new String();
		for (int i = 0; i < spIdList.length; i++) {
			strSpIds = strSpIds + "," + spIdList[i];
		}
		strSpIds = strSpIds.substring(1);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("spId", strSpIds);
		RestTemplate restTemplate = new RestTemplate();
		Info info = restTemplate.postForObject(Constants.url + "deleteSpecialCake", map, Info.class);
		map = new LinkedMultiValueMap<String, Object>();
		map.add("id", strSpIds);
		Info infoSpCk = restTemplate.postForObject(Constants.url + "deleteSpCakeSup", map, Info.class);

		if (info.getError() || infoSpCk.getError()) {
			return "redirect:/showSpecialCake";
		} else {
			return "redirect:/showSpecialCake";

		}

	}

	@RequestMapping(value = "/updateSpCake/{spId}")

	public ModelAndView redirectToshowUpdateSpCake(@PathVariable int spId, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView("spcake/editspcake");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		map.add("spId", spId);
		RestTemplate restTemplate = new RestTemplate();

		SpecialCake specialCake = restTemplate.getForObject(Constants.url + "getSpecialCake?spId={spId}",
				SpecialCake.class, spId);
		try {
			map = new LinkedMultiValueMap<String, Object>();

			map.add("spfId", specialCake.getErpLinkcode());
			List<Flavour> flavoursListSelected = restTemplate.postForObject(Constants.url + "getFlavoursBySpfIdIn", map,
					List.class);
			model.addObject("flavoursListSelected", flavoursListSelected);
			map.add("type", specialCake.getSpType());
			List<Flavour> flavoursListNotSelected = restTemplate
					.postForObject(Constants.url + "getFlavoursBySpfIdNotIn", map, List.class);
			model.addObject("flavoursListNotSelected", flavoursListNotSelected);

		} catch (Exception e) {
			System.out.println("Exc1 In /updateSpSupp" + e.getMessage());
		}
		AllEventListResponse allEventListResponse = restTemplate.getForObject(Constants.url + "showEventList",
				AllEventListResponse.class);

		try {
			map = new LinkedMultiValueMap<String, Object>();
			map.add("id", spId);

			GetSpCkSupplement getSpCkSupplement = restTemplate.postForObject(Constants.url + "/getSpCakeSupplement",
					map, GetSpCkSupplement.class);
			System.out.println("getSpCkSupplement" + getSpCkSupplement.toString());
			List<RawMaterialUom> rawMaterialUomList = restTemplate.getForObject(Constants.url + "rawMaterial/getRmUom",
					List.class);

			model.addObject("rmUomList", rawMaterialUomList);

			model.addObject("spCkSupp", getSpCkSupplement);

		} catch (Exception e) {
			System.out.println("Exc In /updateSpSupp" + e.getMessage());
		}
		List<Event> eventList = new ArrayList<Event>();
		eventList = allEventListResponse.getEvent();

		String strSpeIdList = specialCake.getSpeIdlist();

		List<String> speIdListArray = Arrays.asList(strSpeIdList.split("\\s*,\\s*"));

		List<EventNameId> spePrevEvents = new ArrayList<EventNameId>();

		for (int i = 0; i < speIdListArray.size(); i++) {

			for (int j = 0; j < eventList.size(); j++) {

				if (eventList.get(j).getSpeId().toString().equalsIgnoreCase(speIdListArray.get(i))) {

					EventNameId nameId = new EventNameId();
					nameId.setId(speIdListArray.get(i));
					nameId.setName(eventList.get(j).getSpeName());
					spePrevEvents.add(nameId);
					model.addObject("nameId", nameId);
				}

			}

		}

		model.addObject("specialCake", specialCake);
		model.addObject("spEventsIdList", speIdListArray);
		model.addObject("speEventNameId", spePrevEvents);

		AllRatesResponse allRatesResponse = restTemplate.getForObject(Constants.url + "getAllRates",
				AllRatesResponse.class);

		List<Rate> rateList = new ArrayList<Rate>();
		rateList = allRatesResponse.getRates();

		for (int j = 0; j < speIdListArray.size(); j++) {

			for (int i = 0; i < eventList.size(); i++) {
				if (eventList.get(i).getSpeId().toString().equals(speIdListArray.get(j))) {
					eventList.remove(i);
				}

			}
		}

		String rateName = "";
		int rate = 0;

		for (int j = 0; j < rateList.size(); j++) {

			if (rateList.get(j).getSprId() == specialCake.getSprId()) {

				rateName = rateList.get(j).getSprName();
				rate = rateList.get(j).getSprRate();
			}

		}
		model.addObject("rateName", rateName);
		model.addObject("rate", rate);

		model.addObject("eventList", eventList);

		for (int j = 0; j < rateList.size(); j++) {

			if (rateList.get(j).getSprId() == specialCake.getSprId()) {
				rateList.remove(j);

			}
		}

		model.addObject("rateList", rateList);

		int isSlotUsed = specialCake.getIsSlotUsed();
		String strIsSlotUsed = String.valueOf(isSlotUsed);
		model.addObject("isSlotUsed", strIsSlotUsed);

		int timeTwoappli = specialCake.getTimeTwoappli();
		String strTimeTwoappli = String.valueOf(timeTwoappli);
		model.addObject("strTimeTwoappli", strTimeTwoappli);

		int isUsed = specialCake.getIsUsed();
		String strIsUsed = String.valueOf(isUsed);
		model.addObject("strIsUsed", strIsUsed);
		int allowPhUp = specialCake.getSpPhoupload();
		String strallowPhUp = String.valueOf(allowPhUp);
		model.addObject("strallowPhUp", strallowPhUp);

		int isCustChoiceCk = specialCake.getIsCustChoiceCk();
		String strIsCustChoiceCk = String.valueOf(isCustChoiceCk);
		model.addObject("strIsCustChoiceCk", strIsCustChoiceCk);

		int isAddonRateAppli = specialCake.getIsAddonRateAppli();
		String strIsAddonRateAppli = String.valueOf(isAddonRateAppli);
		model.addObject("strIsAddonRateAppli", strIsAddonRateAppli);

		/*
		 * String cakeType=""; int spcName=specialCake.getSpType(); switch(spcName) {
		 * case 1: cakeType="Chocolate"; break; case 2: cakeType="FC"; break; case 0:
		 * cakeType="All"; break; default: cakeType=""; break;
		 * 
		 * } model.addObject("cakeType",cakeType);
		 */
		model.addObject("url", Constants.SPCAKE_IMAGE_URL);
		return model;

	}

	// @RequestMapping(value = "/updateSpCakeProcess")

	@RequestMapping(value = "/updateSpCake/updateSpCakeProcess", method = RequestMethod.POST)

	public String redirectToUpdateSpCakeProcess(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("sp_image") List<MultipartFile> file) {
		try {
			ModelAndView model = new ModelAndView("spcake/editspcake");

			RestTemplate restTemplate = new RestTemplate();

			String code = request.getParameter("spc_code");

			String name = request.getParameter("spc_name");

			int type = Integer.parseInt(request.getParameter("spc_type"));

			String minwt = request.getParameter("min_weight");

			String maxwt = request.getParameter("max_weight");

			String bookb4 = request.getParameter("book_before");

			String spDesc = request.getParameter("sp_desc");

			int orderQty = Integer.parseInt(request.getParameter("order_qty"));

			float orderDisc = Float.parseFloat(request.getParameter("order_disc"));

			float spRate1 = Float.parseFloat(request.getParameter("sp_rate1"));

			float spRate2 = Float.parseFloat(request.getParameter("sp_rate2"));

			float spRate3 = Float.parseFloat(request.getParameter("sp_rate3"));

			float mrpRate1 = Float.parseFloat(request.getParameter("mrp_rate1"));

			float mrpRate2 = Float.parseFloat(request.getParameter("mrp_rate2"));

			float mrpRate3 = Float.parseFloat(request.getParameter("mrp_rate3"));

			int isCustChoiceCk = Integer.parseInt(request.getParameter("is_cust_choice_ck"));

			int isAddonRateAppli = Integer.parseInt(request.getParameter("is_addon_rate_appli"));

			int isSlotUsed = Integer.parseInt(request.getParameter("isSlotUsed"));

			/*
			 * String spimg = request.getParameter("sp_image");
			 * System.out.println(" fr image"+spimg);
			 */
			double tx1 = Double.parseDouble(request.getParameter("tax_1"));

			double tx2 = Double.parseDouble(request.getParameter("tax_2"));

			double tx3 = Double.parseDouble(request.getParameter("tax_3"));

			String[] eventtypes = (request.getParameterValues("spe_id_list[]"));

			String[] erplinkcode = request.getParameterValues("erplinkcode");

			int type2app = Integer.parseInt(request.getParameter("type_2_applicable"));

			int isused = Integer.parseInt(request.getParameter("is_used"));

			int allowphupload = Integer.parseInt(request.getParameter("allowphupload"));

			int id = Integer.parseInt(request.getParameter("spId"));
			int noOfChars = Integer.parseInt(request.getParameter("no_of_char"));

			// --------------------------------Sp Supplement Data--------------------
			int suppId = 0;
			String spHsncd = "";
			int uomId = 0;
			float spCess = 0.0f;
			String spUom = "";
			int cutSection = 0;
			try {

				suppId = Integer.parseInt(request.getParameter("suppId"));

				spHsncd = request.getParameter("spck_hsncd");

				uomId = Integer.parseInt(request.getParameter("spck_uom"));

				spCess = Float.parseFloat(request.getParameter("sp_cess"));

				spUom = request.getParameter("sp_uom_name");

				cutSection = Integer.parseInt(request.getParameter("cut_section"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			// -----------------------------------------------------------------

			StringBuilder sb = new StringBuilder();
			String strEventTypes = "";
			for (int i = 0; i < eventtypes.length; i++) {
				sb = sb.append(eventtypes[i] + ",");

			}
			String strEvents = sb.toString();
			strEvents = strEvents.substring(0, strEvents.length() - 1);

			// --------------------------------------------------------
			String strFlavours = "";
			if (erplinkcode != null) {
				StringBuilder flav = new StringBuilder();

				for (int i = 0; i < erplinkcode.length; i++) {

					flav = flav.append(erplinkcode[i] + ",");

				}
				strFlavours = flav.toString();

				strFlavours = strFlavours.substring(0, strFlavours.length() - 1);
			}
			// ---------------------------------------------------------

			String spImage = request.getParameter("prevImage");

			if (!file.get(0).getOriginalFilename().equalsIgnoreCase("")) {

				System.out.println("Empty image");
				// spImage= ImageS3Util.uploadSpCakeImage(file);

				VpsImageUpload upload = new VpsImageUpload();

				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				System.out.println(sdf.format(cal.getTime()));

				String curTimeStamp = sdf.format(cal.getTime());
				spImage = null;
				spImage = curTimeStamp + "-" + file.get(0).getOriginalFilename();
				/*
				 * try { spImage=curTimeStamp + "-" + file.get(0).getOriginalFilename();
				 * upload.saveUploadedFiles(file, Constants.SPCAKE_IMAGE_TYPE, curTimeStamp +
				 * "-" + file.get(0).getOriginalFilename());
				 * System.out.println("upload method called for image Upload " +
				 * file.toString());
				 * 
				 * } catch (IOException e) {
				 * 
				 * System.out.println("Exce in File Upload In Sp Cake  Insert " +
				 * e.getMessage()); e.printStackTrace(); }
				 */

				try {

					LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
					String tempFileName;
					FileOutputStream fo;

					tempFileName = curTimeStamp + "-" + file.get(0).getOriginalFilename();
					fo = new FileOutputStream(tempFileName);
					fo.write(file.get(0).getBytes());
					fo.close();
					map.add("file", new FileSystemResource(tempFileName));
					map.add("imageName", tempFileName);
					map.add("type", "sp");
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);

					HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
					Info infoRes = restTemplate.postForObject(Constants.url + "/photoUpload", requestEntity,
							Info.class);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// String spImage= ImageS3Util.uploadSpCakeImage(file);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("spCode", code);
			map.add("spname", name);
			map.add("sptype", type);
			map.add("spminwt", minwt);
			map.add("spmaxwt", maxwt);
			map.add("spbookb4", bookb4);
			map.add("spimage", spImage);
			map.add("sptax1", tx1);
			map.add("sptax2", tx2);
			map.add("sptax3", tx3);
			map.add("spidlist", strEvents);
			map.add("erplinkcode", strFlavours);
			map.add("timetwoappli", type2app);
			map.add("isUsed", isused);
			map.add("spphoupload", allowphupload);
			map.add("id", id);
			map.add("noOfChars", noOfChars);
			map.add("spDesc", spDesc);
			map.add("orderQty", orderQty);
			map.add("orderDiscount", orderDisc);
			map.add("isCustChoiceCk", isCustChoiceCk);
			map.add("isAddonRateAppli", isAddonRateAppli);
			map.add("mrpRate1", mrpRate1);
			map.add("mrpRate2", mrpRate2);
			map.add("mrpRate3", mrpRate3);
			map.add("spRate1", spRate1);
			map.add("spRate2", spRate2);
			map.add("spRate3", spRate3);
			map.add("isSlotUsed", isSlotUsed);

			// System.out.println("sp name is "+name);
			// System.out.println("type 2 value in update event"+type2app);

			SpecialCake cakeResponse = restTemplate.postForObject(Constants.url + "updateSpecialCake", map,
					SpecialCake.class);

			try {

				if (cakeResponse != null) {

					SpCakeSupplement spCakeSupplement = new SpCakeSupplement();
					spCakeSupplement.setId(suppId);
					spCakeSupplement.setUomId(uomId);
					spCakeSupplement.setSpId(id);// add errorMessage in else
					spCakeSupplement.setSpUom(spUom);
					spCakeSupplement.setSpHsncd(spHsncd);
					spCakeSupplement.setSpCess(spCess);
					spCakeSupplement.setDelStatus(0);
					spCakeSupplement.setIsTallySync(0);
					spCakeSupplement.setCutSection(cutSection);

					Info info = restTemplate.postForObject(Constants.url + "/saveSpCakeSup", spCakeSupplement,
							Info.class);
					System.out.println("Response: " + info.toString());
				} else {
					model = new ModelAndView("addSpCake");
				}

			} catch (Exception e) {
				System.out.println("AddSpCakeProcess Excep: " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		return "redirect:/showSpecialCake";

	}

	@RequestMapping(value = "/showSpSupplement", method = RequestMethod.GET)
	public ModelAndView showSpSupplement(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = null;
		HttpSession session = request.getSession();

		List<ModuleJson> newModuleList = (List<ModuleJson>) session.getAttribute("newModuleList");
		Info view = AccessControll.checkAccess("showSpSupplement", "showSpSupplement", "1", "0", "0", "0",
				newModuleList);

		if (view.getError() == true) {

			mav = new ModelAndView("accessDenied");

		} else {
			mav = new ModelAndView("spcake/spCakeSupp");
			Constants.mainAct = 1;
			Constants.subAct = 110;

			RestTemplate restTemplate = new RestTemplate();

			try {
				List<GetSpCkSupplement> spSuppList = restTemplate.getForObject(Constants.url + "/getSpCakeSuppList",
						List.class);
				System.out.println("spSuppList" + spSuppList.toString());

				List<SpCake> spList = restTemplate.getForObject(Constants.url + "/getSpCakeList", List.class);
				System.out.println("spList" + spList.toString());

				List<RawMaterialUom> rawMaterialUomList = restTemplate
						.getForObject(Constants.url + "rawMaterial/getRmUom", List.class);
				mav.addObject("rmUomList", rawMaterialUomList);
				mav.addObject("spSuppList", spSuppList);
				mav.addObject("spList", spList);

			} catch (Exception e) {
				System.out.println("Exc In /spSupList" + e.getMessage());
			}
		}
		return mav;

	}

	// ------------------------------ADD SpCakeSup
	// Process------------------------------------
	@RequestMapping(value = "/addSpCakeSupProcess", method = RequestMethod.POST)
	public String addSpCakeSupProcess(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("spcake/spCakeSupp");
		try {

			int id = 0;

			try {
				id = Integer.parseInt(request.getParameter("id"));

			} catch (Exception e) {
				id = 0;
				System.out.println("In Catch of addSpCakeSupProcess Process Exc:" + e.getMessage());

			}

			String spHsncd = request.getParameter("spck_hsncd");
			int spId = Integer.parseInt(request.getParameter("sp_id"));

			int uomId = Integer.parseInt(request.getParameter("spck_uom"));

			float spCess = Float.parseFloat(request.getParameter("sp_cess"));

			String spUom = request.getParameter("sp_uom_name");

			int cutSection = Integer.parseInt(request.getParameter("cut_section"));

			SpCakeSupplement spCakeSupplement = new SpCakeSupplement();
			spCakeSupplement.setId(id);
			spCakeSupplement.setUomId(uomId);
			spCakeSupplement.setSpId(spId);
			spCakeSupplement.setSpUom(spUom);
			spCakeSupplement.setSpHsncd(spHsncd);
			spCakeSupplement.setSpCess(spCess);
			spCakeSupplement.setDelStatus(0);
			spCakeSupplement.setIsTallySync(0);
			spCakeSupplement.setCutSection(cutSection);

			RestTemplate restTemplate = new RestTemplate();

			Info info = restTemplate.postForObject(Constants.url + "/saveSpCakeSup", spCakeSupplement, Info.class);
			System.out.println("Response: " + info.toString());

			if (info.getError() == true) {

				System.out.println("Error:True" + info.toString());
				return "redirect:/showSpSupplement";

			} else {
				return "redirect:/showSpSupplement";
			}

		} catch (Exception e) {

			System.out.println("Exception In Add SpSupp Process:" + e.getMessage());

		}

		return "redirect:/showSpSupplement";
	}

	// ----------------------------------------END---------------------------------------------------
	@RequestMapping(value = "/getFlavoursByType", method = RequestMethod.GET)
	public @ResponseBody List<Flavour> getFlavoursByType(@RequestParam(value = "type", required = true) int type) {

		RestTemplate restTemplate = new RestTemplate();
		List<Flavour> flavoursList = null;
		try {
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("type", type);
			if (type != 0 && type <= 3) {
				flavoursList = restTemplate.postForObject(Constants.url + "getFlavoursByType", map, List.class);
			} else {
				AllFlavoursListResponse allFlavoursListResponse = restTemplate
						.getForObject(Constants.url + "showFlavourList", AllFlavoursListResponse.class);
				flavoursList = allFlavoursListResponse.getFlavour();
				if (type != 0) {
					List<Flavour> flList = new ArrayList<Flavour>();
					for (Flavour flavour : allFlavoursListResponse.getFlavour()) {
						if (type == 4) {
							if (flavour.getSpType() == 1 || flavour.getSpType() == 2) {
								flList.add(flavour);
							}
						} else if (type == 5) {
							if (flavour.getSpType() == 1 || flavour.getSpType() == 3) {
								flList.add(flavour);
							}
						} else if (type == 6) {
							if (flavour.getSpType() == 2 || flavour.getSpType() == 3) {
								flList.add(flavour);
							}
						}
					}
					flavoursList = flList;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flavoursList;
	}
	// ----------------------------------------END---------------------------------------------------

	@RequestMapping(value = "/updateSpSupp/{id}", method = RequestMethod.GET)
	public ModelAndView updateSpSupp(@PathVariable("id") int id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("spcake/spCakeSupp");

		RestTemplate restTemplate = new RestTemplate();

		try {
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("id", id);

			GetSpCkSupplement getSpCkSupplement = restTemplate.postForObject(Constants.url + "/getSpCakeSupp", map,
					GetSpCkSupplement.class);
			System.out.println("getSpCkSupplement" + getSpCkSupplement.toString());
			List<RawMaterialUom> rawMaterialUomList = restTemplate.getForObject(Constants.url + "rawMaterial/getRmUom",
					List.class);
			List<GetSpCkSupplement> spSuppList = restTemplate.getForObject(Constants.url + "/getSpCakeSuppList",
					List.class);

			SpCakeResponse spCakeResponse = restTemplate.getForObject(Constants.url + "showSpecialCakeList",
					SpCakeResponse.class);
			mav.addObject("spSuppList", spSuppList);
			mav.addObject("spList", spCakeResponse.getSpecialCake());

			mav.addObject("rmUomList", rawMaterialUomList);

			mav.addObject("spCkSupp", getSpCkSupplement);

		} catch (Exception e) {
			System.out.println("Exc In /updateSpSupp" + e.getMessage());
		}

		return mav;

	}
}
