package com.sufaltalukder.Utils;

import java.util.Date;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

@Component
public class AuthJwtUtil {

	private final String SECRET_KEY = "AUTH-USER-ETPL-MAC-LTP-033-SUFAL-0309-ESOLZ-MACOS-11.7.1-Big-Sur-C2VNC0NGG085-1.4-GHz-Dual-Core-Intel-Core-i5";
	private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 5 minutes

	public static final String API_KEY_HEADER = "x-api-key";
	public static final String API_SECRET_HEADER = "x-api-secret";

	private static final String VALID_API_KEY = "20912119727";
	private static final String VALID_API_SECRET = "SufalTalukder70";

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

	public long extractAuthUserId(String token, String apiKey, String apiSecret) {

		if (apiKey == null || !VALID_API_KEY.equals(apiKey)) {
			throw new SecurityException("Invalid or missing x-api-key");
		}

		if (apiSecret == null || !VALID_API_SECRET.equals(apiSecret)) {
			throw new SecurityException("Invalid or missing x-api-secret");
		}

		try {
			Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

			if (claims.getExpiration().before(new Date())) {
				throw new SecurityException("JWT token has expired");
			}

			Object userId = claims.get("authUserId");
			if (userId == null) {
				throw new SecurityException("authUserId not found in JWT token");
			}

			return Long.parseLong(userId.toString());

		} catch (ExpiredJwtException e) {
			throw new SecurityException("JWT token expired", e);
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new SecurityException("Invalid JWT token", e);
		}
	}

	public String verifyAuthUser(String apiKey, String apiSecret) {

		if (apiKey == null || !VALID_API_KEY.equals(apiKey)) {
			throw new SecurityException("Invalid or missing x-api-key");
		}

		if (apiSecret == null || !VALID_API_SECRET.equals(apiSecret)) {
			throw new SecurityException("Invalid or missing x-api-secret");
		}

		return "ok";
	}
}