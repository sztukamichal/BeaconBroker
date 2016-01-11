package com.michalapps.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "beacons")
public class Beacon {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String address;
	private String color;
	private float pos_x;
	private float pos_y;
	private float pos_z;
	private String description;
	
	public Beacon() {
	}
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public float getPos_x() {
		return pos_x;
	}

	public void setPos_x(float pos_x) {
		this.pos_x = pos_x;
	}

	public float getPos_y() {
		return pos_y;
	}

	public void setPos_y(float pos_y) {
		this.pos_y = pos_y;
	}

	public float getPos_z() {
		return pos_z;
	}

	public void setPos_z(float pos_z) {
		this.pos_z = pos_z;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	

}
