package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority {
    ADMIN("Директор"),
    MANAGER("Менеджер"),
    USER("Сотрудник"),
    ;

    private final String name;

    @Override
    public String getAuthority() {
        return name();
    }
}

