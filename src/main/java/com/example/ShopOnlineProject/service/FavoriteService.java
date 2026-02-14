package com.example.ShopOnlineProject.service;

import com.example.ShopOnlineProject.model.Item;
import com.example.ShopOnlineProject.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FavoriteService {

	@Autowired
	private FavoriteRepository favoriteRepository;

	public void addFavorite(String userName, int itemId) {

		if (userName == null || userName.trim().isEmpty()) {
			throw new IllegalArgumentException("Username cannot be empty");
		}


		if (favoriteRepository.isFavoriteExists(userName, itemId)) {
			throw new IllegalArgumentException("Item is already in favorites");
		}


		try {
			favoriteRepository.addFavorite(userName, itemId);
		} catch (DuplicateKeyException e) {
			// אם בכל זאת קרתה תאונת "Check-Then-Act" וה-DB צעק שיש כפילות
			throw new IllegalArgumentException("Item is already in favorites");
		}
	}

	public void removeFavorite(String userName, int itemId) {
		if (userName != null) {
			favoriteRepository.removeFavorite(userName, itemId);
		}
	}

	public List<Item> getFavoritesByUsername(String userName) {
		if (userName == null) return Collections.emptyList();

		return favoriteRepository.getFavoritesByUsername(userName);
	}

	public void deleteFavoritesByUsername(String username) {
		if (username != null) {
			favoriteRepository.deleteFavoritesByUsername(username);
		}
	}
}