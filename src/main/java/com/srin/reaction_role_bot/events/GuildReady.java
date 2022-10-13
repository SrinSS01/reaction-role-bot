package com.srin.reaction_role_bot.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.srin.reaction_role_bot.ReactionRoleBotApplication;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@AllArgsConstructor
public class GuildReady extends ListenerAdapter {
    ReactionRoleBotApplication.Config config;
    private static final Logger LOGGER = LoggerFactory.getLogger(GuildReady.class);

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        Guild guild = event.getGuild();
        config.getReactions().forEach((channelId, messageEmoteMap) -> {
            TextChannel textChannel = guild.getTextChannelById(channelId);
            if (textChannel != null) {
                messageEmoteMap.forEach((messageId, emoteRoleMap) ->
                        emoteRoleMap.forEach((emote, roleId) -> {
                            System.out.println(emote);
                                    textChannel.addReactionById(
                                            messageId,
                                            Emoji.fromFormatted(emote)
                                    ).onErrorMap(err -> {
                                        LOGGER.error("{} for message ID: {}", err.getMessage(), messageId);
                                        return null;
                                    }).queue();
                                }
                        )
                );
            }
        });
    }
}
