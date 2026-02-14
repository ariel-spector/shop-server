package com.example.ShopOnlineProject.controller;

import com.example.ShopOnlineProject.model.Item;
import com.example.ShopOnlineProject.service.FavoriteService;
import com.example.ShopOnlineProject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private FavoriteService favoriteService;


	@PostMapping("/add/{itemId}")
	public ResponseEntity<?> addFavorite(@RequestHeader(value = "Authorization") String token, @PathVariable int itemId) {
		try {
			String username = getUsernameFromToken(token);
			favoriteService.addFavorite(username, itemId);
			return ResponseEntity.ok("Item added to favorites successfully");
		} catch (IllegalArgumentException e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding favorite");
		}
	}


	@GetMapping("/list")
	public ResponseEntity<?> getFavorites(@RequestHeader(value = "Authorization") String token) {
		try {
			String username = getUsernameFromToken(token);
			List<Item> favorites = favoriteService.getFavoritesByUsername(username);
			return ResponseEntity.ok(favorites);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching favorites");
		}
	}


	@DeleteMapping("/remove/{itemId}")
	public ResponseEntity<?> removeFavorite(@RequestHeader(value = "Authorization") String token, @PathVariable int itemId) {
		try {
			String username = getUsernameFromToken(token);
			favoriteService.removeFavorite(username, itemId);
			return ResponseEntity.ok("Item removed from favorites successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing favorite");
		}
	}


	private String getUsernameFromToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			String jwtToken = token.substring(7);
			return jwtUtil.extractUsername(jwtToken);
		}
		throw new IllegalArgumentException("Invalid Token Format");
	}
}