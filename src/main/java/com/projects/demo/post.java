package com.projects.demo;

import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class post {

	@Override
	public String toString() {
		return "post [id=" + id + ", ownerUsername=" + ownerUsername + ", type=" + type + ", give=" + give + ", rooms="
				+ rooms + ", address=" + address + ", price=" + price + ", description=" + description+",image = " +image+ "]";
	}

	@Id
	private String id;
	
	private String ownerUsername;
	private String type;
	private String give;
	private String rooms;
	private String address;
	private String price;
	private String description;
	private List<String> image;

	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public post() {
		// TODO Auto-generated constructor stub
	}

	public post(String id, String ownerUsername, String type, String give, String rooms, String address, String price,
			String description) {
		super();
		this.id = id;
		this.ownerUsername = ownerUsername;
		this.type = type;
		this.give = give;
		this.rooms = rooms;
		this.address = address;
		this.price = price;
		this.description = description;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGive() {
		return give;
	}

	public void setGive(String give) {
		this.give = give;
	}

	public String getRooms() {
		return rooms;
	}

	public void setRooms(String rooms) {
		this.rooms = rooms;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
