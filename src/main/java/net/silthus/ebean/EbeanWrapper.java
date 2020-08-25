package net.silthus.ebean;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.datasource.DataSourceConfig;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

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

    private final ClassLoader classLoader;
    private final io.ebean.config.DatabaseConfig databaseConfig;
    private final DriverMapping driver;

    private Database database;

    /**
     * Creates a new ebean wrapper using the simplified database config and the class loader of this class.
     * <p>
     * You can create the config with the builder from {@link DatabaseConfig#builder()}.
     * <p>
     * Use the alternate {@link #EbeanWrapper(io.ebean.config.DatabaseConfig, DriverMapping, ClassLoader)} constructor to gain full control over the ebean config.
     *
     * @param databaseConfig the database config that should be used
     */
    public EbeanWrapper(DatabaseConfig databaseConfig) {

        this(databaseConfig, EbeanWrapper.class.getClassLoader());
    }

    /**
     * Creates a new ebean wrapper using the simplified database config and the provided class loader.
     * <p>
     * You can create the config with the builder from {@link DatabaseConfig#builder()}.
     * <p>
     * Use the alternate {@link #EbeanWrapper(io.ebean.config.DatabaseConfig, DriverMapping, ClassLoader)} constructor to gain full control over the ebean config.
     *
     * @param config the database config that should be used
     * @param classLoader the class loader that should be used to create the ebean instance
     */
    public EbeanWrapper(@NonNull DatabaseConfig config, ClassLoader classLoader) {

        this.classLoader = classLoader;
        this.driver = config.getDriver();

        databaseConfig = new io.ebean.config.DatabaseConfig();
        // load configuration defaults from application.yml
        databaseConfig.loadFromProperties();
        databaseConfig.setDefaultServer(true);
        databaseConfig.setRegister(true);

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername(config.getUsername());
        dataSourceConfig.setPassword(config.getPassword());
        dataSourceConfig.setUrl(config.getUrl());
        dataSourceConfig.setDriver(driver.getDriverClass());

        databaseConfig.setDataSourceConfig(dataSourceConfig);
    }

    public EbeanWrapper(io.ebean.config.DatabaseConfig config, DriverMapping driver, ClassLoader classLoader) {
        this.databaseConfig = config;
        this.classLoader = classLoader;
        this.driver = driver;
    }

    public File getDriverLocation() {

        return new File("drivers", driver.getIdentifier() + ".jar");
    }

    public boolean driverExists() {

        return getDriverLocation().exists();
    }

    public void downloadDriver(boolean overwrite) throws IOException {

        if (!overwrite && driverExists()) {
            return;
        }

        File driverLocation = getDriverLocation();
        driverLocation.mkdirs();

        try {
            FileUtils.copyURLToFile(new URL(driver.getDownloadUrl()), driverLocation);
            URLClassLoader driverLoader = new URLClassLoader(new URL[]{driverLocation.toURI().toURL()}, classLoader);
            driverLoader.loadClass(driver.getDriverClass());
        } catch (IOException e) {
            throw new IOException("Unable to download " + driver.getIdentifier() + " driver from " + driver.getDownloadUrl() + " to " + driverLocation.getAbsolutePath());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find " + driver.getIdentifier() + " driver class " + driver.getDriverClass() + " inside " + driverLocation.getAbsolutePath(), e);
        }
    }

    public void downloadDriver() throws IOException {

        downloadDriver(false);
    }

    public Database getDatabase() {

        if (database == null) {
            return open();
        }

        return database;
    }

    public Database open() {

        ClassLoader originalContextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);

        database = DatabaseFactory.create(databaseConfig);

        Thread.currentThread().setContextClassLoader(originalContextClassLoader);

        return database;
    }

    @Override
    public void close() {

        database = null;
    }
}
