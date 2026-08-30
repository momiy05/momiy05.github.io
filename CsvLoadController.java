package com.example.demo.app.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("CSVLoader")
public class CsvLoadController {
	@Autowired
	ItemService itemService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	PlaceService placeService;

	@GetMapping
	public ModelAndView csvMain(ModelAndView mvc) {
		mvc.setViewName("/CSV");
		return mvc;
	}

	@PostMapping(params = "csvUpload")
	public ModelAndView csvUpload(@RequestParam("csvUp") MultipartFile file, ModelAndView mvc) {

		List<ItemList> list = itemService.csvInsertItem(file);
		List<DepartmentList> departmentList = departmentService.loadDepartmentList();
		List<PlaceList> placeList = placeService.loadPlaceList();
		mvc.addObject("departmentList", departmentList);
		mvc.addObject("placeList", placeList);
		mvc.addObject("itemList", list);
		mvc.setViewName("/CSV");
		return mvc;
	}
}
