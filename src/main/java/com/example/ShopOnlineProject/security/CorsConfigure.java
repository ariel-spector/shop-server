package com.example.ShopOnlineProject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfigure {

	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer () {
			@Override
			public void addCorsMappings (CorsRegistry registry) {
				registry.addMapping ( "/**" ); // Enable cors for all application
			}
		};
	}
}
