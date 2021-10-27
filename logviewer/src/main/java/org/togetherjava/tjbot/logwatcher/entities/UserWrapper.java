package org.togetherjava.tjbot.logwatcher.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.togetherjava.tjbot.logwatcher.accesscontrol.Role;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class UserWrapper implements Serializable {
    @Serial
    private static final long serialVersionUID = -3701246411434315431L;

    private long discordID;
    private String userName;
    private Set<Role> roles = Collections.emptySet();

    public UserWrapper() {}

    public UserWrapper(long discordID, String userName, Set<Role> roles) {
        this.discordID = discordID;
        this.userName = userName;
        this.roles = roles;
    }

    @Contract(mutates = "this")
    public void setDiscordID(long discordID) {
        this.discordID = discordID;
    }

    @Contract(mutates = "this")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Contract(mutates = "this")
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Contract(pure = true)
    public long getDiscordID() {
        return discordID;
    }

    @Contract(pure = true)
    public String getUserName() {
        return userName;
    }

    @Contract(pure = true)
    public Set<Role> getRoles() {
        return roles;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserWrapper other)) {
            return true;
        }
        return this.discordID == other.discordID && Objects.equals(this.userName, other.userName)
                && Objects.equals(this.roles, other.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discordID, userName, roles);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "UserDTO[" + "discordID=" + discordID + ", " + "userName=" + userName + ", "
                + "roles=" + roles + ']';
    }


}
