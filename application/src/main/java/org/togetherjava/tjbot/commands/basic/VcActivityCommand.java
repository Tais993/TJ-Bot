package org.togetherjava.tjbot.commands.basic;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.togetherjava.tjbot.commands.SlashCommandAdapter;
import org.togetherjava.tjbot.commands.SlashCommandVisibility;

import java.util.List;
import java.util.Objects;

/**
 * Implements the {@code vc-activity} command. Creates VC activities.
 *
 * <p>
 * An VC activity is a so called "Embedded application". To explain it extremely simple, interactive
 * screensharing. <br />
 * To give you a better idea of what it actually is, think about games like Poker, Chess, or
 * watching YouTube Together using one of these. <br />
 */
public final class VcActivityCommand extends SlashCommandAdapter {
    private static final Logger logger = LoggerFactory.getLogger(VcActivityCommand.class);

    private static final String APPLICATION_OPTION = "application";
    private static final String ID_OPTION = "id";

    private static final String MAX_USES_OPTION = "max-uses";
    private static final String MAX_AGE_OPTION = "max-age";

    /**
     * List comes from <a href="https://github.com/DV8FromTheWorld/JDA/pull/1628">the "Implement
     * invite targets" PR on JDA</a>. There is no official list from Discord themselves, so this is
     * our best bet.
     */
    private static final List<Command.Choice> VC_APPLICATIONS =
            List.of(new Command.Choice("YouTube Together", "755600276941176913"),
                    new Command.Choice("Poker", "755827207812677713"),
                    new Command.Choice("Betrayal.io", "773336526917861400"),
                    new Command.Choice("Fishington.io", "814288819477020702"),
                    new Command.Choice("Chess / CG 2 Dev", "832012586023256104"),
                    new Command.Choice("Awkword", "879863881349087252"),
                    new Command.Choice("Spellcast", "852509694341283871"),
                    new Command.Choice("Doodlecrew", "878067389634314250"),
                    new Command.Choice("Wordsnack", "879863976006127627"),
                    new Command.Choice("Lettertile", "879863686565621790"));


    private static final List<OptionData> inviteOptions = List.of(
            new OptionData(OptionType.STRING, MAX_USES_OPTION,
                    "The amount of times the invite can be used, default is infinity", false),
            new OptionData(OptionType.INTEGER, MAX_AGE_OPTION,
                    "Max age in seconds. Set this to 0 to never expire, default is 1 day", false));


    /**
     * Constructs an instance
     *
     * @see VcActivityCommand
     */
    public VcActivityCommand() {
        super("vc-activity",
                "Starts a VC activity (you need to be in an voice channel to run this command)",
                SlashCommandVisibility.GUILD);


        SubcommandData applicationSubCommand =
                new SubcommandData("application", "Choose an application from our list")
                    .addOptions(new OptionData(OptionType.STRING, APPLICATION_OPTION,
                            "the application", true).addChoices(VC_APPLICATIONS))
                    .addOptions(inviteOptions);


        SubcommandData idSubCommand =
                new SubcommandData("id", "specify the ID for the application manually")
                    .addOption(OptionType.STRING, ID_OPTION, "the ID of the application", true)
                    .addOptions(inviteOptions);


        getData().addSubcommands(applicationSubCommand, idSubCommand);
    }


    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Member member = Objects.requireNonNull(event.getMember(), "member is null");
        GuildVoiceState voiceState = Objects.requireNonNull(member.getVoiceState(),
                "Voicestates aren't being cached, check the JDABuilder");


        if (!voiceState.inVoiceChannel()) {
            event.reply("You need to be in a voicechannel to run this command!")
                .setEphemeral(true)
                .queue();

            return;
        }

        VoiceChannel voiceChannel = Objects.requireNonNull(voiceState.getChannel());


        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        if (!selfMember.hasPermission(Permission.CREATE_INSTANT_INVITE)) {
            event.reply("The bot needs the create instant invite permission!")
                .setEphemeral(true)
                .queue();
            logger.warn("Bot doesn't have the create instant permission");
            return;
        }


        OptionMapping applicationOption = event.getOption(APPLICATION_OPTION);
        OptionMapping idOption = event.getOption(ID_OPTION);

        OptionMapping maxUsesOption = event.getOption(MAX_USES_OPTION);
        OptionMapping maxAgeOption = event.getOption(MAX_AGE_OPTION);


        Integer maxUses;

        try {
            maxUses = handleIntegerTypeOption(event, maxUsesOption);
        } catch (IllegalArgumentException ignore) {
            return;
        }

        Integer maxAge;

        try {
            maxAge = handleIntegerTypeOption(event, maxAgeOption);
        } catch (IllegalArgumentException ignore) {
            return;
        }



        OptionMapping usedOption = (applicationOption != null) ? applicationOption : idOption;

        handleSubcommand(event, voiceChannel, usedOption, maxUses, maxAge);
    }

    private static void handleSubcommand(@NotNull SlashCommandEvent event,
            @NotNull VoiceChannel voiceChannel, @NotNull OptionMapping option,
            @Nullable Integer maxUses, @Nullable Integer maxAge) {

        voiceChannel.createInvite()
            .setTargetApplication(option.getAsString())
            .setMaxUses(maxUses)
            .setMaxAge(maxAge)
            .flatMap(invite -> replyInvite(event, invite))
            .queue(null, throwable -> handleErrors(event, throwable));
    }

    private static @NotNull ReplyAction replyInvite(@NotNull SlashCommandEvent event,
            @NotNull Invite invite) {
        return event.reply("""
                I wish you a lot of fun, here's the invite: %s
                If it says the activity ended, click on the URL instead.
                 """.formatted(invite.getUrl()));
    }

    private static void handleErrors(@NotNull SlashCommandEvent event,
            @Nullable Throwable throwable) {
        event.reply("Something went wrong :/").queue();
        logger.warn("Something went wrong in the VcActivityCommand", throwable);
    }


    /**
     * This grabs the OptionMapping, after this it <br />
     * - validates whenever it's within an {@link Integer Integer's} range <br />
     * - validates whenever it's positive <br />
     *
     * <p/>
     *
     * <p>
     * This method throws an {@link IllegalArgumentException} if the option's value is - outside of
     * {@link Integer#MAX_VALUE} - negative
     *
     * @param event the {@link SlashCommandEvent}
     * @param optionMapping the {@link OptionMapping}
     * @return nullable {@link Integer}
     */
    @Contract("_, null -> null")
    private static @Nullable Integer handleIntegerTypeOption(@NotNull SlashCommandEvent event,
            @Nullable OptionMapping optionMapping) {

        int optionValue;

        if (optionMapping == null) {
            return null;
        }

        try {
            optionValue = Math.toIntExact(optionMapping.getAsLong());
        } catch (ArithmeticException e) {
            event
                .reply("The " + optionMapping.getName() + " is above `" + Integer.MAX_VALUE
                        + "`, which is too high")
                .setEphemeral(true)
                .queue();
            throw new IllegalArgumentException(
                    optionMapping.getName() + " can't be above " + Integer.MAX_VALUE);
        }

        if (optionValue < 0) {
            event.reply("The " + optionMapping.getName() + " is negative, which isn't supported")
                .setEphemeral(true)
                .queue();
            throw new IllegalArgumentException(optionMapping.getName() + " can't be negative");
        }


        return optionValue;
    }
}
