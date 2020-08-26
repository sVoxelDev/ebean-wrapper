package net.silthus.ebean;

import io.ebean.Database;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class EbeanWrapperTest {

    @Test
    @DisplayName("should auto-download and use driver")
    void shouldDownloadAndUseDriver() throws IOException {

        EbeanWrapper wrapper = new EbeanWrapper(Config.builder()
                .driver("h2")
                .autoDownloadDriver(true)
                .build());

        FileUtils.deleteDirectory(new File("drivers"));
        assertThat(wrapper.getDriverLocation()).doesNotExist();

        assertThatCode(wrapper::connect)
                .doesNotThrowAnyException();

        assertThat(wrapper.getDriverLocation()).exists();

        wrapper.close();
    }

    @Test
    @DisplayName("should fail if driver does not exist")
    void shouldFailIfDriverDoesNotExist() {

        EbeanWrapper wrapper = new EbeanWrapper(Config.builder()
                .driver("mysql")
                .autoDownloadDriver(false)
                .build());

        assertThat(wrapper.getDriverLocation()).doesNotExist();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(wrapper::connect);
    }

    @Test
    @DisplayName("should store bean into database")
    void shouldStoreTestBeanData() {

        EbeanWrapper wrapper = new EbeanWrapper(Config.builder()
                .autoDownloadDriver(true)
                .build());

        Database database = wrapper.getDatabase();

        TestBean bean = new TestBean();
        bean.setId(11);
        bean.setName("foo");

        assertThatCode(() -> database.save(bean))
                .doesNotThrowAnyException();

        assertThat(database.find(TestBean.class).findList())
                .hasSize(1)
                .extracting(TestBean::getId, TestBean::getName)
                .contains(tuple(11, "foo"));
    }

    @Entity
    @Data
    public static class TestBean {

        @Id
        private int id;
        private String name;
    }
}