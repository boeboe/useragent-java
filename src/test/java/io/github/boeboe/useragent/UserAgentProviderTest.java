package io.github.boeboe.useragent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link UserAgentProvider}.
 */
class UserAgentProviderTest {

  private static final Logger logger = LoggerFactory.getLogger(UserAgentProviderTest.class);
  private UserAgentProvider userAgentProvider;

  @BeforeEach
  void setUp() {
    userAgentProvider = new UserAgentProvider();
  }

  /**
   * Ensures all user agents are successfully loaded.
   */
  @Test
  void testLoadAllUserAgents() {
    Set<UserAgent> allUserAgents = userAgentProvider.getAllUserAgents();
    assertNotNull(allUserAgents, "User agents list should not be null");
    assertFalse(allUserAgents.isEmpty(), "Expected allUserAgents to be non-empty");
    logger.info("Loaded {} user agents", allUserAgents.size());
  }

  /**
   * Ensures latest user agents are successfully loaded.
   */
  @Test
  void testLoadLatestUserAgents() {
    Set<UserAgent> latestUserAgents = userAgentProvider.getLatestUserAgents();
    assertNotNull(latestUserAgents, "Latest user agents list should not be null");
    assertFalse(latestUserAgents.isEmpty(), "Expected latestUserAgents to be non-empty");
    logger.info("Loaded {} latest user agents", latestUserAgents.size());
  }

  /**
   * Ensures JSON files from the resources are loaded and processed.
   */
  @Test
  void testUserAgentsAreLoadedFromResources() {
    Set<UserAgent> allUserAgents = userAgentProvider.getAllUserAgents();
    Set<UserAgent> latestUserAgents = userAgentProvider.getLatestUserAgents();

    assertFalse(allUserAgents.isEmpty(), "Expected allUserAgents to be populated");
    assertFalse(latestUserAgents.isEmpty(), "Expected latestUserAgents to be populated");

    logger.info("All user agents loaded: {}", allUserAgents.size());
    logger.info("Latest user agents loaded: {}", latestUserAgents.size());
  }

  /**
   * Ensures that user agent collections returned are immutable.
   */
  @Test
  void testUnmodifiableUserAgentCollections() {
    Set<UserAgent> allUserAgents = userAgentProvider.getAllUserAgents();
    Set<UserAgent> latestUserAgents = userAgentProvider.getLatestUserAgents();

    assertThrows(UnsupportedOperationException.class,
        () -> allUserAgents.add(new UserAgent("Fake UA", DeviceFilter.ANDROID)));
    assertThrows(UnsupportedOperationException.class,
        () -> latestUserAgents.add(new UserAgent("Fake UA", DeviceFilter.ANDROID)));
  }

  /**
   * Ensures that latest user agents are a subset of all user agents.
   */
  @Test
  void testLatestUserAgentsAreSubsetOfAllUserAgents() {
    Set<UserAgent> allUserAgents = userAgentProvider.getAllUserAgents();
    Set<UserAgent> latestUserAgents = userAgentProvider.getLatestUserAgents();

    assertFalse(allUserAgents.isEmpty(), "Expected allUserAgents to be populated");
    assertFalse(latestUserAgents.isEmpty(), "Expected latestUserAgents to be populated");

    // Ensure every latest user agent exists in the all user agents list
    assertTrue(allUserAgents.containsAll(latestUserAgents), "Latest user agents must be a subset of all user agents");

    logger.info("Verified latest user agents are a subset of all user agents.");
  }

  /**
   * Ensures that loaded user agents contain expected data.
   */
  @Test
  void testValidUserAgentsAreParsed() {
    Set<UserAgent> allUserAgents = userAgentProvider.getAllUserAgents();
    assertFalse(allUserAgents.isEmpty(), "Expected at least some user agents to be loaded");

    for (UserAgent ua : allUserAgents) {
      assertNotNull(ua.getUserAgent(), "User agent string should not be null");
      assertNotNull(ua.getDevice(), "Device should not be null");
    }

    logger.info("User agents parsed correctly with valid attributes.");
  }

  /**
   * Ensures that random user agents are returned.
   */
  @Test
  void testGetRandomUserAgentReturnsValidAgent() {
    UserAgent randomAgent = userAgentProvider.getRandomUserAgent();
    assertNotNull(randomAgent, "Random user agent should not be null");
    logger.info("Random user agent: {}", randomAgent.getUserAgent());
  }

  /**
   * Ensures that random latest user agents are returned.
   */
  @Test
  void testGetRandomLatestUserAgentReturnsValidAgent() {
    UserAgent randomLatestAgent = userAgentProvider.getRandomLatestUserAgent();
    assertNotNull(randomLatestAgent, "Random latest user agent should not be null");
    logger.info("Random latest user agent: {}", randomLatestAgent.getUserAgent());
  }

  /**
   * Ensures that random user agents can be retrieved by device.
   */
  @Test
  void testGetRandomUserAgentByDevice() {
    for (DeviceFilter device : DeviceFilter.values()) {
      UserAgent randomAgent = userAgentProvider.getRandomUserAgent(device);
      if (randomAgent != null) {
        assertEquals(device, randomAgent.getDevice(), "User agent should match requested device");
        logger.info("Random user agent for {}: {}", device, randomAgent.getUserAgent());
      } else {
        logger.info("No user agent found for {}", device);
      }
    }
  }

  /**
   * Ensures that random latest user agents can be retrieved by device.
   */
  @Test
  void testGetRandomLatestUserAgentByDevice() {
    for (DeviceFilter device : DeviceFilter.values()) {
      UserAgent randomAgent = userAgentProvider.getRandomLatestUserAgent(device);
      if (randomAgent != null) {
        assertEquals(device, randomAgent.getDevice(), "Latest user agent should match requested device");
        logger.info("Random latest user agent for {}: {}", device, randomAgent.getUserAgent());
      } else {
        logger.info("No latest user agent found for {}", device);
      }
    }
  }
}