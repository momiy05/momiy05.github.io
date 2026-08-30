package com.example.demo.domain.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.example.demo.domain.model.ItemList;
import com.example.demo.domain.repository.ItemRepository;
import com.example.demo.validator.ItemListException;

@Service
public class ItemService {

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	EntityManager entityManager;

	public Page<ItemList> loadItemListByFlg(Pageable pageable, int flg) {
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM item_list_main WHERE (view_flg = " + flg + ") ORDER BY updated ASC;",
				ItemList.class);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<ItemList> list = query.getResultList();
		Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM item_list_main WHERE (view_flg = " + flg + ")");
		long totalCount = ((Number) countQuery.getSingleResult()).longValue();

		return new PageImpl<>(list, pageable, totalCount);
	}
	
	public List<ItemList> loadItemList() {
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM item_list_main ORDER BY updated ASC;",
				ItemList.class);
		List<ItemList> list = query.getResultList();
		return list;
	}
		

	public Page<ItemList> loadSortOrderList(String option, String select, Pageable pageable,int flg) {
		Query query;
		Query countQuery;
		System.out.println(select);
		if (option.equals("updated")) {
			query = entityManager.createNativeQuery(
					"SELECT * FROM item_list_main WHERE (view_flg = " + flg + ") ORDER BY updated ASC;",
					ItemList.class);
			countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM item_list_main WHERE (view_flg = " + flg + ") ");
		} else {
			query = entityManager.createNativeQuery(""
					+ "SELECT * "
					+ "FROM item_list_main "
					+ "WHERE " + option + " = " + select + " AND (view_flg = " + flg + ")"
					+ " ORDER BY updated ASC; ", ItemList.class);
			countQuery = entityManager.createNativeQuery(
					"SELECT COUNT(*) FROM item_list_main WHERE " + option + " = " + select + " AND (view_flg = " + flg + ") ");
		}
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<ItemList> list = query.getResultList();

		long totalCount = ((Number) countQuery.getSingleResult()).longValue();

		return new PageImpl<>(list, pageable, totalCount);
	}

	public Page<ItemList> loadSearchOrderList(String text, Pageable pageable,int flg) {
		Query query = entityManager.createNativeQuery(""
				+ "SELECT * FROM item_list_main WHERE"
				+ " ((name LIKE '%" + text.replaceAll(" ", "") + "%') OR"
				+ " (serial LIKE '%" + text.replaceAll(" ", "") + "%') OR"
				+ " (maker_serial LIKE '%" + text.replaceAll(" ", "") + "%')) AND"
				+ " (view_flg = " + flg + ")"
				+ " ORDER BY updated DESC;", ItemList.class);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<ItemList> list = query.getResultList();
		Query countQuery = entityManager.createNativeQuery(
				"SELECT COUNT(*) FROM item_list_main WHERE"
						+ " ((name LIKE '%" + text.replaceAll(" ", "") + "%') OR"
						+ " (serial LIKE '%" + text.replaceAll(" ", "") + "%') OR"
						+ " (maker_serial LIKE '%" + text.replaceAll(" ", "") + "%')) AND"
						+ " (view_flg = " + flg + ");");
		long totalCount = ((Number) countQuery.getSingleResult()).longValue();

		return new PageImpl<>(list, pageable, totalCount);
	}

	public ItemList loadItem(int id) {
		ItemList item = itemRepository.getReferenceById(id);
		return item;
	}

	public void hideItem(int id) {
		ItemList item = itemRepository.getReferenceById(id);
		item.setViewFlg(0);
		itemRepository.save(item);
	}
	
	public void displayItem(int id) {
		ItemList item = itemRepository.getReferenceById(id);
		item.setViewFlg(1);
		itemRepository.save(item);
	}

	public void updateItem(ItemList item, MultipartFile file) {
		if (!file.isEmpty()) {
			String filePath = createFile(file);
			item.setFilePath(filePath);
		}
		System.out.println(item.getFilePath() + " " + file.isEmpty());
		itemRepository.save(item);
	}

	public String createFile(MultipartFile file) {
		try {
			Path filename = Paths.get("uploadFile\\" + file.getOriginalFilename());
			OutputStream outputStream = Files.newOutputStream(filename, StandardOpenOption.CREATE);
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
			return file.getOriginalFilename();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ResponseEntity<Resource> downloadFile(String filename) {
		Path path = Paths.get("uploadFile\\" + filename);
		try {
			Resource resource = new UrlResource(path.toUri());
			System.out.println(path.toUri().toString());
			if (!resource.exists()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			System.out.println(UriUtils.encode(filename, StandardCharsets.UTF_8.name()));
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"form-data; name=\"fieldName\"; filename=\""
									+ UriUtils.encode(filename, StandardCharsets.UTF_8.name()) + "\"")
					.body(resource);
		} catch (MalformedURLException e) {
			new ItemListException("正しい値を入力してください");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	public List<ItemList> csvInsertItem(MultipartFile file) {
		List<ItemList> list = new ArrayList<ItemList>();
		try {
			CSVParser parser = CSVFormat.DEFAULT
					.parse(new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8")));
			for (CSVRecord record : parser) {
				System.err.println(record.get(1) //
						+ ": " + record.get(6) + " " + record.get(7) + " " + record.get(8));
				ItemList itemList = new ItemList();
				itemList.setName(record.get(1));
				itemList.setSerial(record.get(2));
				itemList.setMakerSerial(record.get(3));
				itemList.setDepartmentId(Integer.parseInt(record.get(4)));
				itemList.setPlaceId(Integer.parseInt(record.get(5)));
				itemList.setCycle(Integer.parseInt(record.get(6)));
				itemList.setUpdated(LocalDate.parse(record.get(7)));
				itemList.setViewFlg(Integer.parseInt(record.get(8)));
				list.add(itemList);
				itemRepository.save(itemList);
			}
			return list;
		} catch (Exception e) {
			new ItemListException("正しい値を入力してください");
			System.out.println(e.getMessage());
		}
		return list;
	}
}
