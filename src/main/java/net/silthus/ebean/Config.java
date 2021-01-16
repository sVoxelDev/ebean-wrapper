package net.silthus.ebean;

import com.google.common.io.Files;
import io.ebean.config.DatabaseConfig;
import io.ebean.config.ServerConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.migration.MigrationConfig;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Value
public class Config {

    private static Set<Class<?>> classes = new HashSet<>();

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        return builder()
                .driverPath(new File("lib"))
                .autoDownloadDriver(true)
                .migrations(plugin.getClass())
                .migrationTable("db_migrations_" + plugin.getName().replace("-", "_").replace(" ", "_"))
                .url(config.getString("database.url", "jdbc:h2:file:" + new File(plugin.getDataFolder(), plugin.getName() + ".db").getAbsolutePath()))
                .username(config.getString("database.username", "sa"))
                .password(config.getString("database.password", "sa"))
                .driver(config.getString("database.driver", "h2"));
    }

    DriverMapping driver;
    File driverPath;
    DatabaseConfig databaseConfig;
    boolean autoDownloadDriver;
    boolean runMigrations;
    boolean createAll;
    Class<?>[] entities;
    Class<?> migrationClass;
    String migrationPath;
    String migrationTable;

    public Builder toBuilder() {
        DataSourceConfig dataSourceConfig = databaseConfig.getDataSourceConfig();
        return new Builder(driver,
                driverPath,
                databaseConfig,
                dataSourceConfig.getUsername(),
                dataSourceConfig.getPassword(),
                dataSourceConfig.getUrl(),
                dataSourceConfig,
                autoDownloadDriver,
                runMigrations,
                createAll,
                entities,
                migrationClass,
                migrationPath,
                migrationTable);
    }

    @Setter
    @AllArgsConstructor
    @Accessors(fluent = true)
    @Log
    public static class Builder {

        private DriverMapping driver = DriverMapping.DRIVERS.get("h2");
        private File driverPath = new File("lib");
        private DatabaseConfig databaseConfig = defaultDatabaseConfig();
        private String username = "sa";
        private String password = "sa";
        private String url = "jdbc:h2:~/ebean";
        private DataSourceConfig dataSource;
        private boolean autoDownloadDriver = true;
        private boolean runMigrations = false;
        private boolean createAll = true;
        private Class<?>[] entities = new Class[0];
        private Class<?> migrationClass = getClass();
        private String migrationPath = "dbmigration";
        private String migrationTable = "db_migration";

        Builder() {
        }

        public Builder entities(Class<?>... entities) {
            classes.addAll(Arrays.asList(entities.clone()));
            this.entities = entities;
            return this;
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

        public Builder migrations(Class<?> rootClass) {
            return migrationClass(rootClass)
                    .runMigrations(true);
        }

        public Config build() {

            if (dataSource == null) {
                dataSource = new DataSourceConfig()
                        .setPlatform(driver.getIdentifier())
                        .setUsername(username)
                        .setPassword(password)
                        .setUrl(url)
                        .setDriver(driver.getDriverClass());
            }

            databaseConfig.setDataSourceConfig(dataSource);
            classes.forEach(databaseConfig::addClass);

            if (runMigrations) {
                try {
                    File tempDir = Files.createTempDir();
                    File migrationDir = new File(tempDir, migrationPath);
                    JarUtil.copyFolderFromJar(migrationClass, migrationPath, tempDir, JarUtil.CopyOption.REPLACE_IF_EXIST);

                    databaseConfig.setRunMigration(true);
                    databaseConfig.getMigrationConfig().setMigrationPath("filesystem:" + new File(migrationDir, driver.getIdentifier()).getAbsolutePath());
                    databaseConfig.getMigrationConfig().setMetaTable(migrationTable);
                } catch (IOException e) {
                    e.printStackTrace();
                    databaseConfig.setRunMigration(false);
                }
            } else if (createAll) {
                databaseConfig.setRunMigration(false);
                databaseConfig.setDdlGenerate(true);
                databaseConfig.setDdlRun(true);
                databaseConfig.getMigrationConfig().setMetaTable(migrationTable);
            }

            return new Config(driver, driverPath, databaseConfig, autoDownloadDriver, runMigrations, createAll, entities, migrationClass, migrationPath, migrationTable);
        }

        private DatabaseConfig defaultDatabaseConfig() {

            DatabaseConfig databaseConfig = new DatabaseConfig();

            databaseConfig.loadFromProperties();
            databaseConfig.setDefaultServer(true);
            databaseConfig.setRegister(true);

            return databaseConfig;
        }
    }
}
