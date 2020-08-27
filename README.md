# ebean-wrapper

[![Build Status](https://github.com/Silthus/ebean/workflows/Build/badge.svg)](../../actions?query=workflow%3ABuild)
[![GitHub release (latest SemVer including pre-releases)](https://img.shields.io/github/v/release/Silthus/ebean?include_prereleases&label=release)](../../releases)
[![codecov](https://codecov.io/gh/Silthus/spigot-plugin-template/branch/master/graph/badge.svg)](https://codecov.io/gh/Silthus/ebean)
[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)
[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/semantic-release/semantic-release)

A simple wrapper around the [ebean.io](https://ebean.io) library to provide a one-stop-shop of shading all required dependencies and auto downloading the used driver.

## Usage

Include the dependency in your project and shade it into your application or provide it in the classpath.

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation group: "net.silthus", name: "ebean-wrapper", version: "2.1.0"
}
```

### Maven

 ```xml
<project>
  ...
  <repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
  </repositories>
  ...
  <dependencies>
    <dependency>
      <groupId>net.silthus</groupId>
      <artifactId>ebean-wrapper</artifactId>
      <version>2.1.0</version>
    </dependency>
  </dependencies>
  ...
</project>
```

### The EbeanWrapper

You can then create a new instance of the `EbeanWrapper` by providing a `Config` object. Use the config builder `Config.builder()` to create a new instance of the config.
Make sure you provide a valid driver or create your own `DriverMapping`.

```java
EbeanWrapper wrapper = new EbeanWrapper(Config.builder()
    .driver("mysql")
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
The connection will be cached and all subsequent calls to `getDatabase()` use the same connection.

```java
wrapper.connect();
// or directly get the database which will connect automatically
wrapper.getDatabase();
```

## Drivers

The ebean wrapper provides the following default driver mappings for auto downloading the appropriate driver.

| Driver | Documentation | ConnectionString |
| ---- | ----- | ----- |
| `h2` | [h2database.com](http://www.h2database.com/) | `jdbc:h2:~/test` |
| `mysql` | [mysql.com](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html) | `jdbc:mysql://host:port/database` |
| `postgres` | [jdbc.postgresql.org](https://jdbc.postgresql.org/documentation/80/connect.html) | `jdbc:postgresql://host:port/database` |
| `mariadb` | [mariadb.com](https://mariadb.com/kb/en/about-mariadb-connector-j/) | `jdbc:mysql://host:port/database` |
| `sqlserver` | [docs.microsoft.com](https://docs.microsoft.com/en-us/sql/connect/jdbc/building-the-connection-url?view=sql-server-ver15) | `jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks;integratedSecurity=true;` |
| `sqlite` | [github.com/xerial/sqlite-jdbc](https://github.com/xerial/sqlite-jdbc) | `jdbc:sqlite:/home/example/mydatabase.db` |

You can enable to autodownloading of the driver in the configuration builder:

```java
Config.builder().autoDownloadDriver(true)/*...*/.build();
```
