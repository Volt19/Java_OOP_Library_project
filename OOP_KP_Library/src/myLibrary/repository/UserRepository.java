package myLibrary.repository;

import myLibrary.models.User;

import java.io.Serializable;
import java.util.*;

/**
 * Repository pattern for user data management.
 */
public class UserRepository  implements Serializable {
    private final Map<String, User> usersByUsername;

    public UserRepository() {
        this.usersByUsername = new HashMap<>();
    }

    public void addDefaultAdmin() {
        // Create default admin user: username="admin", password="iamsa", role=ADMIN
        User admin = new User("admin", "iamsa", User.UserRole.ADMIN);
        usersByUsername.put(admin.getUsername(), admin);
    }

    public void add(User user) {
        if (usersByUsername.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("User " + user.getUsername() + " already exists");
        }
        usersByUsername.put(user.getUsername(), user);
    }

    public void remove(String username) {
        if ("admin".equals(username)) {
            throw new IllegalArgumentException("Cannot remove default admin user");
        }
        if (!usersByUsername.containsKey(username)) {
            throw new IllegalArgumentException("User " + username + " not found");
        }
        usersByUsername.remove(username);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    public List<User> findAll() {
        return new ArrayList<>(usersByUsername.values());
    }

    public boolean authenticate(String username, String password) {
        return findByUsername(username)
                .map(user -> user.authenticate(password))
                .orElse(false);
    }
}