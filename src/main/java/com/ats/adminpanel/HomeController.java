package com.ats.adminpanel;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.commons.DateConvertor;
import com.ats.adminpanel.model.Login;
import com.ats.adminpanel.model.OrderCount;
import com.ats.adminpanel.model.OrderCountsResponse;

import com.ats.adminpanel.model.accessright.ModuleJson;
import com.ats.adminpanel.model.billing.Company;
import com.ats.adminpanel.model.ggreports.GrnGvnReportByGrnType;
import com.ats.adminpanel.model.login.UserResponse;
import com.ats.adminpanel.model.salesdashboard.CatWiseAllData;
import com.ats.adminpanel.model.salesdashboard.CatWiseAmtDisplay;
import com.ats.adminpanel.model.salesdashboard.CatWiseSaleTotal;
import com.ats.adminpanel.model.salesdashboard.FrWiseSaleForDash;
import com.ats.adminpanel.model.salesdashboard.ItemListBySubCat;
import com.ats.adminpanel.model.salesdashboard.RouteWiseSaleForDash;
import com.ats.adminpanel.model.salesdashboard.SubCatListByCat;
import com.ats.adminpanel.model.salesdashboard.TotalAmount;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Handles requests for the application home page.
 */

@Controller
public class HomeController {

	// github testing file changes

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	RestTemplate restTemplate = new RestTemplate();

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView displayLogin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("login");
		logger.info("/ request mapping.");

