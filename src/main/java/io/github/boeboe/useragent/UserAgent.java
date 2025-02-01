package io.github.boeboe.useragent;

/**
 * Represents a user agent string associated with a specific device type.
 * <p>
 * This class holds the user agent string and the corresponding {@link Device}
 * type.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * UserAgent ua = new UserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)...", Device.WINDOWS);
 * System.out.println(ua.getUseragent());
 * </pre>
 */
public class UserAgent {

  /** The user agent string. */
  private String useragent;

  /** The device type associated with the user agent. */
  private Device device;

  /**
   * Default constructor.
   * Creates an empty {@code UserAgent} instance.
   */
  public UserAgent() {
  }

  /**
   * Constructs a {@code UserAgent} with the given user agent string and device
   * type.
   * 
   * @param useragent the user agent string.
   * @param device    the device type associated with the user agent.
   */
  public UserAgent(String useragent, Device device) {
    this.useragent = useragent;
    this.device = device;
  }

  /**
   * Retrieves the user agent string.
   * 
   * @return the user agent string.
   */
  public String getUseragent() {
    return useragent;
  }

  /**
   * Sets the user agent string.
   * 
   * @param useragent the user agent string to set.
   */
  public void setUseragent(String useragent) {
    this.useragent = useragent;
  }

  /**
   * Retrieves the device type associated with this user agent.
   * 
   * @return the device type.
   */
  public Device getDevice() {
    return device;
  }

  /**
   * Sets the device type associated with this user agent.
   * 
   * @param device the device type to set.
   */
  public void setDevice(Device device) {
    this.device = device;
  }
}