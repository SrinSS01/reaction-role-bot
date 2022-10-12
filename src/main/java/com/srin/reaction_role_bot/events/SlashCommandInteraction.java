package com.srin.reaction_role_bot.events;

import com.srin.reaction_role_bot.database.MessageRole;
import com.srin.reaction_role_bot.database.MessageRoleRepo;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SlashCommandInteraction extends Event {
    public SlashCommandInteraction(MessageRoleRepo repo) {
        super(repo);
    }

    public static SlashCommandInteraction event(MessageRoleRepo repo) {
        return new SlashCommandInteraction(repo);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        try {
            String commandString = event.getName();
            if (commandString.equals("set-reaction-role")) {
                long messageId = Long.parseLong(Objects.requireNonNull(event.getOption("message-id")).getAsString());
                Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();
                String emoji = Objects.requireNonNull(event.getOption("emoji")).getAsString();
                repo.save(MessageRole.create(messageId, role.getIdLong(), emoji));
                event.replyFormat("added reaction role %s to messageId `%s` for emoji %s", role.getAsMention(), messageId, emoji).queue();
            }
        } catch (Exception ignore) {}
    }
}
