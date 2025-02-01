package io.github.boeboe.useragent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAgentProviderIT {

  private static final Logger logger = LoggerFactory.getLogger(UserAgentProviderIT.class);

  private UserAgentProvider userAgentProvider;

  @BeforeEach
  void setUp() {
    userAgentProvider = new UserAgentProvider();
  }

  @Test
  void testGetRandomUserAgent() {
    logger.info("Integration Test: Get random user agent");
    UserAgent userAgent = userAgentProvider.getRandomUserAgent();
    assertNotNull(userAgent);
    assertNotNull(userAgent.getUseragent());
    assertNotNull(userAgent.getDevice());
    logger.info("Random User Agent: {}", userAgent.getUseragent());
    assertTrue(userAgent.getUseragent().contains("Mozilla"), "User agent should contain 'Mozilla'");
  }

  @Test
  void testGetRandomLatestUserAgent() {
    logger.info("Integration Test: Get random latest user agent");
    UserAgent userAgent = userAgentProvider.getRandomLatestUserAgent();
    assertNotNull(userAgent);
    assertNotNull(userAgent.getUseragent());
    assertNotNull(userAgent.getDevice());
    logger.info("Latest User Agent: {}", userAgent.getUseragent());
    assertTrue(userAgent.getUseragent().contains("Mozilla"), "User agent should contain 'Mozilla'");
  }

  @Test
  void testGetRandomUserAgentByDevice() {
    logger.info("Integration Test: Get random user agent by device");
    for (Device device : Device.values()) {
      logger.info("Testing device: {}", device);
      UserAgent userAgent = userAgentProvider.getRandomUserAgent(device);
      assertNotNull(userAgent);
      assertEquals(device, userAgent.getDevice());
      logger.info("Random User Agent for {}: {}", device, userAgent.getUseragent());
      assertTrue(userAgent.getUseragent().contains("Mozilla"), "User agent should contain 'Mozilla'");
    }
  }

  @Test
  void testGetRandomLatestUserAgentByDevice() {
    logger.info("Integration Test: Get random latest user agent by device");
    for (Device device : Device.values()) {
      logger.info("Testing device: {}", device);
      UserAgent userAgent = userAgentProvider.getRandomLatestUserAgent(device);
      assertNotNull(userAgent);
      assertEquals(device, userAgent.getDevice());
      logger.info("Latest User Agent for {}: {}", device, userAgent.getUseragent());
      assertTrue(userAgent.getUseragent().contains("Mozilla"), "User agent should contain 'Mozilla'");
    }
  }
}