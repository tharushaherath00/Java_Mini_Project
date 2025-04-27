package org.example;

import org.mindrot.jbcrypt.BCrypt;

public class PassGen {
    public static void main(String[] args) {
        String password = "admin";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        System.out.println("Hashed password: " + hashed);
    }
}