		return model;

	}

	@RequestMapping(value = "/homenew", method = RequestMethod.GET)
	public ModelAndView displayHome(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("homenew");
		logger.info("/homenew request mapping.");

		return model;

	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView redirectToLogin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("login");
		Login login = new Login();
		Constants.mainAct = 0;
		Constants.subAct = 0;
		model.addObject("login", login);
		return model;

	}

	@RequestMapping(value = "/homeold", method = RequestMethod.GET)
	public ModelAndView redirectToOldHome(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("homeold");

		return model;

	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("index request mapping.");
		return "index";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView showLogin(HttpServletRequest request, HttpServletResponse response) {
		logger.info(" /home request mapping.");

		ModelAndView mav = new ModelAndView("home");
		try {

			RestTemplate restTemplate = new RestTemplate();

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			map.add("cDate", dateFormat.format(new Date()));
			OrderCountsResponse orderCountList = restTemplate.postForObject(Constants.url + "/showOrderCounts", map,
					OrderCountsResponse.class);
			List<OrderCount> orderCounts = new ArrayList<OrderCount>();
			orderCounts = orderCountList.getOrderCount();
			mav.addObject("orderCounts", orderCounts);
			mav.addObject("cDate", dateFormat.format(new Date()));
		} catch (Exception e) {
			System.out.println("HomeController Home Request Page Exception:  " + e.getMessage());
		}

		return mav;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		System.out.println("User Logout");

		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping(value = "/AddNewFranchisee", method = RequestMethod.GET)
	public ModelAndView addNewFranchisee(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Home request mapping.");

		ModelAndView mav = new ModelAndView("franchisee/addnewfranchisee");
		return mav;
	}

	@RequestMapping("/loginProcess")
	public ModelAndView helloWorld(HttpServletRequest request, HttpServletResponse res) throws IOException {

		String name = request.getParameter("username");
		String password = request.getParameter("userpassword");

		ModelAndView mav = new ModelAndView("login");

		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		HttpSession session = request.getSession();

		try {
			System.out.println("Login Process " + name);

			if (name.equalsIgnoreCase("") || password.equalsIgnoreCase("") || name == null || password == null) {

				mav = new ModelAndView("login");
			} else {
				UserResponse userObj = restTemplate.getForObject(
						Constants.url + "/login?username=" + name + "&password=" + password, UserResponse.class);
				try {
					Company company = restTemplate.getForObject(Constants.url + "/getCompany", Company.class);
					Constants.FACTORYNAME = company.getCompName();
					Constants.FACTORYADDRESS = "Address:" + company.getFactAddress() + " ,Phone:"
							+ company.getPhoneNo1();
				} catch (Exception e) {
					e.printStackTrace();
				}
				session.setAttribute("UserDetail", userObj);
				session.setAttribute("userId", userObj.getUser().getId());
				UserResponse userResponse = (UserResponse) session.getAttribute("UserDetail");

				System.out.println("new Field Dept Id = " + userResponse.getUser().getDeptId());
				System.out.println("JSON Response Objet " + userObj.toString());
				String loginResponseMessage = "";

				if (userObj.getErrorMessage().isError() == false) {

					session.setAttribute("userName", name);

					loginResponseMessage = "Login Successful";
					mav.addObject("loginResponseMessage", loginResponseMessage);

					MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
					int userId = userObj.getUser().getId();
					map.add("usrId", userId);
					System.out.println("Before web service");
					try {
						ParameterizedTypeReference<List<ModuleJson>> typeRef = new ParameterizedTypeReference<List<ModuleJson>>() {
						};
						ResponseEntity<List<ModuleJson>> responseEntity = restTemplate.exchange(
								Constants.url + "getRoleJson", HttpMethod.POST, new HttpEntity<>(map), typeRef);

						List<ModuleJson> newModuleList = responseEntity.getBody();

						System.err.println("new Module List " + newModuleList.toString());

						session.setAttribute("newModuleList", newModuleList);
						session.setAttribute("sessionModuleId", 0);
						session.setAttribute("sessionSubModuleId", 0);
						// System.out.println("Role Json "+Commons.newModuleList.toString());
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

					mav = new ModelAndView("home");
					map = new LinkedMultiValueMap<String, Object>();
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

					map.add("cDate", dateFormat.format(new Date()));
					OrderCountsResponse orderCountList = restTemplate.postForObject(Constants.url + "/showOrderCounts",
							map, OrderCountsResponse.class);
					List<OrderCount> orderCounts = new ArrayList<OrderCount>();
					orderCounts = orderCountList.getOrderCount();
					mav.addObject("orderCounts", orderCounts);
					mav.addObject("cDate", dateFormat.format(new Date()));
					System.out.println("menu list ==" + orderCounts.toString());
					System.out.println("order count tile -" + orderCounts.get(0).getMenuTitle());
					System.out.println("order  count -" + orderCounts.get(0).getTotal());
				} else {

					mav = new ModelAndView("login");

					loginResponseMessage = "Invalid Login Credentials";
					mav.addObject("loginResponseMessage", loginResponseMessage);

					System.out.println("Invalid login credentials");

				}

			}
		} catch (Exception e) {
			System.out.println("HomeController Login API Excep:  " + e.getMessage());
		}

		return mav;

	}

	@RequestMapping(value = "/searchOrdersCount", method = RequestMethod.POST)
	public ModelAndView searchOrdersCount(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home");

		try {
			String date = request.getParameter("from_datepicker");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("cDate", date);
			OrderCountsResponse orderCountList = restTemplate.postForObject(Constants.url + "/showOrderCounts", map,
					OrderCountsResponse.class);
			List<OrderCount> orderCounts = new ArrayList<OrderCount>();
			orderCounts = orderCountList.getOrderCount();
			mav.addObject("orderCounts", orderCounts);
			mav.addObject("cDate", date);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@ExceptionHandler(LoginFailException.class)
	public String redirectToLogin() {
		System.out.println("HomeController Login Fail Excep:");

		return "login";
	}

	@RequestMapping(value = "/showCommingSoon", method = RequestMethod.GET)
	public ModelAndView showCommingSoon(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("commingSoon");
		logger.info("/ request mapping.");

		return model;

	}

	@RequestMapping(value = "/sessionTimeOut", method = RequestMethod.GET)
	public ModelAndView displayLoginAgain(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView("login");

		logger.info("/login request mapping.");

		model.addObject("loginResponseMessage", "Session timeout ! Please login again . . .");

		return model;

	}

	@RequestMapping(value = "/setSubModId", method = RequestMethod.GET)
	public @ResponseBody void setSubModId(HttpServletRequest request, HttpServletResponse response) {
		int subModId = Integer.parseInt(request.getParameter("subModId"));
		int modId = Integer.parseInt(request.getParameter("modId"));
		HttpSession session = request.getSession();
		session.setAttribute("sessionModuleId", modId);
		session.setAttribute("sessionSubModuleId", subModId);
		session.removeAttribute("exportExcelList");
	}

	// SALES DASHBOARD - 16-4-2020

	@RequestMapping(value = "/salesDashboard", method = RequestMethod.GET)
	public ModelAndView salesDashboard(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("salesDashboard");

		try {

			String radioVal = request.getParameter("rdDate");
			System.err.println("SEL VAL = " + radioVal);

			String date = request.getParameter("from_datepicker");

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("cDate", date);
			OrderCountsResponse orderCountList = restTemplate.postForObject(Constants.url + "/showOrderCounts", map,
					OrderCountsResponse.class);
			List<OrderCount> orderCounts = new ArrayList<OrderCount>();
			orderCounts = orderCountList.getOrderCount();
			mav.addObject("orderCounts", orderCounts);

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			mav.addObject("cDate", dateFormat.format(new Date()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	List<CatWiseAmtDisplay> dataList = new ArrayList<>();
	List<RouteWiseSaleForDash> routeList = new ArrayList<>();
	List<FrWiseSaleForDash> frList = new ArrayList<>();

	@RequestMapping(value = "/getSalesDashboardData", method = RequestMethod.GET)
	public ModelAndView getSalesDashboardData(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("salesDashboard");

		try {

			int radioVal = 1;
			if (request.getParameter("rdDate") != null) {
				radioVal = Integer.parseInt(request.getParameter("rdDate"));
			}
			System.err.println("SEL VAL = " + radioVal);

			String fromDate = "";
			String toDate = "";

			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			if (radioVal == 1) {

				fromDate = sdf.format(cal.getTime());
				toDate = sdf.format(cal.getTime());

			} else if (radioVal == 2) {

				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

				fromDate = sdf.format(cal.getTime());

				for (int i = 0; i < 6; i++) {
					cal.add(Calendar.DATE, 1);
				}
				toDate = sdf.format(cal.getTime());

			} else if (radioVal == 3) {

				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
				fromDate = sdf.format(cal.getTime());

				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				toDate = sdf.format(cal.getTime());

			} else if (radioVal == 4) {

				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");

			}

			System.err.println("FROM DATE - " + fromDate + "       TO DATE - " + toDate);
			
			mav.addObject("cDate", sdf.format(cal.getTime()));
			mav.addObject("fromDate", fromDate);
			mav.addObject("toDate", toDate);
			mav.addObject("radio", radioVal);

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("fromDate", fromDate);
			map.add("toDate", toDate);

			TotalAmount totalAmt = restTemplate.postForObject(Constants.url + "/getSalesDashAmt", map,
					TotalAmount.class);

			mav.addObject("totalAmt", totalAmt);

			dataList = new ArrayList<>();
			dataList.clear();

			ParameterizedTypeReference<List<CatWiseSaleTotal>> typeRef = new ParameterizedTypeReference<List<CatWiseSaleTotal>>() {
			};
			ResponseEntity<List<CatWiseSaleTotal>> responseEntity1 = restTemplate
					.exchange(Constants.url + "getCatWiseSaleData", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			List<CatWiseSaleTotal> sale = responseEntity1.getBody();

			ResponseEntity<List<CatWiseSaleTotal>> responseEntity2 = restTemplate
					.exchange(Constants.url + "getCatWiseCRNData", HttpMethod.POST, new HttpEntity<>(map), typeRef);

			List<CatWiseSaleTotal> crn = responseEntity2.getBody();

			ResponseEntity<List<CatWiseSaleTotal>> responseEntity3 = restTemplate
					.exchange(Constants.url + "getCatWiseNetData", HttpMethod.POST, new HttpEntity<>(map), typeRef);
			List<CatWiseSaleTotal> net = responseEntity3.getBody();

			if (sale != null) {
				for (int i = 0; i < sale.size(); i++) {

					CatWiseAmtDisplay model = new CatWiseAmtDisplay();
					model.setCatId(sale.get(i).getCatId());
					model.setCatName(sale.get(i).getCatName());
					model.setSale(sale.get(i).getTotal());

					for (int j = 0; j < crn.size(); j++) {
						if (sale.get(i).getCatId() == crn.get(j).getCatId()) {
							model.setCrn(crn.get(j).getTotal());
							break;

						}

					}

					for (int j = 0; j < net.size(); j++) {
						if (sale.get(i).getCatId() == net.get(j).getCatId()) {
							model.setNet(net.get(j).getTotal());
							break;
						}

					}

					dataList.add(model);

				}
			}
			
			
			//FR AND ROUTE DATA
			
			routeList=new ArrayList<>();
			routeList.clear();
			
			frList=new ArrayList<>();
			frList.clear();
			
			ParameterizedTypeReference<List<RouteWiseSaleForDash>> typeRef1 = new ParameterizedTypeReference<List<RouteWiseSaleForDash>>() {
			};
			ResponseEntity<List<RouteWiseSaleForDash>> response1 = restTemplate
					.exchange(Constants.url + "getRouteWiseSaleData", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

			routeList = response1.getBody();
			System.err.println("ROUTE - "+routeList);
			
			mav.addObject("routeList", routeList);
			
			
			ParameterizedTypeReference<List<FrWiseSaleForDash>> typeRef2 = new ParameterizedTypeReference<List<FrWiseSaleForDash>>() {
			};
			ResponseEntity<List<FrWiseSaleForDash>> response2 = restTemplate
					.exchange(Constants.url + "getFrWiseSaleData", HttpMethod.POST, new HttpEntity<>(map), typeRef2);

			frList = response2.getBody();
			System.err.println("FR - "+frList);
			
			mav.addObject("frList", frList);

			
			int radioPieChartFilter = 1;
			if (request.getParameter("rdPie") != null) {
				radioPieChartFilter = Integer.parseInt(request.getParameter("rdPie"));
			}
			mav.addObject("radioPie", radioPieChartFilter);
			
			int radioFrRouteChart = 1;
			if (request.getParameter("rdSale") != null) {
				radioFrRouteChart = Integer.parseInt(request.getParameter("rdSale"));
			}
			mav.addObject("radioFrRoute", radioFrRouteChart);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/getFrGraphData", method = RequestMethod.GET)
	public @ResponseBody List<FrWiseSaleForDash> getFrGraphData(HttpServletRequest request,
			HttpServletResponse response) {
		System.err.println("DATA - " + frList);
		return frList;
	}
	
	@RequestMapping(value = "/getFrGraphDataByRoute", method = RequestMethod.GET)
	public @ResponseBody List<FrWiseSaleForDash> getFrGraphDataByRoute(HttpServletRequest request,
			HttpServletResponse response) {
		
		int routeId = Integer.parseInt(request.getParameter("routeId"));
		System.err.println("DATA - " + frList);
		
		List<FrWiseSaleForDash> frListByRoute=new ArrayList<>();
		
		if(frList!=null) {
			
			for(int i=0;i<frList.size();i++) {
				
				if(frList.get(i).getRouteId()==routeId) {
					frListByRoute.add(frList.get(i));
				}
				
			}
			
		}
		
		return frListByRoute;
	}
	
	@RequestMapping(value = "/getRouteGraphData", method = RequestMethod.GET)
	public @ResponseBody List<RouteWiseSaleForDash> getRouteGraphData(HttpServletRequest request,
			HttpServletResponse response) {
		System.err.println("DATA - " + routeList);
		return routeList;
	}
	
	
	@RequestMapping(value = "/getCatGraphData", method = RequestMethod.GET)
	public @ResponseBody List<CatWiseAmtDisplay> getCatGraphData(HttpServletRequest request,
			HttpServletResponse response) {
		System.err.println("DATA - " + dataList);
		return dataList;
	}
	
	List<SubCatListByCat> data=new ArrayList<>();
	
	@RequestMapping(value = "/getSubCatDataByCat", method = RequestMethod.GET)
	public @ResponseBody List<SubCatListByCat> getSubCatDataByCat(HttpServletRequest request,
			HttpServletResponse response) {
		
		int radioVal = Integer.parseInt(request.getParameter("radio"));
		System.err.println("RADIO VAL - "+radioVal);
		String fromDate = "";
		String toDate = "";

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (radioVal == 1) {

			fromDate = sdf.format(cal.getTime());
			toDate = sdf.format(cal.getTime());

		} else if (radioVal == 2) {

			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			fromDate = sdf.format(cal.getTime());

			for (int i = 0; i < 6; i++) {
				cal.add(Calendar.DATE, 1);
			}
			toDate = sdf.format(cal.getTime());

		} else if (radioVal == 3) {

			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			fromDate = sdf.format(cal.getTime());

			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			toDate = sdf.format(cal.getTime());

		} else if (radioVal == 4) {

			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");

		}
		
		//String fromDate = request.getParameter("fromDate");
		//String toDate = request.getParameter("toDate");
		int catId=Integer.parseInt(request.getParameter("catId"));
		
		System.err.println("FROM - "+fromDate+"      TO - "+toDate+"          CAT ID - "+catId);
		
		data=new ArrayList<>();
		data.clear();
		
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		map.add("fromDate", fromDate);
		map.add("toDate", toDate);
		map.add("catId", catId);
		
		ParameterizedTypeReference<List<SubCatListByCat>> typeRef = new ParameterizedTypeReference<List<SubCatListByCat>>() {
		};
		ResponseEntity<List<SubCatListByCat>> responseEntity1 = restTemplate
				.exchange(Constants.url + "getSubCatDataByCat", HttpMethod.POST, new HttpEntity<>(map), typeRef);

		data = responseEntity1.getBody();
		
		System.err.println("SUB CAT LIST - "+data);
		
		return data;
	}
	
	@RequestMapping(value = "/getSubCatDataByCatForFilter", method = RequestMethod.GET)
	public @ResponseBody List<SubCatListByCat> getSubCatDataByCatForFilter(HttpServletRequest request,
			HttpServletResponse response) {
		
		System.err.println("SUB CAT LIST - "+data);
		
		return data;
	}
	
	
	
	List<ItemListBySubCat> itemData=new ArrayList<>();
	@RequestMapping(value = "/getItemListBySubCat", method = RequestMethod.GET)
	public @ResponseBody List<ItemListBySubCat> getItemListBySubCat(HttpServletRequest request,
			HttpServletResponse response) {
		
		int radioVal = Integer.parseInt(request.getParameter("radio"));
		System.err.println("RADIO VAL - "+radioVal);
		String fromDate = "";
		String toDate = "";

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (radioVal == 1) {

			fromDate = sdf.format(cal.getTime());
			toDate = sdf.format(cal.getTime());

		} else if (radioVal == 2) {

			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			fromDate = sdf.format(cal.getTime());

			for (int i = 0; i < 6; i++) {
				cal.add(Calendar.DATE, 1);
			}
			toDate = sdf.format(cal.getTime());

		} else if (radioVal == 3) {

			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			fromDate = sdf.format(cal.getTime());

			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			toDate = sdf.format(cal.getTime());

		} else if (radioVal == 4) {

			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");

		}
		
		//String fromDate = request.getParameter("fromDate");
		//String toDate = request.getParameter("toDate");
		int catId=Integer.parseInt(request.getParameter("catId"));
		int subCatId=Integer.parseInt(request.getParameter("subCatId"));
		
		System.err.println("FROM - "+fromDate+"      TO - "+toDate+"          CAT ID - "+catId+"       subCatId - "+subCatId);
		
		itemData=new ArrayList<>();
		itemData.clear();
		
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

		map.add("fromDate", fromDate);
		map.add("toDate", toDate);
		map.add("catId", catId);
		map.add("subCatId", subCatId);
		
		ParameterizedTypeReference<List<ItemListBySubCat>> typeRef = new ParameterizedTypeReference<List<ItemListBySubCat>>() {
		};
		ResponseEntity<List<ItemListBySubCat>> responseEntity1 = restTemplate
				.exchange(Constants.url + "getItemListBySubCat", HttpMethod.POST, new HttpEntity<>(map), typeRef);

		itemData = responseEntity1.getBody();
		
		System.err.println("ITEM LIST - "+itemData);
		
		return itemData;
	}
	
	@RequestMapping(value = "/getItemListBySubCatForFilter", method = RequestMethod.GET)
	public @ResponseBody List<ItemListBySubCat> getItemListBySubCatForFilter(HttpServletRequest request,
			HttpServletResponse response) {
		
		System.err.println("SUB CAT LIST - "+itemData);
		
		return itemData;
	}

}
