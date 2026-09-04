package com.ishan.Ecomm.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
   private final String SECRET_KEY_STRING = "MySuperSecretKeyForEcommerceAppWhichIsAtLeast32BytesLong!";
   private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

   public String generateToken(String email, String role) {
      return Jwts.builder()
            .subject(email)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
            .signWith(SECRET_KEY)
            .compact();
   }

   public String extractEmail(String token) {
      return extractAllClaims(token).getSubject();
   }

   public boolean isTokenValid(String token) {
      return !isTokenExpired(token);
   }

   private boolean isTokenExpired(String token) {
      return extractAllClaims(token).getExpiration().before(new Date());
   }

   private Claims extractAllClaims(String token) {// extractAllClaims() method ka kaam hai token ke andar ki asli
                                                  // jankari (payload) ko bahar nikalna.
      return Jwts.parser()// Yeh library ki ek reading machine (parser) chalu kar raha hai jiska kaam hai
                          // ajeeb se dikhne wale token ko padhna (decode karna).
            .verifyWith(SECRET_KEY)// Check karo ki kya ye token sach mein isi chabi se lock hua tha
            .build()
            .parseSignedClaims(token)// Ab is tayaar machine ke andar hum wo actual token (String) dal rahe hain.
                                     // Machine usey kholti hai aur check karti hai ki token ka lock (signature) sahi
                                     // hai ya nahi.
            .getPayload();// Jo data (jaise email, date) nikla usey getPayload() se nikal lete hain.
   }

}
