package de.allcom;

import de.allcom.dto.NewBetDto;
import de.allcom.services.AuctionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
class AllcomApplicationTests {

    @Autowired
    private AuctionService service;

    @Test
    @Sql(scripts = "/sql/data.sql")
    void contextLoads() throws InterruptedException {
        service.scheduleMyTask();
        Thread.sleep(30000);
    }

}
