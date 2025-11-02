package com.scalar.ticketing.app.springboot_crud.domain.model.enums;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    ADMIN(Set.of(
        UserPermission.EVENT_CREATE,
        UserPermission.EVENT_UPDATE,
        UserPermission.EVENT_DELETE,
        UserPermission.EVENT_READ_ALL,
        UserPermission.EVENT_READ_ONE,
        UserPermission.TICKET_MANAGE
    )),

    CUSTOMER(Set.of(
        UserPermission.EVENT_READ_ALL,
        UserPermission.EVENT_READ_ONE,
        UserPermission.TICKET_BUY
    )),

    ORGANIZER(Set.of(
        UserPermission.EVENT_CREATE,
        UserPermission.EVENT_UPDATE,
        UserPermission.EVENT_READ_ALL,
        UserPermission.EVENT_READ_ONE
    ));

     private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

     /**
     * Convierte los permisos en authorities que Spring Security entiende.
     */
    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.name()))
                .collect(Collectors.toSet());

        // También agrega el prefijo ROLE_ para compatibilidad con hasRole()
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}