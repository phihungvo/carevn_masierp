package com.carevn.masi.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    //Roles
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    //Permissions
    public static final String TimeKeepingRead = "PERMISSION.TIME_KEEPING.READ";

    private AuthoritiesConstants() {}
}
