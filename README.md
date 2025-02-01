# **UserAgent Java Library**

[![Build](https://github.com/boeboe/useragent-java/actions/workflows/ci.yml/badge.svg)](https://github.com/boeboe/useragent-java/actions/workflows/ci.yml)  
[![Latest Release](https://img.shields.io/github/v/release/boeboe/useragent-java)](https://github.com/boeboe/useragent-java/releases)  
[![Maven Central](https://img.shields.io/maven-central/v/be.boeboe/user-agent)](https://search.maven.org/artifact/be.boeboe/user-agent)

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
  <groupId>be.boeboe</groupId>
  <artifactId>user-agent</artifactId>
  <version>1.0.0</version> <!-- Update to latest -->
</dependency>
```

### **Using Gradle**

```groovy
dependencies {
  implementation("be.boeboe:user-agent:1.0.4") // Update to latest
}
```

---

## **🛠️ Usage**

### **1️⃣ Create an Instance**

```java
UserAgentProvider provider = new UserAgentProvider();
```

### **2️⃣ Get a Random User Agent**

```java
UserAgent randomAgent = provider.getRandomUserAgent();
System.out.println(randomAgent.getUseragent()); // Prints random UA
```

### **3️⃣ Get Latest User Agent**

```java
UserAgent latestAgent = provider.getRandomLatestUserAgent();
System.out.println(latestAgent.getUseragent());
```

### **4️⃣ Get Random User Agent for a Specific Device**

```java
UserAgent androidAgent = provider.getRandomUserAgent(Device.ANDROID);
System.out.println(androidAgent.getUseragent());
```

### **5️⃣ Get Latest User Agent for a Specific Device**

```java
UserAgent latestWindowsAgent = provider.getRandomLatestUserAgent(Device.WINDOWS);
System.out.println(latestWindowsAgent.getUseragent());
```

---

## **📂 Project Structure**

```
useragent-java/
├── src/main/java/be/boeboe/useragent/
│   ├── Device.java              # Enum representing device types
│   ├── UserAgent.java           # Model for user-agent strings
│   ├── UserAgentProvider.java   # Main provider for user-agent retrieval
├── src/main/resources/
│   ├── latest/                  # JSON files for latest user-agents
│   ├── random/                  # JSON files for random user-agents
├── src/test/java/be/boeboe/useragent/
│   ├── UserAgentProviderTest.java # Unit tests
├── pom.xml                      # Maven project configuration
├── Makefile                     # Makefile helper
└── README.md                    # Documentation
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

---

🚀 **Now your `README.md` is properly formatted, escaped, and structured for developers!** Copy and paste this directly into your repository. 😊
