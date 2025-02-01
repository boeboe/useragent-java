package be.boeboe.useragent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
  Path tempDir;

  @BeforeEach
  void setUp() throws IOException {
    userAgentProvider = new UserAgentProvider(tempDir.resolve("latest"), tempDir.resolve("random"));
    Files.createDirectories(tempDir.resolve("latest"));
    Files.createDirectories(tempDir.resolve("random"));
    writeJsonFile(tempDir.resolve("latest/windows.json"), List.of(
        new UserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/100.0.0.0", Device.WINDOWS)));
    writeJsonFile(tempDir.resolve("latest/macos.json"), List.of(
        new UserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) Safari/537.36", Device.MACOS)));
    writeJsonFile(tempDir.resolve("random/windows.json"), List.of(
        new UserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Firefox/110.0", Device.WINDOWS)));
    writeJsonFile(tempDir.resolve("random/macos.json"), List.of(
        new UserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) Firefox/110.0", Device.MACOS)));
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
    UserAgent userAgentWindows = userAgentProvider.getRandomUserAgent(Device.WINDOWS);
    assertNotNull(userAgentWindows);
    assertEquals(Device.WINDOWS, userAgentWindows.getDevice());
    UserAgent userAgentMacOS = userAgentProvider.getRandomUserAgent(Device.MACOS);
    assertNotNull(userAgentMacOS);
    assertEquals(Device.MACOS, userAgentMacOS.getDevice());
  }

  @Test
  void testGetRandomLatestUserAgentByDevice() {
    logger.info("Test get random latest user agent by device");
    UserAgent userAgentWindows = userAgentProvider.getRandomLatestUserAgent(Device.WINDOWS);
    assertNotNull(userAgentWindows);
    assertEquals(Device.WINDOWS, userAgentWindows.getDevice());
    UserAgent userAgentMacOS = userAgentProvider.getRandomLatestUserAgent(Device.MACOS);
    assertNotNull(userAgentMacOS);
    assertEquals(Device.MACOS, userAgentMacOS.getDevice());
  }

  @Test
  void testGetRandomUserAgentWhenFileIsEmpty() throws IOException {
    logger.info("Test get random user agent when file is empty");
    Path filePath = tempDir.resolve("random/windows.json");
    Files.write(filePath, new byte[0]);
    UserAgentProvider emptyUserAgentProvider = new UserAgentProvider(tempDir.resolve("latest"),
        tempDir.resolve("random"));
    Exception exception = assertThrows(RuntimeException.class, () -> {
      emptyUserAgentProvider.getRandomUserAgent(Device.WINDOWS);
    });
    assertTrue(exception.getMessage().contains("User agent file is empty"));
  }

  @Test
  void testGetRandomUserAgentFromNonExistentFile() {
    logger.info("Test get random user agent from a non-existent file");
    Path missingFilePath = tempDir.resolve("random/android.json");
    assertFalse(Files.exists(missingFilePath));
    UserAgentProvider missingUserAgentProvider = new UserAgentProvider(tempDir.resolve("latest"),
        tempDir.resolve("random"));
    Exception exception = assertThrows(RuntimeException.class, () -> {
      missingUserAgentProvider.getRandomUserAgent(Device.ANDROID);
    });
    assertTrue(exception.getMessage().contains("User agent file does not exist"));
  }

  @Test
  void testGetRandomUserAgentWhenFileContainsEmptyJsonArray() throws IOException {
    logger.info("Test get random user agent when file contains an empty JSON array");
    Path filePath = tempDir.resolve("random/windows.json");
    Files.write(filePath, "[]".getBytes());
    UserAgentProvider emptyUserAgentProvider = new UserAgentProvider(tempDir.resolve("latest"),
        tempDir.resolve("random"));
    Exception exception = assertThrows(RuntimeException.class, () -> {
      emptyUserAgentProvider.getRandomUserAgent(Device.WINDOWS);
    });
    assertTrue(exception.getMessage().contains("User agent file contains an empty JSON array"));
  }

  /**
   * Writes a list of user agents to a JSON file.
   * 
   * @param path       the path to the JSON file.
   * @param userAgents the list of user agents to write.
   * @throws IOException if an I/O error occurs while writing the file.
   */
  private void writeJsonFile(Path path, List<UserAgent> userAgents) throws IOException {
    MAPPER.writeValue(new File(path.toUri()), userAgents);
  }
}