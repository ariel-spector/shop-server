package com.example.ShopOnlineProject.service;

import com.example.ShopOnlineProject.model.Item;
import com.example.ShopOnlineProject.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;



	public String createItem(Item item) {

		if (item == null) {
			throw new IllegalArgumentException("Item object cannot be null");
		}
		if (item.getItemName() == null || item.getItemName().trim().isEmpty()) {
			throw new IllegalArgumentException("Item name is required");
		}
		if (item.getPrice() <= 0) {
			throw new IllegalArgumentException("Price must be positive");
		}
		if (item.getItemQuantity() < 0) {
			throw new IllegalArgumentException("Quantity cannot be negative");
		}

		return itemRepository.createItem(item);
	}

	public Item getItemById(int itemId) {
		return itemRepository.getItemById(itemId);
	}

	public List<Item> searchItemByName(String itemName) {

		if (itemName == null || itemName.trim().isEmpty()) {
			return Collections.emptyList();
		}
		return itemRepository.searchItemByName(itemName);
	}

	public List<Item> getAllItems() {
		return itemRepository.getAllItems();
	}

	public Item updateItem(Item item) {
		if (item.getPrice() <= 0) {
			throw new IllegalArgumentException("Price must be positive");
		}
		return itemRepository.updateItem(item);
	}

	public void deleteItem(int itemId) {
		itemRepository.deleteItem(itemId);
	}


	public void reduceItemQuantity(int itemId, int quantity) {
		itemRepository.reduceItemQuantity(itemId, quantity);
	}

	public void increaseItemQuantity(int itemId, int quantity) {
		itemRepository.increaseItemQuantity(itemId, quantity);
	}
}