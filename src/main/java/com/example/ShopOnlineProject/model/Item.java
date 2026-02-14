package com.example.ShopOnlineProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
	@JsonProperty ("item_id")
	private Integer itemId;

	@JsonProperty("item_name")
	private String itemName;
	@JsonProperty("item_price")
	private Double itemPrice;
	@JsonProperty("item_quantity")

	private Integer itemQuantity;

	@JsonProperty("photo_url")
	private String photoUrl;

	@JsonProperty("brand_name")
	private String brandName;

	public Item() {
	}
	public Item(Integer itemId, String itemName, Double price, Integer itemQuantity, String photoUrl, String brandName) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemPrice = price;
		this.itemQuantity = itemQuantity;
		this.photoUrl = photoUrl;
		this.brandName = brandName;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getPrice() {
		return itemPrice;
	}
	public  void setPrice(Double price) {
		this.itemPrice = price;
	}

	public Integer getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(Integer itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@Override
	public String toString() {
		return "Item{" +
				"itemId=" + itemId +
				", itemName='" + itemName + '\'' +
				", price=" + itemPrice +
				", itemQuantity=" + itemQuantity +
				", photoUrl='" + photoUrl + '\'' +
				", brandName='" + brandName + '\'' +
				'}';
	}
}

