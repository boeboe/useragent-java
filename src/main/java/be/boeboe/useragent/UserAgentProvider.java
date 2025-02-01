package be.boeboe.useragent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides access to user agent strings for different devices and browsers.
 * <p>
 * This class allows users to retrieve either a random user agent or the latest
 * available user agent. It supports filtering by device type.
 * </p>
 *
 * <h3>Usage Examples:</h3>
 * 
 * <pre>
 * UserAgentProvider provider = new UserAgentProvider();
 * 
 * // Get a completely random user agent
 * UserAgent randomAgent = provider.getRandomUserAgent();
 * 
 * // Get the latest user agent available
 * UserAgent latestAgent = provider.getRandomLatestUserAgent();
 * 
 * // Get a random user agent for a specific device
 * UserAgent androidAgent = provider.getRandomUserAgent(Device.ANDROID);
 * 
 * // Get the latest user agent for a specific device
 * UserAgent latestWindowsAgent = provider.getRandomLatestUserAgent(Device.WINDOWS);
 * </pre>
 */
public class UserAgentProvider {
  private static final Path LATEST_DIR = Paths.get("src/main/resources/latest");
  private static final Path RANDOM_DIR = Paths.get("src/main/resources/random");
  private static final Random RANDOM = new Random();
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final Path latestDir;
  private final Path randomDir;

  /**
   * Creates an instance of {@code UserAgentProvider}.
   * <p>
   * This provider will return either a random or latest user agent
   * from a predefined set.
   * </p>
   */
  public UserAgentProvider() {
    this.latestDir = LATEST_DIR;
    this.randomDir = RANDOM_DIR;
  }

  /**
   * Protected constructor for testing purposes to override the JSON file
   * directory paths.
   *
   * @param latestDir Directory containing the latest user agent JSON files.
   * @param randomDir Directory containing randomized user agent JSON files.
   */
  protected UserAgentProvider(Path latestDir, Path randomDir) {
    this.latestDir = latestDir;
    this.randomDir = randomDir;
  }

  /**
   * Retrieves a random user agent.
   * 
   * @return a randomly selected {@link UserAgent}.
   * @throws RuntimeException if an error occurs while retrieving the user agent.
   */
  public UserAgent getRandomUserAgent() {
    return getRandomUserAgentFromDir(randomDir);
  }

  /**
   * Retrieves a random latest available user agent.
   * 
   * @return the latest available {@link UserAgent}.
   * @throws RuntimeException if an error occurs while retrieving the user agent.
   */
  public UserAgent getRandomLatestUserAgent() {
    return getRandomUserAgentFromDir(latestDir);
  }

  /**
   * Retrieves a random user agent for a specific device type.
   * 
   * @param device the {@link Device} type for which to retrieve a user agent.
   * @return a randomly selected {@link UserAgent} for the specified device.
   * @throws RuntimeException if an error occurs while retrieving the user agent.
   */
  public UserAgent getRandomUserAgent(Device device) {
    return getRandomUserAgentFromFile(randomDir.resolve(device.name().toLowerCase() + ".json"));
  }

  /**
   * Retrieves the latest available user agent for a specific device type.
   * 
   * @param device the {@link Device} type for which to retrieve a user agent.
   * @return the latest available {@link UserAgent} for the specified device.
   * @throws RuntimeException if an error occurs while retrieving the user agent.
   */
  public UserAgent getRandomLatestUserAgent(Device device) {
    return getRandomUserAgentFromFile(latestDir.resolve(device.name().toLowerCase() + ".json"));
  }

  /**
   * Retrieves a random user agent from any available JSON file within the
   * specified directory.
   * <p>
   * This method selects a random JSON file from the given directory, then
   * extracts a user agent from it.
   * </p>
   * 
   * @param dir the directory from which to fetch the user agent.
   * @return a randomly selected {@link UserAgent} from one of the available
   *         files.
   * @throws RuntimeException if an error occurs while reading the files or if no
   *                          valid files are found.
   */
  private UserAgent getRandomUserAgentFromDir(Path dir) {
    try {
      List<Path> jsonFiles = Files.list(dir)
          .filter(file -> Files.isRegularFile(file) && file.toString().endsWith(".json"))
          .collect(Collectors.toList());

      if (jsonFiles.isEmpty()) {
        throw new RuntimeException("No user agent files found in directory: " + dir);
      }

      Path randomFile = jsonFiles.get(RANDOM.nextInt(jsonFiles.size()));
      return getRandomUserAgentFromFile(randomFile);
    } catch (IOException e) {
      throw new RuntimeException("Error reading user agent files from directory: " + dir, e);
    }
  }

  /**
   * Reads a JSON file containing user agents and returns a randomly selected one.
   * <p>
   * This method reads the file and deserializes the content into a list of
   * {@link UserAgent} objects.
   * If the file is empty or cannot be parsed, an exception is thrown.
   * </p>
   * 
   * @param filePath the path to the JSON file containing user agent data.
   * @return a randomly selected {@link UserAgent} from the file.
   * @throws RuntimeException if the file is empty, missing, or cannot be parsed
   *                          correctly.
   */
  private UserAgent getRandomUserAgentFromFile(Path filePath) {
    try {
      if (!Files.exists(filePath)) {
        throw new RuntimeException("User agent file does not exist: " + filePath);
      }

      if (Files.size(filePath) == 0) {
        throw new RuntimeException("User agent file is empty: " + filePath);
      }

      List<UserAgent> userAgents = MAPPER.readValue(filePath.toFile(), new TypeReference<List<UserAgent>>() {
      });

      if (userAgents.isEmpty()) {
        throw new RuntimeException("User agent file contains an empty JSON array: " + filePath);
      }

      return userAgents.get(RANDOM.nextInt(userAgents.size()));
    } catch (IOException e) {
      throw new RuntimeException("Error reading user agent file: " + filePath, e);
    }
  }
}