package be.boeboe.useragent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing different types of devices and browsers
 * for which user agents can be retrieved.
 * <p>
 * This enum includes:
 * <ul>
 * <li>{@link #ANDROID} - Represents Android devices.</li>
 * <li>{@link #CHROME} - Represents the Google Chrome browser.</li>
 * <li>{@link #EDGE} - Represents Microsoft Edge browser.</li>
 * <li>{@link #EXPLORER} - Represents Internet Explorer.</li>
 * <li>{@link #FIREFOX} - Represents Mozilla Firefox browser.</li>
 * <li>{@link #IPHONE} - Represents iPhone devices.</li>
 * <li>{@link #LINUX} - Represents Linux-based operating systems.</li>
 * <li>{@link #MACOS} - Represents macOS operating system.</li>
 * <li>{@link #MOBILE} - Represents mobile devices in general.</li>
 * <li>{@link #WINDOWS} - Represents Windows operating system.</li>
 * </ul>
 * </p>
 */
public enum Device {
  /**
   * Represents Android devices.
   */
  ANDROID,

  /**
   * Represents the Google Chrome browser.
   */
  CHROME,

  /**
   * Represents the Microsoft Edge browser.
   */
  EDGE,

  /**
   * Represents the Internet Explorer browser.
   */
  EXPLORER,

  /**
   * Represents the Mozilla Firefox browser.
   */
  FIREFOX,

  /**
   * Represents iPhone devices.
   */
  IPHONE,

  /**
   * Represents Linux-based operating systems.
   */
  LINUX,

  /**
   * Represents macOS operating system.
   */
  MACOS,

  /**
   * Represents mobile devices in general.
   */
  MOBILE,

  /**
   * Represents Windows operating system.
   */
  WINDOWS;

  /**
   * Returns the lowercase string representation of the enum.
   *
   * @return lowercase string of the enum constant.
   */
  @JsonValue
  public String toLowerCase() {
    return name().toLowerCase();
  }

  /**
   * Creates a Device enum instance from a lowercase string.
   *
   * @param value the lowercase string value (e.g., "windows").
   * @return the corresponding Device enum.
   * @throws IllegalArgumentException if the value does not match any enum
   *                                  constant.
   */
  @JsonCreator
  public static Device fromString(String value) {
    for (Device device : Device.values()) {
      if (device.name().equalsIgnoreCase(value)) {
        return device;
      }
    }
    throw new IllegalArgumentException("Invalid device type: " + value);
  }
}