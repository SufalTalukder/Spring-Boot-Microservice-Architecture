package com.sufaltalukder.Security;

import com.sufaltalukder.Utils.AuthJwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AuthJwtUtil authJwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authToken = request.getHeader("authToken");
		String apiKey = request.getHeader("x-api-key");
		String apiSecret = request.getHeader("x-api-secret");

		// Only attempt JWT auth when a token is present
		if (authToken != null && !authToken.isBlank()) {
			try {
				// Validate API credentials first
				authJwtUtil.verifyAuthUser(apiKey, apiSecret);

				Claims claims = authJwtUtil.parseAllClaims(authToken);

				if (claims.getExpiration().after(new Date())) {
					String role = claims.get("authUserType", String.class); // e.g. "SUPER_ADMIN"
					long userId = Long.parseLong(claims.get("authUserId").toString());

					List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,
							null, authorities);

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (Exception ignored) {
				// Invalid token — SecurityContext stays empty, request will be rejected at
				// endpoint
			}
		}

		filterChain.doFilter(request, response);
	}
}