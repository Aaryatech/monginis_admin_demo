package com.ats.adminpanel.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.ats.adminpanel.commons.Constants;
import com.ats.adminpanel.model.FranchiseForDispatch;
import com.ats.adminpanel.model.PDispatchReport;
import com.ats.adminpanel.model.creditnote.GetCreditNoteReport;
import com.ats.adminpanel.model.franchisee.SubCategory;
import com.ats.adminpanel.model.item.CategoryListResponse;
import com.ats.adminpanel.model.item.Item;
import com.ats.adminpanel.model.item.MCategoryList;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
@Scope("session")
public class DispatchController {

	
	
	@RequestMapping(value = "/getDispatchPdfForDispatch/{billDate}/{menuId}/{routeId}/{selectedCat}/{frId}", method = RequestMethod.GET)
	public void getDispatchPdfForDispatch(@PathVariable String billDate, @PathVariable String menuId, @PathVariable String routeId,
			@PathVariable String selectedCat, @PathVariable String frId, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException {
		RestTemplate restTemplate = new RestTemplate();

		List<PDispatchReport> dispatchReportList = new ArrayList<PDispatchReport>();
		List<FranchiseForDispatch> frNameIdByRouteIdList=null;
		List<SubCategory> subCatAList=null;
		List<Item> itemsList=null;			List<Integer> frListOrdersPresent=new ArrayList<>();
		String convertedDate = "";

		try {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateFormat.parse(billDate));
				cal.add(Calendar.DATE, 1);
				convertedDate = dateFormat.format(cal.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			boolean isAllCatSelected = false;
			String selectedFr = null;

			if (selectedCat.contains("-1")) {
				isAllCatSelected = true;
			} else {
					}
						MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			String strFrIdRouteWise = frId.toString();// prev
														// was
														// sbForRouteFrId.toString();
			selectedFr = strFrIdRouteWise.substring(0, strFrIdRouteWise.length());
			System.out.println("fr Id Route WISE = " + selectedFr + "frId" + frId);
			// -----------------new------------------------
			map.add("frIds", frId);

			FranchiseForDispatch[] frNameId = restTemplate
					.postForObject(Constants.url + "getFranchiseForDispatchByFrIds", map, FranchiseForDispatch[].class);

			 frNameIdByRouteIdList = new ArrayList<>(Arrays.asList(frNameId));

			System.out.println("route wise franchisee " + frNameIdByRouteIdList.toString());
			// ---------------------------------------------
			

			if (isAllCatSelected) {
				map = new LinkedMultiValueMap<String, Object>();

				CategoryListResponse categoryListResponse = restTemplate.getForObject(Constants.url + "showAllCategory",
						CategoryListResponse.class);
				List<MCategoryList> categoryList = categoryListResponse.getmCategoryList();

				StringBuilder cateList = new StringBuilder();
				// List<String> cateList = new ArrayList<>();
				for (MCategoryList mCategoryList : categoryList) {
					cateList = cateList.append(mCategoryList.getCatId().toString() + ",");
					// cateList.add("" + mCategoryList.getCatId());
				}
				System.err.println(cateList);
				String catlist = cateList.toString();
				selectedCat = catlist.substring(0, catlist.length() - 1);
				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);
				map.add("menuId", menuId);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.err.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				SubCategory[] subCatList = restTemplate.getForObject(Constants.url + "getAllSubCatList",
						SubCategory[].class);

				 subCatAList = new ArrayList<SubCategory>(Arrays.asList(subCatList));
				 itemsList=responseEntity1.getBody();
			//	model.addObject("dispatchReportList", dispatchReportList);
			//	model.addObject("frList", frNameIdByRouteIdList);
			//	model.addObject("itemList", responseEntity1.getBody());
			//	model.addObject("subCatList", subCatAList);
			} else {
				System.out.println("selectedCat" + selectedCat.toString());
				System.out.println("selectedFr" + selectedFr.toString());

				map.add("categories", selectedCat);
				map.add("productionDate", billDate);
				map.add("frId", selectedFr);
				map.add("menuId", menuId);

				ParameterizedTypeReference<List<PDispatchReport>> typeRef = new ParameterizedTypeReference<List<PDispatchReport>>() {
				};

				ResponseEntity<List<PDispatchReport>> responseEntity = restTemplate.exchange(
						Constants.url + "getPDispatchItemReport", HttpMethod.POST, new HttpEntity<>(map), typeRef);

				dispatchReportList = responseEntity.getBody();
				System.err.println("dispatchReportList = " + dispatchReportList.toString());

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catIdList", selectedCat);
				ParameterizedTypeReference<List<Item>> typeRef1 = new ParameterizedTypeReference<List<Item>>() {
				};

				ResponseEntity<List<Item>> responseEntity1 = restTemplate.exchange(
						Constants.url + "getItemsByCatIdForDisp", HttpMethod.POST, new HttpEntity<>(map), typeRef1);

				map = new LinkedMultiValueMap<String, Object>();
				map.add("catId", selectedCat);
				ParameterizedTypeReference<List<SubCategory>> typeRef2 = new ParameterizedTypeReference<List<SubCategory>>() {
				};

				ResponseEntity<List<SubCategory>> responseEntity2 = restTemplate.exchange(
						Constants.url + "getSubCatListForDis", HttpMethod.POST, new HttpEntity<>(map), typeRef2);
				
				 subCatAList=responseEntity2.getBody();
				 itemsList=responseEntity1.getBody();

		//		model.addObject("dispatchReportList", dispatchReportList);
		//		model.addObject("frList", frNameIdByRouteIdList);
		//		model.addObject("itemList", responseEntity1.getBody());
		//		model.addObject("subCatList", responseEntity2.getBody());
			}
			
		//	model.addObject("convertedDate", convertedDate);
		//	model.addObject("FACTORYNAME", Constants.FACTORYNAME);
		//	model.addObject("FACTORYADDRESS", Constants.FACTORYADDRESS);
			List<Integer> frList = Stream.of(selectedFr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			for(int l=0;l<frList.size();l++)
			{
				int flag=0;
				for(int m=0;m<dispatchReportList.size();m++)
				{
					if(dispatchReportList.get(m).getFrId()==frList.get(l))
					{
						flag=1;
						break;
					}
				}
				if(flag==1)
				{
					frListOrdersPresent.add(frList.get(l));
				}
			}
			
			
			//model.addObject("frListSelected", frListOrdersPresent);

		} catch (Exception e) {
			System.out.println("get Dispatch Report Exception: " + e.getMessage());
			e.printStackTrace();

		}

		BufferedOutputStream outStream = null;
	    Document document = new Document(PageSize.A4);
	  
		String FILE_PATH = Constants.REPORT_SAVE;
		File file = new File(FILE_PATH);

		PdfWriter writer = null;

		FileOutputStream out = new FileOutputStream(FILE_PATH);
		try {
			writer = PdfWriter.getInstance(document, out);
		} catch (DocumentException e) {

			e.printStackTrace();
		}
		 document.open();		
		 
		 try {
			   for(int i=0;i<frListOrdersPresent.size();i++) {
				   
					PdfPTable table = new PdfPTable(4);
					table.setHeaderRows(1);

						table.setWidthPercentage(100);
						table.setWidths(new float[] { 0.4f, 4.0f, 1.9f, 1.7f});
						Font headFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
						Font headFont1 = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
						Font f = new Font(FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLUE);
						Font f1 = new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL, BaseColor.BLACK);
						PdfPCell hcell = new PdfPCell();
					
					
						hcell = new PdfPCell(new Phrase("Sr.", headFont1));
						hcell.setPadding(5);
						hcell.setBackgroundColor(BaseColor.PINK);
						hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(hcell);

						hcell = new PdfPCell(new Phrase("ITEM NAME", headFont1));
						hcell.setBackgroundColor(BaseColor.PINK);
						hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						hcell.setPadding(5);
						table.addCell(hcell);

						hcell = new PdfPCell(new Phrase("DISPATCH QTY", headFont1));
						hcell.setPadding(5);
						hcell.setBackgroundColor(BaseColor.PINK);
						hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(hcell);

						hcell = new PdfPCell(new Phrase("SENT", headFont1));
						hcell.setPadding(5);
						hcell.setBackgroundColor(BaseColor.PINK);
						hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(hcell);


						  for(int j=0;j<subCatAList.size();j++) {
							  int flagnew=0;
							  int srNo=1;
							  for(int k=0;k<itemsList.size();k++) {
								if(itemsList.get(k).getItemGrp2()==subCatAList.get(j).getSubCatId())
								{
									int editQty=0;
									
									for(int l=0;l<frNameIdByRouteIdList.size();l++) {
										
										if(frNameIdByRouteIdList.get(l).getFrId()==frListOrdersPresent.get(i))
										{
											for(int m=0;m<dispatchReportList.size();m++) {
												
												
												if(dispatchReportList.get(m).getItemId()==itemsList.get(k).getId())
												{
													if(dispatchReportList.get(m).getFrId()==frNameIdByRouteIdList.get(l).getFrId())
													{
														editQty=dispatchReportList.get(m).getEditQty();
													}
													
												}
												
												
											}
										}
										
									}
									
									if(editQty>0)
									{
										if(flagnew==0)
										{
											PdfPCell cell;

											cell = new PdfPCell(new Phrase("", headFont));
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setHorizontalAlignment(Element.ALIGN_CENTER);
											cell.setPadding(4);
											table.addCell(cell);

											cell = new PdfPCell(new Phrase("" +subCatAList.get(j).getSubCatName() , headFont));
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setHorizontalAlignment(Element.ALIGN_LEFT);
											cell.setPaddingRight(2);
											cell.setPadding(4);
											table.addCell(cell);

											cell = new PdfPCell(new Phrase("", headFont));
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setHorizontalAlignment(Element.ALIGN_CENTER);
											cell.setPaddingRight(2);
											cell.setPadding(4);
											table.addCell(cell);

											cell = new PdfPCell(new Phrase("", headFont));
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
											cell.setHorizontalAlignment(Element.ALIGN_LEFT);
											cell.setPaddingRight(2);
											cell.setPadding(4);
											table.addCell(cell);
											
											flagnew=1;
										}
										
										PdfPCell cell;

										cell = new PdfPCell(new Phrase(""+srNo, headFont));
										cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
										cell.setHorizontalAlignment(Element.ALIGN_CENTER);
										cell.setPadding(4);
										table.addCell(cell);

										srNo=srNo+1;
										
										cell = new PdfPCell(new Phrase("" +itemsList.get(k).getItemName() , headFont));
										cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
										cell.setHorizontalAlignment(Element.ALIGN_LEFT);
										cell.setPaddingRight(2);
										cell.setPadding(4);
										table.addCell(cell);

										cell = new PdfPCell(new Phrase(""+editQty, headFont));
										cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
										cell.setHorizontalAlignment(Element.ALIGN_CENTER);
										cell.setPaddingRight(2);
										cell.setPadding(4);
										table.addCell(cell);

										cell = new PdfPCell(new Phrase("", headFont));
										cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
										cell.setHorizontalAlignment(Element.ALIGN_LEFT);
										cell.setPaddingRight(2);
										cell.setPadding(4);
										table.addCell(cell);
										
									}
									
									
									
								}
							  }
						  }
						 
						
					

						DateFormat DF = new SimpleDateFormat("dd-MM-yyyy");
						String reportDate = DF.format(new Date());
						for(int l=0;l<frNameIdByRouteIdList.size();l++) {
							if(frNameIdByRouteIdList.get(l).getFrId()==frListOrdersPresent.get(i)) {
								
								Paragraph company = new Paragraph("MONGINIS -- SHOP NAME :--"+frNameIdByRouteIdList.get(l).getFrName() + "\n", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.MAGENTA));
								company.setAlignment(Element.ALIGN_CENTER);
								document.add(company);
								Paragraph  header=new Paragraph(" Dispatch Sheet    Dispatch Date:  "+billDate,f1);
					        	document.add(header);
							}
						}document.add(new Paragraph("\n"));
						document.add(table);
						document.add(new Paragraph("\n"));

						int totalPages = writer.getPageNumber();

						System.out.println("Page no " + totalPages);
						document.newPage();
				    	}
						document.close();
						// Atul Sir code to open a Pdf File
						if (file != null) {

							String mimeType = URLConnection.guessContentTypeFromName(file.getName());

							if (mimeType == null) {

								mimeType = "application/pdf";

							}

							response.setContentType(mimeType);

							response.addHeader("content-disposition", String.format("inline; filename=\"%s\"", file.getName()));

							response.setContentLength((int) file.length());

							InputStream inputStream = null;
							try {
								inputStream = new BufferedInputStream(new FileInputStream(file));
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							try {
								FileCopyUtils.copy(inputStream, response.getOutputStream());
							} catch (IOException e) {
								System.out.println("Excep in Opening a Pdf File");
								e.printStackTrace();
							}
						}

					} catch (DocumentException ex) {

						System.out.println("Pdf Generation Error: BOm Prod  View Prod" + ex.getMessage());

						ex.printStackTrace();

					}

	}

}
