package net.silthus.ebean;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
public class DatabaseConfig {

    public static Builder builder() {

        return new Builder();
    }

    String platform;
    String username ;
    String password;
    String url;
    DriverMapping driver;

    public Builder toBuilder() {

        return new Builder(getPlatform(), getUsername(), getPassword(), getUrl(), getDriver());
    }

    @Setter
    @Accessors(fluent = true)
    @AllArgsConstructor
    public static class Builder {

        private String platform = "h2";
        private String username = "sa";
        private String password = "sa";
        private String url = "jdbc:h2:~/ebean";
        private DriverMapping driver = DriverMapping.DRIVERS.get(platform);

        Builder() {
        }

        public Builder platform(@NonNull String platform) {
            verifyPlatform(platform);
            this.platform = platform;
            this.driver = DriverMapping.DRIVERS.get(platform);
            return this;
        }

        public Builder driver(@NonNull DriverMapping driver) {
            this.driver = driver;
            this.platform = driver.getIdentifier();
            return this;
        }

        public DatabaseConfig build() {

            return new DatabaseConfig(platform, username, password, url, driver);
        }

        private void verifyPlatform(String platform) {
            if (!DriverMapping.DRIVERS.containsKey(platform)) {
                throw new IllegalStateException("Invalid platform " + platform + ". Use one of the following: " + String.join(",", DriverMapping.DRIVERS.keySet()));
            }
        }
    }
}
