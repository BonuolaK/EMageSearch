package com.myproject.v1.viewmodel;

import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthUser extends User {

    public final int ClientId;
    public final int UserId;
    public final String ClientUUId;

    public AuthUser(String username, String password, boolean enabled,
                    boolean accountNonExpired, boolean credentialsNonExpired,
                    boolean accountNonLocked,
                    Collection authorities, Integer clientId, String clientUUId, int userId) {

        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);

        this.ClientId = clientId;
        this.ClientUUId = clientUUId;
        this.UserId = userId;
    }

}