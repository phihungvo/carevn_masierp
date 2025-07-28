package com.carevn.masi.constants;

import java.util.Map;

public final class AuthoritiesConstants {

    //Roles
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String DIRECTOR = "ROLE_DIRECTOR";
    public static final String DEPARTMENT_MANAGER = "ROLE_DEPARTMENT_MANAGER";
    public static final String STAFF = "ROLE_STAFF";
    public static final String WORKER = "ROLE_WORKER";

    private static final Map<String, Integer> ROLES_PRIORITY = Map.of(
            ADMIN, 0,
            DIRECTOR, 10,
            DEPARTMENT_MANAGER, 20,
            STAFF, 30,
            WORKER, 30,
            USER, 50,
            ANONYMOUS, 100
    );

    private AuthoritiesConstants() {
    }

    public static boolean isHigherPriority(String role1, String compareToRole) {
        return ROLES_PRIORITY.getOrDefault(role1,500) <= ROLES_PRIORITY.getOrDefault(compareToRole,10);
    }
}
