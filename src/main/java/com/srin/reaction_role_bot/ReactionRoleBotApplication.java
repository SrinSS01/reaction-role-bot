package com.srin.reaction_role_bot;

import com.srin.reaction_role_bot.events.GuildReady;
import com.srin.reaction_role_bot.events.Reaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class ReactionRoleBotApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReactionRoleBotApplication.class);

	public static void main(String[] args) throws IOException {
		File config = new File("config");
		if (!config.exists()) {
			boolean result = config.mkdirs();
			File properties = new File("config/application.yml");
			if (result) {
				LOGGER.info("Created config directory");
				try (FileWriter writer = new FileWriter(properties)) {
					writer.write("""
						bot:
						 token: "token"
						 reactions:
						   "channelId":
							 "messageId":
							   "[custom-Emote]": "roleId"
							   "[unicode-Emote]": "roleId"
					""");
					writer.flush();
					LOGGER.info("Created application.yml file");
				}
			} else {
				LOGGER.error("Failed to create config directory");
			}
			return;
		}
		SpringApplication.run(ReactionRoleBotApplication.class, args);
	}

	@AllArgsConstructor
	@Component
	private static class Runner implements CommandLineRunner {
		Config config;
		@Override
		public void run(String... args) {
			LOGGER.info("map: {}", config.getReactions());
			LOGGER.info("Started bot with token: {}", config.getToken());
			var jda = JDABuilder.createDefault(config.getToken())
					.enableIntents(
							GatewayIntent.GUILD_MEMBERS,
							GatewayIntent.GUILD_MESSAGE_REACTIONS,
							GatewayIntent.GUILD_MESSAGES
					).setMemberCachePolicy(MemberCachePolicy.ALL)
					.addEventListeners(
							new Reaction(config),
							new GuildReady(config)
					)
					.setActivity(Activity.playing("reaction roles")).build();
			new Thread(() -> {
				try (Scanner sc = new Scanner(System.in)) {
					while (true) {
						String input = sc.nextLine();
						if (input.equalsIgnoreCase("stop")) {
							jda.shutdownNow();
							break;
						}
					}
				}
			}, "Stop Thread").start();
		}
	}

	@Component
	@EnableConfigurationProperties
	@ConfigurationProperties("bot")
	@Getter @Setter
	public static class Config {
		private String token;
		private Map<String, Map<String, Map<String, String>>> reactions = new HashMap<>();
	}
}
