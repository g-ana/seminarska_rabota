package com.example.wp;

import org.junit.Test;
import org.testng.annotations.AfterTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "management.metrics.export.wavefront.enabled=false")
@SpringBootTest
class WpApplicationTests {

    @Test
    void contextLoads() {
    }

}
