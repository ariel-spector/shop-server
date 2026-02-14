package com.example.ShopOnlineProject.repository;

import com.example.ShopOnlineProject.model.Item;
import com.example.ShopOnlineProject.repository.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class FavoriteRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String TABLE_NAME = "favorites";
	private static final String ITEMS_TABLE = "items";


	private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (user_username, item_id) VALUES (?, ?)";
	private static final String SQL_DELETE_SINGLE = "DELETE FROM " + TABLE_NAME + " WHERE user_username = ? AND item_id = ?";
	private static final String SQL_DELETE_ALL_USER = "DELETE FROM " + TABLE_NAME + " WHERE user_username = ?";
	private static final String SQL_CHECK_EXISTS = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE user_username = ? AND item_id = ?";


	private static final String SQL_GET_USER_FAVORITES =
			"SELECT i.* FROM " + ITEMS_TABLE + " i " +
					"JOIN " + TABLE_NAME + " f ON i.item_id = f.item_id " +
					"WHERE f.user_username = ?";

	public void addFavorite(String userName, int itemId) {
		jdbcTemplate.update(SQL_INSERT, userName, itemId);
	}

	public void removeFavorite(String userName, int itemId) {
		jdbcTemplate.update(SQL_DELETE_SINGLE, userName, itemId);
	}

	public List<Item> getFavoritesByUsername(String userName) {
		try {

			return jdbcTemplate.query(SQL_GET_USER_FAVORITES, new ItemMapper(), userName);
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	public void deleteFavoritesByUsername(String username) {
		jdbcTemplate.update(SQL_DELETE_ALL_USER, username);
	}

	public boolean isFavoriteExists(String userName, int itemId) {

		Integer count = jdbcTemplate.queryForObject(SQL_CHECK_EXISTS, Integer.class, userName, itemId);
		return count != null && count > 0;
	}
}