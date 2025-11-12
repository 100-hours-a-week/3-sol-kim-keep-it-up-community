package com.project.community.util;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    @NotNull
    private String secret;

    @NotNull
    private int accessTtl = 15 * 60;

    @NotNull
    private int refreshTtl = 14 * 24 * 3600;
}