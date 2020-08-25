package net.silthus.ebean;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * This class maps a driver to its driver class and provides a valid download url for the driver.
 * <p>
 * The driver will then be downloaded if needed and loaded dynamically at runtime.
 * Provide you custom driver inside the {@link DatabaseConfig.Builder#driver(DriverMapping)} method or
 * use one of the default drivers from the {@link #DRIVERS} map.
 */
@Value
public class DriverMapping {

    public static final Map<String, DriverMapping> DRIVERS = new HashMap<>();

    static {
        DRIVERS.put("h2", new DriverMapping("h2", "org.h2.Driver", "http://repo2.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar"));
        DRIVERS.put("mysql", new DriverMapping("mysql", "com.mysql.jdbc.Driver", "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.21/mysql-connector-java-8.0.21.jar"));
        DRIVERS.put("postgres", new DriverMapping("postgres", "org.postgresql.Driver", "https://jdbc.postgresql.org/download/postgresql-42.2.16.jar"));
        DRIVERS.put("mariadb", new DriverMapping("mariadb", "org.mariadb.jdbc.Driver", "https://downloads.mariadb.com/Connectors/java/connector-java-2.6.2/mariadb-java-client-2.6.2.jar"));
        DRIVERS.put("sqlserver", new DriverMapping("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/8.4.0.jre8/mssql-jdbc-8.4.0.jre8.jar"));
        DRIVERS.put("sqlite", new DriverMapping("sqlite", "org.sqlite.JDBC", "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.32.3.2/sqlite-jdbc-3.32.3.2.jar"));
    }

    String identifier;
    String driverClass;
    String downloadUrl;
}
