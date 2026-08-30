package com.example.demo.app.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.domain.model.DepartmentList;
import com.example.demo.domain.model.ItemList;
import com.example.demo.domain.model.PlaceList;
import com.example.demo.domain.service.DepartmentService;
import com.example.demo.domain.service.ItemService;
import com.example.demo.domain.service.PlaceService;

@Controller
@RequestMapping("QRcode")
public class QrCodeController {

	@Autowired
	ItemService itemService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	PlaceService placeService;

	
	@GetMapping
	public ModelAndView qrReader(ModelAndView mvc,Pageable pageable) {
		List<ItemList> itemList = itemService.loadItemList();
		List<DepartmentList> departmentList = departmentService.loadDepartmentList();
		List<PlaceList> placeList = placeService.loadPlaceList();
		
		ItemList item = new ItemList();
		System.out.println("222222222222222");
		mvc.addObject("item",item);
		mvc.addObject("itemList", itemList);
		mvc.addObject("departmentList", departmentList);
		mvc.addObject("placeList", placeList);
		mvc.setViewName("QR");
		return mvc;
	}
	@GetMapping(params = "download")
	public ResponseEntity<Resource> downloadFile(@ModelAttribute("item") ItemList item) {
		System.out.println(item.getFilePath());
		return itemService.downloadFile(item.getFilePath());
	}
}
