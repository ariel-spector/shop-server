package com.example.ShopOnlineProject.repository.mapper;

import com.example.ShopOnlineProject.model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class OrderMapper implements RowMapper<Order> {
		@Override
		public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
			Order order = new Order();

			// מיפוי נתונים בסיסי
			order.setOrderId(rs.getInt("order_id"));
			order.setUserUsername(rs.getString("user_username"));
			order.setTotalPrice(rs.getDouble("total_price"));
			order.setStatus(rs.getString("status"));


			order.setShippingAddress(rs.getString("shipping_address"));

			// המרת תאריך
			if (rs.getDate("order_placed") != null) {
				order.setOrderPlaced(rs.getDate("order_placed").toLocalDate());
			}

			return order;
		}
	}