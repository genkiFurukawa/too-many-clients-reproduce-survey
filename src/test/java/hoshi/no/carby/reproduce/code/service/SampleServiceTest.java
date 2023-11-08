package hoshi.no.carby.reproduce.code.service;

import com.zaxxer.hikari.HikariDataSource;
import hoshi.no.carby.reproduce.code.config.UnitTestConfig;

import org.junit.jupiter.api.*;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DirtiesContext
class SampleServiceTest {
    @Autowired
    HikariDataSource dataSource;

    @Test
    void 明示的にDIコンテナを作成するとDBの接続数が設定値の数だけ増え_PSQLExceptionがスローされること() throws SQLException, InterruptedException {
        try (Connection conn = dataSource.getConnection()) {
            int numbackends = getNumbackends(conn);
            System.out.println("（ループ0回目）コネクション数: " + numbackends);
        }

        assertThrows(PSQLException.class, () -> {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(3_000); // NOTE: なぜか3秒待つとループの2回目から設定値の数だけ増えるようになる
                var applicationContext = new AnnotationConfigApplicationContext(UnitTestConfig.class);
                HikariDataSource ds = applicationContext.getBean("dataSource", HikariDataSource.class);
                try (Connection conn = ds.getConnection()) {
                    int numbackends = getNumbackends(conn);
                    System.out.println("（ループ" + (i + 1) + "回目）コネクション数: " + numbackends);
                }
            }
        });
    }

    @Test
    void 明示的にDIコンテナを作成して明示的に閉じるとDBの接続数が設定値は増えないこと() throws SQLException, InterruptedException {
        try (Connection conn = dataSource.getConnection()) {
            int numbackends = getNumbackends(conn);
            System.out.println("（ループ0回目）コネクション数: " + numbackends);
        }

        for (int i = 0; i < 10; i++) {
            var applicationContext = new AnnotationConfigApplicationContext(UnitTestConfig.class);
            HikariDataSource ds = applicationContext.getBean("dataSource", HikariDataSource.class);
            try (Connection conn = ds.getConnection()) {
                int numbackends = getNumbackends(conn);
                System.out.println("（ループ" + (i + 1) + "回目）コネクション数: " + numbackends);
            }
            applicationContext.close();
        }
    }

    /**
     * pg_stat_databaseのdatnameがpostgresの接続数を取得する
     *
     * @param conn conn
     * @return 接続数
     * @throws SQLException
     */
    private int getNumbackends(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "SELECT numbackends FROM pg_stat_database where datname = 'postgres';";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int numbackends = rs.getInt("numbackends");
            return numbackends;
        }

        return 0;
    }
}