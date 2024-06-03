package com.sougat818.bidbanker.auctions.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {


    @Value("${jwt.public-key}")
    private String publicKeyString;

    @Value("${jwt.issuer}")
    private String issuer;

    @Bean
    public PublicKey publicKey() throws Exception {
        String publicKeyPEM = publicKeyString
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/users/register").permitAll()
                        .pathMatchers("/auth/login").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {
                            try {
                                jwt.jwtDecoder(jwtDecoder());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                );

        return http.build();
    }

    @Bean
    public NimbusReactiveJwtDecoder jwtDecoder() throws Exception {
        return NimbusReactiveJwtDecoder.withPublicKey((RSAPublicKey) publicKey()).build();
    }
}