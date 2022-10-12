package com.srin.reaction_role_bot.events;

import com.srin.reaction_role_bot.database.MessageRoleRepo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class GuildReady extends Event {
    public GuildReady(MessageRoleRepo repo) {
        super(repo);
    }

    public static GuildReady event(MessageRoleRepo repo) {
        return new GuildReady(repo);
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        Guild guild = event.getGuild();
        guild.updateCommands().addCommands(
                Commands.slash("set-reaction-role", "sets the reaction role for the specified messageID and reaction emoji")
                        .addOption(OptionType.STRING, "message-id", "ID of the message you want the reaction role to be set", true)
                        .addOption(OptionType.ROLE, "role", "name of the role to set to user on reaction", true)
                        .addOption(OptionType.STRING, "emoji", "emoji to react to", true)
        ).queue();
    }
}
