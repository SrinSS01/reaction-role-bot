package com.srin.reaction_role_bot.events;

import com.srin.reaction_role_bot.database.MessageRoleRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@RequiredArgsConstructor
public abstract class Event extends ListenerAdapter {
    @Getter
    protected final MessageRoleRepo repo;
}
