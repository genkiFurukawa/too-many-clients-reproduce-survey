package hoshi.no.carby.reproduce.code.service;

//import hoshi.no.carby.reproduce.code.config.UnitTestAppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@ContextConfiguration(classes = UnitTestAppConfig.class)
@Transactional
@DirtiesContext
class SampleServiceTest {

    @Test
    void サンプル() {

    }
}