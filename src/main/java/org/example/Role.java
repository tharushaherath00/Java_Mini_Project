package org.example;

public enum Role {
    ADMIN("admin"),
    LECTURER("Lecturer"),
    DEAN("Dean"),
    STUDENT("Student"),
    TECHNICAL_OFFICER("Technical Officer");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.getValue().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }
}