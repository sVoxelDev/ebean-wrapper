package net.silthus.ebean;

import io.ebean.datasource.DataSourceConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class ConfigTest {

    @Nested
    class Builder {

        @Test
        @DisplayName("should throw a runtime exception if the platform driver is invalid")
        void shouldThrowRuntimeExceptionIfDriverIsInvalid() {

            assertThatExceptionOfType(RuntimeException.class)
                    .isThrownBy(() -> Config.builder().driver("foo").build())
                    .withMessageContaining("Unable to find a valid driver mapping for foo.");
        }

        @Test
        @DisplayName("should set the correct driver for platform mappings")
        void shouldSetTheCorrectDriver() {

            assertThatCode(() -> assertThat(Config.builder().driver("mysql").build())
                    .extracting(Config::getDriver)
                    .isEqualTo(DriverMapping.DRIVERS.get("mysql"))
            ).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should use custom driver mapping")
        void shouldUseCustomDriverMapping() {

            DriverMapping driver = new DriverMapping("foo", "bar", "none");

            assertThatCode(() -> assertThat(Config.builder()
                    .driver(driver).build())
                    .extracting(Config::getDriver, config -> config.getDatabaseConfig().getDataSourceConfig().getPlatform())
                    .contains(driver, "foo")
            ).doesNotThrowAnyException();
        }
    }
}