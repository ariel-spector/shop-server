package com.example.ShopOnlineProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList; // הוספתי ייבוא
import java.util.List;

public class Order {
	@JsonProperty(value = "order_id")
	private Integer orderId;

	@JsonProperty(value = "user_username")
	private String userUsername;

	@JsonProperty(value = "order_placed")
	private LocalDate orderPlaced;

	@JsonProperty(value = "shipping_address")
	private String shippingAddress;


	@JsonProperty("items")
	private List<OrderItem> items = new ArrayList<>();

	@JsonProperty(value = "total_price")
	private Double totalPrice = 0.0;

	private String status;

	public Order() {
	}

	public Order(Integer orderId, String userUsername, LocalDate orderPlaced, String shippingAddress, List<OrderItem> items, Double totalPrice, String status) {
		this.orderId = orderId;
		this.userUsername = userUsername;
		this.orderPlaced = orderPlaced;
		this.shippingAddress = shippingAddress;
		this.items = items;
		this.totalPrice = totalPrice;
		this.status = status;
	}



	public Integer getOrderId() { return orderId; }
	public void setOrderId(Integer orderId) { this.orderId = orderId; }
	public String getUserUsername() { return userUsername; }
	public void setUserUsername(String userUsername) { this.userUsername = userUsername; }
	public LocalDate getOrderPlaced() { return orderPlaced; }
	public void setOrderPlaced(LocalDate orderPlaced) { this.orderPlaced = orderPlaced; }
	public String getShippingAddress() { return shippingAddress; }
	public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

	public List<OrderItem> getItems() { return items; }
	public void setItems(List<OrderItem> items) { this.items = items; }

	public Double getTotalPrice() { return totalPrice; }
	public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	@Override
	public String toString() {

		return "Order{" +
				"orderId=" + orderId +
				", userUsername='" + userUsername + '\'' +
				", items=" + items +
				", totalPrice=" + totalPrice +
				", status='" + status + '\'' +
				'}';
	}
}