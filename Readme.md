# ebean-wrapper

[![Build Status](https://github.com/Silthus/ebean/workflows/Build/badge.svg)](../../actions?query=workflow%3ABuild)
[![GitHub release (latest SemVer including pre-releases)](https://img.shields.io/github/v/release/Silthus/ebean?include_prereleases&label=release)](../../releases)
[![codecov](https://codecov.io/gh/Silthus/spigot-plugin-template/branch/master/graph/badge.svg)](https://codecov.io/gh/Silthus/ebean)
[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)
[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/semantic-release/semantic-release)

A simple wrapper around the [ebean.io](https://ebean.io) library to provide a one-stop-shop of shading all required dependencies and auto downloading the used driver.

## Usage

Include the `net.silthus:ebean` dependency in you project and shade it into your application or provide it in the classpath.

### Gradle

```groovy
dependencies {
    implementation group: "net.silthus", name: "ebean-wrapper", version: "1.0.0"
}
```

### Maven

 ```xml
<project>
  ...
  <dependencies>
    <dependency>
      <groupId>net.silthus</groupId>
      <artifactId>ebean-wrapper</artifactId>
      <version>1.0.0</version>
    </dependency>
  </dependencies>
  ...
</project>
```

### The EbeanWrapper

You can then create a new instance of the `EbeanWrapper` providing a simplified `net.silthus.DatabaseConfig` created with the `DatabaseConfig.Builder` or provide your own instance of the ebean `io.ebean.config.DatabaseConfig`.

```java
EbeanWrapper wrapper = new EbeanWrapper(DatabaseConfig.builder()
    .platform("mysql")
    .username("root")
    .password("root")
    .url("jdbc:mysql://localhost:3306/foobar")
.build());
```

You can then optionally initiate a download of the driver you are using. This will try to download the jar file and load it into the classpath.
You should run this on an async thread and open the database connection after the download is complete.

```java
wrapper.downloadDriver();
```

Then you can open the database connection or directly get the `Database` object, which will also open the connection.

```java
wrapper.open();
// or directly get the database
wrapper.getDatabase();
```

And when you are done, e.g. your application is shutting down or reloading, close the connection.

```java
wrapper.close();
```
