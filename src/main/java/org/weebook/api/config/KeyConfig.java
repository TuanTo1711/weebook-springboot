package org.weebook.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(value = "app.key")
public record KeyConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
