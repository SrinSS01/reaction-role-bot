package com.srin.reaction_role_bot;

import com.srin.reaction_role_bot.database.MessageRoleRepo;
import com.srin.reaction_role_bot.events.GuildReady;
import com.srin.reaction_role_bot.events.MessageReaction;
import com.srin.reaction_role_bot.events.SlashCommandInteraction;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class ReactionRoleBotApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReactionRoleBotApplication.class);
	@Value("${bot-token}")
	private String token;

	public static void main(String[] args) throws IOException {
		File config = new File("config");
		if (!config.exists()) {
			boolean result = config.mkdirs();
			File properties = new File("config/application.properties");
			if (result) {
				LOGGER.info("Created config directory");
				try (FileWriter writer = new FileWriter(properties)) {
					writer.write("""
                            bot-token=token
                            """);
					writer.flush();
					LOGGER.info("Created application.properties file");
				}
			} else {
				LOGGER.error("Failed to create config directory");
			}
			return;
		}
		SpringApplication.run(ReactionRoleBotApplication.class, args);
	}

	@Bean
	CommandLineRunner init(MessageRoleRepo repo) {
		LOGGER.info("Started bot with token: {}", token);
		return args -> {
			var jda = JDABuilder.createDefault(token)
					.enableIntents(
							GatewayIntent.GUILD_MEMBERS,
							GatewayIntent.GUILD_MESSAGE_REACTIONS,
							GatewayIntent.GUILD_MESSAGES
					).setMemberCachePolicy(MemberCachePolicy.ALL)
					.setActivity(Activity.playing("reaction roles"))
					.addEventListeners(
							GuildReady.event(repo),
							MessageReaction.event(repo),
							SlashCommandInteraction.event(repo)
					).build();
			new Thread(() -> {
				Scanner sc = new Scanner(System.in);
				while (true) {
					String input = sc.nextLine();
					if (input.equalsIgnoreCase("stop")) {
						jda.shutdownNow();
						break;
					}
				}
			}, "Stop Thread").start();
		};
	}
}
