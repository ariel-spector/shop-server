package com.example.ShopOnlineProject.controller;

import com.example.ShopOnlineProject.model.Order;
import com.example.ShopOnlineProject.service.OrderService;
import com.example.ShopOnlineProject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private OrderService orderService;


	@PostMapping("/add")
	public ResponseEntity<?> addToCart(@RequestHeader(value = "Authorization") String token, @RequestBody Order order) {
		try {
			String username = getUsernameFromToken(token);
			order.setUserUsername(username);
			order.setOrderPlaced(LocalDate.now());

			Integer orderId = orderService.addToCart(order);


			Order updatedOrder = orderService.getOrderById(orderId);
			return ResponseEntity.status(HttpStatus.CREATED).body(updatedOrder);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding to cart: " + e.getMessage());
		}
	}


	@PostMapping("/checkout")
	public ResponseEntity<?> checkOut(@RequestHeader(value = "Authorization") String token) {
		try {
			String username = getUsernameFromToken(token);
			Order closedOrder = orderService.checkoutOrder(username);
			return ResponseEntity.ok(closedOrder); // Return the closed order details
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Checkout failed");
		}
	}


	@GetMapping("/list")
	public ResponseEntity<?> getUserOrders(@RequestHeader(value = "Authorization") String token) {
		try {
			String username = getUsernameFromToken(token);
			return ResponseEntity.ok(orderService.getAllOrders(username));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching orders");
		}
	}


	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrderById(@RequestHeader(value = "Authorization") String token, @PathVariable Integer orderId) {
		try {
			String username = getUsernameFromToken(token);
			Order order = orderService.getOrderById(orderId);

			if (order == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
			}
			// בדיקת אבטחה: האם ההזמנה שייכת למשתמש שמבקש אותה?
			if (!order.getUserUsername().equals(username)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this order");
			}
			return ResponseEntity.ok(order);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}


	@DeleteMapping("/remove-item")
	public ResponseEntity<?> removeItemFromCart(@RequestHeader(value = "Authorization") String token, @RequestParam int itemId) {
		try {
			String username = getUsernameFromToken(token);
			Order updatedOrder = orderService.removeItemFromCart(username, itemId);


			return ResponseEntity.ok(updatedOrder != null ? updatedOrder : "Cart is empty");

		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing item");
		}
	}


	private String getUsernameFromToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			return jwtUtil.extractUsername(token.substring(7));
		}
		throw new IllegalArgumentException("Invalid Token");
	}
}