package org.example;

public enum Role {
    ADMIN, STUDENT, TEACHER;

    public static Role fromString(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
