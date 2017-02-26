package com.softserve.if072.common.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * This class stores information about user's role. Implements Spring Security {@link GrantedAuthority} interface.
 */
public class Role implements GrantedAuthority {

    private int id;
    private String authority;
    private String description;

    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}