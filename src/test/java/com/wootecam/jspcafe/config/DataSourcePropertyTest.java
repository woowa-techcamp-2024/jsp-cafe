package com.wootecam.jspcafe.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class DataSourcePropertyTest {

    @Test
    void 테스트_데이터_소스를_불러올_수_있다() {
        // when
        DataSourceProperty dataSourceProperty = new DataSourceProperty("TEST");

        // then
        assertAll(
                () -> assertThat(dataSourceProperty.getDriverClassName()).isEqualTo("org.h2.Driver"),
                () -> assertThat(dataSourceProperty.getUrl()).isEqualTo("jdbc:h2:mem:db"),
                () -> assertThat(dataSourceProperty.getUser()).isEqualTo("sa"),
                () -> assertThat(dataSourceProperty.getPassword()).isEqualTo("")
        );
    }
}
