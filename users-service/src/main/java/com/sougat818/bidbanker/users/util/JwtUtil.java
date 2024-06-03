package com.sougat818.bidbanker.users.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

import static java.security.Security.addProvider;

@Component
public class JwtUtil {

    @Value("${jwt.private-key}")
    private String privateKeyString;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private PrivateKey privateKey;

    @PostConstruct
    public void init() throws Exception {
        // https://stackoverflow.com/questions/6559272/algid-parse-error-not-a-sequence
        addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        privateKey = loadPrivateKey(privateKeyString);

    }

    public String generateToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private PrivateKey loadPrivateKey(String key) throws Exception {

        String privateKeyPEM = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] encoded = java.util.Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return keyFactory.generatePrivate(keySpec);
    }
}