package org.togetherjava.tjbot.commands.system;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Wrapper for component IDs, see
 * {@link org.togetherjava.tjbot.commands.SlashCommand#onSlashCommand(SlashCommandEvent)} for its
 * usages.
 * <p>
 * {@link ComponentIds} can be used to generate and parse instances of this class.
 */
public final class ComponentId {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);
    private final int id;
    private final String commandName;
    private final List<String> elements;

    /**
     * Creates a new component ID with the given data.
     *
     * @param commandName the name of the command that corresponds to this component ID
     * @param elements the extra elements contained in this component ID, may be empty
     */
    public ComponentId(@NotNull String commandName, @NotNull List<String> elements) {
        this(NEXT_ID.getAndIncrement(), commandName, elements);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    private ComponentId(@JsonProperty("id") int id,
            @JsonProperty("commandName") @NotNull String commandName,
            @JsonProperty("elements") @NotNull List<String> elements) {
        this.id = id;
        this.commandName = commandName;
        this.elements = elements;
    }

    /**
     * Gets the name of the command that corresponds to this component ID.
     *
     * @return the name of the command
     */
    @Contract(pure = true)
    public @NotNull String getCommandName() {
        return commandName;
    }

    /**
     * Gets the ID of this component ID, which is unique within the context of the message the
     * component belongs to.
     *
     * @return the ID
     */
    @Contract(pure = true)
    public int getId() {
        return id;
    }

    /**
     * Gets the extra elements contained in this component ID
     *
     * @return the extra elements
     */
    @Contract(pure = true)
    @UnmodifiableView
    public @NotNull List<String> getElements() {
        return Collections.unmodifiableList(elements);
    }
}
