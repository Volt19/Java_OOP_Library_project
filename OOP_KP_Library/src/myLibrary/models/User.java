package myLibrary.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity representing a system user.
 * Follows the Principle of Least Privilege.
 */
public class User implements Serializable {
    private final String username;
    private final String password;
    private final UserRole role;

    public enum UserRole {
        ADMIN,
        REGULAR_USER
    }

    public User(String username, String password, UserRole role) {
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        this.role = Objects.requireNonNull(role, "Role cannot be null");
    }

    public static User createAdmin(String username, String password) {
        return new User(username, password, UserRole.ADMIN);
    }

    public static User createRegularUser(String username, String password) {
        return new User(username, password, UserRole.REGULAR_USER);
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean hasPermission(Permission permission) {
        return permission.isAllowedFor(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
