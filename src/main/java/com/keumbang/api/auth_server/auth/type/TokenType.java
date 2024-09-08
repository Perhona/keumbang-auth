package com.keumbang.api.auth_server.auth.type;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS("access", 1),
    REFRESH("refresh", 24 * 7);
    private final String name;
    private final int expirationHour;

    TokenType(String name, int expirationHour) {
        this.name = name;
        this.expirationHour = expirationHour;
    }
}
