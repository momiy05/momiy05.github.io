package com.example.demo.domain.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "item_list_main")
public class ItemList implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name ="serial")
	private String serial;
	
	@Column(name = "maker_serial")
	private String makerSerial;
	
	@Column(name = "department")
	private Integer departmentId;
	
	@Column(name = "place")
	private Integer placeId;
	
	@Column(name = "cycle")
	private Integer cycle;
	
	@Column(name = "updated")
	private LocalDate updated;
	
	@Column(name = "file_path")
	private String filePath;
	
	@Column(name = "view_flg")
	private Integer viewFlg;
}
