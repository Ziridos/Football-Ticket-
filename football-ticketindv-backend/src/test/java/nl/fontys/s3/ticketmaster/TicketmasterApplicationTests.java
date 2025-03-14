package nl.fontys.s3.ticketmaster;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TicketmasterApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring application context can start successfully.
		// An empty implementation is sufficient as the test will fail if context loading fails.
	}

}
