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
 * Provides functionality to retrieve random or latest user agents from JSON
 * files.
 * <p>
 * This class reads user agent data stored in JSON files and provides methods
 * to fetch random or latest user agents, optionally filtering by device type.
 * </p>
 * 
 * <h3>Usage Example:</h3>
 * 
 * <pre>
 * UserAgentProvider provider = new UserAgentProvider();
 * UserAgent randomAgent = provider.getRandomUserAgent();
 * UserAgent latestAgent = provider.getRandomLatestUserAgent(Device.WINDOWS);
 * </pre>
 * 
 * <p>
 * The JSON files must be structured as an array of user agents, stored in:
 * </p>
 * <ul>
 * <li><b>src/main/resources/latest/</b> - Contains latest user agents.</li>
 * <li><b>src/main/resources/random/</b> - Contains randomized user agents.</li>
 * </ul>
 */
public class UserAgentProvider {
  private static final String LATEST_DIR = "src/main/resources/latest";
  private static final String RANDOM_DIR = "src/main/resources/random";
  private static final Random RANDOM = new Random();
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * Retrieves a random user agent from the general pool.
   * 
   * @return a random {@link UserAgent} instance.
   * @throws RuntimeException if an error occurs while reading the files.
   */
  public UserAgent getRandomUserAgent() {
    return getRandomUserAgentFromDir(RANDOM_DIR);
  }

  /**
   * Retrieves a random user agent from the latest user agent pool.
   * 
   * @return a random {@link UserAgent} from the latest dataset.
   * @throws RuntimeException if an error occurs while reading the files.
   */
  public UserAgent getRandomLatestUserAgent() {
    return getRandomUserAgentFromDir(LATEST_DIR);
  }

  /**
   * Retrieves a random user agent for a specific device type.
   * 
   * @param device the {@link Device} type for which to retrieve a user agent.
   * @return a random {@link UserAgent} matching the device type.
   * @throws RuntimeException if an error occurs while reading the files.
   */
  public UserAgent getRandomUserAgent(Device device) {
    return getRandomUserAgentFromFile(RANDOM_DIR, device);
  }

  /**
   * Retrieves a random latest user agent for a specific device type.
   * 
   * @param device the {@link Device} type for which to retrieve a user agent.
   * @return a random {@link UserAgent} from the latest dataset matching the
   *         device type.
   * @throws RuntimeException if an error occurs while reading the files.
   */
  public UserAgent getRandomLatestUserAgent(Device device) {
    return getRandomUserAgentFromFile(LATEST_DIR, device);
  }

  /**
   * Retrieves a random user agent from any JSON file within the specified
   * directory.
   * 
   * @param dir the directory from which to fetch the user agent.
   * @return a randomly selected {@link UserAgent} from one of the available
   *         files.
   * @throws RuntimeException if an error occurs while reading the files.
   */
  private UserAgent getRandomUserAgentFromDir(String dir) {
    try {
      List<Path> files = Files.list(Paths.get(dir)).collect(Collectors.toList());
      Path randomFile = files.get(RANDOM.nextInt(files.size()));
      return getRandomUserAgentFromFile(randomFile);
    } catch (IOException e) {
      throw new RuntimeException("Error reading user agent files", e);
    }
  }

  /**
   * Retrieves a random user agent from a specific JSON file corresponding to the
   * given device type.
   * 
   * @param dir    the directory containing the user agent JSON files.
   * @param device the {@link Device} type for which to retrieve a user agent.
   * @return a randomly selected {@link UserAgent} from the corresponding file.
   * @throws RuntimeException if an error occurs while reading the file.
   */
  private UserAgent getRandomUserAgentFromFile(String dir, Device device) {
    Path filePath = Paths.get(dir, device.name().toLowerCase() + ".json");
    return getRandomUserAgentFromFile(filePath);
  }

  /**
   * Parses a JSON file containing user agents and returns a randomly selected
   * one.
   * 
   * @param filePath the path to the JSON file containing user agent data.
   * @return a randomly selected {@link UserAgent} from the file.
   * @throws RuntimeException if an error occurs while parsing the JSON file.
   */
  protected UserAgent getRandomUserAgentFromFile(Path filePath) {
    try {
      List<UserAgent> userAgents = MAPPER.readValue(filePath.toFile(), new TypeReference<List<UserAgent>>() {
      });

      if (userAgents.isEmpty()) {
        throw new RuntimeException("User agent file is empty: " + filePath);
      }

      return userAgents.get(RANDOM.nextInt(userAgents.size()));
    } catch (IOException e) {
      throw new RuntimeException("Error reading user agent file: " + filePath, e);
    }
  }
}