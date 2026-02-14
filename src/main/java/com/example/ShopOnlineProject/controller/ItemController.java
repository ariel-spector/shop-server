package com.example.ShopOnlineProject.controller;

import com.example.ShopOnlineProject.model.Item;
import com.example.ShopOnlineProject.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemController {

	@Autowired
	private ItemService itemService;


	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<?> createItem(@RequestBody Item item) {
		try {
			itemService.createItem(item);
			return ResponseEntity.status(HttpStatus.CREATED).body("Item created successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating item");
		}
	}


	@GetMapping
	public ResponseEntity<List<Item>> getAllItems() {
		return ResponseEntity.ok(itemService.getAllItems());
	}


	@GetMapping("/search")
	public ResponseEntity<List<Item>> searchItems(@RequestParam("name") String itemName) {
		return ResponseEntity.ok(itemService.searchItemByName(itemName));
	}


	@GetMapping("/{id}")
	public ResponseEntity<?> getItemById(@PathVariable int id) {
		Item item = itemService.getItemById(id);
		if (item != null) {
			return ResponseEntity.ok(item);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
		}
	}


	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping
	public ResponseEntity<?> updateItem(@RequestBody Item item) {
		if (item.getItemId() == null) {
			return ResponseEntity.badRequest().body("ERROR: itemId is mandatory for update!");
		}
		try {
			Item updatedItem = itemService.updateItem(item);
			return ResponseEntity.ok(updatedItem);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating item");
		}
	}


	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{itemId}")
	public ResponseEntity<?> deleteItem(@PathVariable int itemId) {
		try {
			itemService.deleteItem(itemId);
			return ResponseEntity.ok("Item deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting item");
		}
	}
}