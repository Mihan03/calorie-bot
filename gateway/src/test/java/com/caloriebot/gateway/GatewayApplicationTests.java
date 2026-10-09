package com.caloriebot.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"telegram.bot.token=123456:TEST_TOKEN",
		"telegrambots.enabled=false"
})
class GatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
