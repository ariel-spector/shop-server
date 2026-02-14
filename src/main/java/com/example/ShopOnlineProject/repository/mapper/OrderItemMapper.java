package com.example.ShopOnlineProject.repository.mapper;

import com.example.ShopOnlineProject.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemMapper implements RowMapper<OrderItem> {
	@Override
	public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrderItem orderItem = new OrderItem();
		orderItem.setId(rs.getInt ("id"));
		orderItem.setOrderId(rs.getInt ("order_id"));
		orderItem.setItemId ( rs.getInt ( "item_id" ) );
		orderItem.setQuantity ( rs.getInt ( "quantity" ) );
		orderItem.setItemName ( rs.getString ( "item_name" ) );
		orderItem.setPrice ( rs.getDouble ( "item_price" ) );
		orderItem.setPhotoUrl ( rs.getString ( "photo_url" ) );
		return orderItem;

	}
}
