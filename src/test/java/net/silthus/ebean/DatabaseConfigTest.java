package net.silthus.ebean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class DatabaseConfigTest {

    @Nested
    class Builder {

        @Test
        @DisplayName("should throw a runtime exception if the platform driver is invalid")
        void shouldThrowRuntimeExceptionIfDriverIsInvalid() {

            assertThatExceptionOfType(RuntimeException.class)
                    .isThrownBy(() -> DatabaseConfig.builder().platform("foo").build())
                    .withMessageContaining("Invalid platform");
        }

        @Test
        @DisplayName("should set the correct driver for platform mappings")
        void shouldSetTheCorrectDriver() {

            assertThatCode(() -> assertThat(DatabaseConfig.builder().platform("mysql").build())
                    .extracting(DatabaseConfig::getDriver)
                    .isEqualTo(DriverMapping.DRIVERS.get("mysql"))
            ).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should use custom driver mapping")
        void shouldUseCustomDriverMapping() {

            DriverMapping driver = new DriverMapping("foo", "bar", "none");

            assertThatCode(() -> assertThat(DatabaseConfig.builder()
                    .driver(driver).build())
                    .extracting(DatabaseConfig::getDriver, DatabaseConfig::getPlatform)
                    .contains(driver, "foo")
            ).doesNotThrowAnyException();
        }
    }
}