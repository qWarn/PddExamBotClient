package ru.qwarn.pddexambotclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.qwarn.pddexambotclient.bot.controllers.WebhookController;

import static org.springframework.util.Assert.*;

@SpringBootTest
class PddExamBotClientApplicationTests {

	@Autowired
	private WebhookController webhookController;


	@Test
	void contextLoads() {
		notNull(webhookController, "Context loads failed.");
	}

}
