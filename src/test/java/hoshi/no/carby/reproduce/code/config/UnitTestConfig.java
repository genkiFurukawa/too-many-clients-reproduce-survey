package hoshi.no.carby.reproduce.code.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class UnitTestConfig {
    @Bean
    DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setAutoCommit(false);
        dataSource.setMaximumPoolSize(25);
        dataSource.setMinimumIdle(25);
        return dataSource;
    }
}
