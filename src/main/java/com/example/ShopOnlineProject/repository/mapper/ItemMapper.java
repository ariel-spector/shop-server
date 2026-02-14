package com.example.ShopOnlineProject.repository.mapper;

import com.example.ShopOnlineProject.model.Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements RowMapper<Item> {
	@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
		Item item = new Item ();
		item.setItemId ( rs.getInt ( "item_id" ) );
		item.setItemName ( rs.getString ( "item_name" ) );
		item.setPrice ( rs.getDouble ( "item_price" ) );
		item.setItemQuantity ( rs.getInt ( "item_quantity" ) );
		item.setPhotoUrl ( rs.getString ( "photo_url" ) );
		item.setBrandName ( rs.getString ( "brand_name" ) );
		return item;
	}
}
