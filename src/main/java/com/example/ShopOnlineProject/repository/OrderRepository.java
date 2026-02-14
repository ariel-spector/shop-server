package com.example.ShopOnlineProject.repository;

import com.example.ShopOnlineProject.model.Order;
import com.example.ShopOnlineProject.model.OrderItem;
import com.example.ShopOnlineProject.repository.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private OrderItemRepository orderItemRepository;


	public static final String STATUS_TEMP = "TEMP";
	public static final String STATUS_CLOSED = "CLOSED";

	private static final String TABLE_NAME = "orders";



	private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (user_username, order_placed, shipping_address, total_price, status) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE order_id = ?";
	private static final String SQL_SELECT_ALL_BY_USER = "SELECT * FROM " + TABLE_NAME + " WHERE user_username = ?";
	private static final String SQL_UPDATE_STATUS = "UPDATE " + TABLE_NAME + " SET status = ?, order_placed = ? WHERE order_id = ?";
	private static final String SQL_UPDATE_ORDER_DETAILS = "UPDATE " + TABLE_NAME + " SET total_price = ?, shipping_address = ? WHERE order_id = ?";
	private static final String SQL_FIND_TEMP_CART = "SELECT * FROM " + TABLE_NAME + " WHERE user_username = ? AND status = ?";
	private static final String SQL_UPDATE_PRICE = "UPDATE " + TABLE_NAME + " SET total_price = total_price + ? WHERE order_id = ?";
	private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE order_id = ?";
	private static final String SQL_DELETE_ITEMS_BY_ORDER = "DELETE FROM order_items WHERE order_id = ?";

	public Integer create(Order order) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(SQL_INSERT, new String[]{"order_id"});
			ps.setString(1, order.getUserUsername());


			LocalDate dateToSave = (order.getOrderPlaced() != null) ? order.getOrderPlaced() : LocalDate.now();
			ps.setDate(2, Date.valueOf(dateToSave));

			ps.setString(3, order.getShippingAddress());
			ps.setDouble(4, order.getTotalPrice());
			ps.setString(5, order.getStatus());
			return ps;
		}, keyHolder);

		if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("order_id")) {
			return ((Number) keyHolder.getKeys().get("order_id")).intValue();
		}


		return keyHolder.getKey().intValue();
	}

	public Order findOrderById(int orderId) {
		try {
			Order order = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new OrderMapper(), orderId);


			if (order != null) {
				order.setItems(orderItemRepository.findAllByOrderId(orderId));
			}

			return order;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Order> findAllOrders(String username) {
		try {

			List<Order> orders = jdbcTemplate.query(SQL_SELECT_ALL_BY_USER, new OrderMapper(), username);


			for (Order order : orders) {
				List<OrderItem> items = orderItemRepository.findAllByOrderId(order.getOrderId());
				order.setItems(items);
			}

			return orders;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	public void updateOrderStatus(Order order) {

		Date sqlDate = (order.getOrderPlaced () != null) ? Date.valueOf ( order.getOrderPlaced () ) : Date.valueOf ( LocalDate.now () );


		 jdbcTemplate.update ( SQL_UPDATE_STATUS,
				order.getStatus (),
				sqlDate,
				order.getOrderId () );


	}


	public void updateOrder(Order order) {
		jdbcTemplate.update(SQL_UPDATE_ORDER_DETAILS,
				order.getTotalPrice(),
				order.getShippingAddress(),
				order.getOrderId());
	}

	public Order findTempOrderByUsername(String username) {
		try {

			return jdbcTemplate.queryForObject(SQL_FIND_TEMP_CART, new OrderMapper(), username, STATUS_TEMP);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateOrderPrice(int orderId, double priceToAdd) {
		jdbcTemplate.update(SQL_UPDATE_PRICE, priceToAdd, orderId);
	}

	public void deleteOrder(int orderId) {
		jdbcTemplate.update ( SQL_DELETE_ITEMS_BY_ORDER, orderId );
		jdbcTemplate.update(SQL_DELETE, orderId);
	}
}