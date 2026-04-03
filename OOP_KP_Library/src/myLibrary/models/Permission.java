package myLibrary.models;

/**
 * Enumeration of system permissions with role-based access control.
 */
public enum Permission {
    ADD_BOOK(User.UserRole.ADMIN),
    REMOVE_BOOK(User.UserRole.ADMIN),
    ADD_USER(User.UserRole.ADMIN),
    REMOVE_USER(User.UserRole.ADMIN),
    VIEW_ALL_BOOKS(User.UserRole.REGULAR_USER, User.UserRole.ADMIN),
    VIEW_BOOK_DETAILS(User.UserRole.REGULAR_USER, User.UserRole.ADMIN),
    SEARCH_BOOKS(User.UserRole.REGULAR_USER, User.UserRole.ADMIN),
    SORT_BOOKS(User.UserRole.REGULAR_USER, User.UserRole.ADMIN),
    LOGOUT(User.UserRole.REGULAR_USER, User.UserRole.ADMIN);

    private final User.UserRole[] allowedRoles;

    Permission(User.UserRole... allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    public boolean isAllowedFor(User.UserRole role) {
        for (User.UserRole allowedRole : allowedRoles) {
            if (allowedRole == role) {
                return true;
            }
        }
        return false;
    }
}
