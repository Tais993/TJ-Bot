package org.togetherjava.tjbot.formatter.tokenizer;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Class representing a Token with a given content and a type
 */
public record Token(String content, TokenType type) {
    private static final Set<TokenType> displayTypes =
            Set.of(TokenType.IDENTIFIER, TokenType.UNKNOWN, TokenType.STRING, TokenType.COMMENT);

    @Override
    public @NotNull String toString() {
        return type().name() + formatForDisplay();
    }

    /**
     * Returns a non-empty string if this token has something to display
     *
     * @return the displayed value
     */
    private @NotNull String formatForDisplay() {
        if (displayTypes.contains(type())) {
            return "(" + content() + ")";
        }

        return "";
    }
}
