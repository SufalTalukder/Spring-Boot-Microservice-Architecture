package com.sufaltalukder.Utils;

import java.util.Date;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

@Component
public class AuthJwtUtil {

	private final String SECRET_KEY = "AUTH-USER-ETPL-MAC-LTP-033-SUFAL-0309-ESOLZ-MACOS-11.7.1-Big-Sur-C2VNC0NGG085-1.4-GHz-Dual-Core-Intel-Core-i5";
	private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

	public String generateToken(String authUserEmailAddress, long authUserId) {
		return Jwts.builder().setSubject(authUserEmailAddress).claim("authUserId", authUserId)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public boolean validateToken(String token, String authUserEmailAddress) {
		final String emailFromToken = extractUsername(token);
		return (emailFromToken.equals(authUserEmailAddress) && !isTokenExpired(token));
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

	public long extractAuthUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		Object userIdClaim = claims.get("authUserId");
		if (userIdClaim == null) {
			throw new IllegalArgumentException("Auth user ID not found in token claims");
		}
		return Long.parseLong(userIdClaim.toString());
	}
}