package net.silthus.ebean;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.annotation.Platform;
import io.ebean.config.ClassLoadConfig;
import io.ebean.config.DbMigrationConfig;
import io.ebean.dbmigration.DbMigration;
import io.ebean.migration.MigrationConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The ebean wrapper takes a simplified database config and creates a new ebean database connection with it.
 * <p>
 * It also provides the {@link #downloadDriver()} method to download the driver jar into the drivers/ directory and load it into the classpath.
 * <p>
 * You can implement your own custom {@link DriverMapping} or use one of the existing default mappings from the {@link DriverMapping#DRIVERS} list.
 * <p>
 * You also have the option to use the alternate constructor that will take the native ebean config as input.
 */
public class EbeanWrapper implements AutoCloseable {

    private final Config config;

    private Database database;

    public EbeanWrapper() {
        this(Config.builder().build());
    }

    /**
     * Creates a new ebean wrapper using the simplified database config and the class loader of this class.
     * <p>
     * You can create the config with the builder from {@link Config#builder()}.
     *
     * @param config the database config that should be used
     */
    public EbeanWrapper(Config config) {

        this.config = config;
    }

    public File getDriverLocation() {

        return new File(config.getDriverPath(), config.getDriver().getIdentifier() + ".jar");
    }

    public boolean driverExists() {

        return getDriverLocation().exists();
    }

    public void downloadDriver(boolean overwrite) {

        File driverLocation = getDriverLocation();
        if (overwrite || !driverExists()) {
            DriverMapping driver = config.getDriver();
            try {
                config.getDriverPath().mkdirs();
                FileUtils.copyURLToFile(new URL(driver.getDownloadUrl()), driverLocation);
            } catch (IOException e) {
                throw new RuntimeException("Unable to download " + driver.getIdentifier() + " driver from " + driver.getDownloadUrl() + " to " + driverLocation.getAbsolutePath());
            }
        }
    }

    public void downloadDriver() {

        downloadDriver(false);
    }

    public Database getDatabase() {

        if (database == null) {
            return connect();
        }

        return database;
    }

    public Database connect() {

        ClassLoader originalContextClassLoader = Thread.currentThread().getContextClassLoader();

        DriverMapping driver = config.getDriver();
        File driverLocation = getDriverLocation();

        if (config.isAutoDownloadDriver()) {
            downloadDriver();
        }

        try {
            ClassLoader classLoader = new URLClassLoader(new URL[]{driverLocation.toURI().toURL()}, getClass().getClassLoader());
            Driver d = (Driver) Class.forName(driver.getDriverClass(), true, classLoader).getDeclaredConstructor().newInstance();
            DriverManager.registerDriver(new DriverShim(d));
            config.getDatabaseConfig().getDataSourceConfig().setDriver(DriverShim.DRIVER_NAME);
            Thread.currentThread().setContextClassLoader(classLoader);
        } catch (Exception e) {
            throw new RuntimeException("Unable to find " + driver.getIdentifier() + " driver class " + driver.getDriverClass() + " inside " + driverLocation.getAbsolutePath(), e);
        }

        database = DatabaseFactory.create(config.getDatabaseConfig());

        Thread.currentThread().setContextClassLoader(originalContextClassLoader);

        return database;
    }

    @Override
    public void close() {

        database.shutdown(true, true);
        this.database = null;
    }
}
