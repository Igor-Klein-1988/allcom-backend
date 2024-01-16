package de.allcom.models;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public enum Role {

    ADMIN,
    STOREKEEPER,
    CLIENT;

    public SimpleGrantedAuthority getAuthorities() {
        return new SimpleGrantedAuthority(this.name());
    }

}
