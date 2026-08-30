package com.example.demo.app.item;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.domain.model.DepartmentList;
import com.example.demo.domain.model.ItemList;
import com.example.demo.domain.model.PlaceList;
import com.example.demo.domain.service.DepartmentService;
import com.example.demo.domain.service.ItemService;
import com.example.demo.domain.service.PlaceService;

@Controller
@RequestMapping("/")
public class ItemListController {

	@Autowired
	ItemService itemService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	PlaceService placeService;

	@GetMapping("/")
	ModelAndView ListItems(ModelAndView mvc, Pageable pageable, @RequestParam(required = false) int flg) {
		Page<ItemList> itemList = itemService.loadItemListByFlg(pageable, flg);
		List<DepartmentList> departmentList = departmentService.loadDepartmentList();
		List<PlaceList> placeList = placeService.loadPlaceList();
		mvc.addObject("itemList", itemList);
		mvc.addObject("departmentList", departmentList);
		mvc.addObject("placeList", placeList);
		mvc.addObject("page", itemList.getContent());
		mvc.addObject("order", "");
		mvc.addObject("flg", flg);
		mvc.setViewName("/AssessmentItems");
		return mvc;
	}

	@GetMapping(path = "display")
	String displayItems(@RequestParam(required = false) int flg, @RequestParam(required = false) int... idList) {

		if (Objects.nonNull(idList)) {
			if (flg == 0) {
				for (int hideId : idList) {
					itemService.displayItem(hideId);
				}
			} else {
				for (int hideId : idList) {
					itemService.hideItem(hideId);
				}
			}
		}
		return "redirect:/?flg=0";
	}
	@GetMapping(path = "disposal")
	String disposalItems(@RequestParam(required = false) int... idList) {

		return "redirect:/?flg=0";
	}

	@GetMapping(path = "/sort")
	ModelAndView sortItem(
			ModelAndView mvc,
			@RequestParam(required = false) String option,
			@RequestParam(required = false) String place,
			@RequestParam(required = false) String department,
			Pageable pageable,
			@RequestParam(required = false) int flg) {
		System.out.println("log " + department + " " + option + " " + place);

		String order = "/sort";
		Page<ItemList> list = itemService.loadSortOrderList(option,
				(!Objects.nonNull(department) || department.isEmpty()) ? place : department, pageable, flg);
		List<DepartmentList> departmentList = departmentService.loadDepartmentList();
		List<PlaceList> placeList = placeService.loadPlaceList();
		mvc.addObject("itemList", list);
		mvc.addObject("departmentList", departmentList);
		mvc.addObject("placeList", placeList);
		mvc.addObject("option", option);
		mvc.addObject("place", place);
		mvc.addObject("department", department);
		mvc.addObject("page", list.getContent());
		mvc.addObject("order", order);

		mvc.addObject("flg", flg);
		mvc.setViewName("/AssessmentItems");
		return mvc;
	}

	@GetMapping(path = "/search")
	ModelAndView searchItem(
			ModelAndView mvc,
			@RequestParam(required = false) String like,
			Pageable pageable,
			@RequestParam(required = false) int flg) {
		System.out.println(like);
		Page<ItemList> list = itemService.loadSearchOrderList(like, pageable, flg);
		List<DepartmentList> departmentList = departmentService.loadDepartmentList();
		List<PlaceList> placeList = placeService.loadPlaceList();
		mvc.addObject("itemList", list);
		mvc.addObject("departmentList", departmentList);
		mvc.addObject("placeList", placeList);
		mvc.addObject("like", like);

		mvc.addObject("flg", flg);
		mvc.addObject("page", list.getContent());
		mvc.setViewName("/AssessmentItems");
		return mvc;
	}
}
