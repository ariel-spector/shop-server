package com.example.ShopOnlineProject.controller;

import com.example.ShopOnlineProject.model.CustomUser;
import com.example.ShopOnlineProject.model.ResetPasswordRequest;
import com.example.ShopOnlineProject.service.UserService;
import com.example.ShopOnlineProject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;


	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody CustomUser user) {
		try {
			userService.register(user);
			return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
		} catch (RuntimeException e) {
			// GOSLING FIX: Removed redundant 'IllegalArgumentException |'.
			// RuntimeException captures everything (logic errors, duplicates, bad inputs).
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user");
		}
	}


	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization") String token) {
		try {
			String username = getUsernameFromToken(token);
			CustomUser user = userService.getUserByUsername(username);

			if (user != null) {
				user.setPassword(null);
			}

			return ResponseEntity.ok(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user details");
		}
	}


	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestHeader(value = "Authorization") String token, @RequestBody CustomUser updatedUser) {
		try {
			String username = getUsernameFromToken(token);

			if (!username.equals(updatedUser.getUsername()) && updatedUser.getUsername() != null) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot update another user's profile");
			}

			updatedUser.setUsername(username);

			CustomUser result = userService.updateUser(updatedUser);

			if (result != null) result.setPassword(null);
			return ResponseEntity.ok(result);

		} catch (RuntimeException e) {
			// GOSLING FIX: Catching only RuntimeException covers IllegalArgumentException too.
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
		}
	}


	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
	@DeleteMapping
	public ResponseEntity<?> deleteUser(@RequestHeader(value = "Authorization") String token) {
		try {
			String username = getUsernameFromToken(token);
			userService.deleteUser(username);
			return ResponseEntity.ok("User deleted successfully");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
		}
	}

	private String getUsernameFromToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			return jwtUtil.extractUsername(token.substring(7));
		}
		throw new IllegalArgumentException("Invalid Token");
	}
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
		try {
			userService.resetPassword(request);
			return ResponseEntity.ok("הסיסמה שונתה בהצלחה. כעת ניתן להתחבר.");
		} catch (RuntimeException e) {
			// מחזירים 400 כדי שה-Frontend ידע שהאימות נכשל
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("שגיאת שרת פנימית.");
		}
	}
}