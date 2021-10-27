package org.togetherjava.tjbot.logwatcher.accesscontrol;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Basic Roles for Access Control on Views
 */
public enum Role {
    /**
     * Role for when Stuff goes wrong
     */
    UNKNOWN(0, "unknown"),

    /**
     * Base Role
     */
    USER(1, "user"),

    /**
     * Role for Views that should require more permissions
     */
    ADMIN(2, "admin");

    private final int id;
    @NotNull
    private final String roleName;

    @Contract(pure = true)
    Role(int id, @NotNull String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    @Contract(pure = true)
    public int getId() {
        return id;
    }

    @Contract(pure = true)
    public @NotNull String getRoleName() {
        return roleName;
    }

    @NotNull
    public static Role forID(final int id) {
        return Arrays.stream(values())
            .filter(r -> r.id == id)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Unknown RoleID: %d".formatted(id)));
    }

    @Contract(" -> new")
    public static @NotNull Set<Role> getDisplayableRoles() {
        return EnumSet.of(USER, ADMIN);
    }

}
