package com.example.ShopOnlineProject.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

	@Value ( "${jwt.secret}" )
	private String SECRET_KEY;

	@Value ( "${jwt.expiration}" )
	private Long EXPIRATION_TIME;

	public String extractUsername(String token) {return  extractClaim(token, Claims::getSubject );}

	public Date extractExpiration(String token){return extractClaim(token, Claims::getExpiration);}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply ( claims );
	}

	public List<GrantedAuthority> extractAuthorities(String token) {
		Claims claims = extractAllClaims(token);
		List <String> roles = claims.get ( "roles",List.class );
		List<GrantedAuthority> authorities = new ArrayList<> ();
		if (roles != null){
			for (String role : roles){
				authorities.add ( new SimpleGrantedAuthority ( role ) );
			}
		}
		return authorities;
	}
	private Claims extractAllClaims(String token){
		return Jwts.parser ().setSigningKey ( SECRET_KEY ).parseClaimsJws ( token ).getBody ();
	}
	private Boolean isTokenExpired(String token) {return  extractExpiration ( token ).before ( new Date () );}

	public String generateToken(UserDetails userDetails){
		Map<String,Object> claims = new HashMap<> ();
		claims.put ( "sub" , userDetails.getUsername () );

		List<String> roles = userDetails.getAuthorities ().stream ()
				.map ( GrantedAuthority::getAuthority )
				.collect( Collectors.toList ());

		claims.put ( "roles",roles );

		return  Jwts.builder ()
				.setClaims ( claims )
				.setIssuedAt ( new Date (System.currentTimeMillis ()) )
				.setExpiration ( new Date (System.currentTimeMillis ()+ EXPIRATION_TIME) )
				.signWith ( SignatureAlgorithm.HS256,SECRET_KEY )
				.compact ();


	}
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String usernameFromToken = extractUsername(token);
		final String usernameFromDB = userDetails.getUsername();



		boolean namesMatch = usernameFromToken.equals(usernameFromDB);


		boolean isExpired = isTokenExpired(token);



		return (namesMatch && !isExpired);
	}
	}
