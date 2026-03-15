package com.sufaltalukder.Utils;

import java.util.Date;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

@Component
public class AuthJwtUtil {

	private final String SECRET_KEY = "AUTH-USER-ETPL-MAC-LTP-033-SUFAL-0309-ESOLZ-MACOS-11.7.1-Big-Sur-C2VNC0NGG085-1.4-GHz-Dual-Core-Intel-Core-i5";

	private final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 15; // 15 min
	private final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 days

	public static final String API_KEY_HEADER = "x-api-key";
	public static final String API_SECRET_HEADER = "x-api-secret";

	private static final String VALID_API_KEY = "20912119727";
	private static final String VALID_API_SECRET = "SufalTalukder70";

	/** Now embeds the user's role so the filter can read it without a DB call. */
	public String generateAccessToken(String email, long userId, String role) {
		return Jwts.builder().setSubject(email).claim("authUserId", userId).claim("authUserType", role)
				.claim("type", "ACCESS").setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public String generateRefreshToken(long userId) {
		return Jwts.builder().claim("authUserId", userId).claim("type", "REFRESH").setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	/**
	 * Exposed so JwtAuthenticationFilter can parse claims without repeating logic.
	 */
	public Claims parseAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public String extractUsername(String token) {
		return parseAllClaims(token).getSubject();
	}

	private boolean isTokenExpired(String token) {
		return parseAllClaims(token).getExpiration().before(new Date());
	}

	public boolean validateToken(String token, String email) {
		return extractUsername(token).equals(email) && !isTokenExpired(token);
	}

	public long extractAuthUserId(String token, String apiKey, String apiSecret) {
		verifyAuthUser(apiKey, apiSecret);
		try {
			Claims claims = parseAllClaims(token);

			if (claims.getExpiration().before(new Date())) {
				throw new SecurityException("JWT token has expired");
			}

			Object userId = claims.get("authUserId");
			if (userId == null)
				throw new SecurityException("authUserId missing in token");

			return Long.parseLong(userId.toString());

		} catch (ExpiredJwtException e) {
			throw new SecurityException("JWT token expired", e);
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new SecurityException("Invalid JWT token", e);
		}
	}

	public String verifyAuthUser(String apiKey, String apiSecret) {
		if (apiKey == null || !VALID_API_KEY.equals(apiKey))
			throw new SecurityException("Invalid or missing x-api-key");
		if (apiSecret == null || !VALID_API_SECRET.equals(apiSecret))
			throw new SecurityException("Invalid or missing x-api-secret");
		return "ok";
	}
}