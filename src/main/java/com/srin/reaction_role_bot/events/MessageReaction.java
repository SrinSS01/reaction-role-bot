package com.srin.reaction_role_bot.events;

import com.srin.reaction_role_bot.database.MessageRole;
import com.srin.reaction_role_bot.database.MessageRoleRepo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MessageReaction extends Event {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReaction.class);
    public MessageReaction(MessageRoleRepo repo) {
        super(repo);
    }

    public static MessageReaction event(MessageRoleRepo repo) {
        return new MessageReaction(repo);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        try {
            long messageIdLong = event.getMessageIdLong();
            EmojiUnion emote = event.getEmoji();
            String emoji = emote.getAsReactionCode();
            emoji = switch (emote.getType()) {
                case CUSTOM -> "<:%s>".formatted(emoji);
                case UNICODE -> emoji;
            };
            String finalEmoji = emoji;
            repo.findById(new MessageRole.ID(messageIdLong, emoji)).ifPresent(messageRole -> {
                MessageRole.ID id = messageRole.getId();
                if (finalEmoji.contentEquals(id.getEmoji())) {
                    LOGGER.info("Adding role to user");
                    long roleId = messageRole.getRoleId();
                    Guild guild = event.getGuild();
                    Member member = Objects.requireNonNull(event.getMember());
                    Role role = Objects.requireNonNull(guild.getRoleById(roleId));
                    guild.addRoleToMember(
                            member,
                            role
                    ).queue(v -> LOGGER.info("Added role {} to user {}", role.getName(), member.getUser().getName()));
                }
            });
        } catch (InsufficientPermissionException | HierarchyException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        try {
            long messageIdLong = event.getMessageIdLong();
            EmojiUnion emote = event.getEmoji();
            String emoji = emote.getAsReactionCode();
            emoji = switch (emote.getType()) {
                case CUSTOM -> "<:%s>".formatted(emoji);
                case UNICODE -> emoji;
            };
            String finalEmoji = emoji;
            repo.findById(new MessageRole.ID(messageIdLong, emoji)).ifPresent(messageRole -> {
                MessageRole.ID id = messageRole.getId();
                if (finalEmoji.contentEquals(id.getEmoji())) {
                    LOGGER.info("Removing role from user");
                    long roleId = messageRole.getRoleId();
                    Guild guild = event.getGuild();
                    Member member = Objects.requireNonNull(event.getMember());
                    Objects.requireNonNull(member)
                            .getRoles()
                            .stream()
                            .filter(role -> role.getIdLong() == roleId)
                            .findFirst()
                            .ifPresent(role ->
                                    guild.removeRoleFromMember(member, role)
                                        .queue(v -> LOGGER.info("Removed role {} from user {}", role.getName(), member.getUser().getName()))
                            );
                }
            });
        } catch (InsufficientPermissionException | HierarchyException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
