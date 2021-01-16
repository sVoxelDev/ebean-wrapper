# ebean-wrapper

[![Build Status](https://github.com/Silthus/ebean/workflows/Build/badge.svg)](../../actions?query=workflow%3ABuild)
[![GitHub release (latest SemVer including pre-releases)](https://img.shields.io/github/v/release/Silthus/ebean?include_prereleases&label=release)](../../releases)
[![codecov](https://codecov.io/gh/Silthus/spigot-plugin-template/branch/master/graph/badge.svg)](https://codecov.io/gh/Silthus/ebean)
[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)
[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/semantic-release/semantic-release)

A simple wrapper around the [ebean.io](https://ebean.io) library to provide a one-stop-shop of shading all required dependencies and auto downloading the used driver.

## Usage

Drop the `ebean-wrapper.jar` inside your plugins directory and depend on it, if you are using this wrapper in a Minecraft (Spigot/Paper) server.

As an alternative you can shade the lib into your application or provide it in the classpath.
**This is not recommended for Minecraft plugins!**

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation group: "net.silthus", name: "ebean-wrapper", version: "2.9.1"
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
      <version>2.9.1</version>
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
    .driver(Driver.MariaDB)
    .username("root")
    .password("root")
    .url("jdbc:mysql://localhost:3306/foobar")
.build());
```

You will also need to add all classes that should be treated as entities to the entities method of the config builder.

```java
Config.builder()
    .entities(
        User.class,
        Address.class
    ).driver(Driver.H2)
    ...
    .build();
```

The ebean-wrapper will download the required driver by default. You can disable this behaviour my passing `.autoDownloadDriver(false)` to the `ConfigBuilder`.

If you do that you can control on your own when to download the driver by calling `wrapper.downloadDriver();`.

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

## Migrations

[ebean](http://ebean.io) has great migration support for all of the different database types.
You can use this awesome feature by creating the following `Migrations` class inside your test source root.

```java
import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;

import java.io.IOException;

public class Migrations {

    public static void main(String[] args) throws IOException {

        DbMigration migration = DbMigration.create();

        // location of the migration changeSet and where ddl is generated to
        migration.setPathToResources("src/main/resources");

        // add a series of database platforms to generate the ddl for ...
        migration.addPlatform(Platform.POSTGRES, "postgres");
        migration.addPlatform(Platform.MYSQL, "mysql");
        migration.addPlatform(Platform.MARIADB, "mariadb");
        migration.addPlatform(Platform.H2, "h2");
        migration.addPlatform(Platform.SQLITE, "sqlite");

        migration.generateMigration();
    }
}
```

For this to work you will also need to test depend on all of the different driver implementations.
Add the following test dependencies for it to work.

```groovy
    testImplementation group: 'javax.xml.bind', name: 'jaxb-api', version: '2.9.1'
    testImplementation group: 'com.h2database', name: 'h2', version: '2.9.100'
    testImplementation group: 'org.mariadb.jdbc', name: 'mariadb-java-client', version: '2.9.1'
    testImplementation group: 'mysql', name: 'mysql-connector-java', version: '2.9.12'
    testImplementation group: 'org.xerial', name: 'sqlite-jdbc', version: '3.32.3.2'
    testImplementation group: 'org.postgresql', name: 'postgresql', version: '42.2.18'
```

Also make sure you enable migrations when creating your `EbeanWrapper` by setting `migrations(this.getClass())` to the class of your application.

Then ebean will automatically run the migrations of all of your entities on start.