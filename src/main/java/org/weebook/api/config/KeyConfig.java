package org.weebook.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "app.keys")
public record KeyConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
