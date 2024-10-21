package org.sunbong.board_api1.security.auth;

import java.security.Principal;

public class CustomuserPrincipal implements Principal {

    private final String email;

    public CustomuserPrincipal(final String email) {
        this.email = email;
    }

    @Override
    public String getName() {
        return email;
    }
}
