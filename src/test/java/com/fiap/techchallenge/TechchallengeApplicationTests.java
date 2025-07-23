package com.fiap.techchallenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        // diz ao Spring que use apenas esta Config interna para montar o contexto
        classes = TechchallengeApplicationTests.TestConfig.class
)
class TechchallengeApplicationTests {

    @Test
    void contextLoads() {
        // sobe só os beans da app, sem tentar criar DataSource ou JPA
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            DataSourceTransactionManagerAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class
    })
    static class TestConfig {
        // herda tudo de TechchallengeApplication (component scan, @Bean, etc)
        // sem as auto‐configurações de banco
    }
}
