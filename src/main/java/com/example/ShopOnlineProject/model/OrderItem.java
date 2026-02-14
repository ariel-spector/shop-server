package com.example.ShopOnlineProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItem {

	private int id;
	private int orderId;

	@JsonProperty("item_id")
	private int itemId;

	@JsonProperty("quantity")
	private int quantity;

    @JsonProperty("item_name")
    private  String itemName;

	@JsonProperty("price")
	private double price;

	@JsonProperty("photo_url")
	private String photoUrl;
	private Item item;

	public OrderItem() {
	}

	public OrderItem(int id, int orderId, int itemId, int quantity, String itemName, double price, String photoUrl) {
		this.id = id;
		this.orderId = orderId;
		this.itemId = itemId;
		this.quantity = quantity;
		this.itemName = itemName;
		this.price = price;
		this.photoUrl = photoUrl;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
