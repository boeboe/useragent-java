package io.github.boeboe.useragent;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a user agent string associated with a specific device type.
 * <p>
 * This class holds the user agent string and the corresponding
 * {@link DeviceFilter}
 * type.
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * UserAgent ua = new UserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)...", Device.WINDOWS);
 * System.out.println(ua.getUserAgent());
 * </pre>
 */
public class UserAgent {

  /** The user agent string. */
  @JsonProperty("useragent")
  private String userAgent;

  /** The device type associated with the user agent. */
  @JsonProperty("device")
  private DeviceFilter device;

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
   * @param userAgent the user agent string.
   * @param device    the device type associated with the user agent.
   */
  public UserAgent(String userAgent, DeviceFilter device) {
    this.userAgent = userAgent;
    this.device = device;
  }

  /**
   * Retrieves the user agent string.
   * 
   * @return the user agent string.
   */
  public String getUserAgent() {
    return userAgent;
  }

  /**
   * Sets the user agent string.
   * 
   * @param userAgent the user agent string to set.
   */
  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  /**
   * Retrieves the device type associated with this user agent.
   * 
   * @return the device type.
   */
  public DeviceFilter getDevice() {
    return device;
  }

  /**
   * Sets the device type associated with this user agent.
   * 
   * @param device the device type to set.
   */
  public void setDevice(DeviceFilter device) {
    this.device = device;
  }

  @Override
  public String toString() {
    return userAgent;
  }
}