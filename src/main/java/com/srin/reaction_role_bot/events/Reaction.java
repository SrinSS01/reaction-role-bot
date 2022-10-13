package com.srin.reaction_role_bot.events;

import com.srin.reaction_role_bot.ReactionRoleBotApplication.Config;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@AllArgsConstructor
public class Reaction extends ListenerAdapter {
    Config config;
    private static final Logger LOGGER = LoggerFactory.getLogger(Reaction.class);

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUserIdLong() == event.getJDA().getSelfUser().getIdLong()) return;
        String messageId = event.getMessageId();
        String channelId = event.getChannel().getId();

        var reactions = config.getReactions();
        var messageEmoteMap = reactions.get(channelId);
        if (messageEmoteMap == null) {
            return;
        }
    
        var info = messageEmoteMap.get(messageId);
        if (info != null) {
            EmojiUnion emote = event.getEmoji();
            String emoji = switch (emote.getType()) {
                case CUSTOM -> emote.asCustom().getAsMention();
                case UNICODE -> emote.asUnicode().getAsCodepoints();
            };
            LOGGER.info("added emoji: {} from messageId: {}", emoji, messageId);
            String roleId = info.get(emoji);
            if (roleId != null) {
                Guild guild = event.getGuild();
                Member member = Objects.requireNonNull(event.getMember());
                Role roleById = Objects.requireNonNull(guild.getRoleById(roleId));
                guild.addRoleToMember(member, roleById).queue();
            }
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (event.getUserIdLong() == event.getJDA().getSelfUser().getIdLong()) return;
        String messageId = event.getMessageId();
        String channelId = event.getChannel().getId();

        var reactions = config.getReactions();
        var messageEmoteMap = reactions.get(channelId);
        if (messageEmoteMap == null) {
            return;
        }

        var info = messageEmoteMap.get(messageId);
        if (info != null) {
            EmojiUnion emote = event.getEmoji();
            String emoji = switch (emote.getType()) {
                case CUSTOM -> emote.asCustom().getAsMention();
                case UNICODE -> emote.asUnicode().getAsCodepoints();
            };
            LOGGER.info("removed emoji: {} from messageId: {}", emoji, messageId);
            String roleId = info.get(emoji);
            if (roleId != null) {
                Guild guild = event.getGuild();
                Member member = Objects.requireNonNull(event.getMember());
                Role roleById = Objects.requireNonNull(guild.getRoleById(roleId));
                guild.removeRoleFromMember(member, roleById).queue();
            }
        }
    }
}
