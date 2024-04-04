package com.valeria.demo.db.entity;

/*import org.springframework.security.core.GrantedAuthority;*/

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_USER, ROLE_COMPANY;

    @Override
    public String getAuthority() {
        return name();
    }
}
