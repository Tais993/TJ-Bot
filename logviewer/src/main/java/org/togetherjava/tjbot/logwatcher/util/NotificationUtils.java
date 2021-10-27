package org.togetherjava.tjbot.logwatcher.util;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Util class for handy Methods regarding Notifications for the Client
 */
public final class NotificationUtils {


    @Contract(pure = true)
    private NotificationUtils() {}

    /**
     * Prepares a little Notification to display an Errormessage, in case anything goes wrong
     *
     * @param e Exception that occurred
     * @return Notification for the user
     */
    public static @NotNull Notification getNotificationForError(final @NotNull Exception e) {
        final Notification not = new Notification();
        not.setDuration((int) TimeUnit.SECONDS.toMillis(6));
        not.setPosition(Notification.Position.MIDDLE);
        not.setText("Exception occurred while saving. %s".formatted(e.getMessage()));
        not.addThemeVariants(NotificationVariant.LUMO_ERROR);
        return not;
    }
}
