package com.sufaltalukder.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "USER-ETPL-MAC-LTP-033-SUFAL-0309-ESOLZ-MACOS-11.7.1-Big-Sur-C2VNC0NGG085-1.4-GHz-Dual-Core-Intel-Core-i5";
	private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

	public String generateToken(String phoneNumber, long userId) {
		return Jwts.builder().setSubject(phoneNumber).claim("userId", userId) // Add userId to claims
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public boolean validateToken(String token, String phoneNumber) {
		final String username = extractUsername(token);
		return (username.equals(phoneNumber) && !isTokenExpired(token));
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	public long extractUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		Object userIdClaim = claims.get("userId");
		if (userIdClaim == null) {
			throw new IllegalArgumentException("User ID not found in token claims");
		}
		return Long.parseLong(userIdClaim.toString());
	}
}