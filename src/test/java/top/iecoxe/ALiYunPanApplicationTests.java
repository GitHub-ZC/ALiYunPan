package top.iecoxe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ALiYunPanApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void contextLoads() {
        String result = restTemplate.getForObject("http://www.baidu.com", String.class);
        System.out.println("Get请求:" + result);
    }

}
