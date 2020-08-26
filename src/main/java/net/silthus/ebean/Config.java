package net.silthus.ebean;

import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

import java.io.File;

@Value
public class Config {

    public static Builder builder() {
        return new Builder();
    }

    ClassLoader classLoader;
    DriverMapping driver;
    File driverPath;
    DatabaseConfig databaseConfig;
    boolean autoDownloadDriver;

    public Builder toBuilder() {
        DataSourceConfig dataSourceConfig = databaseConfig.getDataSourceConfig();
        return new Builder(classLoader,
                driver,
                driverPath,
                databaseConfig,
                dataSourceConfig.getUsername(),
                dataSourceConfig.getPassword(),
                dataSourceConfig.getUrl(),
                dataSourceConfig,
                autoDownloadDriver);
    }

    @Setter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public static class Builder {

        private ClassLoader classLoader = getClass().getClassLoader();
        private DriverMapping driver = DriverMapping.DRIVERS.get("h2");
        private File driverPath = new File("drivers");
        private DatabaseConfig databaseConfig = defaultDatabaseConfig();
        private String username = "sa";
        private String password = "sa";
        private String url = "jdbc:h2:~/ebean";
        private DataSourceConfig dataSource;
        private boolean autoDownloadDriver = false;

        Builder() {
        }

        public Builder driver(DriverMapping driver) {
            this.driver = driver;
            return this;
        }

        public Builder driver(String driver) {
            if (!DriverMapping.DRIVERS.containsKey(driver)) {
                throw new IllegalArgumentException("Unable to find a valid driver mapping for " + driver + ". " +
                        "Use your custom driver mapping or one of the following: " + String.join(",", DriverMapping.DRIVERS.keySet()));
            }
            this.driver = DriverMapping.DRIVERS.get(driver);
            return this;
        }

        public Config build() {

            if (dataSource == null) {
                dataSource = new DataSourceConfig()
                        .setPlatform(driver.getIdentifier())
                        .setUsername(username)
                        .setPassword(password)
                        .setUrl(url);
            }

            databaseConfig.setDataSourceConfig(dataSource);

            return new Config(classLoader, driver, driverPath, databaseConfig, autoDownloadDriver);
        }

        private DatabaseConfig defaultDatabaseConfig() {

            DatabaseConfig databaseConfig = new DatabaseConfig();

            // load configuration defaults from application.yml
            databaseConfig.loadFromProperties();
            databaseConfig.setDefaultServer(true);
            databaseConfig.setRegister(true);

            return databaseConfig;
        }
    }
}
