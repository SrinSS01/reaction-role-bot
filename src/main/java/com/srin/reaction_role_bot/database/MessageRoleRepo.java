package com.srin.reaction_role_bot.database;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRoleRepo extends MongoRepository<MessageRole, MessageRole.ID> {
}
