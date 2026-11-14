package com.jcaa.usersmanagement.infrastructure.security;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService {

  private static final String ROLE_CLAIM = "role";
  private static final int MINIMUM_KEY_BYTES = 32;

  private final SecretKey signingKey;
  private final long expirationSeconds;
  private final Clock clock;

  @Autowired
  public JwtTokenService(final JwtProperties properties) {
    this(properties, Clock.systemUTC());
  }

  JwtTokenService(final JwtProperties properties, final Clock clock) {
    this.signingKey = buildSigningKey(properties.secret());
    this.expirationSeconds = properties.expirationSeconds();
    this.clock = clock;
  }

  public String generate(final UserModel user) {
    final Instant issuedAt = clock.instant();
    final Instant expiration = issuedAt.plusSeconds(expirationSeconds);
    return Jwts.builder()
        .subject(user.getId().value())
        .claim(ROLE_CLAIM, user.getRole().name())
        .issuedAt(Date.from(issuedAt))
        .expiration(Date.from(expiration))
        .signWith(signingKey)
        .compact();
  }

  public JwtPrincipal parse(final String token) {
    final Claims claims =
        Jwts.parser()
            .verifyWith(signingKey)
            .clock(() -> Date.from(clock.instant()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    return new JwtPrincipal(claims.getSubject(), UserRole.fromString(claims.get(ROLE_CLAIM, String.class)));
  }

  public long expirationSeconds() {
    return expirationSeconds;
  }

  /**
   * Construye la llave HMAC a partir del secreto configurado.
   *
   * <p>Si el secreto es Base64 de al menos 32 bytes se usa tal cual (comportamiento original). Si
   * no lo es, por ejemplo el valor aleatorio de 32 caracteres que genera Render, se deriva una llave
   * de 32 bytes con SHA-256 en lugar de impedir que la aplicación arranque.
   */
  static SecretKey buildSigningKey(final String secret) {
    final byte[] decoded = decodeBase64OrNull(secret);
    if (decoded != null && decoded.length >= MINIMUM_KEY_BYTES) {
      return Keys.hmacShaKeyFor(decoded);
    }
    return Keys.hmacShaKeyFor(sha256(secret.getBytes(StandardCharsets.UTF_8)));
  }

  private static byte[] decodeBase64OrNull(final String secret) {
    try {
      return Base64.getDecoder().decode(secret);
    } catch (final IllegalArgumentException notBase64) {
      return null;
    }
  }

  private static byte[] sha256(final byte[] input) {
    try {
      return MessageDigest.getInstance("SHA-256").digest(input);
    } catch (final NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 no está disponible en la JVM.", exception);
    }
  }
}
