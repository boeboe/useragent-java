# **UserAgent Java Library**

[![Build & Test](https://github.com/boeboe/useragent-java/actions/workflows/ci.yml/badge.svg)](https://github.com/boeboe/useragent-java/actions/workflows/ci.yml)  
[![Release & Deploy](https://github.com/boeboe/useragent-java/actions/workflows/release.yml/badge.svg)](https://github.com/boeboe/useragent-java/actions/workflows/release.yml)  
[![Latest Release](https://img.shields.io/github/v/release/boeboe/useragent-java)](https://github.com/boeboe/useragent-java/releases)  
[![Maven Central](https://img.shields.io/maven-central/v/io.github.boeboe/user-agent)](https://search.maven.org/artifact/io.github.boeboe/user-agent)

A lightweight **Java library** for retrieving **random** and **latest** user-agent strings for various browsers and devices.

---

## **📌 Features**

✅ Retrieve **random user agents** for multiple devices and browsers  
✅ Retrieve the **latest known user agent** per device/browser  
✅ Uses **predefined JSON datasets** for accuracy  
✅ Simple API with **zero dependencies on external APIs**  
✅ Includes **Javadoc and source JARs**

---

## **📦 Installation**

### **Using Maven**

Add the following dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>io.github.boeboe</groupId>
  <artifactId>user-agent</artifactId>
  <version>1.0.2</version> <!-- Update to latest -->
</dependency>
```

### **Using Gradle**

```groovy
dependencies {
  implementation("io.github.boeboe:user-agent:1.0.2") // Update to latest
}
```

---

## **🛠️ Usage**

### **1️⃣ Retrieve Random or Latest User Agents**

```java
UserAgentProvider provider = new UserAgentProvider();

// Get a completely random user agent
UserAgent randomAgent = provider.getRandomUserAgent();
System.out.println("Random UA: " + randomAgent.getUserAgent());

// Get a random latest user agent
UserAgent latestAgent = provider.getRandomLatestUserAgent();
System.out.println("Latest UA: " + latestAgent.getUserAgent());
```

### **2️⃣ Retrieve Random or Latest User Agents by Device Type**

```java
UserAgentProvider provider = new UserAgentProvider();

// Get a random user agent for a specific device
UserAgent androidAgent = provider.getRandomUserAgent(DeviceFilter.ANDROID);
System.out.println("Random Android UA: " + androidAgent.getUserAgent());

// Get a latest user agent for a specific device
UserAgent latestWindowsAgent = provider.getRandomLatestUserAgent(DeviceFilter.WINDOWS);
System.out.println("Latest Windows UA: " + latestWindowsAgent.getUserAgent());
```

### **3️⃣ Access Full User Agent Data**

```java
UserAgentProvider provider = new UserAgentProvider();

// Retrieve all available user agents
Set<UserAgent> allUserAgents = provider.getAllUserAgents();
System.out.println("Total user agents loaded: " + allUserAgents.size());

// Retrieve only the latest user agents
Set<UserAgent> latestUserAgents = provider.getLatestUserAgents();
System.out.println("Total latest user agents loaded: " + latestUserAgents.size());
```

---

## **📂 Project Structure**

```
useragent-java/
├── src/main/java/io/github/boeboe/useragent/
│   ├── DeviceFilter.java          # Enum representing device types
│   ├── UserAgent.java             # Model for user-agent strings
│   └── UserAgentProvider.java     # Main provider for user-agent retrieval
├── src/main/resources/
│   ├── latest/                    # JSON files for latest user-agents
│   └── random/                    # JSON files for random user-agents
├── src/test/java/io/github/boeboe/useragent/
│   ├── UserAgentProviderIT.java   # Integration tests
│   └── UserAgentProviderTest.java # Unit tests
├── pom.xml                        # Maven project configuration
├── Makefile                       # Makefile helper
└── README.md                      # Documentation
```

---

## **🔨 Makefile Targets**

This project includes a **Makefile** to simplify build, test, and release operations.

### **Available Targets**

| Target               | Description                                               |
| -------------------- | --------------------------------------------------------- |
| `make help`          | Displays available Makefile targets with descriptions.    |
| `make clean`         | Cleans the project by removing compiled artifacts.        |
| `make build`         | Compiles the project source code.                         |
| `make sources`       | Fetches sources and Javadocs for dependencies.            |
| `make test`          | Runs all tests (unit and integration).                    |
| `make package`       | Packages the project into a JAR file.                     |
| `make install`       | Installs the built JAR to the local Maven repository.     |
| `make deploy`        | Deploy built JAR to a remote Maven repository.            |
| `make check-updates` | Checks for dependency updates (libraries, plugins, etc.). |

---

## **🚀 Contributing**

We welcome contributions! To contribute:

1. **Fork** this repository
2. **Create** a feature branch (`git checkout -b feature/new-feature`)
3. **Commit** changes (`git commit -m "Add new feature"`)
4. **Push** to your fork (`git push origin feature/new-feature`)
5. Open a **Pull Request**

---

## **📝 License**

This project is licensed under the **MIT License**.

---

## **📣 Acknowledgements**

- **User agent data** is sourced from known browser user agents.
- Built with ❤️ by **[boeboe](https://github.com/boeboe)**.
