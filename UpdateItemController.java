package com.example.demo.app.update;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.domain.model.DepartmentList;
import com.example.demo.domain.model.ItemList;
import com.example.demo.domain.model.PlaceList;
import com.example.demo.domain.service.DepartmentService;
import com.example.demo.domain.service.ItemService;
import com.example.demo.domain.service.PlaceService;

@Controller
@RequestMapping("update/{id}/{action}")
public class UpdateItemController {
	@Autowired
	ItemService itemService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	PlaceService placeService;

	@GetMapping
	public ModelAndView editPage(ModelAndView mvc, @ModelAttribute("itemList") ItemList itemlist,
			@PathVariable("id") int id, @PathVariable("action") String action) {

		List<DepartmentList> departmentList = departmentService.loadDepartmentList();
		List<PlaceList> placeList = placeService.loadPlaceList();
		mvc.addObject("departmentList", departmentList);
		mvc.addObject("placeList", placeList);
		mvc.addObject("action",action);

		if (action.equals("edit")) {
			ItemList item = itemService.loadItem(id);
			mvc.addObject("item", item);
			mvc.setViewName("edit");
		} else {
			mvc.addObject("item",new ItemList());
			mvc.setViewName("edit");
		}
		
		return mvc;
	}

	@PostMapping(params = "update")
	public String editItem(@ModelAttribute("itemList") ItemList item,
			@RequestParam("file") MultipartFile file) {
		itemService.updateItem(item, file);
		System.out.println(item.getSerial());
		return "redirect:/";
	}

	@PostMapping(params = "download")
	public ResponseEntity<Resource> downloadFile(@ModelAttribute("itemList") ItemList item,ModelAndView mvc) {
		System.out.println(item.getFilePath());
		return itemService.downloadFile(item.getFilePath());
	}
	
}
