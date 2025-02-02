package io.github.boeboe.useragent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides user agents from embedded JSON files inside the JAR.
 */
public class UserAgentProvider {
  private static final Logger logger = LoggerFactory.getLogger(UserAgentProvider.class);

  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final String RANDOM_DIR = "random/";
  private static final String LATEST_DIR = "latest/";

  private final Set<UserAgent> allUserAgents = new HashSet<>();
  private final Set<UserAgent> latestUserAgents = new HashSet<>();

  /**
   * Initializes the provider by loading user agents from resources.
   */
  public UserAgentProvider() {
    loadUserAgents(RANDOM_DIR, allUserAgents);
    loadUserAgents(LATEST_DIR, latestUserAgents);
    allUserAgents.addAll(latestUserAgents);
  }

  /**
   * Returns all loaded user agents.
   *
   * @return Set of all user agents.
   */
  public Set<UserAgent> getAllUserAgents() {
    return Collections.unmodifiableSet(allUserAgents);
  }

  /**
   * Returns only the latest user agents.
   *
   * @return Set of latest user agents.
   */
  public Set<UserAgent> getLatestUserAgents() {
    return Collections.unmodifiableSet(latestUserAgents);
  }

  /**
   * Returns a random user agent from the latest user agents.
   *
   * @return A randomly selected {@link UserAgent} or null if none exist.
   */
  public UserAgent getRandomLatestUserAgent() {
    return getRandomUserAgentFromSet(latestUserAgents, null);
  }

  /**
   * Returns a random user agent from all user agents.
   *
   * @return A randomly selected {@link UserAgent} or null if none exist.
   */
  public UserAgent getRandomUserAgent() {
    return getRandomUserAgentFromSet(allUserAgents, null);
  }

  /**
   * Returns a random latest user agent filtered by device type.
   *
   * @param device The device type to filter by.
   * @return A randomly selected {@link UserAgent} matching the device filter, or
   *         null if none exist.
   */
  public UserAgent getRandomLatestUserAgent(DeviceFilter device) {
    return getRandomUserAgentFromSet(latestUserAgents, device);
  }

  /**
   * Returns a random user agent from all loaded user agents, filtered by device
   * type.
   *
   * @param device The device type to filter by.
   * @return A randomly selected {@link UserAgent} matching the device filter, or
   *         null if none exist.
   */
  public UserAgent getRandomUserAgent(DeviceFilter device) {
    return getRandomUserAgentFromSet(allUserAgents, device);
  }

  /**
   * Loads user agents from JSON files in a given directory inside resources.
   *
   * @param resourceDir The resource directory containing JSON files.
   * @param targetList  The set where parsed user agents should be stored.
   */
  private void loadUserAgents(String resourceDir, Set<UserAgent> targetList) {
    List<String> jsonFiles = listJsonFiles(resourceDir);
    for (String filePath : jsonFiles) {
      try {
        List<UserAgent> parsedAgents = readUserAgentsFromFile(filePath);
        targetList.addAll(parsedAgents);
        logger.debug("Loaded {} user agents from {}", parsedAgents.size(), filePath);
      } catch (IOException e) {
        logger.error("Failed to load user agents from {}: {}", filePath, e.getMessage());
        throw new RuntimeException("Failed to load user agents from: " + filePath, e);
      }
    }
  }

  /**
   * Retrieves all JSON file names in the given resource directory.
   *
   * @param resourceDir The directory inside `src/main/resources` (or JAR).
   * @return List of JSON file paths.
   */
  private List<String> listJsonFiles(String resourceDir) {
    try {
      URL resourceURL = getClass().getClassLoader().getResource(resourceDir);
      if (resourceURL == null) {
        logger.error("Resource directory not found: {}", resourceDir);
        throw new RuntimeException("Resource directory not found: " + resourceDir);
      }

      switch (resourceURL.getProtocol()) {
        case "file":
          logger.debug("Listing JSON files from file system: {}", resourceDir);
          return listJsonFilesFromFileSystem(resourceURL, resourceDir);
        case "jar":
          logger.debug("Listing JSON files from JAR: {}", resourceDir);
          return listJsonFilesFromJar(resourceURL, resourceDir);
        default:
          logger.error("Unsupported resource protocol: {}", resourceURL.getProtocol());
          throw new IOException("Unsupported resource protocol: " + resourceURL.getProtocol());
      }
    } catch (Exception e) {
      logger.error("Failed to list JSON files in {}: {}", resourceDir, e.getMessage());
      throw new RuntimeException("Failed to list JSON files in: " + resourceDir, e);
    }
  }

  /**
   * Retrieves JSON file names from a directory in the file system.
   *
   * @param resourceURL The URL pointing to the resource directory.
   * @param resourceDir The directory path inside resources.
   * @return List of JSON file paths.
   * @throws Exception If reading fails.
   */
  private List<String> listJsonFilesFromFileSystem(URL resourceURL, String resourceDir) throws Exception {
    java.io.File dir = new java.io.File(resourceURL.toURI());
    String[] files = dir.list((d, name) -> name.endsWith(".json"));

    if (files == null || files.length == 0) {
      return Collections.emptyList();
    }

    return Arrays.stream(files)
        .map(name -> resourceDir + name)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves JSON file names from a directory inside a JAR file.
   *
   * @param resourceURL The URL pointing to the resource inside the JAR.
   * @param resourceDir The directory path inside the JAR.
   * @return List of JSON file paths.
   * @throws Exception If reading fails.
   */
  private List<String> listJsonFilesFromJar(URL resourceURL, String resourceDir) throws Exception {
    String jarPath = resourceURL.getPath().substring(5, resourceURL.getPath().indexOf("!"));

    try (JarFile jarFile = new JarFile(jarPath)) {
      return jarFile.stream()
          .map(JarEntry::getName)
          .filter(name -> name.startsWith(resourceDir) && name.endsWith(".json"))
          .collect(Collectors.toList());
    }
  }

  /**
   * Reads a list of user agents from a JSON file inside resources.
   *
   * @param resourcePath Path to the JSON file in the classpath.
   * @return List of parsed UserAgent objects.
   * @throws IOException If reading fails.
   */
  private List<UserAgent> readUserAgentsFromFile(String resourcePath) throws IOException {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
      if (inputStream == null) {
        throw new IOException("Resource not found: " + resourcePath);
      }
      return MAPPER.readValue(inputStream, new TypeReference<List<UserAgent>>() {
      });
    }
  }

  /**
   * Retrieves a random user agent from a given set, with optional filtering by
   * device type.
   *
   * @param userAgents The set of user agents to pick from.
   * @param device     (Optional) The device type to filter by; if null, no
   *                   filtering is applied.
   * @return A randomly selected {@link UserAgent} matching the device filter, or
   *         null if none exist.
   */
  private UserAgent getRandomUserAgentFromSet(Set<UserAgent> userAgents, DeviceFilter device) {
    List<UserAgent> filteredAgents = (device == null)
        ? new ArrayList<>(userAgents)
        : userAgents.stream()
            .filter(ua -> ua.getDevice() == device)
            .collect(Collectors.toList());

    if (filteredAgents.isEmpty()) {
      return null;
    }
    return filteredAgents.get(ThreadLocalRandom.current().nextInt(filteredAgents.size()));
  }
}