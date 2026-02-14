package com.example.ShopOnlineProject.repository;

import com.example.ShopOnlineProject.model.Item;
import com.example.ShopOnlineProject.repository.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String ITEMS_TABLE = "items";



	private static final String SQL_INSERT = "INSERT INTO " + ITEMS_TABLE + " (item_name, price, item_quantity, photo_url, brand_name) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + ITEMS_TABLE + " WHERE item_id = ?";
	private static final String SQL_SEARCH_BY_NAME = "SELECT * FROM items WHERE LOWER(item_name) LIKE LOWER(CONCAT('%', ?, '%'))";
	private static final String SQL_SELECT_ALL = "SELECT * FROM " + ITEMS_TABLE;
	private static final String SQL_UPDATE = "UPDATE " + ITEMS_TABLE + " SET item_name=?, price=?, item_quantity=?, photo_url=?, brand_name=? WHERE item_id=?";
	private static final String SQL_DELETE = "DELETE FROM " + ITEMS_TABLE + " WHERE item_id = ?";
	private static final String SQL_REDUCE_QTY = "UPDATE " + ITEMS_TABLE + " SET item_quantity = item_quantity - ? WHERE item_id = ?";
	private static final String SQL_INCREASE_QTY = "UPDATE " + ITEMS_TABLE + " SET item_quantity = item_quantity + ? WHERE item_id = ?";

	public String createItem(Item item) {

		jdbcTemplate.update(SQL_INSERT,
				item.getItemName(),
				item.getPrice(),
				item.getItemQuantity(),
				item.getPhotoUrl(),
				item.getBrandName()
		);
		return "Item created successfully";
	}

	public Item getItemById(int itemId) {
		try {
			return jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new ItemMapper(), itemId);
		} catch (EmptyResultDataAccessException e) {

			return null;
		}
	}

	public List<Item> searchItemByName(String itemName) {
		String searchPattern = "%" + itemName + "%";
		try {
			return jdbcTemplate.query(SQL_SEARCH_BY_NAME, new ItemMapper(), searchPattern);
		} catch (EmptyResultDataAccessException e) {

			return new ArrayList<>();
		}
	}

	public List<Item> getAllItems() {
		return jdbcTemplate.query(SQL_SELECT_ALL, new ItemMapper());
	}

	public Item updateItem(Item item) {
		jdbcTemplate.update(SQL_UPDATE,
				item.getItemName(),
				item.getPrice(),
				item.getItemQuantity(),
				item.getPhotoUrl(),
				item.getBrandName(),
				item.getItemId()
		);
		return getItemById(item.getItemId());
	}

	public String deleteItem(int itemId) {
		jdbcTemplate.update(SQL_DELETE, itemId);
		return "Item deleted successfully";
	}

	public void reduceItemQuantity(int itemId, int quantity) {

		jdbcTemplate.update(SQL_REDUCE_QTY, quantity, itemId);
	}

	public void increaseItemQuantity(int itemId, int quantity) {
		jdbcTemplate.update(SQL_INCREASE_QTY, quantity, itemId);
	}
}