package com.example.ShopOnlineProject.repository;

import com.example.ShopOnlineProject.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String TABLE_NAME = "order_items";

	// שאילתת הכנסה - נשארת כפי שהיא
	private static final String SQL_INSERT =
			"INSERT INTO " + TABLE_NAME + " (order_id, item_id, quantity, item_name, item_price, photo_url) " +
					"VALUES (?, ?, ?, ?, ?, ?)";

	// GOSLING FIX: שליפה ישירות מהטבלה ללא JOIN (יותר מהיר ובטוח להיסטוריה)
	private static final String SQL_FIND_BY_IDS =
			"SELECT * FROM " + TABLE_NAME + " WHERE order_id = ? AND item_id = ?";

	private static final String SQL_FIND_ALL_BY_ORDER =
			"SELECT * FROM " + TABLE_NAME + " WHERE order_id = ?";

	private static final String SQL_DELETE_SINGLE = "DELETE FROM " + TABLE_NAME + " WHERE order_id = ? AND item_id = ?";
	private static final String SQL_DELETE_ALL_BY_ORDER = "DELETE FROM " + TABLE_NAME + " WHERE order_id = ?";
	private static final String SQL_COUNT = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE order_id = ?";
	private static final String SQL_UPDATE_QTY = "UPDATE " + TABLE_NAME + " SET quantity = ? WHERE order_id = ? AND item_id = ?";

	public void saveOrderItem(OrderItem orderItem) {
		jdbcTemplate.update(SQL_INSERT,
				orderItem.getOrderId(),
				orderItem.getItemId(),
				orderItem.getQuantity(),
				orderItem.getItemName(),
				orderItem.getPrice(),
				orderItem.getPhotoUrl()
		);
	}

	public OrderItem findByOrderIdAndItemId(int orderId, int itemId) {
		try {
			return jdbcTemplate.queryForObject(SQL_FIND_BY_IDS, (rs, rowNum) -> {
				OrderItem item = new OrderItem();
				item.setItemId(rs.getInt("item_id"));
				item.setQuantity(rs.getInt("quantity"));
				item.setItemName(rs.getString("item_name"));
				item.setPrice(rs.getDouble("item_price"));
				item.setPhotoUrl(rs.getString("photo_url"));
				return item;
			}, orderId, itemId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<OrderItem> findAllByOrderId(int orderId) {
		return jdbcTemplate.query(SQL_FIND_ALL_BY_ORDER, (rs, rowNum) -> {
			OrderItem item = new OrderItem();
			item.setItemId(rs.getInt("item_id"));
			item.setQuantity(rs.getInt("quantity"));
			item.setItemName(rs.getString("item_name"));
			item.setPrice(rs.getDouble("item_price"));
			item.setPhotoUrl(rs.getString("photo_url"));
			return item;
		}, orderId);
	}


	public void updateQuantity(int orderId, int itemId, int quantity) {
		jdbcTemplate.update(SQL_UPDATE_QTY, quantity, orderId, itemId);
	}

	public void deleteOrderItem(int orderId, int itemId) {
		jdbcTemplate.update(SQL_DELETE_SINGLE, orderId, itemId);
	}

	public void deleteAllByOrderId(int orderId) {
		jdbcTemplate.update(SQL_DELETE_ALL_BY_ORDER, orderId);
	}

	public int countByOrderId(int orderId) {
		Integer count = jdbcTemplate.queryForObject(SQL_COUNT, Integer.class, orderId);
		return (count != null) ? count : 0;
	}
}
