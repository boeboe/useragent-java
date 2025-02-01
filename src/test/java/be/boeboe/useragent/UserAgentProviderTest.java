package be.boeboe.useragent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserAgentProviderTest {

  private static final Logger logger = LoggerFactory.getLogger(UserAgentProviderTest.class);

  private UserAgentProvider userAgentProvider;
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @TempDir
  Path tempDir; // Temporary directory for mock JSON files

  @BeforeEach
  void setUp() throws IOException {
    userAgentProvider = new UserAgentProvider();

    // Mock JSON data
    List<UserAgent> testUserAgents = List.of(
        new UserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/100.0.0.0", Device.WINDOWS),
        new UserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) Safari/537.36", Device.MACOS));

    // Create mock latest and random directories
    Files.createDirectories(tempDir.resolve("latest"));
    Files.createDirectories(tempDir.resolve("random"));

    // Write test JSON files
    writeJsonFile(tempDir.resolve("latest/windows.json"), testUserAgents);
    writeJsonFile(tempDir.resolve("random/windows.json"), testUserAgents);
  }

  @Test
  void testGetRandomUserAgent() {
    logger.info("Test get random user agent");
    UserAgent userAgent = userAgentProvider.getRandomUserAgent();
    assertNotNull(userAgent);
    assertTrue(userAgent.getUseragent().contains("Mozilla"));
  }

  @Test
  void testGetRandomLatestUserAgent() {
    logger.info("Test get random latest user agent");
    UserAgent userAgent = userAgentProvider.getRandomLatestUserAgent();
    assertNotNull(userAgent);
    assertTrue(userAgent.getUseragent().contains("Mozilla"));
  }

  @Test
  void testGetRandomUserAgentByDevice() {
    logger.info("Test get random user agent by device");
    UserAgent userAgent = userAgentProvider.getRandomUserAgent(Device.WINDOWS);
    assertNotNull(userAgent);
    assertEquals(Device.WINDOWS, userAgent.getDevice());
  }

  @Test
  void testGetRandomLatestUserAgentByDevice() {
    logger.info("Test get random latest user agent by device");
    UserAgent userAgent = userAgentProvider.getRandomLatestUserAgent(Device.WINDOWS);
    assertNotNull(userAgent);
    assertEquals(Device.WINDOWS, userAgent.getDevice());
  }

  @Test
  void testGetRandomUserAgentWhenFileIsEmpty() throws IOException {
    logger.info("Test get random user agent when file is empty");
    writeJsonFile(tempDir.resolve("random/empty.json"), List.of());
    Exception exception = assertThrows(RuntimeException.class, () -> {
      userAgentProvider.getRandomUserAgentFromFile(tempDir.resolve("random/empty.json"));
    });
    assertTrue(exception.getMessage().contains("User agent file is empty"));
  }

  // Helper method to write JSON files
  private void writeJsonFile(Path path, List<UserAgent> userAgents) throws IOException {
    MAPPER.writeValue(new File(path.toUri()), userAgents);
  }
}