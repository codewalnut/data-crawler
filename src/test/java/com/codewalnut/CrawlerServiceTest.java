package com.codewalnut;

import com.codewalnut.service.CrawlerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Weway-RG001 on 2018-02-07 17:42.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerServiceTest {
    @Autowired
    private CrawlerService crawlerService;

    @Test
    public void testFetchAddressInfo() {
        String s = null;
        for (int i = 0; i < 10; i++) {
            s = crawlerService.fetchAddressInfo("14pGtYkccAU9qXStLNtukzp6JQiSNFZZS7");
        }
        System.out.println(s);
    }

}
