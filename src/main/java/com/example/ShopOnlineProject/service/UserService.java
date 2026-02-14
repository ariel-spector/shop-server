package com.example.ShopOnlineProject.service;

import com.example.ShopOnlineProject.model.CustomUser;
import com.example.ShopOnlineProject.model.Order;
import com.example.ShopOnlineProject.model.ResetPasswordRequest;
import com.example.ShopOnlineProject.model.Role;
import com.example.ShopOnlineProject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OrderService orderService;

	@Autowired
	private FavoriteService favoriteService;


	public String register(CustomUser user) {
		if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null
				|| user.getUsername() == null || user.getPassword() == null) {
			throw new IllegalArgumentException("Missing required fields: first name, last name, email, username or password");
		}


		if (userRepository.findUserByEmail(user.getEmail()) != null) {
			throw new RuntimeException("Email already exists");
		}
		if (userRepository.findUserByUsername(user.getUsername()) != null) {
			throw new RuntimeException("Username already exists");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		if (user.getRole() == null) {
			user.setRole(Role.USER);
		}

		return userRepository.register(user);
	}

	public CustomUser getUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	public CustomUser getUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	public List<CustomUser> getAllUsers() {
		return userRepository.findAllUsers();
	}

	public CustomUser updateUser(CustomUser updateUser) {

		return userRepository.updateUser(updateUser);
	}

	@Transactional
	public String deleteUser(String username) {
		CustomUser registeredUser = userRepository.findUserByUsername(username);
		if (registeredUser == null) {
			throw new RuntimeException("User not found");
		}


		favoriteService.deleteFavoritesByUsername(username);


		List<Order> userOrders = orderService.getAllOrders(username);
		for (Order order : userOrders) {

			orderService.deleteOrder (order.getOrderId());
		}


		return userRepository.deleteUser(username);
	}
	public String resetPassword(String username, String email, String newPassword) {

		CustomUser user = userRepository.findUserByUsername(username);


		if (user == null || !user.getEmail().equals(email)) {
			throw new RuntimeException("פרטים לא תקינים - שם משתמש או אימייל אינם תואמים");
		}


		String hashedNewPassword = passwordEncoder.encode(newPassword);


		userRepository.updatePassword(username, hashedNewPassword);

		return "הסיסמה שונתה בהצלחה";
	}
	public void resetPassword(ResetPasswordRequest request) {

		CustomUser user = userRepository.findUserByEmail ( request.getEmail() );


		if (user == null || !user.getEmail().equalsIgnoreCase(request.getEmail())) {
			throw new RuntimeException("פרטים לא תקינים - שם משתמש או אימייל אינם תואמים");
		}

		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
		if (!request.getNewPassword().matches(passwordPattern)) {
			throw new RuntimeException("הסיסמה חייבת להכיל לפחות 8 תווים, כולל אות גדולה, אות קטנה ומספר.");
		}


		String hashedNewPassword = passwordEncoder.encode(request.getNewPassword());


		user.setPassword(hashedNewPassword);
		userRepository.updateUser(user);

		System.out.println("Password reset successful for user: " + request.getUsername());
	}
}



