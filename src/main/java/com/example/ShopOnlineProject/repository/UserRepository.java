package com.example.ShopOnlineProject.repository;

import com.example.ShopOnlineProject.model.CustomUser;
import com.example.ShopOnlineProject.repository.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String USER_TABLE = "users";

	public String register(CustomUser user) {
		String sql = String.format ( "INSERT INTO %s (first_name, last_name, email, phone, address, username, password, role) VALUES (?,?,?,?,?,?,?,?)", USER_TABLE );
		jdbcTemplate.update ( sql, user.getFirstName (), user.getLastName (), user.getEmail (), user.getPhone (), user.getAddress (), user.getUsername (), user.getPassword (), user.getRole ().name () );
		return "User registered successfully";


	}

	public CustomUser findUserByUsername(String username) {
		try {
			String sql = String.format ( "SELECT * FROM %s WHERE username = ? ", USER_TABLE );
			CustomUser user = jdbcTemplate.queryForObject ( sql, new UserMapper (), username );
			return user;
		} catch (Exception e) {
			System.out.println ( e.getMessage () );
			return null;
		}
	}

	public CustomUser findUserByEmail(String email) {
		try {
			String sql = String.format ( "SELECT * FROM %s WHERE email = ? ", USER_TABLE );
			CustomUser user = jdbcTemplate.queryForObject ( sql, new UserMapper (), email );
			return user;

		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

		public List<CustomUser> findAllUsers () {
			String sql = String.format ( "SELECT * FROM %s", USER_TABLE );
			List<CustomUser> users = jdbcTemplate.query ( sql, new UserMapper () );
			return users;
		}
		public CustomUser updateUser (CustomUser user){
			String sql = String.format ( "UPDATE %s SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? WHERE username = ?", USER_TABLE );
			jdbcTemplate.update ( sql, user.getFirstName (), user.getLastName (), user.getEmail (), user.getPhone (), user.getAddress (), user.getUsername () );
			return findUserByUsername ( user.getUsername () );
		}
		public String deleteUser (String username){
			String sqlDeleteItems = "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE user_username = ?)";
			jdbcTemplate.update(sqlDeleteItems, username);


			String sqlDeleteOrders = "DELETE FROM orders WHERE user_username = ?";
			jdbcTemplate.update(sqlDeleteOrders, username);


			String sqlDeleteFavorites = "DELETE FROM favorites WHERE user_username = ?";
			jdbcTemplate.update(sqlDeleteFavorites, username);


			String sqlDeleteUser = String.format("DELETE FROM %s WHERE username = ?", USER_TABLE);
			jdbcTemplate.update(sqlDeleteUser, username);

			return "User and all related data deleted successfully";
		}


	public void updatePassword(String username, String newHashedPassword) {
		String sql = "UPDATE users SET password = ? WHERE username = ?";

		try {
			int rowsAffected = jdbcTemplate.update(sql, newHashedPassword, username);

			if (rowsAffected == 0) {
				throw new RuntimeException("No user found with username: " + username);
			}

			System.out.println("Password successfully updated in DB for user: " + username);
		} catch (Exception e) {
			throw new RuntimeException(" Error update password " + e.getMessage());
		}
	}
	}





