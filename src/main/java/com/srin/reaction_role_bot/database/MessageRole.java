package com.srin.reaction_role_bot.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "reaction_bot")
@AllArgsConstructor
@NoArgsConstructor
public class MessageRole {
    @Id @Getter @Setter
    private ID id;

    @Field
    @Getter @Setter
    private long roleId;

    public static MessageRole create(long messageId, long roleId, String emoji) {
        return new MessageRole(new ID(messageId, emoji), roleId);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class ID implements Serializable {
        @Getter @Setter
        private long messageId;
        @Getter @Setter
        private String emoji;
    }
}
