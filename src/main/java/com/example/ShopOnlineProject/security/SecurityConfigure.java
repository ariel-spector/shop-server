package com.example.ShopOnlineProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigure {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // אין Sessions בשרת


				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers("/api/authenticate").permitAll()
						.requestMatchers("/api/users/register").permitAll()


						.requestMatchers("/api/users/reset-password").permitAll()

						.requestMatchers(HttpMethod.GET, "/api/items/**").permitAll()


						.requestMatchers(HttpMethod.POST, "/api/items/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/items/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/items/**").hasAuthority("ADMIN")

						.requestMatchers("/api/orders/**").hasAnyAuthority("USER", "ADMIN")
						.requestMatchers("/api/favorites/**").hasAnyAuthority("USER", "ADMIN")


						.requestMatchers("/api/users/**").hasAnyAuthority("USER", "ADMIN")

						.anyRequest().authenticated()

				)


				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);


		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();


		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));


		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));


		configuration.setAllowedHeaders(Arrays.asList("*"));


		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}