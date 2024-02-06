package csi.attendence.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import csi.attendence.entity.AuthenticationInfo;
import csi.attendence.entity.User;
import csi.attendence.repository.AuthenticationRepository;
import csi.attendence.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationServiceImpl {

	final CustomUserDetailsService customUserDetailsService;

	private final AuthenticationRepository authenticationRepository;

	@Value("${application.security.jwt.secret_key}")
	private String secretKey;

	@Value("${application.security.jwt.token_expires}")
	private Long tokenExpires;

	@Value("${application.security.jwt.refresh_token_expires}")
	private Long refreshTokenExpires;

	public User validateAccessToken(String token) {

		if (isTokenExpired(token)) {
			throw new RuntimeException();
		}
		AuthenticationInfo authenticationInfo = this.authenticationRepository.findByToken(token)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with the given access token."));
		return authenticationInfo.getUser();
	}

	public AuthenticationInfo generateAuthenticationInfoByRefreshToken(String refreshToken) {
		if (Strings.isBlank(refreshToken)) {
			throw new RuntimeException();
		}
		if (isTokenExpired(refreshToken)) {
			throw new RuntimeException();
		}

		AuthenticationInfo authenticationInfo = this.authenticationRepository.findByRefreshToken(refreshToken)
				.orElseThrow();
		AuthenticationInfo newAuthenticationInfo = generateAuthenticationInfo(authenticationInfo.getUser());
		this.authenticationRepository.deleteById(authenticationInfo.getId());
		return newAuthenticationInfo;
	}

	public AuthenticationInfo generateAuthenticationInfo(User user) {
		UUID uuid = UUID.randomUUID();
		String accessToken = generateAccessToken(uuid, user);
		String refreshToken = generateRefreshToken(user);
		AuthenticationInfo authenticationInfo = new AuthenticationInfo();
		authenticationInfo.setActive(true);
		authenticationInfo.setToken(accessToken);
		authenticationInfo.setRefreshToken(refreshToken);
		authenticationInfo.setUser(user);
		AuthenticationInfo savedAuthenticationInfo = this.authenticationRepository.save(authenticationInfo);
		return savedAuthenticationInfo;
	}

	public String generateAccessToken(UUID id, User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", user.getUsername());
		claims.put("userId", user.getUserId());
		claims.put("email", user.getEmail());
		claims.put("mobile", user.getMobileNumber());
		claims.put("id", id.toString());
		claims.put("generatedAt", new Date());
		return buildToken(claims, user, TimeUnit.DAYS.toMillis(this.tokenExpires));
	}

	public String generateRefreshToken(UserDetails user) {
		return buildToken(new HashMap<>(), user, TimeUnit.DAYS.toMillis(this.refreshTokenExpires));
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSecretKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSecretKey() {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		return key;
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Boolean validateToken(String token, User userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
