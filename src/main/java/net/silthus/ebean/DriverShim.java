package net.silthus.ebean;

import lombok.extern.java.Log;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

// workaround the DriverManagers security refusing to dynamically load drivers from JAR files
// http://www.kfu.com/~nsayer/Java/dyn-jdbc.html
@Log(topic = "ebean-wrapper")
class DriverShim implements Driver {

    public static final String DRIVER_NAME = "net.silthus.ebean.DriverShim";

    private final Driver driver;

    DriverShim(Driver driver) {
        this.driver = driver;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return this.driver.acceptsURL(url);
    }

    @Override
    public Connection connect(String url, Properties properties) throws SQLException {
        return this.driver.connect(url, properties);
    }

    @Override
    public int getMajorVersion() {
        return this.driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return this.driver.getMinorVersion();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties properties) throws SQLException {
        return this.driver.getPropertyInfo(url, properties);
    }

    @Override
    public boolean jdbcCompliant() {
        return this.driver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return log;
    }
}
