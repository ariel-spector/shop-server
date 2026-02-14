package com.example.ShopOnlineProject.service;

import com.example.ShopOnlineProject.model.Item;
import com.example.ShopOnlineProject.model.Order;
import com.example.ShopOnlineProject.model.OrderItem;
import com.example.ShopOnlineProject.repository.ItemRepository;
import com.example.ShopOnlineProject.repository.OrderItemRepository;
import com.example.ShopOnlineProject.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Transactional
	public Integer addToCart(Order order) {
		String currentUsername = order.getUserUsername();
		Integer finalOrderId;


		Order existingCart = orderRepository.findTempOrderByUsername(currentUsername);

		if (existingCart != null) {
			finalOrderId = existingCart.getOrderId();
		} else {

			Order newOrder = new Order();
			newOrder.setUserUsername(currentUsername);
			newOrder.setOrderPlaced(LocalDate.now());
			newOrder.setShippingAddress(order.getShippingAddress() != null ? order.getShippingAddress() : "Default Address");
			newOrder.setTotalPrice(0.0);
			newOrder.setStatus("TEMP");
			finalOrderId = orderRepository.create(newOrder);
		}

		if (order.getItems() == null || order.getItems().isEmpty()) {
			throw new RuntimeException("Order must contain at least one item");
		}

		double priceToAdd = 0.0;

		for (OrderItem itemInRequest : order.getItems()) {
			if (itemInRequest.getQuantity() <= 0) {
				throw new RuntimeException("Quantity must be positive!");
			}

			Item itemFromDb = itemRepository.getItemById(itemInRequest.getItemId());
			if (itemFromDb == null) {
				throw new RuntimeException("Item with id " + itemInRequest.getItemId() + " not found");
			}


			OrderItem existingItemInCart = orderItemRepository.findByOrderIdAndItemId(finalOrderId, itemInRequest.getItemId());
			int currentQtyInCart = (existingItemInCart != null) ? existingItemInCart.getQuantity() : 0;
			int totalQuantity = currentQtyInCart + itemInRequest.getQuantity();

			if (totalQuantity > itemFromDb.getItemQuantity()) {
				throw new RuntimeException("Stock limit exceeded for: " + itemFromDb.getItemName());
			}

			if (existingItemInCart != null) {
				orderItemRepository.updateQuantity(finalOrderId, itemInRequest.getItemId(), totalQuantity);
			} else {
				// העתקת הנתונים ל-Snapshot
				itemInRequest.setOrderId(finalOrderId);
				itemInRequest.setItemName(itemFromDb.getItemName());
				itemInRequest.setPrice(itemFromDb.getPrice());
				itemInRequest.setPhotoUrl(itemFromDb.getPhotoUrl());

				orderItemRepository.saveOrderItem(itemInRequest);
			}

			priceToAdd += itemFromDb.getPrice() * itemInRequest.getQuantity();
		}

		orderRepository.updateOrderPrice(finalOrderId, priceToAdd);
		return finalOrderId;
	}


	public Order getOrderById(Integer id) {
		Order order = orderRepository.findOrderById(id);
		if (order != null) {
			List<OrderItem> items = orderItemRepository.findAllByOrderId(id);
			order.setItems(items);
		}
		return order;
	}

	public List<Order> getAllOrders(String userName) {
		List<Order> orders = orderRepository.findAllOrders(userName);
		for (Order order : orders) {
			order.setItems(orderItemRepository.findAllByOrderId(order.getOrderId()));
		}
		return orders;
	}

	@Transactional
	public Order checkoutOrder(String username) {
		Order temp = orderRepository.findTempOrderByUsername(username);
		if (temp == null) throw new RuntimeException("No TEMP order found");

		List<OrderItem> items = orderItemRepository.findAllByOrderId(temp.getOrderId());
		if (items.isEmpty()) throw new RuntimeException("Cart is empty");

		for (OrderItem orderItem : items) {
			Item itemFromDb = itemRepository.getItemById(orderItem.getItemId());
			if (itemFromDb.getItemQuantity() < orderItem.getQuantity()) {
				throw new RuntimeException("Not enough stock for: " + itemFromDb.getItemName());
			}
			itemRepository.reduceItemQuantity(orderItem.getItemId(), orderItem.getQuantity());
		}

		temp.setStatus("CLOSED");
		temp.setOrderPlaced(LocalDate.now());
		orderRepository.updateOrderStatus(temp);

		return temp;
	}


	@Transactional
	public void deleteOrder(int orderId) {
		Order order = orderRepository.findOrderById(orderId);
		if (order == null) throw new RuntimeException("Order not found");

		List<OrderItem> items = orderItemRepository.findAllByOrderId(orderId);


		if ("CLOSED".equals(order.getStatus())) {
			for (OrderItem orderItem : items) {

				itemRepository.increaseItemQuantity(orderItem.getItemId(), orderItem.getQuantity());
			}

			orderRepository.deleteOrder(orderId);
		}

		else if ("TEMP".equals(order.getStatus())) {

			orderRepository.deleteOrder(orderId);
		}
		else {

			throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
		}
	}
	@Transactional
	public Order removeItemFromCart(String username, int itemId) {

		Order tempOrder = orderRepository.findTempOrderByUsername(username);
		if (tempOrder == null) {
			throw new RuntimeException("No active cart found for user: " + username);
		}

		int orderId = tempOrder.getOrderId();


		OrderItem itemInCart = orderItemRepository.findByOrderIdAndItemId(orderId, itemId);
		if (itemInCart == null) {
			throw new RuntimeException("Item not found in your cart");
		}


		double itemPrice = itemInCart.getPrice();


		if (itemInCart.getQuantity() > 1) {

			int newQty = itemInCart.getQuantity() - 1;
			orderItemRepository.updateQuantity(orderId, itemId, newQty);
		} else {

			orderItemRepository.deleteOrderItem(orderId, itemId);
		}


		orderRepository.updateOrderPrice(orderId, -itemPrice);


		List<OrderItem> remainingItems = orderItemRepository.findAllByOrderId(orderId);
		if (remainingItems.isEmpty()) {
			orderRepository.deleteOrder(orderId);
			return null;
		}


		return getOrderById(orderId);
	}
}